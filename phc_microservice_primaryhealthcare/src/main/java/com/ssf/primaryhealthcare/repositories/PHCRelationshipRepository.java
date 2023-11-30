package com.ssf.primaryhealthcare.repositories;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.ssf.primaryhealthcare.dtos.PHCWithRelationships;
import com.ssf.primaryhealthcare.dtos.RelationshipDTO;
import com.ssf.primaryhealthcare.dtos.RelationshipPatchDTO;
import com.ssf.primaryhealthcare.dtos.properties.ComplaintsOfDTO;
import com.ssf.primaryhealthcare.dtos.properties.DiagnosedWithDTO;
import com.ssf.primaryhealthcare.dtos.properties.VaccinatedAgainstDTO;
import com.ssf.primaryhealthcare.exceptions.CreateRelationshipError;
import com.ssf.primaryhealthcare.exceptions.GetError;
import com.ssf.primaryhealthcare.model.NodeType;
import com.ssf.primaryhealthcare.model.PHCNode;
import com.ssf.primaryhealthcare.model.PHCRelationship;
import com.ssf.primaryhealthcare.model.RelationshipType;
import com.ssf.primaryhealthcare.utils.Utils;
import lombok.SneakyThrows;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PHCRelationshipRepository {

    @Autowired
    PHCRepository PHCRepository;
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    @Autowired
    private Driver driver;

    public PHCRelationshipRepository(Driver driver) {
        this.driver = driver;
    }

    private PHCRelationship getValues(Relationship relation) {
        PHCRelationship relationship = new PHCRelationship();
        relationship.setId(relation.id());
        relationship.setType(RelationshipType.valueOf(relation.type()));
        relationship.setProperties(relation.asMap());
        return relationship;
    }

    private PHCNode getValues(Node node) {
        PHCNode PHCNode = new PHCNode();
        PHCNode.setId(node.id());
        PHCNode.setLabels((List<String>) node.labels());
        PHCNode.setProperties(node.asMap());

        return PHCNode;
    }

    @SneakyThrows
    private List<PHCWithRelationships> getRelationshipBetweenNodes(RelationshipType relationship, String sourceId, String targetId) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            String query = "MATCH (s)-[r:" + relationship.toString() + "]-(t) WHERE s.uuid = '" + sourceId + "' and t.uuid = '" + targetId + "' RETURN s,r,t";
            List<PHCWithRelationships> nodeWithPHCAndRelationships = new ArrayList<>();

            session.run(query).forEachRemaining(result -> {
                PHCWithRelationships node = new PHCWithRelationships();
                result.asMap().forEach((k, v) -> {
                    PHCNode sourceNode, targetNode;
                    PHCRelationship nodeRelationship;
                    if (k.equals("r")) {
                        nodeRelationship = getValues((Relationship) v);
                        nodeRelationship.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(nodeRelationship.getProperties(), Map.class)), nodeRelationship.getType().toString()), Map.class));
                        node.setRelationship(nodeRelationship);
                    }
                    if (k.equals("t")) {
                        targetNode = getValues((Node) v);
                        targetNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(targetNode.getProperties(), Map.class)), targetNode.getLabels().get(0)), Map.class));
                        node.setTargetNode(targetNode);
                    }
                    if (k.equals("s")) {
                        sourceNode = getValues((Node) v);
                        sourceNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(sourceNode.getProperties(), Map.class)), sourceNode.getLabels().get(0)), Map.class));
                        node.setSourceNode(sourceNode);
                    }
                });
                nodeWithPHCAndRelationships.add(node);
            });
            return nodeWithPHCAndRelationships;
        } catch (Exception e) {
            throw new GetError(e.getMessage());
        }
    }

    @SneakyThrows
    public List createRelationshipBetweenNodes(RelationshipDTO relationshipDTO) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();

            if (!isValidRelationship(relationshipDTO.getType(), relationshipDTO.getSource().getType(), relationshipDTO.getTarget().getType())) {
                throw new CreateRelationshipError("Not a valid relationship b/w passed node");
            }

            if (relationshipDTO.getSource().getProperties().containsKey("uuid") && relationshipDTO.getTarget().getProperties().containsKey("uuid")) {
                List<PHCNode> sourceList = PHCRepository.getPHCNode(String.valueOf(relationshipDTO.getSource().getProperties().get("uuid")));
                if (sourceList == null || sourceList.size() == 0) {
                    throw new CreateRelationshipError("PHCNodeDTO with passed source uuid not exist.");
                }
                List<PHCNode> targetList = PHCRepository.getPHCNode(String.valueOf(relationshipDTO.getTarget().getProperties().get("uuid")));
                if (targetList == null || targetList.size() == 0) {
                    throw new CreateRelationshipError("PHCNodeDTO with passed target uuid not exist.");
                }
            } else {
                throw new CreateRelationshipError("Missing key : 'uuid' in source or target properties");
            }


            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s:" + relationshipDTO.getSource().getType() + ") ");
            queryBuilder.append(", ");
            queryBuilder.append("(t:" + relationshipDTO.getTarget().getType() + ") ");
            queryBuilder.append("WHERE ");
            queryBuilder.append("s.uuid = '" + relationshipDTO.getSource().getProperties().get("uuid") + "' ");
            queryBuilder.append("AND ");
            queryBuilder.append("t.uuid = '" + relationshipDTO.getTarget().getProperties().get("uuid") + "' ");

            List<PHCWithRelationships> relationshipBetweenNodes = getRelationshipBetweenNodes(relationshipDTO.getType(), String.valueOf(relationshipDTO.getSource().getProperties().get("uuid")),
                    String.valueOf(relationshipDTO.getTarget().getProperties().get("uuid")));
            if (relationshipBetweenNodes.size() > 0) {
                return relationshipBetweenNodes;
            }

            queryBuilder.append("CREATE ");
            queryBuilder.append("(s)-");
            queryBuilder.append("[r:" + relationshipDTO.getType() + " ");
            queryBuilder.append(addPropertiesToRelationship(relationshipDTO.getProperties(), relationshipDTO.getType(), relationshipDTO.getSource().getType(), relationshipDTO.getTarget().getType()));
            queryBuilder.append(" ]->");
            queryBuilder.append("(t) ");
            queryBuilder.append("RETURN s, t ,r");

            List<PHCWithRelationships> nodeWithPHCAndRelationships = new ArrayList<>();
            String query = queryBuilder.toString();
            session.run(query).forEachRemaining(result -> {
                PHCWithRelationships node = new PHCWithRelationships();
                result.asMap().forEach((k, v) -> {
                    PHCNode sourceNode, targetNode;
                    PHCRelationship nodeRelationship;
                    if (k.equals("r")) {
                        nodeRelationship = getValues((Relationship) v);
                        nodeRelationship.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(nodeRelationship.getProperties(), Map.class)), nodeRelationship.getType().toString()), Map.class));
                        node.setRelationship(nodeRelationship);
                    }
                    if (k.equals("t")) {
                        targetNode = getValues((Node) v);
                        targetNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(targetNode.getProperties(), Map.class)), targetNode.getLabels().get(0)), Map.class));
                        node.setTargetNode(targetNode);
                    }
                    if (k.equals("s")) {
                        sourceNode = getValues((Node) v);
                        sourceNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(sourceNode.getProperties(), Map.class)), sourceNode.getLabels().get(0)), Map.class));
                        node.setSourceNode(sourceNode);
                    }
                });
                nodeWithPHCAndRelationships.add(node);
            });
            return nodeWithPHCAndRelationships;

        } catch (Exception e) {
            throw new CreateRelationshipError(e.getMessage());
        }
    }

    private Boolean isValidRelationship(RelationshipType relationship, NodeType sourceType, NodeType targetType) {

        if (relationship == RelationshipType.VACCINATEDAGAINST && sourceType == NodeType.Citizen && targetType == NodeType.Disease) {
            return true;
        }

        if (relationship == RelationshipType.DIAGNOSEDWITH && sourceType == NodeType.Citizen && targetType == NodeType.Disease) {
            return true;
        }

        if (relationship == RelationshipType.COMPLAINTSOF && sourceType == NodeType.Citizen && targetType == NodeType.Symptom) {
            return true;
        }

        return false;
    }

    private String addPropertiesToRelationship(Map<String, Object> properties, RelationshipType relationship, NodeType sourceType, NodeType targetType) {

        if (relationship == RelationshipType.VACCINATEDAGAINST && sourceType == NodeType.Citizen && targetType == NodeType.Disease) {
            return Utils.mapToString(properties, VaccinatedAgainstDTO.class);
        }
        if (relationship == RelationshipType.DIAGNOSEDWITH && sourceType == NodeType.Citizen && targetType == NodeType.Disease) {
            return Utils.mapToString(properties, DiagnosedWithDTO.class);
        }
        if (relationship == RelationshipType.COMPLAINTSOF && sourceType == NodeType.Citizen && targetType == NodeType.Symptom) {
            return Utils.mapToString(properties, ComplaintsOfDTO.class);
        }
        return "";
    }

    @SneakyThrows
    public List<PHCWithRelationships> updatedRelationshipProperties(String relationshipId, RelationshipPatchDTO patchDTO) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s)-");
            queryBuilder.append("[r:" + patchDTO.getType() + "]->");
            queryBuilder.append("(t) ");
            queryBuilder.append("WHERE ");
            queryBuilder.append("r.uuid = '" + relationshipId + "' ");
            queryBuilder.append("SET r += ");
            queryBuilder.append(addPropertiesToRelationship(patchDTO.getType(), patchDTO.getProperties()) + " ");
            queryBuilder.append("RETURN s,r,t");

            List<PHCWithRelationships> nodeWithPHCAndRelationships = new ArrayList<>();

            session.run(queryBuilder.toString()).forEachRemaining(result -> {
                PHCWithRelationships node = new PHCWithRelationships();
                result.asMap().forEach((k, v) -> {
                    PHCNode sourceNode, targetNode;
                    PHCRelationship nodeRelationship;
                    if (k.equals("r")) {
                        nodeRelationship = getValues((Relationship) v);
                        nodeRelationship.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(nodeRelationship.getProperties(), Map.class)), nodeRelationship.getType().toString()), Map.class));
                        node.setRelationship(nodeRelationship);
                    }
                    if (k.equals("t")) {
                        targetNode = getValues((Node) v);
                        targetNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(targetNode.getProperties(), Map.class)), targetNode.getLabels().get(0)), Map.class));
                        node.setTargetNode(targetNode);
                    }
                    if (k.equals("s")) {
                        sourceNode = getValues((Node) v);
                        sourceNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(sourceNode.getProperties(), Map.class)), sourceNode.getLabels().get(0)), Map.class));
                        node.setSourceNode(sourceNode);
                    }
                });
                nodeWithPHCAndRelationships.add(node);
            });
            return nodeWithPHCAndRelationships;
        } catch (Exception e) {
            throw new GetError(e.getMessage());
        }
    }

    @SneakyThrows
    private String addPropertiesToRelationship(RelationshipType type, Map<String, Object> properties) {
        try {
            switch (type) {
                case COMPLAINTSOF:
                    return Utils.mapToString(properties, ComplaintsOfDTO.class);
                case DIAGNOSEDWITH:
                    return Utils.mapToString(properties, DiagnosedWithDTO.class);
                case VACCINATEDAGAINST:
                    return Utils.mapToString(properties, VaccinatedAgainstDTO.class);
                default:
                    return "";
            }

        } catch (Exception e) {
            throw new CreateRelationshipError(e.getMessage());
        }
    }

}

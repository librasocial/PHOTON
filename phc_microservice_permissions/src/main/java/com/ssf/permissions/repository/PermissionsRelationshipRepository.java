package com.ssf.permissions.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.ssf.permissions.dtos.*;
import com.ssf.permissions.dtos.properties.*;
import com.ssf.permissions.exceptions.CreateRelationshipError;
import com.ssf.permissions.model.*;
import com.ssf.permissions.utils.PermissionConstants;
import com.ssf.permissions.utils.Utils;
import lombok.SneakyThrows;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class PermissionsRelationshipRepository {

    private final Driver driver;

    @Autowired
    private HttpServletRequest request;

    ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

    public PermissionsRelationshipRepository(Driver driver) {
        this.driver = driver;
    }

    private PermissionRelationship getValues(Relationship relation) {
        PermissionRelationship relationship = new PermissionRelationship();
        relationship.setId(relation.id());
        relationship.setType(RelationshipType.valueOf(relation.type()));
        relationship.setProperties(relation.asMap());
        return relationship;
    }

    private PermissionNode getValues(Node node) {
        PermissionNode permissionNode = new PermissionNode();
        permissionNode.setId(node.id());
        permissionNode.setLabels((List<String>) node.labels());
        permissionNode.setProperties(node.asMap());
        return permissionNode;
    }

    @SneakyThrows
    private List<PermissionsWithRelationships> getRelationshipBetweenNodes(RelationshipType relationship, String sourceId, String targetId) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            String query = "MATCH (s)-[r:" + relationship.toString() + "]-(t) WHERE s.uuid = '" + sourceId + "' and t.uuid = '" + targetId + "' RETURN s,r,t";
            List<PermissionsWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();

            session.run(query).forEachRemaining(result -> {
                PermissionsWithRelationships node = new PermissionsWithRelationships();
                result.asMap().forEach((k, v) -> {
                    PermissionNode sourceNode, targetNode;
                    PermissionRelationship nodeRelationship;
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
                nodeWithMembersAndRelationships.add(node);
            });
            return nodeWithMembersAndRelationships;
        } catch (Exception e) {
            throw new CreateRelationshipError(e.getMessage());
        }
    }

    @SneakyThrows
    public List createRelationshipBetweenNodes(RelationshipDTO relationshipDTO) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();

            if (!isValidRelationship(relationshipDTO.getRelationship().getType(), relationshipDTO.getSource().getType(), relationshipDTO.getTarget().getType())) {
                throw new CreateRelationshipError("Not a valid relationship b/w passed node");
            }

            if (relationshipDTO.getSource().getProperties().containsKey("uuid") && relationshipDTO.getTarget().getProperties().containsKey("uuid")) {
                List<PermissionNode> sourceList = getNode(String.valueOf(relationshipDTO.getSource().getProperties().get("uuid")), relationshipDTO.getSource().getType());
                if (sourceList == null || sourceList.size() == 0) {
                    throw new CreateRelationshipError("Node with passed source uuid not exist.");
                }
                List<PermissionNode> targetList = getNode(String.valueOf(relationshipDTO.getTarget().getProperties().get("uuid")), relationshipDTO.getTarget().getType());
                if (targetList == null || targetList.size() == 0) {
                    throw new CreateRelationshipError("Node with passed target uuid not exist.");
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

            List<PermissionsWithRelationships> relationshipBetweenNodes = getRelationshipBetweenNodes(relationshipDTO.getRelationship().getType(), String.valueOf(relationshipDTO.getSource().getProperties().get("uuid")),
                    String.valueOf(relationshipDTO.getTarget().getProperties().get("uuid")));
            if (relationshipBetweenNodes.size() > 0) {
                return relationshipBetweenNodes;
            }

            queryBuilder.append("CREATE ");
            queryBuilder.append("(s)-");
            queryBuilder.append("[r:" + relationshipDTO.getRelationship().getType() + " ");
            queryBuilder.append(addPropertiesToRelationship(relationshipDTO.getRelationship().getProperties(), relationshipDTO.getRelationship().getType()));
            queryBuilder.append(" ]->");
            queryBuilder.append("(t) ");
            queryBuilder.append("RETURN s, t ,r");

            List<PermissionsWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            String query = queryBuilder.toString();
            session.run(query).forEachRemaining(result -> {
                PermissionsWithRelationships node = new PermissionsWithRelationships();
                result.asMap().forEach((k, v) -> {
                    PermissionNode sourceNode, targetNode;
                    PermissionRelationship nodeRelationship;
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
                nodeWithMembersAndRelationships.add(node);
            });
            return nodeWithMembersAndRelationships;

        } catch (Exception e) {
            throw new CreateRelationshipError(e.getMessage());
        }
    }

    private Boolean isValidRelationship(RelationshipType relationship, NodeType sourceType, NodeType targetType) {
        // TODO Write the valid relationship
        return true;
    }

    private String addPropertiesToRelationship(Map<String, Object> properties, RelationshipType relationship) {
        properties.remove("uuid");
        properties = updateAudits(properties);
        switch (relationship) {
            case HASACCESS:
                return Utils.mapToString(properties, HasAccessDTO.class);
            case HASROLE:
                return Utils.mapToString(properties, HasRoleDTO.class);
            case CAPABILITYOF:
                return Utils.mapToString(properties, CapabilityOfDTO.class);
            case ISPARTOF:
                return Utils.mapToString(properties, IsPartOfDTO.class);
            case OWNEROF:
                return Utils.mapToString(properties, OwnerOfDTO.class);
            default:
                return "";
        }
    }


    @SneakyThrows
    public List<PermissionsWithRelationships> updatedRelationshipProperties(String relationshipId, PermissionPatchDTO patchDTO) {
        try {
            Map<String, Object> properties = patchDTO.getProperties();
            properties.put("uuid", relationshipId);
            Gson gson = new Gson();
            Session session = driver.session();
            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s)-");
            queryBuilder.append("[r:" + patchDTO.getType() + "]->");
            queryBuilder.append("(t) ");
            queryBuilder.append("WHERE ");
            queryBuilder.append("r.uuid = '" + relationshipId + "' ");
            queryBuilder.append("SET r += ");
            queryBuilder.append(addPropertiesToRelationship(patchDTO.getType(), properties) + " ");
            queryBuilder.append("RETURN s,r,t");

            List<PermissionsWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();

            session.run(queryBuilder.toString()).forEachRemaining(result -> {
                PermissionsWithRelationships node = new PermissionsWithRelationships();
                result.asMap().forEach((k, v) -> {
                    PermissionNode sourceNode, targetNode;
                    PermissionRelationship nodeRelationship;
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
                nodeWithMembersAndRelationships.add(node);
            });
            return nodeWithMembersAndRelationships;
        } catch (Exception e) {
            throw new CreateRelationshipError(e.getMessage());
        }
    }

    @SneakyThrows
    private String addPropertiesToRelationship(RelationshipType type, Map<String, Object> properties) {
        try {
            properties = updateAudits(properties);
            switch (type) {
                case HASACCESS:
                    return Utils.mapToString(properties, HasAccessDTO.class);
                case HASROLE:
                    return Utils.mapToString(properties, HasRoleDTO.class);
                case CAPABILITYOF:
                    return Utils.mapToString(properties, CapabilityOfDTO.class);
                case ISPARTOF:
                    return Utils.mapToString(properties, IsPartOfDTO.class);
                case OWNEROF:
                    return Utils.mapToString(properties, OwnerOfDTO.class);
                default:
                    return "";
            }

        } catch (Exception e) {
            throw new CreateRelationshipError(e.getMessage());
        }
    }

    public Boolean assertPermission(PermissionAssetFilter assetFilter) {
        Session session = driver.session();

        StringBuilder queryBuilder = new StringBuilder("MATCH ");
        queryBuilder.append("(s)-");
        queryBuilder.append("[r:HASROLE|HASACCESS]->");
        queryBuilder.append("(t) ");
        queryBuilder.append("WHERE s.uuid = '" + assetFilter.getMemberId() + "' ");
        queryBuilder.append("AND ");
        if (assetFilter.getApplicationName() != null) {
            queryBuilder.append(" t.name = '" + assetFilter.getApplicationName() + "' ");
        } else if (assetFilter.getModuleName() != null) {
            queryBuilder.append(" t.name = '" + assetFilter.getModuleName() + "' ");
        } else if (assetFilter.getResourcePath() != null && assetFilter.getResourceMethod() != null) {
            queryBuilder.append(" t.path = '" + assetFilter.getResourcePath() + "' AND t.method = '" + assetFilter.getResourceMethod() + "' ");
        } else {
            throw new NoSuchElementException("");
        }
        queryBuilder.append(" RETURN count(r) as count");

        if (((Long) session.run(queryBuilder.toString()).list().get(0).asMap().get("count")) > 0L) {
            return true;
        } else {
            return false;
        }
    }

    @SneakyThrows
    public List<PermissionNode> getNode(String id, NodeType type) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            String query = "MATCH (n:" + type + ") WHERE n.uuid = '" + id + "' RETURN n ";
            List<PermissionNode> permissionNodes = new ArrayList<>();
            session.run(query).forEachRemaining(r -> {
                PermissionNode permissionNode = getValues(r.get("n").asNode());
                permissionNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(permissionNode.getProperties(), Map.class)), permissionNode.getLabels().get(0)), Map.class));
                permissionNodes.add(permissionNode);
            });
            return permissionNodes;
        } catch (Exception e) {
            throw new CreateRelationshipError(e);
        }
    }

    public PermissionPageResponse getMemberAndRelationships(PermissionFilterDTO filter, Integer page, Integer size) {
        if (filter.getSourceId() != null) {
            if (filter.getRelationshipStepCount() > 1) {
                Long totalElements = getCountOfPermissionNodesWithStepCountOfRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), filter.getSourceId(), null, filter.getRelationshipStepCount());
                List result = getPermissionNodesWithStepCountOfRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), filter.getSourceId(), null, filter.getRelationshipStepCount(), page, size);
                PermissionPageResponse response = PermissionPageResponse.builder().totalElements(totalElements).content(result)
                        .totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).build();
                return response;
            } else {
                Long totalElements = getCountOfPermissionNodesWithRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), filter.getSourceId(), null);
                List result = getPermissionNodeWithRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), filter.getSourceId(), null, page, size);
                PermissionPageResponse response = PermissionPageResponse.builder().totalElements(totalElements).content(result)
                        .totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).build();
                return response;
            }
        } else if (filter.getTargetId() != null) {
            if (filter.getRelationshipStepCount() > 1) {
                Long totalElements = getCountOfPermissionNodesWithStepCountOfRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), null, filter.getTargetId(), filter.getRelationshipStepCount());
                List result = getPermissionNodesWithStepCountOfRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), null, filter.getTargetId(), filter.getRelationshipStepCount(), page, size);
                PermissionPageResponse response = PermissionPageResponse.builder().totalElements(totalElements).content(result)
                        .totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).build();
                return response;
            } else {
                Long totalElements = getCountOfPermissionNodesWithRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), null, filter.getTargetId());
                List result = getPermissionNodeWithRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), null, filter.getTargetId(), page, size);
                PermissionPageResponse response = PermissionPageResponse.builder().totalElements(totalElements).content(result)
                        .totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).build();
                return response;
            }
        } else {
            List result = getPermissionNodesByRelationship(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), page, size);
            Long totalElements = getCountOfPermissionNodesWithRelationship(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType());
            PermissionPageResponse response = PermissionPageResponse.builder().totalElements(totalElements).content(result)
                    .totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).build();
            return response;
        }
    }

    @SneakyThrows
    private List getPermissionNodesWithStepCountOfRelationshipBySourceIdOrTargetId(RelationshipType relationshipType, NodeType targetType, NodeType sourceType, String sourceId, String targetId, Integer stepCount, Integer page, Integer size) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            List<PermissionsWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            String query = "";
            if (sourceId != null) {
                query = "MATCH (s:" + sourceType + ")-[*" + stepCount + "]-(t:" + targetType + ") where s.uuid = '" + sourceId + "' RETURN s, t skip " + (page * size) + " limit " + size;
            } else if (targetId != null) {
                query = "MATCH (s:" + sourceType + ")-[*" + stepCount + "]-(t:" + targetType + ") where t.uuid = '" + targetId + "' RETURN s, t skip " + (page * size) + " limit " + size;
            }
            session.run(query).forEachRemaining(result -> {
                PermissionsWithRelationships node = new PermissionsWithRelationships();
                result.asMap().forEach((k, v) -> {
                    PermissionNode sourceNode, targetNode;
                    PermissionRelationship nodeRelationship = new PermissionRelationship();
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
                    nodeRelationship.setType(relationshipType);
                    node.setRelationship(nodeRelationship);

                });
                nodeWithMembersAndRelationships.add(node);
            });
            return nodeWithMembersAndRelationships;
        } catch (Exception e) {
            throw new CreateRelationshipError(e);
        }
    }

    @SneakyThrows
    public Long getCountOfPermissionNodesWithRelationship(RelationshipType relationshipType, NodeType targetType, NodeType sourceType) {
        try {
            Session session = driver.session();
            String query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ")  RETURN count(s) as count";
            return (Long) session.run(query).list().get(0).asMap().get("count");
        } catch (Exception e) {
            throw new CreateRelationshipError(e);
        }
    }

    @SneakyThrows
    public Long getCountOfPermissionNodesWithRelationshipBySourceIdOrTargetId(RelationshipType relationshipType, NodeType targetType, NodeType sourceType, String sourceId, String targetId) {
        try {
            Session session = driver.session();
            String query = "";
            if (sourceId != null) {
                query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ") where s.uuid = '" + sourceId + "' RETURN count(s) as count";

            } else if (targetId != null) {
                query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ") where t.uuid = '" + targetId + "' RETURN count(s) as count";
            }
            return (Long) session.run(query).list().get(0).asMap().get("count");
        } catch (Exception e) {
            throw new CreateRelationshipError(e);
        }
    }

    @SneakyThrows
    private Long getCountOfPermissionNodesWithStepCountOfRelationshipBySourceIdOrTargetId(RelationshipType relationshipType, NodeType targetType, NodeType sourceType, String sourceId, String targetId, Integer stepCount) {
        try {
            Session session = driver.session();
            String query = "";
            if (sourceId != null) {
                query = "MATCH (s:" + sourceType + ")-[*" + stepCount + "]-(t:" + targetType + ") where s.uuid = '" + sourceId + "' RETURN count(s) as count";
            } else if (targetId != null) {
                query = "MATCH (s:" + sourceType + ")-[*" + stepCount + "]-(t:" + targetType + ") where t.uuid = '" + targetId + "' RETURN count(s) as count";
            }
            return (Long) session.run(query).list().get(0).asMap().get("count");

        } catch (Exception e) {
            throw new CreateRelationshipError(e);
        }
    }

    @SneakyThrows
    public List getPermissionNodesByRelationship(RelationshipType relationshipType, NodeType targetType, NodeType sourceType, Integer page, Integer size) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            List<PermissionsWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            String query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ")  RETURN s, r, t skip " + (page * size) + " limit " + size;
            session.run(query).forEachRemaining(result -> {
                PermissionsWithRelationships node = new PermissionsWithRelationships();
                result.asMap().forEach((k, v) -> {
                    PermissionNode sourceNode, targetNode;
                    PermissionRelationship nodeRelationship;
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
                nodeWithMembersAndRelationships.add(node);
            });
            return nodeWithMembersAndRelationships;
        } catch (Exception e) {
            throw new CreateRelationshipError(e);
        }

    }

    @SneakyThrows
    public List getPermissionNodeWithRelationshipBySourceIdOrTargetId(RelationshipType relationshipType, NodeType targetType, NodeType sourceType, String sourceId, String targetId, Integer page, Integer size) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            List<PermissionsWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            String query = "";
            if (sourceId != null) {
                query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ") where s.uuid= '" + sourceId + "' RETURN s, r, t skip " + (page * size) + " limit " + size;
            } else if (targetId != null) {
                query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ") where t.uuid= '" + targetId + "' RETURN s, r, t skip " + (page * size) + " limit " + size;
            }
            session.run(query).forEachRemaining(result -> {
                PermissionsWithRelationships node = new PermissionsWithRelationships();
                result.asMap().forEach((k, v) -> {
                    PermissionNode sourceNode, targetNode;
                    PermissionRelationship nodeRelationship;
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
                nodeWithMembersAndRelationships.add(node);
            });
            return nodeWithMembersAndRelationships;
        } catch (Exception e) {
            throw new CreateRelationshipError(e);
        }
    }

    private Map<String, Object> updateAudits(Map<String, Object> properties) {
        String username = Utils.getUserFromIdToken(this.request.getHeader(PermissionConstants.ID_TOKEN));
        if (properties.containsKey("uuid")) {
            properties.put("modifiedBy", username);
            properties.put("dateModified", LocalDateTime.now().format(DateTimeFormatter.ofPattern(PermissionConstants.ISO_DATE_TIME)));
        } else {
            properties.put("uuid", UUID.randomUUID().toString());
            properties.put("createdBy", username);
            properties.put("dateCreated", LocalDateTime.now().format(DateTimeFormatter.ofPattern(PermissionConstants.ISO_DATE_TIME)));
            properties.put("modifiedBy", username);
            properties.put("dateModified", LocalDateTime.now().format(DateTimeFormatter.ofPattern(PermissionConstants.ISO_DATE_TIME)));
        }
        return properties;
    }

    @SneakyThrows
    public PermissionCountContentResponse getMemberRelationshipByFilters(RelationshipFilterDTO filterInput) {
        try {

            Session session = driver.session();
            Gson gson = new Gson();
            List<PermissionsWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s:" + filterInput.getSourceType() + ") ");
            if (filterInput.getStepCount() > 1) {
                queryBuilder.append("-[*" + filterInput.getStepCount() + "]- ");
            } else {
                queryBuilder.append("-[r:" + filterInput.getRelationshipType() + "]- ");
            }
            queryBuilder.append("(t:" + filterInput.getTargetType() + ") ");
            queryBuilder.append(sourceTargetPropertiesToWhereClause(filterInput));
            String countQuery = queryBuilder + " RETURN count(s) as count";
            if (filterInput.getStepCount() > 1) {
                queryBuilder.append(" RETURN s, t ");
            } else {
                queryBuilder.append(" RETURN  r,s , t ");
            }

            queryBuilder.append("SKIP " + (filterInput.getPage() * filterInput.getSize()) + " LIMIT " + filterInput.getSize());

            session.run(queryBuilder.toString()).forEachRemaining(result -> {
                PermissionsWithRelationships node = new PermissionsWithRelationships();
                result.asMap().forEach((k, v) -> {
                    PermissionNode sourceNode, targetNode;
                    PermissionRelationship nodeRelationship = null;
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
                    if (node.getRelationship() == null) {
                        nodeRelationship = new PermissionRelationship();
                        nodeRelationship.setProperties(new HashMap<>());
                        nodeRelationship.setType(filterInput.getRelationshipType());
                        node.setRelationship(nodeRelationship);
                    }
                });
                nodeWithMembersAndRelationships.add(node);
            });
            return PermissionCountContentResponse.builder().content(nodeWithMembersAndRelationships).count((Long) session.run(countQuery).list().get(0).asMap().get("count")).build();
        } catch (Exception e) {
            throw new CreateRelationshipError(e.getMessage());
        }
    }

    private String sourceTargetPropertiesToWhereClause(RelationshipFilterDTO filterDTO) {

        StringBuilder whereClause = new StringBuilder("WHERE ");
        filterDTO.getSourceProperties().forEach((k, v) -> {
            if (v.getClass() == String.class) {
                whereClause.append("AND s." + k + " = '" + v + "' ");
            } else {
                whereClause.append("AND s." + k + " = " + v + " ");
            }

        });

        filterDTO.getRelationshipProperties().forEach((k, v) -> {
            if (v.getClass() == String.class) {
                whereClause.append("AND r." + k + " = '" + v + "' ");
            } else {
                whereClause.append("AND r." + k + " = " + v + " ");
            }

        });

        filterDTO.getTargetProperties().forEach((k, v) -> {
            if (v.getClass() == String.class) {
                whereClause.append("AND t." + k + " = '" + v + "' ");
            } else {
                whereClause.append("AND t." + k + " = " + v + " ");
            }
        });

        filterDTO.getSrcInClause().forEach((k, v) -> {
            whereClause.append("AND s." + k + " IN [");
            ((List<Object>) v).forEach((values) -> {
                if (values.getClass() == String.class) {
                    whereClause.append("'" + values + "',");
                } else {
                    whereClause.append("" + values + ",");
                }
            });
            whereClause.append("]");
        });


        filterDTO.getTargetInClause().forEach((k, v) -> {
            whereClause.append("AND t." + k + " IN [");
            ((List<Object>) v).forEach((values) -> {
                if (values.getClass() == String.class) {
                    whereClause.append("'" + values + "',");
                } else {
                    whereClause.append("" + values + ",");
                }
            });
            whereClause.append("]");
        });


        filterDTO.getRelInClause().forEach((k, v) -> {
            whereClause.append("AND r." + k + " IN [");
            ((List<Object>) v).forEach((values) -> {
                if (values.getClass() == String.class) {
                    whereClause.append("'" + values + "',");
                } else {
                    whereClause.append("" + values + ",");
                }
            });
            whereClause.append("]");
        });

        return whereClause.toString().replace("WHERE AND", "WHERE ").replace(",]", "]");
    }

}

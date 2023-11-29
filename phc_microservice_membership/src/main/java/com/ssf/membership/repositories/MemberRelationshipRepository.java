package com.ssf.membership.repositories;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.ssf.membership.dtos.*;
import com.ssf.membership.dtos.properties.MarriedToDto;
import com.ssf.membership.dtos.properties.MotherOfDTO;
import com.ssf.membership.entities.Expression;
import com.ssf.membership.exceptions.CreateRelationshipError;
import com.ssf.membership.exceptions.GetMembersError;
import com.ssf.membership.model.MemberNode;
import com.ssf.membership.model.MemberRelationship;
import com.ssf.membership.model.MemberType;
import com.ssf.membership.model.RelationshipType;
import com.ssf.membership.utils.Utils;
import lombok.SneakyThrows;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MemberRelationshipRepository {

    @Autowired
    MemberRepository memberRepository;
    ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
    @Autowired
    private Driver driver;

    public MemberRelationshipRepository(Driver driver) {
        this.driver = driver;
    }

    public MemberRelationship getValues(Relationship relation) {
        MemberRelationship relationship = new MemberRelationship();
        relationship.setId(relation.id());
        relationship.setType(RelationshipType.valueOf(relation.type()));
        relationship.setProperties(relation.asMap());
        return relationship;
    }

    public MemberNode getValues(Node node) {
        MemberNode memberNode = new MemberNode();
        memberNode.setId(node.id());
        memberNode.setLabels((List<String>) node.labels());
        memberNode.setProperties(node.asMap());

        return memberNode;
    }

    @SneakyThrows
    private List<MemberWithRelationships> getRelationshipBetweenNodes(RelationshipType relationship, String sourceId, String targetId) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            String query = "MATCH (s)-[r:" + relationship.toString() + "]-(t) WHERE s.uuid = '" + sourceId + "' and t.uuid = '" + targetId + "' RETURN s,r,t";
            List<MemberWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();

            session.run(query).forEachRemaining(result -> {
                MemberWithRelationships node = new MemberWithRelationships();
                result.asMap().forEach((k, v) -> {
                    MemberNode sourceNode, targetNode;
                    MemberRelationship nodeRelationship;
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
            throw new GetMembersError(e.getMessage());
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
                List<MemberNode> sourceList = memberRepository.getMembers(String.valueOf(relationshipDTO.getSource().getProperties().get("uuid")));
                if (sourceList == null || sourceList.size() == 0) {
                    throw new CreateRelationshipError("Node with passed source uuid not exist.");
                }
                List<MemberNode> targetList = memberRepository.getMembers(String.valueOf(relationshipDTO.getTarget().getProperties().get("uuid")));
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

            List<MemberWithRelationships> relationshipBetweenNodes = getRelationshipBetweenNodes(relationshipDTO.getType(), String.valueOf(relationshipDTO.getSource().getProperties().get("uuid")), String.valueOf(relationshipDTO.getTarget().getProperties().get("uuid")));
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

            List<MemberWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            String query = queryBuilder.toString();
            session.run(query).forEachRemaining(result -> {
                MemberWithRelationships node = new MemberWithRelationships();
                result.asMap().forEach((k, v) -> {
                    MemberNode sourceNode, targetNode;
                    MemberRelationship nodeRelationship;
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

    private Boolean isValidRelationship(RelationshipType relationship, MemberType sourceType, MemberType targetType) {

        if (MemberType.Citizen == sourceType && RelationshipType.RESIDESIN == relationship && MemberType.HouseHold == targetType) {
            return true;
        }
        if (MemberType.Citizen == sourceType && RelationshipType.MARRIEDTO == relationship && MemberType.Citizen == targetType) {
            return true;
        }
        if (MemberType.Citizen == sourceType && RelationshipType.MOTHEROF == relationship && MemberType.Citizen == targetType) {
            return true;
        }
        if (MemberType.AshaWorker == sourceType && RelationshipType.MEMBEROF == relationship && MemberType.SubCenter == targetType) {
            return true;
        }
        if (MemberType.AshaWorker == sourceType && RelationshipType.REPORTSINTO == relationship && MemberType.PrimaryHealthCareOfficer == targetType) {
            return true;
        }
        if (MemberType.AshaWorker == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.Village == sourceType && RelationshipType.SERVICEDBY == relationship && MemberType.AshaWorker == targetType) {
            return true;
        }
        if (MemberType.DGroupAttender == sourceType && RelationshipType.MEMBEROF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.DGroupAttender == sourceType && RelationshipType.REPORTSINTO == relationship && MemberType.MedicalOfficer == targetType) {
            return true;
        }
        if (MemberType.DGroupAttender == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.GPMember == sourceType && RelationshipType.MEMBEROF == relationship && MemberType.GramPanchayat == targetType) {
            return true;
        }
        if (MemberType.HeadMedicalOfficer == sourceType && RelationshipType.MEMBEROF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.HeadMedicalOfficer == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.JuniorHealthAssistant == sourceType && RelationshipType.MEMBEROF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.JuniorHealthAssistant == sourceType && RelationshipType.REPORTSINTO == relationship && MemberType.MedicalOfficer == targetType) {
            return true;
        }
        if (MemberType.JuniorHealthAssistant == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.JuniorLabTechnician == sourceType && RelationshipType.MEMBEROF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.JuniorLabTechnician == sourceType && RelationshipType.REPORTSINTO == relationship && MemberType.MedicalOfficer == targetType) {
            return true;
        }
        if (MemberType.JuniorLabTechnician == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.JuniorPharmacist == sourceType && RelationshipType.MEMBEROF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.JuniorPharmacist == sourceType && RelationshipType.REPORTSINTO == relationship && MemberType.MedicalOfficer == targetType) {
            return true;
        }
        if (MemberType.JuniorPharmacist == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.MedicalOfficer == sourceType && RelationshipType.MEMBEROF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.MedicalOfficer == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.PhcAdministrator == sourceType && RelationshipType.MEMBEROF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.PhcAdministrator == sourceType && RelationshipType.REPORTSINTO == relationship && MemberType.MedicalOfficer == targetType) {
            return true;
        }
        if (MemberType.PhcAdministrator == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.PrimaryHealthCareOfficer == sourceType && RelationshipType.MEMBEROF == relationship && MemberType.SubCenter == targetType) {
            return true;
        }
        if (MemberType.PrimaryHealthCareOfficer == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.JuniorLabTechnician == sourceType && RelationshipType.HEADOF == relationship && MemberType.Facility == targetType) {
            return true;
        }
        if (MemberType.JuniorHealthAssistant == sourceType && RelationshipType.HEADOF == relationship && MemberType.Facility == targetType) {
            return true;
        }
        if (MemberType.JuniorLabTechnician == sourceType && RelationshipType.HEADOF == relationship && MemberType.Facility == targetType) {
            return true;
        }
        if (MemberType.JuniorPharmacist == sourceType && RelationshipType.HEADOF == relationship && MemberType.Facility == targetType) {
            return true;
        }
        if (MemberType.MedicalOfficer == sourceType && RelationshipType.HEADOF == relationship && MemberType.Facility == targetType) {
            return true;
        }
        if (MemberType.PhcAdministrator == sourceType && RelationshipType.HEADOF == relationship && MemberType.Facility == targetType) {
            return true;
        }
        if (MemberType.PrimaryHealthCareOfficer == sourceType && RelationshipType.HEADOF == relationship && MemberType.Facility == targetType) {
            return true;
        }
        if (MemberType.BlockHealthEducationOfficer == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.FirstDivisionalAssistant == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.SecondDivisionalAssistant == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.JuniorHealthInspectingOfficer == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.SeniorHealthInspectingOfficer == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.JuniorParaMedicalOphthalmicAssistant == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.JuniorPrimaryHealthCareOfficer == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.SeniorPrimaryHealthCareOfficer == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.StaffNurse == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        if (MemberType.Driver == sourceType && RelationshipType.EMPLOYEEOF == relationship && MemberType.Phc == targetType) {
            return true;
        }
        return false;
    }

    private String addPropertiesToRelationship(Map<String, Object> properties, RelationshipType relationship, MemberType sourceType, MemberType targetType) {

        if (relationship == RelationshipType.RESIDESIN && sourceType == MemberType.Citizen && targetType == MemberType.HouseHold) {
            return "";
        }
        if (relationship == RelationshipType.MEMBEROF && sourceType == MemberType.GPMember && targetType == MemberType.GramPanchayat) {
            return "";
        }
        if (relationship == RelationshipType.MARRIEDTO && sourceType == MemberType.Citizen && targetType == MemberType.Citizen) {
            return Utils.mapToString(properties, MarriedToDto.class);
        }
        if (relationship == RelationshipType.MOTHEROF && sourceType == MemberType.Citizen && targetType == MemberType.Citizen) {
            return Utils.mapToString(properties, MotherOfDTO.class);
        }
        return "";
    }

    @SneakyThrows
    public List<HashMap<String, Object>> getRelationshipsGrouping(GroupingDTO groupingDTO) {
        try {
            Session session = driver.session();

            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s:" + groupingDTO.getSource().getType() + ") ");
            if (groupingDTO.getStepCount() > 1) {
                queryBuilder.append("-[*" + groupingDTO.getStepCount() + "]- ");
            } else {
                queryBuilder.append("-[r:" + groupingDTO.getRelationship().toString() + "]- ");
            }
            queryBuilder.append("(t:" + groupingDTO.getTarget().getType() + ") ");
            queryBuilder.append(sourceTargetPropertiesToWhereClause(groupingDTO.getSource().getProperties(), groupingDTO.getTarget().getProperties(), new HashMap<>()));
            if (groupingDTO.getField().equals("")) {
                queryBuilder.append("RETURN count(s) as count ");
            } else {
                queryBuilder.append("RETURN s." + groupingDTO.getField() + " as " + groupingDTO.getField() + ", ");
                queryBuilder.append("count(s." + groupingDTO.getField() + ") as count ");
            }
            List<HashMap<String, Object>> typesCounts = new ArrayList<>();
            String query = queryBuilder.toString();
            session.run(query).forEachRemaining(result -> {
                HashMap<String, Object> typeCount = new HashMap<>();
                result.asMap().forEach((k, v) -> {
                    if (k.equals(groupingDTO.getField())) {
                        typeCount.put(groupingDTO.getField(), v);
                    }
                    if (k.equals("count")) {
                        typeCount.put("count", v);
                    }
                });
                typesCounts.add(typeCount);
            });
            return typesCounts;

        } catch (Exception e) {
            throw new GetMembersError(e.getMessage());
        }

    }

    public String sourceTargetPropertiesToWhereClause(Map<String, Object> sourceProperties, Map<String, Object> targetProperties, Map<String, Object> relationshipProperties) {

        if ((sourceProperties == null || sourceProperties.isEmpty()) && (targetProperties == null || targetProperties.isEmpty())) {
            return "";
        }
        StringBuilder whereClause = new StringBuilder("WHERE ");
        sourceProperties.forEach((k, v) -> {
            if (v.getClass() == String.class) {
                whereClause.append("AND s." + k + " = '" + v + "' ");
            } else {
                whereClause.append("AND s." + k + " = " + v + " ");
            }

        });

        relationshipProperties.forEach((k, v) -> {
            if (v.getClass() == String.class) {
                whereClause.append("AND r." + k + " = '" + v + "' ");
            } else {
                whereClause.append("AND r." + k + " = " + v + " ");
            }

        });

        targetProperties.forEach((k, v) -> {
            if (v.getClass() == String.class) {
                whereClause.append("AND t." + k + " = '" + v + "' ");
            } else {
                whereClause.append("AND t." + k + " = " + v + " ");
            }
        });
        return whereClause.toString().replace("WHERE AND", "WHERE ");
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
            whereClause.append("] ");
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
            whereClause.append("] ");
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
            whereClause.append("] ");
        });

        if (filterDTO.getSrcExpressions() != null && filterDTO.getSrcExpressions().size() > 0) {
            whereClause.append(expressionToString(filterDTO.getSrcExpressions(), "s"));
        }

        if (filterDTO.getTargetExpressions() != null && filterDTO.getTargetExpressions().size() > 0) {
            whereClause.append(expressionToString(filterDTO.getTargetExpressions(), "t"));
        }

        if (filterDTO.getRelExpressions() != null && filterDTO.getRelExpressions().size() > 0) {
            whereClause.append(expressionToString(filterDTO.getRelExpressions(), "r"));
        }

        return whereClause.toString().replace("WHERE AND", "WHERE ").replace(",]", "]");
    }

    public MemberPageResponse getMemberAndRelationships(MemberFilterDTO filter, Integer page, Integer size) {
        if (filter.getSourceId() != null) {
            if (filter.getRelationshipStepCount() > 1) {
                Long totalElements = getCountOfMemberNodesWithStepCountOfRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), filter.getSourceId(), null, filter.getRelationshipStepCount());
                List result = getMemberNodesWithStepCountOfRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), filter.getSourceId(), null, filter.getRelationshipStepCount(), page, size);
                MemberPageResponse response = MemberPageResponse.builder().totalElements(totalElements).content(result).totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).build();
                return response;
            } else {
                Long totalElements = getCountOfMemberNodesWithRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), filter.getSourceId(), null);
                List result = getMemberNodeWithRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), filter.getSourceId(), null, page, size);
                MemberPageResponse response = MemberPageResponse.builder().totalElements(totalElements).content(result).totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).build();
                return response;
            }
        } else if (filter.getTargetId() != null) {
            if (filter.getRelationshipStepCount() > 1) {
                Long totalElements = getCountOfMemberNodesWithStepCountOfRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), null, filter.getTargetId(), filter.getRelationshipStepCount());
                List result = getMemberNodesWithStepCountOfRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), null, filter.getTargetId(), filter.getRelationshipStepCount(), page, size);
                MemberPageResponse response = MemberPageResponse.builder().totalElements(totalElements).content(result).totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).build();
                return response;
            } else {
                Long totalElements = getCountOfMemberNodesWithRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), null, filter.getTargetId());
                List result = getMemberNodeWithRelationshipBySourceIdOrTargetId(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), null, filter.getTargetId(), page, size);
                MemberPageResponse response = MemberPageResponse.builder().totalElements(totalElements).content(result).totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).build();
                return response;
            }
        } else {
            List result = getMembersNodesByRelationship(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType(), page, size);
            Long totalElements = getCountOfMemberNodesWithRelationship(filter.getRelationshipType(), filter.getTargetType(), filter.getSourceType());
            MemberPageResponse response = MemberPageResponse.builder().totalElements(totalElements).content(result).totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).build();
            return response;
        }
    }

    @SneakyThrows
    public List getMembersNodesByRelationship(RelationshipType relationshipType, MemberType targetType, MemberType sourceType, Integer page, Integer size) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            List<MemberWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            String query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ")  RETURN s, r, t skip " + (page * size) + " limit " + size;
            session.run(query).forEachRemaining(result -> {
                MemberWithRelationships node = new MemberWithRelationships();
                result.asMap().forEach((k, v) -> {
                    MemberNode sourceNode, targetNode;
                    MemberRelationship nodeRelationship;
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
            throw new GetMembersError(e.getMessage());
        }

    }

    @SneakyThrows
    public List getMemberNodeWithRelationshipBySourceIdOrTargetId(RelationshipType relationshipType, MemberType targetType, MemberType sourceType, String sourceId, String targetId, Integer page, Integer size) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            List<MemberWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            String query = "";
            if (sourceId != null) {
                query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ") where s.uuid= '" + sourceId + "' RETURN s, r, t skip " + (page * size) + " limit " + size;
            } else if (targetId != null) {
                query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ") where t.uuid= '" + targetId + "' RETURN s, r, t skip " + (page * size) + " limit " + size;
            }
            session.run(query).forEachRemaining(result -> {
                MemberWithRelationships node = new MemberWithRelationships();
                result.asMap().forEach((k, v) -> {
                    MemberNode sourceNode, targetNode;
                    MemberRelationship nodeRelationship;
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
            throw new GetMembersError(e.getMessage());
        }
    }

    @SneakyThrows
    private List getMemberNodesWithStepCountOfRelationshipBySourceIdOrTargetId(RelationshipType relationshipType, MemberType targetType, MemberType sourceType, String sourceId, String targetId, Integer stepCount, Integer page, Integer size) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            List<MemberWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            String query = "";
            if (sourceId != null) {
                query = "MATCH (s:" + sourceType + ")-[*" + stepCount + "]-(t:" + targetType + ") where s.uuid = '" + sourceId + "' RETURN s, t skip " + (page * size) + " limit " + size;
            } else if (targetId != null) {
                query = "MATCH (s:" + sourceType + ")-[*" + stepCount + "]-(t:" + targetType + ") where t.uuid = '" + targetId + "' RETURN s, t skip " + (page * size) + " limit " + size;
            }
            session.run(query).forEachRemaining(result -> {
                MemberWithRelationships node = new MemberWithRelationships();
                result.asMap().forEach((k, v) -> {
                    MemberNode sourceNode, targetNode;
                    MemberRelationship nodeRelationship = new MemberRelationship();
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
            throw new GetMembersError(e.getMessage());
        }
    }

    @SneakyThrows
    public Long getCountOfMemberNodesWithRelationship(RelationshipType relationshipType, MemberType targetType, MemberType sourceType) {
        try {
            Session session = driver.session();
            String query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ")  RETURN count(s) as count";
            return (Long) session.run(query).list().get(0).asMap().get("count");
        } catch (Exception e) {
            throw new GetMembersError(e.getMessage());
        }
    }

    @SneakyThrows
    public Long getCountOfMemberNodesWithRelationshipBySourceIdOrTargetId(RelationshipType relationshipType, MemberType targetType, MemberType sourceType, String sourceId, String targetId) {
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
            throw new GetMembersError(e.getMessage());
        }
    }

    @SneakyThrows
    private Long getCountOfMemberNodesWithStepCountOfRelationshipBySourceIdOrTargetId(RelationshipType relationshipType, MemberType targetType, MemberType sourceType, String sourceId, String targetId, Integer stepCount) {
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
            throw new GetMembersError(e.getMessage());
        }
    }

    @SneakyThrows
    public Long getCountOfRelationshipsByMemberID(String id) {
        try {
            Session session = driver.session();
            String query = "MATCH (sourcenode)-[relation]-(targetnode) WHERE sourcenode.uuid = '" + id + "' RETURN  count(sourcenode) as count";
            return (Long) session.run(query).list().get(0).asMap().get("count");
        } catch (Exception e) {
            throw new GetMembersError(e.getMessage());
        }
    }

    @SneakyThrows
    public List<MemberWithRelationships> updatedRelationshipProperties(String relationshipId, RelationshipPatchDTO patchDTO) {
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

            List<MemberWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            session.run(queryBuilder.toString()).forEachRemaining(result -> {
                MemberWithRelationships node = new MemberWithRelationships();
                result.asMap().forEach((k, v) -> {
                    MemberNode sourceNode, targetNode;
                    MemberRelationship nodeRelationship;
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
            throw new GetMembersError(e.getMessage());
        }
    }

    @SneakyThrows
    private String addPropertiesToRelationship(RelationshipType type, Map<String, Object> properties) {
        try {
            switch (type) {
                case MARRIEDTO:
                    return Utils.mapToString(properties, MarriedToDto.class);
                case MOTHEROF:
                    return Utils.mapToString(properties, MotherOfDTO.class);
                default:
                    return "";
            }

        } catch (Exception e) {
            throw new CreateRelationshipError(e.getMessage());
        }
    }

    @SneakyThrows
    public MembersCountContentResponse getMemberRelationshipByFilters(RelationshipFilterDTO filterInput) {
        try {

            Session session = driver.session();
            Gson gson = new Gson();
            List<MemberWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
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
                MemberWithRelationships node = new MemberWithRelationships();
                result.asMap().forEach((k, v) -> {
                    MemberNode sourceNode, targetNode;
                    MemberRelationship nodeRelationship = null;
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
                        nodeRelationship = new MemberRelationship();
                        nodeRelationship.setProperties(new HashMap<>());
                        nodeRelationship.setType(filterInput.getRelationshipType());
                        node.setRelationship(nodeRelationship);
                    }
                });
                nodeWithMembersAndRelationships.add(node);
            });
            return MembersCountContentResponse.builder().content(nodeWithMembersAndRelationships).count((Long) session.run(countQuery).list().get(0).asMap().get("count")).build();
        } catch (Exception e) {
            throw new GetMembersError(e.getMessage());
        }
    }

    @SneakyThrows
    public List createRelationshipBetweenNodes(CreateRelationshipDTO relationshipDTO) {
        try {
            Session session = driver.session();

            if (!isValidRelationship(relationshipDTO.getType(), relationshipDTO.getSrcType(), relationshipDTO.getTargetType())) {
                throw new CreateRelationshipError("Not a valid relationship b/w passed node");
            }

            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s:" + relationshipDTO.getSrcType() + ") ");
            queryBuilder.append(", ");
            queryBuilder.append("(t:" + relationshipDTO.getTargetType() + ") ");
            queryBuilder.append("WHERE ");
            queryBuilder.append("s.uuid = '" + relationshipDTO.getSrcId() + "' ");
            queryBuilder.append("AND ");
            queryBuilder.append("t.uuid = '" + relationshipDTO.getTargetId() + "' ");

            List<MemberWithRelationships> relationshipBetweenNodes = getRelationshipBetweenNodes(relationshipDTO.getType(), relationshipDTO.getSrcId(), relationshipDTO.getTargetId());
            if (relationshipBetweenNodes.size() > 0) {
                return relationshipBetweenNodes;
            }

            queryBuilder.append("CREATE ");
            queryBuilder.append("(s)-");
            queryBuilder.append("[r:" + relationshipDTO.getType() + " ");
            queryBuilder.append(" ]->");
            queryBuilder.append("(t) ");
            queryBuilder.append("RETURN s, t ,r");

            List<MemberWithRelationships> nodeWithMembersAndRelationships = new ArrayList<>();
            String query = queryBuilder.toString();
            return mapNodeData(session, nodeWithMembersAndRelationships, query);
        } catch (Exception e) {
            throw new CreateRelationshipError(e);
        }
    }


    private List mapNodeData(Session session, List<MemberWithRelationships> nodeWithMembersAndRelationships, String query) {
        Gson gson = new Gson();
        session.run(query).forEachRemaining(result -> {
            MemberWithRelationships node = new MemberWithRelationships();
            result.asMap().forEach((k, v) -> {
                MemberNode sourceNode, targetNode;
                MemberRelationship nodeRelationship;
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
    }

    @SneakyThrows
    private String expressionToString(List<Expression> expressions, String type) {
        if (expressions == null || expressions.size() == 0) {
            return "";
        }
        StringBuilder query = new StringBuilder("");
        for (Expression expression : expressions) {
            String k = expression.getKey();
            if (expression.getValue().getClass() == String.class) {
                if (k.contains("date")) {
                    query.append("AND " + type + "." + k + " " + expression.getSymbol().getSymbol() + "  localdatetime('" + expression.getValue() + "') ");
                } else {
                    query.append("AND " + type + "." + k + " " + expression.getSymbol().getSymbol() + "  '" + expression.getValue() + "' ");
                }
            } else {
                query.append("AND " + type + "." + k + " " + expression.getSymbol().getSymbol() + "  " + expression.getValue() + " ");
            }
        }
        return query.toString();
    }

}

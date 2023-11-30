package com.ssf.organization.repositories;

import com.google.gson.Gson;
import com.ssf.organization.dtos.*;
import com.ssf.organization.exceptions.CreateRelationshipError;
import com.ssf.organization.exceptions.GetOrgError;
import com.ssf.organization.model.*;
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
public class OrganizationRelationshipRepository {

    @Autowired
    private Driver driver;

    private Session session;

    public OrganizationRelationshipRepository(Driver driver) {
        this.driver = driver;
        this.session = driver.session();
    }

    public List<OrgNode> getOrg(String id) {

        try {
            String query = "MATCH (n) WHERE n.uuid = '" + id + "' RETURN n";
            List<OrgNode> nodes = new ArrayList<>();
            session.run(query).forEachRemaining(r -> {
                nodes.add(getValues(r.get("n").asNode()));
            });
            return nodes;
        } catch (Exception e) {
            throw new GetOrgError(e);
        }
    }

    private OrgNode getValues(Node e) {
        OrgNode node = new OrgNode();
        node.setId(e.id());
        node.setLabels((List<String>) e.labels());
        node.setProperties(e.asMap());
        return node;
    }

    private OrgRelationship getValues(Relationship e) {
        OrgRelationship orgRelationship = new OrgRelationship();
        orgRelationship.setId(e.id());
        orgRelationship.setType(RelType.valueOf(e.type()));
        orgRelationship.setProperties(e.asMap());
        return orgRelationship;
    }

    public List getOrgNodeWithRelationship(RelType relationshipType, OrgType targetType, OrgType sourceType, Integer page, Integer size) {

        try {
            List<OrgRelResponse> nodeWithOrgAndRelationship = new ArrayList<>();
            String query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ")  RETURN s, r, t ";
            if (sourceType == OrgType.HouseHold) {
                query = query + " ORDER BY s.houseID ASC ";
            } else if (targetType == OrgType.HouseHold) {
                query = query + " ORDER BY t.houseID ASC ";
            }
            query = query + " skip " + (page * size) + " limit " + size;
            return mapNodeData(session, nodeWithOrgAndRelationship, query);
        } catch (Exception e) {
            throw new GetOrgError(e);
        }

    }

    public List getOrgNodeWithRelationshipBySourceId(RelType relationshipType, OrgType targetType, OrgType sourceType, String sourceId, Integer page, Integer size) {

        try {
            List<OrgRelResponse> nodeWithOrgAndRelationship = new ArrayList<>();
            String query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ") where s.uuid = '" + sourceId + "' RETURN s, r, t ";
            if (sourceType == OrgType.HouseHold) {
                query = query + " ORDER BY s.houseID ASC ";
            } else if (targetType == OrgType.HouseHold) {
                query = query + " ORDER BY t.houseID ASC ";
            }
            query = query + " skip " + (page * size) + " limit " + size;
            return mapNodeData(session, nodeWithOrgAndRelationship, query);
        } catch (Exception e) {
            throw new GetOrgError(e);
        }

    }

    public List getOrgNodeWithRelationshipByTargetId(RelType relationshipType, OrgType targetType, OrgType sourceType, String targetId, Integer page, Integer size) {

        try {
            List<OrgRelResponse> nodeWithOrgAndRelationship = new ArrayList<>();
            String query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ") where t.uuid = '" + targetId + "' RETURN s, r, t ";
            if (sourceType == OrgType.HouseHold) {
                query = query + " ORDER BY s.houseID ASC ";
            } else if (targetType == OrgType.HouseHold) {
                query = query + " ORDER BY t.houseID ASC ";
            }
            query = query + " skip " + (page * size) + " limit " + size;
            return mapNodeData(session, nodeWithOrgAndRelationship, query);
        } catch (Exception e) {
            throw new GetOrgError(e);
        }

    }

    public Long getCountOrgNodeWithRelationship(RelType relationshipType, OrgType targetType, OrgType sourceType) {

        try {
            String query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ")  RETURN count(s) as count";
            return (Long) session.run(query).list().get(0).asMap().get("count");
        } catch (Exception e) {
            throw new GetOrgError(e);
        }
    }

    public Long getCountOrgNodeWithRelationshipBySourceId(RelType relationshipType, OrgType targetType, OrgType sourceType, String sourceId) {

        try {
            String query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ") where s.uuid = '" + sourceId + "'  RETURN count(s) as count";
            return (Long) session.run(query).list().get(0).asMap().get("count");
        } catch (Exception e) {
            throw new GetOrgError(e);
        }
    }

    public Long getCountOrgNodeWithRelationshipByTargetId(RelType relationshipType, OrgType targetType, OrgType sourceType, String targetId) {

        try {
            String query = "MATCH (s:" + sourceType + ") - [r:" + relationshipType + "] - (t:" + targetType + ") where t.uuid = '" + targetId + "'  RETURN count(t) as count";
            return (Long) session.run(query).list().get(0).asMap().get("count");
        } catch (Exception e) {
            throw new GetOrgError(e);
        }
    }

    public OrgCountContentResponse getOrgNodeWithFilters(NodeFilterInput filterInput) {

        StringBuilder queryBuilder = new StringBuilder("MATCH (s:" + filterInput.getType().toString() + ") ");

        if (filterInput.getProperties() != null && filterInput.getProperties().size() > 0) {
            queryBuilder.append("WHERE ");
            filterInput.getProperties().forEach((k, v) -> {
                if (v.getClass() == String.class) {
                    queryBuilder.append("AND s." + k + "=\"" + v + "\" ");
                } else {
                    queryBuilder.append("AND s." + k + "=" + v + " ");
                }
            });
        }

        String countQuery = queryBuilder.toString() + "RETURN count(s) as count";
        queryBuilder.append("RETURN s SKIP " + (filterInput.getPage() * filterInput.getSize()) + " LIMIT " + filterInput.getSize());

        String query = queryBuilder.toString();
        if (filterInput.getProperties() != null && filterInput.getProperties().size() > 0) {
            query = query.replaceAll("WHERE AND", "WHERE");
            countQuery = countQuery.replaceAll("WHERE AND", "WHERE");
        }


        try {
            List<OrgNode> nodes = new ArrayList<>();
            session.run(query).forEachRemaining(result -> {
                result.asMap().forEach((k, v) -> {
                    if (k.equals("s")) {
                        nodes.add(getValues((Node) v));
                    }
                });
            });
            Long count = (Long) session.run(countQuery).list().get(0).asMap().get("count");
            return OrgCountContentResponse.builder().content(nodes).count(count).build();
        } catch (Exception e) {
            throw new GetOrgError(e);
        }
    }

    @SneakyThrows
    public OrgCountContentResponse searchByFullTextIndex(String name, String index, Integer limit, Integer skip, NodeFilterInput filterInput) {


        try {
            String query = "CALL db.index.fulltext.queryNodes('" + index + "', '(?i)" + name + "*' )  YIELD node, score ";

            if (filterInput.getProperties().containsKey("villageId")) {
                query = query + "MATCH (node)-[*1]-(t) WHERE t.lgdCode = '" + filterInput.getProperties().get("villageId").toString() + "' ";
            }
            String countQuery = query + " RETURN count(node) as count";

            query = query + " RETURN node ";

            if (index.equals(SearchIndexType.HouseHoldIDNameIndex.name())) {
                query = query + " ORDER BY node.houseID ASC ";
            }

            query = query + " SKIP " + skip + " LIMIT " + limit;

            List<OrgNode> nodes = new ArrayList<>();

            session.run(query).list().forEach(result -> {
                result.asMap().forEach((k, v) -> {
                    if (k.equals("node")) {
                        nodes.add(getValues((Node) v));
                    }
                });
            });

            return OrgCountContentResponse.builder().content(nodes)
                    .count((Long) session.run(countQuery).list().get(0).asMap().get("count"))
                    .build();
        } catch (Exception e) {
            throw new GetOrgError(e);
        }

    }

    @SneakyThrows
    public List<HashMap<String, Object>> getRelationshipsGrouping(GroupingDTO groupingDTO) {

        try {

            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s:" + groupingDTO.getSource().getType() + ") ");
            if (groupingDTO.getStepCount() > 1) {
                queryBuilder.append("-[*" + groupingDTO.getStepCount() + "]- ");
            } else {
                queryBuilder.append("-[r:" + groupingDTO.getRelationship().toString() + "]- ");
            }
            queryBuilder.append("(t:" + groupingDTO.getTarget().getType() + ") ");
            queryBuilder.append(sourceTargetPropertiesToWhereClause(groupingDTO.getSource().getProperties(), groupingDTO.getTarget().getProperties()));
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
            throw new GetOrgError(e);
        }

    }

    private String sourceTargetPropertiesToWhereClause(Map<String, Object> sourceProperties, Map<String, Object> targetProperties) {
        StringBuilder whereClause = new StringBuilder("WHERE ");

        sourceProperties.forEach((k, v) -> {
            if (v.getClass() == String.class) {
                whereClause.append("AND s." + k + " = '" + v + "' ");
            } else {
                whereClause.append("AND s." + k + " = " + v + " ");
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

    @SneakyThrows
    public OrgCountContentResponse getOrgRelationshipByFilters(RelationshipFilterDTO filterInput) {


        try {

            List<OrgRelResponse> nodeWithOrgRel = new ArrayList<>();
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
            System.out.println(queryBuilder.toString());
            session.run(queryBuilder.toString()).forEachRemaining(result -> {
                OrgRelResponse node = new OrgRelResponse();
                OrgRelationship nodeRelationship = new OrgRelationship();
                result.asMap().forEach((k, v) -> {
                    if (k.equals("r")) {
                        node.setRel(getValues((Relationship) v));
                    }
                    if (k.equals("t")) {
                        node.setTarget(getValues((Node) v));
                    }
                    if (k.equals("s")) {
                        node.setSource(getValues((Node) v));
                    }
                    if (node.getRel() == null) {
                        nodeRelationship.setProperties(new HashMap<>());
                        nodeRelationship.setType(filterInput.getRelationshipType());
                        node.setRel(nodeRelationship);
                    }
                });
                nodeWithOrgRel.add(node);
            });
            return OrgCountContentResponse.builder().content(nodeWithOrgRel).count((Long) session.run(countQuery).list().get(0).asMap().get("count")).build();
        } catch (Exception e) {
            throw new GetOrgError(e);
        }
    }

    @SneakyThrows
    public List createRelationshipBetweenNodes(RelationshipDTO relationshipDTO) {

        try {

            if (!isValidRelationship(relationshipDTO.getType(), relationshipDTO.getSource().getType(), relationshipDTO.getTarget().getType())) {
                throw new CreateRelationshipError("Not a valid relationship b/w passed node");
            }

            if (relationshipDTO.getSource().getProperties().containsKey("uuid") && relationshipDTO.getTarget().getProperties().containsKey("uuid")) {
                List<OrgNode> sourceList = getOrg(String.valueOf(relationshipDTO.getSource().getProperties().get("uuid")));
                if (sourceList == null || sourceList.size() == 0) {
                    throw new CreateRelationshipError("Node with passed source uuid not exist.");
                }
                List<OrgNode> targetList = getOrg(String.valueOf(relationshipDTO.getTarget().getProperties().get("uuid")));
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

            List<OrgRelResponse> relationshipBetweenNodes = getRelationshipBetweenNodes(relationshipDTO.getType(), String.valueOf(relationshipDTO.getSource().getProperties().get("uuid")),
                    String.valueOf(relationshipDTO.getTarget().getProperties().get("uuid")));
            if (relationshipBetweenNodes.size() > 0) {
                return relationshipBetweenNodes;
            }

            queryBuilder.append("CREATE ");
            queryBuilder.append("(s)-");
            queryBuilder.append("[r:" + relationshipDTO.getType() + " ");
            queryBuilder.append(addPropertiesToRelationship(relationshipDTO));
            queryBuilder.append(" ]->");
            queryBuilder.append("(t) ");
            queryBuilder.append("RETURN s, t ,r");

            List<OrgRelResponse> orgRelResponses = new ArrayList<>();
            String query = queryBuilder.toString();
            return mapNodeData(session, orgRelResponses, query);
        } catch (Exception e) {
            throw new CreateRelationshipError(e.getCause().getMessage());
        }
    }

    @SneakyThrows
    public List createRelationshipBetweenNodes(CreateRelationshipDTO relationshipDTO) {


        try {

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

            List<OrgRelResponse> relationshipBetweenNodes = getRelationshipBetweenNodes(relationshipDTO.getType(), relationshipDTO.getSrcId(), relationshipDTO.getTargetId());
            if (relationshipBetweenNodes.size() > 0) {
                return relationshipBetweenNodes;
            }

            queryBuilder.append("CREATE ");
            queryBuilder.append("(s)-");
            queryBuilder.append("[r:" + relationshipDTO.getType() + " ");
            queryBuilder.append(" ]->");
            queryBuilder.append("(t) ");
            queryBuilder.append("RETURN s, t ,r");

            List<OrgRelResponse> orgRelResponses = new ArrayList<>();
            String query = queryBuilder.toString();
            return mapNodeData(session, orgRelResponses, query);
        } catch (Exception e) {
            throw new CreateRelationshipError(e);
        }
    }

    private List mapNodeData(Session session, List<OrgRelResponse> orgRelResponses, String query) {
        session.run(query).forEachRemaining(result -> {
            OrgRelResponse node = new OrgRelResponse();
            result.asMap().forEach((k, v) -> {
                if (k.equals("r")) {
                    node.setRel(getValues((Relationship) v));
                }
                if (k.equals("t")) {
                    node.setTarget(getValues((Node) v));
                }
                if (k.equals("s")) {
                    node.setSource(getValues((Node) v));
                }
            });
            orgRelResponses.add(node);
        });
        return orgRelResponses;
    }

    @SneakyThrows
    public List<OrgRelResponse> updatedRelationshipProperties(String relationshipId, RelationshipPatchDTO patchDTO) {

        try {
            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s)-");
            queryBuilder.append("[r:" + patchDTO.getType() + "]->");
            queryBuilder.append("(t) ");
            queryBuilder.append("WHERE ");
            queryBuilder.append("r.uuid = '" + relationshipId + "' ");
            queryBuilder.append("SET r += ");
            queryBuilder.append(addPropertiesToRelationship(patchDTO.getType(), patchDTO.getProperties()));
            queryBuilder.append("RETURN s,r,t");

            List<OrgRelResponse> orgRelResponses = new ArrayList<>();
            session.run(queryBuilder.toString()).forEachRemaining(result -> {
                OrgRelResponse node = new OrgRelResponse();
                result.asMap().forEach((k, v) -> {
                    if (k.equals("r")) {
                        node.setRel(getValues((Relationship) v));
                    }
                    if (k.equals("t")) {
                        node.setTarget(getValues((Node) v));
                    }
                    if (k.equals("s")) {
                        node.setSource(getValues((Node) v));
                    }
                });
                orgRelResponses.add(node);
            });
            return orgRelResponses;
        } catch (Exception e) {
            throw new GetOrgError(e.getCause().getMessage());
        }
    }

    private Boolean isValidRelationship(RelType relationship, OrgType sourceType, OrgType targetType) {

        if (OrgType.State == sourceType && RelType.CONTAINEDINPLACE == relationship && OrgType.Country == targetType) {
            return true;
        }
        if (OrgType.District == sourceType && RelType.CONTAINEDINPLACE == relationship && OrgType.State == targetType) {
            return true;
        }
        if (OrgType.Taluk == sourceType && RelType.CONTAINEDINPLACE == relationship && OrgType.District == targetType) {
            return true;
        }
        if (OrgType.Taluk == sourceType && RelType.CONTAINEDINPLACE == relationship && OrgType.Village == targetType) {
            return true;
        }
        if (OrgType.GramPanchayat == sourceType && RelType.ADMINISTEREDBY == relationship && OrgType.Taluk == targetType) {
            return true;
        }
        if (OrgType.GramPanchayat == sourceType && RelType.SERVICEDBY == relationship && OrgType.Phc == targetType) {
            return true;
        }
        if (OrgType.Village == sourceType && RelType.GOVERNEDBY == relationship && OrgType.GramPanchayat == targetType) {
            return true;
        }
        if (OrgType.Village == sourceType && RelType.SERVICEDAREA == relationship && OrgType.SubCenter == targetType) {
            return true;
        }
        if (OrgType.SubCenter == sourceType && RelType.SUBORGOF == relationship && OrgType.Phc == targetType) {
            return true;
        }
        if (OrgType.HouseHold == sourceType && RelType.CONTAINEDINPLACE == relationship && OrgType.Village == targetType) {
            return true;
        }
        if (OrgType.HouseHold == sourceType && RelType.CONTAINEDINPLACE == relationship && OrgType.Locality == targetType) {
            return true;
        }
        if (OrgType.Place == sourceType && RelType.CONTAINEDINPLACE == relationship && OrgType.Village == targetType) {
            return true;
        }
        if (OrgType.Facility == sourceType && RelType.HOSTEDIN == relationship && OrgType.Phc == targetType) {
            return true;
        }
        if (OrgType.Facility == sourceType && RelType.HOSTEDIN == relationship && OrgType.SubCenter == targetType) {
            return true;
        }

        return false;
    }

    private String addPropertiesToRelationship(RelationshipDTO relationshipDTO) {
        return "";
    }

    @SneakyThrows
    private String addPropertiesToRelationship(RelType type, Map<String, Object> properties) {
        try {
            return "";
        } catch (Exception e) {
            throw new CreateRelationshipError(e.getCause().getMessage());
        }
    }

    @SneakyThrows
    public List<OrgRelResponse> getRelationshipBetweenNodes(RelType relationship, String sourceId, String targetId) {

        try {
            String query = "MATCH (s)-[r:" + relationship.toString() + "]-(t) WHERE s.uuid = '" + sourceId + "' and t.uuid = '" + targetId + "' RETURN s,r,t";
            List<OrgRelResponse> orgRelResponses = new ArrayList<>();
            return mapNodeData(session, orgRelResponses, query);
        } catch (Exception e) {
            throw new GetOrgError(e.getCause().getMessage());
        }
    }

    public OrgCountContentResponse getOrgsWithFilters(NodeFilterInput filter) {
        try {

            Session session = driver.session();
            Gson gson = new Gson();
            List<OrgNode> orgNodes = new ArrayList<>();
            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s:" + filter.getType() + ") ");
            queryBuilder.append(sourcePropertiesToWhereClause(filter.getExpressions()));
            String countQuery = queryBuilder + " RETURN count(s) as count";
            queryBuilder.append("RETURN s ");
            queryBuilder.append("SKIP " + (filter.getPage() * filter.getSize()) + " LIMIT " + filter.getSize());
            session.run(queryBuilder.toString()).forEachRemaining(result -> {
                result.asMap().forEach((k, v) -> {
                    OrgNode orgNode = null;
                    if (k.equals("s")) {
                        orgNode = getValues((Node) v);
                    }
                    orgNodes.add(orgNode);
                });
            });
            return OrgCountContentResponse.builder().content(orgNodes)
                    .count((Long) session.run(countQuery).list().get(0).asMap().get("count"))
                    .build();

        } catch (Exception e) {
            throw new GetOrgError(e.getMessage());
        }
    }

    @SneakyThrows
    public String sourcePropertiesToWhereClause(List<Expression> expressions) {
        if (expressions == null || expressions.size() == 0) {
            return "";
        }
        StringBuilder whereClause = new StringBuilder("WHERE ");
        for (Expression expression : expressions) {
            String k = expression.getKey();
            if (expression.getValue().getClass() == String.class) {
                if (k.contains("date")) {
                    whereClause.append("AND s." + k + " " + expression.getSymbol().getSymbol() + "  localdatetime('" + expression.getValue() + "') ");
                } else {
                    whereClause.append("AND s." + k + " " + expression.getSymbol().getSymbol() + "  '" + expression.getValue() + "' ");
                }
            } else {
                whereClause.append("AND s." + k + " " + expression.getSymbol().getSymbol() + "  " + expression.getValue() + " ");

            }
        }

        return whereClause.toString().replace("WHERE AND", "WHERE ");
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

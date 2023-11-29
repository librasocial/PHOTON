package com.ssf.membership.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.ssf.membership.dtos.*;
import com.ssf.membership.entities.Expression;
import com.ssf.membership.exceptions.GetMembersError;
import com.ssf.membership.model.MemberNode;
import com.ssf.membership.model.MemberRelationship;
import com.ssf.membership.model.RelationshipType;
import com.ssf.membership.utils.MembershipConstants;
import com.ssf.membership.utils.Utils;
import lombok.SneakyThrows;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MemberRepository {

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    @Autowired
    private Driver driver;

    @Autowired
    private MemberRelationshipRepository memberRelationshipRepository;

    public MemberRepository(Driver driver) {
        this.driver = driver;
    }

    @SneakyThrows
    public List<MemberNode> getMembers(String id) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            String query = "MATCH (n) WHERE n.uuid = '" + id + "' RETURN n ";
            List<MemberNode> memberNodes = new ArrayList<>();
            session.run(query).forEachRemaining(r -> {
                MemberNode memberNode = getValues(r.get("n").asNode());
                memberNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(memberNode.getProperties(), Map.class)), memberNode.getLabels().get(0)), Map.class));
                memberNodes.add(memberNode);
            });
            return memberNodes;
        } catch (Exception e) {
            throw new GetMembersError(e.getMessage());
        }
    }

    @SneakyThrows
    public List getRelationshipsByMemberID(String id, Integer page, Integer size) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            List<MemberWithRelationships> nodesWithRelationships = new ArrayList<>();
            String query = "MATCH (sourcenode)-[relation]-(targetnode) WHERE sourcenode.uuid = '" + id + "' RETURN  sourcenode,relation,targetnode skip " + (page * size) + " limit " + size;
            session.run(query).forEachRemaining(result -> {
                MemberWithRelationships node = new MemberWithRelationships();
                result.asMap().forEach((k, v) -> {
                    MemberNode sourceNode, targetNode;
                    MemberRelationship nodeRelationship;
                    if (k.equals("relation")) {
                        nodeRelationship = getValues((Relationship) v);
                        nodeRelationship.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(nodeRelationship.getProperties(), Map.class)), nodeRelationship.getType().toString()), Map.class));
                        node.setRelationship(nodeRelationship);
                    }
                    if (k.equals("targetnode")) {
                        targetNode = getValues((Node) v);
                        targetNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(targetNode.getProperties(), Map.class)), targetNode.getLabels().get(0)), Map.class));
                        node.setTargetNode(targetNode);
                    }
                    if (k.equals("sourcenode")) {
                        sourceNode = getValues((Node) v);
                        sourceNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(sourceNode.getProperties(), Map.class)), sourceNode.getLabels().get(0)), Map.class));
                        node.setSourceNode(sourceNode);
                    }
                });

                nodesWithRelationships.add(node);
            });

            return nodesWithRelationships;
        } catch (Exception e) {
            throw new GetMembersError(e.getMessage());
        }
    }

    private MemberRelationship getValues(Relationship relation) {
        MemberRelationship relationship = new MemberRelationship();
        relationship.setId(relation.id());
        relationship.setType(RelationshipType.valueOf(relation.type()));
        relationship.setProperties(relation.asMap());
        return relationship;
    }

    private MemberNode getValues(Node node) {
        MemberNode memberNode = new MemberNode();
        memberNode.setId(node.id());
        memberNode.setLabels((List<String>) node.labels());
        memberNode.setProperties(node.asMap());

        return memberNode;
    }


    @SneakyThrows
    public MembersCountContentResponse searchByFullTextIndex(String name, String index, Integer limit, Integer skip, NodeFilterInput filterInput) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            String query = "CALL db.index.fulltext.queryNodes('" + index + "', '" + name + "*')  YIELD node, score ";
            if (filterInput.getProperties().containsKey(MembershipConstants.citizenHealthID) || filterInput.getProperties().containsKey(MembershipConstants.citizenGender) || filterInput.getProperties().containsKey(MembershipConstants.citizenVillage)) {

                if (filterInput.getProperties().containsKey(MembershipConstants.citizenVillage)) {
                    query = query + "MATCH (node)-[*2]-(t) WHERE t.lgdCode IN [" + filterInput.getProperties().get(MembershipConstants.citizenVillage).toString() + "] ";
                } else {
                    query = query + "MATCH (node) WHERE ";
                }
                if (filterInput.getProperties().containsKey(MembershipConstants.citizenVillage) && filterInput.getProperties().containsKey(MembershipConstants.citizenGender)) {
                    query = query + "AND ";
                }

                if (filterInput.getProperties().containsKey(MembershipConstants.citizenGender)) {
                    query = query + " node." + MembershipConstants.citizenGender + " IN [" + filterInput.getProperties().get(MembershipConstants.citizenGender).toString() + "] ";
                }
                if (filterInput.getProperties().containsKey(MembershipConstants.citizenHealthID) && filterInput.getProperties().containsKey(MembershipConstants.citizenGender)) {
                    query = query + "AND ";
                }
                if (filterInput.getProperties().containsKey(MembershipConstants.citizenHealthID) && filterInput.getProperties().containsKey(MembershipConstants.citizenVillage) && !filterInput.getProperties().containsKey(MembershipConstants.citizenGender)) {
                    query = query + "AND ";
                }
                if (filterInput.getProperties().containsKey(MembershipConstants.citizenHealthID)) {
                    if ((Boolean) filterInput.getProperties().get(MembershipConstants.citizenHealthID)) {
                        query = query + " node." + MembershipConstants.citizenHealthID + " IS NOT NULL";
                    } else {
                        query = query + " node." + MembershipConstants.citizenHealthID + " IS NULL";
                    }
                }

            }

            String countQuery = query + " RETURN count(node) as count";

            query = query + " RETURN node SKIP " + skip + " LIMIT " + limit;

            List<MemberNode> members = new ArrayList<>();

            session.run(query).list().forEach(result -> {

                result.asMap().forEach((k, v) -> {
                    MemberNode node;
                    if (k.equals("node")) {
                        node = getValues((Node) v);
                        node.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(node.getProperties(), Map.class)), node.getLabels().get(0)), Map.class));
                        members.add(node);
                    }
                });
            });

            return MembersCountContentResponse.builder().content(members)
                    .count((Long) session.run(countQuery).list().get(0).asMap().get("count"))
                    .build();
        } catch (Exception e) {
            throw new GetMembersError(e.getMessage());
        }

    }

    @SneakyThrows
    public MembersCountContentResponse searchByIndex(String keyword, String property, Integer limit, Integer skip, NodeFilterInput filterInput) {
        try {

            Gson gson = new Gson();
            Session session = driver.session();
            String query = "MATCH (n:Citizen)";

            if (filterInput.getProperties().containsKey(MembershipConstants.citizenVillage)) {
                query = query + "-[*2]-(t) ";
            }

            if (property.equals(MembershipConstants.citizenDateOfBirth)) {
                query = query + " WHERE n." + property + " = DATE('" + keyword + "') ";
            } else {
                query = query + " WHERE n." + property + " = '" + keyword + "' ";
            }

            if (filterInput.getProperties().containsKey(MembershipConstants.citizenVillage)) {
                query = query + " AND t.lgdCode IN [" + filterInput.getProperties().get(MembershipConstants.citizenVillage).toString() + "] ";
            }

            if (filterInput.getProperties().containsKey(MembershipConstants.citizenGender)) {
                query = query + "AND n." + MembershipConstants.citizenGender + " IN [" + filterInput.getProperties().get(MembershipConstants.citizenGender).toString() + "] ";
            }

            if (filterInput.getProperties().containsKey(MembershipConstants.citizenHealthID)) {
                if ((Boolean) filterInput.getProperties().get(MembershipConstants.citizenHealthID)) {
                    query = query + "AND n." + MembershipConstants.citizenHealthID + " IS NOT NULL";
                } else {
                    query = query + "AND n." + MembershipConstants.citizenHealthID + " IS NULL";
                }
            }

            String countQuery = query + " RETURN count(n) as count";

            query = query + " RETURN n SKIP " + skip + " LIMIT " + limit;

            List<MemberNode> nodes = new ArrayList<>();
            session.run(query).list().forEach(result -> {
                result.asMap().forEach((k, v) -> {
                    MemberNode node = getValues((Node) v);
                    node.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(node.getProperties(), Map.class)), node.getLabels().get(0)), Map.class));
                    nodes.add(node);
                });
            });


            return MembersCountContentResponse.builder().content(nodes)
                    .count((Long) session.run(countQuery).list().get(0).asMap().get("count"))
                    .build();
        } catch (Exception e) {
            throw new GetMembersError(e.getMessage());
        }

    }

    @SneakyThrows
    public MembersCountContentResponse getMembersWithFilters(MemberFiltersDTO filter) {
        try {

            Session session = driver.session();
            Gson gson = new Gson();
            List<MemberNode> memberNodes = new ArrayList<>();
            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s:" + filter.getType() + ") ");
            queryBuilder.append(sourcePropertiesToWhereClause(filter.getExpressions()));
            String countQuery = queryBuilder + " RETURN count(s) as count";
            queryBuilder.append("RETURN s ");
            queryBuilder.append("SKIP " + (filter.getPage() * filter.getSize()) + " LIMIT " + filter.getSize());
            session.run(queryBuilder.toString()).forEachRemaining(result -> {
                result.asMap().forEach((k, v) -> {
                    MemberNode memberNode = null;
                    if (k.equals("s")) {
                        memberNode = memberRelationshipRepository.getValues((Node) v);
                        memberNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(memberNode.getProperties(), Map.class)), memberNode.getLabels().get(0)), Map.class));
                    }
                    memberNodes.add(memberNode);
                });
            });
            return MembersCountContentResponse.builder().content(memberNodes)
                    .count((Long) session.run(countQuery).list().get(0).asMap().get("count"))
                    .build();

        } catch (Exception e) {
            throw new GetMembersError(e.getMessage());
        }
    }

    @SneakyThrows
    public MembersCountContentResponse getMembersByType(MemberFilterDTO filter) {
        try {

            Session session = driver.session();
            Gson gson = new Gson();
            List<MemberNode> memberNodes = new ArrayList<>();
            StringBuilder queryBuilder = new StringBuilder("MATCH ");
            queryBuilder.append("(s:" + filter.getSourceType() + ") ");
            queryBuilder.append(memberRelationshipRepository.sourceTargetPropertiesToWhereClause(filter.getAttributes(), new HashMap<>(), new HashMap<>()));
            String countQuery = queryBuilder + " RETURN count(s) as count";
            queryBuilder.append("RETURN s ");
            queryBuilder.append("SKIP " + (filter.getPageable().getPageNumber() * filter.getPageable().getPageSize()) + " LIMIT " + filter.getPageable().getPageSize());
            session.run(queryBuilder.toString()).forEachRemaining(result -> {
                result.asMap().forEach((k, v) -> {
                    MemberNode memberNode = null;
                    if (k.equals("s")) {
                        memberNode = memberRelationshipRepository.getValues((Node) v);
                        memberNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(memberNode.getProperties(), Map.class)), memberNode.getLabels().get(0)), Map.class));
                    }
                    memberNodes.add(memberNode);
                });
            });
            return MembersCountContentResponse.builder().content(memberNodes)
                    .count((Long) session.run(countQuery).list().get(0).asMap().get("count"))
                    .build();

        } catch (Exception e) {
            throw new GetMembersError(e.getMessage());
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
                    whereClause.append("AND s." + k + " " + expression.getSymbol().getSymbol()+ "  localdatetime('" + expression.getValue() + "') ");
                } else {
                    whereClause.append("AND s." + k + " " + expression.getSymbol().getSymbol() + "  '" + expression.getValue() + "' ");
                }
            } else {
                whereClause.append("AND s." + k + " " + expression.getSymbol().getSymbol() + "  " + expression.getValue() + " ");

            }
        }

        return whereClause.toString().replace("WHERE AND", "WHERE ");
    }


}

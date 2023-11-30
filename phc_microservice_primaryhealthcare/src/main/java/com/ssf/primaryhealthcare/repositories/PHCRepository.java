package com.ssf.primaryhealthcare.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.ssf.primaryhealthcare.dtos.PHCWithRelationships;
import com.ssf.primaryhealthcare.exceptions.GetError;
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
public class PHCRepository {

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    @Autowired
    private Driver driver;

    public PHCRepository(Driver driver) {
        this.driver = driver;
    }

    @SneakyThrows
    public List<PHCNode> getPHCNode(String id) {
        try {
            Gson gson = new Gson();
            Session session = driver.session();
            String query = "MATCH (n) WHERE n.uuid = '" + id + "' RETURN n ";
            List<PHCNode> PHCNodes = new ArrayList<>();
            session.run(query).forEachRemaining(r -> {
                PHCNode PHCNode = getValues(r.get("n").asNode());
                PHCNode.setProperties(gson.fromJson(Utils.joltTransform(gson.toJson(mapper.convertValue(PHCNode.getProperties(), Map.class)), PHCNode.getLabels().get(0)), Map.class));
                PHCNodes.add(PHCNode);
            });
            return PHCNodes;
        } catch (Exception e) {
            throw new GetError(e.getMessage());
        }
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


}

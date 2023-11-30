package com.ssf.primaryhealthcare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.primaryhealthcare.dtos.PHCNodeDTO;
import com.ssf.primaryhealthcare.dtos.RelationshipDTO;
import com.ssf.primaryhealthcare.exceptions.CreateRelationshipError;
import com.ssf.primaryhealthcare.model.NodeType;
import com.ssf.primaryhealthcare.model.RelationshipType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class TestPHCNodeService {
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    private IPHCService service;


    @Test
    public void testCreateRelationship() {
        RelationshipDTO relationshipDTO = RelationshipDTO.builder()
                .type(RelationshipType.COMPLAINTSOF)
                .properties(new HashMap<>())
                .source(PHCNodeDTO.builder().type(NodeType.Citizen).properties(new HashMap<>() {{
                    put("uuid", "random-uuid");
                }}).build())
                .target(PHCNodeDTO.builder().type(NodeType.Symptom).properties(new HashMap<>() {{
                    put("uuid", "random-uuid-1");
                }}).build())
                .build();
        assertThrows(CreateRelationshipError.class, () -> {
            service.createRelationship(relationshipDTO);
        });
    }


}

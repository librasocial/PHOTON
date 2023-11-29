package com.ssf.primaryhealthcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.primaryhealthcare.dtos.PHCNodeDTO;
import com.ssf.primaryhealthcare.dtos.RelationshipDTO;
import com.ssf.primaryhealthcare.dtos.RelationshipPatchDTO;
import com.ssf.primaryhealthcare.model.NodeType;
import com.ssf.primaryhealthcare.model.RelationshipType;
import com.ssf.primaryhealthcare.service.IPHCService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PrimaryHealthCareController.class)
@ActiveProfiles("test")
public class TestPrimaryHealthCareController {

    @MockBean
    private IPHCService service;

    @Autowired
    private MockMvc mockMvc;

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateRelationship() throws Exception {
        PHCNodeDTO source = new PHCNodeDTO();
        source.setType(NodeType.Citizen);
        source.setProperties(new HashMap<>());

        PHCNodeDTO target = new PHCNodeDTO();
        target.setType(NodeType.Symptom);
        target.setProperties(new HashMap<>());

        RelationshipDTO relationshipDTO = new RelationshipDTO();
        relationshipDTO.setType(RelationshipType.COMPLAINTSOF);
        relationshipDTO.setSource(source);
        relationshipDTO.setTarget(target);
        relationshipDTO.setProperties(new HashMap<>());

        mockMvc.perform(post("/primaryhealthcare/relationship")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(relationshipDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testRelationshipPatch() throws Exception {

        RelationshipPatchDTO relationshipPatchDTO = RelationshipPatchDTO.builder().type(RelationshipType.COMPLAINTSOF).properties(new HashMap<>()).build();

        mockMvc.perform(patch("/primaryhealthcare/relationship/random-uuid")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(relationshipPatchDTO)))
                .andExpect(status().isOk());
    }


}

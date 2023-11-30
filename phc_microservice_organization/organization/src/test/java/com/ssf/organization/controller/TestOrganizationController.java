package com.ssf.organization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.organization.dtos.NodeFilterInput;
import com.ssf.organization.dtos.OrganizationDTO;
import com.ssf.organization.model.OrgType;
import com.ssf.organization.service.IOrgService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka
@ActiveProfiles("test")
public class TestOrganizationController {

    @MockBean
    private IOrgService service;

    @Autowired
    private MockMvc mockMvc;

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //@Test
    void testCreateOrg() throws Exception {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setType(OrgType.Country);
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "Test Node");
        dto.setProperties(properties);

        mockMvc.perform(post("/v1/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isOk());
    }

    //@Test
    void testCreateOrgWithIncorrecctType() throws Exception {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setProperties(new HashMap<>());

        mockMvc.perform(post("/v1/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andExpect(status().is4xxClientError());
    }

    //@Test
    void testGetOrgByID() throws Exception {

        mockMvc.perform(get("/v1/organizations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //@Test
    void testGetOrgFilters() throws Exception {

        mockMvc.perform(get("/v1/organizations/filter?page=0&size=10&rel=e&value=Dharmaiahnapalya&type=VILLAGE&key=name")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //@Test
    void testGetOrgRelationshipFilters() throws Exception {

        mockMvc.perform(get("/v1/organizations/relationships/filter?page=0&rel=CONTAINEDINPLACE&size=1&targetType=STATE&srcNodeId=44")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //@Test
    void testGetOrgRelationshipFiltersOnlyRel() throws Exception {

        mockMvc.perform(get("/v1/organizations/relationships/filter?page=0&rel=CONTAINEDINPLACE&size=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //@Test
    void testGetOrgRelationshipFiltersWithoutRel() throws Exception {

        mockMvc.perform(get("/v1/organizations/relationships/filter?page=0&size=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetOrgRelationshipFiltersWithoutRel1() throws Exception {

        mockMvc.perform(get("/v1/organizations/relationships/filter?rel=AB&page=0&size=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetOrgRelationshipFiltersWithoutRel2() throws Exception {

        mockMvc.perform(get("/v1/organizations/relationships/filter?rel=AB&page=0&size=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetListOfPHCs() throws Exception {
        mockMvc.perform(get("/PHCs?page=0&size=25")).andExpect(status().isOk());

    }

    @Test
    void testGetOrganizationWithRelationship() throws Exception {
        mockMvc.perform(get("/organizations/relationships/filter?srcType=State&rel=CONTAINEDINPLACE&targetType=Country")).andExpect(status().isOk());

    }

    @Test
    void testOrganizationFilter() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        NodeFilterInput dto = new NodeFilterInput();
        dto.setType(OrgType.HouseHold);
        dto.setPage(0);
        dto.setSize(5);
        dto.setProperties(properties);

        mockMvc.perform(post("/organizations/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isOk());

    }

}

package com.ssf.membership.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.membership.dtos.*;
import com.ssf.membership.model.MemberType;
import com.ssf.membership.model.RelationshipType;
import com.ssf.membership.service.IMemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(MemberController.class)
@ActiveProfiles("test")
@EmbeddedKafka
public class TestMemberController {

    @MockBean
    private IMemberService service;

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
    void testCreateMember() throws Exception {
        MembersDTO memberDTO = new MembersDTO();

        memberDTO.setType(MemberType.AshaWorker);
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "TestAshaWorker");
        memberDTO.setProperties(properties);

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("IdToken", "")
                        .content(asJsonString(memberDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetByMemberID() throws Exception {

        mockMvc.perform(get("/members/82e365a8-d25e-4ed3-bb79-f8ee95dc57ac")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetWithoutMemberID() throws Exception {

        mockMvc.perform(get("/members/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetMemberByFilterValues() throws Exception {

        mockMvc.perform(get("/members/filter?type=Citizen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMemberByFilterWithKeyValues() throws Exception {

        mockMvc.perform(get("/members/filter?type=Citizen&key=gender&value=Male")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMembersRelationships() throws Exception {

        mockMvc.perform(get("/members/random-uuid/relationships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMemberByFilterValuesWithWrongType() throws Exception {

        mockMvc.perform(get("/members/filter?type=Citi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetMemberByFilterWithRelationships() throws Exception {

        mockMvc.perform(get("/members/relationships/filter?srcNodeId=70701f2e-ccb0-47d4-991b-a8f9147a487c&targetType=Citizen&rel=RESIDESIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMemberByFilterWithoutRelationships() throws Exception {

        mockMvc.perform(get("/members/relationships/filter?srcNodeId=70701f2e-ccb0-47d4-991b-a8f9147a487c6&targetType=Citizen&rel=Goo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testUpdateMember() throws Exception {
        MembersDTO memberDTO = new MembersDTO();

        memberDTO.setType(MemberType.AshaWorker);
        Map<String, Object> properties = new HashMap<>();
        properties.put("type", "AshaWorker");
        memberDTO.setProperties(properties);

        mockMvc.perform(patch("/members/82e365a8-d25e-4ed3-bb79-f8ee95dc57ac")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchCitizensByName() throws Exception {
        mockMvc.perform(get("/members/search/?query=su&type=NAME")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchCitizensByHealthID() throws Exception {
        mockMvc.perform(get("/members/search/?query=123456789123&type=UHID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchCitizensByContact() throws Exception {
        mockMvc.perform(get("/members/search/?query=9999888877&type=CONTACT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchCitizensByDateOfBirth() throws Exception {
        mockMvc.perform(get("/members/search/?query=28-10-2001&type=DOB")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    void testCreateRelationship() throws Exception {
        Member source = new Member();
        source.setType(MemberType.Citizen);
        source.setProperties(new HashMap<>());

        Member target = new Member();
        target.setType(MemberType.HouseHold);
        target.setProperties(new HashMap<>());

        RelationshipDTO relationshipDTO = new RelationshipDTO();
        relationshipDTO.setType(RelationshipType.RESIDESIN);
        relationshipDTO.setSource(source);
        relationshipDTO.setTarget(target);
        relationshipDTO.setProperties(new HashMap<>());

        mockMvc.perform(post("/members/ac04c89f-78c3-43ba-9c9f-5c874c6b9a61/relationships")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(relationshipDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testMemberSearch() throws Exception {

        NodeFilterInput filterInput = NodeFilterInput.builder()
                .page(0).size(5).properties(new HashMap<>()).type(MemberType.Citizen).build();

        mockMvc.perform(post("/members/search?query=s&type=NAME")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(filterInput)))
                .andExpect(status().isOk());
    }

    @Test
    void testMemberGrouping() throws Exception {
        GroupingDTO groupingDTO = GroupingDTO.builder().field("").stepCount(1).source(Member.builder().type(MemberType.HouseHold).properties(new HashMap<>()).build()).target(Member.builder().type(MemberType.Village).properties(new HashMap<>() {{
            put("uuid", "random-uuid");
        }}).build()).relationship(RelationshipType.CONTAINEDINPLACE).build();

        mockMvc.perform(post("/members/relationships/grouping")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(groupingDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testRelationshipPatch() throws Exception {

        RelationshipPatchDTO relationshipPatchDTO = RelationshipPatchDTO.builder().type(RelationshipType.RESIDESIN).properties(new HashMap<>()).build();

        mockMvc.perform(patch("/members/relationships/random-uuid")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(relationshipPatchDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPreFetchedUrl() throws Exception {

        mockMvc.perform(get("/members/getprefetchedurl?bucketKey=test"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetImageUrl() throws Exception {

        mockMvc.perform(get("/members/getimageurl?bucketKey=test"))
                .andExpect(status().isOk());
    }

}

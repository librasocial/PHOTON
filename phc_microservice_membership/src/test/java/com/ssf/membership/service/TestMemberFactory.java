package com.ssf.membership.service;

import com.ssf.membership.model.MemberNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
public class TestMemberFactory {

    @Test
    public void testGetMembersAshaWorkerDTO() {
        MemberNode memberDTO = getMemberDTO("AshaWorker");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "TestAshaWorker");
        map.put("contact", "900800760");
        map.put("phc", "Sugganahalli Rural PHC");
        map.put("phc_sub_center", "Sugganahalli Sub-Center (SC-01)");
        map.put("worker_id", "02");
        memberDTO.setProperties(map);
        List<Object> resultAshaWorker = new MemberFactory(memberDTO).getMemberDto();
        assertNotNull(resultAshaWorker);
    }

    @Test
    public void testGetMembersCitizenDTO() {
        MemberNode memberDTO = getMemberDTO("Citizen");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "TestCitizen");
        map.put("contact", "900800760");
        map.put("is_head", true);
        map.put("gender", "Female");
        map.put("caste", "general");
        map.put("age", 29);
        memberDTO.setProperties(map);
        List<Object> resultCitizen = new MemberFactory(memberDTO).getMemberDto();
        assertNotNull(resultCitizen);
    }

    @Test
    public void testGetMembersJuniorHealthAssistantFemaleDTO() {
        MemberNode memberDTO = getMemberDTO("JuniorHealthAssistantFemale");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "TestJuniorHealthAssistantFemale");
        map.put("contact", "900800760");
        memberDTO.setProperties(map);
        List<Object> resultJuniorHealthAssistantFemale = new MemberFactory(memberDTO).getMemberDto();
        assertNotNull(resultJuniorHealthAssistantFemale);
    }

    @Test
    public void testGetMembersJuniorHealthAssistantMaleDTO() {
        MemberNode memberDTO = getMemberDTO("JuniorHealthAssistantMale");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "TestJuniorHealthAssistantMale");
        map.put("contact", "900800760");
        memberDTO.setProperties(map);
        List<Object> resultJuniorHealthAssistantMale = new MemberFactory(memberDTO).getMemberDto();
        assertNotNull(resultJuniorHealthAssistantMale);
    }

    @Test
    public void testGetMembersNullDTO() {
        MemberNode memberDTO = getMemberDTO("Test");
        List<Object> resultNull = new MemberFactory(memberDTO).getMemberDto();
        assertNull(resultNull);
    }

    private MemberNode getMemberDTO(String type) {
        MemberNode memberDTO = new MemberNode();
        List<String> typeList = new ArrayList<>();
        typeList.add(type);
        memberDTO.setLabels(typeList);
        return memberDTO;
    }

}

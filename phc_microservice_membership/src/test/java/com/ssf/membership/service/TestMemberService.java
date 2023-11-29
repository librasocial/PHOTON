package com.ssf.membership.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.membership.dtos.*;
import com.ssf.membership.dtos.Member;
import com.ssf.membership.entities.*;
import com.ssf.membership.exceptions.CreateRelationshipError;
import com.ssf.membership.model.MemberType;
import com.ssf.membership.model.RelationshipType;
import com.ssf.membership.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@EnableKafka
public class TestMemberService {
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    //Create when class
    @MockBean
    private AshaWorkerRepository ashaWorkerRepository;
    @MockBean
    private CitizenRepository citizenRepository;
    @MockBean
    private MedicalOfficerRepository medicalOfficerRepository;
    @MockBean
    private JuniorHealthAssistantFemaleRepository juniorHealthAssistantFemaleRepository;
    @MockBean
    private JuniorHealthAssistantMaleRepository juniorHealthAssistantMaleRepository;
    @MockBean
    private MemberRepository memberRepository;
    @Autowired
    private IMemberService service;

//    @Test
    public void testCreateMemberAsAshaWorker() {
        MembersDTO memberDTO = getMemberDTO(MemberType.AshaWorker);
        Map<String, Object> properties = new HashMap<>();
        memberDTO.setProperties(properties);
        service.createMember(memberDTO);
    }

    //    @Test
    public void testCreateMemberAsCitizen() {
        MembersDTO memberDTO = getMemberDTO(MemberType.Citizen);
        Map<String, Object> properties = new HashMap<>();
        properties.put("houseHold", "2f34e213-b09b-4188-9ea9-ed3feadcd259");
        memberDTO.setProperties(properties);
        service.createMember(memberDTO);
    }

//    @Test
    public void testCreateMemberAsJuniorHealthAssistantFemale() {
        MembersDTO memberDTO = getMemberDTO(MemberType.JuniorHealthAssistantFemale);
        Map<String, Object> properties = new HashMap<>();
        memberDTO.setProperties(properties);
        service.createMember(memberDTO);
    }

//    @Test
    public void testCreateMemberAsJuniorHealthAssistantMale() {
        MembersDTO memberDTO = getMemberDTO(MemberType.JuniorHealthAssistantMale);
        Map<String, Object> properties = new HashMap<>();
        memberDTO.setProperties(properties);
        service.createMember(memberDTO);
    }

//    @Test
    public void testCreateMemberAsMedicalOfficer() {
        MembersDTO memberDTO = getMemberDTO(MemberType.MedicalOfficer);
        Map<String, Object> properties = new HashMap<>();
        memberDTO.setProperties(properties);
        service.createMember(memberDTO);
    }

//    @Test
    public void testGetMemberFilterAllAshaWorker() {
        List<AshaWorker> resultAshaWorker = new ArrayList();
        MemberFilterDTO memberFilterDTO = getMemberFilterDTO();
        memberFilterDTO.setSourceType(MemberType.AshaWorker);
        Mockito.when(ashaWorkerRepository.findAll(memberFilterDTO.getPageable())).thenReturn(
                new PageImpl(resultAshaWorker, memberFilterDTO.getPageable(), resultAshaWorker.size())
        );
        MemberPageResponse result = service.getMembers(memberFilterDTO);
        assertNotNull(result);
    }

//    @Test
    public void testGetMemberFilterAshaWorker() {
        List<AshaWorker> resultAshaWorker = new ArrayList();
        MemberFilterDTO memberFilterDTO = getMemberFilterDTO();
        memberFilterDTO.setSourceType(MemberType.AshaWorker);
        memberFilterDTO.setAttributes(new HashMap<>());
        AshaWorker ashaWorkerArritbuteObject = mapper.convertValue(memberFilterDTO.getAttributes(), AshaWorker.class);
        Example<AshaWorker> exampleAshaWorker = Example.of(ashaWorkerArritbuteObject);
        Mockito.when(ashaWorkerRepository.findAll(exampleAshaWorker, memberFilterDTO.getPageable())).thenReturn(
                new PageImpl(resultAshaWorker, memberFilterDTO.getPageable(), resultAshaWorker.size())
        );
        MemberPageResponse result = service.getMembers(memberFilterDTO);
        assertNotNull(result);
    }

//    @Test
    public void testGetMemberFilterAllCitizen() {
        List<Citizen> resultCitizen = new ArrayList();
        MemberFilterDTO memberFilterDTO = getMemberFilterDTO();
        memberFilterDTO.setSourceType(MemberType.Citizen);
        Mockito.when(citizenRepository.findAll(memberFilterDTO.getPageable())).thenReturn(
                new PageImpl(resultCitizen, memberFilterDTO.getPageable(), resultCitizen.size())
        );
        MemberPageResponse result = service.getMembers(memberFilterDTO);
        assertNotNull(result);
    }

//    @Test
    public void testGetMemberFilterCitizen() {
        List<Citizen> resultCitizen = new ArrayList();
        MemberFilterDTO memberFilterDTO = getMemberFilterDTO();
        memberFilterDTO.setSourceType(MemberType.Citizen);
        memberFilterDTO.setAttributes(new HashMap<>());
        Citizen citizenArritbuteObject = mapper.convertValue(memberFilterDTO.getAttributes(), Citizen.class);
        Example<Citizen> exampleCitizen = Example.of(citizenArritbuteObject);
        Mockito.when(citizenRepository.findAll(exampleCitizen, memberFilterDTO.getPageable())).thenReturn(
                new PageImpl(resultCitizen, memberFilterDTO.getPageable(), resultCitizen.size())
        );
        MemberPageResponse result = service.getMembers(memberFilterDTO);
        assertNotNull(result);
    }

//    @Test
    public void testGetMemberFilterAllHeadMedicalOfficer() {
        List<Citizen> resultMedicalOfficer = new ArrayList();
        MemberFilterDTO memberFilterDTO = getMemberFilterDTO();
        memberFilterDTO.setSourceType(MemberType.MedicalOfficer);
        Mockito.when(medicalOfficerRepository.findAll(memberFilterDTO.getPageable())).thenReturn(
                new PageImpl(resultMedicalOfficer, memberFilterDTO.getPageable(), resultMedicalOfficer.size())
        );
        MemberPageResponse result = service.getMembers(memberFilterDTO);
        assertNotNull(result);
    }

//    @Test
    public void testGetMemberFilterHeadMedicalOfficer() {
        List<Citizen> resultMedicalOfficer = new ArrayList();
        MemberFilterDTO memberFilterDTO = getMemberFilterDTO();
        memberFilterDTO.setSourceType(MemberType.MedicalOfficer);
        memberFilterDTO.setAttributes(new HashMap<>());
        MedicalOfficer medicalOfficerAttributedObject = mapper.convertValue(memberFilterDTO.getAttributes(), MedicalOfficer.class);
        Example<MedicalOfficer> exampleHeadMedicalOfficer = Example.of(medicalOfficerAttributedObject);
        Mockito.when(medicalOfficerRepository.findAll(exampleHeadMedicalOfficer, memberFilterDTO.getPageable())).thenReturn(
                new PageImpl(resultMedicalOfficer, memberFilterDTO.getPageable(), resultMedicalOfficer.size())
        );
        MemberPageResponse result = service.getMembers(memberFilterDTO);
        assertNotNull(result);
    }

//    @Test
    public void testGetMemberRelationshipResidesIn() {
        MemberFilterDTO memberFilterDTO = getMemberFilterDTO();
        memberFilterDTO.setRelationshipType(RelationshipType.RESIDESIN);
        memberFilterDTO.setTargetType(MemberType.Citizen);
        memberFilterDTO.setSourceId("random-uuid");
        memberFilterDTO.setRelationshipStepCount(1);
        MemberPageResponse result = service.getMemberAndRelationships(memberFilterDTO, 0, 25);
        assertNotNull(result);
    }

//    @Test
    public void testGetMemberRelationshipMemberOf() {
        MemberFilterDTO memberFilterDTO = getMemberFilterDTO();
        memberFilterDTO.setRelationshipType(RelationshipType.MEMBEROF);
        memberFilterDTO.setTargetType(MemberType.SubCenter);
        memberFilterDTO.setSourceId("random-uuid");
        memberFilterDTO.setRelationshipStepCount(1);
        MemberPageResponse result = service.getMemberAndRelationships(memberFilterDTO, 0, 25);
        assertNotNull(result);
    }

    private MemberFilterDTO getMemberFilterDTO() {
        PageRequest page = PageRequest.of(1, 1);
        MemberFilterDTO filter = MemberFilterDTO.builder().pageable(page).build();
        return filter;
    }

    @Test
    public void testUpdateAshaWorker() {
        when(ashaWorkerRepository.findByUuid("random-uuid")).thenReturn(Optional.of(getAshWorker()));
        when(ashaWorkerRepository.save(any())).thenReturn(getAshWorker());
        MembersDTO dto = getMemberDTO(MemberType.AshaWorker);
        Map<String, Object> result = service.updateMember(dto, "random-uuid");

        assertNotNull(result);
    }


    @Test
    public void testUpdateJuniorHealthAssistantFemale() {
        when(juniorHealthAssistantFemaleRepository.findByUuid("random-uuid")).thenReturn(Optional.of(getJuniorHealthAssistantFemale()));
        when(juniorHealthAssistantFemaleRepository.save(any())).thenReturn(getJuniorHealthAssistantFemale());
        MembersDTO dto = getMemberDTO(MemberType.JuniorHealthAssistantFemale);
        Map<String, Object> result = service.updateMember(dto, "random-uuid");
        Assertions.assertNotNull(result);
    }

    @Test
    public void testUpdateJuniorHealthAssistantMale() {
        when(juniorHealthAssistantMaleRepository.findByUuid("random-uuid")).thenReturn(Optional.of(getJuniorHealthAssistantMale()));
        when(juniorHealthAssistantMaleRepository.save(any())).thenReturn(getJuniorHealthAssistantMale());
        MembersDTO dto = getMemberDTO(MemberType.JuniorHealthAssistantMale);
        Map<String, Object> result = service.updateMember(dto, "random-uuid");
        Assertions.assertNotNull(result);
    }

//    @Test
    public void testUpdateRelationship() {
        RelationshipPatchDTO patchDTO = RelationshipPatchDTO.builder().type(RelationshipType.MARRIEDTO).properties(new HashMap<>() {{
            put("uuid", "random-uuid-1");
        }}).build();
        MemberPageResponse response = service.updateRelationship("random-uuid", patchDTO);
        Assertions.assertNotNull(response);
    }

//    @Test
    public void testGetRelationshipsGrouping() {
        GroupingDTO groupingDTO = GroupingDTO.builder().relationship(RelationshipType.RESIDESIN)
                .source(Member.builder().type(MemberType.Citizen).properties(new HashMap<>()).build())
                .target(Member.builder().type(MemberType.HouseHold).properties(new HashMap<>()).build())
                .stepCount(1)
                .field("")
                .build();
        MemberPageResponse result = service.getRelationshipsGrouping(groupingDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testCreateRelationship() {
        RelationshipDTO relationshipDTO = RelationshipDTO.builder()
                .type(RelationshipType.MARRIEDTO)
                .properties(new HashMap<>())
                .source(Member.builder().type(MemberType.Citizen).properties(new HashMap<>() {{
                    put("uuid", "random-uuid");
                }}).build())
                .target(Member.builder().type(MemberType.Citizen).properties(new HashMap<>() {{
                    put("uuid", "random-uuid-1");
                }}).build())
                .build();
        assertThrows(CreateRelationshipError.class, () -> {
            service.createRelationship(relationshipDTO);
        });
    }

    private Citizen getCitizen() {
        Citizen citizen = new Citizen();
        citizen.setUuid(UUID.randomUUID().toString());
        citizen.setFirstName("Test");

        return citizen;
    }

    private AshaWorker getAshWorker() {
        AshaWorker ashaWorker = new AshaWorker();
        ashaWorker.setUuid(UUID.randomUUID().toString());
        ashaWorker.setName("Test");

        return ashaWorker;
    }

    private MedicalOfficer getHeadMedicalOfficer() {
        MedicalOfficer medicalOfficer = new MedicalOfficer();
        medicalOfficer.setUuid(UUID.randomUUID().toString());
        medicalOfficer.setName("Test");

        return medicalOfficer;
    }

    private JuniorHealthAssistantFemale getJuniorHealthAssistantFemale() {
        JuniorHealthAssistantFemale juniorHealthAssistantFemale = new JuniorHealthAssistantFemale();
        juniorHealthAssistantFemale.setUuid(UUID.randomUUID().toString());
        juniorHealthAssistantFemale.setName("Test");

        return juniorHealthAssistantFemale;
    }

    private JuniorHealthAssistantMale getJuniorHealthAssistantMale() {
        JuniorHealthAssistantMale juniorHealthAssistantMale = new JuniorHealthAssistantMale();
        juniorHealthAssistantMale.setUuid(UUID.randomUUID().toString());
        juniorHealthAssistantMale.setName("Test");

        return juniorHealthAssistantMale;
    }


    private MembersDTO getMemberDTO(MemberType type) {
        MembersDTO memberDTO = new MembersDTO();

        memberDTO.setType(type);
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "TestAshaWorker");
        memberDTO.setProperties(properties);
        return memberDTO;
    }
}

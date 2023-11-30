package com.ssf.membership.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.ssf.membership.dtos.*;
import com.ssf.membership.entities.*;
import com.ssf.membership.exceptions.CreateUpdateMembersError;
import com.ssf.membership.exceptions.GetMembersError;
import com.ssf.membership.kafka.producer.client.KafkaProducerClient;
import com.ssf.membership.kafka.producer.model.Actor;
import com.ssf.membership.kafka.producer.model.Context;
import com.ssf.membership.kafka.producer.model.KafkaMessage;
import com.ssf.membership.kafka.topics.Topics;
import com.ssf.membership.model.*;
import com.ssf.membership.repositories.*;
import com.ssf.membership.utils.MembershipConstants;
import com.ssf.membership.utils.Utils;
import lombok.SneakyThrows;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@EnableNeo4jAuditing
public class MemberService implements IMemberService {
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();
    @Autowired
    HttpServletRequest request;
    private Gson gson = new Gson();
    @Autowired
    private AshaWorkerRepository ashaWorkerRepository;
    @Autowired
    private CitizenRepository citizenRepository;
    @Autowired
    private KafkaProducerClient producerClient;
    @Autowired
    private MedicalOfficerRepository medicalOfficerRepository;
    @Autowired
    private JuniorHealthAssistantFemaleRepository juniorHealthAssistantFemaleRepository;
    @Autowired
    private JuniorHealthAssistantMaleRepository juniorHealthAssistantMaleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberRelationshipRepository memberRelationshipRepository;
    @Autowired
    private HouseHoldRepository houseHoldRepository;
    @Autowired
    private JuniorHealthAssistantRepository juniorHealthAssistantRepository;
    @Autowired
    private PhcAdministratorRepository phcAdministratorRepository;
    @Autowired
    private JuniorLabTechnicianRepository juniorLabTechnicianRepository;
    @Autowired
    private PhcRepository phcRepository;
    @Autowired
    private SubCenterRepository subCenterRepository;
    @Autowired
    private GramPanchayatRepository gramPanchayatRepository;
    @Autowired
    private GPMemberRepository gpMemberRepository;
    @Autowired
    private DGroupAttenderRepository dGroupAttenderRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private JuniorPharmacistRepository juniorPharmacistRepository;
    @Autowired
    private HeadMedicalOfficerRepository headMedicalOfficerRepository;
    @Autowired
    private HealthInspectingOfficerRepository healthInspectingOfficerRepository;
    @Autowired
    private PrimaryHealthCareOfficerRepository primaryHealthCareOfficerRepository;
    @Autowired
    private StaffNurseRepository staffNurseRepository;
    @Autowired
    private SeniorPrimaryHealthCareOfficerRepository seniorPrimaryHealthCareOfficerRepository;
    @Autowired
    private SeniorHealthInspectingOfficerRepository seniorHealthInspectingOfficerRepository;
    @Autowired
    private BlockHealthEducationOfficerRepository blockHealthEducationOfficerRepository;
    @Autowired
    private JuniorParaMedicalOphthalmicAssistantRepository juniorParaMedicalOphthalmicAssistantRepository;
    @Autowired
    private FirstDivisionalAssistantRepository firstDivisionalAssistantRepository;
    @Autowired
    private SecondDivisionalAssistantRepository secondDivisionalAssistantRepository;
    @Autowired
    private JuniorHealthInspectingOfficerRepository juniorHealthInspectingOfficerRepository;
    @Autowired
    private JuniorPrimaryHealthCareOfficerRepository juniorPrimaryHealthCareOfficerRepository;
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private Driver driver;


    @Override
    public MemberPageResponse getMemberById(String id) {
        MemberPageResponse response;
        List<MemberNode> result = memberRepository.getMembers(id);
        if (result != null && result.size() > 0) {
            response = MemberPageResponse.builder().totalElements(Long.valueOf(result.size())).totalPages(1L).content(result)
                    .build();
        } else {
            response = MemberPageResponse.builder().totalElements(0L).totalPages(0L).content(new ArrayList<>())
                    .build();
        }
        return response;

    }

    @Override
    @SneakyThrows
    public Map<String, Object> createMember(MembersDTO member) {
        return persistMembers(member, null);
    }

    @Override
    @SneakyThrows
    public Map<String, Object> updateMember(MembersDTO member, String id) {
        return persistMembers(member, id);
    }

    @Override
    public MemberPageResponse getMembers(MemberFilterDTO filter) {
        MembersCountContentResponse result = memberRepository.getMembersByType(filter);
        return MemberPageResponse.builder().totalElements(result.getCount())
                .totalPages(Utils.getPagesByElementsAndSize(result.getCount(), filter.getPageable().getPageSize()))
                .content(result.getContent()).build();
    }

    @Override
    public MemberPageResponse getRelationshipsByMemberById(String id, Integer page, Integer size) {
        Long totalElements = memberRelationshipRepository.getCountOfRelationshipsByMemberID(id);
        MemberPageResponse resposne = MemberPageResponse.builder().totalElements(totalElements).totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).content(memberRepository.getRelationshipsByMemberID(id, page, size)).build();
        return resposne;
    }

    @Override
    public MemberPageResponse getMemberAndRelationships(MemberFilterDTO filter, Integer page, Integer size) {

        return memberRelationshipRepository.getMemberAndRelationships(filter, page, size);
    }

    @SneakyThrows
    private Map<String, Object> persistMembers(MembersDTO member, String id) {
        IMemberEntity memberPersisted = null;
        Map<String, Object> properties = updateAudits(member.getProperties(), id);
        switch (member.getType()) {
            case AshaWorker:
                try {
                    checkPHC(properties, true);
                    checkSubCenter(properties, false);
                    if (id != null) {
                        Optional<AshaWorker> dbValue = ashaWorkerRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        AshaWorker data = mapper.convertValue(dbMap, AshaWorker.class);
                        memberPersisted = ashaWorkerRepository.save(data);
                    } else {
                        AshaWorker data = mapper.convertValue(properties, AshaWorker.class);
                        memberPersisted = ashaWorkerRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((AshaWorker) memberPersisted).getUuid()).srcType(MemberType.AshaWorker)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                        if (properties.containsKey("subCenter")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((AshaWorker) memberPersisted).getUuid()).srcType(MemberType.AshaWorker)
                                    .type(RelationshipType.MEMBEROF)
                                    .targetId(properties.get("subCenter").toString()).targetType(MemberType.SubCenter)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case Citizen:
                try {
                    checkHouseHold(properties, true);
                    if (id != null) {
                        Optional<Citizen> dbValue = citizenRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        Citizen data = mapper.convertValue(dbMap, Citizen.class);
                        memberPersisted = citizenRepository.save(data);
                        sendMessage(memberPersisted, Topics.Citizen, "Updated");
                    } else {
                        Citizen data = mapper.convertValue(properties, Citizen.class);
                        memberPersisted = citizenRepository.save(data);
                        if (properties.containsKey("houseHold")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((Citizen) memberPersisted).getUuid()).srcType(MemberType.Citizen)
                                    .type(RelationshipType.RESIDESIN)
                                    .targetId(properties.get("houseHold").toString()).targetType(MemberType.HouseHold)
                                    .build());
                        }
                        sendMessage(memberPersisted, Topics.Citizen, "Created");
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case HeadMedicalOfficer:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<HeadMedicalOfficer> dbValue = headMedicalOfficerRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        HeadMedicalOfficer data = mapper.convertValue(dbMap, HeadMedicalOfficer.class);
                        memberPersisted = headMedicalOfficerRepository.save(data);
                    } else {
                        HeadMedicalOfficer data = mapper.convertValue(properties, HeadMedicalOfficer.class);
                        memberPersisted = headMedicalOfficerRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((HeadMedicalOfficer) memberPersisted).getUuid()).srcType(MemberType.HeadMedicalOfficer)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((HeadMedicalOfficer) memberPersisted).getUuid()).srcType(MemberType.HeadMedicalOfficer)
                                    .type(RelationshipType.MEMBEROF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case HealthInspectingOfficer:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<HealthInspectingOfficer> dbValue = healthInspectingOfficerRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        HealthInspectingOfficer data = mapper.convertValue(dbMap, HealthInspectingOfficer.class);
                        memberPersisted = healthInspectingOfficerRepository.save(data);
                    } else {
                        HealthInspectingOfficer data = mapper.convertValue(properties, HealthInspectingOfficer.class);
                        memberPersisted = healthInspectingOfficerRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((HealthInspectingOfficer) memberPersisted).getUuid()).srcType(MemberType.HealthInspectingOfficer)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((HealthInspectingOfficer) memberPersisted).getUuid()).srcType(MemberType.HealthInspectingOfficer)
                                    .type(RelationshipType.MEMBEROF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case PrimaryHealthCareOfficer:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<PrimaryHealthCareOfficer> dbValue = primaryHealthCareOfficerRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        PrimaryHealthCareOfficer data = mapper.convertValue(dbMap, PrimaryHealthCareOfficer.class);
                        memberPersisted = primaryHealthCareOfficerRepository.save(data);
                    } else {
                        PrimaryHealthCareOfficer data = mapper.convertValue(properties, PrimaryHealthCareOfficer.class);
                        memberPersisted = primaryHealthCareOfficerRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((PrimaryHealthCareOfficer) memberPersisted).getUuid()).srcType(MemberType.PrimaryHealthCareOfficer)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((PrimaryHealthCareOfficer) memberPersisted).getUuid()).srcType(MemberType.PrimaryHealthCareOfficer)
                                    .type(RelationshipType.MEMBEROF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case MedicalOfficer:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<MedicalOfficer> dbValue = medicalOfficerRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        MedicalOfficer data = mapper.convertValue(dbMap, MedicalOfficer.class);
                        memberPersisted = medicalOfficerRepository.save(data);
                    } else {
                        MedicalOfficer data = mapper.convertValue(properties, MedicalOfficer.class);
                        memberPersisted = medicalOfficerRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((MedicalOfficer) memberPersisted).getUuid()).srcType(MemberType.MedicalOfficer)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((MedicalOfficer) memberPersisted).getUuid()).srcType(MemberType.MedicalOfficer)
                                    .type(RelationshipType.MEMBEROF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case JuniorHealthAssistantFemale:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<JuniorHealthAssistantFemale> dbValue = juniorHealthAssistantFemaleRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        JuniorHealthAssistantFemale data = mapper.convertValue(dbMap, JuniorHealthAssistantFemale.class);
                        memberPersisted = juniorHealthAssistantFemaleRepository.save(data);
                    } else {
                        JuniorHealthAssistantFemale data = mapper.convertValue(properties, JuniorHealthAssistantFemale.class);
                        memberPersisted = juniorHealthAssistantFemaleRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((JuniorHealthAssistantFemale) memberPersisted).getUuid()).srcType(MemberType.JuniorHealthAssistantFemale)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case JuniorHealthAssistantMale:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<JuniorHealthAssistantMale> dbValue = juniorHealthAssistantMaleRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        JuniorHealthAssistantMale data = mapper.convertValue(dbMap, JuniorHealthAssistantMale.class);
                        memberPersisted = juniorHealthAssistantMaleRepository.save(data);
                    } else {
                        JuniorHealthAssistantMale data = mapper.convertValue(properties, JuniorHealthAssistantMale.class);
                        memberPersisted = juniorHealthAssistantMaleRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((JuniorHealthAssistantMale) memberPersisted).getUuid()).srcType(MemberType.JuniorHealthAssistantMale)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case JuniorHealthAssistant:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<JuniorHealthAssistant> dbValue = juniorHealthAssistantRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        JuniorHealthAssistant data = mapper.convertValue(dbMap, JuniorHealthAssistant.class);
                        memberPersisted = juniorHealthAssistantRepository.save(data);
                    } else {
                        JuniorHealthAssistant data = mapper.convertValue(properties, JuniorHealthAssistant.class);
                        memberPersisted = juniorHealthAssistantRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((JuniorHealthAssistant) memberPersisted).getUuid()).srcType(MemberType.JuniorHealthAssistant)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case PhcAdministrator:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<PhcAdministrator> dbValue = phcAdministratorRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        PhcAdministrator data = mapper.convertValue(dbMap, PhcAdministrator.class);
                        memberPersisted = phcAdministratorRepository.save(data);
                    } else {
                        PhcAdministrator data = mapper.convertValue(properties, PhcAdministrator.class);
                        memberPersisted = phcAdministratorRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((PhcAdministrator) memberPersisted).getUuid()).srcType(MemberType.PhcAdministrator)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case JuniorPharmacist:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<JuniorPharmacist> dbValue = juniorPharmacistRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        JuniorPharmacist data = mapper.convertValue(dbMap, JuniorPharmacist.class);
                        memberPersisted = juniorPharmacistRepository.save(data);
                    } else {
                        JuniorPharmacist data = mapper.convertValue(properties, JuniorPharmacist.class);
                        memberPersisted = juniorPharmacistRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((JuniorPharmacist) memberPersisted).getUuid()).srcType(MemberType.JuniorPharmacist)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case JuniorLabTechnician:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<JuniorLabTechnician> dbValue = juniorLabTechnicianRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        JuniorLabTechnician data = mapper.convertValue(dbMap, JuniorLabTechnician.class);
                        memberPersisted = juniorLabTechnicianRepository.save(data);
                    } else {
                        JuniorLabTechnician data = mapper.convertValue(properties, JuniorLabTechnician.class);
                        memberPersisted = juniorLabTechnicianRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((JuniorLabTechnician) memberPersisted).getUuid()).srcType(MemberType.JuniorLabTechnician)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case GPMember:
                try {
                    checkGramPanchayat(properties, true);
                    if (id != null) {
                        Optional<GPMember> dbValue = gpMemberRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        GPMember data = mapper.convertValue(dbMap, GPMember.class);
                        memberPersisted = gpMemberRepository.save(data);
                    } else {
                        GPMember data = mapper.convertValue(properties, GPMember.class);
                        memberPersisted = gpMemberRepository.save(data);
                        if (properties.containsKey("gramPanchayat")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((GPMember) memberPersisted).getUuid()).srcType(MemberType.GPMember)
                                    .type(RelationshipType.MEMBEROF)
                                    .targetId(properties.get("gramPanchayat").toString()).targetType(MemberType.GramPanchayat)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case DGroupAttender:
                try {
                    checkPHC(properties, true);
                    if (id != null) {
                        Optional<DGroupAttender> dbValue = dGroupAttenderRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        DGroupAttender data = mapper.convertValue(dbMap, DGroupAttender.class);
                        memberPersisted = dGroupAttenderRepository.save(data);
                    } else {
                        DGroupAttender data = mapper.convertValue(properties, DGroupAttender.class);
                        memberPersisted = dGroupAttenderRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((DGroupAttender) memberPersisted).getUuid()).srcType(MemberType.DGroupAttender)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((DGroupAttender) memberPersisted).getUuid()).srcType(MemberType.DGroupAttender)
                                    .type(RelationshipType.MEMBEROF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case Admin:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<Admin> dbValue = adminRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        Admin data = mapper.convertValue(dbMap, Admin.class);
                        memberPersisted = adminRepository.save(data);
                    } else {
                        Admin data = mapper.convertValue(properties, Admin.class);
                        memberPersisted = adminRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((Admin) memberPersisted).getUuid()).srcType(MemberType.Admin)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case StaffNurse:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<StaffNurse> dbValue = staffNurseRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        StaffNurse data = mapper.convertValue(dbMap, StaffNurse.class);
                        memberPersisted = staffNurseRepository.save(data);
                    } else {
                        StaffNurse data = mapper.convertValue(properties, StaffNurse.class);
                        memberPersisted = staffNurseRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((StaffNurse) memberPersisted).getUuid()).srcType(MemberType.StaffNurse)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case SeniorPrimaryHealthCareOfficer:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<SeniorPrimaryHealthCareOfficer> dbValue = seniorPrimaryHealthCareOfficerRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        SeniorPrimaryHealthCareOfficer data = mapper.convertValue(dbMap, SeniorPrimaryHealthCareOfficer.class);
                        memberPersisted = seniorPrimaryHealthCareOfficerRepository.save(data);
                    } else {
                        SeniorPrimaryHealthCareOfficer data = mapper.convertValue(properties, SeniorPrimaryHealthCareOfficer.class);
                        memberPersisted = seniorPrimaryHealthCareOfficerRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((SeniorPrimaryHealthCareOfficer) memberPersisted).getUuid()).srcType(MemberType.SeniorPrimaryHealthCareOfficer)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case SeniorHealthInspectingOfficer:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<SeniorHealthInspectingOfficer> dbValue = seniorHealthInspectingOfficerRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        SeniorHealthInspectingOfficer data = mapper.convertValue(dbMap, SeniorHealthInspectingOfficer.class);
                        memberPersisted = seniorHealthInspectingOfficerRepository.save(data);
                    } else {
                        SeniorHealthInspectingOfficer data = mapper.convertValue(properties, SeniorHealthInspectingOfficer.class);
                        memberPersisted = seniorHealthInspectingOfficerRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((SeniorHealthInspectingOfficer) memberPersisted).getUuid()).srcType(MemberType.SeniorHealthInspectingOfficer)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case BlockHealthEducationOfficer:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<BlockHealthEducationOfficer> dbValue = blockHealthEducationOfficerRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        BlockHealthEducationOfficer data = mapper.convertValue(dbMap, BlockHealthEducationOfficer.class);
                        memberPersisted = blockHealthEducationOfficerRepository.save(data);
                    } else {
                        BlockHealthEducationOfficer data = mapper.convertValue(properties, BlockHealthEducationOfficer.class);
                        memberPersisted = blockHealthEducationOfficerRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((BlockHealthEducationOfficer) memberPersisted).getUuid()).srcType(MemberType.BlockHealthEducationOfficer)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case JuniorParaMedicalOphthalmicAssistant:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<JuniorParaMedicalOphthalmicAssistant> dbValue = juniorParaMedicalOphthalmicAssistantRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        JuniorParaMedicalOphthalmicAssistant data = mapper.convertValue(dbMap, JuniorParaMedicalOphthalmicAssistant.class);
                        memberPersisted = juniorParaMedicalOphthalmicAssistantRepository.save(data);
                    } else {
                        JuniorParaMedicalOphthalmicAssistant data = mapper.convertValue(properties, JuniorParaMedicalOphthalmicAssistant.class);
                        memberPersisted = juniorParaMedicalOphthalmicAssistantRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((JuniorParaMedicalOphthalmicAssistant) memberPersisted).getUuid()).srcType(MemberType.JuniorParaMedicalOphthalmicAssistant)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case FirstDivisionalAssistant:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<FirstDivisionalAssistant> dbValue = firstDivisionalAssistantRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        FirstDivisionalAssistant data = mapper.convertValue(dbMap, FirstDivisionalAssistant.class);
                        memberPersisted = firstDivisionalAssistantRepository.save(data);
                    } else {
                        FirstDivisionalAssistant data = mapper.convertValue(properties, FirstDivisionalAssistant.class);
                        memberPersisted = firstDivisionalAssistantRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((FirstDivisionalAssistant) memberPersisted).getUuid()).srcType(MemberType.FirstDivisionalAssistant)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case SecondDivisionalAssistant:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<SecondDivisionalAssistant> dbValue = secondDivisionalAssistantRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        SecondDivisionalAssistant data = mapper.convertValue(dbMap, SecondDivisionalAssistant.class);
                        memberPersisted = secondDivisionalAssistantRepository.save(data);
                    } else {
                        SecondDivisionalAssistant data = mapper.convertValue(properties, SecondDivisionalAssistant.class);
                        memberPersisted = secondDivisionalAssistantRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((SecondDivisionalAssistant) memberPersisted).getUuid()).srcType(MemberType.SecondDivisionalAssistant)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case JuniorHealthInspectingOfficer:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<JuniorHealthInspectingOfficer> dbValue = juniorHealthInspectingOfficerRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        JuniorHealthInspectingOfficer data = mapper.convertValue(dbMap, JuniorHealthInspectingOfficer.class);
                        memberPersisted = juniorHealthInspectingOfficerRepository.save(data);
                    } else {
                        JuniorHealthInspectingOfficer data = mapper.convertValue(properties, JuniorHealthInspectingOfficer.class);
                        memberPersisted = juniorHealthInspectingOfficerRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((JuniorHealthInspectingOfficer) memberPersisted).getUuid()).srcType(MemberType.JuniorHealthInspectingOfficer)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case JuniorPrimaryHealthCareOfficer:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<JuniorPrimaryHealthCareOfficer> dbValue = juniorPrimaryHealthCareOfficerRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        JuniorPrimaryHealthCareOfficer data = mapper.convertValue(dbMap, JuniorPrimaryHealthCareOfficer.class);
                        memberPersisted = juniorPrimaryHealthCareOfficerRepository.save(data);
                    } else {
                        JuniorPrimaryHealthCareOfficer data = mapper.convertValue(properties, JuniorPrimaryHealthCareOfficer.class);
                        memberPersisted = juniorPrimaryHealthCareOfficerRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((JuniorPrimaryHealthCareOfficer) memberPersisted).getUuid()).srcType(MemberType.JuniorPrimaryHealthCareOfficer)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            case Driver:
                try {
                    checkPHC(properties, false);
                    if (id != null) {
                        Optional<com.ssf.membership.entities.Driver> dbValue = driverRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        com.ssf.membership.entities.Driver data = mapper.convertValue(dbMap, com.ssf.membership.entities.Driver.class);
                        memberPersisted = driverRepository.save(data);
                    } else {
                        com.ssf.membership.entities.Driver data = mapper.convertValue(properties, com.ssf.membership.entities.Driver.class);
                        memberPersisted = driverRepository.save(data);
                        if (properties.containsKey("phc")) {
                            memberRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((com.ssf.membership.entities.Driver) memberPersisted).getUuid()).srcType(MemberType.Driver)
                                    .type(RelationshipType.EMPLOYEEOF)
                                    .targetId(properties.get("phc").toString()).targetType(MemberType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateMembersError(e);
                }
                break;
            default:
                throw new CreateUpdateMembersError("Unexpected value: " + member.getType());
        }
        String stringifyJson = Utils.joltTransform(gson.toJson(mapper.convertValue(memberPersisted, Map.class)), member.getType().name());
        return gson.fromJson(stringifyJson, Map.class);
    }

    @Override
    public MemberPageResponse searchCitizenByFilters(String query, SearchType type, NodeFilterInput filterInput) {

        Long totalElements = 0L;
        List content = new ArrayList();

        MemberPageResponse response = MemberPageResponse.builder().content(content).totalPages(0L).totalElements(totalElements).build();

        if (query != null && query != "") {
            switch (type) {
                case CONTACT: {
                    MembersCountContentResponse result = memberRepository.searchByIndex(query, MembershipConstants.citizenContact, filterInput.getSize(), (filterInput.getPage() * filterInput.getSize()), filterInput);
                    response = MemberPageResponse.builder().totalElements(result.getCount())
                            .totalPages(Utils.getPagesByElementsAndSize(result.getCount(), filterInput.getSize()))
                            .content(result.getContent()).build();
                    break;
                }
                case DOB: {
                    MembersCountContentResponse result = memberRepository.searchByIndex(query, MembershipConstants.citizenDateOfBirth, filterInput.getSize(), (filterInput.getPage() * filterInput.getSize()), filterInput);
                    response = MemberPageResponse.builder().totalElements(result.getCount())
                            .totalPages(Utils.getPagesByElementsAndSize(result.getCount(), filterInput.getSize()))
                            .content(result.getContent()).build();
                    break;
                }
                case NAME: {
                    MembersCountContentResponse result = memberRepository.searchByFullTextIndex(query, MembershipConstants.citizenNameIndex, filterInput.getSize(), (filterInput.getPage() * filterInput.getSize()), filterInput);
                    response = MemberPageResponse.builder().totalElements(result.getCount())
                            .totalPages(Utils.getPagesByElementsAndSize(result.getCount(), filterInput.getSize()))
                            .content(result.getContent()).build();
                    break;
                }
                case UHID:
                    MembersCountContentResponse result = memberRepository.searchByIndex(query, MembershipConstants.citizenHealthID, filterInput.getSize(), (filterInput.getPage() * filterInput.getSize()), filterInput);
                    response = MemberPageResponse.builder().totalElements(result.getCount())
                            .totalPages(Utils.getPagesByElementsAndSize(result.getCount(), filterInput.getSize()))
                            .content(result.getContent()).build();
                    break;
            }
        }
        return response;
    }

    @Override
    public MemberPageResponse createRelationship(RelationshipDTO relationshipDTO) {
        return MemberPageResponse.builder().totalPages(1L).totalElements(1L).content(memberRelationshipRepository.createRelationshipBetweenNodes(relationshipDTO)).build();
    }

    @Override
    public MemberPageResponse getRelationshipsGrouping(GroupingDTO groupingDTO) {
        List content = memberRelationshipRepository.getRelationshipsGrouping(groupingDTO);
        return MemberPageResponse.builder().totalPages(1L).totalElements(Long.valueOf(content.size())).content(content).build();
    }

    @Override
    public MemberPageResponse updateRelationship(String relationshipId, RelationshipPatchDTO relationshipPatchDTO) {
        List content = memberRelationshipRepository.updatedRelationshipProperties(relationshipId, relationshipPatchDTO);
        return MemberPageResponse.builder().totalPages(1L).totalElements(Long.valueOf(content.size())).content(content).build();
    }

    @Override
    public MemberPageResponse getMemberRelationshipByFilters(RelationshipFilterDTO filterInput) {
        MembersCountContentResponse response = memberRelationshipRepository.getMemberRelationshipByFilters(filterInput);
        return MemberPageResponse.builder().totalPages(Utils.getPagesByElementsAndSize(response.getCount(), filterInput.getSize())).totalElements(response.getCount()).content(response.getContent()).build();
    }

    @Override
    public MemberPageResponse getMembersWithFilters(MemberFiltersDTO filtersDTO) {
        MembersCountContentResponse response = memberRepository.getMembersWithFilters(filtersDTO);
        return MemberPageResponse.builder().totalPages(Utils.getPagesByElementsAndSize(response.getCount(), filtersDTO.getSize())).totalElements(response.getCount()).content(response.getContent()).build();
    }

    private Map<String, Object> updateAudits(Map<String, Object> dbMap, String id) {
        String username = Utils.getUserFromIdToken(this.request.getHeader(MembershipConstants.ID_TOKEN));
        if (id != null) {
            dbMap.put("uuid", id);
            dbMap.put("modifiedBy", username);
        } else {
//            dbMap.remove("uuid");
            dbMap.put("createdBy", username);
            dbMap.put("modifiedBy", username);
        }
        return dbMap;
    }

    private void sendMessage(IMemberEntity member, Topics topic, String type) {
        String originator = request.getHeader(MembershipConstants.ORIGINATOR_SERVICE) != null ? request.getHeader(MembershipConstants.ORIGINATOR_SERVICE) : "";
        if (!originator.equals("PHC_SAGA")) {
            KafkaMessage<Object> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(request.getHeader(MembershipConstants.AUTHORIZATION)).build());
            kafkaMessage.setActor(Actor.builder().id(request.getHeader(MembershipConstants.ID_TOKEN)).build());
            kafkaMessage.setType(topic + type);
            kafkaMessage.setObject(member);
            producerClient.publishToTopic(topic.toString(), kafkaMessage);
        }
    }

    private void checkPHC(Map<String, Object> properties, Boolean isRequried) throws CreateUpdateMembersError {
        if (properties.containsKey("phc")) {
            Optional<Phc> optional = phcRepository.findByUuid(properties.get("phc").toString());
            if (optional.isEmpty()) {
                throw new CreateUpdateMembersError("No 'phc' found for given id");
            }
        } else if (isRequried && !properties.containsKey("uuid")) {
            throw new CreateUpdateMembersError("'phc' uuid is requried");
        }
    }

    private void checkSubCenter(Map<String, Object> properties, Boolean isRequried) throws CreateUpdateMembersError {
        if (properties.containsKey("subCenter")) {
            Optional<SubCenter> optional = subCenterRepository.findByUuid(properties.get("subCenter").toString());
            if (optional.isEmpty()) {
                throw new CreateUpdateMembersError("No 'subCenter' found for given id");
            }
        } else if (isRequried && !properties.containsKey("uuid")) {
            throw new CreateUpdateMembersError("'subCenter' uuid is requried");
        }
    }

    private void checkHouseHold(Map<String, Object> properties, Boolean isRequried) throws CreateUpdateMembersError {
        if (properties.containsKey("houseHold")) {
            Optional<HouseHold> optional = houseHoldRepository.findByUuid(properties.get("houseHold").toString());
            if (optional.isEmpty()) {
                throw new CreateUpdateMembersError("No 'houseHold' found for given id");
            }
        } else if (isRequried && !properties.containsKey("uuid")) {
            throw new CreateUpdateMembersError("'houseHold' uuid is requried");
        }
    }

    private void checkGramPanchayat(Map<String, Object> properties, Boolean isRequried) throws CreateUpdateMembersError {
        if (properties.containsKey("gramPanchayat")) {
            Optional<GramPanchayat> optional = gramPanchayatRepository.findByUuid(properties.get("gramPanchayat").toString());
            if (optional.isEmpty()) {
                throw new CreateUpdateMembersError("No 'gramPanchayat' found for given id");
            }
        } else if (isRequried && !properties.containsKey("uuid")) {
            throw new CreateUpdateMembersError("'gramPanchayat' uuid is requried");
        }
    }
}

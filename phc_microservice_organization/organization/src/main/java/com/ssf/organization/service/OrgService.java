package com.ssf.organization.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.ssf.organization.dtos.*;
import com.ssf.organization.entities.*;
import com.ssf.organization.exceptions.CreateUpdateOrgsError;
import com.ssf.organization.kafka.producer.client.KafkaProducerClient;
import com.ssf.organization.kafka.producer.factory.OrganizationProducerFactory;
import com.ssf.organization.kafka.producer.model.Actor;
import com.ssf.organization.kafka.producer.model.Context;
import com.ssf.organization.kafka.producer.model.KafkaMessage;
import com.ssf.organization.kafka.topics.Topics;
import com.ssf.organization.model.*;
import com.ssf.organization.repositories.*;
import com.ssf.organization.utils.OrgConstants;
import com.ssf.organization.utils.Utils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@EnableNeo4jAuditing

public class OrgService implements IOrgService {

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    static Logger logger = LoggerFactory.getLogger(OrgService.class);

    @Autowired
    private PhcRepository phcRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private GramPanchayatRepository gramPanchayatRepository;
    @Autowired
    private HouseHoldRepository houseHoldRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private SubCenterRepository subCenterRepository;
    @Autowired
    private TalukRepository talukRepository;
    @Autowired
    private VillageRepository villageRepository;
    @Autowired
    private OrganizationRelationshipRepository organizationRelationshipRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceSessionRepository placeSessionRepository;

    @Autowired
    private LocalityRepository localityRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private KafkaProducerClient producerClient;
    @Autowired
    private FacilityRepository facilityRepository;

    @Override
    public OrgPageResponse getAllPhcs(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Phc> pages = phcRepository.findAll(pageable);
        OrgPageResponse response = OrgPageResponse.builder().totalElements(pages.getTotalElements())
                .totalPages(Long.valueOf(pages.getTotalPages())).content(pages.getContent()).build();

        return response;
    }

    @Override
    public Org createOrganization(OrganizationDTO req) {
        return persist(req, null);
    }

    @Override
    public OrganizationDTO getOrgById(String id) {
        // TODO Auto-generated method stub
        List<OrgNode> re = organizationRelationshipRepository.getOrg(id);
        if (re != null && re.size() > 0) {
            return getData(re.get(0));
        }
        throw new RuntimeException("No Node found for the id");
    }

    @Override
    public OrgPageResponse getOrgs(OrgFilterDTO filter) {
        Page<Org> result = orgByType(filter, filter.getType().name());
        Gson gson = new Gson();
        List<Object> list = new ArrayList<>();
        result.getContent().forEach(v -> {
            String s = Utils.joltTransform(gson.toJson(v), filter.getType().name());
            list.add(gson.fromJson(s, Map.class));
        });
        OrgPageResponse resp = OrgPageResponse.builder().content(result.getContent())
                .totalPages(Long.valueOf(result.getTotalPages())).totalElements(result.getTotalElements()).build();
        return resp;
    }

    @Override
    public Org updateOrg(OrganizationDTO req, String id) {
        return persist(req, id);
    }

    private Page<Org> orgByType(OrgFilterDTO filter, String type) {

        try {

            switch (type) {
                case "Phc":
                    Page<Phc> result;
                    if (filter.getRelType() != null) {
                        result = phcRepository.findPhyRel(filter.getSourceId(), filter.getRelType().name(),
                                filter.getPageable());
                    } else {

                        if (filter.getAttributes() != null) {
                            Phc phcObj = mapper.convertValue(filter.getAttributes(), Phc.class);
                            Example<Phc> exPhc = Example.of(phcObj);

                            result = phcRepository.findAll(exPhc, filter.getPageable());
                        } else {
                            result = phcRepository.findAll(filter.getPageable());
                        }
                    }

                    Page<Org> pageOrg = new PageImpl(result.getContent(), filter.getPageable(), result.getContent().size());
                    return pageOrg;

                case "Country":
                    Page<Country> resultCountry;
                    if (filter.getRelType() != null) {
                        resultCountry = countryRepository.findCountryRel(filter.getSourceId(), filter.getRelType().name(),
                                filter.getPageable());
                    } else {
                        if (filter.getAttributes() != null) {
                            Country cObj = mapper.convertValue(filter.getAttributes(), Country.class);
                            Example<Country> exCountry = Example.of(cObj);

                            resultCountry = countryRepository.findAll(exCountry, filter.getPageable());
                        } else
                            resultCountry = countryRepository.findAll(filter.getPageable());
                    }
                    Page<Org> pageCountry = new PageImpl(resultCountry.getContent(), filter.getPageable(),
                            resultCountry.getContent().size());
                    return pageCountry;

                case "District":
                    Page<District> resultD;
                    if (filter.getRelType() != null) {
                        resultD = districtRepository.findDistrictRel(filter.getSourceId(), filter.getRelType().name(),
                                filter.getPageable());
                    } else {
                        if (filter.getAttributes() != null) {
                            District dObj = mapper.convertValue(filter.getAttributes(), District.class);
                            Example<District> exDistrict = Example.of(dObj);

                            resultD = districtRepository.findAll(exDistrict, filter.getPageable());
                        } else
                            resultD = districtRepository.findAll(filter.getPageable());
                    }
                    Page<Org> pageD = new PageImpl(resultD.getContent(), filter.getPageable(), resultD.getContent().size());
                    return pageD;
                case "GramPanchayat":
                    Page<GramPanchayat> resultGM;
                    if (filter.getRelType() != null) {
                        resultGM = gramPanchayatRepository.findGramPanchayatRel(filter.getSourceId(),
                                filter.getRelType().name(), filter.getPageable());
                    } else {
                        if (filter.getAttributes() != null) {
                            GramPanchayat gpObj = mapper.convertValue(filter.getAttributes(), GramPanchayat.class);
                            Example<GramPanchayat> exGP = Example.of(gpObj);

                            resultGM = gramPanchayatRepository.findAll(exGP, filter.getPageable());
                        } else
                            resultGM = gramPanchayatRepository.findAll(filter.getPageable());
                    }
                    Page<Org> pageGM = new PageImpl(resultGM.getContent(), filter.getPageable(),
                            resultGM.getContent().size());
                    return pageGM;

                case "HouseHold":
                    Page<HouseHold> resultH;
                    if (filter.getRelType() != null) {
                        resultH = houseHoldRepository.findHouseHoldRel(filter.getSourceId(), filter.getRelType().name(),
                                filter.getPageable());
                    } else {

                        if (filter.getAttributes() != null) {
                            HouseHold hObj = mapper.convertValue(filter.getPageable(), HouseHold.class);
                            Example<HouseHold> exH = Example.of(hObj);

                            resultH = houseHoldRepository.findAll(exH, filter.getPageable());
                        } else

                            resultH = houseHoldRepository.findAll(filter.getPageable());
                    }
                    Page<Org> pageH = new PageImpl(resultH.getContent(), filter.getPageable(), resultH.getContent().size());
                    return pageH;

                case "State":
                    Page<State> resultS;
                    if (filter.getRelType() != null) {
                        resultS = stateRepository.findStateRel(filter.getSourceId(), filter.getRelType().name(),
                                filter.getPageable());
                    } else {
                        if (filter.getAttributes() != null) {
                            State sObj = mapper.convertValue(filter.getPageable(), State.class);
                            Example<State> exS = Example.of(sObj);

                            resultS = stateRepository.findAll(exS, filter.getPageable());
                        } else
                            resultS = stateRepository.findAll(filter.getPageable());
                    }
                    Page<Org> pageS = new PageImpl(resultS.getContent(), filter.getPageable(), resultS.getContent().size());
                    return pageS;

                case "SubCenter":
                    Page<SubCenter> resultSC;
                    if (filter.getRelType() != null) {
                        resultSC = subCenterRepository.findSubCenterRel(filter.getSourceId(), filter.getRelType().name(),
                                filter.getPageable());
                    } else {
                        if (filter.getAttributes() != null) {
                            SubCenter scObj = mapper.convertValue(filter.getPageable(), SubCenter.class);
                            Example<SubCenter> exSC = Example.of(scObj);

                            resultSC = subCenterRepository.findAll(exSC, filter.getPageable());
                        } else
                            resultSC = subCenterRepository.findAll(filter.getPageable());
                    }
                    Page<Org> pageSC = new PageImpl(resultSC.getContent(), filter.getPageable(),
                            resultSC.getContent().size());
                    return pageSC;
                case "Taluk":
                    Page<Taluk> resultT;
                    if (filter.getRelType() != null) {
                        resultT = talukRepository.findTalukRel(filter.getSourceId(), filter.getRelType().name(),
                                filter.getPageable());
                    } else {
                        if (filter.getAttributes() != null) {
                            Taluk scObj = mapper.convertValue(filter.getAttributes(), Taluk.class);
                            Example<Taluk> exT = Example.of(scObj);

                            resultT = talukRepository.findAll(exT, filter.getPageable());
                        } else
                            resultT = talukRepository.findAll(filter.getPageable());
                    }
                    Page<Org> pageT = new PageImpl(resultT.getContent(), filter.getPageable(), resultT.getContent().size());
                    return pageT;

                case "Village":
                    Page<Village> resultV;
                    if (filter.getRelType() != null) {
                        resultV = villageRepository.findVillageRel(filter.getSourceId(), filter.getRelType().name(),
                                filter.getPageable());
                    } else {
                        if (filter.getAttributes() != null) {
                            Village vObj = mapper.convertValue(filter.getAttributes(), Village.class);
                            Example<Village> exV = Example.of(vObj);

                            resultV = villageRepository.findAll(exV, filter.getPageable());
                        } else
                            resultV = villageRepository.findAll(filter.getPageable());
                    }
                    Page<Org> pageV = new PageImpl(resultV.getContent(), filter.getPageable(), resultV.getContent().size());
                    return pageV;

                case "Place":
                    Page<Place> resultP;
                    if (filter.getRelType() != null) {
                        resultP = placeRepository.findPlaceRel(filter.getSourceId(), filter.getRelType().name(),
                                filter.getPageable());
                    } else {
                        if (filter.getAttributes() != null) {
                            Place pObj = mapper.convertValue(filter.getAttributes(), Place.class);
                            Example<Place> exP = Example.of(pObj);

                            resultP = placeRepository.findAll(exP, filter.getPageable());
                        } else
                            resultP = placeRepository.findAll(filter.getPageable());
                    }
                    Page<Org> pageP = new PageImpl(resultP.getContent(), filter.getPageable(), resultP.getContent().size());
                    return pageP;

                default:
                    return null;
            }
        } catch (Exception e) {
            return new PageImpl<>(new ArrayList<>());
        }

    }

    private OrganizationDTO getData(OrgNode node) {
        OrganizationDTO org = new OrganizationDTO();
        switch (node.getLabels().get(0)) {
            case "Phc":
                org.setType(OrgType.Phc);
                org.setProperties(node.getProperties());
                return org;
            case "Country":
                org.setType(OrgType.Country);
                org.setProperties(node.getProperties());
                return org;
            case "District":
                org.setType(OrgType.District);
                org.setProperties(node.getProperties());
                return org;
            case "GramPanchayat":
                org.setType(OrgType.GramPanchayat);
                org.setProperties(node.getProperties());
                return org;

            case "HouseHold":
                org.setType(OrgType.HouseHold);
                org.setProperties(node.getProperties());
                return org;

            case "State":
                org.setType(OrgType.State);
                org.setProperties(node.getProperties());
                return org;
            case "SubCenter":
                org.setType(OrgType.SubCenter);
                org.setProperties(node.getProperties());
                return org;
            case "Taluk":
                org.setType(OrgType.Taluk);
                org.setProperties(node.getProperties());
                return org;

            case "Village":
                org.setType(OrgType.Village);
                org.setProperties(node.getProperties());
                return org;
            case "Place":
                org.setType(OrgType.Place);
                org.setProperties(node.getProperties());
                return org;
            default:
                return null;
        }
    }

    @SneakyThrows
    @Transactional
    public Org persist(OrganizationDTO req, String id) {
        Org org = null;
        Map<String, Object> properties = updateAudits(req.getProperties(), id);
        switch (req.getType()) {
            case Country:
                try {
                    if (id != null) {
                        Optional<Country> dbValue = countryRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        Country data = mapper.convertValue(dbMap, Country.class);
                        org = countryRepository.save(data);
                    } else {
                        Country data = mapper.convertValue(properties, Country.class);
                        org = countryRepository.save(data);
                    }

                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            case District:
                try {
                    if (properties.containsKey("state")) {
                        Optional<State> optional = stateRepository.findByUuid(properties.get("state").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'state' found for given id");
                        }
                    }
                    if (id != null) {
                        Optional<District> dbValue = districtRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        District data = mapper.convertValue(dbMap, District.class);
                        org = districtRepository.save(data);
                    } else {
                        District data = mapper.convertValue(properties, District.class);
                        org = districtRepository.save(data);
                        if (properties.containsKey("state")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((District) org).getUuid()).srcType(OrgType.District)
                                    .type(RelType.CONTAINEDINPLACE)
                                    .targetId(properties.get("state").toString()).targetType(OrgType.State)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            case GramPanchayat:
                try {
                    if (properties.containsKey("phc")) {
                        Optional<Phc> optional = phcRepository.findByUuid(properties.get("phc").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'phc' found for given id");
                        }
                    }
                    if (properties.containsKey("taluk")) {
                        Optional<Taluk> optional = talukRepository.findByUuid(properties.get("taluk").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'taluk' found for given id");
                        }
                    }
                    if (id != null) {
                        Optional<GramPanchayat> dbValue = gramPanchayatRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        GramPanchayat data = mapper.convertValue(dbMap, GramPanchayat.class);
                        org = gramPanchayatRepository.save(data);
                    } else {
                        GramPanchayat data = mapper.convertValue(properties, GramPanchayat.class);
                        org = gramPanchayatRepository.save(data);
                        if (properties.containsKey("phc")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((GramPanchayat) org).getUuid()).srcType(OrgType.GramPanchayat)
                                    .type(RelType.SERVICEDBY)
                                    .targetId(properties.get("phc").toString()).targetType(OrgType.Phc)
                                    .build());
                        }
                        if (properties.containsKey("taluk")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((GramPanchayat) org).getUuid()).srcType(OrgType.GramPanchayat)
                                    .type(RelType.ADMINISTEREDBY)
                                    .targetId(properties.get("taluk").toString()).targetType(OrgType.Taluk)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            case HouseHold:
                try {
                    if (properties.containsKey("village") && properties.containsKey("locality")) {
                        throw new CreateUpdateOrgsError("for URBAN PHC pass 'locality' only, for RURAL PHC pass 'village' only");
                    }
                    if (id != null) {
                        Optional<HouseHold> dbValue = houseHoldRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        HouseHold data = mapper.convertValue(dbMap, HouseHold.class);
                        org = houseHoldRepository.save(data);
                        sendMessage(org, Topics.HouseHold, "Updated");
                    } else {
                        if (properties.containsKey("village")) {
                            Optional<Village> optional = villageRepository.findByUuid(properties.get("village").toString());
                            if (optional.isEmpty()) {
                                throw new CreateUpdateOrgsError("No 'village' found for given id");
                            }
                        } else if (properties.containsKey("locality")) {
                            Optional<Locality> optional = localityRepository.findByUuid(properties.get("locality").toString());
                            if (optional.isEmpty()) {
                                throw new CreateUpdateOrgsError("No 'locality' found for given id");
                            }
                        } else {
                            throw new CreateUpdateOrgsError("'village' or 'locality' key is must");
                        }
                        HouseHold data = mapper.convertValue(properties, HouseHold.class);
                        org = houseHoldRepository.save(data);
                        if (properties.containsKey("village")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((HouseHold) org).getUuid()).srcType(OrgType.HouseHold)
                                    .type(RelType.CONTAINEDINPLACE)
                                    .targetId(properties.get("village").toString()).targetType(OrgType.Village)
                                    .build());
                        }
                        if (properties.containsKey("locality")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((HouseHold) org).getUuid()).srcType(OrgType.HouseHold)
                                    .type(RelType.CONTAINEDINPLACE)
                                    .targetId(properties.get("locality").toString()).targetType(OrgType.Locality)
                                    .build());
                        }
                        sendMessage(org, Topics.HouseHold, "Created");
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            case State:
                try {
                    if (properties.containsKey("country")) {
                        Optional<Country> optional = countryRepository.findByUuid(properties.get("country").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'country' found for given id");
                        }
                    }
                    if (id != null) {
                        Optional<State> dbValue = stateRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        State data = mapper.convertValue(dbMap, State.class);
                        org = stateRepository.save(data);
                    } else {
                        State data = mapper.convertValue(properties, State.class);
                        org = stateRepository.save(data);
                        if (properties.containsKey("country")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((State) org).getUuid()).srcType(OrgType.State)
                                    .type(RelType.CONTAINEDINPLACE)
                                    .targetId(properties.get("country").toString()).targetType(OrgType.Country)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            case SubCenter:
                try {
                    if (properties.containsKey("phc")) {
                        Optional<Phc> optional = phcRepository.findByUuid(properties.get("phc").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'phc' found for given id");
                        }
                    }
                    if (id != null) {
                        Optional<SubCenter> dbValue = subCenterRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        SubCenter data = mapper.convertValue(dbMap, SubCenter.class);
                        org = subCenterRepository.save(data);
                    } else {
                        SubCenter data = mapper.convertValue(properties, SubCenter.class);
                        org = subCenterRepository.save(data);
                        if (properties.containsKey("phc")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((SubCenter) org).getUuid()).srcType(OrgType.SubCenter)
                                    .type(RelType.SUBORGOF)
                                    .targetId(properties.get("phc").toString()).targetType(OrgType.Phc)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            case Taluk:
                try {
                    if (properties.containsKey("district")) {
                        Optional<District> optional = districtRepository.findByUuid(properties.get("district").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'district' found for given id");
                        }
                    }
                    if (id != null) {
                        Optional<Taluk> dbValue = talukRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        Taluk data = mapper.convertValue(dbMap, Taluk.class);
                        org = talukRepository.save(data);
                    } else {
                        Taluk data = mapper.convertValue(properties, Taluk.class);
                        org = talukRepository.save(data);
                        if (properties.containsKey("district")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((Taluk) org).getUuid()).srcType(OrgType.Taluk)
                                    .type(RelType.CONTAINEDINPLACE)
                                    .targetId(properties.get("district").toString()).targetType(OrgType.District)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            case Village:
                try {
                    if (properties.containsKey("taluk")) {
                        Optional<Taluk> optional = talukRepository.findByUuid(properties.get("taluk").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'taluk' found for given id");
                        }
                    }
                    if (properties.containsKey("subCenter")) {
                        Optional<SubCenter> optional = subCenterRepository.findByUuid(properties.get("subCenter").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'subCenter' found for given id");
                        }
                    }
                    if (properties.containsKey("gramPanchayat")) {
                        Optional<GramPanchayat> optional = gramPanchayatRepository.findByUuid(properties.get("gramPanchayat").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'gramPanchayat' found for given id");
                        }
                    }
                    if (id != null) {
                        Optional<Village> dbValue = villageRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        Village data = mapper.convertValue(dbMap, Village.class);
                        org = villageRepository.save(data);
                    } else {
                        Village data = mapper.convertValue(properties, Village.class);
                        org = villageRepository.save(data);
                        if (properties.containsKey("taluk")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(properties.get("taluk").toString()).srcType(OrgType.Taluk)
                                    .type(RelType.CONTAINEDINPLACE)
                                    .targetId(((Village) org).getUuid()).targetType(OrgType.Village)
                                    .build());
                        }
                        if (properties.containsKey("subCenter")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((Village) org).getUuid()).srcType(OrgType.Village)
                                    .type(RelType.SERVICEDAREA)
                                    .targetId(properties.get("subCenter").toString()).targetType(OrgType.SubCenter)
                                    .build());
                        }
                        if (properties.containsKey("gramPanchayat")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((Village) org).getUuid()).srcType(OrgType.Village)
                                    .type(RelType.GOVERNEDBY)
                                    .targetId(properties.get("gramPanchayat").toString()).targetType(OrgType.GramPanchayat)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            case Place:
                try {
                    if (id != null) {
                        Optional<Place> dbValue = placeRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        Place data = mapper.convertValue(dbMap, Place.class);
                        org = placeRepository.save(data);
                    } else {
                        if (properties.containsKey("village")) {
                            Optional<Village> optional = villageRepository.findByUuid(properties.get("village").toString());
                            if (optional.isEmpty()) {
                                throw new CreateUpdateOrgsError("No 'village' found for given id");
                            }
                        } else {
                            throw new CreateUpdateOrgsError("'village' key is required");
                        }
                        Place data = mapper.convertValue(properties, Place.class);
                        org = placeRepository.save(data);
                        if (properties.containsKey("village")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((Place) org).getUuid()).srcType(OrgType.Place)
                                    .type(RelType.CONTAINEDINPLACE)
                                    .targetId(properties.get("village").toString()).targetType(OrgType.Village)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            case Phc:
                try {
                    if (id != null) {
                        Optional<Phc> dbValue = phcRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        Phc data = mapper.convertValue(dbMap, Phc.class);
                        org = phcRepository.save(data);
                    } else {
                        Phc data = mapper.convertValue(properties, Phc.class);
                        org = phcRepository.save(data);
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }

                break;
            case Locality:
                try {
                    if (id != null) {
                        Optional<Locality> dbValue = localityRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        Locality data = mapper.convertValue(dbMap, Locality.class);
                        org = localityRepository.save(data);
                    } else {
                        Locality data = mapper.convertValue(properties, Locality.class);
                        org = localityRepository.save(data);
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            case Facility:
                try {
                    if (properties.containsKey("phc")) {
                        Optional<Phc> optional = phcRepository.findByUuid(properties.get("phc").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'phc' found for given id");
                        }
                    }
                    if (properties.containsKey("subCenter")) {
                        Optional<SubCenter> optional = subCenterRepository.findByUuid(properties.get("subCenter").toString());
                        if (optional.isEmpty()) {
                            throw new CreateUpdateOrgsError("No 'subCenter' found for given id");
                        }
                    }
                    if (id != null) {
                        Optional<Facility> dbValue = facilityRepository.findByUuid(id);
                        Map<String, Object> dbMap = mapper.convertValue(dbValue.get(), Map.class);
                        dbMap.putAll(properties);
                        Facility data = mapper.convertValue(dbMap, Facility.class);
                        org = facilityRepository.save(data);
                    } else {
                        Facility data = mapper.convertValue(properties, Facility.class);
                        org = facilityRepository.save(data);
                        if (properties.containsKey("phc")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((Facility) org).getUuid()).srcType(OrgType.Facility)
                                    .type(RelType.HOSTEDIN)
                                    .targetId(properties.get("phc").toString()).targetType(OrgType.Phc)
                                    .build());
                        }
                        if (properties.containsKey("subCenter")) {
                            organizationRelationshipRepository.createRelationshipBetweenNodes(CreateRelationshipDTO.builder()
                                    .srcId(((Facility) org).getUuid()).srcType(OrgType.Facility)
                                    .type(RelType.HOSTEDIN)
                                    .targetId(properties.get("subCenter").toString()).targetType(OrgType.SubCenter)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    throw new CreateUpdateOrgsError(e);
                }
                break;
            default:
                throw new CreateUpdateOrgsError("Unexpected value: " + req.getType());
        }
        return org;
    }

    @Override
    public OrgPageResponse getOrgsAndRelationship(OrgFilterDTO filter, Integer page, Integer size) {
        Instant start = Instant.now();
        if (size < 1) {
            size = 25;
        }
        if (filter.getSourceId() != null) {
            List<Org> result = organizationRelationshipRepository.getOrgNodeWithRelationshipBySourceId(filter.getRelType(), filter.getTargetType(), filter.getSourceType(), filter.getSourceId(), page, size);
            Long totalElements = organizationRelationshipRepository.getCountOrgNodeWithRelationshipBySourceId(filter.getRelType(), filter.getTargetType(), filter.getSourceType(), filter.getSourceId());
            OrgPageResponse resp = OrgPageResponse.builder().totalElements(totalElements)
                    .totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).content(result).build();
            Instant end = Instant.now();
            logger.info("Time taken to get details : {} ms", (Duration.between(start, end).toMillis()));
            return resp;
        } else if (filter.getTargetId() != null) {
            List<Org> result = organizationRelationshipRepository.getOrgNodeWithRelationshipByTargetId(filter.getRelType(), filter.getTargetType(), filter.getSourceType(), filter.getTargetId(), page, size);
            Long totalElements = organizationRelationshipRepository.getCountOrgNodeWithRelationshipByTargetId(filter.getRelType(), filter.getTargetType(), filter.getSourceType(), filter.getTargetId());
            OrgPageResponse resp = OrgPageResponse.builder().totalElements(totalElements)
                    .totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).content(result).build();
            Instant end = Instant.now();
            logger.info("Time taken to get details : {} ms", (Duration.between(start, end).toMillis()));
            return resp;
        }
        List<Org> result = organizationRelationshipRepository.getOrgNodeWithRelationship(filter.getRelType(), filter.getTargetType(), filter.getSourceType(), page, size);
        Long totalElements = organizationRelationshipRepository.getCountOrgNodeWithRelationship(filter.getRelType(), filter.getTargetType(), filter.getSourceType());
        OrgPageResponse resp = OrgPageResponse.builder().totalElements(totalElements)
                .totalPages(Utils.getPagesByElementsAndSize(totalElements, size)).content(result).build();
        Instant end = Instant.now();
        logger.info("Time taken to get details : {} ms", (Duration.between(start, end).toMillis()));
        return resp;
    }

    @SneakyThrows
    @Override
    public OrgPageResponse getNearMeOrg(NodeFilterInput filterInput) {

        OrgPageResponse response;
        if (filterInput.getProperties().containsKey("villageId") &&
                filterInput.getProperties().containsKey("latitude") &&
                filterInput.getProperties().containsKey("longitude") &&
                filterInput.getProperties().containsKey("distance")
        ) {
            String villageId = (String) filterInput.getProperties().get("villageId");
            Double latitude = (Double) filterInput.getProperties().get("latitude");
            Double longitude = (Double) filterInput.getProperties().get("longitude");
            Double distance = (Double) filterInput.getProperties().get("distance");

            Pageable pageable = PageRequest.of(filterInput.getPage(), filterInput.getSize());

            switch (filterInput.getType()) {
                case Place:
                    Page<Place> placePageData = placeRepository.findPlacesNearMe(villageId, latitude, longitude, distance, pageable);

                    response = OrgPageResponse.builder().totalPages(Long.valueOf(placePageData.getTotalPages())).totalElements(placePageData.getTotalElements())
                            .content(placePageData.getContent()).build();
                    break;

                case HouseHold:
                    Page<HouseHold> houseHoldPageData = houseHoldRepository.findHouseHoldNearMe(villageId, latitude, longitude, distance, pageable);

                    response = OrgPageResponse.builder().totalPages(Long.valueOf(houseHoldPageData.getTotalPages())).totalElements(houseHoldPageData.getTotalElements())
                            .content(houseHoldPageData.getContent()).build();
                    break;

                default:
                    response = null;
            }

            return response;

        } else {
            throw new Exception("villageId, latitude, longitude and distance is required.");
        }
    }

    @Override
    @SneakyThrows
    public OrgPageResponse getPlacesCount(NodeFilterInput filterInput) {
        String villageId = null;
        AssetType assetType = null;

        if (filterInput.getProperties().containsKey("villageId")) {
            villageId = (String) filterInput.getProperties().get("villageId");
        }
        if (filterInput.getProperties().containsKey("assetType")) {
            assetType = AssetType.valueOf((String) filterInput.getProperties().get("assetType"));
        }

        List<PlaceCount> result = placeSessionRepository.getPlaceCountByGrouping(villageId, assetType);

        return OrgPageResponse.builder().content(result).build();

    }

    @Override
    public OrgPageResponse getOrgsByFilters(NodeFilterInput filterInput) {
        if (filterInput.getExpressions() != null && filterInput.getExpressions().size() > 0) {
            OrgCountContentResponse response = organizationRelationshipRepository.getOrgsWithFilters(filterInput);
            return OrgPageResponse.builder().totalPages(Utils.getPagesByElementsAndSize(response.getCount(), filterInput.getSize())).totalElements(response.getCount()).content(response.getContent()).build();

        } else {
            OrgCountContentResponse result = organizationRelationshipRepository.getOrgNodeWithFilters(filterInput);
            return OrgPageResponse.builder().content(result.getContent())
                    .totalElements(result.getCount())
                    .totalPages(Utils.getPagesByElementsAndSize(result.getCount(), filterInput.getSize())).build();
        }
    }

    @Override
    public OrgPageResponse searchHouseHoldByFilters(String query, NodeFilterInput filterInput) {

        Long totalElements = 0L;
        List content = new ArrayList();

        OrgPageResponse response = OrgPageResponse.builder().content(content).totalPages(0L).totalElements(totalElements).build();

        if (query != null && query != "") {
            OrgCountContentResponse result = organizationRelationshipRepository.searchByFullTextIndex(query, SearchIndexType.HouseHoldIDNameIndex.name(), filterInput.getSize(), (filterInput.getPage() * filterInput.getSize()), filterInput);
            response = OrgPageResponse.builder().totalElements(result.getCount())
                    .totalPages(Utils.getPagesByElementsAndSize(result.getCount(), filterInput.getSize()))
                    .content(result.getContent()).build();
        }
        return response;
    }

    @Override
    public OrgPageResponse getRelationshipsGrouping(GroupingDTO groupingDTO) {
        List content = organizationRelationshipRepository.getRelationshipsGrouping(groupingDTO);
        return OrgPageResponse.builder().totalPages(Utils.getPagesByElementsAndSize(Long.valueOf(content.size()), content.size()))
                .content(content).totalElements(Long.valueOf(content.size())).build();
    }

    @Override
    public OrgPageResponse getOrganisationRelationshipByFilter(RelationshipFilterDTO filterInput) {
        OrgCountContentResponse response = organizationRelationshipRepository.getOrgRelationshipByFilters(filterInput);
        return OrgPageResponse.builder().totalPages(Utils.getPagesByElementsAndSize(response.getCount(), filterInput.getSize())).totalElements(response.getCount()).content(response.getContent()).build();
    }

    @Override
    public OrgPageResponse createRelationship(RelationshipDTO relationshipDTO) {
        return OrgPageResponse.builder().totalPages(1L).totalElements(1L).content(organizationRelationshipRepository.createRelationshipBetweenNodes(relationshipDTO)).build();
    }

    @Override
    public OrgPageResponse updateRelationship(String relationshipId, RelationshipPatchDTO relationshipPatchDTO) {
        List content = organizationRelationshipRepository.updatedRelationshipProperties(relationshipId, relationshipPatchDTO);
        return OrgPageResponse.builder().totalPages(1L).totalElements(Long.valueOf(content.size())).content(content).build();
    }

    private Map<String, Object> updateAudits(Map<String, Object> dbMap, String id) {
        String username = Utils.getUserFromIdToken(this.request.getHeader(OrgConstants.ID_TOKEN));
        if (id != null) {
            dbMap.put("modifiedBy", username);
        } else {
            dbMap.remove("uuid");
            dbMap.put("createdBy", username);
            dbMap.put("modifiedBy", username);
        }
        return dbMap;
    }

    private void sendMessage(Org org, Topics topic, String type) {
        String originator = request.getHeader(OrgConstants.ORIGINATOR_SERVICE) != null ? request.getHeader(OrgConstants.ORIGINATOR_SERVICE) : "";
        if (!originator.equals("PHC_SAGA")) {
            KafkaMessage<Object> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(request.getHeader(OrgConstants.AUTHORIZATION)).build());
            kafkaMessage.setActor(Actor.builder().id(request.getHeader(OrgConstants.ID_TOKEN)).build());
            kafkaMessage.setType(topic + type);
            kafkaMessage.setObject(org);
            producerClient.publishToTopic(topic.toString(), kafkaMessage);
        }
    }

}


package com.ssf.organization.service;

import com.ssf.organization.dtos.OrgFilterDTO;
import com.ssf.organization.dtos.OrgPageResponse;
import com.ssf.organization.dtos.OrganizationDTO;
import com.ssf.organization.entities.Country;
import com.ssf.organization.entities.Org;
import com.ssf.organization.model.OrgType;
import com.ssf.organization.model.RelType;
import com.ssf.organization.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@EmbeddedKafka
@ActiveProfiles("test")
public class TestOrgService {

    @MockBean
    private PhcRepository phcRepository;
    @MockBean
    private CountryRepository countryRepository;
    @MockBean
    private DistrictRepository districtRepository;
    @MockBean
    private GramPanchayatRepository gramPanchayatRepository;
    @MockBean
    private HouseHoldRepository houseHoldRepository;
    @MockBean
    private StateRepository stateRepository;
    @MockBean
    private SubCenterRepository subCenterRepository;
    @MockBean
    private TalukRepository talukRepository;
    @MockBean
    private VillageRepository villageRepository;

    @MockBean
    private OrganizationRelationshipRepository organizationRelationshipRepository;

    @Autowired
    private IOrgService service;

    @Test
    public void testCreateOrgAsCountry() {
        OrganizationDTO dto = getOrgDTO(OrgType.Country);
        service.createOrganization(dto);
    }

    @Test
    public void testCreateOrgAsDistrict() {
        OrganizationDTO dto = getOrgDTO(OrgType.District);
        service.createOrganization(dto);
    }

    @Test
    public void testCreateOrgAsGramPanchyat() {
        OrganizationDTO dto = getOrgDTO(OrgType.GramPanchayat);
        service.createOrganization(dto);
    }

    //    @Test
    public void testCreateOrgAsHouseHold() {
        OrganizationDTO dto = getOrgDTO(OrgType.HouseHold);
        service.createOrganization(dto);
    }

    @Test
    public void testCreateOrgAsPHC() {
        OrganizationDTO dto = getOrgDTO(OrgType.Phc);
        service.createOrganization(dto);
    }

    @Test
    public void testCreateOrgAsState() {
        OrganizationDTO dto = getOrgDTO(OrgType.State);
        service.createOrganization(dto);
    }

    @Test
    public void testCreateOrgAsSubCenter() {
        OrganizationDTO dto = getOrgDTO(OrgType.SubCenter);
        service.createOrganization(dto);
    }

    @Test
    public void testCreateOrgAsTaluk() {
        OrganizationDTO dto = getOrgDTO(OrgType.Taluk);
        service.createOrganization(dto);
    }

    @Test
    public void testCreateOrgAsVillage() {
        OrganizationDTO dto = getOrgDTO(OrgType.Village);
        service.createOrganization(dto);
    }

    @Test
    public void testGetOrgFilterAllPhc() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.Phc);
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterPhc() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.Phc);
        dto.setAttributes(new HashMap<>());
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterAllCountry() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.Country);
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterCountry() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.Country);
        dto.setAttributes(new HashMap<>());
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterAllDISTRICT() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.District);
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterDISTRICT() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.District);
        dto.setAttributes(new HashMap<>());
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterAllGRAM_PANCHAYAT() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.GramPanchayat);
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterGRAM_PANCHAYAT() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.GramPanchayat);
        dto.setAttributes(new HashMap<>());
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterAllHOUSE_HOLD() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.HouseHold);
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterHOUSE_HOLD() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.HouseHold);
        dto.setAttributes(new HashMap<>());
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterAllSTATE() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.State);
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterSTATE() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.State);
        dto.setAttributes(new HashMap<>());
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterAllSUB_CENTER() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.SubCenter);
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterSUB_CENTER() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.SubCenter);
        dto.setAttributes(new HashMap<>());
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterAllTALUK() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.Taluk);
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterTALUK() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.Taluk);
        dto.setAttributes(new HashMap<>());
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterAllVILLAGE() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.Village);
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testGetOrgFilterVILLAGE() {
        OrgFilterDTO dto = getOrgFilterDTO();
        dto.setType(OrgType.Village);
        dto.setAttributes(new HashMap<>());
        OrgPageResponse result = service.getOrgs(dto);
        assertNotNull(result);
    }

    @Test
    public void testUpdateCountry() {
        when(countryRepository.findByUuid("random-uuid")).thenReturn(Optional.of(getCountry()));
        when(countryRepository.save(any())).thenReturn(getCountry());
        OrganizationDTO dto = getOrgDTO(OrgType.Country);
        Org result = service.updateOrg(dto, "random-uuid");

        Assertions.assertNotNull(result);

    }

    private Country getCountry() {
        Country country = new Country();
        country.setUuid(UUID.randomUUID().toString());
        country.setName("India");

        return country;
    }

    private OrgFilterDTO getOrgFilterDTO() {
        PageRequest page = PageRequest.of(1, 1);
        OrgFilterDTO filter = OrgFilterDTO.builder().pageable(page).build();
        return filter;
    }

    private OrganizationDTO getOrgDTO(OrgType type) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setType(type);
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "Test Node");
        dto.setProperties(properties);

        return dto;
    }

}

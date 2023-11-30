package com.ssf.pncregistration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncregistration.constant.MockDataConstant;
import com.ssf.pncregistration.dtos.*;
import com.ssf.pncregistration.entities.Infant;
import com.ssf.pncregistration.entities.PNCRegistration;
import com.ssf.pncregistration.exception.EntityNotFoundException;
import com.ssf.pncregistration.repository.IPNCInfantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Disabled
@ActiveProfiles("test")
@EmbeddedKafka
public class PNCInfantServiceTest {

    @MockBean
    private IPNCInfantRepository repository;
    @Autowired
    private PNCInfantService service;
    @MockBean
    private PNCRegistrationService pncRegistrationService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_TheCreateInfant() throws Exception {

        //Mock
        InfantDto infantDto = MockDataConstant.buildInfantDto();
        Infant infant = mapper.map(infantDto, Infant.class);
        PNCRegistrationDto pncRegistrationDto = MockDataConstant.buildPNCRegDto();
        PNCRegistration pncRegistration = mapper.map(pncRegistrationDto, PNCRegistration.class);

        when(pncRegistrationService.getPNCReg(infantDto.getPncRegistrationId())).thenReturn(pncRegistration);
        Mockito.when(repository.save(infant)).thenReturn(infant);
        //perform and assert
        List<InfantDto> result = service.createInfant(MockDataConstant.PNCID, Arrays.asList(infantDto));
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidInfantRegType_ThenPatchInfant() {
        InfantDto expectedResponse = MockDataConstant.buildInfantDto();
        Map<String, Object> infatProp = objectMapper.convertValue(expectedResponse, Map.class);
        InfantPatchDto request = InfantPatchDto.builder().type(InfantTypeEnum.INFANT)
                .properties(infatProp).build();
        Infant infant = mapper.map(expectedResponse, Infant.class);
        when(repository.findByIdAndRegistrationId(MockDataConstant.INFANT_ID, MockDataConstant.PNCID)).thenReturn(Optional.of(infant));
        Mockito.when(repository.save(infant)).thenReturn(infant);
        InfantDto result = service.patchInfant(MockDataConstant.PNCID, MockDataConstant.INFANT_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidImmuRegType_ThenPatchInfant() {
        //MOCK
        InfantDto expectedResponse = MockDataConstant.buildInfantDto();
        Map<String, Object> infatProp = objectMapper.convertValue(expectedResponse, Map.class);
        InfantPatchDto request = InfantPatchDto.builder().type(InfantTypeEnum.IMMUNIZATION)
                .properties(infatProp).build();
        Infant infant = mapper.map(expectedResponse, Infant.class);
        when(repository.findByIdAndRegistrationId(MockDataConstant.INFANT_ID, MockDataConstant.PNCID)).thenReturn(Optional.of(infant));
        Mockito.when(repository.save(infant)).thenReturn(infant);

        //perform and assert
        InfantDto result = service.patchInfant(MockDataConstant.PNCID, MockDataConstant.INFANT_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidId_thenReadInfant() {
        InfantDto infantDto = MockDataConstant.buildInfantDto();
        Infant infant = mapper.map(infantDto, Infant.class);
        when(repository.findByIdAndRegistrationId(MockDataConstant.INFANT_ID, MockDataConstant.PNCID)).thenReturn(Optional.of(infant));

        // call service
        InfantDto actualResponse = service.readInfants(MockDataConstant.PNCID, MockDataConstant.INFANT_ID);
        assertThat(actualResponse.getPncRegistrationId()).isEqualTo(MockDataConstant.PNCID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadEc() {
        when(repository.findByIdAndRegistrationId(MockDataConstant.INFANT_ID, MockDataConstant.PNCID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readInfants(MockDataConstant.PNCID, MockDataConstant.INFANT_ID));
    }

    @Test
    public void whenIdRgeId_thenReturns200FilterInfant() {
        //Mock
        InfantDto infantDto = MockDataConstant.buildInfantDto();
        Infant infant = mapper.map(infantDto, Infant.class);

        ArrayList<Infant> infants = new ArrayList<>();
        infants.add(infant);
        Page<Infant> infantPage = new PageImpl<>(infants);
        Pageable paging = PageRequest.of(0, 5);

        Infant example = Infant.builder()
                .id(MockDataConstant.INFANT_ID)
                .pncRegistrationId(MockDataConstant.PNCID)
                .build();
        when(repository.findAll(Example.of(example), paging)).thenReturn(infantPage);

        // call to the service class
        InfantPageResponse response = service.filterInfants(MockDataConstant.INFANT_ID, MockDataConstant.PNCID, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenId_thenReturns200FilterPNC() {
        //Mock
        InfantDto infantDto = MockDataConstant.buildInfantDto();
        Infant infant = mapper.map(infantDto, Infant.class);

        ArrayList<Infant> infants = new ArrayList<>();
        infants.add(infant);
        Page<Infant> infantPage = new PageImpl<>(infants);
        Pageable paging = PageRequest.of(0, 5);

        Infant example = Infant.builder()
                .id(MockDataConstant.INFANT_ID)
                .build();
        when(repository.findAll(Example.of(example), paging)).thenReturn(infantPage);
        // call to the service class
        InfantPageResponse response = service.filterInfants(MockDataConstant.INFANT_ID, null, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

}

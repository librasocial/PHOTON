package com.ssf.pncregistration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncregistration.constant.DataEntryStatusEnum;
import com.ssf.pncregistration.constant.MockDataConstant;
import com.ssf.pncregistration.dtos.PNCCRegTypeEnum;
import com.ssf.pncregistration.dtos.PNCRegPageResponse;
import com.ssf.pncregistration.dtos.PNCRegPatchDto;
import com.ssf.pncregistration.dtos.PNCRegistrationDto;
import com.ssf.pncregistration.entities.Couple;
import com.ssf.pncregistration.entities.PNCRegistration;
import com.ssf.pncregistration.exception.EntityNotFoundException;
import com.ssf.pncregistration.repository.IPNCRegistrationRepository;
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

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Disabled
@ActiveProfiles("test")
@EmbeddedKafka
public class PNCRegistrationServiceTest {

    @MockBean
    private IPNCRegistrationRepository repository;
    @Autowired
    private PNCRegistrationService service;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_ThenRegisterPNC() throws Exception {

        PNCRegistrationDto pncRegistrationDto= MockDataConstant.buildPNCRegDto();
        PNCRegistration pncRegistration = mapper.map(pncRegistrationDto, PNCRegistration.class);

        Mockito.when(repository.save(pncRegistration)).thenReturn(pncRegistration);
        PNCRegistrationDto result = service.registration(pncRegistrationDto);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidPNCRegType_ThenPatchPNC() {
        PNCRegistrationDto expectedResponse= MockDataConstant.buildPNCRegDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        PNCRegPatchDto request = PNCRegPatchDto.builder().type(PNCCRegTypeEnum.PNCREGISTRATION)
                .properties(regProp).build();
        PNCRegistration pncRegistration = mapper.map(expectedResponse, PNCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(pncRegistration));
        Mockito.when(repository.save(pncRegistration)).thenReturn(pncRegistration);
        PNCRegistrationDto result = service.patchPNC(MockDataConstant.REG_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidCoupleType_ThenPatchPNC() {
        PNCRegistrationDto expectedResponse= MockDataConstant.buildPNCRegDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        PNCRegPatchDto request = PNCRegPatchDto.builder().type(PNCCRegTypeEnum.COUPLE)
                .properties(regProp).build();
        PNCRegistration pncRegistration = mapper.map(expectedResponse, PNCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(pncRegistration));
        Mockito.when(repository.save(pncRegistration)).thenReturn(pncRegistration);
        PNCRegistrationDto result = service.patchPNC(MockDataConstant.REG_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidMensuralType_ThenPatchPNC() {
        PNCRegistrationDto expectedResponse= MockDataConstant.buildPNCRegDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        PNCRegPatchDto request = PNCRegPatchDto.builder().type(PNCCRegTypeEnum.MENSURALPERIOD)
                .properties(regProp).build();
        PNCRegistration pncRegistration = mapper.map(expectedResponse, PNCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(pncRegistration));
        Mockito.when(repository.save(pncRegistration)).thenReturn(pncRegistration);
        PNCRegistrationDto result = service.patchPNC(MockDataConstant.REG_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidDeliveryType_ThenPatchPNC() {
        PNCRegistrationDto expectedResponse= MockDataConstant.buildPNCRegDto();
        Map<String, Object> regProp = objectMapper.convertValue(expectedResponse, Map.class);
        PNCRegPatchDto request = PNCRegPatchDto.builder().type(PNCCRegTypeEnum.DELIVERYDETAILS)
                .properties(regProp).build();
        PNCRegistration pncRegistration = mapper.map(expectedResponse, PNCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(pncRegistration));
        Mockito.when(repository.save(pncRegistration)).thenReturn(pncRegistration);
        PNCRegistrationDto result = service.patchPNC(MockDataConstant.REG_ID, request);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidId_thenReadEc() {
        PNCRegistrationDto expectedResponse= MockDataConstant.buildPNCRegDto();

        PNCRegistration registration = mapper.map(expectedResponse, PNCRegistration.class);
        when(repository.findById(MockDataConstant.REG_ID)).thenReturn(Optional.of(registration));

        // call service
        PNCRegistrationDto actualResponse = service.readPNC(MockDataConstant.REG_ID);
        assertThat(actualResponse.getCouple().getHusbandId()).isEqualTo(MockDataConstant.HUSBANDID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadEc() {
        when(repository.findById(MockDataConstant.INVALID_PNCID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getPNCReg(MockDataConstant.INVALID_PNCID));
    }

    @Test
    public void whenIdRchIdDataEntryStatus_thenReturns200FilterPNC() {
        //Mock
        PNCRegistrationDto pncRegistrationDto = MockDataConstant.buildPNCRegDto();
        PNCRegistration pncRegistration = mapper.map(pncRegistrationDto, PNCRegistration.class);

        ArrayList<PNCRegistration> pncRegistrations = new ArrayList<>();
        pncRegistrations.add(pncRegistration);
        Page<PNCRegistration> pncRegistrationPage = new PageImpl<>(pncRegistrations);
        Pageable paging = PageRequest.of(0, 5);

        PNCRegistration example = PNCRegistration.builder()
                .couple(Couple.builder().rchId(MockDataConstant.RCHID).build())
                .id(MockDataConstant.REG_ID)
                .dataEntryStatus(DataEntryStatusEnum.DRAFT.name()).build();

        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(pncRegistrationPage);
        ;
        // call to the service class
        PNCRegPageResponse response = service.filterPNCReg(MockDataConstant.RCHID, MockDataConstant.REG_ID, DataEntryStatusEnum.DRAFT.name(), 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenId_thenReturns200FilterPNC() {
        //Mock
        PNCRegistrationDto pncRegistrationDto = MockDataConstant.buildPNCRegDto();
        PNCRegistration pncRegistration = mapper.map(pncRegistrationDto, PNCRegistration.class);

        ArrayList<PNCRegistration> pncRegistrations = new ArrayList<>();
        pncRegistrations.add(pncRegistration);
        Page<PNCRegistration> pncRegistrationPage = new PageImpl<>(pncRegistrations);
        Pageable paging = PageRequest.of(0, 5);

        PNCRegistration example = PNCRegistration.builder()
                .id(MockDataConstant.REG_ID).build();

        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(pncRegistrationPage);
        ;
        // call to the service class
        PNCRegPageResponse response = service.filterPNCReg(null, MockDataConstant.REG_ID, null, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

}

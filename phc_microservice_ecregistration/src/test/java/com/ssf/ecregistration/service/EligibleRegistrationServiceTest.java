package com.ssf.ecregistration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ecregistration.constant.DataEntryStatusEnum;
import com.ssf.ecregistration.constant.ECRegTypeEnum;
import com.ssf.ecregistration.constant.MockDataConstant;
import com.ssf.ecregistration.dtos.*;
import com.ssf.ecregistration.entities.EligibleRegistration;
import com.ssf.ecregistration.exception.EntityNotFoundException;
import com.ssf.ecregistration.kafka.KafkaTestConfig;
import com.ssf.ecregistration.kafka.producer.client.KafkaProducerClient;
import com.ssf.ecregistration.repository.IEligibleRegistrationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@EmbeddedKafka
@ActiveProfiles("test")
public class EligibleRegistrationServiceTest extends KafkaTestConfig {

    @MockBean
    private IEligibleRegistrationRepository repository;
    @Autowired
    private EligibleRegistrationService service;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KafkaProducerClient producerClient;
    @Test
    public void whenValidInput_ThenRegisterEC() throws Exception {
        File file = ResourceUtils.getFile("classpath:sample.json");
        EligibleRegistrationDto expectedResponse = objectMapper.readValue(file, EligibleRegistrationDto.class);
        EligibleRegistration eligibleRegistration = mapper.map(expectedResponse, EligibleRegistration.class);

        Mockito.when(repository.save(eligibleRegistration)).thenReturn(eligibleRegistration);
        EligibleRegistrationDto result = service.registration(expectedResponse);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenCoupleInput_ThenPatchECReg() throws Exception {
        // MockData
        CoupleDto coupleDto = MockDataConstant.buildCoupleDto();
        EligibleRegPatchDto patchDto = EligibleRegPatchDto.builder().type(ECRegTypeEnum.Couple)
                .properties(objectMapper.convertValue(coupleDto, Map.class))
                .build();
        EligibleRegistrationDto eligibleRegistrationDto = MockDataConstant.buildEligibleRegDto();
        EligibleRegistration eligibleRegistration = mapper.map(eligibleRegistrationDto, EligibleRegistration.class);

        Mockito.when(repository.findById(MockDataConstant.REGID)).thenReturn(Optional.ofNullable(eligibleRegistration));
        Mockito.when(repository.save(eligibleRegistration)).thenReturn(eligibleRegistration);

        //Action
        EligibleRegistrationDto result = service.registrationPatch(MockDataConstant.REGID, patchDto);
        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void whenCoupleAddInput_ThenPatchECReg() throws Exception {
        // MockData
        CoupleAdditionalDetailsDto coupleAddDto = MockDataConstant.buildCoupleAddDto();
        EligibleRegPatchDto patchDto = EligibleRegPatchDto.builder().type(ECRegTypeEnum.CoupleAdditionalDetails)
                .properties(objectMapper.convertValue(coupleAddDto, Map.class))
                .build();
        EligibleRegistrationDto eligibleRegistrationDto = MockDataConstant.buildEligibleRegDto();
        eligibleRegistrationDto.setCoupleAdditionalDetails(coupleAddDto);
        EligibleRegistration eligibleRegistration = mapper.map(eligibleRegistrationDto, EligibleRegistration.class);

        Mockito.when(repository.findById(MockDataConstant.REGID)).thenReturn(Optional.ofNullable(eligibleRegistration));
        Mockito.when(repository.save(eligibleRegistration)).thenReturn(eligibleRegistration);

        //Action
        EligibleRegistrationDto result = service.registrationPatch(MockDataConstant.REGID, patchDto);
        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void whenRchInput_ThenPatchECReg() throws Exception {
        // MockData
        RchGenerationDto rchGenerationDto = MockDataConstant.buildRchAddDto();
        EligibleRegPatchDto patchDto = EligibleRegPatchDto.builder().type(ECRegTypeEnum.RchGeneration)
                .properties(objectMapper.convertValue(rchGenerationDto, Map.class))
                .build();
        EligibleRegistrationDto eligibleRegistrationDto = MockDataConstant.buildEligibleRegDto();
        eligibleRegistrationDto.setRchGeneration(rchGenerationDto);
        EligibleRegistration eligibleRegistration = mapper.map(eligibleRegistrationDto, EligibleRegistration.class);

        Mockito.when(repository.findById(MockDataConstant.REGID)).thenReturn(Optional.ofNullable(eligibleRegistration));
        Mockito.when(repository.save(eligibleRegistration)).thenReturn(eligibleRegistration);

        //Action
        EligibleRegistrationDto result = service.registrationPatch(MockDataConstant.REGID, patchDto);
        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void whenDrafStatusInput_ThenPatchECReg() throws Exception {
        // MockData
        EligibleRegistrationDto registrationDto = MockDataConstant.buildEligibleRegDto();
        EligibleRegPatchDto patchDto = EligibleRegPatchDto.builder().type(ECRegTypeEnum.ECRegistration)
                .properties(objectMapper.convertValue(registrationDto, Map.class))
                .build();
        EligibleRegistration eligibleRegistration = mapper.map(registrationDto, EligibleRegistration.class);

        Mockito.when(repository.findById(MockDataConstant.REGID)).thenReturn(Optional.ofNullable(eligibleRegistration));
        Mockito.when(repository.save(eligibleRegistration)).thenReturn(eligibleRegistration);

        //Action
        EligibleRegistrationDto result = service.registrationPatch(MockDataConstant.REGID, patchDto);
        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidId_thenReadEc() {
        EligibleRegistrationDto registrationDto = MockDataConstant.buildEligibleRegDto();

        EligibleRegistration registration = mapper.map(registrationDto, EligibleRegistration.class);
        when(repository.findById(MockDataConstant.REGID)).thenReturn(Optional.of(registration));

        // call service
        EligibleRegistrationDto actualResponse = service.getECRegistrationById(MockDataConstant.REGID);
        assertThat(actualResponse.getCouple().getHusbandId()).isEqualTo(MockDataConstant.HUSBANDID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadEc() {
        when(repository.findById(MockDataConstant.INVALID_REGID)).thenReturn(Optional.empty());

        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getECRegistrationById(MockDataConstant.INVALID_REGID));
    }

    @Test
    public void whenRegIdAndRchId_thenReturns200FilterECReg() {
        //Mock
        EligibleRegistrationDto expectedResponse = MockDataConstant.buildEligibleRegDto();
        EligibleRegistration registration = mapper.map(expectedResponse, EligibleRegistration.class);
        ArrayList<EligibleRegistration> registrations = new ArrayList<>();
        registrations.add(registration);
        Page<EligibleRegistration> registrationPage = new PageImpl<>(registrations);
        Pageable paging = PageRequest.of(0, 5);

        Mockito.when(repository.findByIdAndRchId(MockDataConstant.REGID, MockDataConstant.RCHID, paging)).thenReturn(registrationPage);
        // call to the service class
        EligibleRegPageResponse response = service.filterEcReg(MockDataConstant.RCHID, MockDataConstant.REGID, null,0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenRegId_thenReturns200FilterECReg() {
        //Mock
        EligibleRegistrationDto expectedResponse = MockDataConstant.buildEligibleRegDto();
        EligibleRegistration registration = mapper.map(expectedResponse, EligibleRegistration.class);
        ArrayList<EligibleRegistration> registrations = new ArrayList<>();
        registrations.add(registration);
        Page<EligibleRegistration> registrationPage = new PageImpl<>(registrations);
        Pageable paging = PageRequest.of(0, 5);

        Mockito.when(repository.findById(MockDataConstant.REGID, paging)).thenReturn(registrationPage);
        // call to the service class
        EligibleRegPageResponse response = service.filterEcReg(null, MockDataConstant.REGID, null,0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenRchId_thenReturns200FilterECReg() {
        //Mock
        EligibleRegistrationDto expectedResponse = MockDataConstant.buildEligibleRegDto();
        EligibleRegistration registration = mapper.map(expectedResponse, EligibleRegistration.class);
        ArrayList<EligibleRegistration> registrations = new ArrayList<>();
        registrations.add(registration);
        Page<EligibleRegistration> registrationPage = new PageImpl<>(registrations);
        Pageable paging = PageRequest.of(0, 5);

        Mockito.when(repository.findByRchId(MockDataConstant.RCHID, paging)).thenReturn(registrationPage);
        // call to the service class
        EligibleRegPageResponse response = service.filterEcReg(MockDataConstant.RCHID, null, null,0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenRchIdAndRegId_thenReturnsNoRecordsFilterECReg() {
        Page<EligibleRegistration> registrationPage = new PageImpl<>(new ArrayList<>());
        Pageable paging = PageRequest.of(0, 5);

        Mockito.when(repository.findByIdAndRchId(MockDataConstant.RCHID, MockDataConstant.REGID, paging)).thenReturn(registrationPage);
        // call to the service class
        EligibleRegPageResponse response = service.filterEcReg(MockDataConstant.RCHID, MockDataConstant.REGID, null,0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(0);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(0);
    }

    @Test
    public void whenFindAll_thenReturns200FilterECReg() {
        EligibleRegistrationDto expectedResponse = MockDataConstant.buildEligibleRegDto();
        EligibleRegistration registration = mapper.map(expectedResponse, EligibleRegistration.class);
        ArrayList<EligibleRegistration> registrations = new ArrayList<>();
        registrations.add(registration);
        Page<EligibleRegistration> registrationPage = new PageImpl<>(registrations);
        Pageable paging = PageRequest.of(0, 5);

        Mockito.when(repository.findAll(paging)).thenReturn(registrationPage);
        // call to the service class
        EligibleRegPageResponse response = service.filterEcReg(null, null, null,0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);

    }
    @Test
    public void whenDraftStatus_thenReturns200FilterECReg() {
        //Mock
        EligibleRegistrationDto expectedResponse = MockDataConstant.buildEligibleRegDto();
        EligibleRegistration registration = mapper.map(expectedResponse, EligibleRegistration.class);
        ArrayList<EligibleRegistration> registrations = new ArrayList<>();
        registrations.add(registration);
        Page<EligibleRegistration> registrationPage = new PageImpl<>(registrations);
        Pageable paging = PageRequest.of(0, 5);

        Mockito.when(repository.findByDataEntryStatus(DataEntryStatusEnum.draft.name(), paging)).thenReturn(registrationPage);
        // call to the service class
        EligibleRegPageResponse response = service.filterEcReg(null, null, DataEntryStatusEnum.draft.name(), 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }
}

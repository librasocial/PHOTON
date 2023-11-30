package com.ssf.eligiblecouple.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.eligiblecouple.constant.DataEntryStatusEnum;
import com.ssf.eligiblecouple.constant.MockDataConstant;
import com.ssf.eligiblecouple.dtos.EligibleCoupleDto;
import com.ssf.eligiblecouple.dtos.EligibleCouplePageResponse;
import com.ssf.eligiblecouple.entities.EligibleCouple;
import com.ssf.eligiblecouple.exception.EntityNotFoundException;
import com.ssf.eligiblecouple.kafka.KafkaProducer;
import com.ssf.eligiblecouple.repository.IEligibleCoupleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@EmbeddedKafka
public class EligibleCoupleServiceTest {

    @MockBean
    private IEligibleCoupleRepository repository;
    @SpyBean
    public AuditorAware<String> auditorProvider;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EligibleCoupleService service;
    @MockBean
    private KafkaProducer kafkaProducer;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        when(auditorProvider.getCurrentAuditor()).thenReturn(Optional.of("test_user"));
    }

    @Test
    public void whenValidInput_ThenCreateEc() {
        EligibleCoupleDto expectedResponse = MockDataConstant.buildSampleEligibleCoupleDto();

        EligibleCouple eligibleCoupleEntity = mapper.map(expectedResponse, EligibleCouple.class);

        Mockito.when(repository.save(eligibleCoupleEntity)).thenReturn(eligibleCoupleEntity);
        EligibleCoupleDto result = service.createEc(expectedResponse);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenInvalidInput_ThenCreateEcRuntimeExcpetion() {
        EligibleCoupleDto expectedResponse = MockDataConstant.buildSampleEligibleCoupleDto();
        EligibleCouple eligibleCoupleEntity = mapper.map(expectedResponse, EligibleCouple.class);

        Mockito.when(repository.save(eligibleCoupleEntity)).thenThrow(new RuntimeException("Internal Server Error"));
        Assertions.assertThrows(RuntimeException.class, () -> service.createEc(expectedResponse));
    }

    @Test
    public void whenValidInput_ThenPatchEc() {
        EligibleCoupleDto expectedResponse = MockDataConstant.buildSampleEligibleCoupleDto();
        Map<String, Object> expectedResponseMap = objectMapper.convertValue(expectedResponse, Map.class);

        EligibleCouple eligibleCoupleEntity = mapper.map(expectedResponse, EligibleCouple.class);
        when(repository.findById(MockDataConstant.SERVICE_ID)).thenReturn(Optional.of(eligibleCoupleEntity));
        Mockito.when(repository.save(eligibleCoupleEntity)).thenReturn(eligibleCoupleEntity);
        EligibleCoupleDto result = service.patchEc(MockDataConstant.SERVICE_ID, expectedResponseMap);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenServiceIdAndRchId_thenReturns200FilterECS() {
        //Mock
        EligibleCoupleDto expectedResponse = MockDataConstant.buildSampleEligibleCoupleDto();
        EligibleCouple eligibleCoupleEntity = mapper.map(expectedResponse, EligibleCouple.class);
        ArrayList<EligibleCouple> eligibleCoupleEntities = new ArrayList<>();
        eligibleCoupleEntities.add(eligibleCoupleEntity);
        eligibleCoupleEntities.add(MockDataConstant.buildSampleEligibleCouplEntity());
        Page<EligibleCouple> eligibleCouples = new PageImpl<>(eligibleCoupleEntities);
        Pageable paging = PageRequest.of(0, 5);
        EligibleCouple example = EligibleCouple.builder().rchId(MockDataConstant.RCHID).id(MockDataConstant.SERVICE_ID).build();
        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(eligibleCouples);
        // call to the service class
        EligibleCouplePageResponse response = service.filterEc(MockDataConstant.RCHID, MockDataConstant.SERVICE_ID, null, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(2);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenServiceId_thenReturns200FilterECS() {
        //Mock
        EligibleCoupleDto expectedResponse = MockDataConstant.buildSampleEligibleCoupleDto();
        EligibleCouple eligibleCoupleEntity = mapper.map(expectedResponse, EligibleCouple.class);
        ArrayList<EligibleCouple> eligibleCoupleEntities = new ArrayList<>();
        eligibleCoupleEntities.add(eligibleCoupleEntity);
        Page<EligibleCouple> eligibleCouples = new PageImpl<>(eligibleCoupleEntities);
        Pageable paging = PageRequest.of(0, 5);

        EligibleCouple example = EligibleCouple.builder().id(MockDataConstant.SERVICE_ID).build();
        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(eligibleCouples);

        // call to the service class
        EligibleCouplePageResponse response = service.filterEc(null, MockDataConstant.SERVICE_ID, null, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenDraftEntryStatus_thenReturns200FilterECS() {
        //Mock
        EligibleCoupleDto expectedResponse = MockDataConstant.buildSampleEligibleCoupleDto();
        EligibleCouple eligibleCoupleEntity = mapper.map(expectedResponse, EligibleCouple.class);
        ArrayList<EligibleCouple> eligibleCoupleEntities = new ArrayList<>();
        eligibleCoupleEntities.add(eligibleCoupleEntity);
        Page<EligibleCouple> eligibleCouples = new PageImpl<>(eligibleCoupleEntities);
        Pageable paging = PageRequest.of(0, 5);

        EligibleCouple example = EligibleCouple.builder().dataEntryStatus(DataEntryStatusEnum.DRAFT.name()).build();
        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(eligibleCouples);

        // call to the service class
        EligibleCouplePageResponse response = service.filterEc(null, null, DataEntryStatusEnum.DRAFT.name(), 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenRchId_thenReturns200FilterECS() {
        //Mock
        EligibleCoupleDto expectedResponse = MockDataConstant.buildSampleEligibleCoupleDto();
        EligibleCouple eligibleCoupleEntity = mapper.map(expectedResponse, EligibleCouple.class);
        ArrayList<EligibleCouple> eligibleCoupleEntities = new ArrayList<>();
        eligibleCoupleEntities.add(eligibleCoupleEntity);
        Page<EligibleCouple> eligibleCouples = new PageImpl<>(eligibleCoupleEntities);
        Pageable paging = PageRequest.of(0, 5);

        EligibleCouple example = EligibleCouple.builder().rchId(MockDataConstant.RCHID).build();
        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(eligibleCouples);
        // call to the service class
        EligibleCouplePageResponse response = service.filterEc(MockDataConstant.RCHID, null, null, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenRchIdAndServiceId_thenReturnsNoRecordsFilterECS() {
        //Mock
        ArrayList<EligibleCouple> eligibleCoupleEntities = new ArrayList<>();
        Page<EligibleCouple> eligibleCouples = new PageImpl<>(eligibleCoupleEntities);
        Pageable paging = PageRequest.of(0, 5);

        EligibleCouple example = EligibleCouple.builder().id(MockDataConstant.SERVICE_ID).rchId(MockDataConstant.RCHID).build();
        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(eligibleCouples);
        ;
        // call to the service class
        EligibleCouplePageResponse response = service.filterEc(MockDataConstant.RCHID, MockDataConstant.SERVICE_ID, null, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isZero();
        assertThat(response.getMeta().getTotalPages()).isZero();
    }

    @Test
    public void whenValidId_thenReadEc() {
        EligibleCoupleDto expectedResponse = MockDataConstant.buildSampleEligibleCoupleDto();

        EligibleCouple eligibleCoupleEntity = mapper.map(expectedResponse, EligibleCouple.class);
        when(repository.findById(MockDataConstant.SERVICE_ID)).thenReturn(Optional.of(eligibleCoupleEntity));

        // call service
        EligibleCoupleDto actualResponse = service.readEc(MockDataConstant.SERVICE_ID);
        assertThat(actualResponse.getRchId()).isEqualTo(MockDataConstant.RCHID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadEc() {
        when(repository.findById(MockDataConstant.INVALID_SERVICE_ID)).thenReturn(Optional.empty());

        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readEc(MockDataConstant.SERVICE_ID));
    }

    @Test
    public void whenFindAll_thenReturns200FilterECS() {
        //Mock
        EligibleCoupleDto expectedResponse = MockDataConstant.buildSampleEligibleCoupleDto();
        EligibleCouple eligibleCoupleEntity = mapper.map(expectedResponse, EligibleCouple.class);
        ArrayList<EligibleCouple> eligibleCoupleEntities = new ArrayList<>();
        eligibleCoupleEntities.add(eligibleCoupleEntity);
        Page<EligibleCouple> eligibleCouples = new PageImpl<>(eligibleCoupleEntities);
        Pageable paging = PageRequest.of(0, 5);
        EligibleCouple example = EligibleCouple.builder().build();
        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(eligibleCouples);
        // call to the service class
        EligibleCouplePageResponse response = service.filterEc(null, null, null, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);

    }
}

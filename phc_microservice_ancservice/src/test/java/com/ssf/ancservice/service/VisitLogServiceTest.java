package com.ssf.ancservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ancservice.constant.MockDataConstant;
import com.ssf.ancservice.dtos.VisitLogDto;
import com.ssf.ancservice.dtos.VisitLogDtoPageResponse;
import com.ssf.ancservice.entities.ANCService;
import com.ssf.ancservice.entities.VisitLog;
import com.ssf.ancservice.exception.EntityNotFoundException;
import com.ssf.ancservice.exception.MissmatchException;
import com.ssf.ancservice.repository.IVisitLogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
public class VisitLogServiceTest {

    @MockBean
    private ANCServiceService coupleService;
    @Autowired
    private VisitLogService service;
    @MockBean
    private IVisitLogRepository repository;
    @SpyBean
    public AuditorAware<String> auditorProvider;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_ThenCreateVisitLog() {
        VisitLogDto expectedResponse = MockDataConstant.buildSampleVislitLogDto();
        VisitLog visitLog = mapper.map(expectedResponse, VisitLog.class);
        ANCService ancService = mapper.map(MockDataConstant.buildANCRegDto(), ANCService.class);

        Mockito.when(coupleService.getANCService(MockDataConstant.SERVICE_ID)).thenReturn(ancService);
        Mockito.when(repository.save(visitLog)).thenReturn(visitLog);
        VisitLogDto result = service.createVisitLog(MockDataConstant.SERVICE_ID, expectedResponse);
        assertThat(result).isNotNull();
        assertThat(result.getServiceId()).isEqualTo(expectedResponse.getServiceId());
    }

    @Test
    public void whenInvalidServiceId_ThenCreateVisiltLogEntityNotFoundExcpetion() {
         VisitLogDto expectedResponse = MockDataConstant.buildSampleVislitLogDto();
         Mockito.when(coupleService.getANCService(MockDataConstant.SERVICE_ID)).thenThrow(new EntityNotFoundException("ServiceId not Found"));
         Assertions.assertThrows(EntityNotFoundException.class, () -> service.createVisitLog(MockDataConstant.SERVICE_ID, expectedResponse));
    }

    @Test
    public void whenServiceIdMissmatch_thenReturnsMissMatchVisitLog() throws Exception {
        VisitLogDto expectedResponse = MockDataConstant.buildSampleVislitLogDto();
        expectedResponse.setServiceId("12131");
        VisitLog visitLog = mapper.map(expectedResponse, VisitLog.class);
        ANCService ancService = mapper.map(MockDataConstant.buildANCRegDto(), ANCService.class);

        Mockito.when(coupleService.getANCService(MockDataConstant.SERVICE_ID)).thenReturn(ancService);
        Mockito.when(repository.save(visitLog)).thenReturn(visitLog);
        Assertions.assertThrows(MissmatchException.class, () -> service.createVisitLog(MockDataConstant.SERVICE_ID, expectedResponse));
    }

//    @Test
    public void whenValidInput_ThenPatchVisitLog() {
        VisitLogDto expectedResponse = MockDataConstant.buildSampleVislitLogDto();
        Map<String, Object> expectedResponseMap = objectMapper.convertValue(expectedResponse, Map.class);

        VisitLog visitLog = mapper.map(expectedResponse, VisitLog.class);
        when(repository.findByIdAndServiceId(MockDataConstant.LOG_ID, MockDataConstant.SERVICE_ID)).thenReturn(Optional.of(visitLog));
        Mockito.when(repository.save(visitLog)).thenReturn(visitLog);
        VisitLogDto result = service.patchVisitLog(MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID, expectedResponseMap);
        assertThat(result).isNotNull();
    }

    @Test
    public void whenValidId_thenReadVisitLog() {
        VisitLogDto expectedResponse = MockDataConstant.buildSampleVislitLogDto();

        VisitLog visitLog = mapper.map(expectedResponse, VisitLog.class);
        when(repository.findByIdAndServiceId(MockDataConstant.LOG_ID, MockDataConstant.SERVICE_ID)).thenReturn(Optional.of(visitLog));

        // call service
        VisitLogDto actualResponse = service.readVisitLog(MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID);
        assertThat(actualResponse.getServiceId()).isEqualTo(MockDataConstant.SERVICE_ID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadVisitLog() {
        when(repository.findByIdAndServiceId(MockDataConstant.LOG_ID, MockDataConstant.SERVICE_ID)).thenReturn(Optional.empty());

        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readVisitLog(MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID));
    }

    @Test
    public void whenRchIdAndServiceIdAndLogId_thenReturnsFilterVisitLog() {
        //Mock
        VisitLogDto visitLogDto = MockDataConstant.buildSampleVislitLogDto();
        VisitLog visitLog = mapper.map(visitLogDto, VisitLog.class);

        ArrayList<VisitLog> visitLogs = new ArrayList<>();
        visitLogs.add(visitLog);
        Page<VisitLog> visitLogPage = new PageImpl<>(visitLogs);
        Pageable paging = PageRequest.of(0, 5);

        VisitLog example = VisitLog.builder().serviceId(MockDataConstant.SERVICE_ID).rchId(MockDataConstant.RCHID).id(MockDataConstant.LOG_ID).build();
        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(visitLogPage);
        ;
        // call to the service class
        VisitLogDtoPageResponse response = service.filterVisitLog(MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID, MockDataConstant.RCHID, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isEqualTo(1);
        assertThat(response.getMeta().getTotalPages()).isEqualTo(1);
    }

    @Test
    public void whenRchIdAndServiceIdAndLogId_thenReturnsNoRecordsFilterVisitLog() {
        //Mock
        ArrayList<VisitLog> visitLogs = new ArrayList<>();
        Page<VisitLog> visitLogPage = new PageImpl<>(visitLogs);
        Pageable paging = PageRequest.of(0, 5);

        VisitLog example = VisitLog.builder().serviceId(MockDataConstant.SERVICE_ID).rchId(MockDataConstant.RCHID).id(MockDataConstant.LOG_ID).build();
        Mockito.when(repository.findAll(Example.of(example), paging)).thenReturn(visitLogPage);
        ;
        // call to the service class
        VisitLogDtoPageResponse response = service.filterVisitLog(MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID, MockDataConstant.RCHID, 0, 5);

        //assert
        assertThat(response.getMeta().getTotalElements()).isZero();
        assertThat(response.getMeta().getTotalPages()).isZero();
    }

}

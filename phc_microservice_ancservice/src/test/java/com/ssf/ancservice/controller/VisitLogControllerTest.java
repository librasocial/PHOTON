package com.ssf.ancservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.ancservice.constant.MockDataConstant;
import com.ssf.ancservice.dtos.PageDto;
import com.ssf.ancservice.dtos.VisitLogDto;
import com.ssf.ancservice.dtos.VisitLogDtoPageResponse;
import com.ssf.ancservice.exception.ApiError;
import com.ssf.ancservice.exception.ApiSubError;
import com.ssf.ancservice.exception.EntityNotFoundException;
import com.ssf.ancservice.service.VisitLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VisitLogController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class VisitLogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VisitLogService service;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void whenValidInput_thenReturns201CreateVisitLog() throws Exception {
        VisitLogDto dto = MockDataConstant.buildSampleVislitLogDto();
        mockMvc.perform(post("/ancservices/{serviceId}/visitlogs", MockDataConstant.SERVICE_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvValidBodyInput_thenReturns201CreateVisitLog() throws Exception {
        mockMvc.perform(post("/ancservices/{serviceId}/visitlogs", MockDataConstant.SERVICE_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInput_thenReturns200readVisitLog() throws Exception {
        VisitLogDto dto = MockDataConstant.buildSampleVislitLogDto();

        //Service return
        Mockito.when(service.readVisitLog(MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID)).thenReturn(dto);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/ancservices/{serviceId}/visitlogs/{logId}", MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(dto));

    }

    @Test
    public void whenInvalidValidInput_thenReturns404readVisitLog() throws Exception {
        //Mock data
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("logId and serviceId not found", HttpStatus.NOT_FOUND.name(), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readVisitLog(MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID)).thenThrow(new EntityNotFoundException("logId and service id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/ancservices/{serviceId}/visitlogs/{logId}", MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void whenServiceIdAndRchIdAndLogId_thenReturns200FilterVisitLog() throws Exception {
        VisitLogDto dto = MockDataConstant.buildSampleVislitLogDto();

        VisitLogDtoPageResponse expected = VisitLogDtoPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterVisitLog(MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID, MockDataConstant.RCHID, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ancservices/visitlogs/filter")
                        .contentType("application/json")
                        .param("serviceId", MockDataConstant.SERVICE_ID)
                        .param("logId", MockDataConstant.LOG_ID)
                        .param("rchId", MockDataConstant.RCHID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        VisitLogDtoPageResponse actual = objectMapper.readValue(response, VisitLogDtoPageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenServiceIdAndLogId_thenReturns200FilterVisitLog() throws Exception {
        VisitLogDto dto = MockDataConstant.buildSampleVislitLogDto();

        VisitLogDtoPageResponse expected = VisitLogDtoPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterVisitLog(MockDataConstant.SERVICE_ID, MockDataConstant.LOG_ID, null, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ancservices/visitlogs/filter")
                        .contentType("application/json")
                        .param("serviceId", MockDataConstant.SERVICE_ID)
                        .param("logId", MockDataConstant.LOG_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        VisitLogDtoPageResponse actual = objectMapper.readValue(response, VisitLogDtoPageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenFindAll_thenReturns200FilterVisitLog() throws Exception {
        VisitLogDto dto = MockDataConstant.buildSampleVislitLogDto();

        VisitLogDtoPageResponse expected = VisitLogDtoPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterVisitLog(null, null, null, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ancservices/visitlogs/filter")
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        VisitLogDtoPageResponse actual = objectMapper.readValue(response, VisitLogDtoPageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    private ApiSubError buildApiSubError(String message, String errorCode, String field) {
        return ApiSubError.builder()
                .errorCode(errorCode)
                .message(message)
                .field(field)
                .build();
    }
}

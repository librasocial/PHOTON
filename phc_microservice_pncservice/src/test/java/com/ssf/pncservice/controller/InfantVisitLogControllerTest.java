package com.ssf.pncservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.pncservice.constant.MockDataConstant;
import com.ssf.pncservice.dtos.InfantVisitLogDto;
import com.ssf.pncservice.dtos.PageDto;
import com.ssf.pncservice.dtos.VisitLogDto;
import com.ssf.pncservice.dtos.InfantVisitLogPageResponse;
import com.ssf.pncservice.exception.ApiError;
import com.ssf.pncservice.exception.ApiSubError;
import com.ssf.pncservice.exception.EntityNotFoundException;
import com.ssf.pncservice.service.InfantVisitLogService;
import com.ssf.pncservice.service.VisitLogService;
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

@WebMvcTest(InfantVisitLogController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class InfantVisitLogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InfantVisitLogService service;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void whenValidInput_thenReturns201CreateInfantVisitLog() throws Exception {
        InfantVisitLogDto dto = MockDataConstant.buildSampleInfantVislitLogDto();
        mockMvc.perform(post("/pncservices/{childId}/infants/visitlogs", MockDataConstant.CHILD_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvValidBodyInput_thenReturns201CreateInfantVisitLog() throws Exception {
        mockMvc.perform(post("/pncservices/{childId}/infants/visitlogs", MockDataConstant.CHILD_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInput_thenReturns200readInfantVisitLog() throws Exception {
        InfantVisitLogDto dto = MockDataConstant.buildSampleInfantVislitLogDto();

        //Service return
        Mockito.when(service.readInfantVisitLog(MockDataConstant.CHILD_ID, MockDataConstant.LOG_ID)).thenReturn(dto);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/pncservices/{childId}/infants/visitlogs/{logId}", MockDataConstant.CHILD_ID, MockDataConstant.LOG_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(dto));

    }

    @Test
    public void whenInvalidValidInput_thenReturns404readInfantVisitLog() throws Exception {
        //Mock data
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("logId and serviceId not found", HttpStatus.NOT_FOUND.name(), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readInfantVisitLog(MockDataConstant.CHILD_ID, MockDataConstant.LOG_ID)).thenThrow(new EntityNotFoundException("logId and service id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/pncservices/{serviceId}/infants/visitlogs/{logId}", MockDataConstant.CHILD_ID, MockDataConstant.LOG_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void whenServiceIdAndRchId_thenReturns200FilterInfantVisitLog() throws Exception {
        InfantVisitLogDto dto = MockDataConstant.buildSampleInfantVislitLogDto();

        InfantVisitLogPageResponse expected = InfantVisitLogPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterInfantVisitLog(MockDataConstant.SERVICE_ID, MockDataConstant.CHILD_ID,MockDataConstant.LOG_ID, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/pncservices/infants/visitlogs/filter")
                        .contentType("application/json")
                        .param("serviceId", MockDataConstant.SERVICE_ID)
                        .param("childId", MockDataConstant.CHILD_ID)
                        .param("logId", MockDataConstant.LOG_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to InfantVisitLogPageResponse
        InfantVisitLogPageResponse actual = objectMapper.readValue(response, InfantVisitLogPageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenChildId_thenReturns200FilterInfantVisitLog() throws Exception {
        VisitLogDto dto = MockDataConstant.buildSampleVislitLogDto();

        InfantVisitLogPageResponse expected = InfantVisitLogPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterInfantVisitLog(null, MockDataConstant.CHILD_ID,null, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/pncservices/infants/visitlogs/filter")
                        .contentType("application/json")
                        .param("childId", MockDataConstant.CHILD_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to InfantVisitLogPageResponse
        InfantVisitLogPageResponse actual = objectMapper.readValue(response, InfantVisitLogPageResponse.class);

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

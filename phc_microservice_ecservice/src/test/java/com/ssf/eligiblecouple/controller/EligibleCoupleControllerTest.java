package com.ssf.eligiblecouple.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.eligiblecouple.constant.MockDataConstant;
import com.ssf.eligiblecouple.dtos.EligibleCoupleDto;
import com.ssf.eligiblecouple.dtos.EligibleCouplePageResponse;
import com.ssf.eligiblecouple.dtos.PageDto;
import com.ssf.eligiblecouple.exception.ApiError;
import com.ssf.eligiblecouple.exception.ApiSubError;
import com.ssf.eligiblecouple.exception.EntityNotFoundException;
import com.ssf.eligiblecouple.service.EligibleCoupleService;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EmbeddedKafka
public class EligibleCoupleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EligibleCoupleService service;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void whenValidInput_thenReturns201CreateECS() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();
        mockMvc.perform(post("/ecservices")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInValidInput_thenReturns400CreateECS() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();
        dto.setRchId(null);
        dto.setAshaWorker(null);

        mockMvc.perform(post("/ecservices")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInValidInput_thenReturns400CreateECSRuntimeException() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();
        Mockito.when(service.createEc(dto)).thenThrow(new RuntimeException("Internal Server Error"));

        mockMvc.perform(post("/ecservices")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void whenValidInput_thenReturns200readEc() throws Exception {
        EligibleCoupleDto expectedResponse = MockDataConstant.buildSampleEligibleCoupleDto();

        //Service return
        Mockito.when(service.readEc("76223232")).thenReturn(expectedResponse);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/ecservices/{serviceId}", "76223232")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponse));

    }

    @Test
    public void  whenInvalidValidInput_thenReturns404readEc() throws  Exception {
        //Mock data
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("serviceId not found", HttpStatus.NOT_FOUND.name(), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readEc("76223232")).thenThrow(new EntityNotFoundException("service id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/ecservices/{serviceId}", "76223232")
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }


    @Test
    public void whenValidInput_thenReturns201PatchECS() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();
        mockMvc.perform(patch("/ecservices/{serviceId}", 76223232)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInputWithCouple_thenReturns201PatchECS() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();
        mockMvc.perform(patch("/ecservices/{serviceId}", 76223232)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInValidInputWithCouple_thenReturns400PatchECS() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();

        dto.getCouple().setWifeId(null);
        Map<String, Object> request = objectMapper.convertValue(dto, Map.class);
        Mockito.when(service.patchEc("76223232", request)).thenThrow(Mockito.mock(ConstraintViolationException.class));
        mockMvc.perform(patch("/ecservices/{serviceId}", "76223232")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenServiceIdAndRchId_thenReturns200FilterECS() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();

        EligibleCouplePageResponse expected = EligibleCouplePageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterEc("rchId1", "76223232", "draft", 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ecservices/filter")
                        .contentType("application/json")
                        .param("rchId", "rchId1")
                        .param("serviceId", "76223232")
                        .param("dataEntryStatus", "draft")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        EligibleCouplePageResponse actual = objectMapper.readValue(response, EligibleCouplePageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenRchId_thenReturns200FilterECS() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();

        EligibleCouplePageResponse expected = EligibleCouplePageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterEc("rchId1", null, "draft", 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ecservices/filter")
                        .contentType("application/json")
                        .param("rchId", "rchId1")
                        .param("dataEntryStatus", "draft")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        EligibleCouplePageResponse actual = objectMapper.readValue(response, EligibleCouplePageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenServiceId_thenReturns200FilterECS() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();

        EligibleCouplePageResponse expected = EligibleCouplePageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterEc(null, "76223232", "draft", 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ecservices/filter")
                        .contentType("application/json")
                        .param("serviceId", "76223232")
                        .param("dataEntryStatus", "draft")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        EligibleCouplePageResponse actual = objectMapper.readValue(response, EligibleCouplePageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenFindAll_thenReturns200FilterECS() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();

        EligibleCouplePageResponse expected = EligibleCouplePageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterEc(null, null, null, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ecservices/filter")
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        EligibleCouplePageResponse actual = objectMapper.readValue(response, EligibleCouplePageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenInvalidPathUrl_then4xx() throws Exception {
        //call controller get api and assert
        mockMvc.perform(get("/ecservices/{serviceId}", "")
                        .accept("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        String badRequestJson = "{'test': 'name',}";
        mockMvc.perform(post("/ecservices")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenJHeaderIsWrong_then404() throws Exception {
        EligibleCoupleDto dto = MockDataConstant.buildSampleEligibleCoupleDto();
        Mockito.when(service.createEc(dto)).thenThrow(new UnsupportedJwtException("Invalid Signature"));

        mockMvc.perform(post("/ecservices")
                        .contentType("application/json")
                        .header("x-user-id", "123")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    private ApiSubError buildApiSubError(String message, String errorCode, String field) {
        return ApiSubError.builder()
                .errorCode(errorCode)
                .message(message)
                .field(field)
                .build();
    }
}

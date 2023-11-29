package com.ssf.labtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.labtest.constant.MockDataConstant;
import com.ssf.labtest.dtos.LabTestDTO;
import com.ssf.labtest.dtos.LabTestPageResponse;
import com.ssf.labtest.dtos.PageDTO;
import com.ssf.labtest.dtos.UpdateLabTestDTO;
import com.ssf.labtest.exception.ApiError;
import com.ssf.labtest.exception.ApiSubError;
import com.ssf.labtest.exception.EntityNotFoundException;
import com.ssf.labtest.service.LabTestServiceImpl;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LabTestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class LabTestControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LabTestServiceImpl labTestService;
    @Autowired
    private Validator validator;

    @InjectMocks
    private LabTestController labTestController;

    @Test
    public void shouldCreateLabTestWhenValidInputPassed() throws Exception {
        LabTestDTO labTestDTO = MockDataConstant.buildSampleLabTestDTO();
        mockMvc.perform(post("/labtestservices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labTestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotCreateLabTestWhenInvalidInputPassed() throws Exception {
        LabTestDTO labTestDTO = MockDataConstant.buildSampleLabTestDTO();
        labTestDTO.setCode("");
        mockMvc.perform(post("/labtestservices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labTestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnLabTestWhenServiceIdPassed() throws Exception {
        LabTestDTO expectedResponse = MockDataConstant.buildSampleLabTestDTO();
        Mockito.when(labTestService.getLabTestService("554411226")).thenReturn(expectedResponse);
        String actualResponse = mockMvc.perform(get("/labtestservices/{serviceId}", "554411226")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    public void shouldNotReturnLabTestWhenInvalidServiceIdPassed() throws Exception {
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("serviceId not found", HttpStatus.NOT_FOUND.name(), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();

        Mockito.when(labTestService.getLabTestService("76223232")).thenThrow(new EntityNotFoundException("No record in DB with the Id 76223232"));

        String errorResponseString = mockMvc.perform(get("/labtestservices/{serviceId}", "76223232")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorResponseString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void shouldUpdateLabTestWhenValidInputPassed() throws Exception {
        UpdateLabTestDTO dto = MockDataConstant.buildSampleUpdateLabTestDTO();
        mockMvc.perform(patch("/labtestservices/{serviceId}", 76223232)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldUpdateLabTestWhenInvalidInputPassed() throws Exception {
        UpdateLabTestDTO dto = MockDataConstant.buildSampleUpdateLabTestDTO();

        Mockito.when(labTestService.editLabTestService("76223232", dto)).thenThrow(Mockito.mock(ConstraintViolationException.class));
        mockMvc.perform(patch("/labtestservices/{serviceId}", "76223232")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnLabTestWhenValidLabTestIdsIsPassed() throws Exception {
        LabTestDTO dto = MockDataConstant.buildSampleLabTestDTO();

        LabTestPageResponse expected = LabTestPageResponse.builder()
                .meta(PageDTO.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(labTestService.retrieveLabTestServices("001", "", 0, 5)).thenReturn(expected);
        mockMvc.perform(get("/labtestservices/filter")
                        .contentType("application/json")
                        .param("labTestIds", "001")
                        .accept("application/json"))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void shouldFindAllWhenNoLabTestIdsPassed() throws Exception {
        LabTestDTO dto = MockDataConstant.buildSampleLabTestDTO();

        LabTestPageResponse expected = LabTestPageResponse.builder()
                .meta(PageDTO.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(labTestService.retrieveLabTestServices("001", "", 0, 5)).thenReturn(expected);
        mockMvc.perform(get("/labtestservices/filter")
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn200ErrorWhenLabTestIdsNotPresent() throws Exception {
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("No record in DB with the given filter", HttpStatus.NOT_FOUND.name(), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();

        Mockito.when(labTestService.retrieveLabTestServices("1111", "", 0, 5)).thenThrow(new EntityNotFoundException("No record in DB with the given filter"));

        String response = mockMvc.perform(get("/labtestservices/filter")
                        .contentType("application/json")
                        .param("labTestIds", "1111,random-ids")
                        .accept("application/json"))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void whenInvalidPathUrl_then4xx() throws Exception {
        //call controller get api and assert
        mockMvc.perform(get("/labtestservices/{serviceId}", "")
                        .accept("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        String badRequestJson = "{'test': 'name',}";
        mockMvc.perform(post("/labtestservices")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

//    @Test
    public void whenJHeaderIsWrong_then404() throws Exception {
        LabTestDTO dto = MockDataConstant.buildSampleLabTestDTO();
        Mockito.when(labTestService.createLabTest(dto)).thenThrow(new UnsupportedJwtException("Invalid Signature"));

        mockMvc.perform(post("/labtestservices")
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

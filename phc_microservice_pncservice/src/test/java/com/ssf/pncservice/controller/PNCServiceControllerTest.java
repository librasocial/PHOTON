package com.ssf.pncservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncservice.constant.MockDataConstant;
import com.ssf.pncservice.dtos.PNCPageResponse;
import com.ssf.pncservice.dtos.PNCServiceDto;
import com.ssf.pncservice.dtos.PageDto;
import com.ssf.pncservice.exception.ApiError;
import com.ssf.pncservice.exception.ApiSubError;
import com.ssf.pncservice.exception.EntityNotFoundException;
import com.ssf.pncservice.service.PNCServiceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PNCServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PNCServiceService service;

    @Test
    public void whenValidInput_thenReturns201PNC() throws Exception {
        //Mock
        PNCServiceDto pncServiceDto = MockDataConstant.buildPNCServiceDto();
        //perform and assert
        mockMvc.perform(post("/pncservices")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(pncServiceDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidInput_thenReturns400PNC() throws Exception {
        //Mock
        PNCServiceDto pncServiceDto = MockDataConstant.buildPNCServiceDto();
        pncServiceDto.getCouple().setHusbandId(null);
        //perform and assert
        mockMvc.perform(post("/pncservices")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(pncServiceDto)))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        //Mock
        String badRequestJson = "{'test': 'name',}";
        //perform and assert
        mockMvc.perform(post("/pncservices")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInput_thenReturns201PatchPNC() throws Exception {
        PNCServiceDto pncServiceDto = MockDataConstant.buildPNCServiceDto();
        mockMvc.perform(patch("/pncservices/{serviceId}", MockDataConstant.PNCID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(pncServiceDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInputWithCouple_thenReturns201PatchPNC() throws Exception {
        PNCServiceDto pncServiceDto = MockDataConstant.buildPNCServiceDto();
        mockMvc.perform(patch("/pncservices/{serviceId}", MockDataConstant.PNCID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(pncServiceDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInput_thenReturns200readPNCService() throws Exception {
        PNCServiceDto pncServiceDto = MockDataConstant.buildPNCServiceDto();
        //Service return
        Mockito.when(service.getPNC(MockDataConstant.PNCID)).thenReturn(pncServiceDto);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/pncservices/{serviceId}", MockDataConstant.PNCID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(pncServiceDto));

    }
    @Test
    public void whenInvalidValidInput_thenReturns404readPNCService() throws Exception {
        //Mock datax
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("Service not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.getPNC(MockDataConstant.PNCID)).thenThrow(new EntityNotFoundException("Service id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/pncservices/{serviceId}", MockDataConstant.PNCID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void whenIdRchIdServiceId_thenReturns200FilterPNC() throws Exception {
        PNCServiceDto pncServiceDto= MockDataConstant.buildPNCServiceDto();

        PNCPageResponse expected = PNCPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(pncServiceDto)
                .build();

        Mockito.when(service.getPNCByFilter(MockDataConstant.RCHID, MockDataConstant.PNCID, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/pncservices/filter")
                        .contentType("application/json")
                        .param("rchId", MockDataConstant.RCHID)
                        .param("serviceId", MockDataConstant.PNCID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to PNCRegPageResponse
        PNCPageResponse actual = objectMapper.readValue(response, PNCPageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenServiceId_thenReturns200FilterPNC() throws Exception {
        PNCServiceDto pncServiceDto= MockDataConstant.buildPNCServiceDto();

        PNCPageResponse expected = PNCPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(pncServiceDto)
                .build();

        Mockito.when(service.getPNCByFilter(null, MockDataConstant.PNCID, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/pncservices/filter")
                        .contentType("application/json")
                       .param("serviceId", MockDataConstant.PNCID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to PNCRegPageResponse
        PNCPageResponse actual = objectMapper.readValue(response, PNCPageResponse.class);

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

package com.ssf.pncregistration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncregistration.constant.DataEntryStatusEnum;
import com.ssf.pncregistration.constant.MockDataConstant;
import com.ssf.pncregistration.dtos.PNCRegPageResponse;
import com.ssf.pncregistration.dtos.PNCRegistrationDto;
import com.ssf.pncregistration.dtos.PageDto;
import com.ssf.pncregistration.exception.ApiError;
import com.ssf.pncregistration.exception.ApiSubError;
import com.ssf.pncregistration.exception.EntityNotFoundException;
import com.ssf.pncregistration.service.PNCRegistrationService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PNCRegistrationController.class)
@ActiveProfiles("test")
@EmbeddedKafka
public class PNCRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PNCRegistrationService service;

    @Test
    @Disabled
    public void whenValidInput_thenReturns201RegisterPNC() throws Exception {
        //Mock
        PNCRegistrationDto registrationDto= MockDataConstant.buildPNCRegDto();
        //perform and assert
        mockMvc.perform(post("/pncregistration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isOk());
    }


    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        //Mock
        String badRequestJson = "{'test': 'name',}";
        //perform and assert
        mockMvc.perform(post("/pncregistration")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInput_thenReturns201PatchPNC() throws Exception {
        PNCRegistrationDto registrationDto= MockDataConstant.buildPNCRegDto();
        mockMvc.perform(patch("/pncregistration/{registrationId}", MockDataConstant.REG_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInputWithCouple_thenReturns201PatchPNC() throws Exception {
        PNCRegistrationDto registrationDto= MockDataConstant.buildPNCRegDto();
        mockMvc.perform(patch("/pncregistration/{registrationId}", MockDataConstant.REG_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInput_thenReturns200readPNCReg() throws Exception {
        PNCRegistrationDto registrationDto= MockDataConstant.buildPNCRegDto();
        //Service return
        Mockito.when(service.readPNC(MockDataConstant.REG_ID)).thenReturn(registrationDto);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/pncregistration/{registrationId}", MockDataConstant.REG_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        AssertionsForClassTypes.assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(registrationDto));

    }
    @Test
    public void whenInvalidValidInput_thenReturns404readPNCReg() throws Exception {
        //Mock datax
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("REG not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readPNC(MockDataConstant.REG_ID)).thenThrow(new EntityNotFoundException("REG id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/pncregistration/{registrationId}", MockDataConstant.REG_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void whenIdRchIdDataEntryStatus_thenReturns200FilterPNC() throws Exception {
        PNCRegistrationDto registrationDto= MockDataConstant.buildPNCRegDto();

        PNCRegPageResponse expected = PNCRegPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(registrationDto)
                .build();

        Mockito.when(service.filterPNCReg(MockDataConstant.RCHID, MockDataConstant.REG_ID, DataEntryStatusEnum.DRAFT.name(), 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/pncregistration/filter")
                        .contentType("application/json")
                        .param("rchId", MockDataConstant.RCHID)
                        .param("regId", MockDataConstant.REG_ID)
                        .param("dataEntryStatus", DataEntryStatusEnum.DRAFT.name())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to PNCRegPageResponse
        PNCRegPageResponse actual = objectMapper.readValue(response, PNCRegPageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenId_thenReturns200FilterPNC() throws Exception {
        PNCRegistrationDto dto = MockDataConstant.buildPNCRegDto();

        PNCRegPageResponse expected = PNCRegPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterPNCReg(null, MockDataConstant.REG_ID, null, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/pncregistration/filter")
                        .contentType("application/json")
                        .param("regId", MockDataConstant.REG_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to PNCRegPageResponse
        PNCRegPageResponse actual = objectMapper.readValue(response, PNCRegPageResponse.class);

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

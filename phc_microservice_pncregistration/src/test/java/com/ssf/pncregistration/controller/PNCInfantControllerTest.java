package com.ssf.pncregistration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncregistration.constant.MockDataConstant;
import com.ssf.pncregistration.dtos.*;
import com.ssf.pncregistration.exception.ApiError;
import com.ssf.pncregistration.exception.ApiSubError;
import com.ssf.pncregistration.exception.EntityNotFoundException;
import com.ssf.pncregistration.service.PNCInfantService;
import org.assertj.core.api.AssertionsForClassTypes;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PNCInfantController.class)
@ActiveProfiles("test")
@EmbeddedKafka
public class PNCInfantControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PNCInfantService service;

    @Test
    public void whenValidInput_thenReturns201CreateInfant() throws Exception {
        //Mock
        InfantDto infantDto = MockDataConstant.buildInfantDto();
        Map<String, List<InfantDto>> map = new HashMap<String, List<InfantDto>>(); 
        map.put("infantregistration", Arrays.asList(infantDto));
        
        //perform and assert
        mockMvc.perform(post("/pncregistration/{registrationId}/infants", MockDataConstant.PNCID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInput_thenReturns201PatchInfant() throws Exception {
        //Mock
        InfantDto infantDto = MockDataConstant.buildInfantDto();
        Map<String, Object> infantMap = objectMapper.convertValue(infantDto, Map.class);
        InfantPatchDto infantPatchDto = InfantPatchDto.builder()
                .type(InfantTypeEnum.INFANT)
                .properties(infantMap)
                .build();

        //perform and assert
        mockMvc.perform(patch("/pncregistration/{registrationId}/infants/{id}",
                        MockDataConstant.PNCID, MockDataConstant.INFANT_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(infantPatchDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInputWithImmunization_thenReturns201PatchPNC() throws Exception {
        //Mock
        ImmunizationDto immunizationDto = MockDataConstant.buildImmunizationo();
        Map<String, Object> infantMap = objectMapper.convertValue(immunizationDto, Map.class);
        InfantPatchDto infantPatchDto = InfantPatchDto.builder()
                .type(InfantTypeEnum.IMMUNIZATION)
                .properties(infantMap)
                .build();

        //perform and assert
        mockMvc.perform(patch("/pncregistration/{registrationId}/infants/{id}",
                        MockDataConstant.PNCID, MockDataConstant.INFANT_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(infantPatchDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInput_thenReturns200readInfant() throws Exception {
        InfantDto infantDto = MockDataConstant.buildInfantDto();
        //Service return
        Mockito.when(service.readInfants(MockDataConstant.PNCID, MockDataConstant.INFANT_ID)).thenReturn(infantDto);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/pncregistration/{registrationId}/infants/{id}",
                        MockDataConstant.PNCID, MockDataConstant.INFANT_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        AssertionsForClassTypes.assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(infantDto));

    }

    @Test
    public void whenInvalidValidInput_thenReturns404readInfant() throws Exception {
        //Mock datax
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("Id not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readInfants(MockDataConstant.PNCID, MockDataConstant.INFANT_ID)).thenThrow(new EntityNotFoundException("PNCID id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/pncregistration/{registrationId}/infants/{id}", MockDataConstant.PNCID, MockDataConstant.INFANT_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void whenIdRegId_thenReturns200FilterInfant() throws Exception {
        InfantDto infantDto = MockDataConstant.buildInfantDto();

        InfantPageResponse expected = InfantPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(infantDto)
                .build();

        Mockito.when(service.filterInfants(MockDataConstant.INFANT_ID, MockDataConstant.PNCID, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/pncregistration/infants/filter")
                        .contentType("application/json")
                        .param("infantId", MockDataConstant.INFANT_ID)
                        .param("registrationId", MockDataConstant.PNCID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to PNCRegPageResponse
        InfantPageResponse actual = objectMapper.readValue(response, InfantPageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenId_thenReturns200FilterInfant() throws Exception {
        InfantDto infantDto = MockDataConstant.buildInfantDto();

        InfantPageResponse expected = InfantPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(infantDto)
                .build();

        Mockito.when(service.filterInfants(MockDataConstant.INFANT_ID, null, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/pncregistration/infants/filter")
                        .contentType("application/json")
                        .param("infantId", MockDataConstant.INFANT_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to PNCRegPageResponse
        InfantPageResponse actual = objectMapper.readValue(response, InfantPageResponse.class);

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

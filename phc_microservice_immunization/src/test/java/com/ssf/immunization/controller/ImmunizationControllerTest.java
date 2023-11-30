package com.ssf.immunization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.immunization.dtos.FilterDto;
import com.ssf.immunization.dtos.ImmunizationDto;
import com.ssf.immunization.exception.ApiError;
import com.ssf.immunization.exception.ApiSubError;
import com.ssf.immunization.exception.EntityNotFoundException;
import com.ssf.immunization.service.ImmunizationService;
import com.ssf.immunization.constant.MockDataConstant;
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


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImmunizationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ImmunizationControllerTest {
        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockBean
        private ImmunizationService service;

        @Test
        public void whenValidInput_thenReturns201Immun() throws Exception {
            //Mock
            ImmunizationDto request = MockDataConstant.buildImmunizationDto();
            //perform and assert
            mockMvc.perform(post("/immunizations")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
        @Test
        public void whenJsonRequestBad_then400() throws Exception {
            //Mock
            String badRequestJson = "{'test': 'name',}";
            //perform and assert
            mockMvc.perform(post("/immunizations")
                            .contentType("application/json")
                            .content(badRequestJson))
                    .andExpect(status().isBadRequest());
        }

    @Test
    public void whenValidInput_thenReturns200readImmune() throws Exception {
        ImmunizationDto request = MockDataConstant.buildImmunizationDto();//Service return
        Mockito.when(service.readImmun(MockDataConstant.IMMUN_ID)).thenReturn(request);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/immunizations/{immunizationId}", MockDataConstant.IMMUN_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(request));

    }
    @Test
    public void whenInvalidValidInput_thenReturns404readImmune() throws Exception {
        //Mock datax
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(MockDataConstant.buildApiSubError("Immune ID not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readImmun(MockDataConstant.INVALID_IMMUN_ID)).thenThrow(new EntityNotFoundException("Immunization id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/immunizations/{immunizationId}", MockDataConstant.INVALID_IMMUN_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void whenValidFilterInput_thenReturns200Immun() throws Exception {
        //Mock
        FilterDto request = MockDataConstant.buildFullFilterDto();
        //perform and assert
        mockMvc.perform(post("/immunizations/filter")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPartial_thenReturns200Immun() throws Exception {
        //Mock
        ImmunizationDto immunizationDto = MockDataConstant.buildImmunizationDto();
        //perform and assert
        mockMvc.perform(patch("/immunizations/{immunizationId}", MockDataConstant.IMMUN_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(immunizationDto)))
                .andExpect(status().isOk());
    }
}

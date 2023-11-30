package com.ssf.immunizationrec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.immunizationrec.constant.MockDataConstant;
import com.ssf.immunizationrec.dtos.FilterDto;
import com.ssf.immunizationrec.dtos.ImmunizationRecDto;
import com.ssf.immunizationrec.exception.ApiError;
import com.ssf.immunizationrec.exception.ApiSubError;
import com.ssf.immunizationrec.exception.EntityNotFoundException;
import com.ssf.immunizationrec.service.ImmunizationRecService;
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

@WebMvcTest(ImmunizationRecController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ImmunizationRecControllerTest {
        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockBean
        private ImmunizationRecService service;

        @Test
        public void whenValidInput_thenReturns201ImmunRec() throws Exception {
            //Mock
            ImmunizationRecDto request = MockDataConstant.buildImmunizationRecDto();
            //perform and assert
            mockMvc.perform(post("/immunizationRecommendations")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
        @Test
        public void whenJsonRequestBad_then400() throws Exception {
            //Mock
            String badRequestJson = "{'test': 'name',}";
            //perform and assert
            mockMvc.perform(post("/immunizationRecommendations")
                            .contentType("application/json")
                            .content(badRequestJson))
                    .andExpect(status().isBadRequest());
        }

    @Test
    public void whenValidInput_thenReturns200readImmuneRec() throws Exception {
        ImmunizationRecDto request = MockDataConstant.buildImmunizationRecDto();//Service return
        Mockito.when(service.readImmunRec(MockDataConstant.IMMUN_ID)).thenReturn(request);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/immunizationRecommendations/{immunizationRecommendationId}", MockDataConstant.IMMUN_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(request));

    }
    @Test
    public void whenInvalidValidInput_thenReturns404readImmuneRec() throws Exception {
        //Mock datax
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(MockDataConstant.buildApiSubError("ImmuneRec ID not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readImmunRec(MockDataConstant.INVALID_IMMUN_ID)).thenThrow(new EntityNotFoundException("Service id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/immunizationRecommendations/{immunizationRecommendationId}", MockDataConstant.INVALID_IMMUN_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void whenValidFilterInput_thenReturns200ImmunRec() throws Exception {
        //Mock
        FilterDto request = MockDataConstant.buildFullFilterDto();
        //perform and assert
        mockMvc.perform(post("/immunizationRecommendations/filter")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPartial_thenReturns200ImmunRec() throws Exception {
        //Mock
        ImmunizationRecDto immunizationRecDto = MockDataConstant.buildImmunizationRecDto();
        //perform and assert
        mockMvc.perform(patch("/immunizationRecommendations/{immunizationRecommendationId}", MockDataConstant.IMMUN_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(immunizationRecDto)))
                .andExpect(status().isOk());
    }
}

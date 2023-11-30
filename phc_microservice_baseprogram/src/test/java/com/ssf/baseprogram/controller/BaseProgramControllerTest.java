package com.ssf.baseprogram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.baseprogram.constant.MockDataConstant;
import com.ssf.baseprogram.dtos.BaseProgramDto;
import com.ssf.baseprogram.exception.ApiError;
import com.ssf.baseprogram.exception.ApiSubError;
import com.ssf.baseprogram.exception.EntityNotFoundException;
import com.ssf.baseprogram.service.BaseProgramService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BaseProgramController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@EmbeddedKafka
public class BaseProgramControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockBean
        private BaseProgramService service;

        @Test
        public void whenValidInput_thenReturns201BP() throws Exception {
            //Mock
            BaseProgramDto request = MockDataConstant.buildBaseProgramDto();
            //perform and assert
            mockMvc.perform(post("/baseprograms")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
        @Test
        public void whenJsonRequestBad_then400() throws Exception {
            //Mock
            String badRequestJson = "{'test': 'name',}";
            //perform and assert
            mockMvc.perform(post("/baseprograms")
                            .contentType("application/json")
                            .content(badRequestJson))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void whenValidInput_thenReturns200readBP() throws Exception {
            BaseProgramDto request = MockDataConstant.buildBaseProgramDto();//Service return
            Mockito.when(service.read(MockDataConstant.BASE_PROGRAM_ID)).thenReturn(request);

            //call controller get api
            String actualResponse = mockMvc.perform(get("/baseprograms/{programId}", MockDataConstant.BASE_PROGRAM_ID)
                            .accept("application/json"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            //Assert Expected and actual
            assertThat(actualResponse).isEqualToIgnoringWhitespace(
                    objectMapper.writeValueAsString(request));

        }
        @Test
        public void whenInvalidValidInput_thenReturns404readBP() throws Exception {
            //Mock datax
            List<ApiSubError> errorList = new ArrayList<>();
            errorList.add(MockDataConstant.buildApiSubError("baseprogram ID not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
            ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
            //Service return
            Mockito.when(service.read(MockDataConstant.BASE_PROGRAM_ID)).thenThrow(new EntityNotFoundException("baseprogram id not found"));

            //call controller get api
            String errorRespString = mockMvc.perform(get("/baseprograms/{programId}", MockDataConstant.BASE_PROGRAM_ID)
                            .accept("application/json"))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString();
            ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
            assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
        }
}

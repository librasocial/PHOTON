package com.ssf.ancservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ancservice.constant.MockDataConstant;
import com.ssf.ancservice.dtos.ANCServiceDto;
import com.ssf.ancservice.exception.ApiError;
import com.ssf.ancservice.exception.ApiSubError;
import com.ssf.ancservice.service.ANCServiceService;
import io.jsonwebtoken.UnsupportedJwtException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ANCServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ANCServiceService service;

    @Test
    public void whenValidInput_thenReturns201RegisterANC() throws Exception {
        //Mock
        ANCServiceDto registrationDto = MockDataConstant.buildANCRegDto();
        //perform and assert
        mockMvc.perform(post("/ancservices")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidInput_thenReturns400RegisterANC() throws Exception {
        //Mock
        ANCServiceDto registrationDto = MockDataConstant.buildANCRegDto();
        registrationDto.getCouple().setHusbandId(null);
        //perform and assert
        mockMvc.perform(post("/ancservices")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInValidInput_thenReturns400RegisterECSRuntimeException() throws Exception {
        //Mock
        ANCServiceDto registrationDto = MockDataConstant.buildANCRegDto();
        //perform and assert
        Mockito.when(service.createANC(registrationDto)).thenThrow(new RuntimeException("Internal Server Error"));
        mockMvc.perform(post("/ancservices")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        //Mock
        String badRequestJson = "{'test': 'name',}";
        //perform and assert
        mockMvc.perform(post("/ancservices")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenJHeaderIsWrong_then404() throws Exception {
        //Mock
        ANCServiceDto registrationDto = MockDataConstant.buildANCRegDto();
        Mockito.when(service.createANC(registrationDto)).thenThrow(new UnsupportedJwtException("Invalid Signature"));
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(MockDataConstant.buildApiSubError("Unauthorized", String.valueOf(HttpStatus.UNAUTHORIZED.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();

        //perform and assert
        String errResponse = mockMvc.perform(post("/ancservices")
                        .contentType("application/json")
                        .header("x-user-id", "123")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();
        //Check Error code
        ApiError actualError = objectMapper.readValue(errResponse, ApiError.class);
        assertThat(actualError.getErrors().get(0).getErrorCode()).isEqualTo(expectedErrResp.getErrors().get(0).getErrorCode());
    }

    @Test
    public void whenValidInput_thenReturns201PatchANC() throws Exception {
        ANCServiceDto dto = MockDataConstant.buildANCRegDto();
        mockMvc.perform(patch("/ancservices/{serviceId}", MockDataConstant.SERVICE_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInputWithCouple_thenReturns201PatchANC() throws Exception {
        ANCServiceDto dto = MockDataConstant.buildANCRegDto();
        mockMvc.perform(patch("/ancservices/{serviceId}", MockDataConstant.SERVICE_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInputWithCouple() throws Exception {
        ANCServiceDto dto = MockDataConstant.buildANCRegDto();
        mockMvc.perform(get("/ancservices/{serviceId}", MockDataConstant.SERVICE_ID)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetANCServiceFilter() throws Exception {
        ANCServiceDto dto = MockDataConstant.buildANCRegDto();
        mockMvc.perform(get("/ancservices/filter?page=0&size=5")
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful());
    }
}

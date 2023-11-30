package com.ssf.ancregistration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ancregistration.constant.DataEntryStatusEnum;
import com.ssf.ancregistration.constant.MockDataConstant;
import com.ssf.ancregistration.dtos.ANCRegPageResponse;
import com.ssf.ancregistration.dtos.ANCRegistrationDto;
import com.ssf.ancregistration.dtos.PageDto;
import com.ssf.ancregistration.exception.ApiError;
import com.ssf.ancregistration.exception.ApiSubError;
import com.ssf.ancregistration.exception.EntityNotFoundException;
import com.ssf.ancregistration.service.ANCRegistrationService;
import io.jsonwebtoken.UnsupportedJwtException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
public class ANCRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ANCRegistrationService service;

    @Test
    public void whenValidInput_thenReturns201RegisterANC() throws Exception {
        //Mock
        ANCRegistrationDto registrationDto= MockDataConstant.buildANCRegDto();
        //perform and assert
        mockMvc.perform(post("/ancregistration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenNullCoupleHisbandId_thenReturns400RegisterANC() throws Exception {
        //Mock
        ANCRegistrationDto registrationDto= MockDataConstant.buildANCRegDto();
        registrationDto.getCouple().setHusbandId(null);
        //perform and assert
        mockMvc.perform(post("/ancregistration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInValidInput_thenReturns400RegisterECSRuntimeException() throws Exception {
        //Mock
        ANCRegistrationDto registrationDto= MockDataConstant.buildANCRegDto();
        //perform and assert
        Mockito.when(service.registration(registrationDto)).thenThrow(new RuntimeException("Internal Server Error"));
        mockMvc.perform(post("/ancregistration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        //Mock
        String badRequestJson = "{'test': 'name',}";
        //perform and assert
        mockMvc.perform(post("/ancregistration")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenJHeaderIsWrong_then404() throws Exception {
        //Mock
        ANCRegistrationDto registrationDto= MockDataConstant.buildANCRegDto();
        Mockito.when(service.registration(registrationDto)).thenThrow(new UnsupportedJwtException("Invalid Signature"));
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(MockDataConstant.buildApiSubError("Unauthorized", String.valueOf(HttpStatus.UNAUTHORIZED.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();

        //perform and assert
        String errResponse = mockMvc.perform(post("/ancregistration")
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
        ANCRegistrationDto dto = MockDataConstant.buildANCRegDto();
        mockMvc.perform(patch("/ancregistration/{serviceId}", MockDataConstant.REG_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInputWithCouple_thenReturns201PatchANC() throws Exception {
        ANCRegistrationDto dto = MockDataConstant.buildANCRegDto();
        mockMvc.perform(patch("/ancregistration/{serviceId}", MockDataConstant.REG_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInput_thenReturns200readANCReg() throws Exception {
        ANCRegistrationDto expectedResponse = MockDataConstant.buildANCRegDto();
        //Service return
        Mockito.when(service.readANC(MockDataConstant.REG_ID)).thenReturn(expectedResponse);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/ancregistration/{registrationId}", MockDataConstant.REG_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        AssertionsForClassTypes.assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponse));

    }
    @Test
    public void whenInvalidValidInput_thenReturns404readEcReg() throws Exception {
        //Mock data
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("REG not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readANC(MockDataConstant.REG_ID)).thenThrow(new EntityNotFoundException("REG id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/ancregistration/{registrationId}", MockDataConstant.REG_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        AssertionsForClassTypes.assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void whenIdRchIdDataEntryStatus_thenReturns200FilterANC() throws Exception {
        ANCRegistrationDto dto = MockDataConstant.buildANCRegDto();

        ANCRegPageResponse expected = ANCRegPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterANCReg(MockDataConstant.RCHID, MockDataConstant.REG_ID, DataEntryStatusEnum.DRAFT.name(), 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ancregistration/filter")
                        .contentType("application/json")
                        .param("rchId", MockDataConstant.RCHID)
                        .param("regId", MockDataConstant.REG_ID)
                        .param("dataEntryStatus", DataEntryStatusEnum.DRAFT.name())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to ANCRegPageResponse
        ANCRegPageResponse actual = objectMapper.readValue(response, ANCRegPageResponse.class);

        //Validate the response contain one element
        AssertionsForClassTypes.assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        AssertionsForClassTypes.assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenId_thenReturns200FilterANC() throws Exception {
        ANCRegistrationDto dto = MockDataConstant.buildANCRegDto();

        ANCRegPageResponse expected = ANCRegPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterANCReg(null, MockDataConstant.REG_ID, null, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ancregistration/filter")
                        .contentType("application/json")
                        .param("regId", MockDataConstant.REG_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to ANCRegPageResponse
        ANCRegPageResponse actual = objectMapper.readValue(response, ANCRegPageResponse.class);

        //Validate the response contain one element
        AssertionsForClassTypes.assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        AssertionsForClassTypes.assertThat(actual.getData()).isNotNull();
    }


    private ApiSubError buildApiSubError(String message, String errorCode, String field) {
        return ApiSubError.builder()
                .errorCode(errorCode)
                .message(message)
                .field(field)
                .build();
    }
}

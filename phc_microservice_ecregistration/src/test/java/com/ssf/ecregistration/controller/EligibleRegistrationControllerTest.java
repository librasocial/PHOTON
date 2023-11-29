package com.ssf.ecregistration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ecregistration.constant.ECRegTypeEnum;
import com.ssf.ecregistration.constant.MockDataConstant;
import com.ssf.ecregistration.dtos.*;
import com.ssf.ecregistration.exception.ApiError;
import com.ssf.ecregistration.exception.ApiSubError;
import com.ssf.ecregistration.exception.EntityNotFoundException;
import com.ssf.ecregistration.service.EligibleRegistrationService;
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
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@EmbeddedKafka
@ActiveProfiles("test")
public class EligibleRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EligibleRegistrationService service;

    @Test
    public void whenValidInput_thenReturns201RegisterEC() throws Exception {
        //Mock
         File file = ResourceUtils.getFile("classpath:sample.json");
         EligibleRegistrationDto  registrationDto=  objectMapper.readValue(file, EligibleRegistrationDto.class);
        //perform and assert
         mockMvc.perform(post("/ecregistrations")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(registrationDto)))
                    .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidInput_thenReturns400RegisterEC() throws Exception {
        //Mock
        File file = ResourceUtils.getFile("classpath:sample.json");
        EligibleRegistrationDto  registrationDto=  objectMapper.readValue(file, EligibleRegistrationDto.class);
        registrationDto.getCouple().setHusbandId(null);
        //perform and assert
        mockMvc.perform(post("/ecregistrations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInValidInput_thenReturns400RegisterECSRuntimeException() throws Exception {
        //Mock
        File file = ResourceUtils.getFile("classpath:sample.json");
        EligibleRegistrationDto  registrationDto=  objectMapper.readValue(file, EligibleRegistrationDto.class);
        //perform and assert
        Mockito.when(service.registration(registrationDto)).thenThrow(new RuntimeException("Internal Server Error"));
        mockMvc.perform(post("/ecregistrations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().is5xxServerError());
    }


    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        //Mock
        String badRequestJson = "{'test': 'name',}";
        //perform and assert
        mockMvc.perform(post("/ecregistrations")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenJHeaderIsWrong_then404() throws Exception {
        //Mock
        File file = ResourceUtils.getFile("classpath:sample.json");
        EligibleRegistrationDto  registrationDto=  objectMapper.readValue(file, EligibleRegistrationDto.class);
        registrationDto.getCouple().setHusbandId("121");
        registrationDto.getCouple().setWifeId("122");
        Mockito.when(service.registration(registrationDto)).thenThrow(new UnsupportedJwtException("Invalid Signature"));
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("Unauthorized", String.valueOf(HttpStatus.UNAUTHORIZED.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();

        //perform and assert
        String errResponse = mockMvc.perform(post("/ecregistrations")
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
    public void whenCoupleInput_thenReturns201ECPatchRegister() throws Exception {
        //Mock
        CoupleDto coupleDto = MockDataConstant.buildCoupleDto();
        EligibleRegPatchDto expectedResponse = EligibleRegPatchDto.builder().type(ECRegTypeEnum.Couple)
                .properties(objectMapper.convertValue(coupleDto, HashMap.class))
                .build();
        //perform and assert
        mockMvc.perform(patch("/ecregistrations/{registrationId}", MockDataConstant.REGID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponse)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCoupleAddInput_thenReturns201ECPatchRegister() throws Exception {
        //Mock
        CoupleAdditionalDetailsDto coupleAdditionalDetailsDto = MockDataConstant.buildCoupleAddDto();
        EligibleRegPatchDto expectedResponse = EligibleRegPatchDto.builder().type(ECRegTypeEnum.CoupleAdditionalDetails)
                .properties(objectMapper.convertValue(coupleAdditionalDetailsDto, HashMap.class))
                .build();
        //perform and assert
        mockMvc.perform(patch("/ecregistrations/{registrationId}", MockDataConstant.REGID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponse)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenRchInput_thenReturns201ECPatchRegister() throws Exception {
        //Mock
        RchGenerationDto rchGenerationDto = MockDataConstant.buildRchAddDto();
        EligibleRegPatchDto expectedResponse = EligibleRegPatchDto.builder().type(ECRegTypeEnum.RchGeneration)
                .properties(objectMapper.convertValue(rchGenerationDto, HashMap.class))
                .build();
        //perform and assert
        mockMvc.perform(patch("/ecregistrations/{registrationId}", MockDataConstant.REGID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponse)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInput_thenReturns200readEcReg() throws Exception {
        EligibleRegistrationDto expectedResponse = MockDataConstant.buildEligibleRegDto();

        //Service return
        Mockito.when(service.getECRegistrationById(MockDataConstant.REGID)).thenReturn(expectedResponse);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/ecregistrations/{registrationId}", MockDataConstant.REGID)
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
        Mockito.when(service.getECRegistrationById(MockDataConstant.REGID)).thenThrow(new EntityNotFoundException("REG id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/ecregistrations/{registrationId}", MockDataConstant.REGID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        AssertionsForClassTypes.assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void whenServiceIdAndRchId_thenReturns200FilterECReg() throws Exception {
        EligibleRegistrationDto dto = MockDataConstant.buildEligibleRegDto();

        EligibleRegPageResponse expected = EligibleRegPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterEcReg(MockDataConstant.RCHID, MockDataConstant.REGID, null,0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ecregistrations/filter")
                        .contentType("application/json")
                        .param("rchId", MockDataConstant.RCHID)
                        .param("regId", MockDataConstant.REGID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        EligibleRegPageResponse actual = objectMapper.readValue(response, EligibleRegPageResponse.class);

        //Validate the response contain one element
        AssertionsForClassTypes.assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        AssertionsForClassTypes.assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenRchId_thenReturns200FilterECReg() throws Exception {
        EligibleRegistrationDto dto = MockDataConstant.buildEligibleRegDto();

        EligibleRegPageResponse expected = EligibleRegPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterEcReg(MockDataConstant.RCHID, null, null,0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ecregistrations/filter")
                        .contentType("application/json")
                        .param("rchId", MockDataConstant.RCHID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        EligibleRegPageResponse actual = objectMapper.readValue(response, EligibleRegPageResponse.class);

        //Validate the response contain one element
        AssertionsForClassTypes.assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        AssertionsForClassTypes.assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenServiceId_thenReturns200FilterECReg() throws Exception {
        EligibleRegistrationDto dto = MockDataConstant.buildEligibleRegDto();

        EligibleRegPageResponse expected = EligibleRegPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterEcReg(null, MockDataConstant.REGID, null,0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ecregistrations/filter")
                        .contentType("application/json")
                        .param("regId", MockDataConstant.REGID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        EligibleRegPageResponse actual = objectMapper.readValue(response, EligibleRegPageResponse.class);

        //Validate the response contain one element
        AssertionsForClassTypes.assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        AssertionsForClassTypes.assertThat(actual.getData()).isNotNull();
    }

    @Test
    public void whenFindAll_thenReturns200FilterECS() throws Exception {
        EligibleRegistrationDto dto = MockDataConstant.buildEligibleRegDto();

        EligibleRegPageResponse expected = EligibleRegPageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(dto)
                .build();

        Mockito.when(service.filterEcReg(null, null, null,0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/ecregistrations/filter")
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to EligibleCouplePageResponse
        EligibleRegPageResponse actual = objectMapper.readValue(response, EligibleRegPageResponse.class);

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

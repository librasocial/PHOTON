package com.ssf.childcareservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.childcareservice.config.constant.MockDataConstant;
import com.ssf.childcareservice.dtos.ChildCareDto;
import com.ssf.childcareservice.dtos.PageDto;
import com.ssf.childcareservice.dtos.PageResponse;
import com.ssf.childcareservice.exception.ApiError;
import com.ssf.childcareservice.exception.ApiSubError;
import com.ssf.childcareservice.exception.EntityNotFoundException;
import com.ssf.childcareservice.service.ChildCareService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChildCareController.class)
@ActiveProfiles("test")
public class ChildCareControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ChildCareService service;

    @Test
    public void whenValidInput_thenReturns201ChildCare() throws Exception {
        //Mock
        ChildCareDto childCareDto = MockDataConstant.buildSampleChildCareDto();
        //perform and assert
        mockMvc.perform(post("/childcare")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(childCareDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidInput_thenReturns200readChildCare() throws Exception {
        //MOCK
        ChildCareDto childCareDto = MockDataConstant.buildSampleChildCareDto();
        //Service return
        Mockito.when(service.readChildCare(MockDataConstant.CHILD_CARE_ID)).thenReturn(childCareDto);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/childcare/{childCareId}", MockDataConstant.CHILD_CARE_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(childCareDto));

    }
    @Test
    public void whenInvalidValidInput_thenReturns404readChildCare() throws Exception {
        //Mock datax
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(buildApiSubError("ChildcareId not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readChildCare(MockDataConstant.CHILD_CARE_ID)).thenThrow(new EntityNotFoundException("childcare id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/childcare/{childCareId}", MockDataConstant.CHILD_CARE_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }

    @Test
    public void whenCitizenIdAndCHILDID_thenReturns200FilterChildCare() throws Exception {
        ChildCareDto childCareDto= MockDataConstant.buildSampleChildCareDto();

        PageResponse expected = PageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(childCareDto)
                .build();

        Mockito.when(service.getChildCareByFilter(MockDataConstant.CITIZEN_ID, MockDataConstant.CHILD_ID, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/childcare/filter")
                        .contentType("application/json")
                        .param("citizenId", MockDataConstant.CITIZEN_ID)
                        .param("childId", MockDataConstant.CHILD_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to PNCRegPageResponse
        PageResponse actual = objectMapper.readValue(response, PageResponse.class);

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

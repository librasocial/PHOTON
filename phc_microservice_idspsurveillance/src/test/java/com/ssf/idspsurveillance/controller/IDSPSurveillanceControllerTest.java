package com.ssf.idspsurveillance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.idspsurveillance.constant.IDSPTypeEnum;
import com.ssf.idspsurveillance.controller.constant.MockDataConstant;
import com.ssf.idspsurveillance.dtos.IDSPSurveillanceDto;
import com.ssf.idspsurveillance.dtos.PageDto;
import com.ssf.idspsurveillance.dtos.PageResponse;
import com.ssf.idspsurveillance.dtos.PatchDto;
import com.ssf.idspsurveillance.exception.ApiError;
import com.ssf.idspsurveillance.exception.ApiSubError;
import com.ssf.idspsurveillance.exception.EntityNotFoundException;
import com.ssf.idspsurveillance.service.IDSPSurveillanceService;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IDSPSurveillanceController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@EmbeddedKafka
public class IDSPSurveillanceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IDSPSurveillanceService service;

    @Test
    public void whenValidInput_thenReturns201() throws Exception {
        //Mock
        IDSPSurveillanceDto request = MockDataConstant.builIDSPdto();
        //perform and assert
        mockMvc.perform(post("/idspsurveillances")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        //Mock
        String badRequestJson = "{'test': 'name',}";
        //perform and assert
        mockMvc.perform(post("/idspsurveillances")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInput_thenReturns200read() throws Exception {
        IDSPSurveillanceDto request = MockDataConstant.builIDSPdto();
        Mockito.when(service.read(MockDataConstant.IDSP_ID)).thenReturn(request);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/idspsurveillances/{surveillanceId}", MockDataConstant.IDSP_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(request));

    }
    @Test
    public void whenInvalidValidInput_thenReturns404read() throws Exception {
        //Mock datax
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(MockDataConstant.buildApiSubError("idspsurveillances ID not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.read(MockDataConstant.IDSP_ID)).thenThrow(new EntityNotFoundException("idspsurveillances id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/idspsurveillances/{surveillanceId}", MockDataConstant.IDSP_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }
    @Test
    public void whenValidInput_thenReturns201Patch() throws Exception {
        IDSPSurveillanceDto request = MockDataConstant.builIDSPdto();
        Mockito.when(service.read(MockDataConstant.IDSP_ID)).thenReturn(request);

        PatchDto dto = PatchDto.builder().type(IDSPTypeEnum.IDSP).build();
        mockMvc.perform(patch("/idspsurveillances/{surveillanceId}", MockDataConstant.IDSP_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenvalidInput_thenReturns200Filter() throws Exception {
        IDSPSurveillanceDto request = MockDataConstant.builIDSPdto();

        PageResponse expected = PageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(request)
                .build();

        Mockito.when(service.filter(MockDataConstant.CITIZEN_ID,LocalDate.of(2022, Month.SEPTEMBER,8),
                LocalDate.of(2022, Month.SEPTEMBER,13),
                0,5)).thenReturn(expected);
        String response = mockMvc.perform(get("/idspsurveillances/filter")
                        .contentType("application/json")
                        .param("citizenId", MockDataConstant.CITIZEN_ID)
                        .param("stDate", "2022-09-08")
                        .param("edDate", "2022-09-13")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //convert to PNCRegPageResponse
        PageResponse actual = objectMapper.readValue(response, PageResponse.class);

        //Validate the response contain one element
        assertThat(actual.getMeta().getTotalElements()).isEqualTo(expected.getMeta().getTotalElements());
        assertThat(actual.getData()).isNotNull();
    }
}

package com.ssf.inward.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.inward.constant.InwardTypeEnum;
import com.ssf.inward.constant.MockDataConstant;
import com.ssf.inward.dtos.InwardDto;
import com.ssf.inward.dtos.InwardPatchDto;
import com.ssf.inward.dtos.PageDto;
import com.ssf.inward.dtos.PageResponse;
import com.ssf.inward.exception.ApiError;
import com.ssf.inward.exception.ApiSubError;
import com.ssf.inward.exception.EntityNotFoundException;
import com.ssf.inward.service.InwardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InwardController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class InwardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private InwardService service;

    @Test
    public void whenValidInput_thenReturns201Inward() throws Exception {
        //Mock
        InwardDto request = MockDataConstant.buildInwardDto();
        //perform and assert
        mockMvc.perform(post("/inwards")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        //Mock
        String badRequestJson = "{'test': 'name',}";
        //perform and assert
        mockMvc.perform(post("/inwards")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInput_thenReturns200readInward() throws Exception {
        InwardDto request = MockDataConstant.buildInwardDto();
        Mockito.when(service.readInward(MockDataConstant.INWARD_ID)).thenReturn(request);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/inwards/{InwardId}", MockDataConstant.INWARD_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(request));

    }
    @Test
    public void whenInvalidValidInput_thenReturns404readInward() throws Exception {
        //Mock datax
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(MockDataConstant.buildApiSubError("Inward ID not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readInward(MockDataConstant.INWARD_ID)).thenThrow(new EntityNotFoundException("Inward id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/inwards/{inwardId}", MockDataConstant.INWARD_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }
    @Test
    public void whenValidInput_thenReturns201PatchInwars() throws Exception {
        InwardDto request = MockDataConstant.buildInwardDto();
        Mockito.when(service.readInward(MockDataConstant.INWARD_ID)).thenReturn(request);

        InwardPatchDto dto = InwardPatchDto.builder().type(InwardTypeEnum.INWARD).build();
        mockMvc.perform(patch("/inwards/{inwardId}", MockDataConstant.INWARD_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenvalidInput_thenReturns200FilterInward() throws Exception {
        InwardDto request = MockDataConstant.buildInwardDto();

        PageResponse expected = PageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(request)
                .build();

        Mockito.when(service.filterInward(LocalDate.of(2022, Month.SEPTEMBER,8),
                LocalDate.of(2022, Month.SEPTEMBER,13),
                MockDataConstant.POTYPE, MockDataConstant.SUPPLIER_NAME, MockDataConstant.STATUS,
                0,5)).thenReturn(expected);
        String response = mockMvc.perform(get("/inwards/filter")
                        .contentType("application/json")
                        .param("stDate", "2022-09-08")
                        .param("edDate", "2022-09-13")
                        .param("inwardType", MockDataConstant.POTYPE)
                        .param("supplier", MockDataConstant.SUPPLIER_NAME)
                        .param("status", MockDataConstant.STATUS)
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

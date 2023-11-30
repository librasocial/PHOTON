package com.ssf.bssurveillance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.bssurveillance.constant.BSSurveillanceConstTypeEnum;
import com.ssf.bssurveillance.controller.constant.MockDataConstant;
import com.ssf.bssurveillance.dtos.BSSurveillanceDto;
import com.ssf.bssurveillance.dtos.PageDto;
import com.ssf.bssurveillance.dtos.PageResponse;
import com.ssf.bssurveillance.dtos.PatchDto;
import com.ssf.bssurveillance.service.BSSurveillanceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BSSurveillanceController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class BSSurveillanceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BSSurveillanceService service;

    @Test
    public void whenValidInput_thenReturns201PurchaseOrder() throws Exception {
        //Mock
        BSSurveillanceDto request = MockDataConstant.buildSurveillanceDto();
        //perform and assert
        mockMvc.perform(post("/bloodsmearsurveillances")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        //Mock
        String badRequestJson = "{'test': 'name',}";
        //perform and assert
        mockMvc.perform(post("/bloodsmearsurveillances")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInput_thenReturns200readProduct() throws Exception {
        BSSurveillanceDto request = MockDataConstant.buildSurveillanceDto();
        Mockito.when(service.read(MockDataConstant.BSS_ID)).thenReturn(request);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/bloodsmearsurveillances/{surveillanceId}", MockDataConstant.BSS_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(request));

    }

    @Test
    public void whenValidInput_thenReturns201PatchPO() throws Exception {
        BSSurveillanceDto request = MockDataConstant.buildSurveillanceDto();
        Mockito.when(service.read(MockDataConstant.BSS_ID)).thenReturn(request);

        PatchDto dto = PatchDto.builder().type(BSSurveillanceConstTypeEnum.BS).build();
        mockMvc.perform(patch("/bloodsmearsurveillances/{surveillanceId}", MockDataConstant.BSS_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenvalidInput_thenReturns200FilterPNC() throws Exception {
        BSSurveillanceDto request = MockDataConstant.buildSurveillanceDto();

        PageResponse expected = PageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(request)
                .build();

        Mockito.when(service.filter(MockDataConstant.CITIZEN_ID,
                MockDataConstant.SLIDE_NO,
                MockDataConstant.TYPE,
                0,5)).thenReturn(expected);
        String response = mockMvc.perform(get("/bloodsmearsurveillances/filter")
                        .contentType("application/json")
                        .param("citizenId", MockDataConstant.CITIZEN_ID)
                        .param("slideNo", MockDataConstant.SLIDE_NO)
                        .param("type", MockDataConstant.TYPE)
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

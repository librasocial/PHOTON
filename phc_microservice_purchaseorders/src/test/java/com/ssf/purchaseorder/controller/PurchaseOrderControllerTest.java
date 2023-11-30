package com.ssf.purchaseorder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.purchaseorder.constant.MockDataConstant;
import com.ssf.purchaseorder.constant.PurchaseOrderTypeEnum;
import com.ssf.purchaseorder.constant.StatusEnum;
import com.ssf.purchaseorder.dtos.PageDto;
import com.ssf.purchaseorder.dtos.PageResponse;
import com.ssf.purchaseorder.dtos.PurchaseOrderDto;
import com.ssf.purchaseorder.dtos.PurchaseOrderPatchDto;
import com.ssf.purchaseorder.exception.ApiError;
import com.ssf.purchaseorder.exception.ApiSubError;
import com.ssf.purchaseorder.exception.EntityNotFoundException;
import com.ssf.purchaseorder.service.PurchaseOrderService;
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

@WebMvcTest(PurchaseOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PurchaseOrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PurchaseOrderService service;

    @Test
    public void whenValidInput_thenReturns201PurchaseOrder() throws Exception {
        //Mock
        PurchaseOrderDto request = MockDataConstant.buildPurchaseOrderDto();
        //perform and assert
        mockMvc.perform(post("/purchaseorders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    public void whenJsonRequestBad_then400() throws Exception {
        //Mock
        String badRequestJson = "{'test': 'name',}";
        //perform and assert
        mockMvc.perform(post("/purchaseorders")
                        .contentType("application/json")
                        .content(badRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInput_thenReturns200readProduct() throws Exception {
        PurchaseOrderDto request = MockDataConstant.buildPurchaseOrderDto();
        Mockito.when(service.readPurchaseOrder(MockDataConstant.PURCHASE_ORDER_ID)).thenReturn(request);

        //call controller get api
        String actualResponse = mockMvc.perform(get("/purchaseorders/{orderId}", MockDataConstant.PURCHASE_ORDER_ID)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //Assert Expected and actual
        assertThat(actualResponse).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(request));

    }
    @Test
    public void whenInvalidValidInput_thenReturns404readProduct() throws Exception {
        //Mock datax
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(MockDataConstant.buildApiSubError("Purchaseorder ID not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
        ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
        //Service return
        Mockito.when(service.readPurchaseOrder(MockDataConstant.PURCHASE_ORDER_ID)).thenThrow(new EntityNotFoundException("Purchaseorder id not found"));

        //call controller get api
        String errorRespString = mockMvc.perform(get("/purchaseorders/{orderId}", MockDataConstant.PURCHASE_ORDER_ID)
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
        assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
    }
    @Test
    public void whenValidInput_thenReturns201PatchPO() throws Exception {
        PurchaseOrderDto request = MockDataConstant.buildPurchaseOrderDto();
        Mockito.when(service.readPurchaseOrder(MockDataConstant.PURCHASE_ORDER_ID)).thenReturn(request);

        PurchaseOrderPatchDto dto = PurchaseOrderPatchDto.builder().type(PurchaseOrderTypeEnum.PO).build();
        mockMvc.perform(patch("/purchaseorders/{orderId}", MockDataConstant.PURCHASE_ORDER_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenvalidInput_thenReturns200FilterPNC() throws Exception {
        PurchaseOrderDto request = MockDataConstant.buildPurchaseOrderDto();

        PageResponse expected = PageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(request)
                .build();

        Mockito.when(service.filterPurchaseOrder(LocalDate.of(2022, Month.SEPTEMBER,8),
                LocalDate.of(2022, Month.SEPTEMBER,13),
                MockDataConstant.POTYPE,
                MockDataConstant.SUPPLIER_NAME,
                StatusEnum.DRAFT.name(),
                0,5)).thenReturn(expected);
        String response = mockMvc.perform(get("/purchaseorders/filter")
                        .contentType("application/json")
                        .param("stDate", "2022-09-08")
                        .param("edDate", "2022-09-13")
                        .param("poType", MockDataConstant.POTYPE)
                        .param("supplierName", MockDataConstant.SUPPLIER_NAME)
                        .param("statuses", StatusEnum.DRAFT.name())
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

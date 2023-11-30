package com.ssf.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.product.constant.MockDataConstant;
import com.ssf.product.dtos.InventoryDto;
import com.ssf.product.dtos.PageDto;
import com.ssf.product.dtos.PageResponse;
import com.ssf.product.dtos.ProductDto;
import com.ssf.product.exception.ApiError;
import com.ssf.product.exception.ApiSubError;
import com.ssf.product.exception.EntityNotFoundException;
import com.ssf.product.service.InventoryService;
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

@WebMvcTest(InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class InventoryControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockBean
        private InventoryService service;

        @Test
        public void whenValidInput_thenReturns201ProductInventory() throws Exception {
            //Mock
            InventoryDto request = MockDataConstant.buildInventoryDto();
            //perform and assert
            mockMvc.perform(post("/products/{productId}/inventories", MockDataConstant.PRODUCT_ID)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
        @Test
        public void whenJsonRequestBad_then400() throws Exception {
            //Mock
            String badRequestJson = "{'test': 'name',}";
            //perform and assert
            mockMvc.perform(post("/products/{productId}/inventories", MockDataConstant.PRODUCT_ID)
                            .contentType("application/json")
                            .content(badRequestJson))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void whenValidInput_thenReturns200readProductInventory() throws Exception {
            InventoryDto request = MockDataConstant.buildInventoryDto();
            Mockito.when(service.readInventory(MockDataConstant.PRODUCT_ID, MockDataConstant.INVENTORY_ID)).thenReturn(request);

            //call controller get api
            String actualResponse = mockMvc.perform(get("/products/{productId}/inventories/{inventoryId}",
                            MockDataConstant.PRODUCT_ID,  MockDataConstant.INVENTORY_ID)
                            .accept("application/json"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            //Assert Expected and actual
            assertThat(actualResponse).isEqualToIgnoringWhitespace(
                    objectMapper.writeValueAsString(request));

        }
        @Test
        public void whenInvalidValidInput_thenReturns404readProductInventory() throws Exception {
            //Mock datax
            List<ApiSubError> errorList = new ArrayList<>();
            errorList.add(MockDataConstant.buildApiSubError("Product Inventory ID not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
            ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
            //Service return
            Mockito.when(service.readInventory(MockDataConstant.PRODUCT_ID, MockDataConstant.INVENTORY_ID)).thenThrow(new EntityNotFoundException("Product id not found"));

            //call controller get api
            String errorRespString = mockMvc.perform(get("/products/{productId}/inventories/{inventoryId}",
                            MockDataConstant.PRODUCT_ID,  MockDataConstant.INVENTORY_ID)
                            .accept("application/json"))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString();
            ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
            assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
        }

    @Test
    public void whenValidInputWithCouple_thenReturns201PatchProductInvnetory() throws Exception {
        ProductDto request = MockDataConstant.buildProductDto();

        mockMvc.perform(patch("/products/{productId}/inventories/{inventoryId}",
                        MockDataConstant.PRODUCT_ID,  MockDataConstant.INVENTORY_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    public void whenName_thenReturns200FilterProduct() throws Exception {
        InventoryDto request = MockDataConstant.buildInventoryDto();

        PageResponse expected = PageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(request)
                .build();

        Mockito.when(service.getInventoryByFilter(MockDataConstant.PRODUCT_NAME,  MockDataConstant.PRODUCT_ID, MockDataConstant.BATCH_NO, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/products/inventories/filter")
                        .contentType("application/json")
                        .param("searchStr", MockDataConstant.PRODUCT_NAME)
                        .param("productId", MockDataConstant.PRODUCT_ID)
                        .param("batchNumber", MockDataConstant.BATCH_NO)
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

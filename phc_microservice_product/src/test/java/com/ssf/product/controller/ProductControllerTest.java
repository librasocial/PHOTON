package com.ssf.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.product.constant.MockDataConstant;
import com.ssf.product.dtos.PageDto;
import com.ssf.product.dtos.PageResponse;
import com.ssf.product.dtos.ProductDto;
import com.ssf.product.exception.ApiError;
import com.ssf.product.exception.ApiSubError;
import com.ssf.product.exception.EntityNotFoundException;
import com.ssf.product.service.ProductService;
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

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ProductControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockBean
        private ProductService service;

        @Test
        public void whenValidInput_thenReturns201Product() throws Exception {
            //Mock
            ProductDto request = MockDataConstant.buildProductDto();
            //perform and assert
            mockMvc.perform(post("/products")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
        @Test
        public void whenJsonRequestBad_then400() throws Exception {
            //Mock
            String badRequestJson = "{'test': 'name',}";
            //perform and assert
            mockMvc.perform(post("/products")
                            .contentType("application/json")
                            .content(badRequestJson))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void whenValidInput_thenReturns200readProduct() throws Exception {
            ProductDto request = MockDataConstant.buildProductDto();//Service return
            Mockito.when(service.readProduct(MockDataConstant.PRODUCT_ID)).thenReturn(request);

            //call controller get api
            String actualResponse = mockMvc.perform(get("/products/{productId}", MockDataConstant.PRODUCT_ID)
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
            errorList.add(MockDataConstant.buildApiSubError("Product ID not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
            ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
            //Service return
            Mockito.when(service.readProduct(MockDataConstant.PRODUCT_ID)).thenThrow(new EntityNotFoundException("Product id not found"));

            //call controller get api
            String errorRespString = mockMvc.perform(get("/products/{productId}", MockDataConstant.PRODUCT_ID)
                            .accept("application/json"))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString();
            ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
            assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
        }

    @Test
    public void whenValidInputWithCouple_thenReturns201PatchProduct() throws Exception {
        ProductDto request = MockDataConstant.buildProductDto();

        mockMvc.perform(patch("/products/{productId}", MockDataConstant.PRODUCT_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    public void whenName_thenReturns200FilterProduct() throws Exception {
        ProductDto request = MockDataConstant.buildProductDto();

        PageResponse expected = PageResponse.builder()
                .meta(PageDto.builder()
                        .totalElements(1L)
                        .totalPages(1L)
                        .number(0)
                        .size(5)
                        .build())
                .data(request)
                .build();

        Mockito.when(service.getProductByFilter(MockDataConstant.PRODUCT_NAME, 0, 5)).thenReturn(expected);
        String response = mockMvc.perform(get("/products/filter")
                        .contentType("application/json")
                        .param("searchStr", MockDataConstant.PRODUCT_NAME)
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

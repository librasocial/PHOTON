package com.ssf.pharmacyorder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pharmacyorder.constant.MockDataConstant;
import com.ssf.pharmacyorder.dtos.PharmacyOrderDto;
import com.ssf.pharmacyorder.exception.ApiError;
import com.ssf.pharmacyorder.exception.ApiSubError;
import com.ssf.pharmacyorder.exception.EntityNotFoundException;
import com.ssf.pharmacyorder.service.PharmacyOrderService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PharmacyOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PharmacyOrderControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockBean
        private PharmacyOrderService service;

        @Test
        public void whenValidInput_thenReturns201Order() throws Exception {
            //Mock
            PharmacyOrderDto request = MockDataConstant.buildPharmacyOrderDto();
            //perform and assert
            mockMvc.perform(post("/pharmacyorders")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
        @Test
        public void whenJsonRequestBad_then400() throws Exception {
            //Mock
            String badRequestJson = "{'test': 'name',}";
            //perform and assert
            mockMvc.perform(post("/pharmacyorders")
                            .contentType("application/json")
                            .content(badRequestJson))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void whenValidInput_thenReturns200readOrder() throws Exception {
            PharmacyOrderDto request = MockDataConstant.buildPharmacyOrderDto();//Service return
            Mockito.when(service.readProduct(MockDataConstant.ORDER_ID)).thenReturn(request);

            //call controller get api
            String actualResponse = mockMvc.perform(get("/pharmacyorders/{orderId}", MockDataConstant.ORDER_ID)
                            .accept("application/json"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            //Assert Expected and actual
            assertThat(actualResponse).isEqualToIgnoringWhitespace(
                    objectMapper.writeValueAsString(request));

        }
        @Test
        public void whenInvalidValidInput_thenReturns404readOrder() throws Exception {
            //Mock datax
            List<ApiSubError> errorList = new ArrayList<>();
            errorList.add(MockDataConstant.buildApiSubError("pharmacyorders ID not found", String.valueOf(HttpStatus.NOT_FOUND.value()), null));
            ApiError expectedErrResp = ApiError.builder().description("Resource is not available").errors(errorList).build();
            //Service return
            Mockito.when(service.readProduct(MockDataConstant.ORDER_ID)).thenThrow(new EntityNotFoundException("pharmacyorders id not found"));

            //call controller get api
            String errorRespString = mockMvc.perform(get("/pharmacyorders/{orderId}", MockDataConstant.ORDER_ID)
                            .accept("application/json"))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString();
            ApiError actualErrResp = objectMapper.readValue(errorRespString, ApiError.class);
            assertThat(expectedErrResp.getDescription()).isEqualTo(actualErrResp.getDescription());
        }
}

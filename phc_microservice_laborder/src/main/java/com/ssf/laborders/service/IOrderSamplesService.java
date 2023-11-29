package com.ssf.laborders.service;

import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.dtos.OrderSamplesDTO;
import com.ssf.laborders.dtos.OrderSamplesPatchDTO;

public interface IOrderSamplesService {
    LabOrdersPageResponse createOrderSamples(OrderSamplesDTO orderSamplesDTO);

    LabOrdersPageResponse getOrderSamples(String orderId, String sampleId);

    LabOrdersPageResponse updateOrderSamples(String orderId, String sampleId, OrderSamplesPatchDTO samplesPatchDTO);

    LabOrdersPageResponse getOrderSamplesByFilter(String orderId, Integer page, Integer size);
}

package com.ssf.laborders.service;

import com.ssf.laborders.dtos.LabOrderDTO;
import com.ssf.laborders.dtos.LabOrderPatchDTO;
import com.ssf.laborders.dtos.LabOrdersPageResponse;


public interface ILabOrdersService {
    String getHealth();

    LabOrdersPageResponse createLabOrder(LabOrderDTO labOrderDTO);

    LabOrdersPageResponse getLabOrders(String orderId);

    LabOrdersPageResponse updateLabOrders(String orderId, LabOrderPatchDTO labOrderPatchDto);

    LabOrdersPageResponse getLabOrdersByFilter(String startDate, String endDate, String careContext, String uhId, String encounterId, String statuses, Integer page, Integer size);
}

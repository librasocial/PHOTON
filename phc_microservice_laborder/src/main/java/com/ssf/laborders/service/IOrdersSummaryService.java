package com.ssf.laborders.service;

import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.entities.LabOrder;
import com.ssf.laborders.entities.OrdersSummary;

public interface IOrdersSummaryService {
    LabOrdersPageResponse getOrdersSummaryByFilter(String startDate, String endDate, Integer page, Integer size);

    OrdersSummary updateOrdersSummary(LabOrder order);
}

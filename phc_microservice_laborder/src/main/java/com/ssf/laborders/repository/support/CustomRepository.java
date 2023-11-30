package com.ssf.laborders.repository.support;

import com.ssf.laborders.entities.LabOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

public interface CustomRepository {
    Page<LabOrder> queryLabOrders(Query query, Pageable pageable);
}

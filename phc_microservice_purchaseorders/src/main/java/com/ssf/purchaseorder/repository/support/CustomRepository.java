package com.ssf.purchaseorder.repository.support;

import com.ssf.purchaseorder.entities.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

public interface CustomRepository {
    Page<PurchaseOrder> query(Query query, Pageable pageable);
}

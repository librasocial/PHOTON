package com.ssf.purchaseorder.repository;

import com.ssf.purchaseorder.entities.PurchaseOrder;
import com.ssf.purchaseorder.repository.support.CustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPurchaseOrderRepository extends MongoRepository<PurchaseOrder, String> , CustomRepository {
}

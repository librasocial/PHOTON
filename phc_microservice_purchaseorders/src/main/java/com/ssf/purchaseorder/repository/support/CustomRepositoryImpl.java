package com.ssf.purchaseorder.repository.support;

import com.ssf.purchaseorder.entities.PurchaseOrder;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;


@Slf4j
public class CustomRepositoryImpl implements CustomRepository {

    private final MongoOperations operations;

    @Autowired
    public CustomRepositoryImpl(MongoOperations operations) {
        Assert.notNull(operations, "MongoOperations must not be null!");
        this.operations = operations;
    }
    @Override
    public Page<PurchaseOrder> query(Query query, Pageable pageable) {
        Long totalCount = operations.count(query, PurchaseOrder.class);
        log.debug("Query ==> {} collectionName ==> {} limit ===> {} ", query.with(pageable), operations.getCollectionName(PurchaseOrder.class), query.with(pageable).getLimit());
        List<PurchaseOrder> purchaseOrders =  operations.find(query.with(pageable), PurchaseOrder.class);
        Page<PurchaseOrder> purchaseOrderPage = PageableExecutionUtils.getPage(purchaseOrders, pageable, () -> totalCount);
        return  purchaseOrderPage;
    }
}

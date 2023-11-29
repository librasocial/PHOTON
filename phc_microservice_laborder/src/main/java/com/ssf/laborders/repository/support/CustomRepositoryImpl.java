package com.ssf.laborders.repository.support;

import com.ssf.laborders.entities.LabOrder;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;


public class CustomRepositoryImpl implements CustomRepository {

    private final MongoOperations operations;

    @Autowired
    public CustomRepositoryImpl(MongoOperations operations) {
        Assert.notNull(operations, "MongoOperations must not be null!");
        this.operations = operations;
    }

    @Override
    public Page<LabOrder> queryLabOrders(Query query, Pageable pageable) {
        Long totalCount = operations.count(query.with(pageable), LabOrder.class);
        List<LabOrder> pharmacyOrderList = operations.find(query, LabOrder.class);
        Page<LabOrder> pharmacyOrderPage = PageableExecutionUtils.getPage(pharmacyOrderList, pageable, () -> totalCount);
        return pharmacyOrderPage;
    }
}

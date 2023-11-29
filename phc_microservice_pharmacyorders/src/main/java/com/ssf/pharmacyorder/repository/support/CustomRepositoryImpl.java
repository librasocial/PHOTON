package com.ssf.pharmacyorder.repository.support;

import com.ssf.pharmacyorder.entities.Dispense;
import com.ssf.pharmacyorder.entities.PharmacyOrder;
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
    public Page<PharmacyOrder> query(Query query, Pageable pageable) {
        Long totalCount = operations.count(query, PharmacyOrder.class);
        List<PharmacyOrder> pharmacyOrderList =  operations.find(query.with(pageable), PharmacyOrder.class);
        Page<PharmacyOrder> pharmacyOrderPage = PageableExecutionUtils.getPage(pharmacyOrderList, pageable, () -> totalCount);
        return  pharmacyOrderPage;
    }

    @Override
    public Page<Dispense> queryDispense(Query query, Pageable pageable) {
        Long totalCount = operations.count(query, Dispense.class);
        List<Dispense> dispenseList =  operations.find(query.with(pageable), Dispense.class);
        Page<Dispense> dispensePage = PageableExecutionUtils.getPage(dispenseList, pageable, () -> totalCount);
        return  dispensePage;
    }
}

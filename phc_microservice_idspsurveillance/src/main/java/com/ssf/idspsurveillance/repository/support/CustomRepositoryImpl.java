package com.ssf.idspsurveillance.repository.support;

import com.ssf.idspsurveillance.entites.IDSPSurveillance;
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
    public Page<IDSPSurveillance> query(Query query, Pageable pageable) {
        Long totalCount = operations.count(query, IDSPSurveillance.class);
        List<IDSPSurveillance> idspSurveillanceList =  operations.find(query.with(pageable), IDSPSurveillance.class);
        Page<IDSPSurveillance> idspSurveillancePage = PageableExecutionUtils.getPage(idspSurveillanceList, pageable, () -> totalCount);
        return  idspSurveillancePage;
    }
}

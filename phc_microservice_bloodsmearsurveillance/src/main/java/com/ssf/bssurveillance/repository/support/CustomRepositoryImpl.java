package com.ssf.bssurveillance.repository.support;

import com.ssf.bssurveillance.entities.BSSurveillance;
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
    public Page<BSSurveillance> query(Query query, Pageable pageable) {
        Long totalCount = operations.count(query, BSSurveillance.class);
        List<BSSurveillance> bsSurveillanceList =  operations.find(query.with(pageable), BSSurveillance.class);
        Page<BSSurveillance> bsSurveillancePage = PageableExecutionUtils.getPage(bsSurveillanceList, pageable, () -> totalCount);
        return  bsSurveillancePage;
    }
}

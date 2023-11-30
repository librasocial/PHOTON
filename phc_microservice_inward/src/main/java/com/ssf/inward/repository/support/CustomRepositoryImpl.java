package com.ssf.inward.repository.support;

import com.ssf.inward.entities.Inward;
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
    public Page<Inward> query(Query query, Pageable pageable) {
        Long totalCount = operations.count(query, Inward.class);
        List<Inward> inwards =  operations.find(query.with(pageable), Inward.class);
        Page<Inward> inwardPage = PageableExecutionUtils.getPage(inwards, pageable, () -> totalCount);
        return  inwardPage;
    }
}

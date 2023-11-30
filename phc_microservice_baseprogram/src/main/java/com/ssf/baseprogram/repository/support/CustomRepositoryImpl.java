package com.ssf.baseprogram.repository.support;

import com.ssf.baseprogram.entities.BaseProgram;
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
    public Page<BaseProgram> query(Query query, Pageable pageable) {
        Long totalCount = operations.count(query, BaseProgram.class);
        List<BaseProgram> basePrograms =  operations.find(query.with(pageable), BaseProgram.class);
        Page<BaseProgram> baseProgramPage = PageableExecutionUtils.getPage(basePrograms, pageable, () -> totalCount);
        return  baseProgramPage;
    }
}

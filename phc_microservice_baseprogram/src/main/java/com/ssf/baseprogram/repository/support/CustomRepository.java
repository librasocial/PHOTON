package com.ssf.baseprogram.repository.support;

import com.ssf.baseprogram.entities.BaseProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

public interface CustomRepository {
    Page<BaseProgram> query(Query query, Pageable pageable);
}

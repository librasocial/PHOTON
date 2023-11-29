package com.ssf.inward.repository.support;

import com.ssf.inward.entities.Inward;
;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

public interface CustomRepository {
    Page<Inward> query(Query query, Pageable pageable);
}

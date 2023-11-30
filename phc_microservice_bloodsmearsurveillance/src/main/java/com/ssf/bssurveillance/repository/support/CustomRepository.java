package com.ssf.bssurveillance.repository.support;

import com.ssf.bssurveillance.entities.BSSurveillance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

public interface CustomRepository {
    Page<BSSurveillance> query(Query query, Pageable pageable);
}

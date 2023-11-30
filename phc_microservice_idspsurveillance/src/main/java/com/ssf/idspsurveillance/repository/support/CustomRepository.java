package com.ssf.idspsurveillance.repository.support;

import com.ssf.idspsurveillance.entites.IDSPSurveillance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;



public interface CustomRepository {
    Page<IDSPSurveillance> query(Query query, Pageable pageable);
}

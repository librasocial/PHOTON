package com.ssf.pharmacyorder.repository.support;

import com.ssf.pharmacyorder.entities.Dispense;
import com.ssf.pharmacyorder.entities.PharmacyOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

public interface CustomRepository {
    Page<PharmacyOrder> query(Query query, Pageable pageable);
    Page<Dispense> queryDispense(Query query, Pageable pageable);
}

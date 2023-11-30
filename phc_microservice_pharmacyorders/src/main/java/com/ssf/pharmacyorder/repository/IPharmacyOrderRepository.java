package com.ssf.pharmacyorder.repository;

import com.ssf.pharmacyorder.entities.PharmacyOrder;
import com.ssf.pharmacyorder.repository.support.CustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPharmacyOrderRepository extends MongoRepository<PharmacyOrder, String>, CustomRepository {
}

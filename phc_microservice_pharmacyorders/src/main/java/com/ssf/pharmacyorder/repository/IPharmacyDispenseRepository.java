package com.ssf.pharmacyorder.repository;

import com.ssf.pharmacyorder.entities.Dispense;
import com.ssf.pharmacyorder.repository.support.CustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPharmacyDispenseRepository extends MongoRepository<Dispense, String>, CustomRepository {
    @Query(value = "{'id' : ?0, 'orderId' : ?1 }")
    Optional<Dispense> findByIdAndOrderId(String id, String orderId);
}

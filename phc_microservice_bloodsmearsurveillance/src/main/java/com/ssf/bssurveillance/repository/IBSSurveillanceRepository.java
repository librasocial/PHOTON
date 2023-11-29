package com.ssf.bssurveillance.repository;

import com.ssf.bssurveillance.entities.BSSurveillance;
import com.ssf.bssurveillance.repository.support.CustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IBSSurveillanceRepository extends MongoRepository<BSSurveillance, String>, CustomRepository {
}

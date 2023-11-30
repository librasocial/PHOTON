package com.ssf.idspsurveillance.repository;

import com.ssf.idspsurveillance.entites.IDSPSurveillance;
import com.ssf.idspsurveillance.repository.support.CustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IIDSPSurveillanceRepository extends MongoRepository<IDSPSurveillance, String> , CustomRepository {
}

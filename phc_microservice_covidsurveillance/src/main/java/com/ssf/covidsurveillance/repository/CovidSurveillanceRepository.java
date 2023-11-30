package com.ssf.covidsurveillance.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ssf.covidsurveillance.entities.CovidSurveillance;

@Repository
public interface CovidSurveillanceRepository extends MongoRepository<CovidSurveillance, String> {

}

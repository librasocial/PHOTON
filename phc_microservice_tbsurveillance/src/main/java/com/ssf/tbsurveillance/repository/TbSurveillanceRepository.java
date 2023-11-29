package com.ssf.tbsurveillance.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ssf.tbsurveillance.model.TbSurveillance;

@Repository
public interface TbSurveillanceRepository extends MongoRepository<TbSurveillance, String> {

}

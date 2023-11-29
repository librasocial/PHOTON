package com.ssf.leprosysurveillance.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ssf.leprosysurveillance.model.LeprosySurveillance;

@Repository
public interface LeprosySurveillanceRepository extends MongoRepository<LeprosySurveillance, String> {
	
	 List<LeprosySurveillance> findByIdInAndDateModifiedIsNotNullOrderByDateModifiedDesc(List<String> idList);
}

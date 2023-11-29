package com.ssf.surveycube.repository;

import com.ssf.surveycube.entities.SurveyCube;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyCubeRepository extends MongoRepository<SurveyCube, String> {
}

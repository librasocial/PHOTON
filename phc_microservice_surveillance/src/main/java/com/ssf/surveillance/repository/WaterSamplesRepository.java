package com.ssf.surveillance.repository;

import com.ssf.surveillance.entities.WaterSamples;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterSamplesRepository extends MongoRepository<WaterSamples, String> {
}

package com.ssf.surveillance.repository;

import com.ssf.surveillance.entities.IodineSamples;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IodineSamplesRepository extends MongoRepository<IodineSamples, String> {

    @Query(value = "{'id' : ?0, 'iodineSurveillanceId' : ?1 }")
    Optional<IodineSamples> findByIdAndIodineSurveillanceId(String sampleId, String surveillanceId);

}

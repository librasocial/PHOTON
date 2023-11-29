package com.ssf.surveillance.repository;

import com.ssf.surveillance.entities.LarvaVisits;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LarvaVisitsRepository extends MongoRepository<LarvaVisits, String> {

    @Query(value = "{'id' : ?0, 'larvaSurveillanceId' : ?1 }")
    Optional<LarvaVisits> findByIdAndLarvaSurveillanceId(String visitId, String surveillanceId);

}

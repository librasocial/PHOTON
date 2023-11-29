package com.ssf.immunization.repository;

import com.ssf.immunization.entities.Immunization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImmunizationRepository extends MongoRepository<Immunization, String> {
}

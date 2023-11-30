package com.ssf.immunizationrec.repository;

import com.ssf.immunizationrec.entities.ImmunizationRec;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImmunizationRecRepository extends MongoRepository<ImmunizationRec, String> {
}

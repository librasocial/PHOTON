package com.ssf.pncregistration.repository;

import com.ssf.pncregistration.entities.Infant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPNCInfantRepository extends MongoRepository<Infant, String> {
    @Query(value = "{'id' : ?0, 'pncRegistrationId' : ?1 }")
    Optional<Infant> findByIdAndRegistrationId(String id, String pncRegistrationId);
}

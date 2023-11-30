package com.ssf.pncservice.repository;

import com.ssf.pncservice.entities.Infant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IInfantRepository extends MongoRepository<Infant, String> {

    @Query(value = "{'id' : ?0, 'pncServiceId' : ?1 }")
    Optional<Infant> findByIdAndServiceId(String id, String serviceId);
}

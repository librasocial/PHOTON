package com.ssf.pncservice.repository;

import com.ssf.pncservice.entities.Infant;
import com.ssf.pncservice.entities.InfantVisitLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IInfantVisitlogRepository extends MongoRepository<InfantVisitLog, String> {

    @Query(value = "{'id' : ?0, 'childId' : ?1 }")
    Optional<InfantVisitLog> findByIdAndChildId(String id, String childId);
}

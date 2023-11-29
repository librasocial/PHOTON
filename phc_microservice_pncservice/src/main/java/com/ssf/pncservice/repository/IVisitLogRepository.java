package com.ssf.pncservice.repository;

import com.ssf.pncservice.entities.VisitLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVisitLogRepository extends MongoRepository<VisitLog, String> {

    @Query(value = "{'id' : ?0, 'serviceId' : ?1 }")
    Optional<VisitLog> findByIdAndServiceId(String id, String serviceId);

}

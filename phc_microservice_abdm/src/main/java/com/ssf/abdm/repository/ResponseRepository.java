package com.ssf.abdm.repository;

import com.ssf.abdm.entities.Response;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponseRepository extends MongoRepository<Response, String> {

    Optional<Response> findByRequestId(String s);
}

package com.ssf.laborders.repository;

import com.ssf.laborders.entities.TestResults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestResultsRepository extends MongoRepository<TestResults, String> {

    Optional<TestResults> findByOrderIdAndId(String orderId, String id);

    Page<TestResults> findAllByOrderId(String orderId, Pageable pageable);
}

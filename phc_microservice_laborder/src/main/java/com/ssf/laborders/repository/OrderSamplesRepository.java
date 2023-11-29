package com.ssf.laborders.repository;

import com.ssf.laborders.entities.OrderSamples;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderSamplesRepository extends MongoRepository<OrderSamples, String> {

    Optional<OrderSamples> findByOrderIdAndId(String orderId, String id);

    Page<OrderSamples> findAllByOrderId(String orderId, Pageable pageable);
}

package com.ssf.laborders.repository;

import com.ssf.laborders.entities.OrdersSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OrdersSummaryRepository extends MongoRepository<OrdersSummary, String> {

    @Query("{'workingDay' : { $gte: ?0, $lte: ?1 } }")
    Page<OrdersSummary> findAllByOrderDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<OrdersSummary> findAll(Pageable pageable);

    Optional<OrdersSummary> findByWorkingDay(LocalDate date);

}

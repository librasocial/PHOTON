package com.ssf.laborders.repository;

import com.ssf.laborders.dtos.GenderCountDTO;
import com.ssf.laborders.entities.LabOrder;
import com.ssf.laborders.repository.support.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LabOrdersRepository extends MongoRepository<LabOrder, String>, CustomRepository {
    Page<LabOrder> findAll(Pageable pageable);

    @Aggregation(pipeline = {"{ $match: {'orderDate':{ '$eq': ?0 }} }",
            "{ $group: { _id: '$patient.gender', count: { $sum: 1 } } }",
    })
    List<GenderCountDTO> findOrdersCountOfGenders(LocalDateTime dateTime);
}

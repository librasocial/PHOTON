package com.ssf.eligiblecouple.repository;

import com.ssf.eligiblecouple.entities.EligibleCouple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEligibleCoupleRepository extends MongoRepository<EligibleCouple, String> {
    Page<EligibleCouple> findByRchId(String rchId, Pageable pageable);

    Page<EligibleCouple> findById(String id, Pageable pageable);

    Page<EligibleCouple> findByRchIdAndId(String rchId, String serviceId, Pageable pageable);
    @ExistsQuery("{ 'couple.wifeId': ?0}")
    boolean existByWifeId(String wifeId);
    @ExistsQuery("{ 'couple.husbandId': ?0}")
    boolean existByHusbandId(String husbandId);

}

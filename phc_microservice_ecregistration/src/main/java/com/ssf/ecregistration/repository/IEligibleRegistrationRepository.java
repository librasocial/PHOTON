package com.ssf.ecregistration.repository;

import com.ssf.ecregistration.entities.EligibleRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IEligibleRegistrationRepository extends MongoRepository<EligibleRegistration, String> {

    @Query(value = "{'id' : ?0, 'rchGeneration.rchId' : ?1 }")
    Page<EligibleRegistration> findByIdAndRchId(String id, String rchId, Pageable pageable);

    Page<EligibleRegistration> findById(String id, Pageable pageable);

    @Query(value = "{'rchGeneration.rchId' : ?0}")
    Page<EligibleRegistration> findByRchId(String rchId, Pageable pageable);

    Page<EligibleRegistration> findByDataEntryStatus(String dataEntryStatus, Pageable pageable);
    @ExistsQuery("{ 'couple.wifeId': ?0}")
    boolean existByWifeId(String wifeId);
    @ExistsQuery("{ 'couple.husbandId': ?0}")
    boolean existByHusbandId(String husbandId);

}

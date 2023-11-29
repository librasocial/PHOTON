package com.ssf.pncservice.repository;

import com.ssf.pncservice.entities.PNCService;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPNCServiceRepository extends MongoRepository<PNCService, String> {
    @ExistsQuery("{ 'couple.wifeId': ?0}")
    boolean existByWifeId(String wifeId);
    @ExistsQuery("{ 'couple.husbandId': ?0}")
    boolean existByHusbandId(String husbandId);
}

package com.ssf.ancservice.repository;

import com.ssf.ancservice.entities.ANCService;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IANCServiceRepository extends MongoRepository<ANCService, String> {
    @ExistsQuery("{ 'couple.wifeId': ?0}")
    boolean existByWifeId(String wifeId);
    @ExistsQuery("{ 'couple.husbandId': ?0}")
    boolean existByHusbandId(String husbandId);
}

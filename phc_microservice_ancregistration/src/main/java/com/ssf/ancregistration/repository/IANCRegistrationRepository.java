package com.ssf.ancregistration.repository;

import com.ssf.ancregistration.entities.ANCRegistration;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IANCRegistrationRepository extends MongoRepository<ANCRegistration, String> {
    @ExistsQuery("{ 'couple.wifeId': ?0}")
    boolean existByWifeId(String wifeId);
    @ExistsQuery("{ 'couple.husbandId': ?0}")
    boolean existByHusbandId(String husbandId);
}

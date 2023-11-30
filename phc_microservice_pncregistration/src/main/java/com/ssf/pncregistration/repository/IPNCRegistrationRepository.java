package com.ssf.pncregistration.repository;

import com.ssf.pncregistration.entities.PNCRegistration;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPNCRegistrationRepository extends MongoRepository<PNCRegistration, String> {
    @ExistsQuery("{ 'couple.wifeId': ?0}")
    boolean existByWifeId(String wifeId);
    @ExistsQuery("{ 'couple.husbandId': ?0}")
    boolean existByHusbandId(String husbandId);
}

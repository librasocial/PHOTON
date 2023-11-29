package com.ssf.childcareservice.repository;

import com.ssf.childcareservice.entities.ChildCare;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChildCareRepository extends MongoRepository<ChildCare, String> {
}

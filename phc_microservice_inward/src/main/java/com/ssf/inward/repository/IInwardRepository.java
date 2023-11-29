package com.ssf.inward.repository;

import com.ssf.inward.entities.Inward;
import com.ssf.inward.repository.support.CustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IInwardRepository extends MongoRepository<Inward, String> , CustomRepository {
}

package com.ssf.surveillance.repository;

import com.ssf.surveillance.entities.Larva;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LarvaRepository extends MongoRepository<Larva, String> {
}

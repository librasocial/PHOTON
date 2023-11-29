package com.ssf.baseprogram.repository;

import com.ssf.baseprogram.entities.BaseProgram;
import com.ssf.baseprogram.repository.support.CustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBaseProgramRepository extends MongoRepository<BaseProgram, String>, CustomRepository {
}

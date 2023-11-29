package com.ssf.membership.repositories;


import com.ssf.membership.entities.Driver;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends Neo4jRepository<Driver, String> {
    Optional<Driver> findByUuid(String s);
}

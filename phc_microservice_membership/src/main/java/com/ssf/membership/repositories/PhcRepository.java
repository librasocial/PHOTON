package com.ssf.membership.repositories;

import com.ssf.membership.entities.Phc;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhcRepository extends Neo4jRepository<Phc, String> {
    public Optional<Phc> findByUuid(String id);
}

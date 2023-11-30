package com.ssf.membership.repositories;

import com.ssf.membership.entities.DGroupAttender;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DGroupAttenderRepository extends Neo4jRepository<DGroupAttender, String> {
    Optional<DGroupAttender> findByUuid(String s);
}
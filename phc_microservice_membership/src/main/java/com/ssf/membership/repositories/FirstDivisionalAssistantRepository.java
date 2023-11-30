package com.ssf.membership.repositories;


import com.ssf.membership.entities.FirstDivisionalAssistant;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FirstDivisionalAssistantRepository extends Neo4jRepository<FirstDivisionalAssistant, String> {
    Optional<FirstDivisionalAssistant> findByUuid(String s);
}

package com.ssf.membership.repositories;


import com.ssf.membership.entities.SecondDivisionalAssistant;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecondDivisionalAssistantRepository extends Neo4jRepository<SecondDivisionalAssistant, String> {
    Optional<SecondDivisionalAssistant> findByUuid(String s);
}

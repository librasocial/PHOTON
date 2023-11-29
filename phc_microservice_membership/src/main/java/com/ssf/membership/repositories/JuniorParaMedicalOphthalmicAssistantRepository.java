package com.ssf.membership.repositories;


import com.ssf.membership.entities.JuniorParaMedicalOphthalmicAssistant;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuniorParaMedicalOphthalmicAssistantRepository extends Neo4jRepository<JuniorParaMedicalOphthalmicAssistant, String> {
    Optional<JuniorParaMedicalOphthalmicAssistant> findByUuid(String s);
}

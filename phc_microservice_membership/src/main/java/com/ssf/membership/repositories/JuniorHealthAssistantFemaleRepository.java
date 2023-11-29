package com.ssf.membership.repositories;

import com.ssf.membership.entities.JuniorHealthAssistantFemale;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuniorHealthAssistantFemaleRepository extends Neo4jRepository<JuniorHealthAssistantFemale, String> {

    @Query("MATCH (n:JuniorHealthAssistantFemale) WHERE n.uuid = $s RETURN n")
    Optional<JuniorHealthAssistantFemale> findByUuid(String s);

}

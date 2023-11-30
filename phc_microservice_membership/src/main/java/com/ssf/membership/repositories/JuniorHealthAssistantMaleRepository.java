package com.ssf.membership.repositories;

import com.ssf.membership.entities.JuniorHealthAssistantMale;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuniorHealthAssistantMaleRepository extends Neo4jRepository<JuniorHealthAssistantMale, String> {

    @Query("MATCH (n:JuniorHealthAssistantMale) WHERE n.uuid = $s RETURN n")
    Optional<JuniorHealthAssistantMale> findByUuid(String s);

}

package com.ssf.membership.repositories;

import com.ssf.membership.entities.JuniorHealthAssistant;
import com.ssf.membership.entities.JuniorHealthAssistantMale;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuniorHealthAssistantRepository extends Neo4jRepository<JuniorHealthAssistant, String> {

    @Query("MATCH (n:JuniorHealthAssistant) WHERE n.uuid = $s RETURN n")
    Optional<JuniorHealthAssistant> findByUuid(String s);


}

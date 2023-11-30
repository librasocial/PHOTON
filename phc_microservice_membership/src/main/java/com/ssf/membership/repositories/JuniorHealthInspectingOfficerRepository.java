package com.ssf.membership.repositories;


import com.ssf.membership.entities.JuniorHealthInspectingOfficer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuniorHealthInspectingOfficerRepository extends Neo4jRepository<JuniorHealthInspectingOfficer, String> {
    Optional<JuniorHealthInspectingOfficer> findByUuid(String s);
}

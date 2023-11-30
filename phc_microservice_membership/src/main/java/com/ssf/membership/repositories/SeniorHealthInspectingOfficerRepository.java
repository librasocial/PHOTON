package com.ssf.membership.repositories;


import com.ssf.membership.entities.SeniorHealthInspectingOfficer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeniorHealthInspectingOfficerRepository extends Neo4jRepository<SeniorHealthInspectingOfficer, String> {
    Optional<SeniorHealthInspectingOfficer> findByUuid(String s);
}

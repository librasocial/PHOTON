package com.ssf.membership.repositories;

import com.ssf.membership.entities.HealthInspectingOfficer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthInspectingOfficerRepository extends Neo4jRepository<HealthInspectingOfficer, String> {
    Optional<HealthInspectingOfficer> findByUuid(String s);
}
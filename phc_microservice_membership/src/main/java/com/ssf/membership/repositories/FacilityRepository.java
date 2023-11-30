package com.ssf.membership.repositories;

import com.ssf.membership.entities.Facility;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityRepository extends Neo4jRepository<Facility, String> {
    public Optional<Facility> findByUuid(String id);
}

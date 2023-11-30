package com.ssf.membership.repositories;

import com.ssf.membership.entities.PrimaryHealthCareOfficer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrimaryHealthCareOfficerRepository extends Neo4jRepository<PrimaryHealthCareOfficer, String> {
    Optional<PrimaryHealthCareOfficer> findByUuid(String s);
}

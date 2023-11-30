package com.ssf.membership.repositories;

import com.ssf.membership.entities.HeadMedicalOfficer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeadMedicalOfficerRepository extends Neo4jRepository<HeadMedicalOfficer, String> {
    Optional<HeadMedicalOfficer> findByUuid(String s);
}
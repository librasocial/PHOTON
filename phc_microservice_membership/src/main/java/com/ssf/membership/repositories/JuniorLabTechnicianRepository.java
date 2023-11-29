package com.ssf.membership.repositories;

import com.ssf.membership.entities.JuniorLabTechnician;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuniorLabTechnicianRepository extends Neo4jRepository<JuniorLabTechnician, String> {

    Optional<JuniorLabTechnician> findByUuid(String s);
}
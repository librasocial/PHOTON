package com.ssf.membership.repositories;


import com.ssf.membership.entities.JuniorPrimaryHealthCareOfficer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuniorPrimaryHealthCareOfficerRepository extends Neo4jRepository<JuniorPrimaryHealthCareOfficer, String> {
    Optional<JuniorPrimaryHealthCareOfficer> findByUuid(String s);
}

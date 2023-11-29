package com.ssf.membership.repositories;


import com.ssf.membership.entities.SeniorHealthInspectingOfficer;
import com.ssf.membership.entities.SeniorPrimaryHealthCareOfficer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeniorPrimaryHealthCareOfficerRepository extends Neo4jRepository<SeniorPrimaryHealthCareOfficer, String> {
    Optional<SeniorPrimaryHealthCareOfficer> findByUuid(String s);
}

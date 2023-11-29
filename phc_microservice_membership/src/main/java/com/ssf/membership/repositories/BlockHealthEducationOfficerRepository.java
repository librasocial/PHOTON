package com.ssf.membership.repositories;


import com.ssf.membership.entities.BlockHealthEducationOfficer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockHealthEducationOfficerRepository extends Neo4jRepository<BlockHealthEducationOfficer, String> {
    Optional<BlockHealthEducationOfficer> findByUuid(String s);
}

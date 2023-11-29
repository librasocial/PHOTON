package com.ssf.membership.repositories;

import com.ssf.membership.entities.MedicalOfficer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalOfficerRepository extends Neo4jRepository<MedicalOfficer, String> {

    @Query("MATCH (n:MedicalOfficer) WHERE n.uuid = $s RETURN n")
    Optional<MedicalOfficer> findByUuid(String s);

}

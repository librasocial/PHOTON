package com.ssf.membership.repositories;

import com.ssf.membership.entities.PhcAdministrator;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhcAdministratorRepository extends Neo4jRepository<PhcAdministrator, String> {

    @Query("MATCH (n:PhcAdministrator) WHERE n.uuid = $s RETURN n")
    Optional<PhcAdministrator> findByUuid(String s);

}

package com.ssf.membership.repositories;

import com.ssf.membership.entities.AshaWorker;
import com.ssf.membership.entities.Citizen;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitizenRepository extends Neo4jRepository<Citizen, String> {

    // @Query("MATCH (n:Citizen) WHERE n.uuid = $s RETURN n")
    Optional<Citizen> findByUuid(String s);

}

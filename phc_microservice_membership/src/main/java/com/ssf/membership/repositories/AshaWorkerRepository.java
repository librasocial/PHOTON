package com.ssf.membership.repositories;

import com.ssf.membership.entities.AshaWorker;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AshaWorkerRepository extends Neo4jRepository<AshaWorker, String> {

    @Query("MATCH (n:AshaWorker) WHERE n.uuid = $s RETURN n")
    Optional<AshaWorker> findByUuid(String s);
}

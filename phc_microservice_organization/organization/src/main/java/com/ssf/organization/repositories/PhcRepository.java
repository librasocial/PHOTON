package com.ssf.organization.repositories;

import com.ssf.organization.entities.Phc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhcRepository extends Neo4jRepository<Phc, String> {


    @Query(value = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN t", countQuery = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN  count(t)")
    public Page<Phc> findPhyRel(String sourceId, String rel, Pageable pageable);

    public Optional<Phc> findByUuid(String id);
}

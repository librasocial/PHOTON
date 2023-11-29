package com.ssf.organization.repositories;

import com.ssf.organization.entities.Locality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalityRepository extends Neo4jRepository<Locality, String> {
    public Page<Locality> findLocalityByName(String name, Pageable pageable);

    @Query(value = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN t", countQuery = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN  count(t)")
    public Page<Locality> findLocalityRel(String sourceId, String rel, Pageable pageable);

    public Optional<Locality> findByUuid(String id);
}

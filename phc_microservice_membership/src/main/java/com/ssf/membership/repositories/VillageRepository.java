package com.ssf.membership.repositories;

import com.ssf.membership.entities.Village;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VillageRepository extends Neo4jRepository<Village, String> {
    public Optional<Village> findByUuid(String id);
}

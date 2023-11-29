package com.ssf.membership.repositories;

import com.ssf.membership.entities.SubCenter;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCenterRepository extends Neo4jRepository<SubCenter, String> {
    public Optional<SubCenter> findByUuid(String id);
}

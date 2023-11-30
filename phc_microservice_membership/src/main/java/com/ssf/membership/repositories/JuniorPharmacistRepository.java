package com.ssf.membership.repositories;

import com.ssf.membership.entities.JuniorPharmacist;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuniorPharmacistRepository extends Neo4jRepository<JuniorPharmacist, String> {
    Optional<JuniorPharmacist> findByUuid(String s);
}
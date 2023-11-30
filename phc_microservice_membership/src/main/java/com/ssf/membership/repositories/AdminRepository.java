package com.ssf.membership.repositories;

import com.ssf.membership.entities.Admin;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends Neo4jRepository<Admin, String> {
    Optional<Admin> findByUuid(String s);
}

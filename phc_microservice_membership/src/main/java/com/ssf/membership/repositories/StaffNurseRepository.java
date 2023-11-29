package com.ssf.membership.repositories;


import com.ssf.membership.entities.StaffNurse;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffNurseRepository extends Neo4jRepository<StaffNurse, String> {
    Optional<StaffNurse> findByUuid(String s);
}

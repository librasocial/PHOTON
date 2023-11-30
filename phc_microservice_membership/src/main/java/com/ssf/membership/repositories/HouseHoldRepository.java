package com.ssf.membership.repositories;


import com.ssf.membership.entities.HouseHold;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseHoldRepository extends Neo4jRepository<HouseHold, String> {

    Optional<HouseHold> findByUuid(String s);

}

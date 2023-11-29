package com.ssf.membership.repositories;

import com.ssf.membership.entities.GramPanchayat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GramPanchayatRepository extends Neo4jRepository<GramPanchayat, String> {
    public Optional<GramPanchayat> findByUuid(String id);

}

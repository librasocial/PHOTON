package com.ssf.membership.repositories;

import com.ssf.membership.entities.GPMember;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GPMemberRepository extends Neo4jRepository<GPMember, String> {
    Optional<GPMember> findByUuid(String s);
}

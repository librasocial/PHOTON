package com.ssf.organization.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.ssf.organization.entities.District;
import com.ssf.organization.entities.State;
import com.ssf.organization.entities.SubCenter;

@Repository
public interface SubCenterRepository extends Neo4jRepository<SubCenter, String> {
	public Page<SubCenter> findSubCenterByName(String name, Pageable pageable);

	@Query(value = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN t", countQuery = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN  count(t)")
	public Page<SubCenter> findSubCenterRel(String sourceId, String rel, Pageable pageable);
    
	public Optional<SubCenter> findByUuid(String id);
}

package com.ssf.organization.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.ssf.organization.entities.Country;
import com.ssf.organization.entities.Phc;

@Repository
public interface CountryRepository extends Neo4jRepository<Country, String> {

	public Page<Country> findCountryByName(String name, Pageable pageable);

	@Query(value = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN t", countQuery = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN  count(t)")
	public Page<Country> findCountryRel(String sourceId, String rel, Pageable pageable);

	public Optional<Country> findByUuid(String id);
}

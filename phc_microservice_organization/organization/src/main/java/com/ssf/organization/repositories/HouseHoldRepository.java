package com.ssf.organization.repositories;

import com.ssf.organization.entities.HouseHold;
import com.ssf.organization.entities.Org;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseHoldRepository extends Neo4jRepository<HouseHold, String> {
	@Query(value = "Match(n:HouseHold { h_id: $name } ) return n", countQuery = "Match(n:HouseHold { h_id: $name }) return count(n)")
	public Page<HouseHold> findHouseHoldByhId(@Param("name") String name, Pageable pageable);

	@Query(value = "MATCH v =()-[r:CONTAINEDINPLACE]->(village:Village {name: $name })   RETURN v", countQuery = "MATCH v =()-[r:CONTAINEDINPLACE]->(village:Village {name: $name })   RETURN count(v)")
	public Page<Org> findVillageHouseHold(@Param("name") String name, Pageable pageable);

	@Query(value = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN t", countQuery = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN  count(t)")
	public Page<HouseHold> findHouseHoldRel(String sourceId, String rel, Pageable pageable);
    
	public Optional<HouseHold> findByUuid(String id);


    @Query(value = "MATCH (s:HouseHold)-[:CONTAINEDINPLACE]->(t:Village) WHERE t.uuid = $villageId RETURN count(s) as count")
    public Long getHouseHoldCountByVillage(String villageId);

    @Query(value = "MATCH (s:HouseHold)-[r:CONTAINEDINPLACE]->(t:Village) WHERE t.uuid = $villageId AND round(2 * 6371 * 1000 * (asin(sqrt((sin((radians($latitude) - radians(s.latitude))/2)^2) + cos(radians(s.latitude)) * cos(radians($latitude)) * (sin((radians($longitude)- radians(s.longitude))/ 2)^2)))) ,2) <= $distance return s ", countQuery = "MATCH (s:HouseHold)-[r:CONTAINEDINPLACE]->(t:Village) WHERE t.uuid = $villageId AND round(2 * 6371 * 1000 * (asin(sqrt((sin((radians($latitude) - radians(s.latitude))/2)^2) + cos(radians(s.latitude)) * cos(radians($latitude)) * (sin((radians($longitude)- radians(s.longitude))/ 2)^2)))) ,2) <= $distance return count(s) ")
    public Page<HouseHold> findHouseHoldNearMe(String villageId, Double latitude, Double longitude, Double distance, Pageable pageable);

}

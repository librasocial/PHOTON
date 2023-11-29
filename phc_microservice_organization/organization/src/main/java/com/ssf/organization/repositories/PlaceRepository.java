package com.ssf.organization.repositories;

import com.ssf.organization.entities.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends Neo4jRepository<Place, String> {

    @Query(value = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN t", countQuery = "MATCH p=(t)-[r]->(s) where type(r) = $rel and s.uuid = $sourceId  RETURN  count(t)")
    public Page<Place> findPlaceRel(String sourceId, String rel, Pageable pageable);

    @Query(value = "MATCH (s:Place)-[r:CONTAINEDINPLACE]->(t:Village) WHERE t.uuid = $villageId AND round(2 * 6371 * 1000 * (asin(sqrt((sin((radians($latitude) - radians(s.latitude))/2)^2) + cos(radians(s.latitude)) * cos(radians($latitude)) * (sin((radians($longitude)- radians(s.longitude))/ 2)^2)))) ,2) <= $distance return s ", countQuery = "MATCH (s:Place)-[r:CONTAINEDINPLACE]->(t:Village) WHERE t.uuid = $villageId AND round(2 * 6371 * 1000 * (asin(sqrt((sin((radians($latitude) - radians(s.latitude))/2)^2) + cos(radians(s.latitude)) * cos(radians($latitude)) * (sin((radians($longitude)- radians(s.longitude))/ 2)^2)))) ,2) <= $distance return count(s) ")
    public Page<Place> findPlacesNearMe(String villageId, Double latitude, Double longitude, Double distance, Pageable pageable);

    public Optional<Place> findByUuid(String id);
}

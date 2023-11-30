package com.ssf.organization.repositories;

import com.ssf.organization.dtos.PlaceCount;
import com.ssf.organization.model.AssetType;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlaceSessionRepository {

    @Autowired
    private Driver driver;

    public PlaceSessionRepository(Driver driver) {
        this.driver = driver;
    }


    public List getPlaceCountByGrouping(String villageId, AssetType assetType) {
        Session session = driver.session();
        List<PlaceCount> placeCounts = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder("MATCH (s:Place)-[:CONTAINEDINPLACE]->(t:Village)");

        if (villageId != null && assetType != null) {
            queryBuilder.append(" WHERE t.uuid = \"" + villageId + "\" AND s.assetType = \"" + assetType + "\"");
        } else if (villageId != null) {
            queryBuilder.append(" WHERE t.uuid = \"" + villageId + "\"");
        } else if (assetType != null) {
            queryBuilder.append(" WHERE s.assetType = \"" + assetType + "\"");
        }

        queryBuilder.append(" RETURN DISTINCT s.assetType as assetType , COUNT(s.assetType) AS typeCount");
        String query = queryBuilder.toString();
        session.run(query).forEachRemaining(result -> {
            PlaceCount placeCount = new PlaceCount();
            result.asMap().forEach((k, v) -> {
                if (k.equals("assetType"))
                    placeCount.setAssetType(String.valueOf(v));
                if (k.equals("typeCount"))
                    placeCount.setTypeCount((Long) v);
            });

            placeCounts.add(placeCount);
        });

        return placeCounts;

    }

}

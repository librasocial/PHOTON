package com.ssf.primaryhealthcare.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Data
@Node
@AllArgsConstructor
@NoArgsConstructor
public class Locality {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String name;
    private String localityCode;
    private Double latitude;
    private Double longitude;
    private Long houseHoldCount;
    private Long inAreaCount;
    private Long totalPopulation;

}

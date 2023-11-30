package com.ssf.primaryhealthcare.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseHold {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String houseID;
    private Double latitude;
    private Double longitude;
    private Integer totalFamilyMembers;
    private String houseHeadName;
    private Boolean hasEligibleCouple;
    private String pregnantLadyName;
    private Boolean hasPregnantWoman;
    private Boolean hasNewBorn;
    private Boolean hasDelivered;
    private List<String> houseHeadImageUrls;
    private LocalDateTime dateCreated;
    private String createdBy;
    private LocalDateTime dateModified;
    private String modifiedBy;

    @Relationship(type = "CONTAINEDINPLACE", direction = Relationship.Direction.OUTGOING)
    private Village village;

    @Relationship(type = "CONTAINEDINPLACE", direction = Relationship.Direction.OUTGOING)
    private Locality locality;

}

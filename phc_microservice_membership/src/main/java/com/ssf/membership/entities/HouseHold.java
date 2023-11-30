package com.ssf.membership.entities;

import com.ssf.membership.dtos.AuditDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseHold extends AuditDto {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String houseID;
    private Double latitude;
    private Double longitude;
    private Integer totalFamilyMembers = 0;
    private String houseHeadName = "";
    private Boolean hasEligibleCouple = false;
    private Boolean hasNewBorn = false;
    private Boolean hasPregnantWoman = false;
    private Boolean hasDelivered = false;
    private Boolean hasRegisteredChildCare = false;
    private Boolean hasRegisteredAdolescentCare = false;
    private Boolean hasEligibleChildCare = false;
    private String pregnantLadyName = "";
    private List<String> houseHeadImageUrls = new ArrayList<>();
}

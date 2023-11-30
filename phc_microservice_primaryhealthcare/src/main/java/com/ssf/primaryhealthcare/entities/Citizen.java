package com.ssf.primaryhealthcare.entities;

import com.ssf.primaryhealthcare.model.RMNCHAServiceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Citizen implements IPHCEntity {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String healthID;
    private String faceId;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Integer age;
    private String gender;
    private Boolean isHead;
    private Boolean isAdult;
    private Boolean isPregnant;
    private String contact;
    private String rchId;
    private RMNCHAServiceStatus rmnchaServiceStatus;
    private String residingInVillage;
    private String relationshipWithHead;
    private List<String> imageUrls;
    @CreatedDate
    private LocalDateTime dateCreated;
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime dateModified;
    private String modifiedBy;

    @NotNull
    @Relationship(type = "RESIDESIN", direction = Relationship.Direction.OUTGOING)
    private HouseHold houseHold;

}

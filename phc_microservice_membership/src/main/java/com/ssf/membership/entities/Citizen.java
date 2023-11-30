package com.ssf.membership.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.membership.dtos.AuditDto;
import com.ssf.membership.model.RMNCHAServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Citizen extends AuditDto implements IMemberEntity {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String healthID;
    private String faceId;
    private String firstName;
    private String middleName;
    private String lastName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private LocalDate expiredOn;
    private LocalDate sFormFilledOn;
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
}

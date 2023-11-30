package com.ssf.permissions.entities;



import com.ssf.permissions.dtos.AuditDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Employee extends AuditDto {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String salutation;
    private String name;
    private String email;
    private String contact;
    private String gender;
    private LocalDate dateOfBirth;
    private String healthID;
    private String pan;
    private String citizenIdType;
    private String citizenIdNumber;
    private String bloodGroup;
    private String height;
    private String caste;
    private String religion;
    private String nationality;
    private String photo;
    private String status;
    private String posted_by;
}

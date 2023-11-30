package com.ssf.membership.entities;

import com.ssf.membership.dtos.AuditDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCenter extends AuditDto {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String name;
    private Double latitude;
    private Double longitude;
    private String photo;
    private String description;
    private String addressLine;
    private String stateName;
    private String districtName;
    private String talukName;
    private String villageName;
    private String pin;
    private String contact;
    private String email;
    private String status;
}

package com.ssf.membership.entities;


import com.ssf.membership.dtos.AuditDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node(labels = {"Facility"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Facility extends AuditDto {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String name;
    private String code;
    private String description;
    private String status;
}

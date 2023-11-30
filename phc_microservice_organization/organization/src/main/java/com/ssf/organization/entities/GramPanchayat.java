package com.ssf.organization.entities;

import com.ssf.organization.dtos.AuditDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node(labels = {"GramPanchayat"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GramPanchayat extends AuditDto implements Org {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String name;
    private String lgdCode;
}

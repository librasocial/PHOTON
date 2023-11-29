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

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Village extends AuditDto implements Org {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String name;
    private String lgdCode;
    private String boundary;
    private Double latitude;
    private Double longitude;
    private Long houseHoldCount = 0L;
    private Long inAreaCount = 0L;
    private Long totalPopulation = 0L;
}

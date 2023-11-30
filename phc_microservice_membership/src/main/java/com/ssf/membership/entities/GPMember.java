package com.ssf.membership.entities;

import com.ssf.membership.dtos.AuditDto;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
@Data
public class GPMember extends AuditDto implements IMemberEntity {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String name;
    private String phoneNumber;
    private String emailId;
    private String position;
}



package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"BlockHealthEducationOfficer", "Employee"})
@Data
public class BlockHealthEducationOfficer extends Employee implements IMemberEntity {
}

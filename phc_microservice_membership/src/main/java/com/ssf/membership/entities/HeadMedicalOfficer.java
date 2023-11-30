package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"HeadMedicalOfficer", "Employee"})
@Data
public class HeadMedicalOfficer extends Employee implements IMemberEntity {
}

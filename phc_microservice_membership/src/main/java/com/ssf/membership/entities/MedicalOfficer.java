package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"MedicalOfficer", "Employee"})
@Data
public class MedicalOfficer extends Employee implements IMemberEntity {
}

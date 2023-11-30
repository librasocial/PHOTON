package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"HealthInspectingOfficer", "Employee"})
@Data
public class HealthInspectingOfficer extends Employee implements IMemberEntity {
}

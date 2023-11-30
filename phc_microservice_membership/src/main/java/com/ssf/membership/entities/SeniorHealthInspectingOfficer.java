package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"SeniorHealthInspectingOfficer", "Employee"})
@Data
public class SeniorHealthInspectingOfficer extends Employee implements IMemberEntity {
}

package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"JuniorHealthInspectingOfficer", "Employee"})
@Data
public class JuniorHealthInspectingOfficer extends Employee implements IMemberEntity {
}

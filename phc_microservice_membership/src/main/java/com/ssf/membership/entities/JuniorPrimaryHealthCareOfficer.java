package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"JuniorPrimaryHealthCareOfficer", "Employee"})
@Data
public class JuniorPrimaryHealthCareOfficer extends Employee implements IMemberEntity {
}

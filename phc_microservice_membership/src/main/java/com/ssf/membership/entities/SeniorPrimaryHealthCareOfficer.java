package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"SeniorPrimaryHealthCareOfficer", "Employee"})
@Data
public class SeniorPrimaryHealthCareOfficer extends Employee implements IMemberEntity {
}

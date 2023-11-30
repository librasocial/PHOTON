package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"PhcAdministrator", "Employee"})
@Data
public class PhcAdministrator extends Employee implements IMemberEntity {
}

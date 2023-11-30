package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"StaffNurse", "Employee"})
@Data
public class StaffNurse extends Employee implements IMemberEntity {
}

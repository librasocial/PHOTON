package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"Driver", "Employee"})
@Data
public class Driver extends Employee implements IMemberEntity {
}

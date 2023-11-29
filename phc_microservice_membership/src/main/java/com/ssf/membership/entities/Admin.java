package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"Admin", "Employee"})
@Data
public class Admin extends Employee implements IMemberEntity {
}

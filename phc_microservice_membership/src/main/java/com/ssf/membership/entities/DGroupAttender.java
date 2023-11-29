package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"DGroupAttender", "Employee"})
@Data
public class DGroupAttender extends Employee implements IMemberEntity {
}

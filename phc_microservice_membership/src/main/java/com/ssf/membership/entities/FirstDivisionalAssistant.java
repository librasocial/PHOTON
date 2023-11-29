package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"FirstDivisionalAssistant", "Employee"})
@Data
public class FirstDivisionalAssistant extends Employee implements IMemberEntity {
}

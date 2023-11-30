package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"SecondDivisionalAssistant", "Employee"})
@Data
public class SecondDivisionalAssistant extends Employee implements IMemberEntity {
}

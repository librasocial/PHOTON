package com.ssf.permissions.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"AshaWorker", "Employee"})
@Data
public class AshaWorker extends Employee {
}

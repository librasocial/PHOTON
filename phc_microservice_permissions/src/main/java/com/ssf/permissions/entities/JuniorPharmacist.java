package com.ssf.permissions.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"JuniorPharmacist", "Employee"})
@Data
public class JuniorPharmacist extends Employee {
}

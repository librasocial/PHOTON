package com.ssf.pncregistration.kafka.producer.model;

import lombok.Data;

@Data
public class HasMembership {
    private String role;
    private String organization;
}

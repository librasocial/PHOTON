package com.ssf.membership.kafka.producer.model;

import lombok.Data;

@Data
public class HasMembership {
    private String role;
    private String organization;
}

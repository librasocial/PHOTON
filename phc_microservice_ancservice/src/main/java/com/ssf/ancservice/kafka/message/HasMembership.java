package com.ssf.ancservice.kafka.message;

import lombok.Data;

@Data
public class HasMembership {
    private String role;
    private String organization;
}

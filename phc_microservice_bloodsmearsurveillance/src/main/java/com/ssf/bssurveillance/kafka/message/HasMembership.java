package com.ssf.bssurveillance.kafka.message;

import lombok.Data;

@Data
public class HasMembership {
    private String role;
    private String organization;
}

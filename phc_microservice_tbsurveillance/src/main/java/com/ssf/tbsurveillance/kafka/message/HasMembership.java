package com.ssf.tbsurveillance.kafka.message;

import lombok.Data;

@Data
public class HasMembership {
    private String role;
    private String organization;
}

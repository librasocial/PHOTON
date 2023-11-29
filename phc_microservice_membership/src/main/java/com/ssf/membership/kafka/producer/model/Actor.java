package com.ssf.membership.kafka.producer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Actor {
    private String id;
    private HasMembership hasMembership;
}

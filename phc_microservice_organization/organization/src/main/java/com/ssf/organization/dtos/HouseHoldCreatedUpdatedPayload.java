package com.ssf.organization.dtos;

import com.ssf.organization.entities.Org;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class HouseHoldCreatedUpdatedPayload {
    private String authorization;
    private String idToken;
    private Org payload;
}

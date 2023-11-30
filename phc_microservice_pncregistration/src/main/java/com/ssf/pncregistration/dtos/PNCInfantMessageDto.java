package com.ssf.pncregistration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PNCInfantMessageDto {
    private String authorization;

    private String idToken;

    private PNCInfantPayload payload;

}

package com.ssf.pncregistration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PNCInfantPayload {
    private PNCRegistrationDto pncRegistrationDto;
    private InfantDto infantDto;
}

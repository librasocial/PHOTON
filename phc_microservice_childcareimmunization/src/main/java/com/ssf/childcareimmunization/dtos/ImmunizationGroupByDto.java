package com.ssf.childcareimmunization.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ImmunizationGroupByDto {
	private String doseNumber;
	private ImmunizationSubGroup immunizationSubGroup;

}

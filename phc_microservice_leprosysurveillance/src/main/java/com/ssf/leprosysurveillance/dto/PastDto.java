package com.ssf.leprosysurveillance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PastDto {
	private Boolean wasConfirmed;
	private String result;
	private Boolean hasUndergoneRCSSurgery;

}

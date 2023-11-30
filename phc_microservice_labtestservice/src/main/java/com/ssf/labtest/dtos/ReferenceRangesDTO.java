package com.ssf.labtest.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.labtest.constants.Constant;
import com.ssf.labtest.constants.ValidatorConstants;
import com.ssf.labtest.validators.ValidateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceRangesDTO {
    @ValidateEnum(enumClass = Constant.Gender.class, required = true, message = ValidatorConstants.GENDER_VALIDATION)
    private String gender;
    @ValidateEnum(enumClass = Constant.Period.class, required = true, message = ValidatorConstants.PERIOD_VALIDATION)
    private String period;
    @NotNull(message = "fromAge cannot be null or empty")
    private Integer fromAge;
    private Integer toAge;
    @ValidateEnum(enumClass = Constant.ReferenceIndicators.class, required = true, message = ValidatorConstants.REFERENCE_INDICATOR_VALIDATION)
    private String referenceIndicator;
    @NotNull(message = "minValue cannot be null or empty")
    private String minValue;
    @NotNull(message = "maxValue cannot be null or empty")
    private String maxValue;
    private String description;
}

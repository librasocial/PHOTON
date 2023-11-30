package com.ssf.labtest.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.labtest.constants.Constant;
import com.ssf.labtest.constants.ValidatorConstants;
import com.ssf.labtest.validators.ValidateCode;
import com.ssf.labtest.validators.ValidateEnum;
import com.ssf.labtest.validators.ValidateName;
import com.ssf.labtest.validators.ValidateUnitOfMeasurement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabTestDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @ValidateCode(required = true)
    private String code;
    @ValidateName(required = true)
    private String name;
    @ValidateEnum(enumClass = Constant.TestType.class, required = true, message = ValidatorConstants.TEST_TYPE_VALIDATION)
    private String testType;
    @ValidateUnitOfMeasurement(required = true)
    private String unitOfMeasurement;
    @ValidateEnum(enumClass = Constant.TestGroup.class, required = true, message = ValidatorConstants.GROUP_VALIDATION)
    private String group;
    @ValidateEnum(enumClass = Constant.SampleType.class, message = ValidatorConstants.SAMPLE_TYPE_VALIDATION)
    private String sampleType;
    private String sampleSnomedCode;
    @ValidateEnum(enumClass = Constant.TestMethod.class, message = ValidatorConstants.TEST_VALIDATION)
    private String testMethod;
    @ValidateEnum(enumClass = Constant.ResultType.class, required = true, message = ValidatorConstants.RESULT_TYPE_VALIDATION)
    private String resultType;
    private List<SubTestDTO> subTests = new ArrayList<>();
    private String turnAroundTime;
    @ValidateEnum(enumClass = Constant.TurnAroundUnit.class, message = ValidatorConstants.TURN_AROUND_UNIT_VALIDATION)
    private String turnAroundUnit;
    @ValidateEnum(enumClass = Constant.ResultFormat.class, required = true, message = ValidatorConstants.RESULT_FORMAT_VALIDATION)
    private String resultFormat;
    private String testValue;
    @NotNull(message = ValidatorConstants.OUTSOURCED_EMPTY)
    private Boolean outSourced;
    private String outsourcedOrg;
    @Valid
    private List<ReferenceRangesDTO> referenceRanges;
    @ValidateEnum(enumClass = Constant.Status.class, required = true, message = ValidatorConstants.STATUS_VALIDATION)
    private String status;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDTO audit;
}

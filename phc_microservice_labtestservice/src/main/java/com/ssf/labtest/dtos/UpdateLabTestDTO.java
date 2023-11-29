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
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateLabTestDTO {
    @ValidateCode
    private String code;
    @ValidateName
    private String name;
    @ValidateEnum(enumClass = Constant.TestType.class, message = ValidatorConstants.TEST_TYPE_VALIDATION)
    private String testType;
    @ValidateUnitOfMeasurement
    private String unitOfMeasurement;
    @ValidateEnum(enumClass = Constant.TestGroup.class, message = ValidatorConstants.GROUP_VALIDATION)
    private String group;
    @ValidateEnum(enumClass = Constant.SampleType.class, message = ValidatorConstants.SAMPLE_TYPE_VALIDATION)
    private String sampleType;
    @ValidateEnum(enumClass = Constant.TestMethod.class, message = ValidatorConstants.TEST_VALIDATION)
    private String testMethod;
    @ValidateEnum(enumClass = Constant.ResultType.class, message = ValidatorConstants.RESULT_TYPE_VALIDATION)
    private String resultType;
    private List<SubTestDTO> subTests;
    private Boolean outSourced;
    private String turnAroundTime;
    private String turnAroundUnit;
    @ValidateEnum(enumClass = Constant.ResultFormat.class, required = true, message = ValidatorConstants.RESULT_FORMAT_VALIDATION)
    private String resultFormat;
    private String testValue;
    private String outsourcedOrg;
    @Valid
    private List<ReferenceRangesDTO> referenceRanges;
    @ValidateEnum(enumClass = Constant.Status.class, message = ValidatorConstants.STATUS_VALIDATION)
    private String status;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDTO audit;
}

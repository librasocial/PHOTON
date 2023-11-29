package com.ssf.labtest.entities;

import com.ssf.labtest.constants.Constant;
import com.ssf.labtest.dtos.AuditDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = Constant.COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LabTest extends AuditDTO {
    @Id
    private String id;
    private String code;
    private String name;
    private String testType;
    private String unitOfMeasurement;
    private String group;
    private String sampleType;
    private String sampleSnomedCode;
    private String testMethod;
    private List<SubTest> subTests;
    private String resultType;
    private String turnAroundTime;
    private String turnAroundUnit;
    private String resultFormat;
    private String testValue;
    private Boolean outSourced;
    private String outsourcedOrg;
    private List<ReferenceRanges> referenceRanges;
    private String status;
}

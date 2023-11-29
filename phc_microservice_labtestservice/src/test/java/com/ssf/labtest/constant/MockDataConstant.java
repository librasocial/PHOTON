package com.ssf.labtest.constant;

import com.ssf.labtest.constants.Constant;
import com.ssf.labtest.dtos.LabTestDTO;
import com.ssf.labtest.dtos.UpdateLabTestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MockDataConstant {
    public static LabTestDTO buildSampleLabTestDTO() {
        LabTestDTO labTestDTO = LabTestDTO.builder().code("1111").name("sample test").testType("main test")
                .unitOfMeasurement("Cells/cumm").group("pathology").sampleType("blood").testMethod("Elisa")
                .resultType("single").turnAroundTime("turnaroundtime").turnAroundUnit(Constant.TurnAroundUnit.MINUTES.value).resultFormat("single line").testValue("test-value").outSourced(true).outsourcedOrg("org")
                .status("active").build();
        return labTestDTO;
    }

    public static UpdateLabTestDTO buildSampleUpdateLabTestDTO() {
        UpdateLabTestDTO labTestDTO = UpdateLabTestDTO.builder().code("1111").name("sample test").testType("main test")
                .unitOfMeasurement("Cells/cumm").group("pathology").sampleType("blood").testMethod("Elisa")
                .resultType("single").turnAroundTime("turnaroundtime").testValue("test-value").outsourcedOrg("org").resultFormat("single line")
                .status("active").build();
        return labTestDTO;
    }
}

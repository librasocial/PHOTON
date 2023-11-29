package com.ssf.laborders.dtos;

import com.ssf.laborders.constants.TestResultTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestResultsPatchDTO {
    private TestResultTypeEnum type;
    private Map<String, Object> properties;
}

package com.ssf.laborders.entities;

import lombok.Data;

@Data
public class Result {
    private String key;
    private String value;
    private String unitOfMeasure;
    private String testMethod;
    private String normalRange;
    private String description;
}

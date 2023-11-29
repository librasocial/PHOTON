package com.ssf.primaryhealthcare.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PHCNode {
    private long id;
    private List<String> labels;
    private Map<String,Object> properties;
}

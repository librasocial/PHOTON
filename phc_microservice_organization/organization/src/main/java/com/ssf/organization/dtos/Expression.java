package com.ssf.organization.dtos;

import com.ssf.organization.model.Symbols;
import lombok.Data;

@Data
public class Expression {

    private String key;
    private Object value;
    private Symbols symbol;
}


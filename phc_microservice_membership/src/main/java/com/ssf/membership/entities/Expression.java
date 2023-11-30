package com.ssf.membership.entities;

import com.ssf.membership.model.Symbols;
import lombok.Data;

@Data
public class Expression {

    private String key;
    private Object value;
    private Symbols symbol;
}


package com.ssf.organization.model;


public enum Symbols {
    GT(">"), LT("<"), GTEQ(">="), LTEQ("<="), EQ("=");

    private final String symbol;

    Symbols(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

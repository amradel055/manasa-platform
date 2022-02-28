/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

/**
 *
 * @author hq002
 */
public enum SymbolType {

    LANGUAGE(Values.LANGUAGE), NATIONALITY(Values.NATIONALITY), UNIT(Values.UNIT);

    private SymbolType(String val) {
        // force equality between name of enum instance, and value of constant
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Incorrect Symbol");
        }
    }

    public static class Values {

        public static final String LANGUAGE = "LANGUAGE";
        public static final String NATIONALITY = "NATIONALITY";
        public static final String UNIT = "UNIT";
    }
}

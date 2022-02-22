/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.define;

/**
 *
 * @author AhmedEssam
 */
public enum IsagDocumentEnum {
    COMPANY(1),
    BRANCH(2),
    RSC_TENDER(3);
    
     private final int value;

    IsagDocumentEnum(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}

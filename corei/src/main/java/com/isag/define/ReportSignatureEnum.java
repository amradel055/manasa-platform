/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.define;

/**
 *
 * @author ahmed
 */
public enum ReportSignatureEnum {

    CatchReceipt(0),
    Receipt(1),
    TransferBank(2);

    private final int value;

    ReportSignatureEnum(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}

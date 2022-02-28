/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

/**
 *
 * @author hq002
 */
public class GeneralSymbolDetailReportBean {

    private Integer symbolSerial;
    private String symbolName;

    /**
     * @return the symbolName
     */
    public String getSymbolName() {
        return symbolName;
    }

    /**
     * @param symbolName the symbolName to set
     */
    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    /**
     * @return the symbolSerial
     */
    public Integer getSymbolSerial() {
        return symbolSerial;
    }

    /**
     * @param symbolSerial the symbolSerial to set
     */
    public void setSymbolSerial(Integer symbolSerial) {
        this.symbolSerial = symbolSerial;
    }
}

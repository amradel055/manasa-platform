/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

/**
 *
 * @author WIN7
 */
public class GlBankReport {

    private Integer id;
    private String code;
    private String bankName;
    private String currencyName;
    private Integer accounntCode;
    private String accounntName;
    private Integer type;
    private String typeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Integer getAccounntCode() {
        return accounntCode;
    }

    public void setAccounntCode(Integer accounntCode) {
        this.accounntCode = accounntCode;
    }

    public String getAccounntName() {
        return accounntName;
    }

    public void setAccounntName(String accounntName) {
        this.accounntName = accounntName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

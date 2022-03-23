/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "generalsymbol")
public class GeneralSymbolView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "rowNum")
    private Integer rowNum;

    @Column(name = "generalName")
    private String generalName;    

    @Column(name = "generalSerial")
    private String generalSerial;

    @Column(name = "symbolName")
    private String symbolName;

    @Column(name = "symbolSerial")
    private String symbolSerial;

    @Column(name = "company_id")
    private Integer company_id;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getGeneralName() {
        return generalName;
    }

    public void setGeneralName(String generalName) {
        this.generalName = generalName;
    }

    public String getGeneralSerial() {
        return generalSerial;
    }

    public void setGeneralSerial(String generalSerial) {
        this.generalSerial = generalSerial;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getSymbolSerial() {
        return symbolSerial;
    }

    public void setSymbolSerial(String symbolSerial) {
        this.symbolSerial = symbolSerial;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

}

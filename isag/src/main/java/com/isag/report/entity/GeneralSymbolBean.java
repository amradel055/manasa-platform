/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import java.util.List;

/**
 *
 * @author hq002
 */
public class GeneralSymbolBean {

    private String generalName;
    private Integer generalSerial;
    private Integer symbolSerial;
    private String symbolName;
    private List<GeneralSymbolDetailReportBean> generalSymbolDetailReportBeanList ;

    

    /**
     * @return the generalName
     */
    public String getGeneralName() {
        return generalName;
    }

    /**
     * @param generalName the generalName to set
     */
    public void setGeneralName(String generalName) {
        this.generalName = generalName;
    }



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
     * @return the generalSymbolDetailReportBeanList
     */
    public List<GeneralSymbolDetailReportBean> getGeneralSymbolDetailReportBeanList() {
        return generalSymbolDetailReportBeanList;
    }

    /**
     * @param generalSymbolDetailReportBeanList the generalSymbolDetailReportBeanList to set
     */
    public void setGeneralSymbolDetailReportBeanList(List<GeneralSymbolDetailReportBean> generalSymbolDetailReportBeanList) {
        this.generalSymbolDetailReportBeanList = generalSymbolDetailReportBeanList;
    }

    /**
     * @return the generalSerial
     */
    public Integer getGeneralSerial() {
        return generalSerial;
    }

    /**
     * @param generalSerial the generalSerial to set
     */
    public void setGeneralSerial(Integer generalSerial) {
        this.generalSerial = generalSerial;
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

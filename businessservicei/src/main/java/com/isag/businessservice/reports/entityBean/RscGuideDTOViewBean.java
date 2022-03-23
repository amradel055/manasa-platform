/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.entityBean;

import java.util.List;



/**
 *
 * @author hhhh
 */
public class RscGuideDTOViewBean {
    
    private String workUnitName;
    private String workUnitCode;
    private List<RscExtractJournalViewBean> RscExtractJournalViewBeanList;
    private List<rscExtractJournalReportPrintEntity> extractJournalReportPrintEntityList;

    /**
     * @return the workUnitName
     */
    public String getWorkUnitName() {
        return workUnitName;
    }

    /**
     * @param workUnitName the workUnitName to set
     */
    public void setWorkUnitName(String workUnitName) {
        this.workUnitName = workUnitName;
    }

    /**
     * @return the workUnitCode
     */
    public String getWorkUnitCode() {
        return workUnitCode;
    }

    /**
     * @param workUnitCode the workUnitCode to set
     */
    public void setWorkUnitCode(String workUnitCode) {
        this.workUnitCode = workUnitCode;
    }

    /**
     * @return the RscExtractJournalViewBeanList
     */
    public List<RscExtractJournalViewBean> getRscExtractJournalViewBeanList() {
        return RscExtractJournalViewBeanList;
    }

    /**
     * @param RscExtractJournalViewBeanList the RscExtractJournalViewBeanList to set
     */
    public void setRscExtractJournalViewBeanList(List<RscExtractJournalViewBean> RscExtractJournalViewBeanList) {
        this.RscExtractJournalViewBeanList = RscExtractJournalViewBeanList;
    }

    /**
     * @return the extractJournalReportPrintEntityList
     */
    public List<rscExtractJournalReportPrintEntity> getExtractJournalReportPrintEntityList() {
        return extractJournalReportPrintEntityList;
    }

    /**
     * @param extractJournalReportPrintEntityList the extractJournalReportPrintEntityList to set
     */
    public void setExtractJournalReportPrintEntityList(List<rscExtractJournalReportPrintEntity> extractJournalReportPrintEntityList) {
        this.extractJournalReportPrintEntityList = extractJournalReportPrintEntityList;
    }

    
   

   
}

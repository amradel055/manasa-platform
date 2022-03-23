/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.entity.HosPatientRegistration;


/**
 *
 * @author hhhh
 */
public class HosPatientReportSearchBean {

    private Integer branchId;

    private Integer type;

    private HosPatientRegistration patient;
    

    private Boolean showReport;

    public Boolean getShowReport() {
        return showReport;
    }

    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the patient
     */
    public HosPatientRegistration getPatient() {
        return patient;
    }

    /**
     * @param patient the patient to set
     */
    public void setPatient(HosPatientRegistration patient) {
        this.patient = patient;
    }

   
    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

}

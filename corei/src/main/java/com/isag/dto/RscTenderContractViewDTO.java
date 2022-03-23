package com.isag.dto;

import java.util.Date;

public class RscTenderContractViewDTO extends BaseEntityDTO {

    private String docNumber;
    private Date date;
    private Integer status;
    private Integer customerId;
    private String customerName;
    private String projectManagerName;
    private String generalConsultantName;
    private String profissionalBussinessConsultantName;
    private String customerCode;
    private Integer projectManager;
    private Integer generalConsultant;
    private Integer profissionalBussinessConsultant;
    private String remark;
    private Integer branchId;
    private Integer serial;
    private String statusText;
    
    

    /**
     * @return the docNumber
     */
    public String getDocNumber() {
        return docNumber;
    }

    /**
     * @param docNumber the docNumber to set
     */
    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the projectManager
     */
    public Integer getProjectManager() {
        return projectManager;
    }

    /**
     * @param projectManager the projectManager to set
     */
    public void setProjectManager(Integer projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * @return the generalConsultant
     */
    public Integer getGeneralConsultant() {
        return generalConsultant;
    }

    /**
     * @param generalConsultant the generalConsultant to set
     */
    public void setGeneralConsultant(Integer generalConsultant) {
        this.generalConsultant = generalConsultant;
    }

    /**
     * @return the profissionalBussinessConsultant
     */
    public Integer getProfissionalBussinessConsultant() {
        return profissionalBussinessConsultant;
    }

    /**
     * @param profissionalBussinessConsultant the
     * profissionalBussinessConsultant to set
     */
    public void setProfissionalBussinessConsultant(Integer profissionalBussinessConsultant) {
        this.profissionalBussinessConsultant = profissionalBussinessConsultant;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the statusText
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * @param statusText the statusText to set
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the projectManagerName
     */
    public String getProjectManagerName() {
        return projectManagerName;
    }

    /**
     * @param projectManagerName the projectManagerName to set
     */
    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    /**
     * @return the generalConsultantName
     */
    public String getGeneralConsultantName() {
        return generalConsultantName;
    }

    /**
     * @param generalConsultantName the generalConsultantName to set
     */
    public void setGeneralConsultantName(String generalConsultantName) {
        this.generalConsultantName = generalConsultantName;
    }


    /**
     * @return the profissionalBussinessConsultantName
     */
    public String getProfissionalBussinessConsultantName() {
        return profissionalBussinessConsultantName;
    }

    /**
     * @param profissionalBussinessConsultantName the profissionalBussinessConsultantName to set
     */
    public void setProfissionalBussinessConsultantName(String profissionalBussinessConsultantName) {
        this.profissionalBussinessConsultantName = profissionalBussinessConsultantName;
    }

    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

}

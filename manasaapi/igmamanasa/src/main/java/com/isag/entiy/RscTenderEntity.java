package com.isag.entiy;


import java.util.Date;

public class RscTenderEntity extends BaseEntity {

    private String docNumber;
    private Date date;
    private Integer status;
    private String remark;
    private Integer serial;
    private Integer branchId;
    private Integer customerId;
    private Integer generalConsultant;
    private Integer projectManager;
    private Integer profissionalBussinessConsultant;

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

}

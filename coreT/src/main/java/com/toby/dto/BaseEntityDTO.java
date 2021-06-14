/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hq004
 */
public class BaseEntityDTO implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    protected Integer id;

    private Boolean markEdit;

    private String msg;

    private Boolean markDisable;

    private Integer createdBy;

    private Date createdDate;

    private Integer index;

    private Integer companyId;

    private Integer branchId;

    private Integer serial;

    private String createdByName;
    
    private String createdInvoiceByName;

    private String createdByCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the markEdit
     */
    public Boolean getMarkEdit() {
        if (markEdit == null) {
            markEdit = false;
        }
        return markEdit;
    }

    /**
     * @param markEdit the markEdit to set
     */
    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Integer)) {
            return false;
        }
        Integer other = (Integer) object;
        if ((id == null && id != null) || (id != null && !id.equals(other))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityDTO -> id=" + id + " ]";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the markDisable
     */
    public Boolean getMarkDisable() {
        return markDisable;
    }

    /**
     * @param markDisable the markDisable to set
     */
    public void setMarkDisable(Boolean markDisable) {
        this.markDisable = markDisable;
    }

    /**
     * @return the createdBy
     */
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
     * @return the createdByName
     */
    public String getCreatedByName() {
        return createdByName;
    }

    /**
     * @param createdByName the createdByName to set
     */
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    /**
     * @return the createdByCode
     */
    public String getCreatedByCode() {
        return createdByCode;
    }

    /**
     * @param createdByCode the createdByCode to set
     */
    public void setCreatedByCode(String createdByCode) {
        this.createdByCode = createdByCode;
    }

    /**
     * @return the createdInvoiceByName
     */
    public String getCreatedInvoiceByName() {
        return createdInvoiceByName;
    }

    /**
     * @param createdInvoiceByName the createdInvoiceByName to set
     */
    public void setCreatedInvoiceByName(String createdInvoiceByName) {
        this.createdInvoiceByName = createdInvoiceByName;
    }
}

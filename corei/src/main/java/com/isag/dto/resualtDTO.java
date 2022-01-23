/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.entity.IsagUser;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Lenovo
 */
public class resualtDTO {
    private Integer Id;
    private Integer serial ;
    private String clientName;
    private String createdBy;
    private String phone ;
    private Date creationDate ;
    private Date deletionDate ;
    private String deletedBy ;
    private BigDecimal value;
    private BigDecimal sumValue ;

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
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the sumValue
     */
    public BigDecimal getSumValue() {
        if(sumValue == null){
         sumValue = BigDecimal.ZERO;
        }
        return sumValue;
    }

    /**
     * @param sumValue the sumValue to set
     */
    public void setSumValue(BigDecimal sumValue) {
        
        this.sumValue = sumValue;
    }

    /**
     * @return the deletionDate
     */
    public Date getDeletionDate() {
        return deletionDate;
    }

    /**
     * @param deletionDate the deletionDate to set
     */
    public void setDeletionDate(Date deletionDate) {
        this.deletionDate = deletionDate;
    }

    /**
     * @return the deletedBy
     */
    public String getDeletedBy() {
        return deletedBy;
    }

    /**
     * @param deletedBy the deletedBy to set
     */
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    /**
     * @return the Id
     */
    public Integer getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(Integer Id) {
        this.Id = Id;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
 
}

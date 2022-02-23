/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author AhmedEssam
 */
@Table(name = "gl_bank_transaction_detail_temp")
@Embeddable 
public class GlBankTransactionDetailTemp  implements Serializable{

    

   
    @Column(name = "user_Id")
    private Integer userId; 
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date; 
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "installment_id")
    private Integer installmentId;
    
   

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
     * @return the installmentId
     */
    public Integer getInstallmentId() {
        return installmentId;
    }

    /**
     * @param installmentId the installmentId to set
     */
    public void setInstallmentId(Integer installmentId) {
        this.installmentId = installmentId;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
 
}

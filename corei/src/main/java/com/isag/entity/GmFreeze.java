/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "gm_freeze")
public class GmFreeze  extends BaseEntity{
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite customerId;
     @NotNull
    @Column(name = "dateto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datefrom")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datefrom;
    @Column(name = "number")
    private Integer number;
    @JoinColumn(name = "subscirbtion_id", referencedColumnName = "id")
    @ManyToOne
    private GmSubscriptions subscirbtionId;

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the customerId
     */
    public InvOrganizationSite getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(InvOrganizationSite customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the dateto
     */
    public Date getDateto() {
        return dateto;
    }

    /**
     * @param dateto the dateto to set
     */
    public void setDateto(Date dateto) {
        this.dateto = dateto;
    }

    /**
     * @return the datefrom
     */
    public Date getDatefrom() {
        return datefrom;
    }

    /**
     * @param datefrom the datefrom to set
     */
    public void setDatefrom(Date datefrom) {
        this.datefrom = datefrom;
    }

    /**
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return the subscirbtionId
     */
    public GmSubscriptions getSubscirbtionId() {
        return subscirbtionId;
    }

    /**
     * @param subscirbtionId the subscirbtionId to set
     */
    public void setSubscirbtionId(GmSubscriptions subscirbtionId) {
        this.subscirbtionId = subscirbtionId;
    }
    
    
   
}

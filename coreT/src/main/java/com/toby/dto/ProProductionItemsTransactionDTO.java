/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.Branch;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author H
 */
public class ProProductionItemsTransactionDTO extends BaseEntityDTO{
    private static final long serialVersionUID = 1L;
  
    private Integer id;
  
    private Date creationDate;
   
    private Date modificationDate;
  
    private Integer serial;
   
    private BigDecimal number;
   
    private BigDecimal productionStageCost;
   
    private Date date;
   
    private Integer status;
   
    private Integer isDeleted;
   
    private Branch branchId;
   
    
    private TobyUserDTO createdBy;
    
    private InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailId;
   
    private TobyUserDTO modifiedBy;
   
    private ProProductionStagesDTO proProductionStagesId;
   
    private ProProductionTransactionDTO proProductionTransactionId;



  

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getProductionStageCost() {
        return productionStageCost;
    }

    public void setProductionStageCost(BigDecimal productionStageCost) {
        this.productionStageCost = productionStageCost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }


    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

   

    public InvPurchaseInvoiceDetailDTO getInvPurchaseInvoiceDetailId() {
        return invPurchaseInvoiceDetailId;
    }

    public void setInvPurchaseInvoiceDetailId(InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailId) {
        this.invPurchaseInvoiceDetailId = invPurchaseInvoiceDetailId;
    }

  

  

    public ProProductionStagesDTO getProProductionStagesId() {
        return proProductionStagesId;
    }

    public void setProProductionStagesId(ProProductionStagesDTO proProductionStagesId) {
        this.proProductionStagesId = proProductionStagesId;
    }

    public ProProductionTransactionDTO getProProductionTransactionId() {
        return proProductionTransactionId;
    }

    public void setProProductionTransactionId(ProProductionTransactionDTO proProductionTransactionId) {
        this.proProductionTransactionId = proProductionTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProProductionTransactionDTO)) {
            return false;
        }
        ProProductionTransactionDTO other = (ProProductionTransactionDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.ProProductionItemsTransaction[ id=" + id + " ]";
    }
    
}

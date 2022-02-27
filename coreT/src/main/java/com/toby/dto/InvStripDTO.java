/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.InvStripDetail;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author omar nezam
 */
public class InvStripDTO extends BaseEntityDTO{
   
   
   
    private String remarks;
    private String stripDocument;
    private Date stripDate;
    private Collection<InvStripDetail> invStripDetailCollection;
    private InvInventoryDTO inventoryId;
    private Integer type;
    private List<InvStripDetailDTO> invStripDetailDTOList;
     
    private BigDecimal totalQuantity ;
    private BigDecimal totalBalance ;
   

   
   

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStripDocument() {
        return stripDocument;
    }

    public void setStripDocument(String stripDocument) {
        this.stripDocument = stripDocument;
    }

    public Date getStripDate() {
        return stripDate;
    }

    public void setStripDate(Date stripDate) {
        this.stripDate = stripDate;
    }

    @XmlTransient
    public Collection<InvStripDetail> getInvStripDetailCollection() {
        return invStripDetailCollection;
    }

    public void setInvStripDetailCollection(Collection<InvStripDetail> invStripDetailCollection) {
        this.invStripDetailCollection = invStripDetailCollection;
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
        if (!(object instanceof InvStripDTO)) {
            return false;
        }
        InvStripDTO other = (InvStripDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.InvStrip[ id=" + id + " ]";
    }

    public InvInventoryDTO getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(InvInventoryDTO inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

 
    public List<InvStripDetailDTO> getInvStripDetailDTOList() {
        return invStripDetailDTOList;
    }

    /**
     * @param invStripDetailDTOList the invStripDetailDTOList to set
     */
    public void setInvStripDetailDTOList(List<InvStripDetailDTO> invStripDetailDTOList) {
        this.invStripDetailDTOList = invStripDetailDTOList;
    }

    /**
     * @return the totalQuantity
     */
    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    /**
     * @param totalQuantity the totalQuantity to set
     */
    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    /**
     * @return the totalBalance
     */
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    /**
     * @param totalBalance the totalBalance to set
     */
    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

}

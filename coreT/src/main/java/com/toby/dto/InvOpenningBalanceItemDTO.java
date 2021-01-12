/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author omar nezam
 */
public class InvOpenningBalanceItemDTO extends BaseEntityDTO {

    private Integer serial;

    private String documentStrip;

    private Date date;

    private String remark;
    private BigDecimal sumquantaty;
    private BigDecimal sumNet;

    private InvInventoryDTO invInventoryId;

    private List<InvOpenningBalanceItemDetailDTO> invOpenningBalanceItemDetailDTOList;

    private BigDecimal totalQuatity;
    private BigDecimal total;
    private BigDecimal totalNet;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvOpenningBalanceItemDTO)) {
            return false;
        }
        InvOpenningBalanceItemDTO other = (InvOpenningBalanceItemDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial != null ? serial.toString() : "";
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getDocumentStrip() {
        return documentStrip;
    }

    public void setDocumentStrip(String documentStrip) {
        this.documentStrip = documentStrip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public InvInventoryDTO getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventoryDTO invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

    /**
     * @return the invOpenningBalanceItemDetailDTOList
     */
    public List<InvOpenningBalanceItemDetailDTO> getInvOpenningBalanceItemDetailDTOList() {
//        if (invOpenningBalanceItemDetailDTOList == null || invOpenningBalanceItemDetailDTOList.isEmpty()) {
//            invOpenningBalanceItemDetailDTOList = new ArrayList<>();
//        }
        return invOpenningBalanceItemDetailDTOList;
    }

    /**
     * @param invOpenningBalanceItemDetailDTOList the
     * invOpenningBalanceItemDetailDTOList to set
     */
    public void setInvOpenningBalanceItemDetailDTOList(List<InvOpenningBalanceItemDetailDTO> invOpenningBalanceItemDetailDTOList) {
        this.invOpenningBalanceItemDetailDTOList = invOpenningBalanceItemDetailDTOList;
    }

    /**
     * @return the sumquantaty
     */
    public BigDecimal getSumquantaty() {
        
           if (sumquantaty == null) {
            sumquantaty = BigDecimal.ZERO;}
        return sumquantaty;
    }

    /**
     * @param sumquantaty the sumquantaty to set
     */
    public void setSumquantaty(BigDecimal sumquantaty) {
        this.sumquantaty = sumquantaty;
    }

    /**
     * @return the sumNet
     */
    public BigDecimal getSumNet() {
        
         if (sumNet == null) {
            sumNet = BigDecimal.ZERO;
         
         }
        return sumNet;
    }

    /**
     * @param sumNet the sumNet to set
     */
    public void setSumNet(BigDecimal sumNet) {
        this.sumNet = sumNet;
    }

    /**
     * @return the totalQuatity
     */
    public BigDecimal getTotalQuatity() {
         if (totalQuatity == null) {
            totalQuatity = BigDecimal.ZERO;
        }

        return totalQuatity;
    }

    /**
     * @param totalQuatity the totalQuatity to set
     */
    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        
         if (total == null) {
            total = BigDecimal.ZERO;
        }
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the totalNet
     */
    public BigDecimal getTotalNet() {
        if (totalNet == null) {
            totalNet = BigDecimal.ZERO;
        }
        return totalNet;
    }

    /**
     * @param totalNet the totalNet to set
     */
    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

}

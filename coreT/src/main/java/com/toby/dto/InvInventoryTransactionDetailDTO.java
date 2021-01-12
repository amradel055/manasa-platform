/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.QuantityItemsStoreAddExit;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;

/**
 *
 * @author user4
 */
public class InvInventoryTransactionDetailDTO extends BaseEntityDTO{
     private static final long serialVersionUID = 1L;
    private Integer serial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    private BigDecimal quantity;
    
    private BigDecimal finalQuantity;
    private BigDecimal screwing;
    private String itemBarcode;
    private Integer status;
    private Integer transferFrom;
      
    private InvPurchaseInvoiceDetailDTO selectedId;
    private InvItemDTO itemId;
    private SymbolDTO unitId;
    private InvInventoryTransactionDTO inventoryTransactionId;
    private InvPurchaseOrderDetailDTO selectedPurchaseOrderId;
    private InvPurchaseInvoiceDetailDTO selectedPurchaseId;
    private BigDecimal availableQuantity;
  private Integer isdeleted;
    
    
    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

   
    public BigDecimal getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(BigDecimal finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(Integer transferFrom) {
        this.transferFrom = transferFrom;
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
        if (!(object instanceof InvInventoryTransactionDetailDTO)) {
            return false;
        }
        InvInventoryTransactionDetailDTO other = (InvInventoryTransactionDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "newpackage.InvInventoryTransactionDetail[ id=" + id + " ]";
    }

    /**
     * @param selectedId the selectedId to set
     */
   
    /**
     * @return the itemId
     */
    public InvItemDTO getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(InvItemDTO itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the unitId
     */
    public SymbolDTO getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(SymbolDTO unitId) {
        this.unitId = unitId;
    }

    /**
     * @return the inventoryTransactionId
     */
    public InvInventoryTransactionDTO getInventoryTransactionId() {
        return inventoryTransactionId;
    }

    /**
     * @param inventoryTransactionId the inventoryTransactionId to set
     */
    public void setInventoryTransactionId(InvInventoryTransactionDTO inventoryTransactionId) {
        this.inventoryTransactionId = inventoryTransactionId;
    }

    /**
     * @return the selectedPurchaseOrderId
     */
  

    /**
     * @return the selectedPurchaseId
     */
    public InvPurchaseInvoiceDetailDTO getSelectedPurchaseId() {
        return selectedPurchaseId;
    }

    /**
     * @param selectedPurchaseId the selectedPurchaseId to set
     */
    public void setSelectedPurchaseId(InvPurchaseInvoiceDetailDTO selectedPurchaseId) {
        this.selectedPurchaseId = selectedPurchaseId;
    }

    /**
     * @return the selectedId
     */
    public InvPurchaseInvoiceDetailDTO getSelectedId() {
        return selectedId;
    }

    /**
     * @param selectedId the selectedId to set
     */
    public void setSelectedId(InvPurchaseInvoiceDetailDTO selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * @return the selectedPurchaseOrderId
     */
    public InvPurchaseOrderDetailDTO getSelectedPurchaseOrderId() {
        return selectedPurchaseOrderId;
    }

    /**
     * @param selectedPurchaseOrderId the selectedPurchaseOrderId to set
     */
    public void setSelectedPurchaseOrderId(InvPurchaseOrderDetailDTO selectedPurchaseOrderId) {
        this.selectedPurchaseOrderId = selectedPurchaseOrderId;
    }

    /**
     * @return the availableQuantity
     */
    public BigDecimal getAvailableQuantity() {
        return availableQuantity;
    }

    /**
     * @param availableQuantity the availableQuantity to set
     */
    public void setAvailableQuantity(BigDecimal availableQuantity) {
        this.availableQuantity = availableQuantity;
    }   

    /**
     * @return the isdeleted
     */
    public Integer getIsdeleted() {
        return isdeleted;
    }

    /**
     * @param isdeleted the isdeleted to set
     */
    public void setIsdeleted(Integer isdeleted) {
        this.isdeleted = isdeleted;
    }
   
}

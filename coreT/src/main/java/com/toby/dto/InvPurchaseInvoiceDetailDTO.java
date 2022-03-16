/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.InvReturnPurchaseDetail;
import com.toby.entity.UnitsItems;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author H
 */
public class InvPurchaseInvoiceDetailDTO  extends  BaseEntityDTO{
    private BigDecimal quantity;
    private BigDecimal cost;
    private BigDecimal discount;
    private Integer discountType;
    private Integer transferFrom;
    private BigDecimal finalQuantity;
    private BigDecimal weight;
    private InvPurchaseInvoiceDTO1 invPurchaseInvoiceId;
    private InvItemDTO itemId;
    private Integer item_id;
    private String itemName;
    private Integer unitId;
    private BigDecimal extraCost;
    private Integer selectedId;
    private BigDecimal screwing;
    private String itemBarcode;
    private BigDecimal net;
    private Integer serial;
    private BigDecimal taxValue;
    private Integer status;
    private BigDecimal costAvarage;
    private BigDecimal total;
    private Collection<InvReturnPurchaseDetail> invReturnPurchaseDetailCollection;
    private BigDecimal bounse;
    private BigDecimal availableQuantity;
    private InvInventoryDTO invInventoryDTO;
    private Integer isdeleted;
    private List<UnitsItems> unitsItemseList;
    private Integer unitsItemsSelected;
    private UnitsItems unitsItem;
    private BigDecimal number;
    private InvItemDTO invItemParentId;
    private BigDecimal totalQuantityRow;
    private Integer clothNumber;
    
    public InvPurchaseInvoiceDetailDTO() {
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Integer getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(Integer transferFrom) {
        this.transferFrom = transferFrom;
    }

   


    /**
     * @return the extraCost
     */
    public BigDecimal getExtraCost() {
        return extraCost;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    /**
     * @param extraCost the extraCost to set
     */
    public void setExtraCost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex() != null ? getIndex().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvPurchaseInvoiceDetailDTO)) {
            return false;
        }
        InvPurchaseInvoiceDetailDTO other = (InvPurchaseInvoiceDetailDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return itemBarcode + " " + (getItemId() == null ? "" : getItemId().getName());
    }

   
    /**
     * @return the itemBarcode
     */
    public String getItemBarcode() {
        return itemBarcode;
    }

    /**
     * @param itemBarcode the itemBarcode to set
     */
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }
    
    public BigDecimal getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
    }
    
    @XmlTransient
    public Collection<InvReturnPurchaseDetail> getInvReturnPurchaseDetailCollection() {
        return invReturnPurchaseDetailCollection;
    }

    public void setInvReturnPurchaseDetailCollection(Collection<InvReturnPurchaseDetail> invReturnPurchaseDetailCollection) {
        this.invReturnPurchaseDetailCollection = invReturnPurchaseDetailCollection;
    }

    /**
     * @return the finalQuantity
     */
    public BigDecimal getFinalQuantity() {
        return finalQuantity;
    }

    /**
     * @param finalQuantity the finalQuantity to set
     */
    public void setFinalQuantity(BigDecimal finalQuantity) {
        this.finalQuantity = finalQuantity;
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
     * @return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * @return the costAvarage
     */
    public BigDecimal getCostAvarage() {
        return costAvarage;
    }

    /**
     * @param costAvarage the costAvarage to set
     */
    public void setCostAvarage(BigDecimal costAvarage) {
        this.costAvarage = costAvarage;
    }

    /**
     * @return the invPurchaseInvoiceId
     */
    public InvPurchaseInvoiceDTO1 getInvPurchaseInvoiceId() {
        return invPurchaseInvoiceId;
    }

    /**
     * @param invPurchaseInvoiceId the invPurchaseInvoiceId to set
     */
    public void setInvPurchaseInvoiceId(InvPurchaseInvoiceDTO1 invPurchaseInvoiceId) {
        this.invPurchaseInvoiceId = invPurchaseInvoiceId;
    }

    /**
     * @return the unitId
     */
    public Integer getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

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
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }


    /**
     * @return the bounse
     */
    public BigDecimal getBounse() {
        return bounse;
    }

    /**
     * @param bounse the bounse to set
     */
    public void setBounse(BigDecimal bounse) {
        this.bounse = bounse;
    }


    /**
     * @return the selectedId
     */
    public Integer getSelectedId() {
        return selectedId;
    }

    /**
     * @param selectedId the selectedId to set
     */
    public void setSelectedId(Integer selectedId) {
        this.selectedId = selectedId;
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
     * @return the invInventoryDTO
     */
    public InvInventoryDTO getInvInventoryDTO() {
        return invInventoryDTO;
    }

    /**
     * @param invInventoryDTO the invInventoryDTO to set
     */
    public void setInvInventoryDTO(InvInventoryDTO invInventoryDTO) {
        this.invInventoryDTO = invInventoryDTO;
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

    /**
     * @return the symbolDTOList
     */
    public List<UnitsItems> getUnitsItemseList() {
        return unitsItemseList;
    }

    /**
     * @param symbolDTOList the symbolDTOList to set
     */
    public void setUnitsItemseList(List<UnitsItems> symbolDTOList) {
        this.unitsItemseList = symbolDTOList;
    }

    /**
     * @return the unitsItemsSelected
     */
    public Integer getUnitsItemsSelected() {
        return unitsItemsSelected;
    }

    /**
     * @param unitsItemsSelected the unitsItemsSelected to set
     */
    public void setUnitsItemsSelected(Integer unitsItemsSelected) {
        this.unitsItemsSelected = unitsItemsSelected;
    }

    /**
     * @return the unitsItem
     */
    public UnitsItems getUnitsItem() {
        return unitsItem;
    }

    /**
     * @param unitsItem the unitsItem to set
     */
    public void setUnitsItem(UnitsItems unitsItem) {
        this.unitsItem = unitsItem;
    }

    /**
     * @return the number
     */
    public BigDecimal getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    /**
     * @return the invItemParentId
     */
    public InvItemDTO getInvItemParentId() {
        return invItemParentId;
    }

    /**
     * @param invItemParentId the invItemParentId to set
     */
    public void setInvItemParentId(InvItemDTO invItemParentId) {
        this.invItemParentId = invItemParentId;
    }

    /**
     * @return the totalQuantityRow
     */
    public BigDecimal getTotalQuantityRow() {
        return totalQuantityRow;
    }

    /**
     * @param totalQuantityRow the totalQuantityRow to set
     */
    public void setTotalQuantityRow(BigDecimal totalQuantityRow) {
        this.totalQuantityRow = totalQuantityRow;
    }

    /**
     * @return the clothNumber
     */
    public Integer getClothNumber() {
        return clothNumber;
    }

    /**
     * @param clothNumber the clothNumber to set
     */
    public void setClothNumber(Integer clothNumber) {
        this.clothNumber = clothNumber;
    }

    /**
     * @return the item_id
     */
    public Integer getItem_id() {
        return item_id;
    }

    /**
     * @param item_id the item_id to set
     */
    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

/**
 *
 * @author IT Shop
 */
public class GmSubscriptionsDetailDTO extends BaseEntityDTO {

    private BigDecimal quantity;
    private BigDecimal cost;
    private BigDecimal discount;
    private BigDecimal net;
    private Integer discountType;
    private BigDecimal extraCost;
    private Integer serial;
    private BigDecimal taxValue;
    private Integer status;
    private BigDecimal finalQuantity;
    private BigDecimal addTotal;
    private BigDecimal discountValue;
    private BigDecimal netAfterDiscountHead;
    private Integer gmSubscriptionsId;
    private InvItemDTO itemId;
    private SymbolDTO itemSymbolId;
    private SymbolDTO itemSymbolIdTrans;
    private Boolean isEdit;
    private BigDecimal valueRowDiscount;
    private BigDecimal valueAfterDiscount;
    private BigDecimal totalBeforeDiscount;
    private String itemName;
    private String itemCode;

    public GmSubscriptionsDetailDTO() {
    }

    public BigDecimal getQuantity() {
        if (quantity == null) {
            quantity = BigDecimal.ONE;
        }
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        if (cost == null) {
            cost = BigDecimal.ZERO;
        }
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getDiscount() {
        if (discount == null) {
            discount = BigDecimal.ZERO;
        }
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getNet() {

        net = (getQuantity().multiply(getCost())).add(getExtraCost()).subtract(getValueRowDiscount());

        return net;

    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getExtraCost() {
        if (extraCost == null) {
            extraCost = BigDecimal.ZERO;
        }
        return extraCost;
    }

    public void setExtraCost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getTaxValue() {
        if (taxValue == null) {
            taxValue = BigDecimal.ZERO;
        }
        return taxValue;
    }

    public void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getFinalQuantity() {
        if (finalQuantity == null) {
            finalQuantity = BigDecimal.ZERO;
        }
        return finalQuantity;
    }

    public void setFinalQuantity(BigDecimal finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    public BigDecimal getAddTotal() {
        if (addTotal == null) {
            addTotal = BigDecimal.ZERO;
        }
        return addTotal;
    }

    public void setAddTotal(BigDecimal addTotal) {
        this.addTotal = addTotal;
    }

    public BigDecimal getDiscountValue() {
        if (discountValue == null) {
            discountValue = BigDecimal.ZERO;
        }
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getNetAfterDiscountHead() {
        if (netAfterDiscountHead == null) {
            netAfterDiscountHead = BigDecimal.ZERO;
        }
        return netAfterDiscountHead;
    }

    public void setNetAfterDiscountHead(BigDecimal netAfterDiscountHead) {
        this.netAfterDiscountHead = netAfterDiscountHead;
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
        if (!(object instanceof GmSubscriptionsDetailDTO)) {
            return false;
        }
        GmSubscriptionsDetailDTO other = (GmSubscriptionsDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GmSubscriptionsDetail[ id=" + id + " ]";
    }

    /**
     * @return the gmSubscriptionsId
     */
    public Integer getGmSubscriptionsId() {
        return gmSubscriptionsId;
    }

    /**
     * @param gmSubscriptionsId the gmSubscriptionsId to set
     */
    public void setGmSubscriptionsId(Integer gmSubscriptionsId) {
        this.gmSubscriptionsId = gmSubscriptionsId;
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
     * @return the itemSymbolId
     */
    public SymbolDTO getItemSymbolId() {
        return itemSymbolId;
    }

    /**
     * @param itemSymbolId the itemSymbolId to set
     */
    public void setItemSymbolId(SymbolDTO itemSymbolId) {
        this.itemSymbolId = itemSymbolId;
    }

    /**
     * @return the isEdit
     */
    public Boolean getIsEdit() {
        return isEdit;
    }

    /**
     * @param isEdit the isEdit to set
     */
    public void setIsEdit(Boolean isEdit) {
        this.isEdit = isEdit;
    }

    /**
     * @return the valueRowDiscount
     */
    public BigDecimal getValueRowDiscount() {
        if (valueRowDiscount == null) {
            valueRowDiscount = BigDecimal.ZERO;
        }
        return valueRowDiscount;
    }

    /**
     * @param valueRowDiscount the valueRowDiscount to set
     */
    public void setValueRowDiscount(BigDecimal valueRowDiscount) {
        this.valueRowDiscount = valueRowDiscount;
    }

    /**
     * @return the itemSymbolIdTrans
     */
    public SymbolDTO getItemSymbolIdTrans() {
        return itemSymbolIdTrans;
    }

    /**
     * @param itemSymbolIdTrans the itemSymbolIdTrans to set
     */
    public void setItemSymbolIdTrans(SymbolDTO itemSymbolIdTrans) {
        this.itemSymbolIdTrans = itemSymbolIdTrans;
    }

    /**
     * @return the valueAfterDiscount
     */
    public BigDecimal getValueAfterDiscount() {
        if (valueAfterDiscount == null) {
            valueAfterDiscount = BigDecimal.ZERO;
        }
        return valueAfterDiscount;
    }

    /**
     * @param valueAfterDiscount the valueAfterDiscount to set
     */
    public void setValueAfterDiscount(BigDecimal valueAfterDiscount) {
        this.valueAfterDiscount = valueAfterDiscount;
    }

    /**
     * @return the totalBeforeDiscount
     */
    public BigDecimal getTotalBeforeDiscount() {
        if (totalBeforeDiscount == null) {
            totalBeforeDiscount = BigDecimal.ZERO;
        }
        return totalBeforeDiscount;
    }

    /**
     * @param totalBeforeDiscount the totalBeforeDiscount to set
     */
    public void setTotalBeforeDiscount(BigDecimal totalBeforeDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        if (itemSymbolId != null) {
            itemName = itemSymbolId.getName();
        } else if (itemSymbolIdTrans != null) {
            itemName = itemSymbolIdTrans.getName();
        }
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

}

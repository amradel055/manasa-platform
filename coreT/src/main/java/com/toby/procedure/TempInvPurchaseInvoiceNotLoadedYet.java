/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.procedure;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AhmedEssam
 */
@Entity
@Table(name = "inv_purchase_invoice_not_loaded_yet_procedure")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TempInvPurchaseInvoiceNotLoadedYet.findAll", query = "SELECT t FROM TempInvPurchaseInvoiceNotLoadedYet t")})
public class TempInvPurchaseInvoiceNotLoadedYet implements Serializable {

    private static final long serialVersionUID = 1L;

    public TempInvPurchaseInvoiceNotLoadedYet() {
    }

    @Column(name = "item_id")
    @Id
    private Integer itemId;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "number")
    private BigDecimal number;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Column(name = "unit_id")
    private Integer unitId;
    @Column(name = "item_barcode")
    @Max(40)
    private String itemBarcode;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "symbol_name")
    private String unitName;
    @Column(name = "symbol_serial")
    private Integer unitSerial;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "discount_type")
    private Integer discountType;
    @Column(name = "extra_cost")
    private BigDecimal extraCost;
    @Column(name = "net")
    private BigDecimal net;    
    @Column(name = "is_inventory_item")
    private Integer isInventoryItem;

    /**
     * @return the itemId
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the screwing
     */
    public BigDecimal getScrewing() {
        return screwing;
    }

    /**
     * @param screwing the screwing to set
     */
    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
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
     * @return the unitSerial
     */
    public Integer getUnitSerial() {
        return unitSerial;
    }

    /**
     * @param unitSerial the unitSerial to set
     */
    public void setUnitSerial(Integer unitSerial) {
        this.unitSerial = unitSerial;
    }

    /**
     * @return the unitName
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * @param unitName the unitName to set
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
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
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
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

    /**
     * @return the discountType
     */
    public Integer getDiscountType() {
        return discountType;
    }

    /**
     * @param discountType the discountType to set
     */
    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    /**
     * @return the extraCost
     */
    public BigDecimal getExtraCost() {
        return extraCost;
    }

    /**
     * @param extraCost the extraCost to set
     */
    public void setExtraCost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    /**
     * @return the isInventoryItem
     */
    public Integer getIsInventoryItem() {
        return isInventoryItem;
    }

    /**
     * @param isInventoryItem the isInventoryItem to set
     */
    public void setIsInventoryItem(Integer isInventoryItem) {
        this.isInventoryItem = isInventoryItem;
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

    

}

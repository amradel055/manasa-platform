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
@Table(name = "pro_purchase_invoice_not_loaded_yet_procedure")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TempProPurchaseInvoiceNotLoadedYet.findAll", query = "SELECT t FROM TempProPurchaseInvoiceNotLoadedYet t")})
public class TempProPurchaseInvoiceNotLoadedYet implements Serializable {

    private static final long serialVersionUID = 1L;

    public TempProPurchaseInvoiceNotLoadedYet() {
    }

    @Column(name = "id")
    @Id
    private Integer id;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "unit_id")
    private Integer unitId;
    
    @Column(name = "item_id")
    private Integer itemId;
    @Column(name = "item_barcode")
    @Max(40)
    private String itemBarcode;
    @Column(name = "item_name")
    @Max(100)
    private String itemName;
    @Column(name = "item_id_parent")
    private Integer itemIdParent;
    @Column(name = "item_barcode_parent")
    @Max(40)
    private String itemBarcodeParent;
    @Column(name = "item_name_parent")
    @Max(100)
    private String itemNameParent;
    @Column(name = "cloth_number")
    private Integer clothNumber;
    @Column(name = "number")
    private BigDecimal number;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "symbol_name")
    private String unitName;
    @Column(name = "symbol_serial")
    private Integer unitSerial;

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
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the itemIdParent
     */
    public Integer getItemIdParent() {
        return itemIdParent;
    }

    /**
     * @param itemIdParent the itemIdParent to set
     */
    public void setItemIdParent(Integer itemIdParent) {
        this.itemIdParent = itemIdParent;
    }

    /**
     * @return the itemBarcodeParent
     */
    public String getItemBarcodeParent() {
        return itemBarcodeParent;
    }

    /**
     * @param itemBarcodeParent the itemBarcodeParent to set
     */
    public void setItemBarcodeParent(String itemBarcodeParent) {
        this.itemBarcodeParent = itemBarcodeParent;
    }

    /**
     * @return the itemNameParent
     */
    public String getItemNameParent() {
        return itemNameParent;
    }

    /**
     * @param itemNameParent the itemNameParent to set
     */
    public void setItemNameParent(String itemNameParent) {
        this.itemNameParent = itemNameParent;
    }

    

}

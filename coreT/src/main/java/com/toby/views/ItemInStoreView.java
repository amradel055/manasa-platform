/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "item_in_store_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemInStoreView.findAll", query = "SELECT i FROM ItemInStoreView i")
    , @NamedQuery(name = "ItemInStoreView.findByRowNum", query = "SELECT i FROM ItemInStoreView i WHERE i.rowNum = :rowNum")
    , @NamedQuery(name = "ItemInStoreView.findByItemBarcode", query = "SELECT i FROM ItemInStoreView i WHERE i.itemBarcode = :itemBarcode")
    , @NamedQuery(name = "ItemInStoreView.findByBranchId", query = "SELECT i FROM ItemInStoreView i WHERE i.branchId = :branchId")
    , @NamedQuery(name = "ItemInStoreView.findByInventoryId", query = "SELECT i FROM ItemInStoreView i WHERE i.inventoryId = :inventoryId")})
public class ItemInStoreView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Size(max = 45)
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "inventory_id")
    private Integer inventoryId;

    public ItemInStoreView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }
    
}

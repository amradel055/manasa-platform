/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "inv_barcode")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvBarcode.findAll", query = "SELECT i FROM InvBarcode i"),
    @NamedQuery(name = "InvBarcode.findById", query = "SELECT i FROM InvBarcode i WHERE i.id = :id"),
    @NamedQuery(name = "InvBarcode.findByCode", query = "SELECT i FROM InvBarcode i WHERE i.code = :code"),
    @NamedQuery(name = "InvBarcode.findByPrice1", query = "SELECT i FROM InvBarcode i WHERE i.price1 = :price1"),
    @NamedQuery(name = "InvBarcode.findByScrewing", query = "SELECT i FROM InvBarcode i WHERE i.screwing = :screwing"),
    @NamedQuery(name = "InvBarcode.findByCreationDate", query = "SELECT i FROM InvBarcode i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvBarcode.findByModificationDate", query = "SELECT i FROM InvBarcode i WHERE i.modificationDate = :modificationDate")})
public class InvBarcode extends BaseEntity {

    @Size(max = 450)
    @Column(name = "code")
    private String code;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price1")
    private BigDecimal price1;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvItem itemId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "type_barcode", referencedColumnName = "id")
    @ManyToOne
    private Symbol typeBarcode;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    @Column(name = "height")
    private BigDecimal height;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "length")
    private BigDecimal length;
    @Column(name = "width")
    private BigDecimal width;
    @JoinColumn(name = "paint_color", referencedColumnName = "id")
    @ManyToOne
    private Symbol paintColor;

    public InvBarcode() {
    }

    public InvBarcode(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Symbol getTypeBarcode() {
        return typeBarcode;
    }

    public void setTypeBarcode(Symbol typeBarcode) {
        this.typeBarcode = typeBarcode;
    }

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
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
        if (!(object instanceof InvBarcode)) {
            return false;
        }
        InvBarcode other = (InvBarcode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.InvBarcode[ id=" + id + " ]";
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    /**
     * @return the paintColor
     */
    public Symbol getPaintColor() {
        return paintColor;
    }

    /**
     * @param paintColor the paintColor to set
     */
    public void setPaintColor(Symbol paintColor) {
        this.paintColor = paintColor;
    }

}

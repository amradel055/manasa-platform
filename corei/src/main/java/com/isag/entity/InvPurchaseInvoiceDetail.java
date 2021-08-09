/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inv_purchase_invoice_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findAll", query = "SELECT i FROM InvPurchaseInvoiceDetail i")
    ,
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findById", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.id = :id")
    ,
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByQuantity", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.quantity = :quantity")
    ,
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByCost", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.cost = :cost")
    ,
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByDiscount", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.discount = :discount")
    ,
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByCreationDate", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.creationDate = :creationDate")
    ,
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByModificationDate", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.modificationDate = :modificationDate")
    ,
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByDiscountType", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.discountType = :discountType")
    ,
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByTransferFrom", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.transferFrom = :transferFrom")
    ,
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByScrewing", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.screwing = :screwing")
    ,
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByTaxValue", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.taxValue = :taxValue")})
public class InvPurchaseInvoiceDetail extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "discount_type")
    private Integer discountType;
    @Column(name = "transfer_from")
    private Integer transferFrom;
    @Column(name = "final_quantity")
    private BigDecimal finalQuantity;
    @Column(name = "weight")
    private BigDecimal weight;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inv_purchase_invoice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvPurchaseInvoice invPurchaseInvoiceId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvItem itemId;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    @Column(name = "extra_cost")
    private BigDecimal extraCost;

    @Column(name = "selected_id")
    private Integer selectedId;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "net")
    private BigDecimal net;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "tax_value")
    private BigDecimal taxValue;
    @Column(name = "status")
    private Integer status;
    @Column(name = "cost_avarage")
    private BigDecimal costAvarage;
    @OneToMany(mappedBy = "invInvoicePurchaseDetailId")
    private Collection<InvReturnPurchaseDetail> invReturnPurchaseDetailCollection;

    @Transient
    private BigDecimal sumDetailNet;

    @Column(name = "disc1")
    private BigDecimal disc1;
    @Column(name = "disc2")
    private BigDecimal disc2;
    @Column(name = "disc3")
    private BigDecimal disc3;
    @Column(name = "add1")
    private BigDecimal add1;
    @Column(name = "add2")
    private BigDecimal add2;
    @Column(name = "add3")
    private BigDecimal add3;
    @Column(name = "total_before_fracture")
    private BigDecimal totalBeforefracture;
    @Column(name = "fracture_number")
    private BigDecimal fractureNumber;
    @Column(name = "value_fracture")
    private BigDecimal valuefracture;
    @Column(name = "carton_number")
    private BigDecimal cartonsNumber;
    @Column(name = "cartons_capacity")
    private BigDecimal cartonsCapacity;
    @Column(name = "addTotal")
    private BigDecimal addTotal;

    public InvPurchaseInvoiceDetail() {
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

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvPurchaseInvoice getInvPurchaseInvoiceId() {
        return invPurchaseInvoiceId;
    }

    public void setInvPurchaseInvoiceId(InvPurchaseInvoice invPurchaseInvoiceId) {
        this.invPurchaseInvoiceId = invPurchaseInvoiceId;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvPurchaseInvoiceDetail)) {
            return false;
        }
        InvPurchaseInvoiceDetail other = (InvPurchaseInvoiceDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return itemBarcode + " " + itemId.getName();
    }

    public Integer getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Integer selectedId) {
        this.selectedId = selectedId;
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
     * @return the sumDetailNet
     */
    public BigDecimal getSumDetailNet() {
        return sumDetailNet;
    }

    /**
     * @param sumDetailNet the sumDetailNet to set
     */
    public void setSumDetailNet(BigDecimal sumDetailNet) {
        this.sumDetailNet = sumDetailNet;
    }

    /**
     * @return the disc1
     */
    public BigDecimal getDisc1() {
        return disc1;
    }

    /**
     * @param disc1 the disc1 to set
     */
    public void setDisc1(BigDecimal disc1) {
        this.disc1 = disc1;
    }

    /**
     * @return the disc2
     */
    public BigDecimal getDisc2() {
        return disc2;
    }

    /**
     * @param disc2 the disc2 to set
     */
    public void setDisc2(BigDecimal disc2) {
        this.disc2 = disc2;
    }

    /**
     * @return the disc3
     */
    public BigDecimal getDisc3() {
        return disc3;
    }

    /**
     * @param disc3 the disc3 to set
     */
    public void setDisc3(BigDecimal disc3) {
        this.disc3 = disc3;
    }

    /**
     * @return the add1
     */
    public BigDecimal getAdd1() {
        return add1;
    }

    /**
     * @param add1 the add1 to set
     */
    public void setAdd1(BigDecimal add1) {
        this.add1 = add1;
    }

    /**
     * @return the add2
     */
    public BigDecimal getAdd2() {
        return add2;
    }

    /**
     * @param add2 the add2 to set
     */
    public void setAdd2(BigDecimal add2) {
        this.add2 = add2;
    }

    /**
     * @return the add3
     */
    public BigDecimal getAdd3() {
        return add3;
    }

    /**
     * @param add3 the add3 to set
     */
    public void setAdd3(BigDecimal add3) {
        this.add3 = add3;
    }

    /**
     * @return the totalBeforefracture
     */
    public BigDecimal getTotalBeforefracture() {
        return totalBeforefracture;
    }

    /**
     * @param totalBeforefracture the totalBeforefracture to set
     */
    public void setTotalBeforefracture(BigDecimal totalBeforefracture) {
        this.totalBeforefracture = totalBeforefracture;
    }

    /**
     * @return the fractureNumber
     */
    public BigDecimal getFractureNumber() {
        return fractureNumber;
    }

    /**
     * @param fractureNumber the fractureNumber to set
     */
    public void setFractureNumber(BigDecimal fractureNumber) {
        this.fractureNumber = fractureNumber;
    }

    /**
     * @return the valuefracture
     */
    public BigDecimal getValuefracture() {
        return valuefracture;
    }

    /**
     * @param valuefracture the valuefracture to set
     */
    public void setValuefracture(BigDecimal valuefracture) {
        this.valuefracture = valuefracture;
    }
    /**
     * @return the cartonsNumber
     */
    public BigDecimal getCartonsNumber() {
        return cartonsNumber;
    }

    /**
     * @param cartonsNumber the cartonsNumber to set
     */
    public void setCartonsNumber(BigDecimal cartonsNumber) {
        this.cartonsNumber = cartonsNumber;
    }

    /**
     * @return the cartonsCapacity
     */
    public BigDecimal getCartonsCapacity() {
        return cartonsCapacity;
    }

    /**
     * @param cartonsCapacity the cartonsCapacity to set
     */
    public void setCartonsCapacity(BigDecimal cartonsCapacity) {
        this.cartonsCapacity = cartonsCapacity;
    }

    /**
     * @return the addTotal
     */
    public BigDecimal getAddTotal() {
        return addTotal;
    }

    /**
     * @param addTotal the addTotal to set
     */
    public void setAddTotal(BigDecimal addTotal) {
        this.addTotal = addTotal;
    }

}

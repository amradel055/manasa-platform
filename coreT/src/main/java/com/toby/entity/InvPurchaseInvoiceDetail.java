/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findAll", query = "SELECT i FROM InvPurchaseInvoiceDetail i"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findById", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByQuantity", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByCost", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.cost = :cost"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByDiscount", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.discount = :discount"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByCreationDate", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByModificationDate", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByDiscountType", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.discountType = :discountType"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByTransferFrom", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.transferFrom = :transferFrom"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByScrewing", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.screwing = :screwing"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByTaxValue", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.taxValue = :taxValue"),
    @NamedQuery(name = "InvPurchaseInvoiceDetail.findByBounse", query = "SELECT i FROM InvPurchaseInvoiceDetail i WHERE i.bounse = :bounse")})
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
    @Column(name = "discount_value")
    private BigDecimal discountValue;
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
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventoryId;
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
    @Column(name = "bounse")
    private BigDecimal bounse;
    @Column(name = "is_deleted")
    private Integer isdeleted;
    @Column(name = "number")
    private BigDecimal number;
    @Column(name = "net_after_discount_head")
    private BigDecimal netAfterDiscountHead;
    @JoinColumn(name = "inv_item_parent_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem invItemParentId;
    @Column(name = "cloth_number")
    private Integer clothNumber;
    @Column(name = "quantity_of_one_unit")
    private BigDecimal quantityOfOneUnit;    
    @Column(name = "proof")
    private Integer proof;
    
    
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
     * @return the invInventoryId
     */
    public InvInventory getInvInventoryId() {
        return invInventoryId;
    }

    /**
     * @param invInventoryId the invInventoryId to set
     */
    public void setInvInventoryId(InvInventory invInventoryId) {
        this.invInventoryId = invInventoryId;
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
    public InvItem getInvItemParentId() {
        return invItemParentId;
    }

    /**
     * @param invItemParentId the invItemParentId to set
     */
    public void setInvItemParentId(InvItem invItemParentId) {
        this.invItemParentId = invItemParentId;
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
     * @return the quantityOfOneUnit
     */
    public BigDecimal getQuantityOfOneUnit() {
        return quantityOfOneUnit;
    }

    /**
     * @param quantityOfOneUnit the quantityOfOneUnit to set
     */
    public void setQuantityOfOneUnit(BigDecimal quantityOfOneUnit) {
        this.quantityOfOneUnit = quantityOfOneUnit;
    }

    /**
     * @return the proof
     */
    public Integer getProof() {
        if(proof == null){
            proof = 0 ;
        }
        return proof;
    }

    /**
     * @param proof the proof to set
     */
    public void setProof(Integer proof) {
        this.proof = proof;
    }

    /**
     * @return the sumDetailNet
     */
    public BigDecimal getSumDetailNet() {
        if(sumDetailNet == null){
            sumDetailNet = BigDecimal.ZERO;
        }
        return sumDetailNet;
    }

    /**
     * @param sumDetailNet the sumDetailNet to set
     */
    public void setSumDetailNet(BigDecimal sumDetailNet) {
        this.sumDetailNet = sumDetailNet;
    }

    /**
     * @return the netAfterDiscountHead
     */
    public BigDecimal getNetAfterDiscountHead() {
        return netAfterDiscountHead;
    }

    /**
     * @param netAfterDiscountHead the netAfterDiscountHead to set
     */
    public void setNetAfterDiscountHead(BigDecimal netAfterDiscountHead) {
        this.netAfterDiscountHead = netAfterDiscountHead;
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

}

package com.isag.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import javax.validation.constraints.Size;

/**
 * @author khaled
 */
@Entity
@Table(name = "inv_transfer_detail")
public class InvTransferDetail extends BaseEntity {

   // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Column(name = "post_flag")
    private Integer postFlag;
    @Size(max = 450)
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "transfer_from")
    private Integer transferFrom;
    @Column(name = "status")
    private Integer status;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "final_quantity")
    private BigDecimal finalQuantity;    
    @Column(name = "carton_number")
    private BigDecimal cartonNumber;
    @Column(name = "cartons_capacity")
    private BigDecimal cartonsCapacity;
    @OneToMany(mappedBy = "selectedId")
    private Collection<InvTransferDetail> invTransferDetailCollection;
    @JoinColumn(name = "selected_id", referencedColumnName = "id")
    @ManyToOne
    private InvTransferDetail selectedId;
    @JoinColumn(name = "inv_item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem invItemId;
    @JoinColumn(name = "inv_transfer_id", referencedColumnName = "id")
    @ManyToOne
    private InvTransfer invTransferId;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId; 

    public InvTransferDetail() {
    }

    public InvTransferDetail(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public InvItem getInvItemId() {
        return invItemId;
    }

    public void setInvItemId(InvItem invItemId) {
        this.invItemId = invItemId;
    }

    public InvTransfer getInvTransferId() {
        return invTransferId;
    }

    public void setInvTransferId(InvTransfer invTransferId) {
        this.invTransferId = invTransferId;
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
     * @return the postFlag
     */
    public Integer getPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
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


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvTransferDetail)) {
            return false;
        }
        InvTransferDetail other = (InvTransferDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.InvTransferDetail[ id=" + id + " ]";
    }

   
    /**
     * @return the transferFrom
     */
    public Integer getTransferFrom() {
        return transferFrom;
    }

    /**
     * @param transferFrom the transferFrom to set
     */
    public void setTransferFrom(Integer transferFrom) {
        this.transferFrom = transferFrom;
    }

    /**
     * @return the invTransferDetailCollection
     */
    public Collection<InvTransferDetail> getInvTransferDetailCollection() {
        return invTransferDetailCollection;
    }

    /**
     * @param invTransferDetailCollection the invTransferDetailCollection to set
     */
    public void setInvTransferDetailCollection(Collection<InvTransferDetail> invTransferDetailCollection) {
        this.invTransferDetailCollection = invTransferDetailCollection;
    }

    /**
     * @return the unitId
     */
    public Symbol getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
    }

    /**
     * @return the selectedId
     */
    public InvTransferDetail getSelectedId() {
        return selectedId;
    }

    /**
     * @param selectedId the selectedId to set
     */
    public void setSelectedId(InvTransferDetail selectedId) {
        this.selectedId = selectedId;
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
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the cartonNumber
     */
    public BigDecimal getCartonNumber() {
        return cartonNumber;
    }

    /**
     * @param cartonNumber the cartonNumber to set
     */
    public void setCartonNumber(BigDecimal cartonNumber) {
        this.cartonNumber = cartonNumber;
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

}

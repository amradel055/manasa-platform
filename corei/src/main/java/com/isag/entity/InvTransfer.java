package com.isag.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collection;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author khaled
 */
@Entity
@Table(name = "inv_transfer")
public class InvTransfer extends BaseEntity {

    /**
     * @return the typeActivity
     */
    public Integer getTypeActivity() {
        return typeActivity;
    }

    /**
     * @param typeActivity the typeActivity to set
     */
    public void setTypeActivity(Integer typeActivity) {
        this.typeActivity = typeActivity;
    }

    @Basic(optional = false)
    @Column(name = "serial_no")
    private int serialNo;
    @Column(name = "transfer_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transferDate;
  
    @Size(max = 200)
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "post_flag")
    private Integer postFlag;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "type_activity")
    private Integer typeActivity;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inv_from", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invFrom;
    @JoinColumn(name = "inv_to", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invTo;
    @OneToMany(mappedBy = "invTransferId")
    private Collection<InvTransferDetail> invTransferDetailCollection;
    @Column(name = "transfer_type")
    private Integer transferType;
    @Column(name = "status")
    private Integer status;
    @OneToMany(mappedBy = "parent")
    private Collection<InvTransfer> invTransferCollection;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private InvTransfer parent;

    public InvTransfer() {
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @XmlTransient
    public Collection<InvTransferDetail> getInvTransferDetailCollection() {
        return invTransferDetailCollection;
    }

    public void setInvTransferDetailCollection(Collection<InvTransferDetail> invTransferDetailCollection) {
        this.invTransferDetailCollection = invTransferDetailCollection;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvInventory getInvFrom() {
        return invFrom;
    }

    public void setInvFrom(InvInventory invFrom) {
        this.invFrom = invFrom;
    }

    public InvInventory getInvTo() {
        return invTo;
    }

    public void setInvTo(InvInventory invTo) {
        this.invTo = invTo;
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

  
    @XmlTransient
    public Collection<InvTransfer> getInvTransferCollection() {
        return invTransferCollection;
    }

    public void setInvTransferCollection(Collection<InvTransfer> invTransferCollection) {
        this.invTransferCollection = invTransferCollection;
    }

    public InvTransfer getParent() {
        return parent;
    }

    public void setParent(InvTransfer parent) {
        this.parent = parent;
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
        if (!(object instanceof InvTransfer)) {
            return false;
        }
        InvTransfer other = (InvTransfer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+invTo+ invFrom + serialNo  ;
    }

    /**
     * @return the transferType
     */
    public Integer getTransferType() {
        return transferType;
    }

    /**
     * @param transferType the transferType to set
     */
    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
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
    
}

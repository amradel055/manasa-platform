/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inv_reservation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvReservation.findAll", query = "SELECT i FROM InvReservation i"),
    @NamedQuery(name = "InvReservation.findById", query = "SELECT i FROM InvReservation i WHERE i.id = :id"),
    @NamedQuery(name = "InvReservation.findByModificationDate", query = "SELECT i FROM InvReservation i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvReservation.findByCreationDate", query = "SELECT i FROM InvReservation i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvReservation.findBySerialNo", query = "SELECT i FROM InvReservation i WHERE i.serial = :serial"),
    @NamedQuery(name = "InvReservation.findByReservationDate", query = "SELECT i FROM InvReservation i WHERE i.reservationDate = :reservationDate"),
    @NamedQuery(name = "InvReservation.findByAddress", query = "SELECT i FROM InvReservation i WHERE i.address = :address"),
    @NamedQuery(name = "InvReservation.findByRemarks", query = "SELECT i FROM InvReservation i WHERE i.remarks = :remarks"),
    @NamedQuery(name = "InvReservation.findByEndDate", query = "SELECT i FROM InvReservation i WHERE i.endDate = :endDate")})
public class InvReservation extends BaseEntity {

    @Basic(optional = false)
    @NotNull
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "reservation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationDate;
    @Size(max = 200)
    @Column(name = "address")
    private String address;
    @Size(max = 200)
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "delegator_id", referencedColumnName = "id")
    @ManyToOne
    private InventoryDelegator delegatorId;
    @JoinColumn(name = "inv_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invId;
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite siteId;
    @JoinColumn(name = "site_id_main", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite siteIdMain;    
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "discount_type")
    private Integer discountType;

    public InvReservation() {
    }

    public InvReservation(Integer id) {
        this.id = id;
    }

    public InvReservation(Integer id, Integer serialNo) {
        this.id = id;
        this.serial = serialNo;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InventoryDelegator getDelegatorId() {
        return delegatorId;
    }

    public void setDelegatorId(InventoryDelegator delegatorId) {
        this.delegatorId = delegatorId;
    }

    public InvInventory getInvId() {
        return invId;
    }

    public void setInvId(InvInventory invId) {
        this.invId = invId;
    }

    public InvOrganizationSite getSiteId() {
        return siteId;
    }

    public void setSiteId(InvOrganizationSite siteId) {
        this.siteId = siteId;
    }

    public InvOrganizationSite getSiteIdMain() {
        return siteIdMain;
    }

    public void setSiteIdMain(InvOrganizationSite siteIdMain) {
        this.siteIdMain = siteIdMain;
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
        if (!(object instanceof InvReservation)) {
            return false;
        }
        InvReservation other = (InvReservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "toby.com.entity.InvReservation[ id=" + id + " ]";
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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

}

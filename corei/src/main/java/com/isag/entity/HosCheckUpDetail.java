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
 * @author amr
 */
@Entity
@Table(name = "hos_check_up_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HosCheckUpDetail.findAll", query = "SELECT h FROM HosCheckUpDetail h")
    , @NamedQuery(name = "HosCheckUpDetail.findById", query = "SELECT h FROM HosCheckUpDetail h WHERE h.id = :id")
    , @NamedQuery(name = "HosCheckUpDetail.findByHosCheckUpId", query = "SELECT h FROM HosCheckUpDetail h WHERE h.hosCheckUpId = :hosCheckUpId")
    , @NamedQuery(name = "HosCheckUpDetail.findByRemarks", query = "SELECT h FROM HosCheckUpDetail h WHERE h.remarks = :remarks")
    , @NamedQuery(name = "HosCheckUpDetail.findByValue", query = "SELECT h FROM HosCheckUpDetail h WHERE h.value = :value")
    , @NamedQuery(name = "HosCheckUpDetail.findByDiscountRatio", query = "SELECT h FROM HosCheckUpDetail h WHERE h.discountRatio = :discountRatio")
    , @NamedQuery(name = "HosCheckUpDetail.findByCreationDate", query = "SELECT h FROM HosCheckUpDetail h WHERE h.creationDate = :creationDate")
    , @NamedQuery(name = "HosCheckUpDetail.findByModificationDate", query = "SELECT h FROM HosCheckUpDetail h WHERE h.modificationDate = :modificationDate")
    , @NamedQuery(name = "HosCheckUpDetail.findBySerial", query = "SELECT h FROM HosCheckUpDetail h WHERE h.serial = :serial")
    , @NamedQuery(name = "HosCheckUpDetail.findByTotal", query = "SELECT h FROM HosCheckUpDetail h WHERE h.total = :total")
    , @NamedQuery(name = "HosCheckUpDetail.findByBearRatio", query = "SELECT h FROM HosCheckUpDetail h WHERE h.bearRatio = :bearRatio")})
public class HosCheckUpDetail extends BaseEntity {

    @JoinColumn(name = "hos_check_up_id", referencedColumnName = "id")
    @ManyToOne()
    private HosCheckUp hosCheckUpId;
    @Size(max = 450)
    @Column(name = "remarks")
    private String remarks;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "discount_ratio")
    private BigDecimal discountRatio;

    @Column(name = "serial")
    private Integer serial;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "bear_ratio")
    private BigDecimal bearRatio;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "hos_doctor_service_id", referencedColumnName = "id")
    @ManyToOne
    private HosDoctor hosDoctorServiceId;

    public HosCheckUpDetail() {
    }

    public HosCheckUpDetail(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(BigDecimal discountRatio) {
        this.discountRatio = discountRatio;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getBearRatio() {
        return bearRatio;
    }

    public void setBearRatio(BigDecimal bearRatio) {
        this.bearRatio = bearRatio;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof HosCheckUpDetail)) {
            return false;
        }
        HosCheckUpDetail other = (HosCheckUpDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.en.entity.HosCheckUpDetail[ id=" + id + " ]";
    }

    /**
     * @return the hosDoctorServiceId
     */
    public HosDoctor getHosDoctorServiceId() {
        return hosDoctorServiceId;
    }

    /**
     * @param hosDoctorServiceId the hosDoctorServiceId to set
     */
    public void setHosDoctorServiceId(HosDoctor hosDoctorServiceId) {
        this.hosDoctorServiceId = hosDoctorServiceId;
    }

    /**
     * @return the hosCheckUpId
     */
    public HosCheckUp getHosCheckUpId() {
        return hosCheckUpId;
    }

    /**
     * @param hosCheckUpId the hosCheckUpId to set
     */
    public void setHosCheckUpId(HosCheckUp hosCheckUpId) {
        this.hosCheckUpId = hosCheckUpId;
    }

}

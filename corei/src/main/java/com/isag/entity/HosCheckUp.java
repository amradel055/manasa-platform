/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "hos_check_up")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HosCheckUp.findAll", query = "SELECT h FROM HosCheckUp h")
    , @NamedQuery(name = "HosCheckUp.findById", query = "SELECT h FROM HosCheckUp h WHERE h.id = :id")
    , @NamedQuery(name = "HosCheckUp.findByDate", query = "SELECT h FROM HosCheckUp h WHERE h.date = :date")
    , @NamedQuery(name = "HosCheckUp.findByValue", query = "SELECT h FROM HosCheckUp h WHERE h.value = :value")
    , @NamedQuery(name = "HosCheckUp.findByValueLocal", query = "SELECT h FROM HosCheckUp h WHERE h.valueLocal = :valueLocal")
    , @NamedQuery(name = "HosCheckUp.findByRemark", query = "SELECT h FROM HosCheckUp h WHERE h.remark = :remark")
    , @NamedQuery(name = "HosCheckUp.findByCreationDate", query = "SELECT h FROM HosCheckUp h WHERE h.creationDate = :creationDate")
    , @NamedQuery(name = "HosCheckUp.findByModificationDate", query = "SELECT h FROM HosCheckUp h WHERE h.modificationDate = :modificationDate")
    , @NamedQuery(name = "HosCheckUp.findBySerial", query = "SELECT h FROM HosCheckUp h WHERE h.serial = :serial")
    , @NamedQuery(name = "HosCheckUp.findByPaymentType", query = "SELECT h FROM HosCheckUp h WHERE h.paymentType = :paymentType")
    , @NamedQuery(name = "HosCheckUp.findByHosInsuranceRegisterationId", query = "SELECT h FROM HosCheckUp h WHERE h.hosInsuranceRegisterationId = :hosInsuranceRegisterationId")})
public class HosCheckUp extends BaseEntity {

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "payment_type")
    private Integer paymentType;
    @JoinColumn(name = "hos_insurance_registeration_id", referencedColumnName = "id")
    @ManyToOne
    private HosPatientRegistration hosInsuranceRegisterationId;
    @JoinColumn(name = "gl_bank_id", referencedColumnName = "id")
    @ManyToOne
    private GlBank glBankId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "hos_doctor_id", referencedColumnName = "id")
    @ManyToOne
    private HosDoctor hosDoctorId;
    @JoinColumn(name = "hos_clinic_id", referencedColumnName = "id")
    @ManyToOne
    private HosDoctor hosClinicId;
    @JoinColumn(name = "hos_patient_registeration_id", referencedColumnName = "id")
    @ManyToOne
    private HosPatientRegistration hosPatientRegisterationId;

    public HosCheckUp() {
    }

    public HosCheckUp(Integer id) {
        this.id = id;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValueLocal() {
        return valueLocal;
    }

    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public HosPatientRegistration getHosInsuranceRegisterationId() {
        return hosInsuranceRegisterationId;
    }

    public void setHosInsuranceRegisterationId(HosPatientRegistration hosInsuranceRegisterationId) {
        this.hosInsuranceRegisterationId = hosInsuranceRegisterationId;
    }

    public GlBank getGlBankId() {
        return glBankId;
    }

    public void setGlBankId(GlBank glBankId) {
        this.glBankId = glBankId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public HosDoctor getHosDoctorId() {
        return hosDoctorId;
    }

    public void setHosDoctorId(HosDoctor hosDoctorId) {
        this.hosDoctorId = hosDoctorId;
    }

    public HosPatientRegistration getHosPatientRegisterationId() {
        return hosPatientRegisterationId;
    }

    public void setHosPatientRegisterationId(HosPatientRegistration hosPatientRegisterationId) {
        this.hosPatientRegisterationId = hosPatientRegisterationId;
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
        if (!(object instanceof HosCheckUp)) {
            return false;
        }
        HosCheckUp other = (HosCheckUp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.en.entity.HosCheckUp[ id=" + id + " ]";
    }

    /**
     * @return the hosClinicId
     */
    public HosDoctor getHosClinicId() {
        return hosClinicId;
    }

    /**
     * @param hosClinicId the hosClinicId to set
     */
    public void setHosClinicId(HosDoctor hosClinicId) {
        this.hosClinicId = hosClinicId;
    }

    /**
     * @return the paymentType
     */
    public Integer getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

}

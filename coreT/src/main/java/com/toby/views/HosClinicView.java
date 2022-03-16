/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "hos_clinic_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HosClinicView.findAll", query = "SELECT c FROM HosClinicView c"),
    @NamedQuery(name = "HosClinicView.findByBranchId", query = "SELECT c FROM HosClinicView c WHERE c.branchId = :branchId"),
    @NamedQuery(name = "HosClinicView.findByHosDoctorId", query = "SELECT c FROM HosClinicView c WHERE c.hosDoctorId = :hosDoctorId"),
    @NamedQuery(name = "HosClinicView.findByHosClinicId", query = "SELECT c FROM HosClinicView c WHERE c.hosClinicId = :hosClinicId"),
    @NamedQuery(name = "HosClinicView.findByPaymentType", query = "SELECT c FROM HosClinicView c WHERE c.paymentType = :paymentType"),
    @NamedQuery(name = "HosClinicView.findByDate", query = "SELECT c FROM HosClinicView c WHERE c.date = :date"),
    @NamedQuery(name = "HosClinicView.findByHosCheckUpId", query = "SELECT c FROM HosClinicView c WHERE c.hosCheckUpId = :hosCheckUpId"),
    @NamedQuery(name = "HosClinicView.findByValue", query = "SELECT c FROM HosClinicView c WHERE c.value = :value"),
    @NamedQuery(name = "HosClinicView.findByDiscountRatio", query = "SELECT c FROM HosClinicView c WHERE c.discountRatio = :discountRatio"),
    @NamedQuery(name = "HosClinicView.findByTotal", query = "SELECT c FROM HosClinicView c WHERE c.total = :total"),
    @NamedQuery(name = "HosClinicView.findByBearRatio", query = "SELECT c FROM HosClinicView c WHERE c.bearRatio = :bearRatio"),
    @NamedQuery(name = "HosClinicView.findByClinicCode", query = "SELECT c FROM HosClinicView c WHERE c.clinicCode = :clinicCode"),
    @NamedQuery(name = "HosClinicView.findByClinicName", query = "SELECT c FROM HosClinicView c WHERE c.clinicName = :clinicName"),
    @NamedQuery(name = "HosClinicView.findByDoctorCode", query = "SELECT c FROM HosClinicView c WHERE c.doctorCode = :doctorCode"),
    @NamedQuery(name = "HosClinicView.findByDoctorName", query = "SELECT c FROM HosClinicView c WHERE c.doctorName = :doctorName"),
    @NamedQuery(name = "HosClinicView.findByServiceCode", query = "SELECT c FROM HosClinicView c WHERE c.serviceCode = :serviceCode"),
    @NamedQuery(name = "HosClinicView.findByServiceName", query = "SELECT c FROM HosClinicView c WHERE c.serviceName = :serviceName"),
    @NamedQuery(name = "HosClinicView.findByHosInsuranceId", query = "SELECT c FROM HosClinicView c WHERE c.hosInsuranceId = :hosInsuranceId"),
    @NamedQuery(name = "HosClinicView.findByHosInsuranceName", query = "SELECT c FROM HosClinicView c WHERE c.hosInsuranceName = :hosInsuranceName"),
    @NamedQuery(name = "HosClinicView.findByHosPatientId", query = "SELECT c FROM HosClinicView c WHERE c.hosPatientId = :hosPatientId"),
    @NamedQuery(name = "HosClinicView.findByHosPatientName", query = "SELECT c FROM HosClinicView c WHERE c.hosPatientName = :hosPatientName")})
public class HosClinicView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "rowNum")
    @Id
    private Integer rowNum;
    @Column(name = "branchId")
    private Integer branchId;
    @Column(name = "hosDoctorId")
    private Integer hosDoctorId;
    @Column(name = "hosClinicId")
    private Integer hosClinicId;
    @Column(name = "paymentType")
    private Integer paymentType;
    @Column(name = "hosCheckUpId")
    private Integer hosCheckUpId;
    @Column(name = "hosInsuranceId")
    private Integer hosInsuranceId;
    @Column(name = "hosPatientId")
    private Integer hosPatientId;
    @Size(max = 45)
    @Column(name = "hosInsuranceName")
    private String hosInsuranceName;
    @Size(max = 45)
    @Column(name = "hosPatientName")
    private String hosPatientName;
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "discountRatio")
    private BigDecimal discountRatio;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "bearRatio")
    private BigDecimal bearRatio;
    @Size(max = 45)
    @Column(name = "clinicCode")
    private String clinicCode;
    @Size(max = 45)
    @Column(name = "clinicName")
    private String clinicName;
    @Size(max = 45)
    @Column(name = "doctorCode")
    private String doctorCode;
    @Size(max = 45)
    @Column(name = "doctorName")
    private String doctorName;
    @Size(max = 45)
    @Column(name = "serviceCode")
    private String serviceCode;
    @Size(max = 45)
    @Column(name = "serviceName")
    private String serviceName;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    
    /**
     * @return the rowNum
     */
    public Integer getRowNum() {
        return rowNum;
    }

    /**
     * @param rowNum the rowNum to set
     */
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
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
     * @return the hosDoctorId
     */
    public Integer getHosDoctorId() {
        return hosDoctorId;
    }

    /**
     * @param hosDoctorId the hosDoctorId to set
     */
    public void setHosDoctorId(Integer hosDoctorId) {
        this.hosDoctorId = hosDoctorId;
    }

    /**
     * @return the hosClinicId
     */
    public Integer getHosClinicId() {
        return hosClinicId;
    }

    /**
     * @param hosClinicId the hosClinicId to set
     */
    public void setHosClinicId(Integer hosClinicId) {
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

    /**
     * @return the hosCheckUpId
     */
    public Integer getHosCheckUpId() {
        return hosCheckUpId;
    }

    /**
     * @param hosCheckUpId the hosCheckUpId to set
     */
    public void setHosCheckUpId(Integer hosCheckUpId) {
        this.hosCheckUpId = hosCheckUpId;
    }

    /**
     * @return the hosInsuranceId
     */
    public Integer getHosInsuranceId() {
        return hosInsuranceId;
    }

    /**
     * @param hosInsuranceId the hosInsuranceId to set
     */
    public void setHosInsuranceId(Integer hosInsuranceId) {
        this.hosInsuranceId = hosInsuranceId;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the discountRatio
     */
    public BigDecimal getDiscountRatio() {
        return discountRatio;
    }

    /**
     * @param discountRatio the discountRatio to set
     */
    public void setDiscountRatio(BigDecimal discountRatio) {
        this.discountRatio = discountRatio;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the bearRatio
     */
    public BigDecimal getBearRatio() {
        return bearRatio;
    }

    /**
     * @param bearRatio the bearRatio to set
     */
    public void setBearRatio(BigDecimal bearRatio) {
        this.bearRatio = bearRatio;
    }

    /**
     * @return the clinicCode
     */
    public String getClinicCode() {
        return clinicCode;
    }

    /**
     * @param clinicCode the clinicCode to set
     */
    public void setClinicCode(String clinicCode) {
        this.clinicCode = clinicCode;
    }

    /**
     * @return the clinicName
     */
    public String getClinicName() {
        return clinicName;
    }

    /**
     * @param clinicName the clinicName to set
     */
    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the hosPatientId
     */
    public Integer getHosPatientId() {
        return hosPatientId;
    }

    /**
     * @param hosPatientId the hosPatientId to set
     */
    public void setHosPatientId(Integer hosPatientId) {
        this.hosPatientId = hosPatientId;
    }

    /**
     * @return the hosInsuranceName
     */
    public String getHosInsuranceName() {
        return hosInsuranceName;
    }

    /**
     * @param hosInsuranceName the hosInsuranceName to set
     */
    public void setHosInsuranceName(String hosInsuranceName) {
        this.hosInsuranceName = hosInsuranceName;
    }

    /**
     * @return the hosPatientName
     */
    public String getHosPatientName() {
        return hosPatientName;
    }

    /**
     * @param hosPatientName the hosPatientName to set
     */
    public void setHosPatientName(String hosPatientName) {
        this.hosPatientName = hosPatientName;
    }

    /**
     * @return the doctorCode
     */
    public String getDoctorCode() {
        return doctorCode;
    }

    /**
     * @param doctorCode the doctorCode to set
     */
    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    /**
     * @return the doctorName
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * @param doctorName the doctorName to set
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     * @return the serviceCode
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * @param serviceCode the serviceCode to set
     */
    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
}

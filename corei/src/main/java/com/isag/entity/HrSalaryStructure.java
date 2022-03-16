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
 * @author `*"<:ahmedShaaban:>"*`
 */
@Entity
@Table(name = "hr_salary_structure")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrSalaryStructure.findAll", query = "SELECT h FROM HrSalaryStructure h"),
    @NamedQuery(name = "HrSalaryStructure.findById", query = "SELECT h FROM HrSalaryStructure h WHERE h.id = :id"),
    @NamedQuery(name = "HrSalaryStructure.findByModificationDate", query = "SELECT h FROM HrSalaryStructure h WHERE h.modificationDate = :modificationDate"),
    @NamedQuery(name = "HrSalaryStructure.findByCreationDate", query = "SELECT h FROM HrSalaryStructure h WHERE h.creationDate = :creationDate"),
    @NamedQuery(name = "HrSalaryStructure.findByActive", query = "SELECT h FROM HrSalaryStructure h WHERE h.active = :active"),
    @NamedQuery(name = "HrSalaryStructure.findByDateFrom", query = "SELECT h FROM HrSalaryStructure h WHERE h.dateFrom = :dateFrom"),
    @NamedQuery(name = "HrSalaryStructure.findByDateTo", query = "SELECT h FROM HrSalaryStructure h WHERE h.dateTo = :dateTo"),
    @NamedQuery(name = "HrSalaryStructure.findByTravelTicket", query = "SELECT h FROM HrSalaryStructure h WHERE h.travelTicket = :travelTicket"),
    @NamedQuery(name = "HrSalaryStructure.findByTravelTicketAmount", query = "SELECT h FROM HrSalaryStructure h WHERE h.travelTicketAmount = :travelTicketAmount"),
    @NamedQuery(name = "HrSalaryStructure.findByTravelTicketValue", query = "SELECT h FROM HrSalaryStructure h WHERE h.travelTicketValue = :travelTicketValue"),
    @NamedQuery(name = "HrSalaryStructure.findBySeviceEnd", query = "SELECT h FROM HrSalaryStructure h WHERE h.serviceEnd = :serviceEnd"),
    @NamedQuery(name = "HrSalaryStructure.findBySeviceEndDaysNumber", query = "SELECT h FROM HrSalaryStructure h WHERE h.serviceEndDaysNumber = :serviceEndDaysNumber")})
public class HrSalaryStructure extends BaseEntity {

    @Column(name = "active")
    private Boolean active;
    @Column(name = "date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "travel_ticket")
    private Boolean travelTicket;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "travel_ticket_amount")
    private BigDecimal travelTicketAmount;
    @Column(name = "travel_ticket_value")
    private BigDecimal travelTicketValue;
    @Column(name = "service_end")
    private Boolean serviceEnd;
    @Column(name = "service_end_days_number")
    private BigDecimal serviceEndDaysNumber;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employeeId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @Column(name = "months")
    private Integer months;
    @Column(name = "entry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entryDate;
    @Size(max = 45)
    @Column(name = "remark")
    private String remark;
    @Column(name = "type")
    private Integer type;
    @Column(name = "work_day_of_month")
    private Integer workDayOfMonth;
    @Column(name = "work_hour_of_day")
    private Integer workHourOfDay;
    

    public HrSalaryStructure() {
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Boolean getTravelTicket() {
        return travelTicket;
    }

    public void setTravelTicket(Boolean travelTicket) {
        this.travelTicket = travelTicket;
    }

    public BigDecimal getTravelTicketAmount() {
        return travelTicketAmount;
    }

    public void setTravelTicketAmount(BigDecimal travelTicketAmount) {
        this.travelTicketAmount = travelTicketAmount;
    }

    public BigDecimal getTravelTicketValue() {
        return travelTicketValue;
    }

    public void setTravelTicketValue(BigDecimal travelTicketValue) {
        this.travelTicketValue = travelTicketValue;
    }

    public Boolean getServiceEnd() {
        return serviceEnd;
    }

    public void setServiceEnd(Boolean serviceEnd) {
        this.serviceEnd = serviceEnd;
    }

    public BigDecimal getServiceEndDaysNumber() {
        return serviceEndDaysNumber;
    }

    public void setServiceEndDaysNumber(BigDecimal serviceEndDaysNumber) {
        this.serviceEndDaysNumber = serviceEndDaysNumber;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
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
        if (!(object instanceof HrSalaryStructure)) {
            return false;
        }
        HrSalaryStructure other = (HrSalaryStructure) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.HrSalaryStructure[ id=" + id + " ]";
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
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the entryDate
     */
    public Date getEntryDate() {
        return entryDate;
    }

    /**
     * @param entryDate the entryDate to set
     */
    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    /**
     * @return the months
     */
    public Integer getMonths() {
        return months;
    }

    /**
     * @param months the months to set
     */
    public void setMonths(Integer months) {
        this.months = months;
    }

    /**
     * @return the workDayOfMonth
     */
    public Integer getWorkDayOfMonth() {
        return workDayOfMonth;
    }

    /**
     * @param workDayOfMonth the workDayOfMonth to set
     */
    public void setWorkDayOfMonth(Integer workDayOfMonth) {
        this.workDayOfMonth = workDayOfMonth;
    }

    /**
     * @return the workHourOfDay
     */
    public Integer getWorkHourOfDay() {
        return workHourOfDay;
    }

    /**
     * @param workHourOfDay the workHourOfDay to set
     */
    public void setWorkHourOfDay(Integer workHourOfDay) {
        this.workHourOfDay = workHourOfDay;
    }

}

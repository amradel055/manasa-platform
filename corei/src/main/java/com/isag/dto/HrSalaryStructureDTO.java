/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class HrSalaryStructureDTO extends BaseEntityDTO {

    private Boolean active;
    private String activeText;
    private Date dateFrom;
    private Date dateTo;
    private Boolean travelTicket;
    private BigDecimal travelTicketAmount;
    private BigDecimal travelTicketValue;
    private Boolean serviceEnd;
    private BigDecimal serviceEndDaysNumber;
    private EmployeeDTO employeeId;
    private Integer type;
    private String remark;
    private Date entryDate;
    private String months;
    private Integer workDayOfMonth;
    private Integer workHourOfDay;
    private List<HrSalaryStructureAccuralDeductionDTO> hrEmployeeAccuralDTOList;
    private List<HrSalaryStructureAccuralDeductionDTO> hrEmployeeDeductionDTOList;
    private List<HrSalariesStructureEffectDTO> salariesEffectDTOList;
    private List<HrSalaryStructureAccuralDeductionDTO> hrSalaryStructureAccuralDeductionDTODeletedList;

    public HrSalaryStructureDTO() {
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

    public EmployeeDTO getEmployeeId() {
//        if (employeeId == null) {
//            employeeId = new EmployeeDTO();
//
//        }
        return employeeId;
    }

    public void setEmployeeId(EmployeeDTO employeeId) {
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
        if (!(object instanceof HrSalaryStructureDTO)) {
            return false;
        }
        HrSalaryStructureDTO other = (HrSalaryStructureDTO) object;
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
     * @return the hrEmployeeAccuralDTOList
     */
    public List<HrSalaryStructureAccuralDeductionDTO> getHrEmployeeAccuralDTOList() {
        if (hrEmployeeAccuralDTOList == null || hrEmployeeAccuralDTOList.isEmpty()) {
            hrEmployeeAccuralDTOList = new ArrayList<>();
        }
        return hrEmployeeAccuralDTOList;
    }

    /**
     * @param hrEmployeeAccuralDTOList the hrEmployeeAccuralDTOList to set
     */
    public void setHrEmployeeAccuralDTOList(List<HrSalaryStructureAccuralDeductionDTO> hrEmployeeAccuralDTOList) {
        this.hrEmployeeAccuralDTOList = hrEmployeeAccuralDTOList;
    }

    /**
     * @return the hrEmployeeDeductionDTOList
     */
    public List<HrSalaryStructureAccuralDeductionDTO> getHrEmployeeDeductionDTOList() {
        if (hrEmployeeDeductionDTOList == null || hrEmployeeDeductionDTOList.isEmpty()) {
            hrEmployeeDeductionDTOList = new ArrayList<>();
        }
        return hrEmployeeDeductionDTOList;
    }

    /**
     * @param hrEmployeeDeductionDTOList the hrEmployeeDeductionDTOList to set
     */
    public void setHrEmployeeDeductionDTOList(List<HrSalaryStructureAccuralDeductionDTO> hrEmployeeDeductionDTOList) {
        this.hrEmployeeDeductionDTOList = hrEmployeeDeductionDTOList;
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
     * @return the salariesEffectDTOlIST
     */
    public List<HrSalariesStructureEffectDTO> getSalariesEffectDTOList() {
        if (salariesEffectDTOList == null || salariesEffectDTOList.isEmpty()) {
            salariesEffectDTOList = new ArrayList<>();
        }
        return salariesEffectDTOList;
    }

    /**
     * @param salariesEffectDTOList the salariesEffectDTOlIST to set
     */
    public void setSalariesEffectDTOList(List<HrSalariesStructureEffectDTO> salariesEffectDTOList) {

        this.salariesEffectDTOList = salariesEffectDTOList;
    }

    /**
     * @return the hrSalaryStructureAccuralDeductionDTODeletedList
     */
    public List<HrSalaryStructureAccuralDeductionDTO> getHrSalaryStructureAccuralDeductionDTODeletedList() {
        if (hrSalaryStructureAccuralDeductionDTODeletedList == null || hrSalaryStructureAccuralDeductionDTODeletedList.isEmpty()) {
            hrSalaryStructureAccuralDeductionDTODeletedList = new ArrayList<>();
        }
        return hrSalaryStructureAccuralDeductionDTODeletedList;
    }

    /**
     * @param hrSalaryStructureAccuralDeductionDTODeletedList the
     * hrSalaryStructureAccuralDeductionDTODeletedList to set
     */
    public void setHrSalaryStructureAccuralDeductionDTODeletedList(List<HrSalaryStructureAccuralDeductionDTO> hrSalaryStructureAccuralDeductionDTODeletedList) {
        this.hrSalaryStructureAccuralDeductionDTODeletedList = hrSalaryStructureAccuralDeductionDTODeletedList;
    }

    /**
     * @return the activeText
     */
    public String getActiveText() {
        return activeText;
    }

    /**
     * @param activeText the activeText to set
     */
    public void setActiveText(String activeText) {
        this.activeText = activeText;
    }

    /**
     * @return the months
     */
    public String getMonths() {
        return months;
    }

    /**
     * @param months the months to set
     */
    public void setMonths(String months) {
        this.months = months;
    }

    /**
     * @return the workDayOfMonth
     */
    public Integer getWorkDayOfMonth() {
        if(workDayOfMonth == null){
            workDayOfMonth = 1 ;
        }
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
        if(workHourOfDay == null){
            workHourOfDay = 1 ;
        }
        return workHourOfDay;
    }

    /**
     * @param workHourOfDay the workHourOfDay to set
     */
    public void setWorkHourOfDay(Integer workHourOfDay) {
        this.workHourOfDay = workHourOfDay;
    }

}

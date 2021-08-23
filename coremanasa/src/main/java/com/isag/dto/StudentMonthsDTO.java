/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.entity.*;
import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */

public class StudentMonthsDTO extends BaseEntityDTO {

   
    private Integer monthId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private BigDecimal value;
    private Integer confirm;
    private Integer studentId;
    private String schoolStagesName;
    private Integer schoolStagesId;
    private String studentName;
    private String governorateName;
    private String stateName;
    private String studentMobile;

    public StudentMonthsDTO() {
    }

    public StudentMonthsDTO(Integer id) {
        this.id = id;
    }

   

    public Integer getMonthId() {
        return monthId;
    }

    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
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
        if (!(object instanceof StudentMonthsDTO)) {
            return false;
        }
        StudentMonthsDTO other = (StudentMonthsDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.StudentMonths[ id=" + id + " ]";
    }


    /**
     * @return the schoolStagesName
     */
    public String getSchoolStagesName() {
        return schoolStagesName;
    }

    /**
     * @param schoolStagesName the schoolStagesName to set
     */
    public void setSchoolStagesName(String schoolStagesName) {
        this.schoolStagesName = schoolStagesName;
    }

    /**
     * @return the studentName
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * @param studentName the studentName to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * @return the governorateName
     */
    public String getGovernorateName() {
        return governorateName;
    }

    /**
     * @param governorateName the governorateName to set
     */
    public void setGovernorateName(String governorateName) {
        this.governorateName = governorateName;
    }

    /**
     * @return the stateName
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * @param stateName the stateName to set
     */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

   

    /**
     * @return the studentId
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the studentMobile
     */
    public String getStudentMobile() {
        return studentMobile;
    }

    /**
     * @param studentMobile the studentMobile to set
     */
    public void setStudentMobile(String studentMobile) {
        this.studentMobile = studentMobile;
    }

    /**
     * @return the schoolStagesId
     */
    public Integer getSchoolStagesId() {
        return schoolStagesId;
    }

    /**
     * @param schoolStagesId the schoolStagesId to set
     */
    public void setSchoolStagesId(Integer schoolStagesId) {
        this.schoolStagesId = schoolStagesId;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class HrEmpQualificationsDTO extends BaseEntityDTO {

    private String schoolUniversity;
    private String percentage;
    private Integer graduationYear;
    private String chosenTopics;
    private Integer employeeId;
    private SymbolDTO educationLevel;

    public HrEmpQualificationsDTO() {
    }

    public String getSchoolUniversity() {
        return schoolUniversity;
    }

    public void setSchoolUniversity(String schoolUniversity) {
        this.schoolUniversity = schoolUniversity;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getChosenTopics() {
        return chosenTopics;
    }

    public void setChosenTopics(String chosenTopics) {
        this.chosenTopics = chosenTopics;
    }

    /**
     * @return the educationLevel
     */
    public SymbolDTO getEducationLevel() {
        return educationLevel;
    }

    /**
     * @param educationLevel the educationLevel to set
     */
    public void setEducationLevel(SymbolDTO educationLevel) {
        this.educationLevel = educationLevel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HrEmpQualificationsDTO)) {
            return false;
        }
        HrEmpQualificationsDTO other = (HrEmpQualificationsDTO) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HR.HrEmpQualifications[ id=" + getId() + " ]";
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the employeeId
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

}

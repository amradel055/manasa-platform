/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
 * @author Ahmed Shaaban
 */
@Entity
@Table(name = "hr_emp_qualifications")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrEmpQualifications.findAll", query = "SELECT h FROM HrEmpQualifications h"),
    @NamedQuery(name = "HrEmpQualifications.findById", query = "SELECT h FROM HrEmpQualifications h WHERE h.id = :id"),
    @NamedQuery(name = "HrEmpQualifications.findBySchoolUniversity", query = "SELECT h FROM HrEmpQualifications h WHERE h.schoolUniversity = :schoolUniversity"),
    @NamedQuery(name = "HrEmpQualifications.findByPercentage", query = "SELECT h FROM HrEmpQualifications h WHERE h.percentage = :percentage"),
    @NamedQuery(name = "HrEmpQualifications.findByGraduationYear", query = "SELECT h FROM HrEmpQualifications h WHERE h.graduationYear = :graduationYear"),
    @NamedQuery(name = "HrEmpQualifications.findByChosenTopics", query = "SELECT h FROM HrEmpQualifications h WHERE h.chosenTopics = :chosenTopics"),
    @NamedQuery(name = "HrEmpQualifications.findByCreationDate", query = "SELECT h FROM HrEmpQualifications h WHERE h.creationDate = :creationDate"),
    @NamedQuery(name = "HrEmpQualifications.findByModificationDate", query = "SELECT h FROM HrEmpQualifications h WHERE h.modificationDate = :modificationDate")})
public class HrEmpQualifications extends BaseEntity{

    @Size(max = 45)
    @Column(name = "school_university")
    private String schoolUniversity;
    @Size(max = 45)
    @Column(name = "percentage")
    private String percentage;
    @Column(name = "graduation_year")
    private Integer graduationYear;
    @Size(max = 200)
    @Column(name = "chosen_topics")
    private String chosenTopics;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employeeId;
    @JoinColumn(name = "education_level", referencedColumnName = "id")
    @ManyToOne
    private Symbol educationLevel;

    public HrEmpQualifications() {
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
    public Symbol getEducationLevel() {
        return educationLevel;
    }

    /**
     * @param educationLevel the educationLevel to set
     */
    public void setEducationLevel(Symbol educationLevel) {
        this.educationLevel = educationLevel;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof HrEmpQualifications)) {
            return false;
        }
        HrEmpQualifications other = (HrEmpQualifications) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HR.HrEmpQualifications[ id=" + id + " ]";
    }

}

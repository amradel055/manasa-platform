/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeDTO extends BaseEntityDTO {

    private String name;
    private String nameEn;
    private String email;
    private Integer code;
    private Integer idNumber;
    private Integer socialInsuranceId;
    private Integer status;
    private DepartmentDTO departmentId;
    private String mobile;
    private String phone;
    private String passportNumber;
    private String imgName;
    private String cvName;
    private Date passportReleaseDate;
    private Date passportEndDate;
    private Date retirementDate;
    private Date contractEndDate;
    private Date contractStartDate;
    private Date idNumberEndDate;
    private Date idNumberReleaseDate;
    private Date joinDate;
    private Date dateBirth;
    private EmployeeDTO parentId;
    private SymbolDTO religion;
    private SymbolDTO jobGrade;
    private SymbolDTO placeBirth;
    private SymbolDTO nationality;
    private SymbolDTO maritalStatus;
    private SymbolDTO designation;
    private SymbolDTO gender;
    private List<Integer> empList;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Integer idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getSocialInsuranceId() {
        return socialInsuranceId;
    }

    public void setSocialInsuranceId(Integer socialInsuranceId) {
        this.socialInsuranceId = socialInsuranceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getCvName() {
        return cvName;
    }

    public void setCvName(String cvName) {
        this.cvName = cvName;
    }

    public EmployeeDTO getParentId() {
        return parentId;
    }

    public void setParentId(EmployeeDTO parentId) {
        this.parentId = parentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex() != null ? getIndex().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeDTO)) {
            return false;
        }
        EmployeeDTO other = (EmployeeDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + code + "";
    }

    /**
     * @return the religion
     */
    public SymbolDTO getReligion() {
        return religion;
    }

    /**
     * @param religion the religion to set
     */
    public void setReligion(SymbolDTO religion) {
        this.religion = religion;
    }

    /**
     * @return the placeBirth
     */
    public SymbolDTO getPlaceBirth() {
        return placeBirth;
    }

    /**
     * @param placeBirth the placeBirth to set
     */
    public void setPlaceBirth(SymbolDTO placeBirth) {
        this.placeBirth = placeBirth;
    }

    /**
     * @return the nationality
     */
    public SymbolDTO getNationality() {
        return nationality;
    }

    /**
     * @param nationality the nationality to set
     */
    public void setNationality(SymbolDTO nationality) {
        this.nationality = nationality;
    }

    /**
     * @return the maritalStatus
     */
    public SymbolDTO getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @param maritalStatus the maritalStatus to set
     */
    public void setMaritalStatus(SymbolDTO maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * @return the designation
     */
    public SymbolDTO getDesignation() {
        return designation;
    }

    /**
     * @param designation the designation to set
     */
    public void setDesignation(SymbolDTO designation) {
        this.designation = designation;
    }

    /**
     * @return the gender
     */
    public SymbolDTO getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(SymbolDTO gender) {
        this.gender = gender;
    }

    /**
     * @return the passportReleaseDate
     */
    public Date getPassportReleaseDate() {
        return passportReleaseDate;
    }

    /**
     * @param passportReleaseDate the passportReleaseDate to set
     */
    public void setPassportReleaseDate(Date passportReleaseDate) {
        this.passportReleaseDate = passportReleaseDate;
    }

    /**
     * @return the passportEndDate
     */
    public Date getPassportEndDate() {
        return passportEndDate;
    }

    /**
     * @param passportEndDate the passportEndDate to set
     */
    public void setPassportEndDate(Date passportEndDate) {
        this.passportEndDate = passportEndDate;
    }

    /**
     * @return the retirementDate
     */
    public Date getRetirementDate() {
        return retirementDate;
    }

    /**
     * @param retirementDate the retirementDate to set
     */
    public void setRetirementDate(Date retirementDate) {
        this.retirementDate = retirementDate;
    }

    /**
     * @return the contractEndDate
     */
    public Date getContractEndDate() {
        return contractEndDate;
    }

    /**
     * @param contractEndDate the contractEndDate to set
     */
    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    /**
     * @return the contractStartDate
     */
    public Date getContractStartDate() {
        return contractStartDate;
    }

    /**
     * @param contractStartDate the contractStartDate to set
     */
    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    /**
     * @return the idNumberEndDate
     */
    public Date getIdNumberEndDate() {
        return idNumberEndDate;
    }

    /**
     * @param idNumberEndDate the idNumberEndDate to set
     */
    public void setIdNumberEndDate(Date idNumberEndDate) {
        this.idNumberEndDate = idNumberEndDate;
    }

    /**
     * @return the idNumberReleaseDate
     */
    public Date getIdNumberReleaseDate() {
        return idNumberReleaseDate;
    }

    /**
     * @param idNumberReleaseDate the idNumberReleaseDate to set
     */
    public void setIdNumberReleaseDate(Date idNumberReleaseDate) {
        this.idNumberReleaseDate = idNumberReleaseDate;
    }

    /**
     * @return the joinDate
     */
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * @param joinDate the joinDate to set
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * @return the dateBirth
     */
    public Date getDateBirth() {
        return dateBirth;
    }

    /**
     * @param dateBirth the dateBirth to set
     */
    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    /**
     * @return the jobGrade
     */
    public SymbolDTO getJobGrade() {
        return jobGrade;
    }

    /**
     * @param jobGrade the jobGrade to set
     */
    public void setJobGrade(SymbolDTO jobGrade) {
        this.jobGrade = jobGrade;
    }

    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(DepartmentDTO departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return the departmentId
     */
    public DepartmentDTO getDepartmentId() {
        return departmentId;
    }

    /**
     * @return the empList
     */
    public List<Integer> getEmpList() {
        return empList;
    }

    /**
     * @param empList the empList to set
     */
    public void setEmpList(List<Integer> empList) {
        this.empList = empList;
    }

}

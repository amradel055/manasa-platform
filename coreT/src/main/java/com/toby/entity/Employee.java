/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Ahmed Shaaban
 */
@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {

    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "date_birth")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateBirth;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 45)
    @Column(name = "email")
    private String email;
    @Column(name = "join_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinDate;
    @Column(name = "code")
    private Integer code;
    @Column(name = "id_number")
    private Integer idNumber;
    @Column(name = "id_number_release_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idNumberReleaseDate;
    @Column(name = "id_number_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idNumberEndDate;
    @Column(name = "social_insurance_id")
    private Integer socialInsuranceId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "contract_start_date")
    @Temporal(TemporalType.DATE)
    private Date contractStartDate;
    @Column(name = "contract_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractEndDate;
    @Column(name = "retirement_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date retirementDate;
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    @ManyToOne
    private Department departmentId;
    @Size(max = 45)
    @Column(name = "mobile")
    private String mobile;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 45)
    @Column(name = "phone")
    private String phone;
    @Size(max = 45)
    @Column(name = "passport_number")
    private String passportNumber;
    @Column(name = "passport_release_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passportReleaseDate;
    @Column(name = "passport_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passportEndDate;
    @Size(max = 45)
    @Column(name = "img_name")
    private String imgName;
    @Size(max = 45)
    @Column(name = "cv_name")
    private String cvName;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "designation", referencedColumnName = "id")
    @ManyToOne
    private Symbol designation;
    @JoinColumn(name = "marital_status", referencedColumnName = "id")
    @ManyToOne
    private Symbol maritalStatus;
    @JoinColumn(name = "nationality", referencedColumnName = "id")
    @ManyToOne
    private Symbol nationality;
    @JoinColumn(name = "place_birth", referencedColumnName = "id")
    @ManyToOne
    private Symbol placeBirth;
    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
    private List<Employee> childEmployee;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private Employee parentId;
    @JoinColumn(name = "religion", referencedColumnName = "id")
    @ManyToOne
    private Symbol religion;
    @JoinColumn(name = "gender", referencedColumnName = "id")
    @ManyToOne
    private Symbol gender;
    @JoinColumn(name = "job_grade", referencedColumnName = "id")
    @ManyToOne
    private Symbol jobGrade;


    public Employee() {
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

    public Symbol getGender() {
        return gender;
    }

    public void setGender(Symbol gender) {
        this.gender = gender;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
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

    public Date getIdNumberReleaseDate() {
        return idNumberReleaseDate;
    }

    public void setIdNumberReleaseDate(Date idNumberReleaseDate) {
        this.idNumberReleaseDate = idNumberReleaseDate;
    }

    public Date getIdNumberEndDate() {
        return idNumberEndDate;
    }

    public void setIdNumberEndDate(Date idNumberEndDate) {
        this.idNumberEndDate = idNumberEndDate;
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

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Date getRetirementDate() {
        return retirementDate;
    }

    public void setRetirementDate(Date retirementDate) {
        this.retirementDate = retirementDate;
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

    public Date getPassportReleaseDate() {
        return passportReleaseDate;
    }

    public void setPassportReleaseDate(Date passportReleaseDate) {
        this.passportReleaseDate = passportReleaseDate;
    }

    public Date getPassportEndDate() {
        return passportEndDate;
    }

    public void setPassportEndDate(Date passportEndDate) {
        this.passportEndDate = passportEndDate;
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

   

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }



    public Symbol getDesignation() {
        return designation;
    }

    public void setDesignation(Symbol designation) {
        this.designation = designation;
    }

    public Symbol getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Symbol maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

  

    public Symbol getNationality() {
        return nationality;
    }

    public void setNationality(Symbol nationality) {
        this.nationality = nationality;
    }

    public Symbol getPlaceBirth() {
        return placeBirth;
    }

    public void setPlaceBirth(Symbol placeBirth) {
        this.placeBirth = placeBirth;
    }

  

    public Employee getParentId() {
        return parentId;
    }

    public void setParentId(Employee parentId) {
        this.parentId = parentId;
    }

    public Symbol getReligion() {
        return religion;
    }

    public void setReligion(Symbol religion) {
        this.religion = religion;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return   name + " "+code;
    }

    /**
     * @return the jobGrade
     */
    public Symbol getJobGrade() {
        return jobGrade;
    }

    /**
     * @param jobGrade the jobGrade to set
     */
    public void setJobGrade(Symbol jobGrade) {
        this.jobGrade = jobGrade;
    }

    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(Department departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return the departmentId
     */
    public Department getDepartmentId() {
        return departmentId;
    }

    /**
     * @return the childEmployee
     */
    public List<Employee> getChildEmployee() {
        return childEmployee;
    }

    /**
     * @param childEmployee the childEmployee to set
     */
    public void setChildEmployee(List<Employee> childEmployee) {
        this.childEmployee = childEmployee;
    }

    
    
}

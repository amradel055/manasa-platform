/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ahmed Shaaban
 */
@Entity
@Table(name = "employee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id"),
    @NamedQuery(name = "Employee.findByName", query = "SELECT e FROM Employee e WHERE e.name = :name"),
    @NamedQuery(name = "Employee.findByNameEn", query = "SELECT e FROM Employee e WHERE e.nameEn = :nameEn"),
    @NamedQuery(name = "Employee.findByDateBirth", query = "SELECT e FROM Employee e WHERE e.dateBirth = :dateBirth"),
    @NamedQuery(name = "Employee.findByEmail", query = "SELECT e FROM Employee e WHERE e.email = :email"),
    @NamedQuery(name = "Employee.findByJoinDate", query = "SELECT e FROM Employee e WHERE e.joinDate = :joinDate"),
    @NamedQuery(name = "Employee.findByCode", query = "SELECT e FROM Employee e WHERE e.code = :code"),
    @NamedQuery(name = "Employee.findByIdNumber", query = "SELECT e FROM Employee e WHERE e.idNumber = :idNumber"),
    @NamedQuery(name = "Employee.findByIdNumberReleaseDate", query = "SELECT e FROM Employee e WHERE e.idNumberReleaseDate = :idNumberReleaseDate"),
    @NamedQuery(name = "Employee.findByIdNumberEndDate", query = "SELECT e FROM Employee e WHERE e.idNumberEndDate = :idNumberEndDate"),
    @NamedQuery(name = "Employee.findBySocialInsuranceId", query = "SELECT e FROM Employee e WHERE e.socialInsuranceId = :socialInsuranceId"),
    @NamedQuery(name = "Employee.findByStatus", query = "SELECT e FROM Employee e WHERE e.status = :status"),
    @NamedQuery(name = "Employee.findByContractStartDate", query = "SELECT e FROM Employee e WHERE e.contractStartDate = :contractStartDate"),
    @NamedQuery(name = "Employee.findByContractEndDate", query = "SELECT e FROM Employee e WHERE e.contractEndDate = :contractEndDate"),
    @NamedQuery(name = "Employee.findByRetirementDate", query = "SELECT e FROM Employee e WHERE e.retirementDate = :retirementDate"),
    @NamedQuery(name = "Employee.findByDepartmentId", query = "SELECT e FROM Employee e WHERE e.departmentId = :departmentId"),
    @NamedQuery(name = "Employee.findByMobile", query = "SELECT e FROM Employee e WHERE e.mobile = :mobile"),
    @NamedQuery(name = "Employee.findByPhone", query = "SELECT e FROM Employee e WHERE e.phone = :phone"),
    @NamedQuery(name = "Employee.findByPassportNumber", query = "SELECT e FROM Employee e WHERE e.passportNumber = :passportNumber"),
    @NamedQuery(name = "Employee.findByPassportReleaseDate", query = "SELECT e FROM Employee e WHERE e.passportReleaseDate = :passportReleaseDate"),
    @NamedQuery(name = "Employee.findByPassportEndDate", query = "SELECT e FROM Employee e WHERE e.passportEndDate = :passportEndDate"),
    @NamedQuery(name = "Employee.findByImgName", query = "SELECT e FROM Employee e WHERE e.imgName = :imgName"),
    @NamedQuery(name = "Employee.findByCvName", query = "SELECT e FROM Employee e WHERE e.cvName = :cvName"),
    @NamedQuery(name = "Employee.findByModificationDate", query = "SELECT e FROM Employee e WHERE e.modificationDate = :modificationDate"),
    @NamedQuery(name = "Employee.findByCreationDate", query = "SELECT e FROM Employee e WHERE e.creationDate = :creationDate")})
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "gender")
    private String gender;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 60)
    @Column(name = "current_address")
    private String currentAddress;
    @Size(max = 60)
    @Column(name = "permenant_address")
    private String permenantAddress;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser createdBy;
    @JoinColumn(name = "sponsor_id", referencedColumnName = "id")
    @ManyToOne
    private HrSponsor sponsorId;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser modifiedBy;
    @OneToMany(mappedBy = "employeeId")
    private Collection<IgmaUser> isagUserCollection;
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
    @OneToMany(mappedBy = "parentId", fetch = FetchType.EAGER)
    private List<Employee> childEmployee;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private Employee parentId;
    @JoinColumn(name = "religion", referencedColumnName = "id")
    @ManyToOne
    private Symbol religion;
   
    @JoinColumn(name = "job_grade", referencedColumnName = "id")
    @ManyToOne
    private Symbol jobGrade;
    @Size(max = 45)
    @Column(name = "user_name")
    private String userName;
    @Size(max = 45)
    @Column(name = "password")
    private String password;
    @Column(name = "rem_type")
    private Integer remType;


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

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the remType
     */
    public Integer getRemType() {
        return remType;
    }

    /**
     * @param remType the remType to set
     */
    public void setRemType(Integer remType) {
        this.remType = remType;
    }

    public Employee(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

   

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermenantAddress() {
        return permenantAddress;
    }

    public void setPermenantAddress(String permenantAddress) {
        this.permenantAddress = permenantAddress;
    }

    public IgmaCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(IgmaCompany companyId) {
        this.companyId = companyId;
    }

    public IgmaUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IgmaUser createdBy) {
        this.createdBy = createdBy;
    }

    public HrSponsor getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(HrSponsor sponsorId) {
        this.sponsorId = sponsorId;
    }

    public IgmaUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(IgmaUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @XmlTransient
    public Collection<IgmaUser> getIsagUserCollection() {
        return isagUserCollection;
    }

    public void setIsagUserCollection(Collection<IgmaUser> isagUserCollection) {
        this.isagUserCollection = isagUserCollection;
    }
}

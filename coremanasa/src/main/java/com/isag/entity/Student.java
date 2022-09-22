/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "student")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s")
    , @NamedQuery(name = "Student.findById", query = "SELECT s FROM Student s WHERE s.id = :id")
    , @NamedQuery(name = "Student.findByFirstName", query = "SELECT s FROM Student s WHERE s.fullName = :fullName")
    , @NamedQuery(name = "Student.findByUserName", query = "SELECT s FROM Student s WHERE s.userName = :userName")
    , @NamedQuery(name = "Student.findByPassword", query = "SELECT s FROM Student s WHERE s.password = :password")
    , @NamedQuery(name = "Student.findByCode", query = "SELECT s FROM Student s WHERE s.code = :code")
    , @NamedQuery(name = "Student.findByCreationDate", query = "SELECT s FROM Student s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "Student.findByModificationDate", query = "SELECT s FROM Student s WHERE s.modificationDate = :modificationDate")
    , @NamedQuery(name = "Student.findByImagePath", query = "SELECT s FROM Student s WHERE s.imagePath = :imagePath")
    , @NamedQuery(name = "Student.findByEmail", query = "SELECT s FROM Student s WHERE s.email = :email")})
public class Student extends BaseEntity {

    @Column(name = "type_login")
    private Integer typeLogin;
    @Size(max = 45)
    @Column(name = "full_name")
    private String fullName;
    @Size(max = 45)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 45)
    @Column(name = "father_name")
    private String fatherName;
    @Size(max = 45)
    @Column(name = "grand_father_name")
    private String grandFatherName;
    @Size(max = 45)
    @Column(name = "family_name")
    private String familyName;
    @Size(max = 45)
    @Column(name = "user_name")
    private String userName;
    @Size(max = 45)
    @Column(name = "password")
    private String password;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Size(max = 70)
    @Column(name = "image_path")
    private String imagePath;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    @ManyToOne()
    private States stateId;
    @Column(name = "gender")
    private Integer gender;
    @Size(max = 45)
    @Column(name = "mobile")
    private String mobile;
    @Size(max = 45)
    @Column(name = "father_mobile")
    private String fatherMobile;
    @Size(max = 450)
    @Column(name = "mac_id")
    private String macId;
    @Size(max = 450)
    @Column(name = "ip_adress")
    private String ipAddress;
    @Size(max = 100)
    @Column(name = "father_job")
    private String fatherJob;
    @Column(name = "image")
    private String image;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    @JoinColumn(name = "school_stages_id", referencedColumnName = "id")
    @ManyToOne
    private SchoolStagesGuide schoolStagesId;
    @OneToMany(mappedBy = "studentId")
    private Collection<ExamDegreeRegisterDetail> examDegreeRegisterDetailCollection;
    @OneToMany(mappedBy = "studentId")
    private Collection<StudentStudyGroup> studentStudyGroupCollection;

    public Student() {
    }

    public Student(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SchoolStagesGuide getSchoolStagesId() {
        return schoolStagesId;
    }

    public void setSchoolStagesId(SchoolStagesGuide schoolStagesId) {
        this.schoolStagesId = schoolStagesId;
    }

    @XmlTransient
    public Collection<ExamDegreeRegisterDetail> getExamDegreeRegisterDetailCollection() {
        return examDegreeRegisterDetailCollection;
    }

    public void setExamDegreeRegisterDetailCollection(Collection<ExamDegreeRegisterDetail> examDegreeRegisterDetailCollection) {
        this.examDegreeRegisterDetailCollection = examDegreeRegisterDetailCollection;
    }

    @XmlTransient
    public Collection<StudentStudyGroup> getStudentStudyGroupCollection() {
        return studentStudyGroupCollection;
    }

    public void setStudentStudyGroupCollection(Collection<StudentStudyGroup> studentStudyGroupCollection) {
        this.studentStudyGroupCollection = studentStudyGroupCollection;
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
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Student[ id=" + id + " ]";
    }

    public Object getChildStudent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object getRemCompany() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the fatherMobile
     */
    public String getFatherMobile() {
        return fatherMobile;
    }

    /**
     * @param fatherMobile the fatherMobile to set
     */
    public void setFatherMobile(String fatherMobile) {
        this.fatherMobile = fatherMobile;
    }

    /**
     * @return the gender
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Integer gender) {
        this.gender = gender;
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
     * @return the stateId
     */
    public States getStateId() {
        return stateId;
    }

    /**
     * @param stateId the stateId to set
     */
    public void setStateId(States stateId) {
        this.stateId = stateId;
    }

    /**
     * @return the macId
     */
    public String getMacId() {
        return macId;
    }

    /**
     * @param macId the macId to set
     */
    public void setMacId(String macId) {
        this.macId = macId;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the typeLogin
     */
    public Integer getTypeLogin() {
        return typeLogin;
    }

    /**
     * @param typeLogin the typeLogin to set
     */
    public void setTypeLogin(Integer typeLogin) {
        this.typeLogin = typeLogin;
    }

    /**
     * @return the fatherJob
     */
    public String getFatherJob() {
        return fatherJob;
    }

    /**
     * @param fatherJob the fatherJob to set
     */
    public void setFatherJob(String fatherJob) {
        this.fatherJob = fatherJob;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the fatherName
     */
    public String getFatherName() {
        return fatherName;
    }

    /**
     * @param fatherName the fatherName to set
     */
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    /**
     * @return the grandFatherName
     */
    public String getGrandFatherName() {
        return grandFatherName;
    }

    /**
     * @param grandFatherName the grandFatherName to set
     */
    public void setGrandFatherName(String grandFatherName) {
        this.grandFatherName = grandFatherName;
    }

    /**
     * @return the familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * @param familyName the familyName to set
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

}

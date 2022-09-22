/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.entity.*;

/**
 *
 * @author ahmed
 */
public class StudentDTO extends BaseEntityDTO {

    private String fullName;
    private String image;
    private String userName;
    private String password;
    private String code;
    private String imagePath;
    private String email;
    private Integer gender;
    private StatesDTO stateId;
    private String mobile;
    private String fatherMobile;
    private SchoolStagesGuideDTO schoolStagesId;
    private Integer typeLogin;
    private String macId;
    private String ipAddress;
    private String fatherJob;
    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String familyName;

    public StudentDTO() {
    }

    public StudentDTO(Integer id) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentDTO)) {
            return false;
        }
        StudentDTO other = (StudentDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Student[ id=" + id + " ]";
    }

    public String getFullName() {
        return firstName + " " + fatherName + " " + grandFatherName + " " + familyName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
     * @return the schoolStagesId
     */
    public SchoolStagesGuideDTO getSchoolStagesId() {
        return schoolStagesId;
    }

    /**
     * @param schoolStagesId the schoolStagesId to set
     */
    public void setSchoolStagesId(SchoolStagesGuideDTO schoolStagesId) {
        this.schoolStagesId = schoolStagesId;
    }

    /**
     * @param stateId the stateId to set
     */
    public void setStateId(StatesDTO stateId) {
        this.stateId = stateId;
    }

    /**
     * @return the stateId
     */
    public StatesDTO getStateId() {
        return stateId;
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

    /**
     * @return the Number
     */
}

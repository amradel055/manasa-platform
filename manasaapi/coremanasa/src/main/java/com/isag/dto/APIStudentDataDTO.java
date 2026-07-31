/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.views.TeachersByStudentId;
import java.util.List;

/**
 *
 * @author ahmed
 */
public class APIStudentDataDTO {

    private List<Integer> monthList;
    private List<Integer> periodList;
    private List<TeachersByStudentId> teacherList;
    private List<TeachersDTO> teacherDtoList;

    private Integer id;
    private String fullName;
    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String familyName;
    private String userName;
    private String password;
    private String code;
    private String imagePath;
    private String email;
    private Integer gender;
    private Integer stateId;
    private Integer governorateId;
    private String mobile;
    private String fatherMobile;
    private Integer schoolStagesId;
    private String image;
    private Integer branchId;
    private Integer companyId;
    private Integer typeLogin;
    private String macId;
    private String ipAddress;
    private String msg;
    private String msgType;
    private String schoolStagesName;
    private String stateName;
    private String governorateName;
    private String fatherJob;
    
    private Integer materialId ;
    private Integer periodId ;
    private Integer periodOrderId55 ;
    private String periodName ;
    
    private String macMobileId;

    /**
     * @return the monthList
     */
    public List<Integer> getMonthList() {
        return monthList;
    }

    /**
     * @param monthList the monthList to set
     */
    public void setMonthList(List<Integer> monthList) {
        this.monthList = monthList;
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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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
     * @return the stateId
     */
    public Integer getStateId() {
        return stateId;
    }

    /**
     * @param stateId the stateId to set
     */
    public void setStateId(Integer stateId) {
        this.stateId = stateId;
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
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the msgType
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
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
     * @return the governorateId
     */
    public Integer getGovernorateId() {
        return governorateId;
    }

    /**
     * @param governorateId the governorateId to set
     */
    public void setGovernorateId(Integer governorateId) {
        this.governorateId = governorateId;
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
     * @return the periodList
     */
    public List<Integer> getPeriodList() {
        return periodList;
    }

    /**
     * @param periodList the periodList to set
     */
    public void setPeriodList(List<Integer> periodList) {
        this.periodList = periodList;
    }

    /**
     * @return the teacherList
     */
    public List<TeachersByStudentId> getTeacherList() {
        return teacherList;
    }

    /**
     * @param teacherList the teacherList to set
     */
    public void setTeacherList(List<TeachersByStudentId> teacherList) {
        this.teacherList = teacherList;
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
     * @return the materialId
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    /**
     * @return the periodId
     */
    public Integer getPeriodId() {
        return periodId;
    }

    /**
     * @param periodId the periodId to set
     */
    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    /**
     * @return the periodOrderId55
     */
    public Integer getPeriodOrderId55() {
        return periodOrderId55;
    }

    /**
     * @param periodOrderId55 the periodOrderId55 to set
     */
    public void setPeriodOrderId55(Integer periodOrderId55) {
        this.periodOrderId55 = periodOrderId55;
    }

    /**
     * @return the periodName
     */
    public String getPeriodName() {
        return periodName;
    }

    /**
     * @param periodName the periodName to set
     */
    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    /**
     * @return the macMobileId
     */
    public String getMacMobileId() {
        return macMobileId;
    }

    /**
     * @param macMobileId the macMobileId to set
     */
    public void setMacMobileId(String macMobileId) {
        this.macMobileId = macMobileId;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.List;

/**
 *
 * @author ahmed
 */
public class APIloginDTO {

    private Boolean success;
    private String msg;
    private String token;
    private String downloadUrl;
    private APIDataDTO data;
    private APIStudentDataDTO studentData;
    private Integer teacherId;
    private List<TeachersDTO> teacherList;

    /**
     * @return the success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(Boolean success) {
        this.success = success;
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
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the data
     */
    public APIDataDTO getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(APIDataDTO data) {
        this.data = data;
    }

    /**
     * @return the studentData
     */
    public APIStudentDataDTO getStudentData() {
        return studentData;
    }

    /**
     * @param studentData the studentData to set
     */
    public void setStudentData(APIStudentDataDTO studentData) {
        this.studentData = studentData;
    }

    /**
     * @return the teacherId
     */
    public Integer getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * @return the teacherList
     */
    public List<TeachersDTO> getTeacherList() {
        return teacherList;
    }

    /**
     * @param teacherList the teacherList to set
     */
    public void setTeacherList(List<TeachersDTO> teacherList) {
        this.teacherList = teacherList;
    }

    /**
     * @return the downloadUrl
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * @param downloadUrl the downloadUrl to set
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

}

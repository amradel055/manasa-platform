/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

/**
 *
 * @author m lapan
 */
public class TeachersByStudentId {

    private String teacherName;
    private Integer teacherId;

    public TeachersByStudentId() {
    }

    public TeachersByStudentId(String teacherName, Integer teacherId) {
        this.teacherName = teacherName;
        this.teacherId = teacherId;
    }

    /**
     * @return the teacherName
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * @param teacherName the teacherName to set
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author m lapan
 */
public class MnsLoginExamDTO extends BaseEntityDTO {

    private Integer examId;
    private Integer studentId;
    private Integer timer;

    public MnsLoginExamDTO() {
    }

    public MnsLoginExamDTO(Integer id) {
        this.id = id;
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
        if (!(object instanceof MnsLoginExamDTO)) {
            return false;
        }
        MnsLoginExamDTO other = (MnsLoginExamDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.MnsLoginExam[ id=" + id + " ]";
    }

    /**
     * @return the examId
     */
    public Integer getExamId() {
        return examId;
    }

    /**
     * @param examId the examId to set
     */
    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    /**
     * @return the studentId
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the timer
     */
    public Integer getTimer() {
        return timer;
    }

    /**
     * @param timer the timer to set
     */
    public void setTimer(Integer timer) {
        this.timer = timer;
    }

    
}


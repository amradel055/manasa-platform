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

public class ExamDegreeRegisterDetailDTO extends BaseEntityDTO {

    /**
     * @return the studentId
     */
    public Student getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    
    private Long degree;
    private Integer serial;
    private Student studentId;
    private ExamDegreeRegister examDegreeRegisterId;

    public ExamDegreeRegisterDetailDTO() {
    }

    

    public Long getDegree() {
        return degree;
    }

    public void setDegree(Long degree) {
        this.degree = degree;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }


    public ExamDegreeRegister getExamDegreeRegisterId() {
        return examDegreeRegisterId;
    }

    public void setExamDegreeRegisterId(ExamDegreeRegister examDegreeRegisterId) {
        this.examDegreeRegisterId = examDegreeRegisterId;
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
        if (!(object instanceof ExamDegreeRegisterDetailDTO)) {
            return false;
        }
        ExamDegreeRegisterDetailDTO other = (ExamDegreeRegisterDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.ExamDegreeRegisterDetail[ id=" + id + " ]";
    }
    
}

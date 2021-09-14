/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "exam_degree_register_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExamDegreeRegisterDetail.findAll", query = "SELECT e FROM ExamDegreeRegisterDetail e")
    , @NamedQuery(name = "ExamDegreeRegisterDetail.findById", query = "SELECT e FROM ExamDegreeRegisterDetail e WHERE e.id = :id")
    , @NamedQuery(name = "ExamDegreeRegisterDetail.findByDegree", query = "SELECT e FROM ExamDegreeRegisterDetail e WHERE e.degree = :degree")
    , @NamedQuery(name = "ExamDegreeRegisterDetail.findBySerial", query = "SELECT e FROM ExamDegreeRegisterDetail e WHERE e.serial = :serial")
    , @NamedQuery(name = "ExamDegreeRegisterDetail.findByCreationDate", query = "SELECT e FROM ExamDegreeRegisterDetail e WHERE e.creationDate = :creationDate")
    , @NamedQuery(name = "ExamDegreeRegisterDetail.findByModificationDate", query = "SELECT e FROM ExamDegreeRegisterDetail e WHERE e.modificationDate = :modificationDate")})
public class ExamDegreeRegisterDetail extends BaseEntity {

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

    
    @Column(name = "degree")
    private Long degree;
    @Column(name = "serial")
    private Integer serial;
   
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;
    @JoinColumn(name = "exam_degree_register_id", referencedColumnName = "id")
    @ManyToOne
    private ExamDegreeRegister examDegreeRegisterId;

    public ExamDegreeRegisterDetail() {
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

    

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof ExamDegreeRegisterDetail)) {
            return false;
        }
        ExamDegreeRegisterDetail other = (ExamDegreeRegisterDetail) object;
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

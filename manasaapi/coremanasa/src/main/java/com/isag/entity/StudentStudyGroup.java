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
@Table(name = "student_study_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentStudyGroup.findAll", query = "SELECT s FROM StudentStudyGroup s")
    , @NamedQuery(name = "StudentStudyGroup.findById", query = "SELECT s FROM StudentStudyGroup s WHERE s.id = :id")
    , @NamedQuery(name = "StudentStudyGroup.findBySerial", query = "SELECT s FROM StudentStudyGroup s WHERE s.serial = :serial")
    , @NamedQuery(name = "StudentStudyGroup.findByCreationDate", query = "SELECT s FROM StudentStudyGroup s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "StudentStudyGroup.findByModificationDate", query = "SELECT s FROM StudentStudyGroup s WHERE s.modificationDate = :modificationDate")})
public class StudentStudyGroup extends BaseEntity {

    
    @Column(name = "serial")
    private Integer serial;
   
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
   
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;
    @JoinColumn(name = "study_group_id", referencedColumnName = "id")
    @ManyToOne
    private StudyGroup studyGroupId;

    public StudentStudyGroup() {
    }

    public StudentStudyGroup(Integer id) {
        this.id = id;
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

    

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public StudyGroup getStudyGroupId() {
        return studyGroupId;
    }

    public void setStudyGroupId(StudyGroup studyGroupId) {
        this.studyGroupId = studyGroupId;
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
        if (!(object instanceof StudentStudyGroup)) {
            return false;
        }
        StudentStudyGroup other = (StudentStudyGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.StudentStudyGroup[ id=" + id + " ]";
    }
    
}

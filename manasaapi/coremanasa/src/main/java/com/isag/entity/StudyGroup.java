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
@Table(name = "study_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudyGroup.findAll", query = "SELECT s FROM StudyGroup s")
    , @NamedQuery(name = "StudyGroup.findById", query = "SELECT s FROM StudyGroup s WHERE s.id = :id")
    , @NamedQuery(name = "StudyGroup.findByName", query = "SELECT s FROM StudyGroup s WHERE s.name = :name")
    , @NamedQuery(name = "StudyGroup.findByCode", query = "SELECT s FROM StudyGroup s WHERE s.code = :code")
    , @NamedQuery(name = "StudyGroup.findBySerial", query = "SELECT s FROM StudyGroup s WHERE s.serial = :serial")
    , @NamedQuery(name = "StudyGroup.findByCreationDate", query = "SELECT s FROM StudyGroup s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "StudyGroup.findByModificationDate", query = "SELECT s FROM StudyGroup s WHERE s.modificationDate = :modificationDate")})
public class StudyGroup extends BaseEntity {

    
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Column(name = "serial")
    private Integer serial;
    
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    
    @JoinColumn(name = "school_stages_id", referencedColumnName = "id")
    @ManyToOne
    private SchoolStagesGuide schoolStagesId;
    @OneToMany(mappedBy = "studyGroupId")
    private Collection<ExamDegreeRegister> examDegreeRegisterCollection;
    @OneToMany(mappedBy = "studyGroupId")
    private Collection<StudentStudyGroup> studentStudyGroupCollection;

    public StudyGroup() {
    }

    public StudyGroup(Integer id) {
        this.id = id;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    

    public SchoolStagesGuide getSchoolStagesId() {
        return schoolStagesId;
    }

    public void setSchoolStagesId(SchoolStagesGuide schoolStagesId) {
        this.schoolStagesId = schoolStagesId;
    }

    @XmlTransient
    public Collection<ExamDegreeRegister> getExamDegreeRegisterCollection() {
        return examDegreeRegisterCollection;
    }

    public void setExamDegreeRegisterCollection(Collection<ExamDegreeRegister> examDegreeRegisterCollection) {
        this.examDegreeRegisterCollection = examDegreeRegisterCollection;
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
        if (!(object instanceof StudyGroup)) {
            return false;
        }
        StudyGroup other = (StudyGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.StudyGroup[ id=" + id + " ]";
    }
    
}

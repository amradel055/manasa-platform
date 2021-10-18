/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "exam_degree_register")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExamDegreeRegister.findAll", query = "SELECT e FROM ExamDegreeRegister e")
    , @NamedQuery(name = "ExamDegreeRegister.findById", query = "SELECT e FROM ExamDegreeRegister e WHERE e.id = :id")
    , @NamedQuery(name = "ExamDegreeRegister.findByDegree", query = "SELECT e FROM ExamDegreeRegister e WHERE e.degree = :degree")
    , @NamedQuery(name = "ExamDegreeRegister.findBySerial", query = "SELECT e FROM ExamDegreeRegister e WHERE e.serial = :serial")
    , @NamedQuery(name = "ExamDegreeRegister.findByCreationDate", query = "SELECT e FROM ExamDegreeRegister e WHERE e.creationDate = :creationDate")
    , @NamedQuery(name = "ExamDegreeRegister.findByModificationDate", query = "SELECT e FROM ExamDegreeRegister e WHERE e.modificationDate = :modificationDate")
    , @NamedQuery(name = "ExamDegreeRegister.findByName", query = "SELECT e FROM ExamDegreeRegister e WHERE e.name = :name")
    , @NamedQuery(name = "ExamDegreeRegister.findByCode", query = "SELECT e FROM ExamDegreeRegister e WHERE e.code = :code")
    , @NamedQuery(name = "ExamDegreeRegister.findByDate", query = "SELECT e FROM ExamDegreeRegister e WHERE e.date = :date")})
public class ExamDegreeRegister extends BaseEntity {

  
    @Column(name = "degree")
    private Long degree;
    @Column(name = "serial")
    private Integer serial;
    
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    @ManyToOne
    private ScheduleGuide scheduleId;
    @JoinColumn(name = "study_group_id", referencedColumnName = "id")
    @ManyToOne
    private StudyGroup studyGroupId;
    @OneToMany(mappedBy = "examDegreeRegisterId")
    private Collection<ExamDegreeRegisterDetail> examDegreeRegisterDetailCollection;

    public ExamDegreeRegister() {
    }

    public ExamDegreeRegister(Integer id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

   

    public ScheduleGuide getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(ScheduleGuide scheduleId) {
        this.scheduleId = scheduleId;
    }

    public StudyGroup getStudyGroupId() {
        return studyGroupId;
    }

    public void setStudyGroupId(StudyGroup studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    @XmlTransient
    public Collection<ExamDegreeRegisterDetail> getExamDegreeRegisterDetailCollection() {
        return examDegreeRegisterDetailCollection;
    }

    public void setExamDegreeRegisterDetailCollection(Collection<ExamDegreeRegisterDetail> examDegreeRegisterDetailCollection) {
        this.examDegreeRegisterDetailCollection = examDegreeRegisterDetailCollection;
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
        if (!(object instanceof ExamDegreeRegister)) {
            return false;
        }
        ExamDegreeRegister other = (ExamDegreeRegister) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.ExamDegreeRegister[ id=" + id + " ]";
    }
    
}

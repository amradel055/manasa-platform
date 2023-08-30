/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m lapan
 */
@Entity
@Table(name = "periods")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Periods.findAll", query = "SELECT p FROM Periods p")
    , @NamedQuery(name = "Periods.findById", query = "SELECT p FROM Periods p WHERE p.id = :id")
    , @NamedQuery(name = "Periods.findByName", query = "SELECT p FROM Periods p WHERE p.name = :name")
    , @NamedQuery(name = "Periods.findByCreationDate", query = "SELECT p FROM Periods p WHERE p.creationDate = :creationDate")
    , @NamedQuery(name = "Periods.findByModificationDate", query = "SELECT p FROM Periods p WHERE p.modificationDate = :modificationDate")})
public class Periods extends BaseEntity {

    @Column(name = "name")
    private String name;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser teacherId;

    @JoinColumn(name = "material_id", referencedColumnName = "id")
    @ManyToOne
    private Materials materialId;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private Periods parentPeriod;
    @OneToMany(mappedBy = "parentPeriod", fetch = FetchType.EAGER)
    private List<Periods> childPeriods;

    @Column(name = "question_number")
    private Integer questionNumber;
    
    @Column(name = "auto_time")
    private Integer autoTime;
    
    public Periods() {
    }

    public Periods(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof Periods)) {
            return false;
        }
        Periods other = (Periods) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.Periods[ id=" + id + " ]";
    }

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the teacherId
     */
    public IgmaUser getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(IgmaUser teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * @return the parentPeriod
     */
    public Periods getParentPeriod() {
        return parentPeriod;
    }

    /**
     * @param parentPeriod the parentPeriod to set
     */
    public void setParentPeriod(Periods parentPeriod) {
        this.parentPeriod = parentPeriod;
    }

    /**
     * @return the childPeriods
     */
    public List<Periods> getChildPeriods() {
        return childPeriods;
    }

    /**
     * @param childPeriods the childPeriods to set
     */
    public void setChildPeriods(List<Periods> childPeriods) {
        this.childPeriods = childPeriods;
    }

    /**
     * @return the materialId
     */
    public Materials getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(Materials materialId) {
        this.materialId = materialId;
    }

    /**
     * @return the questionNumber
     */
    public Integer getQuestionNumber() {
        return questionNumber;
    }

    /**
     * @param questionNumber the questionNumber to set
     */
    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    /**
     * @return the autoTime
     */
    public Integer getAutoTime() {
        return autoTime;
    }

    /**
     * @param autoTime the autoTime to set
     */
    public void setAutoTime(Integer autoTime) {
        this.autoTime = autoTime;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
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
 * @author m lapan
 */
@Entity
@Table(name = "student_periods")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentPeriods.findAll", query = "SELECT s FROM StudentPeriods s")
    , @NamedQuery(name = "StudentPeriods.findById", query = "SELECT s FROM StudentPeriods s WHERE s.id = :id")
    , @NamedQuery(name = "StudentPeriods.findByValue", query = "SELECT s FROM StudentPeriods s WHERE s.value = :value")
    , @NamedQuery(name = "StudentPeriods.findByConfirm", query = "SELECT s FROM StudentPeriods s WHERE s.confirm = :confirm")
    , @NamedQuery(name = "StudentPeriods.findByCreationDate", query = "SELECT s FROM StudentPeriods s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "StudentPeriods.findByModificationDate", query = "SELECT s FROM StudentPeriods s WHERE s.modificationDate = :modificationDate")})
public class StudentPeriods extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "confirm")
    private Integer confirm;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "period_id", referencedColumnName = "id")
    @ManyToOne
    private Periods periodId;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser teacherId;

    public StudentPeriods() {
    }

    public StudentPeriods(Integer id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Periods getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Periods periodId) {
        this.periodId = periodId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
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
        if (!(object instanceof StudentPeriods)) {
            return false;
        }
        StudentPeriods other = (StudentPeriods) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.StudentPeriods[ id=" + id + " ]";
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

}

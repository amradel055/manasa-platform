/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m lapan
 */
@Entity
@Table(name = "student_reset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentReset.findAll", query = "SELECT s FROM StudentReset s")
    , @NamedQuery(name = "StudentReset.findById", query = "SELECT s FROM StudentReset s WHERE s.id = :id")
    , @NamedQuery(name = "StudentReset.findByStudentCode", query = "SELECT s FROM StudentReset s WHERE s.studentCode = :studentCode")
    , @NamedQuery(name = "StudentReset.findByDate", query = "SELECT s FROM StudentReset s WHERE s.date = :date")
    , @NamedQuery(name = "StudentReset.findByCreationDate", query = "SELECT s FROM StudentReset s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "StudentReset.findByModificationDate", query = "SELECT s FROM StudentReset s WHERE s.modificationDate = :modificationDate")})
public class StudentReset extends BaseEntity {

    @Column(name = "student_code")
    private String studentCode;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;

    public StudentReset() {
    }

    public StudentReset(Integer id) {
        this.id = id;
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
        if (!(object instanceof StudentReset)) {
            return false;
        }
        StudentReset other = (StudentReset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.StudentReset[ id=" + id + " ]";
    }

    /**
     * @return the studentCode
     */
    public String getStudentCode() {
        return studentCode;
    }

    /**
     * @param studentCode the studentCode to set
     */
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

}

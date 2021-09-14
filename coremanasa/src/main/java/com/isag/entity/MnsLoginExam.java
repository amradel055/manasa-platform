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
import javax.persistence.metamodel.SingularAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m lapan
 */
@Entity
@Table(name = "mns_login_exam")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MnsLoginExam.findAll", query = "SELECT m FROM MnsLoginExam m")
    , @NamedQuery(name = "MnsLoginExam.findById", query = "SELECT m FROM MnsLoginExam m WHERE m.id = :id")
    , @NamedQuery(name = "MnsLoginExam.findByLoginDate", query = "SELECT m FROM MnsLoginExam m WHERE m.loginDate = :loginDate")
    , @NamedQuery(name = "MnsLoginExam.findByCreationDate", query = "SELECT m FROM MnsLoginExam m WHERE m.creationDate = :creationDate")
    , @NamedQuery(name = "MnsLoginExam.findByModificationDate", query = "SELECT m FROM MnsLoginExam m WHERE m.modificationDate = :modificationDate")})
public class MnsLoginExam extends BaseEntity {

   
    @Column(name = "login_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date loginDate;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "exam_id", referencedColumnName = "id")
    @ManyToOne
    private Exams examId;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;

    public MnsLoginExam() {
    }

    public MnsLoginExam(Integer id) {
        this.id = id;
    }

   

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

   

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

  

    public Exams getExamId() {
        return examId;
    }

    public void setExamId(Exams examId) {
        this.examId = examId;
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
        if (!(object instanceof MnsLoginExam)) {
            return false;
        }
        MnsLoginExam other = (MnsLoginExam) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.MnsLoginExam[ id=" + id + " ]";
    }

    public void setLoginDate(SingularAttribute<MnsLoginExam, Date> loginDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

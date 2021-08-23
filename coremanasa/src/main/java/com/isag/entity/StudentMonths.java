/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "student_months")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentMonths.findAll", query = "SELECT s FROM StudentMonths s")
    , @NamedQuery(name = "StudentMonths.findById", query = "SELECT s FROM StudentMonths s WHERE s.id = :id")
    , @NamedQuery(name = "StudentMonths.findByMonthId", query = "SELECT s FROM StudentMonths s WHERE s.monthId = :monthId")
    , @NamedQuery(name = "StudentMonths.findByValue", query = "SELECT s FROM StudentMonths s WHERE s.value = :value")
    , @NamedQuery(name = "StudentMonths.findByConfirm", query = "SELECT s FROM StudentMonths s WHERE s.confirm = :confirm")
    , @NamedQuery(name = "StudentMonths.findByCreationDate", query = "SELECT s FROM StudentMonths s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "StudentMonths.findByModificationDate", query = "SELECT s FROM StudentMonths s WHERE s.modificationDate = :modificationDate")})
public class StudentMonths extends BaseEntity {

   
    @Column(name = "month_id")
    private Integer monthId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "confirm")
    private Integer confirm;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentDTO;

    public StudentMonths() {
    }

    public StudentMonths(Integer id) {
        this.id = id;
    }

    

    public Integer getMonthId() {
        return monthId;
    }

    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
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

   
   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentMonths)) {
            return false;
        }
        StudentMonths other = (StudentMonths) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.StudentMonths[ id=" + id + " ]";
    }

    /**
     * @return the studentDTO
     */
    public Student getStudentDTO() {
        return studentDTO;
    }

    /**
     * @param studentDTO the studentDTO to set
     */
    public void setStudentDTO(Student studentDTO) {
        this.studentDTO = studentDTO;
    }

}

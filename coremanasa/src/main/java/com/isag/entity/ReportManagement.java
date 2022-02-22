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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "report_management")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportManagement.findAll", query = "SELECT r FROM ReportManagement r")
    , @NamedQuery(name = "ReportManagement.findById", query = "SELECT r FROM ReportManagement r WHERE r.id = :id")
    , @NamedQuery(name = "ReportManagement.findByLeftUp1", query = "SELECT r FROM ReportManagement r WHERE r.leftUp1 = :leftUp1")
    , @NamedQuery(name = "ReportManagement.findByLeftUp2", query = "SELECT r FROM ReportManagement r WHERE r.leftUp2 = :leftUp2")
    , @NamedQuery(name = "ReportManagement.findByLeftUp3", query = "SELECT r FROM ReportManagement r WHERE r.leftUp3 = :leftUp3")
    , @NamedQuery(name = "ReportManagement.findByRightUp1", query = "SELECT r FROM ReportManagement r WHERE r.rightUp1 = :rightUp1")
    , @NamedQuery(name = "ReportManagement.findByRightUp2", query = "SELECT r FROM ReportManagement r WHERE r.rightUp2 = :rightUp2")
    , @NamedQuery(name = "ReportManagement.findByRightUp3", query = "SELECT r FROM ReportManagement r WHERE r.rightUp3 = :rightUp3")
    , @NamedQuery(name = "ReportManagement.findByRightUp4", query = "SELECT r FROM ReportManagement r WHERE r.rightUp4 = :rightUp4")
    , @NamedQuery(name = "ReportManagement.findByLeftUp4", query = "SELECT r FROM ReportManagement r WHERE r.leftUp4 = :leftUp4")
    , @NamedQuery(name = "ReportManagement.findByModificationDate", query = "SELECT r FROM ReportManagement r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "ReportManagement.findByCreationDate", query = "SELECT r FROM ReportManagement r WHERE r.creationDate = :creationDate")})
public class ReportManagement extends BaseEntity {


    @Size(max = 450)
    @Column(name = "left_up_1")
    private String leftUp1;
    @Size(max = 450)
    @Column(name = "left_up_2")
    private String leftUp2;
    @Size(max = 450)
    @Column(name = "left_up_3")
    private String leftUp3;
    @Size(max = 450)
    @Column(name = "right_up_1")
    private String rightUp1;
    @Size(max = 450)
    @Column(name = "right_up_2")
    private String rightUp2;
    @Size(max = 450)
    @Column(name = "right_up_3")
    private String rightUp3;
    @Size(max = 450)
    @Column(name = "right_up_4")
    private String rightUp4;
    @Size(max = 450)
    @Column(name = "left_up_4")
    private String leftUp4;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public ReportManagement() {
    }

    public String getLeftUp1() {
        return leftUp1;
    }

    public void setLeftUp1(String leftUp1) {
        this.leftUp1 = leftUp1;
    }

    public String getLeftUp2() {
        return leftUp2;
    }

    public void setLeftUp2(String leftUp2) {
        this.leftUp2 = leftUp2;
    }

    public String getLeftUp3() {
        return leftUp3;
    }

    public void setLeftUp3(String leftUp3) {
        this.leftUp3 = leftUp3;
    }

    public String getRightUp1() {
        return rightUp1;
    }

    public void setRightUp1(String rightUp1) {
        this.rightUp1 = rightUp1;
    }

    public String getRightUp2() {
        return rightUp2;
    }

    public void setRightUp2(String rightUp2) {
        this.rightUp2 = rightUp2;
    }

    public String getRightUp3() {
        return rightUp3;
    }

    public void setRightUp3(String rightUp3) {
        this.rightUp3 = rightUp3;
    }

    public String getRightUp4() {
        return rightUp4;
    }

    public void setRightUp4(String rightUp4) {
        this.rightUp4 = rightUp4;
    }

    public String getLeftUp4() {
        return leftUp4;
    }

    public void setLeftUp4(String leftUp4) {
        this.leftUp4 = leftUp4;
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
        if (!(object instanceof ReportManagement)) {
            return false;
        }
        ReportManagement other = (ReportManagement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.en.entity.ReportManagement[ id=" + id + " ]";
    }
    
}

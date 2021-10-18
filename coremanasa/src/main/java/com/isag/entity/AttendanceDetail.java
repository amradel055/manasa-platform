/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
@Table(name = "attendance_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AttendanceDetail.findAll", query = "SELECT a FROM AttendanceDetail a")
    , @NamedQuery(name = "AttendanceDetail.findById", query = "SELECT a FROM AttendanceDetail a WHERE a.id = :id")
    , @NamedQuery(name = "AttendanceDetail.findByCreationDate", query = "SELECT a FROM AttendanceDetail a WHERE a.creationDate = :creationDate")
    , @NamedQuery(name = "AttendanceDetail.findByModificationDate", query = "SELECT a FROM AttendanceDetail a WHERE a.modificationDate = :modificationDate")})
public class AttendanceDetail extends BaseEntity {

    @JoinColumn(name = "attendance_id", referencedColumnName = "id")
    @ManyToOne
    private Attendance attendanceId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public AttendanceDetail() {
    }

    public AttendanceDetail(Integer id) {
        this.id = id;
    }

   
    public Attendance getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Attendance attendanceId) {
        this.attendanceId = attendanceId;
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
        if (!(object instanceof AttendanceDetail)) {
            return false;
        }
        AttendanceDetail other = (AttendanceDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.AttendanceDetail[ id=" + id + " ]";
    }

}

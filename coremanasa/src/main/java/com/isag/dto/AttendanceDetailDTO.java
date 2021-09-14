/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.entity.*;

/**
 *
 * @author ahmed
 */

public class AttendanceDetailDTO extends BaseEntityDTO {

    
    private Attendance attendanceId;

    public AttendanceDetailDTO() {
    }

    public AttendanceDetailDTO(Integer id) {
        this.id = id;
    }

   
    public Attendance getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Attendance attendanceId) {
        this.attendanceId = attendanceId;
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
        if (!(object instanceof AttendanceDetailDTO)) {
            return false;
        }
        AttendanceDetailDTO other = (AttendanceDetailDTO) object;
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

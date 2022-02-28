/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.OtherAppointmentsDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface OtherAppointmentsService {

    public List<OtherAppointmentsDTO> findOtherAppointments(Integer branchId);

    public OtherAppointmentsDTO save(OtherAppointmentsDTO otherAppointmentsDTO, IgmaUser isagUser);

    public List<OtherAppointmentsDTO> findOtherAppointmentsByTeacherAndSchoolStageAndMaterialId(Integer teacherId , Integer schoolStageId ,  
            Integer materialId,Integer type);
    
    public List<OtherAppointmentsDTO> findOtherAppointmentsByStudentId(Integer studentId) ;

    public void deleteOtherAppointments(Integer otherAppointmentsDTO);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.AppointmentsDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface AppointmentsService {

    public List<AppointmentsDTO> findAppointments(Integer branchId);

    public AppointmentsDTO save(AppointmentsDTO appointmentsDTO, IgmaUser isagUser);

    public List<AppointmentsDTO> findAppointmentsByTeacherAndSchoolStage(Integer teacherId, Integer schoolStageId);

    public void deleteAppointments(Integer appointmentsDTO);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.AppointmentsService;
import com.isag.config.UserLoginData;
import com.isag.dto.AppointmentsDTO;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author AhmedEssam <ahmedEssam.com>
 */
@Path("/appointments")
public class AppointmentsRest {

    private UserLoginData userLoginData;
    @EJB
    AppointmentsService appointmentsService;

    @Transactional
    @Path("/allAppointments")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response allAppointments(AppointmentsDTO appointmentsDTO) {
        if (appointmentsDTO != null) {
            try {
                List<AppointmentsDTO> dTOs = appointmentsService.findAppointments(appointmentsDTO.getBranchId());
                GenericEntity<List<AppointmentsDTO>> list = new GenericEntity<List<AppointmentsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            appointmentsDTO = new AppointmentsDTO();
            appointmentsDTO.setMsg("No Id is Sent");
            return Response.ok(appointmentsDTO).build();
        }
    }

    @Transactional
    @Path("/appointmentsByTeacher")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AppointmentsByTeacherId(AppointmentsDTO appointmentsDTO) {
        if (appointmentsDTO != null) {
            try {
                List<AppointmentsDTO> dTOs = appointmentsService.findAppointmentsByTeacherAndSchoolStageAndMaterialId(
                        appointmentsDTO.getTeacherId(),
                        appointmentsDTO.getSchoolStageId(),
                        appointmentsDTO.getMaterialId()
                );
                GenericEntity<List<AppointmentsDTO>> list = new GenericEntity<List<AppointmentsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            appointmentsDTO = new AppointmentsDTO();
            appointmentsDTO.setMsg("No Id is Sent");
            return Response.ok(appointmentsDTO).build();
        }
    }

    
    @Transactional
    @Path("/appointmentsByStudent")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AppointmentsByStudentId(AppointmentsDTO appointmentsDTO) {
        if (appointmentsDTO != null) {
            try {
                List<AppointmentsDTO> dTOs = appointmentsService.findAppointmentsgroup(
                        appointmentsDTO.getStudentId(),appointmentsDTO.getSchoolStageId()
                );
                GenericEntity<List<AppointmentsDTO>> list = new GenericEntity<List<AppointmentsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            appointmentsDTO = new AppointmentsDTO();
            appointmentsDTO.setMsg("No Id is Sent");
            return Response.ok(appointmentsDTO).build();
        }
    }

    
    @Transactional
    @Path("/saveAppointments")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveAppointments(AppointmentsDTO appointmentsDTO) {
        AppointmentsDTO msg;
        try {
            msg = appointmentsService.save(appointmentsDTO, null);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteAppointments")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAppointments(AppointmentsDTO appointmentsDTO) {
        AppointmentsDTO msg = new AppointmentsDTO();
        try {
            appointmentsService.deleteAppointments(appointmentsDTO.getId());
            msg.setMsg("تم الحذف بنجاح");
            msg.setMsgType("success");
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    public UserLoginData getUserLoginData(@Context ContainerRequestContext crc) {
        userLoginData = (UserLoginData) crc.getProperty("userLogInData");
        return userLoginData;
    }

    /**
     * @return the userLoginData
     */
    public UserLoginData getUserLoginData() {
        return userLoginData;
    }

    /**
     * @param userLoginData the userLoginData to set
     */
    public void setUserLoginData(UserLoginData userLoginData) {
        this.userLoginData = userLoginData;
    }

}

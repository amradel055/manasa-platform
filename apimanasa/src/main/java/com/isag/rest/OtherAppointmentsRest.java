/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.AppointmentsService;
import com.isag.businessservice.OtherAppointmentsService;
import com.isag.config.UserLoginData;
import com.isag.dto.AppointmentsDTO;
import com.isag.dto.OtherAppointmentsDTO;
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
@Path("/otherappointments")
public class OtherAppointmentsRest {

    private UserLoginData userLoginData;
    @EJB
    OtherAppointmentsService appointmentsService;

    @Transactional
    @Path("/allAppointments")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response allAppointments(OtherAppointmentsDTO appointmentsDTO) {
        if (appointmentsDTO != null) {
            try {
                List<OtherAppointmentsDTO> dTOs = appointmentsService.findOtherAppointments(appointmentsDTO.getBranchId());
                GenericEntity<List<OtherAppointmentsDTO>> list = new GenericEntity<List<OtherAppointmentsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            appointmentsDTO = new OtherAppointmentsDTO();
            appointmentsDTO.setMsg("No Id is Sent");
            return Response.ok(appointmentsDTO).build();
        }
    }

    @Transactional
    @Path("/appointmentsByTeacher")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AppointmentsByTeacherId(OtherAppointmentsDTO appointmentsDTO) {
        if (appointmentsDTO != null) {
            try {
                List<OtherAppointmentsDTO> dTOs = appointmentsService.findOtherAppointmentsByTeacherAndSchoolStageAndMaterialId(
                        appointmentsDTO.getTeacherId(),
                        appointmentsDTO.getSchoolStageId(),
                        appointmentsDTO.getMaterialId(),
                        appointmentsDTO.getAppointmentsType()
                );
                GenericEntity<List<OtherAppointmentsDTO>> list = new GenericEntity<List<OtherAppointmentsDTO>>(dTOs) {
                };
                return Response.ok(list).build();
            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            appointmentsDTO = new OtherAppointmentsDTO();
            appointmentsDTO.setMsg("No Id is Sent");
            return Response.ok(appointmentsDTO).build();
        }
    }

    
//    @Transactional
//    @Path("/appointmentsByStudent")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response AppointmentsByStudentId(OtherAppointmentsDTO appointmentsDTO) {
//        if (appointmentsDTO != null) {
//            try {
//                List<OtherAppointmentsDTO> dTOs = appointmentsService.findOtherAppointmentsByStudentId(
//                        appointmentsDTO.getStudentId()
//                );
//                GenericEntity<List<OtherAppointmentsDTO>> list = new GenericEntity<List<OtherAppointmentsDTO>>(dTOs) {
//                };
//                return Response.ok(list).build();
//
//            } catch (Exception e) {
//                return Response.status(Response.Status.FORBIDDEN).build();
//            }
//
//        } else {
//            appointmentsDTO = new AppointmentsDTO();
//            appointmentsDTO.setMsg("No Id is Sent");
//            return Response.ok(appointmentsDTO).build();
//        }
//    }

    
    @Transactional
    @Path("/saveOtherAppointments")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveAppointments(OtherAppointmentsDTO appointmentsDTO) {
        OtherAppointmentsDTO msg;
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
        OtherAppointmentsDTO msg = new OtherAppointmentsDTO();
        try {
            appointmentsService.deleteOtherAppointments(appointmentsDTO.getId());
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

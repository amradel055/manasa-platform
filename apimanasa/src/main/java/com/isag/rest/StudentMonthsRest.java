/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.StudentMonthsService;
import com.isag.businessservice.StudentStatusService;
import com.isag.config.UserLoginData;
import com.isag.dto.StudentMonthsDTO;
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
@Path("/studentMonths")
public class StudentMonthsRest {

    private UserLoginData userLoginData;
    @EJB
    StudentMonthsService studentMonthsService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/allStudentMonths")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response allStudentMonths(StudentMonthsDTO studentMonthsDTO) {
        if (studentMonthsDTO != null) {
            try {
                List<StudentMonthsDTO> dTOs = studentMonthsService.findStudentMonths(studentMonthsDTO.getBranchId(), studentMonthsDTO.getId());
                GenericEntity<List<StudentMonthsDTO>> list = new GenericEntity<List<StudentMonthsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            studentMonthsDTO = new StudentMonthsDTO();
            studentMonthsDTO.setMsg("No Id is Sent");
            return Response.ok(studentMonthsDTO).build();
        }
    }

    @Transactional
    @Path("/studentMonthsByTeacher")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response StudentMonthsByTeacherId(StudentMonthsDTO studentMonthsDTO) {
        if (studentMonthsDTO != null) {
            try {
                List<StudentMonthsDTO> dTOs = studentMonthsService.findStudentMonthsByTeacherId(studentMonthsDTO.getTeacherId());
                GenericEntity<List<StudentMonthsDTO>> list = new GenericEntity<List<StudentMonthsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            studentMonthsDTO = new StudentMonthsDTO();
            studentMonthsDTO.setMsg("No Id is Sent");
            return Response.ok(studentMonthsDTO).build();
        }
    }

    @Transactional
    @Path("/saveStudentMonths")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveStudentMonths(StudentMonthsDTO studentMonthsDTO) {
        StudentMonthsDTO msg;
        try {
            msg = studentMonthsService.save(studentMonthsDTO, null);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteStudentMonths")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteStudentMonths(StudentMonthsDTO studentMonthsDTO) {
        StudentMonthsDTO msg = new StudentMonthsDTO();
        try {
            studentMonthsService.deleteStudentMonths(studentMonthsDTO.getId());
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

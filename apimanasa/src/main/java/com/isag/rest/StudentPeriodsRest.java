/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.StudentPeriodsService;
import com.isag.businessservice.StudentService;
import com.isag.businessservice.StudentStatusService;
import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.StudentPeriodsDTO;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author AhmedEssam <ahmedEssam.com>
 */
@Path("/studentPeriods")
public class StudentPeriodsRest {

    @EJB
    StudentPeriodsService studentPeriodsService;
    @EJB
    StudentService studentService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/allStudentPeriods")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response allStudentPeriods(StudentPeriodsDTO studentPeriodsDTO) {
        if (studentPeriodsDTO.getTeacherId() != null) {
            try {
                List<StudentPeriodsDTO> dTOs = studentPeriodsService.findStudentPeriods(studentPeriodsDTO);
                GenericEntity<List<StudentPeriodsDTO>> list = new GenericEntity<List<StudentPeriodsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            studentPeriodsDTO = new StudentPeriodsDTO();
            studentPeriodsDTO.setMsg("No Id is Sent");
            return Response.ok(studentPeriodsDTO).build();
        }
    }

    @Transactional
    @Path("/saveStudentPeriods")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveStudentPeriods(StudentPeriodsDTO studentPeriodsDTO) {
        StudentPeriodsDTO msg;
        try {
            msg = studentPeriodsService.save(studentPeriodsDTO, null);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteStudentPeriods")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteStudentPeriods(StudentPeriodsDTO studentPeriodsDTO) {
        StudentPeriodsDTO msg = new StudentPeriodsDTO();
        try {
            studentPeriodsService.deleteStudentPeriods1(studentPeriodsDTO.getId());
            msg.setMsg("تم الحذف بنجاح");
            msg.setMsgType("success");
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteStudentPeriodsBy")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteStudentPeriodsBy(StudentPeriodsDTO studentPeriodsDTO) {
        StudentPeriodsDTO msg = new StudentPeriodsDTO();
        if (studentPeriodsDTO.getId() != null && studentPeriodsDTO.getTeacherId() != null && studentPeriodsDTO.getPeriodId() != null && studentPeriodsDTO.getStudentId() != null) {
            try {
                studentPeriodsService.deleteStudentPeriodsBy(studentPeriodsDTO.getId() , studentPeriodsDTO.getTeacherId(), studentPeriodsDTO.getPeriodId(), studentPeriodsDTO.getStudentId());
                msg.setMsg("تم الحذف بنجاح");
                msg.setMsgType("success");
                return Response.ok(msg).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            msg.setMsg("لم يتم الحذف");
            msg.setMsgType("No id is sent");
            return Response.ok(msg).build();
        }
    }

    @Transactional
    @Path("/resetPassword")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ResetPassword(APIStudentDataDTO aPIStudentDataDTO) {
        APIStudentDataDTO msg;
        try {
            msg = studentService.ResetPassword(aPIStudentDataDTO);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.LessonService;
import com.isag.config.UserLoginData;
import com.isag.dto.LessonDTO;
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
@Path("/lesson")
public class LessonRest {

    private UserLoginData userLoginData;
    @EJB
    LessonService lessonService;

    @Transactional
    @Path("/lessonList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findLessonList(LessonDTO lessonId) {
        if (lessonId != null) {
            try {
                List<LessonDTO> dTOs = lessonService.findAllLessonsByTeacher(lessonId.getBranchId(), lessonId.getCompanyId(),
                        lessonId.getTeacherId(), lessonId.getMaterialId(), lessonId.getId());
                GenericEntity<List<LessonDTO>> list = new GenericEntity<List<LessonDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            lessonId = new LessonDTO();
            lessonId.setMsg("No Id is Sent");
            return Response.ok(lessonId).build();
        }
    }

    @Transactional
    @Path("/lessonregister")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response lessonRegister(LessonDTO lessonDTO) {
        LessonDTO msg;
        try {
            msg = lessonService.save(lessonDTO, null);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteLesson")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteLesson(LessonDTO lessonDTO) {
        LessonDTO msg = new LessonDTO();
        try {
            lessonService.deleteLesson(lessonDTO.getId());
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

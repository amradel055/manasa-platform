/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.LessonLocationService;
import com.isag.businessservice.StudentStatusService;
import com.isag.config.UserLoginData;
import com.isag.dto.LessonLocationDTO;
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
@Path("/lessonlocation")
public class LessonLocationRest {

    private UserLoginData userLoginData;
    @EJB
    LessonLocationService lessonLocationService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/lessonlocationList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findLessonList(LessonLocationDTO locationDTO) {
        try {
            List<LessonLocationDTO> dTOs = lessonLocationService.findAllLessonLocation();
            GenericEntity<List<LessonLocationDTO>> list = new GenericEntity<List<LessonLocationDTO>>(dTOs) {
            };
            return Response.ok(list).build();

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

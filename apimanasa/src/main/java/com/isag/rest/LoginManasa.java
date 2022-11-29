/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.api.dto.TeacherDataAPI;
import com.isag.businessservice.IgmaUserService;
import com.isag.dto.APIloginDTO;
import com.isag.entity.IgmaUser;
import com.isag.general.GeneralService;
import com.isag.root.LoginRoot;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author AHMEDESSAM
 */
@Path("/authenticationManasa")
public class LoginManasa {

    @EJB
    private GeneralService generalService;
    @EJB
    IgmaUserService igmaUserService;

    @Transactional
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRoot loginRoot) {
        try {
            // Issue a token for the user
            APIloginDTO token = generalService.authenticateManasa(loginRoot);
            // Return the token on the response
            return Response.ok().entity(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    @Transactional
    @Path("/updatePassword")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePassword(LoginRoot loginRoot) {
        try {
            // Issue a token for the user
            String token = generalService.changePasswordManasa(loginRoot);
            // Return the token on the response
            APIloginDTO  dTO= new APIloginDTO();
            dTO.setMsg(token);
            return Response.ok().entity(dTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
     @Transactional
    @Path("/findTeachersNew")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TeacherDataAPI> findTeachersNew(LoginRoot igmaUser) {
        List<TeacherDataAPI> teacherDataAPI = new ArrayList<>();
        if (igmaUser.branchId != null) {
            teacherDataAPI = igmaUserService.getTeacherDataAPI(igmaUser.branchId);
        }
        return teacherDataAPI;
    }
}



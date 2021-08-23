/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.APIloginDTO;
import com.isag.general.GeneralService;
import com.isag.root.LoginRoot;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.isag.businessservice.IgmaUserLoginService;

/**
 *
 * @author AHMEDESSAM
 */
@Path("/authenticationStudent")
public class LoginStudentRest {

    @EJB
    private GeneralService generalService;
    @EJB
    private IgmaUserLoginService isagUserLoginService;

    @Transactional
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRoot loginRoot) {
        try {
            // Issue a token for the user
            APIloginDTO token = generalService.authenticateStudent(loginRoot);
            // Return the token on the response
            return Response.ok().entity(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    @Transactional
    @Path("/logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(APIStudentDataDTO aPIStudentDataDTO) {
        try {
            APIStudentDataDTO msg = new APIStudentDataDTO();
            // Issue a token for the user
            isagUserLoginService.saveUserData(aPIStudentDataDTO.getBranchId(), aPIStudentDataDTO.getCompanyId(), aPIStudentDataDTO.getMacId(), aPIStudentDataDTO.getId(),0);
            msg.setMsg("تم الخروج بنجاح");
            msg.setMsgType("success");
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}

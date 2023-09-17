/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.igma.manasasecurity.entities.Security;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.igma.manasasecurity.services.LoginDataService;
import javax.ejb.EJB;

/**
 *
 * @author AhmedEssam <ahmedEssam.com>
 */
@Path("/logindata")
public class LoginData {
    
    @EJB
    private  LoginDataService loginService ;
    

    @Transactional
    @Path("/finddata")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findExamsForAdmin(Security security) {
        if (security != null) {
            try {
                security = loginService.findData(security.getName() , security.getVersion());
                GenericEntity<Security> list = new GenericEntity<Security>(security) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            return Response.ok(null).build();
        }
    }

}

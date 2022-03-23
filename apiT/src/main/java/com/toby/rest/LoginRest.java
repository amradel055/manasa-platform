package com.toby.rest;

import com.toby.dto.ApiLoginDTO;
import com.toby.general.GeneralService;
import com.toby.root.LoginRoot;
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
 * @author Lenovo
 */
@Path("/authentication")
public class LoginRest {

    @EJB
    GeneralService generalService;

    @Transactional
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRoot loginRoot) {
        try {
            ApiLoginDTO apiLoginDTO = generalService.authAdmin(loginRoot);
            return Response.ok().entity(apiLoginDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
     @Transactional
    @Path("/loginCustomer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginCustomer(LoginRoot loginRoot) {
        try {
            ApiLoginDTO apiLoginDTO = generalService.authCustomer(loginRoot);
            return Response.ok().entity(apiLoginDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}

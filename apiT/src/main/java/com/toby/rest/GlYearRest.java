package com.toby.rest;

import com.toby.API.GlYearDTOAPI;
import com.toby.businessservice.TobyUserYearService;
import com.toby.config.UserLoginData;
import com.toby.dto.GlYearDTO;
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
//import sun.misc.IOUtils;

/**
 *
 * @author Lenovo
 */
@Path("/glyear")
public class GlYearRest {

    @EJB
    TobyUserYearService tobyUserYearService;

    @Transactional
    @Path("/findGlYearForUserByBranch")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<GlYearDTOAPI> findGlYearForUserByBranch(UserLoginData user) {
        try {
            if (user.getUserId() != null && user.getBranchId() != null) {
                List<GlYearDTOAPI> listYear = tobyUserYearService.findYearDTOByUserIdAPI(user.getUserId(), user.getBranchId());

                return listYear;
            } else {
                GlYearDTO year = new GlYearDTO();
                year.setMsg("branch or User id not found");
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Path("/findGlYearForUserByBranchOld")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findGlYearForUserByBranchOld(UserLoginData user) {
        try {
            if (user.getUserId() != null && user.getBranchId() != null) {
                List<GlYearDTO> listYear = tobyUserYearService.findYearDTOByUserId(user.getUserId(), user.getBranchId());
                GenericEntity<List<GlYearDTO>> list = new GenericEntity<List<GlYearDTO>>(listYear) {
                };
                return Response.ok(list).build();
            } else {
                GlYearDTO year = new GlYearDTO();
                year.setMsg("branch or User id not found");
                return Response.ok().entity(year).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

package com.toby.rest;

import com.toby.businessservice.TobyUserYearService;
import com.toby.dto.GlYearDTO;
import com.toby.dto.OffersDTO;
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
    public Response findGlYearForUserByBranch(Integer userId, Integer branchId) {
        try {
            if (userId != null && branchId != null) {
                List<GlYearDTO> listYear = tobyUserYearService.findYearDTOByUserId(userId, branchId);
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

package com.toby.rest;

import com.toby.API.GlAccountDTOAPI;
import com.toby.businessservice.GlAccountServiceDTO;
import com.toby.config.UserLoginData;
import com.toby.dto.GlAccountDTO;
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
@Path("/glaccount")
public class GlAccountRest {

    @EJB
    GlAccountServiceDTO glAccountServiceDTO;

    @Transactional
    @Path("/glaccountsactive")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<GlAccountDTOAPI> findBranchGLAccountsActive(UserLoginData user) {
        try {
            if (user.getBranchId() != null) {
                List<GlAccountDTOAPI> listDocument = glAccountServiceDTO.getBranchGLAccountsActiveAPI(user.getBranchId());
                return listDocument;
            } else {

                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Path("/glaccountsactiveOld")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findBranchGLAccountsActiveOld(UserLoginData user) {
        try {
            if (user.getBranchId() != null) {
                List<GlAccountDTO> listDocument = glAccountServiceDTO.getBranchGLAccountsActive(user.getBranchId());
                GenericEntity<List<GlAccountDTO>> list = new GenericEntity<List<GlAccountDTO>>(listDocument) {
                };
                return Response.ok(list).build();
            } else {
                GlAccountDTO dTO = new GlAccountDTO();
                dTO.setMsg("branch id not found");
                return Response.ok().entity(dTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

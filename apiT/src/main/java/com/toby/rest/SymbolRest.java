package com.toby.rest;

import com.toby.businessservice.SymbolService;
import com.toby.config.UserLoginData;
import com.toby.dto.SymbolDTO;
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
@Path("/symbol")
public class SymbolRest {

    @EJB
    SymbolService symbolService;

    @Transactional
    @Path("/documentstypes")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findDocumentsTypesDTO(UserLoginData user) {
        try {
            if (user.getCompanyId() != null) {
                List<SymbolDTO> listDocument = symbolService.getDocumentsTypesDTO(user.getCompanyId());
                GenericEntity<List<SymbolDTO>> list = new GenericEntity<List<SymbolDTO>>(listDocument) {
                };
                return Response.ok(list).build();
            } else {
                SymbolDTO dTO = new SymbolDTO();
                dTO.setMsg("company id not found");
                return Response.ok().entity(dTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

package com.toby.rest;

import com.toby.businessservice.CostCenterServiceDTO;
import com.toby.businessservice.SymbolService;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.CostCenter;
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
@Path("/costcenter")
public class CostCenterRest {

    @EJB
    CostCenterServiceDTO costCenterServiceDTO;

    @Transactional
    @Path("/costcenterlist")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findCostCenter(Integer branchId) {
        try { 
            if (branchId != null) {
                List<CostCenterDTO> listCostCenter = costCenterServiceDTO.getAllSubCostCenterByBranchIdActive(branchId);
                GenericEntity<List<CostCenterDTO>> list = new GenericEntity<List<CostCenterDTO>>(listCostCenter) {
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

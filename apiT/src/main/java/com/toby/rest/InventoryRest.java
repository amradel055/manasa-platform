package com.toby.rest;

import com.toby.businessservice.InvInventoryService;
import com.toby.dto.InvInventoryDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//import sun.misc.IOUtils;

/**
 *
 * @author Lenovo
 */
@Path("/inventory")
public class InventoryRest {

    @EJB
    InvInventoryService invInventoryService;

   

    @Transactional
    @Path("/findInventoryList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvInventoryDTO> FindfindInventoryList(InvInventoryDTO invInventoryDTO) {
        List<InvInventoryDTO> inventoryDTOList = new ArrayList<>();
        if (invInventoryDTO.getBranchId() != null) {
            inventoryDTOList = invInventoryService.findInventoryByBranchIdDTO(invInventoryDTO.getBranchId(),null);
        }
        return inventoryDTOList;
    }
}

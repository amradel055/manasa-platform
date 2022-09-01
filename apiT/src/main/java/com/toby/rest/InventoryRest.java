package com.toby.rest;

import com.toby.businessservice.InvInventoryService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.entity.Branch;
import com.toby.entity.TobyUser;
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
    TobyUserInventoryService tobyUserInventoryService;
    @EJB
    AutoCompleteService autoCompleteService;
    @EJB
    InvInventoryService invInventoryService;

    @Transactional
    @Path("/findInventoryList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvInventoryDTO> FindfindInventoryList(TobyUserDTO tobyUserDTO) {
        List<InvInventoryDTO> inventoryDTOList = new ArrayList<>();
        if (tobyUserDTO.getId() != null && tobyUserDTO.getBranchId() != null) {
            TobyUser tobyUser = new TobyUser();
            tobyUser.setId(tobyUserDTO.getId());
            Branch branch = new Branch();
            branch.setId(tobyUserDTO.getBranchId());
            tobyUser.setId(tobyUserDTO.getId());
            tobyUser.setBranchId(branch);
            inventoryDTOList = tobyUserInventoryService.getAllGallaryByUserAndBranch(tobyUser);
        }
        return inventoryDTOList;
    }

    // المخازن الخاصة بالمستخدم
    @Transactional
    @Path("/findAllInventroisByUser")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvInventoryDTO> FindAllInventroisByUser(TobyUserDTO tobyUserDTO) {
        List<InvInventoryDTO> inventoryDTOList = new ArrayList<>();
        if (tobyUserDTO.getId() != null && tobyUserDTO.getBranchId() != null) {
            TobyUser tobyUser = new TobyUser();
            tobyUser.setId(tobyUserDTO.getId());
            Branch branch = new Branch();
            branch.setId(tobyUserDTO.getBranchId());
            tobyUser.setId(tobyUserDTO.getId());
            tobyUser.setBranchId(branch);
            inventoryDTOList = tobyUserInventoryService.getAllInventroisByUserAndBranchPerDTO(tobyUserDTO.getId(), tobyUserDTO.getBranchId());
        }
        return inventoryDTOList;
    }

    // المعارض الخاصة للمستخدم
    @Transactional
    @Path("/findAllGallaryByUser")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvInventoryDTO> FindAllGallaryByUser(TobyUserDTO tobyUserDTO) {
        List<InvInventoryDTO> inventoryDTOList = new ArrayList<>();
        if (tobyUserDTO.getId() != null && tobyUserDTO.getBranchId() != null) {
            TobyUser tobyUser = new TobyUser();
            tobyUser.setId(tobyUserDTO.getId());
            Branch branch = new Branch();
            branch.setId(tobyUserDTO.getBranchId());
            tobyUser.setId(tobyUserDTO.getId());
            tobyUser.setBranchId(branch);
            inventoryDTOList = tobyUserInventoryService.getAllGallaryByUserAndBranch(tobyUser);
        }
        return inventoryDTOList;
    }

    // أماكن الاستلام
    @Transactional
    @Path("/findInventoryByBranch")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvInventoryDTO> findInventoryByBranch(TobyUserDTO tobyUserDTO) {
        List<InvInventoryDTO> inventoryDTOList = new ArrayList<>();
        if (tobyUserDTO.getId() != null && tobyUserDTO.getBranchId() != null) {
            inventoryDTOList = invInventoryService.findInventoryByBranch(tobyUserDTO.getBranchId(), null);
        }
        return inventoryDTOList;
    }
}

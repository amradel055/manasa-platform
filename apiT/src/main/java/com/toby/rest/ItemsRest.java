/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.businessservice.InvItemService;
import com.toby.dto.InvItemDTO;
import com.toby.entity.InvItem;
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

@Path("/items")
public class ItemsRest {

    @EJB
    InvItemService invItemService;

    @Transactional
    @Path("/groupItems")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findInvItemsByBranchAndGroup(InvItemDTO invItemDTO) {
        try {
            if (invItemDTO.getBranchId() != null && invItemDTO.getGroupId() != null) {
                List<InvItemDTO> invItemDTOList = invItemService.getInvItemByBranchIdAndGroupIdDTO(invItemDTO.getBranchId(), invItemDTO.getGroupId().getId());
                GenericEntity<List<InvItemDTO>> list = new GenericEntity<List<InvItemDTO>>(invItemDTOList) {
                };
                return Response.ok(list).build();
            } else {
                invItemDTO.setMsg("يجب اختيار فرع ");
                return Response.ok(invItemDTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/updateItem")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateItem(InvItem invItem) {
        try {
            if (invItem.getId() != null) {
                invItem = invItemService.updateInvItemWebSite(invItem);
                return Response.ok().entity("تم الحفظ بنجاح").build();
            } else {
                 return Response.ok().entity("يجب اختيار منتج ").build();
            }
           
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    public InvItemDTO valitade(InvItemDTO invItemDTO) {
        if (invItemDTO.getBranchId() == null) {
            invItemDTO.setMsg("يجب اختيار فرع");
        }

        return invItemDTO;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.apiServices.AttributeDetailsService;
import com.toby.apiServices.AttributeService;
import com.toby.dto.AttributeDTO;
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

/**
 *
 * @author Lenovo
 */
@Path("/attributes")
public class AttributeRest {

    @EJB
    AttributeService attributeService;
    
      @EJB
      AttributeDetailsService attributeDetailsService;

    @Transactional
    @Path("/addAttribute")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAttributehead(AttributeDTO attribute) {
        try {
            attribute = valitade(attribute);
            if (attribute.getMsg() == null) {
                attribute = attributeService.addAtrribute(attribute);
            }
            return Response.ok(attribute).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/updateAttribute")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAttributehead(AttributeDTO attribute) {
        try {

            if (attribute.getId() != null) {
                attribute = valitade(attribute);
                if (attribute.getMsg() == null) {
                    attribute = attributeService.updateAttribute(attribute);
                }
            } else {
                attribute.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(attribute).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteAttribute")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAttributehead(AttributeDTO attribute) {
        try {
            if (attribute.getId() != null) {
                attribute = valitade(attribute);
                if(attribute.getMsg() == null){
                 attributeDetailsService.DeleteAttributeDetailsForHeadByBranchId(attribute);
                 attributeService.deleteAttribute(attribute);
                }
                
            } else {
                attribute.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(attribute).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/allAttributes")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findAttributeheadByBranch(AttributeDTO attributeDTO){
        try {
            if (attributeDTO.getBranchId() != null && attributeDTO.getCompanyId() != null) {
                List<AttributeDTO> attributesList = attributeService.getAllAttributesByCompanyIdAndBranchId(attributeDTO);
                GenericEntity<List<AttributeDTO>> list = new GenericEntity<List<AttributeDTO>>(attributesList) {
                };
                return Response.ok(list).build();
            } else {
                AttributeDTO attribute = new AttributeDTO();
                attribute.setMsg("branch or company id not found");
                return Response.ok().entity(attribute).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    public AttributeDTO valitade(AttributeDTO dTO) {

        if (dTO.getName() == null || dTO.getName().isEmpty() ){
            dTO.setMsg("يجب ادخال الاسم");
        }
        if (dTO.getCreatedBy() == null || dTO.getCompanyId() == null || dTO.getBranchId() == null) {
            dTO.setMsg("يجب تسجيل الدخول ");
        }
        return dTO;
    }
}

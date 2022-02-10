/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.apiServices.VariationProductService;
import com.toby.dto.AttributeDTO;
import com.toby.dto.VariationDTO;
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
@Path("/variations")
public class VariationsRest {

    @EJB
    VariationProductService variationProductService;

    @Transactional
    @Path("/addVariations")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAttributehead(VariationDTO variationDTO) {
        try {
            variationDTO = valitade(variationDTO);
            if (variationDTO.getMsg() == null) {
                variationDTO = variationProductService.addVariation(variationDTO);
            }
            return Response.ok(variationDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/updateVariations")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAttributehead(VariationDTO variationDTO) {
        try {

            if (variationDTO.getId() != null) {
                variationDTO = valitade(variationDTO);
                if (variationDTO.getMsg() == null) {
                    variationDTO = variationProductService.updateVariation(variationDTO);
                }
            } else {
                variationDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(variationDTO).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteVariations")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAttributehead(VariationDTO variationDTO) {
        try {
            if (variationDTO.getId() != null) {
                variationDTO = variationProductService.deleteVariation(variationDTO);
            } else {
                variationDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(variationDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/allVariations")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findAttributeheadByBranch(VariationDTO variationDTO) {
        try {
            if (variationDTO.getBranchId() != null && variationDTO.getCompanyId() != null) {
                List<VariationDTO> variationlist = variationProductService.getAllVariationByCompanyIdAndBranchId(variationDTO);
                GenericEntity<List<VariationDTO>> list = new GenericEntity<List<VariationDTO>>(variationlist) {
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

    public VariationDTO valitade(VariationDTO dTO) {

        if (dTO.getItemId() == null) {
            dTO.setMsg("يجب ادخال الصنف");
        }

        if (dTO.getCreatedBy() == null || dTO.getCompanyId() == null || dTO.getBranchId() == null) {
            dTO.setMsg("يجب تسجيل الدخول ");
        }
        return dTO;
    }
}

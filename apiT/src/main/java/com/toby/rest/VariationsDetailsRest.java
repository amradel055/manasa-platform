/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.apiServices.VariationProductDetailsService;
import com.toby.dto.AttributeDTO;
import com.toby.dto.VariationDetailsDTO;
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
@Path("/variationsDetails")
public class VariationsDetailsRest {

    @EJB
    VariationProductDetailsService variationProductDetailsService;

    @Transactional
    @Path("/addVariationsDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAttributehead(VariationDetailsDTO variationDetailsDTO) {
        try {
            variationDetailsDTO = valitade(variationDetailsDTO);
            if (variationDetailsDTO.getMsg() == null) {
                variationDetailsDTO = variationProductDetailsService.addVariationDetails(variationDetailsDTO);
            }
            return Response.ok(variationDetailsDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/updateVariationsDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAttributehead(VariationDetailsDTO variationDetailsDTO) {
        try {

            if (variationDetailsDTO.getId() != null) {
                variationDetailsDTO = valitade(variationDetailsDTO);
                if (variationDetailsDTO.getMsg() == null) {
                    variationDetailsDTO = variationProductDetailsService.updateVariationDetails(variationDetailsDTO);
                }
            } else {
                variationDetailsDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(variationDetailsDTO).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteVariationsDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAttributehead(VariationDetailsDTO variationDetailsDTO) {
        try {
            if (variationDetailsDTO.getId() != null) {
              variationDetailsDTO = variationProductDetailsService.deleteVariationDetails(variationDetailsDTO);
            } else {
                variationDetailsDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(variationDetailsDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/allVariationsDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findAttributeheadByBranch(VariationDetailsDTO variationDetailsDTO) {
        try {
            if (variationDetailsDTO.getBranchId() != null && variationDetailsDTO.getCompanyId() != null) {
                List<VariationDetailsDTO> variationlist = variationProductDetailsService.getAllVariationDetailsByCompanyIdAndBranchId(variationDetailsDTO);
                GenericEntity<List<VariationDetailsDTO>> list = new GenericEntity<List<VariationDetailsDTO>>(variationlist) {
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

    public VariationDetailsDTO valitade(VariationDetailsDTO dTO) {

        if (dTO.getAttributeDetailsId()== null) {
            dTO.setMsg("يجب ادخال مواصفات الاساسيه المنتج ");
        }
        if (dTO.getVariationId() == null) {
            dTO.setMsg("يجب ادخال مواصفات المنتج ");
        }

        if (dTO.getCreatedBy() == null || dTO.getCompanyId() == null || dTO.getBranchId() == null) {
            dTO.setMsg("يجب تسجيل الدخول ");
        }
        return dTO;
    }
}

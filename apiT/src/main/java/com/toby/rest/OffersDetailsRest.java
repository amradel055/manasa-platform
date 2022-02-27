/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.apiServices.OffersDatailsService;
import com.toby.dto.AttributeDTO;
import com.toby.dto.OffersDetailsDTO;
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
@Path("/offersDetails")
public class OffersDetailsRest {

    @EJB
    OffersDatailsService offersDetailsService;

    @Transactional
    @Path("/addOfferDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAttributehead(OffersDetailsDTO offersDetailsDTO) {
        try {
            offersDetailsDTO = valitade(offersDetailsDTO);
            if (offersDetailsDTO.getMsg() == null) {
                offersDetailsDTO = offersDetailsService.addOffersDetails(offersDetailsDTO);
            }
            return Response.ok(offersDetailsDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/updateOfferDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAttributehead(OffersDetailsDTO offersDetailsDTO) {
        try {

            if (offersDetailsDTO.getId() != null) {
                offersDetailsDTO = valitade(offersDetailsDTO);
                if (offersDetailsDTO.getMsg() == null) {
                    offersDetailsDTO = offersDetailsService.updateOffersDetails(offersDetailsDTO);
                }
            } else {
                offersDetailsDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(offersDetailsDTO).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteOfferDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAttributehead(OffersDetailsDTO offersDetailsDTO) {
        try {
            if (offersDetailsDTO.getId() != null) {
                offersDetailsService.deleteOffersDetails(offersDetailsDTO);
            } else {
                offersDetailsDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(offersDetailsDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/allOffersDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findAttributeheadByBranch(OffersDetailsDTO offersDetailsDTO) {
        try {
            if (offersDetailsDTO.getBranchId() != null && offersDetailsDTO.getCompanyId() != null) {
                List<OffersDetailsDTO> offersDTOsList = offersDetailsService.getAllOffersDetailsByCompanyIdAndBranchId(offersDetailsDTO);
                GenericEntity<List<OffersDetailsDTO>> list = new GenericEntity<List<OffersDetailsDTO>>(offersDTOsList) {
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

    public OffersDetailsDTO valitade(OffersDetailsDTO dTO) {

        if (dTO.getItemId() == null) {
            dTO.setMsg("يجب اخيار صنف ");
        }
        if (dTO.getCategoryId() == null) {
            dTO.setMsg("يجب اختياار مجموعة");
        }
        if (dTO.getStatus() == null) {
            dTO.setMsg("يجب ادخال حالة الصنف");
        }
        if (dTO.getCreatedBy() == null || dTO.getCompanyId() == null || dTO.getBranchId() == null) {
            dTO.setMsg("يجب تسجيل الدخول ");
        }
        return dTO;
    }
}

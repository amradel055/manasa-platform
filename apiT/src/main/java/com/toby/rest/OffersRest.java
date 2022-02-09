/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.apiServices.AttributeService;
import com.toby.apiServices.OffersService;
import com.toby.dto.AttributeDTO;
import com.toby.dto.OffersDTO;
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
@Path("/offers")
public class OffersRest {

    @EJB
    OffersService offersService;

    @Transactional
    @Path("/addOffer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAttributehead(OffersDTO offersDTO) {
        try {
            offersDTO = valitade(offersDTO);
            if (offersDTO.getMsg() == null) {
                offersDTO = offersService.addOffers(offersDTO);
            }
            return Response.ok(offersDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/updateOffer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAttributehead(OffersDTO offersDTO) {
        try {

            if (offersDTO.getId() != null) {
                offersDTO = valitade(offersDTO);
                if (offersDTO.getMsg() == null) {
                    offersDTO = offersService.updateOffers(offersDTO);
                }
            } else {
                offersDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(offersDTO).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteOffer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAttributehead(OffersDTO offersDTO) {
        try {
            if (offersDTO.getId() != null) {
                offersService.deleteOffers(offersDTO);
            } else {
                offersDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(offersDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/allOffers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findAttributeheadByBranch(OffersDTO offersDTO) {
        try {
            if (offersDTO.getBranchId() != null && offersDTO.getCompanyId() != null) {
                List<OffersDTO> offersDTOsList = offersService.getAllOffersByCompanyIdAndBranchId(offersDTO);
                GenericEntity<List<OffersDTO>> list = new GenericEntity<List<OffersDTO>>(offersDTOsList) {
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

    public OffersDTO valitade(OffersDTO dTO) {

        if (dTO.getName() == null || dTO.getName().isEmpty()) {
            dTO.setMsg("يجب ادخال الاسم");
        }
        if (dTO.getValue() == null) {
            dTO.setMsg("يجب ادخال قيمة العرض");
        }
        if (dTO.getType() == null) {
            dTO.setMsg("يجب ادخال نوع العرض");
        }

        if (dTO.getCreatedBy() == null || dTO.getCompanyId() == null || dTO.getBranchId() == null) {
            dTO.setMsg("يجب تسجيل الدخول ");
        }
        return dTO;
    }
}

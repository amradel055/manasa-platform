/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.apiServices.AttributeDetailsService;
import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.dto.CrmEventDTO;
import com.toby.entity.TobyUser;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Lenovo
 */
@Path("/crmEvent")
public class CrmEventRest {
    @EJB
    CRMEventServiceDTO cRMEventServiceDTO;

    @Transactional
    @Path("/addCrm")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AddCrmEvent(CrmEventDTO crmEventDTO) {
        try {
            if (crmEventDTO != null) {
                TobyUser tobyUser = new TobyUser();
                tobyUser.setId(crmEventDTO.getCreatedBy());
                crmEventDTO = cRMEventServiceDTO.save(crmEventDTO, tobyUser);
            }
            return Response.ok(crmEventDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteCrmEvent")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteEvent(CrmEventDTO crmEventDTO) {
        try {

            if (crmEventDTO.getId() != null) {
                TobyUser tobyUser = new TobyUser();
                tobyUser.setId(crmEventDTO.getCreatedBy());
                cRMEventServiceDTO.deleteCrmEventDTO(crmEventDTO.getId(), tobyUser);

            }

            return Response.ok().entity(cRMEventServiceDTO).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

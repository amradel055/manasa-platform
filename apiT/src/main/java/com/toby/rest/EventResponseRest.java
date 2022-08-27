/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.businessservice.EventResponseService;
import com.toby.dto.EventResponseDTO;
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
import javax.ws.rs.core.Response;

/**
 *
 * @author Lenovo
 */
@Path("/EventResponse")
public class EventResponseRest {

    @EJB
    EventResponseService eventResponseService;

    @Transactional
    @Path("/addEventResponse")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response AddEventResponse(EventResponseDTO eventResponseDTO) {
        try {
            if (eventResponseDTO != null) {
                TobyUser tobyUser = new TobyUser();
                tobyUser.setId(eventResponseDTO.getCreatedBy());
                eventResponseService.save(eventResponseDTO, tobyUser);
            }
            return Response.ok(eventResponseDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteEventResponse")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteEventResponse(EventResponseDTO eventResponseDTO) {
        try {

            if (eventResponseDTO.getId() != null) {
                eventResponseService.delete(eventResponseDTO.getId());

            }

            return Response.ok().entity(eventResponseDTO).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/findEventResponse")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<EventResponseDTO> FindEventResponseByEventId(EventResponseDTO eventResponseDTO) {
        List<EventResponseDTO> eventResponseDTOList = new ArrayList<>();
        if (eventResponseDTO.getId() != null) {
            eventResponseDTOList = eventResponseService.returnEventResponseDTOByEventId(eventResponseDTO.getEventId());
        }
        return eventResponseDTOList;
    }

}

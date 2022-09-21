/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.CRMEventSearchBean;
import com.toby.API.CrmEventDTOAPI;
import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.businessservice.EventResponseService;
import com.toby.dto.CrmEventDTO;
import com.toby.dto.EventResponseDTO;
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
import javax.ws.rs.core.Response;

/**
 *
 * @author Lenovo
 */
@Path("/crmEvent")
public class CrmEventRest {

    @EJB
    CRMEventServiceDTO cRMEventServiceDTO;

    @EJB
    EventResponseService eventResponseService;

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
                Branch branch = new Branch();
                branch.setId(crmEventDTO.getBranchId());
                tobyUser.setBranchId(branch);
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
    public Response deleteEvent(CRMEventSearchBean crmEventDTO) {
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

    @Transactional
    @Path("/FindCrmEvent")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<CrmEventDTOAPI> FindCrmEvent(CRMEventSearchBean crmEventDTO) {
        List<CrmEventDTOAPI> crmEventDTOList = new ArrayList<>();
        if (crmEventDTO != null) {
            crmEventDTOList = cRMEventServiceDTO.findCrmEventListAPI(crmEventDTO.getPriorityId(), crmEventDTO.getGallaryId(), crmEventDTO.getCustomerCode(), crmEventDTO.getCreatedBy(), crmEventDTO.getAssignedTo(), crmEventDTO.getDateFrom(), crmEventDTO.getDateTo(), crmEventDTO.getStatus(), crmEventDTO.getBranchId());
        }
        return crmEventDTOList;
    }

    @Transactional
    @Path("/FindResponseCrm")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<EventResponseDTO> FindResponseCrm(CRMEventSearchBean crmEventDTO) {
        List<EventResponseDTO> crmEventDTOList = new ArrayList<>();
        if (crmEventDTO.getId() != null) {
            crmEventDTOList = eventResponseService.returnEventResponseDTOByEventId(crmEventDTO.getId());
        }
        return crmEventDTOList;
    }

    @Transactional
    @Path("/FindCrmEventCount")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CRMEventSearchBean FindCrmEventCount(CRMEventSearchBean crmEventDTO) {
        Integer y = null;
        if (crmEventDTO != null) {
            y = cRMEventServiceDTO.findCountCrmEvent(crmEventDTO.getPriorityId(), crmEventDTO.getGallaryId(), crmEventDTO.getCustomerCode(), crmEventDTO.getCreatedBy(), crmEventDTO.getAssignedTo(), crmEventDTO.getDateFrom(), crmEventDTO.getDateTo(), crmEventDTO.getStatus(), crmEventDTO.getBranchId());
        }
        CRMEventSearchBean c = new CRMEventSearchBean();
        c.setNumber(y);
        return c;
    }

    @Transactional
    @Path("/FindALLCrmEvent")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<CrmEventDTO> FindALLCrmEvent(CRMEventSearchBean crmEventDTO) {
        List<CrmEventDTO> crmEventDTOList = new ArrayList<>();
        if (crmEventDTO != null) {
            crmEventDTOList = cRMEventServiceDTO.findALLEventList(crmEventDTO.getBranchId());
        }
        return crmEventDTOList;
    }

    @Transactional
    @Path("/FindALLCrmEventCount")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CrmEventDTO FindALLCrmEventCount(CRMEventSearchBean crmEventDTO) {

        Integer y = null;
        if (crmEventDTO.getBranchId() != null) {
            y = cRMEventServiceDTO.findALLCountEventList(crmEventDTO.getBranchId());
        }
        CrmEventDTO c = new CrmEventDTO();
        c.setNumberOfEvent(y);
        return c;
    }

}

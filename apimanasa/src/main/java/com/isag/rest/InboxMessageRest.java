/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.InboxMessageService;
import com.isag.dto.InboxMessagesDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.InboxMessages;
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
 * @author AhmedEssam <ahmedEssam.com>
 */
@Path("/inboxmessage")
public class InboxMessageRest {

    @EJB
    InboxMessageService inboxMessageService;

    @Transactional
    @Path("/findmessages")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InboxMessagesDTO> findMessages(InboxMessagesDTO inboxMessagesDTO) {
        return inboxMessageService.find(inboxMessagesDTO.getStudentId(), inboxMessagesDTO.getTeacherId(), inboxMessagesDTO.getSchoolId(), inboxMessagesDTO.getMaterialId());
    }

    @Transactional
    @Path("/findmessage")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InboxMessagesDTO findMessage(InboxMessagesDTO inboxMessagesDTO) {
        return inboxMessageService.findById(inboxMessagesDTO.getId());
    }

    @Transactional
    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response lessonRegister(InboxMessagesDTO dto) {
        try {
            IgmaUser igmaUser = new IgmaUser();
            Branch branchId = new Branch(dto.getBranchId());
            igmaUser.setBranchId(branchId);
            IgmaCompany companyId = new IgmaCompany(dto.getCompanyId());
            igmaUser.setCompanyId(companyId);
            igmaUser.setId(dto.getTeacherId());
            dto = inboxMessageService.save(dto , igmaUser);
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("deletequestion")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteQuestion(InboxMessages dto) {
        try {
            inboxMessageService.delete(dto.getId());
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

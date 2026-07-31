/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.InboxReplyService;
import com.isag.businessservice.StudentStatusService;
import com.isag.dto.InboxReplyDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.InboxReply;
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
@Path("/inboxreply")
public class InboxReplyRest {

    
    @EJB
    InboxReplyService inboxReplyService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/findreplys")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InboxReplyDTO> findReplys(InboxReply inboxReply) {
        return inboxReplyService.find(inboxReply.getMessage());
    }

    @Transactional
    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(InboxReplyDTO dto) {
        try {
             IgmaUser igmaUser = new IgmaUser();
            Branch branchId = new Branch(dto.getBranchId());
            igmaUser.setBranchId(branchId);
            IgmaCompany companyId = new IgmaCompany(dto.getCompanyId());
            igmaUser.setCompanyId(companyId);
            igmaUser.setId(dto.getCreatedBy());
            dto = inboxReplyService.save(dto , igmaUser);
            dto.setMsgType("success");
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("deletereply")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteReply(InboxReply dto) {
        try {
            inboxReplyService.delete(dto.getId());
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    

}

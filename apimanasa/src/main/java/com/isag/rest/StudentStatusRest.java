/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.StudentStatusService;
import com.isag.dto.StudentStatusDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
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
@Path("/studentstatus")
public class StudentStatusRest {

    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveGeneratorCode(StudentStatusDTO dto) {
        try {
            IgmaUser igmaUser = new IgmaUser();
            Branch branchId = new Branch(dto.getBranchId());
            igmaUser.setBranchId(branchId);
            IgmaCompany companyId = new IgmaCompany(dto.getCompanyId());
            igmaUser.setCompanyId(companyId);
            igmaUser.setId(dto.getCreatedBy());
            dto = studentStatusService.save(dto, null);
            dto.setMsgType("success");
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/find")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response find(StudentStatusDTO dto) {
        try {
            Integer studentStatus = studentStatusService.findStudentStatus(dto.getStudentId(), dto.getBranchId());
            return Response.ok(studentStatus).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteStudentReset(StudentStatusDTO dto) {
        try {
            studentStatusService.deleteStudentReset(dto.getId());
            dto.setMsg("تم الحذف بنجاح");
            dto.setMsgType("success");
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.StudentStatusService;
import com.isag.businessservice.ZoomLinkService;
import com.isag.dto.ZoomLinkDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
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
@Path("/zoomlink")
public class ZoomLinkRest {

    @EJB
    ZoomLinkService zoomLinkService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/findbyid")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ZoomLinkDTO findById(ZoomLinkDTO dTO) {
        return zoomLinkService.findById(dTO.getId());
    }

    @Transactional
    @Path("/find")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ZoomLinkDTO> find(ZoomLinkDTO dTO) {
        return zoomLinkService.find(dTO.getTeacherId(), dTO.getSchoolId(), dTO.getMaterialId());
    }

    @Transactional
    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(ZoomLinkDTO dto) {
        try {
            IgmaUser igmaUser = new IgmaUser();
            Branch branchId = new Branch(dto.getBranchId());
            igmaUser.setBranchId(branchId);
            IgmaCompany companyId = new IgmaCompany(dto.getCompanyId());
            igmaUser.setCompanyId(companyId);
            igmaUser.setId(dto.getCreatedBy());
            dto = zoomLinkService.save(dto, igmaUser);
            dto.setMsgType("success");
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(ZoomLinkDTO dto) {
        try {
            zoomLinkService.delete(dto.getId());
            dto.setMsg("تم الحذف بنجاح");
            dto.setMsgType("success");
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

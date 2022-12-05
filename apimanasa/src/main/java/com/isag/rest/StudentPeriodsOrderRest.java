/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.StudentPeriodsOrderService;
import com.isag.config.UserLoginData;
import com.isag.dto.StudentPeriodsOrderDTO;
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
@Path("/studentPeriods")
public class StudentPeriodsOrderRest {

    @EJB
    StudentPeriodsOrderService studentPeriodsOrderService;

    @Transactional
    @Path("/saveStudentPeriodsOrder")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveStudentPeriods(StudentPeriodsOrderDTO studentPeriodsOrderDTO) {
        IgmaUser igmaUser = new IgmaUser();
        igmaUser.setId(studentPeriodsOrderDTO.getCreatedBy());
        IgmaCompany company = new IgmaCompany() ;
        company.setId(studentPeriodsOrderDTO.getCompanyId());
        igmaUser.setCompanyId(company);
        try {
        String msg = studentPeriodsOrderService.save(studentPeriodsOrderDTO, igmaUser);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.StudentPeriodsOrderService;
import com.isag.businessservice.StudentStatusService;
import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.StudentPeriodsDTO;
import com.isag.dto.StudentPeriodsOrderDTO;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
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
 * @author AhmedEssam <ahmedEssam.com>
 */
@Path("/studentPeriodsOrders")
public class StudentPeriodsOrderRest {

    @EJB
    StudentPeriodsOrderService studentPeriodsOrderService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/saveStudentPeriodsOrder")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveStudentPeriods(StudentPeriodsOrderDTO studentPeriodsOrderDTO) {
        IgmaUser igmaUser = new IgmaUser();
        igmaUser.setId(studentPeriodsOrderDTO.getCreatedBy());
        IgmaCompany company = new IgmaCompany();
        company.setId(studentPeriodsOrderDTO.getCompanyId());
        igmaUser.setCompanyId(company);
        try {
            String msg = studentPeriodsOrderService.save(studentPeriodsOrderDTO, igmaUser);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/studentactivation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response studentactivation(APIStudentDataDTO aPIStudentDataDTO) {
        if (aPIStudentDataDTO != null) {
            try {
                List<APIStudentDataDTO> dTOs = studentPeriodsOrderService.studentActivation(aPIStudentDataDTO.getBranchId(), aPIStudentDataDTO.getSchoolStagesId(), aPIStudentDataDTO.getId());
                GenericEntity<List<APIStudentDataDTO>> list = new GenericEntity<List<APIStudentDataDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            aPIStudentDataDTO = new APIStudentDataDTO();
            aPIStudentDataDTO.setMsg("No Id is Sent");
            return Response.ok(aPIStudentDataDTO).build();
        }
    }
    
    @Transactional
    @Path("/studentActivationOrder")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response studentActivationOrder(APIStudentDataDTO aPIStudentDataDTO) {
        if (aPIStudentDataDTO != null) {
            try {
                List<APIStudentDataDTO> dTOs = studentPeriodsOrderService.studentActivationOrder(aPIStudentDataDTO.getId());
                GenericEntity<List<APIStudentDataDTO>> list = new GenericEntity<List<APIStudentDataDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            aPIStudentDataDTO = new APIStudentDataDTO();
            aPIStudentDataDTO.setMsg("No Id is Sent");
            return Response.ok(aPIStudentDataDTO).build();
        }
    }

    @Transactional
    @Path("/deletestudentactivation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletestudentactivation(APIStudentDataDTO aPIStudentDataDTO) {
        if (aPIStudentDataDTO != null) {
            try {
                studentPeriodsOrderService.deleteByStudentId(aPIStudentDataDTO.getPeriodOrderId55());
//                GenericEntity<List<APIStudentDataDTO>> list = new GenericEntity<List<APIStudentDataDTO>>(null) {
//                };
//                return Response.ok(list).build();
                StudentPeriodsDTO msg = new StudentPeriodsDTO();
                msg.setMsg("تم الحذف بنجاح");
                msg.setMsgType("success");
                return Response.ok(msg).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            aPIStudentDataDTO = new APIStudentDataDTO();
            aPIStudentDataDTO.setMsg("No Id is Sent");
            return Response.ok(aPIStudentDataDTO).build();
        }
    }

    @Transactional
    @Path("/studenttransaction")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response studentTransaction(APIStudentDataDTO aPIStudentDataDTO) {
        if (aPIStudentDataDTO != null) {
            try {
                studentPeriodsOrderService.transaction(aPIStudentDataDTO.getPeriodOrderId55(), null);
                StudentPeriodsDTO msg = new StudentPeriodsDTO();
                msg.setMsg("تم الاشتراك بنجاح");
                msg.setMsgType("success");
                return Response.ok(msg).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            aPIStudentDataDTO = new APIStudentDataDTO();
            aPIStudentDataDTO.setMsg("No Id is Sent");
            return Response.ok(aPIStudentDataDTO).build();
        }
    }
}

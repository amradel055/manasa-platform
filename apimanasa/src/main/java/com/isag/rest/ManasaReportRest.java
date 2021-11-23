/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.ManasaReportService;
import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.ExamsDTO;
import com.isag.dto.QuestionsDTO;
import java.util.ArrayList;
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
@Path("/manasaReportRest")
public class ManasaReportRest {

    @EJB
    ManasaReportService manasaReportService;
    
    @Transactional
    @Path("/examStudentNumber")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findExamStudentNumber(ExamsDTO examsDTO) {
        if (examsDTO != null) {
            try {
                Integer dTO = manasaReportService.findExamStudentNumber(examsDTO.getId());
                List<ExamsDTO> number = new ArrayList<>();
                examsDTO.setPeriodId(dTO);
                number.add(examsDTO);
                GenericEntity<List<ExamsDTO>> list = new GenericEntity<List<ExamsDTO>>(number) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            examsDTO = new ExamsDTO();
            examsDTO.setMsg("No Id is Sent");
            examsDTO.setMsgType("failed");
            return Response.ok(examsDTO).build();
        }
    }
 
    
    
    
    @Transactional
    @Path("/examStudentbestNumbers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findExamBestStudent(ExamsDTO examsDTO) {
        if (examsDTO != null) {
            try {
                List<ExamsDTO> dTO = manasaReportService.findExamBestStudents(examsDTO.getId());
                
                GenericEntity<List<ExamsDTO>> list = new GenericEntity<List<ExamsDTO>>(dTO) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            examsDTO = new ExamsDTO();
            examsDTO.setMsg("No Id is Sent");
            examsDTO.setMsgType("failed");
            return Response.ok(examsDTO).build();
        }
    }
    
    
    
    @Transactional
    @Path("/mostMistakes")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findExamMostMistakes(ExamsDTO examsDTO) {
        if (examsDTO != null) {
            try {
                List<QuestionsDTO> dTO = manasaReportService.findExamMostMisatkes(examsDTO.getId());
                
                GenericEntity<List<QuestionsDTO>> list = new GenericEntity<List<QuestionsDTO>>(dTO) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            examsDTO = new ExamsDTO();
            examsDTO.setMsg("No Id is Sent");
            examsDTO.setMsgType("failed");
            return Response.ok(examsDTO).build();
        }
    }
}

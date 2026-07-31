/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.QuestionAnswerDetailService;
import com.isag.businessservice.QuestionCommonService;
import com.isag.businessservice.StudentQuestionsService;
import com.isag.businessservice.StudentStatusService;
import com.isag.dto.QuestionCommonDTO;
import com.isag.dto.StudentQuestionsDTO;
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
@Path("/commonquestions")
public class QuestionCommonRest {

    @EJB
    QuestionCommonService questionCommonService;
    @EJB
    QuestionAnswerDetailService questionAnswerDetailService;
    @EJB
    StudentQuestionsService studentQuestionsService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/findcommonquestions")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<QuestionCommonDTO> finQuestionCommon(QuestionCommonDTO questionCommonDTO) {
        List<QuestionCommonDTO> questionCommonDTOList = questionCommonService.findQuestionCommonAndAnswer(questionCommonDTO.getTeacherId(), questionCommonDTO.getMaterialId() ,questionCommonDTO.getSchoolId() );
        return questionCommonDTOList;
    }

    @Transactional
    @Path("/studentQuestion")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<StudentQuestionsDTO> finQuestionCommon(StudentQuestionsDTO studentQuestionsDTO) {
        List<StudentQuestionsDTO> studentQuestionsDTOList = studentQuestionsService.findStudentQuestionsAndAnswer(studentQuestionsDTO.getTeacherId(), studentQuestionsDTO.getMaterialId());
        return studentQuestionsDTOList;
    }
    
    @Transactional
    @Path("/findQuestion")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public QuestionCommonDTO findQuestion(QuestionCommonDTO question) {
        QuestionCommonDTO questionCommon = questionCommonService.findDto(question.getId());
        return questionCommon;
    }

    @Transactional
    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response lessonRegister(QuestionCommonDTO commonDTO) {
        try {
            IgmaUser igmaUser = new IgmaUser();
            Branch branchId = new Branch(commonDTO.getBranchId());
            igmaUser.setBranchId(branchId);
            IgmaCompany companyId = new IgmaCompany(commonDTO.getCompanyId());
            igmaUser.setCompanyId(companyId);
            igmaUser.setId(commonDTO.getTeacherId());
            QuestionCommonDTO msg = questionCommonService.save(commonDTO, igmaUser);
           msg.setMsgType("success");
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    @Transactional
    @Path("deletequestion")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteQuestion(QuestionCommonDTO dto) {
        try {
            questionCommonService.delete(dto.getId());
            dto.setMsg("تم الحذف بنجاح");
            dto.setMsgType("success");
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    

}

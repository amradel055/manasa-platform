/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.QuestionCommonService;
import com.isag.businessservice.StudentQuestionsService;
import com.isag.config.UserLoginData;
import com.isag.dto.QuestionCommonDTO;
import com.isag.dto.StudentQuestionsDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author AhmedEssam <ahmedEssam.com>
 */
@Path("/question")
public class QuestionCommonRest {

    private UserLoginData userLoginData;
    @EJB
    QuestionCommonService questionCommonService;
    @EJB
    StudentQuestionsService studentQuestionsService;

    @Transactional
    @Path("/QuestionCommon")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<QuestionCommonDTO> finQuestionCommon(QuestionCommonDTO questionCommonDTO) {
        List<QuestionCommonDTO> questionCommonDTOList = new ArrayList<>();
        questionCommonDTOList = questionCommonService.findQuestionCommonAndAnswer(questionCommonDTO.getTeacherId(), questionCommonDTO.getMaterialId());

        return questionCommonDTOList;
    }

    @Transactional
    @Path("/studentQuestion")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<StudentQuestionsDTO> finQuestionCommon(StudentQuestionsDTO studentQuestionsDTO) {
        List<StudentQuestionsDTO> studentQuestionsDTOList = new ArrayList<>();
        studentQuestionsDTOList = studentQuestionsService.findStudentQuestionsAndAnswer(studentQuestionsDTO.getTeacherId(), studentQuestionsDTO.getMaterialId());
        return studentQuestionsDTOList;
    }

}

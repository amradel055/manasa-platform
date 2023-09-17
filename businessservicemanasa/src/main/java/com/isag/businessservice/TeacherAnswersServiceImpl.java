/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.TeacherAnswersDTO;
import com.isag.entity.Branch;
import com.isag.entity.TeacherAnswers;
import com.isag.entity.IgmaUser;
import com.isag.entity.QuestionCommon;
import com.isag.entity.StudentQuestions;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class TeacherAnswersServiceImpl implements TeacherAnswersService {

    @EJB
    private GenericDAO dao;
    @EJB
    private TeacherAnswerDetailService teacherAnswerDetailService;

    private TeacherAnswersDTO mapTODTO(TeacherAnswers entity) {
        TeacherAnswersDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    @Override
    public void save(TeacherAnswersDTO teacherAnswers, IgmaUser isagUser) {
        TeacherAnswers studentPeriodsOrder = mapToEntity(teacherAnswers, isagUser);
        dao.updateEntity(studentPeriodsOrder);
    }

    @Override
    public void delete(Integer id, IgmaUser igmaUser) {
        TeacherAnswers studentPeriodsOrder = dao.findEntityById(TeacherAnswers.class, id);
        dao.deleteEntity(studentPeriodsOrder);
    }



    @Override
    public List<TeacherAnswersDTO> findTeacherAnswersDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM TeacherAnswers d WHERE d.branchId.id = :branchId  ");
        List<TeacherAnswers> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }


    private List<TeacherAnswersDTO> returnListDTO(List<TeacherAnswers> list) {
        List<TeacherAnswersDTO> dTOList = new ArrayList<>();
        for (TeacherAnswers entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private TeacherAnswersDTO initMapToDTO(TeacherAnswers entity) {

        TeacherAnswersDTO dTO = new TeacherAnswersDTO();
        dTO.setText(entity.getText());
        dTO.setImage(entity.getImage());
        dTO.setTeacherAnswerDetailDTOList(teacherAnswerDetailService.findAnswerDetail(entity.getId()));
        dTO.setStudentQuestionId(entity.getStudentQuestionId().getId());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private TeacherAnswers mapToEntity(TeacherAnswersDTO dTO, IgmaUser isagUser) {

        TeacherAnswers entity = new TeacherAnswers();

        entity.setId(dTO.getId());
        entity.setText(dTO.getText());
        entity.setImage(dTO.getImage());
        if(dTO.getStudentQuestionId()!= null){
            StudentQuestions studentQuestions = new StudentQuestions() ;
            studentQuestions.setId(dTO.getStudentQuestionId());
            entity.setStudentQuestionId(studentQuestions);
        }

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setBranchId(isagUser.getBranchId());
            entity.setCreationDate(new Date());
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }
        

        Branch branch = new Branch();
        branch.setId(dTO.getBranchId());
        entity.setBranchId(branch);
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }
    
    
    @Override
    public TeacherAnswersDTO findQuestioAnswer(Integer questionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("questionId", questionId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM TeacherAnswers d WHERE d.studentQuestionId.id = :questionId ");
        TeacherAnswers questionAnswer = dao.findEntityByQuery(builder.toString(), params);
        return mapTODTO(questionAnswer);
    }


}

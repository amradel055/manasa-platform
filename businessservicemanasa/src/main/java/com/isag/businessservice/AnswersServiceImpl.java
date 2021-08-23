/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.AnswersDTO;
import com.isag.entity.Branch;
import com.isag.entity.IsagCompany;
import com.isag.entity.Answers;
import com.isag.entity.Exams;
import com.isag.entity.IsagUser;
import com.isag.entity.Questions;
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
public class AnswersServiceImpl implements AnswersService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<AnswersDTO> findAnswersByQuestionId(Integer questionId) {
        Map<String, Object> params = new HashMap<>();// here
        params.put("questionId", questionId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Answers d WHERE d.questionId.id = :questionId order by d.id asc ");
        List<Answers> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public Map<Integer, String> findAnswersByExamId(Integer examId) {
        Map<String, Object> params = new HashMap<>();
        Map<Integer, String> answerMap = new HashMap<>();
        params.put("examId", examId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Answers d WHERE d.examId.id = :examId AND d.correct = 1");
        List<Answers> list = dao.findListByQuery(builder.toString(), params);
        for (Answers answers : list) {
            answerMap.put(answers.getQuestionId().getId(), answers.getAnswer());
        }
        return answerMap;
    }

    @Override
    public AnswersDTO saveList(List<AnswersDTO> answersDTOList, IsagUser isagUser, Integer questionId, Integer examId) {
        AnswersDTO answersDTO = null;
        for (AnswersDTO answersDTO1 : answersDTOList) {
            answersDTO = save(answersDTO1, isagUser, questionId, examId);
        }
        return answersDTO;
    }

    @Override
    public void deleteList(List<AnswersDTO> answersDTOsList) {

        for (AnswersDTO answersDTO : answersDTOsList) {
            deleteAnswers(answersDTO);
        }

    }

    @Override
    public void deleteListt(List<Integer> answersDTOsList) {

        for (Integer answersDTO : answersDTOsList) {
            deleteAnswers(answersDTO);
        }

    }

    @Override
    public AnswersDTO save(AnswersDTO answersDTO, IsagUser isagUser, Integer questionId, Integer examId) {
        AnswersDTO msg = new AnswersDTO();
        Answers answers = null;
        if (answersDTO.getId() != null) {
            answers = dao.findEntityById(Answers.class, answersDTO.getId());
        }
        if (answersDTO != null && questionId != null && examId != null) {
            if (isagUser == null) {
                isagUser = new IsagUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(answersDTO.getBranchId());
                isagUser.setBranchId(branch);
                IsagCompany isagCompany = new IsagCompany();
                isagCompany.setId(answersDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            answers = mapToEntity(answersDTO, isagUser, answers, questionId, examId);
            dao.updateEntity(answers);
            msg.setMsg("تم الحفظ بنجاح");
            msg.setMsgType("success");
            return msg;
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    @Override
    public void deleteAnswers(AnswersDTO answersDTO) {
        Answers entity = new Answers();
        entity.setId(answersDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteAnswers(Integer answerId) {
        Answers entity = new Answers();
        entity.setId(answerId);
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteAnswersFromQuestion(Integer questionId) {
        List<AnswersDTO> list = findAnswersByQuestionId(questionId);
        deleteList(list);
    }

    private List<AnswersDTO> returnListDTO(List<Answers> list) {
        List<AnswersDTO> dTOList = new ArrayList<>();
        for (Answers entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private AnswersDTO mapTODTO(Answers entity) {
        AnswersDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private AnswersDTO initMapToDTO(Answers entity) {

        AnswersDTO dTO = new AnswersDTO();
        dTO.setId(entity.getId());
        dTO.setAnswer(entity.getAnswer());
        dTO.setCorrect(entity.getCorrect());
        dTO.setReason(entity.getReason());
        dTO.setSelection(entity.getSelection());

        if (entity.getQuestionId() != null) {
            dTO.setQuestionId(entity.getQuestionId().getId());
        }

        if (entity.getExamId() != null) {
            dTO.setExamId(entity.getExamId().getId());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private Answers mapToEntity(AnswersDTO dTO, IsagUser isagUser, Answers entity, Integer questionId, Integer examId) {
        if (entity == null) {
            entity = new Answers();
        }

        entity.setId(dTO.getId());

            entity.setAnswer(dTO.getAnswer());

        entity.setCorrect(dTO.getCorrect());
        entity.setReason(dTO.getReason());
        entity.setSelection(dTO.getSelection());

        if (questionId != null) {
            Questions questions = new Questions();
            questions.setId(questionId);
            entity.setQuestionId(questions);
        }
        if (examId != null) {
            Exams exams = new Exams();
            exams.setId(examId);
            entity.setExamId(exams);
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

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}

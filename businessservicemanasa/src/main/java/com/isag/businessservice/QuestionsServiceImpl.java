/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.AnswersDTO;
import com.isag.dto.QuestionsDTO;
import com.isag.entity.Branch;
import com.isag.entity.Exams;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.Questions;
import com.isag.entity.Student;
import java.math.BigDecimal;
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
public class QuestionsServiceImpl implements QuestionsService {

    @EJB
    private GenericDAO dao;
    @EJB
    AnswersService answersService;

    @Override
    public List<QuestionsDTO> findQuestionsByExamId(Integer examsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("examsId", examsId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Questions d WHERE d.examsId.id = :examsId order by d.id asc ");
        List<Questions> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public QuestionsDTO saveList(List<QuestionsDTO> questionsDTOsList, IgmaUser isagUser, Integer examsId) {

        QuestionsDTO questionsDTO = null;
        for (QuestionsDTO questionsDTO1 : questionsDTOsList) {
            questionsDTO = save(questionsDTO1, isagUser, examsId);
            if (questionsDTO.getMsg() != null && questionsDTO.getMsgType().equals("success")) {
                if (questionsDTO1.getAnswersdtoList() != null) {
                    answersService.saveList(questionsDTO1.getAnswersdtoList(), isagUser, questionsDTO.getId(), examsId);
                }
            }
        }
        return questionsDTO;
    }

    @Override
    public QuestionsDTO saveListForStudent(List<QuestionsDTO> questionsDTOsList, IgmaUser isagUser, Integer examsId, Integer studentId, Integer parentExamId) {

        QuestionsDTO questionsDTO = null;
        BigDecimal total = BigDecimal.ZERO;
        Map<Integer, String> mapAnswer = answersService.findAnswersByExamId(parentExamId);
        for (QuestionsDTO questionsDTO1 : questionsDTOsList) {
            BigDecimal qestionDegree = BigDecimal.ZERO;
            for (AnswersDTO answersDTO : questionsDTO1.getAnswersdtoList()) {
                if (mapAnswer.containsKey(questionsDTO1.getParent()) && answersDTO.getSelection() != null && answersDTO.getSelection() == 1) {
                    String answer = mapAnswer.get(questionsDTO1.getParent());
                    if (answer != null && !answer.isEmpty() && answer.equals(answersDTO.getAnswer())) {
                        total = total.add(questionsDTO1.getQuestionDegree());
                        qestionDegree = questionsDTO1.getQuestionDegree();
                    }
                }
            }
            questionsDTO1.setQuestionDegree(qestionDegree);
            questionsDTO = save(questionsDTO1, isagUser, examsId);
            answersService.saveList(questionsDTO1.getAnswersdtoList(), isagUser, questionsDTO.getId(), examsId);
        }
        questionsDTO.setTotalDegree(total);
        return questionsDTO;
    }

    @Override
    public void deleteList(List<Integer> questionsDTOsList) {

        for (Integer questionsDTO : questionsDTOsList) {
            deleteQuestions(questionsDTO);
        }
    }

    public void deleteListQuestionsDTO(List<QuestionsDTO> questionsDTOsList) {

        for (QuestionsDTO questionsDTO : questionsDTOsList) {
            deleteQuestionAndAnswer(questionsDTO.getId());
        }
    }

    @Override
    public void deleteQuestionAndAnswer(Integer questionsId) {
        updateParentToNull(questionsId);
        answersService.deleteAnswersFromQuestion(questionsId);
        deleteQuestions(questionsId);
    }

    @Override
    public QuestionsDTO save(QuestionsDTO questionsDTO, IgmaUser isagUser, Integer examsId) {
        QuestionsDTO msg = new QuestionsDTO();
        Questions questions = null;
        if (questionsDTO.getId() != null) {
            questions = dao.findEntityById(Questions.class, questionsDTO.getId());
        }
        if (questionsDTO != null && examsId != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(questionsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(questionsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            questions = mapToEntity(questionsDTO, isagUser, questions, examsId);
            questions = dao.updateEntity(questions);
            msg.setId(questions.getId());
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
    public void deleteQuestions(QuestionsDTO questionDTO) {
        Questions entity = new Questions();
        entity.setId(questionDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteQuestions(Integer questionId) {
        Questions entity = new Questions();
        entity.setId(questionId);
        dao.deleteEntity(entity);
    }

    public void updateParentToNull(Integer id) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        String builder = " update Questions e set e.parent = null where e.parent.id = :id ";
        dao.executeUpdateQuery(builder, params);
    }

    @Override
    public void deleteQuestionsForExam(Integer examId) {
        List<QuestionsDTO> questionsDTOsList = findQuestionsByExamId(examId);
        deleteListQuestionsDTO(questionsDTOsList);
    }

    private List<QuestionsDTO> returnListDTO(List<Questions> list) {
        List<QuestionsDTO> dTOList = new ArrayList<>();
        for (Questions entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private QuestionsDTO mapTODTO(Questions entity) {
        QuestionsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private QuestionsDTO initMapToDTO(Questions entity) {

        QuestionsDTO dTO = new QuestionsDTO();
        dTO.setId(entity.getId());
        dTO.setQuestionName(entity.getQuestionName());
        dTO.setQuestionType(entity.getQuestionType());
        dTO.setQuestionDegree(entity.getQuestionDegree());
        dTO.setImg(entity.getImg());

        if (entity.getExamsId() != null) {
            dTO.setExamsId(entity.getExamsId().getId());
        }
        if (entity.getStudentId() != null) {
            dTO.setStudentId(entity.getStudentId().getId());
        }
        if (entity.getParent() != null) {
            dTO.setParent(entity.getParent().getId());
        }

        dTO.setAnswersdtoList(answersService.findAnswersByQuestionId(entity.getId()));

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private Questions mapToEntity(QuestionsDTO dTO, IgmaUser isagUser, Questions entity, Integer examsId) {
        if (entity == null) {
            entity = new Questions();
        }

        entity.setId(dTO.getId());
        entity.setQuestionName(dTO.getQuestionName());
        entity.setQuestionType(dTO.getQuestionType());
        entity.setQuestionDegree(dTO.getQuestionDegree());
        entity.setImg(dTO.getImg());

        if (examsId != null) {
            Exams exams = new Exams();
            exams.setId(examsId);
            entity.setExamsId(exams);
        }

        if (dTO.getStudentId() != null) {
            Student student = new Student();
            student.setId(dTO.getStudentId());
            entity.setStudentId(student);
        }

        if (dTO.getParent() != null) {
            Questions questions = new Questions();
            questions.setId(dTO.getParent());
            entity.setParent(questions);
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

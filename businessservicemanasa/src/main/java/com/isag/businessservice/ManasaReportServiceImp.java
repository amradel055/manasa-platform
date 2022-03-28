/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.ExamsDTO;
import com.isag.dto.QuestionsDTO;
import com.isag.entity.Exams;
import com.isag.entity.Questions;
import com.isag.entity.Student;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class ManasaReportServiceImp implements ManasaReportService {

    @EJB
    private GenericDAO dao;

    @EJB
    ExamsService examsService;

    @EJB
    QuestionsService questionsService;

    @Override
    public Integer findExamStudentNumber(Integer examId) {

        Map<String, Object> params = new HashMap<>();
    
        params.put("parent", examId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(e.id) from Exams e where e.parent.id = :parent group by e.parent.id  ");
        List<Long> list = dao.findListByQuery(builder.toString(), params);
        Integer number = 0;
        if (list != null && !list.isEmpty()) {
            Long x = list.get(0);
            number = x.intValue();
        }
    
        return number;
    }

    @Override
    public List<ExamsDTO> findExamBestStudents(Integer examId) {

        Map<String, Object> params = new HashMap<>();
        params.put("parent", examId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e from Exams e where e.parent.id = :parent order by e.score  ");
        List<Exams> list = dao.findListByQuery(builder.toString(), params);
        List<ExamsDTO> examsList = examsService.returnListDTO(list, false);
        return examsList;
    }

    @Override
    public List<QuestionsDTO> findExamMostMisatkes(Integer examId) {
        Map<String, Object> params = new HashMap<>();
//        params.put("parent", examId);
        StringBuilder builder = new StringBuilder();

//        builder.append("SELECT count(a.questionId.id), a.questionId.questionName "
//                + " from Answers a where a.selection = 1 "
//                + " and a.correct is null "
//                + "and  a.examId is not null "
//                + "and  a.examId.parent is not null "
////                + "and  a.examId.parent.id = :parent "
//                + " group by a.questionId.parent.id, a.questionId.questionName order by count(a.questionId.id) desc ");

   builder.append(
                    "SELECT count(q.id), q.question_name from answers a \n" +
                    " left join questions q on q.id = a.question_id \n" +
                    " left join exams e on e.id = q.exams_id \n" +
                    " where e.parent = " + examId +
                    " and a.correct is null and a.selection = 1 \n" +
                    " group by q.parent,q.question_name order by count(q.id) desc ");

        List<Object[]> res = dao.executeNativeQuery(builder.toString());
        List<QuestionsDTO> questionsDTOList = new ArrayList<>();
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            QuestionsDTO questionsDTO = new QuestionsDTO();
            BigDecimal d = new BigDecimal((Long) line[0]);
            questionsDTO.setQuestionDegree((BigDecimal) d );
            questionsDTO.setQuestionName((String) line[1]);
            questionsDTOList.add(questionsDTO);
        }
        return questionsDTOList;
    }
    

}

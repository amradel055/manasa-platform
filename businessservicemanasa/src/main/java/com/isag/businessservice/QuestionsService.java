/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.QuestionsDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface QuestionsService {

    public List<QuestionsDTO> findQuestionsByQuestionId(Integer examsId);

    public QuestionsDTO save(QuestionsDTO questionsDTO, IgmaUser isagUser, Integer examsId);
    
    public void deleteQuestionAndAnswer(Integer questionsId);

    public void deleteQuestions(QuestionsDTO questionDTO);

    public void deleteQuestions(Integer questionId);

    public QuestionsDTO saveList(List<QuestionsDTO> questionsDTOsList, IgmaUser isagUser, Integer examsId);

    public QuestionsDTO saveListForStudent(List<QuestionsDTO> questionsDTOsList, IgmaUser isagUser, Integer examsId, Integer studentId, Integer parentExamId);

    public void deleteList(List<Integer> questionsDTOsList);
    
     public void deleteQuestionsForExam(Integer examId);

}

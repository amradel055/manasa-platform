/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.AnswersDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface AnswersService {

    public List<AnswersDTO> findAnswersByQuestionId(Integer questionId);
    
    public Map<Integer, String> findAnswersByExamId(Integer examId);

    public AnswersDTO save(AnswersDTO answersDTO, IsagUser isagUser, Integer questionId,Integer examId);

    public void deleteAnswers(AnswersDTO answersDTO);

    public void deleteAnswers(Integer answerId);

    public void deleteListt(List<Integer> answersDTOsList);

    public void deleteList(List<AnswersDTO> answersDTOsList);

    public AnswersDTO saveList(List<AnswersDTO> answersDTOList, IsagUser isagUser, Integer questionId,Integer examId);
    
    public void deleteAnswersFromQuestion(Integer questionId);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.QuestionAnswerDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface QuestionAnswerService {

    public void save(QuestionAnswerDTO questionAnswerDTO, IgmaUser isagUser);

    public void delete(Integer id, IgmaUser igmaUser);

    public List<QuestionAnswerDTO> findQuestionAnswerDTO(Integer branchId);
    
     public QuestionAnswerDTO findQuestioAnswer(Integer questionId)  ;

}

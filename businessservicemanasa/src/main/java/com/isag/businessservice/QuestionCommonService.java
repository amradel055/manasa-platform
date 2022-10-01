/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.QuestionCommonDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface QuestionCommonService {

    public QuestionCommonDTO save(QuestionCommonDTO questionCommonDTO, IgmaUser isagUser) ;

    public void delete(Integer id, IgmaUser igmaUser);

    public List<QuestionCommonDTO> findStudemtPeriodOrderDTO(Integer branchId);

    public List<QuestionCommonDTO> findQuestionCommonAndAnswer(Integer teacherId, Integer materialId);

}

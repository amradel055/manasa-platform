/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.QuestionAnswerDetailDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface QuestionAnswerDetailService {

    public List<QuestionAnswerDetailDTO> save(Integer answerId, List<QuestionAnswerDetailDTO> questionAnswerDetailDTOList, IgmaUser isagUser);

    public void delete(Integer answerId) ;

    public List<QuestionAnswerDetailDTO> findQuestionAnswerDetailDTO(Integer branchId);

    public List<QuestionAnswerDetailDTO> findAnswerDetail(Integer answerId);
    
     public void deleteByDetailId(Integer detailId) ;

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.QuestionAnswerDTO;
import com.isag.dto.QuestionAnswerDetailDTO;
import com.isag.entity.Branch;
import com.isag.entity.QuestionAnswerDetail;
import com.isag.entity.IgmaUser;
import com.isag.entity.QuestionAnswer;
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
public class QuestionAnswerDetailServiceImpl implements QuestionAnswerDetailService {

    @EJB
    private GenericDAO dao;

    private QuestionAnswerDetailDTO mapTODTO(QuestionAnswerDetail entity) {
        QuestionAnswerDetailDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    @Override
    public List<QuestionAnswerDetailDTO> save(Integer answerId, List<QuestionAnswerDetailDTO> questionAnswerDetailDTOList, IgmaUser isagUser) {
        List<QuestionAnswerDetailDTO> list = new ArrayList<>();
        for (QuestionAnswerDetailDTO questionAnswerDetailDTO : questionAnswerDetailDTOList) {
            QuestionAnswerDetailDTO detailDTO = new QuestionAnswerDetailDTO();
            QuestionAnswerDetail questionAnswerDetail = mapToEntity(answerId, questionAnswerDetailDTO, isagUser);
            questionAnswerDetail = dao.updateEntity(questionAnswerDetail);
            detailDTO = mapTODTO(questionAnswerDetail);
            list.add(detailDTO);
        }
        return (list);
    }

    @Override
    public void delete(Integer questionCommonId) {
        Map<String, Object> params = new HashMap<>();
        params.put("questionCommonId", questionCommonId);
        dao.executeDeleteQuery("delete FROM QuestionAnswerDetail e WHERE e.questionAnswerId.questionCommonId.id = " + questionCommonId);
    }

    @Override
    public void deleteByDetailId(Integer detailId) {
        QuestionAnswerDetail answerDetail = new QuestionAnswerDetail();
        answerDetail.setId(detailId);
        dao.deleteEntity(answerDetail);
    }

    @Override
    public List<QuestionAnswerDetailDTO> findQuestionAnswerDetailDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM QuestionAnswerDetail d WHERE d.branchId.id = :branchId  ");
        List<QuestionAnswerDetail> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    private List<QuestionAnswerDetailDTO> returnListDTO(List<QuestionAnswerDetail> list) {
        List<QuestionAnswerDetailDTO> dTOList = new ArrayList<>();
        for (QuestionAnswerDetail entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private QuestionAnswerDetailDTO initMapToDTO(QuestionAnswerDetail entity) {

        QuestionAnswerDetailDTO dTO = new QuestionAnswerDetailDTO();
        dTO.setDescription(entity.getDescription());
        dTO.setImage(entity.getImage());
        if (entity.getQuestionAnswerId().getId() != null) {
            dTO.setQuestionAnswerId(entity.getQuestionAnswerId().getId());
        }
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private QuestionAnswerDetail mapToEntity(Integer answerId, QuestionAnswerDetailDTO dTO, IgmaUser isagUser) {

        QuestionAnswerDetail entity = new QuestionAnswerDetail();

        entity.setId(dTO.getId());
        entity.setDescription(dTO.getDescription());
        if (dTO.getImage() != null) {
            entity.setImage(dTO.getImage());
        }
        if (answerId != null) {
            QuestionAnswer questionAnswer = new QuestionAnswer();
            questionAnswer.setId(answerId);
            entity.setQuestionAnswerId(questionAnswer);
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
    public List<QuestionAnswerDetailDTO> findAnswerDetail(Integer answerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("answerId", answerId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM QuestionAnswerDetail d WHERE d.questionAnswerId.id = :answerId ");
        List<QuestionAnswerDetail> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

}

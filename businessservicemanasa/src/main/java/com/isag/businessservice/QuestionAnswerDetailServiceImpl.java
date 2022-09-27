/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
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
    public void save(QuestionAnswerDetailDTO questionAnswerDetailDTO, IgmaUser isagUser) {
        QuestionAnswerDetail studentPeriodsOrder = mapToEntity(questionAnswerDetailDTO, isagUser);
        dao.updateEntity(studentPeriodsOrder);
    }

    @Override
    public void delete(Integer id, IgmaUser igmaUser) {
        QuestionAnswerDetail studentPeriodsOrder = dao.findEntityById(QuestionAnswerDetail.class, id);
        dao.deleteEntity(studentPeriodsOrder);
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
        dTO.setQuestionAnswerId(entity.getQuestionAnswerId().getId());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private QuestionAnswerDetail mapToEntity(QuestionAnswerDetailDTO dTO, IgmaUser isagUser) {

        QuestionAnswerDetail entity = new QuestionAnswerDetail();

        entity.setId(dTO.getId());
        entity.setDescription(dTO.getDescription());
        entity.setImage(dTO.getImage());
        if (dTO.getQuestionAnswerId() != null) {
            QuestionAnswer questionAnswer = new QuestionAnswer();
            questionAnswer.setId(dTO.getQuestionAnswerId());
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
        branch.setId(232);
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

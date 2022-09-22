/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.QuestionAnswerDTO;
import com.isag.entity.Branch;
import com.isag.entity.QuestionAnswer;
import com.isag.entity.IgmaUser;
import com.isag.entity.QuestionCommon;
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
public class QuestionAnswerServiceImpl implements QuestionAnswerService {

    @EJB
    private GenericDAO dao;


    private QuestionAnswerDTO mapTODTO(QuestionAnswer entity) {
        QuestionAnswerDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    @Override
    public void save(QuestionAnswerDTO studentPeriodsOrderDTO, IgmaUser isagUser) {
        QuestionAnswer studentPeriodsOrder = mapToEntity(studentPeriodsOrderDTO, isagUser);
        dao.updateEntity(studentPeriodsOrder);
    }

    @Override
    public void delete(Integer id, IgmaUser igmaUser) {
        QuestionAnswer studentPeriodsOrder = dao.findEntityById(QuestionAnswer.class, id);
        dao.deleteEntity(studentPeriodsOrder);
    }



    @Override
    public List<QuestionAnswerDTO> findQuestionAnswerDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM QuestionAnswer d WHERE d.branchId.id = :branchId  ");
        List<QuestionAnswer> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }


    private List<QuestionAnswerDTO> returnListDTO(List<QuestionAnswer> list) {
        List<QuestionAnswerDTO> dTOList = new ArrayList<>();
        for (QuestionAnswer entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private QuestionAnswerDTO initMapToDTO(QuestionAnswer entity) {

        QuestionAnswerDTO dTO = new QuestionAnswerDTO();
        dTO.setText(entity.getText());
        dTO.setImage(entity.getImage());
        dTO.setQuestionCommonId(entity.getQuestionCommonId().getId());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private QuestionAnswer mapToEntity(QuestionAnswerDTO dTO, IgmaUser isagUser) {

        QuestionAnswer entity = new QuestionAnswer();

        entity.setId(dTO.getId());
        entity.setText(dTO.getText());
        entity.setImage(dTO.getImage());
        if(dTO.getQuestionCommonId() != null){
            QuestionCommon questionCommon = new QuestionCommon() ;
            questionCommon.setId(dTO.getQuestionCommonId());
            entity.setQuestionCommonId(questionCommon);
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

}

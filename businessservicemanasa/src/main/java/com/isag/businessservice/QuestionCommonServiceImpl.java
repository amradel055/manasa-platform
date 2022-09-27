/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.QuestionCommonDTO;
import com.isag.entity.Branch;
import com.isag.entity.QuestionCommon;
import com.isag.entity.IgmaUser;
import com.isag.entity.Materials;
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
public class QuestionCommonServiceImpl implements QuestionCommonService {

    @EJB
    private GenericDAO dao;
    @EJB
    private QuestionAnswerService questionAnswerService;

    private QuestionCommonDTO mapTODTO(QuestionCommon entity) {
        QuestionCommonDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    @Override
    public void save(QuestionCommonDTO questionCommonDTO, IgmaUser isagUser) {
        QuestionCommon studentPeriodsOrder = mapToEntity(questionCommonDTO, isagUser);
        dao.updateEntity(studentPeriodsOrder);
    }

    @Override
    public void delete(Integer id, IgmaUser igmaUser) {
        QuestionCommon studentPeriodsOrder = dao.findEntityById(QuestionCommon.class, id);
        dao.deleteEntity(studentPeriodsOrder);
    }

    @Override
    public List<QuestionCommonDTO> findStudemtPeriodOrderDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM QuestionCommon d WHERE d.branchId.id = :branchId  ");
        List<QuestionCommon> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    private List<QuestionCommonDTO> returnListDTO(List<QuestionCommon> list) {
        List<QuestionCommonDTO> dTOList = new ArrayList<>();
        for (QuestionCommon entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private QuestionCommonDTO initMapToDTO(QuestionCommon entity) {

        QuestionCommonDTO dTO = new QuestionCommonDTO();
        dTO.setText(entity.getText());
        dTO.setImage(entity.getImage());
        dTO.setQuestionAnswerDTO(questionAnswerService.findQuestioAnswer(entity.getId()));
        dTO.setTeacherId(entity.getTeacherId().getId());
        dTO.setId(entity.getId());
        dTO.setMaterialId(entity.getMaterialId().getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private QuestionCommon mapToEntity(QuestionCommonDTO dTO, IgmaUser isagUser) {

        QuestionCommon entity = new QuestionCommon();

        if (dTO.getMaterialId() != null) {
            Materials materials = new Materials();
            materials.setId(dTO.getMaterialId());
            entity.setMaterialId(materials);
        }

        entity.setId(dTO.getId());
        entity.setText(dTO.getText());
        entity.setImage(dTO.getImage());
        if (dTO.getTeacherId() != null) {
            IgmaUser igmaUser = new IgmaUser();
            igmaUser.setId(dTO.getTeacherId());
            entity.setTeacherId(igmaUser);

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
    public List<QuestionCommonDTO> findQuestionCommonAndAnswer(Integer teacherId, Integer materialId) {
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", teacherId);
        params.put("materialId", materialId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM QuestionCommon d WHERE d.teacherId.id = :teacherId AND d.materialId.id = :materialId   ");
        List<QuestionCommon> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

}

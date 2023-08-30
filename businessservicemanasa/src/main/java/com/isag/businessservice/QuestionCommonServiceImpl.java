/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.QuestionAnswerDTO;
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
    public QuestionCommonDTO save(QuestionCommonDTO questionCommonDTO, IgmaUser isagUser) {
        QuestionCommon questionCommon = mapToEntity(questionCommonDTO, isagUser);
        questionCommon = dao.updateEntity(questionCommon);
        QuestionAnswerDTO questionAnswerDTO = questionAnswerService.save(questionCommon.getId(), questionCommonDTO.getQuestionAnswerDTO(), isagUser);
        QuestionCommonDTO questionCommonDTO1 = mapTODTO(questionCommon);
        questionCommonDTO1.setQuestionAnswerDTO(questionAnswerDTO);
        return mapTODTO(questionCommon);

    }

    @Override
    public void delete(Integer id) {
        QuestionCommon studentPeriodsOrder = dao.findEntityById(QuestionCommon.class, id);

        questionAnswerService.delete(studentPeriodsOrder.getId());

        dao.deleteEntity(studentPeriodsOrder);

    }

    @Override
    public QuestionCommonDTO findDto(Integer id) {
        QuestionCommon questionCommon = dao.findEntityById(QuestionCommon.class, id);
        return mapTODTO(questionCommon);
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
        dTO.setTeacherId(entity.getTeacherId() != null ? entity.getTeacherId().getId() : null);
        dTO.setId(entity.getId());
        dTO.setMaterialId(entity.getMaterialId() != null ? entity.getMaterialId().getId() : null);
        dTO.setSchoolId(entity.getMaterialId() != null ? entity.getMaterialId().getStageId().getId() : null);
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        dTO.setVideo(entity.getVideo());
        dTO.setAudio(entity.getAudio());

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

        entity.setVideo(dTO.getVideo());
        entity.setAudio(dTO.getAudio());

        if (isagUser != null) {
            entity.setTeacherId(isagUser);
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
    public List<QuestionCommonDTO> findQuestionCommonAndAnswer(Integer teacherId, Integer materialId, Integer stageId) {
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", teacherId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM QuestionCommon d WHERE d.teacherId.id = :teacherId   ");

        if (materialId != null) {
            params.put("materialId", materialId);
            builder.append(" AND d.materialId.id = :materialId   ");
        }
        if (stageId != null) {
            params.put("stageId", stageId);
            builder.append(" AND d.materialId.stageId.id = :stageId   ");
        }

        List<QuestionCommon> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

}

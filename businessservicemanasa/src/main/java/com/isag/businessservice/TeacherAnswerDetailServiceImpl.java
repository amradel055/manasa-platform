/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;
import com.isag.core.GenericDAO;
import com.isag.dto.TeacherAnswerDetailDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaUser;
import com.isag.entity.TeacherAnswerDetail;
import com.isag.entity.TeacherAnswers;
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
public class TeacherAnswerDetailServiceImpl implements TeacherAnswerDetailService {

    @EJB
    private GenericDAO dao;

    private TeacherAnswerDetailDTO mapTODTO(TeacherAnswerDetail entity) {
        TeacherAnswerDetailDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    @Override
    public void save(TeacherAnswerDetailDTO teacherAnswersDetailDTO, IgmaUser isagUser) {
        TeacherAnswerDetail teacherAnswersDetail = mapToEntity(teacherAnswersDetailDTO, isagUser);
        dao.updateEntity(teacherAnswersDetail);
    }

    @Override
    public void delete(Integer id, IgmaUser igmaUser) {
        TeacherAnswerDetail teacherAnswersDetail = dao.findEntityById(TeacherAnswerDetail.class, id);
        dao.deleteEntity(teacherAnswersDetail);
    }

    @Override
    public List<TeacherAnswerDetailDTO> findTeacherAnswerDetailDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM TeacherAnswerDetail d WHERE d.branchId.id = :branchId  ");
        List<TeacherAnswerDetail> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    private List<TeacherAnswerDetailDTO> returnListDTO(List<TeacherAnswerDetail> list) {
        List<TeacherAnswerDetailDTO> dTOList = new ArrayList<>();
        for (TeacherAnswerDetail entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private TeacherAnswerDetailDTO initMapToDTO(TeacherAnswerDetail entity) {

        TeacherAnswerDetailDTO dTO = new TeacherAnswerDetailDTO();
        dTO.setDescription(entity.getDescription());
        dTO.setImage(entity.getImage());
        dTO.setTeacherAnswerId(entity.getTeacherAnswerId().getId());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private TeacherAnswerDetail mapToEntity(TeacherAnswerDetailDTO dTO, IgmaUser isagUser) {
 
        TeacherAnswerDetail entity = new TeacherAnswerDetail();

        entity.setId(dTO.getId());
        entity.setDescription(dTO.getDescription());
        entity.setImage(dTO.getImage());
        if (dTO.getTeacherAnswerId()!= null) {
            TeacherAnswers teacherAnswers = new TeacherAnswers();
            teacherAnswers.setId(dTO.getTeacherAnswerId());
            entity.setTeacherAnswerId(teacherAnswers);
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
    public List<TeacherAnswerDetailDTO> findAnswerDetail(Integer answerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("answerId", answerId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM TeacherAnswerDetail d WHERE d.teacherAnswerId.id = :answerId ");
        List<TeacherAnswerDetail> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

}

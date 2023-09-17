/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.SchoolStagesGuideDTO;
import com.isag.entity.SchoolStagesGuide;
import com.isag.entity.IgmaUser;
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
public class schoolStagesGuideServiceImpl implements SchoolStagesGuideService {
    
    @EJB
    private GenericDAO dao;
    
    @Override
    public List<SchoolStagesGuideDTO> findschoolStagesGuide(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (branchId != null) {
            params.put("branchId", branchId);
            query = "SELECT b FROM SchoolStagesGuide b WHERE b.branchId.id = :branchId order by b.id desc";
        }
        List<SchoolStagesGuide> schoolStagesGuideList = dao.findListByQuery(query, params);
        List<SchoolStagesGuideDTO> listDTO = returnListDTO(schoolStagesGuideList);
        return listDTO;
    }
    
    @Override
    public List<SchoolStagesGuideDTO> findschoolStagesGuideByTeacherId(Integer teacherId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (teacherId != null) {
            params.put("teacherId", teacherId);
            query = " SELECT b FROM SchoolStagesGuide b WHERE b.teacherId.id = :teacherId ";
        }
        List<SchoolStagesGuide> schoolStagesGuideList = dao.findListByQuery(query, params);
        List<SchoolStagesGuideDTO> listDTO = returnListDTO(schoolStagesGuideList);
        return listDTO;
    }
    
    @Override
    public List<SchoolStagesGuideDTO> findschoolStagesGuideByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (companyId != null) {
            params.put("companyId", companyId);
            query = " SELECT b FROM SchoolStagesGuide b WHERE b.companyId.id = :companyId ";
        }
        List<SchoolStagesGuide> schoolStagesGuideList = dao.findListByQuery(query, params);
        List<SchoolStagesGuideDTO> listDTO = returnListDTO(schoolStagesGuideList);
        return listDTO;
    }
    
    private List<SchoolStagesGuideDTO> returnListDTO(List<SchoolStagesGuide> list) {
        List<SchoolStagesGuideDTO> dTOList = new ArrayList<>();
        for (SchoolStagesGuide entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }
    
    private SchoolStagesGuideDTO mapTODTO(SchoolStagesGuide entity) {
        SchoolStagesGuideDTO dTO = initMapToDTO(entity);
        
        return dTO;
    }
    
    private SchoolStagesGuideDTO initMapToDTO(SchoolStagesGuide entity) {
        
        SchoolStagesGuideDTO dTO = new SchoolStagesGuideDTO();
        dTO.setId(entity.getId());
        dTO.setName(entity.getName());
        dTO.setCode(entity.getCode());
        dTO.setLevel(entity.getLevel());
        dTO.setType(entity.getType());
        
        if (entity.getTeacherId() != null) {
            dTO.setTeacherId(entity.getTeacherId() == null ? null : entity.getTeacherId().getId());
        }
        
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        
        return dTO;
    }
    
    private SchoolStagesGuide mapToEntity(SchoolStagesGuideDTO dTO, IgmaUser isagUser) {
        SchoolStagesGuide entity = new SchoolStagesGuide();
        
        entity.setName(dTO.getName());
        entity.setCode(dTO.getCode());
        entity.setLevel(dTO.getLevel());
        entity.setType(dTO.getType());
        
        if (dTO.getTeacherId() != null) {
            IgmaUser igmaUser = new IgmaUser();
            igmaUser.setId(dTO.getTeacherId());
            entity.setTeacherId(igmaUser);
        }
        
        if (dTO.getId() == null) {
            
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IgmaUser isagUser1 = new IgmaUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }
        
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }
    
    @Override
    public List<SchoolStagesGuideDTO> findStage(Integer materialId) {
        List<SchoolStagesGuide> schoolStagesGuide = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("materialId", materialId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d.stageId FROM Materials d WHERE d.id = :materialId  ");
        schoolStagesGuide = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(schoolStagesGuide);
    }
    
    @Override
    public void deleteStage(Integer stageId) {
        dao.deleteEntity(new SchoolStagesGuide(stageId));
    }
    
    @Override
    public SchoolStagesGuide findschoolStagesGuideById(Integer stageId) {
        SchoolStagesGuide schoolStagesGuide = dao.findEntityById(SchoolStagesGuide.class, stageId) ;
        if(schoolStagesGuide != null){
            return schoolStagesGuide;
        }
        return null;
    }
    
    @Override
    public SchoolStagesGuide addSchoolStagesGuide(SchoolStagesGuide schoolStagesGuide) {
        return dao.updateEntity(schoolStagesGuide);
    }

    
}

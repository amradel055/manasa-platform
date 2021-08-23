/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.SchoolStagesGuideDTO;
import com.isag.entity.SchoolStagesGuide;
import com.isag.entity.IsagUser;
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

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    private SchoolStagesGuide mapToEntity(SchoolStagesGuideDTO dTO, IsagUser isagUser) {
        SchoolStagesGuide entity = new SchoolStagesGuide();

        entity.setName(dTO.getName());
        entity.setCode(dTO.getCode());
        entity.setLevel(dTO.getLevel());
        entity.setType(dTO.getType());
       

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
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

}

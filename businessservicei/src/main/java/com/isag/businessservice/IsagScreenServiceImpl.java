/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IsagScreenDTO;
import com.isag.entity.IsagScreen;
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
 * @author hq004
 */
@Stateless
public class IsagScreenServiceImpl implements IsagScreenService {

    @EJB
    private GenericDAO dao;
    
    @Override
    public List<IsagScreenDTO> getAllScreens(Integer moduleType){
        
        Map<String, Object> params = new HashMap<>();
        params.put("moduleType",moduleType);
        
        List<IsagScreen> screen = dao.findListByQuery(" SELECT i FROM IsagScreen i where i.moduleType = :moduleType ", params);
        
        return returnListDTO(screen);
    }
    
     private List<IsagScreenDTO> returnListDTO(List<IsagScreen> list) {
        List<IsagScreenDTO> dTOList = new ArrayList<>();
        for (IsagScreen entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }
    
    private IsagScreenDTO mapTODTO(IsagScreen entity) {
        IsagScreenDTO dTO = initMapToDTO(entity);
        return dTO;
    }
    
    private IsagScreenDTO initMapToDTO(IsagScreen entity) {
        IsagScreenDTO dTO = new IsagScreenDTO();
        
        dTO.setModuleAr(entity.getModuleAr());
        dTO.setModuleEn(entity.getModuleEn());
        dTO.setModuleType(entity.getModuleType());
        dTO.setName(entity.getName());
        dTO.setNamear(entity.getNamear());
        dTO.setSerial(entity.getSerial());
        dTO.setTabAr(entity.getTabAr());
        dTO.setTabEn(entity.getTabEn());
         
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        dTO.setCreatedDate(entity.getCreationDate());
        
        return dTO;
    }
    
    private IsagScreen mapToEntity (IsagScreenDTO dTO, IsagUser isagUser) {
        IsagScreen entity = new IsagScreen();
        
        entity.setModuleAr(dTO.getModuleAr());
        entity.setModuleEn(dTO.getModuleEn());
        entity.setModuleType(dTO.getModuleType());
        entity.setName(dTO.getName());
        entity.setNamear(dTO.getNamear());
        entity.setSerial(dTO.getSerial());
        entity.setTabAr(dTO.getTabAr());
        entity.setTabEn(dTO.getTabEn());
        
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
        entity.setId(dTO.getId());        
        
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
        
    }

   

}

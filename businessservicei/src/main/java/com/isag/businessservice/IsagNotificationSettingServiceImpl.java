/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IsagNotificationSettingDTO;
import com.isag.entity.IsagNotificationSetting;
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
 * @author ahmedEssam
 */
@Stateless
public class IsagNotificationSettingServiceImpl implements IsagNotificationSettingService {

    @EJB
    private GenericDAO dao;

    
    
     @Override
    public IsagNotificationSettingDTO addIsagNotificationSettingDTO(IsagNotificationSettingDTO isagNotificationSettingDTO, IsagUser isagUser) {
        IsagNotificationSetting entity = mapFromDTO(isagNotificationSettingDTO, isagUser);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity);
    }
    
    @Override
    public List<IsagNotificationSettingDTO> getAllisagNotificationSettingDTO(Integer branchid) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", branchid);
        List<IsagNotificationSetting> isagNotificationSettingList = dao.findListByQuery("SELECT INS FROM IsagNotificationSetting INS WHERE INS.branchId.id = :branchid", params);
        return returnListDTO(isagNotificationSettingList);
    }
     
     @Override
    public IsagNotificationSettingDTO getIsagNotificationSettingDTOByUserId(Integer branchId , Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", branchId); 
        params.put("createdBy", userId);
         IsagNotificationSetting isagNotificationSettingList = new IsagNotificationSetting();
         isagNotificationSettingList = dao.findEntityByQuery("SELECT INS FROM IsagNotificationSetting INS WHERE INS.branchId.id = :branchid AND INS.createdBy.id = :createdBy", params);
       
         return isagNotificationSettingList != null ? initMapToDTO(isagNotificationSettingList) : new IsagNotificationSettingDTO(); 
    }
    
    private List<IsagNotificationSettingDTO> returnListDTO(List<IsagNotificationSetting> list) {
        List<IsagNotificationSettingDTO> dTOList = new ArrayList<>();
        for (IsagNotificationSetting entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private IsagNotificationSettingDTO mapTODTO(IsagNotificationSetting entity) {
        IsagNotificationSettingDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private IsagNotificationSettingDTO initMapToDTO(IsagNotificationSetting entity) {

        IsagNotificationSettingDTO dTO = new IsagNotificationSettingDTO();

        dTO.setPosition(entity.getPosition());
        dTO.setTimeout(entity.getTimeout());
        dTO.setTheme(entity.getTheme());
        dTO.setSound(entity.getSound());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        dTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dTO.setCreatedDate(entity.getCreationDate());
        return dTO;
    }

    private IsagNotificationSetting mapFromDTO(IsagNotificationSettingDTO dTO, IsagUser isagUser) {
        IsagNotificationSetting entity = new IsagNotificationSetting();

        entity.setPosition(dTO.getPosition());
        entity.setTimeout(dTO.getTimeout());
        entity.setTheme(dTO.getTheme());
        entity.setSound(dTO.getSound());
         
        
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
        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;

    }
}

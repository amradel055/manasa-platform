/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.TobyNotificationSettingDTO;
import com.toby.entity.TobyNotificationSetting;
import com.toby.entity.TobyUser;
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
public class TobyNotificationSettingServiceImpl implements TobyNotificationSettingService {

    @EJB
    private GenericDAO dao;

    
    
     @Override
    public TobyNotificationSettingDTO addTobyNotificationSettingDTO(TobyNotificationSettingDTO tobyNotificationSettingDTO, TobyUser tobyUser) {
        TobyNotificationSetting entity = mapFromDTO(tobyNotificationSettingDTO, tobyUser);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity);
    }
    
    @Override
    public List<TobyNotificationSettingDTO> getAlltobyNotificationSettingDTO(Integer branchid) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", branchid);
        List<TobyNotificationSetting> tobyNotificationSettingList = dao.findListByQuery("SELECT INS FROM TobyNotificationSetting INS WHERE INS.branchId.id = :branchid", params);
        return returnListDTO(tobyNotificationSettingList);
    }
     
     @Override
    public TobyNotificationSettingDTO getTobyNotificationSettingDTOByUserId(Integer branchId , Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", branchId); 
        params.put("createdBy", userId);
         TobyNotificationSetting tobyNotificationSettingList = new TobyNotificationSetting();
         tobyNotificationSettingList = dao.findEntityByQuery("SELECT INS FROM TobyNotificationSetting INS WHERE INS.branchId.id = :branchid AND INS.createdBy.id = :createdBy", params);
       
         return tobyNotificationSettingList != null ? initMapToDTO(tobyNotificationSettingList) : new TobyNotificationSettingDTO(); 
    }
    
    private List<TobyNotificationSettingDTO> returnListDTO(List<TobyNotificationSetting> list) {
        List<TobyNotificationSettingDTO> dTOList = new ArrayList<>();
        for (TobyNotificationSetting entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private TobyNotificationSettingDTO mapTODTO(TobyNotificationSetting entity) {
        TobyNotificationSettingDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private TobyNotificationSettingDTO initMapToDTO(TobyNotificationSetting entity) {

        TobyNotificationSettingDTO dTO = new TobyNotificationSettingDTO();

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

    private TobyNotificationSetting mapFromDTO(TobyNotificationSettingDTO dTO, TobyUser tobyUser) {
        TobyNotificationSetting entity = new TobyNotificationSetting();

        entity.setPosition(dTO.getPosition());
        entity.setTimeout(dTO.getTimeout());
        entity.setTheme(dTO.getTheme());
        entity.setSound(dTO.getSound());
         
        
        if (dTO.getId() == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);

        }
        entity.setId(dTO.getId());
        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;

    }
}

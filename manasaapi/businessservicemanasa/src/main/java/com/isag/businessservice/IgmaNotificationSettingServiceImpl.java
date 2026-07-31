/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IgmaNotificationSettingDTO;
import com.isag.entity.IgmaNotificationSetting;
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
 * @author ahmedEssam
 */
@Stateless
public class IgmaNotificationSettingServiceImpl implements IgmaNotificationSettingService {

    @EJB
    private GenericDAO dao;

    
    
     @Override
    public IgmaNotificationSettingDTO addIsagNotificationSettingDTO(IgmaNotificationSettingDTO isagNotificationSettingDTO, IgmaUser isagUser) {
        IgmaNotificationSetting entity = mapFromDTO(isagNotificationSettingDTO, isagUser);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity);
    }
    
    @Override
    public List<IgmaNotificationSettingDTO> getAllisagNotificationSettingDTO(Integer branchid) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", branchid);
        List<IgmaNotificationSetting> isagNotificationSettingList = dao.findListByQuery("SELECT INS FROM IsagNotificationSetting INS WHERE INS.branchId.id = :branchid", params);
        return returnListDTO(isagNotificationSettingList);
    }
     
     @Override
    public IgmaNotificationSettingDTO getIsagNotificationSettingDTOByUserId(Integer branchId , Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", branchId); 
        params.put("createdBy", userId);
         IgmaNotificationSetting isagNotificationSettingList = new IgmaNotificationSetting();
         isagNotificationSettingList = dao.findEntityByQuery("SELECT INS FROM IsagNotificationSetting INS WHERE INS.branchId.id = :branchid AND INS.createdBy.id = :createdBy", params);
       
         return isagNotificationSettingList != null ? initMapToDTO(isagNotificationSettingList) : new IgmaNotificationSettingDTO(); 
    }
    
    private List<IgmaNotificationSettingDTO> returnListDTO(List<IgmaNotificationSetting> list) {
        List<IgmaNotificationSettingDTO> dTOList = new ArrayList<>();
        for (IgmaNotificationSetting entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private IgmaNotificationSettingDTO mapTODTO(IgmaNotificationSetting entity) {
        IgmaNotificationSettingDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private IgmaNotificationSettingDTO initMapToDTO(IgmaNotificationSetting entity) {

        IgmaNotificationSettingDTO dTO = new IgmaNotificationSettingDTO();

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

    private IgmaNotificationSetting mapFromDTO(IgmaNotificationSettingDTO dTO, IgmaUser isagUser) {
        IgmaNotificationSetting entity = new IgmaNotificationSetting();

        entity.setPosition(dTO.getPosition());
        entity.setTimeout(dTO.getTimeout());
        entity.setTheme(dTO.getTheme());
        entity.setSound(dTO.getSound());
         
        
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
        entity.setId(dTO.getId());
        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.CrmSettingDTO;
import com.isag.entity.CrmSetting;
import com.isag.entity.IsagUser;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmedEssam
 */
@Stateless
public class CrmSettingServiceImp implements CrmSettingService {

    @EJB
    private GenericDAO dao;

    @Override
    public CrmSettingDTO addCrmSettingDTO(CrmSettingDTO crmSettingDTO, IsagUser isagUser) {
        CrmSetting entity = mapFromDTO(crmSettingDTO, isagUser);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity);
    }

    @Override
    public CrmSettingDTO getCrmSettingDTOByUserId(Integer branchId, Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", branchId);
        params.put("createdBy", userId);
        CrmSetting crmSetting = new CrmSetting();
        crmSetting = dao.findEntityByQuery("SELECT c FROM CrmSetting c WHERE c.branchId.id = :branchid AND c.createdBy.id = :createdBy", params);

        return crmSetting != null ? initMapToDTO(crmSetting) : new CrmSettingDTO();

    }

    private CrmSettingDTO mapTODTO(CrmSetting entity) {
        CrmSettingDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private CrmSettingDTO initMapToDTO(CrmSetting entity) {

        CrmSettingDTO dTO = new CrmSettingDTO();
        dTO.setShowEvent(entity.getShowEvent());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        dTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dTO.setCreatedDate(entity.getCreationDate());
        return dTO;
    }

    private CrmSetting mapFromDTO(CrmSettingDTO dTO, IsagUser isagUser) {
        CrmSetting entity = new CrmSetting();

        entity.setShowEvent(dTO.getShowEvent());

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

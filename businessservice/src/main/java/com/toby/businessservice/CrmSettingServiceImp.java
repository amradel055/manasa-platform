/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.CrmSettingDTO;
import com.toby.entity.CrmSetting;
import com.toby.entity.TobyUser;
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
    public CrmSettingDTO addCrmSettingDTO(CrmSettingDTO crmSettingDTO, TobyUser tobyUser) {
        CrmSetting entity = mapFromDTO(crmSettingDTO, tobyUser);
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

    private CrmSetting mapFromDTO(CrmSettingDTO dTO, TobyUser tobyUser) {
        CrmSetting entity = new CrmSetting();

        entity.setShowEvent(dTO.getShowEvent());

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

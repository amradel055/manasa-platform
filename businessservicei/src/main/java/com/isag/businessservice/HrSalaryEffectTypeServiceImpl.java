/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.HrSalaryEffectTypeDTO;
import com.isag.entity.HrSalaryEffectType;
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
 * @author AhmedEssam
 */
@Stateless
public class HrSalaryEffectTypeServiceImpl implements HrSalaryEffectTypeService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public List<HrSalaryEffectTypeDTO> findAllSalaryEffectTypeByBranchId(Integer BranchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("BranchId", BranchId);
        List<HrSalaryEffectType> hrSalaryEffectTypeList = dao.findListByQuery("SELECT h FROM HrSalaryEffectType h WHERE h.branchId.id = :BranchId ", params);
        List<HrSalaryEffectTypeDTO> dTOList = new ArrayList<>();
        returnListDTO(hrSalaryEffectTypeList, dTOList);
        return dTOList;
    }

    @Override
    public List<HrSalaryEffectTypeDTO> getAllHrSalaryEffectTypeDTOByCompanyId(Integer companyId,Integer type) {
        Map<String, Object> params = new HashMap<>();
        List<HrSalaryEffectTypeDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        params.put("type", type);
        List<HrSalaryEffectType> list = dao.findListByQuery("SELECT h FROM HrSalaryEffectType h WHERE h.companyId.id=:companyId AND h.effectType = :type", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public List<HrSalaryEffectTypeDTO> findExistSalaryEffectType(Integer BranchId, HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO) {
        List<HrSalaryEffectTypeDTO> dTOList = new ArrayList<>();
        if (hrSalaryEffectTypeDTO != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("type", Integer.valueOf(hrSalaryEffectTypeDTO.getType()));
            params.put("name", hrSalaryEffectTypeDTO.getName());
            params.put("effectType", Integer.valueOf(hrSalaryEffectTypeDTO.getEffectType()));
            params.put("BranchId", BranchId);
            List<HrSalaryEffectType> hrSalaryEffectTypeList = dao.findListByQuery("SELECT h FROM HrSalaryEffectType h WHERE h.type = :type  AND h.name = :name AND h.effectType = :effectType AND h.branchId.id = :BranchId ", params);
            returnListDTO(hrSalaryEffectTypeList, dTOList);
        }
        return dTOList;
    }

    @Override
    public HrSalaryEffectTypeDTO findSalaryEffectTypeDto(Integer id) {
        HrSalaryEffectType hrSalaryEffectType = dao.findEntityById(HrSalaryEffectType.class, id);
        HrSalaryEffectTypeDTO dTO = new HrSalaryEffectTypeDTO();
        return mapTODTO(hrSalaryEffectType, dTO);
    }

    @Override
    public HrSalaryEffectTypeDTO addSalaryEffectType(HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO, IsagUser isagUser) {
        HrSalaryEffectType entity = mapFromDTO(hrSalaryEffectTypeDTO, isagUser);
        entity = dao.updateEntity(entity);
        hrSalaryEffectTypeDTO = mapTODTO(entity, hrSalaryEffectTypeDTO);
        return hrSalaryEffectTypeDTO;
    }

    @Override
    public void deleteSalaryEffectType(HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO, IsagUser isagUser) {
        HrSalaryEffectType entity = new HrSalaryEffectType();
        entity.setId(hrSalaryEffectTypeDTO.getId());
        dao.deleteEntity(entity);
    }

    public HrSalaryEffectTypeDTO mapTODTO(HrSalaryEffectType entity, HrSalaryEffectTypeDTO dTO) {
        if (dTO == null) {
            dTO = new HrSalaryEffectTypeDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(HrSalaryEffectTypeDTO dTO, HrSalaryEffectType entity) {
        if (entity.getType() != null) {
            switch (entity.getType()) {
                case 0:
                    dTO.setTypeText("ايجابي");
                    break;
                case 1:
                    dTO.setTypeText("سلبي");
                    break;
                default:
                    dTO.setTypeText(null);
                    break;
            }
             dTO.setType(entity.getType());
        }

        if (entity.getEffectType() != null) {
            switch (entity.getEffectType()) {
                case 0:
                    dTO.setEffectTypeText("استحقاق");
                    break;
                case 1:
                    dTO.setEffectTypeText("استقطاع");
                    break;
                default:
                    dTO.setEffectTypeText(null);
                    break;
            }
           dTO.setEffectType(entity.getEffectType());
        }

        dTO.setName(entity.getName());
        dTO.setId(entity.getId());
        dTO.setSerial(entity.getSerial());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

    }

    public HrSalaryEffectType mapFromDTO(HrSalaryEffectTypeDTO dTO, IsagUser isagUser) {
        HrSalaryEffectType entity = new HrSalaryEffectType();

        entity.setType(Integer.valueOf(dTO.getType()));
        entity.setEffectType(Integer.valueOf(dTO.getEffectType()));
        entity.setId(dTO.getId());
        entity.setName(dTO.getName());

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        if (entity.getId() == null) {
            Integer serial = genericService.getMaxSerialForInvoice(HrSalaryEffectType.class, entity.getBranchId().getId(), null, null);
            entity.setSerial(serial);
        }
        return entity;
    }

    private void returnListDTO(List<HrSalaryEffectType> list, List<HrSalaryEffectTypeDTO> dTOList) {
        for (HrSalaryEffectType entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }
}

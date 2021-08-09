/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.HrSalariesStructureEffectDTO;
import com.isag.dto.HrSalaryEffectDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.HrSalariesStructureEffect;
import com.isag.entity.IsagUser;
import com.isag.entity.HrSalaryEffect;
import com.isag.entity.Symbol;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class HrSalariesEffectServiceImpl implements HrSalariesEffectService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteSalariesEffect(HrSalariesStructureEffectDTO hrSalariesEffectDTO, IsagUser isagUser) {
        HrSalaryEffect entity = new HrSalaryEffect();
        entity.setId(hrSalariesEffectDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public HrSalariesStructureEffectDTO addSalariesEffect(HrSalariesStructureEffectDTO hrSalariesEffectDTO, IsagUser isagUser) {
        HrSalariesStructureEffect entity = mapFromDTO(hrSalariesEffectDTO, isagUser);
        entity = dao.updateEntity(entity);
        hrSalariesEffectDTO = mapTODTO(entity, hrSalariesEffectDTO);

        return hrSalariesEffectDTO;
    }

    @Override
    public HrSalariesStructureEffectDTO findSalariesEffect(Integer id) {
        HrSalariesStructureEffect effectType = dao.findEntityById(HrSalariesStructureEffect.class, id);
        HrSalariesStructureEffectDTO dTO = new HrSalariesStructureEffectDTO();
        return mapTODTO(effectType, dTO);
    }

    public HrSalariesStructureEffectDTO mapTODTO(HrSalariesStructureEffect entity, HrSalariesStructureEffectDTO dTO) {
        if (dTO == null) {
            dTO = new HrSalariesStructureEffectDTO();
        }
        initMapToDTO(dTO, entity);

        return dTO;
    }

    private void initMapToDTO(HrSalariesStructureEffectDTO dTO, HrSalariesStructureEffect entity) {

        if (entity.getUnitType() != null && entity.getUnitType().getId() != null) {
            SymbolDTO unitTypeDTO = new SymbolDTO();
            unitTypeDTO.setName(entity.getUnitType().getName());
            unitTypeDTO.setId(entity.getUnitType().getId());
            dTO.setUnitType(unitTypeDTO);
        }

        if (entity.getEffectType() != null && entity.getEffectType().getId() != null) {
            HrSalaryEffectDTO salaryEffectTypeDTO = new HrSalaryEffectDTO();
            salaryEffectTypeDTO.setId(entity.getEffectType().getId());
            salaryEffectTypeDTO.setName(entity.getEffectType().getName());

            dTO.setEffectType(salaryEffectTypeDTO);
        }
        dTO.setSalaryStructureId(entity.getSalaryStructureId());
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());
        dTO.setEffectDate(entity.getEffectDate());
        dTO.setStatus(entity.getStatus());
        dTO.setUnitEffectCount(entity.getUnitEffectCount());

        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public HrSalariesStructureEffect mapFromDTO(HrSalariesStructureEffectDTO dTO, IsagUser isagUser) {
        HrSalariesStructureEffect entity = new HrSalariesStructureEffect();
        if (dTO.getUnitType() != null && dTO.getUnitType().getId() != null) {
            Symbol unitType = new Symbol();
            unitType.setId(dTO.getUnitType().getId());
            unitType.setName(dTO.getUnitType().getName());
            entity.setUnitType(unitType);
        }

        if (dTO.getEffectType() != null && dTO.getEffectType().getId() != null) {
            HrSalaryEffect salaryEffect = new HrSalaryEffect();
            salaryEffect.setId(dTO.getEffectType().getId());
            salaryEffect.setName(dTO.getEffectType().getName());

            entity.setEffectType(salaryEffect);
        }
        entity.setSalaryStructureId(dTO.getSalaryStructureId());
        entity.setId(dTO.getId());
        entity.setEffectDate(dTO.getEffectDate());
        entity.setUnitEffectCount(dTO.getUnitEffectCount());
        entity.setStatus(dTO.getStatus());

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

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<HrSalariesStructureEffect> list, List<HrSalariesStructureEffectDTO> dTOList) {
        for (HrSalariesStructureEffect entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    @Override
    public List<HrSalariesStructureEffectDTO> addHrSalariesEffectList(List<HrSalariesStructureEffectDTO> hrSalariesEffectDTOList, IsagUser isagUser, Integer salaryStructureId) {
        List<HrSalariesStructureEffectDTO> hrSalariesEffectDTOListReturnDTO = new ArrayList<>();
        for (HrSalariesStructureEffectDTO hrSalariesEffectDTO : hrSalariesEffectDTOList) {
            hrSalariesEffectDTO.setSalaryStructureId(salaryStructureId);
            HrSalariesStructureEffect hrEmpQualifications = mapFromDTO(hrSalariesEffectDTO, isagUser);
            hrEmpQualifications = dao.updateEntity(hrEmpQualifications);
            hrSalariesEffectDTO = mapTODTO(hrEmpQualifications, hrSalariesEffectDTO);
            hrSalariesEffectDTOListReturnDTO.add(hrSalariesEffectDTO);
        }

        return hrSalariesEffectDTOListReturnDTO;
    }

    @Override
    public List<HrSalariesStructureEffectDTO> findSalariesEffectById(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<HrSalariesStructureEffect> hrSalariesEffectList = dao.findListByQuery("SELECT d FROM HrSalariesStructureEffect d WHERE salaryStructureId=:id", params);
        List<HrSalariesStructureEffectDTO> hrSalariesEffectDTOList = new ArrayList<>();
        returnListDTO(hrSalariesEffectList, hrSalariesEffectDTOList);
        return hrSalariesEffectDTOList;
    }

    @Override
    public List<HrSalariesStructureEffectDTO> findSalariesEffectByEmployeeId(Integer employeeId) {
        Map<String, Object> params = new HashMap();
        params.put("employeeId", employeeId);

        List<HrSalariesStructureEffect> hrSalariesEffectList = dao.findListByQuery("SELECT d FROM HrSalariesStructureEffect d WHERE d.SalaryStructureId=:employeeId", params);
        List<HrSalariesStructureEffectDTO> hrSalariesEffectDTOList = new ArrayList<>();
        returnListDTO(hrSalariesEffectList, hrSalariesEffectDTOList);
        return hrSalariesEffectDTOList;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.HrSalaryEffectDTO;
import com.isag.dto.HrSalaryEffectTypeDTO;
import com.isag.dto.HrSalaryStructureAccuralDeductionDTO;
import com.isag.dto.HrSalaryStructureAccuralDeductionEffectTypeDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.HrSalaryEffect;
import com.isag.entity.HrSalaryEffectType;
import com.isag.entity.HrSalaryStructureAccuralDeduction;
import com.isag.entity.HrSalaryStructureAccuralDeductionEffectType;
import com.isag.entity.IsagUser;
import com.isag.entity.RscAccrualDeduction;
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
public class HrSalaryStructureAccuralDeductionEffectTypeServiceImpl implements HrSalaryStructureAccuralDeductionEffectTypeService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> findAllSalaryStructureAccuralDeductionEffectTypeByBranchId(Integer BranchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("BranchId", BranchId);
        List<HrSalaryStructureAccuralDeductionEffectType> accuralDeductionEffectTypes = dao.findListByQuery("SELECT h FROM HrSalaryStructureAccuralDeductionEffectType h WHERE h.branchId.id = :BranchId ", params);
        List<HrSalaryStructureAccuralDeductionEffectTypeDTO> dTOList = new ArrayList<>();
        returnListDTO(accuralDeductionEffectTypes, dTOList);
        return dTOList;
    }

    @Override
    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> getAllHrSalaryStructureAccuralDeductionEffectTypeByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<HrSalaryStructureAccuralDeductionEffectTypeDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<HrSalaryStructureAccuralDeductionEffectType> list = dao.findListByQuery("SELECT h FROM HrSalaryStructureAccuralDeductionEffectType h WHERE h.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> findAllSalaryStructureAccuralDeductionEffectTypeByBranchId(Integer BranchId, Integer SalaryStructureAccuralDeduction, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("BranchId", BranchId);
        params.put("SalaryStructureAccuralDeduction", SalaryStructureAccuralDeduction);
        params.put("type", type);
        List<HrSalaryStructureAccuralDeductionEffectType> accuralDeductionEffectTypes = dao.findListByQuery("SELECT h FROM HrSalaryStructureAccuralDeductionEffectType h WHERE h.branchId.id = :BranchId AND h.hrSalaryStructureAccuralDeduction.id = :SalaryStructureAccuralDeduction AND h.hrSalaryStructureAccuralDeduction.accuralDeductionId.type =:type", params);
        List<HrSalaryStructureAccuralDeductionEffectTypeDTO> dTOList = new ArrayList<>();
        returnListDTO(accuralDeductionEffectTypes, dTOList);
        return dTOList;
    }

    @Override
    public HrSalaryStructureAccuralDeductionEffectTypeDTO findHrSalaryStructureAccuralDeductionEffectTypeDto(Integer id) {
        HrSalaryStructureAccuralDeductionEffectType accuralDeductionEffectType = dao.findEntityById(HrSalaryStructureAccuralDeductionEffectType.class, id);
        HrSalaryStructureAccuralDeductionEffectTypeDTO dTO = new HrSalaryStructureAccuralDeductionEffectTypeDTO();
        return mapTODTO(accuralDeductionEffectType, dTO);
    }

    @Override
    public HrSalaryStructureAccuralDeductionEffectTypeDTO addHrSalaryStructureAccuralDeductionEffectType(HrSalaryStructureAccuralDeductionEffectTypeDTO accuralDeductionEffectTypeDTO, IsagUser isagUser) {
        HrSalaryStructureAccuralDeductionEffectType entity = mapFromDTO(accuralDeductionEffectTypeDTO, isagUser);
        entity = dao.updateEntity(entity);
        accuralDeductionEffectTypeDTO = mapTODTO(entity, accuralDeductionEffectTypeDTO);
        return accuralDeductionEffectTypeDTO;
    }

    @Override
    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> addHrSalaryStructureAccuralDeductionEffectTypeList(List<HrSalaryStructureAccuralDeductionEffectTypeDTO> accuralDeductionEffectTypeDTOList, Integer hrSalaryStructureAccuralDeductionDtoID, IsagUser isagUser) {
        List<HrSalaryStructureAccuralDeductionEffectTypeDTO> list = new ArrayList<>();
        if (accuralDeductionEffectTypeDTOList != null || !accuralDeductionEffectTypeDTOList.isEmpty()) {
            for (HrSalaryStructureAccuralDeductionEffectTypeDTO hrSalaryStructureAccuralDeductionEffectTypeDTO : accuralDeductionEffectTypeDTOList) {
                HrSalaryStructureAccuralDeductionDTO accuralDeductionDTO = new HrSalaryStructureAccuralDeductionDTO();
                accuralDeductionDTO.setId(hrSalaryStructureAccuralDeductionDtoID);
                hrSalaryStructureAccuralDeductionEffectTypeDTO.setHrSalaryStructureAccuralDeduction(accuralDeductionDTO);
                list.add(addHrSalaryStructureAccuralDeductionEffectType(hrSalaryStructureAccuralDeductionEffectTypeDTO, isagUser));
            }
        }
        return list;
    }

    @Override
    public void deleteSalaryAccuralDeductionEffectTypeDTO(HrSalaryStructureAccuralDeductionEffectTypeDTO accuralDeductionEffectTypeDTO, IsagUser isagUser) {
        HrSalaryStructureAccuralDeductionEffectType entity = new HrSalaryStructureAccuralDeductionEffectType();
        entity.setId(accuralDeductionEffectTypeDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteListSalaryAccuralDeductionEffectTypeDTO(List<HrSalaryStructureAccuralDeductionEffectTypeDTO> accuralDeductionEffectTypeDTOList, IsagUser isagUser) {
        if (accuralDeductionEffectTypeDTOList != null || !accuralDeductionEffectTypeDTOList.isEmpty()) {
            for (HrSalaryStructureAccuralDeductionEffectTypeDTO hrSalaryStructureAccuralDeductionEffectTypeDTO : accuralDeductionEffectTypeDTOList) {
                deleteSalaryAccuralDeductionEffectTypeDTO(hrSalaryStructureAccuralDeductionEffectTypeDTO, isagUser);
            }
        }
    }
    
    @Override
    public List<String> getAccuralDeductionCode(Integer hrSalaryEffect , Integer salaryStructureId , Integer accuralDeductionType) {
        Map<String, Object> params = new HashMap<>();
        List<String> accuralDeductionCode = new ArrayList<>();
        params.put("hrSalaryEffect", hrSalaryEffect);
        params.put("salaryStructureId", salaryStructureId);
        params.put("accuralDeductionType", accuralDeductionType);
        List<RscAccrualDeduction> list = dao.findListByQuery("SELECT h.hrSalaryStructureAccuralDeduction.accuralDeductionId FROM HrSalaryStructureAccuralDeductionEffectType h WHERE h.hrSalaryEffect.id = :hrSalaryEffect AND h.hrSalaryStructureAccuralDeduction.salaryStructureId.id = :salaryStructureId AND h.hrSalaryStructureAccuralDeduction.accuralDeductionId.type = :accuralDeductionType ", params);
        for(RscAccrualDeduction accrualDeduction : list){
            accuralDeductionCode.add(accrualDeduction.getCode());
        }
        return accuralDeductionCode;
    }

    public HrSalaryStructureAccuralDeductionEffectTypeDTO mapTODTO(HrSalaryStructureAccuralDeductionEffectType entity, HrSalaryStructureAccuralDeductionEffectTypeDTO dTO) {
        if (dTO == null) {
            dTO = new HrSalaryStructureAccuralDeductionEffectTypeDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(HrSalaryStructureAccuralDeductionEffectTypeDTO dTO, HrSalaryStructureAccuralDeductionEffectType entity) {

        dTO.setActive(entity.getActive());
        dTO.setId(entity.getId());
        dTO.setSerial(entity.getSerial());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setType(entity.getHrSalaryStructureAccuralDeduction() != null ? (entity.getHrSalaryStructureAccuralDeduction().getAccuralDeductionId() != null ? entity.getHrSalaryStructureAccuralDeduction().getAccuralDeductionId().getType() : null) : null);
        if (entity.getHrSalaryEffectType() != null) {
            HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO = new HrSalaryEffectTypeDTO();
            hrSalaryEffectTypeDTO.setId(entity.getHrSalaryEffectType().getId());
            hrSalaryEffectTypeDTO.setName(entity.getHrSalaryEffectType().getName());
            hrSalaryEffectTypeDTO.setSerial(entity.getHrSalaryEffectType().getSerial());
            hrSalaryEffectTypeDTO.setIndex(entity.getHrSalaryEffectType().getId());
            dTO.setHrSalaryEffectType(hrSalaryEffectTypeDTO);
        }
        if (entity.getHrSalaryEffect() != null) {
            HrSalaryEffectDTO hrSalaryEffectDTO = new HrSalaryEffectDTO();
            hrSalaryEffectDTO.setId(entity.getHrSalaryEffect().getId());
            hrSalaryEffectDTO.setName(entity.getHrSalaryEffect().getName());
            hrSalaryEffectDTO.setCode(entity.getHrSalaryEffect().getCode());
            hrSalaryEffectDTO.setIndex(entity.getHrSalaryEffect().getId());
            hrSalaryEffectDTO.setCofficientPerDay(entity.getHrSalaryEffect().getCofficientPerDay());
            hrSalaryEffectDTO.setCofficientPerHour(entity.getHrSalaryEffect().getCofficientPerHour());
            hrSalaryEffectDTO.setImpactIndicator(entity.getHrSalaryEffect().getImpactIndicator());
            if (entity.getHrSalaryEffect().getType() != null) {
                SymbolDTO symbolDTO = new SymbolDTO();
                symbolDTO.setId(entity.getHrSalaryEffect().getType().getId());
                symbolDTO.setName(entity.getHrSalaryEffect().getType().getName());
                symbolDTO.setSerial(entity.getHrSalaryEffect().getType().getSerial());
                hrSalaryEffectDTO.setType(symbolDTO);
            }

            dTO.setHrSalaryEffectDTO(hrSalaryEffectDTO);
        }

        if (entity.getHrSalaryStructureAccuralDeduction() != null) {
            HrSalaryStructureAccuralDeductionDTO hrSalaryStructureAccuralDeductionDTO = new HrSalaryStructureAccuralDeductionDTO();
            hrSalaryStructureAccuralDeductionDTO.setId(entity.getHrSalaryStructureAccuralDeduction().getId());
            dTO.setHrSalaryStructureAccuralDeduction(hrSalaryStructureAccuralDeductionDTO);
        }
    }

    public HrSalaryStructureAccuralDeductionEffectType mapFromDTO(HrSalaryStructureAccuralDeductionEffectTypeDTO dTO, IsagUser isagUser) {
        HrSalaryStructureAccuralDeductionEffectType entity = new HrSalaryStructureAccuralDeductionEffectType();

        entity.setActive(dTO.getActive());
        entity.setId(dTO.getId());

        if (dTO.getHrSalaryEffectType() != null) {
            HrSalaryEffectType hrSalaryEffectType = new HrSalaryEffectType();
            hrSalaryEffectType.setId(dTO.getHrSalaryEffectType().getId());
            entity.setHrSalaryEffectType(hrSalaryEffectType);
        }
        if (dTO.getHrSalaryEffectDTO()!= null) {
            HrSalaryEffect hrSalaryEffect = new HrSalaryEffect();
            hrSalaryEffect.setId(dTO.getHrSalaryEffectDTO().getId());
            entity.setHrSalaryEffect(hrSalaryEffect);
        }

        if (dTO.getHrSalaryStructureAccuralDeduction() != null) {
            HrSalaryStructureAccuralDeduction hrSalaryStructureAccuralDeduction = new HrSalaryStructureAccuralDeduction();
            hrSalaryStructureAccuralDeduction.setId(dTO.getHrSalaryStructureAccuralDeduction().getId());
            entity.setHrSalaryStructureAccuralDeduction(hrSalaryStructureAccuralDeduction);
        }
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

    private void returnListDTO(List<HrSalaryStructureAccuralDeductionEffectType> list, List<HrSalaryStructureAccuralDeductionEffectTypeDTO> dTOList) {
        for (HrSalaryStructureAccuralDeductionEffectType entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }
}

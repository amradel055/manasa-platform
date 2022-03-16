/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GlAccountDTO;
import com.isag.dto.HrSalaryEffectDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.GlAccount;
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
public class HrSalaryEffectServiceImpl implements HrSalaryEffectService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteSalaryEffectType(HrSalaryEffectDTO salaryEffectDTO) {
        HrSalaryEffect entity = new HrSalaryEffect();
        entity.setId(salaryEffectDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public HrSalaryEffectDTO addSalaryEffectType(HrSalaryEffectDTO salaryEffectDTO, IsagUser isagUser) {
        HrSalaryEffect entity = mapFromDTO(salaryEffectDTO, isagUser);
        entity = dao.updateEntity(entity);
        salaryEffectDTO = mapTODTO(entity, salaryEffectDTO);

        return salaryEffectDTO;
    }

    @Override
    public HrSalaryEffectDTO findSalaryEffect(Integer id) {
        HrSalaryEffect effectType = dao.findEntityById(HrSalaryEffect.class, id);
        HrSalaryEffectDTO dTO = new HrSalaryEffectDTO();
        return mapTODTO(effectType, dTO);
    }
    
    @Override
    public List<HrSalaryEffectDTO> getAllSalariesEffectTypeByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<HrSalaryEffectDTO> dTOList = new ArrayList<>();
        List<HrSalaryEffect> list = dao.findListByQuery("SELECT d FROM HrSalaryEffect d WHERE d.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
      @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(h) FROM HrSalaryEffect h WHERE h.branchId.id = :branchId  ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        
        return details;
    }
    
     private void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and  (h.employeeId.name  like CONCAT('%',:globalFilter ,'%')) OR (h.employeeId.designation.name like CONCAT('%',:globalFilter ,'%') OR (h.employeeId.departmentId.name like CONCAT('%',:globalFilter ,'%')) OR (h.employeeId.active like CONCAT('%',:globalFilter ,'%'))";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }
     
      @Override
    public List<HrSalaryEffectDTO> getALLHrSalaryEffectByBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT h FROM HrSalaryEffect h WHERE h.branchId.id = :branchId   ");
        appendFilter(filters, branchId, builder);
        builder.append(" ORDER BY h.id DESC");
        List<HrSalaryEffect> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<HrSalaryEffectDTO> dTOList = new ArrayList<>();
        returnListDTO(details, dTOList);
        return dTOList;
    }
    
      @Override
    public List<HrSalaryEffectDTO> getALLHrSalaryEffectByBranchId(Integer branchId) {
        
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        builder.append("SELECT h FROM HrSalaryEffect h WHERE h.branchId.id = :branchId   ");
        builder.append(" ORDER BY h.id DESC");
        List<HrSalaryEffect> details = dao.findListByQuery(builder.toString(),params);
        List<HrSalaryEffectDTO> dTOList = new ArrayList<>();
        returnListDTO(details, dTOList);
        return dTOList;
    }

    public HrSalaryEffectDTO mapTODTO(HrSalaryEffect entity, HrSalaryEffectDTO dTO) {
        if (dTO == null) {
            dTO = new HrSalaryEffectDTO();
        }
        initMapToDTO(dTO, entity);

        return dTO;
    }

    private void initMapToDTO(HrSalaryEffectDTO dTO, HrSalaryEffect entity) {
        if (entity.getType() != null && entity.getType().getId() != null) {
            SymbolDTO typeDTO = new SymbolDTO();
            typeDTO.setName(entity.getType().getName());
            typeDTO.setId(entity.getType().getId());
            dTO.setType(typeDTO);
        }
        if (entity.getGlAccountId() != null && entity.getGlAccountId().getId() != null) {
            GlAccountDTO glAccountDTO = new GlAccountDTO();
            glAccountDTO.setName(entity.getGlAccountId().getName());
            glAccountDTO.setId(entity.getGlAccountId().getId());
            dTO.setGlAccountId(glAccountDTO);
        }
        dTO.setName(entity.getName());
        dTO.setId(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setCofficientPerDay(entity.getCofficientPerDay());
        dTO.setCofficientPerHour(entity.getCofficientPerHour());
        dTO.setImpactIndicator(entity.getImpactIndicator());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public HrSalaryEffect mapFromDTO(HrSalaryEffectDTO dTO, IsagUser isagUser) {
        HrSalaryEffect entity = new HrSalaryEffect();
        if (dTO.getType() != null && dTO.getType().getId() != null) {
            Symbol type = new Symbol();
            type.setId(dTO.getType().getId());
            type.setName(dTO.getType().getName());
            entity.setType(type);
        }
        if (dTO.getGlAccountId() != null && dTO.getGlAccountId().getId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(dTO.getGlAccountId().getId());
            glAccount.setName(dTO.getGlAccountId().getName());
            entity.setGlAccountId(glAccount);
        }

        entity.setId(dTO.getId());
        entity.setName(dTO.getName());
        entity.setCode(dTO.getCode());
        entity.setCofficientPerDay(dTO.getCofficientPerDay());
        entity.setCofficientPerHour(dTO.getCofficientPerHour());
        entity.setImpactIndicator(dTO.getImpactIndicator());

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

    private void returnListDTO(List<HrSalaryEffect> list, List<HrSalaryEffectDTO> dTOList) {
        for (HrSalaryEffect entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

}

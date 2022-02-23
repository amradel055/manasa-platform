/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.HrSalaryStructureAccuralDeductionDTO;
import com.isag.dto.HrSalaryStructureDTO;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.entity.Employee;
import com.isag.entity.HrSalaryStructureAccuralDeduction;
import com.isag.entity.HrSalaryStructure;
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
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class HrSalaryStructureAccuralDeductionServiceImpl implements HrSalaryStructureAccuralDeductionService {

    @EJB
    private GenericDAO dao;
    @EJB
    HrSalaryStructureService hrSalaryStructureService;
    @EJB
    HrSalaryStructureAccuralDeductionEffectTypeService hrSalaryStructureAccuralDeductionEffectTypeService;

    @Override
    public void deleteHrEmployeeAccuralDeduction(HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTO, IsagUser isagUser) {

        HrSalaryStructureAccuralDeduction entity = new HrSalaryStructureAccuralDeduction();
        entity.setId(hrEmployeeAccuralDeductionDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public List<HrSalaryStructureAccuralDeductionDTO> addHrEmployeeAccuralDeductionList(List<HrSalaryStructureAccuralDeductionDTO> hrEmployeeAccuralDeductionDTOList, IsagUser isagUser, Integer salaryStructureId) {
        // a method which returns a list from EmployeeAccuralDeduction and prepare it to be saved.

        List<HrSalaryStructureAccuralDeductionDTO> hrEmployeeAccuralDeductionDTOReturn = new ArrayList<>();
        for (HrSalaryStructureAccuralDeductionDTO accuralDeductionDTO : hrEmployeeAccuralDeductionDTOList) {
            accuralDeductionDTO.setSalaryStructureId(hrSalaryStructureService.findSalaryStructure(salaryStructureId));
            HrSalaryStructureAccuralDeduction hrEmployeeAccuralDeduction = mapFromDTO(accuralDeductionDTO, isagUser);
            hrEmployeeAccuralDeduction = dao.updateEntity(hrEmployeeAccuralDeduction);

            if (accuralDeductionDTO.getHrSalaryStructureAccuralEffectType() != null || !accuralDeductionDTO.getHrSalaryStructureAccuralEffectType().isEmpty()) {
                hrSalaryStructureAccuralDeductionEffectTypeService.addHrSalaryStructureAccuralDeductionEffectTypeList(accuralDeductionDTO.getHrSalaryStructureAccuralEffectType(), hrEmployeeAccuralDeduction.getId(), isagUser);
            }
            if (accuralDeductionDTO.getHrSalaryStructureDeductionEffectType() != null || !accuralDeductionDTO.getHrSalaryStructureDeductionEffectType().isEmpty()) {
                hrSalaryStructureAccuralDeductionEffectTypeService.addHrSalaryStructureAccuralDeductionEffectTypeList(accuralDeductionDTO.getHrSalaryStructureDeductionEffectType(), hrEmployeeAccuralDeduction.getId(), isagUser);
            }
            if (accuralDeductionDTO.getHrSalaryStructureAccuralDeductionEffectTypeDelete() != null || !accuralDeductionDTO.getHrSalaryStructureAccuralDeductionEffectTypeDelete().isEmpty()) {
                hrSalaryStructureAccuralDeductionEffectTypeService.deleteListSalaryAccuralDeductionEffectTypeDTO(accuralDeductionDTO.getHrSalaryStructureAccuralDeductionEffectTypeDelete(), isagUser);
            }
            
            
            
            accuralDeductionDTO = mapTODTO(hrEmployeeAccuralDeduction, accuralDeductionDTO);
            hrEmployeeAccuralDeductionDTOReturn.add(accuralDeductionDTO);
        }
        return hrEmployeeAccuralDeductionDTOReturn;

    }

    @Override
    public HrSalaryStructureAccuralDeductionDTO addHrEEmployeeAccuralDeduction(HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTO, IsagUser isagUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteHrSalaryStructureAccuralDeductionList(List<HrSalaryStructureAccuralDeductionDTO> hrSalaryStructureAccuralDeductionDTOList, IsagUser isagUser) {
        if (hrSalaryStructureAccuralDeductionDTOList != null && !hrSalaryStructureAccuralDeductionDTOList.isEmpty()) {
            for (HrSalaryStructureAccuralDeductionDTO accuralDeductionDTO : hrSalaryStructureAccuralDeductionDTOList) {
                deleteHrEmployeeAccuralDeduction(accuralDeductionDTO, isagUser);
            }
        }
    }

    public HrSalaryStructureAccuralDeductionDTO mapTODTO(HrSalaryStructureAccuralDeduction entity, HrSalaryStructureAccuralDeductionDTO dTO) {
        if (dTO == null) {
            dTO = new HrSalaryStructureAccuralDeductionDTO();
        }
        initMapToDTO(dTO, entity);

        return dTO;
    }

    private void initMapToDTO(HrSalaryStructureAccuralDeductionDTO dTO, HrSalaryStructureAccuralDeduction entity) {
      

        if (entity.getSalaryStructureId() != null && entity.getSalaryStructureId().getId() != null) {
            HrSalaryStructureDTO parentDTO = new HrSalaryStructureDTO();
            parentDTO.setId(entity.getSalaryStructureId().getId());
            parentDTO.setType(entity.getSalaryStructureId().getType());
            dTO.setSalaryStructureId(parentDTO);
        }
        if (entity.getAccuralDeductionId() != null && entity.getAccuralDeductionId().getId() != null) {
            RscAccrualDeductionDTO parentDTO = new RscAccrualDeductionDTO();
            parentDTO.setId(entity.getAccuralDeductionId().getId());
            parentDTO.setRscAccrualDeductionCode(entity.getAccuralDeductionId().getCode());
            parentDTO.setRscAccrualDeductionName(entity.getAccuralDeductionId().getName());
            parentDTO.setRscAccrualDeductionType(entity.getAccuralDeductionId().getType());
            parentDTO.setSerial(entity.getAccuralDeductionId().getSerial());

            dTO.setAccuralDeductionId(parentDTO);
        }
        if (entity.getId() != null && entity.getAccuralDeductionId() != null) {
            if (entity.getAccuralDeductionId().getType().equals(0)) {
                dTO.setHrSalaryStructureAccuralEffectType(hrSalaryStructureAccuralDeductionEffectTypeService.findAllSalaryStructureAccuralDeductionEffectTypeByBranchId(entity.getBranchId().getId(), entity.getId(), 0));
            }
            if (entity.getAccuralDeductionId().getType().equals(1)) {
                dTO.setHrSalaryStructureDeductionEffectType(hrSalaryStructureAccuralDeductionEffectTypeService.findAllSalaryStructureAccuralDeductionEffectTypeByBranchId(entity.getBranchId().getId(), entity.getId(), 1));
            }
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setId(entity.getId());
        dTO.setValue(entity.getValue());
  
    }

    public HrSalaryStructureAccuralDeduction mapFromDTO(HrSalaryStructureAccuralDeductionDTO dTO, IsagUser isagUser) {
        HrSalaryStructureAccuralDeduction entity = new HrSalaryStructureAccuralDeduction();

        if (dTO.getSalaryStructureId() != null && dTO.getSalaryStructureId().getId() != null) {
            HrSalaryStructure parent = new HrSalaryStructure();
            parent.setId(dTO.getSalaryStructureId().getId());
            parent.setType(dTO.getSalaryStructureId().getType());
            entity.setSalaryStructureId(parent);
        }
        if (dTO.getAccuralDeductionId() != null && dTO.getAccuralDeductionId().getId() != null) {
            RscAccrualDeduction parent = new RscAccrualDeduction();
            parent.setId(dTO.getAccuralDeductionId().getId());
            parent.setName(dTO.getAccuralDeductionId().getRscAccrualDeductionName());
            parent.setCode(dTO.getAccuralDeductionId().getRscAccrualDeductionCode());
            parent.setType(dTO.getAccuralDeductionId().getRscAccrualDeductionType());

            parent.setSerial(dTO.getAccuralDeductionId().getSerial());
            entity.setAccuralDeductionId(parent);
        }
     
        entity.setId(dTO.getId());
        entity.setValue(dTO.getValue());
        
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

    private void returnListDTO(List<HrSalaryStructureAccuralDeduction> list, List<HrSalaryStructureAccuralDeductionDTO> dTOList) {
        for (HrSalaryStructureAccuralDeduction entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    @Override
    public void deleteByEmployeeId(Integer employeeId) {

        Map<String, Object> params = new HashMap();
        params.put("employeeId", employeeId);

        dao.executeDeleteQuery("delete from HrSalaryStructureAccuralDeduction d WHERE d.salaryStructureId.employeeId.id=" + employeeId);

    }

    @Override
    public List<HrSalaryStructureAccuralDeductionDTO> findHrEmpAccuralDTOBySalaryStructureId(Integer salaryStructureId, Integer type) {
        Map<String, Object> params = new HashMap();
        params.put("salaryStructureId", salaryStructureId);
        params.put("type", type);

        List<HrSalaryStructureAccuralDeduction> hrEmployeeAccuralDeductionList = dao.findListByQuery("SELECT d FROM HrSalaryStructureAccuralDeduction d WHERE d.salaryStructureId.id=:salaryStructureId and d.accuralDeductionId.type = :type", params);
        List<HrSalaryStructureAccuralDeductionDTO> hrEmployeeAccuralDeductionDTOList = new ArrayList<>();
        returnListDTO(hrEmployeeAccuralDeductionList, hrEmployeeAccuralDeductionDTOList);
        return hrEmployeeAccuralDeductionDTOList;
    }
    
    @Override
    public List<HrSalaryStructureAccuralDeductionDTO> findHrEmpAccuralDTOBySalaryStructureBysalaryStructureId(Integer salaryStructureId) {
        Map<String, Object> params = new HashMap();
        params.put("salaryStructureId", salaryStructureId); 
        List<HrSalaryStructureAccuralDeduction> hrEmployeeAccuralDeductionList = dao.findListByQuery("SELECT d FROM HrSalaryStructureAccuralDeduction d WHERE d.salaryStructureId.id=:salaryStructureId", params);
        List<HrSalaryStructureAccuralDeductionDTO> hrEmployeeAccuralDeductionDTOList = new ArrayList<>();
        returnListDTO(hrEmployeeAccuralDeductionList, hrEmployeeAccuralDeductionDTOList);
        return hrEmployeeAccuralDeductionDTOList;
    }

}

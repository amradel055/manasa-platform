/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.DepartmentDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.HrSalaryStructureAccuralDeductionDTO;
import com.isag.dto.HrSalaryStructureAccuralDeductionEffectTypeDTO;
import com.isag.dto.HrSalaryStructureDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.Employee;
import com.isag.entity.HrSalaryStructure;
import com.isag.entity.IsagUser;
import java.lang.reflect.Array;
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
public class HrSalaryStructureServiceImpl implements HrSalaryStructureService {
    
    @EJB
    private GenericDAO dao;
    
    @EJB
    HrSalaryStructureAccuralDeductionService accuralDeductionService;
    @EJB
    HrSalariesEffectService salariesEffectService;
    
    @Override
    public List<HrSalaryStructureDTO> findAllSalaryStructureByBranchId(Integer BranchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("BranchId", BranchId);
        List<HrSalaryStructure> hrSalaryStructureList = dao.findListByQuery("SELECT h FROM HrSalaryStructure h WHERE h.branchId.id = :BranchId ", params);
        List<HrSalaryStructureDTO> dTOList = new ArrayList<>();
        returnListDTO(hrSalaryStructureList, dTOList);
        return dTOList;
    }
    
    @Override
    public HrSalaryStructureDTO findSalaryStructure(Integer hrSalaryStructureId) {
        HrSalaryStructure hrSalaryStructure = dao.findEntityById(HrSalaryStructure.class, hrSalaryStructureId);
        HrSalaryStructureDTO hrSalaryStructureDTO = mapTODTO(hrSalaryStructure, null);
        
        return hrSalaryStructureDTO;
        
    }
    
    @Override
    public void deleteSalaryStructure(HrSalaryStructureDTO salaryStructureDTO, IsagUser isagUser) {
        HrSalaryStructure entity = new HrSalaryStructure();
        entity.setId(salaryStructureDTO.getId());
        dao.deleteEntity(entity);
        
    }
    
    @Override
    public HrSalaryStructureDTO addAccuralDeduction(HrSalaryStructureDTO salaryStructureDTO, IsagUser isagUser) {
        
        if (salaryStructureDTO.getHrSalaryStructureAccuralDeductionDTODeletedList() != null && !salaryStructureDTO.getHrSalaryStructureAccuralDeductionDTODeletedList().isEmpty()) {
            accuralDeductionService.deleteHrSalaryStructureAccuralDeductionList(salaryStructureDTO.getHrSalaryStructureAccuralDeductionDTODeletedList(), isagUser);
        }
        
        HrSalaryStructure entity = mapFromDTO(salaryStructureDTO, isagUser);
        entity = dao.updateEntity(entity);
        salariesEffectService.addHrSalariesEffectList(salaryStructureDTO.getSalariesEffectDTOList(), isagUser, entity.getId());
        accuralDeductionService.addHrEmployeeAccuralDeductionList(salaryStructureDTO.getHrEmployeeAccuralDTOList(), isagUser, entity.getId());
        accuralDeductionService.addHrEmployeeAccuralDeductionList(salaryStructureDTO.getHrEmployeeDeductionDTOList(), isagUser, entity.getId());
        salaryStructureDTO = mapTODTO(entity, salaryStructureDTO);
        
        return salaryStructureDTO;
    }
    
    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(h) FROM HrSalaryStructure h WHERE h.branchId.id = :branchId  ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        
        return details;
    }
    
    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and  (h.employeeId.name  like CONCAT('%',:globalFilter ,'%')) OR (h.employeeId.designation.name like CONCAT('%',:globalFilter ,'%') OR (h.employeeId.departmentId.name like CONCAT('%',:globalFilter ,'%')) OR (h.employeeId.active like CONCAT('%',:globalFilter ,'%'))";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }
    
    @Override
    public List<HrSalaryStructureDTO> getALLSalaryStructureByBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT h FROM HrSalaryStructure h WHERE h.branchId.id = :branchId   ");
        appendFilter(filters, branchId, builder);
        builder.append(" ORDER BY h.id DESC");
        List<HrSalaryStructure> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<HrSalaryStructureDTO> dTOList = new ArrayList<>();
        returnListDTO(details, dTOList);
        return dTOList;
    }
    
    public HrSalaryStructureDTO mapTODTO(HrSalaryStructure entity, HrSalaryStructureDTO dTO) {
        if (dTO == null) {
            dTO = new HrSalaryStructureDTO();
        }
        initMapToDTO(dTO, entity);
        
        return dTO;
    }
    
    private void initMapToDTO(HrSalaryStructureDTO dTO, HrSalaryStructure entity) {
        
        if (entity.getEmployeeId() != null) {
            EmployeeDTO parentDTO = new EmployeeDTO();
            parentDTO.setName(entity.getEmployeeId().getName());
            parentDTO.setCode(entity.getEmployeeId().getCode());
            parentDTO.setId(entity.getEmployeeId().getId());
            
            if (entity.getEmployeeId().getDepartmentId() != null) {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(entity.getEmployeeId().getDepartmentId().getId());
                departmentDTO.setName(entity.getEmployeeId().getDepartmentId().getName());
                parentDTO.setDepartmentId(departmentDTO);
            }
            
            if (entity.getEmployeeId().getDesignation() != null) {
                SymbolDTO symbolDTO = new SymbolDTO();
                symbolDTO.setId(entity.getEmployeeId().getDesignation().getId());
                symbolDTO.setName(entity.getEmployeeId().getDesignation().getName());
                parentDTO.setDesignation(symbolDTO);
            }
            dTO.setEmployeeId(parentDTO);
        }
        dTO.setHrEmployeeAccuralDTOList(accuralDeductionService.findHrEmpAccuralDTOBySalaryStructureId(entity.getId(), 0));
        dTO.setHrEmployeeDeductionDTOList(accuralDeductionService.findHrEmpAccuralDTOBySalaryStructureId(entity.getId(), 1));
        dTO.setDateFrom(entity.getDateFrom());
        dTO.setEntryDate(entity.getEntryDate());
        dTO.setMonths(entity.getMonths() == null ? null : entity.getMonths().toString());
        dTO.setRemark(entity.getRemark());
        dTO.setType(entity.getType());
        dTO.setWorkDayOfMonth(entity.getWorkDayOfMonth());
        dTO.setWorkHourOfDay(entity.getWorkHourOfDay());
        
        dTO.setDateTo(entity.getDateTo());
        dTO.setId(entity.getId());
        dTO.setActive(entity.getActive());
        if (entity.getActive().equals(Boolean.TRUE)) {
            dTO.setActiveText("نشط");
        } else {
            dTO.setActiveText("غير نشط");
        }
        dTO.setIndex(entity.getId());
        dTO.setTravelTicket(entity.getTravelTicket());
        dTO.setTravelTicketAmount(entity.getTravelTicketAmount());
        dTO.setTravelTicketValue(entity.getTravelTicketValue());
        dTO.setServiceEnd(entity.getServiceEnd());
        dTO.setServiceEndDaysNumber(entity.getServiceEndDaysNumber());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        
    }
    
    public HrSalaryStructure mapFromDTO(HrSalaryStructureDTO dTO, IsagUser isagUser) {
        HrSalaryStructure entity = new HrSalaryStructure();
        
        if (dTO.getEmployeeId() != null) {
            Employee parent = new Employee();
            parent.setId(dTO.getEmployeeId().getId());
            entity.setEmployeeId(parent);
        }
        
        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
            entity.setId(dTO.getId());
            entity.setEntryDate(dTO.getEntryDate());
            
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }
        
        entity.setWorkDayOfMonth(dTO.getWorkDayOfMonth());
        entity.setWorkHourOfDay(dTO.getWorkHourOfDay());
        entity.setDateFrom(dTO.getDateFrom());
        entity.setActive(dTO.getActive());
        entity.setTravelTicket(dTO.getTravelTicket());
        entity.setTravelTicketAmount(dTO.getTravelTicketAmount());
        entity.setTravelTicketValue(dTO.getTravelTicketValue());
        entity.setServiceEnd(dTO.getServiceEnd());
        entity.setServiceEndDaysNumber(dTO.getServiceEndDaysNumber());
        entity.setType(dTO.getType());
        entity.setMonths(dTO.getMonths() == null ? null : Integer.parseInt(dTO.getMonths()));
        entity.setRemark(dTO.getRemark());
        entity.setDateTo(dTO.getDateTo());
        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }
    
    private void returnListDTO(List<HrSalaryStructure> list, List<HrSalaryStructureDTO> dTOList) {
        for (HrSalaryStructure entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }
    
    @Override
    public List<HrSalaryStructureDTO> getEmployeesListByEmployeeId(Integer employeeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public HrSalaryStructureDTO getSalaryStructureByEmployeeId(Integer employeeId) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }
    
    @Override
    public Integer getSalaryStructureByEmployeeIdAndDate(Integer employeeId,String month,Integer year) {
        
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap();
        params.put("employeeId", employeeId);
        builder.append("SELECT id FROM hr_salary_structure where EXTRACT( YEAR_MONTH FROM `date_from` )  <= ").append(year).append(month).append(" and EXTRACT( YEAR_MONTH FROM `date_to` )  >= ").append(year).append(month).append(" and employee_id =    ").append(employeeId);
        List<Integer> details = dao.executeNativeQuery(builder.toString());
        if(details != null && !details.isEmpty()){
           return details.get(0);
        }
        return null;
    }
    
}

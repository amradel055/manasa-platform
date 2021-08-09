/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.IsagUserDTO;
import com.isag.dto.IsagUserEmployeeDTO;
import com.isag.entity.Employee;
import com.isag.entity.IsagUserEmployee;
import com.isag.entity.IsagUserRole;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class IsagUserEmployeeServiceImpl implements IsagUserEmployeeService {

    @EJB
    private GenericDAO dao;

    @EJB
    IsagUserInventoryService isagUserInventoryService;

    
    
     private List<IsagUserEmployeeDTO> returnListDTO(List<IsagUserEmployee> list) {
        List<IsagUserEmployeeDTO> dTOList = new ArrayList<>();
        for (IsagUserEmployee entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private IsagUserEmployeeDTO mapTODTO(IsagUserEmployee entity) {
        IsagUserEmployeeDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private IsagUserEmployeeDTO initMapToDTO(IsagUserEmployee entity) {
        IsagUserEmployeeDTO dTO = new IsagUserEmployeeDTO();
        EmployeeServiceImpl employeeServiceImpl = new EmployeeServiceImpl();
        EmployeeDTO employeeDTO = employeeServiceImpl.mapTODTO(entity.getEmployee(), null);
        
        dTO.setEmployee(employeeDTO);

        IsagUserServiceImpl iSAGUserServiceImpl = new IsagUserServiceImpl();
        IsagUserDTO isagUserDTO = iSAGUserServiceImpl.mapTODTO(entity.getUserId(), null);
        
        dTO.setUserId(isagUserDTO);
        
        return dTO;
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesByUserAndBranch(Integer userId, Integer branchId) {
        
        List<IsagUserEmployeeDTO> isagUserEmployeeList = getAllIsagUserEmployeesByUserAndBranch(userId, branchId);

        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        for (IsagUserEmployeeDTO isagUserBank : isagUserEmployeeList) {
            employeeDTOList.add(isagUserBank.getEmployee() != null ? isagUserBank.getEmployee() : null);
        }

        return employeeDTOList;
    }

    @Override
    public List<IsagUserEmployeeDTO> getAllIsagUserEmployeesByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);
        List<IsagUserEmployee> list = dao.findListByQuery("SELECT e FROM IsagUserEmployee e WHERE e.branchId.id = :branchId  AND e.userId.id = :userId", params);
        return returnListDTO(list);
    }

    @Override
    public IsagUserEmployee updateIsagUserEmployee(IsagUserEmployee isagUserEmployee) {
        return dao.updateEntity(isagUserEmployee);
    }

    @Override
    public void deleteEmployee(IsagUserRole isagUserRole, Employee employee) {
        String sql = "delete from IsagUserEmployee e  where e.branchId.id  = " + isagUserRole.getBranchId().getId()
                + " AND e.userId.id = " + isagUserRole.getUserId().getId() + " AND e.employee.id = " + employee.getId();
        dao.executeDeleteQuery(sql);
    }

    @Override
    public void deleteIsagUserEmployee(List<IsagUserEmployee> isagUserEmployeeList) {
        dao.deleteEntity(isagUserEmployeeList);
    }

}

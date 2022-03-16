/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.entity.Employee;
import com.isag.entity.InvInventory;
import com.isag.entity.IsagUser;
import com.isag.entity.IsagUserEmployee;
import com.isag.entity.IsagUserInventory;
import com.isag.entity.IsagUserRole;
import java.util.ArrayList;
import java.util.Date;

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
public class IsagUserInventoryServiceImpl implements IsagUserInventoryService {

    @EJB
    private GenericDAO dao;

    @EJB
    IsagUserInventoryService isagUserInventoryService;
    
    @EJB
    IsagUserService iSAGUserService;

    @Override
    public List<InvInventory> getAllInventroisByUserAndBranch(Integer userId, Integer branchId) {

        List<IsagUserInventory> isagUserBankList = isagUserInventoryService.getAllIsagUserInventroiesByUserAndBranch(userId, branchId);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (IsagUserInventory isagUserBank : isagUserBankList) {
            inventoryList.add(isagUserBank.getInvInventoryId() != null ? isagUserBank.getInvInventoryId() : null);
        }

        return inventoryList;
    }

    @Override
    public List<InvInventory> getAllInventroisByUserAndBranchPer(Integer userId, Integer branchId) {

        List<IsagUserInventory> isagUserBankList = isagUserInventoryService.getAllIsagUserInventroiesByUserAndBranch(userId, branchId);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (IsagUserInventory isagUserBank : isagUserBankList) {
            InvInventory inventory = new InvInventory();
            if (isagUserBank.getInvInventoryId() != null) {
                inventory.setId(isagUserBank.getInvInventoryId().getId());
                inventory.setCode(isagUserBank.getInvInventoryId().getCode());
                inventory.setName(isagUserBank.getInvInventoryId().getName());
                inventoryList.add(inventory);
            }
        }

        return inventoryList;
    }

    @Override
    public List<IsagUserInventory> getAllIsagUserInventroiesByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);

        return dao.findListByQuery("SELECT e FROM IsagUserInventory e WHERE e.branchId.id = :branchId  AND e.userId.id = :userId", params);
    }

    @Override
    public List<IsagUserEmployee> getAllIsagUserEmployeesByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);

        return dao.findListByQuery("SELECT e FROM IsagUserEmployee e WHERE e.branchId.id = :branchId  AND e.userId.id = :userId", params);
    }

    @Override
    public IsagUserInventory updateIsagUserInventory(IsagUserInventory isagUserInventory) {
        return dao.updateEntity(isagUserInventory);
    }

    @Override
    public void deleteInventory(IsagUserRole isagUserRole, InvInventory inventory) {
        String sql = "delete from IsagUserInventory e  where e.branchId.id  = " + isagUserRole.getBranchId().getId()
                + " AND e.userId.id = " + isagUserRole.getUserId().getId() + " AND e.invInventoryId.id = " + inventory.getId();
        dao.executeDeleteQuery(sql);
    }

    @Override
    public void deleteIsagUserInventory(List<IsagUserInventory> isagUserInventoryList) {
        dao.deleteEntity(isagUserInventoryList);
    }

    @Override
    public List<InvInventoryDTO> getAllInventroisByUserAndBranchPerDTO(Integer userId, Integer branchId) {

        List<IsagUserInventory> isagUserBankList = isagUserInventoryService.getAllIsagUserInventroiesByUserAndBranch(userId, branchId);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (IsagUserInventory isagUserBank : isagUserBankList) {
            InvInventory inventory = new InvInventory();
            if (isagUserBank.getInvInventoryId() != null) {
                inventory.setId(isagUserBank.getInvInventoryId().getId());
                inventory.setCode(isagUserBank.getInvInventoryId().getCode());
                inventory.setName(isagUserBank.getInvInventoryId().getName());
                inventoryList.add(inventory);
            }
        }

        return returnListDTO(inventoryList);
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesByUserAndBranchPerDTO(Integer userId, Integer branchId) {

        List<Employee> userEmployeeList = iSAGUserService.getEmployeesByUserAndBranch(userId, branchId);

        List<Employee> employeeList = new ArrayList<>();

        for (Employee emp : userEmployeeList) {
            Employee employee = new Employee();
            if (emp != null) {
                employee.setId(emp.getId());
                employee.setCode(emp.getCode());
                employee.setName(emp.getName());
                if (emp.getParentId() != null) {
                    Employee parent = new Employee();
                    parent.setId(emp.getParentId().getId());
                    employee.setParentId(parent);
                }
                employeeList.add(employee);
            }
        }

        return returnListDTOEmployee(employeeList);
    }

    private List<EmployeeDTO> returnListDTOEmployee(List<Employee> list) {
        List<EmployeeDTO> dTOList = new ArrayList<>();
        for (Employee entity : list) {
            dTOList.add(mapTODTOEmployee(entity, false));
        }
        return dTOList;
    }

    private EmployeeDTO mapTODTOEmployee(Employee entity, Boolean needDetail) {
        EmployeeDTO dTO = initMapToDTOEmployee(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private EmployeeDTO initMapToDTOEmployee(Employee entity) {

        EmployeeDTO dTO = new EmployeeDTO();
        dTO.setId(entity.getId());
        if (entity.getParentId() != null) {
            EmployeeDTO parentDTO = new EmployeeDTO();
            parentDTO.setId(entity.getParentId().getId());
            dTO.setParentId(parentDTO);
        }
        dTO.setName(entity.getName());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);

        return dTO;
    }

    private InvInventory mapFromDTO(InvInventoryDTO dTO, IsagUser isagUser) {
        InvInventory entity = new InvInventory();

        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());

        if (dTO.getId()
                == null) {
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

        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private List<InvInventoryDTO> returnListDTO(List<InvInventory> list) {
        List<InvInventoryDTO> dTOList = new ArrayList<>();
        for (InvInventory entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private InvInventoryDTO mapTODTO(InvInventory entity, Boolean needDetail) {
        InvInventoryDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private InvInventoryDTO initMapToDTO(InvInventory entity) {

        InvInventoryDTO dTO = new InvInventoryDTO();
        dTO.setId(entity.getId());

        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);

        return dTO;
    }

}

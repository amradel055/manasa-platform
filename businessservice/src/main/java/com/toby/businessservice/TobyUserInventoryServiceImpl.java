/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.EmployeeDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.entity.Employee;
import com.toby.entity.InvInventory;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserInventory;
import com.toby.entity.TobyUserRole;
import java.util.ArrayList;
import java.util.Collections;
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
public class TobyUserInventoryServiceImpl implements TobyUserInventoryService {

    @EJB
    private GenericDAO dao;

    @EJB
    TobyUserService tobyUserService;

    @Override
    public List<InvInventory> getAllInventroisByUserAndBranch(Integer userId, Integer branchId, Integer type) {

        List<TobyUserInventory> tobyUserBankList = getAllTobyUserInventroiesByUserAndBranchByType(userId, branchId, type);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            InvInventory inventoryNew = new InvInventory();
            inventoryNew.setId(tobyUserBank.getInvInventoryId().getId());
            inventoryNew.setName(tobyUserBank.getInvInventoryId().getName());            
            inventoryList.add(tobyUserBank.getInvInventoryId() != null ? tobyUserBank.getInvInventoryId() : null);
        }

        return inventoryList;
    }

    @Override
    public List<InvInventory> getAllInventroisByUserAndBranchPer(Integer userId, Integer branchId) {

        List<TobyUserInventory> tobyUserBankList = getAllTobyUserInventroiesByUserAndBranch(userId, branchId);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            InvInventory inventory = new InvInventory();
            if (tobyUserBank.getInvInventoryId() != null) {
                inventory.setId(tobyUserBank.getInvInventoryId().getId());
                inventory.setCode(tobyUserBank.getInvInventoryId().getCode());
                inventory.setName(tobyUserBank.getInvInventoryId().getName());
                inventoryList.add(inventory);
            }
        }

        return inventoryList;
    }

    @Override
    public List<InvInventory> getAllInventroisByUserAndBranchPerByType(Integer userId, Integer branchId, Integer invType) {

        List<TobyUserInventory> tobyUserBankList = getAllTobyUserInventroiesByUserAndBranchByType(userId, branchId, invType);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            InvInventory inventory = new InvInventory();
            if (tobyUserBank.getInvInventoryId() != null) {
                inventory.setId(tobyUserBank.getInvInventoryId().getId());
                inventory.setCode(tobyUserBank.getInvInventoryId().getCode());
                inventory.setName(tobyUserBank.getInvInventoryId().getName());
                inventoryList.add(inventory);
            }
        }

        return inventoryList;
    }

    @Override
    public List<TobyUserInventory> getAllTobyUserInventroiesByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);
        List<TobyUserInventory> list = dao.findListByQuery("SELECT e FROM TobyUserInventory e WHERE e.branchId.id = :branchId  AND e.userId.id = :userId AND e.invInventoryId.invType = 0 ", params);
        return list;
    }

    @Override
    public List<TobyUserInventory> getAllTobyUserInventroiesByUserAndBranchByType(Integer userId, Integer branchId, Integer invType) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);
        params.put("invType", invType);
        return dao.findListByQuery("SELECT e FROM TobyUserInventory e WHERE e.branchId.id = :branchId  AND e.userId.id = :userId AND e.invInventoryId.invType = :invType", params);
    }

    @Override
    public TobyUserInventory updateTobyUserInventory(TobyUserInventory tobyUserInventory) {
        return dao.updateEntity(tobyUserInventory);
    }

    @Override
    public void deleteInventory(TobyUserRole tobyUserRole, InvInventory inventory) {
        String sql = "delete from TobyUserInventory e  where e.branchId.id  = " + tobyUserRole.getBranchId().getId()
                + " AND e.userId.id = " + tobyUserRole.getUserId().getId() + " AND e.invInventoryId.id = " + inventory.getId();
        dao.executeDeleteQuery(sql);
    }

    @Override
    public void deleteTobyUserInventory(List<TobyUserInventory> tobyUserInventoryList) {
        dao.deleteEntity(tobyUserInventoryList);
    }

    @Override
    public List<InvInventoryDTO> getAllInventroyDTOByUserAndBranch(TobyUser tobyUser) {
// صفحة فواتير المبيعات
        List<TobyUserInventory> tobyUserBankList = getAllTobyUserInventroiesByUserAndBranch(tobyUser.getId(), tobyUser.getBranchId().getId());

        List<InvInventoryDTO> inventoryList = new ArrayList<>();

        InvInventoryImpl inventoryImpl = new InvInventoryImpl();
        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            inventoryList.add(inventoryImpl.mapToDTO(tobyUserBank.getInvInventoryId()));
        }

        return inventoryList;
    }

    @Override
    public List<InvInventoryDTO> getAllInventroyDTOByUserAndBranchByType(TobyUser tobyUser, Integer invType) {

        List<TobyUserInventory> tobyUserBankList = getAllTobyUserInventroiesByUserAndBranchByType(tobyUser.getId(), tobyUser.getBranchId().getId(), invType);

        List<InvInventoryDTO> inventoryList = new ArrayList<>();

        InvInventoryImpl inventoryImpl = new InvInventoryImpl();
        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            inventoryList.add(inventoryImpl.mapToDTO(tobyUserBank.getInvInventoryId()));
        }

        return inventoryList;
    }

    public List<TobyUserInventory> getAllTobyUserGallaryByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);
        params.put("invType", 2);
       List<TobyUserInventory> list =  dao.findListByQuery("SELECT e FROM TobyUserInventory e WHERE (e.branchId.id = :branchId  AND e.userId.id = :userId AND e.invInventoryId.invType = :invType)", params);
        return list;
    }

    @Override
    public List<InvInventoryDTO> getAllGallaryByUserAndBranch(TobyUser tobyUser) {

        List<TobyUserInventory> tobyUserBankList = getAllTobyUserGallaryByUserAndBranch(tobyUser.getId(), tobyUser.getBranchId() == null ? null : tobyUser.getBranchId().getId());

        List<InvInventoryDTO> inventoryList = new ArrayList<>();

        InvInventoryImpl inventoryImpl = new InvInventoryImpl();
        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            inventoryList.add(inventoryImpl.mapToDTO(tobyUserBank.getInvInventoryId()));
        }
        Collections.sort(inventoryList);
        return inventoryList;
    }

    @Override
    public List<InvInventoryDTO> getAllInventroisByUserAndBranchPerDTO(Integer userId, Integer branchId) {

        List<TobyUserInventory> tobyUserBankList = getAllTobyUserInventroiesByUserAndBranch(userId, branchId);

        List<InvInventory> inventoryList = new ArrayList<>();

        for (TobyUserInventory tobyUserBank : tobyUserBankList) {
            InvInventory inventory = new InvInventory();
            if (tobyUserBank.getInvInventoryId() != null) {
                inventory.setId(tobyUserBank.getInvInventoryId().getId());
                inventory.setCode(tobyUserBank.getInvInventoryId().getCode());
                inventory.setName(tobyUserBank.getInvInventoryId().getName());
                inventoryList.add(inventory);
            }
        }

        return returnListDTO(inventoryList);
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

    private InvInventory mapFromDTO(InvInventoryDTO dTO, TobyUser tobyUser) {
        InvInventory entity = new InvInventory();

        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());

        if (dTO.getId()
                == null) {
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

        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }
    
    @Override
    public List<EmployeeDTO> getAllEmployeesByUserAndBranchPerDTO(Integer userId, Integer branchId) {

        List<Employee> userEmployeeList = tobyUserService.getEmployeesByUserAndBranch(userId, branchId);

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


}

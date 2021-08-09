/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.RemBusesDTO;
import com.isag.dto.RemWorkOrderDTO;
import com.isag.entity.Employee;
import com.isag.entity.RemWorkOrder;
import com.isag.entity.IsagUser;
import com.isag.entity.RemBuses;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class RemWorkOrderServiceImpl implements RemWorkOrderService{

    @EJB
    private GenericDAO dao;
    @EJB
    RemWorkOrderDetailService remWorkOrderDetailService;

    @Override
    public RemWorkOrderDTO findWorkOrder(Integer workOrderId) {
        if (workOrderId != null) {
            RemWorkOrder entity = dao.findEntityById(RemWorkOrder.class, workOrderId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public void deleteWorkOrder(RemWorkOrderDTO workOrderesDTO) {
        remWorkOrderDetailService.deleteAllWorkOrderDetail(workOrderesDTO.getRemWorkOrderDetailDTOList());
        RemWorkOrder workOrderes = new RemWorkOrder();
        workOrderes.setId(workOrderesDTO.getId());
        dao.deleteEntity(workOrderes);
    }
    
    @Override
    public RemWorkOrderDTO save(RemWorkOrderDTO workOrderesDTO, IsagUser isagUser) {
        RemWorkOrder workOrder = mapToEntity(workOrderesDTO, isagUser);
        workOrder = dao.updateEntity(workOrder);
        workOrderesDTO.setRemWorkOrderDetailDTOList(remWorkOrderDetailService.saveAll(workOrderesDTO.getRemWorkOrderDetailDTOList(), isagUser, workOrder.getId()));
        remWorkOrderDetailService.deleteAllWorkOrderDetail(workOrderesDTO.getRemWorkOrderDetailDTOListDeleted());
        workOrderesDTO = mapTODTO(workOrder);
        return workOrderesDTO;
    }

    @Override
    public List<RemWorkOrderDTO> findWorkOrders(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (branchId != null) {
            params.put("branchId", branchId);
            query = "SELECT b FROM RemWorkOrder b WHERE b.branchId.id = :branchId";
        } 
        List<RemWorkOrder> workOrdereseList = dao.findListByQuery(query, params);
        List<RemWorkOrderDTO> listDTO = returnListDTO(workOrdereseList);
        return listDTO;
    }

    private List<RemWorkOrderDTO> returnListDTO(List<RemWorkOrder> list) {
        List<RemWorkOrderDTO> dTOList = new ArrayList<>();
        for (RemWorkOrder entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private RemWorkOrderDTO mapTODTO(RemWorkOrder entity) {
        RemWorkOrderDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private RemWorkOrderDTO initMapToDTO(RemWorkOrder entity) {

        RemWorkOrderDTO dTO = new RemWorkOrderDTO();
        
        dTO.setRemWorkOrderDetailDTOList(remWorkOrderDetailService.findAllWorkOrderDetail(entity.getId()));
        
        dTO.setId(entity.getId());

        dTO.setDate(entity.getDate());
        
        if(entity.getBusesId() != null){
            RemBusesDTO remBusesDTO = new RemBusesDTO();
            remBusesDTO.setId(entity.getBusesId().getId());
            remBusesDTO.setCode(entity.getBusesId().getCode());
            remBusesDTO.setName(entity.getBusesId().getName());
            dTO.setBusesId(remBusesDTO);
        }
        
        if(entity.getDriverId() != null){
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(entity.getDriverId().getId());
            employeeDTO.setCode(entity.getDriverId().getCode());
            employeeDTO.setName(entity.getDriverId().getName());
            dTO.setDriverId(employeeDTO);
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private RemWorkOrder mapToEntity(RemWorkOrderDTO dTO, IsagUser isagUser) {
        RemWorkOrder entity = new RemWorkOrder();

        entity.setDate(dTO.getDate());
      
        
        if(dTO.getBusesId()!= null && dTO.getBusesId().getId() != null){
            RemBuses remBuses = new RemBuses();
            remBuses.setId(dTO.getBusesId().getId());
            entity.setBusesId(remBuses);
        }
        
        if(dTO.getDriverId() != null && dTO.getDriverId().getId() != null){
            Employee employee = new Employee();
            employee.setId(dTO.getDriverId().getId());
            entity.setDriverId(employee);
        }

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setBranchId(isagUser.getBranchId());
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

}

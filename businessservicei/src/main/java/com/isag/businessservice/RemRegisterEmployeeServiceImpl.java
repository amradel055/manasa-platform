/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RemBusesDTO;
import com.isag.dto.RemRegisterEmployeeDTO;
import com.isag.dto.RemEmployeeShiftDTO;
import com.isag.dto.RemPointsDTO;
import com.isag.dto.RemTrafficDTO;
import com.isag.dto.RemWorkOrderDetailDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RemBuses;
import com.isag.entity.RemRegisterEmployee;
import com.isag.entity.RemEmployeeShift;
import com.isag.entity.RemPoints;
import com.isag.entity.RemTraffic;
import com.isag.entity.RemWorkOrderDetail;
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
public class RemRegisterEmployeeServiceImpl implements RemRegisterEmployeeService {

    @EJB
    private GenericDAO dao;

    @Override
    public RemRegisterEmployeeDTO findRegisterEmployee(Integer registerEmployeeId) {
        if (registerEmployeeId != null) {
            RemRegisterEmployee entity = dao.findEntityById(RemRegisterEmployee.class, registerEmployeeId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public void deleteRegisterEmployee(RemRegisterEmployeeDTO registerEmployeeDTO) {
        RemRegisterEmployee registerEmployees = new RemRegisterEmployee();
        registerEmployees.setId(registerEmployeeDTO.getId());
        dao.deleteEntity(registerEmployees);
    }

    @Override
    public RemRegisterEmployeeDTO save(RemRegisterEmployeeDTO registerEmployeeDTO, IsagUser isagUser) {
        RemRegisterEmployee bus = mapToEntity(registerEmployeeDTO, isagUser);
        bus = dao.updateEntity(bus);
        registerEmployeeDTO = mapTODTO(bus);
        return registerEmployeeDTO;
    }

    @Override
    public List<RemRegisterEmployeeDTO> findRegisterEmployeeList(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        String query = "SELECT r from RemRegisterEmployee r where r.branchId.id = :branchId";
        List<RemRegisterEmployee> buseseList = dao.findListByQuery(query, params);
        List<RemRegisterEmployeeDTO> listDTO = returnListDTO(buseseList);
        return listDTO;
    }

    private List<RemRegisterEmployeeDTO> returnListDTO(List<RemRegisterEmployee> list) {
        List<RemRegisterEmployeeDTO> dTOList = new ArrayList<>();
        for (RemRegisterEmployee entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private RemRegisterEmployeeDTO mapTODTO(RemRegisterEmployee entity) {
        RemRegisterEmployeeDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private RemRegisterEmployeeDTO initMapToDTO(RemRegisterEmployee entity) {

        RemRegisterEmployeeDTO dTO = new RemRegisterEmployeeDTO();
        dTO.setId(entity.getId());
        dTO.setDateArrival(entity.getDateArrival());
        dTO.setLag(entity.getLag());
        dTO.setLat(entity.getLat());

        if(entity.getBusId() != null){
            RemBusesDTO remBusesDTO = new RemBusesDTO();
            remBusesDTO.setId(entity.getBusId().getId());
            remBusesDTO.setChairNumber(entity.getBusId().getChairNumber());
            remBusesDTO.setCode(entity.getBusId().getCode());
            remBusesDTO.setName(entity.getBusId().getName());
            dTO.setBusId(remBusesDTO);
        }
        
        if(entity.getShiftId() != null){
            RemEmployeeShiftDTO remEmployeeShiftDTO = new RemEmployeeShiftDTO();
            remEmployeeShiftDTO.setId(entity.getShiftId().getId());
            remEmployeeShiftDTO.setCode(entity.getShiftId().getCode());
            remEmployeeShiftDTO.setName(entity.getShiftId().getName());
            dTO.setShiftId(remEmployeeShiftDTO);
        }
        
        if(entity.getTrafficId() != null){
            RemTrafficDTO remTrafficDTO = new RemTrafficDTO();
            remTrafficDTO.setId(entity.getTrafficId().getId());
            remTrafficDTO.setCode(entity.getTrafficId().getCode());
            remTrafficDTO.setName(entity.getTrafficId().getName());
            dTO.setTrafficId(remTrafficDTO);
        }
        
        if(entity.getRemWorkOrderDetailId() != null){
            RemWorkOrderDetailDTO remWorkOrderDetailDTO = new RemWorkOrderDetailDTO();
            remWorkOrderDetailDTO.setId(entity.getRemWorkOrderDetailId().getId());
            dTO.setRemWorkOrderDetailId(remWorkOrderDetailDTO);
        }
        
        if(entity.getPointId() != null){
            RemPointsDTO remPointsDTO = new RemPointsDTO();
            remPointsDTO.setId(entity.getPointId().getId());
            remPointsDTO.setName(entity.getPointId().getName());
            dTO.setPointId(remPointsDTO);
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private RemRegisterEmployee mapToEntity(RemRegisterEmployeeDTO dTO, IsagUser isagUser) {
        RemRegisterEmployee entity = new RemRegisterEmployee();

        entity.setDateArrival(dTO.getDateArrival());
        entity.setLag(dTO.getLag());
        entity.setLat(dTO.getLat());
        
        
        if(dTO.getBusId() != null){
            RemBuses remBuses = new RemBuses();
            remBuses.setId(dTO.getBusId().getId());
            entity.setBusId(remBuses);
        }
        
        if(dTO.getShiftId() != null){
            RemEmployeeShift remEmployeeShift = new RemEmployeeShift();
            remEmployeeShift.setId(dTO.getShiftId().getId());
            entity.setShiftId(remEmployeeShift);
        }
        
        if(dTO.getTrafficId() != null){
            RemTraffic remTraffic = new RemTraffic();
            remTraffic.setId(dTO.getTrafficId().getId());
            entity.setTrafficId(remTraffic);
        }
        
        if(dTO.getRemWorkOrderDetailId() != null){
            RemWorkOrderDetail remWorkOrderDetail = new RemWorkOrderDetail();
            remWorkOrderDetail.setId(dTO.getRemWorkOrderDetailId().getId());
            entity.setRemWorkOrderDetailId(remWorkOrderDetail);
        }
        
        if(dTO.getPointId() != null){
            RemPoints remPoints = new RemPoints();
            remPoints.setId(dTO.getPointId().getId());
            entity.setPointId(remPoints);
        }
        

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

}

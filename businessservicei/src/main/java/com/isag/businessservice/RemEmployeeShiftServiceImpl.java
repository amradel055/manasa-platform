/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RemBusesDTO;
import com.isag.dto.RemCompanyDTO;
import com.isag.dto.RemEmployeeShiftDTO;
import com.isag.dto.RemTrafficDTO;
import com.isag.entity.RemEmployeeShift;
import com.isag.entity.IsagUser;
import com.isag.entity.RemBuses;
import com.isag.entity.RemCompany;
import com.isag.entity.RemTraffic;
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
public class RemEmployeeShiftServiceImpl implements RemEmployeeShiftService{

    @EJB
    private GenericDAO dao;
    @EJB
    RemEmployeeShiftDetailService remEmployeeShiftDetailService;

    @Override
    public RemEmployeeShiftDTO findEmployeeShift(Integer employeeShiftId) {
        if (employeeShiftId != null) {
            RemEmployeeShift entity = dao.findEntityById(RemEmployeeShift.class, employeeShiftId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public void deleteEmployeeShift(RemEmployeeShiftDTO employeeShiftesDTO) {
        remEmployeeShiftDetailService.deleteAllEmployeeShiftDetail(employeeShiftesDTO.getRemEmployeeShiftDetailList());
        RemEmployeeShift employeeShiftes = new RemEmployeeShift();
        employeeShiftes.setId(employeeShiftesDTO.getId());
        dao.deleteEntity(employeeShiftes);
    }
    
    @Override
    public RemEmployeeShiftDTO save(RemEmployeeShiftDTO employeeShiftesDTO, IsagUser isagUser) {
        RemEmployeeShift employeeShift = mapToEntity(employeeShiftesDTO, isagUser);
        employeeShift = dao.updateEntity(employeeShift);
        employeeShiftesDTO.setRemEmployeeShiftDetailList(remEmployeeShiftDetailService.saveAll(employeeShiftesDTO.getRemEmployeeShiftDetailList(), isagUser, employeeShift.getId()));
        remEmployeeShiftDetailService.deleteAllEmployeeShiftDetail(employeeShiftesDTO.getRemEmployeeShiftDetailListDeleted());
        employeeShiftesDTO = mapTODTO(employeeShift);
        return employeeShiftesDTO;
    }

    @Override
    public List<RemEmployeeShiftDTO> findEmployeeShifts(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (branchId != null) {
            params.put("branchId", branchId);
            query = "SELECT b FROM RemEmployeeShift b WHERE b.branchId.id = :branchId";
        } 
        List<RemEmployeeShift> employeeShifteseList = dao.findListByQuery(query, params);
        List<RemEmployeeShiftDTO> listDTO = returnListDTO(employeeShifteseList);
        return listDTO;
    }

    private List<RemEmployeeShiftDTO> returnListDTO(List<RemEmployeeShift> list) {
        List<RemEmployeeShiftDTO> dTOList = new ArrayList<>();
        for (RemEmployeeShift entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private RemEmployeeShiftDTO mapTODTO(RemEmployeeShift entity) {
        RemEmployeeShiftDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private RemEmployeeShiftDTO initMapToDTO(RemEmployeeShift entity) {

        RemEmployeeShiftDTO dTO = new RemEmployeeShiftDTO();
        
        dTO.setRemEmployeeShiftDetailList(remEmployeeShiftDetailService.findAllEmployeeShiftDetail(entity.getId()));
        
        dTO.setId(entity.getId());

        dTO.setName(entity.getName());
        dTO.setCode(entity.getCode());
        
        if(entity.getRemCompanyId() != null){
            RemCompanyDTO companyDTO = new RemCompanyDTO();
            companyDTO.setId(entity.getRemCompanyId().getId());
            companyDTO.setCode(entity.getRemCompanyId().getCode());
            companyDTO.setName(entity.getRemCompanyId().getName());
            dTO.setRemCompanyId(companyDTO);
        }
        
        if(entity.getRemBusesId() != null){
            RemBusesDTO remBusesDTO = new RemBusesDTO();
            remBusesDTO.setId(entity.getRemBusesId().getId());
            remBusesDTO.setName(entity.getRemBusesId().getName());
            remBusesDTO.setCode(entity.getRemBusesId().getCode());
            remBusesDTO.setChairNumber(entity.getRemBusesId().getChairNumber());
            dTO.setRemBusesId(remBusesDTO);
        }
        
        if(entity.getRemTrafficId() != null){
            RemTrafficDTO remTrafficDTO = new RemTrafficDTO();
            remTrafficDTO.setId(entity.getRemTrafficId() .getId());
            remTrafficDTO.setCode(entity.getRemTrafficId() .getCode());
            remTrafficDTO.setName(entity.getRemTrafficId().getName());
            dTO.setRemTrafficId(remTrafficDTO);
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private RemEmployeeShift mapToEntity(RemEmployeeShiftDTO dTO, IsagUser isagUser) {
        RemEmployeeShift entity = new RemEmployeeShift();

        entity.setName(dTO.getName());
        entity.setCode(dTO.getCode());
        
        if(dTO.getRemCompanyId() != null && dTO.getRemCompanyId().getId() != null){
            RemCompany remCompany = new RemCompany();
            remCompany.setId(dTO.getRemCompanyId().getId());
            entity.setRemCompanyId(remCompany);
        }
        
        if(dTO.getRemBusesId() != null){
            RemBuses remBuses = new RemBuses();
            remBuses.setId(dTO.getRemBusesId().getId());
            entity.setRemBusesId(remBuses);
        }
        
        if(dTO.getRemTrafficId() != null){
            RemTraffic remTraffic = new RemTraffic();
            remTraffic.setId(dTO.getRemTrafficId() .getId());
            entity.setRemTrafficId(remTraffic);
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

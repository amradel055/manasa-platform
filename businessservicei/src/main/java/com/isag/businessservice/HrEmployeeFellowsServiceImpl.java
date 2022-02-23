/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.HrEmployeeFellowsDTO;
import com.isag.entity.Employee;
import com.isag.entity.HrEmployeeFellows;
import com.isag.entity.IsagUser;
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
public class HrEmployeeFellowsServiceImpl implements HrEmployeeFellowsService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public HrEmployeeFellowsDTO addHrEmployeeFellows(HrEmployeeFellowsDTO hrEmployeeFellowsDTO, IsagUser isagUser) {
        HrEmployeeFellows entity = mapFromDTO(hrEmployeeFellowsDTO, isagUser);

        entity = dao.updateEntity(entity);
        hrEmployeeFellowsDTO = mapTODTO(entity, hrEmployeeFellowsDTO);
        return hrEmployeeFellowsDTO;
    }

    @Override
    public List<HrEmployeeFellowsDTO> addHrEmployeeFellowsList(List<HrEmployeeFellowsDTO> hrEmployeeFellowsDTOList, IsagUser isagUser, Integer employeeId) {

        // a method which returns a list from EmployeeFellowsDTO and prepare it to be saved.
        List<HrEmployeeFellowsDTO> hrEmpQualificationsReturnDTO = new ArrayList<>();

        for (HrEmployeeFellowsDTO hrEmployeeFellowsDTO : hrEmployeeFellowsDTOList) {

            hrEmployeeFellowsDTO.setEmployeeId(employeeId);
            HrEmployeeFellows hrEmployeeFellows = mapFromDTO(hrEmployeeFellowsDTO, isagUser);
            hrEmployeeFellows = dao.updateEntity(hrEmployeeFellows);
            hrEmployeeFellowsDTO = mapTODTO(hrEmployeeFellows, hrEmployeeFellowsDTO);
            hrEmpQualificationsReturnDTO.add(hrEmployeeFellowsDTO);

        }
        return hrEmpQualificationsReturnDTO;
    }

    @Override
    public List<HrEmployeeFellowsDTO> findHrEmployeeFellowsByEmployeeId(Integer employeeId) {

        Map<String, Object> params = new HashMap();
        params.put("employeeId", employeeId);

        List<HrEmployeeFellows> hrEmployeeFellowsList = dao.findListByQuery("SELECT d FROM HrEmployeeFellows d WHERE d.employeeId.id=:employeeId", params);
        List<HrEmployeeFellowsDTO> hrEmployeeFellowsDTOList = new ArrayList<>();
        returnListDTO(hrEmployeeFellowsList, hrEmployeeFellowsDTOList);
        return hrEmployeeFellowsDTOList;
    }

    @Override
    public void deleteHrEmployeeFellows(HrEmployeeFellowsDTO hrEmployeeFellowsDTO, IsagUser isagUser) {

        HrEmployeeFellows entity = new HrEmployeeFellows();
        entity.setId(hrEmployeeFellowsDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteHrEmployeeFellowsList(List<HrEmployeeFellowsDTO> hrEmployeeFellowsDTOList, IsagUser isagUser) {
        for (HrEmployeeFellowsDTO fellowsList : hrEmployeeFellowsDTOList) {
            HrEmployeeFellows hrEmployeeFellows = mapFromDTO(fellowsList, isagUser);
            dao.deleteEntity(hrEmployeeFellows);
        }
    }

    public HrEmployeeFellowsDTO mapTODTO(HrEmployeeFellows entity, HrEmployeeFellowsDTO dTO) {
        if (dTO == null) {
            dTO = new HrEmployeeFellowsDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(HrEmployeeFellowsDTO dTO, HrEmployeeFellows entity) {

        dTO.setIdNumber(entity.getIdNumber());
        dTO.setIdEndDate(entity.getIdEndDate());
        dTO.setRelation(entity.getRelation());
        dTO.setName(entity.getName());
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());
        dTO.setEmployeeId(entity.getEmployeeId().getId());

        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public HrEmployeeFellows mapFromDTO(HrEmployeeFellowsDTO dTO, IsagUser isagUser) {
        HrEmployeeFellows entity = new HrEmployeeFellows();
        if (dTO.getEmployeeId() != null) {
            Employee employee = new Employee();
            employee.setId(dTO.getEmployeeId());
            entity.setEmployeeId(employee);
        }
        entity.setIdEndDate(dTO.getIdEndDate());
        entity.setRelation(dTO.getRelation());
        entity.setName(dTO.getName());
        entity.setId(dTO.getId());
        entity.setIdNumber(dTO.getIdNumber());

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

    private void returnListDTO(List<HrEmployeeFellows> list, List<HrEmployeeFellowsDTO> dTOList) {
        for (HrEmployeeFellows entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    @Override
    public void deleteHrEmployeeFellowsByEmployeeId(Integer employeeId) {
        dao.executeDeleteQuery("delete from HrEmployeeFellows d WHERE d.employeeId.id =" + employeeId);
    }

}

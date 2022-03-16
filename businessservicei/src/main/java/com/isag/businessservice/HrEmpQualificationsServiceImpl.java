/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.HrEmpQualificationsDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.Employee;
import com.isag.entity.HrEmpQualifications;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class HrEmpQualificationsServiceImpl implements HrEmpQualificationsService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public HrEmpQualificationsDTO addHrEmpQualifications(HrEmpQualificationsDTO hrEmpQualificationsDTO, IsagUser isagUser) {
        HrEmpQualifications entity = mapFromDTO(hrEmpQualificationsDTO, isagUser);

        entity = dao.updateEntity(entity);
        hrEmpQualificationsDTO = mapTODTO(entity, hrEmpQualificationsDTO);
        return hrEmpQualificationsDTO;
    }

    @Override
    public List<HrEmpQualificationsDTO> addHrEmpQualificationsList(List<HrEmpQualificationsDTO> hrEmpQualificationsDTOList, IsagUser isagUser, Integer employeeId) {
        List<HrEmpQualificationsDTO> hrEmpQualificationsReturnDTO = new ArrayList<>();
        for (HrEmpQualificationsDTO hrEmpQualificationsDTO : hrEmpQualificationsDTOList) {
            hrEmpQualificationsDTO.setEmployeeId(employeeId);
            HrEmpQualifications hrEmpQualifications = mapFromDTO(hrEmpQualificationsDTO, isagUser);
            hrEmpQualifications = dao.updateEntity(hrEmpQualifications);
            hrEmpQualificationsDTO = mapTODTO(hrEmpQualifications, hrEmpQualificationsDTO);
            hrEmpQualificationsReturnDTO.add(hrEmpQualificationsDTO);
        }

        return hrEmpQualificationsReturnDTO;
    }

    @Override
    public List<HrEmpQualificationsDTO> findHrEmpQualificationsByEmployeeId(Integer employeeId) {

        Map<String, Object> params = new HashMap();
        params.put("employeeId", employeeId);

        List<HrEmpQualifications> hrEmpQualificationsList = dao.findListByQuery("SELECT d FROM HrEmpQualifications d WHERE d.employeeId.id=:employeeId", params);
        List<HrEmpQualificationsDTO> hrEmpQualificationsDTOList = new ArrayList<>();
        returnListDTO(hrEmpQualificationsList, hrEmpQualificationsDTOList);
        return hrEmpQualificationsDTOList;
    }

    @Override
    public void deleteHrEmpQualifications(HrEmpQualificationsDTO qualifications, IsagUser isagUser) {

        HrEmpQualifications entity = new HrEmpQualifications();
        entity.setId(qualifications.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteHrEmpQualificationsList(List<HrEmpQualificationsDTO> hrEmpQualificationsDTOList, IsagUser isagUser) {
        for (HrEmpQualificationsDTO qualificationList : hrEmpQualificationsDTOList) {
            HrEmpQualifications hrEmpQualifications = mapFromDTO(qualificationList, isagUser);
            dao.deleteEntity(hrEmpQualifications);
        }
    }

    public HrEmpQualificationsDTO mapTODTO(HrEmpQualifications entity, HrEmpQualificationsDTO dTO) {
        if (dTO == null) {
            dTO = new HrEmpQualificationsDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(HrEmpQualificationsDTO dTO, HrEmpQualifications entity) {
        dTO.setEmployeeId(entity.getId());
        if (entity.getEducationLevel() != null && entity.getEducationLevel().getId() != null) {
            SymbolDTO educationLevelDTO = new SymbolDTO();
            educationLevelDTO.setName(entity.getEducationLevel().getName());
            educationLevelDTO.setId(entity.getEducationLevel().getId());
            dTO.setEducationLevel(educationLevelDTO);
        }
        dTO.setChosenTopics(entity.getChosenTopics());
        dTO.setGraduationYear(entity.getGraduationYear());
        dTO.setPercentage(entity.getPercentage());
        dTO.setSchoolUniversity(entity.getSchoolUniversity());
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());

        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public HrEmpQualifications mapFromDTO(HrEmpQualificationsDTO dTO, IsagUser isagUser) {
        HrEmpQualifications entity = new HrEmpQualifications();
        if (dTO.getEmployeeId() != null) {
            Employee parent = new Employee();
            parent.setId(dTO.getEmployeeId());
            entity.setEmployeeId(parent);
        }
        if (dTO.getEducationLevel() != null && dTO.getEducationLevel().getId() != null) {
            Symbol educationLevel = new Symbol();
            educationLevel.setId(dTO.getEducationLevel().getId());
            entity.setEducationLevel(educationLevel);
        }

        entity.setId(dTO.getId());
        entity.setGraduationYear(dTO.getGraduationYear());
        entity.setPercentage(dTO.getPercentage());
        entity.setChosenTopics(dTO.getChosenTopics());
        entity.setSchoolUniversity(dTO.getSchoolUniversity());

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

    private void returnListDTO(List<HrEmpQualifications> list, List<HrEmpQualificationsDTO> dTOList) {
        for (HrEmpQualifications entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    @Override
    public void deleteByEmployeeId(Integer employeeId) {
        Map<String, Object> params = new HashMap();
        params.put("employeeId", employeeId);

        dao.executeDeleteQuery("delete from HrEmpQualifications d WHERE d.employeeId.id=" + employeeId);

    }

}

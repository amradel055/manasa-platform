/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.RemEmployeeShiftDTO;
import com.isag.dto.RemEmployeeShiftDetailDTO;
import com.isag.entity.Employee;
import com.isag.entity.RemEmployeeShiftDetail;
import com.isag.entity.IsagUser;
import com.isag.entity.RemEmployeeShift;
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
public class RemEmployeeShiftDetailServiceImpl implements RemEmployeeShiftDetailService {

    @EJB
    private GenericDAO dao;

    @Override
    public RemEmployeeShiftDetailDTO findEmployeeShiftDetail(Integer employeeShiftDetailId) {
        if (employeeShiftDetailId != null) {
            RemEmployeeShiftDetail entity = dao.findEntityById(RemEmployeeShiftDetail.class, employeeShiftDetailId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public List<RemEmployeeShiftDetailDTO> findAllEmployeeShiftDetail(Integer employeeShiftId) {
        if (employeeShiftId != null) {

            Map<String, Object> params = new HashMap<>();
            String query = "";
            params.put("employeeShiftId", employeeShiftId);
            query = "SELECT b FROM RemEmployeeShiftDetail b WHERE b.remEmployeeShiftId.id = :employeeShiftId";
            List<RemEmployeeShiftDetail> employeeShiftDetaileseList = dao.findListByQuery(query, params);
            return returnListDTO(employeeShiftDetaileseList);
        } else {
            return null;
        }
    }

    @Override
    public void deleteEmployeeShiftDetail(RemEmployeeShiftDetailDTO employeeShiftDetailesDTO) {
        RemEmployeeShiftDetail employeeShiftDetailes = new RemEmployeeShiftDetail();
        employeeShiftDetailes.setId(employeeShiftDetailesDTO.getId());
        dao.deleteEntity(employeeShiftDetailes);
    }

    @Override
    public void deleteAllEmployeeShiftDetail(List<RemEmployeeShiftDetailDTO> employeeShiftDetailesDTOList) {
        if (employeeShiftDetailesDTOList != null) {
            for (RemEmployeeShiftDetailDTO detail : employeeShiftDetailesDTOList) {
                if (detail.getId() != null) {
                    deleteEmployeeShiftDetail(detail);
                }
            }
        }
    }

    @Override
    public RemEmployeeShiftDetailDTO save(RemEmployeeShiftDetailDTO employeeShiftDetailesDTO, IsagUser isagUser, Integer remEmployeeShiftId) {
        RemEmployeeShiftDetail employeeShiftDetail = mapToEntity(employeeShiftDetailesDTO, isagUser, remEmployeeShiftId);
        employeeShiftDetail = dao.updateEntity(employeeShiftDetail);
        employeeShiftDetailesDTO = mapTODTO(employeeShiftDetail);
        return employeeShiftDetailesDTO;
    }

    @Override
    public List<RemEmployeeShiftDetailDTO> saveAll(List<RemEmployeeShiftDetailDTO> employeeShiftDetailesDTOList, IsagUser isagUser, Integer remEmployeeShiftId) {
        List<RemEmployeeShiftDetailDTO> dTOList = new ArrayList<>();
        for (RemEmployeeShiftDetailDTO detailDTO : employeeShiftDetailesDTOList) {
            dTOList.add(save(detailDTO, isagUser, remEmployeeShiftId));
        }
        return dTOList;
    }

    public RemEmployeeShiftDTO addDetail(RemEmployeeShiftDTO remEmployeeShiftDTO, Integer index) {
        if (remEmployeeShiftDTO != null) {
            RemEmployeeShiftDetailDTO remEmployeeShiftDetailDTO = new RemEmployeeShiftDetailDTO();
            remEmployeeShiftDetailDTO.setIndex(index);
            remEmployeeShiftDTO.getRemEmployeeShiftDetailList().add(remEmployeeShiftDetailDTO);
        }
        return remEmployeeShiftDTO;
    }

    private List<RemEmployeeShiftDetailDTO> returnListDTO(List<RemEmployeeShiftDetail> list) {
        List<RemEmployeeShiftDetailDTO> dTOList = new ArrayList<>();
        for (RemEmployeeShiftDetail entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private RemEmployeeShiftDetailDTO mapTODTO(RemEmployeeShiftDetail entity) {
        RemEmployeeShiftDetailDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private RemEmployeeShiftDetailDTO initMapToDTO(RemEmployeeShiftDetail entity) {

        RemEmployeeShiftDetailDTO dTO = new RemEmployeeShiftDetailDTO();
        dTO.setId(entity.getId());

        dTO.setRemEmployeeShiftId(entity.getRemEmployeeShiftId().getId());
        if (entity.getEmployeeId() != null) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setName(entity.getEmployeeId().getName());
            employeeDTO.setCode(entity.getEmployeeId().getCode());
            dTO.setEmployeeId(employeeDTO);
            dTO.setEmployeeIdBak(employeeDTO);
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private RemEmployeeShiftDetail mapToEntity(RemEmployeeShiftDetailDTO dTO, IsagUser isagUser, Integer remEmployeeShiftId) {
        RemEmployeeShiftDetail entity = new RemEmployeeShiftDetail();

        if (remEmployeeShiftId != null) {
            RemEmployeeShift remEmployeeShift = new RemEmployeeShift();
            remEmployeeShift.setId(remEmployeeShiftId);
            entity.setRemEmployeeShiftId(remEmployeeShift);
        }

        if (dTO.getEmployeeId() != null) {
            Employee employee = new Employee();
            employee.setId(dTO.getEmployeeId().getId());
            entity.setEmployeeId(employee);
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

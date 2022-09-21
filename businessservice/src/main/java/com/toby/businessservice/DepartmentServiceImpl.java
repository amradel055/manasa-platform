/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.DepartmentDTO;
import com.toby.entity.Department;
import com.toby.entity.TobyUser;
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
public class DepartmentServiceImpl implements DepartmentService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public DepartmentDTO addDepartment(DepartmentDTO department) {
        return dao.updateEntity(department);
    }

    @Override
    public DepartmentDTO findDepartment(Integer departmentId) {
        Department department = dao.findEntityById(Department.class, departmentId);
        DepartmentDTO departmentDTO = mapTODTO(department, null);
        return departmentDTO;
    }

    @Override
    public DepartmentDTO updateDepartment(DepartmentDTO department) {
        return dao.updateEntity(department);
    }

    @Override
    public void deleteDepartment(DepartmentDTO departmentDTO, TobyUser tobyUser) {
        Department entity = new Department();
        entity.setId(departmentDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public List<DepartmentDTO> getAllDepartmentsListByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<DepartmentDTO> dTOList = new ArrayList<>();
        List<Department> list = dao.findListByQuery("SELECT d FROM Department d WHERE d.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public DepartmentDTO mapTODTO(Department entity, DepartmentDTO dTO) {
        if (dTO == null) {
            dTO = new DepartmentDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(DepartmentDTO dTO, Department entity) {
        if (entity.getParentId() != null && entity.getParentId().getId() != null) {
            DepartmentDTO parentDTO = new DepartmentDTO();
            parentDTO.setName(entity.getParentId().getName());
            parentDTO.setCode(entity.getParentId().getCode());
            parentDTO.setId(entity.getParentId().getId());
            dTO.setParentId(parentDTO);
        }
        dTO.setName(entity.getName());
        dTO.setCode(entity.getCode());
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());

        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public Department mapFromDTO(DepartmentDTO dTO, TobyUser tobyUser) {
        Department entity = new Department();
        if (dTO.getParentId() != null) {
            Department parent = new Department();
            parent.setId(dTO.getParentId().getId());
            entity.setParentId(parent);
        }

        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());
        entity.setId(dTO.getId());

        if (dTO.getId() == null) {
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

        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<Department> list, List<DepartmentDTO> dTOList) {
        for (Department entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    @Override
    public List<DepartmentDTO> getDepartmentsListByDepartmentId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DepartmentDTO addDepartment(DepartmentDTO department, TobyUser tobyUser) {
        Department entity = mapFromDTO(department, tobyUser);

        entity = dao.updateEntity(entity);
        department = mapTODTO(entity, department);
        return department;

    }

    @Override
    public List<DepartmentDTO> getDepartmentsListByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<DepartmentDTO> dtoList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Department> list = dao.findListByQuery("SELECT d FROM Department d WHERE d.companyId.id=:companyId", params);
        returnListDTO(list, dtoList);
        return dtoList;

    }

}

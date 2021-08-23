/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.DepartmentDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface DepartmentService {

    public List<DepartmentDTO> getAllDepartmentsListByBranchId(Integer branchId);

    public List<DepartmentDTO> getDepartmentsListByDepartmentId(Integer id);

    public List<DepartmentDTO> getDepartmentsListByCompanyId(Integer companyId);

    public DepartmentDTO addDepartment(DepartmentDTO department);

    public DepartmentDTO findDepartment(Integer departmentId);

    public DepartmentDTO updateDepartment(DepartmentDTO department);

    public void deleteDepartment(DepartmentDTO department, IsagUser isagUser);

    public DepartmentDTO addDepartment(DepartmentDTO department, IsagUser isagUser);

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.EmployeeReportSearchBean;
import com.toby.dto.EmployeeDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import com.toby.entity.Employee;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface EmployeeService {

    public List<EmployeeDTO> getAllEmployeesListByCompanyId(Integer companyId);

    public List<EmployeeDTO> getAllEmployeesListByCompanyId(Integer companyId, Integer branchId);

    public List<EmployeeDTO> getAllEmployeesListByParentANDUserLogin(Integer employeeId);

    public List<Employee> getAllEmployeeListByCompanyId(Integer companyId);

    public List<Integer> findEmployeeForMe(Integer employeeId);

    public StringBuilder returnEmployeeList(Integer assignedTo, StringBuilder empIds);

    public List<Employee> getAllEmployeesListByBranchId(Integer branchId);

    public List<EmployeeDTO> getAllEmployeesListByBranchIdDTO(Integer branchId);

    public List<EmployeeDTO> getEmployeebyBranchId(Integer branchId, EmployeeReportSearchBean employeeReportSearchBean);

    public EmployeeDTO findEmployeeId(Integer id);

    public EmployeeDTO addEmployee(EmployeeDTO employee);

    public List<Employee> findEmployeeByCode(Integer companyId, Integer code);

    public EmployeeDTO findEmployee(Integer departmentId);

    public EmployeeDTO updateEmployee(EmployeeDTO employee);

    public void deleteEmployee(EmployeeDTO employee, TobyUser tobyUser);

    public EmployeeDTO addEmployee(EmployeeDTO employee, TobyUser tobyUser);

    public void deleteIdfromFellowsQualifications(Integer employeeId);

    public List<EmployeeDTO> getAllEmployeesListByBranchIdAndHrSalaryStructure(Integer branchId);

    public List<EmployeeDTO> getAllEmployeesListByParentId(Integer parentId);

    public Boolean getAllEmployeesIsSuperVisor(Integer employeeId);

    public List<EmployeeDTO> getAllEmployeesListByParentId(Integer companyId, Integer branchId, Integer parentId, boolean isActive);

    public List<EmployeeDTO> getAllEmployeesListByParentAndchields(Integer branchId, Integer parentId);

    public List<Integer> getAllEmployeesListByParentAndchield(Integer branchId, Integer parentId);

    public List<EmployeeDTO> getAllEmployeesListByCompanyIdAndBranchId(Integer companyId, Integer branchId);

    public EmployeeDTO findEmployeeByIDANDBranchID(Integer employeeId, Integer branchId);

    public List<EmployeeDTO> getAllEmployeesListByBranchID(Integer branchId);

    public List<EmployeeDTO> getAllCRMEmployee(Integer branchId);
}

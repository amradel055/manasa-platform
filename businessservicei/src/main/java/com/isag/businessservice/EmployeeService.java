/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.EmployeeReportSearchBean;
import com.isag.dto.EmployeeDTO;
import com.isag.entity.Employee;
import com.isag.entity.IsagUser;
import java.util.List;
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
    
    public  List<EmployeeDTO> getEmployeebyBranchId(Integer branchId , EmployeeReportSearchBean employeeReportSearchBean );

    public EmployeeDTO findEmployeeId(Integer id);

    public EmployeeDTO addEmployee(EmployeeDTO employee);

    public List<Employee> findEmployeeByCode(Integer companyId, Integer code);

    public EmployeeDTO findEmployee(Integer departmentId);

    public EmployeeDTO updateEmployee(EmployeeDTO employee);

    public void deleteEmployee(EmployeeDTO employee, IsagUser isagUser);

    public EmployeeDTO addEmployee(EmployeeDTO employee, IsagUser isagUser);

    public void deleteIdfromFellowsQualifications(Integer employeeId);

    public List<EmployeeDTO> getAllEmployeesListByBranchIdAndHrSalaryStructure(Integer branchId);

    public List<EmployeeDTO> getAllEmployeesListByParentId(Integer parentId);

    public Boolean getAllEmployeesIsSuperVisor(Integer employeeId);

    public List<EmployeeDTO> getAllEmployeesListByParentId(Integer companyId, Integer branchId, Integer parentId);

    public List<EmployeeDTO> getAllEmployeesListByParentAndchields(Integer branchId, Integer parentId);

    public List<Integer> getAllEmployeesListByParentAndchield(Integer branchId, Integer parentId);
    
    public List<EmployeeDTO> getAllEmployeesListByCompanyIdAndBranchId(Integer companyId, Integer branchId);
    
    public List<EmployeeDTO> getAllDriversListByBranchId(Integer branchId,Integer type);

}

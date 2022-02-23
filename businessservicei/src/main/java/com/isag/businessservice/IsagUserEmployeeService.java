/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.EmployeeDTO;
import com.isag.dto.IsagUserEmployeeDTO;
import com.isag.entity.Employee;
import com.isag.entity.IsagUserEmployee;
import com.isag.entity.IsagUserRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface IsagUserEmployeeService {

    public List<EmployeeDTO> getAllEmployeesByUserAndBranch(Integer userId, Integer branchId);
    
    public List<IsagUserEmployeeDTO> getAllIsagUserEmployeesByUserAndBranch(Integer userId, Integer branchId);

    public IsagUserEmployee updateIsagUserEmployee(IsagUserEmployee isagUserEmployeeDTO);

    public void deleteEmployee(IsagUserRole isagUserRole, Employee employeeDTO);

    public void deleteIsagUserEmployee(List<IsagUserEmployee> isagUserEmployeeList);
    
}

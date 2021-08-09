/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.HrSalaryStructureDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface HrSalaryStructureService {
    
    public List<HrSalaryStructureDTO> findAllSalaryStructureByBranchId(Integer BranchId);

    public HrSalaryStructureDTO findSalaryStructure(Integer employeeId);

    public HrSalaryStructureDTO getSalaryStructureByEmployeeId(Integer employeeId);

    public List<HrSalaryStructureDTO> getEmployeesListByEmployeeId(Integer employeeId);

    public void deleteSalaryStructure(HrSalaryStructureDTO salaryStructureDTO, IsagUser isagUser);

    public HrSalaryStructureDTO addAccuralDeduction(HrSalaryStructureDTO salaryStructureDTO, IsagUser isagUser);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters);
    
    public List<HrSalaryStructureDTO> getALLSalaryStructureByBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public Integer getSalaryStructureByEmployeeIdAndDate(Integer employeeId,String month,Integer year);
}

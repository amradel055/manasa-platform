/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.HrSalaryStructureAccuralDeductionDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface HrSalaryStructureAccuralDeductionService {

    public void deleteByEmployeeId(Integer employeeId);

    public List<HrSalaryStructureAccuralDeductionDTO> findHrEmpAccuralDTOBySalaryStructureId(Integer salaryStructureId, Integer type);

    public void deleteHrEmployeeAccuralDeduction(HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTO, IsagUser isagUser);

    public List<HrSalaryStructureAccuralDeductionDTO> addHrEmployeeAccuralDeductionList(List<HrSalaryStructureAccuralDeductionDTO> hrEmployeeAccuralDeductionDTOList, IsagUser isagUser, Integer salaryStructureId);

    public HrSalaryStructureAccuralDeductionDTO addHrEEmployeeAccuralDeduction(HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTO, IsagUser isagUser);

    public void deleteHrSalaryStructureAccuralDeductionList(List<HrSalaryStructureAccuralDeductionDTO> hrSalaryStructureAccuralDeductionDTOList,IsagUser isagUser);
    
    public List<HrSalaryStructureAccuralDeductionDTO> findHrEmpAccuralDTOBySalaryStructureBysalaryStructureId(Integer salaryStructureId);
}

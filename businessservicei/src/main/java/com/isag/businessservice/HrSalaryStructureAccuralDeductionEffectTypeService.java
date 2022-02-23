/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.HrSalaryStructureAccuralDeductionEffectTypeDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface HrSalaryStructureAccuralDeductionEffectTypeService {

    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> findAllSalaryStructureAccuralDeductionEffectTypeByBranchId(Integer BranchId);

    public HrSalaryStructureAccuralDeductionEffectTypeDTO findHrSalaryStructureAccuralDeductionEffectTypeDto(Integer id);

    public HrSalaryStructureAccuralDeductionEffectTypeDTO addHrSalaryStructureAccuralDeductionEffectType(HrSalaryStructureAccuralDeductionEffectTypeDTO accuralDeductionEffectTypeDTO, IsagUser isagUser);

    public void deleteSalaryAccuralDeductionEffectTypeDTO(HrSalaryStructureAccuralDeductionEffectTypeDTO accuralDeductionEffectTypeDTO, IsagUser isagUser);
    
    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> findAllSalaryStructureAccuralDeductionEffectTypeByBranchId(Integer BranchId,Integer SalaryStructureAccuralDeduction,Integer type);

    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> getAllHrSalaryStructureAccuralDeductionEffectTypeByCompanyId(Integer companyId);
    
    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> addHrSalaryStructureAccuralDeductionEffectTypeList(List<HrSalaryStructureAccuralDeductionEffectTypeDTO> accuralDeductionEffectTypeDTOList, Integer hrSalaryStructureAccuralDeductionDtoID, IsagUser isagUser);
    
    public void deleteListSalaryAccuralDeductionEffectTypeDTO(List<HrSalaryStructureAccuralDeductionEffectTypeDTO> accuralDeductionEffectTypeDTOList, IsagUser isagUser);
    
    public List<String> getAccuralDeductionCode(Integer hrSalaryEffect , Integer salaryStructureId , Integer accuralDeductionType);
}

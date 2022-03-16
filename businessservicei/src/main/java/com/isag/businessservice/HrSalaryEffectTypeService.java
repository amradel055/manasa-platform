/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.HrSalaryEffectTypeDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface HrSalaryEffectTypeService {
    
    
     public HrSalaryEffectTypeDTO addSalaryEffectType(HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO, IsagUser isagUser);
     
     public void deleteSalaryEffectType(HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO, IsagUser isagUser);
     
     public List<HrSalaryEffectTypeDTO> findAllSalaryEffectTypeByBranchId(Integer BranchId);
     
     public HrSalaryEffectTypeDTO findSalaryEffectTypeDto(Integer id);
     
     public List<HrSalaryEffectTypeDTO> findExistSalaryEffectType(Integer BranchId, HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO);
     
     public List<HrSalaryEffectTypeDTO> getAllHrSalaryEffectTypeDTOByCompanyId(Integer companyId,Integer type);
}

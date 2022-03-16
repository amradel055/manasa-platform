/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.HrSalaryEffectDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface HrSalaryEffectService {

    public void deleteSalaryEffectType(HrSalaryEffectDTO salaryEffectDTO);

    public HrSalaryEffectDTO addSalaryEffectType(HrSalaryEffectDTO salaryEffectDTO, IsagUser isagUser);

    public HrSalaryEffectDTO findSalaryEffect(Integer id);

    public List<HrSalaryEffectDTO> getAllSalariesEffectTypeByBranchId(Integer branchId);
    
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters);
    
    public List<HrSalaryEffectDTO> getALLHrSalaryEffectByBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public List<HrSalaryEffectDTO> getALLHrSalaryEffectByBranchId(Integer branchId);
}

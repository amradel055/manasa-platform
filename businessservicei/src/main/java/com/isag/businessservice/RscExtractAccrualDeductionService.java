/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscExtractAccuralDeductionDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscExtractRegister;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscExtractAccrualDeductionService {
public List<RscExtractAccuralDeductionDTO> getBranchRscExtractAccuralDeductionByBranch(Integer branchId);
public List<RscExtractAccuralDeductionDTO> getRscExtractAccuralOrDeductionByBranchAndTypeAndExtract(Integer branchId ,Integer type);
public List<RscExtractAccuralDeductionDTO> getRscExtractAccuralOrDeductionByExtractForDelete(Integer ExtractId); 
public RscExtractAccuralDeductionDTO findExtractAccuralDeduction(Integer deduction);
public void deleteExtractAccuralDeduction(RscExtractAccuralDeductionDTO deduction, IsagUser isagUser);
public List<RscExtractAccuralDeductionDTO> getAllRscExtractAccuralDeductionByBranchIdAndserial(Integer branchId, Integer serial);
public void addExtractDeductionList(List<RscExtractAccuralDeductionDTO> accrualDTOlist, RscExtractRegister extractRegister, IsagUser isagUser);
public void addExtractAccuralDeductionList(List<RscExtractAccuralDeductionDTO> deductionDTOlist, RscExtractRegister extractRegister, IsagUser isagUser);
public void deleteExtractAccuralDeductionList(List<RscExtractAccuralDeductionDTO> deductionDTOlist, IsagUser isagUser);
public List<RscExtractAccuralDeductionDTO> getRscExtractAccuralDeductionByExtractId(Integer id);

}

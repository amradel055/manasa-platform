/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscAccrualDeductionService {

    public List<RscAccrualDeductionDTO> getBranchRscAccrualActiveByBranch(Integer branchId,Integer type1,Integer type2);

    public RscAccrualDeductionDTO findRscAccrual(Integer accrualId);

    public RscAccrualDeductionDTO addRscAccrual(RscAccrualDeductionDTO accrualDTO, IsagUser isagUser);

    public void deleteRscAccrual(RscAccrualDeductionDTO accrualDTO, IsagUser isagUser);

    public RscAccrualDeductionDTO getAllRscAccrualByBranchIdAndserial(Integer branchId, Integer serial);

    public List<RscAccrualDeductionDTO> getAllRscAccrualByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId);

    public List<RscAccrualDeductionDTO> getRscAccuralDeductionByBranchAndType(Integer branchId, Integer type);

}

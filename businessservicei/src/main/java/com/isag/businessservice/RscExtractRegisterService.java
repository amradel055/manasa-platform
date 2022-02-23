/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscExtractAccuralDeductionDTO;
import com.isag.dto.RscExtractRegisterDTO;
import com.isag.dto.RscExtractRegisterDetailDTO;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscExtractRegister;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscExtractRegisterService {

    public List<RscExtractRegisterDTO> getBranchRscExtractRegisterActiveByBranch(Integer branchId);

    public RscExtractRegisterDTO findRscExtractRegister(Integer extractId, String uri);

    public RscExtractRegisterDTO addRscExtractRegister(RscExtractRegisterDTO extractDTO,
            List<RscExtractRegisterDetailDTO> rscExtractRegisterDetailDTODeletedList,
            List<RscExtractAccuralDeductionDTO> rscExtractAccuralDTODeletedList,
            List<RscExtractAccuralDeductionDTO> rscExtractDeductionDTODeletedList, IsagUser isagUser);

    public void deleteRscExtractRegister(RscExtractRegisterDTO extractDTO, IsagUser isagUser);

    public List<RscExtractRegisterDTO> getAllRscExtractRegisterByBranchIdAndserial(Integer branchId, Integer serial);
    
    public RscExtractRegisterDTO getAllRscExtractRegisterById(Integer id);

    public List<RscExtractRegisterDTO> getAllRscExtractRegisterByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId);

    public BigDecimal getTotalNetFromOldExtraction(Integer tenderId);

    public RscExtractRegisterDTO updateSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage);

    public RscExtractRegisterDTO updateWorkUnitSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage);

    public RscExtractRegisterDTO updateDeductionSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage);

    public RscExtractRegisterDTO updateAccuralSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage);

    public RscExtractRegisterDTO loadRestriction(RscExtractRegisterDTO extractRegisterDTO);
    
    public RscExtractRegisterDTO loadRestrictionList(List<RscRestrictionQuantitiesDTO> extractRegisterDTOList) ;

    public RscExtractRegisterDTO updateExtractSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage);

    public RscExtractRegisterDTO updateWorkUnitExtractSummary(RscExtractRegisterDTO extractRegisterDTO, Boolean ispercentage);

    public BigDecimal getTotalNetCost(Integer branchId, Integer rscExtractRegisterId);

    public RscExtractRegister updaterscExtractRegister(RscExtractRegister rscExtractRegister);
}

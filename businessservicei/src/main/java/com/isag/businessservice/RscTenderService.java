/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.RscExtractReportSearchBean;
import com.isag.dto.RscTenderDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscTenderService {

    public List<RscTenderDTO> getBranchRscTenderActiveByBranch(Integer branchId, IsagUser isagUser);

    public RscTenderDTO findRscTender(Integer tender, IsagUser isagUser);

    public RscTenderDTO addRscTender(RscTenderDTO tenderDTO, IsagUser isagUser);

    public void deleteRscTender(RscTenderDTO tender, IsagUser isagUser);

    public List<RscTenderDTO> getAllRscTenderByBranchIdAndserial(Integer branchId, Integer serial, IsagUser isagUser);

    public List<RscTenderDTO> getAllRscTenderByBranchId(IsagUser isagUser);

    public List<RscTenderDTO> getAllRscTenderByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId, IsagUser isagUser);

    public void updateSummary(RscTenderDTO rscTenderDTO);

    public RscTenderDTO updateWorkUnitSummary(RscTenderDTO rscTenderDTO);

    public RscTenderDTO updateDeductionAccural(RscTenderDTO rscTenderDTO);

    public RscTenderDTO updateAccuralSummary(RscTenderDTO rscTenderDTO);

    public List<RscTenderDTO> getRscTenderActiveByBranch(Integer branchId, IsagUser isagUser);

    public List<RscTenderDTO> getAllRscExtractReport(RscExtractReportSearchBean rscExtractReportSearchBean, Integer branchId);

}

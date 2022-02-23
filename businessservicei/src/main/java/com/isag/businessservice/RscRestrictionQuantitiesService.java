/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.entityBean.RscExtractReport;
import com.isag.businessservice.reports.searchBean.RscContractExtractionWorkunitSearchBean;
import com.isag.businessservice.reports.searchBean.RscExtractReportSearchBean;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.dto.RscRestrictionQuantitiesDetailDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscRestrictionQuantitiesService {
    
    public void saveRscRestrictionQuantities(RscRestrictionQuantitiesDTO rscRestrictionQuantitiesDTO,IsagUser isagUser);
    
    public List<RscExtractReport> getAllRscExtractReport(RscExtractReportSearchBean rscExtractReportSearchBean, Integer branchId) ;

    public List<RscRestrictionQuantitiesDTO> getBranchRscRestrictionQuantitiesActiveByBranch(Integer branchId);

    public RscRestrictionQuantitiesDTO findRscRestrictionQuantities(Integer restrictionId);
    
    public void updateRscRestrictionQuantities(Integer restrictionId, Integer extractId) ;

    public RscRestrictionQuantitiesDTO addRscRestrictionQuantities(RscRestrictionQuantitiesDTO restrictionQuantitiesDTO, List<RscRestrictionQuantitiesDetailDTO> rscRestrictionQuantitiesDetailsDTOList, List<RscRestrictionQuantitiesDetailDTO> rscRestrictionQuantitiesDetailDeletedList, IsagUser isagUser);

    public String deleteRscRestrictionQuantities(RscRestrictionQuantitiesDTO quantitiesDTO, IsagUser isagUser);

    public List<RscRestrictionQuantitiesDTO> getAllRscRestrictionQuantitiesByBranchIdAndserial(Integer branchId, Integer serial);

    public List<RscRestrictionQuantitiesDTO> getAllRscRestrictionQuantitiesByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId);

    public List<RscRestrictionQuantitiesDTO> getRscRestrictionQuantitiesByBranch(Integer branchId);

    public Set<Integer> getContractByBranch(Integer branchId);

    public List<RscRestrictionQuantitiesDTO> getBranchRscRestrictionQuantitiesActiveByBranch(Integer branchId, RscContractExtractionWorkunitSearchBean extractionWorkunitSearchBean);

}

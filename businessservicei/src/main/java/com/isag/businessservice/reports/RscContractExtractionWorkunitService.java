/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.entityBean.RscGuideDTOViewBean;
import com.isag.businessservice.reports.entityBean.completionRateReportEntity;
import com.isag.businessservice.reports.entityBean.rscExtractJournalReportPrintEntity;
import com.isag.businessservice.reports.searchBean.RscContractExtractionWorkunitSearchBean;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.views.RscContractExtractionWorkunitView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Mostafa
 */
@Remote
public interface RscContractExtractionWorkunitService {

    public List<RscGuideDTOViewBean> getAllRscContractExtractionWorkunitViewBySearchBean(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean,IsagUser isagUser);

    public List<completionRateReportEntity> getAllcompletionRateReportEntityViewBySearchBean(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean);

    public List<RscWorkUnitsGuideDTO> getAllWorkunitViewByContract(Integer contractId, Integer branchId);

    public List<InvOrganizationSiteDTO> getAllInvOrganizationSiteByContract(Integer contractId, Integer branchId);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.entityBean.RscExtractReport;
import com.isag.businessservice.reports.searchBean.ProjectAccountSearchBean;
import com.isag.businessservice.reports.searchBean.RscExtractReportSearchBean;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.RscExtractRegisterDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Mostafa
 */
@Remote
public interface RscExtractReportService {

    public List<RscExtractReport> getAllRscExtractReport(RscExtractReportSearchBean rscExtractReportSearchBean, Integer branchId);

    public List<RscExtractRegisterDTO> getAllRscExtractRegisterReportMB(ProjectAccountSearchBean projectAccountSearchBean);

    public List<InstProjectGuideDTO> getAllRscExtractRegisterDetailReportMB(ProjectAccountSearchBean projectAccountSearchBean);

    public List<RscExtractRegisterDTO> getAllRscExtractRegisterReport(ProjectAccountSearchBean projectAccountSearchBean);


}

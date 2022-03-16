/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.CrmEventsReportSearchBean;
import com.isag.dto.CrmCustomerInterestedProductsDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Mostafa--
 */

@Remote
public interface CrmCutomerProjrctService {
    
     public List<CrmCustomerInterestedProductsDTO> getCrmCustomerInterestedProductsByOrganizationSiteId(Integer branchId, CrmEventsReportSearchBean crmEventsReportSearchBean);
     
}

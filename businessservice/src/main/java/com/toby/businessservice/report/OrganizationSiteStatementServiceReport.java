/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.OrganizationSiteStatementSearchBean;
import com.toby.views.OrganizationSiteStatementView;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface OrganizationSiteStatementServiceReport {

   public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBean(OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean);
   
   public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementBalancesSearchBean(OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean, Integer type);
  
   public Map<Integer , BigDecimal> getAllOrganizationSiteStatementOpeningBalancesSearchBean(OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean);
   
}

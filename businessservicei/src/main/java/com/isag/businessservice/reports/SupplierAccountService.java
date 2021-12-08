/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.SupplierAccountSearchBean;

import com.isag.views.OrganizationSiteStatementView;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface SupplierAccountService {

    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBean(SupplierAccountSearchBean supplierAccountSearchBean);

    public List<OrganizationSiteStatementView> getAllOpeningBalanceBeforeBalanceBySearchBean(SupplierAccountSearchBean supplierAccountSearchBean);
    
    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBeanById(SupplierAccountSearchBean supplierAccountSearchBean);

    public BigDecimal getAccountForOrganizationSite(Integer organizationSiteId);
    
    public BigDecimal getAccountForOrganizationSiteNew(Integer organizationSiteId);
    
    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBeanNew(Integer organizationSiteId, Date dateFrom, Date dateTo);


}

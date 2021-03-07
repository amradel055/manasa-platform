/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.toby.views.OrganizationSiteStatementView;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface SupplierAccountService {

    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBean(SupplierAccountSearchBean supplierAccountSearchBean);
    
    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBeanById(SupplierAccountSearchBean supplierAccountSearchBean);

    public List<OrganizationSiteStatementView> getAllOpeningBalanceBeforeBalanceBySearchBean(SupplierAccountSearchBean supplierAccountSearchBean);
    
    public BigDecimal getAccountForOrganizationSite(Integer organizationSiteId);

}

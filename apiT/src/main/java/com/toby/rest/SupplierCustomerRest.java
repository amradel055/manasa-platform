/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;
import com.toby.API.SupplierDataAPI;
import com.toby.businessservice.report.SupplierAccountService;
import com.toby.businessservice.report.SupplierDataServiceReport;
import com.toby.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.toby.entity.InvOrganizationSite;
import com.toby.views.OrganizationSiteStatementView;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Lenovo
 */
@Path("/customerBalance")
public class SupplierCustomerRest {

    @EJB
    SupplierAccountService supplierAccountService;
        @EJB
        SupplierDataServiceReport supplierDataServiceReport;


    @Transactional
    @Path("/supplierAcountList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<OrganizationSiteStatementView> supplierAcountList(SupplierAccountSearchBean supplierAccountSearchBean) {
        List<OrganizationSiteStatementView> organizationSiteStatementViewList = new ArrayList<>();
        if (supplierAccountSearchBean != null) {
         organizationSiteStatementViewList = supplierAccountService.getAllOpeningBalanceBeforeBalanceBySearchBean(supplierAccountSearchBean);
        }
        return organizationSiteStatementViewList ;
    }
    
    @Transactional
    @Path("/findSupplierData")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvOrganizationSite> findSupplierData(SupplierDataAPI supplierDataAPI) {
        List<InvOrganizationSite> organizationSiteList = new ArrayList<>();
        if (supplierDataAPI != null) {
         organizationSiteList = supplierDataServiceReport.getAllSupplierDataReportSearchBeanAPI(supplierDataAPI);
        }
        return organizationSiteList ;
    }
}

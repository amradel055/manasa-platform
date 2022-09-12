/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;
import com.toby.API.BankBalanceReportAPI;
import com.toby.businessservice.ReportsService;
import com.toby.businessservice.report.PurchaseOfItemsBySuppliersReportService;
import com.toby.views.CompanyInvoicesWithoutSewing;
import com.toby.views.ItemBalancesStatement;
import com.toby.views.SalesOfItemsByCompany;
import com.toby.views.SalesStatementForThePeriod;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/reports")

public class ReportsRest {

    @EJB
    private ReportsService reportsService;
    @EJB
    private PurchaseOfItemsBySuppliersReportService purchaseOfItemsBySuppliersReportService;

    @Transactional
    @Path("/companyInvoicesWithoutSewing")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<CompanyInvoicesWithoutSewing> companyInvoicesWithoutSewing(BankBalanceReportAPI bankBalanceReportAPI) {
        return reportsService.getCompanyInvoicesWithoutSewingAPI(bankBalanceReportAPI);
    }

    @Transactional
    @Path("/ItemBalancesStatements")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ItemBalancesStatement> ItemBalancesStatements(BankBalanceReportAPI bankBalanceReportAPI) {
        return reportsService.getItemBalancesStatementAPI(bankBalanceReportAPI);
    }

    @Transactional
    @Path("/salesOfItemByCompany")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SalesOfItemsByCompany> salesOfItemByCompany(BankBalanceReportAPI bankBalanceReportAPI) {
        return purchaseOfItemsBySuppliersReportService.getAllSalesOfItemsByCompanyAPI(bankBalanceReportAPI);
    }

    @Transactional
    @Path("/FindSalesForPeriod")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SalesStatementForThePeriod> FindSalesForPeriod(BankBalanceReportAPI bankBalanceReportAPI) {
        return reportsService.getSalesStatementForThePeriodAPI(bankBalanceReportAPI);
    }

}

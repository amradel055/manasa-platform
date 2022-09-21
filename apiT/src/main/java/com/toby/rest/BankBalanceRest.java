/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.BankBalanceReportAPI;
import com.toby.API.CashAccountSettlementAPI;
import com.toby.bean.BankBalanceReportBean;
import com.toby.businessservice.report.BankBalanceViewService;
import com.toby.businessservice.report.CashAccountSettlmentService;
import com.toby.views.CashAccountSettlementBean;
import com.toby.views.SubAccountSummaryReport;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/bankBalance")

public class BankBalanceRest {

    @EJB
    private CashAccountSettlmentService cashAccountSettlmentService;

    @EJB
    private BankBalanceViewService bankBalanceViewService;

    @Transactional
    @Path("/BankBalanceSettlement")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SubAccountSummaryReport> FindBankBalanceSettlement(CashAccountSettlementAPI cashAccountSettlementAPI) {
        CashAccountSettlementBean cashAccountSettlementBean = new CashAccountSettlementBean();
        if (cashAccountSettlementAPI != null) {
            cashAccountSettlementBean = cashAccountSettlmentService.searchNewApi(cashAccountSettlementAPI);
        }
        return cashAccountSettlementBean.getCashAccountSettlementReports();

    }

    @Transactional
    @Path("/findBankBalance")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<BankBalanceReportBean> findBankBalance(BankBalanceReportAPI bankBalanceReportAPI) {
        return bankBalanceViewService.searchNativeAPI(bankBalanceReportAPI);

    }

}

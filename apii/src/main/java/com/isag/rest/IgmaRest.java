/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.reports.CashAccountSettlmentService;
import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.config.Secured;
import com.isag.views.CashAccountSettlementBean;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author ahmedEssam <ahmedEssam@isag.com>
 */
@Path("/igmaRest")
public class IgmaRest extends AccountBase {

    private SubAccountSummarySearchBean cashAccountSettlementSearchBean;
    private CashAccountSettlementBean accountSettlementBean;

    @EJB
    CashAccountSettlmentService cashAccountSettlmentService;

    
    @Transactional
    @Secured
    @Path("/customer/{branshSerial}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CashAccountSettlementBean search(Integer branchId) {
        getCashAccountSettlementSearchBean().setBranchId(branchId);
        setAccountSettlementBean(cashAccountSettlmentService.search(getCashAccountSettlementSearchBean()));
        return getAccountSettlementBean();
    }

    /**
     * @return the cashAccountSettlementSearchBean
     */
    public SubAccountSummarySearchBean getCashAccountSettlementSearchBean() {
        return cashAccountSettlementSearchBean;
    }

    /**
     * @param cashAccountSettlementSearchBean the
     * cashAccountSettlementSearchBean to set
     */
    public void setCashAccountSettlementSearchBean(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        this.cashAccountSettlementSearchBean = cashAccountSettlementSearchBean;
    }

    /**
     * @return the accountSettlementBean
     */
    public CashAccountSettlementBean getAccountSettlementBean() {
        return accountSettlementBean;
    }

    /**
     * @param accountSettlementBean the accountSettlementBean to set
     */
    public void setAccountSettlementBean(CashAccountSettlementBean accountSettlementBean) {
        this.accountSettlementBean = accountSettlementBean;
    }

}

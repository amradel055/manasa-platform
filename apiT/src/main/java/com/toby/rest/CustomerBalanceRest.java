/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.BankBalanceReportAPI;
import com.toby.businessservice.ReportsService;
import com.toby.views.CustomerBalances;
import com.toby.views.CustomerBalancesDebtAge;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/customerBalance")

public class CustomerBalanceRest {

    @EJB
    private ReportsService reportsService;


    @Transactional
    @Path("/findCustomerBalance")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<CustomerBalances> findCustomerBalance(BankBalanceReportAPI bankBalanceReportAPI) {
        return reportsService.getCustomerBalancesAPI(bankBalanceReportAPI);

    }
    
    
  @Transactional
    @Path("/findCustomerBalanceDeptAge")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<CustomerBalancesDebtAge> findCustomerBalanceDeptAge(BankBalanceReportAPI bankBalanceReportAPI) {
        return reportsService.getCustomerBalancesDebtAgeAPI(bankBalanceReportAPI);
    }
    
    

}

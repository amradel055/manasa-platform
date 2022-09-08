/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;
import com.toby.API.BankBalanceReportAPI;
import com.toby.businessservice.ReportsService;
import com.toby.views.SalesReportByValueAded;
import com.toby.views.SalesReportByValueAdedDetails;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/salesValueAdded")

public class SalesReportValueAdedRest {

    @EJB
    private ReportsService  reportsService ;
    @Transactional
    @Path("/FindSalesValueAdded")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SalesReportByValueAded> FindSalesValueAdded(BankBalanceReportAPI bankBalanceReportAPI) {
    return reportsService.getSalesReportByValueAdedAPI(bankBalanceReportAPI);

    }
    
    @Transactional
    @Path("/FindSalesValueAddedDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SalesReportByValueAdedDetails> FindSalesValueAddedDetails(BankBalanceReportAPI bankBalanceReportAPI) {
    return reportsService.getSalesReportByValueAdedDetailsAPI(bankBalanceReportAPI);

    }

}

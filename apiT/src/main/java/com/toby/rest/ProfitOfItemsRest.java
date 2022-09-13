/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.ItemsDataAPI;
import com.toby.businessservice.ReportsService;
import com.toby.businessservice.report.SupplierAccountService;
import com.toby.businessservice.report.SupplierDataServiceReport;
import com.toby.views.ProfitOfItemsSold;
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
@Path("/itemsData")
public class ProfitOfItemsRest {

    @EJB
    SupplierAccountService supplierAccountService;
    @EJB
    SupplierDataServiceReport supplierDataServiceReport;
    @EJB
    ReportsService reportsService;

    @Transactional
    @Path("/profitOfItemsSold")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ProfitOfItemsSold> supplierAcountList(ItemsDataAPI itemsDataAPI) {
        List<ProfitOfItemsSold> profitOfItemsSoldList = new ArrayList<>();
        if (itemsDataAPI != null) {
          profitOfItemsSoldList  = reportsService.getProfitOfItemsSoldAPI(itemsDataAPI ,itemsDataAPI.getInvType(),itemsDataAPI.getBranchId(),itemsDataAPI.getInvoiceType());
        }
        return profitOfItemsSoldList;
    }

}

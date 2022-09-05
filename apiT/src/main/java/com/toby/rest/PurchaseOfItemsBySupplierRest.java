/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;
import com.toby.API.SummitionItemAPI;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.report.PurchaseOfItemsBySuppliersReportService;
import com.toby.dto.InvInventoryDTO;
import com.toby.entity.TobyUser;
import com.toby.views.ItemSalesView;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/purchaseBySupplier")

public class PurchaseOfItemsBySupplierRest {

    @EJB
    private PurchaseOfItemsBySuppliersReportService  purchaseOfItemsBySuppliersReportService;

    @EJB
    private TobyUserInventoryService  tobyUserInventoryService;

    
    @Transactional
    @Path("/purchaseSearch")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ItemSalesView> purchaseSearch(SummitionItemAPI summitionItemAPI) {
    return purchaseOfItemsBySuppliersReportService.getAllPurchaseOfItemsBySuppliersSearchBeanNewAPI(summitionItemAPI , null);

    }
    
  

}

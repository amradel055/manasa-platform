/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.BankBalanceReportAPI;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.views.ClientsNoMovment;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ClientNoMovement")

public class ClientNoMovementRest {

    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;

        

    @Transactional
    @Path("/findClientNoMovement")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ClientsNoMovment> findClientNoMovement(BankBalanceReportAPI bankBalanceReportAPI) {
        return invPurchaseInvoiceService_1.findClientsNoMovmentAPI(bankBalanceReportAPI);
    }

}

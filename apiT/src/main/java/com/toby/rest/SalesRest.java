/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.InvInvoiceDTOApi;
import com.toby.API.ResponseApi;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.dto.InvInvoiceDTO;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sales")
public class SalesRest {

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;

    @Transactional
    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(InvInvoiceDTOApi invInvoiceDTOApi) {
        try {
            ResponseApi responseApi = invPurchaseInvoiceService_1.SaveApi(invInvoiceDTOApi);
            return Response.ok().entity(responseApi).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/saveDeskTop")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveDeskTop(InvInvoiceDTOApi invInvoiceDTOApi) {
        try {
            ResponseApi responseApi = invPurchaseInvoiceService_1.SaveApiDeskTop(invInvoiceDTOApi);
            return Response.ok().entity(responseApi).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("findInvPurchaseInvoiceById")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvInvoiceDTO findInvPurchaseInvoiceById(InvInvoiceDTOApi dTO) {
        return this.invPurchaseInvoiceService_1.findInvPurchaseInvoiceById(dTO.getId());
    }

    @Transactional
    @Path("findInvPurchaseInvoiceByserial")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvInvoiceDTO findInvPurchaseInvoiceByserial(InvInvoiceDTOApi dTO) {
        return this.invPurchaseInvoiceService_1.findInvPurchaseInvoiceByserial(dTO.getSerial());
    }
}

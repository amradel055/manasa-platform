/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.InvInvoiceDTOApi;
import com.toby.API.InvoiceDiscDTOApi;
import com.toby.businessservice.InvAddingOrderService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.entity.Branch;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ejb.EJB;

@Path("/permisionOrder/")
public class PermisionOrderRest {

    @EJB
    InvAddingOrderService invAddingOrderService;
    @EJB
    InvPurchaseInvoiceService_1 purchaseInvoiceService_1;

    @Transactional
    @Path("findInvoiceDataDis")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvoiceDiscDTOApi findInvoiceDataDis(InvoiceDiscDTOApi dTO) {
        return this.purchaseInvoiceService_1.findInvoiceDataDis(dTO.getInvSerial());
    }

    @Transactional
    @Path("/saveDeskTop")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvInvoiceDTOApi saveDeskTop(InvInvoiceDTOApi invInvoiceDTOApi) {
        try {
            if (invInvoiceDTOApi.getId() == null && invInvoiceDTOApi.getCustomerId() != null && invInvoiceDTOApi.getGallaryId() != null) {
                TobyUser tobyUser = new TobyUser();
                tobyUser.setId(invInvoiceDTOApi.getCreatedBy());
                TobyCompany tobyCompany = new TobyCompany();
                tobyCompany.setId(invInvoiceDTOApi.getCompanyId());
                tobyUser.setCompanyId(tobyCompany);
                Branch branch = new Branch();
                branch.setId(invInvoiceDTOApi.getBranchId());
                tobyUser.setBranchId(branch);
                InvInvoiceDTOApi invoiceDTO = invAddingOrderService.save(invInvoiceDTOApi, tobyUser);
                return invoiceDTO;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

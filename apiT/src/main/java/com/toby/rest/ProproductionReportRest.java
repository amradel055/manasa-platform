/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.InvInvoiceDTOApi;
import com.toby.businessservice.InvoiceMovementReportService;
import com.toby.views.InvoiceMovementReport;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/proproductionreport")
public class ProproductionReportRest {

    @EJB
    InvoiceMovementReportService invoiceMovementReportService;

    @Transactional
    @Path("/invoiceMovementReport")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvoiceMovementReport> invoiceMovementReport(InvInvoiceDTOApi invoiceDTOApi) {
        try {
            if (invoiceDTOApi.getSerial() != null && invoiceDTOApi.getBranchId() != null) {
                List<InvoiceMovementReport> list = invoiceMovementReportService.searchReportAPI(invoiceDTOApi.getSerial(),invoiceDTOApi.getBranchId());
                return list;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    @Transactional
    @Path("/invoiceProductionStages")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvoiceMovementReport> invoiceProductionStages(InvInvoiceDTOApi invoiceDTOApi) {
        try {
            if (invoiceDTOApi.getSerial() != null && invoiceDTOApi.getBranchId() != null) {
                List<InvoiceMovementReport> list = invoiceMovementReportService.searchReportAPI(invoiceDTOApi.getSerial(),invoiceDTOApi.getBranchId());
                return list;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}

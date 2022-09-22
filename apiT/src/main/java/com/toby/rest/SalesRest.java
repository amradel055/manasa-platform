/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.InvDetailsDTOAPI;
import com.toby.API.InvInvoiceDTOApi;
import com.toby.API.InvoiceDiscDTOApi;
import com.toby.API.ResponseApi;
import com.toby.businessservice.InvInventoryService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.dto.InvInvoiceDTO;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ejb.EJB;

@Path("/sales/")
public class SalesRest {

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    InvInventoryService inventoryService;

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
    @Path("/offerone")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvDetailsDTOAPI> offerone(InvInvoiceDTOApi invInvoiceDTOApi) {
        try {
            if (invInvoiceDTOApi.getInvoiceDetailApiList() != null && !invInvoiceDTOApi.getInvoiceDetailApiList().isEmpty()) {
                if (invInvoiceDTOApi.getGalleryType() != null && invInvoiceDTOApi.getGalleryType() == 1) {
                    List<InvDetailsDTOAPI> list = invPurchaseInvoiceService_1.offer999Les16Les20Only(invInvoiceDTOApi.getInvoiceDetailApiList());
                    return list;
                } else if (invInvoiceDTOApi.getGalleryType() != null && invInvoiceDTOApi.getGalleryType() == 0) {
                    List<InvDetailsDTOAPI> list = invPurchaseInvoiceService_1.offer999Les16Les20OnlyPrinceTwo(invInvoiceDTOApi.getInvoiceDetailApiList());
                    return list;
                }
                return invInvoiceDTOApi.getInvoiceDetailApiList();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Path("/offeroneNew")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvDetailsDTOAPI> offerone(List<InvDetailsDTOAPI> invoiceDetailApiList) {
        try {
            if (invoiceDetailApiList != null && !invoiceDetailApiList.isEmpty()) {
                List<InvDetailsDTOAPI> invoiceDTO = invPurchaseInvoiceService_1.offer999Les16Les20Only(invoiceDetailApiList);
                return invoiceDTO;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Path("/saveDeskTop")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvInvoiceDTOApi saveDeskTop(InvInvoiceDTOApi invInvoiceDTOApi) {
        try {
            if (invInvoiceDTOApi.getId() == null && invInvoiceDTOApi.getCustomerId() != null && invInvoiceDTOApi.getGallaryId() != null) {
                InvInvoiceDTOApi invoiceDTO = invPurchaseInvoiceService_1.SaveApiDeskTop(invInvoiceDTOApi);
                return invoiceDTO;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Path("/tarhilSalesAPI")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvInvoiceDTOApi tarhilSalesAPI(InvInvoiceDTOApi invInvoiceDTOApi) {
        try {
            System.err.println(" -- 01 -> " + new Date());
            if (invInvoiceDTOApi.getId() != null && invInvoiceDTOApi.getCustomerId() != null && invInvoiceDTOApi.getGallaryId() != null) {
                invPurchaseInvoiceService_1.updateTarhil(invInvoiceDTOApi.getId());
                System.err.println(" -- 04 -> " + new Date());
                InvInvoiceDTO invInvoiceDTO = invPurchaseInvoiceService_1.mapToDTOApiDeskTop(invInvoiceDTOApi, false);
                System.err.println(" -- 05 -> " + new Date());
                InvInvoiceDTOApi invInvoiceDTOAPI = invPurchaseInvoiceService_1.tarhilSalesAPI(invInvoiceDTO);
                System.err.println(" -- 20 -> " + new Date());
                return invInvoiceDTOAPI;
            }
            return null;
        } catch (Exception e) {
            return null;
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
    public InvInvoiceDTOApi findInvPurchaseInvoiceByserial(InvInvoiceDTOApi dTO) {
        return this.invPurchaseInvoiceService_1.findInvPurchaseInvoiceByserialAPI(dTO.getSerial(), dTO.getGallaryId(), dTO.getBranchId());
    }
}

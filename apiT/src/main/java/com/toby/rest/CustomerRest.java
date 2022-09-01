/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.report.SupplierAccountService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.views.OrganizationSiteStatementView;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Lenovo
 */
@Path("/customer")
public class CustomerRest {

    @EJB
    SupplierAccountService supplierAccountService;
    @EJB
    OrganizationSiteService organizationSiteService;
    @EJB
    AutoCompleteService autoCompleteService;
    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;

    @Transactional
    @Path("/customerBalance")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<OrganizationSiteStatementView> customerBalancesList(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        List<OrganizationSiteStatementView> statementViews = new ArrayList<>();
        if (invOrganizationSiteDTO != null) {
            statementViews = supplierAccountService.getStatementForOrganizationSite(invOrganizationSiteDTO.getId());
        }
        return statementViews;
    }

    @Transactional
    @Path("/customerDate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvOrganizationSiteDTO customerDate(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
        if (invOrganizationSiteDTO != null) {
            siteDTO = organizationSiteService.findOrganizationSiteDTOByCodeAndGallaryIdDTO(invOrganizationSiteDTO.getBranchId(), invOrganizationSiteDTO.getCode(), invOrganizationSiteDTO.getGallaryIdAPI());
        }
        return siteDTO;
    }

    @Transactional
    @Path("/findBy")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findCustomersByQuery(InvOrganizationSiteDTO dto){
        try {
            List<InvOrganizationSiteDTO> invOrganizationSiteDTOs = autoCompleteService.autoCompleteOrganizationSiteDTOAnotherBranchAnotherGallary(dto.getBranchId(), dto.getPhone(), dto.getGallaryIdAPI());
            GenericEntity<List<InvOrganizationSiteDTO>> list = new GenericEntity<List<InvOrganizationSiteDTO>>(invOrganizationSiteDTOs) {
            };
            return Response.ok(list).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/findCustomerInvoicesBy")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findCustomersInvoicesByQuery(InvOrganizationSiteDTO dto) {
        try {
            List<InvInvoiceDTO> invoiceDTOs = invPurchaseInvoiceService_1.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary2(4, dto.getGallaryIdAPI(), dto.getId());
            GenericEntity<List<InvInvoiceDTO>> list = new GenericEntity<List<InvInvoiceDTO>>(invoiceDTOs) {
            };
            return Response.ok(list).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }


}

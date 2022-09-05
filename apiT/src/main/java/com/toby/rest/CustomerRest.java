/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.InvOrganizationSiteDTOAPI;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.report.SupplierAccountService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.Branch;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.OrganizationSiteStatementView;
import java.math.BigDecimal;
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

    // رصيد العميل
    @Transactional
    @Path("/customerBalance")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public BigDecimal customerBalancesList(InvOrganizationSiteDTOAPI invOrganizationSiteDTO) {
        BigDecimal statementViews = BigDecimal.ZERO;
        if (invOrganizationSiteDTO != null) {
            statementViews = supplierAccountService.getAccountForOrganizationSite(invOrganizationSiteDTO.getId());
        }
        return statementViews;
    }

    // كشف حساب العميل
    @Transactional
    @Path("/statementForOrganizationSite")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<OrganizationSiteStatementView> statementForOrganizationSite(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        List<OrganizationSiteStatementView> statementViews = new ArrayList<>();
        if (invOrganizationSiteDTO != null) {
            statementViews = supplierAccountService.getStatementForOrganizationSite(invOrganizationSiteDTO.getId());
        }
        return statementViews;
    }

    // البحث عن العميل بكوده او موبايله او جزء من اسمه
    @Transactional
    @Path("/listCustomerData")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<InvOrganizationSiteDTO> customerData(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        List<InvOrganizationSiteDTO> siteDTO = new ArrayList<>();
        if (invOrganizationSiteDTO != null) {
            siteDTO = autoCompleteService.autoCompleteOrganizationSiteDTO(invOrganizationSiteDTO.getBranchId(), Boolean.TRUE, 0, invOrganizationSiteDTO.getCode(), invOrganizationSiteDTO.getGallaryIdAPI());
        }
        return siteDTO;
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
    public Response findCustomersByQuery(InvOrganizationSiteDTOAPI dto) {
        try {
//            List<InvOrganizationSiteDTO> invOrganizationSiteDTOs = autoCompleteService.autoCompleteOrganizationSiteDTOAnotherBranchAnotherGallary(dto.getBranchId(), dto.getPhone(), dto.getGallaryIdAPI());
            List<InvOrganizationSiteDTOAPI> invOrganizationSiteDTOs = autoCompleteService.autoCompleteOrganizationSiteDTOAPI(dto.getBranchId(), Boolean.TRUE, 0, dto.getCode(), dto.getGallaryIdAPI());
            GenericEntity<List<InvOrganizationSiteDTOAPI>> list = new GenericEntity<List<InvOrganizationSiteDTOAPI>>(invOrganizationSiteDTOs) {
            };
            return Response.ok(list).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    // فواتير العميل
    @Transactional
    @Path("/findCustomerInvoicesBy")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findCustomersInvoicesByQuery(InvOrganizationSiteDTOAPI dto) {
        try {
            List<InvInvoiceDTO> invoiceDTOs = invPurchaseInvoiceService_1.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary2(4, dto.getGallaryIdAPI(), dto.getId());
            GenericEntity<List<InvInvoiceDTO>> list = new GenericEntity<List<InvInvoiceDTO>>(invoiceDTOs) {
            };
            return Response.ok(list).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/addNewCustomer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewCustomer(InvOrganizationSiteDTOAPI dto) {

        try {

            TobyUser user = new TobyUser();

            user.setId(dto.getCreatedBy());

            Branch branch = new Branch();
            branch.setId(dto.getBranchId());
            user.setBranchId(branch);

            TobyCompany company = new TobyCompany();
            company.setId(dto.getCompanyId());
            user.setCompanyId(company);

            InvOrganizationSiteDTO dto1 = organizationSiteService.addNewCustomerAPI(dto, user, dto.getAccountIdAPI());
            return Response.ok(dto1).build();

        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }
    }

    @Transactional
    @Path("/updateCustomer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(InvOrganizationSiteDTOAPI dto) {

        try {
            organizationSiteService.updateOrganizationSite(dto);
            return Response.ok(dto).build();

        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }
    }

}

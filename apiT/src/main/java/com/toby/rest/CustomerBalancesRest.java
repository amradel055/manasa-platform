/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;
import com.toby.businessservice.report.SupplierAccountService;
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
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Lenovo
 */
@Path("/customerBalance")
public class CustomerBalancesRest {

    @EJB
    SupplierAccountService supplierAccountService;

    @Transactional
    @Path("/customerBalanceList")
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
}

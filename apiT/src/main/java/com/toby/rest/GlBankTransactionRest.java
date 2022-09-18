/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.GlBankTransactionAPI;
import com.toby.API.GlPayDTOAPI;
import com.toby.businessservice.GlBankTransactionDetailsService;
import com.toby.businessservice.GlBankTransactionService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.entity.Branch;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/glBankTransaction")

public class GlBankTransactionRest {

    @EJB
    private GlBankTransactionService glBankTransactionService;
    @EJB
    private GlBankTransactionDetailsService glBankTransactionDetailsService;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;

    @Transactional
    @Path("/saveGlBankTransaction")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GlBankTransactionAPI saveGlBankTransaction(GlBankTransactionAPI glBankTransactionAPI) {
        GlBankTransactionAPI bankTransactionAPI = glBankTransactionService.addCashAPI(glBankTransactionAPI);
        TobyUser tobyUser1 = new TobyUser();
        Branch branch = new Branch();
        branch.setId(glBankTransactionAPI.getBranchId());
        tobyUser1.setBranchId(branch);
        TobyCompany tobyCompany = new TobyCompany();
        tobyCompany.setId(glBankTransactionAPI.getCompanyId());
        tobyUser1.setCompanyId(tobyCompany);

        return bankTransactionAPI;
    }

    @Transactional
    @Path("/tarhilGlPayDTOAPI")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<GlPayDTOAPI> tarhilSalesAPI(List<GlPayDTOAPI> glPayDTOAPIList) {
        try {

            for (GlPayDTOAPI glPayDTOAPI : glPayDTOAPIList) {
                TobyUser tobyUser1 = new TobyUser();
                Branch branch = new Branch();
                branch.setId(182);
                tobyUser1.setBranchId(branch);
                TobyCompany tobyCompany = new TobyCompany();
                tobyCompany.setId(125);
                tobyUser1.setCompanyId(tobyCompany);
                tobyUser1.setId(201);

                if (glPayDTOAPI.getId() != null) {
                    glBankTransactionDetailsService.tarhilAfterSave(glPayDTOAPI.getId(), tobyUser1);
                }
            }
            return glPayDTOAPIList;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Transactional
    @Path("/tarhilGlPayDTOAPINew")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<GlPayDTOAPI> tarhilSalesAPINew(GlBankTransactionAPI glBankTransactionAPI) {
        try {

            for (GlPayDTOAPI glPayDTOAPI : glBankTransactionAPI.getGlPayDTOAPIList()) {
                TobyUser tobyUser1 = new TobyUser();
                Branch branch = new Branch();
                branch.setId(182);
                tobyUser1.setBranchId(branch);
                TobyCompany tobyCompany = new TobyCompany();
                tobyCompany.setId(125);
                tobyUser1.setCompanyId(tobyCompany);
                tobyUser1.setId(201);

                if (glPayDTOAPI.getId() != null) {
                    glBankTransactionDetailsService.tarhilAfterSave(glPayDTOAPI.getId(), tobyUser1);
                }
            }
            return glBankTransactionAPI.getGlPayDTOAPIList();
        } catch (Exception e) {
            return null;
        }
    }

}

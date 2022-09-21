/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.GlBankTransactionAPI;
import com.toby.businessservice.GlBankTransactionDetailsService;
import com.toby.businessservice.GlBankTransactionService;
import com.toby.dto.GlBankTransactionDTO;
import com.toby.entity.Branch;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
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

}

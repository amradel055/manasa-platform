/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;
import com.toby.API.GlBankTransactionAPI;
import com.toby.businessservice.GlBankTransactionService;
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

    
    
    @Transactional
    @Path("/saveGlBankTransaction")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GlBankTransactionAPI saveGlBankTransaction(GlBankTransactionAPI glBankTransactionAPI) {
    return glBankTransactionService.addCashAPI(glBankTransactionAPI);

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.businessservice.TobyUserBankService;
import com.toby.dto.GlBankDTO;
import com.toby.dto.GlPayDTO;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/bank")

public class GlbankRest {

    @EJB
    private TobyUserBankService tobyUserBankService;

    @Transactional
    @Path("bankUserList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<GlBankDTO> findBankUserList(GlBankDTO dTO) {
        return this.tobyUserBankService.getAllGlBankForUserByTypeAndBranchIdPerDTO(dTO.getId(), dTO.getBranchId(), null, null);
    }

    @Transactional
    @Path("glPayDTOList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<GlPayDTO> findGlPayDTOUserList(GlBankDTO dTO) {
        return this.tobyUserBankService.getGlPayDTOForUserByTypeAndBranchIdPerDTO(dTO.getId(), dTO.getBranchId(), null, null);
    }

}

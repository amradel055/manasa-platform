package com.toby.rest;

import com.toby.API.ResponseApi;
import com.toby.businessservice.GeneraljournalServiceDTO;
import com.toby.dto.GeneralJournalDTO;
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
import javax.ws.rs.core.Response;
//import sun.misc.IOUtils;

/**
 *
 * @author Lenovo
 */
@Path("/generaljournal")
public class GeneralJournalRest {

    @EJB
    GeneraljournalServiceDTO generaljournalServiceDTO;

    @Transactional
    @Path("/addOfferDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGeneralJournal(GeneralJournalDTO generalJournalDTO) {
        try {
            TobyUser tobyUser = new TobyUser();
            tobyUser.setId(generalJournalDTO.getCreatedBy());
            Branch branch = new Branch();
            branch.setId(generalJournalDTO.getBranchId());
            tobyUser.setBranchId(branch);
            TobyCompany company = new TobyCompany();
            company.setId(generalJournalDTO.getCompanyId());
            tobyUser.setCompanyId(company);
            ResponseApi responseApi = generaljournalServiceDTO.save(generalJournalDTO, tobyUser);
            return Response.ok(responseApi).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

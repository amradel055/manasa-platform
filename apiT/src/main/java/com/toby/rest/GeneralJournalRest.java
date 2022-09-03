package com.toby.rest;

import com.toby.API.GeneralJournalDTOAPI;
import com.toby.API.GeneralJournalDetailsDTOAPI;
import com.toby.businessservice.GeneraljournalDetailsServiceDTO;
import com.toby.businessservice.GeneraljournalServiceDTO;
import com.toby.dto.GeneralJournalDTO;
import com.toby.entity.Branch;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
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
//import sun.misc.IOUtils;

/**
 *
 * @author Lenovo
 */
@Path("/generaljournal")
public class GeneralJournalRest {

    @EJB
    GeneraljournalServiceDTO generaljournalServiceDTO;
    @EJB
    GeneraljournalDetailsServiceDTO generaljournalDetailsServiceDTO;

    @Transactional
    @Path("/addgeneraljournal")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralJournalDTOAPI addGeneralJournal(GeneralJournalDTO generalJournalDTO) {
        try {
            TobyUser tobyUser = new TobyUser();
            tobyUser.setId(generalJournalDTO.getCreatedBy());
            Branch branch = new Branch();
            branch.setId(generalJournalDTO.getBranchId());
            tobyUser.setBranchId(branch);
            TobyCompany company = new TobyCompany();
            company.setId(generalJournalDTO.getCompanyId());
            tobyUser.setCompanyId(company);
            GeneralJournalDTOAPI responseApi = generaljournalServiceDTO.save(generalJournalDTO, tobyUser);
            return responseApi;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Path("/findgeneraljournal")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GeneralJournalDTOAPI findGeneralJournal(GeneralJournalDTO generalJournalDTO) {
        try {
            if (generalJournalDTO.getSerial() != null || generalJournalDTO.getGeneralDecument() != null) {
                GeneralJournalDTOAPI entity = generaljournalServiceDTO.getGeneralJournalDTOByBranchIdAndSerialAPI(generalJournalDTO.getSerial(), generalJournalDTO.getGeneralDecument(), generalJournalDTO.getGlYearId(), generalJournalDTO.getBranchId());
                List<GeneralJournalDetailsDTOAPI> detailList = generaljournalDetailsServiceDTO.getAllJournalDetailsForJorunalAPI(entity.getId());
                entity.setDetailDTOList(detailList);
                return entity;
            } else {
                GeneralJournalDTOAPI journalDTO = new GeneralJournalDTOAPI();
                journalDTO.setMsg("branch or User id not found");
                return journalDTO;
            }

        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Path("/findgeneraljournalList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findGeneralJournalList(GeneralJournalDTO generalJournalDTO) {
        try {
            if (generalJournalDTO.getSerial() != null || generalJournalDTO.getGeneralDecument() != null) {
                List<GeneralJournalDTO> journalDTOList = generaljournalServiceDTO.getGeneralJournalDTOByBranchIdAndSerial(generalJournalDTO.getSerial(), generalJournalDTO.getGeneralDecument(), generalJournalDTO.getGlYearId(), generalJournalDTO.getBranchId());
                GeneralJournalDTO journalDTO = null;
                if (journalDTOList != null && !journalDTOList.isEmpty()) {
                    journalDTO = journalDTOList.get(0);
                    journalDTO.setDetailDTOList(generaljournalDetailsServiceDTO.getAllJournalDetailsForJorunal(journalDTO.getId()));
                }
                List<GeneralJournalDTO> listNew = new ArrayList<>();
                listNew.add(journalDTO);
                GenericEntity<List<GeneralJournalDTO>> list = new GenericEntity<List<GeneralJournalDTO>>(listNew) {
                };
                return Response.ok(list).build();
            } else {
                GeneralJournalDTO journalDTO = new GeneralJournalDTO();
                journalDTO.setMsg("branch or User id not found");
                return Response.ok().entity(journalDTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

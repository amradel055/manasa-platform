package com.toby.rest;

import com.toby.API.SymbolDTOAPI;
import com.toby.businessservice.SymbolService;
import com.toby.config.UserLoginData;
import com.toby.dto.SymbolDTO;
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
@Path("/symbol")
public class SymbolRest {

    @EJB
    SymbolService symbolService;

    @Transactional
    @Path("/documentstypes")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SymbolDTOAPI> findDocumentsTypesDTO(UserLoginData user) {
        try {
            if (user.getCompanyId() != null) {
                List<SymbolDTOAPI> listDocument = symbolService.getDocumentsTypesDTOAPI(user.getCompanyId());
                return listDocument;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Path("/documentstypesOld")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findDocumentsTypesDTOOld(UserLoginData user) {
        try {
            if (user.getCompanyId() != null) {
                List<SymbolDTO> listDocument = symbolService.getDocumentsTypesDTO(user.getCompanyId());
                GenericEntity<List<SymbolDTO>> list = new GenericEntity<List<SymbolDTO>>(listDocument) {
                };
                return Response.ok(list).build();
            } else {
                SymbolDTO dTO = new SymbolDTO();
                dTO.setMsg("company id not found");
                return Response.ok().entity(dTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/crmStatus")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SymbolDTO> findcrmStatus(SymbolDTO symbolDTO) {
        List<SymbolDTO> symbolDTOList = new ArrayList<>();
        if (symbolDTO.getCompanyId() != null) {
            symbolDTOList = symbolService.getCrmStatusByCompanyIdDTO(symbolDTO.getCompanyId());
        }
        return symbolDTOList;
    }

    @Transactional
    @Path("/crmPeriority")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SymbolDTO> FindcrmPeriority(SymbolDTO symbolDTO) {
        List<SymbolDTO> symbolDTOList = new ArrayList<>();
        if (symbolDTO.getCompanyId() != null) {
            symbolDTOList = symbolService.getCrmPeriorityByCompanyIdDTO(symbolDTO.getCompanyId());
        }
        return symbolDTOList;
    }

    @Transactional
    @Path("/learnAboutFrom")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SymbolDTO> FindcrmLearnAboutFrom(SymbolDTO symbolDTO) {
        List<SymbolDTO> symbolDTOList = new ArrayList<>();
        if (symbolDTO.getCompanyId() != null) {
            symbolDTOList = symbolService.getCrmLearnAboutFromByCompanyIdDTO(symbolDTO.getCompanyId());
        }
        return symbolDTOList;
    }

    @Transactional
    @Path("/workType")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SymbolDTO> FindcrmWorkTypeSymbolDTO(SymbolDTO symbolDTO) {
        List<SymbolDTO> symbolDTOList = new ArrayList<>();
        if (symbolDTO.getCompanyId() != null) {
            symbolDTOList = symbolService.getAllworkTypeIdByCompanyId(symbolDTO.getCompanyId());
        }
        return symbolDTOList;
    }

    @Transactional
    @Path("/eventType")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<SymbolDTO> FindcrEventTypeSymbolDTO(SymbolDTO symbolDTO) {
        List<SymbolDTO> symbolDTOList = new ArrayList<>();
        if (symbolDTO.getCompanyId() != null) {
            symbolDTOList = symbolService.getCrmEventTypeFromByCompanyIdDTO(symbolDTO.getCompanyId());
        }
        return symbolDTOList;
    }
}

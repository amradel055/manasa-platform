package com.toby.rest;
import com.toby.businessservice.TobyUserService;
import com.toby.dto.TobyUserDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//import sun.misc.IOUtils;

/**
 *
 * @author Lenovo
 */
@Path("/tobyUser")
public class TobyUserRest {

    @EJB
    TobyUserService tobyUserService;

   

    @Transactional
    @Path("/findtobyUserDtoList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TobyUserDTO> FindtobyUserList(TobyUserDTO tobyUserDTO) {
        List<TobyUserDTO> tobyUserDTOList = new ArrayList<>();
        if (tobyUserDTO.getCompanyId() != null) {
            tobyUserDTOList = tobyUserService.getUsersByCompanyId(tobyUserDTO.getCompanyId());
        }
        return tobyUserDTOList;
    }
}

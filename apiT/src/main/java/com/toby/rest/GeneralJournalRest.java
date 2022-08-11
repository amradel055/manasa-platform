package com.toby.rest;

import com.toby.businessservice.GlAccountServiceDTO;
import com.toby.dto.GlAccountDTO;
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
    GlAccountServiceDTO glAccountServiceDTO;

    

}

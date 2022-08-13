package com.toby.rest;

import com.toby.businessservice.GlAccountServiceDTO;
import javax.ejb.EJB;
import javax.ws.rs.Path;
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

package com.toby.rest;

import com.toby.businessservice.EmployeeService;
import com.toby.dto.EmployeeDTO;
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
@Path("/employee")
public class EmployeeRest {

    @EJB
    EmployeeService employeeService;

   

    @Transactional
    @Path("/findEmployeeList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<EmployeeDTO> FindfindEmployeeList(EmployeeDTO employeeDTO) {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        if (employeeDTO.getCompanyId() != null) {
            employeeDTOList = employeeService.getAllEmployeesListByCompanyId(employeeDTO.getCompanyId());
        }
        return employeeDTOList;
    }
}

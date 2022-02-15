/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.apiServices.AttributeDetailsService;
import com.toby.dto.AttributeDTO;
import com.toby.dto.AttributeDetailsDTO;
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

/**
 *
 * @author Lenovo
 */
@Path("/attributesDetails")
public class AttributeDetailsRest {

    @EJB
    AttributeDetailsService attributeDetailsService;

    @Transactional
    @Path("/addAttributeDetail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAttributeDetails(AttributeDetailsDTO attributeDetailsDTO) {
        try {
            attributeDetailsDTO = valitade(attributeDetailsDTO);
            if (attributeDetailsDTO.getMsg() == null) {
                attributeDetailsDTO = attributeDetailsService.addAtrributeDetails(attributeDetailsDTO);
            }
            return Response.ok(attributeDetailsDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/updateAttributeDetail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAttributeDetails(AttributeDetailsDTO attributeDetailsDTO) {
        try {

            if (attributeDetailsDTO.getId() != null) {
                attributeDetailsDTO = valitade(attributeDetailsDTO);
                if (attributeDetailsDTO.getMsg() == null) {
                    attributeDetailsDTO = attributeDetailsService.updateAttributeDetails(attributeDetailsDTO);
                }
            } else {
                attributeDetailsDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(attributeDetailsDTO).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteAttributeDetail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAttributeDetails(AttributeDetailsDTO attributeDetailsDTO) {
        try {
            if (attributeDetailsDTO.getId() != null) {
                attributeDetailsService.DeleteAttributeDetails(attributeDetailsDTO);
            } else {
                attributeDetailsDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(attributeDetailsDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    @Transactional
    @Path("/deleteAttributeDetailByHead")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAttributeDetailsForHead(AttributeDTO attributeDTO) {
        try {
            if (attributeDTO.getId() != null && attributeDTO.getCompanyId()!= null && attributeDTO.getBranchId()!= null) {
                attributeDetailsService.DeleteAttributeDetailsForHeadByBranchId(attributeDTO);
            } else {
                attributeDTO.setMsg("يجب اختيار عنصر");
            }

            return Response.ok().entity(attributeDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    

    @Transactional
    @Path("/allAttributesDetails")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findAttributeDetailsByBranch(AttributeDetailsDTO attributeDetailsDTO) {
        try {
            if (attributeDetailsDTO.getBranchId() != null && attributeDetailsDTO.getCompanyId() != null) {
                List<AttributeDetailsDTO> attributesList = attributeDetailsService.getAllAttributesDetailsByCompanyIdAndBranchId(attributeDetailsDTO);
                GenericEntity<List<AttributeDetailsDTO>> list = new GenericEntity<List<AttributeDetailsDTO>>(attributesList) {
                };
                return Response.ok(list).build();
            } else {
                AttributeDetailsDTO attributeDTO = new AttributeDetailsDTO();
                attributeDTO.setMsg("branch or company id not found");
                return Response.ok().entity(attributeDTO).build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
//    @Transactional
//    @Path("/allAttributesDetailsByProduct")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response findAttributeDetailsByBranchAndProduct(AttributeDetailsDTO attributeDetailsDTO) {
//        try {
//            if (attributeDetailsDTO.getBranchId() != null && attributeDetailsDTO.getCompanyId() != null && attributeDetailsDTO.getItemId() != null) {
//                List<AttributeDetailsDTO> attributesList = attributeDetailsService.getAllAttributesDetailsByCompanyIdAndBranchId(attributeDetailsDTO);
//                GenericEntity<List<AttributeDetailsDTO>> list = new GenericEntity<List<AttributeDetailsDTO>>(attributesList) {
//                };
//                return Response.ok(list).build();
//            } else {
//                AttributeDetailsDTO attributeDTO = new AttributeDetailsDTO();
//                attributeDTO.setMsg("branch or company id or item id not found");
//                return Response.ok().entity(attributeDTO).build();
//            }
//
//        } catch (Exception e) {
//            return Response.status(Response.Status.FORBIDDEN).build();
//        }
//    }


    public AttributeDetailsDTO valitade(AttributeDetailsDTO dTO) {

        if (dTO.getValue()== null || dTO.getValue().isEmpty() ){
            dTO.setMsg("يجب ادخال الاسم");
        }
//         if (dTO.getItemId()== null){
//            dTO.setMsg("يجب ادخال صنف");
//        }
         if (dTO.getHeadId() == null){
            dTO.setMsg("يجب اختيار صنف رئيسي");
        }
        if (dTO.getCompanyId() == null || dTO.getBranchId() == null) {
            dTO.setMsg("يجب تسجيل الدخول ");
        }
        return dTO;
    }
}

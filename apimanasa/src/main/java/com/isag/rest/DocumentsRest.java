/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.DocumentsService;
import com.isag.config.UserLoginData;
import com.isag.dto.DocumentsDTO;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author AhmedEssam <ahmedEssam.com>
 */
@Path("/documents")
public class DocumentsRest {
    
    private UserLoginData userLoginData;
    @EJB
    DocumentsService documentsService;
    
    @Transactional
    @Path("/allDocuments")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response allDocuments(DocumentsDTO lessonId) {
        if (lessonId != null) {
            try {
                List<DocumentsDTO> dTOs = documentsService.findDocumentsByLessonId(lessonId.getLessonId());
                GenericEntity<List<DocumentsDTO>> list = new GenericEntity<List<DocumentsDTO>>(dTOs) {
                };
                return Response.ok(list).build();
                
            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            
        } else {
            lessonId = new DocumentsDTO();
            lessonId.setMsg("No Id is Sent");
            return Response.ok(lessonId).build();
        }
    }
    
    @Transactional
    @Path("/saveDocuments")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveDocuments(DocumentsDTO documentsDTO) {
        DocumentsDTO msg;
        try {
            msg = documentsService.save(documentsDTO, null);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    @Transactional
    @Path("/deleteDocuments")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteDocuments(DocumentsDTO documentsDTO) {
        DocumentsDTO msg = new DocumentsDTO();
        try {
            documentsService.deleteDocuments(documentsDTO.getId());
            msg.setMsg("تم الحذف بنجاح");
            msg.setMsgType("success");
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    public UserLoginData getUserLoginData(@Context ContainerRequestContext crc) {
        userLoginData = (UserLoginData) crc.getProperty("userLogInData");
        return userLoginData;
    }

    /**
     * @return the userLoginData
     */
    public UserLoginData getUserLoginData() {
        return userLoginData;
    }

    /**
     * @param userLoginData the userLoginData to set
     */
    public void setUserLoginData(UserLoginData userLoginData) {
        this.userLoginData = userLoginData;
    }
    
}

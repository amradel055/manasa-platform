/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.api.dto.TeacherDataAPI;
import com.isag.businessservice.IgmaUserService;
import com.isag.businessservice.MaterialsService;
import com.isag.businessservice.TeachersService;
import com.isag.config.UserLoginData;
import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.MaterialsDTO;
import com.isag.dto.TeachersDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
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
@Path("/teachers")
public class TeachersRest {

    private UserLoginData userLoginData;
    @EJB
    TeachersService teachersService;
    @EJB
    IgmaUserService igmaUserService;
    @EJB
    MaterialsService materialsService;
    @EJB
    private ServletContext context;

    @Transactional
    @Path("/allTeachers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response allTeachers(TeachersDTO teachersDTO) {
        if (teachersDTO != null) {
            try {
                List<TeachersDTO> dTOs = teachersService.findTeachers(teachersDTO.getBranchId());
                GenericEntity<List<TeachersDTO>> list = new GenericEntity<List<TeachersDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            teachersDTO = new TeachersDTO();
            teachersDTO.setMsg("No Id is Sent");
            return Response.ok(teachersDTO).build();
        }
    }

    @Transactional
    @Path("/saveTeachers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveTeachers(TeachersDTO teachersDTO) {
        TeachersDTO msg;
        try {
            msg = teachersService.save(teachersDTO, null);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteTeachers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTeachers(TeachersDTO teachersDTO) {
        TeachersDTO msg = new TeachersDTO();
        try {
            teachersService.deleteTeachers(teachersDTO.getId());
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

    @Transactional
    @Path("/findTeachers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TeacherDataAPI> findTeachers(APIStudentDataDTO igmaUserDTOABI) {
        List<TeacherDataAPI> teacherDataAPI = new ArrayList<>();
        if (igmaUserDTOABI.getBranchId()!= null) {
            teacherDataAPI = igmaUserService.getTeacherDataAPI(igmaUserDTOABI.getBranchId());
        }
        return teacherDataAPI;
    }

    @Transactional
    @Path("/findMaterial")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<MaterialsDTO> findMaterial(MaterialsDTO materialsDTO) {
        List<MaterialsDTO> materialsDTOList = new ArrayList<>();
        if (materialsDTO.getTeacherId() != null) {
            materialsDTOList = materialsService.findMaterialsABI(materialsDTO.getTeacherId() , materialsDTO.getStageId());
        }
        return materialsDTOList;
    }

}

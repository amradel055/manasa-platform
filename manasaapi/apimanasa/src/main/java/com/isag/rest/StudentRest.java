/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.api.dto.TeacherDataAPI;
import com.isag.businessservice.IgmaUserService;
import com.isag.businessservice.MaterialsService;
import com.isag.businessservice.StudentService;
import com.isag.businessservice.StudentStatusService;
import com.isag.config.UserLoginData;
import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.MaterialsDTO;
import com.isag.dto.StudentDTO;
import java.util.ArrayList;
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
@Path("/student")
public class StudentRest {

    private UserLoginData userLoginData;
    @EJB
    private StudentService studentService;
    @EJB
    IgmaUserService igmaUserService;

    @EJB
    MaterialsService materialsService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/studentregister")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response studentRegister(APIStudentDataDTO aPIStudentDataDTO) {
        APIStudentDataDTO msg = new APIStudentDataDTO();
        try {
            msg = studentService.save(aPIStudentDataDTO);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    public UserLoginData getUserLoginData(@Context ContainerRequestContext crc) {
        userLoginData = (UserLoginData) crc.getProperty("userLogInData");
        return userLoginData;
    }

    @Transactional
    @Path("/studentList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findStudentList(APIStudentDataDTO aPIStudentDataDTO) {
        if (aPIStudentDataDTO != null) {
            try {
                List<APIStudentDataDTO> dTOs = studentService.getStudentbyBranchId(aPIStudentDataDTO.getBranchId(), aPIStudentDataDTO.getSchoolStagesId(), aPIStudentDataDTO.getId());
                GenericEntity<List<APIStudentDataDTO>> list = new GenericEntity<List<APIStudentDataDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            aPIStudentDataDTO = new APIStudentDataDTO();
            aPIStudentDataDTO.setMsg("No Id is Sent");
            return Response.ok(aPIStudentDataDTO).build();
        }
    }

    @Transactional
    @Path("/findstudentlogindata")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findStudentLoginData(APIStudentDataDTO aPIStudentDataDTO) {
        if (aPIStudentDataDTO != null) {
            try {
                StudentDTO studentData = studentService.findStudentLoginData(aPIStudentDataDTO.getId());
                return Response.ok(studentData).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            aPIStudentDataDTO = new APIStudentDataDTO();
            aPIStudentDataDTO.setMsg("No Data is Sent");
            return Response.ok(aPIStudentDataDTO).build();
        }
    }
    
    @Transactional
    @Path("/findstudentdata")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findStudentData(APIStudentDataDTO aPIStudentDataDTO) {
        if (aPIStudentDataDTO != null) {
            try {
                StudentDTO studentData = studentService.findStudentId(aPIStudentDataDTO.getId());
                return Response.ok(studentData).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            aPIStudentDataDTO = new APIStudentDataDTO();
            aPIStudentDataDTO.setMsg("No Data is Sent");
            return Response.ok(aPIStudentDataDTO).build();
        }
    }

    @Transactional
    @Path("deleteStudent")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteStudent(APIStudentDataDTO aPIStudentDataDTO) {
        APIStudentDataDTO msg = new APIStudentDataDTO();
        try {
            studentService.deleteStudent(aPIStudentDataDTO.getId());
            msg.setMsg("تم الحذف بنجاح");
            msg.setMsgType("success");
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /**
     * @param userLoginData the userLoginData to set
     */
    public void setUserLoginData(UserLoginData userLoginData) {
        this.userLoginData = userLoginData;
    }

    @Transactional
    @Path("/findTeacher")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TeacherDataAPI> findTeacher(APIStudentDataDTO igmaUserDTOABI) {
        List<TeacherDataAPI> teacherDataAPI = new ArrayList<>();
        if (igmaUserDTOABI.getBranchId() != null) {
            teacherDataAPI = igmaUserService.getTeacherDataAPI(igmaUserDTOABI.getBranchId());
        }
        return teacherDataAPI;
    }
    
    
    

    @Transactional
    @Path("/findMaterials")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<MaterialsDTO> findMaterial(MaterialsDTO materialsDTO) {
        List<MaterialsDTO> materialsDTOList = new ArrayList<>();
        if (materialsDTO.getTeacherId() != null) {
            materialsDTOList = materialsService.findMaterialsABI(materialsDTO.getTeacherId(), materialsDTO.getStageId());
        }
        return materialsDTOList;
    }
    
    
    

}

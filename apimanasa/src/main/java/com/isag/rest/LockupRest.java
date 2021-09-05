/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.GovernoratesService;
import com.isag.businessservice.MaterialsService;
import com.isag.businessservice.SchoolStagesGuideService;
import com.isag.businessservice.StatesService;
import com.isag.config.UserLoginData;
import com.isag.dto.GovernoratesDTO;
import com.isag.dto.MaterialsDTO;
import com.isag.dto.SchoolStagesGuideDTO;
import com.isag.dto.StatesDTO;
import com.isag.dto.TeacherMaterialsDTO;
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
@Path("/lockup")
public class LockupRest {

    private UserLoginData userLoginData;

    @EJB
    private GovernoratesService governoratesService;
    @EJB
    private StatesService statesService;
    @EJB
    SchoolStagesGuideService schoolStagesGuideService;
    @EJB
    MaterialsService materialsService;

    @Transactional
    @Path("/governorateList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findGovernorateList(@Context ContainerRequestContext crc) {

        try {
            List<GovernoratesDTO> dTOs = governoratesService.findGovernorate();
            GenericEntity<List<GovernoratesDTO>> list = new GenericEntity<List<GovernoratesDTO>>(dTOs) {
            };
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/statesList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findStatesList(GovernoratesDTO governateId) {
        if (governateId != null) {
            try {
                List<StatesDTO> dTOs = statesService.findStatesBygovernorate(governateId.getId());
                GenericEntity<List<StatesDTO>> list = new GenericEntity<List<StatesDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            governateId = new GovernoratesDTO();
            governateId.setMsg("No Id is Sent");
            return Response.ok(governateId).build();

        }
    }

    @Transactional
    @Path("/levelList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findSchoolStagesGuideList(GovernoratesDTO governateId) {
        if (governateId != null) {
            try {
                List<SchoolStagesGuideDTO> dTOs = schoolStagesGuideService.findschoolStagesGuide(governateId.getBranchId());
                GenericEntity<List<SchoolStagesGuideDTO>> list = new GenericEntity<List<SchoolStagesGuideDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            governateId = new GovernoratesDTO();
            governateId.setMsg("No branchId is Sent");
            return Response.ok(governateId).build();

        }
    }

    @Transactional
    @Path("/schoolStagesGuideByTeacher")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findSchoolStagesGuideByTeacher(SchoolStagesGuideDTO guideDTO) {
        if (guideDTO != null) {
            try {
                List<SchoolStagesGuideDTO> dTOs = schoolStagesGuideService.findschoolStagesGuideByTeacherId(guideDTO.getTeacherId());
                GenericEntity<List<SchoolStagesGuideDTO>> list = new GenericEntity<List<SchoolStagesGuideDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            guideDTO = new SchoolStagesGuideDTO();
            guideDTO.setMsg("No teacherId is Sent");
            return Response.ok(guideDTO).build();

        }
    }

    @Transactional
    @Path("/materialList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findMaterials(TeacherMaterialsDTO materialsDTO) {
        if (materialsDTO != null) {
            try {
                List<MaterialsDTO> dTOs = materialsService.findMaterialsByTeacher(materialsDTO.getTeacherId());
                GenericEntity<List<MaterialsDTO>> list = new GenericEntity<List<MaterialsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            materialsDTO = new TeacherMaterialsDTO();
            materialsDTO.setMsg("No Id is Sent");
            return Response.ok(materialsDTO).build();

        }
    }

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

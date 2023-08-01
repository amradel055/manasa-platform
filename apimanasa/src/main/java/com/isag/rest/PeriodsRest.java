/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.PeriodsService;
import com.isag.config.UserLoginData;
import com.isag.dto.PeriodsDTO;
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
@Path("/periods")
public class PeriodsRest {

    private UserLoginData userLoginData;
    @EJB
    PeriodsService periodsService;

    @Transactional
    @Path("/allPeriods")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response allPeriods(PeriodsDTO periodsDTO) {
        if (periodsDTO != null) {
            try {
                List<PeriodsDTO> dTOs = periodsService.findPeriodsByTeacherId(periodsDTO.getTeacherId());
                GenericEntity<List<PeriodsDTO>> list = new GenericEntity<List<PeriodsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            periodsDTO = new PeriodsDTO();
            periodsDTO.setMsg("No Id is Sent");
            return Response.ok(periodsDTO).build();
        }
    }

    @Transactional
    @Path("/periodsByTeacher")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response PeriodsByTeacherId(PeriodsDTO periodsDTO) {
        if (periodsDTO != null) {
            try {
                List<PeriodsDTO> dTOs = periodsService.findPeriodsByTeacherId(periodsDTO.getTeacherId());
                GenericEntity<List<PeriodsDTO>> list = new GenericEntity<List<PeriodsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            periodsDTO = new PeriodsDTO();
            periodsDTO.setMsg("No Id is Sent");
            return Response.ok(periodsDTO).build();
        }
    }

    @Transactional
    @Path("/savePeriods")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePeriods(PeriodsDTO periodsDTO) {
        PeriodsDTO msg;
        try {
            msg = periodsService.save(periodsDTO, null);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deletePeriods")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePeriods(PeriodsDTO periodsDTO) {
        PeriodsDTO msg = new PeriodsDTO();
        try {
            periodsService.deletePeriods(periodsDTO.getId());
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
    @Path("/findPeriod")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PeriodsDTO> findPeriod(PeriodsDTO periodsDTO) {
        List<PeriodsDTO> periodsDTOList = new ArrayList<>();
        if (periodsDTO.getMaterialId()!= null) {
            periodsDTOList = periodsService.findPeriodsByMaterialId(periodsDTO.getMaterialId());
        }
        return periodsDTOList;
    }

}

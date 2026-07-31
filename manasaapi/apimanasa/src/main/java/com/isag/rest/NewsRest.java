/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.NewsService;
import com.isag.businessservice.StudentStatusService;
import com.isag.config.UserLoginData;
import com.isag.dto.NewsDTO;
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
@Path("/news")
public class NewsRest {

    private UserLoginData userLoginData;
    @EJB
    NewsService newsService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/allNews")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findAllNews(NewsDTO newsDTO) {
        if (newsDTO != null) {
            try {
                List<NewsDTO> dTOs = newsService.findNews(newsDTO.getId(), newsDTO.getTeacherId(),
                        newsDTO.getNewsType(), newsDTO.getSchoolStageId() , newsDTO.getActive());
                GenericEntity<List<NewsDTO>> list = new GenericEntity<List<NewsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            newsDTO = new NewsDTO();
            newsDTO.setMsg("No Id is Sent");
            return Response.ok(newsDTO).build();
        }
    }

    @Transactional
    @Path("/saveNews")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveNews(NewsDTO newsDTO) {
        NewsDTO msg;
        try {
            msg = newsService.save(newsDTO, null);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteNews")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteNews(NewsDTO newsDTO) {
        NewsDTO msg = new NewsDTO();
        try {
            newsService.deleteNews(newsDTO.getId());
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.LessonVoiceService;
import com.isag.businessservice.StudentStatusService;
import com.isag.dto.LessonDTO;
import com.isag.dto.LessonVoiceDTO;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author AhmedEssam <ahmedEssam.com>
 */
@Path("/lessonvoice")
public class LessonVoiceRest {

    @EJB
    LessonVoiceService lessonVoiceService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/deletevoice")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteLesson(LessonVoiceDTO dTO) {
        LessonDTO msg = new LessonDTO();
        try {
            lessonVoiceService.deleteVoice(dTO.getVoice());
            msg.setMsg("تم الحذف بنجاح");
            msg.setMsgType("success");
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

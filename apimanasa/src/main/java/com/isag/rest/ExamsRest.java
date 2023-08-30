/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.AnswersService;
import com.isag.businessservice.ExamsService;
import com.isag.businessservice.MnsLoginExamService;
import com.isag.businessservice.QuestionsService;
import com.isag.businessservice.StudentStatusService;
import com.isag.config.UserLoginData;
import com.isag.dto.AnswersDTO;
import com.isag.dto.ExamsDTO;
import com.isag.dto.MnsLoginExamDTO;
import com.isag.dto.QuestionsDTO;
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
@Path("/exams")
public class ExamsRest {

    private UserLoginData userLoginData;
    @EJB
    ExamsService examsService;
    @EJB
    AnswersService answersService;
    @EJB
    QuestionsService questionsService;
    @EJB
    MnsLoginExamService loginExamService;
    @EJB
    private StudentStatusService studentStatusService;

    @Transactional
    @Path("/findExamsForAdmin")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findExamsForAdmin(ExamsDTO examsDTO) {
        if (examsDTO != null) {
            try {
                List<ExamsDTO> dTOs = examsService.findExamsListForTeacher(examsDTO.getSchoolStagesId(), examsDTO.getTeacherId(), examsDTO.getLessonId());
                GenericEntity<List<ExamsDTO>> list = new GenericEntity<List<ExamsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            examsDTO = new ExamsDTO();
            examsDTO.setMsg("No Id is Sent");
            return Response.ok(examsDTO).build();
        }
    }

    @Transactional
    @Path("/findExamsForStudent")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findExamsForStudent(ExamsDTO examsDTO) {
        if (examsDTO != null) {
            try {
                List<ExamsDTO> dTOs ;
                if (examsDTO.getExamType() != null && examsDTO.getExamType() == 0) {
                    dTOs = examsService.findAutoExamsListForStudent(examsDTO);
                } else {
                    dTOs = examsService.findExamsListForStudent(examsDTO);
                }
                GenericEntity<List<ExamsDTO>> list = new GenericEntity<List<ExamsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            examsDTO = new ExamsDTO();
            examsDTO.setMsg("No Id is Sent");
            return Response.ok(examsDTO).build();
        }
    }

    @Transactional
    @Path("/findSolvedExamsForStudent")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findSolvedExamsForStudent(ExamsDTO examsDTO) {
        if (examsDTO != null) {
            try {
                List<ExamsDTO> dTOs = new ArrayList<>();

                dTOs = examsService.findSolvedExamsListForStudent(examsDTO);
                GenericEntity<List<ExamsDTO>> list = new GenericEntity<List<ExamsDTO>>(dTOs) {
                };
                return Response.ok(list).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            examsDTO = new ExamsDTO();
            examsDTO.setMsg("No Id is Sent");
            return Response.ok(examsDTO).build();
        }
    }

    @Transactional
    @Path("/findExams")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findExams(ExamsDTO examsDTO) {
        if (examsDTO != null) {
            try {
                if (examsDTO.getId() != null) {
                    ExamsDTO dTO = examsService.findExams(examsDTO.getId());
                    return Response.ok(dTO).build();
                } else {
                    return Response.ok(null).build();
                }
            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            examsDTO = new ExamsDTO();
            examsDTO.setMsg("No Id is Sent");
            examsDTO.setMsgType("failed");
            return Response.ok(examsDTO).build();
        }
    }

    @Transactional
    @Path("/checkExams")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkExams(ExamsDTO examsDTO) {
        if (examsDTO != null) {
            try {
                ExamsDTO dTO = examsService.checkExamForStudent(examsDTO.getStudentId(), examsDTO.getId());
                return Response.ok(dTO).build();

            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            examsDTO = new ExamsDTO();
            examsDTO.setMsg("No Id is Sent");
            examsDTO.setMsgType("failed");
            return Response.ok(examsDTO).build();
        }
    }

    @Transactional
    @Path("/saveExams")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveExams(ExamsDTO examsDTO) {
        ExamsDTO msg;
        try {
            msg = examsService.save(examsDTO, null);
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteExams")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteExams(ExamsDTO examsDTO) {
        ExamsDTO msg = new ExamsDTO();
        try {
            examsService.deleteExams(examsDTO.getId());
            msg.setMsg("تم الحذف بنجاح");
            msg.setMsgType("success");
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteQuestion")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteQuestion(QuestionsDTO questionsDTO) {
        ExamsDTO msg = new ExamsDTO();
        try {
            questionsService.deleteQuestionAndAnswer(questionsDTO.getId());
            msg.setMsg("تم الحذف بنجاح");
            msg.setMsgType("success");
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/deleteAnswer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAnswer(AnswersDTO answersDTO) {
        ExamsDTO msg = new ExamsDTO();
        try {
            answersService.deleteAnswers(answersDTO.getId());
            msg.setMsg("تم الحذف بنجاح");
            msg.setMsgType("success");
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/findLoginExam")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response findLoginExam(MnsLoginExamDTO loginExamDTO) {
        if (loginExamDTO != null) {
            try {
                if (loginExamDTO.getId() != null) {
                    Integer dTO = loginExamService.findLoginExam(loginExamDTO);
                    return Response.ok(dTO).build();
                } else {
                    return Response.ok(null).build();
                }
            } catch (Exception e) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

        } else {
            loginExamDTO = new MnsLoginExamDTO();
            loginExamDTO.setMsg("No Id is Sent");
            loginExamDTO.setMsgType("failed");
            return Response.ok(loginExamDTO).build();
        }
    }

    @Transactional
    @Path("/checkexamautoforlesson")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkExamAutoForLesson(ExamsDTO examsDTO) {
        try {
            boolean msg = examsService.checkExamAutoForLesson(examsDTO.getLessonId());
            return Response.ok(msg).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}

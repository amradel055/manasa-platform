/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.DocumentsDTO;
import com.isag.dto.LessonDTO;
import com.isag.dto.SchoolStagesGuideDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.Lesson;
import com.isag.entity.IgmaUser;
import com.isag.entity.Periods;
import com.isag.entity.SchoolStagesGuide;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class LessonServiceImpl implements LessonService {

    @EJB
    private GenericDAO dao;
    @EJB
    private DocumentsService documentsService;
    @EJB
    private CommentsService commentsService;

    @Override
    public List<LessonDTO> findAllLessons() {
        List<Lesson> list = dao.findAll(Lesson.class);
        return returnListDTO(list);
    }

    @Override
    public List<LessonDTO> findLessonByPeriod(Integer periodId, Integer branchId, Integer companyId, Integer schoolStagesGuideId, Integer lessonType, Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("companyId", companyId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Lesson d WHERE d.branchId.id = :branchId AND d.companyId.id = :companyId ");
        if (periodId != null) {
            params.put("periodId", periodId);
            builder.append(" AND d.periodId.id = :periodId ");
        }
        if (id != null) {
            params.put("id", id);
            builder.append(" AND d.id = :id ");
        }
        if (schoolStagesGuideId != null) {
            params.put("schoolStagesGuideId", schoolStagesGuideId);
            builder.append(" AND d.schoolStagesGuideId.id = :schoolStagesGuideId  ");
        }
        if (lessonType != null) {
            params.put("lessonType", lessonType);
            builder.append(" AND d.lessonType = :lessonType ");
        }
        builder.append(" order by d.id desc ");
        List<Lesson> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public List<LessonDTO> findFreeLesson(Integer branchId, Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("companyId", companyId);
        List<LessonDTO> dTOList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Lesson d WHERE d.branchId.id = :branchId AND d.companyId.id = :companyId");
        List<Lesson> list = dao.findListByQuery(builder.toString(), params);
        returnListDTO(list);
        return dTOList;
    }

    @Override
    public List<LessonDTO> findAllLessonsByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<LessonDTO> dTOList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Lesson d WHERE d.branchId.id = :branchId");
        List<Lesson> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    public Boolean validateLesson(LessonDTO lessonDTO) {
        Map<String, Object> params = new HashMap();
        params.put("lessonName", lessonDTO.getLessonName());
        params.put("lessonType", lessonDTO.getLessonType());
        params.put("lessonDescription", lessonDTO.getLessonDescription());
        params.put("videoLink", lessonDTO.getVideoLink());
        params.put("branchId", lessonDTO.getBranchId());
        params.put("companyId", lessonDTO.getCompanyId());
        List<Lesson> lesson = dao.findListByQuery("SELECT e FROM Lesson e WHERE (e.lessonName = :lessonName or e.lessonType = :lessonType or e.lessonDescription = :lessonDescription or e.videoLink = :videoLink) AND e.branchId.id = :branchId AND e.companyId.id = :companyId ", params);
        if (lesson != null && !lesson.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public void deleteLesson(LessonDTO lessonDTO) {
        Lesson entity = new Lesson();
        entity.setId(lessonDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteLesson(Integer lessonDTO) {
        Lesson entity = new Lesson();
        entity.setId(lessonDTO);
        documentsService.deleteDocumentsByLesson(lessonDTO);
        commentsService.deleteCommentsByLesson(lessonDTO);
        dao.deleteEntity(entity);
    }

    @Override
    public LessonDTO save(LessonDTO lessonDTO, IgmaUser isagUser) {
        LessonDTO msg = new LessonDTO();
        Lesson lesson = null;
        if (lessonDTO.getId() != null) {
            lesson = dao.findEntityById(Lesson.class, lessonDTO.getId());
        }
        if (lessonDTO != null && lessonDTO.getLessonName() != null
                && lessonDTO.getLessonDescription() != null
                && lessonDTO.getLessonType() != null
                && lessonDTO.getPeriodId() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(lessonDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(lessonDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            List<String> documents = lessonDTO.getDocuments();
            lesson = mapToEntity(lessonDTO, isagUser, lesson);
            lesson = dao.updateEntity(lesson);
            lessonDTO = mapTODTO(lesson);
            DocumentsDTO documentsDTO = documentsService.saveList(documents, lesson.getId(), lesson.getBranchId().getId(), lesson.getCompanyId().getId());
            if (documentsDTO.getMsg() != null && !documentsDTO.getMsg().isEmpty()) {
                msg.setMsg("يوجد خطأ فى بيانات المذكرات");
                msg.setMsgType("fail");
                dao.rollbackQuery();
            } else {
                msg.setMsg("تم الحفظ بنجاح");
                msg.setMsgType("success");
            }
            return msg;
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    private List<LessonDTO> returnListDTO(List<Lesson> list) {
        List<LessonDTO> dTOList = new ArrayList<>();
        for (Lesson entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private LessonDTO mapTODTO(Lesson entity) {
        LessonDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private LessonDTO initMapToDTO(Lesson entity) {

        LessonDTO dTO = new LessonDTO();

        dTO.setId(entity.getId());
        dTO.setLessonName(entity.getLessonName());
        dTO.setLessonDescription(entity.getLessonDescription());
        dTO.setLessonType(entity.getLessonType());
        dTO.setMonthId(entity.getMonthId());
        dTO.setVideoLink(entity.getVideoLink());
        dTO.setImage(entity.getImage());
        dTO.setYoutubeLink(entity.getYoutubeLink());
        dTO.setVimeoLink(entity.getVimeoLink());
        dTO.setThumb(entity.getThumb());

        dTO.setDocuments(documentsService.findDocumentsByLessonIdLinks(entity.getId()));

        if (entity.getSchoolStagesGuideId() != null) {
            SchoolStagesGuideDTO schoolStagesGuideDTO = new SchoolStagesGuideDTO();
            schoolStagesGuideDTO.setId(entity.getSchoolStagesGuideId().getId());
            schoolStagesGuideDTO.setName(entity.getSchoolStagesGuideId().getName());
            schoolStagesGuideDTO.setCode(entity.getSchoolStagesGuideId().getCode());
            dTO.setSchoolStagesName(entity.getSchoolStagesGuideId().getName());
            dTO.setSchoolStagesGuideId(schoolStagesGuideDTO);
        }

        if (entity.getPeriodId() != null) {
            dTO.setPeriodId(entity.getPeriodId().getId());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCompanyId(entity.getCompanyId().getId());

        return dTO;
    }

    private Lesson mapToEntity(LessonDTO dTO, IgmaUser isagUser, Lesson entity) {
        if (entity == null) {
            entity = new Lesson();
        }
        entity.setLessonName(dTO.getLessonName());
        entity.setLessonDescription(dTO.getLessonDescription());
        entity.setLessonType(dTO.getLessonType());
        entity.setMonthId(dTO.getMonthId());
        entity.setVideoLink(dTO.getVideoLink());
        entity.setImage(dTO.getImage());
        entity.setYoutubeLink(dTO.getYoutubeLink());
        entity.setVimeoLink(dTO.getVimeoLink());
        entity.setThumb(dTO.getThumb());

        if (dTO.getSchoolId() != null) {
            SchoolStagesGuide schoolStagesGuide = new SchoolStagesGuide();
            schoolStagesGuide.setId(dTO.getSchoolId());
            entity.setSchoolStagesGuideId(schoolStagesGuide);
        }

        if (dTO.getPeriodId() != null) {
            Periods periods = new Periods();
            periods.setId(dTO.getPeriodId());
            entity.setPeriodId(periods);
        }

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setBranchId(isagUser.getBranchId());
            entity.setCreationDate(new Date());
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
            documentsService.deleteDocumentsByLesson(dTO.getId());
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}

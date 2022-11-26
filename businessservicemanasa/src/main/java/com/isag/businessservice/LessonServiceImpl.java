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
import com.isag.entity.Materials;
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
    @EJB
    private LessonImgService lessonImgService;

    @EJB
    private StudentPeriodsService studentPeriodsService;

    private StringBuilder periodNew;

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
    public List<LessonDTO> findAllLessonsByTeacher(Integer branchId, Integer companyId, Integer teacherId,
            Integer materialId, Integer schoolStageId, Integer id, Integer periodId, Integer studentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("companyId", companyId);
        params.put("teacherId", teacherId);
        periodNew = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Lesson d WHERE d.branchId.id = :branchId AND d.companyId.id = :companyId AND d.teacherId.id = :teacherId ");
        if (id != null) {
            params.put("id", id);
            builder.append(" AND d.id = :id");
        }
        if (materialId != null) {
            params.put("materialId", materialId);
            builder.append(" AND d.materialId.id = :materialId");
        }
        if (schoolStageId != null) {
            params.put("schoolStageId", schoolStageId);
            builder.append(" AND d.schoolStagesGuideId.id = :schoolStageId");
        }
        List<Lesson> list = dao.findListByQuery(builder.toString(), params);

        return returnListDTO(list);
    }

    @Override
    public List<LessonDTO> findAllLessonsByStudent(Integer branchId, Integer companyId, Integer teacherId,
            Integer materialId, Integer schoolStageId, Integer id, Integer periodId, Integer studentId) {
        if (studentId != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("branchId", branchId);
            params.put("companyId", companyId);
            params.put("teacherId", teacherId);
            periodNew = new StringBuilder();
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT d FROM Lesson d WHERE d.branchId.id = :branchId AND d.companyId.id = :companyId AND d.teacherId.id = :teacherId ");
            if (id != null) {
                params.put("id", id);
                builder.append(" AND d.id = :id");
            }
            if (materialId != null) {
                params.put("materialId", materialId);
                builder.append(" AND d.materialId.id = :materialId");
            }
            if (schoolStageId != null) {
                params.put("schoolStageId", schoolStageId);
                builder.append(" AND d.schoolStagesGuideId.id = :schoolStageId");
            }

            List<Integer> periodList = studentPeriodsService.findPeriodsByStudentId(studentId, materialId);
            for (Integer period : periodList) {
                if (periodNew.toString().isEmpty()) {
                    periodNew.append(period);
                } else {
                    periodNew.append(",").append(period);
                }
                findAllLessonsPeriod(period);
            }
            builder.append(" AND d.periodId.id in (").append(periodNew.toString()).append(")");
//            builder.append(" AND d.materialId.id in (").append(" SELECT b.materialId.id FROM StudentPeriods b WHERE b.studentId.id = :studentId ").append(")");

            List<Lesson> list = dao.findListByQuery(builder.toString(), params);

//             List<StudentPeriods> periodList = studentPeriodsService.findPeriodsByStudentId(studentId);
//            for (StudentPeriods period : periodList) {
//                if (periodString == null) {
//                    periodString = new StringBuilder();
//                    periodString.append(period.getPeriodId());
//                } else {
//                    periodString.append(" , ");
//                    periodString.append(period.getPeriodId());
//                }
//                
//                if (materialString == null) {
//                    materialString = new StringBuilder();
//                    materialString.append(period.getMaterialId());
//                } else {
//                    materialString.append(" , ");
//                    materialString.append(period.getMaterialId());
//                }
//                
//            }
//            builder.append(" AND ( d.periodId.id in (").append(periodString.toString()).append(")");
//            builder.append(" OR d.periodId.parentPeriod.id in (").append(periodString.toString()).append(") )");
//            builder.append(" AND d.materialId.id in (").append(materialString.toString()).append(")");
            return returnListDTO(list);
        }
        return null;
    }

    public StringBuilder findAllLessonsPeriod(Integer parent) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder periadsId = null;
        params.put("parent", parent);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p FROM Periods p WHERE p.parentPeriod.id = :parent");
        List<Periods> list = dao.findListByQuery(builder.toString(), params);
        if (list != null && !list.isEmpty()) {
            periadsId = findAllLessonsPeriod(list, periadsId);
        }
        return periadsId;
    }

    public StringBuilder findAllLessonsPeriod(List<Periods> listPeriods, StringBuilder builder) {
        if (listPeriods != null) {
            for (Periods periods : listPeriods) {
                periodNew.append(" , ").append(periods.getId());
                findAllLessonsPeriod(periods.getId());
            }
        }
        return builder;
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
        lessonImgService.deleteLesson(lessonDTO);
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
                && lessonDTO.getMaterialId() != null
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
            List<String> lessonImg = lessonDTO.getImages();
            lesson = mapToEntity(lessonDTO, isagUser, lesson);
            lesson = dao.updateEntity(lesson);
            lessonDTO = mapTODTO(lesson);
            DocumentsDTO documentsDTO = new DocumentsDTO() ;
//                    documentsService.saveList(documents, lesson.getId(), lesson.getBranchId().getId(), lesson.getCompanyId().getId());
//            lessonImgService.saveList(lessonImg, lesson.getId(), lesson.getBranchId().getId(), lesson.getCompanyId().getId());
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
        dTO.setVoiceLink(entity.getVoiceLink());

        dTO.setDocuments(documentsService.findDocumentsByLessonIdLinks(entity.getId()));
        dTO.setImages(lessonImgService.findAllLessons(entity.getId()));

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
        if (entity.getMaterialId() != null) {
            dTO.setMaterialId(entity.getMaterialId().getId());
        }
        if (entity.getTeacherId() != null) {
            dTO.setTeacherId(entity.getTeacherId().getId());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(232);
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);

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
        entity.setVoiceLink(dTO.getVoiceLink());

        if (dTO.getSchoolStagesGuideId() != null) {
            SchoolStagesGuide schoolStagesGuide = new SchoolStagesGuide();
            schoolStagesGuide.setId(dTO.getSchoolStagesGuideId().getId());
            entity.setSchoolStagesGuideId(schoolStagesGuide);
        }

        if (dTO.getPeriodId() != null) {
            Periods periods = new Periods();
            periods.setId(dTO.getPeriodId());
            entity.setPeriodId(periods);
        }
        if (dTO.getMaterialId() != null) {
            Materials materials = new Materials();
            materials.setId(dTO.getMaterialId());
            entity.setMaterialId(materials);
        }
        if (isagUser.getId() != null) {
            IgmaUser teacher = new IgmaUser();
            teacher.setId(isagUser.getId());
            entity.setTeacherId(teacher);
        }

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
//            entity.setBranchId(isagUser.getBranchId());
            entity.setCreationDate(new Date());
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
            documentsService.deleteDocumentsByLesson(dTO.getId());
            lessonImgService.deleteLesson(dTO.getId());
        }

        Branch branch = new Branch();
        branch.setId(232);
        entity.setBranchId(branch);
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    @Override
    public LessonDTO findLessonById(Integer lessonId) {
        Lesson lesson = dao.findEntityById(Lesson.class, lessonId);
        return mapTODTO(lesson);
    }

    @Override
    public List<LessonDTO> findLessonByTeacherId(Integer teacherId) {
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", teacherId);
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT d FROM Lesson d WHERE d.teacherId.id = :teacherId ");
        List<Lesson> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

}

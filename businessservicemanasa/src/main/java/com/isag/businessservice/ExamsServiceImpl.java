/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.ExamsDTO;
import com.isag.dto.QuestionsDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.Exams;
import com.isag.entity.Lesson;
import com.isag.entity.Materials;
import com.isag.entity.Periods;
import com.isag.entity.SchoolStagesGuide;
import com.isag.entity.Student;
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
public class ExamsServiceImpl implements ExamsService {

    @EJB
    private GenericDAO dao;
    @EJB
    QuestionsService questionsService;

    @Override
    public ExamsDTO findExams(Integer id) {
        Exams exams = dao.findEntityById(Exams.class, id);
        return mapTODTO(exams, Boolean.TRUE, null, null);
    }

    @Override
    public ExamsDTO checkExamForStudent(Integer studentId, Integer examId) {
        ExamsDTO examsDTO = new ExamsDTO();

        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        params.put("examId", examId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Exams d WHERE d.parent.id = :examId AND d.studentId.id = :studentId ");
        List<Exams> list = dao.findListByQuery(builder.toString(), params);
        if (list != null && !list.isEmpty()) {
            examsDTO.setMsg("نم امتحانه من قبل");
            examsDTO.setMsgType("fail");
            return examsDTO;
        } else {
            examsDTO.setMsg("دخول");
            examsDTO.setMsgType("success");
            return examsDTO;
        }
    }

    @Override
    public List<ExamsDTO> findExamsListForTeacher(Integer schoolStagesId, Integer teacherId, Integer lessonId) {
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", teacherId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Exams d WHERE d.teacherId.id = :teacherId AND d.parent IS NULL ");

        if (lessonId != null) {
            params.put("lessonId", lessonId);
            builder.append(" AND d.lessonId.id = :lessonId ");
        }
        if (schoolStagesId != null) {
            params.put("schoolStagesId", schoolStagesId);
            builder.append(" AND d.schoolStagesId.id = :schoolStagesId ");
        }

        builder.append(" order by d.id desc ");
        List<Exams> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list, false);
    }

//    @Override
//    public List<ExamsDTO> findExamsListForStudent(Integer schoolStagesId, Integer branchId, Integer lessonId, Integer companyId, Integer studentId, Integer periodId) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", branchId);
//        params.put("companyId", companyId);
//        params.put("studentId", studentId);
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT d FROM Exams d WHERE d.active = 1 AND d.branchId.id = :branchId AND d.companyId.id = :companyId AND  d.parent IS NULL AND d.id not in (select e.parent.id from Exams e where e.studentId.id = :studentId)");
//
//        if (periodId != null) {
//            params.put("periodId", periodId);
//            builder.append(" AND d.periodId.id = :periodId ");
//        }
//        if (lessonId != null) {
//            params.put("lessonId", lessonId);
//            builder.append(" AND d.lessonId.id = :lessonId ");
//        }
//        if (schoolStagesId != null) {
//            params.put("schoolStagesId", schoolStagesId);
//            builder.append(" AND d.schoolStagesId.id = :schoolStagesId ");
//        }
//        builder.append(" order by d.id desc ");
//        List<Exams> list = dao.findListByQuery(builder.toString(), params);
//
//        Map<String, Object> params2 = new HashMap<>();
//        params2.put("studentId", studentId);
//        StringBuilder builder2 = new StringBuilder();
//        builder2.append("SELECT d FROM Exams d WHERE d.studentId.id = :studentId");
//        if (lessonId != null) {
//            params2.put("lessonId", lessonId);
//            builder2.append(" AND d.lessonId.id = :lessonId ");
//        }
//        builder2.append(" order by d.id desc ");
//        List<Exams> list2 = dao.findListByQuery(builder2.toString(), params2);
//        list.addAll(list2);
//        return returnListDTO(list, false);
//    }
    @Override
    public List<ExamsDTO> findAutoExamsListForStudent(ExamsDTO examsDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", examsDTO.getTeacherId());
//        params.put("examType", examsDTO.getExamType());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Exams d WHERE d.studentId is null AND d.active = 1 AND d.teacherId.id = :teacherId");
        //
        StringBuilder materialId = null;
        //
        for (Integer materialsDTO : examsDTO.getMaterialList()) {
            if (materialId == null) {
                materialId = new StringBuilder();
                materialId.append(materialsDTO);
            } else {
                materialId.append(",").append(materialsDTO);
            }
        }
        if (materialId != null) {
            builder.append(" and d.materialId.id in(  ").append(materialId).append(" )");
        }

        List<Exams> list = dao.findListByQuery(builder.toString(), params);

        examsDTO = mapTODTO(list.get(0), Boolean.TRUE, 1, examsDTO.getPeriodList());
        List<ExamsDTO> examsDTOList = new ArrayList<>();
        examsDTO.setLessonId(null);
        examsDTO.setParent(null);
        examsDTO.setPeriodId(null);
        examsDTO.setExamType(0);
        examsDTOList.add(examsDTO);
        return examsDTOList;

    }

    @Override
    public List<ExamsDTO> findExamsListForStudent(ExamsDTO examsDTO) {
        Map<String, Object> params = new HashMap<>();

        params.put("studentId", examsDTO.getStudentId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Exams d WHERE d.studentId is null and d.id not in (select e.parent.id from Exams e where e.studentId.id = :studentId ) AND d.active = 1 ");
        //
        if (examsDTO.getTeacherId() != null) {
            params.put("teacherId", examsDTO.getTeacherId());
            builder.append(" AND d.teacherId.id = :teacherId");
        }
        StringBuilder materialId = null, periodId = null;
        //
        if (examsDTO.getMaterialList() != null && !examsDTO.getMaterialList().isEmpty()) {
            for (Integer materialsDTO : examsDTO.getMaterialList()) {
                if (materialId == null) {
                    materialId = new StringBuilder();
                    materialId.append(materialsDTO);
                } else {
                    materialId.append(",").append(materialsDTO);
                }
            }
            if (materialId != null) {
                builder.append(" and d.materialId.id in(  ").append(materialId).append(" )");
            }
        }
        if (examsDTO.getPeriodList() != null && !examsDTO.getPeriodList().isEmpty()) {
            for (Integer periodsDTO : examsDTO.getPeriodList()) {
                if (periodId == null) {
                    periodId = new StringBuilder();
                    periodId.append(periodsDTO);
                } else {
                    periodId.append(",").append(periodsDTO);
                }
            }
            if (periodId != null) {
                builder.append(" and d.periodId.id in(  ").append(periodId).append(" )");
            }
        }
        if (examsDTO.getExamType() != null) {
            params.put("examType", examsDTO.getExamType());
            builder.append(" and d.examType = :examType ");
        }

        List<Exams> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list, true);
    }

    @Override
    public List<ExamsDTO> findSolvedExamsListForStudent(ExamsDTO examsDTO) {
        Map<String, Object> params = new HashMap<>();

        params.put("studentId", examsDTO.getStudentId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Exams d WHERE d.studentId is not null and d.studentId.id = :studentId ");
        //
        if (examsDTO.getTeacherId() != null) {
            params.put("teacherId", examsDTO.getTeacherId());
            builder.append(" AND d.teacherId.id = :teacherId");
        }
        StringBuilder materialId = null, periodId = null;
        //
        if (examsDTO.getMaterialList() != null && !examsDTO.getMaterialList().isEmpty()) {
            for (Integer materialsDTO : examsDTO.getMaterialList()) {
                if (materialId == null) {
                    materialId = new StringBuilder();
                    materialId.append(materialsDTO);
                } else {
                    materialId.append(",").append(materialsDTO);
                }
            }
            if (materialId != null) {
                builder.append(" and d.materialId.id in(  ").append(materialId).append(" )");
            }
        }
        if (examsDTO.getPeriodList() != null && !examsDTO.getPeriodList().isEmpty()) {
            for (Integer periodsDTO : examsDTO.getPeriodList()) {
                if (periodId == null) {
                    periodId = new StringBuilder();
                    periodId.append(periodsDTO);
                } else {
                    periodId.append(",").append(periodsDTO);
                }
            }
            if (periodId != null) {
                builder.append(" and d.periodId.id in(  ").append(periodId).append(" )");
            }
        }
        if (examsDTO.getExamType() != null) {
            params.put("examType", examsDTO.getExamType());
            builder.append(" and d.examType = :examType ");
        }

        List<Exams> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list, true);
    }

    @Override
    public List<ExamsDTO> deleteExamsListForStudent(Integer studentId) {

        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT d FROM Exams d WHERE d.studentId.id = :studentId order by d.id desc ");
        List<Exams> list = dao.findListByQuery(builder.toString(), params);
        for (Exams examsDTO : list) {
            deleteExams(examsDTO.getId());
        }

        return returnListDTO(list, false);

    }

    public ExamsDTO saveList(List<ExamsDTO> examsDTOsList, IgmaUser isagUser) {

        ExamsDTO exams = null;
        for (ExamsDTO examsDTO : examsDTOsList) {
            exams = save(examsDTO, isagUser);
            questionsService.saveList(examsDTO.getQuestionsList(), isagUser, examsDTO.getId());
        }
        return exams;
    }

    public void deleteList(List<Integer> examsDTOsList) {

        for (Integer examsDTO : examsDTOsList) {
            deleteExams(examsDTO);
        }
    }

    @Override
    public ExamsDTO save(ExamsDTO examsDTO, IgmaUser isagUser) {
        ExamsDTO msg = new ExamsDTO();
        Exams exams = null;
        if (examsDTO.getId() != null) {
            exams = dao.findEntityById(Exams.class, examsDTO.getId());
        }
        if (examsDTO.getSchoolStagesId() != null
                //                && examsDTO.getPeriodId() != null
                && examsDTO.getExamName() != null
                && examsDTO.getTeacherId() != null
                && examsDTO.getMaterialId() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(examsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(examsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            exams = mapToEntity(examsDTO, isagUser, exams);
            exams = dao.updateEntity(exams);

            if (examsDTO.getStudentId() != null) {
                QuestionsDTO x = questionsService.saveListForStudent(examsDTO.getQuestionsList(), isagUser, exams.getId(), examsDTO.getStudentId(), examsDTO.getParent(), examsDTO.getExamType());
                exams.setScore(x.getTotalDegree());
                dao.updateEntity(exams);
            } else {
                if (examsDTO.getQuestionsList() != null) {
                    questionsService.saveList(examsDTO.getQuestionsList(), isagUser, exams.getId());
                }
            }

            msg.setMsg("تم الحفظ بنجاح");
            msg.setMsgType("success");
            return msg;
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    @Override
    public void deleteExams(ExamsDTO examsDTO) {
        Exams entity = new Exams();
        entity.setId(examsDTO.getId());
        dao.deleteEntity(entity);
    }

    public void updateParentToZero(Integer id) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        String builder = " update Exams e set e.parent = null where e.parent.id = :id ";
        dao.executeUpdateQuery(builder, params);
    }

    @Override
    public void deleteExams(Integer examId) {
        Exams entity = new Exams();
        entity.setId(examId);
        updateParentToZero(examId);
        questionsService.deleteQuestionsForExam(examId);
        dao.deleteEntity(entity);
    }

    public List<ExamsDTO> returnListDTO(List<Exams> list, Boolean detail) {
        List<ExamsDTO> dTOList = new ArrayList<>();
        for (Exams entity : list) {
            dTOList.add(mapTODTO(entity, detail, null, null));
        }
        return dTOList;
    }

    private ExamsDTO mapTODTO(Exams entity, Boolean detail, Integer examtype, List<Integer> periodList) {
        ExamsDTO dTO = initMapToDTO(entity, detail, examtype, periodList);

        return dTO;
    }

    private ExamsDTO initMapToDTO(Exams entity, Boolean detail, Integer examtype, List<Integer> periodList) {

        ExamsDTO dTO = new ExamsDTO();
        dTO.setId(entity.getId());
        dTO.setExamName(entity.getExamName());
        dTO.setDegree(entity.getDegree());
        dTO.setMonthId(entity.getMonthId());
        dTO.setScore(entity.getScore());
        dTO.setActive(entity.getActive());
        dTO.setExamType(entity.getExamType());
        dTO.setTimer(entity.getTimer());
        dTO.setPeriodList(periodList);
        dTO.setSolutionTime(entity.getSolutionTime());
        if (entity.getTeacherId() != null) {
            dTO.setTeacherId(entity.getTeacherId().getId());
        }

        if (entity.getPeriodId() != null) {
            dTO.setPeriodId(entity.getPeriodId().getId());
        }

        if (entity.getSchoolStagesId() != null) {
            dTO.setSchoolStagesId(entity.getSchoolStagesId().getId());
            dTO.setSchoolStagesName(entity.getSchoolStagesId().getName());
        }
        if (entity.getLessonId() != null) {
            dTO.setLessonId(entity.getLessonId().getId());
            dTO.setLessonName(entity.getLessonId().getLessonName());
        }

        if (entity.getParent() != null) {
            dTO.setParent(entity.getParent().getId());
        }

        if (entity.getStudentId() != null) {
            dTO.setStudentId(entity.getStudentId().getId());
            dTO.setStudentName(entity.getStudentId().getFullName());
        }
        if (entity.getMaterialId() != null) {
            dTO.setMaterialId(entity.getMaterialId().getId());
        }

        if (detail) {
            if (examtype != null) {
                dTO.setQuestionsList(questionsService.findGeneralQuestions(dTO));
                for (QuestionsDTO questionsDTO : dTO.getQuestionsList()) {
                    dTO.setScore(dTO.getScore().add(questionsDTO.getQuestionDegree()));
                }
            } else {
                dTO.setQuestionsList(questionsService.findQuestionsByExamId(entity.getId()));
            }
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private Exams mapToEntity(ExamsDTO dTO, IgmaUser isagUser, Exams entity) {
        if (entity == null) {
            entity = new Exams();
        }

        entity.setId(dTO.getId());
        entity.setExamName(dTO.getExamName());
        if (dTO.getExamType() != null && dTO.getExamType() == 0) {
            for (QuestionsDTO questionsDTO : dTO.getQuestionsList()) {
                dTO.setDegree(dTO.getDegree().add(questionsDTO.getQuestionDegree()));
            }
            entity.setDegree(dTO.getDegree());
        }
            entity.setDegree(dTO.getDegree());
        entity.setActive(dTO.getActive());
        entity.setScore(dTO.getScore());
        entity.setMonthId(dTO.getMonthId());
        entity.setExamType(dTO.getExamType());
        entity.setTimer(dTO.getTimer());
        entity.setSolutionTime(dTO.getSolutionTime());
        if (dTO.getTeacherId() != null) {
            IgmaUser igmaUser = new IgmaUser();
            igmaUser.setId(dTO.getTeacherId());
            entity.setTeacherId(igmaUser);
        }

        if (dTO.getParent() != null) {
            Exams exams = new Exams();
            exams.setId(dTO.getParent());
            entity.setParent(exams);
        }

        if (dTO.getStudentId() != null) {
            Student student = new Student();
            student.setId(dTO.getStudentId());
            entity.setStudentId(student);
        }

        if (dTO.getSchoolStagesId() != null) {
            SchoolStagesGuide schoolStagesGuide = new SchoolStagesGuide();
            schoolStagesGuide.setId(dTO.getSchoolStagesId());
            entity.setSchoolStagesId(schoolStagesGuide);
        }

        if (dTO.getLessonId() != null) {
            Lesson lesson = new Lesson();
            lesson.setId(dTO.getLessonId());
            entity.setLessonId(lesson);
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

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setBranchId(isagUser.getBranchId());
            entity.setCreationDate(new Date());
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}

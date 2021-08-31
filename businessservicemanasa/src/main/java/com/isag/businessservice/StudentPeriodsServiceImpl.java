/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.StudentDTO;
import com.isag.dto.StudentPeriodsDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.StudentPeriods;
import com.isag.entity.IgmaUser;
import com.isag.entity.Periods;
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
public class StudentPeriodsServiceImpl implements StudentPeriodsService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<Integer> findPeriodsByStudentId(Integer studentId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (studentId != null) {
            params.put("studentId", studentId);
            query = "SELECT b.periodId.id FROM StudentPeriods b WHERE b.studentId.id = :studentId";
        }
        List<Integer> PeriodsList = dao.findListByQuery(query, params);
        return PeriodsList;
    }

    @Override
    public List<Integer> findteachersByStudentId(Integer studentId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (studentId != null) {
            params.put("studentId", studentId);
            query = "SELECT b.teacherId.id FROM StudentPeriods b WHERE b.studentId.id = :studentId";
        }
        List<Integer> teachersList = dao.findListByQuery(query, params);
        return teachersList;
    }

    @Override
    public List<StudentPeriodsDTO> findStudentPeriods(StudentPeriodsDTO studentPeriodsDTO) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (studentPeriodsDTO.getTeacherId() != null) {
            params.put("teacherId", studentPeriodsDTO.getTeacherId());
            builder.append("SELECT d FROM StudentPeriods d WHERE d.teacherId.id = :teacherId ");
        }
        if (studentPeriodsDTO.getPeriodId() != null) {
            params.put("periodId", studentPeriodsDTO.getPeriodId());
            builder.append(" AND d.periodId.id = :periodId ");
        }
        List<StudentPeriods> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public void deleteStudentPeriodsBy(Integer teacherId, Integer periodId, Integer studentId) {
        if (teacherId != null && periodId != null && studentId != null) {
            String sql = " delete from StudentPeriods s where s.teacherId.id = " + teacherId
                    + " AND s.periodId.id = " + periodId + " AND s.studentId.id = " + studentId;
            dao.executeDeleteQuery(sql);
        }
    }

    public Boolean validateStudentMonth(StudentPeriodsDTO studentPeriodsDTO) {
        Map<String, Object> params = new HashMap();
        params.put("periodId", studentPeriodsDTO.getPeriodId());
        params.put("studentId", studentPeriodsDTO.getStudentId());
        params.put("teacherId", studentPeriodsDTO.getTeacherId());
        List<StudentPeriods> studentPeriod = dao.findListByQuery(" SELECT e FROM StudentPeriods e WHERE e.periodId.id = :periodId "
                + " AND e.studentId.id = :studentId  AND e.teacherId.id = :teacherId ", params);
        if (studentPeriod != null && !studentPeriod.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public void deleteStudentPeriods(StudentPeriodsDTO studentPeriodsDTO) {
        StudentPeriods entity = new StudentPeriods();
        entity.setId(studentPeriodsDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteStudentPeriods(Integer studentPeriodsDTO) {
        StudentPeriods entity = new StudentPeriods();
        entity.setId(studentPeriodsDTO);
        dao.deleteEntity(entity);
    }

    @Override
    public StudentPeriodsDTO save(StudentPeriodsDTO studentPeriodsDTO, IgmaUser isagUser) {
        StudentPeriodsDTO msg = new StudentPeriodsDTO();
        StudentPeriods studentPeriods = null;
        if (studentPeriodsDTO.getId() != null) {
            studentPeriods = dao.findEntityById(StudentPeriods.class, studentPeriodsDTO.getId());
        }
        if (studentPeriodsDTO != null && studentPeriodsDTO.getStudentId() != null
                && studentPeriodsDTO.getBranchId() != null
                && studentPeriodsDTO.getCompanyId() != null
                && studentPeriodsDTO.getValue() != null
                && studentPeriodsDTO.getTeacherId() != null
                && studentPeriodsDTO.getPeriodId() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(studentPeriodsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(studentPeriodsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            if (validateStudentMonth(studentPeriodsDTO)) {
                studentPeriods = mapToEntity(studentPeriodsDTO, isagUser, studentPeriods);
                dao.updateEntity(studentPeriods);
                msg.setMsg("تم الحفظ بنجاح");
                msg.setMsgType("success");
                return msg;
            } else {
                msg.setMsg(" مدفوع بالفعل");
                msg.setMsgType("fail");
                return msg;
            }
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    public StudentDTO ResetPassword(StudentDTO studentDTO) {
        StudentDTO msg = new StudentDTO();
        Student student = null;
        if (studentDTO.getId() != null && studentDTO.getPassword() != null) {
            student = dao.findEntityById(Student.class, studentDTO.getId());
        }
        if (studentDTO.getPassword().equals(student.getPassword())) {
            student.setMacId(null);
            dao.updateEntity(student);
            msg.setMsg("تم الحفظ بنجاح");
            msg.setMsgType("success");
            return msg;
        } else {
            msg.setMsg("خطأ فى الباسورد ");
            msg.setMsgType("fail");
            return msg;
        }

    }

    private List<StudentPeriodsDTO> returnListDTO(List<StudentPeriods> list) {
        List<StudentPeriodsDTO> dTOList = new ArrayList<>();
        for (StudentPeriods entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private StudentPeriodsDTO mapTODTO(StudentPeriods entity) {
        StudentPeriodsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private StudentPeriodsDTO initMapToDTO(StudentPeriods entity) {

        StudentPeriodsDTO dTO = new StudentPeriodsDTO();
        dTO.setId(entity.getId());
        dTO.setConfirm(entity.getConfirm());
        dTO.setValue(entity.getValue());
        dTO.setStudentName(entity.getStudentId().getFullName());
        dTO.setGovernorateName(entity.getStudentId().getStateId().getGovernorateId().getName());
        dTO.setStateName(entity.getStudentId().getStateId().getName());
        dTO.setSchoolStagesName(entity.getStudentId().getSchoolStagesId().getName());
        dTO.setSchoolStagesId(entity.getStudentId().getSchoolStagesId().getId());
        dTO.setStudentMobile(entity.getStudentId().getMobile());

        if (entity.getStudentId() != null) {
            dTO.setStudentId(entity.getStudentId().getId());
            dTO.setStudentName(entity.getStudentId().getFullName());
        }

        if (entity.getTeacherId() != null) {
            dTO.setTeacherId(entity.getTeacherId().getId());
            dTO.setTeacherName(entity.getTeacherId().getName());
        }

        if (entity.getPeriodId() != null) {
            dTO.setPeriodId(entity.getPeriodId().getId());
            dTO.setPeriodName(entity.getPeriodId().getName());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private StudentPeriods mapToEntity(StudentPeriodsDTO dTO, IgmaUser isagUser, StudentPeriods entity) {
        if (entity == null) {
            entity = new StudentPeriods();
        }
        entity.setValue(dTO.getValue());
        entity.setId(dTO.getId());
        entity.setConfirm(dTO.getConfirm());
        //entity.setMonthId(dTO.getMonthId());

        if (dTO.getStudentId() != null) {
            Student student = new Student();
            student.setId(dTO.getStudentId());
            entity.setStudentId(student);
        }
        if (dTO.getTeacherId() != null) {
            IgmaUser igmaUser = new IgmaUser();
            igmaUser.setId(dTO.getTeacherId());
            entity.setTeacherId(igmaUser);
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
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}

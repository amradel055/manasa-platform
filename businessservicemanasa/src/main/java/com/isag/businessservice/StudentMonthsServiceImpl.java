/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.StudentMonthsDTO;
import com.isag.entity.Branch;
import com.isag.entity.IsagCompany;
import com.isag.entity.StudentMonths;
import com.isag.entity.IsagUser;
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
public class StudentMonthsServiceImpl implements StudentMonthsService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<Integer> findStudentMonthsByMonths(Integer studentDTO) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (studentDTO != null) {
            params.put("studentId", studentDTO);
            query = "SELECT b.monthId FROM StudentMonths b WHERE b.studentDTO.id = :studentId";
        }
        List<Integer> studentMonthsList = dao.findListByQuery(query, params);
        return studentMonthsList;
    }

    @Override
    public List<StudentMonthsDTO> findStudentMonths(Integer branchId, Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM StudentMonths d WHERE d.branchId.id = :branchId  ");
        if (id != null) {
            params.put("id", id);
            builder.append(" AND d.id = :id ");
        }
        builder.append(" order by d.id desc ");
        List<StudentMonths> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    public Boolean validateStudentMonth(StudentMonthsDTO studentMonthsDTO) {
        Map<String, Object> params = new HashMap();
        params.put("monthId", studentMonthsDTO.getMonthId());
        params.put("studentId", studentMonthsDTO.getStudentId());
        List<StudentMonths> studentMonth = dao.findListByQuery("SELECT e FROM StudentMonths e WHERE e.monthId = :monthId AND e.studentDTO.id = :studentId ", params);
        if (studentMonth != null && !studentMonth.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    @Override
    public void deleteStudentMontsByStudent(Integer studentDTO) {
        String sql = " delete from StudentMonths s where s.id > 0 AND s.studentDTO.id = " + studentDTO;
        dao.executeDeleteQuery(sql);
    }

    @Override
    public void deleteStudentMonths(StudentMonthsDTO studentMonthsDTO) {
        StudentMonths entity = new StudentMonths();
        entity.setId(studentMonthsDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteStudentMonths(Integer studentMonthsDTO) {
        StudentMonths entity = new StudentMonths();
        entity.setId(studentMonthsDTO);
        dao.deleteEntity(entity);
    }

    @Override
    public StudentMonthsDTO save(StudentMonthsDTO studentMonthsDTO, IsagUser isagUser) {
        StudentMonthsDTO msg = new StudentMonthsDTO();
        StudentMonths studentMonths = null;
        if (studentMonthsDTO.getId() != null) {
            studentMonths = dao.findEntityById(StudentMonths.class, studentMonthsDTO.getId());
        }
        if (studentMonthsDTO != null && studentMonthsDTO.getStudentId() != null
                && studentMonthsDTO.getMonthId() != null
                && studentMonthsDTO.getBranchId() != null
                && studentMonthsDTO.getCompanyId() != null
                && studentMonthsDTO.getValue() != null) {
            if (isagUser == null) {
                isagUser = new IsagUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(studentMonthsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IsagCompany isagCompany = new IsagCompany();
                isagCompany.setId(studentMonthsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            if (validateStudentMonth(studentMonthsDTO)) {
                studentMonths = mapToEntity(studentMonthsDTO, isagUser, studentMonths);
                dao.updateEntity(studentMonths);
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

    private List<StudentMonthsDTO> returnListDTO(List<StudentMonths> list) {
        List<StudentMonthsDTO> dTOList = new ArrayList<>();
        for (StudentMonths entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private StudentMonthsDTO mapTODTO(StudentMonths entity) {
        StudentMonthsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private StudentMonthsDTO initMapToDTO(StudentMonths entity) {

        StudentMonthsDTO dTO = new StudentMonthsDTO();
        dTO.setId(entity.getId());
        dTO.setConfirm(entity.getConfirm());
        dTO.setValue(entity.getValue());
        dTO.setMonthId(entity.getMonthId());
        dTO.setStudentName(entity.getStudentDTO().getFullName());
        dTO.setGovernorateName(entity.getStudentDTO().getStateId().getGovernorateId().getName());
        dTO.setStateName(entity.getStudentDTO().getStateId().getName());
        dTO.setSchoolStagesName(entity.getStudentDTO().getSchoolStagesId().getName());
        dTO.setSchoolStagesId(entity.getStudentDTO().getSchoolStagesId().getId());
        dTO.setStudentMobile(entity.getStudentDTO().getMobile());

        if (entity.getStudentDTO() != null) {
            dTO.setStudentId(entity.getStudentDTO().getId());
            dTO.setStateName(entity.getStudentDTO().getFullName());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private StudentMonths mapToEntity(StudentMonthsDTO dTO, IsagUser isagUser, StudentMonths entity) {
        if (entity == null) {
            entity = new StudentMonths();
        }
        entity.setValue(dTO.getValue());
        entity.setId(dTO.getId());
        entity.setConfirm(dTO.getConfirm());
        entity.setMonthId(dTO.getMonthId());

        if (dTO.getStudentId() != null) {
            Student student = new Student();
            student.setId(dTO.getStudentId());
            entity.setStudentDTO(student);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.api.dto.SearchStudentDTO;
import com.isag.core.GenericDAO;
import com.isag.dto.StudentPeriodsOrderDTO;
import com.isag.entity.Branch;
import com.isag.entity.StudentPeriodsOrder;
import com.isag.entity.Materials;
import com.isag.entity.Periods;
import com.isag.entity.IgmaUser;
import com.isag.entity.Student;
import com.isag.entity.StudentPeriods;
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
public class StudentPeriodsOrderServiceImpl implements StudentPeriodsOrderService {

    @EJB
    private GenericDAO dao;

    @EJB
    StudentPeriodsService periodsService;

    private StudentPeriodsOrderDTO mapTODTO(StudentPeriodsOrder entity) {
        StudentPeriodsOrderDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    @Override
    public void save(StudentPeriodsOrderDTO studentPeriodsOrderDTO, IgmaUser isagUser) {
        StudentPeriodsOrder studentPeriodsOrder = mapToEntity(studentPeriodsOrderDTO, isagUser, null);
        dao.updateEntity(studentPeriodsOrder);
    }

    @Override
    public void delete(Integer studentPeriodId, IgmaUser igmaUser) {
        StudentPeriodsOrder studentPeriodsOrder = dao.findEntityById(StudentPeriodsOrder.class, studentPeriodId);
        dao.deleteEntity(studentPeriodsOrder);
    }

    @Override
    public StudentPeriodsOrderDTO findStudentPeriod(Integer studentPeriodId) {
        StudentPeriodsOrder studentPeriodsOrder = dao.findEntityById(StudentPeriodsOrder.class, studentPeriodId);
        return mapTODTO(studentPeriodsOrder);
    }

    @Override
    public List<StudentPeriodsOrderDTO> findStudemtPeriodOrderDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM StudentPeriodsOrder d WHERE d.branchId.id = :branchId  ");
        List<StudentPeriodsOrder> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public List<SearchStudentDTO> searchStudent(Integer periodId, Integer materialId) {
        List<SearchStudentDTO> searchStudentDTOList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM StudentPeriodsOrder d ");

        if (materialId != null) {
            params.put("materialId", materialId);
            builder.append(" WHERE d.materialId.id = :materialId  ");
        }
        if (periodId != null) {
            params.put("periodId", periodId);

            builder.append(" AND d.periodId.id = :periodId ");
        }

        List<StudentPeriodsOrder> studentList = dao.findListByQuery(builder.toString(), params);
        for (StudentPeriodsOrder student : studentList) {
            SearchStudentDTO searchStudentDTO = new SearchStudentDTO();
            searchStudentDTO.setMobile(student.getStudentId().getMobile());
            searchStudentDTO.setName(student.getStudentId().getFullName());
            searchStudentDTO.setId(student.getId());
            searchStudentDTO.setCash(student.getValue());
            searchStudentDTOList.add(searchStudentDTO);
        }
        return searchStudentDTOList;
    }

    private List<StudentPeriodsOrderDTO> returnListDTO(List<StudentPeriodsOrder> list) {
        List<StudentPeriodsOrderDTO> dTOList = new ArrayList<>();
        for (StudentPeriodsOrder entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private StudentPeriodsOrderDTO initMapToDTO(StudentPeriodsOrder entity) {

        StudentPeriodsOrderDTO dTO = new StudentPeriodsOrderDTO();
        dTO.setId(entity.getId());
        dTO.setConfirm(entity.getConfirm());
        dTO.setValue(entity.getValue());
        dTO.setRemark(entity.getRemark());
        dTO.setStudentName(entity.getStudentId().getFullName());
        if (entity.getStudentId() != null && entity.getStudentId().getStateId() != null
                && entity.getStudentId().getStateId().getGovernorateId() != null) {
            dTO.setGovernorateName(entity.getStudentId().getStateId().getGovernorateId().getName());
            dTO.setStateName(entity.getStudentId().getStateId().getName());
        }
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

        if (entity.getMaterialId() != null) {
            dTO.setMaterialId(entity.getMaterialId().getId());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private StudentPeriodsOrder mapToEntity(StudentPeriodsOrderDTO dTO, IgmaUser isagUser, StudentPeriodsOrder entity) {
        if (entity == null) {
            entity = new StudentPeriodsOrder();
        }
        entity.setValue(dTO.getValue());
        entity.setRemark(dTO.getRemark());
        entity.setId(dTO.getId());
        entity.setConfirm(dTO.getConfirm());

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

        if (dTO.getMaterialId() != null) {
            Materials materials = new Materials();
            materials.setId(dTO.getMaterialId());
            entity.setMaterialId(materials);
        }

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        Branch branch = new Branch();
        branch.setId(232);
        entity.setBranchId(branch);
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    @Override
    public void transaction(Integer studentPeriodOrderId, IgmaUser igmaUser) {
        StudentPeriodsOrder studentPeriodsOrder = dao.findEntityById(StudentPeriodsOrder.class, studentPeriodOrderId);
        if (studentPeriodsOrder != null) {
            StudentPeriods studentPeriods = new StudentPeriods();
            studentPeriods.setBranchId(studentPeriodsOrder.getBranchId());
            studentPeriods.setCompanyId(igmaUser.getCompanyId());
            studentPeriods.setCreationDate(new Date());
            studentPeriods.setCreatedBy(igmaUser.getCreatedBy());
            studentPeriods.setStudentId(studentPeriodsOrder.getStudentId());
            studentPeriods.setTeacherId(studentPeriodsOrder.getTeacherId());
            studentPeriods.setPeriodId(studentPeriodsOrder.getPeriodId());
            studentPeriods.setMaterialId(studentPeriodsOrder.getMaterialId());
            studentPeriods.setRemark(studentPeriodsOrder.getRemark());
            studentPeriods.setValue(studentPeriodsOrder.getValue());
            studentPeriods.setConfirm(1);
            periodsService.saveTransaction(studentPeriods);
//            dao.updateEntity(studentPeriods);
        }
        dao.deleteEntity(studentPeriodsOrder);
    }

}

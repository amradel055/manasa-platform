/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.StudentStatusDTO;
import com.isag.entity.IgmaUser;
import com.isag.entity.Student;
import com.isag.entity.StudentStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class StudentStatusServiceImpl implements StudentStatusService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteStudentReset(Integer studentResetDTO) {
        StudentStatus entity = new StudentStatus();
        entity.setId(studentResetDTO);
        dao.deleteEntity(entity);
    }

    @Override
    public StudentStatusDTO save(StudentStatusDTO studentResetDTO, IgmaUser igmaUser) {
        StudentStatus reset = mapToEntity(studentResetDTO, igmaUser);
        reset = dao.updateEntity(reset);
        studentResetDTO = mapTODTO(reset);
        return studentResetDTO;
    }
    
    @Override
    public Integer findStudentStatus(Integer studentId , Integer branchId) {
        List<Integer> status = dao.executeNativeQuery("SELECT student_status FROM student_status WHERE student_id = "+studentId+" and branch_id = "+branchId);
        // 1 -> is active this is default
        return status != null && !status.isEmpty() ? status.get(0) : 1;
    }

    private List<StudentStatusDTO> returnListDTO(List<StudentStatus> list) {
        List<StudentStatusDTO> dTOList = new ArrayList<>();
        for (StudentStatus entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private StudentStatusDTO mapTODTO(StudentStatus entity) {
        StudentStatusDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private StudentStatusDTO initMapToDTO(StudentStatus entity) {

        StudentStatusDTO dTO = new StudentStatusDTO();
        dTO.setId(entity.getId());
        dTO.setStudentStatus(entity.getStudentStatus());

        if (entity.getStudentId() != null) {
            dTO.setStudentId(entity.getStudentId().getId());
            dTO.setStudentName(entity.getStudentId().getFullName());
        }

        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private StudentStatus mapToEntity(StudentStatusDTO dTO, IgmaUser isagUser) {

        StudentStatus entity = new StudentStatus();

        entity.setId(dTO.getId());
        entity.setStudentStatus(dTO.getStudentStatus());

        if (dTO.getStudentId() != null) {
            Student student = new Student();
            student.setId(dTO.getStudentId());
            entity.setStudentId(student);
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

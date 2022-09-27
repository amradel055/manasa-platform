/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.StudentQuestionsDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaUser;
import com.isag.entity.Materials;
import com.isag.entity.StudentQuestions;
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
public class StudentQuestionsServiceImpl implements StudentQuestionsService {

    @EJB
    private GenericDAO dao;
    @EJB
    private TeacherAnswersService teacherAnswersService;

    private StudentQuestionsDTO mapTODTO(StudentQuestions entity) {
        StudentQuestionsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    @Override
    public void save(StudentQuestionsDTO studentQuestionsDTO, IgmaUser isagUser) {
        StudentQuestions studentPeriodsOrder = mapToEntity(studentQuestionsDTO, isagUser);
        dao.updateEntity(studentPeriodsOrder);
    }

    @Override
    public void delete(Integer id, IgmaUser igmaUser) {
        StudentQuestions studentPeriodsOrder = dao.findEntityById(StudentQuestions.class, id);
        dao.deleteEntity(studentPeriodsOrder);
    }

    @Override
    public List<StudentQuestionsDTO> findStudemtPeriodOrderDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM StudentQuestions d WHERE d.branchId.id = :branchId  ");
        List<StudentQuestions> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    private List<StudentQuestionsDTO> returnListDTO(List<StudentQuestions> list) {
        List<StudentQuestionsDTO> dTOList = new ArrayList<>();
        for (StudentQuestions entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private StudentQuestionsDTO initMapToDTO(StudentQuestions entity) {

        StudentQuestionsDTO dTO = new StudentQuestionsDTO();
        dTO.setText(entity.getText());
        dTO.setImage(entity.getImage());
        dTO.setMaterialId(entity.getMaterialId().getId());
        dTO.setTeacherAnswersDTO(teacherAnswersService.findQuestioAnswer(entity.getId()));
        dTO.setTeacherId(entity.getTeacherId().getId());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private StudentQuestions mapToEntity(StudentQuestionsDTO dTO, IgmaUser isagUser) {

        StudentQuestions entity = new StudentQuestions();
        entity.setId(dTO.getId());
        entity.setText(dTO.getText());
        entity.setImage(dTO.getImage());
        if(dTO.getMaterialId() != null){
            Materials  materials = new Materials() ;
            materials.setId(dTO.getMaterialId());
            entity.setMaterialId(materials);
            
        }
        if (dTO.getTeacherId() != null) {
            IgmaUser igmaUser = new IgmaUser();
            igmaUser.setId(dTO.getTeacherId());
            entity.setTeacherId(igmaUser);

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

        Branch branch = new Branch();
        branch.setId(232);
        entity.setBranchId(branch);
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    @Override
    public List<StudentQuestionsDTO> findStudentQuestionsAndAnswer(Integer teacherId, Integer materialId) {
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", teacherId);
        params.put("materialId", materialId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM StudentQuestions d WHERE d.teacherId.id = :teacherId AND d.materialId.id = :materialId  ");
        List<StudentQuestions> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

}

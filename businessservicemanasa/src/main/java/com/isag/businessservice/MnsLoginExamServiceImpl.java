/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.MnsLoginExamDTO;
import com.isag.entity.Branch;
import com.isag.entity.Exams;
import com.isag.entity.IgmaCompany;
import com.isag.entity.MnsLoginExam;
import com.isag.entity.IgmaUser;
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
public class MnsLoginExamServiceImpl implements MnsLoginExamService {

    @EJB
    private GenericDAO dao;

    @Override
    public Integer findLoginExam(MnsLoginExamDTO loginExamDTO) {
        Map<String, Object> params = new HashMap<>();
        MnsLoginExam loginExam = new MnsLoginExam();
        if (loginExam.getExamId().getId() != null && loginExam.getStudentId().getId() != null && loginExamDTO.getTimer() != null) {
            params.put("examId", loginExam.getExamId().getId());
            params.put("studentId", loginExam.getStudentId().getId());
            StringBuilder builder = new StringBuilder();
            builder.append(" SELECT d FROM MnsLoginExam d WHERE d.examId.id = :examId AND d.studentId.id = :studentId ");
            List<MnsLoginExam> list = dao.findListByQuery(builder.toString(), params);
            if (list != null && !list.isEmpty()) {
                MnsLoginExam loginExam1 = list.get(0);
                Date firstLoginDate = loginExam1.getLoginDate();
                Date currentDate = new Date();
                Long allTime = currentDate.getTime() - firstLoginDate.getTime();
                Integer timer = loginExamDTO.getTimer();
                Long timer1 = timer.longValue();
                timer1 = (allTime - timer1);
                return timer1.intValue();
            } else {
                save(loginExamDTO, null);
            }
        }

        return null;
    }

    public MnsLoginExamDTO save(MnsLoginExamDTO loginExamDTO, IgmaUser isagUser) {
        MnsLoginExamDTO msg = new MnsLoginExamDTO();
        MnsLoginExam loginExam = null;

        if (loginExamDTO != null && loginExamDTO.getExamId() != null
                && loginExamDTO.getStudentId() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(loginExamDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(loginExamDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            loginExam = mapToEntity(loginExamDTO, isagUser, null);
            loginExam = dao.updateEntity(loginExam);
            msg.setId(loginExam.getId());
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
    public void deleteMnsLoginExam(MnsLoginExamDTO mnsLoginExamDTO) {
        MnsLoginExam entity = new MnsLoginExam();
        entity.setId(mnsLoginExamDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteMnsLoginExam(Integer mnsLoginExamId) {
        MnsLoginExam entity = new MnsLoginExam();
        entity.setId(mnsLoginExamId);
        dao.deleteEntity(entity);
    }

    private List<MnsLoginExamDTO> returnListDTO(List<MnsLoginExam> list) {
        List<MnsLoginExamDTO> dTOList = new ArrayList<>();
        for (MnsLoginExam entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private MnsLoginExamDTO mapTODTO(MnsLoginExam entity) {
        MnsLoginExamDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private MnsLoginExamDTO initMapToDTO(MnsLoginExam entity) {

        MnsLoginExamDTO dTO = new MnsLoginExamDTO();
        dTO.setId(entity.getId());

        if (entity.getExamId() != null) {
            dTO.setExamId(entity.getExamId().getId());
        }
        if (entity.getStudentId() != null) {
            dTO.setStudentId(entity.getStudentId().getId());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private MnsLoginExam mapToEntity(MnsLoginExamDTO dTO, IgmaUser isagUser, MnsLoginExam entity) {

        if (entity == null) {
            entity = new MnsLoginExam();
        }

        entity.setId(dTO.getId());
        entity.setLoginDate(new Date());

        if (dTO.getExamId() != null) {
            Exams exams = new Exams();
            exams.setId(dTO.getExamId());
            entity.setExamId(exams);
        }
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

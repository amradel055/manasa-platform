/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.OtherAppointmentsDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.Appointments;
import com.isag.entity.Materials;
import com.isag.entity.OtherAppointments;
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
public class OtherAppointmentsServiceImpl implements OtherAppointmentsService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<OtherAppointmentsDTO> findOtherAppointments(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM OtherAppointments d WHERE d.branchId.id = :branchId  ");
        List<OtherAppointments> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public List<OtherAppointmentsDTO> findOtherAppointmentsByTeacherAndSchoolStageAndMaterialId(Integer teacherId , Integer schoolStageId ,  Integer materialId,Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM OtherAppointments d WHERE 1=1  ");
        if(teacherId != null){
            params.put("teacherId", teacherId);
            builder.append(" AND d.teacherId.id = :teacherId ");
        }
        if(schoolStageId != null){
            params.put("schoolStageId", schoolStageId);
            builder.append(" AND d.schoolStageId.id = :schoolStageId ");
        }
        if(materialId != null){
            params.put("materialId", materialId);
            builder.append(" AND d.materialId.id = :materialId ");
        }
        if(type != null){
            params.put("type", type);
            builder.append(" AND d.appointmentsType = :type ");
        }
        List<OtherAppointments> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }
    
    @Override
    public List<OtherAppointmentsDTO> findOtherAppointmentsByStudentId(Integer studentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM OtherAppointments d WHERE d.teacherId.id in (select s.teacherId.id from StudentPeriods where s.studentId.id = :studentId) ");
        builder.append(" AND d.materialId.id in (select s.materialId.id from StudentPeriods where s.studentId.id = :studentId) ");
        builder.append(" order by d.schoolStageId.id , d.teacherId.id ");
        List<OtherAppointments> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }
    

    @Override
    public void deleteOtherAppointments(Integer otherAppointmentsDTO) {
        OtherAppointments entity = new OtherAppointments();
        entity.setId(otherAppointmentsDTO);
        dao.deleteEntity(entity);
    }

    @Override
    public OtherAppointmentsDTO save(OtherAppointmentsDTO otherAppointmentsDTO, IgmaUser isagUser) {
        OtherAppointmentsDTO msg = new OtherAppointmentsDTO();
        OtherAppointments appointments = null;
        if (otherAppointmentsDTO.getId() != null) {
            appointments = dao.findEntityById(OtherAppointments.class, otherAppointmentsDTO.getId());
        }
        if (otherAppointmentsDTO.getTeacherId() != null
                && otherAppointmentsDTO.getSchoolStageId() != null
                && otherAppointmentsDTO.getImg() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(otherAppointmentsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(otherAppointmentsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            appointments = mapToEntity(otherAppointmentsDTO, isagUser, appointments);
            dao.updateEntity(appointments);
            msg.setMsg("تم الحفظ بنجاح");
            msg.setMsgType("success");
            return msg;
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    private List<OtherAppointmentsDTO> returnListDTO(List<OtherAppointments> list) {
        List<OtherAppointmentsDTO> dTOList = new ArrayList<>();
        for (OtherAppointments entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private OtherAppointmentsDTO mapTODTO(OtherAppointments entity) {
        OtherAppointmentsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private OtherAppointmentsDTO initMapToDTO(OtherAppointments entity) {

        OtherAppointmentsDTO dTO = new OtherAppointmentsDTO();
        dTO.setId(entity.getId());
        dTO.setImg(entity.getImg());
        dTO.setAppointmentsType(entity.getAppointmentsType());
        

        if (entity.getTeacherId() != null) {
            dTO.setTeacherId(entity.getTeacherId().getId());
        }
        if (entity.getSchoolStageId() != null) {
            dTO.setSchoolStageId(entity.getSchoolStageId().getId());
        }
        if (entity.getMaterialId() != null) {
            dTO.setMaterialId(entity.getMaterialId().getId());
            dTO.setMaterialName(entity.getMaterialId().getName());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private OtherAppointments mapToEntity(OtherAppointmentsDTO dTO, IgmaUser isagUser, OtherAppointments entity) {
        if (entity == null) {
            entity = new OtherAppointments();
        }

        entity.setId(dTO.getId());
        entity.setImg(dTO.getImg());
        entity.setAppointmentsType(dTO.getAppointmentsType());

        if (dTO.getTeacherId() != null) {
            IgmaUser teachers = new IgmaUser();
            teachers.setId(dTO.getTeacherId());
            entity.setTeacherId(teachers);
        }
        if (dTO.getSchoolStageId() != null) {
            SchoolStagesGuide guide = new SchoolStagesGuide();
            guide.setId(dTO.getSchoolStageId());
            entity.setSchoolStageId(guide);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.AppointmentsDTO;
import com.isag.dto.LessonLocationDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.Appointments;
import com.isag.entity.Materials;
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
public class AppointmentsServiceImpl implements AppointmentsService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<AppointmentsDTO> findAppointments(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Appointments d WHERE d.branchId.id = :branchId  ");
        List<Appointments> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }
    
    @Override
    public List<AppointmentsDTO> findAppointmentsgroup(Integer branchId,Integer schoolStageId ) {
        List<AppointmentsDTO> listNew = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("schoolStageId", schoolStageId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Appointments d WHERE d.branchId.id = :branchId AND d.schoolStageId.id = :schoolStageId  ");
        List<Appointments> list = dao.findListByQuery(builder.toString(), params);
        List<AppointmentsDTO> listDTO = returnListDTO(list);
        Map<String, List<AppointmentsDTO>> mapGroup = new HashMap<>();
        for(AppointmentsDTO dTO : listDTO){
            if(mapGroup.containsKey(dTO.getLessonLocation().getName())){
                List<AppointmentsDTO> appointmentsDTOs = mapGroup.get(dTO.getLessonLocation().getName());
                appointmentsDTOs.add(dTO);
                mapGroup.put(dTO.getLessonLocation().getName(), appointmentsDTOs);
            }else{
                List<AppointmentsDTO> appointmentsDTOs = new ArrayList<>();
                appointmentsDTOs.add(dTO);
                mapGroup.put(dTO.getLessonLocation().getName(), appointmentsDTOs);
            }
        }
        AppointmentsDTO dTO = new AppointmentsDTO();
        dTO.setMap(mapGroup);
        listNew.add(dTO);
        return listNew;
    }

    @Override
    public List<AppointmentsDTO> findAppointmentsByTeacherAndSchoolStageAndMaterialId(Integer teacherId , Integer schoolStageId ,  Integer materialId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Appointments d WHERE 1=1  ");
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
        List<Appointments> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }
    
    @Override
    public List<AppointmentsDTO> findAppointmentsByStudentId(Integer studentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Appointments d WHERE d.teacherId.id in (select s.teacherId.id from StudentPeriods where s.studentId.id = :studentId) ");
        builder.append(" AND d.materialId.id in (select s.materialId.id from StudentPeriods where s.studentId.id = :studentId) ");
        builder.append(" order by d.schoolStageId.id , d.day , d.teacherId.id ");
        List<Appointments> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }
    

    @Override
    public void deleteAppointments(Integer appointmentsDTO) {
        Appointments entity = new Appointments();
        entity.setId(appointmentsDTO);
        dao.deleteEntity(entity);
    }

    @Override
    public AppointmentsDTO save(AppointmentsDTO appointmentsDTO, IgmaUser isagUser) {
        AppointmentsDTO msg = new AppointmentsDTO();
        Appointments appointments = null;
        if (appointmentsDTO.getId() != null) {
            appointments = dao.findEntityById(Appointments.class, appointmentsDTO.getId());
        }
        if (appointmentsDTO.getTeacherId() != null
                && appointmentsDTO.getSchoolStageId() != null
                && appointmentsDTO.getDay() != null
                && appointmentsDTO.getTime() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(appointmentsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(appointmentsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            appointments = mapToEntity(appointmentsDTO, isagUser, appointments);
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

    private List<AppointmentsDTO> returnListDTO(List<Appointments> list) {
        List<AppointmentsDTO> dTOList = new ArrayList<>();
        for (Appointments entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private AppointmentsDTO mapTODTO(Appointments entity) {
        AppointmentsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private AppointmentsDTO initMapToDTO(Appointments entity) {

        AppointmentsDTO dTO = new AppointmentsDTO();
        dTO.setId(entity.getId());
        dTO.setDay(entity.getDay());
        dTO.setTime(entity.getTime());
        dTO.setAppointmentsType(entity.getAppointmentsType());
        dTO.setRemarks(entity.getRemarks());
        
        if(entity.getLessonLocation() != null){
            LessonLocationDTO lessonLocationDTO = new LessonLocationDTO();
            lessonLocationDTO.setName(entity.getLessonLocation().getName());
            dTO.setLessonLocation(lessonLocationDTO);
        }
        

        if(null != entity.getDay())switch (entity.getDay()) {
            case 6:
                dTO.setDayText("السبت");
                break;
            case 7:
                dTO.setDayText("الأحد");
                break;
            case 1:
                dTO.setDayText("الإثنين");
                break;
            case 2:
                dTO.setDayText("الثلاثاء");
                break;
            case 3:
                dTO.setDayText("الأربعاء");
                break;
            case 4:
                dTO.setDayText("الخميس");
                break;
            case 5:
                dTO.setDayText("الجمعه");
                break;
            default:
                break;
        }
        if (entity.getTeacherId() != null) {
            dTO.setTeacherId(entity.getTeacherId().getId());
            dTO.setTeacherName(entity.getTeacherId().getName());
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

    private Appointments mapToEntity(AppointmentsDTO dTO, IgmaUser isagUser, Appointments entity) {
        if (entity == null) {
            entity = new Appointments();
        }

        entity.setId(dTO.getId());
        entity.setDay(dTO.getDay());
        entity.setTime(dTO.getTime());
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

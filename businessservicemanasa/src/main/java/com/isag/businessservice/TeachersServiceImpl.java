/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.TeachersDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.Teachers;
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
public class TeachersServiceImpl implements TeachersService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<TeachersDTO> findTeachers(Integer branchId, Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Teachers d WHERE d.branchId.id = :branchId  ");
        if (id != null) {
            params.put("id", id);
            builder.append(" AND d.id = :id ");
        }
        builder.append(" order by d.id desc ");
        List<Teachers> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    public Boolean validateTeacher(TeachersDTO teachersDTO) {
        Map<String, Object> params = new HashMap();
        params.put("serial", teachersDTO.getSerial());
        List<Teachers> teacherses = dao.findListByQuery(" SELECT e FROM Teachers e WHERE e.serial = :serial ", params);
        if (teacherses != null && !teacherses.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public void deleteTeachers(TeachersDTO teachersDTO) {
        Teachers entity = new Teachers();
        entity.setId(teachersDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteTeachers(Integer teachersDTO) {
        Teachers entity = new Teachers();
        entity.setId(teachersDTO);
        dao.deleteEntity(entity);
    }

    @Override
    public TeachersDTO save(TeachersDTO teachersDTO, IgmaUser isagUser) {
        TeachersDTO msg = new TeachersDTO();
        Teachers teachers = null;
        if (teachersDTO.getId() != null) {
            teachers = dao.findEntityById(Teachers.class, teachersDTO.getId());
        }
        if (teachersDTO != null && teachersDTO.getName() != null
                && teachersDTO.getBranchId() != null
                && teachersDTO.getCompanyId() != null
                && teachersDTO.getSerial() != null
                && teachersDTO.getDescription() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(teachersDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(teachersDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            if (validateTeacher(teachersDTO)) {
                teachers = mapToEntity(teachersDTO, isagUser, teachers);
                dao.updateEntity(teachers);
                msg.setMsg("تم الحفظ بنجاح");
                msg.setMsgType("success");
                return msg;
            } else {
                msg.setMsg(" موجود بالفعل");
                msg.setMsgType("fail");
                return msg;
            }
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    private List<TeachersDTO> returnListDTO(List<Teachers> list) {
        List<TeachersDTO> dTOList = new ArrayList<>();
        for (Teachers entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private TeachersDTO mapTODTO(Teachers entity) {
        TeachersDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private TeachersDTO initMapToDTO(Teachers entity) {

        TeachersDTO dTO = new TeachersDTO();
        dTO.setId(entity.getId());
        dTO.setName(entity.getName());
        dTO.setSerial(entity.getSerial());
        dTO.setDescription(entity.getDescription());
        dTO.setImage(entity.getImage());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private Teachers mapToEntity(TeachersDTO dTO, IgmaUser isagUser, Teachers entity) {
        if (entity == null) {
            entity = new Teachers();
        }

        entity.setId(dTO.getId());
        entity.setName(dTO.getName());
        entity.setDescription(dTO.getDescription());
        entity.setSerial(dTO.getSerial());
        entity.setImage(dTO.getImage());

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

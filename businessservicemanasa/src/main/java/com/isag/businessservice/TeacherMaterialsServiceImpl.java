/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.TeacherMaterialsDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.Materials;
import com.isag.entity.TeacherMaterials;
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
public class TeacherMaterialsServiceImpl implements TeacherMaterialsService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<TeacherMaterialsDTO> findTeacherMaterials(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM TeacherMaterials d WHERE d.branchId.id = :branchId  ");
        List<TeacherMaterials> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public void deleteTeacherMaterials(Integer teacherMaterialsDTO) {
        TeacherMaterials entity = new TeacherMaterials();
        entity.setId(teacherMaterialsDTO);
        dao.deleteEntity(entity);
    }

    @Override
    public TeacherMaterialsDTO save(TeacherMaterialsDTO teacherMaterialsDTO, IgmaUser isagUser) {
        TeacherMaterialsDTO msg = new TeacherMaterialsDTO();
        TeacherMaterials teacherMaterials = null;
        if (teacherMaterialsDTO.getId() != null) {
            teacherMaterials = dao.findEntityById(TeacherMaterials.class, teacherMaterialsDTO.getId());
        }
        if (teacherMaterialsDTO.getTeacherId() != null
                && teacherMaterialsDTO.getMaterialId() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(teacherMaterialsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(teacherMaterialsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            teacherMaterials = mapToEntity(teacherMaterialsDTO, isagUser, teacherMaterials);
            dao.updateEntity(teacherMaterials);
            msg.setMsg("تم الحفظ بنجاح");
            msg.setMsgType("success");
            return msg;
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    private List<TeacherMaterialsDTO> returnListDTO(List<TeacherMaterials> list) {
        List<TeacherMaterialsDTO> dTOList = new ArrayList<>();
        for (TeacherMaterials entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private TeacherMaterialsDTO mapTODTO(TeacherMaterials entity) {
        TeacherMaterialsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private TeacherMaterialsDTO initMapToDTO(TeacherMaterials entity) {

        TeacherMaterialsDTO dTO = new TeacherMaterialsDTO();
        dTO.setId(entity.getId());

        if (entity.getTeacherId() != null) {
            dTO.setTeacherId(entity.getTeacherId().getId());
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

    private TeacherMaterials mapToEntity(TeacherMaterialsDTO dTO, IgmaUser isagUser, TeacherMaterials entity) {
        if (entity == null) {
            entity = new TeacherMaterials();
        }

        entity.setId(dTO.getId());

        if (dTO.getTeacherId() != null) {
            IgmaUser igmaUser = new IgmaUser();
            igmaUser.setId(dTO.getTeacherId());
            entity.setTeacherId(igmaUser);
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

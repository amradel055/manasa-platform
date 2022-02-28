/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.MaterialsDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.Materials;
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
public class MaterialsServiceImpl implements MaterialsService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<MaterialsDTO> findMaterials(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Materials d WHERE d.branchId.id = :branchId  ");
        List<Materials> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public List<MaterialsDTO> findMaterialsByTeacher(Integer teacherId) {
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", teacherId);
        StringBuilder builder = new StringBuilder();
        
        builder.append("SELECT d.materialId FROM TeacherMaterials d WHERE d.teacherId.id = :teacherId  ");
        List<Materials> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public void deleteMaterials(Integer materialsDTO) {
        Materials entity = new Materials();
        entity.setId(materialsDTO);
        dao.deleteEntity(entity);
    }

    @Override
    public MaterialsDTO save(MaterialsDTO materialsDTO, IgmaUser isagUser) {
        MaterialsDTO msg = new MaterialsDTO();
        Materials materials = null;
        if (materialsDTO.getId() != null) {
            materials = dao.findEntityById(Materials.class, materialsDTO.getId());
        }
        if (materialsDTO.getName() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(materialsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(materialsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            materials = mapToEntity(materialsDTO, isagUser, materials);
            dao.updateEntity(materials);
            msg.setMsg("تم الحفظ بنجاح");
            msg.setMsgType("success");
            return msg;
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    private List<MaterialsDTO> returnListDTO(List<Materials> list) {
        List<MaterialsDTO> dTOList = new ArrayList<>();
        for (Materials entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private MaterialsDTO mapTODTO(Materials entity) {
        MaterialsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private MaterialsDTO initMapToDTO(Materials entity) {

        MaterialsDTO dTO = new MaterialsDTO();
        dTO.setId(entity.getId());
        dTO.setName(entity.getName());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private Materials mapToEntity(MaterialsDTO dTO, IgmaUser isagUser, Materials entity) {
        if (entity == null) {
            entity = new Materials();
        }

        entity.setId(dTO.getId());
        entity.setName(dTO.getName());

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

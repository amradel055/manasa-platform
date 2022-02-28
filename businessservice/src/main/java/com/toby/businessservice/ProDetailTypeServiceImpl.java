/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.ProDetailTypeDTO;
import com.toby.entity.Branch;
import com.toby.entity.ProDetailType;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ProDetailTypeServiceImpl implements ProDetailTypeService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ProDetailTypeDTO> getallProDetailTypeByBranchId(Integer branchId, String query) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        sql.append("SELECT e FROM ProDetailType e WHERE e.branchId.id = :branchId");
        if (query != null && !query.isEmpty()) {
            params.put("query", query);
            appendOrganizationTypeAndActive(sql);
        }

        List<ProDetailType> invGroupList = dao.findListByQuery(sql.toString(), params, 0, 10);
        List<ProDetailTypeDTO> listDTO = mapToDTO(invGroupList);
        return listDTO;
    }

    private void appendOrganizationTypeAndActive(StringBuilder sql) {
        sql.append("  AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ))");
        sql.append(" ORDER BY e.code");
    }

    @Override
    public void deleteProDetailTypeDTO(ProDetailTypeDTO dto) {
        ProDetailType entity = new ProDetailType();
        entity.setId(dto.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public ProDetailTypeDTO findProDetailTypeDTO(Integer id, TobyUser tobyUser) {
        ProDetailType proDetailType = dao.findEntityById(ProDetailType.class, id);
        return mapToDTO(proDetailType, tobyUser);
    }

    @Override
    public ProDetailTypeDTO saveProDetailTypeDTO(ProDetailTypeDTO dto, TobyUser tobyUser) {
        try {

            dto.setMsg(null);
            String error = genralValidate(tobyUser, dto);
            if (error.isEmpty()) {
                ProDetailType invGroup = dao.updateEntity(mapToEntity(dto, tobyUser));
                dto = mapToDTO(invGroup, tobyUser);
            } else {
                dto.setMsg(error);
            }
            return dto;
        } catch (Exception e) {
        }
        return null;
    }

    public String genralValidate(TobyUser tobyUser, ProDetailTypeDTO dto) {
        StringBuilder errorMessage = new StringBuilder();
        if (!validateName(tobyUser, dto.getId(), dto.getName())) {
            errorMessage.append("الاسم موجود");

        }
        if (!validateCode(tobyUser, dto.getId(), dto.getCode())) {
            errorMessage.append("الكود موجود");
        }

        return errorMessage.toString();
    }

    @Override
    public Boolean validateName(TobyUser tobyUser, Integer ProDetailTypeId, String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());

        params.put("name", name);
        StringBuilder querry = new StringBuilder();
        querry.append("SELECT i FROM ProDetailType i where i.branchId.id = :branchId  and i.name = :name");
        if (ProDetailTypeId != null) {
            params.put("ProDetailTypeId", ProDetailTypeId);
            querry.append(" and i.id != :ProDetailTypeId");
        }
        List<ProDetailType> ProDetailTypeList = dao.findListByQuery(querry.toString(), params);
        if (ProDetailTypeList.isEmpty()) {
            return true;
        } else {

            return false;
        }
    }

    @Override
    public Boolean validateCode(TobyUser tobyUser, Integer ProDetailTypeId, Integer code) {

        StringBuilder quary = new StringBuilder();

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("code", code);
        StringBuilder querry = new StringBuilder();
        querry.append("SELECT e FROM ProDetailType e where e.branchId.id = :branchId  and (e.code = :code)");
        if (ProDetailTypeId != null) {
            params.put("ProDetailTypeId", ProDetailTypeId);
            querry.append("and e.id != :ProDetailTypeId");
        }
        List<ProDetailType> ProDetailTypeList = dao.findListByQuery(querry.toString(), params);
        if (ProDetailTypeList.isEmpty()) {
            return true;
        } else {

            return false;
        }

    }

    private List<ProDetailTypeDTO> mapToDTO(List<ProDetailType> list) {
        List<ProDetailTypeDTO> listDTO = new ArrayList<>();
        for (ProDetailType group : list) {
            ProDetailTypeDTO dto = new ProDetailTypeDTO();
            dto.setId(group.getId());
            dto.setName(group.getName());
            dto.setCode(group.getCode());
            listDTO.add(dto);
        }
        return listDTO;
    }

    public ProDetailTypeDTO mapToDTO(ProDetailType entity, TobyUser tobyUser) {

        ProDetailTypeDTO dto = new ProDetailTypeDTO();

        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreationDate());
        dto.setCode(entity.getCode());
        dto.setIndex(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setBranchId(entity.getBranchId().getId() != null ? entity.getBranchId().getId() : null);
        dto.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);

        return dto;
    }

    public ProDetailType mapToEntity(ProDetailTypeDTO dto, TobyUser tobyUser) {
        ProDetailType entity = new ProDetailType();
        entity.setId(dto.getId());

        entity.setCode(dto.getCode());

        entity.setName(dto.getName());
        if (dto.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(dto.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dto.getCreatedDate());
            entity.setModifiedBy(tobyUser);
            entity.setModificationDate(new Date());
            if (dto.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(dto.getCompanyId());
                entity.setCompanyId(company);
            }
            if (dto.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(dto.getBranchId());
                entity.setBranchId(branch);
            }
        } else {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
            entity.setCompanyId(tobyUser.getCompanyId());
            entity.setBranchId(tobyUser.getBranchId());

        }
        return entity;

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.ZoomLinkDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaUser;
import com.isag.entity.Materials;
import com.isag.entity.SchoolStagesGuide;
import com.isag.entity.ZoomLink;
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
public class ZoomLinkServiceImpl implements ZoomLinkService {

    @EJB
    private GenericDAO dao;

    @Override
    public ZoomLinkDTO save(ZoomLinkDTO dto, IgmaUser igmaUser) {
        if (dto != null) {
            ZoomLink entity = mapToEntity(dto, igmaUser);
            entity = dao.updateEntity(entity);
            dto.setId(entity.getId());
            return dto;
        }
        return dto;
    }

    @Override
    public void delete(Integer id) {
        ZoomLink zoomLink = dao.findEntityById(ZoomLink.class, id);
        dao.deleteEntity(zoomLink);
    }

    @Override
    public ZoomLinkDTO findById(Integer id) {
        ZoomLink entity = dao.findEntityById(ZoomLink.class, id);
        return mapToDTO(entity);
    }

    private ZoomLinkDTO mapToDTO(ZoomLink entity) {
        if (entity != null) {
            ZoomLinkDTO dto = new ZoomLinkDTO();
            dto.setId(entity.getId());
            dto.setMaterialId(entity.getMaterialId() == null ? null : entity.getMaterialId().getId());
            dto.setMaterialName(entity.getMaterialId() == null ? null : entity.getMaterialId().getName());
            dto.setLink(entity.getLink());
            dto.setTeacherId(entity.getTeacherId() == null ? null : entity.getTeacherId().getId());
            dto.setSchoolId(entity.getSchoolId() == null ? null : entity.getSchoolId().getId());
            dto.setSchoolName(entity.getSchoolId() == null ? null : entity.getSchoolId().getName());
            return dto;
        }
        return null;
    }

    private ZoomLink mapToEntity(ZoomLinkDTO dto, IgmaUser igmaUser) {
        if (dto != null) {
            ZoomLink entity = new ZoomLink();
            entity.setId(dto.getId());
            entity.setLink(dto.getLink());
            if (dto.getSchoolId() != null) {
                entity.setSchoolId(new SchoolStagesGuide(dto.getSchoolId()));
            }
            if (dto.getMaterialId() != null) {
                entity.setMaterialId(new Materials(dto.getMaterialId()));
            }
            if (dto.getTeacherId() != null) {
                entity.setTeacherId(new IgmaUser(dto.getTeacherId()));
            }
            if (dto.getId() == null) {
                entity.setCreatedBy(igmaUser);
                entity.setCreationDate(new Date());
            } else {
                entity.setId(dto.getId());
                entity.setModificationDate(new Date());
                entity.setModifiedBy(igmaUser);
            }

            Branch branch = new Branch();
            branch.setId(232);
            entity.setBranchId(branch);
            entity.setCompanyId(igmaUser.getCompanyId());

            return entity;
        }
        return null;
    }

    @Override
    public List<ZoomLinkDTO> find(Integer teacherId, Integer schoolId, Integer materialId) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder builder = new StringBuilder();

        builder.append("SELECT d FROM ZoomLink d WHERE 1=1    ");

        if (teacherId != null) {
            params.put("teacherId", teacherId);
            builder.append(" AND d.teacherId.id = :teacherId");
        }
        if (materialId != null) {
            params.put("materialId", materialId);
            builder.append(" AND d.materialId.id = :materialId");
        }
        if (schoolId != null) {
            params.put("schoolId", schoolId);
            builder.append(" AND d.materialId.stageId.id = :schoolId");
        }

        builder.append(" order by d.id ASC ");

        List<ZoomLink> list = dao.findListByQuery(builder.toString(), params);
        if (list != null && !list.isEmpty()) {
            List<ZoomLinkDTO> listDTO = new ArrayList<>();
            for (ZoomLink entity : list) {
                listDTO.add(mapToDTO(entity));
            }
            return listDTO;
        }
        return null;
    }

}

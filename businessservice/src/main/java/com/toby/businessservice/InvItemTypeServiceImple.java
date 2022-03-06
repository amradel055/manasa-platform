/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvItemTypeDTO;
import com.toby.entity.InvItemType;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class InvItemTypeServiceImple implements InvItemTypeService {

    @EJB
    private GenericDAO dao;

    @Override
    public InvItemTypeDTO findInvItemTypeDTOById(Integer invItemId) {
        return dao.findEntityById(InvItemTypeDTO.class, invItemId);
    }

    @Override
    public InvItemTypeDTO save(InvItemTypeDTO invItemTypeDTO, TobyUser tobyUser) {
        InvItemType invItemType = null;
        if (invItemTypeDTO != null) {
            invItemType = mapToEntity(invItemTypeDTO, tobyUser);
            if (invItemTypeDTO.getId() != null) {
                invItemType = dao.updateEntity(invItemType);

            } else {
                invItemType = dao.updateEntity(invItemType);

            }

            invItemTypeDTO = mapTODTO(invItemType);
        }
        return invItemTypeDTO;
    }

    @Override
    public List<InvItemTypeDTO> getALLInvItemTypeDTOByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM InvItemType e WHERE e.branchId.id = :branchId ", params);
    }

    @Override
    public void deleteInvItemType(Integer id) {
        InvItemType invItemType = new InvItemType();
        invItemType.setId(id);
        dao.deleteEntity(invItemType);
    }

    private List<InvItemTypeDTO> returnListDTO(List<InvItemType> list) {
        List<InvItemTypeDTO> dtoList = new ArrayList<>();
        for (InvItemType entity : list) {
            dtoList.add(mapTODTO(entity));
        }
        return dtoList;
    }

    public InvItemTypeDTO mapTODTO(InvItemType entity) {
        InvItemTypeDTO dTO = new InvItemTypeDTO();
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setName(entity.getName());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());
        return dTO;

    }

    public InvItemType mapToEntity(InvItemTypeDTO dTO, TobyUser tobyUser) {
        InvItemType entity = new InvItemType();
        entity.setId(dTO.getId());
        entity.setName(dTO.getName());
        if (dTO.getId() == null) {

            entity.setCreatedBy(tobyUser);
            entity.setBranchId(tobyUser.getBranchId());
            entity.setCreationDate(new Date());
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }

}

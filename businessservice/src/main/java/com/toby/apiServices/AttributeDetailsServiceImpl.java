/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.core.GenericDAO;
import com.toby.dto.AttributeDTO;
import com.toby.dto.AttributeDetailsDTO;
import com.toby.entity.Attribute;
import com.toby.entity.AttributeDetails;
import com.toby.entity.Branch;
import com.toby.entity.InvItem;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Lenovo
 */
@Stateless
public class AttributeDetailsServiceImpl implements AttributeDetailsService {

    @EJB
    private GenericDAO dao;

    @Override
    public AttributeDetailsDTO addAtrributeDetails(AttributeDetailsDTO attributeDetailsDTO) {
        AttributeDetails attributeDetails = mapToEntity(attributeDetailsDTO);
        attributeDetails = dao.updateEntity(attributeDetails);
        return mapToDTO(attributeDetails);
    }

    @Override
    public AttributeDetailsDTO updateAttributeDetails(AttributeDetailsDTO attributeDetailsDTO) {
        AttributeDetails attributeDetails = mapToEntity(attributeDetailsDTO);
        attributeDetails = dao.updateEntity(attributeDetails);
        return mapToDTO(attributeDetails);
    }

    @Override
    public AttributeDetailsDTO DeleteAttributeDetails(AttributeDetailsDTO attributeDetailsDTO) {
        AttributeDetails attributeDetails = mapToEntity(attributeDetailsDTO);
        dao.deleteEntity(attributeDetails);
        return mapToDTO(attributeDetails);
    }

    @Override
    public AttributeDetailsDTO findAttributeDetails(AttributeDetailsDTO attributeDetailsDTO) {
        AttributeDetails attributeDetails = dao.findEntityById(AttributeDetails.class, attributeDetailsDTO.getId());
        return mapToDTO(attributeDetails);
    }

    @Override
    public List<AttributeDetailsDTO> findAttributeDetailsForHeadByBranchId(AttributeDTO attributeDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", attributeDTO.getCompanyId());
        params.put("branchId", attributeDTO.getBranchId());
        params.put("headId", attributeDTO.getId());
        List<AttributeDetails> attributeDetailses = dao.findListByQuery("SELECT a FROM AttributeDetails a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId AND a.headId.id =:headId AND a.itemId = null order by a.orderBy ", params);
        if (attributeDetailses != null) {
            return mapToDTOList(attributeDetailses);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<AttributeDetailsDTO> getAllAttributesDetailsByCompanyId(AttributeDetailsDTO attributeDetailsDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", attributeDetailsDTO.getCompanyId());
        List<AttributeDetails> attributeDetailses = dao.findListByQuery("SELECT a FROM AttributeDetails a WHERE a.companyId.id =:companyId", params);
        return mapToDTOList(attributeDetailses);
    }

    @Override
    public List<AttributeDetailsDTO> getAllAttributesDetailsByCompanyIdAndBranchId(AttributeDetailsDTO attributeDetailsDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", attributeDetailsDTO.getCompanyId());
        params.put("branchId", attributeDetailsDTO.getBranchId());
        return dao.findListByQuery("SELECT a FROM AttributeDetails a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId", params);
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(a.id) FROM AttributeDetails a");
        return maxId;
    }

    public List<AttributeDetailsDTO> mapToDTOList(List<AttributeDetails> entityList) {
        List<AttributeDetailsDTO> dTOsList = new ArrayList<>();
        for (AttributeDetails entity : entityList) {
            AttributeDetailsDTO dTO = mapToDTO(entity);
            dTOsList.add(dTO);
        }
        return dTOsList;
    }

    public AttributeDetailsDTO mapToDTO(AttributeDetails entity) {
        AttributeDetailsDTO attributeDetailsDTO = new AttributeDetailsDTO();
        if (entity.getBranchId() != null) {
            attributeDetailsDTO.setBranchId(entity.getBranchId().getId());
        }
        if (entity.getCompanyId() != null) {
            attributeDetailsDTO.setCompanyId(entity.getCompanyId().getId());
        }
        if (entity.getHeadId() != null) {
            attributeDetailsDTO.setHeadId(entity.getHeadId().getId());
        }
        if (entity.getItemId() != null) {
            attributeDetailsDTO.setItemId(entity.getItemId().getId());
        }
        attributeDetailsDTO.setMsg(entity.getMsg());
        attributeDetailsDTO.setValue(entity.getValue());
        attributeDetailsDTO.setId(entity.getId());
        attributeDetailsDTO.setOrderBy(entity.getOrderBy());

        return attributeDetailsDTO;
    }

    public AttributeDetails mapToEntity(AttributeDetailsDTO dTO) {
        AttributeDetails attributeDetails = new AttributeDetails();
        attributeDetails.setId(dTO.getId());
        if (dTO.getBranchId() != null) {
            Branch branch = new Branch();
            branch.setId(dTO.getBranchId());
            attributeDetails.setBranchId(branch);
        }
        if (dTO.getCompanyId() != null) {
            TobyCompany company = new TobyCompany();
            company.setId(dTO.getCompanyId());
            attributeDetails.setCompanyId(company);
        }
        if (dTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            attributeDetails.setCreatedBy(user);
        }

        if (dTO.getHeadId() != null) {
            Attribute attribute = new Attribute();
            attribute.setId(dTO.getCreatedBy());
            attributeDetails.setHeadId(attribute);
        }

        if (dTO.getItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getCreatedBy());
            attributeDetails.setItemId(invItem);
        }

        attributeDetails.setCreationDate(dTO.getCreatedDate());
        attributeDetails.setMsg(dTO.getMsg());
        attributeDetails.setValue(dTO.getValue());
        attributeDetails.setOrderBy(dTO.getOrderBy());

        return attributeDetails;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.core.GenericDAO;
import com.toby.dto.AttributeDTO;
import com.toby.entity.Attribute;
import com.toby.entity.Branch;
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
public class AttributeServiceImpl implements AttributeService {

    @EJB
    private GenericDAO dao;
    
    @EJB
    AttributeDetailsService attributeDetailsService ;

    @Override
    public AttributeDTO addAtrribute(AttributeDTO attribute) {

        Attribute entity = mapToEntity(attribute);
        entity = dao.updateEntity(entity);
        return mapToDTO(entity , attribute);

    }

    @Override
    public AttributeDTO updateAttribute(AttributeDTO attribute) {

        Attribute entity = mapToEntity(attribute);
        entity = dao.updateEntity(entity);
        return mapToDTO(entity , attribute);
    }

    @Override
    public void deleteAttribute(AttributeDTO attribute) {
        Attribute entity = mapToEntity(attribute);
        dao.deleteEntity(entity);
    }

    @Override
    public AttributeDTO findAttribute(AttributeDTO dTO) {

        Attribute entity = dao.findEntityById(Attribute.class, dTO.getId());
        return mapToDTO(entity , dTO);
    }

    @Override
    public List<AttributeDTO> getAllAttributesByCompanyId(AttributeDTO attributeDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", attributeDTO.getCompanyId());
        List<Attribute> entityList = dao.findListByQuery("SELECT a FROM Attribute a WHERE a.companyId.id =:companyId", params);
        //return dao.findEntityListByCompanyId(Branch.class, companyId);
        return mapToDTOList(entityList , attributeDTO);
    }

    @Override
    public List<AttributeDTO> getAllAttributesByCompanyIdAndBranchId(AttributeDTO attributeDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", attributeDTO.getCompanyId());
        params.put("branchId", attributeDTO.getBranchId());
        List<Attribute> entityList = dao.findListByQuery("SELECT a FROM Attribute a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId", params);
        List<AttributeDTO> list = mapToDTOList(entityList , attributeDTO);
        return list;
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(a.id) FROM Attribute a");
        return maxId;
    }

    public List<AttributeDTO> mapToDTOList(List<Attribute> entityList , AttributeDTO attributeDTO) {
        List<AttributeDTO> dTOsList = new ArrayList<>();
        for (Attribute entity : entityList) {
            AttributeDTO dTO = mapToDTO(entity , attributeDTO );
            dTOsList.add(dTO);
        }
        return dTOsList;
    }

    public AttributeDTO mapToDTO(Attribute entity , AttributeDTO dTO) {
        AttributeDTO attributeDTO = new AttributeDTO();
        if (entity.getBranchId() != null) {
            attributeDTO.setBranchId(entity.getBranchId().getId());
        }
        if (entity.getCompanyId() != null) {
            attributeDTO.setCompanyId(entity.getCompanyId().getId());
        }
         if (entity.getCreatedBy() != null) {
            attributeDTO.setCreatedBy(entity.getCreatedBy().getId());
        }
        attributeDTO.setMsg(entity.getMsg());
        attributeDTO.setName(entity.getName());
        attributeDTO.setParent(entity.getParent());
        attributeDTO.setId(entity.getId());
        if(dTO.getItemId()!= null){
             attributeDTO.setItemId(dTO.getItemId());
        }else {
        attributeDTO.setItemId(null);
        }
        attributeDTO.setTerms(attributeDetailsService.findAttributeDetailsForHeadByBranchId(attributeDTO));
        return attributeDTO;
    }

    public Attribute mapToEntity(AttributeDTO dTO) {
        Attribute attribute = new Attribute();
        attribute.setId(dTO.getId());
        if (dTO.getBranchId() != null) {
            Branch branch = new Branch();
            branch.setId(dTO.getBranchId());
            attribute.setBranchId(branch);
        }
        if (dTO.getCompanyId() != null) {
            TobyCompany company = new TobyCompany();
            company.setId(dTO.getCompanyId());
            attribute.setCompanyId(company);
        }
        if (dTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            attribute.setCreatedBy(user);
        }
        attribute.setCreationDate(dTO.getCreatedDate());
        attribute.setMsg(dTO.getMsg());
        attribute.setName(dTO.getName());
        attribute.setParent(dTO.getParent());

        return attribute;
    }

}

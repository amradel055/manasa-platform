/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.core.GenericDAO;
import com.toby.dto.VariationDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvItem;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.entity.Variation;
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
public class VariationProductServiceImpl implements VariationProductService {

    @EJB
    private GenericDAO dao;

    @Override
    public VariationDTO addVariation(VariationDTO variationDTO) {
        Variation variation = mapToEntity(variationDTO);
        variation = dao.updateEntity(variation);
        return mapToDTO(variation);
    }

    @Override
    public VariationDTO updateVariation(VariationDTO variationDTO) {
        Variation variation = mapToEntity(variationDTO);
        variation = dao.updateEntity(variation);
        return mapToDTO(variation);
    }

    @Override
    public VariationDTO deleteVariation(VariationDTO variationDTO) {
        Variation variation = mapToEntity(variationDTO);
        dao.deleteEntity(variation);
        return mapToDTO(variation);
    }

    @Override
    public VariationDTO findVariation(VariationDTO variationDTO) {
        Variation variation = dao.findEntityById(Variation.class, variationDTO.getId());
        return mapToDTO(variation);
    }

    @Override
    public List<VariationDTO> getAllVariationsByCompanyId(VariationDTO variationDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", variationDTO.getCompanyId());
        List<Variation> variations = dao.findListByQuery("SELECT a FROM Variation a WHERE a.companyId.id =:companyId", params);
        return mapToDTOList(variations);
    }

    @Override
    public List<VariationDTO> getAllVariationByCompanyIdAndBranchId(VariationDTO variationDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", variationDTO.getCompanyId());
        params.put("branchId", variationDTO.getBranchId());
        List<Variation> variations = dao.findListByQuery("SELECT a FROM Variation a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId", params);
        return mapToDTOList(variations);
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(a.id) FROM Variation a");
        return maxId;
    }

    public List<VariationDTO> mapToDTOList(List<Variation> entityList) {
        List<VariationDTO> dTOsList = new ArrayList<>();
        for (Variation entity : entityList) {
            VariationDTO dTO = mapToDTO(entity);
            dTOsList.add(dTO);
        }
        return dTOsList;
    }

    public VariationDTO mapToDTO(Variation entity) {
        VariationDTO variationDTO = new VariationDTO();
        if (entity.getBranchId() != null) {
            variationDTO.setBranchId(entity.getBranchId().getId());
        }
        if (entity.getCompanyId() != null) {
            variationDTO.setCompanyId(entity.getCompanyId().getId());
        }

        if (entity.getCreatedBy() != null) {
            variationDTO.setCreatedBy(entity.getCreatedBy().getId());
        }

        if (entity.getItemId() != null) {
            variationDTO.setItemId(entity.getItemId().getId());
        }

        variationDTO.setMsg(entity.getMsg());
        variationDTO.setCreatedDate(entity.getCreationDate());
        variationDTO.setId(entity.getId());
        variationDTO.setDescribtion(entity.getDescribtion());
        variationDTO.setDefaultVariation(entity.getDefaultVariation());
        variationDTO.setSalePrice(entity.getSalePrice());
        variationDTO.setPrice(entity.getPrice());
        variationDTO.setImage(entity.getImage());

        return variationDTO;
    }

    public Variation mapToEntity(VariationDTO dTO) {
        Variation variation = new Variation();
        variation.setId(dTO.getId());
        if (dTO.getBranchId() != null) {
            Branch branch = new Branch();
            branch.setId(dTO.getBranchId());
            variation.setBranchId(branch);
        }
        if (dTO.getCompanyId() != null) {
            TobyCompany company = new TobyCompany();
            company.setId(dTO.getCompanyId());
            variation.setCompanyId(company);
        }
        if (dTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            variation.setCreatedBy(user);
        }

        if (dTO.getItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemId());
            variation.setItemId(invItem);
        }

        variation.setMsg(dTO.getMsg());
        variation.setCreationDate(dTO.getCreatedDate());
        variation.setImage(dTO.getImage());
        variation.setId(dTO.getId());
        variation.setDescribtion(dTO.getDescribtion());
        variation.setDefaultVariation(dTO.getDefaultVariation());
        variation.setSalePrice(dTO.getSalePrice());
        variation.setPrice(dTO.getPrice());

        return variation;
    }
}

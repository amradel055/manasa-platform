/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.core.GenericDAO;
import com.toby.dto.VariationDTO;
import com.toby.dto.VariationDetailsDTO;
import com.toby.entity.AttributeDetails;
import com.toby.entity.Branch;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.entity.VariationProduct;
import com.toby.entity.VariationDetails;
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
public class VariationProductDatailsServiceImpl implements VariationProductDetailsService {

    @EJB
    private GenericDAO dao;

    @Override
    public VariationDetailsDTO addVariationDetails(VariationDetailsDTO variationDetailsDTO) {
        VariationDetails variationDetails = mapToEntity(variationDetailsDTO);
        variationDetails = dao.updateEntity(variationDetails);
        return mapToDTO(variationDetails);
    }

    @Override
    public VariationDetailsDTO updateVariationDetails(VariationDetailsDTO variationDetailsDTO) {
        VariationDetails variationDetails = mapToEntity(variationDetailsDTO);
        variationDetails = dao.updateEntity(variationDetails);
        return mapToDTO(variationDetails);
    }

    @Override
    public VariationDetailsDTO deleteVariationDetails(VariationDetailsDTO variationDetailsDTO) {
        VariationDetails variationDetails = mapToEntity(variationDetailsDTO);
        dao.deleteEntity(variationDetails);
        return mapToDTO(variationDetails);
    }

    @Override
    public void deleteVariationDetailsByVariationId(VariationDTO variationDTO) {
        if (variationDTO.getId() != null && variationDTO.getBranchId() != null && variationDTO.getCompanyId() != null) {
            dao.executeDeleteQuery("DELETE FROM VariationDetails a WHERE a.companyId.id = " + variationDTO.getCompanyId() + " AND a.branchId.id =" + variationDTO.getBranchId() + " AND a.variationId.id = " + variationDTO.getId() + " ");
        }
    }

    @Override
    public VariationDetailsDTO findVariationDetails(VariationDetailsDTO variationDetailsDTO) {
        VariationDetails variationDetails = dao.findEntityById(VariationDetails.class, variationDetailsDTO.getId());
        return mapToDTO(variationDetails);
    }

    @Override
    public List<VariationDetailsDTO> getAllVariationsDetailsByCompanyId(VariationDetailsDTO variationDetailsDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", variationDetailsDTO.getCompanyId());
        List<VariationDetails> detailses = dao.findListByQuery("SELECT a FROM VariationDetails a WHERE a.companyId.id =:companyId", params);
        return mapToDTOList(detailses);
    }

    @Override
    public List<VariationDetailsDTO> getAllVariationDetailsByCompanyIdAndBranchId(VariationDetailsDTO variationDetailsDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", variationDetailsDTO.getCompanyId());
        params.put("branchId", variationDetailsDTO.getBranchId());
        List<VariationDetails> detailses = dao.findListByQuery("SELECT a FROM VariationDetails a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId", params);
        return mapToDTOList(detailses);
    }

    @Override
    public List<VariationDetailsDTO> getAllVariationDetailsByCompanyIdAndBranchIdAndVariationID(VariationDTO variationDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", variationDTO.getCompanyId());
        params.put("branchId", variationDTO.getBranchId());
        params.put("varId", variationDTO.getId());
        List<VariationDetails> detailses;
        detailses = dao.findListByQuery("SELECT a FROM VariationDetails a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId AND a.variationId.id =:varId ", params);
        if (detailses != null) {
            return mapToDTOList(detailses);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(a.id) FROM VariationDetails a");
        return maxId;
    }

    public List<VariationDetailsDTO> mapToDTOList(List<VariationDetails> entityList) {
        List<VariationDetailsDTO> dTOsList = new ArrayList<>();
        for (VariationDetails entity : entityList) {
            VariationDetailsDTO dTO = mapToDTO(entity);
            dTOsList.add(dTO);
        }
        return dTOsList;
    }

    public VariationDetailsDTO mapToDTO(VariationDetails entity) {
        VariationDetailsDTO variationDetailsDTO = new VariationDetailsDTO();
        if (entity.getBranchId() != null) {
            variationDetailsDTO.setBranchId(entity.getBranchId().getId());
        }
        if (entity.getCompanyId() != null) {
            variationDetailsDTO.setCompanyId(entity.getCompanyId().getId());
        }

        if (entity.getCreatedBy() != null) {
            variationDetailsDTO.setCreatedBy(entity.getCreatedBy().getId());
        }

        if (entity.getAttributeDetailsId() != null) {
            variationDetailsDTO.setAttributeDetailsId(entity.getAttributeDetailsId().getId());
        }

        if (entity.getVariationId() != null) {
            variationDetailsDTO.setVariationId(entity.getVariationId().getId());
        }

        variationDetailsDTO.setMsg(entity.getMsg());
        variationDetailsDTO.setCreatedDate(entity.getCreationDate());

        return variationDetailsDTO;
    }

    public VariationDetails mapToEntity(VariationDetailsDTO dTO) {
        VariationDetails variationDetails = new VariationDetails();
        variationDetails.setId(dTO.getId());
        if (dTO.getBranchId() != null) {
            Branch branch = new Branch();
            branch.setId(dTO.getBranchId());
            variationDetails.setBranchId(branch);
        }
        if (dTO.getCompanyId() != null) {
            TobyCompany company = new TobyCompany();
            company.setId(dTO.getCompanyId());
            variationDetails.setCompanyId(company);
        }
        if (dTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            variationDetails.setCreatedBy(user);
        }

        if (dTO.getVariationId() != null) {
            VariationProduct variation = new VariationProduct();
            variation.setId(dTO.getVariationId());
            variationDetails.setVariationId(variation);
        }

        if (dTO.getAttributeDetailsId() != null) {
            AttributeDetails attributeDetails = new AttributeDetails();
            attributeDetails.setId(dTO.getAttributeDetailsId());
            variationDetails.setAttributeDetailsId(attributeDetails);
        }

        variationDetails.setMsg(dTO.getMsg());
        variationDetails.setCreationDate(dTO.getCreatedDate());

        return variationDetails;
    }
}

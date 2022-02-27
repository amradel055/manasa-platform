/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.core.GenericDAO;
import com.toby.dto.OffersDetailsDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvItem;
import com.toby.entity.Offers;
import com.toby.entity.OffersDetails;
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
public class OffersDetailsServiceImpl implements OffersDatailsService {

    @EJB
    private GenericDAO dao;

    @Override
    public OffersDetailsDTO addOffersDetails(OffersDetailsDTO offersDetailsDTO) {
        OffersDetails details = mapToEntity(offersDetailsDTO);
        details = dao.updateEntity(details);
        return mapToDTO(details);
    }

    @Override
    public OffersDetailsDTO updateOffersDetails(OffersDetailsDTO offersDetailsDTO) {
        OffersDetails details = mapToEntity(offersDetailsDTO);
        details = dao.updateEntity(details);
        return mapToDTO(details);
    }

    @Override
    public OffersDetailsDTO deleteOffersDetails(OffersDetailsDTO offersDetailsDTO) {
        OffersDetails details = mapToEntity(offersDetailsDTO);
        dao.deleteEntity(details);
        return mapToDTO(details);
    }

    @Override
    public OffersDetailsDTO findOffersDetails(OffersDetailsDTO offersDetailsDTO) {
        OffersDetails details = dao.findEntityById(OffersDetails.class, offersDetailsDTO.getId());
        return mapToDTO(details);
    }

    @Override
    public List<OffersDetailsDTO> getAllOffersDetailsByCompanyId(OffersDetailsDTO offersDetailsDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", offersDetailsDTO.getCompanyId());
        List<OffersDetails> offersDetailses = dao.findListByQuery("SELECT a FROM OffersDetails a WHERE a.companyId.id =:companyId", params);
        return mapToDTOList(offersDetailses);
    }

    @Override

    public List<OffersDetailsDTO> getAllOffersDetailsByCompanyIdAndBranchId(OffersDetailsDTO offersDetailsDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", offersDetailsDTO.getCompanyId());
        params.put("branchId", offersDetailsDTO.getBranchId());
        List<OffersDetails> offersDetailses = dao.findListByQuery("SELECT a FROM OffersDetails a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId", params);
        return mapToDTOList(offersDetailses);
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(a.id) FROM OffersDetails a");
        return maxId;
    }

    public List<OffersDetailsDTO> mapToDTOList(List<OffersDetails> entityList) {
        List<OffersDetailsDTO> dTOsList = new ArrayList<>();
        for (OffersDetails entity : entityList) {
            OffersDetailsDTO dTO = mapToDTO(entity);
            dTOsList.add(dTO);
        }
        return dTOsList;
    }

    public OffersDetailsDTO mapToDTO(OffersDetails entity) {
        OffersDetailsDTO offersDetailsDTO = new OffersDetailsDTO();
        if (entity.getBranchId() != null) {
            offersDetailsDTO.setBranchId(entity.getBranchId().getId());
        }
        if (entity.getCompanyId() != null) {
            offersDetailsDTO.setCompanyId(entity.getCompanyId().getId());
        }

        if (entity.getCreatedBy() != null) {
            offersDetailsDTO.setCreatedBy(entity.getCreatedBy().getId());
        }
        if (entity.getCategoryId() != null) {
            offersDetailsDTO.setCategoryId(entity.getCategoryId());
        }
        if (entity.getItemId() != null) {
            offersDetailsDTO.setItemId(entity.getItemId().getId());
        }
        if (entity.getParentOfferId() != null) {
            offersDetailsDTO.setParentOfferId(entity.getParentOfferId().getId());
        }
        offersDetailsDTO.setMsg(entity.getMsg());
        offersDetailsDTO.setCreatedDate(entity.getCreationDate());
        offersDetailsDTO.setId(entity.getId());
        offersDetailsDTO.setStatus(entity.getStatus());
        return offersDetailsDTO;
    }

    public OffersDetails mapToEntity(OffersDetailsDTO dTO) {
        OffersDetails offersDetails = new OffersDetails();
        offersDetails.setId(dTO.getId());
        if (dTO.getBranchId() != null) {
            Branch branch = new Branch();
            branch.setId(dTO.getBranchId());
            offersDetails.setBranchId(branch);
        }
        if (dTO.getCompanyId() != null) {
            TobyCompany company = new TobyCompany();
            company.setId(dTO.getCompanyId());
            offersDetails.setCompanyId(company);
        }
        if (dTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            offersDetails.setCreatedBy(user);
        }

        if (dTO.getItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemId());
            offersDetails.setItemId(invItem);
        }
        if (dTO.getParentOfferId() != null) {
            Offers offers = new Offers();
            offers.setId(dTO.getItemId());
            offersDetails.setParentOfferId(offers);
        }

        offersDetails.setMsg(dTO.getMsg());
        offersDetails.setCreationDate(dTO.getCreatedDate());
        offersDetails.setStatus(dTO.getStatus());
        offersDetails.setId(dTO.getId());
        offersDetails.setCategoryId(dTO.getCategoryId());

        return offersDetails;
    }
}

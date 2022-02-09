/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.core.GenericDAO;
import com.toby.dto.OffersDTO;
import com.toby.entity.Branch;
import com.toby.entity.Offers;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;

/**
 *
 * @author Lenovo
 */
public class OffersServiceImpl implements OffersService {

    @EJB
    private GenericDAO dao;

    @Override
    public OffersDTO addOffers(OffersDTO offersDTO) {
        Offers offers = mapToEntity(offersDTO);
        offers = dao.updateEntity(offers);
        return mapToDTO(offers);
    }

    @Override
    public OffersDTO updateOffers(OffersDTO offersDTO) {
        Offers offers = mapToEntity(offersDTO);
        offers = dao.updateEntity(offers);
        return mapToDTO(offers);
    }
    
    @Override
    public OffersDTO deleteOffers(OffersDTO offersDTO) {
        Offers offers = mapToEntity(offersDTO);
         dao.deleteEntity(offers);
        return mapToDTO(offers);
    }

    @Override
    public OffersDTO findOffers(OffersDTO offersDTO) {
        Offers offers = dao.findEntityById(Offers.class, offersDTO.getId());
        return mapToDTO(offers);
    }

    @Override
    public List<OffersDTO> getAllOffersByCompanyId(OffersDTO offersDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", offersDTO.getCompanyId());
        List<Offers> offerses = dao.findListByQuery("SELECT a FROM Offers a WHERE a.companyId.id =:companyId", params);
        return mapToDTOList(offerses);
    }

    @Override
    public List<OffersDTO> getAllOffersByCompanyIdAndBranchId(OffersDTO offersDTO) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", offersDTO.getCompanyId());
        params.put("branchId", offersDTO.getBranchId());
        List<Offers> offerses = dao.findListByQuery("SELECT a FROM Offers a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId", params);
        return mapToDTOList(offerses);
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(a.id) FROM Offers a");
        return maxId;
    }

    public List<OffersDTO> mapToDTOList(List<Offers> entityList) {
        List<OffersDTO> dTOsList = new ArrayList<>();
        for (Offers entity : entityList) {
            OffersDTO dTO = mapToDTO(entity);
            dTOsList.add(dTO);
        }
        return dTOsList;
    }

    public OffersDTO mapToDTO(Offers entity) {
        OffersDTO offersDTO = new OffersDTO();
        if (entity.getBranchId() != null) {
            offersDTO.setBranchId(entity.getBranchId().getId());
        }
        if (entity.getCompanyId() != null) {
            offersDTO.setCompanyId(entity.getCompanyId().getId());
        }

        if (entity.getCreatedBy() != null) {
            offersDTO.setCreatedBy(entity.getCreatedBy().getId());
        }
        offersDTO.setMsg(entity.getMsg());
        offersDTO.setCreatedDate(entity.getCreationDate());
        offersDTO.setName(entity.getName());
        offersDTO.setLimit(entity.getLimit());
        offersDTO.setMin(entity.getMin());
        offersDTO.setMax(entity.getMax());
        offersDTO.setId(entity.getId());
        offersDTO.setDateFrom(entity.getDateFrom());
        offersDTO.setDateTo(entity.getDateTo());
        offersDTO.setDescribtion(entity.getDescribtion());
        offersDTO.setType(entity.getType());
        offersDTO.setValue(entity.getValue());
        return offersDTO;
    }

    public Offers mapToEntity(OffersDTO dTO) {
        Offers offers = new Offers();
        offers.setId(dTO.getId());
        if (dTO.getBranchId() != null) {
            Branch branch = new Branch();
            branch.setId(dTO.getBranchId());
            offers.setBranchId(branch);
        }
        if (dTO.getCompanyId() != null) {
            TobyCompany company = new TobyCompany();
            company.setId(dTO.getCompanyId());
            offers.setCompanyId(company);
        }
        if (dTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            offers.setCreatedBy(user);
        }

        offers.setMsg(dTO.getMsg());
        offers.setCreationDate(dTO.getCreatedDate());
        offers.setName(dTO.getName());
        offers.setLimit(dTO.getLimit());
        offers.setMin(dTO.getMin());
        offers.setMax(dTO.getMax());
        offers.setId(dTO.getId());
        offers.setDateFrom(dTO.getDateFrom());
        offers.setDateTo(dTO.getDateTo());
        offers.setDescribtion(dTO.getDescribtion());
        offers.setType(dTO.getType());
        offers.setValue(dTO.getValue());

        return offers;
    }

}

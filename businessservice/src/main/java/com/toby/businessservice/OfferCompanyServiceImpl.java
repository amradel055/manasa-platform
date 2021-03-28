/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.OfferCompanyDTO;
import com.toby.entity.Branch;
import com.toby.entity.OfferCompany;
import com.toby.entity.TobyCompany;
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
 * @author hq004
 */
@Stateless
public class OfferCompanyServiceImpl implements OfferCompanyService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteOfferCompany(OfferCompanyDTO offerCompanyDTO) {
        if (offerCompanyDTO.getId() != null) {
            OfferCompany offerCompany = new OfferCompany();
            offerCompany.setId(offerCompanyDTO.getId());
            dao.deleteEntity(offerCompany);
        }
    }

    @Override
    public List<OfferCompanyDTO> getAllOfferCompany(TobyUser tobyUser) {
        List<OfferCompany> offerCompanyList = dao.findAll(OfferCompany.class);
        return mapToDTOList(offerCompanyList, tobyUser);
    }

    @Override
    public OfferCompanyDTO addOfferCompany(OfferCompanyDTO offerCompanyDTO, TobyUser tobyUser) {
        OfferCompany offerCompany = mapToEntity(offerCompanyDTO, tobyUser);
        dao.updateEntity(offerCompany);
        offerCompanyDTO = mapToDTO(offerCompany, tobyUser);
        return offerCompanyDTO;
    }

    @Override
    public OfferCompanyDTO findOfferCompany(Integer offerCompanyId, TobyUser tobyUser) {
        OfferCompany offerCompany = dao.findEntityById(OfferCompany.class, offerCompanyId);
        return mapToDTO(offerCompany, tobyUser);
    }

    @Override
    public List<OfferCompanyDTO> getALLGlofferCompanyByCompanyId(Integer companyId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<OfferCompany> offerCompanyList = dao.findListByQuery("SELECT e FROM OfferCompany e WHERE e.companyId.id =:companyId", params);
        return mapToDTOList(offerCompanyList, tobyUser);
    }

    @Override
    public List<OfferCompanyDTO> getALLGlofferCompanyByBranchId(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<OfferCompany> offerCompanyList = dao.findListByQuery("SELECT e FROM OfferCompany e WHERE e.branchId.id =:branchId", params);
        return mapToDTOList(offerCompanyList, tobyUser);
    }

    public OfferCompany mapToEntity(OfferCompanyDTO offerCompanyDTO, TobyUser tobyUser) {
        OfferCompany offerCompany1 = new OfferCompany();
        offerCompany1.setId(offerCompanyDTO.getId());
        offerCompany1.setCode(offerCompanyDTO.getCode());
        offerCompany1.setName(offerCompanyDTO.getName());

        if (offerCompanyDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(offerCompanyDTO.getCreatedBy());
            offerCompany1.setCreatedBy(user);
            offerCompany1.setCreationDate(offerCompanyDTO.getCreatedDate());
            offerCompany1.setModifiedBy(tobyUser);
            offerCompany1.setModificationDate(new Date());
            if (offerCompanyDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(offerCompanyDTO.getCompanyId());
                offerCompany1.setCompanyId(company);
            }
            if (offerCompanyDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(offerCompanyDTO.getBranchId());
                offerCompany1.setBranchId(branch);
            }
        } else {
            offerCompany1.setCreatedBy(tobyUser);
            offerCompany1.setCreationDate(new Date());
            offerCompany1.setCompanyId(tobyUser.getCompanyId());
            offerCompany1.setBranchId(tobyUser.getBranchId());

        }
        return offerCompany1;

    }

    public OfferCompanyDTO mapToDTO(OfferCompany offerCompany, TobyUser tobyUser) {

        OfferCompanyDTO offerCompanyDTO = new OfferCompanyDTO();

        offerCompanyDTO.setId(offerCompany.getId());
        offerCompanyDTO.setCreatedDate(offerCompany.getCreationDate());
        offerCompanyDTO.setCode(offerCompany.getCode());
        offerCompanyDTO.setIndex(offerCompany.getId());

        offerCompanyDTO.setName(offerCompany.getName());
        offerCompanyDTO.setCreatedBy(offerCompany.getCreatedBy() != null ? offerCompany.getCreatedBy().getId() : null);
        offerCompanyDTO.setCreatedDate(offerCompany.getCreationDate());
        offerCompanyDTO.setBranchId(offerCompany.getBranchId().getId() != null ? offerCompany.getBranchId().getId() : null);
        offerCompanyDTO.setCompanyId(offerCompany.getCompanyId() != null ? offerCompany.getCompanyId().getId() : null);

        return offerCompanyDTO;
    }

    public List<OfferCompanyDTO> mapToDTOList(List<OfferCompany> offerCompanyList, TobyUser tobyUser) {
        List<OfferCompanyDTO> offerCompanyDTOList = new ArrayList<>();
        for (OfferCompany offerCompany : offerCompanyList) {
            offerCompanyDTOList.add(mapToDTO(offerCompany, tobyUser));
        }
        return offerCompanyDTOList;
    }

}

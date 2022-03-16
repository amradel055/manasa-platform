/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.DiscountCodesDTO;
import com.toby.entity.Branch;
import com.toby.entity.DiscountCodes;
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
public class DiscountCodesServiceImpl implements DiscountCodesService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteDiscountCodes(DiscountCodesDTO discountCodesDTO) {
        if (discountCodesDTO.getId() != null) {
            DiscountCodes discountCodes = new DiscountCodes();
            discountCodes.setId(discountCodesDTO.getId());
            dao.deleteEntity(discountCodes);
        }
    }

    @Override
    public List<DiscountCodesDTO> getAllDiscountCodes(TobyUser tobyUser) {
        List<DiscountCodes> discountCodesList = dao.findAll(DiscountCodes.class);
        return mapToDTOList(discountCodesList, tobyUser);
    }

    @Override
    public DiscountCodesDTO addDiscountCodes(DiscountCodesDTO discountCodesDTO, TobyUser tobyUser) {
        DiscountCodes discountCodes = mapToEntity(discountCodesDTO, tobyUser);
        dao.updateEntity(discountCodes);
        discountCodesDTO = mapToDTO(discountCodes, tobyUser);
        return discountCodesDTO;
    }

    @Override
    public DiscountCodesDTO findDiscountCodes(Integer discountCodesId, TobyUser tobyUser) {
        DiscountCodes discountCodes = dao.findEntityById(DiscountCodes.class, discountCodesId);
        return mapToDTO(discountCodes, tobyUser);
    }

    @Override
    public List<DiscountCodesDTO> getDiscountCodesByCompanyId(Integer companyId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<DiscountCodes> discountCodesList = dao.findListByQuery("SELECT e FROM DiscountCodes e WHERE e.companyId.id =:companyId", params);
        return mapToDTOList(discountCodesList, tobyUser);
    }

    @Override
    public List<DiscountCodesDTO> getDiscountCodesByBranchId(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<DiscountCodes> discountCodesList = dao.findListByQuery("SELECT e FROM DiscountCodes e WHERE e.branchId.id =:branchId", params);
        return mapToDTOList(discountCodesList, tobyUser);
    }

   

    public DiscountCodes mapToEntity(DiscountCodesDTO discountCodesDTO, TobyUser tobyUser) {
        DiscountCodes discountCodes1 = new DiscountCodes();
        discountCodes1.setId(discountCodesDTO.getId());
        discountCodes1.setCode(discountCodesDTO.getCode());
        discountCodes1.setBankName(discountCodesDTO.getBankName());
        discountCodes1.setDiscountValue(discountCodesDTO.getDiscountValue());
        

        if (discountCodesDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(discountCodesDTO.getCreatedBy());
            discountCodes1.setCreatedBy(user);
            discountCodes1.setCreationDate(discountCodesDTO.getCreatedDate());
            discountCodes1.setModifiedBy(tobyUser);
            discountCodes1.setModificationDate(new Date());
            if (discountCodesDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(discountCodesDTO.getCompanyId());
                discountCodes1.setCompanyId(company);
            }
            if (discountCodesDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(discountCodesDTO.getBranchId());
                discountCodes1.setBranchId(branch);
            }
        } else {
            discountCodes1.setCreatedBy(tobyUser);
            discountCodes1.setCreationDate(new Date());
            discountCodes1.setCompanyId(tobyUser.getCompanyId());
            discountCodes1.setBranchId(tobyUser.getBranchId());

        }
        return discountCodes1;

    }

    public DiscountCodesDTO mapToDTO(DiscountCodes discountCodes, TobyUser tobyUser) {

        DiscountCodesDTO discountCodesDTO = new DiscountCodesDTO();

        discountCodesDTO.setId(discountCodes.getId());
        discountCodesDTO.setCreatedDate(discountCodes.getCreationDate());
        discountCodesDTO.setCode(discountCodes.getCode());
        discountCodesDTO.setBankName(discountCodes.getBankName());
        discountCodesDTO.setDiscountValue(discountCodes.getDiscountValue());
        

       
        discountCodesDTO.setCreatedBy(discountCodes.getCreatedBy() != null ? discountCodes.getCreatedBy().getId() : null);
        discountCodesDTO.setCreatedDate(discountCodes.getCreationDate());
        discountCodesDTO.setBranchId(discountCodes.getBranchId().getId() != null ? discountCodes.getBranchId().getId() : null);
        discountCodesDTO.setCompanyId(discountCodes.getCompanyId() != null ? discountCodes.getCompanyId().getId() : null);

        return discountCodesDTO;
    }

    public List<DiscountCodesDTO> mapToDTOList(List<DiscountCodes> discountCodesList, TobyUser tobyUser) {
        List<DiscountCodesDTO> discountCodesDTOList = new ArrayList<>();
        for (DiscountCodes discountCodes : discountCodesList) {
            discountCodesDTOList.add(mapToDTO(discountCodes, tobyUser));
        }
        return discountCodesDTOList;
    }

}

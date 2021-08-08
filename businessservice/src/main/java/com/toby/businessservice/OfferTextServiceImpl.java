/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.OfferTextDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.OfferText;
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
public class OfferTextServiceImpl implements OfferTextService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteOfferText(OfferTextDTO offerTextDTO) {
        if (offerTextDTO.getId() != null) {
            OfferText offerText = new OfferText();
            offerText.setId(offerTextDTO.getId());
            dao.deleteEntity(offerText);
        }
    }

    @Override
    public List<OfferTextDTO> getAllOfferText(TobyUser tobyUser) {
        List<OfferText> offerTextList = dao.findAll(OfferText.class);
        return mapToDTOList(offerTextList, tobyUser);
    }

    @Override
    public OfferTextDTO addOfferText(OfferTextDTO offerTextDTO, TobyUser tobyUser) {
        OfferText offerText = mapToEntity(offerTextDTO, tobyUser);
        dao.updateEntity(offerText);
        offerTextDTO = mapToDTO(offerText, tobyUser);
        return offerTextDTO;
    }

    @Override
    public OfferTextDTO findOfferText(Integer offerTextId, TobyUser tobyUser) {
        OfferText offerText = dao.findEntityById(OfferText.class, offerTextId);
        return mapToDTO(offerText, tobyUser);
    }

    @Override
    public List<OfferTextDTO> getALLOfferTextByCompanyId(Integer companyId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<OfferText> offerTextList = dao.findListByQuery("SELECT e FROM OfferText e WHERE e.companyId.id =:companyId", params);
        return mapToDTOList(offerTextList, tobyUser);
    }

    @Override
    public List<OfferTextDTO> getALLOfferTextByBranchId(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<OfferText> offerTextList = dao.findListByQuery("SELECT e FROM OfferText e WHERE e.branchId.id =:branchId", params);
        return mapToDTOList(offerTextList, tobyUser);
    }

    @Override
    public String getNoticeByCustomerId(Integer id) {
        String offerTextNotice = "";
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT oc.notice FROM offer_company oc\n"
                + "join toby.inv_organization_site os\n"
                + "on os.offer_company_id = oc.id\n").
                append("where os.id =  \n ").
                append(id).append(" ");
        List<String> details = dao.executeNativeQuery(builder.toString());
        for (String customerNotice : details) {
            offerTextNotice = customerNotice;
        }
        return offerTextNotice;
    }

    public OfferText mapToEntity(OfferTextDTO offerTextDTO, TobyUser tobyUser) {
        OfferText offerText1 = new OfferText();
        offerText1.setId(offerTextDTO.getId());
        offerText1.setText(offerTextDTO.getText());
        offerText1.setDateFrom(offerTextDTO.getDateFrom());
        offerText1.setDateTo(offerTextDTO.getDateTo());

        if (offerTextDTO.getGalaryDTO() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(offerTextDTO.getGalaryDTO().getId());
            offerText1.setGalaryId(invInventory);
        }

        if (offerTextDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(offerTextDTO.getCreatedBy());
            offerText1.setCreatedBy(user);
            offerText1.setCreationDate(offerTextDTO.getCreatedDate());
            offerText1.setModifiedBy(tobyUser);
            offerText1.setModificationDate(new Date());
            if (offerTextDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(offerTextDTO.getCompanyId());
                offerText1.setCompanyId(company);
            }
            if (offerTextDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(offerTextDTO.getBranchId());
                offerText1.setBranchId(branch);
            }
        } else {
            offerText1.setCreatedBy(tobyUser);
            offerText1.setCreationDate(new Date());
            offerText1.setCompanyId(tobyUser.getCompanyId());
            offerText1.setBranchId(tobyUser.getBranchId());

        }
        return offerText1;

    }

    public OfferTextDTO mapToDTO(OfferText offerText, TobyUser tobyUser) {

        OfferTextDTO offerTextDTO = new OfferTextDTO();

        offerTextDTO.setId(offerText.getId());
        offerTextDTO.setText(offerText.getText());
        offerTextDTO.setDateFrom(offerText.getDateFrom());
        offerTextDTO.setDateTo(offerText.getDateTo());

        if (offerText.getGalaryId() != null) {
            InvInventoryDTO inventoryDTO = new InvInventoryDTO();
            inventoryDTO.setId(offerText.getGalaryId().getId());
            inventoryDTO.setName(offerText.getGalaryId().getName());
            inventoryDTO.setCode(offerText.getGalaryId().getCode());
            offerTextDTO.setGalaryDTO(inventoryDTO);
        }

        offerTextDTO.setCreatedBy(offerText.getCreatedBy() != null ? offerText.getCreatedBy().getId() : null);
        offerTextDTO.setCreatedDate(offerText.getCreationDate());
        offerTextDTO.setBranchId(offerText.getBranchId().getId() != null ? offerText.getBranchId().getId() : null);
        offerTextDTO.setCompanyId(offerText.getCompanyId() != null ? offerText.getCompanyId().getId() : null);

        return offerTextDTO;
    }

    public List<OfferTextDTO> mapToDTOList(List<OfferText> offerTextList, TobyUser tobyUser) {
        List<OfferTextDTO> offerTextDTOList = new ArrayList<>();
        for (OfferText offerText : offerTextList) {
            offerTextDTOList.add(mapToDTO(offerText, tobyUser));
        }
        return offerTextDTOList;
    }

}

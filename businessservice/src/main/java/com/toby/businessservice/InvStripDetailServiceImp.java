package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvStripDTO;
import com.toby.dto.InvStripDetailDTO;
import com.toby.dto.QuantityItemsStoreDTO;
import com.toby.entity.InvItem;
import com.toby.entity.InvStrip;
import com.toby.entity.InvStripDetail;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Stateless
public class InvStripDetailServiceImp implements InvStripDetailService {

    @EJB
    private GenericDAO dao;

    @Override
    public void addInvStripDetails(InvStripDetail details) {
        dao.saveEntity(details);
    }

    @Override
    public void deleteInvStripDetails(InvStripDetail details) {
        dao.deleteEntity(details);
    }

    @Override
    public InvStripDetail updateInvStripDetails(InvStripDetail details) {
        return dao.updateEntity(details);
    }

    @Override
    public List<InvStripDetail> getAllInvStripDetails() {
        return dao.findAll(InvStripDetail.class);
    }

    @Override
    public List<InvStripDetail> getAllInvStripDetails(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("invStrip", id);
        return dao.findListByQuery("SELECT d FROM InvStripDetail d WHERE d.invStripId.id =:invStrip", params);
    }

    public InvStripDetailDTO mapToDTO(InvStripDetail invStripDetail) {

        InvStripDetailDTO invStripDetailDTO = new InvStripDetailDTO();

        invStripDetailDTO.setId(invStripDetail.getId());
        invStripDetailDTO.setActualAmount(invStripDetail.getActualAmount());
        invStripDetailDTO.setBookBalance(invStripDetail.getBookBalance());
        invStripDetailDTO.setDifference(invStripDetail.getDifference());
        invStripDetailDTO.setIndex(invStripDetail.getId());
        invStripDetailDTO.setSerial(invStripDetail.getSerial());
        if (invStripDetail.getInvStripId() != null) {
            InvStripDTO invStripDTO = new InvStripDTO();
            invStripDTO.setId(invStripDetail.getInvStripId().getId());
            invStripDetailDTO.setInvStripId(invStripDTO);
        }

        if (invStripDetail.getInvItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(invStripDetail.getInvItemId().getId());
            invItemDTO.setName(invStripDetail.getInvItemId().getName());
            invItemDTO.setCode(invStripDetail.getInvItemId().getCode());
            invStripDetailDTO.setInvItemId(invItemDTO);

        }

        invStripDetailDTO.setCreatedBy(invStripDetail.getCreatedBy() != null ? invStripDetail.getCreatedBy().getId() : null);
        invStripDetailDTO.setCreatedDate(invStripDetail.getCreationDate());

        invStripDetailDTO.setCompanyId(invStripDetail.getCompanyId() != null ? invStripDetail.getCompanyId().getId() : null);

        return invStripDetailDTO;
    }

    public InvStripDetail mapToEntity(InvStripDetailDTO invStripDetailDTO, TobyUser tobyUser) {

        InvStripDetail invStripDetail = new InvStripDetail();

        invStripDetail.setId(invStripDetailDTO.getId());
        invStripDetail.setActualAmount(invStripDetailDTO.getActualAmount());
        invStripDetail.setBookBalance(invStripDetailDTO.getBookBalance());
        invStripDetail.setDifference(invStripDetailDTO.getDifference());
        invStripDetail.setSerial(invStripDetailDTO.getSerial());
        if (invStripDetailDTO.getInvStripId() != null) {
            InvStrip invStrip = new InvStrip();
            invStrip.setId(invStripDetailDTO.getInvStripId().getId());
            invStripDetail.setInvStripId(invStrip);
        }

        if (invStripDetailDTO.getInvItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(invStripDetailDTO.getInvItemId().getId());
            invItem.setName(invStripDetailDTO.getInvItemId().getName());
            invItem.setCode(invStripDetailDTO.getInvItemId().getCode());
            invStripDetail.setInvItemId(invItem);
        }

        if (invStripDetailDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invStripDetailDTO.getCreatedBy());
            invStripDetail.setCreatedBy(user);
            invStripDetail.setCreationDate(invStripDetailDTO.getCreatedDate());
            invStripDetail.setModifiedBy(tobyUser);
            invStripDetail.setModificationDate(new Date());
            if (invStripDetailDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invStripDetailDTO.getCompanyId());
                invStripDetail.setCompanyId(company);
            }

        } else {
            invStripDetail.setCreatedBy(tobyUser);
            invStripDetail.setCreationDate(new Date());
            invStripDetail.setCompanyId(tobyUser.getCompanyId());

        }
        return invStripDetail;
    }

    public List<InvStripDetailDTO> mapToDTOList(List<InvStripDetail> invStripDetailList, TobyUser tobyUser) {
        List<InvStripDetailDTO> invStripDetailDTOList = new ArrayList<>();
        for (InvStripDetail invStripDetail : invStripDetailList) {
            invStripDetailDTOList.add(mapToDTO(invStripDetail));
        }
        return invStripDetailDTOList;
    }

    @Override
    public List<InvStripDetailDTO> mapToDTOList1(List<QuantityItemsStoreDTO> quantityItemsStoreList,  InvStripDTO invStrip, TobyUser tobyUser) {
        List<InvStripDetailDTO> invStripDetailDTOList = new ArrayList<>();
        for (QuantityItemsStoreDTO quantityItemsStoreDTO : quantityItemsStoreList) {
            
            InvStripDetailDTO detailDTO = mapToDTO1(quantityItemsStoreDTO, invStrip);
            invStripDetailDTOList.add(detailDTO);
        }
        return invStripDetailDTOList;
    }

    public InvStripDetailDTO mapToDTO1(QuantityItemsStoreDTO quantityItemsStoreDTO, InvStripDTO invStrip) {
        InvStripDetailDTO invStripDetailDTO = new InvStripDetailDTO();
        invStripDetailDTO.setActualAmount(BigDecimal.ZERO);
        invStripDetailDTO.setBookBalance(quantityItemsStoreDTO.getQty());
        invStripDetailDTO.setDifference(invStripDetailDTO.getActualAmount().subtract(invStripDetailDTO.getBookBalance()));

        InvItemDTO invItemDTO = new InvItemDTO();
        invItemDTO.setCode(quantityItemsStoreDTO.getItemCode());
        invItemDTO.setName(quantityItemsStoreDTO.getItemName());
        invItemDTO.setId(quantityItemsStoreDTO.getId());
        invStripDetailDTO.setInvItemId(invItemDTO);
        updateSummition(invStripDetailDTO, invStrip);
        return invStripDetailDTO;
    }

    @Override
    public InvStripDTO updateSummition(InvStripDetailDTO detailEntity, InvStripDTO invStrip) {
        invStrip.getTotalQuantity().add(detailEntity.getActualAmount() != null ? detailEntity.getActualAmount() : BigDecimal.ZERO);
        invStrip.getTotalBalance().add(detailEntity.getBookBalance() != null ? detailEntity.getBookBalance() : BigDecimal.ZERO);
        return invStrip;
    }

    @Override
    public List<InvStripDetailDTO> getAllInvStripDetailsDTO(Integer id, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap();
        params.put("invStrip", id);
        List<InvStripDetail> details = dao.findListByQuery("SELECT d FROM InvStripDetail d WHERE d.invStripId.id =:invStrip", params);
        return mapToDTOList(details, tobyUser);

    }

}

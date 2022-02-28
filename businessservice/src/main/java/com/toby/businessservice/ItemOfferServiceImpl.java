/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.ItemOffer;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class ItemOfferServiceImpl implements ItemOfferService {

    @EJB
    private GenericDAO dao;
    @EJB
    ItemOfferDetailService itemOfferDetailService;

    @Override
    public InvInvoiceDTO findItemOfferByItemId(Integer itemId) {
        ItemOffer itemOffer = dao.findEntityById(ItemOffer.class, itemId);
        InvInvoiceDTO itemOfferDTO = mapTODTO(itemOffer, null);
        return itemOfferDTO;
    }

    @Override
    public void deleteItemOffer(InvInvoiceDTO itemOfferDTO, TobyUser tobyUser) {
        ItemOffer entity = new ItemOffer();
        itemOfferDetailService.deleteItemOfferDetail(itemOfferDTO.getId());
        entity.setId(itemOfferDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public InvInvoiceDTO addItemOffer(InvInvoiceDTO itemOfferDTO, TobyUser tobyUser) {

        ItemOffer entity = mapToEntity(itemOfferDTO, tobyUser);

        entity = dao.updateEntity(entity);
        if (itemOfferDTO.getInvPurchaseInvoiceDetailsDeletedList() != null && !itemOfferDTO.getInvPurchaseInvoiceDetailsDeletedList().isEmpty()) {
            itemOfferDetailService.deleteItemOfferDetailList(itemOfferDTO.getInvPurchaseInvoiceDetailsDeletedList());
        }

        if (itemOfferDTO.getInvPurchaseInvoiceDetailList() != null && !itemOfferDTO.getInvPurchaseInvoiceDetailList().isEmpty()) {
            itemOfferDetailService.addItemOfferDetailList(itemOfferDTO.getInvPurchaseInvoiceDetailList(),entity.getId(), tobyUser);
        }
        itemOfferDTO = mapTODTO(entity, itemOfferDTO);
        return itemOfferDTO;
    }

    @Override
    public List<InvInvoiceDTO> findItemOfferByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        List<InvInvoiceDTO> itemOfferDTOList = new ArrayList<>();
        params.put("branchId", branchId);

        if (branchId != null) {
            List<ItemOffer> list = dao.findListByQuery("SELECT i FROM ItemOffer i WHERE i.branchId.id =:branchId order by i.id desc ", params);
            returnListDTO(list, itemOfferDTOList);
        }
        return itemOfferDTOList;
    }
    @Override
    public BigDecimal findCostOffer(Integer branchId,String itemCode,Date date) {
        List<BigDecimal> costAvarage = new ArrayList<>();
        if (branchId != null) {
            StringBuilder query = new StringBuilder();
            query.append("select cost_after from item_offer iof left join item_offer_detail iofd on iof.id = iofd.item_offer  where iof.branch_id = ").append(branchId);
            query.append(" and item_barcode = '").append(itemCode).append("' AND '"); 
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append(dateFormat.format(date)).append("' >= iof.date_from AND '").append(dateFormat.format(date)).append("' <= iof.date_to  limit 1");
            costAvarage = dao.executeNativeQuery(query.toString());
        }
        return (costAvarage == null || costAvarage.isEmpty()) ? null : costAvarage.get(0);
    }

    public InvInvoiceDTO mapTODTO(ItemOffer entity, InvInvoiceDTO dTO) {
        if (dTO == null) {
            dTO = new InvInvoiceDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(InvInvoiceDTO dTO, ItemOffer entity) {
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());
        dTO.setId(entity.getId());
        dTO.setDate(entity.getDateFrom());
        dTO.setDateEndOffer(entity.getDateTo());
        dTO.setInvPurchaseInvoiceDetailList(itemOfferDetailService.findItemOfferDetailByItemOfferId(entity.getBranchId().getId(), dTO.getId()));
        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public ItemOffer mapToEntity(InvInvoiceDTO dTO, TobyUser tobyUser) {
        ItemOffer entity = new ItemOffer();
        entity.setId(dTO.getId());
        entity.setDateFrom(dTO.getDate());
        entity.setDateTo(dTO.getDateEndOffer());
        if (dTO.getId() == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<ItemOffer> list, List<InvInvoiceDTO> dTOList) {
        for (ItemOffer entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

}

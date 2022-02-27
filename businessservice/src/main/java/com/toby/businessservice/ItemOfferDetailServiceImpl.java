/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.ItemOfferDTO;
import com.toby.dto.ItemOfferDetailDTO;
import com.toby.entity.InvItem;
import com.toby.entity.ItemOffer;
import com.toby.entity.ItemOfferDetail;
import com.toby.views.ItemsBarcodesDetailsView;
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
public class ItemOfferDetailServiceImpl implements ItemOfferDetailService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InvGlobalService invGlobalService;
    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;

    @Override
    public InvDetailDTO findItemOfferDetailByItemId(Integer itemId, Integer branchId) {
        ItemOfferDetail itemOfferDetail = dao.findEntityById(ItemOfferDetail.class, itemId);

        List<ItemOfferDetail> dTOList = new ArrayList<>();
        dTOList.add(itemOfferDetail);
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = invPurchaseInvoiceDetailsService.findItemBarcodeDetails(dTOList, branchId);
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        InvDetailDTO itemOfferDetailDTO = mapTODTO(itemOfferDetail, null, itemsBarcodeMap);
        return itemOfferDetailDTO;
    }

    @Override
    public void deleteItemOfferDetail(ItemOfferDetailDTO itemOfferDetailDTO) {
        ItemOfferDetail entity = new ItemOfferDetail();
        entity.setId(itemOfferDetailDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteItemOfferDetailList(List<InvDetailDTO> itemOfferDetailDTOList) {
        if (itemOfferDetailDTOList != null && !itemOfferDetailDTOList.isEmpty()) {
            for (InvDetailDTO detailDTO : itemOfferDetailDTOList) {
                if (detailDTO.getId() != null) {
                    ItemOfferDetail detail = new ItemOfferDetail();
                    detail.setId(detailDTO.getId());
                    dao.deleteEntity(detail);
                }
            }
        }

    }

    @Override
    public void deleteItemOfferDetail(Integer itemOfferDTOId) {
        if (itemOfferDTOId != null && itemOfferDTOId > 0) {
            dao.executeDeleteQuery("delete from ItemOfferDetail e WHERE e.id > 0 AND e.ItemOffer.id = " + itemOfferDTOId);
        }
    }

    @Override
    public List<InvDetailDTO> addItemOfferDetailList(List<InvDetailDTO> itemOfferDetailDTOList,Integer itemOfferId, TobyUser tobyUser) {
        List<ItemOfferDetail> itemOfferDetailDTOListNew = new ArrayList<>();
        for (InvDetailDTO itemOfferDetailDTO : itemOfferDetailDTOList) {
            ItemOfferDetail entity = mapToEntity(itemOfferDetailDTO,itemOfferId, tobyUser);
            entity = dao.updateEntity(entity);
            itemOfferDetailDTOListNew.add(entity);
        }
        return returnListDTO(itemOfferDetailDTOListNew, tobyUser.getBranchId().getId());
    }

    @Override
    public void addItemOfferDetail(InvDetailDTO itemOfferDetailDTO,Integer itemOfferId, TobyUser tobyUser) {

        ItemOfferDetail entity = mapToEntity(itemOfferDetailDTO,itemOfferId, tobyUser);

        entity = dao.updateEntity(entity);
    }

    @Override
    public List<ItemOfferDetailDTO> findItemOfferDetailByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        List<ItemOfferDetailDTO> itemOfferDetailDTOList = new ArrayList<>();
        params.put("branchId", branchId);

        if (branchId == null) {
            List<ItemOfferDetail> list = dao.findListByQuery("SELECT i FROM ItemOfferDetail i WHERE i.branchId.id =:branchId order by id desc ", params);

            returnListDTO(list, branchId);
        }
        return itemOfferDetailDTOList;
    }

    @Override
    public List<InvDetailDTO> findItemOfferDetailByItemOfferId(Integer branchId, Integer itemOffer) {
        Map<String, Object> params = new HashMap();
        params.put("itemOffer", itemOffer);

        if (itemOffer != null) {
            List<ItemOfferDetail> list = dao.findListByQuery("SELECT i FROM ItemOfferDetail i WHERE  i.itemOffer.id = :itemOffer order by i.id desc ", params);

            return returnListDTO(list, branchId);
        }
        return null;
    }

    @Override
    public ItemOfferDTO addInvPurchaseInvoiceDetail(ItemOfferDTO itemOfferDTO, Integer index) {
        try {
            ItemOfferDetailDTO itemOfferDetailDTO = new ItemOfferDetailDTO();
            itemOfferDetailDTO.setIndex(index);
            itemOfferDTO.setItemOfferDetailDTOSelected(itemOfferDetailDTO);
            itemOfferDTO.getItemOfferDetailDTOList().add(itemOfferDetailDTO);

        } catch (Exception e) {
            itemOfferDTO.setMsg(e.toString());
        }
        return itemOfferDTO;
    }

    private List<InvDetailDTO> returnListDTO(List<ItemOfferDetail> list, Integer branchId) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = invPurchaseInvoiceDetailsService.findItemBarcodeDetails(list, branchId);
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (ItemOfferDetail entity : list) {
            dTOList.add(mapTODTO(entity, null, itemsBarcodeMap));
        }
        return dTOList;
    }

    public InvDetailDTO mapTODTO(ItemOfferDetail entity, InvDetailDTO dTO, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap) {
        if (dTO == null) {
            dTO = new InvDetailDTO();
        }
        initMapToDTO(dTO, entity, itemsBarcodeMap);
        return dTO;
    }

    private void initMapToDTO(InvDetailDTO dTO, ItemOfferDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap) {
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());
        dTO.setId(entity.getId());
        dTO.setDiscount(entity.getDiscountRate());
        dTO.setDiscountValue(entity.getDiscountValue());
        dTO.setCostBefor(entity.getCostBefor());
        dTO.setCostAfter(entity.getCostAfter());

        if (entity.getItemBarcode() != null && !entity.getItemBarcode().isEmpty()) {
            ItemsBarcodesDetailsView barcodesDetailsView = itemsBarcodeMap.get(entity.getItemBarcode());
            dTO.setItemsBarcodesDetail(barcodesDetailsView);
            dTO.setItemsBarcodesDetailTrans(dTO.getItemsBarcodesDetail());
        }
        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public ItemOfferDetail mapToEntity(InvDetailDTO dTO,Integer itemOfferId, TobyUser tobyUser) {
        ItemOfferDetail entity = new ItemOfferDetail();
        entity.setId(dTO.getId());
        entity.setDiscountRate(dTO.getDiscount());
        entity.setDiscountValue(dTO.getDiscountValue());
        entity.setCostBefor(dTO.getCostBefor());
        entity.setCostAfter(dTO.getCostAfter());
        
        ItemOffer itemOffer = new ItemOffer();
        itemOffer.setId(itemOfferId);
        
        entity.setItemOffer(itemOffer);

        if (dTO.getItemsBarcodesDetail() == null && dTO.getItemsBarcodesDetailTrans() != null) {
            dTO.setItemsBarcodesDetail(dTO.getItemsBarcodesDetailTrans());
        }
        if (dTO.getItemsBarcodesDetail() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemsBarcodesDetail().getItemId());
            entity.setItemId(invItem);
            entity.setItemBarcode(dTO.getItemsBarcodesDetail().getBarcode());
        }

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

}

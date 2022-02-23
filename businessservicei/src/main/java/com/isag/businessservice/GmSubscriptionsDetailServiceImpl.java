/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.core.storedProcedureParameter;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.GmSubscriptionsDetailDTO;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.entity.GlYear;
import com.isag.entity.GmSubscriptions;
import com.isag.entity.GmSubscriptionsDetail;
import com.isag.entity.InvItem;
import com.isag.entity.InvTransferDetail;
import com.isag.entity.InventorySetup;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class GmSubscriptionsDetailServiceImpl implements GmSubscriptionsDetailService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    GmSubscriptionsService gmSubscriptionsDTOService;
    @EJB
    ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    InvGlobalService invGlobalService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private QuantityItemsStoreByDateService quantityItemsStoreByDateService;
    @EJB
    GenericService genericService;
    @EJB
    SymbolService symbolService;

    @Override
    public void deleteGmSubscriptionsDetails(GmSubscriptionsDetail gmSubscriptionsDetail) {
        dao.deleteEntity(gmSubscriptionsDetail);
    }

    @Override
    public List<GmSubscriptionsDetail> getAllGmSubscriptionsDetailDTOsByGmSubscriptionsDTOId(Integer gmSubscriptionsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("gmSubscriptionsId", gmSubscriptionsId);
        List<GmSubscriptionsDetail> details = dao.findListByQuery("SELECT e FROM GmSubscriptionsDetail e WHERE e.gmSubscriptionsId.id = :gmSubscriptionsId", params);
        return details;
    }

    @Override
    public GmSubscriptionsDetail findGmSubscriptionsDetailById(Integer gmSubscriptionsDetailDTOsId) {
        return dao.findEntityById(GmSubscriptionsDetail.class, gmSubscriptionsDetailDTOsId);
    }

    @Override
    public List<GmSubscriptionsDetailDTO> getAllGmSubscriptionsDetailDTOsByGmSubscriptionsDTOIdDTO(GmSubscriptionsDTO dto) {
        List<GmSubscriptionsDetailDTO> invDetailDTOList = null;

        invDetailDTOList = getAllGmSubscriptionsDetailDTOList(dto);

        return invDetailDTOList;
    }

    @Override
    public List<GmSubscriptionsDetailDTO> getAllGmSubscriptionsDetailDTOList(GmSubscriptionsDTO dto) {
        List<GmSubscriptionsDetail> details;
        List<GmSubscriptionsDetailDTO> invDetailDTOList;
        Map<String, Object> params = new HashMap<>();
        params.put("gmSubscriptionsId", dto.getId());
        details = dao.findListByQuery("SELECT e FROM GmSubscriptionsDetail e WHERE e.gmSubscriptionsId.id = :gmSubscriptionsId", params);
        invDetailDTOList = returnListDTO(details);
        return invDetailDTOList;
    }

    private List<GmSubscriptionsDetailDTO> returnListDTO(List<GmSubscriptionsDetail> list) {
        List<GmSubscriptionsDetailDTO> dTOList = new ArrayList<>();
        for (GmSubscriptionsDetail entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private GmSubscriptionsDetailDTO mapTODTO(GmSubscriptionsDetail Entity) {
        GmSubscriptionsDetailDTO dTO = initMapToDTO(Entity);
        return dTO;
    }

    private GmSubscriptionsDetailDTO initMapToDTO(GmSubscriptionsDetail entity) {

        GmSubscriptionsDetailDTO dTO = new GmSubscriptionsDetailDTO();

        dTO.setId(entity.getId());
        dTO.setSerial(entity.getSerial());

        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCost(entity.getCost() != null ? entity.getCost().setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
        dTO.setDiscount(entity.getDiscount() != null ? entity.getDiscount().setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
        dTO.setQuantity(entity.getQuantity() != null ? entity.getQuantity().setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
        dTO.setIsEdit(Boolean.TRUE);
        dTO.setDiscountType(entity.getDiscountType());
        dTO.setAddTotal(entity.getAddTotal());
        dTO.setValueRowDiscount(entity.getDiscountValue());

        if (entity.getItemSymbolId() != null) {
            dTO.setItemSymbolId(symbolService.mapTODTO(entity.getItemSymbolId(), null));
        }

        dTO.setExtraCost(entity.getExtraCost());
        dTO.setNet(entity.getNet());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        dTO.setItemId(invItemService.mapToDTO(entity.getItemId()));
        return dTO;
    }

    private GmSubscriptionsDetail mapToEntity(GmSubscriptionsDetailDTO dTO, Integer gmSubscriptionsDTOId, IsagUser isagUser) {
        GmSubscriptionsDetail entity = new GmSubscriptionsDetail();

        entity.setSerial(dTO.getSerial());
        entity.setCost(dTO.getCost().setScale(3, BigDecimal.ROUND_UP));
        entity.setDiscount(dTO.getDiscount());
        entity.setDiscountType(dTO.getDiscountType());
        entity.setDiscountValue(dTO.getValueRowDiscount());
        entity.setExtraCost(dTO.getExtraCost());
        entity.setFinalQuantity(dTO.getFinalQuantity());
        entity.setNet(dTO.getNet());
        entity.setQuantity(dTO.getQuantity());
        entity.setStatus(dTO.getStatus());
        entity.setTaxValue(dTO.getTaxValue());
        InvItem invItem = new InvItem();
        invItem.setId(41);
        entity.setItemId(invItem);

        entity.setAddTotal(dTO.getAddTotal());

        if (gmSubscriptionsDTOId != null) {
            GmSubscriptions gmSubscriptions = new GmSubscriptions();
            gmSubscriptions.setId(gmSubscriptionsDTOId);
            entity.setGmSubscriptionsId(gmSubscriptions);
        }

        if (dTO.getItemSymbolId() != null && dTO.getItemSymbolId().getId() != null) {
            Symbol item = new Symbol();
            item.setId(dTO.getItemSymbolId().getId());
            entity.setItemSymbolId(item);
        }

        if (dTO.getId() == null) {
            if (entity.getSerial() == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("gmSubscriptionsDTOId", gmSubscriptionsDTOId);
                Integer max = genericService.getMaxGenericSerialForDetail(GmSubscriptionsDetail.class, "gmSubscriptionsId", gmSubscriptionsDTOId);
                entity.setSerial(max);
            }
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser user = new IsagUser();
            user.setId(dTO.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setCompanyId(isagUser.getCompanyId());
        entity.setBranchId(isagUser.getBranchId());
        return entity;
    }

    @Override
    public void deleteGmSubscriptionsDetailDTOListDTO(List<GmSubscriptionsDetailDTO> gmSubscriptionsDetailDTOList) {
        if (gmSubscriptionsDetailDTOList != null && !gmSubscriptionsDetailDTOList.isEmpty()) {
            for (GmSubscriptionsDetailDTO detailDTO : gmSubscriptionsDetailDTOList) {
                if (detailDTO.getId() != null) {
                    GmSubscriptionsDetail gmSubscriptionsDetail = new GmSubscriptionsDetail();
                    gmSubscriptionsDetail.setId(detailDTO.getId());
                    dao.deleteEntity(gmSubscriptionsDetail);
                }
            }
        }

    }

    @Override
    public void deleteGmSubscriptionsDetailDTOListDTO(Integer SubscriptionsId) {
        List<GmSubscriptionsDetail> detailList = getAllGmSubscriptionsDetailDTOsByGmSubscriptionsDTOId(SubscriptionsId);

        if (detailList != null && !detailList.isEmpty()) {
            for (GmSubscriptionsDetail detail : detailList) {
                dao.deleteEntity(detail);
            }
        }

    }

    @Override
    public String addGmSubscriptionsDetailDTOsDTO(List<GmSubscriptionsDetailDTO> gmSubscriptionsDetailDTOList, GmSubscriptionsDTO gmSubscriptionsDTO, IsagUser isagUser) {
        String error = null;
        if (gmSubscriptionsDetailDTOList != null && !gmSubscriptionsDetailDTOList.isEmpty()) {
            if (error != null && !error.isEmpty()) {
                return error;
            }
            GmSubscriptionsDetail detail;
            for (GmSubscriptionsDetailDTO detailDTO : gmSubscriptionsDetailDTOList) {
                detail = mapToEntity(detailDTO, gmSubscriptionsDTO.getId(), isagUser);
                dao.updateEntity(detail);
            }
        }

        return error;
    }

}

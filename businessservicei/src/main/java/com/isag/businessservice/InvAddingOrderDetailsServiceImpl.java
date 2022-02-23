/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entity.InvItem;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
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
public class InvAddingOrderDetailsServiceImpl implements InvAddingOrderDetailsService {

    @EJB
    private GenericDAO dao;
    @EJB
    ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    InvGlobalService invGlobalService;
    @EJB
    GenericService genericService;

    @Override
    public void addInvAddingOrderDetails(InvAddingorderDetail invAddingOrderDetail) {
        dao.saveEntity(invAddingOrderDetail);
    }

    @Override
    public void deleteInvAddingOrderDetails(InvAddingorderDetail invAddingOrderDetail) {
        dao.deleteEntity(invAddingOrderDetail);
    }

    @Override
    public InvAddingorderDetail updateInvAddingOrderDetails(InvAddingorderDetail invAddingOrderDetail) {
        return dao.updateEntity(invAddingOrderDetail);
    }

    @Override
    public List<InvAddingorderDetail> getAllInvAddingOrderDetailsByInvAddingOrderId(Integer invAddingOrderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invAddingOrderId", invAddingOrderId);
        List<InvAddingorderDetail> details = dao.findListByQuery("SELECT e FROM InvAddingorderDetail e WHERE e.addingorderId.id = :invAddingOrderId", params);
        return details;
    }

    @Override
    public List<InvAddingorderDetail> getAllInvAddingOrderDetailsByInvAddingOrderIdWithStatus(Integer invAddingOrderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invAddingOrderId", invAddingOrderId);
        List<InvAddingorderDetail> details = dao.findListByQuery("SELECT e FROM InvAddingorderDetail e WHERE e.addingorderId.id = :invAddingOrderId and e.status not in (2) or e.status is null ", params);
        return details;
    }

    @Override
    public InvAddingorderDetail findInvAddingOrderDetailsById(Integer invAddingOrderDetailsId) {
        return dao.findEntityById(InvAddingorderDetail.class, invAddingOrderDetailsId);
    }

    @Override
    public BigDecimal findQuantityUsageForItem(Integer invPurchaseOrderDetailId) {
        Map<String, Object> params = new HashMap<>();
        params.put("selectedInvPurchaseOrderDetailId", invPurchaseOrderDetailId);
        BigDecimal invAddingOrdersList = dao.findEntityByQuery("SELECT sum(e.quantity) FROM InvAddingorderDetail e WHERE e.selectedPurchaseOrderId.id = :selectedInvPurchaseOrderDetailId  ", params);
        return invAddingOrdersList;
    }

    @Override
    public synchronized Integer getMaxSerialForInvoiceDetail(Integer addingOrderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("addingorderId", addingOrderId);

        Integer serial = dao.findEntityByQuery("SELECT max(e.serial) FROM InvAddingorderDetail e WHERE e.addingorderId.id =:addingorderId ", params);

        if (serial == null) {
            return 1;
        } else {
            serial = serial + 1;
            return serial;
        }
    }

    @Override
    public List<InvDetailDTO> getAllInvAddingOrderDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO dto) {
        Map<String, Object> params = new HashMap<>();
        params.put("invAddingOrderId", dto.getId());
        List<InvAddingorderDetail> details = dao.findListByQuery("SELECT e FROM InvAddingorderDetail e WHERE e.addingorderId.id = :invAddingOrderId", params);
        return returnListDTO(details, dto);
    }

    private List<InvDetailDTO> returnListDTO(List<InvAddingorderDetail> list, InvInvoiceDTO dto) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(dto.getBranchId());
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (InvAddingorderDetail entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, dto));
        }
        return dTOList;
    }

    private InvDetailDTO mapTODTO(InvAddingorderDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvDetailDTO dTO = initMapToDTO(entity, itemsBarcodeMap, invPurchaseInvoiceDTO);
        return dTO;
    }

    private InvDetailDTO initMapToDTO(InvAddingorderDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invInvoiceDTO) {

        InvDetailDTO dTO = new InvDetailDTO();
        dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                    entity.getId(), entity.getSerial(), (entity.getBranchId() != null ? entity.getBranchId().getId() : null), BigDecimal.ZERO,
                    BigDecimal.ZERO, entity.getQuantity(), 0, (entity.getAddingorderId() != null ? entity.getAddingorderId().getId() : null),
                    BigDecimal.ZERO, BigDecimal.ZERO, entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                    entity.getItemBarcode(), entity.getScrewing(), entity.getItemId().getId(), entity.getWeight(), 
                    entity.getUnitId().getId(), entity.getUnitId().getSerial(), 
                    entity.getUnitId().getName(),entity.getItemId().getIsInventoryItem(),BigDecimal.ZERO,BigDecimal.ZERO
                    ,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        if(invInvoiceDTO.getLoadedSerial() != null){
            dTO.setIsEdit(Boolean.TRUE);
        }
        dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true);        
        dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        return dTO;
    }

    @Override
    public String addInvAddingOrderDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, invInvoiceDTO.getInvInventoryId().getId(), invInvoiceDTO.getScreenName(),
                    invInvoiceDTO.getInventorySetupDTO().getNegativeSell(),(invInvoiceDTO.getLoadedSerial() != null),
                    invInvoiceDTO.getInventorySetupDTO().getSellBuy(),invInvoiceDTO.getSupplierInvoiceNumber());
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvAddingorderDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), isagUser);
                if (detail.getMsg() != null && !detail.getMsg().isEmpty()) {
                    error = detail.getMsg();
                    return error;
                } else {
                    dao.updateEntity(detail);
                }
            }
        }

        return error;
    }

    private InvAddingorderDetail mapToEntity(InvDetailDTO dTO, Integer invAddingOrderId, IsagUser isagUser) {
        InvAddingorderDetail entity = new InvAddingorderDetail();

        entity.setSerial(dTO.getSerial());
        entity.setFinalQuantity(dTO.getFinalQuantity());
        entity.setItemBarcode(dTO.getItemBarcode());
        entity.setQuantity(dTO.getQuantity());
        entity.setScrewing(dTO.getScrewing());
        entity.setStatus(dTO.getStatus());
        entity.setWeight(dTO.getWeight());

        if (invAddingOrderId != null) {
            InvAddingorder invAddingorder = new InvAddingorder();
            invAddingorder.setId(invAddingOrderId);
            entity.setAddingorderId(invAddingorder);
        }

        if (dTO.getItemsBarcodesDetail() != null || dTO.getItemsBarcodesDetailTrans() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getItemId() : dTO.getItemsBarcodesDetailTrans().getItemId());
            entity.setItemId(invItem);
            entity.setItemBarcode(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getBarcode() : dTO.getItemsBarcodesDetailTrans().getBarcode());
        }

        if (dTO.getItemsBarcodesDetail() != null && dTO.getItemsBarcodesDetail().getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getItemsBarcodesDetail().getUnitId());
            entity.setUnitId(symbol);
        } else if (dTO.getItemsBarcodesDetailTrans() != null && dTO.getItemsBarcodesDetailTrans().getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getItemsBarcodesDetailTrans().getUnitId());
            entity.setUnitId(symbol);
        } else {
            entity.setMsg("يجب اختيار صنف");
            return entity;
        }

        if (dTO.getId() == null) {
            if (entity.getSerial() == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("invPurchaseOrderId", invAddingOrderId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvAddingorderDetail.class, "addingorderId", invAddingOrderId);
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
    public void deleteInvAddingOrderDetailListDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList) {
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {
                if (detailDTO.getId() != null) {
                    InvAddingorderDetail addingorderDetail = new InvAddingorderDetail();
                    addingorderDetail.setId(detailDTO.getId());
                    dao.deleteEntity(addingorderDetail);
                }
            }
        }

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.core.storedProcedureParameter;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.InvItem;
import com.toby.entity.InvPurchaseOrder;
import com.toby.entity.InvPurchaseOrderDetail;
import com.toby.entity.Symbol;
import com.toby.procedure.TempInvPurchaseInvoiceNotLoadedYet;
import com.toby.procedure.TempInvPurchaseOrderNotLoadedYet;
import com.toby.views.ItemsBarcodesDetailsView;
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
 * @author hq002
 */
@Stateless
public class InvPurchaseOrderDetailsServiceImpl implements InvPurchaseOrderDetailsService {

    @EJB
    private GenericDAO dao;
    @EJB
    ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    InvGlobalService invGlobalService;
    @EJB
    GenericService genericService;

    @Override
    public void addPurchaseOrderDetails(InvPurchaseOrderDetail details) {
        dao.saveEntity(details);
    }

    @Override
    public void deletePurchaseOrderDetails(InvPurchaseOrderDetail details) {
        dao.deleteEntity(details);
    }

    @Override
    public InvPurchaseOrderDetail updatePurchaseOrderDetails(InvPurchaseOrderDetail details) {
        return dao.updateEntity(details);
    }

    @Override
    public List<InvPurchaseOrderDetail> getAllPurchaseOrderDetails() {
        return dao.findAll(InvPurchaseOrderDetail.class);
    }

    @Override
    public List<InvPurchaseOrderDetail> getAllPurchaseOrderDetailsByInvPurchaseOrderId(Integer invPurchaseOrderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseOrderId", invPurchaseOrderId);
        List<InvPurchaseOrderDetail> details = dao.findListByQuery("SELECT e FROM InvPurchaseOrderDetail e WHERE e.invPurchaseOrderId.id = :invPurchaseOrderId", params);
        return details;
    }

    @Override
    public List<InvPurchaseOrderDetail> getAllPurchaseOrderDetails(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("purchaseId", id);
        return dao.findListByQuery("SELECT d FROM InvPurchaseOrderDetail d WHERE d.invPurchaseOrderId.id=:purchaseId", params);
    }

    @Override
    public List<InvPurchaseOrderDetail> getAllPurchaseOrderDetailsWithStatus(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("purchaseId", id);
        return dao.findListByQuery("SELECT d FROM InvPurchaseOrderDetail d WHERE d.invPurchaseOrderId.id=:purchaseId and d.status != 2 ", params);
    }

    @Override
    public InvPurchaseOrderDetail findInvPurchaseOrderDetailById(Integer invPurchaseOrderDetailId) {
        return dao.findEntityById(InvPurchaseOrderDetail.class, invPurchaseOrderDetailId);
    }

    private void invPurchaseOrderNotLoadedYetProcedure(Integer inv_purchase_order_id1) {
        List<storedProcedureParameter> procedureParameter = new ArrayList<>();
        procedureParameter.add(new storedProcedureParameter("inv_purchase_order_id1", Integer.class, 'i'));
        Map<String, Object> params2 = new HashMap<>();
        params2.put("inv_purchase_order_id1", inv_purchase_order_id1);
        dao.StoredProcedure("inv_purchase_order_not_loaded_yet_procedure", procedureParameter, params2);
    }

    @Override
    public List<InvDetailDTO> getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO dto) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseOrderId", dto.getId());
        List<InvPurchaseOrderDetail> details;
        List<InvDetailDTO> invDetailDTOList;
        if (dto.getNeedLoadedSerial() != null && dto.getNeedLoadedSerial() != 0) {
            invPurchaseOrderNotLoadedYetProcedure(dto.getId());
            List<TempInvPurchaseInvoiceNotLoadedYet> list = dao.findListByQuery("SELECT t FROM TempInvPurchaseOrderNotLoadedYet t");
            invDetailDTOList = returnListDTO(list, dto);
        } else {
            details = dao.findListByQuery("SELECT e FROM InvPurchaseOrderDetail e WHERE e.invPurchaseOrderId.id = :invPurchaseOrderId", params);
            invDetailDTOList = returnListDTO(details, dto);
        }

        return invDetailDTOList;
    }

    private <T> List<InvDetailDTO> returnListDTO(List<T> list, InvInvoiceDTO dto) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(dto.getBranchId());
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (T entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, dto));
        }
        return dTOList;
    }

    private <T> InvDetailDTO mapTODTO(T Entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvDetailDTO dTO = initMapToDTO(Entity, itemsBarcodeMap, invPurchaseInvoiceDTO);
        return dTO;
    }

    private <T> InvDetailDTO initMapToDTO(T Entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invInvoiceDTO) {

        InvDetailDTO dTO = new InvDetailDTO();
        if (Entity.getClass().equals(InvPurchaseOrderDetail.class)) {
            InvPurchaseOrderDetail entity = (InvPurchaseOrderDetail) Entity;
            dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                    entity.getId(), entity.getSerial(), (entity.getBranchId() != null ? entity.getBranchId().getId() : null), entity.getPrice(),
                    entity.getDiscountRate(), entity.getQuantity(), 0, (entity.getInvPurchaseOrderId() != null ? entity.getInvPurchaseOrderId().getId() : null),
                    BigDecimal.ZERO, entity.getNet(), entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                    null,null, 
                    null, 
                    null,null,
                    entity.getItemBarcode(), entity.getScrewing(), entity.getItemId().getId(), entity.getWeight(),
                    entity.getUnitId().getId(), entity.getUnitId().getSerial(), entity.getUnitId().getName(),
                    entity.getItemId().getIsInventoryItem(),
                    null,null,null,null,
                    invInvoiceDTO.getCompanyActivity(),invInvoiceDTO.getScreenName(),null);
            dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true);
            dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        } else if (Entity.getClass().equals(TempInvPurchaseOrderNotLoadedYet.class)) {
            TempInvPurchaseOrderNotLoadedYet entity = (TempInvPurchaseOrderNotLoadedYet) Entity;
            dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                    null, entity.getSerial(), null, BigDecimal.ZERO,
                    BigDecimal.ZERO, entity.getQuantity(), 0, null,
                    BigDecimal.ZERO, BigDecimal.ZERO, entity.getItemId(), null, null,
                    null,null, 
                    null, 
                    null,null,
                    entity.getItemBarcode(), entity.getScrewing(), entity.getItemId(), entity.getWeight(),
                    entity.getUnitId(), entity.getUnitSerial(), entity.getUnitName(),entity.getIsInventoryItem(),
                    null,null,null,null,
                    invInvoiceDTO.getCompanyActivity(),invInvoiceDTO.getScreenName(),null);
            dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true);
            dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        }
        return dTO;
    }

    @Override
    public void deleteInvPurchaseInvoiceDetailListDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList) {
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {
                if (detailDTO.getId() != null) {
                    InvPurchaseOrderDetail invPurchaseInvoiceDetail = new InvPurchaseOrderDetail();
                    invPurchaseInvoiceDetail.setId(detailDTO.getId());
                    dao.deleteEntity(invPurchaseInvoiceDetail);
                }
            }
        }

    }

    @Override
    public String addInvPurchaseOrderDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, ScreenNameClassEnum screenNameClassEnum, TobyUser tobyUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, null, screenNameClassEnum, invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), (invInvoiceDTO.getLoadedSerial() != null), invInvoiceDTO.getInventorySetupDTO().getSellBuy(),invInvoiceDTO.getCompanyActivity());
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvPurchaseOrderDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), tobyUser);
                dao.updateEntity(detail);
            }
        }

        return error;
    }

    private InvPurchaseOrderDetail mapToEntity(InvDetailDTO dTO, Integer invPurchaseOrderId, TobyUser tobyUser) {
        InvPurchaseOrderDetail entity = new InvPurchaseOrderDetail();

        entity.setSerial(dTO.getSerial());
        entity.setFinalQuantity(dTO.getFinalQuantity());
        entity.setItemBarcode(dTO.getItemBarcode());
        entity.setNet(dTO.getNet());
        entity.setQuantity(dTO.getQuantity());
        entity.setScrewing(dTO.getScrewing());
        entity.setStatus(dTO.getStatus());
        entity.setWeight(dTO.getWeight());
        entity.setPrice(dTO.getCost());
        entity.setDiscountRate(dTO.getDiscount());

        if (invPurchaseOrderId != null) {
            InvPurchaseOrder invPurchaseOrder = new InvPurchaseOrder();
            invPurchaseOrder.setId(invPurchaseOrderId);
            entity.setInvPurchaseOrderId(invPurchaseOrder);
        }

        if (dTO.getItemsBarcodesDetail() != null || dTO.getItemsBarcodesDetailTrans() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getItemId() : dTO.getItemsBarcodesDetailTrans().getItemId());
            entity.setItemId(invItem);
            entity.setItemBarcode(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getBarcode() : dTO.getItemsBarcodesDetailTrans().getBarcode());
        }

        if (dTO.getItemsBarcodesDetail().getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getItemsBarcodesDetail().getUnitId());
            entity.setUnitId(symbol);
        }

        if (dTO.getId() == null) {
            if (entity.getSerial() == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("invPurchaseOrderId", invPurchaseOrderId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvPurchaseOrderDetail.class, "invPurchaseOrderId", invPurchaseOrderId);
                entity.setSerial(max);
            }
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setCompanyId(tobyUser.getCompanyId());
        entity.setBranchId(tobyUser.getBranchId());
        return entity;
    }

}

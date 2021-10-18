/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.core.storedProcedureParameter;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InvItem;
import com.isag.entity.InvQotation;
import com.isag.entity.InvQotationDetail;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.procedure.TempInvQotationNotLoadedYet;
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
public class InvQuotationDetailsServiceImpl implements InvQuotationDetailsService {

    @EJB
    private GenericDAO dao;
    @EJB
    ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    InvGlobalService invGlobalService;
    @EJB
    GenericService genericService;

    @Override
    public void addInvQotationDetails(InvQotationDetail details) {
        dao.saveEntity(details);
    }

    @Override
    public void deleteInvQotationDetails(InvQotationDetail details) {
        dao.deleteEntity(details);
    }

    @Override
    public InvQotationDetail updateInvQotationDetails(InvQotationDetail details) {
        return dao.updateEntity(details);
    }

    @Override
    public List<InvQotationDetail> getAllInvQotationDetails() {
        return dao.findAll(InvQotationDetail.class);
    }

    @Override
    public List<InvQotationDetail> getAllInvQotationDetails(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("quotationId", id);
        return dao.findListByQuery("SELECT d FROM InvQotationDetail d WHERE d.qotationId.id =:quotationId", params);
    }

    @Override
    public InvQotationDetail findInvQotationDetailById(Integer invQotationDetailId) {
        return dao.findEntityById(InvQotationDetail.class, invQotationDetailId);
    }

    @Override
    public List<InvQotationDetail> getAllInvQotationDetailsByIInvQotationIdWithStatus(Integer invQotationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("qotationId", invQotationId);
        List<InvQotationDetail> details = dao.findListByQuery("SELECT e FROM InvQotationDetail e WHERE e.qotationId.id = :qotationId and (e.status != 2  OR e.status is null) ", params);
        return details;
    }


    private void invQotationNotLoadedYetProcedure(Integer inv_purchase_order_id1, Integer type1) {
        List<storedProcedureParameter> procedureParameter = new ArrayList<>();
        procedureParameter.add(new storedProcedureParameter("inv_purchase_invoice_id1", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("type1", Integer.class, 'i'));
        Map<String, Object> params2 = new HashMap<>();
        params2.put("inv_purchase_invoice_id1", inv_purchase_order_id1);
        params2.put("type1", type1);
        dao.StoredProcedure("inv_qotation_not_loaded_yet_procedure", procedureParameter, params2);
    }

    @Override
    public List<InvDetailDTO> getAllInvQutationDetailByInvPurchaseInvoiceIdDTO(InvInvoiceDTO dto) {
        Map<String, Object> params = new HashMap<>();
        params.put("qotationId", dto.getId());
        List<InvQotationDetail> details ;
        List<InvDetailDTO> invDetailDTOList ;
        if (dto.getNeedLoadedSerial() != null && dto.getNeedLoadedSerial() != 0) {
            invQotationNotLoadedYetProcedure(dto.getId(), dto.getType());
            List<TempInvQotationNotLoadedYet> list = dao.findListByQuery("SELECT t FROM TempInvQotationNotLoadedYet t");
            invDetailDTOList = returnListDTO(list, dto);
        } else {
            details = dao.findListByQuery("SELECT e FROM InvQotationDetail e WHERE e.qotationId.id = :qotationId", params);
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
        if (Entity.getClass().equals(InvQotationDetail.class)) {
            InvQotationDetail entity = (InvQotationDetail) Entity;
            dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                    entity.getId(), entity.getSerial(), (entity.getBranchId() != null ? entity.getBranchId().getId() : null), entity.getPrice(),
                    entity.getDiscount(), entity.getQuantity(), entity.getDiscountId(), (entity.getQotationId() != null ? entity.getQotationId().getId() : null),
                    BigDecimal.ZERO, entity.getNet(), entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                    entity.getItemBarcode(), entity.getScrewing(), entity.getItemId().getId(), entity.getWeight(), 
                    entity.getUnitId().getId(), entity.getUnitId().getSerial(), entity.getUnitId().getName(),
                    entity.getItemId().getIsInventoryItem(),BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,
                    BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
            dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true);
            dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        } else if (Entity.getClass().equals(TempInvQotationNotLoadedYet.class)) {
            TempInvQotationNotLoadedYet entity = (TempInvQotationNotLoadedYet) Entity;
            
             dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                    null, entity.getSerial(), null, entity.getCost(),
                    entity.getDiscount(), entity.getQuantity(), entity.getDiscountType(), null,
                    entity.getExtraCost(), entity.getNet(), entity.getItemId(), null, null,
                    entity.getItemBarcode(), entity.getScrewing(), entity.getItemId(), entity.getWeight(), 
                    entity.getUnitId(), entity.getUnitSerial(), entity.getUnitName(),1,BigDecimal.ZERO,BigDecimal.ZERO
                    ,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
             
            dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true);
            dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        }
        return dTO;
    }

    private InvDetailDTO initMapToDTO(InvQotationDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invInvoiceDTO) {

        InvDetailDTO dTO = new InvDetailDTO();
        dTO.setId(entity.getId());
        dTO.setSerial(entity.getSerial());
        dTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dTO.setCost(entity.getPrice() != null ? entity.getPrice().setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
        dTO.setDiscount(entity.getDiscount() != null ? entity.getDiscount().setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
        dTO.setQuantity(entity.getQuantity() != null ? entity.getQuantity().setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO);

        dTO.setDiscountType(0);

        if (entity.getItemBarcode() != null && !"".equals(entity.getItemBarcode())) {
            ItemsBarcodesDetailsView barcodesDetailsView = itemsBarcodeMap.get(entity.getItemBarcode());
            dTO.setItemsBarcodesDetail(itemsBarcodeMap.get(entity.getItemBarcode()));
            dTO.setItemsBarcodesDetailTrans(dTO.getItemsBarcodesDetail());
            dTO.setScrewing(entity.getScrewing());
            if (entity.getItemId() != null && barcodesDetailsView != null && barcodesDetailsView.getPaintColor() != null) {
                SymbolDTO paintColor = new SymbolDTO();
                paintColor.setId(barcodesDetailsView.getPaintColor());
                paintColor.setName(barcodesDetailsView.getPaintColorName());
                dTO.setPaintColor(paintColor);
            }
            dTO.setLength(entity.getItemId().getLength());
        }

        dTO.setWeight(entity.getWeight() != null ? entity.getWeight() : BigDecimal.ZERO);
        if (entity.getUnitId() != null) {
            SymbolDTO unitDTO = new SymbolDTO();
            unitDTO.setId(entity.getUnitId().getId());
            unitDTO.setName(entity.getUnitId().getName());
            unitDTO.setSerial(entity.getUnitId().getSerial());
            dTO.setUnitId(unitDTO);
        }

        dTO.setNet(entity.getNet());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true);
        dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        return dTO;
    }

    @Override
    public void deleteInvQuotationDetailListDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList) {
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {
                if (detailDTO.getId() != null) {
                    InvQotationDetail invPurchaseInvoiceDetail = new InvQotationDetail();
                    invPurchaseInvoiceDetail.setId(detailDTO.getId());
                    dao.deleteEntity(invPurchaseInvoiceDetail);
                }
            }
        }

    }

    @Override
    public String addInvQuotationDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, ScreenNameClassEnum screenNameClassEnum, IsagUser isagUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, null, screenNameClassEnum, invInvoiceDTO.getInventorySetupDTO().getNegativeSell(),
                    (invInvoiceDTO.getLoadedSerial() != null),invInvoiceDTO.getInventorySetupDTO().getSellBuy(),invInvoiceDTO.getSupplierInvoiceNumber());
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvQotationDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), isagUser);
                dao.updateEntity(detail);
            }
        }

        return error;
    }

    private InvQotationDetail mapToEntity(InvDetailDTO dTO, Integer InvQotationId, IsagUser isagUser) {
        InvQotationDetail entity = new InvQotationDetail();

        entity.setSerial(dTO.getSerial());
        entity.setFinalQuantity(dTO.getFinalQuantity());
        entity.setItemBarcode(dTO.getItemBarcode());
        entity.setNet(dTO.getNet());
        entity.setQuantity(dTO.getQuantity());
        entity.setScrewing(dTO.getScrewing());
        entity.setStatus(dTO.getStatus());
        entity.setWeight(dTO.getWeight());
        entity.setPrice(dTO.getCost());
        entity.setDiscount(dTO.getDiscount());

        if (InvQotationId != null) {
            InvQotation invQotation = new InvQotation();
            invQotation.setId(InvQotationId);
            entity.setQotationId(invQotation);
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
                params.put("qotationId", InvQotationId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvQotationDetail.class, "qotationId", InvQotationId);
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

}

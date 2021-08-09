/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.core.storedProcedureParameter;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InvDetailDTO;
import com.isag.entity.GlYear;
import com.isag.entity.InvItem;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvTransferDetail;
import com.isag.entity.InventorySetup;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.procedure.TempInvPurchaseInvoiceNotLoadedYet;
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
public class InvPurchaseInvoiceDetailsServiceImpl implements InvPurchaseInvoiceDetailsService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    InvPurchaseInvoiceService invPurchaseInvoiceService;
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

    @Override
    public List<InvPurchaseInvoiceDetail> getAllInvPurchaseInvoiceDetailByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvPurchaseInvoiceDetail> detailses = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.branchId.id = :branchId", params);
        return detailses;
    }

    @Override
    public void reCalcCostAvarage(Integer branchId) {
        invItemService.getInvItemListByBranchId(branchId);
        InventorySetup invSetup = inventorySetupService.getInventoryByBranchId(branchId);
        List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList = getAllInvPurchaseInvoiceDetailByBranchId(branchId);
        for (InvPurchaseInvoiceDetail detail : invPurchaseInvoiceDetailList) {
            BigDecimal quantityItemsStoreByDate = invPurchaseInvoiceService.findQuantityItemByDateWithOutInventory(detail.getInvPurchaseInvoiceId(), invSetup.getSellBuy(), detail);
            invPurchaseInvoiceService.saveDetail(detail, detail.getInvPurchaseInvoiceId(), detail.getInvPurchaseInvoiceId().getType(), quantityItemsStoreByDate, invSetup);
            System.out.println("item_id -> " + detail.getItemId().getId() + " ; Item_Name -> " + detail.getItemId().getName() + " ; Qty -> " + quantityItemsStoreByDate + "");
        }
    }

    @Override
    public List<InvPurchaseInvoiceDetail> getAllInvPurchaseInvoiceDetailByBranchIdByDate(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvPurchaseInvoiceDetail> detailses = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.branchId.id = :branchId order e.invPurchaseInvoiceId.date ", params);
        return detailses;
    }

    @Override
    public void addInvPurchaseInvoiceDetails(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        dao.saveEntity(invPurchaseInvoiceDetail);
    }

    @Override
    public void deleteInvPurchaseInvoiceDetails(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        dao.deleteEntity(invPurchaseInvoiceDetail);
    }

    @Override
    public InvPurchaseInvoiceDetail updateInvPurchaseInvoiceDetails(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        return dao.updateEntity(invPurchaseInvoiceDetail);
    }

    @Override
    public List<InvPurchaseInvoiceDetail> getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(Integer invPurchaseInvoiceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);
        List<InvPurchaseInvoiceDetail> details = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id = :invPurchaseInvoiceId", params);
        return details;
    }

    @Override
    public List<InvPurchaseInvoiceDetail> getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdWithStatus(Integer invPurchaseInvoiceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);
        List<InvPurchaseInvoiceDetail> details = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id = :invPurchaseInvoiceId AND (e.status != 2 OR e.status is null)", params);
        return details;
    }

    @Override
    public InvPurchaseInvoiceDetail findInvPurchaseInvoiceDetailsById(Integer invPurchaseInvoiceDetailsId) {
        return dao.findEntityById(InvPurchaseInvoiceDetail.class, invPurchaseInvoiceDetailsId);
    }

    @Override
    public BigDecimal getLastCostItem(Integer itemId, GlYear glYear, Integer branchId) {
        List<BigDecimal> purchaseInvoiceDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("itemId", itemId);
        params.put("branchId", branchId);
        params.put("dateFrom", glYear.getDateFrom());
        params.put("dateTo", glYear.getDateTo());
        purchaseInvoiceDetails = dao.findListByQuery("SELECT d.price FROM InvPurchaseOrder ipo Left join InvPurchaseOrderDetail d  WHERE ipo.branchId.id = :branchId AND d.itemId.id = :itemId AND ipo.date >= :dateFrom AND ipo.date <= :dateTo order by ipo.date desc", params);
        if (purchaseInvoiceDetails != null && !purchaseInvoiceDetails.isEmpty()) {
            return purchaseInvoiceDetails.get(0);
        }

        return BigDecimal.ZERO;
    }

    public void updateCostAvarage(Integer branchId) {
        List<InvPurchaseInvoiceDetail> purchaseInvoiceDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("type", true);

        purchaseInvoiceDetails = dao.findListByQuery("SELECT ipid FROM InvPurchaseInvoiceDetail ipid Left join InvPurchaseInvoice ipi  WHERE ipi.branchId.id = :branchId AND ipi.id = ipid.invPurchaseInvoiceId.id AND ipi.type = :type", params);

        for (InvPurchaseInvoiceDetail detail : purchaseInvoiceDetails) {
            if (detail.getItemId() != null) {
                List<BigDecimal> costAvarage = dao.executeNativeQuery("select avarage_cost from update_cost_avarage  where item_id = " + detail.getItemId().getId());
                if (costAvarage != null && !costAvarage.isEmpty()) {
                    detail.setCostAvarage(costAvarage.get(0));
                    dao.updateEntity(detail);
                }

            }
        }

    }

    private void invPurchaseInvoiceNotLoadedYetProcedure(Integer inv_purchase_order_id1, Integer type1) {
        List<storedProcedureParameter> procedureParameter = new ArrayList<>();
        procedureParameter.add(new storedProcedureParameter("inv_purchase_invoice_id1", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("type1", Integer.class, 'i'));
        Map<String, Object> params2 = new HashMap<>();
        params2.put("inv_purchase_invoice_id1", inv_purchase_order_id1);
        params2.put("type1", type1);
        dao.StoredProcedure("inv_purchase_invoice_not_loaded_yet_procedure", procedureParameter, params2);
    }

    @Override
    public List<InvDetailDTO> getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO dto) {
        List<InvDetailDTO> invDetailDTOList = null;
        if (dto.getNeedLoadedSerial() != null && dto.getNeedLoadedSerial() != 0) {
            if (ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(dto.getScreenName())) {
                invPurchaseInvoiceNotLoadedYetProcedure(dto.getId(), 2);
            } else if (ScreenNameClassEnum.INVRETURNSALESFORM.equals(dto.getScreenName())
                    || ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(dto.getScreenName())) {
                invPurchaseInvoiceNotLoadedYetProcedure(dto.getId(), 3);
            }

            List<TempInvPurchaseInvoiceNotLoadedYet> list = dao.findListByQuery("SELECT t FROM TempInvPurchaseInvoiceNotLoadedYet t");
            invDetailDTOList = returnListDTO(list, dto);
        } else {
            invDetailDTOList = getAllInvPurchaseInvoiceDetailList(dto);
        }

        return invDetailDTOList;
    }

    @Override
    public List<InvDetailDTO> getAllInvPurchaseInvoiceDetailList(InvInvoiceDTO dto) {
        List<InvPurchaseInvoiceDetail> details;
        List<InvDetailDTO> invDetailDTOList;
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", dto.getId());
        details = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id = :invPurchaseInvoiceId", params);
        invDetailDTOList = returnListDTO(details, dto);
        return invDetailDTOList;
    }

    private <T> List<InvDetailDTO> returnListDTO(List<T> list, InvInvoiceDTO invInvoiceDTO) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
//        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(dto.getBranchId());
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
//        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
//            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
//        }
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = findItemBarcodeDetails(list, invInvoiceDTO);

        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (T entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, invInvoiceDTO));
        }
        return dTOList;
    }

    @Override
    public <T> List<ItemsBarcodesDetailsView> findItemBarcodeDetails(List<T> list, InvInvoiceDTO invInvoiceDTO) {
        List<String> barcodeList = new ArrayList<>();
        for (T Entity : list) {
            if (Entity.getClass().equals(InvPurchaseInvoiceDetail.class)) {
                InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) Entity;
                barcodeList.add(entity.getItemBarcode());
            } else if (Entity.getClass().equals(InvTransferDetail.class)) {
                InvTransferDetail entity = (InvTransferDetail) Entity;
                barcodeList.add(entity.getItemBarcode());
            } else if (Entity.getClass().equals(TempInvPurchaseInvoiceNotLoadedYet.class)) {
                TempInvPurchaseInvoiceNotLoadedYet entity = (TempInvPurchaseInvoiceNotLoadedYet) Entity;
                barcodeList.add(entity.getItemBarcode());
            }
        }
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdByBarcode(invInvoiceDTO.getBranchId(), barcodeList);
        return itemsBarcodesDetailsViewList;
    }

    private <T> InvDetailDTO mapTODTO(T Entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvDetailDTO dTO = initMapToDTO(Entity, itemsBarcodeMap, invPurchaseInvoiceDTO);
        return dTO;
    }

    private <T> InvDetailDTO initMapToDTO(T Entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invInvoiceDTO) {

        InvDetailDTO dTO = new InvDetailDTO();
        if (Entity.getClass().equals(InvPurchaseInvoiceDetail.class)) {
            InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) Entity;
            dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                    entity.getId(), entity.getSerial(), (entity.getBranchId() != null ? entity.getBranchId().getId() : null), entity.getCost(),
                    entity.getDiscount(), entity.getQuantity(), entity.getDiscountType(), (entity.getInvPurchaseInvoiceId() != null ? entity.getInvPurchaseInvoiceId().getId() : null),
                    entity.getExtraCost(), entity.getNet(), entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                    entity.getItemBarcode(), entity.getScrewing(), entity.getItemId().getId(), entity.getWeight(),
                    entity.getUnitId().getId(), entity.getUnitId().getSerial(), entity.getUnitId().getName(),
                    entity.getItemId().getIsInventoryItem(), BigDecimal.ZERO, BigDecimal.ZERO, entity.getAdd1(),
                    entity.getAdd2(), entity.getAdd3(), entity.getDisc1(), entity.getDisc2(), entity.getDisc3(), entity.getTotalBeforefracture(), entity.getFractureNumber(), entity.getValuefracture(), entity.getCartonsNumber(), entity.getCartonsCapacity(), entity.getAddTotal());
            dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true);
            dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
            dTO.setItemId(invItemService.mapToDTO(entity.getItemId()));
        } else if (Entity.getClass().equals(TempInvPurchaseInvoiceNotLoadedYet.class)) {
            TempInvPurchaseInvoiceNotLoadedYet entity = (TempInvPurchaseInvoiceNotLoadedYet) Entity;

            dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                    null, entity.getSerial(), null, entity.getCost(),
                    entity.getDiscount(), entity.getQuantity(), entity.getDiscountType(), null,
                    entity.getExtraCost(), entity.getNet(), entity.getItemId(), null, null,
                    entity.getItemBarcode(), entity.getScrewing(), entity.getItemId(), entity.getWeight(),
                    entity.getUnitId(), entity.getUnitSerial(), entity.getUnitName(), entity.getIsInventoryItem(), BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

            dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true);
            dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        }
        return dTO;
    }

    private InvPurchaseInvoiceDetail mapToEntity(InvDetailDTO dTO, Integer invPurchaseInvoiceId, IsagUser isagUser) {
        InvPurchaseInvoiceDetail entity = new InvPurchaseInvoiceDetail();

        entity.setSerial(dTO.getSerial());
        entity.setCost(dTO.getCost().setScale(3, BigDecimal.ROUND_UP));
        entity.setCostAvarage(dTO.getCostAvarage());
        entity.setDiscount(dTO.getDiscount());
        entity.setDiscountType(dTO.getDiscountType());
        entity.setExtraCost(dTO.getExtraCost());
        entity.setFinalQuantity(dTO.getFinalQuantity());
        entity.setItemBarcode(dTO.getItemBarcode());
        entity.setNet(dTO.getNet());
        entity.setQuantity(dTO.getQuantity());
        entity.setScrewing(dTO.getScrewing());
        entity.setStatus(dTO.getStatus());
        entity.setTaxValue(dTO.getTaxValue());
        entity.setTransferFrom(dTO.getTransferFrom());
        entity.setWeight(dTO.getWeight());
        entity.setDisc1(dTO.getDisc1());
        entity.setDisc2(dTO.getDisc2());
        entity.setDisc3(dTO.getDisc3());
        entity.setAdd1(dTO.getAdd1());
        entity.setAdd2(dTO.getAdd2());
        entity.setAdd3(dTO.getAdd3());
        entity.setFractureNumber(dTO.getFractureNumber());
        entity.setValuefracture(dTO.getValuefracture());
        entity.setTotalBeforefracture(dTO.getTotalBeforefracture());
        entity.setCartonsCapacity(dTO.getCartonsCapacity());
        entity.setCartonsNumber(dTO.getCartonsNumber());
        entity.setAddTotal(dTO.getAddTotal());

        if (invPurchaseInvoiceId != null) {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(invPurchaseInvoiceId);
            entity.setInvPurchaseInvoiceId(invPurchaseInvoice);
        }

//        if (dTO.getItemId() != null && dTO.getItemId().getId() != null) {
//            InvItem item = new InvItem();
//            item.setId(dTO.getItemId().getId());
//            entity.setItemId(item);
//        }
        if (dTO.getItemsBarcodesDetail() == null && dTO.getItemsBarcodesDetailTrans() != null) {
            dTO.setItemsBarcodesDetail(dTO.getItemsBarcodesDetailTrans());
        }

        if (dTO.getItemsBarcodesDetail() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemsBarcodesDetail().getItemId());
            entity.setItemId(invItem);
            entity.setItemBarcode(dTO.getItemsBarcodesDetail().getBarcode());

            Symbol symbol = new Symbol();
            symbol.setId(dTO.getItemsBarcodesDetail().getUnitId());
            entity.setUnitId(symbol);

        }

        if (dTO.getId() == null) {
            if (entity.getSerial() == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvPurchaseInvoiceDetail.class, "invPurchaseInvoiceId", invPurchaseInvoiceId);
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
    public void deleteInvPurchaseInvoiceDetailListDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList) {
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {
                if (detailDTO.getId() != null) {
                    InvPurchaseInvoiceDetail invPurchaseInvoiceDetail = new InvPurchaseInvoiceDetail();
                    invPurchaseInvoiceDetail.setId(detailDTO.getId());
                    dao.deleteEntity(invPurchaseInvoiceDetail);
                }
            }
        }

    }

    @Override
    public String addInvPurchaseInvoiceDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, invInvoiceDTO.getInvInventoryId().getId(), invInvoiceDTO.getScreenName(),
                    invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), (invInvoiceDTO.getLoadedSerial() != null),
                    invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getSupplierInvoiceNumber());
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvPurchaseInvoiceDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), isagUser);
                if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(invInvoiceDTO.getScreenName())) {
                    if (!invInvoiceDTO.getInventorySetupDTO().getNegativeSell()) {
                        BigDecimal quantityItemsStoreByDate = findQuantityItemByDate(invInvoiceDTO, detailDTO);
                        detail.setCostAvarage(updateCostAvarage(detailDTO, quantityItemsStoreByDate));
                    }
                } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(invInvoiceDTO.getScreenName())) {
                    detail.setCostAvarage(findCostAvarageForItemByDate(invInvoiceDTO.getInvInventoryId().getId(), invInvoiceDTO.getBranchId(), detail.getItemId().getId(), invInvoiceDTO.getDate()));
                }
                dao.updateEntity(detail);
            }
        }

        return error;
    }

    public BigDecimal updateCostAvarage(InvDetailDTO invDetailDTO, BigDecimal quantityItemsStoreByDate) {
        InvItem invItem = invItemService.findInvItem(invDetailDTO.getItemsBarcodesDetail().getItemId());
        BigDecimal costAvarageDetail = invDetailDTO.getNet().divide(((invDetailDTO.getScrewing() == null ? BigDecimal.ONE : invDetailDTO.getScrewing()).multiply(invDetailDTO.getQuantity())), 2, RoundingMode.HALF_UP).setScale(3, BigDecimal.ROUND_UP);
        if (invItem.getCostAverage() == null) {
            invItem.setCostAverage(costAvarageDetail);
        } else {
            BigDecimal allQuantity = (invDetailDTO.getScrewing() == null ? BigDecimal.ONE : invDetailDTO.getScrewing()).multiply(invDetailDTO.getQuantity());
            BigDecimal oldValue = invItem.getCostAverage().multiply(quantityItemsStoreByDate);
            BigDecimal newValue = costAvarageDetail.multiply(allQuantity);
            BigDecimal allAmount = quantityItemsStoreByDate.add(allQuantity);
            if (allAmount.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal costAvarage = (oldValue.add(newValue)).divide(allAmount, 2, RoundingMode.HALF_UP);
                invItem.setCostAverage(costAvarage);
            }
        }
        invItemService.updateInvItem(invItem);
        return invItem.getCostAverage();
    }

    private BigDecimal findQuantityItemByDate(InvInvoiceDTO invPurchaseInvoiceDTO, InvDetailDTO invDetailDTO) {
        BigDecimal quantityItemsStoreByDate;
        quantityItemsStoreByDate = quantityItemsStoreByDateService.getQuantatyItemByinventory(invPurchaseInvoiceDTO.getInvInventoryId().getId(),
                invDetailDTO.getItemsBarcodesDetail().getItemId(), invPurchaseInvoiceDTO.getInventorySetupDTO().getSellBuy(), invPurchaseInvoiceDTO.getDate());
        return quantityItemsStoreByDate;
    }

    public synchronized BigDecimal findCostAvarageForItemByDate(Integer inventoryId, Integer branchId, Integer itemId, Date date) {
        Map<String, Object> params = new HashMap();
        Map<String, Object> params1 = new HashMap();
//        params.put("inventoryId", inventoryId);
        params.put("branchId", branchId);
        params.put("itemId", itemId);
        params.put("date", date);
        StringBuilder sql = new StringBuilder();
        List<BigDecimal> itemsStores = null;
        sql.append("SELECT ipid.costAvarage FROM InvPurchaseInvoiceDetail ipid LEFT JOIN InvPurchaseInvoice ipi WHERE ipid.invPurchaseInvoiceId.id = ipi.id  AND  ipi.branchId.id = :branchId and ipi.type = false AND ipid.itemId.id = :itemId AND ipi.date <= :date order by ipi.date DESC");
//        sql.append("SELECT ipid.costAvarage FROM InvPurchaseInvoiceDetail ipid LEFT JOIN InvPurchaseInvoice ipi WHERE ipid.invPurchaseInvoiceId.id = ipi.id  AND ipi.invInventoryId.id = :inventoryId AND ipi.branchId.id = :branchId and ipi.type = false AND ipid.itemId.id = :itemId AND ipi.date <= :date order by ipi.date DESC");
//        sql.append("SELECT i.costAverage FROM InvItem i where i.id = :itemId ");

        itemsStores = dao.findListByQuery(sql.toString(), params);

        if (itemsStores == null || itemsStores.isEmpty()) {
            params1.put("itemId", itemId);
            StringBuilder query = new StringBuilder();
            query.append("SELECT i.cost FROM InvOpenningBalanceItemDetail i where i.itemId.id = :itemId ");
            itemsStores = dao.findListByQuery(query.toString(), params1);
        }

        return (itemsStores != null && itemsStores.size() > 0) ? itemsStores.get(0) : null;
    }

    @Override
    public BigDecimal findLastCost(Integer itemId) {

        List<BigDecimal> lastCost = dao.executeNativeQuery("select cost from isag.inv_purchase_invoice_detail ipid left join inv_purchase_invoice ipi \n"
                + "on ipi.id = ipid.inv_purchase_invoice_id where  ipi.type = 0 and  ipid.item_id =" + itemId + " order by ipid.id desc limit 1");
        if (lastCost != null && !lastCost.isEmpty()) {
            return lastCost.get(0);
        } else {
            return BigDecimal.ZERO;
        }
    }

}

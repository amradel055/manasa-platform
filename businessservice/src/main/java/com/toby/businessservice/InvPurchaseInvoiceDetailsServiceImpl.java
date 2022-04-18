/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.API.InvDetailsDTOAPI;
import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.core.storedProcedureParameter;
import com.toby.define.ModuleClassEnum;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.balanceGalary;
import com.toby.entity.GlYear;
import com.toby.entity.InvAddingorderDetail;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.InvStripDetail;
import com.toby.entity.InvTransferDetail;
import com.toby.entity.InvUpdate;
import com.toby.entity.InvUpdateDetail;
import com.toby.entity.InventorySetup;
import com.toby.entity.ItemOfferDetail;
import com.toby.entity.ProFactoryDelivery;
import com.toby.entity.Symbol;
import com.toby.procedure.InvoiceNotLoadedYetProFactoeyProcedure;
import com.toby.procedure.TempInvPurchaseInvoiceNotLoadedYet;
import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @EJB
    ProGlobalService proGlobalService;
    @EJB
    private InvPurchaseInvoiceDetailDetailService invPurchaseInvoiceDetailDetailService;

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

    @Override
    public BigDecimal getNumberOftoob(Integer id) {
        List<BigDecimal> purchaseInvoiceDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        purchaseInvoiceDetails = dao.findListByQuery(" SELECT sum(ipid.number) from InvPurchaseInvoiceDetail  ipid where ipid.invPurchaseInvoiceId.id = :id AND ( ipid.itemId.proGroupId.id = 1 OR ipid.itemId.proGroupId.id = 5)", params);
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

    private void invoiceNotLoadedYetProFactoeyProcedure(Integer inv_purchase_order_id1, Integer type1) {
        List<storedProcedureParameter> procedureParameter = new ArrayList<>();
        procedureParameter.add(new storedProcedureParameter("inv_purchase_invoice_id1", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("type1", Integer.class, 'i'));
        Map<String, Object> params2 = new HashMap<>();
        params2.put("inv_purchase_invoice_id1", inv_purchase_order_id1);
        params2.put("type1", type1);
        dao.StoredProcedure("invoice_not_loaded_yet_Pro_Factoey_procedure", procedureParameter, params2);
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
            if (ModuleClassEnum.PRODUCTION.equals(dto.getModuleClassEnum())) {
//                Map<String, Object> params = new HashMap<>();
//                params.put("id", dto.getId());
//                List<InvPurchaseInvoiceDetail> list = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id = :id AND "
//                        + "e.id not in (select i.invPurchaseInvoiceDetailId.id from ProFactoryDeliveryDetail i where i.proFactoryDeliveryId.invPurchaseInvoiceId.id = :id)", params);
//                invDetailDTOList = returnListDTO(list, dto);

                invoiceNotLoadedYetProFactoeyProcedure(dto.getId(), 0);
                List<InvoiceNotLoadedYetProFactoeyProcedure> list = dao.findListByQuery("SELECT t FROM InvoiceNotLoadedYetProFactoeyProcedure t");
                invDetailDTOList = returnListDTO(list, dto);

            } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(dto.getScreenName())) {
                Map<String, Object> params = new HashMap<>();
                params.put("id", dto.getId());
                params.put("proFactoryDeliveryId", dto.getProFactoryDeliveryId());
                List<InvPurchaseInvoiceDetail> list = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id = :id AND "
                        + "e.id in (select i.invPurchaseInvoiceDetailId.id from ProFactoryDeliveryDetail i where i.proFactoryDeliveryId.invPurchaseInvoiceId.id = :id AND i.proFactoryDeliveryId.id = :proFactoryDeliveryId)", params);
                invDetailDTOList = returnListDTO(list, dto);
            } else {
                if (ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(dto.getScreenName())) {
                    invPurchaseInvoiceNotLoadedYetProcedure(dto.getId(), 2);
                } else if (ScreenNameClassEnum.INVRETURNSALESFORM.equals(dto.getScreenName())
                        || ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(dto.getScreenName())) {
                    invPurchaseInvoiceNotLoadedYetProcedure(dto.getId(), 3);
                }
                List<TempInvPurchaseInvoiceNotLoadedYet> list = dao.findListByQuery("SELECT t FROM TempInvPurchaseInvoiceNotLoadedYet t");
                invDetailDTOList = returnListDTO(list, dto);
            }

        } else {
            invDetailDTOList = getAllInvPurchaseInvoiceDetailList(dto);
        }

        return invDetailDTOList;
    }

    @Override
    public List<InvDetailDTO> getAllInvPurchaseInvoiceDetailList(InvInvoiceDTO dto) {
        List<InvPurchaseInvoiceDetail> details = null;
        List<InvDetailDTO> invDetailDTOList;
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", dto.getId());
        if (ScreenNameClassEnum.PROFACTORYDELIVERY.equals(dto.getScreenName()) && dto.getProof()) {
            List<ProFactoryDelivery> proDetails = dao.findListByQuery("Select p from ProFactoryDelivery p where p.invPurchaseInvoiceId.id = :invPurchaseInvoiceId", params);
            if (proDetails != null && !proDetails.isEmpty()) {
                details = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id = :invPurchaseInvoiceId", params);
            } else {
                details = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id = :invPurchaseInvoiceId AND e.proof = 1", params);
            }
        } else {
            details = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id = :invPurchaseInvoiceId", params);
        }
        invDetailDTOList = returnListDTO(details, dto);
        return invDetailDTOList;
    }

    @Override
    public List<InvDetailsDTOAPI> getAllInvPurchaseInvoiceDetailListWebSite(InvInvoiceDTO dto) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", dto.getId());
        List<InvPurchaseInvoiceDetail> details = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id = :invPurchaseInvoiceId", params);
        return mapToDTOAPIList(details);
    }

    public List<InvDetailsDTOAPI> mapToDTOAPIList(List<InvPurchaseInvoiceDetail> detail) {
        List<InvDetailsDTOAPI> aPIs = new ArrayList<>();
        for (InvPurchaseInvoiceDetail dTO : detail) {
            InvDetailsDTOAPI detailsDTOAPI = mapToDTOAPI(dTO);
            aPIs.add(detailsDTOAPI);
        }
        return aPIs;
    }

    public InvDetailsDTOAPI mapToDTOAPI(InvPurchaseInvoiceDetail detail) {
        InvDetailsDTOAPI api = new InvDetailsDTOAPI();
        api.setId(detail.getItemId().getId());
        api.setCode(detail.getItemId().getCode());
        api.setName(detail.getItemId().getName());
        api.setPrice(detail.getCost());
        api.setImage(detail.getItemId().getImage());
        if (detail.getItemId().getGroupId() != null) {
            api.setGroupId(detail.getItemId().getGroupId().getId());
        }
        return api;
    }

    private <T> List<InvDetailDTO> returnListDTO(List<T> list, InvInvoiceDTO invInvoiceDTO) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = findItemBarcodeDetails(list, invInvoiceDTO);

        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        if (ModuleClassEnum.PRODUCTION.equals(invInvoiceDTO.getModuleClassEnum())) {
            for (T entity : list) {
                dTOList.add(mapTODTO(entity, itemsBarcodeMap, invInvoiceDTO));
            }
        } else {

            for (T entity : list) {
                dTOList.add(mapTODTO(entity, itemsBarcodeMap, invInvoiceDTO));
            }
        }
        return dTOList;
    }

//    private <T> List<ItemsBarcodesDetailsView> findItemBarcodeDetails(List<T> list, InvInvoiceDTO invInvoiceDTO) {
//        StringBuilder builder = new StringBuilder();
//        builder.append(" 0 ");
//        List<String> barcodeList = new ArrayList<>();
//        for (T Entity : list) {
//                if (Entity.getClass().equals(InvPurchaseInvoiceDetail.class)) {
//                    InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) Entity;
//                    builder.append(" , ").append(entity.getItemBarcode());
//                    barcodeList.add(entity.getItemBarcode());
//                } else if (Entity.getClass().equals(TempInvPurchaseInvoiceNotLoadedYet.class)) {
//                    TempInvPurchaseInvoiceNotLoadedYet entity = (TempInvPurchaseInvoiceNotLoadedYet) Entity;
//                    builder.append(" , ").append(entity.getItemBarcode());
//                    barcodeList.add(entity.getItemBarcode());
//                }
//        }
//        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdByBarcode(invInvoiceDTO.getBranchId(), builder,barcodeList);
//        return itemsBarcodesDetailsViewList;
//    }
    private <T> List<ItemsBarcodesDetailsView> findItemBarcodeDetails(List<T> list, InvInvoiceDTO invInvoiceDTO) {
        List<String> barcodeList = new ArrayList<>();
        for (T Entity : list) {
            if (Entity.getClass().equals(InvPurchaseInvoiceDetail.class)) {
                InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) Entity;
                barcodeList.add(entity.getItemBarcode());
            } else if (Entity.getClass().equals(TempInvPurchaseInvoiceNotLoadedYet.class)) {
                TempInvPurchaseInvoiceNotLoadedYet entity = (TempInvPurchaseInvoiceNotLoadedYet) Entity;
                barcodeList.add(entity.getItemBarcode());
            } else if (Entity.getClass().equals(InvoiceNotLoadedYetProFactoeyProcedure.class)) {
                InvoiceNotLoadedYetProFactoeyProcedure entity = (InvoiceNotLoadedYetProFactoeyProcedure) Entity;
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
        if (ModuleClassEnum.PRODUCTION.equals(invInvoiceDTO.getModuleClassEnum())) {
            if (Entity.getClass().equals(InvPurchaseInvoiceDetail.class)) {
                InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) Entity;
                dTO = proGlobalService.fillItemDetail(invInvoiceDTO.getIsOld(), dTO,
                        entity.getId(), entity.getId(), entity.getSerial(), null,
                        null,
                        entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                        entity.getItemId().getId(), entity.getItemId().getCode(), entity.getItemId().getName(), entity.getClothNumber(),
                        entity.getItemId().getUnitId().getSerial(), entity.getItemId().getUnitId().getName(),
                        entity.getNumber(),
                        entity.getInvItemParentId() == null ? null : entity.getInvItemParentId().getId(),
                        entity.getInvItemParentId() == null ? null : entity.getInvItemParentId().getCode(),
                        entity.getInvItemParentId() == null ? null : entity.getInvItemParentId().getName(),
                        invInvoiceDTO.getCompanyActivity(), invInvoiceDTO.getScreenName(), entity.getCostAvarage());
            } else if (Entity.getClass().equals(InvoiceNotLoadedYetProFactoeyProcedure.class)) {
                InvoiceNotLoadedYetProFactoeyProcedure entity = (InvoiceNotLoadedYetProFactoeyProcedure) Entity;

                dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                        null, null, null, null,
                        null, null, null, null,
                        null, null, entity.getItemId(), null, null,
                        null, null,
                        null,
                        null, null,
                        entity.getItemBarcode(), null, entity.getItemId(), null,
                        null, null, null, null,
                        entity.getNumber(), null, null, null,
                        invInvoiceDTO.getCompanyActivity(), invInvoiceDTO.getScreenName(), entity.getDetailId(), BigDecimal.ZERO);

            }
        } else {
            if (Entity.getClass().equals(InvPurchaseInvoiceDetail.class)) {
                InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) Entity;
                dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                        entity.getId(), entity.getSerial(), (entity.getBranchId() != null ? entity.getBranchId().getId() : null), entity.getCost(),
                        entity.getDiscount(), entity.getQuantity(), entity.getDiscountType(), (entity.getInvPurchaseInvoiceId() != null ? entity.getInvPurchaseInvoiceId().getId() : null),
                        entity.getExtraCost(), entity.getNet(), entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                        entity.getClothNumber(), entity.getInvItemParentId() == null ? null : entity.getInvItemParentId().getId(),
                        entity.getInvItemParentId() == null ? null : entity.getInvItemParentId().getCode(),
                        entity.getInvItemParentId() == null ? null : entity.getInvItemParentId().getName(), entity.getProof(),
                        entity.getItemBarcode(), entity.getScrewing(), entity.getItemId().getId(), entity.getWeight(),
                        entity.getUnitId().getId(), entity.getUnitId().getSerial(), entity.getUnitId().getName(),
                        entity.getItemId().getIsInventoryItem(),
                        entity.getNumber(), entity.getQuantityOfOneUnit(), entity.getInvInventoryId(), entity.getItemId(),
                        invInvoiceDTO.getCompanyActivity(), invInvoiceDTO.getScreenName(), null, entity.getCostAvarage());
//                dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true,false);
//                dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
                dTO.setDiscountValue(entity.getDiscountValue());
                dTO = invPurchaseInvoiceDetailDetailService.findDetailDetail(dTO, invInvoiceDTO.getScreenName());

            } else if (Entity.getClass().equals(TempInvPurchaseInvoiceNotLoadedYet.class)) {
                TempInvPurchaseInvoiceNotLoadedYet entity = (TempInvPurchaseInvoiceNotLoadedYet) Entity;

                dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                        null, entity.getSerial(), null, entity.getCost(),
                        entity.getDiscount(), entity.getQuantity(), entity.getDiscountType(), null,
                        entity.getExtraCost(), entity.getNet(), entity.getItemId(), null, null,
                        null, null,
                        null,
                        null, null,
                        entity.getItemBarcode(), entity.getScrewing(), entity.getItemId(), entity.getWeight(),
                        entity.getUnitId(), entity.getUnitSerial(), entity.getUnitName(), entity.getIsInventoryItem(),
                        null, null, null, null,
                        invInvoiceDTO.getCompanyActivity(), invInvoiceDTO.getScreenName(), null, entity.getCostAvarage());

                dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true, false);
                dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
            }
        }
        return dTO;
    }

    private InvPurchaseInvoiceDetail mapToEntity(InvDetailDTO dTO, Integer invPurchaseInvoiceId, Integer discountType, BigDecimal discount, BigDecimal finalTotal, TobyUser tobyUser) {
        InvPurchaseInvoiceDetail entity = new InvPurchaseInvoiceDetail();

        entity.setSerial(dTO.getSerial());
        entity.setCost(dTO.getCost().setScale(3, BigDecimal.ROUND_UP));
        entity.setCostAvarage(dTO.getCostAvarage());
        entity.setDiscount(dTO.getDiscount());
        entity.setDiscountType(dTO.getDiscountType());
        entity.setDiscountValue(dTO.getDiscountValue());
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
        entity.setQuantityOfOneUnit(dTO.getQuantityOfOneUnit());
        entity.setNumber(dTO.getNumber());
        entity.setClothNumber(dTO.getClothNumber());
        entity.setBounse(dTO.getBounse());
        entity.setProof(dTO.getProof() ? 1 : 0);

        if (discount.compareTo(BigDecimal.ZERO) == 1) {
            if (discountType == 1) {
                entity.setNetAfterDiscountHead(dTO.getNet().subtract(dTO.getNet().multiply((discount.divide(new BigDecimal(100))))));
            } else {
                BigDecimal percentage = discount.divide(finalTotal, 5, RoundingMode.HALF_DOWN);
                entity.setNetAfterDiscountHead(dTO.getNet().subtract(dTO.getNet().multiply((percentage))));
            }
        } else {
            entity.setNetAfterDiscountHead(dTO.getNet());
        }

        if (invPurchaseInvoiceId != null) {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(invPurchaseInvoiceId);
            entity.setInvPurchaseInvoiceId(invPurchaseInvoice);
        }

        if (dTO.getInvItemParentId() != null && dTO.getInvItemParentId().getId() != null) {
            InvItem item = new InvItem();
            item.setId(dTO.getInvItemParentId().getId());
            entity.setInvItemParentId(item);
        }
        if (dTO.getInventoryDTO() != null && dTO.getInventoryDTO().getId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(dTO.getInventoryDTO().getId());
            entity.setInvInventoryId(invInventory);
        }

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
    public <T> List<ItemsBarcodesDetailsView> findItemBarcodeDetails(List<T> list, Integer branchId) {
        List<String> barcodeList = new ArrayList<>();
        for (T Entity : list) {
            if (Entity.getClass().equals(InvPurchaseInvoiceDetail.class)) {
                InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) Entity;
                barcodeList.add(entity.getItemBarcode());
            } else if (Entity.getClass().equals(ItemOfferDetail.class)) {
                ItemOfferDetail entity = (ItemOfferDetail) Entity;
                barcodeList.add(entity.getItemBarcode());
            } else if (Entity.getClass().equals(TempInvPurchaseInvoiceNotLoadedYet.class)) {
                TempInvPurchaseInvoiceNotLoadedYet entity = (TempInvPurchaseInvoiceNotLoadedYet) Entity;
                barcodeList.add(entity.getItemBarcode());
            } else if (Entity.getClass().equals(InvAddingorderDetail.class)) {
                InvAddingorderDetail entity = (InvAddingorderDetail) Entity;
                barcodeList.add(entity.getItemBarcode());
            } else if (Entity.getClass().equals(InvTransferDetail.class)) {
                InvTransferDetail entity = (InvTransferDetail) Entity;
                barcodeList.add(entity.getItemBarcode());
            } else if (Entity.getClass().equals(InvUpdateDetail.class)) {
                InvUpdateDetail entity = (InvUpdateDetail) Entity;
                barcodeList.add(entity.getItemBarcode());
            } else if (Entity.getClass().equals(InvStripDetail.class)) {
                InvStripDetail entity = (InvStripDetail) Entity;
                barcodeList.add(entity.getItemBarcode());
            }

        }
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdByBarcode(branchId, barcodeList);
        return itemsBarcodesDetailsViewList;
    }

    @Override
    public List<InvDetailDTO> addInvPurchaseInvoiceDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {
        String error = null;
        List<InvDetailDTO> list = new ArrayList<>();
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, invInvoiceDTO.getInvInventoryId().getId(),
                    invInvoiceDTO.getScreenName(), invInvoiceDTO.getInventorySetupDTO().getNegativeSell(),
                    (invInvoiceDTO.getLoadedSerial() != null), invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getCompanyActivity());
            if (error != null && !error.isEmpty()) {
                InvDetailDTO dTO = new InvDetailDTO();
                dTO.setMsg(error);
                list.add(dTO);
                return list;
            }
            InvPurchaseInvoiceDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), invInvoiceDTO.getDiscountType(), invInvoiceDTO.getDiscount(), invInvoiceDTO.getTotalNet(), tobyUser);
                if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(invInvoiceDTO.getScreenName())) {
                    if (!invInvoiceDTO.getInventorySetupDTO().getNegativeSell()) {
                        BigDecimal quantityItemsStoreByDate = findQuantityItemByDate(invInvoiceDTO, detailDTO);
                        detail.setCostAvarage(updateCostAvarage(detailDTO, quantityItemsStoreByDate));
                    }
                } else if (ScreenNameClassEnum.PROWORKORDER.equals(invInvoiceDTO.getScreenName())) {
                    detail.setCostAvarage(findCostAvarageForItemByDate(invInvoiceDTO.getInvInventoryId().getId(), invInvoiceDTO.getBranchId(), detail.getItemId().getId(), invInvoiceDTO.getDate()));
                }

                detail = dao.updateEntity(detail);
                detailDTO.setId(detail.getId());
                detailDTO.setIndex(detail.getId());
                detailDTO.setSerial(detail.getSerial());
                list.add(detailDTO);
                if (detailDTO.getProDetailTypeDTOList() != null && !detailDTO.getProDetailTypeDTOList().isEmpty()) {
                    invPurchaseInvoiceDetailDetailService.save(detailDTO, tobyUser);
                }
            }
        }

        return list;
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
//        BigDecimal quantityItemsStoreByDate;
//        quantityItemsStoreByDate = quantityItemsStoreByDateService.findInventoryItem(invPurchaseInvoiceDTO.getInvInventoryId().getId(),
//                invPurchaseInvoiceDTO.getBranchId(), invDetailDTO.getItemsBarcodesDetail().getItemId(), invPurchaseInvoiceDTO.getInventorySetupDTO().getSellBuy(), invPurchaseInvoiceDTO.getDate());
//        return quantityItemsStoreByDate;
        BigDecimal quantityItemsStoreByDate;
        quantityItemsStoreByDate = quantityItemsStoreByDateService.getQuantatyItemByinventory(invPurchaseInvoiceDTO.getInvInventoryId().getId(),
                invDetailDTO.getItemsBarcodesDetail().getItemId(), invPurchaseInvoiceDTO.getInventorySetupDTO().getSellBuy(), invPurchaseInvoiceDTO.getDate());
        return quantityItemsStoreByDate;
    }

    public synchronized BigDecimal findCostAvarageForItemByDate(Integer inventoryId, Integer branchId, Integer itemId, Date date) {
        Map<String, Object> params = new HashMap();
//        params.put("inventoryId", inventoryId);
//        params.put("branchId", branchId);
        params.put("itemId", itemId);
//        params.put("date", date);
        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT ipid.costAvarage FROM InvPurchaseInvoiceDetail ipid LEFT JOIN InvPurchaseInvoice ipi WHERE ipid.invPurchaseInvoiceId.id = ipi.id  AND ipi.invInventoryId.id = :inventoryId AND ipi.branchId.id = :branchId and ipi.type = false AND ipid.itemId.id = :itemId AND ipi.date <= :date order by ipi.date DESC");
        sql.append("SELECT i.costAverage FROM InvItem i where i.id = :itemId ");

        List<BigDecimal> itemsStores = dao.findListByQuery(sql.toString(), params);

        return (itemsStores != null && itemsStores.size() > 0) ? itemsStores.get(0) : null;
    }

    @Override
    public BigDecimal findLastCost(Integer itemId) {

        List<BigDecimal> lastCost = dao.executeNativeQuery("select cost from inv_purchase_invoice_detail ipid left join inv_purchase_invoice ipi \n"
                + "on ipi.id = ipid.inv_purchase_invoice_id where  ipi.type = 0 and  ipid.item_id =" + itemId + " order by ipid.id desc limit 1");
        if (lastCost != null && !lastCost.isEmpty()) {
            return lastCost.get(0);
        } else {
            lastCost = dao.executeNativeQuery("select last_cost from inv_item ii where id = " + itemId);
            if (lastCost != null && !lastCost.isEmpty()) {
                return lastCost.get(0);
            } else {
                return BigDecimal.ZERO;
            }
        }
    }

    @Override
    public List<InvDetailDTO> addInvPurchaseInvoiceDetailsDTOOne(List<InvDetailDTO> invPurchaseInvoiceDetailList, Integer headId, TobyUser tobyUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<balanceGalary> UnPaidAmountAfterSearchNative(Integer branchId, Date dateFrom1, Date dateTo1, String stringBuilder, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        String dateFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(dateFrom1);
        String dateTo = new SimpleDateFormat("yyyy-MM-dd  23:59:59").format(dateTo1);
        List<balanceGalary> galarysLIST = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("select name , sum(value) from (select ii.name as name , sum(net_after_discount_head)  as value from inv_purchase_invoice_detail ipid left join inv_purchase_invoice ipi on ipi.id = ipid.inv_purchase_invoice_id left join inv_inventory ii on ii.id = ipi.galary_Id where ipi.branch_id = "+branchId+" and ii.id in (").append(stringBuilder).append(") and ipi.date >= '").append(dateFrom).append("' and ipi.date <= '").append(dateTo).append("' group by ii.id union all select ii.name as name ,(sum(value) * -1) as value from gl_bank_transaction gbt left join inv_purchase_invoice ipi on gbt.invoice_id = ipi.id left join inv_inventory ii on ii.id = ipi.galary_Id where ipi.branch_id = "+branchId+" and ii.id in (").append(stringBuilder).append(") and ipi.date >= '").append(dateFrom).append("' and ipi.date <= '").append(dateTo).append("' group by ii.id union all select ii.name as name ,(sum(value) * -1) as value from gl_bank_transaction_detail gbt left join inv_purchase_invoice ipi on gbt.invoice_id = ipi.id left join inv_inventory ii on ii.id = ipi.galary_Id where ipi.branch_id = "+branchId+" and ii.id in (").append(stringBuilder).append(") and ipi.date >= '").append(dateFrom).append("' and ipi.date <= '").append(dateTo).append("' group by ii.id ) s group by name");

        List<Object[]> res = dao.executeNativeQuery(builder.toString());
        for (Object[] object : res) {
            balanceGalary galary = new balanceGalary();
            galary.setGallaryName((String) object[0]);
            galary.setValue((BigDecimal) object[1]);
            galarysLIST.add(galary);
        }

        return galarysLIST;

    }

}

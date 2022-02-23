/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invaddingorder;

import com.isag.businessservice.InvAddingOrderDetailsService;
import com.isag.businessservice.InvAddingOrderService;
import com.isag.businessservice.InvAddingOrderServiceImpl;
import com.isag.businessservice.InvPurchaseOrderDetailsService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.QuantityItemsStoreService;
import com.isag.converter.InvPurchaseOrderDetailConverter;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.InventorySetup;
import com.isag.entity.Symbol;
import com.isag.entiy.InvAddingOrderDetailsEntity;
import com.isag.entiy.InvAddingOrderEntity;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.views.PurchaseOrderNotLoadedFromInvAddingOrder;
import com.isag.views.QuantityItemsStore;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author amr
 */
public class InvAddingOrderParent implements Serializable {

    BigDecimal hundred = new BigDecimal(100);

    IReportService iReportService = new IReportServiceImpl();

    private InvAddingOrderService invAddingOrderService;

    private InvAddingOrderDetailsService invAddingorderDetailsService;

    private QuantityItemsStoreService quantityItemsStoreService;

    private InvPurchaseOrderDetailsService invPurchaseOrderDetailsService;

    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;

    public InvAddingOrderParent(InvAddingOrderService invAddingOrderService, ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService,
            InvAddingOrderDetailsService invAddingorderDetailsService,
            QuantityItemsStoreService quantityItemsStoreService,
            InvPurchaseOrderDetailsService invPurchaseOrderDetailsService) {
        this.invAddingOrderService = invAddingOrderService;
        this.invAddingorderDetailsService = invAddingorderDetailsService;
        this.quantityItemsStoreService = quantityItemsStoreService;
        this.invPurchaseOrderDetailsService = invPurchaseOrderDetailsService;
        this.itemsBarcodesDetailsViewService = itemsBarcodesDetailsViewService;
    }

    public void editInvAddingOrder(Integer invAddingOrderId, InvAddingorder invAddingOrder,
            List<InvAddingorderDetail> invAddingorderDetailList,
            InvAddingOrderEntity invAddingOrderEntity,
            InvAddingOrderEntity invAddingOrderEntityRetrieved, InvAddingOrderDetailsEntity invAddingOrderDetailsEntity,
            Boolean isPermission, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, BigDecimal totalQuatity,
            List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityList,
            List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityListFirstBackup,
            StringBuilder headIdList,
            InventorySetup invSetup,
            Map<Integer, BigDecimal> itemsMap,
            Integer branchId, List<InvPurchaseOrderDetail> invPurchaseOrdersDetailList,
            InvPurchaseOrderDetailConverter invPurchaseOrderDetailConverter) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        invAddingOrderId = (Integer) context.getSessionMap().get("invAddingOrderSelected");
        invAddingOrder = invAddingOrderService.findInvAddingOrderByInvAddingOrderId(invAddingOrderId);

        invAddingorderDetailList = invAddingorderDetailsService.getAllInvAddingOrderDetailsByInvAddingOrderId(invAddingOrder.getId());

        invAddingOrderEntity = mapInvAddingOrderToInvAddingOrderEntity(invAddingOrder,
                invAddingorderDetailList,
                invAddingOrderEntity,
                invAddingOrderEntityRetrieved,
                invAddingOrderDetailsEntity,
                isPermission, itemsBarcodeMap,
                totalQuatity,
                invAddingOrderDetailsEntityList,
                invAddingOrderDetailsEntityListFirstBackup,
                headIdList);

        if (invAddingOrder.getPurchaseOrderId() != null) {
            initPurchaseOrder(invPurchaseOrdersDetailList, invAddingOrderEntity, invPurchaseOrderDetailConverter,
                    invAddingOrderDetailsEntity, invAddingOrderDetailsEntityList);
        }

        fillItemMap(invAddingOrderEntity, invAddingOrderDetailsEntityList, isPermission, invSetup, itemsMap, branchId);
    }

    public InvAddingOrderEntity mapInvAddingOrderToInvAddingOrderEntity(InvAddingorder invAddingOrder,
            List<InvAddingorderDetail> invAddingorderDetailList,
            InvAddingOrderEntity invAddingOrderEntity,
            InvAddingOrderEntity invAddingOrderEntityRetrieved,
            InvAddingOrderDetailsEntity invAddingOrderDetailsEntity,
            Boolean isPermission,
            Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap,
            BigDecimal totalQuatity,
            List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityList,
            List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityListFirstBackup,
            StringBuilder headIdList) {

        invAddingOrderEntityRetrieved = new InvAddingOrderEntity();
        // invAddingOrderEntityRetrieved.setBranchId(invAddingOrder.getBranchId() != null ? invAddingOrder.getBranchId().getId() : null);
        invAddingOrderEntityRetrieved.setDate(invAddingOrder.getDate());
        invAddingOrderEntityRetrieved.setId(invAddingOrder.getId());
        invAddingOrderEntityRetrieved.setSerial(invAddingOrder.getSerial());
        invAddingOrderEntityRetrieved.setMarkEdit(invAddingOrder.getMarkEdit());
        invAddingOrderEntityRetrieved.setSupplierId(invAddingOrder.getOrganizationSiteId());
        invAddingOrderEntityRetrieved.setPostFlag(invAddingOrder.getPostFlag() != null ? invAddingOrder.getPostFlag() : 0);
        invAddingOrderEntityRetrieved.setPurchaseOrderId(invAddingOrder.getPurchaseOrderId());
        invAddingOrderEntityRetrieved.setRemark(invAddingOrder.getRemark());
        invAddingOrderEntityRetrieved.setSupplierDate(invAddingOrder.getSupplierDate());
        invAddingOrderEntityRetrieved.setSupplierInvoice(invAddingOrder.getSupplierInvoice());
        invAddingOrderEntityRetrieved.setInvInventory(invAddingOrder.getInvInventoryId());
        if (invAddingOrder.getPurchaseOrderId() != null) {
            PurchaseOrderNotLoadedFromInvAddingOrder notLoadedFromInvAddingOrder = new PurchaseOrderNotLoadedFromInvAddingOrder();
            notLoadedFromInvAddingOrder.setBranchId(invAddingOrder.getPurchaseOrderId().getBranchId().getId());
            notLoadedFromInvAddingOrder.setPurchaseId(invAddingOrder.getPurchaseOrderId().getId());
            notLoadedFromInvAddingOrder.setSerial(invAddingOrder.getPurchaseOrderId().getSerial());
            loadDefaultInvPurchaseOrderData(invAddingOrderEntity);
            invAddingOrderEntityRetrieved.setPurchaseOrderNLoaded(notLoadedFromInvAddingOrder);
        }

        for (InvAddingorderDetail invAddingorderDetail : invAddingorderDetailList) {
            invAddingOrderDetailsEntity = new InvAddingOrderDetailsEntity();
            invAddingOrderDetailsEntity.setBranchId(invAddingorderDetail.getBranchId() != null ? invAddingorderDetail.getBranchId().getId() : null);
            invAddingOrderDetailsEntity.setId(invAddingorderDetail.getId());
            invAddingOrderDetailsEntity.setSerial(invAddingorderDetail.getSerial());
            invAddingOrderDetailsEntity.setInvAddingOrderId(invAddingorderDetail.getAddingorderId().getId());
            invAddingOrderDetailsEntity.setItemDataId(invAddingorderDetail.getItemId() != null ? invAddingorderDetail.getItemId() : null);
            invAddingOrderDetailsEntity.setItemId(invAddingorderDetail.getItemId() != null ? invAddingorderDetail.getItemId() : null);

            invAddingOrderDetailsEntity.setItem(invAddingorderDetail.getItemId().getName() != null ? invAddingorderDetail.getItemId().getName() : "");
            invAddingOrderDetailsEntity.setMarkEdit(invAddingorderDetail.getMarkEdit());
            invAddingOrderDetailsEntity.setQuantity(invAddingorderDetail.getQuantity());
            invAddingOrderDetailsEntity.setPurchaseOrderDetail(invAddingorderDetail.getSelectedPurchaseOrderId());
            if (isPermission) {
                invAddingOrderDetailsEntity.setMainQuantity(invAddingorderDetail.getQuantity().add(invAddingorderDetail.getSelectedId() != null ? invAddingorderDetail.getSelectedId().getFinalQuantity() : BigDecimal.ZERO));
            } else {
                invAddingOrderDetailsEntity.setMainQuantity(invAddingorderDetail.getQuantity().add(invAddingorderDetail.getSelectedPurchaseId() != null ? invAddingorderDetail.getSelectedPurchaseId().getFinalQuantity() : BigDecimal.ZERO));
            }

            invAddingOrderDetailsEntity.setCreatedBy(invAddingorderDetail.getCreatedBy());
            invAddingOrderDetailsEntity.setCreationDate(invAddingorderDetail.getCreationDate());
            invAddingOrderDetailsEntity.setScrewing(invAddingorderDetail.getScrewing() == null ? BigDecimal.ONE : invAddingorderDetail.getScrewing());
            if (isPermission) {
                invAddingOrderDetailsEntity.setSelectedId(invAddingorderDetail.getSelectedId());
            } else {
                invAddingOrderDetailsEntity.setSelectedPurchaseId(invAddingorderDetail.getSelectedPurchaseId());
            }

            if (invAddingorderDetail.getItemBarcode() != null && !"".equals(invAddingorderDetail.getItemBarcode())) {
                invAddingOrderDetailsEntity.setItemsBarcodesDetail(itemsBarcodeMap.get(invAddingorderDetail.getItemBarcode()));
                invAddingOrderDetailsEntity.setItemsBarcodesDetailTrans(invAddingOrderDetailsEntity.getItemsBarcodesDetail());
                invAddingOrderDetailsEntity.setUnitName(invAddingOrderDetailsEntity.getItemsBarcodesDetail().getUnitName());
            }
            invAddingOrderDetailsEntity.setTransferFrom(0);
            totalQuatity = totalQuatity.add(invAddingorderDetail.getQuantity() != null ? invAddingorderDetail.getQuantity() : BigDecimal.ZERO);
            invAddingOrderDetailsEntityList.add(invAddingOrderDetailsEntity);
            invAddingOrderDetailsEntityListFirstBackup.add(invAddingOrderDetailsEntity);
            if (isPermission) {
                if (invAddingorderDetail != null && invAddingorderDetail.getSelectedId() != null) {
                    if (headIdList == null || headIdList.length() == 0) {
                        headIdList.append(invAddingorderDetail.getSelectedId().getId());
                    } else {
                        headIdList.append(",").append(invAddingorderDetail.getSelectedId().getId());
                    }
                }
            } else {
                if (invAddingorderDetail != null && invAddingorderDetail.getSelectedPurchaseId() != null) {
                    if (headIdList == null || headIdList.length() == 0) {
                        headIdList.append(invAddingorderDetail.getSelectedPurchaseId().getId());
                    } else {
                        headIdList.append(",").append(invAddingorderDetail.getSelectedPurchaseId().getId());
                    }
                }
            }

            invAddingOrderEntityRetrieved.setInvAddingOrderDetailList(invAddingOrderDetailsEntityList);

        }
        return invAddingOrderEntityRetrieved;
    }

    public void loadDefaultInvPurchaseOrderData(InvAddingOrderEntity invAddingOrderEntity) {
        InvOrganizationSite supplier = new InvOrganizationSite();
        supplier.setId(invAddingOrderEntity.getPurchaseOrderNLoaded().getOrganizationId());
        supplier.setName(invAddingOrderEntity.getPurchaseOrderNLoaded().getOrganizationName());
        supplier.setCode(invAddingOrderEntity.getPurchaseOrderNLoaded().getOrganizationCode());
        invAddingOrderEntity.setSupplierId(supplier);
        invAddingOrderEntity.setSupplierDate(invAddingOrderEntity.getPurchaseOrderNLoaded().getDate());
        invAddingOrderEntity.setSupplierInvoice(invAddingOrderEntity.getPurchaseOrderNLoaded().getOrganizationCode());
        InventoryDelegator delegator = new InventoryDelegator();
        delegator.setId(invAddingOrderEntity.getPurchaseOrderNLoaded().getDelegatorId());
        delegator.setName(invAddingOrderEntity.getPurchaseOrderNLoaded().getDelegatorName());
        delegator.setCode(invAddingOrderEntity.getPurchaseOrderNLoaded().getDelegatorCode());
        invAddingOrderEntity.setDelegatorId(delegator);
        invAddingOrderEntity.setRemark(invAddingOrderEntity.getPurchaseOrderNLoaded().getRemarks());
    }

    public void fillItemMap(InvAddingOrderEntity invAddingOrderEntity,
            List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityList,
            Boolean isPermission,
            InventorySetup invSetup,
            Map<Integer, BigDecimal> itemsMap,
            Integer branchId) {
        if (invSetup != null && invSetup.getNegativeSell() == false && invAddingOrderEntity.getInvInventory() != null) {
            List<QuantityItemsStore> quantityItemsStoresList = quantityItemsStoreService.findInventoryItemsList(invAddingOrderEntity.getInvInventory().getId(), branchId, invSetup.getSellBuy());

            for (QuantityItemsStore quantityItemsStore : quantityItemsStoresList) {
                itemsMap.put(quantityItemsStore.getItemId(), quantityItemsStore.getQty());
            }

            if (isPermission) {
                completeFillMap(invAddingOrderDetailsEntityList, itemsMap);
            }
        }
    }

    public void completeFillMap(List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityList, Map<Integer, BigDecimal> itemsMap) {
        if (invAddingOrderDetailsEntityList != null && !invAddingOrderDetailsEntityList.isEmpty()) {
            for (InvAddingOrderDetailsEntity item : invAddingOrderDetailsEntityList) {
                if (item.getItemsBarcodesDetail() != null) {
                    BigDecimal dbQut = itemsMap.get(item.getItemsBarcodesDetail().getItemId());
                    if (dbQut != null) {
                        itemsMap.put(item.getItemsBarcodesDetail().getItemId(), dbQut.add(item.getQuantity() == null ? BigDecimal.ZERO : item.getQuantity()));
                    } else {
                        itemsMap.put(item.getItemsBarcodesDetail().getItemId(), item.getQuantity() == null ? BigDecimal.ZERO : item.getQuantity());
                    }
                }
            }
        }

    }

    public void initPurchaseOrder(List<InvPurchaseOrderDetail> invPurchaseOrdersDetailList,
            InvAddingOrderEntity invAddingOrderEntity, InvPurchaseOrderDetailConverter invPurchaseOrderDetailConverter,
            InvAddingOrderDetailsEntity invAddingOrderDetailsEntity, List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityList) {

        invPurchaseOrdersDetailList = invPurchaseOrderDetailsService.getAllPurchaseOrderDetailsByInvPurchaseOrderId(invAddingOrderEntity.getPurchaseOrderNLoaded().getPurchaseId());
        invAddingOrderEntity.setPurchaseOrderNLoadedTrans(invAddingOrderEntity.getPurchaseOrderNLoaded());
        loadDefaultInvPurchaseOrderData(invAddingOrderEntity);
        invPurchaseOrderDetailConverter = new InvPurchaseOrderDetailConverter(invPurchaseOrdersDetailList);
        for (InvPurchaseOrderDetail invPurchaseOrderDetail : invPurchaseOrdersDetailList) {
            invAddingOrderDetailsEntity = new InvAddingOrderDetailsEntity();

            Symbol unit = new Symbol();
            unit.setId(invPurchaseOrderDetail.getUnitId().getId());
            unit.setName(invPurchaseOrderDetail.getUnitId().getName());
            unit.setSerial(invPurchaseOrderDetail.getUnitId().getSerial());
            invAddingOrderDetailsEntity.setUnitName(unit.getName());
            InvItem item = new InvItem();
            item.setId(invPurchaseOrderDetail.getItemId().getId());
            item.setName(invPurchaseOrderDetail.getItemId().getName());
            item.setCode(invPurchaseOrderDetail.getItemBarcode());
            item.setUnitId(invPurchaseOrderDetail.getItemId().getUnitId());
            invAddingOrderDetailsEntity.setItemId(item);
            invAddingOrderDetailsEntity.setItem(item.getName());
            invAddingOrderDetailsEntity.setQuantity(invPurchaseOrderDetail.getQuantity());
            invAddingOrderDetailsEntity.setPurchaseOrderDetail(invPurchaseOrderDetail);
            invAddingOrderDetailsEntity.setPurchaseOrderDetailTrans(invPurchaseOrderDetail);
            invAddingOrderDetailsEntityList.add(invAddingOrderDetailsEntity);
        }
    }

    public void fillItemBarcodeMap(List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, Integer branchId) {
        itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }

    }

    public void print(InvAddingOrderEntity invAddingOrderEntity,
            List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityList, UserData userData, Boolean isPermission) {

        if (isPermission) {
//            iReportService.printReportInvPermissionOrder(invAddingOrderEntity, invAddingOrderDetailsEntityList, userData);
        } else {
//            iReportService.printReportInvAddingOrder(invAddingOrderEntity, invAddingOrderDetailsEntityList, userData);
        }

    }

    public boolean errorMessage(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
        return false;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invaddingorder;

import com.isag.businessservice.InvAddingOrderDetailsService;
import com.isag.businessservice.InvAddingOrderService;
import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.InvPurchaseOrderDetailsService;
import com.isag.businessservice.InvPurchaseOrderService;
import com.isag.businessservice.InventorySetupService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.QuantityItemsStoreService;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.converter.InvPurchaseInvoiceConverter;
import com.isag.converter.InvPurchaseOrderConverter;
import com.isag.converter.InvPurchaseOrderDetailConverter;
import com.isag.converter.PurchaseOrderNotLoadedViewConverter;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entity.InventorySetup;
import com.isag.entity.Symbol;
import com.isag.entiy.InvAddingOrderDetailsEntity;
import com.isag.entiy.InvAddingOrderEntity;
import com.isag.entiy.ItemDataEntity;
import com.isag.isag.InventoryBasicDataForm;
import com.isag.isag.UserData;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.views.PurchaseOrderNotLoadedFromInvAddingOrder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author WIN7
 */
@Named(value = "invAddingOrderFormMB")
@ViewScoped
public class invAddingOrderFormMB extends InventoryBasicDataForm implements Serializable {

    private Integer branchId;
    private Integer companyId;
    private Integer invAddingOrderId;
    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private BigDecimal hundred = new BigDecimal(100);
    private ItemDataEntity itemDataEntity;
    private List<ItemDataEntity> itemsDataEntityList;
    Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private Boolean showDialogMessage = Boolean.FALSE;
    private InvAddingOrderEntity invAddingOrderEntity;
    private InvAddingOrderEntity invAddingOrderEntityRetrieved;
    private List<InvAddingOrderEntity> invAddingOrderEntityList;
    private InvAddingOrderDetailsEntity invAddingOrderDetailsEntity;
    private InvAddingOrderDetailsEntity invAddingOrderDetailsEntitySelected;
    private List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityList;
    private List<InvAddingOrderDetailsEntity> invAddingOrderEntityBackList;
    private List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityListFirstBackup;
    private List<InvAddingOrderDetailsEntity> rowsDeleted = new ArrayList<>();
    private List<Integer> updatedPurchaseOrAddingOrderList;
    private InvAddingorder invAddingOrder;
    private InvAddingorder invAddingUpdated;
    private InvAddingorderDetail invAddingOrderDetail;
    private StringBuilder headIdList = new StringBuilder();
    private List<InvAddingorderDetail> invAddingorderDetailList;
    private List<InvAddingorderDetail> invAddingorderDetailListUpdated;
    private List<InvAddingorderDetail> invAddingorderDetailListDeleted;
    private List<InvPurchaseOrder> invPurchaseOrdersList;
    private List<InvPurchaseOrderDetail> invPurchaseOrdersDetailList;
    private List<InvPurchaseInvoice> invPurchaseInvoiceSelectedList;
    private List<InvPurchaseInvoice> invPurchaseInvoiceSelectedListBackup = new ArrayList<>();
    private List<InvPurchaseInvoice> invPurchaseInvoiceSelectedListDeleted = new ArrayList<>();
    private List<InvPurchaseInvoice> invPurchaseInvoiceSelectedListExist = new ArrayList<>();
    private List<InvPurchaseInvoice> invPurchaseInvoiceSelectedListAdd = new ArrayList<>();
    private List<InvPurchaseInvoice> invPurchaseInvoiceList;
    private InvPurchaseInvoiceConverter invPurchaseInvoiceConverter;
    private InvPurchaseOrderConverter invPurchaseOrderConverter;
    private InvPurchaseOrderDetailConverter invPurchaseOrderDetailConverter;
    private List<PurchaseOrderNotLoadedFromInvAddingOrder> purchaseOrderNotLoadedFromInvAddingOrderList;
    private PurchaseOrderNotLoadedViewConverter purchaseOrderNotLoadedViewConverter;

    private List<InvItem> invItemsList;
    Map<Integer, BigDecimal> itemsMap;
    private String uri;
    private boolean isPermission;

    private InvAddingOrderParent invAddingOrderParent;
    private InventorySetup invSetup;

    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    private QuantityItemsStoreService quantityItemsStoreService;
    @EJB
    private InvAddingOrderService invAddingOrderService;
    @EJB
    private InvAddingOrderDetailsService invAddingorderDetailsService;
    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;
    @EJB
    private InvPurchaseOrderDetailsService invPurchaseOrderDetailsService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    private InvAddingOrderDetailsService addingOrderDetailsService;
    @EJB
    private InventorySetupService inventorySetupService;

    @EJB
    InvDelegatorService invDelegatorService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            setBranchId(getUserData().getUserBranch().getId());
            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

            if (uri.contains("invaddingorder1")) {
                isPermission = false;
            } else {
                isPermission = true;
            }

            initObjs();
            fillListss();

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetInvAddingOrderForm();
                showMessageDetails = showMessageGeneral = showDialogMessage = false;
                getInvAddingOrderParent().fillItemMap(invAddingOrderEntity, invAddingOrderDetailsEntityList,
                        isPermission, getInvSetup(), itemsMap, branchId);
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                setInvAddingOrderId((Integer) context.getSessionMap().get("invAddingOrderSelected"));
                invAddingOrder = invAddingOrderService.findInvAddingOrderByInvAddingOrderId(getInvAddingOrderId());

                invAddingUpdated = invAddingOrder;
                invAddingorderDetailList = invAddingorderDetailsService.getAllInvAddingOrderDetailsByInvAddingOrderId(invAddingOrder.getId());

                invAddingOrderEntity = getInvAddingOrderParent().mapInvAddingOrderToInvAddingOrderEntity(invAddingOrder,
                        invAddingorderDetailList, invAddingOrderEntity,
                        invAddingOrderEntityRetrieved, invAddingOrderDetailsEntity,
                        isPermission, itemsBarcodeMap, totalQuatity, invAddingOrderDetailsEntityList,
                        invAddingOrderDetailsEntityListFirstBackup, headIdList);

                if (invAddingOrder.getPurchaseOrderId() != null) {
                    getInvAddingOrderParent().initPurchaseOrder(invPurchaseOrdersDetailList, invAddingOrderEntity,
                            invPurchaseOrderDetailConverter, invAddingOrderDetailsEntity, invAddingOrderDetailsEntityList);
                }
                getInvAddingOrderParent().fillItemMap(invAddingOrderEntity, invAddingOrderDetailsEntityList,
                        isPermission, getInvSetup(), itemsMap, branchId);
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "load");
        }
    }

    private void initObjs() {
        try {
            rowsDeleted = new ArrayList<>();
            invAddingorderDetailListUpdated = new ArrayList<>();
            itemsDataEntityList = new ArrayList<>();
            invAddingOrderEntityList = new ArrayList<>();
            invAddingOrderDetailsEntityList = new ArrayList<>();
            invAddingOrderEntityBackList = new ArrayList<>();
            invAddingOrderDetailsEntityListFirstBackup = new ArrayList<>();
            itemDataEntity = new ItemDataEntity();
            invAddingOrderDetailsEntity = new InvAddingOrderDetailsEntity();
            invAddingOrderEntity = new InvAddingOrderEntity();
            invAddingOrder = new InvAddingorder();
            invAddingOrderDetail = new InvAddingorderDetail();
            invPurchaseOrdersList = new ArrayList<>();
            purchaseOrderNotLoadedFromInvAddingOrderList = new ArrayList<>();
            invAddingorderDetailListDeleted = new ArrayList<>();
            itemsBarcodeMap = new HashMap<>();
            itemsMap = new HashMap<>();
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "initObjs");
        }
    }

    private void fillListss() {
        try {
            if (uri.contains("invaddingorder1")) {
                purchaseOrderNotLoadedFromInvAddingOrderList = invPurchaseOrderService.getAllPurchaseOrderNotLoaded(branchId);
                setPurchaseOrderNotLoadedViewConverter(new PurchaseOrderNotLoadedViewConverter(purchaseOrderNotLoadedFromInvAddingOrderList));
                setSiteType(1);
            } else {
                invPurchaseInvoiceList = invPurchaseInvoiceService.getALLSalesInvoiceByBranchIdWithStatusPer(branchId);
                invPurchaseInvoiceConverter = new InvPurchaseInvoiceConverter(invPurchaseInvoiceList);
                setSiteType(0);
            }
            getInventoryDelegatorList(0);
            getInvOrganizationSiteList();
            getInvInventoryList();
            fillItemMap(itemsBarcodeMap, null);
            getInvSetup();
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "fillLists");
        }
    }

    public void loadInvSalesInvoiceFromText() {

    }

    public void loadInvSalesInvoice() {
        try {
            if ((invPurchaseInvoiceSelectedList == null || invPurchaseInvoiceSelectedList.isEmpty())) {
                for (InvAddingOrderDetailsEntity entity : invAddingOrderDetailsEntityListFirstBackup) {
                    if (isPermission) {
                        if (entity.getSelectedId() != null && entity.getId() == null) {
                            invAddingOrderDetailsEntityList.remove(entity);
                        }
                    } else {
                        if (entity.getSelectedPurchaseId() != null && entity.getId() == null) {
                            invAddingOrderDetailsEntityList.remove(entity);
                        }
                    }
                }
                invAddingOrderDetailsEntityListFirstBackup = new ArrayList<>();
                invPurchaseInvoiceSelectedListBackup = new ArrayList<>();
                for (InvAddingOrderDetailsEntity detailbackup : invAddingOrderDetailsEntityListFirstBackup) {
                    for (InvPurchaseInvoice selectedBackup : invPurchaseInvoiceSelectedListBackup) {
                        invAddingOrderDetailsEntityList.add(detailbackup);
                        invPurchaseInvoiceSelectedList.add(selectedBackup);
                        loadDefaultInvinvoiceData();
                    }

                }

            } else {
                invPurchaseInvoiceSelectedListDeleted = new ArrayList<>();
                invPurchaseInvoiceSelectedListExist = new ArrayList<>();
                invPurchaseInvoiceSelectedListAdd = new ArrayList<>();
                for (InvPurchaseInvoice back : invPurchaseInvoiceSelectedListBackup) {
                    if (invPurchaseInvoiceSelectedList.contains(back)) {
                        invPurchaseInvoiceSelectedListExist.add(back);

                    } else {
                        invPurchaseInvoiceSelectedListDeleted.add(back);

                    }

                }

                for (InvPurchaseInvoice back : invPurchaseInvoiceSelectedList) {
                    if (!invPurchaseInvoiceSelectedListExist.contains(back)) {
                        invPurchaseInvoiceSelectedListAdd.add(back);
                        loadDefaultInvinvoiceData();
                    }
                }

                invPurchaseInvoiceSelectedListBackup = invPurchaseInvoiceSelectedList;
                for (InvPurchaseInvoice back : invPurchaseInvoiceSelectedListDeleted) {
                    for (InvAddingOrderDetailsEntity entity : invAddingOrderDetailsEntityListFirstBackup) {
                        if (isPermission) {
                            if (entity.getSelectedId() != null && entity.getId() == null) {
                                if (entity.getSelectedId().getInvPurchaseInvoiceId().getId().compareTo(back.getId()) == 0) {
                                    invAddingOrderDetailsEntityList.remove(entity);
                                }
                            }
                        } else {
                            if (entity.getSelectedPurchaseId() != null && entity.getId() == null) {
                                if (entity.getSelectedPurchaseId().getInvPurchaseInvoiceId().getId().compareTo(back.getId()) == 0) {
                                    invAddingOrderDetailsEntityList.remove(entity);
                                }
                            }
                        }

                    }
                }

//
//            invAddingOrderDetailsEntityList = new ArrayList<>();
                updatedPurchaseOrAddingOrderList = new ArrayList<>();

                if (invPurchaseInvoiceSelectedListAdd != null && !invPurchaseInvoiceSelectedListAdd.isEmpty()) {
                    InvAddingOrderDetailsEntity invAddingOrderDetailsEntitySelected;
                    List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsList;

                    for (InvPurchaseInvoice invPurchaseInvoice : invPurchaseInvoiceSelectedListAdd) {

                        updatedPurchaseOrAddingOrderList.add(invPurchaseInvoice.getId());
                        loadDefaultInvinvoiceData();
                        // isPurchaseOrder = true;

                        invPurchaseInvoiceDetailsList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdWithStatus(invPurchaseInvoice.getId());
                        for (InvPurchaseInvoiceDetail purchaseInvoiceDetail : invPurchaseInvoiceDetailsList) {

                            invAddingOrderDetailsEntitySelected = new InvAddingOrderDetailsEntity();

                            if (purchaseInvoiceDetail.getItemId() != null) {
                                invAddingOrderDetailsEntitySelected.setItemsBarcodesDetail(itemsBarcodeMap.get(purchaseInvoiceDetail.getItemId().getCode()));
                                invAddingOrderDetailsEntitySelected.setItemsBarcodesDetailTrans(invAddingOrderDetailsEntitySelected.getItemsBarcodesDetail());
                                Symbol unit = new Symbol();
                                unit.setId(invAddingOrderDetailsEntitySelected.getItemsBarcodesDetail().getUnitId());
                                unit.setName(invAddingOrderDetailsEntitySelected.getItemsBarcodesDetail().getUnitName());
                                invAddingOrderDetailsEntitySelected.setUnitName(unit.getName());
                            }
                            invAddingOrderDetailsEntitySelected.setItemId(purchaseInvoiceDetail.getItemId() != null ? purchaseInvoiceDetail.getItemId() : null);
                            invAddingOrderDetailsEntitySelected.setItem(purchaseInvoiceDetail.getItemId().getName() != null ? purchaseInvoiceDetail.getItemId().getName() : "");
                            invAddingOrderDetailsEntitySelected.setQuantity(purchaseInvoiceDetail.getFinalQuantity() != null ? purchaseInvoiceDetail.getFinalQuantity() : purchaseInvoiceDetail.getQuantity());
                            invAddingOrderDetailsEntitySelected.setScrewing(purchaseInvoiceDetail.getScrewing() != null ? purchaseInvoiceDetail.getScrewing() : BigDecimal.ONE);
                            invAddingOrderDetailsEntitySelected.setUnitName(purchaseInvoiceDetail.getUnitId().getName() != null ? purchaseInvoiceDetail.getUnitId().getName() : "");
                            invAddingOrderDetailsEntitySelected.setTransferFrom(0);
                            invAddingOrderDetailsEntitySelected.setMainQuantity(purchaseInvoiceDetail.getFinalQuantity() != null ? purchaseInvoiceDetail.getFinalQuantity() : purchaseInvoiceDetail.getQuantity());

                            if (isPermission) {
                                invAddingOrderDetailsEntitySelected.setSelectedId(purchaseInvoiceDetail);
                            } else {
                                invAddingOrderDetailsEntitySelected.setSelectedPurchaseId(purchaseInvoiceDetail);
                            }
                            invAddingOrderDetailsEntitySelected.setMarkEdit(Boolean.FALSE);
                            invAddingOrderDetailsEntityList.add(invAddingOrderDetailsEntitySelected);
                            invAddingOrderDetailsEntityListFirstBackup.add(invAddingOrderDetailsEntitySelected);
                        }
                    }

                    updateSummition();

                    return;
                }

            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "loadInvSalesInvoice");
        }
    }

    public void loadSalesInvoice() {

    }

    public void updateSummition() {
        try {
            BigDecimal quantity = BigDecimal.ZERO;

            totalQuatity = BigDecimal.ZERO;

            // BigDecimal rate = invAddingOrderEntity.getRate() != null ? invAddingOrderEntity.getRate() : BigDecimal.ONE;
            for (InvAddingOrderDetailsEntity detailEntity : invAddingOrderDetailsEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
                quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
                totalQuatity = totalQuatity.add(detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO);
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "updateSummition");
        }

    }

    private void resetInvAddingOrderForm() {
        try {

            invAddingOrderEntity = new InvAddingOrderEntity();
            invAddingOrderEntity.setDate(new Date());

            if (getInvOrganizationSiteList() != null && !getInvOrganizationSiteList().isEmpty()) {
                invAddingOrderEntity.setSupplierId(getInvOrganizationSiteList().get(0));
            }
            if (getInvInventoryList() != null && !getInvInventoryList().isEmpty()) {
                invAddingOrderEntity.setInvInventory(getInvInventoryList().get(0));

            }
            if (invPurchaseOrdersList != null && !invPurchaseOrdersList.isEmpty()) {
                invAddingOrderEntity.setPurchaseOrderId(invPurchaseOrdersList.get(0));
                getInvAddingOrderParent().loadDefaultInvPurchaseOrderData(invAddingOrderEntity);
            }
            if (invPurchaseInvoiceList != null && !invPurchaseInvoiceList.isEmpty()) {
                loadDefaultInvinvoiceData();
            }
            invAddingOrderEntity.setInvAddingOrderDetailList(new ArrayList<InvAddingOrderDetailsEntity>());

            invAddingOrderEntityList = new ArrayList<>();
            invAddingOrderDetailsEntitySelected = new InvAddingOrderDetailsEntity();
            // invPurchaseOrdersList=new ArrayList<>();
            invAddingOrderDetailsEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "resetInvAddingOrderForm");
        }
    }

    public void addDetails() {
        try {
            for (InvAddingOrderDetailsEntity detailEntity : invAddingOrderDetailsEntityList) {
                detailEntity.setMarkEdit(Boolean.FALSE);
                if (invAddingOrderEntity.getPurchaseOrderNLoaded() != null) {
                    if (detailEntity.getPurchaseOrderDetailTrans() == null || detailEntity.getItemId() == null || detailEntity.getQuantity() == null) {
                        errorMessage("يجب ادخال تفاصيل الفاتورة");
                        return;
                    }

                    // get old value from trans as they are deleted when delete row
                    detailEntity.setPurchaseOrderDetail(detailEntity.getPurchaseOrderDetailTrans());

                    InvItem item = new InvItem();
                    item.setId(detailEntity.getPurchaseOrderDetailTrans().getItemId().getId());
                    item.setName(detailEntity.getPurchaseOrderDetailTrans().getItemId().getName());
                    item.setCode(detailEntity.getPurchaseOrderDetailTrans().getItemBarcode());
                    item.setUnitId(detailEntity.getPurchaseOrderDetailTrans().getItemId().getUnitId());

                    detailEntity.setItemId(item);
                    detailEntity.setItem(item.getName());
                } else {

                    /*      if (detailEntity.getItemsBarcodesDetailTrans() == null || detailEntity.getItemId() == null || detailEntity.getQuantity() == null) {
                     errorMessage("يجب ادخال تفاصيل الفاتورة");
                     return;
                     }*/
                    if (detailEntity.getItemsBarcodesDetailTrans() != null && detailEntity.getItemsBarcodesDetail() != null) {
                        detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                    } else {
                        errorMessage("يجب ادخال تفاصيل الفاتورة");
                        return;
                    }
                    InvItem item = new InvItem();
                    Symbol unit = new Symbol();

                    unit.setId(detailEntity.getItemsBarcodesDetailTrans().getUnitId());
                    unit.setName(detailEntity.getItemsBarcodesDetailTrans().getUnitName());

                    item.setId(detailEntity.getItemsBarcodesDetailTrans().getItemId());
                    item.setName(detailEntity.getItemsBarcodesDetailTrans().getName());
                    item.setCode(detailEntity.getItemsBarcodesDetailTrans().getBarcode());
                    item.setUnitId(unit);
                    // detailEntity.setUnitName(detailEntity.getItemsBarcodesDetailTrans().getUnitName());

                    detailEntity.setItemId(item);
                    detailEntity.setItem(item.getName());
                    if (isPermission) {
                        if (!validateQuantity(detailEntity)) {

                            errorMessage("خطأ فى الكميات");
                            return;
                        }
                    }

                }
            }
            InvAddingOrderDetailsEntity invAddingOrderDetailsNew = new InvAddingOrderDetailsEntity();
            invAddingOrderDetailsNew.setMarkEdit(Boolean.TRUE);
            invAddingOrderDetailsEntitySelected = invAddingOrderDetailsNew;
            invAddingOrderDetailsEntityList.add(invAddingOrderDetailsNew);
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "addDetails");
        }
    }

    public void deleteInvOrderDetail() {
        try {

            if (invAddingOrderDetailsEntitySelected != null && invAddingOrderDetailsEntitySelected.getId() != null) {
                getRowsDeleted().add(invAddingOrderDetailsEntitySelected);
            }

            invAddingOrderDetailsEntityList.remove(invAddingOrderDetailsEntitySelected);

            updateQuantity();
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "deleteInvOrderDetail");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {
            String column_name = event.getColumn().getClientId();
            String[] parts = column_name.split("form:invAddingOrderDetailsTable:0:");
            String part1 = parts[0];

            if (column_name.contains("Quantity")) {
                Object oldValue = event.getOldValue();  //here i get old value
                Object newValue = event.getNewValue(); //new value
                totalQuatity = totalQuatity.subtract(oldValue != null ? (BigDecimal) oldValue : BigDecimal.ZERO);
                totalQuatity = totalQuatity.add(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO);
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "onCellEdit");
        }
    }

    public Boolean validateItems(InvAddingOrderDetailsEntity invPurchaseOrderDetailTable) {
        try {
            if (invPurchaseOrderDetailTable.getItemsBarcodesDetail() == null) {
                resetInvItem(invPurchaseOrderDetailTable);
            } else {
                invPurchaseOrderDetailTable.setItemsBarcodesDetailTrans(invPurchaseOrderDetailTable.getItemsBarcodesDetail());

                if (invPurchaseOrderDetailTable.getItemsBarcodesDetail().getUnitName() != null
                        && !"".equals(invPurchaseOrderDetailTable.getItemsBarcodesDetail().getUnitName())) {
                    invPurchaseOrderDetailTable.setUnitName(invPurchaseOrderDetailTable.getItemsBarcodesDetail().getUnitName());
                    invPurchaseOrderDetailTable.setScrewing(invPurchaseOrderDetailTable.getItemsBarcodesDetail().getScrewing());
                    invPurchaseOrderDetailTable.setMainQuantity(BigDecimal.ZERO);
                } else {
                    resetInvItem(invPurchaseOrderDetailTable);
                    return errorMessage("هذا الصنف ليس لديه وحدات.");
                }
            }
            return true;
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "validateItems");
            return null;
        }
    }
    //-------------------------   

    public void loadPurchaseOrder() {
        try {
            if (invAddingOrderEntity.getPurchaseOrderNLoaded() == null) {
                if (invAddingOrderDetailsEntityList != null && !invAddingOrderDetailsEntityList.isEmpty()) {
                    OpenDlg("dlg1");
                }
            } else {
                if (invAddingOrderDetailsEntityList != null && !invAddingOrderDetailsEntityList.isEmpty()) {
                    if (!invAddingOrderEntity.getPurchaseOrderNLoaded().equals(invAddingOrderEntity.getPurchaseOrderNLoadedTrans())) {
                        OpenDlg("dlg2");
                    }
                } else {
                    invAddingOrderDetailsEntityList = new ArrayList<>();
                    getInvAddingOrderParent().initPurchaseOrder(invPurchaseOrdersDetailList, invAddingOrderEntity, invPurchaseOrderDetailConverter, invAddingOrderDetailsEntity, invAddingOrderDetailsEntityList);
                    getInvPurchaseOrdersDetailList();
                }
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "loadPurchaseOrder");
        }

    }

    public void OpenDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "OpenDlg");
        }
    }

    BigDecimal subtract = BigDecimal.ZERO;

    public Boolean validateItem(InvAddingOrderDetailsEntity addingOrderDetailsEntity) {
        try {
            if (addingOrderDetailsEntity.getPurchaseOrderDetail() != null) {

                addingOrderDetailsEntity.setUseQuantity(addingOrderDetailsService.findQuantityUsageForItem(addingOrderDetailsEntity.getPurchaseOrderDetail().getId()));
                addingOrderDetailsEntity.setPurchaseOrderDetailTrans(addingOrderDetailsEntity.getPurchaseOrderDetail());
                addingOrderDetailsEntity.setItemsBarcodesDetail(addingOrderDetailsEntity.getItemsBarcodesDetail());
                for (InvAddingOrderDetailsEntity detailEntity : invAddingOrderDetailsEntityList) {
                    if (!detailEntity.getCounter().equals(addingOrderDetailsEntity.getCounter())
                            && detailEntity.getPurchaseOrderDetailTrans().equals(addingOrderDetailsEntity.getPurchaseOrderDetailTrans())) {
                        resetInvItem(addingOrderDetailsEntity);
                        return errorMessage("تم اختيار هذا الصنف من قبل");
                    }
                }
                subtract = (addingOrderDetailsEntity.getPurchaseOrderDetailTrans().getQuantity()).subtract(addingOrderDetailsEntity.getUseQuantity() != null ? addingOrderDetailsEntity.getUseQuantity() : BigDecimal.ZERO);
                if (subtract.compareTo(addingOrderDetailsEntity.getUseQuantity() != null ? addingOrderDetailsEntity.getUseQuantity() : BigDecimal.ZERO) > 0) {
                    addingOrderDetailsEntity.setQuantity(subtract);
                } else {
                    addingOrderDetailsEntity.setQuantity(BigDecimal.ZERO);
                }
                addingOrderDetailsEntity.setUnitName(addingOrderDetailsEntity.getPurchaseOrderDetailTrans().getUnitId().getName());
                addingOrderDetailsEntity.setScrewing(addingOrderDetailsEntity.getPurchaseOrderDetailTrans().getScrewing());
                InvItem item = new InvItem();
                item.setId(addingOrderDetailsEntity.getPurchaseOrderDetailTrans().getItemId().getId());
                item.setName(addingOrderDetailsEntity.getPurchaseOrderDetailTrans().getItemId().getName());
                item.setCode(addingOrderDetailsEntity.getPurchaseOrderDetailTrans().getItemBarcode());
                item.setUnitId(addingOrderDetailsEntity.getPurchaseOrderDetailTrans().getItemId().getUnitId());
                addingOrderDetailsEntity.setItemId(item);
                addingOrderDetailsEntity.setItem(item.getName());
            }

            updateSummition();
            return true;
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "validateItem");
            return null;
        }
    }

    //----------------------------------
    private void loadDefaultInvinvoiceData() {
        try {
            InvOrganizationSite supplier = new InvOrganizationSite();
            supplier.setId(invAddingOrderEntity.getSupplierId().getId());
            supplier.setName(invAddingOrderEntity.getSupplierId().getName());
            supplier.setCode(invAddingOrderEntity.getSupplierId().getCode());
            invAddingOrderEntity.setSupplierId(supplier);
            invAddingOrderEntity.setSupplierInvoice(invAddingOrderEntity.getSupplierId().getCode());
            invAddingOrderEntity.setSupplierDate(invAddingOrderEntity.getSupplierId().getOpenBalanceDate());
            invAddingOrderEntity.setRemark(invAddingOrderEntity.getSupplierId().getRemark());
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "loadDefaultInvinvoiceData");
        }
    }

    private void resetInvItem(InvAddingOrderDetailsEntity invPurchaseOrderDetailTable) {
        try {
            invPurchaseOrderDetailTable.setUnitName(null);
            invPurchaseOrderDetailTable.setQuantity(null);
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "resetInvItem");
        }
    }

    public Boolean validateQuantityColum(InvAddingOrderDetailsEntity invAddingOrderDetailsTable) {
        try {
            if (invAddingOrderDetailsTable.getSelectedId() != null && !validateQuantity(invAddingOrderDetailsTable)) {

                if (invAddingOrderDetailsTable.getItemsBarcodesDetail() != null) {
                    invAddingOrderDetailsTable.setQuantity(invAddingOrderDetailsTable.getMainQuantity());
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "الكمية المطلوبة اكبر من الموجودة بالمخزن"));
                    return false;
                } else {
                    invAddingOrderDetailsTable.setQuantity(null);
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ادخال تفاصيل الفاتورة"));
                    return false;
                }

            }
            updateQuantity();
            return true;
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "validateQuantityColum");
            return null;
        }

    }

    public Boolean validateQuantity(InvAddingOrderDetailsEntity invAddingOrderDetailsTable) {
        try {

            if (isPermission) {
                if (invAddingOrderDetailsTable != null && invAddingOrderDetailsTable.getItemsBarcodesDetailTrans() != null
                        && invAddingOrderDetailsTable.getItemsBarcodesDetailTrans().getItemId() != null) {

                    if (invAddingOrderDetailsTable.getQuantity() != null
                            && invAddingOrderDetailsTable.getQuantity().compareTo(BigDecimal.ZERO) == 1) {
                        if (getInvSetup().getNegativeSell() == false) {
                            if (!itemsMap.isEmpty()) {
                                BigDecimal dbQut = itemsMap.get(invAddingOrderDetailsTable.getItemsBarcodesDetail().getItemId());
                                if (dbQut != null) {
                                    BigDecimal usrQut = sumItemQuantity(invAddingOrderDetailsTable.getItemsBarcodesDetail().getItemId());
                                    return dbQut.compareTo(BigDecimal.ZERO) == 1 && dbQut.compareTo(usrQut) != -1;
                                } else {
                                    return errorMessage("يجب ادخال كمية أقل");
                                }
                            }
                        } else {
                            return true;
                        }
                    } else {
                        return errorMessage("يجب ادخال كمية");
                    }
                } else {
                    return errorMessage("يجب اختيار صنف");
                }
                return false;

            } else {
                if (invAddingOrderDetailsTable.getQuantity().compareTo(invAddingOrderDetailsTable.getMainQuantity()) > 0) {
                    return false;
                }

                return invAddingOrderDetailsTable != null && invAddingOrderDetailsTable.getItemsBarcodesDetail() != null
                        && invAddingOrderDetailsTable.getQuantity() != null
                        && invAddingOrderDetailsTable.getQuantity().compareTo(BigDecimal.ZERO) == 1;
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "validateQuantity");
            return null;
        }

    }

    BigDecimal sumItemQuantity(Integer itemId) {
        try {

            BigDecimal detailQuant, detailScrewing, quant = BigDecimal.ZERO;
            for (InvAddingOrderDetailsEntity detailsEntity : invAddingOrderDetailsEntityList) {
                if (detailsEntity.getItemsBarcodesDetail() != null && detailsEntity.getItemsBarcodesDetail().getItemId() != null
                        && Objects.equals(itemId, detailsEntity.getItemsBarcodesDetail().getItemId())
                        && detailsEntity.getQuantity() != null && detailsEntity.getQuantity().compareTo(BigDecimal.ZERO) == 1) {

                    detailQuant = detailsEntity.getQuantity();
                    detailScrewing = detailsEntity.getScrewing() != null ? detailsEntity.getScrewing() : BigDecimal.ONE;

                    quant = quant.add(detailQuant.multiply(detailScrewing));
                }
            }
            return quant;
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "sumItemQuantity");
            return null;
        }

    }

    public void updateQuantity() {
        try {
            totalQuatity = BigDecimal.ZERO;
            for (InvAddingOrderDetailsEntity detailEntity : invAddingOrderDetailsEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }

                totalQuatity = totalQuatity.add(detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO);
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "updateQuantity");
        }

    }

    @Override
    public String save() {
        try {
            totalQuatity = BigDecimal.ZERO;

            if (invAddingOrderEntity != null && invAddingOrderDetailsEntityList != null && !invAddingOrderDetailsEntityList.isEmpty()) {

                invAddingOrder.setType(isPermission ? 1 : 0);

                if (invAddingOrderEntity.getId() != null) {
                    invAddingOrder.setId(invAddingOrderEntity.getId());
                    invAddingOrder.setCreatedBy(getInvAddingUpdated().getCreatedBy());
                    invAddingOrder.setCreationDate(getInvAddingUpdated().getCreationDate() != null ? getInvAddingUpdated().getCreationDate() : null);

                    invAddingOrder.setModifiedBy(getUserData().getUser());
                    invAddingOrder.setModificationDate(new Date());

                } else {
                    invAddingOrder.setCreatedBy(getUserData().getUser());
                    invAddingOrder.setCreationDate(new Date());
                    invAddingOrder.setStatus(0);
                }
                invAddingOrder.setSerial(invAddingOrderEntity.getSerial());
                invAddingOrder.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch() : null);
                invAddingOrder.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);
                invAddingOrder.setDate(invAddingOrderEntity.getDate() != null ? invAddingOrderEntity.getDate() : null);
                invAddingOrder.setPostFlag(invAddingOrderEntity.getPostFlag() != null ? invAddingOrderEntity.getPostFlag() : 0);
                invAddingOrder.setRemark(invAddingOrderEntity.getRemark() != null ? invAddingOrderEntity.getRemark() : null);
                invAddingOrder.setSupplierInvoice(invAddingOrderEntity.getSupplierInvoice() != null ? invAddingOrderEntity.getSupplierInvoice() : null);
                invAddingOrder.setSupplierDate(invAddingOrderEntity.getSupplierDate() != null ? invAddingOrderEntity.getSupplierDate() : null);

                invAddingOrder.setOrganizationSiteId(invAddingOrderEntity.getSupplierId());
                invAddingOrder.setInvDelegatorId(invAddingOrderEntity.getDelegatorId());
                invAddingOrder.setPurchaseOrderId(invAddingOrderEntity.getPurchaseOrderId());
                if (invAddingOrder.getPurchaseOrderId() != null) {
                    PurchaseOrderNotLoadedFromInvAddingOrder notLoadedFromInvAddingOrder = new PurchaseOrderNotLoadedFromInvAddingOrder();
                    notLoadedFromInvAddingOrder.setBranchId(invAddingOrder.getPurchaseOrderId().getBranchId().getId());
                    notLoadedFromInvAddingOrder.setPurchaseId(invAddingOrder.getPurchaseOrderId().getId());
                    notLoadedFromInvAddingOrder.setSerial(invAddingOrder.getPurchaseOrderId().getSerial());
                    getInvAddingOrderParent().loadDefaultInvPurchaseOrderData(invAddingOrderEntity);
                    invAddingOrderEntity.setPurchaseOrderNLoaded(notLoadedFromInvAddingOrder);
                }

                invAddingOrder.setInvInventoryId(invAddingOrderEntity.getInvInventory());
                InvAddingorderDetail invAddingOrderDetailUpdated;
                InvAddingorderDetail invAddingOrderDetailExist;

                invAddingorderDetailListUpdated = new ArrayList<>();
                invAddingorderDetailListDeleted = new ArrayList<>();
                updatedPurchaseOrAddingOrderList = new ArrayList<>();
                InvItem invItem;
                for (InvAddingOrderDetailsEntity orderDetailsEntity : invAddingOrderDetailsEntityList) {
                    invAddingOrderDetailUpdated = new InvAddingorderDetail();

                    if (invAddingOrderEntity.getPurchaseOrderNLoaded() != null) {
                        if (orderDetailsEntity.getPurchaseOrderDetailTrans() == null) {
                            errorMessage("يجب ادخال الصنف");
                            return null;
                        } else {
                            invAddingOrderDetailUpdated.setItemBarcode(orderDetailsEntity.getItemId().getCode());
                            invAddingOrderDetailUpdated.setItemId(orderDetailsEntity.getItemId());

                            //  invAddingOrderDetailUpdated.set(orderDetailsEntity.getPurchaseOrderDetailTrans());
                        }
                    } else {

                        if (orderDetailsEntity.getItemsBarcodesDetail() == null && orderDetailsEntity.getItemsBarcodesDetailTrans() == null) {
                            errorMessage("يجب ادخال الصنف");
                            showMessageDetails = true;
                            return "";
                        } else {
                            invItem = new InvItem();
//                        invItem.setId(orderDetailsEntity.getItemsBarcodesDetailTrans().getItemId());
                            Symbol unit = new Symbol();

                            unit.setId(orderDetailsEntity.getItemsBarcodesDetailTrans().getUnitId());
                            unit.setName(orderDetailsEntity.getItemsBarcodesDetailTrans().getUnitName());

                            invItem.setId(orderDetailsEntity.getItemsBarcodesDetailTrans().getItemId());
                            invItem.setName(orderDetailsEntity.getItemsBarcodesDetailTrans().getName());
                            invItem.setCode(orderDetailsEntity.getItemsBarcodesDetailTrans().getBarcode());
                            invItem.setUnitId(unit);

                            invAddingOrderDetailUpdated.setItemId(invItem);

                            invAddingOrderDetailUpdated.setItemBarcode(orderDetailsEntity.getItemsBarcodesDetailTrans().getBarcode());
                            orderDetailsEntity.setItemsBarcodesDetail(orderDetailsEntity.getItemsBarcodesDetailTrans());
                        }
                    }

                    invAddingOrderDetailUpdated.setSelectedPurchaseOrderId(orderDetailsEntity.getPurchaseOrderDetail());

                    if (invAddingOrderDetailUpdated.getSelectedId() != null) {
                        if (validateQuantity(orderDetailsEntity)) {
                            invAddingOrderDetailUpdated.setQuantity(orderDetailsEntity.getQuantity());
                            invAddingOrderDetailUpdated.setFinalQuantity(orderDetailsEntity.getQuantity());
                        } else {
                            errorMessage("يجب ادخال كمية");
                            return "";
                        }
                    } else {
                        invAddingOrderDetailUpdated.setQuantity(orderDetailsEntity.getQuantity());
                        invAddingOrderDetailUpdated.setFinalQuantity(orderDetailsEntity.getQuantity());
                    }

                    if (orderDetailsEntity.getId() != null) {
                        invAddingOrderDetailExist = new InvAddingorderDetail();
                        invAddingOrderDetailUpdated.setId(orderDetailsEntity.getId());
                        invAddingOrderDetailExist = invAddingorderDetailsService.findInvAddingOrderDetailsById(orderDetailsEntity.getId());
                        invAddingOrderDetailUpdated.setCreatedBy(invAddingOrderDetailExist.getCreatedBy());
                        invAddingOrderDetailUpdated.setCreationDate(invAddingOrderDetailExist.getCreationDate());
                        invAddingOrderDetailUpdated.setModificationDate(new Date());
                        invAddingOrderDetailUpdated.setModifiedBy(getUserData().getUser());

                    } else {
                        invAddingOrderDetailUpdated.setCreatedBy(getUserData().getUser());
                        invAddingOrderDetailUpdated.setCreationDate(new Date());
                        invAddingOrderDetailUpdated.setStatus(0);
                    }

                    invAddingOrderDetailUpdated.setBranchId(getUserData().getUserBranch());
                    invAddingOrderDetailUpdated.setCompanyId(getUserData().getCompany());
                    invAddingOrderDetailUpdated.setTransferFrom(orderDetailsEntity.getTransferFrom());

                    invAddingOrderDetailUpdated.setScrewing(orderDetailsEntity.getScrewing());
                    if (isPermission) {
                        invAddingOrderDetailUpdated.setSelectedId(orderDetailsEntity.getSelectedId() == null ? null : orderDetailsEntity.getSelectedId());
                    } else {
                        invAddingOrderDetailUpdated.setSelectedPurchaseId(orderDetailsEntity.getSelectedPurchaseId() == null ? null : orderDetailsEntity.getSelectedPurchaseId());

                    }

                    invAddingorderDetailListUpdated.add(invAddingOrderDetailUpdated);
                }

                if (!getRowsDeleted().isEmpty() && getRowsDeleted() != null) {
                    for (InvAddingOrderDetailsEntity rowDeleted : getRowsDeleted()) {
                        invAddingOrderDetailUpdated = new InvAddingorderDetail();
                        invAddingOrderDetailUpdated.setId(rowDeleted.getId());
                        invAddingOrderDetailUpdated.setTransferFrom(rowDeleted.getTransferFrom() != null ? rowDeleted.getTransferFrom() : null);
                        if (isPermission) {

                            invAddingOrderDetailUpdated.setSelectedId(rowDeleted.getSelectedId() != null ? rowDeleted.getSelectedId() : null);

                        } else {

                            invAddingOrderDetailUpdated.setSelectedPurchaseId(rowDeleted.getSelectedPurchaseId() != null ? rowDeleted.getSelectedPurchaseId() : null);

                        }
                        invAddingorderDetailListDeleted.add(invAddingOrderDetailUpdated);
                    }
                }
                InvPurchaseReturnSave invPurchaseReturnSave;
                invPurchaseReturnSave = invAddingOrderService.addInvAddingOrder(invAddingOrder, invAddingorderDetailListUpdated, invAddingorderDetailListDeleted, updatedPurchaseOrAddingOrderList, headIdList, isPermission);
                //     invAddingOrderService.addInvAddingOrder(invAddingOrder, invAddingorderDetailListUpdated, invAddingorderDetailListDeleted);

                if (invPurchaseReturnSave.getMsgAdding() != null && !invPurchaseReturnSave.getMsgAdding().isEmpty()) {
                    errorMessage("يجب ادخال كمية");
                    return "";
                } else {
                    resetadding();

                    invAddingOrder = invPurchaseReturnSave.getInvAddingorder();
                    invAddingUpdated = invAddingOrder;
                    invAddingorderDetailList = invPurchaseReturnSave.getInvAddingorderDetailList();

                    invAddingOrderEntity = getInvAddingOrderParent().mapInvAddingOrderToInvAddingOrderEntity(invAddingOrder, invAddingorderDetailList, invAddingOrderEntity, invAddingOrderEntityRetrieved, invAddingOrderDetailsEntity, isPermission, itemsBarcodeMap, totalQuatity, invAddingOrderDetailsEntityList, invAddingOrderDetailsEntityListFirstBackup, headIdList);

                    if (invAddingOrder.getPurchaseOrderId() != null) {
                        getInvAddingOrderParent().initPurchaseOrder(invPurchaseOrdersDetailList, invAddingOrderEntity, invPurchaseOrderDetailConverter, invAddingOrderDetailsEntity, invAddingOrderDetailsEntityList);
                    }

                    getInvAddingOrderParent().fillItemMap(invAddingOrderEntity, invAddingOrderDetailsEntityList, isPermission, getInvSetup(), itemsMap, branchId);
                }
                savedMeesage(getUserData().getUserDDs().get("SAVED"));
            } else {
                savedMeesage(getUserData().getUserDDs().get("ERROR"));
            }
            return "";
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "save");
            return null;
        }
    }

    /**
     * @return the invSetup
     */
    public InventorySetup getInvSetup() {
        if (invSetup == null || invSetup.getId() == null) {
            setInvSetup(inventorySetupService.getInventoryByBranchId(getUserData().getUserBranch().getId()));
        }
        return invSetup;
    }

    /**
     * @param invSetup the invSetup to set
     */
    public void setInvSetup(InventorySetup invSetup) {
        this.invSetup = invSetup;
    }

    public void reset() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            context.getSessionMap().replace("ScreenMode", "Add");
            setScreenMode("Add");

            init();
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "reset");
        }
    }

    public void resetadding() {
        try {
            invAddingOrder = new InvAddingorder();
            invAddingorderDetailListUpdated = new ArrayList<>();
            invAddingorderDetailListDeleted = new ArrayList<>();
            updatedPurchaseOrAddingOrderList = new ArrayList<>();
        //   invPurchaseInvoiceSelectedListBackup = new ArrayList<>();
            //    invPurchaseInvoiceSelectedListDeleted = new ArrayList<>();

            //   invAddingorderDetailList = new ArrayList<>();
            headIdList = new StringBuilder();
//        isPermission = false;
            invAddingOrderEntity.setSerial(null);
            invAddingOrderEntity.setId(null);
            invAddingOrderDetailsEntityList = new ArrayList<>();
            totalQuatity = BigDecimal.ZERO;
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "resetadding");
        }

    }
    //------------------------------------

    public List<InvPurchaseOrderDetail> completePurchaseOrderDetail(String query) {
        try {
            List<InvPurchaseOrderDetail> orderDetails = getInvPurchaseOrdersDetailList();
            if (query == null || query.trim().equals("")) {
                invPurchaseOrderDetailConverter = new InvPurchaseOrderDetailConverter(orderDetails);
                return orderDetails;
            }
            List<InvPurchaseOrderDetail> filteredItems = new ArrayList<>();

            String nameAr;
            String code;
            InvPurchaseOrderDetail purchaseOrderDetail;
            for (int i = 0; i < invPurchaseOrdersDetailList.size(); i++) {
                purchaseOrderDetail = orderDetails.get(i);
                nameAr = purchaseOrderDetail.getItemId().getName();
                if (nameAr != null && nameAr.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(purchaseOrderDetail)) {
                        filteredItems.add(purchaseOrderDetail);
                    }
                }

                code = purchaseOrderDetail.getItemId().getCode();
                if (code != null && code.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(purchaseOrderDetail)) {
                        filteredItems.add(purchaseOrderDetail);
                    }
                }
            }

            invPurchaseOrderDetailConverter = new InvPurchaseOrderDetailConverter(filteredItems);
            return filteredItems;
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "completePurchaseOrderDetail");
            return null;
        }
    }

    public void print() {
        try {
            getInvAddingOrderParent().print(invAddingOrderEntity, invAddingOrderDetailsEntityList, getUserData(), isPermission);
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "print");
        }
    }
    //------------------------------------    

    public List<InvPurchaseOrder> completePurchaseNumber(String query) {
        try {
            List<InvPurchaseOrder> supplierList = getInvPurchaseOrdersList();
            if (query == null || query.trim().equals("")) {

                invPurchaseOrderConverter = new InvPurchaseOrderConverter(supplierList);
                return supplierList;
            }
            List<InvPurchaseOrder> filteredSuppliers = new ArrayList<>();

            Integer code;
            InvPurchaseOrder supplierFilter;
            for (int i = 0; i < invPurchaseOrdersList.size(); i++) {
                supplierFilter = supplierList.get(i);

                code = supplierFilter.getId();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase()) && !filteredSuppliers.contains(supplierFilter)) {
                    filteredSuppliers.add(supplierFilter);
                }
            }

            invPurchaseOrderConverter = new InvPurchaseOrderConverter(filteredSuppliers);
            return filteredSuppliers;
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "completePurchaseNumber");
            return null;
        }
    }

    public List<PurchaseOrderNotLoadedFromInvAddingOrder> completePurchaseOrderN(String query) {
        try {
            List<PurchaseOrderNotLoadedFromInvAddingOrder> supplierList = getPurchaseOrderNotLoadedFromInvAddingOrderList();
            if (query == null || query.trim().equals("")) {

                setPurchaseOrderNotLoadedViewConverter(new PurchaseOrderNotLoadedViewConverter(supplierList));
                return supplierList;
            }
            List<PurchaseOrderNotLoadedFromInvAddingOrder> filteredSuppliers = new ArrayList<>();

            Integer code;
            PurchaseOrderNotLoadedFromInvAddingOrder supplierFilter;
            for (int i = 0; i < purchaseOrderNotLoadedFromInvAddingOrderList.size(); i++) {
                supplierFilter = supplierList.get(i);

                code = supplierFilter.getSerial();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase()) && !filteredSuppliers.contains(supplierFilter)) {
                    filteredSuppliers.add(supplierFilter);
                }
            }

            setPurchaseOrderNotLoadedViewConverter(new PurchaseOrderNotLoadedViewConverter(filteredSuppliers));
            return filteredSuppliers;
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "completePurchaseOrderN");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        try {
            if (isPermission) {
                exit("../invpermissionorder/invpermissionorderlist.xhtml");
            } else {
                exit("../invaddingorder1/invAddingOrderList1.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "exit");
            return null;
        }
    }

    public void configmDlg() {
        try {
            if (invAddingOrderEntity.getPurchaseOrderNLoaded() != null) {
                invAddingOrderDetailsEntityList = new ArrayList<>();
                getInvAddingOrderParent().initPurchaseOrder(invPurchaseOrdersDetailList, invAddingOrderEntity, invPurchaseOrderDetailConverter, invAddingOrderDetailsEntity, invAddingOrderDetailsEntityList);
                CloseDlg("dlg2");
            } else {
                invAddingOrderDetailsEntityList = new ArrayList<>();
                invAddingOrderEntity.setPurchaseOrderNLoaded(null);
                invAddingOrderEntity.setPurchaseOrderNLoadedTrans(null);
                CloseDlg("dlg1");
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "configmDlg");
        }
    }

    public void CloseDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
            invAddingOrderEntity.setPurchaseOrderNLoaded(invAddingOrderEntity.getPurchaseOrderNLoadedTrans());
        } catch (Exception e) {
            saveError(e, "invAddingOrderFormMB", "CloseDlg");
        }
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public InvAddingOrderDetailsEntity getInvAddingOrderDetailsEntity() {
        if (invAddingOrderDetailsEntity == null) {
            invAddingOrderDetailsEntity = new InvAddingOrderDetailsEntity();
        }
        return invAddingOrderDetailsEntity;
    }

    public void setInvAddingOrderDetailsEntity(InvAddingOrderDetailsEntity invAddingOrderDetailsEntity) {
        this.invAddingOrderDetailsEntity = invAddingOrderDetailsEntity;
    }

    public InvAddingOrderEntity getInvAddingOrderEntity() {
//        if (invAddingOrderEntity == null) {
//            invAddingOrderEntity = new InvAddingOrderEntity();
//        }
        return invAddingOrderEntity;
    }

    public void setInvAddingOrderEntity(InvAddingOrderEntity invAddingOrderEntity) {
        this.invAddingOrderEntity = invAddingOrderEntity;
    }

    public InvAddingorder getInvAddingOrder() {
        if (invAddingOrder == null) {
            invAddingOrder = new InvAddingorder();
        }
        return invAddingOrder;
    }

    public void setInvAddingOrder(InvAddingorder invAddingOrder) {
        this.invAddingOrder = invAddingOrder;
    }

    public InvAddingorderDetail getInvAddingOrderDetail() {
        if (invAddingOrderDetail == null) {
            invAddingOrderDetail = new InvAddingorderDetail();
        }
        return invAddingOrderDetail;
    }

    public void setInvAddingOrderDetail(InvAddingorderDetail invAddingOrderDetail) {
        this.invAddingOrderDetail = invAddingOrderDetail;
    }

    public List<InvItem> getInvItemsList() {
        if (invItemsList == null || invItemsList.isEmpty()) {
            invItemsList = new ArrayList<>();
        }
        return invItemsList;
    }

    public void setInvItemsList(List<InvItem> invItemsList) {
        this.invItemsList = invItemsList;
    }

    public List<ItemDataEntity> getItemsDataEntityList() {
        if (itemsDataEntityList == null || itemsDataEntityList.isEmpty()) {
            itemsDataEntityList = new ArrayList<>();
        }
        return itemsDataEntityList;
    }

    public void setItemsDataEntityList(List<ItemDataEntity> itemsDataEntityList) {
        this.itemsDataEntityList = itemsDataEntityList;
    }

    public List<InvAddingOrderDetailsEntity> getInvAddingOrderDetailsEntityList() {
        if (invAddingOrderDetailsEntityList == null || invAddingOrderDetailsEntityList.isEmpty()) {
            invAddingOrderDetailsEntityList = new ArrayList<>();
        }
        return invAddingOrderDetailsEntityList;
    }

    public void setInvAddingOrderDetailsEntityList(List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityList) {
        this.invAddingOrderDetailsEntityList = invAddingOrderDetailsEntityList;
    }

    public List<InvAddingOrderEntity> getInvAddingOrderEntityList() {
        if (invAddingOrderEntityList == null || invAddingOrderEntityList.isEmpty()) {
            invAddingOrderEntityList = new ArrayList<>();
        }
        return invAddingOrderEntityList;
    }

    public void setInvAddingOrderEntityList(List<InvAddingOrderEntity> invAddingOrderEntityList) {
        this.invAddingOrderEntityList = invAddingOrderEntityList;
    }

    public ItemDataEntity getItemDataEntity() {
        if (itemDataEntity == null) {
            itemDataEntity = new ItemDataEntity();
        }
        return itemDataEntity;
    }

    public void setItemDataEntity(ItemDataEntity itemDataEntity) {
        this.itemDataEntity = itemDataEntity;
    }

    public InvAddingOrderDetailsEntity getInvAddingOrderDetailsEntitySelected() {
        if (invAddingOrderDetailsEntitySelected == null) {
            invAddingOrderDetailsEntitySelected = new InvAddingOrderDetailsEntity();
        }
        return invAddingOrderDetailsEntitySelected;
    }

    public void setInvAddingOrderDetailsEntitySelected(InvAddingOrderDetailsEntity invAddingOrderDetailsEntitySelected) {
        this.invAddingOrderDetailsEntitySelected = invAddingOrderDetailsEntitySelected;
    }

    public Integer getInvAddingOrderId() {
        return invAddingOrderId;
    }

    public void setInvAddingOrderId(Integer invAddingOrderId) {
        this.invAddingOrderId = invAddingOrderId;
    }

    public List<InvAddingorderDetail> getInvAddingorderDetailList() {
        if (invAddingorderDetailList == null || invAddingorderDetailList.isEmpty()) {
            invAddingorderDetailList = new ArrayList<>();
        }
        return invAddingorderDetailList;
    }

    public void setInvAddingorderDetailList(List<InvAddingorderDetail> invAddingorderDetailList) {
        this.invAddingorderDetailList = invAddingorderDetailList;
    }

    public BigDecimal getTotalQuatity() {
        if (totalQuatity == null) {
            totalQuatity = BigDecimal.ZERO;
        }
        return totalQuatity;
    }

    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
    }

    public List<InvAddingOrderDetailsEntity> getRowsDeleted() {
        if (rowsDeleted == null || rowsDeleted.isEmpty()) {
            rowsDeleted = new ArrayList<>();
        }
        return rowsDeleted;
    }

    public void setRowsDeleted(List<InvAddingOrderDetailsEntity> rowsDeleted) {
        this.rowsDeleted = rowsDeleted;
    }

    public InvAddingOrderEntity getInvAddingOrderEntityRetrieved() {
        if (invAddingOrderEntityRetrieved == null) {
            invAddingOrderEntityRetrieved = new InvAddingOrderEntity();
        }
        return invAddingOrderEntityRetrieved;
    }

    public void setInvAddingOrderEntityRetrieved(InvAddingOrderEntity invAddingOrderEntityRetrieved) {
        this.invAddingOrderEntityRetrieved = invAddingOrderEntityRetrieved;
    }

    public List<InvAddingorderDetail> getInvAddingorderDetailListUpdated() {
        if (invAddingorderDetailListUpdated == null || invAddingorderDetailListUpdated.isEmpty()) {
            invAddingorderDetailListUpdated = new ArrayList<>();
        }
        return invAddingorderDetailListUpdated;
    }

    public void setInvAddingorderDetailListUpdated(List<InvAddingorderDetail> invAddingorderDetailListUpdated) {
        this.invAddingorderDetailListUpdated = invAddingorderDetailListUpdated;
    }

    public List<InvAddingorderDetail> getInvAddingorderDetailListDeleted() {
        if (invAddingorderDetailListDeleted == null || invAddingorderDetailListDeleted.isEmpty()) {
            invAddingorderDetailListDeleted = new ArrayList<>();
        }
        return invAddingorderDetailListDeleted;
    }

    public void setInvAddingorderDetailListDeleted(List<InvAddingorderDetail> invAddingorderDetailListDeleted) {
        this.invAddingorderDetailListDeleted = invAddingorderDetailListDeleted;
    }

    public List<InvPurchaseOrder> getInvPurchaseOrdersList() {
        if (invPurchaseOrdersList == null || invPurchaseOrdersList.isEmpty()) {
            invPurchaseOrdersList = new ArrayList<>();
        }
        return invPurchaseOrdersList;
    }

    public void setInvPurchaseOrdersList(List<InvPurchaseOrder> invPurchaseOrdersList) {
        this.invPurchaseOrdersList = invPurchaseOrdersList;
    }

    public InvAddingorder getInvAddingUpdated() {
        if (invAddingUpdated == null) {
            invAddingUpdated = new InvAddingorder();
        }
        return invAddingUpdated;
    }

    public void setInvAddingUpdated(InvAddingorder invAddingUpdated) {
        this.invAddingUpdated = invAddingUpdated;
    }

    public InvPurchaseOrderConverter getInvPurchaseOrderConverter() {
        if (invPurchaseOrderConverter == null) {
            invPurchaseOrderConverter = new InvPurchaseOrderConverter(invPurchaseOrdersList);
        }
        return invPurchaseOrderConverter;
    }

    public void setInvPurchaseOrderConverter(InvPurchaseOrderConverter invPurchaseOrderConverter) {
        this.invPurchaseOrderConverter = invPurchaseOrderConverter;
    }

    /**
     * @return the invPurchaseInvoiceConverter
     */
    public InvPurchaseInvoiceConverter getInvPurchaseInvoiceConverter() {
        if (invPurchaseInvoiceConverter == null) {
            invPurchaseInvoiceConverter = new InvPurchaseInvoiceConverter(invPurchaseInvoiceList);
        }
        return invPurchaseInvoiceConverter;
    }

    /**
     * @param invPurchaseInvoiceConverter the invPurchaseInvoiceConverter to set
     */
    public void setInvPurchaseInvoiceConverter(InvPurchaseInvoiceConverter invPurchaseInvoiceConverter) {
        this.invPurchaseInvoiceConverter = invPurchaseInvoiceConverter;
    }

    /**
     * @return the invPurchaseInvoiceSelectedList
     */
    public List<InvPurchaseInvoice> getInvPurchaseInvoiceSelectedList() {
        if (invPurchaseInvoiceSelectedList == null || invPurchaseInvoiceSelectedList.isEmpty()) {
            invPurchaseInvoiceSelectedList = new ArrayList<>();
        }
        return invPurchaseInvoiceSelectedList;
    }

    /**
     * @param invPurchaseInvoiceSelectedList the invPurchaseInvoiceSelectedList
     * to set
     */
    public void setInvPurchaseInvoiceSelectedList(List<InvPurchaseInvoice> invPurchaseInvoiceSelectedList) {
        this.invPurchaseInvoiceSelectedList = invPurchaseInvoiceSelectedList;
    }

    /**
     * @return the hundred
     */
    public BigDecimal getHundred() {
        return hundred;
    }

    /**
     * @param hundred the hundred to set
     */
    public void setHundred(BigDecimal hundred) {
        this.hundred = hundred;
    }

    /**
     * @return the showDialogMessage
     */
    public Boolean getShowDialogMessage() {
        return showDialogMessage;
    }

    /**
     * @param showDialogMessage the showDialogMessage to set
     */
    public void setShowDialogMessage(Boolean showDialogMessage) {
        this.showDialogMessage = showDialogMessage;
    }

    public Boolean getShowMessageDetails() {
        return showMessageDetails;
    }

    public void setShowMessageDetails(Boolean showMessageDetails) {
        this.showMessageDetails = showMessageDetails;
    }

    public Boolean getShowMessageGeneral() {
        return showMessageGeneral;
    }

    public void setShowMessageGeneral(Boolean showMessageGeneral) {
        this.showMessageGeneral = showMessageGeneral;
    }

    /**
     * @return the invPurchaseInvoiceListad
     */
    public List<InvPurchaseInvoice> getInvPurchaseInvoiceList() {
        if (invPurchaseInvoiceList == null || invPurchaseInvoiceList.isEmpty()) {
            invPurchaseInvoiceList = new ArrayList<>();
        }
        return invPurchaseInvoiceList;
    }

    /**
     * @param invPurchaseInvoiceList the invPurchaseInvoiceList to set
     */
    public void setInvPurchaseInvoiceList(List<InvPurchaseInvoice> invPurchaseInvoiceList) {
        this.invPurchaseInvoiceList = invPurchaseInvoiceList;
    }

    /**
     * @return the invPurchaseOrdersDetailList
     */
    public List<InvPurchaseOrderDetail> getInvPurchaseOrdersDetailList() {
        if (invAddingOrderEntity.getPurchaseOrderNLoaded() != null) {
            invPurchaseOrdersDetailList = invPurchaseOrderDetailsService.getAllPurchaseOrderDetailsByInvPurchaseOrderId(invAddingOrderEntity.getPurchaseOrderNLoaded().getPurchaseId());
            invPurchaseOrderDetailConverter = new InvPurchaseOrderDetailConverter(invPurchaseOrdersDetailList);
        }
        return invPurchaseOrdersDetailList;
    }

    /**
     * @param invPurchaseOrdersDetailList the invPurchaseOrdersDetailList to set
     */
    public void setInvPurchaseOrdersDetailList(List<InvPurchaseOrderDetail> invPurchaseOrdersDetailList) {
        this.invPurchaseOrdersDetailList = invPurchaseOrdersDetailList;
    }

    /**
     * @return the invPurchaseOrderDetailConverter
     */
    public InvPurchaseOrderDetailConverter getInvPurchaseOrderDetailConverter() {
        return invPurchaseOrderDetailConverter;
    }

    /**
     * @param invPurchaseOrderDetailConverter the
     * invPurchaseOrderDetailConverter to set
     */
    public void setInvPurchaseOrderDetailConverter(InvPurchaseOrderDetailConverter invPurchaseOrderDetailConverter) {
        this.invPurchaseOrderDetailConverter = invPurchaseOrderDetailConverter;
    }

    /**
     * @return the purchaseOrderNotLoadedFromInvAddingOrderList
     */
    public List<PurchaseOrderNotLoadedFromInvAddingOrder> getPurchaseOrderNotLoadedFromInvAddingOrderList() {
        if (purchaseOrderNotLoadedFromInvAddingOrderList == null || purchaseOrderNotLoadedFromInvAddingOrderList.isEmpty()) {
            purchaseOrderNotLoadedFromInvAddingOrderList = new ArrayList<>();
        }
        return purchaseOrderNotLoadedFromInvAddingOrderList;
    }

    /**
     * @param purchaseOrderNotLoadedFromInvAddingOrderList the
     * purchaseOrderNotLoadedFromInvAddingOrderList to set
     */
    public void setPurchaseOrderNotLoadedFromInvAddingOrderList(List<PurchaseOrderNotLoadedFromInvAddingOrder> purchaseOrderNotLoadedFromInvAddingOrderList) {
        this.purchaseOrderNotLoadedFromInvAddingOrderList = purchaseOrderNotLoadedFromInvAddingOrderList;
    }

    /**
     * @return the invAddingOrderEntityBackList
     */
    public List<InvAddingOrderDetailsEntity> getInvAddingOrderEntityBackList() {
        if (invAddingOrderEntityBackList == null || invAddingOrderEntityBackList.isEmpty()) {
            invAddingOrderEntityBackList = new ArrayList<>();
        }
        return invAddingOrderEntityBackList;
    }

    /**
     * @param invAddingOrderEntityBackList the invAddingOrderEntityBackList to
     * set
     */
    public void setInvAddingOrderEntityBackList(List<InvAddingOrderDetailsEntity> invAddingOrderEntityBackList) {
        this.invAddingOrderEntityBackList = invAddingOrderEntityBackList;
    }

    /**
     * @return the purchaseOrderNotLoadedViewConverter
     */
    public PurchaseOrderNotLoadedViewConverter getPurchaseOrderNotLoadedViewConverter() {
        if (purchaseOrderNotLoadedViewConverter == null) {
            purchaseOrderNotLoadedViewConverter = new PurchaseOrderNotLoadedViewConverter(purchaseOrderNotLoadedFromInvAddingOrderList);
        }
        return purchaseOrderNotLoadedViewConverter;
    }

    /**
     * @param purchaseOrderNotLoadedViewConverter the
     * purchaseOrderNotLoadedViewConverter to set
     */
    public void setPurchaseOrderNotLoadedViewConverter(PurchaseOrderNotLoadedViewConverter purchaseOrderNotLoadedViewConverter) {
        this.purchaseOrderNotLoadedViewConverter = purchaseOrderNotLoadedViewConverter;
    }

    /**
     * @return the invAddingOrderParent
     */
    public InvAddingOrderParent getInvAddingOrderParent() {
        if (invAddingOrderParent == null) {
            invAddingOrderParent = new InvAddingOrderParent(invAddingOrderService, itemsBarcodesDetailsViewService,
                    invAddingorderDetailsService,
                    quantityItemsStoreService,
                    invPurchaseOrderDetailsService);
        }
        return invAddingOrderParent;
    }

    /**
     * @param invAddingOrderParent the invAddingOrderParent to set
     */
    public void setInvAddingOrderParent(InvAddingOrderParent invAddingOrderParent) {
        this.invAddingOrderParent = invAddingOrderParent;
    }

}

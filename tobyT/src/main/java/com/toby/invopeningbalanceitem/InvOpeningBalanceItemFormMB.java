/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invopeningbalanceitem;

import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.InvDelegatorService;
import com.toby.businessservice.InvItemService;
import com.toby.businessservice.InvOpeningBalanceItemService;
import com.toby.businessservice.InvQuotationDetailsService;
import com.toby.businessservice.ItemsBarcodesDetailsViewService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.converter.InvDelegatorConvertor;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.ItemConverter;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvOpenningBalanceItem;
import com.toby.entity.InvOpenningBalanceItemDetail;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvQotationDetail;
import com.toby.entity.InventoryDelegator;
import com.toby.entiy.InvOpeningBalanceItemDetailEntity;
import com.toby.entiy.InvOpeningBalanceItemEntity;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author hq002
 */
@Named(value = "InvOpeningBalanceItemFormMB")
@ViewScoped
public class InvOpeningBalanceItemFormMB extends BaseFormBean {

    private List<InventoryDelegator> inventoryDelegatorList;
    private List<InvInventory> invInventoryList;
    private List<InvItem> invItemList;

    private List<InvOrganizationSite> customerAndSupplierList;

    private InvOpenningBalanceItem invOpeningBalanceItem;
//    private List<InvQotation> InvQotations;

    private InvQotationDetail invQotationDetail;
    private List<InvQotationDetail> invQotationDetails;

    private InvOpeningBalanceItemEntity invOpeningBalanceItemEntity;
    private InvInventoryConverter invInventoryConverter;
    private InvOpeningBalanceItemDetailEntity invOpenningBalanceItemDetailEntity;
    private InvOpeningBalanceItemDetailEntity invOpenningBalanceItemDetailEntitySelection;
    private List<InvOpeningBalanceItemDetailEntity> invOpenningBalanceItemDetailEntityList;
    private List<InvOpeningBalanceItemDetailEntity> rowDeleted;

    private List<InvOpenningBalanceItemDetail> invOpenningBalanceItemDetailsList;
    private List<InvOpenningBalanceItemDetail> invOpenningBalanceItemDeletedList;
    private List<InvOpenningBalanceItemDetail> invOpenningBalanceItemsList;

    private Integer invQotationId;

    private BigDecimal totalPrice = BigDecimal.ZERO;
    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal totalNet = BigDecimal.ZERO;
    BigDecimal hundred = new BigDecimal(100);

    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private boolean showMessage = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

    private ItemConverter itemConverter;
    private InvDelegatorConvertor invDelegatorConverter;
    private InvOrganizationSiteConverter organizationSiteConverter;

    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;
//    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;

    ExternalContext context;

    Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();

    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private InvOpeningBalanceItemService invOpenningBalanceItemService;
    @EJB
    private InvQuotationDetailsService invQuotationDetailsService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "init");
        }

    }

    @Override
    public void load() {
        try {

            initObjs();

            fillLists();

//            invQotationDetails = invQuotationDetailsService.getAllInvQotationDetails(invOpeningBalanceItem.getId());

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetItemDataForm();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    invOpenningBalanceItemDetailEntitySelection = new InvOpeningBalanceItemDetailEntity();

                    invQotationId = (Integer) context.getSessionMap().get("invOpenningBalanceItemSelected");

                    invOpeningBalanceItem = new InvOpenningBalanceItem();
                    invOpeningBalanceItem = invOpenningBalanceItemService.findInvOpeningBalanceItemByInvOpeningBalanceItemId(invQotationId);
                    invOpenningBalanceItemDetailsList = invOpenningBalanceItemService.getALLInvOpeningBalanceItemDetailsByInvOpeningBalanceItem(invQotationId);

                    invOpeningBalanceItemEntity = mapInvOpenningBalanceItemToEntity(invOpeningBalanceItem);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fillItemList();
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "load");
        }
    }

    private void initObjs() {
        try {
            totalPrice = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            total = BigDecimal.ZERO;

            invOpeningBalanceItem = new InvOpenningBalanceItem();
            invQotationDetail = new InvQotationDetail();
            invOpeningBalanceItemEntity = new InvOpeningBalanceItemEntity();
            invOpenningBalanceItemDetailEntity = new InvOpeningBalanceItemDetailEntity();
            invOpenningBalanceItemsList = new ArrayList<>();
            inventoryDelegatorList = new ArrayList<>();
            customerAndSupplierList = new ArrayList<>();
            invItemList = new ArrayList<>();
            invOpenningBalanceItemDetailsList = new ArrayList<>();
            invOpenningBalanceItemDeletedList = new ArrayList<>();
            invOpenningBalanceItemDetailEntitySelection = new InvOpeningBalanceItemDetailEntity();
            invOpenningBalanceItemDetailEntityList = new ArrayList<>();
            itemsBarcodesDetailsViewList = new ArrayList<>();
            invInventoryList = new ArrayList<>();
            rowDeleted = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "initObjs");
        }
    }

    private void fillLists() {
        try {
            invInventoryList = tobyUserInventoryService.getAllInventroisByUserAndBranch(getUserData().getUser().getId(), getUserData().getUserBranch().getId(),0);
            invInventoryConverter = new InvInventoryConverter(invInventoryList);

            //        inventoryDelegatorList = invDelegatorService.getSalesByBranch(branchId);
//        invDelegatorConverter = new InvDelegatorConvertor(inventoryDelegatorList);
//        customerAndSupplierList = organizationSiteService.getCustomerByBranchId(branchId);
//        organizationSiteConverter = new InvOrganizationSiteConverter(customerAndSupplierList);
//        invItemList = invItemService.getInvItemByBranchId(branchId);
//        itemConverter = new ItemConverter(invItemList);
//        itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
//        itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViewList);
//
//        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
//            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
//        }
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "fillLists");
        }

    }

    public void fillItemList() {
        try {
            if ((invItemList == null || invItemList.isEmpty()) && invOpeningBalanceItemEntity.getInvInventoryId() != null) {
                invItemList = invItemService.getInvItemListByBranchIdForOpeningBalanceItemForm(getUserData().getUserBranch().getId());
                itemConverter = new ItemConverter(invItemList);
            }
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "fillItemList");
        }
    }

    public void reset() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            context.getSessionMap().replace("ScreenMode", "Add");
            setScreenMode("Add");

            init();
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "reset");
        }
    }

    private void resetItemDataForm() {
        try {
            invOpeningBalanceItemEntity = new InvOpeningBalanceItemEntity();
            invOpeningBalanceItemEntity.setDate(new Date());
            invOpenningBalanceItemDetailsList = new ArrayList<>();
            invOpenningBalanceItemDetailEntitySelection = new InvOpeningBalanceItemDetailEntity();
            invOpenningBalanceItemDetailEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "resetItemDataForm");
        }
    }

    public InvOpeningBalanceItemEntity mapInvOpenningBalanceItemToEntity(InvOpenningBalanceItem invOpenningBalanceItem) {
        try {

            invOpeningBalanceItemEntity.setId(invOpenningBalanceItem.getId());
            invOpeningBalanceItemEntity.setSerial(invOpenningBalanceItem.getSerial());
            invOpeningBalanceItemEntity.setBranchId(invOpenningBalanceItem.getBranchId());
            invOpeningBalanceItemEntity.setDate(invOpenningBalanceItem.getDate());
            invOpeningBalanceItemEntity.setRemark(invOpenningBalanceItem.getRemark());
            invOpeningBalanceItemEntity.setCreatedBy(invOpenningBalanceItem.getCreatedBy());
            invOpeningBalanceItemEntity.setCreationDate(invOpenningBalanceItem.getCreationDate());
            invOpeningBalanceItemEntity.setDocumentStrip(invOpenningBalanceItem.getDocumentStrip());
            invOpeningBalanceItemEntity.setInvInventoryId(invOpenningBalanceItem.getInvInventoryId());

            invOpenningBalanceItemDetailEntityList = new ArrayList<>();

            totalQuatity = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;
            for (InvOpenningBalanceItemDetail itemDetail : invOpenningBalanceItemDetailsList) {
                invOpenningBalanceItemDetailEntity = new InvOpeningBalanceItemDetailEntity();
                invOpenningBalanceItemDetailEntity.setId(itemDetail.getId());
                invOpenningBalanceItemDetailEntity.setSerial(itemDetail.getSerial());
                invOpenningBalanceItemDetailEntity.setBranchId(itemDetail.getBranchId());
                invOpenningBalanceItemDetailEntity.setCreatedBy(itemDetail.getCreatedBy());
                invOpenningBalanceItemDetailEntity.setCreationDate(itemDetail.getCreationDate());
                invOpenningBalanceItemDetailEntity.setScrewing(BigDecimal.ONE);

                invOpenningBalanceItemDetailEntity.setItemId(itemDetail.getItemId());
                invOpenningBalanceItemDetailEntity.setItemIdTrans(itemDetail.getItemId());
                invOpenningBalanceItemDetailEntity.setQuantity(itemDetail.getQuantity());
                invOpenningBalanceItemDetailEntity.setCost(itemDetail.getCost());
                invOpenningBalanceItemDetailEntity.setNet(itemDetail.getQuantity().multiply(itemDetail.getCost()));

                totalQuatity = totalQuatity.add(itemDetail.getQuantity() != null ? itemDetail.getQuantity() : BigDecimal.ZERO);
                totalNet = totalNet.add(invOpenningBalanceItemDetailEntity.getNet() != null ? invOpenningBalanceItemDetailEntity.getNet() : BigDecimal.ZERO);

                invOpenningBalanceItemDetailEntityList.add(invOpenningBalanceItemDetailEntity);
            }

            return invOpeningBalanceItemEntity;
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "mapInvOpenningBalanceItemToEntity");
            return null;
        }
    }

    public void adddetails() {
        try {
            for (InvOpeningBalanceItemDetailEntity detailEntity : invOpenningBalanceItemDetailEntityList) {
                if (detailEntity.getItemId() == null && detailEntity.getItemIdTrans() != null) {
                    detailEntity.setItemId(detailEntity.getItemIdTrans());
                }

                if (detailEntity.getItemId() == null || !validateQuantity(detailEntity) || !validatePrice(detailEntity)) {
                    errorMessage("يجب ادخال تفاصيل الفاتورة");
                    return;
                }
                detailEntity.setMarkEdit(Boolean.FALSE);
            }

            InvOpeningBalanceItemDetailEntity quotationDetailsEntity = new InvOpeningBalanceItemDetailEntity();
            quotationDetailsEntity.setMarkEdit(Boolean.TRUE);
            invOpenningBalanceItemDetailEntitySelection = quotationDetailsEntity;
            invOpenningBalanceItemDetailEntityList.add(quotationDetailsEntity);
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "adddetails");
        }
    }

    public void deleteInvQuotationDetail() {
        try {

            if (invOpenningBalanceItemDetailEntitySelection != null) {
                if (invOpenningBalanceItemDetailEntitySelection.getId() != null) {
                    setDelete(Boolean.TRUE);
                    rowDeleted.add(invOpenningBalanceItemDetailEntitySelection);
                }

                for (InvOpeningBalanceItemDetailEntity detailEntity : invOpenningBalanceItemDetailEntityList) {
                    if (detailEntity.getItemId() == null && detailEntity.getItemIdTrans() != null) {
                        detailEntity.setItemId(detailEntity.getItemIdTrans());
                    }
                }
                invOpenningBalanceItemDetailEntityList.remove(invOpenningBalanceItemDetailEntitySelection);

                updateSummition();
            } else {
                errorMessage("يجب اختيار سطر للمسح");
            }
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "deleteInvQuotationDetail");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {
            Object oldValue = event.getOldValue() != null ? event.getOldValue() : null;
            Object newValue = event.getNewValue() != null ? event.getNewValue() : null;

            String column_name = event.getColumn().getClientId();

            DataTable dataTable = (DataTable) event.getSource();
            invOpenningBalanceItemDetailEntitySelection = (InvOpeningBalanceItemDetailEntity) dataTable.getRowData();

            if (column_name.contains("itemNumber") || column_name.contains("itemName")) {
                validateItems(invOpenningBalanceItemDetailEntity);
            }

            if (column_name.contains("Quantity")) {
//            if (!validateQuantity(newValue != null ? (BigDecimal) newValue : null)) {
//                invOpenningBalanceItemDetailEntitySelection.setQuantity((BigDecimal) oldValue);
//            }
            }

            if (column_name.contains("Price")) {
//            if (!validatePrice(newValue != null ? (BigDecimal) newValue : null)) {
//                invOpenningBalanceItemDetailEntitySelection.setPrice((BigDecimal) oldValue);
//            }
            }

            if (column_name.contains("Discount")) {
//            validateDiscount(newValue != null ? (BigDecimal) newValue : null);
            }

            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "onCellEdit");
        }

    }

    public void validatePriceColumn(InvOpeningBalanceItemDetailEntity invOpenningBalanceTable) {
        try {
            if (!validatePrice(invOpenningBalanceTable)) {
                invOpenningBalanceTable.setCost(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "validatePriceColumn");
        }
    }

    public Boolean validatePrice(InvOpeningBalanceItemDetailEntity invOpenningBalanceTable) {
        try {
            return invOpenningBalanceTable != null
                    && invOpenningBalanceTable.getCost() != null
                    && invOpenningBalanceTable.getCost().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "validatePrice");
            return null;
        }
    }

    public void validateQuantityColum(InvOpeningBalanceItemDetailEntity invOpenningBalanceTable) {
        try {
            if (!validateQuantity(invOpenningBalanceTable)) {
                if (invOpenningBalanceTable.getItemId() != null) {
                    invOpenningBalanceTable.setQuantity(BigDecimal.ZERO);
                } else {
                    invOpenningBalanceTable.setQuantity(null);
                }
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "validateQuantityColum");
        }
    }

    public Boolean validateQuantity(InvOpeningBalanceItemDetailEntity invOpenningBalanceTable) {
        try {
            return invOpenningBalanceTable != null && invOpenningBalanceTable.getItemId() != null
                    && invOpenningBalanceTable.getQuantity() != null
                    && invOpenningBalanceTable.getQuantity().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "validateQuantity");
            return null;
        }
    }

    public Boolean validateItems(InvOpeningBalanceItemDetailEntity invOpenningBalanceTable) {
        try {
            if (invOpenningBalanceTable.getItemId() == null) {
                resetInvItem(invOpenningBalanceTable);
                updateSummition();
            } else {

                invOpenningBalanceTable.setItemIdTrans(invOpenningBalanceTable.getItemId());
                if (invOpenningBalanceTable.getItemId().getUnitId() == null
                        || "".equals(invOpenningBalanceTable.getItemId().getUnitId().getName())) {
                    resetInvItem(invOpenningBalanceTable);
                    updateSummition();
                    return errorMessage("هذا الصنف ليس لديه وحدات");
                }
            }
            return true;
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "validateItems");
            return null;
        }
    }

    private void resetInvItem(InvOpeningBalanceItemDetailEntity invOpenningBalanceTable) {
        try {
            invOpenningBalanceTable.setQuantity(null);
            invOpenningBalanceTable.setCost(null);
            invOpenningBalanceTable.setUnitId(null);
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "resetInvItem");
        }
    }

    public void updateSummition() {
        try {

            BigDecimal quantity, cost;
            totalQuatity = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;

            for (InvOpeningBalanceItemDetailEntity detailEntity : invOpenningBalanceItemDetailEntityList) {
                if (detailEntity.getItemId() == null && detailEntity.getItemIdTrans() != null) {
                    detailEntity.setItemId(detailEntity.getItemIdTrans());
                }

                quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
                cost = detailEntity.getCost() != null ? detailEntity.getCost() : BigDecimal.ZERO;

                total = quantity.multiply(cost);
                detailEntity.setNet(total.setScale(2, BigDecimal.ROUND_UP));

                totalQuatity = totalQuatity.add(quantity);
                totalNet = (totalNet.add(detailEntity.getNet())).setScale(2, BigDecimal.ROUND_UP);
            }
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "updateSummition");
        }
    }

    @Override
    public String save() {
        try {

            invOpeningBalanceItem.setCompanyId(getUserData().getCompany());
            invOpeningBalanceItem.setBranchId(getUserData().getUserBranch());

            if (invOpeningBalanceItem.getId() == null) {
                invOpeningBalanceItem.setCreationDate(new Date());
                invOpeningBalanceItem.setCreatedBy(getUserData().getUser());
            } else {
                invOpeningBalanceItem.setModificationDate(new Date());
                invOpeningBalanceItem.setModifiedBy(getUserData().getUser());
            }

            invOpeningBalanceItem.setDate(invOpeningBalanceItemEntity.getDate());
            invOpeningBalanceItem.setRemark(invOpeningBalanceItemEntity.getRemark());
            invOpeningBalanceItem.setDocumentStrip(invOpeningBalanceItemEntity.getDocumentStrip());

            if (invOpeningBalanceItemEntity.getInvInventoryId() == null) {
                errorMessage("يجب اختيار مخزن");
                return "";
            } else {
                invOpeningBalanceItem.setInvInventoryId(invOpeningBalanceItemEntity.getInvInventoryId());
            }

            InvOpenningBalanceItemDetail balanceItemDetail;
            invOpenningBalanceItemsList = new ArrayList<>();
            if (invOpenningBalanceItemDetailEntityList != null && !invOpenningBalanceItemDetailEntityList.isEmpty()) {
                for (InvOpeningBalanceItemDetailEntity detailsEntity : invOpenningBalanceItemDetailEntityList) {
                    balanceItemDetail = new InvOpenningBalanceItemDetail();

                    if (detailsEntity.getId() != null) {
                        balanceItemDetail.setId(detailsEntity.getId());
                        balanceItemDetail.setSerial(detailsEntity.getSerial());
                        balanceItemDetail.setCreationDate(detailsEntity.getCreationDate());
                        balanceItemDetail.setCreatedBy(detailsEntity.getCreatedBy());
                        balanceItemDetail.setModificationDate(new Date());
                        balanceItemDetail.setModifiedBy(getUserData().getUser());

                    } else {
                        balanceItemDetail.setCreationDate(new Date());
                        balanceItemDetail.setCreatedBy(getUserData().getUser());
                    }

                    balanceItemDetail.setBranchId(getUserData().getUserBranch());
                    balanceItemDetail.setCompanyId(getUserData().getCompany());
                    balanceItemDetail.setScrewing(detailsEntity.getScrewing());

                    if (validatePrice(detailsEntity)) {
                        balanceItemDetail.setCost(detailsEntity.getCost());
                    } else {
                        errorMessage("يجب ادخال السعر");
                        return "";
                    }

                    if (detailsEntity.getItemId() == null && detailsEntity.getItemIdTrans() == null) {
                        errorMessage("يجب ادخال صنف");
                        return "";
                    } else {
                        detailsEntity.setItemId(detailsEntity.getItemIdTrans());
                        balanceItemDetail.setItemId(detailsEntity.getItemId());
                    }

                    if (validateQuantity(detailsEntity)) {
                        balanceItemDetail.setQuantity(detailsEntity.getQuantity());
                    } else {
                        errorMessage("يجب ادخال الكمية");
                        return "";
                    }

                    balanceItemDetail.setNet(detailsEntity.getNet());

                    invOpenningBalanceItemsList.add(balanceItemDetail);
                }
            } else {
                errorMessage("يجب ادخال عروض");
                return "";
            }

            if (rowDeleted != null) {
                InvOpenningBalanceItemDetail ipo;

                for (InvOpeningBalanceItemDetailEntity deletedDetailsEntity : rowDeleted) {
                    ipo = new InvOpenningBalanceItemDetail();
                    ipo.setId(deletedDetailsEntity.getId());
                    invOpenningBalanceItemDeletedList.add(ipo);
                }
            }
            InvPurchaseReturnSave invPurchaseReturnSave;
            invPurchaseReturnSave = invOpenningBalanceItemService.addInvOpeningBalanceItem(invOpeningBalanceItem, invOpenningBalanceItemsList, invOpenningBalanceItemDeletedList);

            resetObalance();
            invOpeningBalanceItem = invPurchaseReturnSave.getInvOpenningBalanceItem();
            invOpenningBalanceItemDetailsList = invPurchaseReturnSave.getInvOpenningBalanceItemDetailList();

            invOpeningBalanceItemEntity = mapInvOpenningBalanceItemToEntity(invPurchaseReturnSave.getInvOpenningBalanceItem());

            invOpeningBalanceItemEntity.setId(invOpeningBalanceItem.getId());
             savedMeesage(getUserData().getUserDDs().get("SAVED"));
            return "";
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "save");
            return null;
        }
    }
//----------------------------

    public void resetObalance() {
        try {
            invOpeningBalanceItem = new InvOpenningBalanceItem();
            invOpenningBalanceItemsList = new ArrayList<>();
            invOpenningBalanceItemDeletedList = new ArrayList<>();

            invOpenningBalanceItemDetailsList = new ArrayList<>();

            invOpeningBalanceItemEntity.setSerial(null);
            invOpeningBalanceItemEntity.setId(null);

            totalQuatity = BigDecimal.ZERO;
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "resetObalance");
        }

    }

    //------------------------------------
    public List<InvItem> completeInvItemData(String query) {
        try {
            List<InvItem> itemsList = invItemList;
            if (query == null || query.trim().equals("")) {

                itemConverter = new ItemConverter(itemsList);
                return itemsList;
            }
            List<InvItem> filteredItems = new ArrayList<>();

            String nameAr;
            String code;
            InvItem itemFilter;
            for (int i = 0; i < invItemList.size(); i++) {
                itemFilter = itemsList.get(i);
                nameAr = itemFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredItems.contains(itemFilter)) {
                        filteredItems.add(itemFilter);
                    }
                }

                code = itemFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredItems.contains(itemFilter)) {
                        filteredItems.add(itemFilter);
                    }
                }
            }

            itemConverter = new ItemConverter(filteredItems);
            return filteredItems;
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "completeInvItemData");
            return null;
        }
    }

//    public List<ItemsBarcodesDetailsView> completeItemsData(String query) {
//        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViews = itemsBarcodesDetailsViewList;
//        if (query == null || query.trim().equals("")) {
//            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViews);
//            return itemsBarcodesDetailsViews;
//        }
//        List<ItemsBarcodesDetailsView> filteredItems = new ArrayList<>();
//
//        String nameAr;
//        String code;
//        ItemsBarcodesDetailsView barcodesDetailsView;
//        for (int i = 0; i < itemsBarcodesDetailsViewList.size(); i++) {
//            barcodesDetailsView = itemsBarcodesDetailsViews.get(i);
//            nameAr = barcodesDetailsView.getName();
//            if (nameAr != null && nameAr.toLowerCase().trim().contains(query.toLowerCase().trim())) {
//                if (!filteredItems.contains(barcodesDetailsView)) {
//                    filteredItems.add(barcodesDetailsView);
//                }
//            }
//
//            code = barcodesDetailsView.getBarcode();
//            if (code != null && code.toLowerCase().trim().contains(query.toLowerCase().trim())) {
//                if (!filteredItems.contains(barcodesDetailsView)) {
//                    filteredItems.add(barcodesDetailsView);
//                }
//            }
//        }
//
//        itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(filteredItems);
//        return filteredItems;
//    }
    public List<InvOrganizationSite> completeOrganizationSite(String query) {
        try {
            List<InvOrganizationSite> invOrganizationList = customerAndSupplierList;
            if (query == null || query.trim().equals("")) {

                organizationSiteConverter = new InvOrganizationSiteConverter(invOrganizationList);
                return invOrganizationList;
            }
            List<InvOrganizationSite> filteredInvOrganizations = new ArrayList<>();

            String nameAr;
            String code;
            InvOrganizationSite invOrganizationFilter;
            for (int i = 0; i < customerAndSupplierList.size(); i++) {
                invOrganizationFilter = invOrganizationList.get(i);
                nameAr = invOrganizationFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvOrganizations.contains(invOrganizationFilter)) {
                        filteredInvOrganizations.add(invOrganizationFilter);
                    }
                }

                code = invOrganizationFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvOrganizations.contains(invOrganizationFilter)) {
                        filteredInvOrganizations.add(invOrganizationFilter);
                    }
                }
            }

            organizationSiteConverter = new InvOrganizationSiteConverter(filteredInvOrganizations);
            return filteredInvOrganizations;
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "completeOrganizationSite");
            return null;
        }
    }

    public List<InventoryDelegator> completeInvDelegator(String query) {
        try {
            List<InventoryDelegator> invDelegatorList = inventoryDelegatorList;
            if (query == null || query.trim().equals("")) {
                invDelegatorConverter = new InvDelegatorConvertor(invDelegatorList);
                return invDelegatorList;
            }
            List<InventoryDelegator> filteredInvDelegators = new ArrayList<>();

            String nameAr;
            Integer code;
            InventoryDelegator invDelegatorFilter;
            for (int i = 0; i < inventoryDelegatorList.size(); i++) {
                invDelegatorFilter = invDelegatorList.get(i);
                nameAr = invDelegatorFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvDelegators.contains(invDelegatorFilter)) {
                        filteredInvDelegators.add(invDelegatorFilter);
                    }
                }

                code = invDelegatorFilter.getId();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvDelegators.contains(invDelegatorFilter)) {
                        filteredInvDelegators.add(invDelegatorFilter);
                    }
                }
            }

            invDelegatorConverter = new InvDelegatorConvertor(filteredInvDelegators);
            return filteredInvDelegators;
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "completeInvDelegator");
            return null;
        }
    }

    public List<InvInventory> completeInventory(String query) {

        try {
            List<InvInventory> invList = invInventoryList;
            if (query == null || query.trim().equals("")) {

                invInventoryConverter = new InvInventoryConverter(invList);
                return invList;
            }
            List<InvInventory> filteredInvs = new ArrayList<>();

            String nameAr;
            String code;
            InvInventory invFilter;
            for (int i = 0; i < invInventoryList.size(); i++) {
                invFilter = invList.get(i);
                nameAr = invFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invFilter)) {
                        filteredInvs.add(invFilter);
                    }
                }

                code = invFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invFilter)) {
                        filteredInvs.add(invFilter);
                    }
                }
            }

            invInventoryConverter = new InvInventoryConverter(filteredInvs);
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "completeInventory");
            return null;
        }

    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        try {
            exit("../invopeningbalanceitem/invopeningbalanceitemlist.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemFormMB", "exit");
            return null;
        }
    }

    public List<InventoryDelegator> getInventoryDelegatorList() {
        return inventoryDelegatorList;
    }

    public void setInventoryDelegatorList(List<InventoryDelegator> inventoryDelegatorList) {
        this.inventoryDelegatorList = inventoryDelegatorList;
    }

    public List<InvItem> getInvItemList() {
        return invItemList;
    }

    public void setInvItemList(List<InvItem> invItemList) {
        this.invItemList = invItemList;
    }

    public List<InvOrganizationSite> getCustomerAndSupplierList() {
        return customerAndSupplierList;
    }

    public void setCustomerAndSupplierList(List<InvOrganizationSite> customerAndSupplierList) {
        this.customerAndSupplierList = customerAndSupplierList;
    }

    public InvOpenningBalanceItem getInvOpeningBalanceItem() {
        return invOpeningBalanceItem;
    }

    public void setInvOpeningBalanceItem(InvOpenningBalanceItem invOpeningBalanceItem) {
        this.invOpeningBalanceItem = invOpeningBalanceItem;
    }

    public InvQotationDetail getInvQotationDetail() {
        return invQotationDetail;
    }

    public void setInvQotationDetail(InvQotationDetail invQotationDetail) {
        this.invQotationDetail = invQotationDetail;
    }

    public List<InvQotationDetail> getInvQotationDetails() {
        return invQotationDetails;
    }

    public void setInvQotationDetails(List<InvQotationDetail> invQotationDetails) {
        this.invQotationDetails = invQotationDetails;
    }

    public InvOpeningBalanceItemEntity getInvOpeningBalanceItemEntity() {
        return invOpeningBalanceItemEntity;
    }

    public void setInvOpeningBalanceItemEntity(InvOpeningBalanceItemEntity invOpeningBalanceItemEntity) {
        this.invOpeningBalanceItemEntity = invOpeningBalanceItemEntity;
    }

    public Integer getInvQotationId() {
        return invQotationId;
    }

    public void setInvQotationId(Integer invQotationId) {
        this.invQotationId = invQotationId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalQuatity() {
        return totalQuatity;
    }

    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalNet() {
        return totalNet;
    }

    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
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

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public ItemConverter getItemConverter() {
        return itemConverter;
    }

    public void setItemConverter(ItemConverter itemConverter) {
        this.itemConverter = itemConverter;
    }

    public InvDelegatorConvertor getInvDelegatorConverter() {
        return invDelegatorConverter;
    }

    public void setInvDelegatorConverter(InvDelegatorConvertor invDelegatorConverter) {
        this.invDelegatorConverter = invDelegatorConverter;
    }

    public InvOrganizationSiteConverter getOrganizationSiteConverter() {
        return organizationSiteConverter;
    }

    public void setOrganizationSiteConverter(InvOrganizationSiteConverter organizationSiteConverter) {
        this.organizationSiteConverter = organizationSiteConverter;
    }

//    public ItemsBarcodesDetailsViewConverter getItemsBarcodesDetailsViewConverter() {
//        return itemsBarcodesDetailsViewConverter;
//    }
//
//    public void setItemsBarcodesDetailsViewConverter(ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter) {
//        this.itemsBarcodesDetailsViewConverter = itemsBarcodesDetailsViewConverter;
//    }
    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    public InvOpeningBalanceItemDetailEntity getInvOpenningBalanceItemDetailEntity() {
        return invOpenningBalanceItemDetailEntity;
    }

    public void setInvOpenningBalanceItemDetailEntity(InvOpeningBalanceItemDetailEntity invOpenningBalanceItemDetailEntity) {
        this.invOpenningBalanceItemDetailEntity = invOpenningBalanceItemDetailEntity;
    }

    public InvOpeningBalanceItemDetailEntity getInvOpenningBalanceItemDetailEntitySelection() {
        return invOpenningBalanceItemDetailEntitySelection;
    }

    public void setInvOpenningBalanceItemDetailEntitySelection(InvOpeningBalanceItemDetailEntity invOpenningBalanceItemDetailEntitySelection) {
        this.invOpenningBalanceItemDetailEntitySelection = invOpenningBalanceItemDetailEntitySelection;
    }

    public List<InvOpeningBalanceItemDetailEntity> getInvOpenningBalanceItemDetailEntityList() {
        return invOpenningBalanceItemDetailEntityList;
    }

    public void setInvOpenningBalanceItemDetailEntityList(List<InvOpeningBalanceItemDetailEntity> invOpenningBalanceItemDetailEntityList) {
        this.invOpenningBalanceItemDetailEntityList = invOpenningBalanceItemDetailEntityList;
    }

    public List<InvOpeningBalanceItemDetailEntity> getRowDeleted() {
        return rowDeleted;
    }

    public void setRowDeleted(List<InvOpeningBalanceItemDetailEntity> rowDeleted) {
        this.rowDeleted = rowDeleted;
    }

    public List<InvOpenningBalanceItemDetail> getInvOpenningBalanceItemDetailsList() {
        return invOpenningBalanceItemDetailsList;
    }

    public void setInvOpenningBalanceItemDetailsList(List<InvOpenningBalanceItemDetail> invOpenningBalanceItemDetailsList) {
        this.invOpenningBalanceItemDetailsList = invOpenningBalanceItemDetailsList;
    }

    public List<InvOpenningBalanceItemDetail> getInvOpenningBalanceItemDeletedList() {
        return invOpenningBalanceItemDeletedList;
    }

    public void setInvOpenningBalanceItemDeletedList(List<InvOpenningBalanceItemDetail> invOpenningBalanceItemDeletedList) {
        this.invOpenningBalanceItemDeletedList = invOpenningBalanceItemDeletedList;
    }

    public List<InvOpenningBalanceItemDetail> getInvOpenningBalanceItemsList() {
        return invOpenningBalanceItemsList;
    }

    public void setInvOpenningBalanceItemsList(List<InvOpenningBalanceItemDetail> invOpenningBalanceItemsList) {
        this.invOpenningBalanceItemsList = invOpenningBalanceItemsList;
    }
}

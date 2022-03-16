package com.isag.invreservation;

import com.isag.businessservice.*;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.converter.InvDelegatorConvertor;
import com.isag.converter.InvInventoryConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.ItemsBarcodesDetailsViewConverter;
import com.isag.entity.*;
import com.isag.entiy.InvReservationDetailEntity;
import com.isag.entiy.InvReservationEntity;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.views.QuantityItemsStore;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author khaled
 */
@Named(value = "invReservationFormMB")
@ViewScoped
public class InvReservationFormMB extends BaseFormBean {

    private List<InventoryDelegator> inventoryDelegatorList;
    private InvDelegatorConvertor delegatorConvertor;
    private InventoryDelegator inventoryDelegator;

    private List<InvUser> invUserList;
    private List<InvInventory> invInventoryList;

    private List<InvItem> invItemList;
    private InvItem invItem;

    private InvOrganizationSite site;
    private InvOrganizationSite mainSite;
    private List<InvOrganizationSite> customerAndSupplierList;

    private InvReservation invReservation;
    private InventorySetup invSetup;
    private List<InvReservation> invReservationList;
    Map<Integer, BigDecimal> itemsMap;
    private InvReservationDetail invReservationDetail;
    private List<InvReservationDetail> invReservationDetails;

    private InvReservationEntity invReservationEntity;
    private InvReservationDetailEntity invReservationDetailEntity;
    private InvReservationDetailEntity invReservationDetailEntitySelection;
    private List<InvReservationDetailEntity> rowDeleted;
    private List<InvReservationDetailEntity> invReservationDetailsEntityList;

    private List<InvReservationDetail> invReservationDetailList;
    private List<InvReservationDetail> invReservationDeletedList;
    private List<InvReservationDetail> inveDetailsList;
    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;
    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;
    Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap;
    private Integer invReservationId;

    private Integer index = 0;
    private Integer branchId;
    private BigDecimal finalTotal;

    private BigDecimal totalPrice = BigDecimal.ZERO;
    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private BigDecimal totalTotal = BigDecimal.ZERO;
    private BigDecimal totalAdding = BigDecimal.ZERO;
    private BigDecimal totalDiscount = BigDecimal.ZERO;
    private BigDecimal totalNet = BigDecimal.ZERO;
    BigDecimal hundred = new BigDecimal(100);

    private Boolean markEdit = Boolean.FALSE;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private boolean showMessage = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

    private InvOrganizationSiteConverter customerConvertor;
    private InvInventoryConverter invInventoryConverter;

    private Map<Integer, InvReservationDetail> invReservationDetailMap;
    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private InvReservationService invReservationService;
    @EJB
    private InvReservationDetailService invReservationDetailService;
    @EJB
    private InvUserService invUserService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    private QuantityItemsStoreService quantityItemsStoreService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));

            branchId = getUserData().getUserBranch().getId();
            initObjs();
            fillLists();

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetItemDataForm();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    invReservationId = (Integer) context.getSessionMap().get("invReservationSelected");
                    invReservation = invReservationService.findInvReservationByInvReservationId(invReservationId);
                    invReservationDetailList = invReservationService.getALLInvReservationDetailsByInvReservation(invReservationId);

                    invReservationEntity = mapInvReservationToEntity(invReservation);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            fillItemMap();
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "load");
        }
    }

    private void initObjs() {
        try {
            invReservation = new InvReservation();
            invReservationDetail = new InvReservationDetail();
            invReservationEntity = new InvReservationEntity();
            invReservationDetailEntity = new InvReservationDetailEntity();
            inveDetailsList = new ArrayList<>();
            invReservationDeletedList = new ArrayList<>();
            invInventoryList = new ArrayList<>();
            invReservationDetailEntitySelection = new InvReservationDetailEntity();
            invReservationDetailsEntityList = new ArrayList<>();
            rowDeleted = new ArrayList<>();
            inventoryDelegatorList = new ArrayList<>();
            itemsBarcodesDetailsViewList = new ArrayList<>();
            invReservationDetailMap = new HashMap<>();
            itemsBarcodeMap = new HashMap<>();
            itemsMap = new HashMap<>();
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "initObjs");
        }
    }

    private void fillLists() {
        try {
            itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViewList);

            for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
                itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
            }

            invReservationDetailList = invReservationDetailService.getAllInvReservationDetails(invReservation.getId());

            inventoryDelegatorList = invDelegatorService.getSalesByBranch(branchId);
            delegatorConvertor = new InvDelegatorConvertor(inventoryDelegatorList);

            invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId);
            invInventoryConverter = new InvInventoryConverter(invInventoryList);

//        invItemList = invItemService.getInvItemByBranchId(branchId);
            customerAndSupplierList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(branchId, true, 0);
            customerConvertor = new InvOrganizationSiteConverter(customerAndSupplierList);

            invReservationDetails = invReservationDetailService.getAllInvReservationDetails(invReservation.getId());
            invReservationList = invReservationService.getALLInvReservationByBranchId(branchId);
            invSetup = inventorySetupService.getInventoryByBranchId(branchId);
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "fillLists");
        }
    }

    private void resetItemDataForm() {
        try {
            invReservationEntity = new InvReservationEntity();
            invReservationEntity.setReservationDate(new Date());
            invReservationDetailList = new ArrayList<>();

            invReservationDetailEntitySelection = new InvReservationDetailEntity();
            invReservationDetailsEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "resetItemDataForm");
        }
    }

    public InvReservationEntity mapInvReservationToEntity(InvReservation invReservation) {
        try {

            invReservationEntity.setAddress(invReservation.getAddress());
            invReservationEntity.setId(invReservation.getId());
            invReservationEntity.setSerial(invReservation.getSerial());
            invReservationEntity.setBranchId(invReservation.getBranchId());
            invReservationEntity.setDelegator(invReservation.getDelegatorId());
            invReservationEntity.setEndDate(invReservation.getEndDate());
            invReservationEntity.setInventory(invReservation.getInvId());
            invReservationEntity.setRemark(invReservation.getRemarks());
            invReservationEntity.setReservationDate(invReservation.getReservationDate());
            invReservationEntity.setSite(invReservation.getSiteId());
            invReservationEntity.setMainSite(invReservation.getSiteIdMain());

            for (InvReservationDetail reservationDetail : invReservationDetailList) {
                finalTotal = BigDecimal.ZERO;
                invReservationDetailEntity = new InvReservationDetailEntity();

                invReservationDetailEntity.setCreatedBy(reservationDetail.getCreatedBy());
                invReservationDetailEntity.setCreationDate(reservationDetail.getCreationDate());

                if (reservationDetail.getItemBarcode() != null && !"".equals(reservationDetail.getItemBarcode())) {
                    invReservationDetailEntity.setItemsBarcodesDetail(itemsBarcodeMap.get(reservationDetail.getItemBarcode()));
                    invReservationDetailEntity.setItemsBarcodesDetailTrans(invReservationDetailEntity.getItemsBarcodesDetail());
                    invReservationDetailEntity.setScrewing(invReservationDetailEntity.getItemsBarcodesDetail().getScrewing());
                }

                invReservationDetailEntity.setId(reservationDetail.getId());
                invReservationDetailEntity.setSerial(reservationDetail.getSerial());

                if (reservationDetail.getItemId() != null) {
                    invReservationDetailEntity.setItem(reservationDetail.getItemId().getId());
                    invReservationDetailEntity.setItemCode(reservationDetail.getItemId().getCode());
                    invReservationDetailEntity.setItemName(reservationDetail.getItemId().getName());
                    invReservationDetailEntity.setUnit(reservationDetail.getItemId().getUnitId() != null ? reservationDetail.getItemId().getUnitId().getName() : null);
                }

                invReservationDetailEntity.setAmount(reservationDetail.getAmount() != null ? reservationDetail.getAmount() : BigDecimal.ZERO);
                invReservationDetailEntity.setPrice(reservationDetail.getPrice() != null ? reservationDetail.getPrice() : BigDecimal.ZERO);
                invReservationDetailEntity.setDiscount(reservationDetail.getDiscount() != null ? reservationDetail.getDiscount() : BigDecimal.ZERO);
                invReservationDetailEntity.setTotal(reservationDetail.getTotal() != null ? reservationDetail.getTotal() : BigDecimal.ZERO);
                invReservationDetailEntity.setAdding(reservationDetail.getAdding() != null ? reservationDetail.getAdding() : BigDecimal.ZERO);
                invReservationDetailEntity.setNet(reservationDetail.getNet() != null ? reservationDetail.getNet() : BigDecimal.ZERO);

                invReservationDetailMap.put(reservationDetail.getId(), reservationDetail);
//            totalPrice = totalPrice.add(reservationDetail.getPrice() != null ? reservationDetail.getPrice() : BigDecimal.ZERO);
//            totalTotal = totalTotal.add(reservationDetail.getTotal() != null ? reservationDetail.getTotal() : BigDecimal.ZERO);
//            totalAdding = totalAdding.add(reservationDetail.getAdding() != null ? reservationDetail.getAdding() : BigDecimal.ZERO);
//            totalDiscount = totalDiscount.add(reservationDetail.getDiscount() != null ? reservationDetail.getDiscount() : BigDecimal.ZERO);
                totalQuatity = totalQuatity.add(reservationDetail.getAmount() != null ? reservationDetail.getAmount() : BigDecimal.ZERO);
                totalNet = totalNet.add(reservationDetail.getNet() != null ? reservationDetail.getNet() : BigDecimal.ZERO);

                invReservationDetailsEntityList.add(invReservationDetailEntity);
                BigDecimal dbQut = itemsMap.get(invReservationDetailEntity.getItemsBarcodesDetail().getItemId());
                if (dbQut != null) {
                    itemsMap.put(invReservationDetailEntity.getItemsBarcodesDetail().getItemId(), dbQut.add(reservationDetail.getAmount()));
                } else {
                    itemsMap.put(invReservationDetailEntity.getItemsBarcodesDetail().getItemId(), reservationDetail.getAmount());
                }
            }

            return invReservationEntity;
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "mapInvReservationToEntity");
            return null;
        }

    }

    public void fillItemMap() {
        try {
            if (invSetup != null && invSetup.getNegativeSell() == false && invReservationEntity.getInventory() != null) {
                List<QuantityItemsStore> quantityItemsStoresList = quantityItemsStoreService.findInventoryItemsList(invReservationEntity.getInventory().getId(), branchId, invSetup.getSellBuy());

                for (QuantityItemsStore quantityItemsStore : quantityItemsStoresList) {
                    itemsMap.put(quantityItemsStore.getItemId(), quantityItemsStore.getQty());
                }
                completeFillMap();
            }
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "fillItemMap");
        }
    }

    public void completeFillMap() {
        try {
            if (invReservationDetailsEntityList != null && !invReservationDetailsEntityList.isEmpty()) {
                for (InvReservationDetailEntity item : invReservationDetailsEntityList) {
                    BigDecimal dbQut = null;

                    dbQut = itemsMap.get(item.getItemsBarcodesDetail().getItemId());

                    if (dbQut != null) {
                        if (item.getId() != null) {
                            itemsMap.put(item.getItemsBarcodesDetail().getItemId(), dbQut.add(item.getAmount()));
                        }
                    } else {
                        itemsMap.put(item.getItemsBarcodesDetail().getItemId(), item.getAmount());
                    }
                }
            }

        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "completeFillMap");
        }
    }

    public void adddetails() {
        try {
            for (InvReservationDetailEntity detailEntity : invReservationDetailsEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }

                if (detailEntity.getItemsBarcodesDetailTrans() == null
                        || !validateQuantity(detailEntity) || !validatePrice(detailEntity)) {

                    errorMessage("يجب ادخال تفاصيل الفاتورة");
                    return;
                }
                detailEntity.setMarkEdit(Boolean.FALSE);
            }

            InvReservationDetailEntity invReservationDetailEntity = new InvReservationDetailEntity();
            invReservationDetailEntity.setMarkEdit(Boolean.TRUE);
            invReservationDetailEntitySelection = invReservationDetailEntity;
            invReservationDetailsEntityList.add(invReservationDetailEntity);
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "adddetails");
        }
    }

    public void deleteInvReservationDetail() {
        try {
            if (invReservationDetailEntitySelection != null) {
                if (invReservationDetailEntitySelection.getId() != null) {
                    setDelete(Boolean.TRUE);
                    rowDeleted.add(invReservationDetailEntitySelection);
                }

                invReservationDetailsEntityList.remove(invReservationDetailEntitySelection);
                updateSummition();
            } else {
                errorMessage("يجب اختيار سطر للمسح");
            }
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "deleteInvReservationDetail");
        }
    }

    @Override
    public String save() {
        try {
            invReservation.setCompanyId(getUserData().getCompany());
            invReservation.setBranchId(getUserData().getUserBranch());

            if (invReservation.getId() == null) {
                invReservation.setCreationDate(new Date());
                invReservation.setCreatedBy(getUserData().getUser());
            } else {
                invReservation.setModificationDate(new Date());
                invReservation.setModifiedBy(getUserData().getUser());
                invReservation.setStatus(0);
            }

            invReservation.setDelegatorId(invReservationEntity.getDelegator());
            invReservation.setInvId(invReservationEntity.getInventory());
            invReservation.setSiteId(invReservationEntity.getSite());
            invReservation.setSiteIdMain(invReservationEntity.getMainSite());
            invReservation.setReservationDate(invReservationEntity.getReservationDate());
            invReservation.setEndDate(invReservationEntity.getEndDate());
            invReservation.setAddress(invReservationEntity.getAddress());
            invReservation.setRemarks(invReservationEntity.getRemark());

            InvReservationDetail reservationDetail;
            InvItem item;
            if (invReservationDetailsEntityList != null && !invReservationDetailsEntityList.isEmpty()) {
                inveDetailsList = new ArrayList<>();
                invReservationDeletedList = new ArrayList<>();
                for (InvReservationDetailEntity reservationDetailEntity : invReservationDetailsEntityList) {
                    reservationDetail = new InvReservationDetail();

                    if (reservationDetailEntity.getId() != null) {
                        reservationDetail.setId(reservationDetailEntity.getId());

                        reservationDetail.setCreatedBy(reservationDetailEntity.getCreatedBy());
                        reservationDetail.setCreationDate(reservationDetailEntity.getCreationDate());

                        reservationDetail.setModificationDate(new Date());
                        reservationDetail.setModifiedBy(getUserData().getUser() != null ? getUserData().getUser() : null);

                    } else {
                        reservationDetail.setCreatedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                        reservationDetail.setCreationDate(new Date());
                        reservationDetail.setStatus(0);
                    }

                    reservationDetail.setCompanyId(getUserData().getCompany());
                    reservationDetail.setBranchId(getUserData().getUserBranch());
                    reservationDetail.setSerial(reservationDetailEntity.getSerial());

                    if (reservationDetailEntity.getDiscount() != null) {
                        if (isDiscountValid(reservationDetailEntity.getDiscount())) {
                            reservationDetail.setDiscount(reservationDetailEntity.getDiscount());
                        } else {
                            return "";
                        }
                    }

                    if (reservationDetailEntity.getItemsBarcodesDetail() == null
                            && reservationDetailEntity.getItemsBarcodesDetailTrans() == null) {
                        errorMessage("يجب ادخال الصنف");
                        return "";
                    } else {
                        item = new InvItem();
                        item.setId(reservationDetailEntity.getItemsBarcodesDetailTrans().getItemId());
                        reservationDetail.setItemId(item);
                        reservationDetail.setItemBarcode(reservationDetailEntity.getItemsBarcodesDetailTrans().getBarcode());
                        reservationDetail.setScrewing(reservationDetailEntity.getItemsBarcodesDetailTrans().getScrewing());
                        reservationDetailEntity.setItemsBarcodesDetail(reservationDetailEntity.getItemsBarcodesDetailTrans());
                    }

                    if (validatePrice(reservationDetailEntity)) {
                        reservationDetail.setPrice(reservationDetailEntity.getPrice().setScale(2, BigDecimal.ROUND_UP));
                    } else {
                        errorMessage("يجب ادخال تفاصيل الفاتورة");
                        return "";
                    }

                    if (validateQuantity(reservationDetailEntity)) {
                        reservationDetail.setAmount(reservationDetailEntity.getAmount());
                    } else {
                        errorMessage("يجب ادخال تفاصيل الفاتورة");
                        return "";
                    }

                    if (reservationDetailEntity.getAdding() != null && reservationDetailEntity.getAdding().compareTo(BigDecimal.ZERO) == 1) {
                        if (validateAdding(reservationDetailEntity)) {
                            reservationDetail.setAdding(reservationDetailEntity.getAdding());
                        } else {
                            errorMessage("يجب ادخال تفاصيل الفاتورة");
                            return "";
                        }
                    }

                    reservationDetail.setNet(reservationDetailEntity.getNet());

                    inveDetailsList.add(reservationDetail);
                }

                if (rowDeleted != null) {
                    InvReservationDetail ipo;

                    for (InvReservationDetailEntity deletedDetailsEntity : getRowDeleted()) {
                        ipo = new InvReservationDetail();
                        ipo.setId(deletedDetailsEntity.getId());
                        invReservationDeletedList.add(ipo);
                    }
                }
                InvPurchaseReturnSave invPurchaseReturnSave;

                invPurchaseReturnSave = invReservationService.addInvReservation(invReservation, inveDetailsList, invReservationDeletedList);

                if (invReservation.getMsg() != null && !"".equals(invReservation.getMsg())) {
                    errorMessage("يجب ادخال كميةاكبر من الموجودة بالمخزن");
                    return "";
                }

                invReservationEntity.setId(invReservation.getId());
                resetReservation();
                invReservation = invPurchaseReturnSave.getInvReservation();
                invReservationDetailList = (List<InvReservationDetail>) invPurchaseReturnSave.getInvReservationDetailList();

                invReservationEntity = mapInvReservationToEntity(invPurchaseReturnSave.getInvReservation());

            } else {
                errorMessage("يجب ادخال تفاصيل حجز البضاعة");
                return "";
            }
            savedMeesage(getUserData().getUserDDs().get("SAVED"));
            return "";
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "save");
            return null;
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
            saveError(e, "InvReservationFormMB", "reset");
        }
    }

    public void resetReservation() {
        try {
            invReservation = new InvReservation();

            invReservationDeletedList = new ArrayList<>();
            invReservationEntity.setSerial(null);
            invReservationEntity.setId(null);
            invReservationDetailsEntityList = new ArrayList<>();

            totalPrice = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            totalTotal = BigDecimal.ZERO;
            totalAdding = BigDecimal.ZERO;
            totalDiscount = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "resetReservation");
        }

    }

    public void onCellEdit(CellEditEvent event) {
        try {
            Object newValue = event.getNewValue();
            String column_name = event.getColumn().getClientId();

            DataTable dataTable = (DataTable) event.getSource();
            invReservationDetailEntitySelection = (InvReservationDetailEntity) dataTable.getRowData();

            if (column_name.contains("itemName") || column_name.contains("itemNumber")) {
                updateInvItemsRelated((Integer) newValue);
            }

            if (column_name.contains("Price")) {
                invReservationDetailEntitySelection.setTotal(invReservationDetailEntitySelection.getAmount()
                        .multiply(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO));
                invReservationDetailEntitySelection.setNet(invReservationDetailEntitySelection.getAmount()
                        .multiply(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO)
                        .add(invReservationDetailEntitySelection.getAdding())
                        .subtract(invReservationDetailEntitySelection.getDiscount() != null ? invReservationDetailEntitySelection.getDiscount() : BigDecimal.ZERO));
            }
            if (column_name.contains("Quantity")) {
                invReservationDetailEntitySelection.setTotal(invReservationDetailEntitySelection.getPrice()
                        .multiply(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO));
                invReservationDetailEntitySelection.setNet(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO
                        .multiply(invReservationDetailEntitySelection.getPrice())
                        .add(invReservationDetailEntitySelection.getAdding())
                        .subtract(invReservationDetailEntitySelection.getDiscount() != null ? invReservationDetailEntitySelection.getDiscount() : BigDecimal.ZERO));
            }
            if (column_name.contains("adding")) {
                invReservationDetailEntitySelection.setNet(invReservationDetailEntitySelection.getAmount()
                        .multiply(invReservationDetailEntitySelection.getPrice())
                        .add(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO)
                        .subtract(invReservationDetailEntitySelection.getDiscount() != null ? invReservationDetailEntitySelection.getDiscount() : BigDecimal.ZERO));
            }
            if (column_name.contains("Discount")) {
                invReservationDetailEntitySelection.setNet(invReservationDetailEntitySelection.getAmount()
                        .multiply(invReservationDetailEntitySelection.getPrice())
                        .add(invReservationDetailEntitySelection.getAdding())
                        .subtract(invReservationDetailEntitySelection.getDiscount() != null ? invReservationDetailEntitySelection.getDiscount() : BigDecimal.ZERO));
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "onCellEdit");
        }
    }

    public Boolean validateItems(InvReservationDetailEntity invReservationTable) {
        try {
            if (invReservationTable.getItemsBarcodesDetail() == null) {
                resetInvItem(invReservationTable);
            } else {
                invReservationTable.setItemsBarcodesDetailTrans(invReservationTable.getItemsBarcodesDetail());

                if (invReservationTable.getItemsBarcodesDetail().getUnitName() != null
                        && !"".equals(invReservationTable.getItemsBarcodesDetail().getUnitName())) {
                    invReservationTable.setUnit(invReservationTable.getItemsBarcodesDetail().getUnitName());
                    invReservationTable.setPrice(invReservationTable.getItemsBarcodesDetail().getSellPrice());
                    invReservationTable.setScrewing(invReservationTable.getItemsBarcodesDetail().getScrewing());
                } else {
                    resetInvItem(invReservationTable);
                    errorMessage("هذا الصنف ليس لديه وحدات.");
                    return false;
                }
            }
            updateSummition();
            return true;
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "validateItems");
            return null;
        }
    }

    private void resetInvItem(InvReservationDetailEntity invReservationTable) {
        try {
            invReservationTable.setAmount(null);
            invReservationTable.setPrice(null);
            invReservationTable.setDiscount(null);
            invReservationTable.setUnit(null);
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "resetInvItem");
        }
    }

    public void validateQuantityColum(InvReservationDetailEntity invReservationTable) {
        try {
            if (!validateQuantity(invReservationTable)) {
                errorMessage("الكمية المطلوبة اكبر من الموجودة بالمخزن");
                if (invReservationTable.getItemsBarcodesDetail() != null
                        && invReservationTable.getItemsBarcodesDetail().getItemId() != null) {
                    invReservationTable.setAmount(BigDecimal.ZERO);
                } else {
                    invReservationTable.setAmount(null);
                }
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "validateQuantityColum");
        }
    }

    public void validateDiscountColumn(InvReservationDetailEntity invReservationTable) {
        try {
            if (!validateDiscount(invReservationTable)) {
                invReservationTable.setDiscount(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "validateDiscountColumn");
        }
    }

    public void validateAddingColum(InvReservationDetailEntity invReservationTable) {
        try {
            if (!validateAdding(invReservationTable)) {
                if (invReservationTable.getItemsBarcodesDetail() != null) {
                    invReservationTable.setAdding(BigDecimal.ZERO);
                } else {
                    invReservationTable.setAdding(null);
                }
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "validateAddingColum");
        }

    }

    public void validatePriceColumn(InvReservationDetailEntity invReservationTable) {
        try {
            if (!validatePrice(invReservationTable)) {
                invReservationTable.setPrice(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "validatePriceColumn");
        }
    }

    public Boolean validateQuantity(InvReservationDetailEntity invReservationTable) {
        try {
            if (invReservationTable != null && invReservationTable.getItemsBarcodesDetailTrans() != null
                    && invReservationTable.getItemsBarcodesDetailTrans().getItemId() != null) {

                if (invReservationTable.getAmount() != null
                        && invReservationTable.getAmount().compareTo(BigDecimal.ZERO) == 1) {
                    if (invSetup.getNegativeSell() == false) {
                        if (!itemsMap.isEmpty()) {
                            BigDecimal dbQut = itemsMap.get(invReservationTable.getItemsBarcodesDetail().getItemId());
                            if (dbQut != null) {
                                BigDecimal usrQut = sumItemQuantity(invReservationTable.getItemsBarcodesDetail().getItemId());
                                return dbQut.compareTo(BigDecimal.ZERO) == 1 && dbQut.compareTo(usrQut) != -1;
                            } else {
                                return false;
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
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "validateQuantity");
            return null;
        }
    }

    BigDecimal sumItemQuantity(Integer itemId) {
        try {

            BigDecimal detailQuant, detailScrewing, quant = BigDecimal.ZERO;
            for (InvReservationDetailEntity detailsEntity : invReservationDetailsEntityList) {
                if (detailsEntity.getItemsBarcodesDetail() != null && detailsEntity.getItemsBarcodesDetail().getItemId() != null
                        && Objects.equals(itemId, detailsEntity.getItemsBarcodesDetail().getItemId())
                        && detailsEntity.getAmount() != null && detailsEntity.getAmount().compareTo(BigDecimal.ZERO) == 1) {

                    detailQuant = detailsEntity.getAmount();
                    detailScrewing = detailsEntity.getScrewing() != null ? detailsEntity.getScrewing() : BigDecimal.ONE;

                    quant = quant.add(detailQuant.multiply(detailScrewing));
                }
            }
            return quant;
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "sumItemQuantity");
            return null;
        }

    }

    public Boolean validateAdding(InvReservationDetailEntity invReservationTable) {
        try {
            return invReservationTable != null && invReservationTable.getItemsBarcodesDetail() != null
                    && invReservationTable.getAdding() != null
                    && invReservationTable.getAdding().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "validateAdding");
            return null;
        }
    }

    public Boolean validateUnit(InvReservationDetailEntity invReservationTable) {
        try {
            return invReservationTable != null && invReservationTable.getItemsBarcodesDetail() != null
                    && invReservationTable.getItemsBarcodesDetail().getUnitId() != null
                    && invReservationTable.getUnit() != null;
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "validateUnit");
            return null;
        }
    }

    public Boolean validatePrice(InvReservationDetailEntity invReservationTable) {
        try {
            return invReservationTable != null
                    && invReservationTable.getItemsBarcodesDetailTrans() != null
                    && invReservationTable.getPrice() != null
                    && invReservationTable.getPrice().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "validatePrice");
            return null;
        }
    }

    public Boolean validateDiscount(InvReservationDetailEntity invReservationTable) {
        try {
            return isDiscountValid(invReservationTable.getDiscount());
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "validateDiscount");
            return null;
        }
    }

    private Boolean isDiscountValid(BigDecimal discValue) {
        try {
            if (discValue != null) {
                if ((discValue.compareTo(BigDecimal.ZERO) == 0 || discValue.compareTo(BigDecimal.ZERO) == 1)
                        && (discValue.compareTo(hundred) == 0 || hundred.compareTo(discValue) == 1)) {
                    return true;
                } else {
                    return errorMessage("لا يجب ادخال خصم اعلى من 100% في حالة النسبة");
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "isDiscountValid");
            return null;
        }
    }

    private void updateInvItemsRelated(Integer id) {
        try {
            InvItem invItem = invItemService.findInvItem(id);
            invReservationDetailEntitySelection.setItemCode(invItem.getCode());
            invReservationDetailEntitySelection.setItemName(invItem.getName());
            invReservationDetailEntitySelection.setUnit(invItem.getUnitId() != null ? invItem.getUnitId().getName() : null);
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "updateInvItemsRelated");
        }
    }

    public void updateSummition() {
        try {
            BigDecimal quantity, discount, cost, total, valueAfterDiscount;

            totalPrice = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            totalTotal = BigDecimal.ZERO;
            totalAdding = BigDecimal.ZERO;
            totalDiscount = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;
            for (InvReservationDetailEntity reservationDetailEntity : invReservationDetailsEntityList) {
                if (reservationDetailEntity.getItemsBarcodesDetail() == null && reservationDetailEntity.getItemsBarcodesDetailTrans() != null) {
                    reservationDetailEntity.setItemsBarcodesDetail(reservationDetailEntity.getItemsBarcodesDetailTrans());
                }

                quantity = reservationDetailEntity.getAmount() != null ? reservationDetailEntity.getAmount() : BigDecimal.ZERO;
                cost = reservationDetailEntity.getPrice() != null ? reservationDetailEntity.getPrice() : BigDecimal.ZERO;
                discount = reservationDetailEntity.getDiscount() != null ? reservationDetailEntity.getDiscount() : BigDecimal.ZERO;

                total = quantity.multiply(cost);
                reservationDetailEntity.setTotal(total);
                valueAfterDiscount = total.subtract((total.multiply(discount).divide(hundred)));

                reservationDetailEntity.setNet(valueAfterDiscount);

                totalQuatity = totalQuatity.add(quantity);
                totalPrice = totalPrice.add(reservationDetailEntity.getPrice() != null ? reservationDetailEntity.getPrice() : BigDecimal.ZERO);
                totalTotal = totalTotal.add(reservationDetailEntity.getTotal() != null ? reservationDetailEntity.getTotal() : BigDecimal.ZERO);
                totalAdding = totalAdding.add(reservationDetailEntity.getAdding() != null ? reservationDetailEntity.getAdding() : BigDecimal.ZERO);
                totalDiscount = totalDiscount.add(reservationDetailEntity.getDiscount() != null ? reservationDetailEntity.getDiscount() : BigDecimal.ZERO);
                totalNet = totalNet.add(reservationDetailEntity.getNet() != null ? reservationDetailEntity.getNet() : BigDecimal.ZERO);
            }
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "updateSummition");
        }
    }

    public String exit() {
        try {
            exit("../invreservation/invReservationList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "exit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<ItemsBarcodesDetailsView> completeItemsData(String query) {
        try {
            List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViews = itemsBarcodesDetailsViewList;
            if (query == null || query.trim().equals("")) {
                itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViews);
                return itemsBarcodesDetailsViews;
            }
            List<ItemsBarcodesDetailsView> filteredItems = new ArrayList<>();

            String nameAr;
            String code;
            ItemsBarcodesDetailsView barcodesDetailsView;
            for (int i = 0; i < itemsBarcodesDetailsViewList.size(); i++) {
                barcodesDetailsView = itemsBarcodesDetailsViews.get(i);
                nameAr = barcodesDetailsView.getName();
                if (nameAr != null && nameAr.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }

                code = barcodesDetailsView.getBarcode();
                if (code != null && code.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }
            }

            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(filteredItems);
            return filteredItems;
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "completeItemsData");
            return null;
        }
    }

    public List<InvOrganizationSite> completeCustomers(String query) {
        try {
            List<InvOrganizationSite> organizationSiteList = customerAndSupplierList;
            if (query == null || query.trim().equals("")) {

                customerConvertor = new InvOrganizationSiteConverter(organizationSiteList);
                return organizationSiteList;
            }
            List<InvOrganizationSite> filteredCustomers = new ArrayList<>();

            String nameAr;
            String code;
            InvOrganizationSite customerFilter;
            for (int i = 0; i < customerAndSupplierList.size(); i++) {
                customerFilter = organizationSiteList.get(i);
                nameAr = customerFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCustomers.contains(customerFilter)) {
                        filteredCustomers.add(customerFilter);
                    }
                }

                code = customerFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCustomers.contains(customerFilter)) {
                        filteredCustomers.add(customerFilter);
                    }
                }
            }

            customerConvertor = new InvOrganizationSiteConverter(filteredCustomers);
            return filteredCustomers;
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "completeCustomers");
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
            saveError(e, "InvReservationFormMB", "completeInventory");
            return null;
        }
    }

    public List<InventoryDelegator> completeDelegators(String query) {
        try {
            List<InventoryDelegator> invList = inventoryDelegatorList;
            if (query == null || query.trim().equals("")) {

                delegatorConvertor = new InvDelegatorConvertor(invList);
                return invList;
            }
            List<InventoryDelegator> filteredInvs = new ArrayList<>();

            String nameAr;
            String code;
            InventoryDelegator invFilter;
            for (int i = 0; i < inventoryDelegatorList.size(); i++) {
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

            delegatorConvertor = new InvDelegatorConvertor(filteredInvs);
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "InvReservationFormMB", "completeDelegators");
            return null;
        }
    }

    public List<InventoryDelegator> getInventoryDelegatorList() {
        return inventoryDelegatorList;
    }

    public void setInventoryDelegatorList(List<InventoryDelegator> inventoryDelegatorList) {
        this.inventoryDelegatorList = inventoryDelegatorList;
    }

    public InventoryDelegator getInventoryDelegator() {
        return inventoryDelegator;
    }

    public void setInventoryDelegator(InventoryDelegator inventoryDelegator) {
        this.inventoryDelegator = inventoryDelegator;
    }

    public List<InvItem> getInvItemList() {
        return invItemList;
    }

    public void setInvItemList(List<InvItem> invItemList) {
        this.invItemList = invItemList;
    }

    public InvItem getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItem invItem) {
        this.invItem = invItem;
    }

    public InvOrganizationSite getSite() {
        return site;
    }

    public void setSite(InvOrganizationSite site) {
        this.site = site;
    }

    public InvOrganizationSite getMainSite() {
        return mainSite;
    }

    public void setMainSite(InvOrganizationSite mainSite) {
        this.mainSite = mainSite;
    }

    public List<InvOrganizationSite> getCustomerAndSupplierList() {
        return customerAndSupplierList;
    }

    public void setCustomerAndSupplierList(List<InvOrganizationSite> customerAndSupplierList) {
        this.customerAndSupplierList = customerAndSupplierList;
    }

    public InvReservation getInvReservation() {
        return invReservation;
    }

    public void setInvReservation(InvReservation invReservation) {
        this.invReservation = invReservation;
    }

    public List<InvReservation> getInvReservationList() {
        return invReservationList;
    }

    public void setInvReservationList(List<InvReservation> invReservationList) {
        this.invReservationList = invReservationList;
    }

    public InvReservationDetail getInvReservationDetail() {
        return invReservationDetail;
    }

    public void setInvReservationDetail(InvReservationDetail invReservationDetail) {
        this.invReservationDetail = invReservationDetail;
    }

    public List<InvReservationDetail> getInvReservationDetails() {
        return invReservationDetails;
    }

    public void setInvReservationDetails(List<InvReservationDetail> invReservationDetails) {
        this.invReservationDetails = invReservationDetails;
    }

    public InvReservationEntity getInvReservationEntity() {
        return invReservationEntity;
    }

    public void setInvReservationEntity(InvReservationEntity invReservationEntity) {
        this.invReservationEntity = invReservationEntity;
    }

    public InvReservationDetailEntity getInvReservationDetailEntity() {
        return invReservationDetailEntity;
    }

    public void setInvReservationDetailEntity(InvReservationDetailEntity invReservationDetailEntity) {
        this.invReservationDetailEntity = invReservationDetailEntity;
    }

    public InvReservationDetailEntity getInvReservationDetailEntitySelection() {
        return invReservationDetailEntitySelection;
    }

    public void setInvReservationDetailEntitySelection(InvReservationDetailEntity invReservationDetailEntitySelection) {
        this.invReservationDetailEntitySelection = invReservationDetailEntitySelection;
    }

    public List<InvReservationDetailEntity> getRowDeleted() {
        return rowDeleted;
    }

    public void setRowDeleted(List<InvReservationDetailEntity> rowDeleted) {
        this.rowDeleted = rowDeleted;
    }

    public List<InvReservationDetail> getInvReservationDetailList() {
        return invReservationDetailList;
    }

    public void setInvReservationDetailList(List<InvReservationDetail> invReservationDetailList) {
        this.invReservationDetailList = invReservationDetailList;
    }

    public List<InvReservationDetail> getInvReservationDeletedList() {
        return invReservationDeletedList;
    }

    public void setInvReservationDeletedList(List<InvReservationDetail> invReservationDeletedList) {
        this.invReservationDeletedList = invReservationDeletedList;
    }

    public List<InvReservationDetail> getInveDetailsList() {
        return inveDetailsList;
    }

    public void setInveDetailsList(List<InvReservationDetail> inveDetailsList) {
        this.inveDetailsList = inveDetailsList;
    }

    public Integer getInvReservationId() {
        return invReservationId;
    }

    public void setInvReservationId(Integer invReservationId) {
        this.invReservationId = invReservationId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public BigDecimal getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(BigDecimal finalTotal) {
        this.finalTotal = finalTotal;
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

    public BigDecimal getTotalTotal() {
        return totalTotal;
    }

    public void setTotalTotal(BigDecimal totalTotal) {
        this.totalTotal = totalTotal;
    }

    public BigDecimal getTotalAdding() {
        return totalAdding;
    }

    public void setTotalAdding(BigDecimal totalAdding) {
        this.totalAdding = totalAdding;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getTotalNet() {
        return totalNet;
    }

    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    public Boolean getMarkEdit() {
        return markEdit;
    }

    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
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

    public Map<Integer, InvReservationDetail> getInvReservationDetailMap() {
        return invReservationDetailMap;
    }

    public void setInvReservationDetailMap(Map<Integer, InvReservationDetail> invReservationDetailMap) {
        this.invReservationDetailMap = invReservationDetailMap;
    }

    public List<InvUser> getInvUserList() {
        return invUserList;
    }

    public void setInvUserList(List<InvUser> invUserList) {
        this.invUserList = invUserList;
    }

    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }

    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    public InvOrganizationSiteConverter getCustomerConvertor() {
        return customerConvertor;
    }

    public void setCustomerConvertor(InvOrganizationSiteConverter customerConvertor) {
        this.customerConvertor = customerConvertor;
    }

    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    public InvDelegatorConvertor getDelegatorConvertor() {
        return delegatorConvertor;
    }

    public void setDelegatorConvertor(InvDelegatorConvertor delegatorConvertor) {
        this.delegatorConvertor = delegatorConvertor;
    }

    public List<InvReservationDetailEntity> getInvReservationDetailsEntityList() {
        return invReservationDetailsEntityList;
    }

    public void setInvReservationDetailsEntityList(List<InvReservationDetailEntity> invReservationDetailsEntityList) {
        this.invReservationDetailsEntityList = invReservationDetailsEntityList;
    }

    public ItemsBarcodesDetailsViewConverter getItemsBarcodesDetailsViewConverter() {
        return itemsBarcodesDetailsViewConverter;
    }

    public void setItemsBarcodesDetailsViewConverter(ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter) {
        this.itemsBarcodesDetailsViewConverter = itemsBarcodesDetailsViewConverter;
    }

    /**
     * @return the invSetup
     */
    public InventorySetup getInvSetup() {
        return invSetup;
    }

    /**
     * @param invSetup the invSetup to set
     */
    public void setInvSetup(InventorySetup invSetup) {
        this.invSetup = invSetup;
    }
}

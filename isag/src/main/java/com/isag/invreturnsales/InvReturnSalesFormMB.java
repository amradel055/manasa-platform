/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invreturnsales;

import com.isag.businessservice.CostCenterService;
import com.isag.businessservice.CurrencyOperationService;
import com.isag.businessservice.CurrencyService;
import com.isag.businessservice.GlAdminUnitService;
import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.InvReturnPurchaseDetailService;
import com.isag.businessservice.InvReturnPurchaseService;
import com.isag.businessservice.InventorySetupService;
import com.isag.businessservice.InvoiceNetService;
import com.isag.businessservice.IsagUserBankService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.QuantityItemsStoreService;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.converter.CostCenterConverter;
import com.isag.converter.CurrencyConverter;
import com.isag.converter.GlAdminUnitConverter;
import com.isag.converter.GlBankConverter;
import com.isag.converter.InvDelegatorConvertor;
import com.isag.converter.InvInventoryConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.InvPurchaseInvoiceConverter;
import com.isag.converter.InvPurchaseInvoiceDetailConverter;
import com.isag.converter.ItemConverter;
import com.isag.converter.ItemsBarcodesDetailsViewConverter;
import com.isag.converter.SymbolConverter;
import com.isag.entity.CostCenter;
import com.isag.entity.Currency;
import com.isag.entity.CurrencyOperation;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.GlBank;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InvReturnPurchase;
import com.isag.entity.InvReturnPurchaseDetail;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.InventorySetup;
import com.isag.entity.Symbol;
import com.isag.entiy.InvPurchaseInvoiceDetailsEntity;
import com.isag.entiy.InvReturnDetailEntity;
import com.isag.entiy.InvReturnPurchaseDetailEntity;
import com.isag.entiy.InvReturnPurchaseEntity;
import com.isag.invPurchaseInvoice.InvPurchaseInvoiceFormMB;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.views.NetView;
import com.isag.views.QuantityItemsStore;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author WIN7
 */
@Named(value = "InvReturnSalesFormMB")
@ViewScoped
public class InvReturnSalesFormMB extends BaseFormBean {

    private Integer branchId;
    private Integer companyId;
    private Integer invReturnPurchaseId;
    private Boolean viewDuDate = Boolean.FALSE;
    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private BigDecimal totalSum = BigDecimal.ZERO;
    private BigDecimal totalLocal = BigDecimal.ZERO;
    private BigDecimal totalNet = BigDecimal.ZERO;
    private BigDecimal netAll = BigDecimal.ZERO;
    private BigDecimal totalNetLocal = BigDecimal.ZERO;
    Integer index = 0;
    BigDecimal rate;

    BigDecimal hundred = new BigDecimal(100);

    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;

    private CurrencyOperation currencyOperation;

    // Converters
    private CostCenterConverter costCenterConverter;
    private SymbolConverter unitConverter;
    private ItemConverter invItemConverter;
    private CurrencyConverter currencyConverter;
    private InvDelegatorConvertor salesConverter;
    private GlAdminUnitConverter adminUnitConverter;
    private InvInventoryConverter invInventoryConverter;
    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;
    private InvPurchaseInvoiceConverter invSalesInvoiceConverter;
    private InvPurchaseInvoiceDetailConverter invSalesInvoiceDetailConverter;
    private GlBankConverter glBankConverter;

    // DB Entities
    private InvReturnPurchase invReturnSales;
    private List<InvReturnPurchaseDetail> invReturnSalesDetailList;
    private List<InvReturnPurchaseDetail> invReturnSalesDetailUpdatedList;
    private List<InvReturnPurchaseDetail> invReturnSalesDetailDeletedList;
    private List<InvReturnPurchaseDetail> invReturnPurchaseDetailList;
    private List<InvReturnDetailEntity> invReturnDetailEntityList;
    private List<InvPurchaseInvoice> invSalesInvoiceList;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsLoadedList;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsDeletedList;
    private List<InvInventory> invInventoryList;

    // Interface Entities
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntitySelectedList;
    private InvReturnPurchaseEntity invReturnSalesEntity;
    private InvReturnPurchaseDetailEntity invReturnPurchaseDetailEntity;
    private InvReturnPurchaseDetailEntity invReturnPurchaseDetailEntitySelected;
    private List<InvReturnPurchaseDetailEntity> invReturnPurchaseDetailEntityList;

    private List<InvOrganizationSite> invOrganizationSiteList;
    private InvOrganizationSiteConverter supplierConvertor;
    private List<CostCenter> costCenterList;
    private List<GlAdminUnit> adminUnitList;
    private List<InventoryDelegator> salesPersonsList;
    private List<InvItem> invItemList;
    private List<Currency> currencyList;
    private List<InvPurchaseOrder> invPurchaseOrderList;
    private List<InvAddingorder> invAddingOrderList;
    private List<InvAddingorder> invAddingOrderSelectedList;
    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;
    private List<GlBank> glBankList;

    private List<InvReturnPurchaseDetailEntity> rowsDeleted;

    private List<InvPurchaseOrder> invPurchaseOrderSelectedList;

    InventorySetup invSetup;

    Map<Integer, BigDecimal> itemsMap = new HashMap<>();
    Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();

    @EJB
    private InvReturnPurchaseService invReturnPurchaseService;
    @EJB
    private InvReturnPurchaseDetailService invReturnPurchaseDetailService;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    private CurrencyOperationService currencyOperationService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private CostCenterService costCenterService;
    @EJB
    private GlAdminUnitService glAdminUnitService;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    private IsagUserBankService isagUserBankService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private QuantityItemsStoreService quantityItemsStoreService;
    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private InvoiceNetService invoiceNetService;

    @Override
    @PostConstruct
    public void init() {
        intializeObjects();
        load();
    }

    private void intializeObjects() {
        invReturnSales = new InvReturnPurchase();
        invSalesInvoiceList = new ArrayList<>();
        invReturnSalesDetailList = new ArrayList<>();
        invReturnSalesEntity = new InvReturnPurchaseEntity();
        invReturnPurchaseDetailEntity = new InvReturnPurchaseDetailEntity();
        invReturnPurchaseDetailEntitySelected = new InvReturnPurchaseDetailEntity();
        invPurchaseInvoiceDetailsEntitySelectedList = new ArrayList<>();
        invReturnPurchaseDetailEntityList = new ArrayList<>();
        rowsDeleted = new ArrayList<>();
        invPurchaseInvoiceDetailsLoadedList = new ArrayList<>();
        invReturnSalesDetailUpdatedList = new ArrayList<>();
        invPurchaseInvoiceDetailsDeletedList = new ArrayList<>();
        invOrganizationSiteList = new ArrayList<>();
        costCenterList = new ArrayList<>();
        adminUnitList = new ArrayList<>();
        invItemList = new ArrayList<>();
        currencyList = new ArrayList<>();
        invPurchaseOrderList = new ArrayList<>();
        invAddingOrderList = new ArrayList<>();
        invInventoryList = new ArrayList<>();
        invSetup = new InventorySetup();
        itemsBarcodesDetailsViewList = new ArrayList<>();
        invReturnSalesDetailDeletedList = new ArrayList<>();
        salesPersonsList = new ArrayList<>();
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            setBranchId(getUserData().getUserBranch().getId());
            setCompanyId(getUserData().getCompany().getId());

            fillLists();

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetInvReturnPurchaseForm();
                showMessageDetails = showMessageGeneral = false;
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {

                setInvReturnPurchaseId((Integer) context.getSessionMap().get("invPurchaseInvoiceIdSeclected"));

                invReturnSales = invReturnPurchaseService.findInvReturnPurchaseById(invReturnPurchaseId);

                invReturnSalesDetailList = invReturnPurchaseDetailService.getAllReturnPurchaseDetailsByReturnPurchaseId(invReturnPurchaseId);

                invReturnSalesEntity = mapReturnPurchaseToReturnPurchaseEntity(invReturnSales);
            }

            fillItemMap();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "load");
        }

        //      loadPurchaseInvoice();
    }

    private void fillLists() {
        try {
            invOrganizationSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(branchId, true, 0); // العميل
            supplierConvertor = new InvOrganizationSiteConverter(invOrganizationSiteList);

            salesPersonsList = invDelegatorService.getSalesByBranch(branchId); // مندوبين المبيعات
            salesConverter = new InvDelegatorConvertor(salesPersonsList);

            costCenterList = costCenterService.getActiveSubCostCentersByBranch(branchId); // مراكز التكلفة
            costCenterConverter = new CostCenterConverter(costCenterList);

            adminUnitList = glAdminUnitService.getAllSubAdminUnitByBranchIdActive(branchId); // الوحدات الادارية
            adminUnitConverter = new GlAdminUnitConverter(adminUnitList);

            invSalesInvoiceList = invPurchaseInvoiceService.getALLInvPurchaseInvoicePostFlagedByBranchIdPer(branchId, 1); // فواتير المبيعات
            invSalesInvoiceConverter = new InvPurchaseInvoiceConverter(invSalesInvoiceList);

//        invItemList = invItemService.getInvItemByBranchId(branchId); // الاصناف
//        invItemConverter = new ItemConverter(invItemList);
            itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViewList);

            for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
                itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
            }

            currencyList = currencyService.getAllCurrenciesWithLocalCurrencyHavingRatesByCompanyId(companyId); // العملات
            currencyConverter = new CurrencyConverter(currencyList);

            invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId);
            invInventoryConverter = new InvInventoryConverter(invInventoryList);

            glBankList = isagUserBankService.getAllGlBankForUserByTypeAndBranchIdPer(getUserData().getUser().getId(), branchId, 0);
            glBankConverter = new GlBankConverter(glBankList);

            invSetup = inventorySetupService.getInventoryByBranchId(branchId);
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "fillLists");
        }

    }

    public void fillItemMap() {
        try {
            if (invReturnSalesEntity.getInvInventoryId() != null) {
                List<QuantityItemsStore> quantityItemsStoresList = quantityItemsStoreService.findInventoryItemsList(invReturnSalesEntity.getInvInventoryId().getId(), branchId, invSetup.getSellBuy());

                for (QuantityItemsStore quantityItemsStore : quantityItemsStoresList) {
                    itemsMap.put(quantityItemsStore.getItemId(), quantityItemsStore.getQty());
                }
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "fillItemMap");
        }
    }

    public void hishis() {

    }

    public void glBankNull() {
        try {
            if (invReturnSalesEntity.getPaymentType() == 0) {
                invReturnSalesEntity.setGlBankId(glBankList.get(0));
                setViewDuDate((Boolean) false);
            } else {
                invReturnSalesEntity.setGlBankId(null);
                setViewDuDate((Boolean) true);
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "glBankNull");
        }
    }

    private void resetInvReturnPurchaseForm() {
        try {
            invReturnSalesEntity = new InvReturnPurchaseEntity();
            invReturnSalesEntity.setDate(new Date());

            if (invInventoryList != null && !invInventoryList.isEmpty()) {
                invReturnSalesEntity.setInvInventoryId(invInventoryList.get(0));
            }
            if (glBankList != null && !glBankList.isEmpty()) {
                invReturnSalesEntity.setGlBankId(glBankList.get(0));
            } else {
                errorMessage("لا توجد خزائن");
            }

            if (invOrganizationSiteList != null && !invOrganizationSiteList.isEmpty()) {
                invReturnSalesEntity.setSupplierId(invOrganizationSiteList.get(0));
            } else {
                errorMessage("لا توجد موردين");
            }

            if (invSalesInvoiceList != null && !invSalesInvoiceList.isEmpty()) {
                invReturnSalesEntity.setPurchaseInvoice(invSalesInvoiceList.get(0));
                loadDefaultInvPurchaseData();
            }

            if (salesPersonsList != null && !salesPersonsList.isEmpty()) {
                invReturnSalesEntity.setSalesPerson(salesPersonsList.get(0));
            } else {
                errorMessage("لا يوجد مندوبين مبيعات");
            }

            updateRate();

            invReturnPurchaseDetailEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "resetInvReturnPurchaseForm");
        }
    }

    private InvReturnPurchaseEntity mapReturnPurchaseToReturnPurchaseEntity(InvReturnPurchase returnPurchase) {
        try {

            invReturnSalesEntity = new InvReturnPurchaseEntity();

            invReturnSalesEntity.setId(returnPurchase.getId());
            invReturnSalesEntity.setSerial(returnPurchase.getSerial());
            invReturnSalesEntity.setBranchId(returnPurchase.getBranchId());
            invReturnSalesEntity.setCostCenter(returnPurchase.getCostCenter());
            invReturnSalesEntity.setCurrencyId(returnPurchase.getCurrencyId());
            invReturnSalesEntity.setDate(returnPurchase.getDate() != null ? returnPurchase.getDate() : new Date());
            invReturnSalesEntity.setRemark(returnPurchase.getRemark());
            invReturnSalesEntity.setRate(returnPurchase.getRate() != null ? returnPurchase.getRate() : BigDecimal.ONE);
            invReturnSalesEntity.setSupplierId(returnPurchase.getSupplierId());
            invReturnSalesEntity.setInvInventoryId(returnPurchase.getInvInventoryId());
            invReturnSalesEntity.setGlBankId(returnPurchase.getGlBankId());
            invReturnSalesEntity.setPaymentType(returnPurchase.getPaymentType());
            invReturnSalesEntity.setTaxvalue(returnPurchase.getTaxValue());
            invReturnSalesEntity.setDiscount(returnPurchase.getDiscountValue());

            if (returnPurchase.getPurchaseInvoiceId() != null) {
                invReturnSalesEntity.setPurchaseInvoice(returnPurchase.getPurchaseInvoiceId());
                invReturnSalesEntity.setInvoiceDate(returnPurchase.getPurchaseInvoiceId().getDate());
                loadDefaultInvPurchaseData();
            }

            totalNet = BigDecimal.ZERO;
            totalSum = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            totalNetLocal = BigDecimal.ZERO;
            invReturnPurchaseDetailEntityList = new ArrayList<>();
            InvItem item;
            for (InvReturnPurchaseDetail purchaseDetail : invReturnSalesDetailList) {

                totalLocal = BigDecimal.ZERO;
                invReturnPurchaseDetailEntity = new InvReturnPurchaseDetailEntity();
                invReturnPurchaseDetailEntity.setId(purchaseDetail.getId());
                invReturnPurchaseDetailEntity.setSerial(purchaseDetail.getSerial());
                invReturnPurchaseDetailEntity.setMarkEdit(false);
                invReturnPurchaseDetailEntity.setBranchId(purchaseDetail.getBranchId());
                invReturnPurchaseDetailEntity.setDicountType(purchaseDetail.getDicountType());
                invReturnPurchaseDetailEntity.setInvReturnPurchaseId(purchaseDetail.getInvReturnPurchaseId());

                item = new InvItem();

                item.setId(purchaseDetail.getItemId().getId());
                item.setName(purchaseDetail.getItemId().getName());
                item.setCode(purchaseDetail.getItemBarcode());
                item.setUnitId(purchaseDetail.getItemId().getUnitId());

                invReturnPurchaseDetailEntity.setItemId(item);
                invReturnPurchaseDetailEntity.setQuantity(purchaseDetail.getQuantity());
                invReturnPurchaseDetailEntity.setUnitPrice(purchaseDetail.getUnitPrice());
                invReturnPurchaseDetailEntity.setNet(purchaseDetail.getNet());
                invReturnPurchaseDetailEntity.setScrewing(purchaseDetail.getScrewing());

                totalQuatity = (totalQuatity.add(invReturnPurchaseDetailEntity.getQuantity() != null ? invReturnPurchaseDetailEntity.getQuantity() : BigDecimal.ZERO)).setScale(2);
                totalNet = (totalNet.add(invReturnPurchaseDetailEntity.getNet() != null ? invReturnPurchaseDetailEntity.getNet() : BigDecimal.ZERO)).setScale(2);

                invReturnPurchaseDetailEntityList.add(invReturnPurchaseDetailEntity);
            }

            invReturnSalesEntity.setTotal(totalNet);

            updatefooter();

            return invReturnSalesEntity;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "mapReturnPurchaseToReturnPurchaseEntity");
            return null;
        }
    }

    public void rowSelected(SelectEvent event) {
        try {
            DataTable dataTable = (DataTable) event.getSource();
            invReturnPurchaseDetailEntitySelected = (InvReturnPurchaseDetailEntity) dataTable.getRowData();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "rowSelected");
        }

    }

    public void addInvPurchaseInvoiceDetail() {
        try {

            for (InvReturnPurchaseDetailEntity detailEntity : invReturnPurchaseDetailEntityList) {
                detailEntity.setMarkEdit(Boolean.FALSE);

                if (invReturnSalesEntity.getPurchaseInvoice() != null) {
                    if (detailEntity.getPurchaseInvoiceDetailTrans() == null || detailEntity.getItemId() == null || detailEntity.getQuantity() == null) {
                        errorMessage("يجب ادخال تفاصيل الفاتورة");
                        return;
                    }

                    // get old value from trans as they are deleted when delete row
                    detailEntity.setPurchaseInvoiceDetail(detailEntity.getPurchaseInvoiceDetailTrans());

                    InvItem item = new InvItem();
                    item.setId(detailEntity.getPurchaseInvoiceDetailTrans().getItemId().getId());
                    item.setName(detailEntity.getPurchaseInvoiceDetailTrans().getItemId().getName());
                    item.setCode(detailEntity.getPurchaseInvoiceDetailTrans().getItemBarcode());
                    item.setUnitId(detailEntity.getPurchaseInvoiceDetailTrans().getItemId().getUnitId());

                    detailEntity.setItemId(item);
                } else {
                    if (detailEntity.getItemId() == null || detailEntity.getQuantity() == null) {
                        errorMessage("يجب ادخال تفاصيل الفاتورة");
                        return;
                    }

                    // get old value from trans as they are deleted when delete row
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());

                    InvItem item = new InvItem();
                    Symbol unit = new Symbol();

                    unit.setId(detailEntity.getItemsBarcodesDetailTrans().getUnitId());
                    unit.setName(detailEntity.getItemsBarcodesDetailTrans().getUnitName());

                    item.setId(detailEntity.getItemsBarcodesDetailTrans().getItemId());
                    item.setName(detailEntity.getItemsBarcodesDetailTrans().getName());
                    item.setCode(detailEntity.getItemsBarcodesDetailTrans().getBarcode());
                    item.setUnitId(unit);
                    detailEntity.setUnit(unit);

                    detailEntity.setItemId(item);
                }

                if (!validateQuantity(detailEntity)) {
                    return;
                }
            }

            InvReturnPurchaseDetailEntity returnPurchaseDetailEntityNew = new InvReturnPurchaseDetailEntity();
            returnPurchaseDetailEntityNew.setMarkEdit(Boolean.TRUE);

            returnPurchaseDetailEntityNew.setCounter(++index);
            invReturnPurchaseDetailEntitySelected = returnPurchaseDetailEntityNew;
            invReturnPurchaseDetailEntityList.add(returnPurchaseDetailEntityNew);
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "addInvPurchaseInvoiceDetail");
        }
    }

    public void deleteInvPurchaseInvoiceDetail() {
        try {
            showMessageGeneral = Boolean.FALSE;
            showMessageDetails = Boolean.TRUE;

            if (invReturnPurchaseDetailEntitySelected.getId() != null) {
                getRowsDeleted().add(invReturnPurchaseDetailEntitySelected);
            }

            for (InvReturnPurchaseDetailEntity detailEntity : invReturnPurchaseDetailEntityList) {
                if (invReturnSalesEntity.getPurchaseInvoice() != null) {
                    if (detailEntity.getPurchaseInvoiceDetail() == null && detailEntity.getPurchaseInvoiceDetailTrans() != null) {
                        detailEntity.setPurchaseInvoiceDetail(detailEntity.getPurchaseInvoiceDetailTrans());
                    }
                } else {
                    if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                        detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                    }
                }
            }

            invReturnPurchaseDetailEntityList.remove(invReturnPurchaseDetailEntitySelected);

            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "deleteInvPurchaseInvoiceDetail");
        }
    }

    public List<CostCenter> completeCostCenters(String query) {
        try {
            List<CostCenter> centersList = costCenterList;
            if (query == null || query.trim().equals("")) {

                costCenterConverter = new CostCenterConverter(centersList);
                return centersList;
            }
            List<CostCenter> filteredCostCenters = new ArrayList<>();

            String nameAr;
            Integer code;
            CostCenter costCenterFilter;
            for (int i = 0; i < costCenterList.size(); i++) {
                costCenterFilter = centersList.get(i);
                nameAr = costCenterFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(costCenterFilter)) {
                        filteredCostCenters.add(costCenterFilter);
                    }
                }

                code = costCenterFilter.getCode();
                if (code != null && String.valueOf(code).contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(costCenterFilter)) {
                        filteredCostCenters.add(costCenterFilter);
                    }
                }
            }

            costCenterConverter = new CostCenterConverter(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "completeCostCenters");
            return null;
        }
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
            saveError(e, "InvReturnSalesFormMB", "completeItemsData");
            return null;
        }
    }

    public List<InvPurchaseInvoiceDetail> completePurchaseInvoiceDetail(String query) {
        try {
            List<InvPurchaseInvoiceDetail> invoiceDetails = invPurchaseInvoiceDetailsLoadedList;
            if (query == null || query.trim().equals("")) {
                invSalesInvoiceDetailConverter = new InvPurchaseInvoiceDetailConverter(invoiceDetails);
                return invoiceDetails;
            }
            List<InvPurchaseInvoiceDetail> filteredItems = new ArrayList<>();

            String nameAr;
            String code;
            InvPurchaseInvoiceDetail barcodesDetailsView;
            for (int i = 0; i < invPurchaseInvoiceDetailsLoadedList.size(); i++) {
                barcodesDetailsView = invoiceDetails.get(i);
                nameAr = barcodesDetailsView.getItemId().getName();
                if (nameAr != null && nameAr.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }

                code = barcodesDetailsView.getItemBarcode();
                if (code != null && code.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }
            }

            invSalesInvoiceDetailConverter = new InvPurchaseInvoiceDetailConverter(filteredItems);
            return filteredItems;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "completePurchaseInvoiceDetail");
            return null;
        }
    }

    public List<InvOrganizationSite> completeSuppllier(String query) {
        try {
            List<InvOrganizationSite> supplierList = invOrganizationSiteList;
            if (query == null || query.trim().equals("")) {

                supplierConvertor = new InvOrganizationSiteConverter(supplierList);
                return supplierList;
            }
            List<InvOrganizationSite> filteredSuppliers = new ArrayList<>();

            String nameAr;
            String code;
            InvOrganizationSite supplierFilter;
            for (int i = 0; i < invOrganizationSiteList.size(); i++) {
                supplierFilter = supplierList.get(i);
                nameAr = supplierFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }

                code = supplierFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }
            }

            supplierConvertor = new InvOrganizationSiteConverter(filteredSuppliers);
            return filteredSuppliers;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "completeSuppllier");
            return null;
        }
    }

    public List<Currency> completeCurrency(String query) {
        try {
            List<Currency> currencyListF = currencyList;
            if (query == null || query.trim().equals("")) {

                currencyConverter = new CurrencyConverter(currencyListF);
                return currencyListF;
            }
            List<Currency> filteredCurrencies = new ArrayList<>();

            String nameAr;
            String code;
            Currency currencyFilter;
            for (int i = 0; i < currencyList.size(); i++) {
                currencyFilter = currencyListF.get(i);
                nameAr = currencyFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCurrencies.contains(currencyFilter)) {
                        filteredCurrencies.add(currencyFilter);
                    }
                }

                code = currencyFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCurrencies.contains(currencyFilter)) {
                        filteredCurrencies.add(currencyFilter);
                    }
                }
            }

            currencyConverter = new CurrencyConverter(filteredCurrencies);
            return filteredCurrencies;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "completeCurrency");
            return null;
        }
    }

    public List<GlAdminUnit> completeAdminUnits(String query) {
        try {
            List<GlAdminUnit> adminList = adminUnitList;
            if (query == null || query.trim().equals("")) {

                adminUnitConverter = new GlAdminUnitConverter(adminList);
                return adminList;
            }
            List<GlAdminUnit> filteredAdminUnits = new ArrayList<>();

            String nameAr;
            Integer code;
            GlAdminUnit currencyFilter;
            for (int i = 0; i < adminUnitList.size(); i++) {
                currencyFilter = adminList.get(i);
                nameAr = currencyFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredAdminUnits.contains(currencyFilter)) {
                        filteredAdminUnits.add(currencyFilter);
                    }
                }

                code = currencyFilter.getCode();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredAdminUnits.contains(currencyFilter)) {
                        filteredAdminUnits.add(currencyFilter);
                    }
                }
            }

            adminUnitConverter = new GlAdminUnitConverter(filteredAdminUnits);
            return filteredAdminUnits;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "completeAdminUnits");
            return null;
        }
    }

    public List<InventoryDelegator> completeSalesPerson(String query) {
        try {
            List<InventoryDelegator> centersList = salesPersonsList;
            if (query == null || query.trim().equals("")) {

                salesConverter = new InvDelegatorConvertor(centersList);
                return centersList;
            }
            List<InventoryDelegator> filteredCostCenters = new ArrayList<>();

            String nameAr;
            String code;
            InventoryDelegator costCenterFilter;
            for (int i = 0; i < salesPersonsList.size(); i++) {
                costCenterFilter = centersList.get(i);
                nameAr = costCenterFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(costCenterFilter)) {
                        filteredCostCenters.add(costCenterFilter);
                    }
                }

                code = costCenterFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(costCenterFilter)) {
                        filteredCostCenters.add(costCenterFilter);
                    }
                }
            }

            salesConverter = new InvDelegatorConvertor(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "completeSalesPerson");
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
            saveError(e, "InvReturnSalesFormMB", "completeInventory");
            return null;
        }
    }

    public List<InvPurchaseInvoice> completePurchaseInvoice(String query) {
        try {
            List<InvPurchaseInvoice> invList = invSalesInvoiceList;
            if (query == null || query.trim().equals("")) {

                invSalesInvoiceConverter = new InvPurchaseInvoiceConverter(invList);
                return invList;
            }
            List<InvPurchaseInvoice> filteredInvs = new ArrayList<>();

            Integer code;
            InvPurchaseInvoice invFilter;
            for (int i = 0; i < invSalesInvoiceList.size(); i++) {
                invFilter = invList.get(i);

                code = invFilter.getSerial();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invFilter)) {
                        filteredInvs.add(invFilter);
                    }
                }
            }

            invSalesInvoiceConverter = new InvPurchaseInvoiceConverter(filteredInvs);
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "completePurchaseInvoice");
            return null;
        }
    }

    public List<GlBank> completeGlBank(String query) {
        try {
            List<GlBank> glBanksList = glBankList;
            if (query == null || query.trim().equals("")) {

                glBankConverter = new GlBankConverter(glBanksList);
                return glBanksList;
            }
            List<GlBank> filteredGlBanks = new ArrayList<>();

            String nameAr;
            String code;
            GlBank glBankFilter;
            for (int i = 0; i < glBankList.size(); i++) {
                glBankFilter = glBanksList.get(i);
                nameAr = glBankFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlBanks.contains(glBankFilter)) {
                        filteredGlBanks.add(glBankFilter);
                    }
                }

                code = glBankFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlBanks.contains(glBankFilter)) {
                        filteredGlBanks.add(glBankFilter);
                    }
                }
            }

            glBankConverter = new GlBankConverter(filteredGlBanks);
            return filteredGlBanks;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "completeGlBank");
            return null;
        }
    }

    public void loadPurchaseInvoice() {
        try {
            if (invReturnSalesEntity.getPurchaseInvoice() == null) {
                if (invReturnPurchaseDetailEntityList != null && !invReturnPurchaseDetailEntityList.isEmpty()) {
                    OpenDlg("dlg1");
                } else {
                    invReturnSalesEntity.setSupplierId((invOrganizationSiteList != null && !invOrganizationSiteList.isEmpty()) ? invOrganizationSiteList.get(0) : null);
                }
            } else {
                if (invReturnPurchaseDetailEntityList != null && !invReturnPurchaseDetailEntityList.isEmpty()) {
                    if (!invReturnSalesEntity.getPurchaseInvoice().equals(invReturnSalesEntity.getPurchaseInvoiceTrans())) {
                        OpenDlg("dlg2");
                    }
                } else {
                    initPurchaseInvoice();
                }
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "loadPurchaseInvoice");

        }
    }

    public void configmDlg() {
        try {
            if (invReturnSalesEntity.getPurchaseInvoice() != null) {
                initPurchaseInvoice();
                CloseDlg("dlg2");
            } else {
                invReturnPurchaseDetailEntityList = new ArrayList<>();
                invReturnSalesEntity.setPurchaseInvoice(null);
                invReturnSalesEntity.setPurchaseInvoiceTrans(null);
                CloseDlg("dlg1");
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "configmDlg");

        }
    }

    private void initPurchaseInvoice() {
        try {
            invReturnPurchaseDetailEntityList = new ArrayList<>();
            invPurchaseInvoiceDetailsLoadedList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(invReturnSalesEntity.getPurchaseInvoice().getId());
            invReturnSalesEntity.setPurchaseInvoiceTrans(invReturnSalesEntity.getPurchaseInvoice());
            loadDefaultInvPurchaseData();
            invSalesInvoiceDetailConverter = new InvPurchaseInvoiceDetailConverter(invPurchaseInvoiceDetailsLoadedList);
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "initPurchaseInvoice");

        }
    }

    private void loadDefaultInvPurchaseData() {
        try {
            invReturnSalesEntity.setSupplierId(invReturnSalesEntity.getPurchaseInvoice().getOrganizationSiteId());
            invReturnSalesEntity.setPaymentType(invReturnSalesEntity.getPurchaseInvoice().getPaymentType());
            invReturnSalesEntity.setInvInventoryId(invReturnSalesEntity.getPurchaseInvoice().getInvInventoryId());
            invReturnSalesEntity.setGlBankId(invReturnSalesEntity.getPurchaseInvoice().getGlBankId());
            invReturnSalesEntity.setCostCenter(invReturnSalesEntity.getPurchaseInvoice().getCostCenterId());
            invReturnSalesEntity.setInvoiceDate(invReturnSalesEntity.getPurchaseInvoice().getDate());
            invReturnSalesEntity.setRemark(invReturnSalesEntity.getPurchaseInvoice().getRemarks());
            invReturnSalesEntity.setDiscount(invReturnSalesEntity.getPurchaseInvoice().getDiscount());
            if (invReturnSalesEntity.getPurchaseInvoice() != null && invReturnSalesEntity.getPurchaseInvoice().getTaxflag() != null) {
//            NetView sumAfterDiscount = invoiceNetService.getInvPurchaseInvoiceFromViewByInvoiceId(branchId, invReturnSalesEntity.getPurchaseInvoice().getType(), invReturnSalesEntity.getPurchaseInvoice().getId());
//            sumAfterDiscount.getSumAfterDiscount();
//            BigDecimal netBeforTax = invReturnSalesEntity.getTotal().subtract(invReturnSalesEntity.getDiscount() != null ? invReturnSalesEntity.getDiscount() : BigDecimal.ZERO);
//            invReturnSalesEntity.setTaxvalue((netBeforTax.multiply(new BigDecimal(14))).divide(new BigDecimal(100)));
            }

            stringPaymentType(invReturnSalesEntity);
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "loadDefaultInvPurchaseData");

        }
    }

    public void stringPaymentType(InvReturnPurchaseEntity invReturnPurchaseEntity) {
        try {
            if (invReturnPurchaseEntity.getPurchaseInvoice() != null) {
                if (invReturnPurchaseEntity.getPurchaseInvoice().getPaymentType() != null && invReturnPurchaseEntity.getPurchaseInvoice().getPaymentType() == 0) {
                    invReturnPurchaseEntity.setPaymentTypeText("نقدى");
                } else if (invReturnPurchaseEntity.getPurchaseInvoice().getPaymentType() != null && invReturnPurchaseEntity.getPurchaseInvoice().getPaymentType() == 1) {
                    invReturnPurchaseEntity.setPaymentTypeText("أجل");
                } else if (invReturnPurchaseEntity.getPurchaseInvoice().getPaymentType() != null && invReturnPurchaseEntity.getPurchaseInvoice().getPaymentType() == 2) {
                    invReturnPurchaseEntity.setPaymentTypeText("اعتماد مستندى");
                }
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "stringPaymentType");

        }
    }

    public void OpenDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "OpenDlg");

        }
    }

    public void CloseDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
            invReturnSalesEntity.setPurchaseInvoice(invReturnSalesEntity.getPurchaseInvoiceTrans());
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "CloseDlg");

        }
    }

    public void putCurrency() {
        try {
            if (invReturnSalesEntity.getSupplierId() != null && invReturnSalesEntity.getSupplierId().getCurrencyId() != null) {
                invReturnSalesEntity.setCurrencyId(invReturnSalesEntity.getSupplierId().getCurrencyId());
                updateRate();
                updateSummition();
            } else {
                errorMessage("هذا المورد ليس له عملة!");
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "putCurrency");

        }
    }

    public void validateDiscountColumn(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            if (!validateDiscount(returnPurchaseDetailEntity)) {
                returnPurchaseDetailEntity.setDiscount(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "validateDiscountColumn");

        }
    }

    public Boolean validateDiscount(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            return returnPurchaseDetailEntity.getDiscount() != null && returnPurchaseDetailEntity.getDiscount().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "validateDiscount");
            return null;
        }
    }

    public void validatePriceColumn(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            if (!validatePrice(returnPurchaseDetailEntity)) {
                returnPurchaseDetailEntity.setUnitPrice(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "validatePriceColumn");
        }
    }

    public Boolean validatePrice(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            return returnPurchaseDetailEntity != null
                    && returnPurchaseDetailEntity.getItemId() != null
                    && returnPurchaseDetailEntity.getUnitPrice() != null
                    && returnPurchaseDetailEntity.getUnitPrice().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "validatePrice");
            return null;
        }
    }

    public void validateQuantityColum(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            if (!validateQuantity(returnPurchaseDetailEntity)) {
                if (returnPurchaseDetailEntity.getPurchaseInvoiceDetail() != null) {
                    returnPurchaseDetailEntity.setQuantity(BigDecimal.ZERO);
                } else {
                    returnPurchaseDetailEntity.setQuantity(null);
                }
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "validateQuantityColum");
        }
    }

    public Boolean validateItems(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            if (returnPurchaseDetailEntity.getItemsBarcodesDetail() == null) {
                resetInvItem(returnPurchaseDetailEntity);
            } else {
                for (InvReturnPurchaseDetailEntity detailEntity : invReturnPurchaseDetailEntityList) {
                    if (!detailEntity.getCounter().equals(returnPurchaseDetailEntity.getCounter())
                            && detailEntity.getItemsBarcodesDetailTrans().getBarcode().equals(returnPurchaseDetailEntity.getItemsBarcodesDetail().getBarcode())) {
                        resetSelected();
                        return errorMessage("تم اختيار هذا الصنف من قبل");
                    }
                }

                returnPurchaseDetailEntity.setItemsBarcodesDetailTrans(returnPurchaseDetailEntity.getItemsBarcodesDetail());
                returnPurchaseDetailEntity.setUnitPrice(returnPurchaseDetailEntity.getItemsBarcodesDetail().getCostAverage());

                Symbol unit = new Symbol();
                unit.setId(returnPurchaseDetailEntity.getItemsBarcodesDetail().getUnitId());
                unit.setName(returnPurchaseDetailEntity.getItemsBarcodesDetail().getUnitName());
                returnPurchaseDetailEntity.setUnit(unit);

                InvItem item = new InvItem();
                item.setId(returnPurchaseDetailEntity.getItemsBarcodesDetail().getItemId());
                item.setName(returnPurchaseDetailEntity.getItemsBarcodesDetail().getName());
                item.setCode(returnPurchaseDetailEntity.getItemsBarcodesDetail().getBarcode());
                item.setUnitId(unit);
                returnPurchaseDetailEntity.setItemId(item);
            }
            updateSummition();
            return true;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "validateItems");
            return null;
        }

    }

    public Boolean validateItem(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            if (returnPurchaseDetailEntity.getPurchaseInvoiceDetail() != null) {
                returnPurchaseDetailEntity.setPurchaseInvoiceDetailTrans(returnPurchaseDetailEntity.getPurchaseInvoiceDetail());

                for (InvReturnPurchaseDetailEntity detailEntity : invReturnPurchaseDetailEntityList) {
                    if (!detailEntity.getCounter().equals(returnPurchaseDetailEntity.getCounter())
                            && detailEntity.getPurchaseInvoiceDetailTrans().equals(returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans())) {
                        resetSelected();
                        return errorMessage("تم اختيار هذا الصنف من قبل");
                    }
                }
                BigDecimal returnPurchaseSumQuantity, quantity;
                returnPurchaseSumQuantity = invReturnPurchaseDetailService.getQuantitySummitionForReturnPurchaseByPurchaseInvoiceDetail(branchId, returnPurchaseDetailEntity.getPurchaseInvoiceDetail().getId());

                quantity = returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans().getQuantity()
                        .subtract(returnPurchaseSumQuantity != null ? returnPurchaseSumQuantity : BigDecimal.ZERO);

                returnPurchaseDetailEntity.setLoaddedQuantity(quantity);
                returnPurchaseDetailEntity.setQuantity(quantity);

                returnPurchaseDetailEntity.setDiscount(returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans().getDiscount());
                returnPurchaseDetailEntity.setUnitPrice(returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans().getCost());
                returnPurchaseDetailEntity.setScrewing(returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans().getScrewing());
                InvItem item = new InvItem();
                item.setId(returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans().getItemId().getId());
                item.setName(returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans().getItemId().getName());
                item.setCode(returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans().getItemBarcode());
                item.setUnitId(returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans().getItemId().getUnitId());
                returnPurchaseDetailEntity.setItemId(item);
            }

            updateSummition();
            return true;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "validateItem");
            return null;
        }
    }

    void resetSelected() {
        try {
            invReturnPurchaseDetailEntitySelected.setQuantity(null);
            invReturnPurchaseDetailEntitySelected.setNet(null);
            invReturnPurchaseDetailEntitySelected.setPurchaseInvoiceDetail(null);
            invReturnPurchaseDetailEntitySelected.setItemsBarcodesDetail(null);
//        invReturnPurchaseDetailEntitySelected.getItemId().getUnitId().setName(null);
            invReturnPurchaseDetailEntitySelected.setUnitPrice(null);
            invReturnPurchaseDetailEntitySelected.setDiscount(null);
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "resetSelected");
        }
    }

    public void onRowSelect(SelectEvent selectEvent) {
        try {
            DataTable dataTable = (DataTable) selectEvent.getSource();
            invReturnPurchaseDetailEntitySelected = (InvReturnPurchaseDetailEntity) dataTable.getRowData();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "onRowSelect");
        }
    }

    public Boolean validateItemQuantity(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            if (returnPurchaseDetailEntity.getQuantity() != null && returnPurchaseDetailEntity.getQuantity().compareTo(BigDecimal.ZERO) == 1) {
                if (invReturnSalesEntity.getInvInventoryId() != null) {
                    return compareStoreQuantity(returnPurchaseDetailEntity);
                } else {
                    return errorMessage("يجب إختيار مخزن!");
                }
            } else {
                return errorMessage("يجب ادخال كمية اكبر من الصفر!");
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "validateItemQuantity");
            return null;
        }
    }

    private Boolean compareStoreQuantity(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            Integer itemId;
            BigDecimal quantityStore;
            if (invReturnSalesEntity.getPurchaseInvoice() != null) {
                itemId = returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans().getItemId().getId();
            } else {
                itemId = returnPurchaseDetailEntity.getItemsBarcodesDetailTrans().getItemId();
            }
            if (!itemsMap.isEmpty()) {
                quantityStore = itemsMap.get(itemId);

                if (quantityStore != null && quantityStore.compareTo(returnPurchaseDetailEntity.getQuantity()) != -1) {
                    updateSummition();
                    return true;
                } else {
                    return errorMessage("هذه الكمية غير متاحة بالمخزن");
                }
            } else {
                return errorMessage("هذه الكمية غير متاحة بالمخزن");
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "compareStoreQuantity");
            return null;
        }
    }

    public Boolean validateQuantity(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            if (invReturnSalesEntity.getPurchaseInvoice() != null && invReturnSalesEntity.getPurchaseInvoice().getId() != null) {
                if (returnPurchaseDetailEntity != null && returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans() != null
                        && returnPurchaseDetailEntity.getPurchaseInvoiceDetailTrans().getItemId() != null) {
                    if (invReturnSalesEntity.getInvInventoryId() != null) {
                        if (returnPurchaseDetailEntity.getQuantity() != null
                                && returnPurchaseDetailEntity.getQuantity().compareTo(BigDecimal.ZERO) == 1) {
                            if (returnPurchaseDetailEntity.getLoaddedQuantity() != null
                                    && returnPurchaseDetailEntity.getLoaddedQuantity().compareTo(returnPurchaseDetailEntity.getQuantity()) != -1) {
                                return compareStoreQuantity(returnPurchaseDetailEntity);
                            } else {
                                return errorMessage("يجب ادخال كمية أقل من الموجودة لدى فاتورة المبيعات!");
                            }
                        } else {
                            return errorMessage("يجب ادخال كمية اكبر من الصفر!");
                        }

                    } else {
                        return errorMessage("يجب إختيار مخزن!");
                    }
                } else {
                    return errorMessage("يجب اختيار صنف!");
                }
            } else {
                if (returnPurchaseDetailEntity != null && returnPurchaseDetailEntity.getItemId() != null
                        && returnPurchaseDetailEntity.getQuantity() != null
                        && returnPurchaseDetailEntity.getQuantity().compareTo(BigDecimal.ZERO) == 1) {
                    return compareStoreQuantity(returnPurchaseDetailEntity);
                } else {
                    return errorMessage("يجب ادخال كمية اكبر من الصفر!");
                }
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "validateQuantity");
            return null;
        }
    }

    private void resetInvItem(InvReturnPurchaseDetailEntity returnPurchaseDetailEntity) {
        try {
            returnPurchaseDetailEntity.setQuantity(null);
            returnPurchaseDetailEntity.setUnitPrice(null);
            returnPurchaseDetailEntity.setDiscount(null);
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "resetInvItem");
        }
    }

    public void updateSummition() {
        try {
            BigDecimal quantity, discount, cost, total, valueAfterDiscount;
            totalSum = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;
            totalNetLocal = BigDecimal.ZERO;
            valueAfterDiscount = BigDecimal.ZERO;

            rate = invReturnSalesEntity.getRate() != null && invReturnSalesEntity.getRate().compareTo(BigDecimal.ZERO) == 1
                    ? invReturnSalesEntity.getRate() : BigDecimal.ONE;

            for (InvReturnPurchaseDetailEntity detailEntity : invReturnPurchaseDetailEntityList) {
                quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
                cost = detailEntity.getUnitPrice() != null ? detailEntity.getUnitPrice().multiply(rate) : BigDecimal.ZERO;
                discount = detailEntity.getDiscount() != null && detailEntity.getDiscount().compareTo(BigDecimal.ZERO) == 1
                        && hundred.compareTo(detailEntity.getDiscount()) == 1 ? detailEntity.getDiscount() : BigDecimal.ZERO;

                total = quantity.multiply(cost);
                detailEntity.setTotal(total);

                valueAfterDiscount = (total.subtract((total.multiply(discount).divide(hundred)))).setScale(2, BigDecimal.ROUND_UP);
                detailEntity.setNet(valueAfterDiscount);

                totalSum = (totalSum.add(detailEntity.getTotal() != null ? detailEntity.getTotal() : BigDecimal.ZERO)).setScale(2, BigDecimal.ROUND_UP);
                totalQuatity = (totalQuatity.add(detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO)).setScale(2, BigDecimal.ROUND_UP);
                totalNet = (totalNet.add(detailEntity.getNet() != null ? detailEntity.getNet() : BigDecimal.ZERO)).setScale(2, BigDecimal.ROUND_UP);
            }
            invReturnSalesEntity.setTotal(totalNet);
            calculateTotalNetAfterDiscount();
            updatefooter();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "updateSummition");
        }

    }

    public void updatefooter() {
        try {
            if (invReturnSalesEntity.getTotal() != null) {
                invReturnSalesEntity.setTotalNetAfterDiscount(invReturnSalesEntity.getTotal().subtract(invReturnSalesEntity.getDiscount() == null ? BigDecimal.ZERO : invReturnSalesEntity.getDiscount()));
                invReturnSalesEntity.setTotalWithTaxValue(invReturnSalesEntity.getTotalNetAfterDiscount().subtract(invReturnSalesEntity.getTaxvalue() == null ? BigDecimal.ZERO : invReturnSalesEntity.getTaxvalue()));
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "updatefooter");
        }
    }

    public void calculateTotalNetAfterDiscount() {
        try {
            rate = invReturnSalesEntity.getRate() != null && invReturnSalesEntity.getRate().compareTo(BigDecimal.ZERO) == 1
                    ? invReturnSalesEntity.getRate() : BigDecimal.ONE;

            totalNetLocal = BigDecimal.ZERO;
            if (invReturnPurchaseDetailEntityList != null && !invReturnPurchaseDetailEntityList.isEmpty()) {
                if (totalNet.compareTo(BigDecimal.ZERO) == 1) {
                    totalNetLocal = (totalNet.multiply(rate)).setScale(2, BigDecimal.ROUND_UP);
                } else {
                    totalNetLocal = BigDecimal.ZERO;
                }
            }

        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "calculateTotalNetAfterDiscount");
        }
    }

    public void updateRate() {
        try {
            if (invReturnSalesEntity.getCurrencyId() == null) {
                invReturnSalesEntity.setCurrencyId(currencyList.get(0));
            }
            currencyOperation = currencyOperationService.getRatesByDates(invReturnSalesEntity.getCurrencyId().getId(), invReturnSalesEntity.getDate(), getUserData().getCompany().getId());
            invReturnSalesEntity.setRate(currencyOperation == null ? BigDecimal.ONE : currencyOperation.getRate());

        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "updateRate");
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
            saveError(e, "InvReturnSalesFormMB", "reset");
        }
    }

    public String exportPDF(ActionEvent actionEvent) {
        try {
            print();
            return "";
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "reset");
            return null;
        }
    }

    public String print() {
        try {
            if (invReturnPurchaseId != null && prepareListToPrint(invReturnPurchaseId)) {
                //   exit();
                return "invpurchaseinvoicelist";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "print");
            return null;
        }
    }

    private boolean prepareListToPrint(Integer returnDetailId) {

        if (returnDetailId != null) {
            invReturnPurchaseDetailList = new ArrayList<>();
            invReturnDetailEntityList = new ArrayList<>();
            invReturnPurchaseDetailList = invReturnPurchaseDetailService.getAllReturnPurchaseDetailsByReturnPurchaseId(returnDetailId);

            InvReturnDetailEntity details;
            for (InvReturnPurchaseDetail detailsEntity : invReturnPurchaseDetailList) {
                details = new InvReturnDetailEntity();
                details.setSerial(detailsEntity.getSerial());
                details.setItemCode(detailsEntity.getItemBarcode());
                details.setItemName(detailsEntity.getItemId().getName());
                details.setUnitName(detailsEntity.getItemId().getUnitId().getName());
                details.setScrewing(detailsEntity.getScrewing());
                details.setQuantity(detailsEntity.getQuantity());
                details.setCost(detailsEntity.getUnitPrice());
                details.setNet(detailsEntity.getNet());

                invReturnDetailEntityList.add(details);
            }
            try {
                fillReport(prepareReport(), getUserData().getReportPath() + "ReturnSalesReport.jasper", invReturnDetailEntityList, "pdf");
            } catch (JRException | IOException ex) {
                Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        }
        return false;

    }

    public HashMap prepareReport() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            HashMap hashMap = new HashMap();

            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());
            if (isFileExist(getUserData().getImageReportPath())) {
                hashMap.put("companyImage", getUserData().getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }
            hashMap.put("oresText", "مردودات المبيعات");
            hashMap.put("returnReportText", "فاتورة مردودات مبيعات آجلة");

            hashMap.put("receiptNumberText", "رقم المردود");
            hashMap.put("salesNumberText", "رقم الفاتورة");
            hashMap.put("dateText", userDDs.get("DATE"));
            hashMap.put("remarkText", userDDs.get("REMARK"));

            hashMap.put("serialText", "رقم الفاتورة");
            hashMap.put("itemNumberText", "رقم الصنف");
            hashMap.put("itemNameText", "اسم الصنف");
            hashMap.put("unitText", "الوحدة");
            hashMap.put("screwingText", "الشد");
            hashMap.put("amountText", "الكمية");
            hashMap.put("unitCostText", "التكلفة");
            hashMap.put("supplierName", "العميل");
            hashMap.put("supplierValue", invReturnSalesEntity.getSupplierId().getName());
            hashMap.put("invName", "المخزن");
            hashMap.put("invValue", invReturnSalesEntity.getInvInventoryId().getName());
            hashMap.put("totalText", "الاجمالى");

            hashMap.put("discountText", "الخصم");
            hashMap.put("netText", "الصافي");

            hashMap.put("receiptNumberValue", invReturnSalesEntity.getSerial());
            hashMap.put("serialNumberValue", invReturnSalesEntity.getInvInventoryId().getCode());
            hashMap.put("dateValue", invReturnSalesEntity.getDate());
            hashMap.put("supplierReceiptValue", invReturnSalesEntity.getPurchaseInvoice() != null ? invReturnSalesEntity.getPurchaseInvoice().getSerial() : null);
            hashMap.put("remarkValue", invReturnSalesEntity.getRemark());

            hashMap.put("netValue", totalNet);
            hashMap.put("reportDate", new Date());

            return hashMap;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "prepareListToPrint");
            return null;
        }
    }

    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            if ("pdf".equals(exportType)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                servletOutputStream.flush();
                servletOutputStream.close();
                httpServletResponse.getOutputStream().flush();
                httpServletResponse.getOutputStream().close();
            } else if ("excel".equals(exportType)) {

            } else if ("html".equals(exportType)) {

            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "fillReport");
        }
    }

    private InvReturnPurchase saveReport() {
        try {
            if (invReturnSalesEntity != null) {
                if (invReturnSalesEntity.getInvInventoryId() == null) {
                    errorMessage("يجب أختيار مخزن");
                    return null;
                } else {
                    invReturnSales.setInvInventoryId(invReturnSalesEntity.getInvInventoryId());
                }

                if (invReturnSalesEntity.getId() == null) {
                    invReturnSales.setCreatedBy(getUserData().getUser());
                    invReturnSales.setCreationDate(new Date());
                }
                if (invReturnSalesEntity.getPaymentType() == 1) {
                    invReturnSales.setGlBankId(null);
                } else {
                    if (invReturnSalesEntity.getGlBankId() == null) {
                        errorMessage("يجب أختيار خزينة");
                        return null;
                    } else {
                        invReturnSales.setGlBankId(invReturnSalesEntity.getGlBankId());
                    }
                }
                invReturnSales.setPaymentType(invReturnSalesEntity.getPaymentType());
                invReturnSales.setCostCenter(invReturnSalesEntity.getCostCenter());
                invReturnSales.setCurrencyId(invReturnSalesEntity.getCurrencyId());
                invReturnSales.setSupplierId(invReturnSalesEntity.getSupplierId());
                invReturnSales.setTaxValue(invReturnSalesEntity.getTaxvalue());
                invReturnSales.setDiscountValue(invReturnSalesEntity.getDiscount());
                invReturnSales.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch() : null);
                invReturnSales.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);
                if (invReturnSalesEntity.getDate() != null) {
                    invReturnSales.setDate(invReturnSalesEntity.getDate());
                } else {
                    errorMessage("يجب ادخال تاريخ");
                    return null;
                }
                invReturnSales.setType(Boolean.TRUE);
                invReturnSales.setRate(invReturnSalesEntity.getRate());
                invReturnSales.setSalesPerson(invReturnSalesEntity.getSalesPerson());
                invReturnSales.setRemark(invReturnSalesEntity.getRemark());

                if (invReturnSalesEntity.getPurchaseInvoice() != null) {
                    invReturnSales.setPurchaseInvoiceId(invReturnSalesEntity.getPurchaseInvoice());
                    invReturnSales.setInvoiceDate(invReturnSalesEntity.getPurchaseInvoice().getDate());
                }

                InvReturnPurchaseDetail purchaseDetail;
                InvReturnPurchaseDetail purchaseDetailExist;
                invReturnSalesDetailUpdatedList = new ArrayList<>();
                if (invReturnPurchaseDetailEntityList != null && !invReturnPurchaseDetailEntityList.isEmpty()) {
                    for (InvReturnPurchaseDetailEntity detailsEntity : invReturnPurchaseDetailEntityList) {
                        purchaseDetail = new InvReturnPurchaseDetail();

                        if (invReturnSalesEntity.getPurchaseInvoice() != null) {
                            if (detailsEntity.getPurchaseInvoiceDetailTrans() == null) {
                                errorMessage("يجب ادخال الصنف");
                                return null;
                            } else {
                                purchaseDetail.setItemBarcode(detailsEntity.getItemId().getCode());
                                purchaseDetail.setItemId(detailsEntity.getItemId());
                                purchaseDetail.setInvInvoicePurchaseDetailId(detailsEntity.getPurchaseInvoiceDetailTrans());
                            }
                        } else {

                            if (detailsEntity.getItemsBarcodesDetailTrans() == null) {
                                errorMessage("يجب ادخال الصنف");
                                return null;
                            } else {
                                purchaseDetail.setItemBarcode(detailsEntity.getItemId().getCode());
                                purchaseDetail.setItemId(detailsEntity.getItemId());
                            }
                        }

                        if (detailsEntity.getId() != null) {
                            purchaseDetailExist = new InvReturnPurchaseDetail();
                            purchaseDetail.setId(detailsEntity.getId());

                            purchaseDetailExist = invReturnPurchaseDetailService.findInvReturnPurchaseDetailListById(detailsEntity.getId());
                            purchaseDetail.setCreatedBy(purchaseDetailExist.getCreatedBy());
                            purchaseDetail.setCreationDate(purchaseDetailExist.getCreationDate());

                            purchaseDetail.setModificationDate(new Date());
                            purchaseDetail.setModifiedBy(getUserData().getUser() != null ? getUserData().getUser() : null);

                        } else {
                            purchaseDetail.setCreatedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                            purchaseDetail.setCreationDate(new Date());
                        }

                        purchaseDetail.setBranchId(getUserData().getUserBranch());
                        purchaseDetail.setCompanyId(getUserData().getCompany());
                        purchaseDetail.setDiscount(detailsEntity.getDiscount());
                        purchaseDetail.setDicountType(detailsEntity.getDicountType());
                        purchaseDetail.setUnitPrice(detailsEntity.getUnitPrice());
                        purchaseDetail.setScrewing(detailsEntity.getScrewing());

                        if (validateQuantity(detailsEntity)) {
                            purchaseDetail.setQuantity(detailsEntity.getQuantity());
                        } else {
                            errorMessage("يجب ادخال تفاصيل الفاتورة");
                            return null;
                        }

                        purchaseDetail.setNet(detailsEntity.getNet());

                        invReturnSalesDetailUpdatedList.add(purchaseDetail);
                    }
                } else {
                    errorMessage("يجب ادخال تفاصيل الفاتورة");
                    return null;
                }
                InvReturnPurchaseDetail purchaseDetailDeleted;
                for (InvReturnPurchaseDetailEntity rowsDeleted : getRowsDeleted()) {
                    purchaseDetailDeleted = new InvReturnPurchaseDetail();
                    purchaseDetailDeleted.setId(rowsDeleted.getId());

                    invReturnSalesDetailDeletedList.add(purchaseDetailDeleted);
                }
                InvPurchaseReturnSave invPurchaseReturnSave;
                invPurchaseReturnSave = invReturnPurchaseService.addInvReturnPurchase(invReturnSales, invReturnSalesDetailUpdatedList, invReturnSalesDetailDeletedList);
            //  invReturnSalesEntity.setId(invReturnSales.getId());
                //   invReturnSalesEntity.setSerial(invReturnSales.getSerial());

                resetReturnsales();
                invReturnSales = invPurchaseReturnSave.getInvReturnPurchase();
                invReturnPurchaseId = invReturnSales.getId();
                invReturnSalesDetailList = (List<InvReturnPurchaseDetail>) invPurchaseReturnSave.getInvReturnPurchaseDetailList();

                invReturnSalesEntity = mapReturnPurchaseToReturnPurchaseEntity(invPurchaseReturnSave.getInvReturnPurchase());

                return invReturnSales;
            }

            return null;
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "saveReport");
            return null;
        }
    }

    @Override
    public String save() {
        try {
            if (saveReport() == null) {
                return "";
            } else {
                return exit();
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "save");
            return null;
        }
    }

    public String saveAndContinue() {
        try {
            if (saveReport() == null) {
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "saveAndContinue");
            return null;
        }
    }

    public void resetReturnsales() {
        try {
            invReturnSales = new InvReturnPurchase();
            invReturnSalesDetailUpdatedList = new ArrayList<>();
            invReturnSalesDetailDeletedList = new ArrayList<>();
            totalQuatity = BigDecimal.ZERO;
            totalSum = BigDecimal.ZERO;
            totalLocal = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;
            netAll = BigDecimal.ZERO;
            totalNetLocal = BigDecimal.ZERO;

        //     showMessageDetails = Boolean.FALSE;
            //    showMessageGeneral = Boolean.FALSE;
            invReturnPurchaseDetailList = new ArrayList<>();
            invReturnPurchaseDetailEntityList = new ArrayList<>();
        // invReturnPurchaseDetailEntity = new InvReturnPurchaseDetailEntity();

        //  invReturnPurchaseDetailEntitySelected = new InvReturnPurchaseDetailEntity();
            //   invPurchaseInvoiceDetailsEntitySelectedList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "resetReturnsales");

        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        try {
            exit("../invreturnsales/invreturnsaleslist.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "InvReturnSalesFormMB", "exit");
            return null;
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

    public List<InvPurchaseInvoice> getInvSalesInvoiceList() {
        return invSalesInvoiceList;
    }

    public void setInvSalesInvoiceList(List<InvPurchaseInvoice> invSalesInvoiceList) {
        this.invSalesInvoiceList = invSalesInvoiceList;
    }

    public List<InvPurchaseOrder> getInvPurchaseOrderList() {
        return invPurchaseOrderList;
    }

    public void setInvPurchaseOrderList(List<InvPurchaseOrder> invPurchaseOrderList) {
        this.invPurchaseOrderList = invPurchaseOrderList;
    }

    public Integer getInvReturnPurchaseId() {
        return invReturnPurchaseId;
    }

    public void setInvReturnPurchaseId(Integer invReturnPurchaseId) {
        this.invReturnPurchaseId = invReturnPurchaseId;
    }

    public List<InvOrganizationSite> getInvOrganizationSiteList() {
        return invOrganizationSiteList;
    }

    public void setInvOrganizationSiteList(List<InvOrganizationSite> invOrganizationSiteList) {
        this.invOrganizationSiteList = invOrganizationSiteList;
    }

    public List<CostCenter> getCostCenterList() {
        return costCenterList;
    }

    public void setCostCenterList(List<CostCenter> costCenterList) {
        this.costCenterList = costCenterList;
    }

    public List<GlAdminUnit> getAdminUnitList() {
        return adminUnitList;
    }

    public void setAdminUnitList(List<GlAdminUnit> adminUnitList) {
        this.adminUnitList = adminUnitList;
    }

    public List<InvItem> getInvItemList() {
        return invItemList;
    }

    public void setInvItemList(List<InvItem> invItemList) {
        this.invItemList = invItemList;
    }

    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    public InvReturnPurchaseEntity getInvReturnSalesEntity() {
        return invReturnSalesEntity;
    }

    public void setInvReturnSalesEntity(InvReturnPurchaseEntity invReturnSalesEntity) {
        this.invReturnSalesEntity = invReturnSalesEntity;
    }

    public InvReturnPurchaseDetailEntity getInvReturnPurchaseDetailEntity() {
        return invReturnPurchaseDetailEntity;
    }

    public void setInvReturnPurchaseDetailEntity(InvReturnPurchaseDetailEntity invReturnPurchaseDetailEntity) {
        this.invReturnPurchaseDetailEntity = invReturnPurchaseDetailEntity;
    }

    public List<InvReturnPurchaseDetailEntity> getInvReturnPurchaseDetailEntityList() {
        return invReturnPurchaseDetailEntityList;
    }

    public void setInvReturnPurchaseDetailEntityList(List<InvReturnPurchaseDetailEntity> invReturnPurchaseDetailEntityList) {
        this.invReturnPurchaseDetailEntityList = invReturnPurchaseDetailEntityList;
    }

    public BigDecimal getTotalQuatity() {
        return totalQuatity;
    }

    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
    }

    public List<InvReturnPurchaseDetailEntity> getRowsDeleted() {
        return rowsDeleted;
    }

    public void setRowsDeleted(List<InvReturnPurchaseDetailEntity> rowsDeleted) {
        this.rowsDeleted = rowsDeleted;
    }

    public List<InvPurchaseInvoiceDetail> getInvPurchaseInvoiceDetailsLoadedList() {
        return invPurchaseInvoiceDetailsLoadedList;
    }

    public void setInvPurchaseInvoiceDetailsLoadedList(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsLoadedList) {
        this.invPurchaseInvoiceDetailsLoadedList = invPurchaseInvoiceDetailsLoadedList;
    }

    public List<InvPurchaseInvoiceDetail> getInvPurchaseInvoiceDetailsDeletedList() {
        return invPurchaseInvoiceDetailsDeletedList;
    }

    public void setInvPurchaseInvoiceDetailsDeletedList(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsDeletedList) {
        this.invPurchaseInvoiceDetailsDeletedList = invPurchaseInvoiceDetailsDeletedList;
    }

    public CurrencyOperation getCurrencyOperation() {
        return currencyOperation;
    }

    public void setCurrencyOperation(CurrencyOperation currencyOperation) {
        this.currencyOperation = currencyOperation;
    }

    public BigDecimal getTotalLocal() {
        return totalLocal;
    }

    public void setTotalLocal(BigDecimal totalLocal) {
        this.totalLocal = totalLocal;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

    public List<InvPurchaseInvoiceDetailsEntity> getInvPurchaseInvoiceDetailsEntitySelectedList() {
        return invPurchaseInvoiceDetailsEntitySelectedList;
    }

    public void setInvPurchaseInvoiceDetailsEntitySelectedList(List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntitySelectedList) {
        this.invPurchaseInvoiceDetailsEntitySelectedList = invPurchaseInvoiceDetailsEntitySelectedList;
    }

    public BigDecimal getTotalNet() {
        return totalNet;
    }

    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    public SymbolConverter getUnitConverter() {
        return unitConverter;
    }

    public void setUnitConverter(SymbolConverter unitConverter) {
        this.unitConverter = unitConverter;
    }

    public ItemConverter getInvItemConverter() {
        return invItemConverter;
    }

    public void setInvItemConverter(ItemConverter invItemConverter) {
        this.invItemConverter = invItemConverter;
    }

    public InvOrganizationSiteConverter getSupplierConvertor() {
        return supplierConvertor;
    }

    public void setSupplierConvertor(InvOrganizationSiteConverter supplierConvertor) {
        this.supplierConvertor = supplierConvertor;
    }

    public CurrencyConverter getCurrencyConverter() {
        return currencyConverter;
    }

    public void setCurrencyConverter(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    public CostCenterConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    public void setCostCenterConverter(CostCenterConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }

    public GlAdminUnitConverter getAdminUnitConverter() {
        return adminUnitConverter;
    }

    public void setAdminUnitConverter(GlAdminUnitConverter adminUnitConverter) {
        this.adminUnitConverter = adminUnitConverter;
    }

    public BigDecimal getNetAll() {
        return netAll;
    }

    public void setNetAll(BigDecimal netAll) {
        this.netAll = netAll;
    }

    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }

    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    public List<InvPurchaseOrder> getInvPurchaseOrderSelectedList() {
        return invPurchaseOrderSelectedList;
    }

    public void setInvPurchaseOrderSelectedList(List<InvPurchaseOrder> invPurchaseOrderSelectedList) {
        this.invPurchaseOrderSelectedList = invPurchaseOrderSelectedList;
    }

    public List<InvAddingorder> getInvAddingOrderList() {
        return invAddingOrderList;
    }

    public void setInvAddingOrderList(List<InvAddingorder> invAddingOrderList) {
        this.invAddingOrderList = invAddingOrderList;
    }

    public List<InvAddingorder> getInvAddingOrderSelectedList() {
        return invAddingOrderSelectedList;
    }

    public void setInvAddingOrderSelectedList(List<InvAddingorder> invAddingOrderSelectedList) {
        this.invAddingOrderSelectedList = invAddingOrderSelectedList;
    }

    public BigDecimal getTotalNetLocal() {
        return totalNetLocal;
    }

    public void setTotalNetLocal(BigDecimal totalNetLocal) {
        this.totalNetLocal = totalNetLocal;
    }

    public List<ItemsBarcodesDetailsView> getItemsBarcodesDetailsViewList() {
        return itemsBarcodesDetailsViewList;
    }

    public void setItemsBarcodesDetailsViewList(List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList) {
        this.itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewList;
    }

    public ItemsBarcodesDetailsViewConverter getItemsBarcodesDetailsViewConverter() {
        return itemsBarcodesDetailsViewConverter;
    }

    public void setItemsBarcodesDetailsViewConverter(ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter) {
        this.itemsBarcodesDetailsViewConverter = itemsBarcodesDetailsViewConverter;
    }

    public GlBankConverter getGlBankConverter() {
        return glBankConverter;
    }

    public void setGlBankConverter(GlBankConverter glBankConverter) {
        this.glBankConverter = glBankConverter;
    }

    public List<GlBank> getGlBankList() {
        return glBankList;
    }

    public void setGlBankList(List<GlBank> glBankList) {
        this.glBankList = glBankList;
    }

    public InvPurchaseInvoiceConverter getInvSalesInvoiceConverter() {
        return invSalesInvoiceConverter;
    }

    public void setInvSalesInvoiceConverter(InvPurchaseInvoiceConverter invSalesInvoiceConverter) {
        this.invSalesInvoiceConverter = invSalesInvoiceConverter;
    }

    public InvReturnPurchase getInvReturnSales() {
        return invReturnSales;
    }

    public void setInvReturnSales(InvReturnPurchase invReturnSales) {
        this.invReturnSales = invReturnSales;
    }

    public InvReturnPurchaseDetailEntity getInvReturnPurchaseDetailEntitySelected() {
        return invReturnPurchaseDetailEntitySelected;
    }

    public void setInvReturnPurchaseDetailEntitySelected(InvReturnPurchaseDetailEntity invReturnPurchaseDetailEntitySelected) {
        this.invReturnPurchaseDetailEntitySelected = invReturnPurchaseDetailEntitySelected;
    }

    public List<InvReturnPurchaseDetail> getInvReturnSalesDetailList() {
        return invReturnSalesDetailList;
    }

    public void setInvReturnSalesDetailList(List<InvReturnPurchaseDetail> invReturnSalesDetailList) {
        this.invReturnSalesDetailList = invReturnSalesDetailList;
    }

    public List<InvReturnPurchaseDetail> getInvReturnSalesDetailUpdatedList() {
        return invReturnSalesDetailUpdatedList;
    }

    public void setInvReturnSalesDetailUpdatedList(List<InvReturnPurchaseDetail> invReturnSalesDetailUpdatedList) {
        this.invReturnSalesDetailUpdatedList = invReturnSalesDetailUpdatedList;
    }

    public List<InvReturnPurchaseDetail> getInvReturnSalesDetailDeletedList() {
        return invReturnSalesDetailDeletedList;
    }

    public void setInvReturnSalesDetailDeletedList(List<InvReturnPurchaseDetail> invReturnSalesDetailDeletedList) {
        this.invReturnSalesDetailDeletedList = invReturnSalesDetailDeletedList;
    }

    public InvPurchaseInvoiceDetailConverter getInvSalesInvoiceDetailConverter() {
        return invSalesInvoiceDetailConverter;
    }

    public void setInvSalesInvoiceDetailConverter(InvPurchaseInvoiceDetailConverter invSalesInvoiceDetailConverter) {
        this.invSalesInvoiceDetailConverter = invSalesInvoiceDetailConverter;
    }

    public InvDelegatorConvertor getSalesConverter() {
        return salesConverter;
    }

    public void setSalesConverter(InvDelegatorConvertor salesConverter) {
        this.salesConverter = salesConverter;
    }

    public List<InventoryDelegator> getSalesPersonsList() {
        return salesPersonsList;
    }

    public void setSalesPersonsList(List<InventoryDelegator> salesPersonsList) {
        this.salesPersonsList = salesPersonsList;
    }

    public List<InvReturnPurchaseDetail> getInvReturnPurchaseDetailList() {
        return invReturnPurchaseDetailList;
    }

    public void setInvReturnPurchaseDetailList(List<InvReturnPurchaseDetail> invReturnPurchaseDetailList) {
        this.invReturnPurchaseDetailList = invReturnPurchaseDetailList;
    }

    public List<InvReturnDetailEntity> getInvReturnDetailEntityList() {
        return invReturnDetailEntityList;
    }

    public void setInvReturnDetailEntityList(List<InvReturnDetailEntity> invReturnDetailEntityList) {
        this.invReturnDetailEntityList = invReturnDetailEntityList;
    }

    /**
     * @return the viewDuDate
     */
    public Boolean getViewDuDate() {
        return viewDuDate;
    }

    /**
     * @param viewDuDate the viewDuDate to set
     */
    public void setViewDuDate(Boolean viewDuDate) {
        this.viewDuDate = viewDuDate;
    }
}

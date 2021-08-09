/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invPurchaseInvoice;

import com.isag.businessservice.CostCenterService;
import com.isag.businessservice.CurrencyOperationService;
import com.isag.businessservice.CurrencyService;
import com.isag.businessservice.GlAdminUnitService;
import com.isag.businessservice.InvAddingOrderDetailsService;
import com.isag.businessservice.InvAddingOrderService;
import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.InvPurchaseOrderDetailsService;
import com.isag.businessservice.InvPurchaseOrderService;
import com.isag.businessservice.InventorySetupService;
import com.isag.businessservice.IsagUserBankService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.SymbolService;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.converter.CostCenterConverter;
import com.isag.converter.CurrencyConverter;
import com.isag.converter.GlAdminUnitConverter;
import com.isag.converter.GlBankConverter;
import com.isag.converter.InvDelegatorConvertor;
import com.isag.converter.InvInventoryConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.ItemConverter;
import com.isag.converter.ItemsBarcodesDetailsViewConverter;
import com.isag.converter.SymbolConverter;
import com.isag.entity.CostCenter;
import com.isag.entity.Currency;
import com.isag.entity.CurrencyOperation;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.GlBank;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.InventorySetup;
import com.isag.entity.Symbol;
import com.isag.entiy.InvPurchaseInvoiceDetailsEntity;
import com.isag.entiy.InvPurchaseInvoiceEntity;
import com.isag.entiy.InvReturnPurchaseDetailEntity;
import com.isag.entiy.InvReturnPurchaseEntity;
import com.isag.entiy.InvTransferDetailsEntity;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.views.ItemsBarcodesDetailsView;
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
import javax.faces.application.FacesMessage;
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
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import tafqeet.Tafqeet;

/**
 *
 * @author WIN7
 */
@Named(value = "invPurchaseInvoiceFormMB")
@ViewScoped
public class InvPurchaseInvoiceFormMB extends BaseFormBean {

    private Integer branchId;
    private Integer companyId;
    private Integer invPurchaseInvoiceId;

    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private BigDecimal totalWeight = BigDecimal.ZERO;
    private BigDecimal totalSum = BigDecimal.ZERO;
    private BigDecimal totalLocal = BigDecimal.ZERO;
    private BigDecimal totalNet = BigDecimal.ZERO;
    private BigDecimal netAll = BigDecimal.ZERO;
    private BigDecimal totalValueAfterDiscount = BigDecimal.ZERO;
    private BigDecimal taxvalue = BigDecimal.ZERO;
    private BigDecimal totalWithTaxValue = BigDecimal.ZERO;
    private BigDecimal subtractWeight = BigDecimal.ZERO;
    private BigDecimal multiply = BigDecimal.ZERO;
    private BigDecimal finalNet = BigDecimal.ZERO;
    private BigDecimal totalNetAfterDiscount = BigDecimal.ZERO;
    BigDecimal hundred = new BigDecimal(100);

    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private Boolean showDialogMessage = Boolean.FALSE;
    private boolean purchaseOrderTabView = Boolean.FALSE;
    private boolean addingOrderTabView = Boolean.FALSE;

    boolean toggleSelectedPurchaseOrder = Boolean.FALSE;
    boolean toggleSelectedAddingOrder = Boolean.FALSE;

    private Boolean viewDuDate = Boolean.FALSE;
    private Boolean viewTab = Boolean.FALSE;

    private CurrencyOperation currencyOperation;
    private List<InvOrganizationSite> customersList;
    // Converters
    private CostCenterConverter costCenterConverter;
    private SymbolConverter unitConverter;
    private ItemConverter itemConverter;
    private CurrencyConverter currencyConverter;
    private GlAdminUnitConverter adminUnitConverter;
    private InvInventoryConverter invInventoryConverter;
    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;
    private InvDelegatorConvertor purchasePersonConvertor;
    private GlBankConverter glBankConverter;

    // DB Entities
    private InvPurchaseInvoice invPurchaseInvoice;
    private InvPurchaseReturnSave invPurchaseReturnSave;
    private InvPurchaseInvoice invPurchaseInvoiceExist;
    private List<InvPurchaseInvoice> invPurchaseInvoiceList;
    private InvPurchaseInvoiceDetail invPurchaseInvoiceDetail;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsList;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsUpdatedList;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsDeletedList;
    private List<InvInventory> invInventoryList;

    // Interface Entities
    private InvPurchaseInvoiceEntity invPurchaseInvoiceEntity;
    private List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList;
    private InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntity;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityList;
    private List<InvTransferDetailsEntity> invTransferDetailsEntityList;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityReport;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntitySelectedList;
    private InvReturnPurchaseEntity invReturnPurchaseEntity;
    private List<InvReturnPurchaseEntity> invReturnPurchaseEntityList;
    private InvReturnPurchaseDetailEntity invReturnPurchaseDetailEntity;
    private List<InvReturnPurchaseDetailEntity> invReturnPurchaseDetailEntityList;
    private InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntitySelected;
    private InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsUpdatedSelected;

//    Map<Integer, List<Symbol>> unitsMap;
    // Lists
    private List<InvOrganizationSite> invOrganizationSiteList;
    private InvOrganizationSiteConverter supplierConvertor;
    private List<CostCenter> costCenterList;
    private List<GlAdminUnit> adminUnitList;
    private List<InvItem> invItemList;
    private List<Symbol> unitList;
    private List<Currency> currencyList;
    private List<InvPurchaseOrder> invPurchaseOrderList;
    private List<InvAddingorder> invAddingOrderList;

    private List<InvAddingorder> invAddingOrderSelectedList;

    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;
    private List<InventoryDelegator> purchasePersonList;
    private List<GlBank> glBankList;

    private List<InvPurchaseInvoiceDetailsEntity> rowsDeleted;

    private List<InvPurchaseOrder> invPurchaseOrderSelectedList;

    private List<Integer> updatedPurchaseOrAddingOrderList;
    private Integer isPurchaseOrder;

    private StringBuilder headIdList = new StringBuilder();

    private InventorySetup invSetup;

    private String headRemarks;

//    private Map<String, InvItem> mapItems = new HashMap<String, InvItem>();
    Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<String, ItemsBarcodesDetailsView>();

    private ExternalContext context;
    // EJBs
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
    InvDelegatorService ineventoryDelegatorService;
    @EJB
    private GlAdminUnitService glAdminUnitService;
//    @EJB
//    private InvItemService invItemService;
    @EJB
    private SymbolService symbolService;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private InvAddingOrderService invAddingOrderService;

    @EJB
    private InvAddingOrderDetailsService invAddingOrderDetailsService;
    @EJB
    private InvPurchaseOrderDetailsService invPurchaseOrderDetailsService;
    @EJB
    InventorySetupService inventorySetupService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    IsagUserBankService isagUserBankService;

    @Override
    @PostConstruct
    public void init() {
        try {
            invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoiceList = new ArrayList<>();
            invPurchaseInvoiceDetail = new InvPurchaseInvoiceDetail();
            invPurchaseInvoiceDetailsList = new ArrayList<>();
            invPurchaseInvoiceEntity = new InvPurchaseInvoiceEntity();
            invPurchaseInvoiceEntityList = new ArrayList<>();
            invReturnPurchaseEntity = new InvReturnPurchaseEntity();
            invReturnPurchaseDetailEntity = new InvReturnPurchaseDetailEntity();
            invPurchaseInvoiceDetailsEntity = new InvPurchaseInvoiceDetailsEntity();
            invPurchaseInvoiceDetailsEntityList = new ArrayList<>();
            invPurchaseInvoiceDetailsEntitySelected = new InvPurchaseInvoiceDetailsEntity();
            invPurchaseInvoiceDetailsUpdatedSelected = new InvPurchaseInvoiceDetailsEntity();
            invPurchaseInvoiceDetailsEntitySelectedList = new ArrayList<>();
            invReturnPurchaseDetailEntityList = new ArrayList<>();
            invReturnPurchaseEntityList = new ArrayList<>();
            rowsDeleted = new ArrayList<>();
            invPurchaseInvoiceDetailsUpdatedList = new ArrayList<>();
            invPurchaseInvoiceDetailsDeletedList = new ArrayList<>();

            invOrganizationSiteList = new ArrayList<>();
            costCenterList = new ArrayList<>();
            adminUnitList = new ArrayList<>();
            invItemList = new ArrayList<>();
            unitList = new ArrayList<>();
            currencyList = new ArrayList<>();
            invPurchaseOrderList = new ArrayList<>();
            invAddingOrderList = new ArrayList<>();

            invInventoryList = new ArrayList<>();
            setInvSetup(new InventorySetup());
            itemsBarcodesDetailsViewList = new ArrayList<>();
            purchasePersonList = new ArrayList<>();

//        unitsMap = new HashMap<>();
            load();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    public void reset() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            context.getSessionMap().replace("ScreenMode", "Add");
            setScreenMode("Add");

            init();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "reset");
        }
    }

    @Override
    public void load() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            setBranchId(getUserData().getUserBranch().getId());
            setCompanyId(getUserData().getCompany().getId());

            fillLists();
            if (getInvSetup() != null) {
                viewTab = getInvSetup().getSellBuy();
                if (invSetup.getSellBuy() == false) {
                    addingOrderTabView = true;
                    purchaseOrderTabView = false;
                } else {
                    addingOrderTabView = false;
                    purchaseOrderTabView = true;
                }
            }

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetInvPurchaseInvoiceForm();
                // showMessageDetails = showMessageGeneral = showDialogMessage = false;
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {

                setInvPurchaseInvoiceId((Integer) context.getSessionMap().get("invPurchaseInvoiceIdSeclected"));
                invPurchaseInvoice = invPurchaseInvoiceService.findInvPurchaseInvoiceById(getInvPurchaseInvoiceId());

                setInvPurchaseInvoiceExist(invPurchaseInvoice);

                invPurchaseInvoiceDetailsList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(getInvPurchaseInvoiceId());
                invPurchaseInvoiceEntity = mapInvPurchaseInvoiceToInvPurchaseInvoiceEntity(invPurchaseInvoice);
            }

            // addingOrderTabView = purchaseOrderTabView = true;
            showDuDate();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    private void fillLists() {
        try {
            invOrganizationSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(branchId, true, 1); // المورد واحد
            costCenterList = costCenterService.getActiveSubCostCentersByBranch(branchId); // مراكز التكلفة
            adminUnitList = glAdminUnitService.getAllSubAdminUnitByBranchIdActive(branchId); // الوحدات الادارية

            purchasePersonList = ineventoryDelegatorService.getPurchaseByBranch(branchId); // مندوبين المشتريات
            purchasePersonConvertor = new InvDelegatorConvertor(purchasePersonList);

//        invItemList = invItemService.getInvItemByBranchId(branchId); // الاصناف
            itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViewList);

            for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
                itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
            }
            unitList = symbolService.getUnitsByCompanyId(companyId); // الوحدات: جرام, طن
            currencyList = currencyService.getAllCurrenciesWithLocalCurrencyHavingRatesByCompanyId(companyId); // العملات
            invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId); // ودا بردوا

            supplierConvertor = new InvOrganizationSiteConverter(invOrganizationSiteList);
            costCenterConverter = new CostCenterConverter(costCenterList);
            adminUnitConverter = new GlAdminUnitConverter(adminUnitList);
//        itemConverter = new ItemConverter(invItemList);
            unitConverter = new SymbolConverter(unitList);
            currencyConverter = new CurrencyConverter(currencyList);
            invInventoryConverter = new InvInventoryConverter(invInventoryList);
            setInvSetup(inventorySetupService.getInventoryByBranchId(branchId));

            glBankList = isagUserBankService.getAllGlBankForUserByTypeAndBranchIdPer(getUserData().getUser().getId(), branchId, 0); //  ودا بردوا
            glBankConverter = new GlBankConverter(glBankList);

//        unitsMap = invBarcodeSevice.getAllUnitsForAllInvItemsByBranch(branchId);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "fillLists");
        }
    }

    private void resetInvPurchaseInvoiceForm() {
        try {
            invPurchaseInvoiceEntity = new InvPurchaseInvoiceEntity();
            invPurchaseInvoiceEntity.setDate(new Date());
            invPurchaseInvoiceEntity.setCurrencyDate(new Date());
            invPurchaseInvoiceEntity.setDiscountType(0);
            invPurchaseInvoiceEntity.setPaymentType(0);
            invPurchaseInvoiceEntity.setHeadDiscount(BigDecimal.ZERO);
            if (invInventoryList != null && !invInventoryList.isEmpty()) {
                InvInventory invInventorySelection = (InvInventory) getContext().getSessionMap().get("defaultInventory");
                if (invInventorySelection != null) {
                    invPurchaseInvoiceEntity.setInvInventory(invInventorySelection);
                } else {
                    invPurchaseInvoiceEntity.setInvInventory(invInventoryList.get(0));
                }
            } else {
                errorMessage("لا توجد مخازن");
            }
            if (glBankList != null && !glBankList.isEmpty()) {
                invPurchaseInvoiceEntity.setGlBank(glBankList.get(0));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد خزائن"));
                showMessageGeneral = true;
            }

            if (invOrganizationSiteList != null && !invOrganizationSiteList.isEmpty()) {
                invPurchaseInvoiceEntity.setSupplierId(invOrganizationSiteList.get(0));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد موردين"));
                showMessageGeneral = true;
            }

//        invPurchaseInvoiceEntity.setAmountInv(BigDecimal.ZERO);
//        invPurchaseInvoiceEntity.setAmountInvSupplier(BigDecimal.ZERO);
            updateRate();

            invPurchaseInvoiceEntityList = new ArrayList<>();
            invPurchaseInvoiceDetailsEntityList = new ArrayList<>();
            invReturnPurchaseDetailEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "resetInvPurchaseInvoiceForm");
        }
    }

    private InvPurchaseInvoiceEntity mapInvPurchaseInvoiceToInvPurchaseInvoiceEntity(InvPurchaseInvoice invPurchaseInvoice) {
        try {

            invPurchaseInvoiceEntity = new InvPurchaseInvoiceEntity();

            invPurchaseInvoiceEntity.setId(invPurchaseInvoice.getId() != null ? invPurchaseInvoice.getId() : null);
            invPurchaseInvoiceEntity.setSerial(invPurchaseInvoice.getSerial());
            invPurchaseInvoiceEntity.setAdminUnitId(invPurchaseInvoice.getAdminUnitId() != null ? invPurchaseInvoice.getAdminUnitId() : null);
            invPurchaseInvoiceEntity.setBranchId(invPurchaseInvoice.getBranchId() != null ? invPurchaseInvoice.getBranchId().getId() : null);
            invPurchaseInvoiceEntity.setCostCenterId(invPurchaseInvoice.getCostCenterId() != null ? invPurchaseInvoice.getCostCenterId() : null);
            invPurchaseInvoiceEntity.setCurrencyId(invPurchaseInvoice.getCurrencyId() != null ? invPurchaseInvoice.getCurrencyId() : null);
            invPurchaseInvoiceEntity.setDate(invPurchaseInvoice.getDate() != null ? invPurchaseInvoice.getDate() : new Date());

            invPurchaseInvoiceEntity.setAccountId(invPurchaseInvoice.getAccountId() != null ? invPurchaseInvoice.getAccountId().getId() : null);
            headRemarks = invPurchaseInvoice.getRemarks();
            invPurchaseInvoiceEntity.setRemarks(invPurchaseInvoice.getRemarks() != null ? invPurchaseInvoice.getRemarks() : null);
            invPurchaseInvoiceEntity.setHeadDiscount(invPurchaseInvoice.getDiscount() != null ? invPurchaseInvoice.getDiscount() : BigDecimal.ZERO);
            invPurchaseInvoiceEntity.setDiscountType(invPurchaseInvoice.getDiscountType() != null ? invPurchaseInvoice.getDiscountType() : 0);
            invPurchaseInvoiceEntity.setDueDate(invPurchaseInvoice.getDueDate() != null ? invPurchaseInvoice.getDueDate() : null);
            invPurchaseInvoiceEntity.setPaymentType(invPurchaseInvoice.getPaymentType() > 0 ? invPurchaseInvoice.getPaymentType() : 0);
            invPurchaseInvoiceEntity.setPostFlag(invPurchaseInvoice.getPostFlag() > 0 ? invPurchaseInvoice.getPostFlag() : 0);
            invPurchaseInvoiceEntity.setSupplierInvoiceDate(invPurchaseInvoice.getSupplierInvoiceDate());
            invPurchaseInvoiceEntity.setRate(invPurchaseInvoice.getRate() != null ? invPurchaseInvoice.getRate() : BigDecimal.ONE);
            invPurchaseInvoiceEntity.setSupplierId(invPurchaseInvoice.getOrganizationSiteId() != null ? invPurchaseInvoice.getOrganizationSiteId() : null);
            invPurchaseInvoiceEntity.setSupplierInvoiceNumber(invPurchaseInvoice.getSupplierInvoiceNumber());

            invPurchaseInvoiceEntity.setInvInventory(invPurchaseInvoice.getInvInventoryId() != null ? invPurchaseInvoice.getInvInventoryId() : null);

            invPurchaseInvoiceEntity.setExtraCost(invPurchaseInvoice.getExtraCost() != null ? invPurchaseInvoice.getExtraCost() : null);
            invPurchaseInvoiceEntity.setTaxdiscflag(invPurchaseInvoice.getTaxdiscflag());
            invPurchaseInvoiceEntity.setTaxdiscvalue(invPurchaseInvoice.getTaxdiscvalue());
            invPurchaseInvoiceEntity.setPurchasePerson(invPurchaseInvoice.getInvDelegatorId());
            invPurchaseInvoiceEntity.setGlBank(invPurchaseInvoice.getGlBankId());
            invPurchaseInvoiceEntity.setTaxFlag(invPurchaseInvoice.getTaxflag());
            invPurchaseInvoiceEntity.setActualWeight(invPurchaseInvoice.getActualWeight() != null ? invPurchaseInvoice.getActualWeight() : BigDecimal.ZERO);
            invPurchaseInvoiceEntity.setTotalActualWeight(invPurchaseInvoice.getTotalActualWeight() != null ? invPurchaseInvoice.getTotalActualWeight() : BigDecimal.ZERO);
            invPurchaseInvoiceEntity.setPriceKilo(invPurchaseInvoice.getPriceKilo() != null ? invPurchaseInvoice.getPriceKilo() : BigDecimal.ZERO);
//        invPurchaseInvoice.getInvPurchaseInvoiceDetailCollection();
            updateTaxDiscValue();
            BigDecimal quantity, weight, discount, cost, total, percintage, subtractVaue, net;

            totalNet = BigDecimal.ZERO;
            totalSum = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            setTotalWeight(BigDecimal.ZERO);
            multiply = BigDecimal.ZERO;
            finalNet = BigDecimal.ZERO;
            totalValueAfterDiscount = totalWithTaxValue;
            taxvalue = totalWithTaxValue;
            for (InvPurchaseInvoiceDetail purchaseInvoiceDetail : invPurchaseInvoiceDetailsList) {

                totalLocal = BigDecimal.ZERO;
                invPurchaseInvoiceDetailsEntity = new InvPurchaseInvoiceDetailsEntity();

                quantity = purchaseInvoiceDetail.getQuantity() != null ? purchaseInvoiceDetail.getQuantity().setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO;
                cost = purchaseInvoiceDetail.getCost() != null ? purchaseInvoiceDetail.getCost().setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO;
                discount = purchaseInvoiceDetail.getDiscount() != null ? purchaseInvoiceDetail.getDiscount().setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO;

                invPurchaseInvoiceDetailsEntity.setBranchId(purchaseInvoiceDetail.getBranchId() != null ? purchaseInvoiceDetail.getBranchId().getId() : null);
                invPurchaseInvoiceDetailsEntity.setCost(cost);
                invPurchaseInvoiceDetailsEntity.setFirstCost(cost);
                invPurchaseInvoiceDetailsEntity.setDiscount(discount);
                invPurchaseInvoiceDetailsEntity.setQuantity(quantity);

                invPurchaseInvoiceDetailsEntity.setDiscountType(purchaseInvoiceDetail.getDiscountType());
                invPurchaseInvoiceDetailsEntity.setId(purchaseInvoiceDetail.getId());
                invPurchaseInvoiceDetailsEntity.setSerial(purchaseInvoiceDetail.getSerial());
                invPurchaseInvoiceDetailsEntity.setInvPurchaseInvoiceId(purchaseInvoiceDetail.getInvPurchaseInvoiceId() != null ? purchaseInvoiceDetail.getInvPurchaseInvoiceId().getId() : null);

//            invPurchaseInvoiceDetailsEntity.setItemId(purchaseInvoiceDetail.getItemId() != null ? purchaseInvoiceDetail.getItemId().getId() : null);
//            invPurchaseInvoiceDetailsEntity.setInvItem(purchaseInvoiceDetail.getItemId() != null ? purchaseInvoiceDetail.getItemId() : null);
                if (purchaseInvoiceDetail.getItemBarcode() != null && !"".equals(purchaseInvoiceDetail.getItemBarcode())) {
                    invPurchaseInvoiceDetailsEntity.setItemsBarcodesDetail(itemsBarcodeMap.get(purchaseInvoiceDetail.getItemBarcode()));
                    invPurchaseInvoiceDetailsEntity.setItemsBarcodesDetailTrans(invPurchaseInvoiceDetailsEntity.getItemsBarcodesDetail());
                    invPurchaseInvoiceDetailsEntity.setScrewing(invPurchaseInvoiceDetailsEntity.getItemsBarcodesDetail().getScrewing());
                    invPurchaseInvoiceDetailsEntity.setPaintColor(invPurchaseInvoiceDetailsEntity.getPaintColor());
                    invPurchaseInvoiceDetailsEntity.setLength(invPurchaseInvoiceDetailsEntity.getLength());
                }
                invPurchaseInvoiceDetailsEntity.setWeight(purchaseInvoiceDetail.getWeight() != null ? purchaseInvoiceDetail.getWeight() : BigDecimal.ZERO);
                invPurchaseInvoiceDetailsEntity.setUnitId(purchaseInvoiceDetail.getUnitId() != null ? purchaseInvoiceDetail.getUnitId().getId() : null);
                invPurchaseInvoiceDetailsEntity.setUnit(purchaseInvoiceDetail.getUnitId() != null ? purchaseInvoiceDetail.getUnitId() : null);
                invPurchaseInvoiceDetailsEntity.setMarkEdit(Boolean.FALSE);
                invPurchaseInvoiceDetailsEntity.setSelectedId(purchaseInvoiceDetail.getSelectedId() != null ? purchaseInvoiceDetail.getSelectedId() : null);
                invPurchaseInvoiceDetailsEntity.setTransferFrom(purchaseInvoiceDetail.getTransferFrom() != null ? purchaseInvoiceDetail.getTransferFrom() : 0);
                invPurchaseInvoiceDetailsEntity.setCostUpdated(false);
                invPurchaseInvoiceDetailsEntity.setExtraCost(purchaseInvoiceDetail.getExtraCost());
                if (purchaseInvoiceDetail.getWeight() == null || purchaseInvoiceDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                    total = (quantity.multiply(cost)).setScale(3, BigDecimal.ROUND_UP);
                } else {
                    total = cost.setScale(3, BigDecimal.ROUND_UP);
                }
                invPurchaseInvoiceDetailsEntity.setTotal(total);

                net = total.subtract(total.multiply(discount).divide(hundred)).setScale(3, BigDecimal.ROUND_UP);
                invPurchaseInvoiceDetailsEntity.setValueAfterDiscount(net);

                invPurchaseInvoiceDetailsEntity.setNet(net.setScale(3));

//            invPurchaseInvoiceDetailsEntity.setNet(net.add(net.multiply(percintage)));
                totalSum = (totalSum.add(invPurchaseInvoiceDetailsEntity.getTotal() != null ? invPurchaseInvoiceDetailsEntity.getTotal() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_UP);
                totalQuatity = totalQuatity.add(quantity != null ? quantity : BigDecimal.ZERO).setScale(3);
                weight = purchaseInvoiceDetail.getWeight() != null ? purchaseInvoiceDetail.getWeight() : BigDecimal.ZERO;
                if (weight != null && weight.compareTo(BigDecimal.ZERO) > 0) {
                    if (quantity != null) {
//                        setTotalWeight(getTotalWeight().add(weight.multiply(quantity)));
                        if (weight == null || weight.compareTo(BigDecimal.ZERO) == 0) {//                            
                            setTotalWeight(getTotalWeight().add(weight));
                        } else {
                            setTotalWeight(getTotalWeight().add(weight));
                        }
                    } else {
                        setTotalWeight(getTotalWeight().add(weight));
                    }
                } else {
                    setTotalWeight(getTotalWeight().add(BigDecimal.ZERO));
                }
//            setTotalWeight(getTotalWeight().add(weight != null ? weight : BigDecimal.ZERO));
                totalNet = (totalNet.add(invPurchaseInvoiceDetailsEntity.getNet() != null ? invPurchaseInvoiceDetailsEntity.getNet() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_UP);

                totalValueAfterDiscount = totalValueAfterDiscount.add(invPurchaseInvoiceDetailsEntity.getValueAfterDiscount() != null
                        ? invPurchaseInvoiceDetailsEntity.getValueAfterDiscount() : BigDecimal.ZERO).setScale(3);

                if (purchaseInvoiceDetail != null && purchaseInvoiceDetail.getSelectedId() != null) {
                    if (headIdList == null || headIdList.length() == 0) {
                        headIdList.append(purchaseInvoiceDetail.getSelectedId());
                    } else {
                        headIdList.append(",").append(purchaseInvoiceDetail.getSelectedId());
                    }
                }
                taxvalue = taxvalue.add(purchaseInvoiceDetail.getTaxValue() == null ? BigDecimal.ZERO : purchaseInvoiceDetail.getTaxValue());
                invPurchaseInvoiceDetailsEntityList.add(invPurchaseInvoiceDetailsEntity);
            }

            calculateactualweight();
            updateSummition();
            calculateTotalNetAfterDiscount();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "mapInvPurchaseInvoiceToInvPurchaseInvoiceEntity");
        }
        return invPurchaseInvoiceEntity;
    }

    public void calculateactualweight() {
        try {
            subtractWeight = (invPurchaseInvoiceEntity.getActualWeight() != null ? invPurchaseInvoiceEntity.getActualWeight() : BigDecimal.ZERO).subtract(getTotalWeight() == null ? BigDecimal.ZERO : getTotalWeight());
            multiply = subtractWeight.multiply(invPurchaseInvoiceEntity.getPriceKilo() != null ? invPurchaseInvoiceEntity.getPriceKilo() : BigDecimal.ZERO);
            finalNet = totalNet.add(multiply);
            invPurchaseInvoiceEntity.setTotalActualWeight(finalNet);
            calculateTotalNetAfterDiscount();
            updateTaxDiscValue();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "calculateactualweight");
        }

    }

    public void recalculateExtraCost() {
        try {
            BigDecimal extraCost;
            if (invPurchaseInvoiceEntity.getExtraCost() != null && invPurchaseInvoiceEntity.getExtraCost().compareTo(BigDecimal.ZERO) == 1
                    && totalValueAfterDiscount != null && totalValueAfterDiscount.compareTo(BigDecimal.ZERO) == 1) {

                totalNet = BigDecimal.ZERO;
//            totalValueAfterDiscount = BigDecimal.ZERO;
                for (InvPurchaseInvoiceDetailsEntity detailEntity : invPurchaseInvoiceDetailsEntityList) {
                    extraCost = recaluclateRowDetail(totalValueAfterDiscount, detailEntity.getValueAfterDiscount(), invPurchaseInvoiceEntity.getExtraCost());
                    detailEntity.setExtraCost(extraCost);
                    detailEntity.setNet((extraCost.add(detailEntity.getValueAfterDiscount())).setScale(3, BigDecimal.ROUND_UP));
//
//                totalSum = totalSum.add(detailEntity.getTotal() != null ? detailEntity.getTotal() : BigDecimal.ZERO);
//                totalQuatity = totalQuatity.add(detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO);
                    totalNet = (totalNet.add(detailEntity.getNet() != null ? detailEntity.getNet() : BigDecimal.ZERO)).setScale(3);
//                totalValueAfterDiscount = totalValueAfterDiscount.add(detailEntity.getValueAfterDiscount() != null ? detailEntity.getValueAfterDiscount() : BigDecimal.ZERO);
                }
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "recalculateExtraCost");
        }

    }

    public BigDecimal recaluclateRowDetail(BigDecimal totalValueAfterDiscount, BigDecimal valueAfterDiscount, BigDecimal extraCostHead) {

        BigDecimal x = valueAfterDiscount.divide(totalValueAfterDiscount, 4, 4);
        return extraCostHead.multiply(x);
    }

    public void loadInvPurchaseOrder() {
        try {
            if ((invPurchaseOrderSelectedList == null || invPurchaseOrderSelectedList.isEmpty())
                    && (invAddingOrderSelectedList == null || invAddingOrderSelectedList.isEmpty())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار امر شراء"));
                showDialogMessage = true;
                OpenDlg("dlg2");
            } else {

                for (InvPurchaseInvoiceDetailsEntity entity : invPurchaseInvoiceDetailsEntityList) {
                    if (entity.getId() != null) {
                        rowsDeleted.add(entity);
                    }
                }

                invPurchaseInvoiceDetailsEntityList = new ArrayList<>();
                updatedPurchaseOrAddingOrderList = new ArrayList<>();

                if (invPurchaseOrderSelectedList != null && !invPurchaseOrderSelectedList.isEmpty()) {
                    InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceEntitySelected;
                    List<InvPurchaseOrderDetail> invPurchaseOrderDetailsList;

                    for (InvPurchaseOrder invPurchaseOrder : invPurchaseOrderSelectedList) {

                        updatedPurchaseOrAddingOrderList.add(invPurchaseOrder.getId());
                        isPurchaseOrder = 0;
                        loadHeadData(invPurchaseOrder, null);
                        invPurchaseOrderDetailsList = invPurchaseOrderDetailsService.getAllPurchaseOrderDetailsWithStatus(invPurchaseOrder.getId());
                        for (InvPurchaseOrderDetail purchaseOrderDetail : invPurchaseOrderDetailsList) {
                            invPurchaseInvoiceEntitySelected = new InvPurchaseInvoiceDetailsEntity();

                            if (purchaseOrderDetail.getItemId() != null) {
                                invPurchaseInvoiceEntitySelected.setItemsBarcodesDetail(itemsBarcodeMap.get(purchaseOrderDetail.getItemId().getCode()));
                                invPurchaseInvoiceEntitySelected.setItemsBarcodesDetailTrans(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail());
                                Symbol unit = new Symbol();
                                unit.setId(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail().getUnitId());
                                unit.setName(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail().getUnitName());
                                invPurchaseInvoiceEntitySelected.setUnit(unit);
                            }
//                        invPurchaseInvoiceEntitySelected.setInvItem(purchaseOrderDetail.getItemId() != null ? purchaseOrderDetail.getItemId() : null);
                            invPurchaseInvoiceEntitySelected.setQuantity(purchaseOrderDetail.getFinalQuantity() != null ? purchaseOrderDetail.getFinalQuantity() : null);
                            invPurchaseInvoiceEntitySelected.setCost(purchaseOrderDetail.getPrice() != null ? purchaseOrderDetail.getPrice() : null);
                            invPurchaseInvoiceEntitySelected.setUnit(purchaseOrderDetail.getUnitId() != null ? purchaseOrderDetail.getUnitId() : null);
                            invPurchaseInvoiceEntitySelected.setTransferFrom(0);
                            invPurchaseInvoiceEntitySelected.setSelectedId(purchaseOrderDetail.getId());
                            invPurchaseInvoiceEntitySelected.setQuantityDisabled(Boolean.FALSE);
                            invPurchaseInvoiceDetailsEntityList.add(invPurchaseInvoiceEntitySelected);
                        }
                    }
                    CloseDlg("dlg2");

                    updateSummition();

                    return;
                }

                if (invAddingOrderSelectedList != null && !invAddingOrderSelectedList.isEmpty()) {
                    InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceEntitySelected;
                    List<InvAddingorderDetail> invAddingOrderDetailsList;
                    invPurchaseInvoiceDetailsEntityList = new ArrayList<>();

                    for (InvAddingorder invAddingorder : invAddingOrderSelectedList) {

                        updatedPurchaseOrAddingOrderList.add(invAddingorder.getId());
                        isPurchaseOrder = 1;
                        loadHeadData(null, invAddingorder);
                        invAddingOrderDetailsList = invAddingOrderDetailsService.getAllInvAddingOrderDetailsByInvAddingOrderIdWithStatus(invAddingorder.getId());
                        for (InvAddingorderDetail invAddingOrderDetail : invAddingOrderDetailsList) {
                            invPurchaseInvoiceEntitySelected = new InvPurchaseInvoiceDetailsEntity();

                            if (invAddingOrderDetail.getItemId() != null) {
                                invPurchaseInvoiceEntitySelected.setItemsBarcodesDetail(itemsBarcodeMap.get(invAddingOrderDetail.getItemId().getCode()));
                                invPurchaseInvoiceEntitySelected.setItemsBarcodesDetailTrans(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail());
                                Symbol unit = new Symbol();
                                unit.setId(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail().getUnitId());
                                unit.setName(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail().getUnitName());
                                invPurchaseInvoiceEntitySelected.setUnit(unit);
                            }

//                        invPurchaseInvoiceEntitySelected.setInvItem(invAddingOrderDetail.getItemId() != null ? invAddingOrderDetail.getItemId() : null);
                            invPurchaseInvoiceEntitySelected.setQuantity(invAddingOrderDetail.getFinalQuantity() != null ? invAddingOrderDetail.getFinalQuantity() : null);

                            invPurchaseInvoiceEntitySelected.setSelectedId(invAddingOrderDetail.getId());
                            invPurchaseInvoiceEntitySelected.setTransferFrom(1);
                            invPurchaseInvoiceEntitySelected.setQuantityDisabled(Boolean.TRUE);
                            invPurchaseInvoiceDetailsEntityList.add(invPurchaseInvoiceEntitySelected);
                        }
                    }
                    CloseDlg("dlg2");
                    updateSummition();

                }

            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "loadInvPurchaseOrder");
        }

    }

    private void loadHeadData(InvPurchaseOrder invPurchaseOrder, InvAddingorder invAddingorder) {
        try {

            if (invAddingorder != null) {
                invPurchaseInvoiceEntity.setSupplierInvoiceNumber(invAddingorder.getSupplierInvoice());
                invPurchaseInvoiceEntity.setSupplierInvoiceDate(invAddingorder.getSupplierDate());
                invPurchaseInvoiceEntity.setRemarks(invAddingorder.getRemark());
            }

            if (invPurchaseOrder != null) {
                invPurchaseInvoiceEntity.setRemarks(invPurchaseOrder.getRemarks());
                setHeadRemarks(invPurchaseOrder.getRemarks());
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "loadHeadData");
        }
    }

    public void loadDialog() {
        try {
            invPurchaseOrderList = invPurchaseOrderService.getALLPurchaseOrderByBranchIdByStatus(branchId); // اوامر الشراء
            invAddingOrderList = invAddingOrderService.getALLInvAddingOrderByBranchIdByStatus(branchId);

            if (invPurchaseOrderSelectedList != null && !invPurchaseOrderSelectedList.isEmpty()
                    && invAddingOrderSelectedList != null && !invAddingOrderSelectedList.isEmpty()) {

                onRowPurchaseOrderSelect();
                invAddingOrderSelectedList = new ArrayList<>();

            }

            OpenDlg("dlg2");
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "loadDialog");
        }

    }

    public void OpenDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "OpenDlg");
        }
    }

    public void CloseDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "CloseDlg");
        }
    }

    public void showDuDate() {
        try {
            if (invPurchaseInvoiceEntity.getPaymentType() == 0) {
                invPurchaseInvoiceEntity.setGlBank(glBankList.get(0));
                viewDuDate = false;
            } else {
                invPurchaseInvoiceEntity.setGlBank(null);
                viewDuDate = true;
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "showDuDate");
        }
    }

    public void rowSelected(SelectEvent event) {
        try {
            DataTable dataTable = (DataTable) event.getSource();
            invPurchaseInvoiceDetailsUpdatedSelected = (InvPurchaseInvoiceDetailsEntity) dataTable.getRowData();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "rowSelected");
        }
    }

    public void addInvPurchaseInvoiceDetail() {
        try {
            for (InvPurchaseInvoiceDetailsEntity detailEntity : invPurchaseInvoiceDetailsEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }

                if (detailEntity.getItemsBarcodesDetailTrans() == null
                        || !validateQuantity(detailEntity) || !validatePrice(detailEntity)) {

                    errorMessage();
                    return;
                }
                detailEntity.setMarkEdit(Boolean.FALSE);
            }

            InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntityNew = new InvPurchaseInvoiceDetailsEntity();
            invPurchaseInvoiceDetailsEntityNew.setMarkEdit(Boolean.TRUE);
            invPurchaseInvoiceDetailsUpdatedSelected = invPurchaseInvoiceDetailsEntityNew;
            invPurchaseInvoiceDetailsEntityList.add(invPurchaseInvoiceDetailsEntityNew);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "addInvPurchaseInvoiceDetail");
        }
    }

    public void deleteInvPurchaseInvoiceDetail() {
        try {
            setShowMessageGeneral(Boolean.FALSE);
            setShowMessageDetails(Boolean.TRUE);

            if (invPurchaseInvoiceDetailsEntitySelected.getId() != null) {
                getRowsDeleted().add(invPurchaseInvoiceDetailsEntitySelected);
            }

//        totalQuatity = totalQuatity.subtract(invPurchaseInvoiceDetailsEntitySelected.getQuantity() != null ? invPurchaseInvoiceDetailsEntitySelected.getQuantity() : BigDecimal.ZERO);
//        totalNet = totalNet.subtract(invPurchaseInvoiceDetailsEntitySelected.getNet() != null ? invPurchaseInvoiceDetailsEntitySelected.getNet() : BigDecimal.ZERO);
//        totalLocal = totalLocal.subtract(invPurchaseInvoiceDetailsEntitySelected.getTotal() != null ? invPurchaseInvoiceDetailsEntitySelected.getTotal() : BigDecimal.ZERO);
//        totalSum = totalSum.subtract(invPurchaseInvoiceDetailsEntitySelected.getTotal() != null ? invPurchaseInvoiceDetailsEntitySelected.getTotal() : BigDecimal.ZERO);
//
//        calculateTotalNetAfterDiscount();
//
//        recalculateExtraCost();
            for (InvPurchaseInvoiceDetailsEntity detailEntity : invPurchaseInvoiceDetailsEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
            }
            invPurchaseInvoiceDetailsEntityList.remove(invPurchaseInvoiceDetailsEntitySelected);
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
        }
    }

    public void calculateTotalNetAfterDiscount() {
        try {

            totalNetAfterDiscount = BigDecimal.ZERO;
            if (invPurchaseInvoiceDetailsEntityList != null && !invPurchaseInvoiceDetailsEntityList.isEmpty()) {
                if (invPurchaseInvoiceEntity.getDiscountType() == 0) {
                    if (invPurchaseInvoiceEntity != null && finalNet != null
                            && invPurchaseInvoiceEntity.getHeadDiscount() != null
                            && finalNet.compareTo(BigDecimal.ZERO) == 1
                            && (finalNet.compareTo(invPurchaseInvoiceEntity.getHeadDiscount()) == 0
                            || finalNet.compareTo(invPurchaseInvoiceEntity.getHeadDiscount()) == 1)) {
                        totalNetAfterDiscount = (finalNet.subtract(invPurchaseInvoiceEntity.getHeadDiscount() != null ? invPurchaseInvoiceEntity.getHeadDiscount() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_UP);
                    } else {
                        invPurchaseInvoiceEntity.setHeadDiscount(BigDecimal.ZERO);
                        totalNetAfterDiscount = finalNet.subtract(BigDecimal.ZERO);
                    }

                } else if (isDiscountValid(invPurchaseInvoiceEntity.getHeadDiscount())) {
                    totalNetAfterDiscount = finalNet.subtract(finalNet.multiply(invPurchaseInvoiceEntity.getHeadDiscount()).divide(hundred));
                } else {
                    invPurchaseInvoiceEntity.setHeadDiscount(BigDecimal.ZERO);
                    totalNetAfterDiscount = finalNet.subtract(BigDecimal.ZERO);
                }
                updateTax();
                updateTaxDiscValue();
            } else {
                removeTaxValue();
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "calculateTotalNetAfterDiscount");
        }
    }

    public List<CostCenter> completeCostCenters(String query) {
        List<CostCenter> filteredCostCenters = new ArrayList<>();
        try {
            List<CostCenter> centersList = costCenterList;
            if (query == null || query.trim().equals("")) {

                costCenterConverter = new CostCenterConverter(centersList);
                return centersList;
            }

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
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "completeCostCenters");
        }
        return filteredCostCenters;
    }

    public List<ItemsBarcodesDetailsView> completeItemsData(String query) {
        List<ItemsBarcodesDetailsView> filteredItems = new ArrayList<>();
        try {

            List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViews = itemsBarcodesDetailsViewList;
            if (query == null || query.trim().equals("")) {
                itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViews);
                return itemsBarcodesDetailsViews;
            }

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
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "completeItemsData");
        }
        return filteredItems;
    }

    public List<Symbol> completeSymbolUnit(String query) {
        List<Symbol> filteredUnits = new ArrayList<>();
        try {
//        if (invPurchaseInvoiceDetailsUpdatedSelected != null && invPurchaseInvoiceDetailsUpdatedSelected.getUnits() != null && !invPurchaseInvoiceDetailsUpdatedSelected.getUnits().isEmpty()) {
            List<Symbol> unitSymbolList = unitList;
            if (query == null || query.trim().equals("")) {

                unitConverter = new SymbolConverter(unitSymbolList);
                return unitSymbolList;
            }

            String nameAr;
            Integer code;
            Symbol unitFilter;
            for (int i = 0; i < unitList.size(); i++) {
                unitFilter = unitSymbolList.get(i);
                nameAr = unitFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredUnits.contains(unitFilter)) {
                        filteredUnits.add(unitFilter);
                    }
                }

                code = unitFilter.getSerial();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredUnits.contains(unitFilter)) {
                        filteredUnits.add(unitFilter);
                    }
                }
            }

            unitConverter = new SymbolConverter(filteredUnits);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "completeSymbolUnit");
        }
        return filteredUnits;
//        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار سطر للتعديل"));
//            showMessageDetails = true;
//            return null;
//        }
    }

    public List<InvOrganizationSite> completeSuppllier(String query) {
        List<InvOrganizationSite> filteredSuppliers = new ArrayList<>();
        try {
            List<InvOrganizationSite> supplierList = invOrganizationSiteList;
            if (query == null || query.trim().equals("")) {

                supplierConvertor = new InvOrganizationSiteConverter(supplierList);
                return supplierList;
            }

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
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "completeSuppllier");
        }
        return filteredSuppliers;
    }

    public List<Currency> completeCurrency(String query) {
        List<Currency> filteredCurrencies = new ArrayList<>();
        try {
            List<Currency> currencyListF = currencyList;
            if (query == null || query.trim().equals("")) {

                currencyConverter = new CurrencyConverter(currencyListF);
                return currencyListF;
            }

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
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "completeCurrency");
        }
        return filteredCurrencies;
    }

    public List<GlAdminUnit> completeAdminUnits(String query) {
        List<GlAdminUnit> filteredAdminUnits = new ArrayList<>();
        try {
            List<GlAdminUnit> adminList = adminUnitList;
            if (query == null || query.trim().equals("")) {

                adminUnitConverter = new GlAdminUnitConverter(adminList);
                return adminList;
            }

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
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "completeAdminUnits");
        }
        return filteredAdminUnits;
    }

    public List<InvInventory> completeInventory(String query) {
        List<InvInventory> filteredInvs = new ArrayList<>();
        try {
            List<InvInventory> invList = invInventoryList;
            if (query == null || query.trim().equals("")) {

                invInventoryConverter = new InvInventoryConverter(invList);
                return invList;
            }

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
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "completeInventory");
        }
        return filteredInvs;
    }

    public List<InventoryDelegator> completePurchasePerson(String query) {
        List<InventoryDelegator> filteredInvs = new ArrayList<>();
        try {

            List<InventoryDelegator> invList = purchasePersonList;
            if (query == null || query.trim().equals("")) {

                purchasePersonConvertor = new InvDelegatorConvertor(invList);
                return invList;
            }

            String nameAr;
            String code;
            InventoryDelegator invFilter;
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

            purchasePersonConvertor = new InvDelegatorConvertor(filteredInvs);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "completePurchasePerson");
        }
        return filteredInvs;
    }

    public List<GlBank> completeGlBank(String query) {
        List<GlBank> filteredGlBanks = new ArrayList<>();
        try {
            List<GlBank> glBanksList = glBankList;
            if (query == null || query.trim().equals("")) {

                glBankConverter = new GlBankConverter(glBanksList);
                return glBanksList;
            }

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
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "completeGlBank");
        }
        return filteredGlBanks;
    }

    public void putCurrency() {
        try {
            if (invPurchaseInvoiceEntity.getSupplierId() != null && invPurchaseInvoiceEntity.getSupplierId().getCurrencyId() != null) {
                invPurchaseInvoiceEntity.setCurrencyId(invPurchaseInvoiceEntity.getSupplierId().getCurrencyId());
                updateRate();

                updateSummition();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "هذا المورد ليس له عملة!"));
                showMessageGeneral = true;
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "putCurrency");
        }
    }

    public void toggleSelectPurchaseOrder() {
        try {
            if (!toggleSelectedPurchaseOrder) {
                toggleSelectedPurchaseOrder = true;
                onRowPurchaseOrderSelect();
            } else {
                toggleSelectedPurchaseOrder = false;
                unSelectRow();
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "toggleSelectPurchaseOrder");
        }
    }

    public void toggleSelectAddingOrder() {
        try {
            if (!toggleSelectedAddingOrder) {
                toggleSelectedAddingOrder = true;
                onRowInvAddingOrderSelect();
            } else {
                toggleSelectedAddingOrder = false;
                unSelectRow();
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "toggleSelectAddingOrder");
        }
    }

    public void onRowPurchaseOrderSelect() {
        try {
            purchaseOrderTabView = true;
            addingOrderTabView = false;
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "onRowPurchaseOrderSelect");
        }

    }

    public void onRowInvAddingOrderSelect() {
        try {
            purchaseOrderTabView = false;
            addingOrderTabView = true;
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "onRowInvAddingOrderSelect");
        }
    }

    public void unSelectRow() {
        try {
            if ((invPurchaseOrderSelectedList == null || invPurchaseOrderSelectedList.isEmpty())
                    && (invAddingOrderSelectedList == null || invAddingOrderSelectedList.isEmpty())) {
                purchaseOrderTabView = addingOrderTabView = true;
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "unSelectRow");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {
            Object oldValue = event.getOldValue() != null ? event.getOldValue() : null;

            String column_name = event.getColumn().getClientId();

            DataTable dataTable = (DataTable) event.getSource();
            invPurchaseInvoiceDetailsUpdatedSelected = (InvPurchaseInvoiceDetailsEntity) dataTable.getRowData();

            if (invPurchaseInvoiceDetailsUpdatedSelected != null) {

                for (InvPurchaseInvoiceDetailsEntity detailEntity : invPurchaseInvoiceDetailsEntityList) {
                    detailEntity.setMarkEdit(Boolean.FALSE);
                }
                invPurchaseInvoiceDetailsUpdatedSelected.setMarkEdit(Boolean.TRUE);

//            if (column_name.contains("itemNumber") || column_name.contains("itemName")) {
//                validateItems(invPurchaseInvoiceDetailsUpdatedSelected);
//            }
//            validateQuantityColum();
//            if (column_name.contains("COST")) {
//                validatePriceColumn(oldValue);
//            }
//
//            if (column_name.contains("Discount")) {
//                validateDiscountColumn(oldValue);
//            }
                updateSummition();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار سطر للتعديل"));
                showMessageDetails = true;
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "onCellEdit");
        }
    }

    public void validateDiscountColumn(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {
        try {
            if (!validateDiscount(invPurchaseInvoiceDetailsValidate)) {
                invPurchaseInvoiceDetailsValidate.setDiscount(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateDiscountColumn");
        }
    }

    public void validatePriceColumn(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {
        try {
            if (!validatePrice(invPurchaseInvoiceDetailsValidate)) {
                invPurchaseInvoiceDetailsValidate.setCost(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validatePriceColumn");
        }
    }

    public void validateQuantityColum(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {
        try {
            if (!validateQuantity(invPurchaseInvoiceDetailsValidate)) {
                if (invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() != null) {
                    invPurchaseInvoiceDetailsValidate.setQuantity(BigDecimal.ZERO);
                } else {
                    invPurchaseInvoiceDetailsValidate.setQuantity(null);
                }
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateQuantityColum");
        }
    }

    public Boolean validateQuantity(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {

        return invPurchaseInvoiceDetailsValidate != null && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() != null
                && invPurchaseInvoiceDetailsValidate.getQuantity() != null
                && invPurchaseInvoiceDetailsValidate.getQuantity().compareTo(BigDecimal.ZERO) == 1;

    }

    public Boolean validateUnit(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {

        return invPurchaseInvoiceDetailsValidate != null && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() != null
                && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getUnitId() != null
                && invPurchaseInvoiceDetailsValidate.getUnit() != null;

    }

    public Boolean validatePrice(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {
        return invPurchaseInvoiceDetailsValidate != null
                && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetailTrans() != null
                && invPurchaseInvoiceDetailsValidate.getCost() != null
                && invPurchaseInvoiceDetailsValidate.getCost().compareTo(BigDecimal.ZERO) == 1;
    }

    public Boolean validateDiscount(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {
        return isDiscountValid(invPurchaseInvoiceDetailsValidate.getDiscount());
    }

    private Boolean isDiscountValid(BigDecimal discValue) {

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
    }

    public Boolean validateItems(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {
        try {
            if (invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() == null) {
                resetInvItem(invPurchaseInvoiceDetailsValidate);
            } else {
//            invPurchaseInvoiceDetailsValidate.setUnits(unitsMap.get(invPurchaseInvoiceDetailsValidate.getInvItem().getId()));
//            if (invPurchaseInvoiceDetailsValidate.getUnits() != null) {

                invPurchaseInvoiceDetailsValidate.setItemsBarcodesDetailTrans(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail());

                if (invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getUnitName() != null
                        && !"".equals(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getUnitName())) {
                    Symbol unit = new Symbol();
                    unit.setId(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getUnitId());
                    unit.setName(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getUnitName());
                    invPurchaseInvoiceDetailsValidate.setUnit(unit);
                    Symbol paintColor = new Symbol();
                    paintColor.setId(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getPaintColor());
                    paintColor.setName(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getPaintColorName());
                    invPurchaseInvoiceDetailsValidate.setPaintColor(paintColor);
//                invPurchaseInvoiceDetailsValidate.setCost(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getSellPrice());
                    invPurchaseInvoiceDetailsValidate.setScrewing(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getScrewing());
                    invPurchaseInvoiceDetailsValidate.setLength(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getLength());
                    invPurchaseInvoiceDetailsValidate.setWeight(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getWeight() != null ? invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getWeight() : BigDecimal.ZERO);
                } else {
                    resetInvItem(invPurchaseInvoiceDetailsValidate);
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "هذا الصنف ليس لديه وحدات."));
                    showMessageDetails = true;
                    return false;
                }
//            } else {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "هذا الصنف ليس لديه وحدات."));
//                showMessageDetails = true;
//                return false;
//            }
//            invPurchaseInvoiceDetailsValidate.setCost(invPurchaseInvoiceDetailsValidate.getInvItem().getSellPrice() != null ? invPurchaseInvoiceDetailsValidate.getInvItem().getSellPrice() : BigDecimal.ZERO);
            }
            updateSummition();

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateItems");
            return null;
        }
        return true;
    }

    private void resetInvItem(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {
        try {
            invPurchaseInvoiceDetailsValidate.setQuantity(null);
            invPurchaseInvoiceDetailsValidate.setWeight(null);
            invPurchaseInvoiceDetailsValidate.setCost(null);
            invPurchaseInvoiceDetailsValidate.setDiscount(null);
            invPurchaseInvoiceDetailsValidate.setUnit(null);
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "resetInvItem");

        }

    }

    public void updateSummition() {
        try {
            BigDecimal quantity, weight, discount, cost, total, valueAfterDiscount;
            totalSum = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;
            setTotalWeight(BigDecimal.ZERO);
            totalValueAfterDiscount = BigDecimal.ZERO;

            BigDecimal rate = invPurchaseInvoiceEntity.getRate() != null ? invPurchaseInvoiceEntity.getRate() : BigDecimal.ONE;
            if (invPurchaseInvoiceDetailsEntityList != null && !invPurchaseInvoiceDetailsEntityList.isEmpty()) {
                for (InvPurchaseInvoiceDetailsEntity detailEntity : invPurchaseInvoiceDetailsEntityList) {
                    if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                        detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                    }
                    quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
                    weight = (detailEntity.getWeight() == null || detailEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : detailEntity.getWeight();
                    cost = detailEntity.getCost() != null ? detailEntity.getCost().multiply(rate) : BigDecimal.ZERO;
                    discount = detailEntity.getDiscount() != null ? detailEntity.getDiscount() : BigDecimal.ZERO;
//            detailEntity.setCost(cost);
                    if (weight == null || weight.compareTo(BigDecimal.ZERO) == 0) {
                        total = quantity.multiply(cost);
                    } else {
                        total = weight.multiply(cost);
                    }
//                                       
                    detailEntity.setTotal(total);

                    valueAfterDiscount = total.subtract((total.multiply(discount).divide(hundred)));
                    detailEntity.setValueAfterDiscount(valueAfterDiscount);
//            detailEntity.setNet((net.add(net.multiply(percintage))).setScale(3, BigDecimal.ROUND_UP));

//            detailEntity.setNet((quantity.multiply(cost).subtract(quantity.multiply(cost).multiply(discount).divide(hundred))).setScale(3, BigDecimal.ROUND_UP));
                    totalSum = totalSum.add(detailEntity.getTotal() != null ? detailEntity.getTotal() : BigDecimal.ZERO);
                    totalQuatity = totalQuatity.add(detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO);

                    if (detailEntity.getWeight() != null && detailEntity.getWeight().compareTo(BigDecimal.ZERO) > 0) {
                        if (detailEntity.getQuantity() != null) {
                            if (detailEntity.getWeight() == null || detailEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                                setTotalWeight(getTotalWeight().add(detailEntity.getWeight().multiply(detailEntity.getQuantity())));

                            } else {
                                setTotalWeight(getTotalWeight().add(detailEntity.getWeight()));
                            }
                        } else {
                            setTotalWeight(getTotalWeight().add(detailEntity.getWeight()));
                        }
                    } else {
                        setTotalWeight(getTotalWeight().add(BigDecimal.ZERO));
                    }

//            totalNet = totalNet.add(detailEntity.getNet() != null ? detailEntity.getNet() : BigDecimal.ZERO);
                    totalValueAfterDiscount = totalValueAfterDiscount.add(detailEntity.getValueAfterDiscount() != null ? detailEntity.getValueAfterDiscount() : BigDecimal.ZERO);
                    detailEntity.setNet(valueAfterDiscount.setScale(3, BigDecimal.ROUND_UP));
                    totalNet = (totalNet.add(detailEntity.getNet() != null ? detailEntity.getNet() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_UP);
                }
                if (invPurchaseInvoiceEntity.getExtraCost() != null && invPurchaseInvoiceEntity.getExtraCost().compareTo(BigDecimal.ZERO) == 1) {
                    recalculateExtraCost();
                }
                calculateactualweight();
                calculateTotalNetAfterDiscount();
                updateTax();
                updateTaxDiscValue();
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "updateSummition");
        }
    }

    public void updateRate() {
        try {
            if (invPurchaseInvoiceEntity.getCurrencyId() == null) {
                invPurchaseInvoiceEntity.setCurrencyId(currencyList.get(0));
            }
            currencyOperation = currencyOperationService.getRatesByDates(invPurchaseInvoiceEntity.getCurrencyId().getId(), invPurchaseInvoiceEntity.getDate(), getUserData().getCompany().getId());
            invPurchaseInvoiceEntity.setRate(currencyOperation == null ? BigDecimal.ONE : currencyOperation.getRate());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "updateRate");
        }
    }

    public void updateDate(SelectEvent event) {
        try {
            updateRate();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "updateDate");
        }
    }

    public String print() {
        try {
            if (invPurchaseInvoice != null && invPurchaseInvoice.getId() != null) {
                invPurchaseInvoiceDetailsList = new ArrayList<>();
                invPurchaseInvoiceDetailsEntityReport = new ArrayList<>();
                invPurchaseInvoiceDetailsList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(invPurchaseInvoiceId);
                StringBuilder sb = new StringBuilder();
                InvPurchaseInvoiceDetailsEntity details;
                Map<String, ItemsBarcodesDetailsView> map = new HashMap();
                for (InvPurchaseInvoiceDetail detailsEntity : invPurchaseInvoiceDetailsList) {
                    if (sb.toString().isEmpty()) {
                        sb.append(detailsEntity.getItemBarcode());
                    } else {
                        sb.append(" , ").append(detailsEntity.getItemBarcode());
                    }
                }
                List<ItemsBarcodesDetailsView> barcodesDetailsViews = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdAndStore(branchId, sb.toString());

                for (ItemsBarcodesDetailsView view : barcodesDetailsViews) {
                    map.put(view.getBarcode(), view);
                }

                for (InvPurchaseInvoiceDetail detailsEntity : invPurchaseInvoiceDetailsList) {
                    details = new InvPurchaseInvoiceDetailsEntity();
                    details.setSerial(detailsEntity.getSerial());
                    details.setItemCode(detailsEntity.getItemBarcode());
                    details.setItemName(detailsEntity.getItemId().getName());
                    details.setUnitName(detailsEntity.getUnitId().getName());
                    details.setQuantity(detailsEntity.getQuantity());
                    details.setCost(detailsEntity.getCost());
                    details.setNet(detailsEntity.getNet());
                    details.setColor(map.get(detailsEntity.getItemBarcode()).getPaintColorName());
                    details.setLength(map.get(detailsEntity.getItemBarcode()).getLength());
                    invPurchaseInvoiceDetailsEntityReport.add(details);
                }
                try {
                    fillReport(prepareReport(), getUserData().getReportPath() + "purchaseReceiptCash.jasper", invPurchaseInvoiceDetailsEntityReport, "pdf");
                } catch (JRException ex) {
                    Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                }
                exit();
                return "invpurchaseinvoicelist";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "print");
        }
        return "";
    }

    public String exportPDF(ActionEvent actionEvent) {
        try {

            if (saveReport() && invPurchaseInvoice != null && invPurchaseInvoice.getId() != null) {
                invPurchaseInvoiceDetailsList = new ArrayList<>();
                invPurchaseInvoiceDetailsEntityReport = new ArrayList<>();
                invPurchaseInvoiceDetailsList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(invPurchaseInvoice.getId());

                InvPurchaseInvoiceDetailsEntity details;
                for (InvPurchaseInvoiceDetail detailsEntity : invPurchaseInvoiceDetailsList) {
                    details = new InvPurchaseInvoiceDetailsEntity();
                    details.setSerial(detailsEntity.getSerial());
                    details.setItemCode(detailsEntity.getItemBarcode());
                    details.setItemName(detailsEntity.getItemId().getName());
                    details.setUnitName(detailsEntity.getItemId().getUnitId().getName());
                    details.setQuantity(detailsEntity.getQuantity());
                    details.setWeight(detailsEntity.getWeight());
                    details.setCost(detailsEntity.getCost());
                    details.setNet(detailsEntity.getNet());

                    invPurchaseInvoiceDetailsEntityReport.add(details);
                }
                try {
                    fillReport(prepareReport(), getUserData().getReportPath() + "purchaseReceiptCash.jasper", invPurchaseInvoiceDetailsEntityReport, "pdf");
                } catch (JRException ex) {
                    Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                }
                exit();
                return "invpurchaseinvoicelist";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "exportPDF");
        }
        return "";
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
            saveError(e, "InvPurchaseInvoiceFormMB", "fillReports");
        }
    }

    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();

            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());
            //   hashMap.put("companyImage", getUserData().getImageReportPath());
            hashMap.put("oresText", invPurchaseInvoiceEntity.getInvInventory().getName());
            hashMap.put("dateText", userDDs.get("DATE"));

            if (invPurchaseInvoiceEntity.getPaymentType() == 0) {
                hashMap.put("salesReceiptText", "فاتورة مشتريات نقدية");
            } else if (invPurchaseInvoiceEntity.getPaymentType() == 1) {
                hashMap.put("salesReceiptText", "فاتورة مشتريات آجلة");
            } else {
                hashMap.put("salesReceiptText", "فاتورة مشتريات اعتماد مستندي");
            }

            hashMap.put("receiptNumberText", "رقم الفاتورة");
            hashMap.put("weightDifferenceText", "فرق الوزن");
            hashMap.put("weightDifference", multiply);

            hashMap.put("documentNumberText", "رقم سند استلام");
            hashMap.put("remarkText", userDDs.get("REMARK"));

            hashMap.put("supplierText", userDDs.get("SUPPLIER"));

            hashMap.put("supplierReceiptText", "فاتورة المورد");
            hashMap.put("transportationSupplierText", "مورد النقل");

            hashMap.put("secondDateText", "تاريخها");

            hashMap.put("serialText", "م");

            hashMap.put("itemNumberText", "رقم الصنف");
            hashMap.put("itemNameText", "اسم الصنف");
            hashMap.put("unitText", "الوحدة");
            hashMap.put("colorText", "اللون");
            hashMap.put("lengthText", "الطول");
            hashMap.put("amountText", "كمية");
            hashMap.put("unitCostText", "تكلفة وحدة");
            hashMap.put("totalText", "القيمة");
            hashMap.put("nolonText", "النولون");
            hashMap.put("discountText", "الخصم");
            hashMap.put("netText", "الصافي");
            hashMap.put("totalWithTaxText", " الاجمالي");
            hashMap.put("tax", "الضريبة");

            hashMap.put("taxValue", taxvalue);
            hashMap.put("totalWithTaxValue", totalWithTaxValue);
            hashMap.put("receiptNumberValue", invPurchaseInvoiceEntity.getSerial());
            hashMap.put("dateValue", invPurchaseInvoiceEntity.getDate());
            hashMap.put("remarkValue", headRemarks);
            hashMap.put("supplierValue", invPurchaseInvoiceEntity.getSupplierId() != null ? invPurchaseInvoiceEntity.getSupplierId().getCode() : "");
            hashMap.put("supplierName", invPurchaseInvoiceEntity.getSupplierId() != null ? invPurchaseInvoiceEntity.getSupplierId().getName() : "");
            hashMap.put("supplierReceiptValue", invPurchaseInvoiceEntity.getSupplierInvoiceNumber());
//        hashMap.put("transportationSupplierValue", invPurchaseInvoiceEntity.get);
//        hashMap.put("transportationSupplierName", "الصافي");
            hashMap.put("nolonValue", invPurchaseInvoiceEntity.getExtraCost());

            hashMap.put("discountValue", invPurchaseInvoiceEntity.getHeadDiscount());
            if (invPurchaseInvoiceEntity.getDiscountType() != 0) {
                hashMap.put("%", " % ");
            } else {
                hashMap.put("%", "");
            }

            hashMap.put("netValue", totalNet);

            hashMap.put("netValueArabic", Tafqeet.numberToText(Double.parseDouble(totalNetAfterDiscount.toString()), "جنيه", "قرش"));

            hashMap.put("secondDateValue", invPurchaseInvoiceEntity.getSupplierInvoiceDate());

            hashMap.put("reportDate", new Date());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "prepareReport");

        }

        return hashMap;
    }

    public void updateTax() {

        try {
            totalWithTaxValue = totalNetAfterDiscount;
            taxvalue = BigDecimal.ZERO;
            if (invPurchaseInvoiceEntity.getTaxFlag() != null && invPurchaseInvoiceEntity.getTaxFlag()) {
                taxvalue = totalNetAfterDiscount.multiply(BigDecimal.valueOf(0.14));
                totalWithTaxValue = totalNetAfterDiscount.add(taxvalue);
            }
            updateTaxDiscValue();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "updateTax");

        }
    }

    public void updateTaxDiscValue() {
        try {

            if (invPurchaseInvoiceEntity.getTaxdiscflag() != null && invPurchaseInvoiceEntity.getTaxdiscflag()) {
                invPurchaseInvoiceEntity.setTaxdiscvalue(totalNetAfterDiscount.multiply(BigDecimal.valueOf(0.01)));
                totalWithTaxValue = totalWithTaxValue.subtract(invPurchaseInvoiceEntity.getTaxdiscvalue());
            } else {
                invPurchaseInvoiceEntity.setTaxdiscvalue(BigDecimal.ZERO);
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "updateTaxDiscValue");

        }

    }

    public void removeTaxValue() {
        try {
            totalWithTaxValue = totalNetAfterDiscount;
            taxvalue = BigDecimal.ZERO;
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "removeTaxValue");
        }
    }

    @Override
    public String save() {
        try {
            if (saveReport()) {
                return "invpurchaseinvoicelist";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "save");
            return null;
        }
    }

    public String saveAndContinue() {
        try {
            if (saveReport()) {
//            reset();
                return "";
            }
            return "";
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "save");
            return null;
        }

    }

    public void resetPurchaseInvoice() {
        try {
            invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoiceDetailsUpdatedList = new ArrayList<>();
            invPurchaseInvoiceDetailsDeletedList = new ArrayList<>();
            updatedPurchaseOrAddingOrderList = new ArrayList<>();

            invPurchaseInvoiceExist = new InvPurchaseInvoice();
            headIdList = new StringBuilder();

            invPurchaseInvoiceEntity.setSerial(null);

            invPurchaseInvoiceEntity.setId(null);

            invPurchaseInvoiceDetailsEntityList = new ArrayList<>();

            invPurchaseInvoiceEntity.setActualWeight(BigDecimal.ZERO);
            invPurchaseInvoiceEntity.setPriceKilo(BigDecimal.ZERO);
            invPurchaseInvoiceEntity.setTotalActualWeight(BigDecimal.ZERO);
            invPurchaseInvoiceEntity.setHeadDiscount(BigDecimal.ZERO);

            totalNetAfterDiscount = BigDecimal.ZERO;
            taxvalue = BigDecimal.ZERO;
            totalWithTaxValue = BigDecimal.ZERO;

            totalNet = BigDecimal.ZERO;
            totalSum = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            totalWeight = BigDecimal.ZERO;
            multiply = BigDecimal.ZERO;
            finalNet = BigDecimal.ZERO;
            updateTaxDiscValue();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "save");

        }

    }

    public boolean saveReport() {
        try {
            if (invPurchaseInvoiceEntity != null) {

                if (invPurchaseInvoiceEntity.getInvInventory() == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب أختيار مخزن"));
                    showMessageDetails = true;
                    return false;
                } else {
                    invPurchaseInvoice.setInvInventoryId(invPurchaseInvoiceEntity.getInvInventory());
                }

                if (invPurchaseInvoiceEntity.getId() != null) {
                    invPurchaseInvoice.setId(invPurchaseInvoiceEntity.getId());
                    invPurchaseInvoice.setCreatedBy(getInvPurchaseInvoiceExist().getCreatedBy() != null ? getInvPurchaseInvoiceExist().getCreatedBy() : null);
                    invPurchaseInvoice.setCreationDate(getInvPurchaseInvoiceExist().getCreationDate() != null ? getInvPurchaseInvoiceExist().getCreationDate() : null);

                    invPurchaseInvoice.setModifiedBy(getUserData().getUser());
                    invPurchaseInvoice.setModificationDate(new Date());

                } else {
                    invPurchaseInvoice.setCreatedBy(getUserData().getUser());
                    invPurchaseInvoice.setCreationDate(new Date());
                }

                if (invPurchaseInvoiceEntity.getAdminUnitId() != null) {
                    invPurchaseInvoice.setAdminUnitId(invPurchaseInvoiceEntity.getAdminUnitId());
                }
                if (invPurchaseInvoiceEntity.getCostCenterId() != null) {
                    invPurchaseInvoice.setCostCenterId(invPurchaseInvoiceEntity.getCostCenterId());
                }

                if (invPurchaseInvoiceEntity.getCurrencyId() != null) {
                    invPurchaseInvoice.setCurrencyId(invPurchaseInvoiceEntity.getCurrencyId());
                }

                if (invPurchaseInvoiceEntity.getSupplierId() != null) {
                    invPurchaseInvoice.setOrganizationSiteId(invPurchaseInvoiceEntity.getSupplierId());
                }

                invPurchaseInvoice.setTaxflag(invPurchaseInvoiceEntity.getTaxFlag());
                invPurchaseInvoice.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch() : null);
                invPurchaseInvoice.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);
                invPurchaseInvoice.setDate(invPurchaseInvoiceEntity.getDate() != null ? invPurchaseInvoiceEntity.getDate() : null);
                invPurchaseInvoice.setPostFlag(0);
                invPurchaseInvoice.setActualWeight(invPurchaseInvoiceEntity.getActualWeight());
                invPurchaseInvoice.setPriceKilo(invPurchaseInvoiceEntity.getPriceKilo());
                calculateactualweight();
                invPurchaseInvoice.setTotalActualWeight(finalNet);
                invPurchaseInvoice.setDiscount(invPurchaseInvoiceEntity.getHeadDiscount() != null ? invPurchaseInvoiceEntity.getHeadDiscount() : BigDecimal.ZERO);
                invPurchaseInvoice.setDiscountType(invPurchaseInvoiceEntity.getDiscountType() != null ? invPurchaseInvoiceEntity.getDiscountType() : null);
                invPurchaseInvoice.setDueDate(invPurchaseInvoiceEntity.getDueDate() != null ? invPurchaseInvoiceEntity.getDueDate() : null);
                invPurchaseInvoice.setPaymentType(invPurchaseInvoiceEntity.getPaymentType() > 0 ? invPurchaseInvoiceEntity.getPaymentType() : 0);
                invPurchaseInvoice.setRate(invPurchaseInvoiceEntity.getRate() != null ? invPurchaseInvoiceEntity.getRate() : null);
                invPurchaseInvoice.setSupplierInvoiceNumber(invPurchaseInvoiceEntity.getSupplierInvoiceNumber());
                invPurchaseInvoice.setSupplierInvoiceDate(invPurchaseInvoiceEntity.getSupplierInvoiceDate());
                invPurchaseInvoice.setExtraCost(invPurchaseInvoiceEntity.getExtraCost() != null ? invPurchaseInvoiceEntity.getExtraCost() : BigDecimal.ZERO);
                invPurchaseInvoice.setInvDelegatorId(invPurchaseInvoiceEntity.getPurchasePerson());
                invPurchaseInvoice.setGlBankId(invPurchaseInvoiceEntity.getGlBank());
                updateTaxDiscValue();
                invPurchaseInvoice.setTaxdiscflag(invPurchaseInvoiceEntity.getTaxdiscflag());

                invPurchaseInvoice.setTaxdiscvalue(invPurchaseInvoiceEntity.getTaxdiscvalue());
                invPurchaseInvoiceEntity.setRemarks(headRemarks);
                invPurchaseInvoice.setRemarks(headRemarks);

                invPurchaseInvoice.setType(0);

                if (invPurchaseInvoiceEntity.getAccountId() != null) {
                    GlAccount accountId = new GlAccount();
                    accountId.setId(invPurchaseInvoiceEntity.getAccountId());
                    invPurchaseInvoice.setAccountId(accountId);
                }

                InvPurchaseInvoiceDetail purchaseInvoiceDetail;
                InvPurchaseInvoiceDetail purchaseInvoiceDetailExist;

                if (invPurchaseInvoiceDetailsEntityList != null && !invPurchaseInvoiceDetailsEntityList.isEmpty()) {
                    invPurchaseInvoiceDetailsUpdatedList = new ArrayList<>();
                    invPurchaseInvoiceDetailsDeletedList = new ArrayList<>();
                    for (InvPurchaseInvoiceDetailsEntity detailsEntity : invPurchaseInvoiceDetailsEntityList) {
                        purchaseInvoiceDetail = new InvPurchaseInvoiceDetail();

                        if (detailsEntity.getItemsBarcodesDetail() == null && detailsEntity.getItemsBarcodesDetailTrans() == null) {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ادخال الصنف"));
                            showMessageDetails = true;
                            return false;
                        } else {
                            InvItem invItem = new InvItem();
                            invItem.setId(detailsEntity.getItemsBarcodesDetailTrans().getItemId());
                            purchaseInvoiceDetail.setItemId(invItem);
                            purchaseInvoiceDetail.setItemBarcode(detailsEntity.getItemsBarcodesDetailTrans().getBarcode());
                            detailsEntity.setItemsBarcodesDetail(detailsEntity.getItemsBarcodesDetailTrans());
                        }
                        purchaseInvoiceDetail.setWeight(detailsEntity.getWeight());
                        if (detailsEntity.getId() != null) {
                            purchaseInvoiceDetailExist = new InvPurchaseInvoiceDetail();
                            purchaseInvoiceDetail.setId(detailsEntity.getId());

                            purchaseInvoiceDetailExist = invPurchaseInvoiceDetailsService.findInvPurchaseInvoiceDetailsById(detailsEntity.getId());
                            purchaseInvoiceDetail.setCreatedBy(purchaseInvoiceDetailExist.getCreatedBy());
                            purchaseInvoiceDetail.setCreationDate(purchaseInvoiceDetailExist.getCreationDate());

                            purchaseInvoiceDetail.setModificationDate(new Date());
                            purchaseInvoiceDetail.setModifiedBy(getUserData().getUser() != null ? getUserData().getUser() : null);

                        } else {
                            purchaseInvoiceDetail.setCreatedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                            purchaseInvoiceDetail.setCreationDate(new Date());
                        }

                        purchaseInvoiceDetail.setTransferFrom(detailsEntity.getTransferFrom() != null ? detailsEntity.getTransferFrom() : null);
                        purchaseInvoiceDetail.setSelectedId(detailsEntity.getSelectedId() != null ? detailsEntity.getSelectedId() : null);
                        purchaseInvoiceDetail.setScrewing(detailsEntity.getScrewing());

                        purchaseInvoiceDetail.setBranchId(getUserData().getUserBranch());
                        purchaseInvoiceDetail.setCompanyId(getUserData().getCompany());
                        if (detailsEntity.getDiscount() != null) {
                            if (isDiscountValid(detailsEntity.getDiscount())) {
                                purchaseInvoiceDetail.setDiscount(detailsEntity.getDiscount());
                            } else {
                                return false;
                            }
                        }
                        purchaseInvoiceDetail.setDiscountType(detailsEntity.getDiscountType());
                        purchaseInvoiceDetail.setExtraCost(detailsEntity.getExtraCost());

                        if (validatePrice(detailsEntity)) {
                            purchaseInvoiceDetail.setCost(detailsEntity.getCost().setScale(3, BigDecimal.ROUND_UP));
                        } else {
                            errorMessage();
                            return false;
                        }

                        if (validateQuantity(detailsEntity)) {
                            purchaseInvoiceDetail.setQuantity(detailsEntity.getQuantity());
                        } else {
                            errorMessage();
                            return false;
                        }

                        purchaseInvoiceDetail.setUnitId(detailsEntity.getUnit());
                        purchaseInvoiceDetail.setNet(detailsEntity.getNet());

                        if (invPurchaseInvoice.getTaxflag()) {
                            purchaseInvoiceDetail.setTaxValue((purchaseInvoiceDetail.getNet().multiply(BigDecimal.valueOf(0.14))));
//                        taxvalue = taxvalue.add(purchaseInvoiceDetail.getTaxValue());
                        }
                        invPurchaseInvoiceDetailsUpdatedList.add(purchaseInvoiceDetail);
                    }
                    totalWithTaxValue = totalNetAfterDiscount.add(taxvalue);
                } else {
                    errorMessage();
                    return false;
                }

                if (!getRowsDeleted().isEmpty() && getRowsDeleted() != null) {
                    for (InvPurchaseInvoiceDetailsEntity rowDeleted : getRowsDeleted()) {
                        purchaseInvoiceDetail = new InvPurchaseInvoiceDetail();
                        purchaseInvoiceDetail.setId(rowDeleted.getId());
                        purchaseInvoiceDetail.setSelectedId(rowDeleted.getSelectedId() != null ? rowDeleted.getSelectedId() : null);
                        purchaseInvoiceDetail.setQuantity(rowDeleted.getQuantity() != null ? rowDeleted.getQuantity() : BigDecimal.ZERO);
                        purchaseInvoiceDetail.setTransferFrom(rowDeleted.getTransferFrom() != null ? rowDeleted.getTransferFrom() : null);
                        purchaseInvoiceDetail.setCost(rowDeleted.getCost() != null ? rowDeleted.getCost() : null);
                        invPurchaseInvoiceDetailsDeletedList.add(purchaseInvoiceDetail);
                    }
                }

                invPurchaseReturnSave = invPurchaseInvoiceService.addInvPurchaseInvoice(invPurchaseInvoice, invPurchaseInvoiceDetailsUpdatedList,
                        invPurchaseInvoiceDetailsDeletedList, updatedPurchaseOrAddingOrderList, isPurchaseOrder, headIdList, 1);

                invPurchaseInvoiceEntity.setId(invPurchaseReturnSave.getInvPurchaseInvoice().getId());
                invPurchaseInvoiceEntity.setSerial(invPurchaseReturnSave.getInvPurchaseInvoice().getSerial());
                if (invPurchaseInvoice.getMsg() != null && !"".equals(invPurchaseInvoice.getMsg())) {
                    return errorMessage("يجب ادخال كميةاكبر من الموجودة بالمخزن");
                }
                resetPurchaseInvoice();

                invPurchaseInvoice = invPurchaseReturnSave.getInvPurchaseInvoice();

                setInvPurchaseInvoiceId(invPurchaseInvoice.getId());

                setInvPurchaseInvoiceExist(invPurchaseInvoice);

                invPurchaseInvoiceDetailsList = (List<InvPurchaseInvoiceDetail>) invPurchaseReturnSave.getInvPurchaseInvoiceDetailList();

                invPurchaseInvoiceEntity = mapInvPurchaseInvoiceToInvPurchaseInvoiceEntity(invPurchaseReturnSave.getInvPurchaseInvoice());

                return true;
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "saveReport");

        }
        return false;

    }

    private void errorMessage() {
        try {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ادخال تفاصيل الفاتورة"));
            showMessageDetails = true;
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "errorMessage");

        }

    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../invpurchaseinvoice/invpurchaseinvoicelist.xhtml");
        return "";
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

    public List<InvPurchaseInvoice> getInvPurchaseInvoiceList() {
        return invPurchaseInvoiceList;
    }

    public void setInvPurchaseInvoiceList(List<InvPurchaseInvoice> invPurchaseInvoiceList) {
        this.invPurchaseInvoiceList = invPurchaseInvoiceList;
    }

    public List<InvPurchaseInvoiceDetail> getInvPurchaseInvoiceDetailsList() {
        return invPurchaseInvoiceDetailsList;
    }

    public void setInvPurchaseInvoiceDetailsList(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsList) {
        this.invPurchaseInvoiceDetailsList = invPurchaseInvoiceDetailsList;
    }

    public InvPurchaseInvoiceEntity getInvPurchaseInvoiceEntity() {
        return invPurchaseInvoiceEntity;
    }

    public void setInvPurchaseInvoiceEntity(InvPurchaseInvoiceEntity invPurchaseInvoiceEntity) {
        this.invPurchaseInvoiceEntity = invPurchaseInvoiceEntity;
    }

    public List<InvPurchaseInvoiceEntity> getInvPurchaseInvoiceEntityList() {
        return invPurchaseInvoiceEntityList;
    }

    public void setInvPurchaseInvoiceEntityList(List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList) {
        this.invPurchaseInvoiceEntityList = invPurchaseInvoiceEntityList;
    }

    public List<Symbol> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Symbol> unitList) {
        this.unitList = unitList;
    }

    public List<InvPurchaseOrder> getInvPurchaseOrderList() {
        return invPurchaseOrderList;
    }

    public void setInvPurchaseOrderList(List<InvPurchaseOrder> invPurchaseOrderList) {
        this.invPurchaseOrderList = invPurchaseOrderList;
    }

    public Integer getInvPurchaseInvoiceId() {
        return invPurchaseInvoiceId;
    }

    public void setInvPurchaseInvoiceId(Integer invPurchaseInvoiceId) {
        this.invPurchaseInvoiceId = invPurchaseInvoiceId;
    }

    public InvPurchaseInvoice getInvPurchaseInvoice() {
        return invPurchaseInvoice;
    }

    public void setInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice) {
        this.invPurchaseInvoice = invPurchaseInvoice;
    }

    public InvPurchaseInvoiceDetail getInvPurchaseInvoiceDetail() {
        return invPurchaseInvoiceDetail;
    }

    public void setInvPurchaseInvoiceDetail(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        this.invPurchaseInvoiceDetail = invPurchaseInvoiceDetail;
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

    public InvReturnPurchaseEntity getInvReturnPurchaseEntity() {
        return invReturnPurchaseEntity;
    }

    public void setInvReturnPurchaseEntity(InvReturnPurchaseEntity invReturnPurchaseEntity) {
        this.invReturnPurchaseEntity = invReturnPurchaseEntity;
    }

    public InvReturnPurchaseDetailEntity getInvReturnPurchaseDetailEntity() {
        return invReturnPurchaseDetailEntity;
    }

    public void setInvReturnPurchaseDetailEntity(InvReturnPurchaseDetailEntity invReturnPurchaseDetailEntity) {
        this.invReturnPurchaseDetailEntity = invReturnPurchaseDetailEntity;
    }

    public InvPurchaseInvoiceDetailsEntity getInvPurchaseInvoiceDetailsEntity() {
        return invPurchaseInvoiceDetailsEntity;
    }

    public void setInvPurchaseInvoiceDetailsEntity(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntity) {
        this.invPurchaseInvoiceDetailsEntity = invPurchaseInvoiceDetailsEntity;
    }

    public List<InvPurchaseInvoiceDetailsEntity> getInvPurchaseInvoiceDetailsEntityList() {
        return invPurchaseInvoiceDetailsEntityList;
    }

    public void setInvPurchaseInvoiceDetailsEntityList(List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityList) {
        this.invPurchaseInvoiceDetailsEntityList = invPurchaseInvoiceDetailsEntityList;
    }

    public List<InvReturnPurchaseDetailEntity> getInvReturnPurchaseDetailEntityList() {
        return invReturnPurchaseDetailEntityList;
    }

    public void setInvReturnPurchaseDetailEntityList(List<InvReturnPurchaseDetailEntity> invReturnPurchaseDetailEntityList) {
        this.invReturnPurchaseDetailEntityList = invReturnPurchaseDetailEntityList;
    }

    public List<InvReturnPurchaseEntity> getInvReturnPurchaseEntityList() {
        return invReturnPurchaseEntityList;
    }

    public void setInvReturnPurchaseEntityList(List<InvReturnPurchaseEntity> invReturnPurchaseEntityList) {
        this.invReturnPurchaseEntityList = invReturnPurchaseEntityList;
    }

    public BigDecimal getTotalQuatity() {
        return totalQuatity;
    }

    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
    }

    public InvPurchaseInvoice getInvPurchaseInvoiceExist() {
        return invPurchaseInvoiceExist;
    }

    public void setInvPurchaseInvoiceExist(InvPurchaseInvoice invPurchaseInvoiceExist) {
        this.invPurchaseInvoiceExist = invPurchaseInvoiceExist;
    }

    public InvPurchaseInvoiceDetailsEntity getInvPurchaseInvoiceDetailsEntitySelected() {
        return invPurchaseInvoiceDetailsEntitySelected;
    }

    public void setInvPurchaseInvoiceDetailsEntitySelected(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntitySelected) {
        this.invPurchaseInvoiceDetailsEntitySelected = invPurchaseInvoiceDetailsEntitySelected;
    }

    public List<InvPurchaseInvoiceDetailsEntity> getRowsDeleted() {
        return rowsDeleted;
    }

    public void setRowsDeleted(List<InvPurchaseInvoiceDetailsEntity> rowsDeleted) {
        this.rowsDeleted = rowsDeleted;
    }

    public List<InvPurchaseInvoiceDetail> getInvPurchaseInvoiceDetailsUpdatedList() {
        return invPurchaseInvoiceDetailsUpdatedList;
    }

    public void setInvPurchaseInvoiceDetailsUpdatedList(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsUpdatedList) {
        this.invPurchaseInvoiceDetailsUpdatedList = invPurchaseInvoiceDetailsUpdatedList;
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

    public InvPurchaseInvoiceDetailsEntity getInvPurchaseInvoiceDetailsUpdatedSelected() {
        return invPurchaseInvoiceDetailsUpdatedSelected;
    }

    public void setInvPurchaseInvoiceDetailsUpdatedSelected(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsUpdatedSelected) {
        this.invPurchaseInvoiceDetailsUpdatedSelected = invPurchaseInvoiceDetailsUpdatedSelected;
    }

    public BigDecimal getTotalNet() {
        return totalNet;
    }

    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    public BigDecimal getTotalNetAfterDiscount() {
        return totalNetAfterDiscount;
    }

    public void setTotalNetAfterDiscount(BigDecimal totalNetAfterDiscount) {
        this.totalNetAfterDiscount = totalNetAfterDiscount;
    }

    public SymbolConverter getUnitConverter() {
        return unitConverter;
    }

    public void setUnitConverter(SymbolConverter unitConverter) {
        this.unitConverter = unitConverter;
    }

    public ItemConverter getItemConverter() {
        return itemConverter;
    }

    public void setItemConverter(ItemConverter itemConverter) {
        this.itemConverter = itemConverter;
    }

    public Boolean getShowDialogMessage() {
        return showDialogMessage;
    }

    public void setShowDialogMessage(Boolean showDialogMessage) {
        this.showDialogMessage = showDialogMessage;
    }

    public Boolean getViewDuDate() {
        return viewDuDate;
    }

    public void setViewDuDate(Boolean viewDuDate) {
        this.viewDuDate = viewDuDate;
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

    public Boolean getViewTab() {
        return viewTab;
    }

    public void setViewTab(Boolean viewTab) {
        this.viewTab = viewTab;
    }

    public boolean isPurchaseOrderTabView() {
        return purchaseOrderTabView;
    }

    public void setPurchaseOrderTabView(boolean purchaseOrderTabView) {
        this.purchaseOrderTabView = purchaseOrderTabView;
    }

    public boolean isAddingOrderTabView() {
        return addingOrderTabView;
    }

    public void setAddingOrderTabView(boolean addingOrderTabView) {
        this.addingOrderTabView = addingOrderTabView;
    }

    public BigDecimal getTotalValueAfterDiscount() {
        return totalValueAfterDiscount;
    }

    public void setTotalValueAfterDiscount(BigDecimal totalValueAfterDiscount) {
        this.totalValueAfterDiscount = totalValueAfterDiscount;
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

    public InvDelegatorConvertor getPurchasePersonConvertor() {
        return purchasePersonConvertor;
    }

    public void setPurchasePersonConvertor(InvDelegatorConvertor purchasePersonConvertor) {
        this.purchasePersonConvertor = purchasePersonConvertor;
    }

    public List<InventoryDelegator> getPurchasePersonList() {
        return purchasePersonList;
    }

    public void setPurchasePersonList(List<InventoryDelegator> purchasePersonList) {
        this.purchasePersonList = purchasePersonList;
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

    public List<InvPurchaseInvoiceDetailsEntity> getInvPurchaseInvoiceDetailsEntityReport() {
        return invPurchaseInvoiceDetailsEntityReport;
    }

    public void setInvPurchaseInvoiceDetailsEntityReport(List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityReport) {
        this.invPurchaseInvoiceDetailsEntityReport = invPurchaseInvoiceDetailsEntityReport;
    }

    public BigDecimal getTaxvalue() {
        return taxvalue;
    }

    public void setTaxvalue(BigDecimal taxvalue) {
        this.taxvalue = taxvalue;
    }

    public String getHeadRemarks() {
        return headRemarks;
    }

    public void setHeadRemarks(String headRemarks) {
        this.headRemarks = headRemarks;
    }

    /**
     * @return the totalWithTaxValue
     */
    public BigDecimal getTotalWithTaxValue() {
        return totalWithTaxValue;
    }

    /**
     * @param totalWithTaxValue the totalWithTaxValue to set
     */
    public void setTotalWithTaxValue(BigDecimal totalWithTaxValue) {
        this.totalWithTaxValue = totalWithTaxValue;
    }

    /**
     * @return the totalWeight
     */
    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    /**
     * @param totalWeight the totalWeight to set
     */
    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    /**
     * @return the invTransferDetailsEntityList
     */
    public List<InvTransferDetailsEntity> getInvTransferDetailsEntityList() {
        return invTransferDetailsEntityList;
    }

    /**
     * @param invTransferDetailsEntityList the invTransferDetailsEntityList to
     * set
     */
    public void setInvTransferDetailsEntityList(List<InvTransferDetailsEntity> invTransferDetailsEntityList) {
        this.invTransferDetailsEntityList = invTransferDetailsEntityList;
    }

    /**
     * @return the invPurchaseReturnSave
     */
    public InvPurchaseReturnSave getInvPurchaseReturnSave() {
        return invPurchaseReturnSave;
    }

    /**
     * @param invPurchaseReturnSave the invPurchaseReturnSave to set
     */
    public void setInvPurchaseReturnSave(InvPurchaseReturnSave invPurchaseReturnSave) {
        this.invPurchaseReturnSave = invPurchaseReturnSave;
    }

    /**
     * @return the context
     */
    public ExternalContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(ExternalContext context) {
        this.context = context;
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

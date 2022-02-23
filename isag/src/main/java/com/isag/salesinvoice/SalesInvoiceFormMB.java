/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.salesinvoice;

import com.isag.businessservice.CostCenterService;
import com.isag.businessservice.GlAdminUnitService;
import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.businessservice.InventorySetupService;
import com.isag.businessservice.IsagUserBankService;
import com.isag.businessservice.SymbolService;
import com.isag.converter.CostCenterConverter;
import com.isag.converter.GlBankConverter;
import com.isag.converter.InvDelegatorConvertor;
import com.isag.converter.ItemConverter;
import com.isag.converter.SymbolConverter;
import com.isag.entity.CostCenter;
import com.isag.entity.CurrencyOperation;
import com.isag.entity.GlBank;
import com.isag.entity.InvBarcode;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.InventorySetup;
import com.isag.entity.Symbol;
import com.isag.entiy.InvPurchaseInvoiceDetailsEntity;
import com.isag.entiy.InvPurchaseInvoiceEntity;
import com.isag.entiy.InvReturnPurchaseDetailEntity;
import com.isag.entiy.InvReturnPurchaseEntity;
import com.isag.entiy.SalesInvoiceEntity;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.InvQuotationDetailsService;
import com.isag.businessservice.InvQuotationService;
import com.isag.businessservice.InvReservationDetailService;
import com.isag.businessservice.InvReservationService;
import com.isag.businessservice.InvTransferDetailService;
import com.isag.businessservice.InvTransferService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.QuantityItemsStoreService;
import com.isag.businessservice.ReportManagementService;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.converter.GlAdminUnitConverter;
import com.isag.converter.InvInventoryConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.ItemsBarcodesDetailsViewConverter;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvQotation;
import com.isag.entity.InvQotationDetail;
import com.isag.entity.InvReservation;
import com.isag.entity.InvReservationDetail;
import com.isag.entity.InvTransfer;
import com.isag.entity.InvTransferDetail;
import com.isag.entity.ReportManagement;
import com.isag.invPurchaseInvoice.InvPurchaseInvoiceFormMB;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.views.QuantityItemsStore;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;

/**
 *
 * @author WIN7
 */
@Named(value = "SalesInvoiceFormMB")
@ViewScoped
public class SalesInvoiceFormMB extends BaseFormBean {
    
    private Integer branchId;
    private Integer companyId;
    private Integer invPurchaseInvoiceId;
    
    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private BigDecimal payed = BigDecimal.ZERO;
    private BigDecimal remain = BigDecimal.ZERO;
    private BigDecimal totalWeight = BigDecimal.ZERO;
    
    private BigDecimal totalSum = BigDecimal.ZERO;
    private BigDecimal totalNet = BigDecimal.ZERO;
    private BigDecimal totalNetAfterDiscount = BigDecimal.ZERO;
    private BigDecimal taxvalue = BigDecimal.ZERO;
    private BigDecimal totalWithTaxValue = BigDecimal.ZERO;
    private BigDecimal subtractWeight = BigDecimal.ZERO;
    private BigDecimal multiply = BigDecimal.ZERO;
    private BigDecimal finalNet = BigDecimal.ZERO;
    
    BigDecimal hundred = new BigDecimal(100);
    
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private Boolean showDialogMessage = Boolean.FALSE;
    private boolean invQuotationTabView = Boolean.FALSE;
    private boolean invReservationTabView = Boolean.FALSE;
    private boolean invTransferTabView = Boolean.FALSE;
    boolean toggleSelectedQutaion = Boolean.FALSE;
    boolean toggleSelectedReservation = Boolean.FALSE;
    boolean toggleSelectedTransfer = Boolean.FALSE;
    private Boolean viewDuDate = Boolean.FALSE;
    private Boolean viewTaxCode = Boolean.FALSE;
    
    private Integer isQutation;
    private CurrencyOperation currencyOperation;
    private StringBuilder headIdList = new StringBuilder();

    // DB Entities
    private InvPurchaseInvoice invPurchaseInvoice;
    private InvItem invItem;
    private InvPurchaseInvoice invPurchaseInvoiceExist;
    private List<InvPurchaseInvoice> invPurchaseInvoiceList;
    private InvPurchaseInvoiceDetail invPurchaseInvoiceDetail;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsList;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityReport;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsUpdatedList;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsDeletedList;

    // Interface Entities
    private SalesInvoiceEntity salesInvoiceEntity;
    
    private List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList;
    private InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntity;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityList;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntitySelectedList;
    private InvReturnPurchaseEntity invReturnPurchaseEntity;
    private List<InvReturnPurchaseEntity> invReturnPurchaseEntityList;
    private InvReturnPurchaseDetailEntity invReturnPurchaseDetailEntity;
    private List<InvReturnPurchaseDetailEntity> invReturnPurchaseDetailEntityList;
    private InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntitySelected;
    private InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsUpdatedSelected;
    
    private List<InvPurchaseInvoiceDetailsEntity> rowsDeleted;

    // Lists
    private List<InventoryDelegator> salesPersonsList;
    private List<GlAdminUnit> adminUnitList;
    private List<InvOrganizationSite> customersList;
    private List<CostCenter> costCenterList;
    private List<GlBank> glBankList;
//    private List<InvItem> invItemList;
    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;
    private List<Symbol> unitList;
    private List<InvInventory> invInventoryList;
    
    Map<Integer, BigDecimal> itemsMap;
    Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap;

//    Map<Integer, List<Symbol>> unitsMap;
    private List<InvBarcode> invBarcodesList;

    // Converters
    private GlBankConverter glBankConverter;
    private ItemConverter itemConverter;
    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;
    private CostCenterConverter costCenterConverter;
    private SymbolConverter unitConverter;
    private InvInventoryConverter invInventoryConverter;
    private InvDelegatorConvertor salesConverter;
    private InvOrganizationSiteConverter customerConvertor;
    private GlAdminUnitConverter adminUnitConverter;
    private Integer settingType;
    
    InventorySetup invSetup;

    // Loaded Lists
    private List<InvQotation> invQutationList = new ArrayList<>();
    private List<InvQotation> invQutationSelectedList = new ArrayList<>();
    
    private List<InvTransfer> invTransferList = new ArrayList<>();
    private List<InvTransfer> invTransferSelectedList = new ArrayList<>();
    
    private List<InvReservation> invReservationList = new ArrayList<>();
    private List<InvReservation> invReservationSelectedList = new ArrayList<>();
    
    private List<Integer> updatedPurchaseOrAddingOrderList;
    
    private ExternalContext context;
    
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    InventorySetupService inventorySetupService;
    @EJB
    private GlAdminUnitService glAdminUnitService;
    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private InvTransferService invTransferService;
    @EJB
    private InvTransferDetailService invTransferDetailService;
    @EJB
    IsagUserBankService isagUserBankService;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    private CostCenterService costCenterService;
    
    @EJB
    private SymbolService symbolService;
    @EJB
    private QuantityItemsStoreService quantityItemsStoreService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    private InvReservationService invReservationService;
    @EJB
    private InvQuotationService invQuotationService;
    @EJB
    private InvReservationDetailService invReservationDetailService;
    @EJB
    private InvQuotationDetailsService invQuotationDetailsService;
    @EJB
    private ReportManagementService reportManagementService;
    
    @Override
    @PostConstruct
    public void init() {
        try {
            invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoiceList = new ArrayList<>();
            invPurchaseInvoiceDetail = new InvPurchaseInvoiceDetail();
            invPurchaseInvoiceDetailsList = new ArrayList<>();
            salesInvoiceEntity = new SalesInvoiceEntity();
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
            salesPersonsList = new ArrayList<>();
            costCenterList = new ArrayList<>();
            glBankList = new ArrayList<>();
            invBarcodesList = new ArrayList<>();
            unitList = new ArrayList<>();
            invInventoryList = new ArrayList<>();
            itemsBarcodesDetailsViewList = new ArrayList<>();
            itemsMap = new HashMap<>();
            itemsBarcodeMap = new HashMap<>();
            invSetup = new InventorySetup();
            customersList = new ArrayList<>();
            adminUnitList = new ArrayList<>();
            
            load();
            
            showDuDate();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "init");
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

//        unitsMap = invBarcodeSevice.getAllUnitsForAllInvItemsByBranch(branchId);
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetInvPurchaseInvoiceForm();
                
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                
                setInvPurchaseInvoiceId((Integer) context.getSessionMap().get("salesinvoiceIdSeclected"));
                invPurchaseInvoice = invPurchaseInvoiceService.findInvPurchaseInvoiceById(getInvPurchaseInvoiceId());
                
                setInvPurchaseInvoiceExist(invPurchaseInvoice);
                
                invPurchaseInvoiceDetailsList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(getInvPurchaseInvoiceId());
                
                salesInvoiceEntity = mapInvPurchaseInvoiceToInvPurchaseInvoiceEntity(invPurchaseInvoice);
            }
            
            fillItemMap();
            
            updateTabView();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "load");
        }
    }
    
    public void updateTabView() {
        try {
            if (isQutation == null) {
                
                invQuotationTabView = invReservationTabView = invTransferTabView = Boolean.TRUE;
            } else if (isQutation == 2) {
                onRowQutaionSelect();
            } else if (isQutation == 3) {
                onRowReservationSelect();
            } else if (isQutation == 4) {
                onRowTransferSelect();
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "updateTabView");
        }
    }
    
    public void reset() {
        try {
            invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoiceDetailsUpdatedList = new ArrayList<>();
            invPurchaseInvoiceDetailsDeletedList = new ArrayList<>();
            updatedPurchaseOrAddingOrderList = new ArrayList<>();
            
            invPurchaseInvoiceExist = new InvPurchaseInvoice();
            headIdList = new StringBuilder();
            
            salesInvoiceEntity.setSerial(null);
            salesInvoiceEntity.setSerialTax(null);
            salesInvoiceEntity.setId(null);
            
            invPurchaseInvoiceDetailsEntityList = new ArrayList<>();
            
            salesInvoiceEntity.setActualWeight(BigDecimal.ZERO);
            salesInvoiceEntity.setPriceKilo(BigDecimal.ZERO);
            salesInvoiceEntity.setTotalActualWeight(BigDecimal.ZERO);
            salesInvoiceEntity.setHeadDiscount(BigDecimal.ZERO);
            
            totalNetAfterDiscount = BigDecimal.ZERO;
            taxvalue = BigDecimal.ZERO;
            totalWithTaxValue = BigDecimal.ZERO;
            payed = BigDecimal.ZERO;
            remain = BigDecimal.ZERO;
            
            totalNet = BigDecimal.ZERO;
            totalSum = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            totalWeight = BigDecimal.ZERO;
            multiply = BigDecimal.ZERO;
            finalNet = BigDecimal.ZERO;
//            updateTaxDiscValue();
            showDuDate();
            
            updateTabView();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "reset");
        }
    }
    
    private void fillLists() {
        try {
            customersList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 0); // العملاء
            adminUnitList = glAdminUnitService.getAllSubAdminUnitByBranchIdActive(branchId); // الوحدات الادارية
            adminUnitConverter = new GlAdminUnitConverter(adminUnitList);
            customerConvertor = new InvOrganizationSiteConverter(customersList);
            
            salesPersonsList = invDelegatorService.getSalesByBranch(branchId); // مندوبين المبيعات
            salesConverter = new InvDelegatorConvertor(salesPersonsList);
            
            costCenterList = costCenterService.getActiveSubCostCentersByBranch(branchId); // مراكز التكلفة
            costCenterConverter = new CostCenterConverter(costCenterList);
            
            glBankList = isagUserBankService.getAllGlBankForUserByTypeAndBranchIdPer(getUserData().getUser().getId(), getUserData().getUserBranch().getId(), 0);
            glBankConverter = new GlBankConverter(glBankList);

//        invItemList = invItemService.getInvItemByBranchId(branchId); // الاصناف
//        itemConverter = new ItemConverter(invItemList);
            itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViewList);
            
            for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
                itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
            }
            
            unitList = symbolService.getUnitsByCompanyId(companyId); // الوحدات: جرام, طن
            unitConverter = new SymbolConverter(unitList);
            
            invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId);
            
            invInventoryConverter = new InvInventoryConverter(invInventoryList);
            invSetup = inventorySetupService.getInventoryByBranchId(branchId);
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "fillLists");
        }
    }
    
    public void loadSalesInvoice2() {
        salesInvoiceEntity.setRemarks("yarab");
        CloseDlg("dlg2");
    }
    
    private SalesInvoiceEntity mapInvPurchaseInvoiceToInvPurchaseInvoiceEntity(InvPurchaseInvoice invPurchaseInvoice) {
        try {
            
            salesInvoiceEntity = new SalesInvoiceEntity();
            
            salesInvoiceEntity.setId(invPurchaseInvoice.getId() != null ? invPurchaseInvoice.getId() : null);
            salesInvoiceEntity.setBranchId(invPurchaseInvoice.getBranchId() != null ? invPurchaseInvoice.getBranchId().getId() : null);
            salesInvoiceEntity.setCostCenterId(invPurchaseInvoice.getCostCenterId() != null ? invPurchaseInvoice.getCostCenterId() : null);
            salesInvoiceEntity.setDate(invPurchaseInvoice.getDate() != null ? invPurchaseInvoice.getDate() : new Date());
            salesInvoiceEntity.setRemarks(invPurchaseInvoice.getRemarks() != null ? invPurchaseInvoice.getRemarks() : null);
            salesInvoiceEntity.setHeadDiscount(invPurchaseInvoice.getDiscount() != null ? invPurchaseInvoice.getDiscount() : BigDecimal.ZERO);
            salesInvoiceEntity.setDiscountType(invPurchaseInvoice.getDiscountType() != null ? invPurchaseInvoice.getDiscountType() : 0);
            salesInvoiceEntity.setGlBank(invPurchaseInvoice.getGlBankId() != null ? invPurchaseInvoice.getGlBankId() : null);
            salesInvoiceEntity.setSalesPerson(invPurchaseInvoice.getInvDelegatorId() != null ? invPurchaseInvoice.getInvDelegatorId() : null);
            salesInvoiceEntity.setCustomer(invPurchaseInvoice.getOrganizationSiteId());
            salesInvoiceEntity.setPaymentType(invPurchaseInvoice.getPaymentType() > 0 ? invPurchaseInvoice.getPaymentType() : 0);
            salesInvoiceEntity.setSerial(invPurchaseInvoice.getSerial());
            salesInvoiceEntity.setSerialTax(invPurchaseInvoice.getSerialTax());
            salesInvoiceEntity.setTaxdiscflag(invPurchaseInvoice.getTaxdiscflag());
            salesInvoiceEntity.setTaxdiscvalue(invPurchaseInvoice.getTaxdiscvalue());
            salesInvoiceEntity.setTaxFlag(invPurchaseInvoice.getTaxflag());
            setViewTaxCode(invPurchaseInvoice.getTaxFlagFinal());
            salesInvoiceEntity.setTaxFlagFinal(viewTaxCode);
            salesInvoiceEntity.setInvInventory(invPurchaseInvoice.getInvInventoryId() != null ? invPurchaseInvoice.getInvInventoryId() : null);
            salesInvoiceEntity.setActualWeight(invPurchaseInvoice.getActualWeight() != null ? invPurchaseInvoice.getActualWeight() : BigDecimal.ZERO);
            salesInvoiceEntity.setTotalActualWeight(invPurchaseInvoice.getTotalActualWeight() != null ? invPurchaseInvoice.getTotalActualWeight() : BigDecimal.ZERO);
            salesInvoiceEntity.setPriceKilo(invPurchaseInvoice.getPriceKilo() != null ? invPurchaseInvoice.getPriceKilo() : BigDecimal.ZERO);
            BigDecimal quantity, weight, discount, cost;
            totalNet = BigDecimal.ZERO;
            totalSum = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            totalWeight = BigDecimal.ZERO;
            multiply = BigDecimal.ZERO;
            finalNet = BigDecimal.ZERO;
            int i = 0;
            //   updateTaxDiscValue();
            invPurchaseInvoiceDetailsEntityList = new ArrayList<>();
            for (InvPurchaseInvoiceDetail purchaseInvoiceDetail : invPurchaseInvoiceDetailsList) {
                System.err.println(i++);
                invPurchaseInvoiceDetailsEntity = new InvPurchaseInvoiceDetailsEntity();
                
                invPurchaseInvoiceDetailsEntity.setId(purchaseInvoiceDetail.getId() != null ? purchaseInvoiceDetail.getId() : null);
                invPurchaseInvoiceDetailsEntity.setBranchId(purchaseInvoiceDetail.getBranchId() != null ? purchaseInvoiceDetail.getBranchId().getId() : null);
                invPurchaseInvoiceDetailsEntity.setCost(purchaseInvoiceDetail.getCost() != null ? purchaseInvoiceDetail.getCost() : null);
                invPurchaseInvoiceDetailsEntity.setDiscount(purchaseInvoiceDetail.getDiscount() != null ? purchaseInvoiceDetail.getDiscount() : null);
                invPurchaseInvoiceDetailsEntity.setDiscountType(purchaseInvoiceDetail.getDiscountType() != null ? purchaseInvoiceDetail.getDiscountType() : null);
                invPurchaseInvoiceDetailsEntity.setInvPurchaseInvoiceId(purchaseInvoiceDetail.getInvPurchaseInvoiceId() != null ? purchaseInvoiceDetail.getInvPurchaseInvoiceId().getId() : null);
                invPurchaseInvoiceDetailsEntity.setTransferFrom(purchaseInvoiceDetail.getTransferFrom() != null ? purchaseInvoiceDetail.getTransferFrom() : 0);
                invPurchaseInvoiceDetailsEntity.setSerial(purchaseInvoiceDetail.getSerial());
                
                if (invPurchaseInvoiceDetailsEntity.getTransferFrom() != null) {
                    isQutation = purchaseInvoiceDetail.getTransferFrom();
                }
                
                if (purchaseInvoiceDetail.getItemId() != null) {
                    invPurchaseInvoiceDetailsEntity.setInvItem(purchaseInvoiceDetail.getItemId());
                    
                }
                
                if (purchaseInvoiceDetail.getItemBarcode() != null && !"".equals(purchaseInvoiceDetail.getItemBarcode())) {
                    invPurchaseInvoiceDetailsEntity.setItemsBarcodesDetail(itemsBarcodeMap.get(purchaseInvoiceDetail.getItemBarcode()));
                    invPurchaseInvoiceDetailsEntity.setItemsBarcodesDetailTrans(invPurchaseInvoiceDetailsEntity.getItemsBarcodesDetail());
                    invPurchaseInvoiceDetailsEntity.setScrewing(invPurchaseInvoiceDetailsEntity.getItemsBarcodesDetail().getScrewing());
                    invPurchaseInvoiceDetailsEntity.setPaintColor(invPurchaseInvoiceDetailsEntity.getPaintColor());
                    invPurchaseInvoiceDetailsEntity.setLength(invPurchaseInvoiceDetailsEntity.getLength());
                }

//            invPurchaseInvoiceDetailsEntity.setUnits(unitsMap.get(invPurchaseInvoiceDetailsEntity.getInvItem().getId()));
//            unitConverter = new SymbolConverter(invPurchaseInvoiceDetailsEntity.getUnits());
                invPurchaseInvoiceDetailsEntity.setQuantity(purchaseInvoiceDetail.getQuantity() != null ? purchaseInvoiceDetail.getQuantity() : null);
                invPurchaseInvoiceDetailsEntity.setWeight(purchaseInvoiceDetail.getWeight() != null ? purchaseInvoiceDetail.getWeight() : BigDecimal.ZERO);
                invPurchaseInvoiceDetailsEntity.setUnit(purchaseInvoiceDetail.getUnitId() != null ? purchaseInvoiceDetail.getUnitId() : null);
                invPurchaseInvoiceDetailsEntity.setMarkEdit(Boolean.FALSE);
                
                totalQuatity = totalQuatity.add(purchaseInvoiceDetail.getQuantity() != null ? purchaseInvoiceDetail.getQuantity() : BigDecimal.ZERO);
                
                quantity = purchaseInvoiceDetail.getQuantity() != null ? purchaseInvoiceDetail.getQuantity() : BigDecimal.ZERO;
                weight = purchaseInvoiceDetail.getWeight() != null ? purchaseInvoiceDetail.getWeight() : BigDecimal.ZERO;
                if (weight != null && weight.compareTo(BigDecimal.ZERO) > 0) {
                    if (quantity != null) {
//                        setTotalWeight(getTotalWeight().add(weight.multiply(quantity)));
                        setTotalWeight(getTotalWeight().add(weight));
                    } else {
                        setTotalWeight(getTotalWeight().add(weight));
                    }
                } else {
                    setTotalWeight(getTotalWeight().add(BigDecimal.ZERO));
                }
//            totalWeight = getTotalWeight().add(weight != null ? weight : BigDecimal.ZERO);
                cost = purchaseInvoiceDetail.getCost() != null ? purchaseInvoiceDetail.getCost() : BigDecimal.ZERO;
                
                discount = purchaseInvoiceDetail.getDiscount() != null ? purchaseInvoiceDetail.getDiscount() : BigDecimal.ZERO;
                //---------------------------------------------------------------
                invPurchaseInvoiceDetailsEntity.setTotal((quantity.multiply(cost)));
                invPurchaseInvoiceDetailsEntity.setNet(purchaseInvoiceDetail.getNet());
                totalNet = totalNet.add(invPurchaseInvoiceDetailsEntity.getNet() != null ? invPurchaseInvoiceDetailsEntity.getNet() : BigDecimal.ZERO);
                
                salesInvoiceEntity.setTotalActualWeight(finalNet);
                totalSum = totalSum.add(invPurchaseInvoiceDetailsEntity.getTotal() != null ? invPurchaseInvoiceDetailsEntity.getTotal() : BigDecimal.ZERO);
                
                if (purchaseInvoiceDetail != null && purchaseInvoiceDetail.getSelectedId() != null) {
                    if (headIdList == null || headIdList.length() == 0) {
                        headIdList.append(purchaseInvoiceDetail.getSelectedId());
                    } else {
                        headIdList.append(",").append(purchaseInvoiceDetail.getSelectedId());
                    }
                }
                invPurchaseInvoiceDetailsEntityList.add(invPurchaseInvoiceDetailsEntity);
                
                BigDecimal dbQut = itemsMap.get(invPurchaseInvoiceDetailsEntity.getItemsBarcodesDetail().getItemId());
                if (dbQut != null) {
                    itemsMap.put(invPurchaseInvoiceDetailsEntity.getItemsBarcodesDetail().getItemId(), dbQut.add(quantity));
                } else {
                    itemsMap.put(invPurchaseInvoiceDetailsEntity.getItemsBarcodesDetail().getItemId(), quantity);
                }
            }
            
            calculateactualweight();
            //  calculateTotalNetAfterDiscount();

            return salesInvoiceEntity;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "mapInvPurchaseInvoiceToInvPurchaseInvoiceEntity");
            return null;
        }
    }
    
    public void calculateRemainValue() {
        try {
            remain = payed.subtract(totalWithTaxValue);
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "calculateRemainValue");
        }
    }
    
    public void calculateactualweight() {
        try {
            subtractWeight = (salesInvoiceEntity.getActualWeight() != null ? salesInvoiceEntity.getActualWeight() : BigDecimal.ZERO).subtract(totalWeight == null ? BigDecimal.ZERO : totalWeight);
            multiply = subtractWeight.multiply(salesInvoiceEntity.getPriceKilo() != null ? salesInvoiceEntity.getPriceKilo() : BigDecimal.ZERO);
            finalNet = totalNet.add(multiply);
            salesInvoiceEntity.setTotalActualWeight(finalNet);
            calculateTotalNetAfterDiscount();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "calculateactualweight");
        }
        
    }
    
    public void calculateTotalNetAfterDiscount() {
        try {
            totalNetAfterDiscount = BigDecimal.ZERO;
            if (invPurchaseInvoiceDetailsEntityList != null && !invPurchaseInvoiceDetailsEntityList.isEmpty()) {
                if (salesInvoiceEntity.getDiscountType() != null && salesInvoiceEntity.getDiscountType().equals(0)) {
                    if (salesInvoiceEntity != null && finalNet != null
                            && salesInvoiceEntity.getHeadDiscount() != null
                            && finalNet.compareTo(BigDecimal.ZERO) == 1
                            && (finalNet.compareTo(salesInvoiceEntity.getHeadDiscount()) == 0
                            || finalNet.compareTo(salesInvoiceEntity.getHeadDiscount()) == 1)) {
                        totalNetAfterDiscount = (finalNet.subtract(salesInvoiceEntity.getHeadDiscount() != null ? salesInvoiceEntity.getHeadDiscount() : BigDecimal.ZERO)).setScale(3);
                    } else {
                        salesInvoiceEntity.setHeadDiscount(BigDecimal.ZERO);
                        totalNetAfterDiscount = finalNet.subtract(BigDecimal.ZERO);
                    }
                } else {
                    if (isDiscountValid(salesInvoiceEntity.getHeadDiscount())) {
                        totalNetAfterDiscount = finalNet.subtract(finalNet.multiply(salesInvoiceEntity.getHeadDiscount()).divide(hundred)).setScale(3);
                    } else {
                        salesInvoiceEntity.setHeadDiscount(BigDecimal.ZERO);
                        totalNetAfterDiscount = finalNet.subtract(BigDecimal.ZERO);
                    }
                }
                updateTax();
                //  updateTaxDiscValue();
            } else {
                removeTaxValue();
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "calculateTotalNetAfterDiscount");
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
            saveError(e, "SalesInvoiceFormMB", "isDiscountValid");
            return false;
        }
    }
    
    public void fillItemMap() {
        try {
            if (invSetup != null && invSetup.getNegativeSell() == false && salesInvoiceEntity.getInvInventory() != null) {
                List<QuantityItemsStore> quantityItemsStoresList = quantityItemsStoreService.findInventoryItemsList(salesInvoiceEntity.getInvInventory().getId(), branchId, invSetup.getSellBuy());
                
                for (QuantityItemsStore quantityItemsStore : quantityItemsStoresList) {
                    itemsMap.put(quantityItemsStore.getItemId(), quantityItemsStore.getQty());
                }
                completeFillMap();
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "fillItemMap");
        }
    }
    
    public void completeFillMap() {
        try {
            if (invPurchaseInvoiceDetailsEntityList != null && !invPurchaseInvoiceDetailsEntityList.isEmpty()) {
                for (InvPurchaseInvoiceDetailsEntity item : invPurchaseInvoiceDetailsEntityList) {
                    BigDecimal dbQut = itemsMap.get(item.getItemsBarcodesDetail().getItemId());
                    if (dbQut != null) {
                        itemsMap.put(item.getItemsBarcodesDetail().getItemId(), dbQut.add(item.getQuantity()));
                    } else {
                        itemsMap.put(item.getItemsBarcodesDetail().getItemId(), item.getQuantity());
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "completeFillMap");
        }
        
    }
    
    public void showDuDate() {
        try {
            if (salesInvoiceEntity.getPaymentType() == 0) {
                salesInvoiceEntity.setGlBank(glBankList.get(0));
                viewDuDate = false;
            } else {
                salesInvoiceEntity.setGlBank(null);
                viewDuDate = true;
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "showDuDate");
        }
    }
    
    public void loadInvPurchaseOrder() {
        try {
            if ((invQutationSelectedList == null || invQutationSelectedList.isEmpty())
                    && (invReservationSelectedList == null || invReservationSelectedList.isEmpty())
                    && (invTransferSelectedList == null || invTransferSelectedList.isEmpty())) {
                errorMessage("يجب اختيار حجز بضاعة أو حجز سعر أو تحويل بين المخازن");
                OpenDlg("dlg2");
            } else {
                
                for (InvPurchaseInvoiceDetailsEntity entity : invPurchaseInvoiceDetailsEntityList) {
                    if (entity.getId() != null) {
                        rowsDeleted.add(entity);
                    }
                }
                
                invPurchaseInvoiceDetailsEntityList = new ArrayList<>();
                updatedPurchaseOrAddingOrderList = new ArrayList<>();
                
                if (invQutationSelectedList != null && !invQutationSelectedList.isEmpty()) {
                    InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceEntitySelected;
                    List<InvQotationDetail> qotationDetailList;
                    
                    for (InvQotation qotation : invQutationSelectedList) {
                        
                        updatedPurchaseOrAddingOrderList.add(qotation.getId());
                        isQutation = 2;
                        loadHeadData(qotation, null, null);
                        qotationDetailList = invQuotationDetailsService.getAllInvQotationDetailsByIInvQotationIdWithStatus(qotation.getId());
                        for (InvQotationDetail qotationDetail : qotationDetailList) {
                            invPurchaseInvoiceEntitySelected = new InvPurchaseInvoiceDetailsEntity();
                            
                            if (qotationDetail.getItemId() != null) {
                                invPurchaseInvoiceEntitySelected.setItemsBarcodesDetail(itemsBarcodeMap.get(qotationDetail.getItemId().getCode()));
                                invPurchaseInvoiceEntitySelected.setItemsBarcodesDetailTrans(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail());
                                Symbol unit = new Symbol();
                                unit.setId(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail().getUnitId());
                                unit.setName(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail().getUnitName());
                                invPurchaseInvoiceEntitySelected.setUnit(unit);
                            }
//                        invPurchaseInvoiceEntitySelected.setInvItem(qotationDetail.getItemId() != null ? qotationDetail.getItemId() : null);
                            invPurchaseInvoiceEntitySelected.setCost(qotationDetail.getPrice() != null ? qotationDetail.getPrice() : null);
                            invPurchaseInvoiceEntitySelected.setQuantity(qotationDetail.getFinalQuantity() != null ? qotationDetail.getFinalQuantity() : null);
                            invPurchaseInvoiceEntitySelected.setTransferFrom(2);
                            invPurchaseInvoiceEntitySelected.setSelectedId(qotationDetail.getId());
                            invPurchaseInvoiceEntitySelected.setQuantityDisabled(Boolean.FALSE);
                            invPurchaseInvoiceEntitySelected.setDiscount(qotationDetail.getDiscount());
                            invPurchaseInvoiceEntitySelected.setWeight(qotationDetail.getWeight());
                            invPurchaseInvoiceDetailsEntityList.add(invPurchaseInvoiceEntitySelected);
                        }
                    }
                    CloseDlg("dlg2");
                    
                    updateSummition();
                    
                    return;
                }
                
                if (invReservationSelectedList != null && !invReservationSelectedList.isEmpty()) {
                    InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceEntitySelected;
                    List<InvReservationDetail> reservationDetailList;
                    invPurchaseInvoiceDetailsEntityList = new ArrayList<>();
                    
                    for (InvReservation reservation : invReservationSelectedList) {
                        
                        updatedPurchaseOrAddingOrderList.add(reservation.getId());
                        isQutation = 3;
                        loadHeadData(null, reservation, null);
                        reservationDetailList = invReservationDetailService.getAllInvReservationDetailsByInvReservationIdWithStatus(reservation.getId());
                        for (InvReservationDetail reservationDetail : reservationDetailList) {
                            invPurchaseInvoiceEntitySelected = new InvPurchaseInvoiceDetailsEntity();
                            
                            if (reservationDetail.getItemId() != null) {
                                invPurchaseInvoiceEntitySelected.setItemsBarcodesDetail(itemsBarcodeMap.get(reservationDetail.getItemId().getCode()));
                                invPurchaseInvoiceEntitySelected.setItemsBarcodesDetailTrans(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail());
                                Symbol unit = new Symbol();
                                unit.setId(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail().getUnitId());
                                unit.setName(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail().getUnitName());
                                invPurchaseInvoiceEntitySelected.setUnit(unit);
                            }

//                        invPurchaseInvoiceEntitySelected.setInvItem(reservationDetail.getItemId() != null ? reservationDetail.getItemId() : null);
                            invPurchaseInvoiceEntitySelected.setQuantity(reservationDetail.getFinalQuantity() != null ? reservationDetail.getFinalQuantity() : null);
                            
                            invPurchaseInvoiceEntitySelected.setSelectedId(reservationDetail.getId());
                            invPurchaseInvoiceEntitySelected.setTransferFrom(3);
                            invPurchaseInvoiceEntitySelected.setQuantityDisabled(Boolean.TRUE);
                            invPurchaseInvoiceDetailsEntityList.add(invPurchaseInvoiceEntitySelected);
                        }
                    }
                    CloseDlg("dlg2");
                    updateSummition();
                }
                if (invTransferSelectedList != null && !invTransferSelectedList.isEmpty()) {
                    InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceEntitySelected;
                    List<InvTransferDetail> invTransferDetList;
                    invPurchaseInvoiceDetailsEntityList = new ArrayList<>();
                    
                    for (InvTransfer transfer : invTransferSelectedList) {
                        
                        updatedPurchaseOrAddingOrderList.add(transfer.getId());
                        isQutation = 4;
                        loadHeadData(null, null, transfer);
                        invTransferDetList = invTransferDetailService.getAllInvTransferDetailsByInvTransferIdWithStatus(transfer.getId());
                        for (InvTransferDetail transferDetail : invTransferDetList) {
                            invPurchaseInvoiceEntitySelected = new InvPurchaseInvoiceDetailsEntity();
                            
                            if (transferDetail.getInvItemId() != null) {
                                invPurchaseInvoiceEntitySelected.setItemsBarcodesDetail(itemsBarcodeMap.get(transferDetail.getInvItemId().getCode()));
                                invPurchaseInvoiceEntitySelected.setItemsBarcodesDetailTrans(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail());
                                Symbol unit = new Symbol();
                                unit.setId(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail().getUnitId());
                                unit.setName(invPurchaseInvoiceEntitySelected.getItemsBarcodesDetail().getUnitName());
                                invPurchaseInvoiceEntitySelected.setUnit(unit);
                            }

//                        invPurchaseInvoiceEntitySelected.setInvItem(reservationDetail.getItemId() != null ? reservationDetail.getItemId() : null);
                            invPurchaseInvoiceEntitySelected.setQuantity(transferDetail.getFinalQuantity() != null ? transferDetail.getFinalQuantity() : null);
                            
                            invPurchaseInvoiceEntitySelected.setSelectedId(transferDetail.getId());
                            invPurchaseInvoiceEntitySelected.setTransferFrom(4);
                            invPurchaseInvoiceEntitySelected.setQuantityDisabled(Boolean.TRUE);
                            invPurchaseInvoiceDetailsEntityList.add(invPurchaseInvoiceEntitySelected);
                        }
                    }
                    CloseDlg("dlg2");
                    updateSummition();
                }
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "loadInvPurchaseOrder");
        }
        
    }
    
    private void loadHeadData(InvQotation invQotation, InvReservation invReservation, InvTransfer invTransfer) {
        try {
            
            if (invReservation != null) {
                salesInvoiceEntity.setSalesPerson(invReservation.getDelegatorId());
                salesInvoiceEntity.setInvInventory(invReservation.getInvId());
                salesInvoiceEntity.setRemarks(invReservation.getRemarks());
            }
            
            if (invQotation != null) {
                
                salesInvoiceEntity.setRemarks(invQotation.getRemark());
                salesInvoiceEntity.setCustomer(invQotation.getCustomerId());
                salesInvoiceEntity.setSalesPerson(invQotation.getDelegatorId());
                int DiscountType;
                if (invQotation.getDiscountType()) {
                    DiscountType = 1;
                } else {
                    DiscountType = 0;
                }
                salesInvoiceEntity.setDiscountType(DiscountType);
                salesInvoiceEntity.setHeadDiscount(invQotation.getDiscount());
            }
            
            if (invTransfer != null) {
                salesInvoiceEntity.setInvInventory(invTransfer.getInvTo());
                salesInvoiceEntity.setRemarks(invTransfer.getRemarks());
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "loadHeadData");
        }
        
    }
    
    public void loadDialog() {
        try {
            invQutationList = invQuotationService.getALLInvQotationByBranchIdByStatus(branchId);
//        invReservationList = invReservationService.getALLInvReservationsByBranchIdByStatus(branchId);
//        invTransferList = invTransferService.getALLInvTransferByBranchIdAndStatusandType(branchId);

//        if (invQutationSelectedList != null && !invQutationSelectedList.isEmpty()
//                && invReservationSelectedList != null && !invReservationSelectedList.isEmpty()
//                && invTransferSelectedList != null && !invTransferSelectedList.isEmpty()) {
//
//            onRowQutaionSelect();
//            invReservationSelectedList = new ArrayList<>();
//            invTransferSelectedList = new ArrayList<>();
//        }
            OpenDlg("dlg2");
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "loadDialog");
        }
        
    }
    
    public void OpenDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "OpenDlg");
        }
    }
    
    public void CloseDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "CloseDlg");
        }
    }
    
    public void toggleSelectInvQutaion() {
        try {
            if (!toggleSelectedQutaion) {
                toggleSelectedQutaion = true;
                onRowQutaionSelect();
            } else {
                toggleSelectedQutaion = false;
                unSelectRow();
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "toggleSelectInvQutaion");
        }
    }
    
    public void toggleSelectInvTransfer() {
        try {
            if (!toggleSelectedTransfer) {
                toggleSelectedTransfer = true;
                onRowTransferSelect();
            } else {
                toggleSelectedTransfer = false;
                unSelectRow();
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "toggleSelectInvTransfer");
        }
    }
    
    public void toggleSelectReservation() {
        try {
            if (!toggleSelectedReservation) {
                toggleSelectedReservation = true;
                onRowReservationSelect();
            } else {
                toggleSelectedReservation = false;
                unSelectRow();
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "toggleSelectReservation");
        }
    }
    
    public void onRowQutaionSelect() {
        try {
            invQuotationTabView = true;
            invReservationTabView = false;
            invTransferTabView = false;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "onRowQutaionSelect");
        }
    }
    
    public void onRowReservationSelect() {
        try {
            invQuotationTabView = false;
            invReservationTabView = true;
            invTransferTabView = false;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "onRowReservationSelect");
        }
    }
    
    public void onRowTransferSelect() {
        try {
            invQuotationTabView = false;
            invReservationTabView = false;
            invTransferTabView = true;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "onRowTransferSelect");
        }
    }
    
    public void unSelectRow() {
        try {
            if ((invQutationSelectedList == null || invQutationSelectedList.isEmpty())
                    && (invReservationSelectedList == null || invReservationSelectedList.isEmpty())
                    && (invTransferSelectedList == null || invTransferSelectedList.isEmpty())) {
                invQuotationTabView = invReservationTabView = invTransferTabView = true;
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "unSelectRow");
        }
    }
    
    private void resetInvPurchaseInvoiceForm() {
        try {
            salesInvoiceEntity = new SalesInvoiceEntity();
            salesInvoiceEntity.setPaymentType(0);
            salesInvoiceEntity.setDate(new Date());
            salesInvoiceEntity.setDiscountType(0);
            salesInvoiceEntity.setHeadDiscount(BigDecimal.ZERO);
            salesInvoiceEntity.setTaxFlag(invSetup.getDefaultSalesTax());
            
            salesInvoiceEntity.setTaxFlagFinal(Boolean.FALSE);
            setViewTaxCode(Boolean.FALSE);
            if (invInventoryList != null && !invInventoryList.isEmpty()) {
                InvInventory invInventorySelection = (InvInventory) getContext().getSessionMap().get("defaultInventory");
                if (invInventorySelection == null) {
                    salesInvoiceEntity.setInvInventory(invInventoryList.get(0));
                } else {
                    salesInvoiceEntity.setInvInventory(invInventorySelection);
                }
            } else {
                errorMessage("لا توجد مخازن");
            }
            
            if (glBankList != null && !glBankList.isEmpty()) {
                salesInvoiceEntity.setGlBank(glBankList.get(0));
            } else {
                errorMessage("لا توجد خزائن");
            }
            
            if (customersList != null && !customersList.isEmpty()) {
                salesInvoiceEntity.setCustomer(customersList.get(0));
            } else {
                errorMessage("لا يوجد عملاء");
                
            }
            
            if (salesPersonsList != null && !salesPersonsList.isEmpty()) {
                salesInvoiceEntity.setSalesPerson(salesPersonsList.get(0));
            } else {
                errorMessage("لا يوجد مندوبين مبيعات");
            }
            
            invPurchaseInvoiceEntityList = new ArrayList<>();
            invPurchaseInvoiceDetailsEntityList = new ArrayList<>();
            invReturnPurchaseDetailEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "resetInvPurchaseInvoiceForm");
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
                    
                    errorMessage("يجب ادخال تفاصيل الفاتورة");
                    return;
                }
                detailEntity.setMarkEdit(Boolean.FALSE);
            }
            
            InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntityNew = new InvPurchaseInvoiceDetailsEntity();
            invPurchaseInvoiceDetailsEntityNew.setMarkEdit(Boolean.TRUE);
            invPurchaseInvoiceDetailsUpdatedSelected = invPurchaseInvoiceDetailsEntityNew;
            invPurchaseInvoiceDetailsEntityList.add(invPurchaseInvoiceDetailsEntityNew);
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "addInvPurchaseInvoiceDetail");
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
//        totalSum = totalSum.subtract(invPurchaseInvoiceDetailsEntitySelected.getTotal() != null ? invPurchaseInvoiceDetailsEntitySelected.getTotal() : BigDecimal.ZERO);
//        calculateTotalNetAfterDiscount();
            for (InvPurchaseInvoiceDetailsEntity detailEntity : invPurchaseInvoiceDetailsEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
            }
            invPurchaseInvoiceDetailsEntityList.remove(invPurchaseInvoiceDetailsEntitySelected);
            updateSummition();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
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
            saveError(e, "SalesInvoiceFormMB", "completeSalesPerson");
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
            saveError(e, "SalesInvoiceFormMB", "completeAdminUnits");
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
            saveError(e, "SalesInvoiceFormMB", "completeGlBank");
            return null;
        }
    }
    
    public List<InvInventory> completInventory(String query) {
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
            saveError(e, "SalesInvoiceFormMB", "completInventory");
            return null;
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
            saveError(e, "SalesInvoiceFormMB", "completeCostCenters");
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
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }
                
                code = barcodesDetailsView.getBarcode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }
            }
            
            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(filteredItems);
            return filteredItems;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "completeItemsData");
            return null;
        }
    }
    
    public List<Symbol> completeSymbolUnit(String query) {
        try {
            List<Symbol> unitSymbolList = unitList;
            if (query == null || query.trim().equals("")) {
                
                unitConverter = new SymbolConverter(unitSymbolList);
                return unitSymbolList;
            }
            List<Symbol> filteredUnits = new ArrayList<>();
            
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
            return filteredUnits;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "completeSymbolUnit");
            return null;
        }
    }
    
    public List<InvOrganizationSite> completeCustomers(String query) {
        try {
            List<InvOrganizationSite> organizationSiteList = customersList;
            if (query == null || query.trim().equals("")) {
                
                customerConvertor = new InvOrganizationSiteConverter(organizationSiteList);
                return organizationSiteList;
            }
            List<InvOrganizationSite> filteredCustomers = new ArrayList<>();
            
            String nameAr;
            String code;
            InvOrganizationSite customerFilter;
            for (int i = 0; i < customersList.size(); i++) {
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
            saveError(e, "SalesInvoiceFormMB", "completeCustomers");
            return null;
        }
    }
    
    public void onCellEdit(CellEditEvent event) {
        try {
            
            Object oldValue = event.getOldValue() != null ? event.getOldValue() : null;
            Object newValue = event.getNewValue() != null ? event.getNewValue() : null;
            
            String column_name = event.getColumn().getClientId();
            
            DataTable dataTable = (DataTable) event.getSource();
            invPurchaseInvoiceDetailsUpdatedSelected = (InvPurchaseInvoiceDetailsEntity) dataTable.getRowData();
            
            if (invPurchaseInvoiceDetailsUpdatedSelected != null) {
                
                if (column_name.contains("itemNumber") || column_name.contains("itemName")) {
                    validateItems(invPurchaseInvoiceDetailsUpdatedSelected);
                }

//            if (column_name.contains("Unit")) {
//                if (!validateUnit(invPurchaseInvoiceDetailsUpdatedSelected)) {
//                    invPurchaseInvoiceDetailsUpdatedSelected.setUnit((Symbol) oldValue);
//                }
//            }
                if (column_name.contains("Quantity")) {
                    if (!validateQuantity(invPurchaseInvoiceDetailsUpdatedSelected)) {
                        errorMessage("الكمية المطلوبة اكبر من الموجودة بالمخزن");
                        if (invPurchaseInvoiceDetailsUpdatedSelected.getItemsBarcodesDetail() != null
                                && invPurchaseInvoiceDetailsUpdatedSelected.getItemsBarcodesDetail().getItemId() != null) {
                            invPurchaseInvoiceDetailsUpdatedSelected.setQuantity((BigDecimal) oldValue);
                        } else {
                            invPurchaseInvoiceDetailsUpdatedSelected.setQuantity(null);
                        }
                    }
                }
                
                if (column_name.contains("COST")) {
                    if (!validatePrice(invPurchaseInvoiceDetailsUpdatedSelected)) {
                        invPurchaseInvoiceDetailsUpdatedSelected.setCost((BigDecimal) oldValue);
                    }
                }
                
                if (column_name.contains("Discount")) {
                    if (!validateDiscount(invPurchaseInvoiceDetailsUpdatedSelected)) {
                        invPurchaseInvoiceDetailsUpdatedSelected.setDiscount((BigDecimal) oldValue);
                    }
                }
                
                updateSummition();

//            if (column_name.contains("Quantity")) {
//
//            }
            } else {
                errorMessage("يجب اختيار سطر للتعديل");
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "onCellEdit");
        }
        
    }
    
    public void validateQuantityColum(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsTable) {
        try {
            if (!validateQuantity(invPurchaseInvoiceDetailsTable)) {
                errorMessage("الكمية المطلوبة اكبر من الموجودة بالمخزن");
                if (invPurchaseInvoiceDetailsTable.getItemsBarcodesDetail() != null
                        && invPurchaseInvoiceDetailsTable.getItemsBarcodesDetail().getItemId() != null) {
                    invPurchaseInvoiceDetailsTable.setQuantity(BigDecimal.ZERO);
                } else {
                    invPurchaseInvoiceDetailsTable.setQuantity(null);
                }
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "validateQuantityColum");
        }
    }
    
    private Boolean validateQuantity(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {
        try {
            if (invPurchaseInvoiceDetailsValidate != null && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetailTrans() != null
                    && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetailTrans().getItemId() != null) {
                
                if (invPurchaseInvoiceDetailsValidate.getQuantity() != null
                        && invPurchaseInvoiceDetailsValidate.getQuantity().compareTo(BigDecimal.ZERO) == 1) {
                    if (invSetup.getNegativeSell() == false) {
                        if (!itemsMap.isEmpty()) {
                            BigDecimal dbQut = itemsMap.get(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getItemId());
                            if (dbQut != null) {
                                BigDecimal usrQut = sumItemQuantity(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getItemId());
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
            saveError(e, "SalesInvoiceFormMB", "validateQuantity");
            return false;
        }
    }
    
    BigDecimal sumItemQuantity(Integer itemId) {
        BigDecimal quant = BigDecimal.ZERO;
        try {
            
            BigDecimal detailQuant, detailScrewing;
            for (InvPurchaseInvoiceDetailsEntity detailsEntity : invPurchaseInvoiceDetailsEntityList) {
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
            saveError(e, "SalesInvoiceFormMB", "sumItemQuantity");
            return quant;
        }
        
    }
    
    public Boolean validateUnit(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsTable) {
        try {
            return invPurchaseInvoiceDetailsTable != null && invPurchaseInvoiceDetailsTable.getItemsBarcodesDetail() != null
                    && invPurchaseInvoiceDetailsTable.getItemsBarcodesDetail().getUnitId() != null
                    && invPurchaseInvoiceDetailsTable.getUnit() != null;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "validateUnit");
            return false;
        }
    }
    
    public void validatePriceColumn(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsTable) {
        try {
            if (!validatePrice(invPurchaseInvoiceDetailsTable)) {
                invPurchaseInvoiceDetailsTable.setCost(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "validatePriceColumn");
        }
    }
    
    private Boolean validatePrice(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsTable) {
        try {
            if (invPurchaseInvoiceDetailsTable != null && invPurchaseInvoiceDetailsTable.getItemsBarcodesDetail() != null
                    && invPurchaseInvoiceDetailsTable.getItemsBarcodesDetail().getItemId() != null) {
                if (invPurchaseInvoiceDetailsTable.getCost() != null
                        && invPurchaseInvoiceDetailsTable.getCost().compareTo(BigDecimal.ZERO) == 1) {
                    if (invSetup != null && !invSetup.getSellAllow()) {
                        
                        if (invPurchaseInvoiceDetailsTable.getItemsBarcodesDetail().getBarcode() != null
                                && !"".equals(invPurchaseInvoiceDetailsTable.getItemsBarcodesDetail().getBarcode())) {
                            
                            ItemsBarcodesDetailsView barcodesDetailsView
                                    = itemsBarcodeMap.get(invPurchaseInvoiceDetailsTable.getItemsBarcodesDetail().getBarcode());
                            
                            if (invPurchaseInvoiceDetailsTable.getCost().compareTo(barcodesDetailsView.getCostAverage() != null ? barcodesDetailsView.getCostAverage() : BigDecimal.ONE) == -1) {
                                
                                return errorMessage("يجب ادخال سعر أعلى من سعر متوسط التكلفة");
                            }
                        } else {
                            return errorMessage("هذا الصنف ليس لديه باركود");
                        }
                    }
                } else {
                    return errorMessage("يجب ادخال سعر");
                }
                return true;
            } else {
                return errorMessage("يجب اختيار صنف");
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "validatePrice");
            return false;
        }
    }
    
    public void validateDiscountColumn(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsTable) {
        try {
            if (!validateDiscount(invPurchaseInvoiceDetailsTable)) {
                invPurchaseInvoiceDetailsTable.setDiscount(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "validateDiscountColumn");
        }
    }
    
    private Boolean validateDiscount(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsTable) {
        return isDiscountValid(invPurchaseInvoiceDetailsTable.getDiscount());
    }
    
    public Boolean validateItems(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsValidate) {
        try {
            if (invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() == null) {
                resetInvItem(invPurchaseInvoiceDetailsValidate);
            } else {
                
                invPurchaseInvoiceDetailsValidate.setItemsBarcodesDetailTrans(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail());
                if (invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getSellPrice() != null) {
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
                        invPurchaseInvoiceDetailsValidate.setQuantity(BigDecimal.ONE);
                        invPurchaseInvoiceDetailsValidate.setCost(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getSellPrice());
                        invPurchaseInvoiceDetailsValidate.setScrewing(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getScrewing());
                        invPurchaseInvoiceDetailsValidate.setLength(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getLength());
                        invPurchaseInvoiceDetailsValidate.setWeight(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getWeight() != null ? invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getWeight() : BigDecimal.ZERO);
                    } else {
                        resetInvItem(invPurchaseInvoiceDetailsValidate);
                        return errorMessage("هذا الصنف ليس لديه وحدات");
                    }
                } else {
                    resetInvItem(invPurchaseInvoiceDetailsValidate);
                    return errorMessage("هذا الصنف ليس لديه سعر بيع");
                }
            }
            updateSummition();
            validateQuantity(invPurchaseInvoiceDetailsValidate);
            return true;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "validateItems");
            return false;
        }
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
            saveError(e, "SalesInvoiceFormMB", "resetInvItem");
        }
        
    }
    
    public void rowSelected(SelectEvent event) {
        try {
            DataTable dataTable = (DataTable) event.getSource();
            invPurchaseInvoiceDetailsUpdatedSelected = (InvPurchaseInvoiceDetailsEntity) dataTable.getRowData();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "rowSelected");
        }
    }
    
    public void updateSummition() {
        try {
            
            BigDecimal quantity, weight, discount, cost;
            totalNet = BigDecimal.ZERO;
            totalSum = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            totalWeight = BigDecimal.ZERO;
            
            for (InvPurchaseInvoiceDetailsEntity detailEntity : invPurchaseInvoiceDetailsEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
                quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
                weight = (detailEntity.getWeight() == null || detailEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : detailEntity.getWeight();
                cost = detailEntity.getCost() != null ? detailEntity.getCost() : BigDecimal.ZERO;
                discount = detailEntity.getDiscount() != null ? detailEntity.getDiscount() : BigDecimal.ZERO;
//            detailEntity.setTotal(quantity.multiply(cost));
                if (weight != null && weight.compareTo(BigDecimal.ZERO) > 0) {
                    detailEntity.setNet((weight.multiply(cost).subtract(weight.multiply(cost).multiply(discount).divide(hundred))).setScale(3, BigDecimal.ROUND_UP));
                } else {
                    detailEntity.setNet((quantity.multiply(cost).subtract(quantity.multiply(cost).multiply(discount).divide(hundred))).setScale(3, BigDecimal.ROUND_UP));
                }
                
                totalQuatity = totalQuatity.add(detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO);
                
                if (detailEntity.getWeight() != null && detailEntity.getWeight().compareTo(BigDecimal.ZERO) > 0) {
                    if (detailEntity.getQuantity() != null) {
                        setTotalWeight(getTotalWeight().add(detailEntity.getWeight().multiply(detailEntity.getQuantity())));
                    } else {
                        setTotalWeight(getTotalWeight().add(detailEntity.getWeight()));
                    }
                } else {
                    setTotalWeight(getTotalWeight().add(BigDecimal.ZERO));
                }

//            totalWeight = getTotalWeight().add(detailEntity.getWeight() != null ? detailEntity.getWeight() : BigDecimal.ZERO);
                totalSum = totalSum.add(detailEntity.getTotal() != null ? detailEntity.getTotal() : BigDecimal.ZERO);
                
                totalNet = (totalNet.add(detailEntity.getNet() != null ? detailEntity.getNet() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_UP);
            }
            calculateactualweight();
            calculateTotalNetAfterDiscount();
            calculateRemainValue();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "updateSummition");
        }
        
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
                    fillReport(prepareReport(), getUserData().getReportPath() + "salesReceiptCash.jasper", invPurchaseInvoiceDetailsEntityReport, "pdf");
                } catch (JRException ex) {
                    Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                }
                exit();
                return "invpurchaseinvoicelist";
            }
            return "";
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "exportPDF");
            return null;
        }
    }
    
    public String printA4() {
        return print("salesReceiptCash.jasper");
    }
    
    public String printRec() {
        return print("InvReceiptCash.jasper");
    }
    
    public String print(String name) {
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
                    details.setUnitName(detailsEntity.getItemId().getUnitId().getName());
                    details.setQuantity(detailsEntity.getQuantity());
                    details.setWeight(detailsEntity.getWeight());
                    details.setCost(detailsEntity.getCost());
                    details.setNet(detailsEntity.getNet());
                    details.setColor(map.get(detailsEntity.getItemBarcode()).getPaintColorName());
                    details.setLength(map.get(detailsEntity.getItemBarcode()).getLength());
                    invPurchaseInvoiceDetailsEntityReport.add(details);
                }
                try {
                    fillReport(prepareReport(), getUserData().getReportPath() + name, invPurchaseInvoiceDetailsEntityReport, "pdf");
                } catch (JRException ex) {
                    Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                }
                exit();
                return "invpurchaseinvoicelist";
            }
            return "";
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "print");
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
            saveError(e, "SalesInvoiceFormMB", "fillReport");
        }
        
    }
    
    public HashMap prepareReport() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            HashMap hashMap = new HashMap();
            
            ReportManagement reportManagement = reportManagementService.reportManagementByBranch(getUserData().getUserBranch().getId());
            if (reportManagement != null) {
                hashMap.put("LeftUp1", reportManagement.getLeftUp1());
                hashMap.put("LeftUp2", reportManagement.getLeftUp2());
                hashMap.put("LeftUp3", reportManagement.getLeftUp3());
                hashMap.put("LeftUp4", reportManagement.getLeftUp4());
                hashMap.put("RightUp1", reportManagement.getRightUp1());
                hashMap.put("RightUp2", reportManagement.getRightUp2());
                hashMap.put("RightUp3", reportManagement.getRightUp3());
                hashMap.put("RightUp4", reportManagement.getRightUp4());
                
            }
            if (isFileExist(getUserData().getImageReportPath())) {
                hashMap.put("companyImage", getUserData().getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }
            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());
            hashMap.put("phone", getUserData().getUserBranch().getPhone());
            hashMap.put("phoneText", "تليفون/فاكس");
            hashMap.put("address", getUserData().getUserBranch().getAddress1());
            hashMap.put("branchNameEn", getUserData().getUserBranch().getNameEn());
            hashMap.put("companyNameEn", getUserData().getCompany().getNameEn());
            hashMap.put("addressEn", getUserData().getUserBranch().getAddress2());
            
            if (getViewTaxCode()) {
                hashMap.put("taxCodeText", getUserData().getUserBranch().getTaxCode());
                if (salesInvoiceEntity.getPaymentType() == 0) {
                    hashMap.put("salesReceiptText", "فاتورة مبيعات نقدي");
                } else if (salesInvoiceEntity.getPaymentType() == 1) {
                    hashMap.put("salesReceiptText", "فاتورة مبيعات آجلة");
                } else if (salesInvoiceEntity.getPaymentType() == 2) {
                    hashMap.put("salesReceiptText", "فاتورة مبيعات اعتماد مستندي");
                }
                
                hashMap.put("receiptNumberText", "رقم الفاتورة");
            } else {
                hashMap.put("salesReceiptText", getUserData().getUserDDs().get("TITLE_INVOICE_SALES_NOT_TAX"));
                hashMap.put("receiptNumberText", getUserData().getUserDDs().get("NUMBER_INVOICE_SALES_NOT_TAX"));
            }
            
            if (salesInvoiceEntity.getDiscountType() != null && salesInvoiceEntity.getDiscountType() == 1) {
                hashMap.put("percentage", "%");
            }
            hashMap.put("adressText", getUserData().getUserBranch().getAddress1());
            hashMap.put("weightDifferenceText", "فرق الوزن");
            hashMap.put("weightDifference", multiply);
            hashMap.put("customerText", "العميل");
            hashMap.put("addressText", "المخزن");
            hashMap.put("invNameText", "المخزن");
            hashMap.put("timeText", "الوقت");
            hashMap.put("colorText", "اللون");
            hashMap.put("lengthText", "الطول");
            hashMap.put("remarkText", userDDs.get("REMARK"));
            hashMap.put("dateText", userDDs.get("DATE"));
            hashMap.put("salesText", "المندوب");
            hashMap.put("sellText", "المبيعات");
            hashMap.put("accountText", "الحسابات");
            hashMap.put("deliveryText", "المستلم");
            hashMap.put("serialText", "م");
            hashMap.put("itemNumberText", "رقم الصنف");
            hashMap.put("itemNameText", "اسم الصنف");
            hashMap.put("unitText", "الوحدة");
            hashMap.put("amountText", "كمية");
            hashMap.put("unitCostText", "السعر");
            hashMap.put("totalText", "القيمة");
            hashMap.put("discountText", "الخصم");
            hashMap.put("netText", "الصافي");
            hashMap.put("totalWithTaxText", "الإجمالي");
            hashMap.put("payedText", "المبلغ المدفوع");
            hashMap.put("remainText", "المبلغ المتبقى");
            hashMap.put("tax", "الضريبةً");
            hashMap.put("taxValue", taxvalue);
            hashMap.put("totalValue", totalNet);
            hashMap.put("totalWithTaxValue", totalWithTaxValue);
            hashMap.put("payedValue", payed);
            hashMap.put("remainValue", remain);
            hashMap.put("receiptNumberValue", salesInvoiceEntity.getSerial());
            hashMap.put("invName", salesInvoiceEntity.getInvInventory().getName());
            hashMap.put("customerName", salesInvoiceEntity.getCustomer().getName());
            hashMap.put("customerPhone", salesInvoiceEntity.getCustomer().getMobile());
            hashMap.put("addressValue", salesInvoiceEntity.getCustomer().getAddress());
            hashMap.put("remarkValue", salesInvoiceEntity.getRemarks());
            hashMap.put("dateValue", salesInvoiceEntity.getDate());
            hashMap.put("salesValue", salesInvoiceEntity.getSalesPerson().getName());
//        hashMap.put("companyImage", getUserData().getImageReportPath());
            hashMap.put("discountValue", salesInvoiceEntity.getHeadDiscount());
            hashMap.put("netValue", totalNetAfterDiscount);
            
            return hashMap;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "prepareReport");
            return null;
        }
    }
    
    private boolean saveReport() {
        try {
            if (salesInvoiceEntity != null) {
                
                if (salesInvoiceEntity.getPaymentType() != 1 && salesInvoiceEntity.getInvInventory() == null) {
                    return errorMessage("يجب أختيار مخزن");
                } else {
                    invPurchaseInvoice.setInvInventoryId(salesInvoiceEntity.getInvInventory());
                }
                
                if (salesInvoiceEntity.getId() != null) {
                    invPurchaseInvoice.setId(salesInvoiceEntity.getId());
                    invPurchaseInvoice.setCreatedBy(getInvPurchaseInvoiceExist().getCreatedBy() != null ? getInvPurchaseInvoiceExist().getCreatedBy() : null);
                    invPurchaseInvoice.setCreationDate(getInvPurchaseInvoiceExist().getCreationDate() != null ? getInvPurchaseInvoiceExist().getCreationDate() : null);
                    
                    invPurchaseInvoice.setModifiedBy(getUserData().getUser());
                    invPurchaseInvoice.setModificationDate(new Date());
                    
                } else {
                    invPurchaseInvoice.setCreatedBy(getUserData().getUser());
                    invPurchaseInvoice.setCreationDate(new Date());
                }
                
                if (salesInvoiceEntity.getCostCenterId() != null) {
                    invPurchaseInvoice.setCostCenterId(salesInvoiceEntity.getCostCenterId());
                }
                
                invPurchaseInvoice.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch() : null);
                invPurchaseInvoice.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);
                invPurchaseInvoice.setDate(salesInvoiceEntity.getDate() != null ? salesInvoiceEntity.getDate() : null);
                invPurchaseInvoice.setDiscount(salesInvoiceEntity.getHeadDiscount() != null ? salesInvoiceEntity.getHeadDiscount() : null);
                invPurchaseInvoice.setDiscountType(salesInvoiceEntity.getDiscountType() != null ? salesInvoiceEntity.getDiscountType() : null);
                invPurchaseInvoice.setRemarks(salesInvoiceEntity.getRemarks() != null ? salesInvoiceEntity.getRemarks() : null);
                invPurchaseInvoice.setGlBankId(salesInvoiceEntity.getGlBank() != null ? salesInvoiceEntity.getGlBank() : null);
                invPurchaseInvoice.setTaxflag(salesInvoiceEntity.getTaxFlag());
                updateTaxDiscValue();
                invPurchaseInvoice.setTaxdiscflag(salesInvoiceEntity.getTaxdiscflag());
                invPurchaseInvoice.setTaxdiscvalue(salesInvoiceEntity.getTaxdiscvalue());
                invPurchaseInvoice.setTaxFlagFinal(getViewTaxCode());
                if (salesInvoiceEntity.getSalesPerson() != null) {
                    invPurchaseInvoice.setInvDelegatorId(salesInvoiceEntity.getSalesPerson());
                } else {
                    return errorMessage("يجب أختيار مندوب مبيعات");
                }
                
                if (salesInvoiceEntity.getCustomer() != null) {
                    invPurchaseInvoice.setOrganizationSiteId(salesInvoiceEntity.getCustomer());
                } else {
                    return errorMessage("يجب أختيار عميل");
                }
                invPurchaseInvoice.setPaymentType(salesInvoiceEntity.getPaymentType());
                
                invPurchaseInvoice.setSerial(salesInvoiceEntity.getSerial());
                invPurchaseInvoice.setSerialTax(salesInvoiceEntity.getSerialTax());
                invPurchaseInvoice.setType(1);
                invPurchaseInvoice.setPostFlag(0);
                invPurchaseInvoice.setActualWeight(salesInvoiceEntity.getActualWeight());
                invPurchaseInvoice.setPriceKilo(salesInvoiceEntity.getPriceKilo());
                calculateactualweight();
                invPurchaseInvoice.setTotalActualWeight(finalNet);
                InvPurchaseInvoiceDetail purchaseInvoiceDetail;
                InvPurchaseInvoiceDetail purchaseInvoiceDetailExist;
                
                if (invPurchaseInvoiceDetailsEntityList != null && !invPurchaseInvoiceDetailsEntityList.isEmpty()) {
                    invPurchaseInvoiceDetailsUpdatedList = new ArrayList<>();
                    invPurchaseInvoiceDetailsDeletedList = new ArrayList<>();
                    for (InvPurchaseInvoiceDetailsEntity detailsEntity : invPurchaseInvoiceDetailsEntityList) {
                        purchaseInvoiceDetail = new InvPurchaseInvoiceDetail();
                        
                        if (detailsEntity.getItemsBarcodesDetail() == null && detailsEntity.getItemsBarcodesDetailTrans() == null) {
                            return errorMessage("يجب ادخال الصنف");
                        } else {
                            InvItem invItem = new InvItem();
                            invItem.setId(detailsEntity.getItemsBarcodesDetailTrans().getItemId());
                            purchaseInvoiceDetail.setItemId(invItem);
                            purchaseInvoiceDetail.setItemBarcode(detailsEntity.getItemsBarcodesDetailTrans().getBarcode());
                            detailsEntity.setItemsBarcodesDetail(detailsEntity.getItemsBarcodesDetailTrans());
                        }
                        
                        if (validateQuantity(detailsEntity)) {
                            purchaseInvoiceDetail.setQuantity(detailsEntity.getQuantity());
                        } else {
                            return errorMessage("يجب ادخال كمية");
                        }
                        
                        purchaseInvoiceDetail.setWeight(detailsEntity.getWeight());
                        
                        if (validatePrice(detailsEntity)) {
                            purchaseInvoiceDetail.setCost(detailsEntity.getCost());
                        } else {
                            return errorMessage("يجب ادخال سعر أعلى من سعر التكلفة");
                        }
                        
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
                        
                        purchaseInvoiceDetail.setSerial(detailsEntity.getSerial());
                        purchaseInvoiceDetail.setBranchId(getUserData().getUserBranch());
                        purchaseInvoiceDetail.setCompanyId(getUserData().getCompany());
                        purchaseInvoiceDetail.setDiscount(detailsEntity.getDiscount() != null ? detailsEntity.getDiscount() : null);
                        purchaseInvoiceDetail.setDiscountType(detailsEntity.getDiscountType() != null ? detailsEntity.getDiscountType() : null);
                        purchaseInvoiceDetail.setTransferFrom(detailsEntity.getTransferFrom() != null ? detailsEntity.getTransferFrom() : null);
                        purchaseInvoiceDetail.setScrewing(detailsEntity.getScrewing());
                        purchaseInvoiceDetail.setSelectedId(detailsEntity.getSelectedId() != null ? detailsEntity.getSelectedId() : null);
                        purchaseInvoiceDetail.setNet(detailsEntity.getNet());
                        
                        if (detailsEntity.getUnit() != null) {
                            purchaseInvoiceDetail.setUnitId(detailsEntity.getUnit());
                        }
                        
                        if (invPurchaseInvoice.getTaxflag()) {
                            purchaseInvoiceDetail.setTaxValue((purchaseInvoiceDetail.getNet().multiply(BigDecimal.valueOf(0.14))));
//                        taxvalue = taxvalue.add(purchaseInvoiceDetail.getTaxValue());
                        }
                        invPurchaseInvoiceDetailsUpdatedList.add(purchaseInvoiceDetail);
                    }
                    totalWithTaxValue = totalNetAfterDiscount.add(taxvalue);
                } else {
                    return errorMessage("يجب ادخال فاتورة على الاقل");
                }
                
                if (!getRowsDeleted().isEmpty() && getRowsDeleted() != null) {
                    for (InvPurchaseInvoiceDetailsEntity rowDeleted : getRowsDeleted()) {
                        purchaseInvoiceDetail = new InvPurchaseInvoiceDetail();
                        purchaseInvoiceDetail.setId(rowDeleted.getId());
                        invPurchaseInvoiceDetailsDeletedList.add(purchaseInvoiceDetail);
                    }
                }
                
                InvPurchaseReturnSave invPurchaseReturnSave;
                
                invPurchaseReturnSave = invPurchaseInvoiceService.addInvPurchaseInvoice(invPurchaseInvoice, invPurchaseInvoiceDetailsUpdatedList,
                        invPurchaseInvoiceDetailsDeletedList, updatedPurchaseOrAddingOrderList, isQutation, headIdList, 0);
                
                if (invPurchaseInvoice.getMsg() != null && !"".equals(invPurchaseInvoice.getMsg())) {
                    return errorMessage("يجب ادخال كميةاكبر من الموجودة بالمخزن");
                }
                
                reset();
                
                invPurchaseInvoice = invPurchaseReturnSave.getInvPurchaseInvoice();
                
                setInvPurchaseInvoiceExist(invPurchaseInvoice);
                
                invPurchaseInvoiceDetailsList = (List<InvPurchaseInvoiceDetail>) invPurchaseReturnSave.getInvPurchaseInvoiceDetailList();
                
                salesInvoiceEntity = mapInvPurchaseInvoiceToInvPurchaseInvoiceEntity(invPurchaseReturnSave.getInvPurchaseInvoice());
                
                return true;
            }
            return false;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "saveReport");
            return false;
        }
    }
    
    public void updateTax() {
        try {
            removeTaxValue();
            if (salesInvoiceEntity.getTaxFlag() != null && salesInvoiceEntity.getTaxFlag()) {
                taxvalue = totalNetAfterDiscount.multiply(BigDecimal.valueOf(0.14)).setScale(3,RoundingMode.FLOOR);
                totalWithTaxValue = totalNetAfterDiscount.add(taxvalue);
            }
            updateTaxDiscValue();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "updateTax");
        }
    }
    
    public void updateTaxDiscValue() {
        try {
            
            if (salesInvoiceEntity.getTaxdiscflag() != null && salesInvoiceEntity.getTaxdiscflag()) {
                salesInvoiceEntity.setTaxdiscvalue(totalNetAfterDiscount.multiply(BigDecimal.valueOf(0.01)).setScale(3));
                totalWithTaxValue = totalWithTaxValue.subtract(salesInvoiceEntity.getTaxdiscvalue().setScale(3));
            } else {
                salesInvoiceEntity.setTaxdiscvalue(BigDecimal.ZERO);
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "updateTaxDiscValue");
        }
    }
    
    public void removeTaxValue() {
        try {
            totalWithTaxValue = totalNetAfterDiscount;
            taxvalue = BigDecimal.ZERO;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "removeTaxValue");
        }
    }
    
    @Override
    public String save() {
        try {
            if (saveReport()) {
                exit();
                return "invsalesinvoicelist";
            }
            return "";
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "save");
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
            saveError(e, "SalesInvoiceFormMB", "saveAndContinue");
            return null;
        }
    }
    
    @Override
    public String getScreenName() {
        return "";
    }
    
    public String exit() {
        try {
            exit("../invsalesinvoice/invsalesinvoicelist.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "exit");
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
    
    public SalesInvoiceEntity getSalesInvoiceEntity() {
        return salesInvoiceEntity;
    }
    
    public void setSalesInvoiceEntity(SalesInvoiceEntity salesInvoiceEntity) {
        this.salesInvoiceEntity = salesInvoiceEntity;
    }
    
    public List<InvPurchaseInvoiceEntity> getInvPurchaseInvoiceEntityList() {
        return invPurchaseInvoiceEntityList;
    }
    
    public void setInvPurchaseInvoiceEntityList(List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList) {
        this.invPurchaseInvoiceEntityList = invPurchaseInvoiceEntityList;
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
    
    public List<InventoryDelegator> getSalesPersonsList() {
        return salesPersonsList;
    }
    
    public void setSalesPersonsList(List<InventoryDelegator> salesPersonsList) {
        this.salesPersonsList = salesPersonsList;
    }
    
    public List<CostCenter> getCostCenterList() {
        return costCenterList;
    }
    
    public void setCostCenterList(List<CostCenter> costCenterList) {
        this.costCenterList = costCenterList;
    }
    
    public List<GlBank> getGlBankList() {
        return glBankList;
    }
    
    public void setGlBankList(List<GlBank> glBankList) {
        this.glBankList = glBankList;
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
    
    public BigDecimal getTotalNet() {
        return totalNet;
    }
    
    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
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
    
    public CostCenterConverter getCostCenterConverter() {
        return costCenterConverter;
    }
    
    public void setCostCenterConverter(CostCenterConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }
    
    public SymbolConverter getUnitConverter() {
        return unitConverter;
    }
    
    public void setUnitConverter(SymbolConverter unitConverter) {
        this.unitConverter = unitConverter;
    }
    
    public GlBankConverter getGlBankConverter() {
        return glBankConverter;
    }
    
    public void setGlBankConverter(GlBankConverter glBankConverter) {
        this.glBankConverter = glBankConverter;
    }
    
    public ItemConverter getItemConverter() {
        return itemConverter;
    }
    
    public void setItemConverter(ItemConverter itemConverter) {
        this.itemConverter = itemConverter;
    }
    
    public InvDelegatorConvertor getSalesConverter() {
        return salesConverter;
    }
    
    public void setSalesConverter(InvDelegatorConvertor salesConverter) {
        this.salesConverter = salesConverter;
    }
    
    public BigDecimal getTotalNetAfterDiscount() {
        return totalNetAfterDiscount;
    }
    
    public void setTotalNetAfterDiscount(BigDecimal totalNetAfterDiscount) {
        this.totalNetAfterDiscount = totalNetAfterDiscount;
    }
    
    public List<Symbol> getUnitList() {
        return unitList;
    }
    
    public void setUnitList(List<Symbol> unitList) {
        this.unitList = unitList;
    }
    
    public List<InvBarcode> getInvBarcodesList() {
        return invBarcodesList;
    }
    
    public void setInvBarcodesList(List<InvBarcode> invBarcodesList) {
        this.invBarcodesList = invBarcodesList;
    }
    
    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }
    
    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }
    
    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }
    
    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }
    
    public ItemsBarcodesDetailsViewConverter getItemsBarcodesDetailsViewConverter() {
        return itemsBarcodesDetailsViewConverter;
    }
    
    public void setItemsBarcodesDetailsViewConverter(ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter) {
        this.itemsBarcodesDetailsViewConverter = itemsBarcodesDetailsViewConverter;
    }
    
    public List<ItemsBarcodesDetailsView> getItemsBarcodesDetailsViewList() {
        return itemsBarcodesDetailsViewList;
    }
    
    public void setItemsBarcodesDetailsViewList(List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList) {
        this.itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewList;
    }
    
    public Boolean getShowDialogMessage() {
        return showDialogMessage;
    }
    
    public void setShowDialogMessage(Boolean showDialogMessage) {
        this.showDialogMessage = showDialogMessage;
    }
    
    public List<InvQotation> getInvQutationList() {
        return invQutationList;
    }
    
    public void setInvQutationList(List<InvQotation> invQutationList) {
        this.invQutationList = invQutationList;
    }
    
    public List<InvQotation> getInvQutationSelectedList() {
        return invQutationSelectedList;
    }
    
    public void setInvQutationSelectedList(List<InvQotation> invQutationSelectedList) {
        this.invQutationSelectedList = invQutationSelectedList;
    }
    
    public boolean isInvQuotationTabView() {
        return invQuotationTabView;
    }
    
    public void setInvQuotationTabView(boolean invQuotationTabView) {
        this.invQuotationTabView = invQuotationTabView;
    }
    
    public boolean isInvReservationTabView() {
        return invReservationTabView;
    }
    
    public void setInvReservationTabView(boolean invReservationTabView) {
        this.invReservationTabView = invReservationTabView;
    }
    
    public List<InvReservation> getInvReservationList() {
        return invReservationList;
    }
    
    public void setInvReservationList(List<InvReservation> invReservationList) {
        this.invReservationList = invReservationList;
    }
    
    public List<InvReservation> getInvReservationSelectedList() {
        return invReservationSelectedList;
    }
    
    public void setInvReservationSelectedList(List<InvReservation> invReservationSelectedList) {
        this.invReservationSelectedList = invReservationSelectedList;
    }
    
    public List<InvOrganizationSite> getCustomersList() {
        return customersList;
    }
    
    public void setCustomersList(List<InvOrganizationSite> customersList) {
        this.customersList = customersList;
    }
    
    public InvOrganizationSiteConverter getCustomerConvertor() {
        return customerConvertor;
    }
    
    public void setCustomerConvertor(InvOrganizationSiteConverter customerConvertor) {
        this.customerConvertor = customerConvertor;
    }
    
    public List<GlAdminUnit> getAdminUnitList() {
        return adminUnitList;
    }
    
    public void setAdminUnitList(List<GlAdminUnit> adminUnitList) {
        this.adminUnitList = adminUnitList;
    }
    
    public GlAdminUnitConverter getAdminUnitConverter() {
        return adminUnitConverter;
    }
    
    public void setAdminUnitConverter(GlAdminUnitConverter adminUnitConverter) {
        this.adminUnitConverter = adminUnitConverter;
    }
    
    public Boolean getViewDuDate() {
        return viewDuDate;
    }
    
    public void setViewDuDate(Boolean viewDuDate) {
        this.viewDuDate = viewDuDate;
    }

    /**
     * @return the taxvalue
     */
    public BigDecimal getTaxvalue() {
        return taxvalue;
    }

    /**
     * @param taxvalue the taxvalue to set
     */
    public void setTaxvalue(BigDecimal taxvalue) {
        this.taxvalue = taxvalue;
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
     * @return the viewTaxCode
     */
    public Boolean getViewTaxCode() {
        return viewTaxCode;
    }

    /**
     * @param viewTaxCode the viewTaxCode to set
     */
    public void setViewTaxCode(Boolean viewTaxCode) {
        this.viewTaxCode = viewTaxCode;
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
     * @return the payed
     */
    public BigDecimal getPayed() {
        return payed;
    }

    /**
     * @param payed the payed to set
     */
    public void setPayed(BigDecimal payed) {
        this.payed = payed;
    }

    /**
     * @return the remain
     */
    public BigDecimal getRemain() {
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    /**
     * @return the settingType
     */
    public Integer getSettingType() {
        return settingType;
    }

    /**
     * @param settingType the settingType to set
     */
    public void setSettingType(Integer settingType) {
        this.settingType = settingType;
    }

    /**
     * @return the invTransferList
     */
    public List<InvTransfer> getInvTransferList() {
        return invTransferList;
    }

    /**
     * @param invTransferList the invTransferList to set
     */
    public void setInvTransferList(List<InvTransfer> invTransferList) {
        this.invTransferList = invTransferList;
    }

    /**
     * @return the invTransferSelectedList
     */
    public List<InvTransfer> getInvTransferSelectedList() {
        return invTransferSelectedList;
    }

    /**
     * @param invTransferSelectedList the invTransferSelectedList to set
     */
    public void setInvTransferSelectedList(List<InvTransfer> invTransferSelectedList) {
        this.invTransferSelectedList = invTransferSelectedList;
    }

    /**
     * @return the invTransferService
     */
    public InvTransferService getInvTransferService() {
        return invTransferService;
    }

    /**
     * @param invTransferService the invTransferService to set
     */
    public void setInvTransferService(InvTransferService invTransferService) {
        this.invTransferService = invTransferService;
    }

    /**
     * @return the invTransferDetailService
     */
    public InvTransferDetailService getInvTransferDetailService() {
        return invTransferDetailService;
    }

    /**
     * @param invTransferDetailService the invTransferDetailService to set
     */
    public void setInvTransferDetailService(InvTransferDetailService invTransferDetailService) {
        this.invTransferDetailService = invTransferDetailService;
    }

    /**
     * @return the invTransferTabView
     */
    public boolean isInvTransferTabView() {
        return invTransferTabView;
    }

    /**
     * @param invTransferTabView the invTransferTabView to set
     */
    public void setInvTransferTabView(boolean invTransferTabView) {
        this.invTransferTabView = invTransferTabView;
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.CostCenterService;
import com.isag.businessservice.CurrencyService;
import com.isag.businessservice.GlAdminUnitService;
import com.isag.businessservice.InvAddingOrderService;
import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.InvDialogueService;
import com.isag.businessservice.InvGlobalService;
import com.isag.businessservice.InvInventoryService;
import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvOpeningBalanceItemService;
import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.businessservice.InvPurchaseOrderService;
import com.isag.businessservice.InvQuotationService;
import com.isag.businessservice.InvReservationService;
import com.isag.businessservice.InvStripService;
import com.isag.businessservice.InvTransferService;
import com.isag.businessservice.InvUpdateService;
import com.isag.businessservice.InventorySetupService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.MonthTargetService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.QuantityItemsStoreService;
import com.isag.businessservice.ReportManagementService;
import com.isag.businessservice.SymbolService;
import com.isag.businessservice.reports.SupplierAccountService;
import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.converter.CostCenterDTOConverter;
import com.isag.converter.CurrencyDTOConverter;
import com.isag.converter.GlAdminUnitDTOConverter;
import com.isag.converter.GlBankDTOConverter;
import com.isag.converter.InvDelegatorConvertor;
import com.isag.converter.InvDelegatorDTOConvertor;
import com.isag.converter.InvInventoryConverter;
import com.isag.converter.InvInventoryDTOConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.converter.ItemDTOConverter;
import com.isag.converter.ItemsBarcodesDetailsViewConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.CostCenterDTO;
import com.isag.dto.CurrencyDTO;
import com.isag.dto.GlAdminUnitDTO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.InvDailogueData;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InvItemDTO;
import com.isag.dto.InvMonthTargetDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.InventoryDelegatorDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.views.QuantityItemsStore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author m_els
 */
public abstract class InventoryBasicDataForm extends BaseFormBean {

    private Integer siteType;
    private String itemName;

    private List<InvOrganizationSite> invOrganizationSiteList;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
    private List<SymbolDTO> regionList;
    private List<InvItem> invItemList;
    private List<InventoryDelegatorDTO> inventoryDelegatorList;
    private List<InvInventory> invInventoryList;
    private List<InvMonthTargetDTO> invMonthTargetDTOList;

    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;
    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverterForSearch;
    private InvDelegatorConvertor purchasePersonConvertor;
    private InvOrganizationSiteConverter supplierConvertor;
    private InvOrganizationSiteDTOConverter organizationSiteDTOConvertor;
    private InvDelegatorDTOConvertor invDelegatorDTOConvertor;
    private InvInventoryConverter invInventoryConverter;
    private SymbolDTOConverter regionConverter;

    private List<InvInventoryDTO> invInventoryDTOList;
    private List<InvInventoryDTO> invInventoryDTOListTo;
    private InvInventoryDTOConverter invInventoryDTOConverter;
    private InvInventoryDTOConverter invInventoryDTOConverterTO;
    private List<CurrencyDTO> currencyDTOList;
    private CurrencyDTOConverter currencyConverter;
    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;
    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewListForSearch;
    private ItemsBarcodesDetailsView itemsBarcodesDetailsViewListForSearchSelected;
    private Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap;
    private List<CostCenterDTO> costCenterList;
    private CostCenterDTOConverter costCenterConverter;
    private List<GlAdminUnitDTO> adminUnitList;
    private GlAdminUnitDTOConverter adminUnitConverter;
    private List<InvItemDTO> invItemDTOList;
    private List<SymbolDTO> unitList;
    private SymbolDTOConverter unitConverter;
    private List<GlBankDTO> glBankList;
    private GlBankDTOConverter glBankConverter;

    private ItemDTOConverter itemDTOConverter;

    private InvInvoiceDTO invInvoiceDTO;
    private InvInvoiceDTO invInvoiceDTOSelected;

    IReportService iReportService = new IReportServiceImpl();

    private LazyDataModel<InvInvoiceDTO> dataModel;
    private LazyDataModel<InvDailogueData> dataModelInvDailogueData;
    private Map<String, ItemsBarcodesDetailsView> itemsBarcodesDetailsViewMap;

    private Boolean firstLoad;
    private Boolean checkDiscountValue = false;
    private Boolean checkDiscountRate = false;

    private InvOrganizationSiteDTO organizationSiteId;

    private Integer barcodeType;

    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private SymbolService symbolService;

    @EJB
    private CostCenterService costCenterService;
    @EJB
    private GlAdminUnitService glAdminUnitService;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;
    @EJB
    private InvAddingOrderService invAddingOrderService;
    @EJB
    private InvQuotationService invQuotationService;
    @EJB
    private InvReservationService invReservationService;
    @EJB
    private QuantityItemsStoreService quantityItemsStoreService;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    private ReportManagementService reportManagementService;
    @EJB
    private InvGlobalService invGlobalService;
    @EJB
    private InvDialogueService invDialogueService;
    @EJB
    private InvTransferService invTransferService;
    @EJB
    private MonthTargetService monthTargetService;
    @EJB
    private InvStripService invStripService;
    @EJB
    private InvUpdateService invUpdateService;
    @EJB
    private InvOpeningBalanceItemService invOpeningBalanceItemService;
    @EJB
    private AutoCompleteService autoCompleteService;
    @EJB
    SupplierAccountService supplierAccountService;
    @EJB
    InvInventoryService invInventoryService;

    @Override
    public abstract String save();

    @Override
    public abstract void init();

    public abstract String exit();

    @Override
    public abstract void load();

    @Override
    public abstract String getScreenName();

    public void reset() {
        try {
            setInvInvoiceDTO(null);
            setOrganizationSiteId(null);
            getInvInvoiceDTO().setInvInventoryId(getUserData().getInventoryDTODefault());
            getInvInvoiceDTO().setGlBankId(getUserData().getGlBankDTODefault());
            if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPURCHASEORDERFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)) {
                getInvInvoiceDTO().setOrganizationSiteId(getInvOrganizationSiteDTOList(true, 1).get(0));
                getInvInvoiceDTO().setInvDelegatorId(getInventoryDelegatorList(0).get(0));
                putCurrency();
            } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESORDERFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPOSINVOICEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRESERVATIONFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVQUOTATIONFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPERMISSIONORDERFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
                getInvInvoiceDTO().setOrganizationSiteId(getInvOrganizationSiteDTOList(true, 0).get(0));
                getInvInvoiceDTO().setInvDelegatorId(getInventoryDelegatorList(1).get(0));
                putCurrency();
            } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVTRANSFERINVENTORY)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVTRANSFER)) {
                if (getInvInventoryDTOList() != null && !getInvInventoryDTOList().isEmpty()) {
                    getInvInvoiceDTO().setInvInventoryId(getInvInventoryDTOList().get(0));
                    getInvInvoiceDTO().setInvInventoryIdTo(getInvInventoryDTOList().get(getInvInventoryDTOList().size() - 1));
                }
            } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVMONTHTARGET)) {
                getInvInvoiceDTO().setInvMonthTargetDTO(new InvMonthTargetDTO());
            } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSTRIP)) {
                getInvInvoiceDTO().setInvInventoryId(getInvInventoryDTOList().get(0));
            } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVUPDATE)) {
                getInvInvoiceDTO().setInvInventoryId(getInvInventoryDTOList().get(0));
            } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM)) {
                getInvInvoiceDTO().setInvInventoryId(getInvInventoryDTOList().get(0));
            }

            if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
                addInvDetail();
                changeFocus("form:invPurchaseInvoiceDetailsTable:0:invItemNameIn");
            }

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "resetInvPurchaseInvoiceForm");
        }
    }

    public void fillDefaultList() {
        getInvInventoryDTOList();
        if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPURCHASEORDERFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)) {
            getInvOrganizationSiteDTOList(true, 1);
            getInventoryDelegatorList(0);
        } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPOSINVOICEFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRESERVATIONFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVQUOTATIONFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPERMISSIONORDERFORM)) {
            getInvOrganizationSiteDTOList(true, 0);
            getInventoryDelegatorList(1);
        }

        if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPOSINVOICEFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
            getGlBankList();
        }
    }

    public void fillLists() {
        try {
            fillDefaultList();
            if (!getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVTRANSFER)
                    && !getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM)
                    && !getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVUPDATE)
                    && !getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSTRIP)) {
                getCurrencyDTOList();
            }
            getItemsBarcodesDetailsViewList(getInvInvoiceDTO().getParent());
            if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPOSINVOICEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
                getCostCenterList();
                getAdminUnitList();
            }
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "fillLists");
        }

    }

    public void posSetting() {
        if (getUri().contains("invposinvoiceform")
                && (getInvInvoiceDTO().getMsg() == null || getInvInvoiceDTO().getMsg().isEmpty())) {
            if (getScreenMode().equalsIgnoreCase("addInvoice")) {
                addInvDetail();
                changeFocus("form:invPurchaseInvoiceDetailsTable:" + getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().size() + ":invItemNameIn");
            } else {
                calcAfterDiscount();
            }

        }
    }

    public void addInvDetail() {
        try {
            checkDetailData();
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invGlobalService.addInvPurchaseInvoiceDetail(getInvInvoiceDTO(), getIndex()));
            errorMsg(getInvInvoiceDTO().getMsg());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "addInvPurchaseInvoiceDetail");
        }
    }

    public void validatedetail() {
        checkDetailData();
        String error = invGlobalService.validateSave(getInvInvoiceDTO().getInvPurchaseInvoiceDetailList(),
                getInvInvoiceDTO().getInvInventoryId() != null ? getInvInvoiceDTO().getInvInventoryId().getId() : null,
                getInvInvoiceDTO().getScreenName(), getInvInvoiceDTO().getInventorySetupDTO().getNegativeSell(),
                (getInvInvoiceDTO().getLoadedSerial() != null), getInvInvoiceDTO().getInventorySetupDTO().getSellBuy(),
                getInvInvoiceDTO().getSupplierInvoiceNumber());
        if (error != null && !error.isEmpty()) {
            errorMsg(error);
        }
    }

    public void recalcHeadValues() {
        try {
            checkDetailData();
            setInvInvoiceDTO(invGlobalService.recalcHeadValues(getInvInvoiceDTO()));
            calculateRemainValue();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "recalcHeadValues");
        }
    }

    public void calculateRemainValue() {
        try {
            checkDetailData();
            getInvInvoiceDTO().setRemain(getInvInvoiceDTO().getPayed().add(getInvInvoiceDTO().getPayNet()).add(getInvInvoiceDTO().getDiscount()).subtract(getInvInvoiceDTO().getTotalNet()));
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "calculateRemainValue");
        }
    }

    public void validateAddingColumn(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            validateDiscountColumn(invPurchaseInvoiceDetailsValidate);
            addInvDetail();
//            changeFocus("form:invPurchaseInvoiceDetailsTable:" + getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().size() + ":invItemNameIn");
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateDiscountColumn");
        }
    }

    public void validateDiscountColumn(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            checkDetailData();
            invPurchaseInvoiceDetailsValidate.setMsg(null);
            invPurchaseInvoiceDetailsValidate = invGlobalService.validateDiscountColumn(invPurchaseInvoiceDetailsValidate);
            afterValidation(invPurchaseInvoiceDetailsValidate);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateDiscountColumn");
        }
    }

    private void afterValidation(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        replacedetail(invPurchaseInvoiceDetailsValidate);
        updateSummition();
        errorMsg(invPurchaseInvoiceDetailsValidate.getMsg());
    }

    public void validatePriceColumn(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            checkDetailData();
            invPurchaseInvoiceDetailsValidate.setMsg(null);
            invPurchaseInvoiceDetailsValidate = invGlobalService.validatePriceColumn(invPurchaseInvoiceDetailsValidate);
            afterValidation(invPurchaseInvoiceDetailsValidate);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validatePriceColumn");
        }
    }

    public void validateNetColumn(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            invPurchaseInvoiceDetailsValidate.setMsg(null);
            invPurchaseInvoiceDetailsValidate.setCost(new BigDecimal((invPurchaseInvoiceDetailsValidate.getNet().divide(invPurchaseInvoiceDetailsValidate.getQuantity(), 3, RoundingMode.HALF_UP)).setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            invPurchaseInvoiceDetailsValidate = invGlobalService.validateNetColumn(invPurchaseInvoiceDetailsValidate);
            afterValidation(invPurchaseInvoiceDetailsValidate);
//            addInvDetail();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validatePriceColumn");
        }
    }

    public void validateQuantityColum(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            checkDetailData();
            if (checkdetail(invPurchaseInvoiceDetailsValidate)) {
                invPurchaseInvoiceDetailsValidate.setMsg(null);
                invPurchaseInvoiceDetailsValidate = invGlobalService.validateQuantityColum(invPurchaseInvoiceDetailsValidate, (getInvInvoiceDTO().getLoadedSerial() != null), getInvInvoiceDTO().getScreenName(), getInvInvoiceDTO().getInventorySetupDTO().getNegativeSell(), getInvInvoiceDTO().getCompanyActivity());
                afterValidation(invPurchaseInvoiceDetailsValidate);
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateQuantityColum");
        }
    }

    public void validateActulQuantityColum(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            if (checkdetail(invPurchaseInvoiceDetailsValidate)) {
                invPurchaseInvoiceDetailsValidate.setMsg(null);
                invPurchaseInvoiceDetailsValidate = invGlobalService.validateActualQuantityColumn(invPurchaseInvoiceDetailsValidate);
                afterValidation(invPurchaseInvoiceDetailsValidate);
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateQuantityColum");
        }
    }

    public Boolean validateItems(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            checkDetailData();
            if (checkdetail(invPurchaseInvoiceDetailsValidate)) {
                invPurchaseInvoiceDetailsValidate.setMsg(null);
                invPurchaseInvoiceDetailsValidate = invGlobalService.validateItems(invPurchaseInvoiceDetailsValidate,
                        getInvInvoiceDTO().getInvInventoryId() != null ? getInvInvoiceDTO().getInvInventoryId().getId() : null,
                        getInvInvoiceDTO().getScreenName(), getInvInvoiceDTO().getInventorySetupDTO().getSellBuy(),
                        getInvInvoiceDTO().getInventorySetupDTO().getNegativeSell(), false, getInvInvoiceDTO().getCompanyActivity());
                afterValidation(invPurchaseInvoiceDetailsValidate);
                if (getUri().contains("invpurchaseinvoiceformCeramic")) {
                    changeFocus("form:invPurchaseInvoiceDetailsTable:" + (getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().size() - 1) + ":cartonsNumberv");
                } else {
                    changeFocus("form:invPurchaseInvoiceDetailsTable:" + (getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().size() - 1) + ":QuantityT");
                }
                if (invPurchaseInvoiceDetailsValidate.getMsg() == null || invPurchaseInvoiceDetailsValidate.getMsg().isEmpty()) {
                    posSetting();
                }

            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateItems");
            return null;
        }
        return true;
    }

    public void validateItems() {
        if (getUri().contains("invposinvoiceform")
                && (getInvInvoiceDTO().getMsg() == null || getInvInvoiceDTO().getMsg().isEmpty())) {
            getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected().setItemsBarcodesDetail(itemsBarcodesDetailsViewMap.get(getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected().getBarCode()));
            posSetting();
        }

    }

    public void detailBlur(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
//        validateItems(invPurchaseInvoiceDetailsValidate);
        if (invPurchaseInvoiceDetailsValidate == null) {
            invPurchaseInvoiceDetailsValidate = getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected();
        }
        if (invPurchaseInvoiceDetailsValidate != null) {
            if (invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() == null
                    && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetailTrans() == null
                    && (invPurchaseInvoiceDetailsValidate.getMsg() == null || invPurchaseInvoiceDetailsValidate.getMsg().isEmpty())) {
                changeFocus("form:payNet");
                deleteInvDetail(invPurchaseInvoiceDetailsValidate);
                calcAfterPayNet();
            } else {
                changeFocus("form:invPurchaseInvoiceDetailsTable:" + (getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().size() - 1) + ":invItemNameIn");
                invPurchaseInvoiceDetailsValidate.setMsg(null);
                replacedetail(invPurchaseInvoiceDetailsValidate);
            }
        }
    }

    public void RemainValue() {
        calculateRemainValue();
        changeFocus("form:invPurchaseInvoiceDetailsTable:saveB");
        setFirstLoad(Boolean.TRUE);
    }

    public void PayNet() {
        calcAfterPayNet();
        changeFocus("form:payed");
        setFirstLoad(Boolean.TRUE);
    }

    public void Discount() {
        calcAfterDiscount();
        setFirstLoad(Boolean.TRUE);
    }

    public void calcAfterDiscount() {
        try {
            getInvInvoiceDTO().setTotalNetAfterDiscount(getInvInvoiceDTO().getFinalNet().subtract(getInvInvoiceDTO().getDiscount()));
            calcAfterPayNet();
            calculateRemainValue();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "calcAfterDiscount");
        }
    }

    public void calcAfterPayNet() {
        try {
            getInvInvoiceDTO().setPayed(getInvInvoiceDTO().getTotalNet().subtract(getInvInvoiceDTO().getPayNet().add(getInvInvoiceDTO().getDiscount())));
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "calculateRemainValue");
        }
    }

    private Boolean checkdetail(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        if (invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() != null
                && !invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getBarcode().isEmpty()) {
            if (getInvInvoiceDTO().getScreenName() != null && !getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPOSINVOICEFORM)
                    && !getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSTRIP)) {
                for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                    if (detailDTO.getItemsBarcodesDetail() != null && detailDTO.getItemsBarcodesDetail().getBarcode().equals(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getBarcode()) && !detailDTO.getIndex().equals(invPurchaseInvoiceDetailsValidate.getIndex())) {
                        invPurchaseInvoiceDetailsValidate.setItemsBarcodesDetail(null);
                        replacedetail(invPurchaseInvoiceDetailsValidate);
                        errorMessage("تم ادخال الصنف من قبل");
                        return false;
                    }
                }
            }
            for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailsDeletedList()) {
                if (detailDTO.getItemsBarcodesDetail() != null && detailDTO.getItemsBarcodesDetail().equals(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail()) && !detailDTO.getIndex().equals(invPurchaseInvoiceDetailsValidate.getIndex())) {
                    invPurchaseInvoiceDetailsValidate.setItemsBarcodesDetail(null);
                    replacedetail(invPurchaseInvoiceDetailsValidate);
                    errorMessage("برجاء الحفظ أولا قبل ادخال الصنف المحذوف مرة أخري");
                    return false;
                }
            }
            return true;
        } else {
            errorMessage("برجاء اختيار الصنف أولا");
            return false;
        }
    }

    private void replacedetail(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
            if (detailDTO.getIndex().equals(invPurchaseInvoiceDetailsValidate.getIndex())) {
                int itemIndex = getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().indexOf(detailDTO);
                if (itemIndex != -1) {
                    getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().set(itemIndex, invPurchaseInvoiceDetailsValidate);
                }
            }
        }
    }

    public void updateRate() {
        try {
            setInvInvoiceDTO(invPurchaseInvoiceService.updateRate(getInvInvoiceDTO()));
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

    public void putCurrency() {
        try {
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invPurchaseInvoiceService.putCurrency(getInvInvoiceDTO()));
            errorMsg(getInvInvoiceDTO().getMsg());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "putCurrency");
        }
    }

    public List<InvOrganizationSiteDTO> completeCustomerFrom(String query) {
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getUserData().getUserBranch().getId(), Boolean.TRUE, 0, query);
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public void addNewCustomer() {
        InvOrganizationSiteDTO newCustomer = organizationSiteService.addNewCustomer(getOrganizationSiteId(), getInvOrganizationSiteDTOList(true, 0), getUserData().getUser());
        getInvInvoiceDTO().setOrganizationSiteId(newCustomer);
        getInvInvoiceDTO().setShowCustomerDate(Boolean.FALSE);
        invOrganizationSiteDTOList.add(newCustomer);
    }

    public void addNewCustomer2() {
        getInvInvoiceDTO().setOrganizationSiteId(organizationSiteService.addNewCustomer(getOrganizationSiteId(), getInvOrganizationSiteDTOList(true, 0), getUserData().getUser()));
        getInvInvoiceDTO().setShowCustomerDate(Boolean.FALSE);
    }

    public void addNewCustomerForRest() {
        InvOrganizationSiteDTO newCustomer = organizationSiteService.addNewCustomer(getOrganizationSiteId(), getInvOrganizationSiteDTOList(true, 0), getUserData().getUser());
        getInvInvoiceDTO().setOrganizationSiteId(newCustomer);
        getInvInvoiceDTO().setShowCustomerDate(Boolean.FALSE);
    }

    public void fillItemMap(Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, Integer invPurchaseInvoiceId) {
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : getItemsBarcodesDetailsViewList(invPurchaseInvoiceId)) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
    }

    public InvInvoiceDTO fillItemMap(InvInvoiceDTO invPurchaseInvoiceDTO) {
        try {
            if (invPurchaseInvoiceDTO.getInvInventoryId() != null) {
                invPurchaseInvoiceDTO.setItemsMap(new HashMap<>());
                List<QuantityItemsStore> quantityItemsStoresList = quantityItemsStoreService.findInventoryItemsList(invPurchaseInvoiceDTO.getInvInventoryId().getId(), getUserData().getUserBranch().getId(), invPurchaseInvoiceDTO.getInventorySetupDTO().getSellBuy());

                for (QuantityItemsStore quantityItemsStore : quantityItemsStoresList) {
                    invPurchaseInvoiceDTO.getItemsMap().put(quantityItemsStore.getItemId(), quantityItemsStore.getQty());
                }
            }
        } catch (Exception e) {
            saveError(e, "InvReturnPurchaseFormMB", "fillItemMap");
        }
        return invPurchaseInvoiceDTO;
    }

    public void deleteInvDetail(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            checkDetailData();
            if (invPurchaseInvoiceDetailsValidate.getId() != null) {
                getInvInvoiceDTO().getInvPurchaseInvoiceDetailsDeletedList().add(invPurchaseInvoiceDetailsValidate);
            }
//            checkDetailData();
            getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().remove(invPurchaseInvoiceDetailsValidate);
            getInvInvoiceDTO().setItemsNumber(Boolean.FALSE);
            updateSummition();
//            checkDetailData();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
        }
    }

    public void checkDetailData() {
        for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
            if (detailDTO.getItemsBarcodesDetailTrans() != null) {
                detailDTO.setItemsBarcodesDetail(detailDTO.getItemsBarcodesDetailTrans());
            }
        }
    }

    public void deleteInvDetailPOS() {
        try {
            if (getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected().getId() != null) {
                getInvInvoiceDTO().getInvPurchaseInvoiceDetailsDeletedList().add(getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected());
            }
            for (InvDetailDTO detailEntity : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
            }
            getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().remove(getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected());
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
        }
    }

    public void deleteAllSelection() {
        if (getInvInvoiceDTO().getInvPurchaseInvoiceDetailListSelected() != null) {
            for (InvDetailDTO detailEntity : getInvInvoiceDTO().getInvPurchaseInvoiceDetailListSelected()) {
                if (detailEntity.getId() != null) {
                    getInvInvoiceDTO().getInvPurchaseInvoiceDetailsDeletedList().add(detailEntity);
                }
                getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().remove(detailEntity);
            }
            updateSummition();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
    }

    public void updateSummition() {
        setInvInvoiceDTO(invGlobalService.updateSummition(getInvInvoiceDTO()));
    }

    public void printBarcode380_125() {
        barcodeType = 1;
        printBarcodeNew();
    }

    public void printBarcode380_25() {
        barcodeType = 0;
        printBarcodeNew();
    }

    ////  Print Barcode
    public void printBarcodeNew() {

        try {
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
                List<InvDetailDTO> listNew = new ArrayList<>();
                for (InvDetailDTO dTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                    if (dTO.getQuantity() != null && dTO.getQuantity().compareTo(BigDecimal.ZERO) == 1) {
                        for (int i = 0; i < dTO.getQuantity().intValue(); i++) {

                            InvDetailDTO invDetailDTO = new InvDetailDTO();
                            invDetailDTO = (InvDetailDTO) dTO.clone();

                            if (invDetailDTO.getItemsBarcodesDetail() == null && invDetailDTO.getItemsBarcodesDetailTrans() != null) {
                                invDetailDTO.setItemsBarcodesDetail(invDetailDTO.getItemsBarcodesDetailTrans());
                            }
                            invDetailDTO.setItemCode(invDetailDTO.getItemsBarcodesDetail().getBarcodeMain());
                            invDetailDTO.setItemName(invDetailDTO.getItemName());
                            invDetailDTO.setCost(invDetailDTO.getItemsBarcodesDetail() == null ? null : invDetailDTO.getItemsBarcodesDetail().getSellPrice());
                            listNew.add(invDetailDTO);
                        }
                    }
                }
                if (barcodeType == 0) {
                    fillReport(prepareReport(), getUserData().getReportPath() + "itemBarcodeIgma.jasper", listNew, "pdf");
                } else if (barcodeType == 1) {
                    fillReport(prepareReport(), getUserData().getReportPath() + "itemBarcodeIgma1.jasper", listNew, "pdf");
                }
            }
        } catch (Exception e) {

        }
    }

    public HashMap prepareReport() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("companyName", getUserData().getCompany().getName());
            return hashMap;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "prepareReport");
            return null;
        }
    }

    ////////// load dialogue //////////
    public void loadDailogue() {
        this.setDataModelInvDailogueData(new LazyDataModel<InvDailogueData>() {
            private static final long serialVersionUID = 1L;

            @Override
            public InvDailogueData getRowData(String rowKey) {
                for (InvDailogueData invDailogueData : getInvInvoiceDTO().getInvDailogueDataList()) {
                    if (invDailogueData.getIndex().toString().equals(rowKey)) {
                        return invDailogueData;
                    }
                }

                return null;
            }

            @Override
            public Integer getRowKey(InvDailogueData invDailogueData) {
                return invDailogueData.getIndex();
            }

            @Override
            public List<InvDailogueData> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(invDialogueService.getTotalRegistors(getUserData().getUserBranch().getId(), getInvInvoiceDTO().getInvInventoryId().getId(), filters, getInvInvoiceDTO().getInventorySetupDTO().getSellBuy(), getInvInvoiceDTO().getScreenName()).intValue());
                List<InvDailogueData> dailogueDatas = invDialogueService.loadInvDailogueData(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), filters, getInvInvoiceDTO().getInventorySetupDTO(), getInvInvoiceDTO().getScreenName(), getInvInvoiceDTO().getInvInventoryId().getId());
                getInvInvoiceDTO().setInvDailogueDataList(dailogueDatas);
                return dailogueDatas;
            }

        });
        OpenDlg("loaddlg");
    }

    public void hishis() {
        checkDetailData();
        System.out.print("ttt");
    }

    public void findInvInvoiceDto() {
        setInvInvoiceDTO(invPurchaseInvoiceService.getInvPurchaseInvoiceDTOBySupplierInvoiceNumber(getInvInvoiceDTO(), getUserData().getUserBranch().getId()));

    }

    public void MonthTargetDlg() {
        getInvInvoiceDTO().getInvMonthTargetDTO().setDateFrom(new Date());
        getInvInvoiceDTO().setInvMonthTargetDTO(monthTargetService.monthTargetDTOByduration(getInvInvoiceDTO()));
        if (getInvInvoiceDTO().getInvMonthTargetDTO() != null && getInvInvoiceDTO().getInvMonthTargetDTO().getTarget() != null) {
            getInvInvoiceDTO().getInvMonthTargetDTO().setActualTotal(invPurchaseInvoiceService.returnInvInvoiceSummarySummationDuration(getInvInvoiceDTO().getBranchId(), getInvInvoiceDTO().getInvMonthTargetDTO().getType(), getInvInvoiceDTO().getInvMonthTargetDTO().getDateFrom(), getInvInvoiceDTO().getInvMonthTargetDTO().getDateTo()));
        }
        OpenDlg("loadMonthTargetdlg");
    }

    public void loadDlg() {
        if (getInvInvoiceDTO().getInvDailogeDataSelected() != null || getInvInvoiceDTO().getNeedLoadedSerial() != null || (getInvInvoiceDTO().getInvDailogueDataListSelected() != null && !getInvInvoiceDTO().getInvDailogueDataListSelected().isEmpty())) {
            setInvInvoiceDTO(invDialogueService.loadDlg(getInvInvoiceDTO()));
            fillLists();
            if (!getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVUPDATE)) {
                putCurrency();
            }
            CloseDlg("loaddlg");
            errorMsg(getInvInvoiceDTO().getMsg());
        }
    }

    public void printBarcode8CM() {
        barcodeCounter();
        getInvInvoiceDTO().setBarcodeSelect(Boolean.TRUE);
        InvInventoryDTO inventoryDTO = invInventoryService.findInventoryDTO(getInvInvoiceDTO().getInvInventoryId().getId());
        getInvInvoiceDTO().setInvInventoryId(inventoryDTO);
        getInvInvoiceDTO().setPrintType(4);
        iReportService.printReportInvoice(getInvInvoiceDTO(), getUserData());
    }

    public void printBarcode() {
        barcodeCounter();
        getInvInvoiceDTO().setBarcodeSelect(Boolean.TRUE);
        InvInventoryDTO inventoryDTO = invInventoryService.findInventoryDTO(getInvInvoiceDTO().getInvInventoryId().getId());
        getInvInvoiceDTO().setInvInventoryId(inventoryDTO);
        iReportService.printReportInvoice(getInvInvoiceDTO(), getUserData());
    }

    public void printResetWithOutTotalRow() throws CloneNotSupportedException {
        barcodeCounter();
        getInvInvoiceDTO().setBarcodeSelect(Boolean.TRUE);

        InvInvoiceDTO invoiceDTOPrint = (InvInvoiceDTO) getInvInvoiceDTO().clone();

        List<InvDetailDTO> invDetailDTOList = new ArrayList<>();

        for (InvDetailDTO detailDTO : invoiceDTOPrint.getInvPurchaseInvoiceDetailList()) {
            if (detailDTO.getItemId().getPrintShow() == null || detailDTO.getItemId().getPrintShow() == 0) {
                invDetailDTOList.add(detailDTO);
            }
        }
        getInvInvoiceDTO().setPrintType(3);
        getInvInvoiceDTO().setInvPurchaseInvoiceDetailList(invDetailDTOList);
        InvInventoryDTO inventoryDTO = invInventoryService.findInventoryDTO(getInvInvoiceDTO().getInvInventoryId().getId());
        getInvInvoiceDTO().setInvInventoryId(inventoryDTO);
        iReportService.printReportInvoice(getInvInvoiceDTO(), getUserData());
    }

    public void updateCustomerData() {
        putCurrency();
//        findAccountStatement();
    }

    public void findAccountStatement() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getOrganizationSiteId() != null && getInvInvoiceDTO().getOrganizationSiteId().getId() != null) {
            getInvInvoiceDTO().setAccountStatement(supplierAccountService.getAccountForOrganizationSiteNew(getInvInvoiceDTO().getOrganizationSiteId().getId()));
        }
    }

    public void print() {
        getInvInvoiceDTO().setBarcodeSelect(Boolean.FALSE);
        InvInventoryDTO inventoryDTO = invInventoryService.findInventoryDTO(getInvInvoiceDTO().getInvInventoryId().getId());
        getInvInvoiceDTO().setInvInventoryId(inventoryDTO);
        iReportService.printReportInvoice(getInvInvoiceDTO(), getUserData());
    }

    public void printStrip() {
        getInvInvoiceDTO().setBarcodeSelect(Boolean.TRUE);
        InvInventoryDTO inventoryDTO = invInventoryService.findInventoryDTO(getInvInvoiceDTO().getInvInventoryId().getId());
        getInvInvoiceDTO().setInvInventoryId(inventoryDTO);
        iReportService.printReportInvoice(getInvInvoiceDTO(), getUserData());
    }

    private void barcodeCounter() {
        List<InvDetailDTO> barcodeCounterList = new ArrayList<>();
        for (InvDetailDTO detail : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
            for (Integer x = 0; x < detail.getQuantity().intValue(); x++) {
                barcodeCounterList.add(detail);
            }
        }
        getInvInvoiceDTO().setInvPurchaseInvoiceDetailBarcodeList(null);
        getInvInvoiceDTO().setInvPurchaseInvoiceDetailBarcodeList(barcodeCounterList);
        barcodeCounterList = null;

    }

    public void printList() {
        if (getUri().contains("invpurchaseinvoice_1")
                || getUri().contains("invsalesinvoice_1")
                || getUri().contains("invreturnpurchase_1")
                || getUri().contains("invposinvoiceform")
                || getUri().contains("invreturnsales_1")) {
            getContext().getSessionMap().put("invPurchaseInvoiceIdSeclected", getInvInvoiceDTOSelected().getId());
        } else if (getUri().contains("invpurchaseorder_1") || getUri().contains("invSalesOrderForm")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invPurchaseOrderSelected"));
            setInvInvoiceDTO(invPurchaseOrderService.findInvPurchaseOrderById(getInvInvoiceDTO(), getUserData().getUser()));
        } else if (getUri().contains("invaddingorder") || getUri().contains("invpermissionorder_1")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invAddingOrderSelected"));
            setInvInvoiceDTO(invAddingOrderService.findInvAddingOrderById(getInvInvoiceDTO(), getUserData().getUser()));
        } else if (getUri().contains("invQuotation_1")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invQuotationSelected"));
            setInvInvoiceDTO(invQuotationService.findInvPurchaseOrderById(getInvInvoiceDTO(), getUserData().getUser()));
        } else if (getUri().contains("invReservationForm")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invReservationSelected"));
            setInvInvoiceDTO(invReservationService.findInvPurchaseInvoiceById(getInvInvoiceDTO(), getUserData().getUser()));
        } else if (getUri().contains("invtransferInventory")) {
            getContext().getSessionMap().put("invTransferSelected", getInvInvoiceDTOSelected().getId());
        }
        fillInvInvoiceDetailDTO();
        print();
    }

    public List<ItemsBarcodesDetailsView> completeItemsDatas(String query) {
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViews = completeItemsData(query, getInvInvoiceDTO().getParent());
        return itemsBarcodesDetailsViews;
    }

    public List<ItemsBarcodesDetailsView> completeItemsBarcodesDetailsView(String query) {
        List<ItemsBarcodesDetailsView> autoCompleteInvItem = null;
        if (query.trim().isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlg4').show();");
        } else {
            autoCompleteInvItem = autoCompleteService.autoCompleteItemsBarcodesDetailsViewBranchId(getUserData().getUserBranch().getId(), query, getInvInvoiceDTO().getInvInventoryId() == null ? null : getInvInvoiceDTO().getInvInventoryId().getId());

            setItemsBarcodesDetailsViewConverter(new ItemsBarcodesDetailsViewConverter(autoCompleteInvItem));
        }
        return autoCompleteInvItem;
    }

    public List<ItemsBarcodesDetailsView> completeItemsDatas2(String query) {

        return getItemsBarcodesDetailsViewList();
    }

    protected String validateloginDistributedScreens(ScreenNameClassEnum screenNameClassEnum) {
        return invPurchaseInvoiceService.validateloginDistributedScreens(getUserDataDTO(), screenNameClassEnum);
    }

    public List<InvOrganizationSiteDTO> completeFromCustomer(String query) {
        return AutoComplete.completOrgSiteDTO(query, getInvOrganizationSiteDTOList(Boolean.TRUE, 0), getOrganizationSiteDTOConvertor());

    }

    public List<InvOrganizationSiteDTO> completeToCustomer(String query) {
        return AutoComplete.completOrgSiteDTO(query, getInvOrganizationSiteDTOList(Boolean.TRUE, 0), getOrganizationSiteDTOConvertor());
    }

    public List<InventoryDelegatorDTO> completeDelegatorSales(String query) {
        return AutoComplete.completeDelegatorDTO(query, getInventoryDelegatorList(1), getInvDelegatorDTOConvertor());

    }

    public List<InventoryDelegatorDTO> completeDelegatorPurchase(String query) {
        return AutoComplete.completeDelegatorDTO(query, getInventoryDelegatorList(0), getInvDelegatorDTOConvertor());

    }

    public List<SymbolDTO> completeFromRegion(String query) {
        try {
            List<SymbolDTO> regionListFilter = getRegionList();
            if (query == null || query.trim().equals("")) {

                regionConverter = new SymbolDTOConverter(regionListFilter);
                return regionListFilter;
            }
            List<SymbolDTO> regionListFiltered = new ArrayList<>();

            String nameAr;
            Integer code;
            SymbolDTO regionFilter;
            for (int i = 0; i < getRegionList().size(); i++) {
                regionFilter = regionListFilter.get(i);
                nameAr = regionFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!regionListFiltered.contains(regionFilter)) {
                        regionListFiltered.add(regionFilter);
                    }
                }

                code = regionFilter.getSerial();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!regionListFiltered.contains(regionFilter)) {
                        regionListFiltered.add(regionFilter);
                    }
                }
            }

            regionConverter = new SymbolDTOConverter(regionListFiltered);
            return regionListFiltered;
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "completeRegion");
            return null;
        }
    }

    public List<SymbolDTO> completeToRegion(String query) {
        try {
            List<SymbolDTO> regionListFilter = getRegionList();
            if (query == null || query.trim().equals("")) {

                regionConverter = new SymbolDTOConverter(regionListFilter);
                return regionListFilter;
            }
            List<SymbolDTO> regionListFiltered = new ArrayList<>();

            String nameAr;
            Integer code;
            SymbolDTO regionFilter;
            for (int i = 0; i < getRegionList().size(); i++) {
                regionFilter = regionListFilter.get(i);
                nameAr = regionFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!regionListFiltered.contains(regionFilter)) {
                        regionListFiltered.add(regionFilter);
                    }
                }

                code = regionFilter.getSerial();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!regionListFiltered.contains(regionFilter)) {
                        regionListFiltered.add(regionFilter);
                    }
                }
            }

            regionConverter = new SymbolDTOConverter(regionListFiltered);
            return regionListFiltered;
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "completeRegion");
            return null;
        }
    }

    public InvDetailDTO checkdiscountvalue(InvDetailDTO invDetailDTO) {
        if (invDetailDTO.getValueRowDiscount() != null) {
            setCheckDiscountRate(true);
            invDetailDTO.setDiscount((invDetailDTO.getValueRowDiscount().divide(invDetailDTO.getNet(), 3, RoundingMode.HALF_UP)).multiply(new BigDecimal(100)));
            invDetailDTO.setNet(invDetailDTO.getTotalBeforeDiscount().subtract(invDetailDTO.getValueRowDiscount()));
        } else {
            setCheckDiscountRate(false);
            invDetailDTO.setDiscount(null);
        }

        afterValidation(invDetailDTO);
        return invDetailDTO;
    }

    public InvDetailDTO checkdiscountrate(InvDetailDTO invDetailDTO) {
        if (invDetailDTO.getDiscount() != null) {
            setCheckDiscountValue(true);
            BigDecimal discount = (invDetailDTO.getTotalBeforeDiscount()).multiply(invDetailDTO.getDiscount().divide(new BigDecimal(100)));
            invDetailDTO.setValueRowDiscount(new BigDecimal(discount.setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            invDetailDTO.setNet(invDetailDTO.getTotalBeforeDiscount().subtract(discount));

        } else {
            setCheckDiscountValue(false);
            invDetailDTO.setValueRowDiscount(null);
        }

        afterValidation(invDetailDTO);
        return invDetailDTO;
    }

    public List<CostCenterDTO> completeCostCenters(String query) {
        return AutoComplete.completeCostCenters(query, getCostCenterList(), getCostCenterConverter());
    }

    public void findAllItemsBarcodesDetailsViewBranchId() {
        setItemsBarcodesDetailsViewListForSearch(autoCompleteService.findAllItemsBarcodesDetailsViewBranchId(getUserData().getUserBranch().getId(), getItemName()));
        setItemsBarcodesDetailsViewConverterForSearch(getItemsBarcodesDetailsViewConverterForSearch());
    }

    public void putItemsBarcodesDetailsViewBranchId() {
        if (getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected() != null) {
            getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected().setItemsBarcodesDetail(getItemsBarcodesDetailsViewListForSearchSelected());
            validateItems(getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected());
            for (InvDetailDTO invDetailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                if (invDetailDTO.getItemsBarcodesDetail() == null && invDetailDTO.getItemsBarcodesDetailTrans() != null) {
                    invDetailDTO.setItemsBarcodesDetail(invDetailDTO.getItemsBarcodesDetailTrans());
                } else if (invDetailDTO.getItemsBarcodesDetail() != null && invDetailDTO.getItemsBarcodesDetailTrans() == null) {
                    invDetailDTO.setItemsBarcodesDetailTrans(invDetailDTO.getItemsBarcodesDetail());
                }
            }
        }
    }

    public List<ItemsBarcodesDetailsView> completeItemsData(String query, Integer invPurchaseInvoiceId) {
        return AutoComplete.completeItemsData(query, getItemsBarcodesDetailsViewList(invPurchaseInvoiceId), getItemsBarcodesDetailsViewConverter());
    }

    public List<SymbolDTO> completeSymbolUnit(String query) {
        return AutoComplete.completeSymbolDTO(query, getUnitList(), getUnitConverter());
    }

    public List<InvOrganizationSiteDTO> completeSuppllier(String query) {
        return AutoComplete.completOrgSiteDTO(query, getInvOrganizationSiteDTOList(Boolean.TRUE, 1), getOrganizationSiteDTOConvertor());
    }

    public List<CurrencyDTO> completeCurrency(String query) {
        return AutoComplete.completeCurrency(query, getCurrencyDTOList(), getCurrencyConverter());

    }

    public List<GlAdminUnitDTO> completeAdminUnits(String query) {
        return AutoComplete.completeAdminUnits(query, getAdminUnitList(), getAdminUnitConverter());
    }

    public List<InvInventoryDTO> completeInventory(String query) {
        return AutoComplete.completeInventoryDTO(query, getInvInventoryDTOList(), getInvInventoryDTOConverter());
    }

    public List<InvInventoryDTO> completeInventoryTo(String query) {
        return AutoComplete.completeInventoryDTO(query, getInvInventoryDTOListTo(), getInvInventoryDTOConverterTO());
    }

    public List<GlBankDTO> completeGlBank(String query) {
        return AutoComplete.completeGlBank(query, getGlBankList(), getGlBankConverter());
    }

    /**
     * @return the invOrganizationSiteList
     */
    public List<InvOrganizationSite> getInvOrganizationSiteList() {
        if (invOrganizationSiteList == null) {
            invOrganizationSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, getSiteType()); // المورد
            setSupplierConvertor(new InvOrganizationSiteConverter(invOrganizationSiteList));
        }
        return invOrganizationSiteList;
    }

    /**
     * @param invOrganizationSiteList the invOrganizationSiteList to set
     */
    public void setInvOrganizationSiteList(List<InvOrganizationSite> invOrganizationSiteList) {
        this.invOrganizationSiteList = invOrganizationSiteList;
    }

    /**
     * @param active
     * @param type
     * @return the invOrganizationSiteList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOList(Boolean active, Integer type) {
        if (invOrganizationSiteDTOList == null) {
            setInvOrganizationSiteDTOList(organizationSiteService.getorganizationSiteByBranchIdDTO(getUserlogin().getBranchId().getId(), active, type));
            setOrganizationSiteDTOConvertor(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList));
        }
        return invOrganizationSiteDTOList;
    }

    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOList() {
        if (invOrganizationSiteDTOList == null) {
            invOrganizationSiteDTOList = new ArrayList<>();
        }
        return invOrganizationSiteDTOList;
    }

    /**
     * @param invOrganizationSiteList the invOrganizationSiteDTOList to set
     */
    public void setInvOrganizationSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        this.invOrganizationSiteDTOList = invOrganizationSiteDTOList;
    }

    public void chagePaymentType() {
        if (getInvInvoiceDTO().getPaymentType() == 1) {
            getInvInvoiceDTO().setGlBankId(null);
        } else {
            getInvInvoiceDTO().setGlBankId(getUserData().getGlBankDTODefault());
        }
    }

    /**
     * @return the invItemList
     */
    public List<InvItem> getInvItemList() {
        if (invItemList == null) {
            invItemList = invItemService.getInvItemByBranchId(getUserData().getUserBranch().getId());
        }
        return invItemList;
    }

    /**
     * @param invItemList the invItemList to set
     */
    public void setInvItemList(List<InvItem> invItemList) {
        this.invItemList = invItemList;
    }

    public List<ItemsBarcodesDetailsView> getItemsBarcodesDetailsViewList(Integer invPurchaseInvoiceId) {
        if (itemsBarcodesDetailsViewList == null || itemsBarcodesDetailsViewList.isEmpty()) {
//            if (invPurchaseInvoiceId == null) {
            itemsBarcodesDetailsViewList = getItemsBarcodesDetailsViewService().findItemsBarcodesDetailsViewBranchId(getUserData().getUserBranch().getId());
//            } else {
//                itemsBarcodesDetailsViewList = getItemsBarcodesDetailsViewService().findItemsBarcodesDetailsViewByInvoiceId(invPurchaseInvoiceId, getInvInvoiceDTO().getScreenName());
//            }
            setItemsBarcodesDetailsViewConverter(new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViewList));
        }
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

    public List<InventoryDelegatorDTO> getInventoryDelegatorList(Integer type) {
        if (inventoryDelegatorList == null) {
            setInventoryDelegatorList(invDelegatorService.getDelegatorDTOByBranch(getUserData().getUserBranch().getId(), type));// )مندوب المشتريات
            setInvDelegatorDTOConvertor(new InvDelegatorDTOConvertor(inventoryDelegatorList));
        }
        return inventoryDelegatorList;
    }

    public void setInventoryDelegatorList(List<InventoryDelegatorDTO> inventoryDelegatorList) {
        this.inventoryDelegatorList = inventoryDelegatorList;
    }

    public InvOrganizationSiteConverter getSupplierConvertor() {
        return supplierConvertor;
    }

    public void setSupplierConvertor(InvOrganizationSiteConverter supplierConvertor) {
        this.supplierConvertor = supplierConvertor;
    }

    /**
     * @return the siteType
     */
    public Integer getSiteType() {
        return siteType;
    }

    /**
     * @param siteType the siteType to set
     */
    public void setSiteType(Integer siteType) {
        this.siteType = siteType;
    }

    /**
     * @return the itemsBarcodesDetailsViewService
     */
    public ItemsBarcodesDetailsViewService getItemsBarcodesDetailsViewService() {
        return itemsBarcodesDetailsViewService;
    }

    /**
     * @param itemsBarcodesDetailsViewService the
     * itemsBarcodesDetailsViewService to set
     */
    public void setItemsBarcodesDetailsViewService(ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService) {
        this.itemsBarcodesDetailsViewService = itemsBarcodesDetailsViewService;
    }

    /**
     * @return the invInventoryList
     */
    public List<InvInventory> getInvInventoryList() {
        if (invInventoryList == null || invInventoryList.isEmpty()) {
            invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), getUserData().getUserBranch().getId());
            invInventoryConverter = new InvInventoryConverter(invInventoryList);
        }
        return invInventoryList;
    }

    /**
     * @param invInventoryList the invInventoryList to set
     */
    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    /**
     * @return the invInventoryConverter
     */
    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    /**
     * @param invInventoryConverter the invInventoryConverter to set
     */
    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    /**
     * @return the organizationSiteDTOConvertor
     */
    public InvOrganizationSiteDTOConverter getOrganizationSiteDTOConvertor() {
        return organizationSiteDTOConvertor;
    }

    /**
     * @param organizationSiteDTOConvertor the organizationSiteDTOConvertor to
     * set
     */
    public void setOrganizationSiteDTOConvertor(InvOrganizationSiteDTOConverter organizationSiteDTOConvertor) {
        this.organizationSiteDTOConvertor = organizationSiteDTOConvertor;
    }

    /**
     * @return the regionConverter
     */
    public SymbolDTOConverter getRegionConverter() {
        return regionConverter;
    }

    /**
     * @param regionConverter the regionConverter to set
     */
    public void setRegionConverter(SymbolDTOConverter regionConverter) {
        this.regionConverter = regionConverter;
    }

    /**
     * @return the regionList
     */
    public List<SymbolDTO> getRegionList() {
        if (regionList == null) {
            regionList = symbolService.getAllStoreRegionDTOByCompanyId(getUserData().getCompany().getId());
            setRegionConverter(new SymbolDTOConverter(regionList));
        }
        return regionList;
    }

    /**
     * @param regionList the regionList to set
     */
    public void setRegionList(List<SymbolDTO> regionList) {
        this.regionList = regionList;
    }

    /**
     * @return the invInventoryDTOList
     */
    public List<InvInventoryDTO> getInvInventoryDTOList() {
        if (invInventoryDTOList == null) {
            invInventoryDTOList = getUserData().getInventoryDTOList();
            setInvInventoryDTOConverter(new InvInventoryDTOConverter(invInventoryDTOList));
        }
        return invInventoryDTOList;
    }

    /**
     * @param invInventoryDTOList the invInventoryDTOList to set
     */
    public void setInvInventoryDTOList(List<InvInventoryDTO> invInventoryDTOList) {
        this.invInventoryDTOList = invInventoryDTOList;
    }

    /**
     * @return the invInventoryDTOConverter
     */
    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    /**
     * @param invInventoryDTOConverter the invInventoryDTOConverter to set
     */
    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
    }

    /**
     * @return the currencyDTOList
     */
    public List<CurrencyDTO> getCurrencyDTOList() {
        if (currencyDTOList == null) {
            currencyDTOList = currencyService.getAllCurrenciesWithLocalCurrencyHavingRatesByCompanyIdDTO(getUserData().getCompany().getId()); // العملات
            setCurrencyConverter(new CurrencyDTOConverter(currencyDTOList));
        }
        return currencyDTOList;
    }

    /**
     * @param currencyDTOList the currencyDTOList to set
     */
    public void setCurrencyDTOList(List<CurrencyDTO> currencyDTOList) {
        this.currencyDTOList = currencyDTOList;
    }

    /**
     * @return the currencyConverter
     */
    public CurrencyDTOConverter getCurrencyConverter() {
        return currencyConverter;
    }

    /**
     * @param currencyConverter the currencyConverter to set
     */
    public void setCurrencyConverter(CurrencyDTOConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    /**
     * @return the itemsBarcodeMap
     */
    public Map<String, ItemsBarcodesDetailsView> getItemsBarcodeMap() {
        if (itemsBarcodesDetailsViewList == null || itemsBarcodesDetailsViewList.isEmpty()) {
            itemsBarcodeMap = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdMap(getUserData().getUserBranch().getId());
        }
        return itemsBarcodeMap;
    }

    /**
     * @param itemsBarcodeMap the itemsBarcodeMap to set
     */
    public void setItemsBarcodeMap(Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap) {
        this.itemsBarcodeMap = itemsBarcodeMap;
    }

    /**
     * @return the costCenterList
     */
    public List<CostCenterDTO> getCostCenterList() {
        if (costCenterList == null) {
            costCenterList = costCenterService.getActiveSubCostCentersByBranchDTO(getUserData().getUserBranch().getId()); // مراكز التكلفة
            setCostCenterConverter(new CostCenterDTOConverter(costCenterList));
        }
        return costCenterList;
    }

    /**
     * @param costCenterList the costCenterList to set
     */
    public void setCostCenterList(List<CostCenterDTO> costCenterList) {
        this.costCenterList = costCenterList;
    }

    /**
     * @return the adminUnitList
     */
    public List<GlAdminUnitDTO> getAdminUnitList() {
        if (adminUnitList == null) {
            adminUnitList = glAdminUnitService.getAllSubAdminUnitByBranchIdActiveDTO(getUserData().getUserBranch().getId()); // الوحدات الادارية
            setAdminUnitConverter(new GlAdminUnitDTOConverter(adminUnitList));
        }
        return adminUnitList;
    }

    /**
     * @param adminUnitList the adminUnitList to set
     */
    public void setAdminUnitList(List<GlAdminUnitDTO> adminUnitList) {
        this.adminUnitList = adminUnitList;
    }

    /**
     * @return the invItemDTOList
     */
    public List<InvItemDTO> getInvItemDTOList() {
        return invItemDTOList;
    }

    /**
     * @param invItemDTOList the invItemDTOList to set
     */
    public void setInvItemDTOList(List<InvItemDTO> invItemDTOList) {
        this.invItemDTOList = invItemDTOList;
    }

    /**
     * @return the unitList
     */
    public List<SymbolDTO> getUnitList() {
        if (unitList == null) {
            unitList = symbolService.getUnitsByCompanyIdDTO(getUserData().getCompany().getId()); // الوحدات: جرام, طن
            setUnitConverter(new SymbolDTOConverter(unitList));
        }
        return unitList;
    }

    /**
     * @param unitList the unitList to set
     */
    public void setUnitList(List<SymbolDTO> unitList) {
        this.unitList = unitList;
    }

    /**
     * @return the costCenterConverter
     */
    public CostCenterDTOConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    /**
     * @param costCenterConverter the costCenterConverter to set
     */
    public void setCostCenterConverter(CostCenterDTOConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }

    /**
     * @return the adminUnitConverter
     */
    public GlAdminUnitDTOConverter getAdminUnitConverter() {
        return adminUnitConverter;
    }

    /**
     * @param adminUnitConverter the adminUnitConverter to set
     */
    public void setAdminUnitConverter(GlAdminUnitDTOConverter adminUnitConverter) {
        this.adminUnitConverter = adminUnitConverter;
    }

    /**
     * @return the unitConverter
     */
    public SymbolDTOConverter getUnitConverter() {
        return unitConverter;
    }

    /**
     * @param unitConverter the unitConverter to set
     */
    public void setUnitConverter(SymbolDTOConverter unitConverter) {
        this.unitConverter = unitConverter;
    }

    public List<GlBankDTO> getGlBankList() {
        if (glBankList == null) {
            glBankList = getUserData().getGlBankDTOList(); //  ودا بردوا
            setGlBankConverter(new GlBankDTOConverter(glBankList));
        }
        return glBankList;
    }

    public void setGlBankList(List<GlBankDTO> glBankList) {
        this.glBankList = glBankList;
    }

    /**
     * @return the glBankConverter
     */
    public GlBankDTOConverter getGlBankConverter() {
        return glBankConverter;
    }

    /**
     * @param glBankConverter the glBankConverter to set
     */
    public void setGlBankConverter(GlBankDTOConverter glBankConverter) {
        this.glBankConverter = glBankConverter;
    }

    /**
     * @return the invDelegatorDTOConvertor
     */
    public InvDelegatorDTOConvertor getInvDelegatorDTOConvertor() {
        return invDelegatorDTOConvertor;
    }

    /**
     * @param invDelegatorDTOConvertor the invDelegatorDTOConvertor to set
     */
    public void setInvDelegatorDTOConvertor(InvDelegatorDTOConvertor invDelegatorDTOConvertor) {
        this.invDelegatorDTOConvertor = invDelegatorDTOConvertor;
    }

    /**
     * @return the invInvoiceDTO
     */
    public InvInvoiceDTO getInvInvoiceDTO() {
        if (invInvoiceDTO == null) {
            invInvoiceDTO = new InvInvoiceDTO();
            invInvoiceDTO = settingInventory(invInvoiceDTO);
        }
        return invInvoiceDTO;
    }

    public InvInvoiceDTO settingInventory(InvInvoiceDTO invInvoiceDTO) {
        invInvoiceDTO.setCompanyActivity(getUserData().getUserBranch().getCompanyActivity());

        findScreenName();

        if (invInvoiceDTO.getId() == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            System.out.println(formatter.format(date));
            invInvoiceDTO.setDate(date);
        }

        invInvoiceDTO.setInventorySetupDTO(inventorySetupService.getInventoryByBranchIdDTO(getUserData().getUserBranch().getId()));

        if (getInvInvoiceDTO().getInventorySetupDTO().getDefaultSalesTax() != null) {
            invInvoiceDTO.setTaxflag(getInvInvoiceDTO().getInventorySetupDTO().getDefaultSalesTax());
        }
        invInvoiceDTO.setReportManagement(reportManagementService.reportManagementByBranch(getUserData().getUserBranch().getId()));

        invInvoiceDTO.setBranchId(getUserData().getUserBranch().getId());

        return invInvoiceDTO;
    }

    private void findScreenName() {
        if (getUri().contains("invpurchaseinvoice_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVPURCHASEINVOICEFORM);
            getInvInvoiceDTO().setType(0);
            getInvInvoiceDTO().setBarcodeSelect(Boolean.FALSE);
        } else if (getUri().contains("invsalesinvoice_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVSALESINVOICEFORM);
            getInvInvoiceDTO().setType(1);
        } else if (getUri().contains("invposinvoiceform")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVPOSINVOICEFORM);
            getInvInvoiceDTO().setType(1);
        } else if (getUri().contains("invposrestform")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVPOSREST);
            getInvInvoiceDTO().setType(1);
        } else if (getUri().contains("invreturnpurchase_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVRETURNPURCHASEFORM);
            getInvInvoiceDTO().setType(2);
        } else if (getUri().contains("invreturnsales_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVRETURNSALESFORM);
            getInvInvoiceDTO().setType(3);
        } else if (getUri().contains("invpurchaseorder_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVPURCHASEORDERFORM);
            getInvInvoiceDTO().setType(0);
        } else if (getUri().contains("invSalesOrderForm")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVSALESORDERFORM);
            getInvInvoiceDTO().setType(1);
        } else if (getUri().contains("invaddingorder")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVADDINGORDERFORM);
            getInvInvoiceDTO().setType(0);
        } else if (getUri().contains("invpermissionorder_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVPERMISSIONORDERFORM);
            getInvInvoiceDTO().setType(1);
        } else if (getUri().contains("invQuotation_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVQUOTATIONFORM);
            getInvInvoiceDTO().setType(1);
        } else if (getUri().contains("invReservationForm")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVRESERVATIONFORM);
        } else if (getUri().contains("invtransfer")) {
            if (getUri().contains("invtransferInventory")) {
                getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVTRANSFERINVENTORY);
                getInvInvoiceDTO().setType(0);
            } else {
                getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVTRANSFER);
                getInvInvoiceDTO().setType(1);
            }
        } else if (getUri().contains("invMonthTarget")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVMONTHTARGET);
        } else if (getUri().contains("invstrip")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVSTRIP);
            getInvInvoiceDTO().setType(0);
        } else if (getUri().contains("invupdate")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVUPDATE);
        } else if (getUri().contains("invopeningbalanceitem")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM);
        }
    }

    public void fillInvInvoiceDTO() {
        fillInvInvoiceDetailDTO();
        setInvInvoiceDTO(settingInventory(getInvInvoiceDTO()));
        fillLists();
        if (!ScreenNameClassEnum.INVTRANSFER.equals(getInvInvoiceDTO().getScreenName())
                && !ScreenNameClassEnum.INVSTRIP.equals(getInvInvoiceDTO().getScreenName())
                && !ScreenNameClassEnum.INVUPDATE.equals(getInvInvoiceDTO().getScreenName())
                && !ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM.equals(getInvInvoiceDTO().getScreenName())
                && !ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(getInvInvoiceDTO().getScreenName())) {
            putCurrency();
        }
    }


    private void fillInvInvoiceDetailDTO() {
        if (getUri().contains("invpurchaseinvoice_1")
                || getUri().contains("invsalesinvoice_1")
                || getUri().contains("invposinvoiceform")
                || getUri().contains("invreturnpurchase_1")
                || getUri().contains("invreturnsales_1")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invPurchaseInvoiceIdSeclected"));
            setInvInvoiceDTO(invPurchaseInvoiceService.findInvPurchaseInvoiceById(getInvInvoiceDTO(), getUserData().getUser()));
        } else if (getUri().contains("invpurchaseorder_1") || getUri().contains("invSalesOrderForm")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invPurchaseOrderSelected"));
            setInvInvoiceDTO(invPurchaseOrderService.findInvPurchaseOrderById(getInvInvoiceDTO(), getUserData().getUser()));
        } else if (getUri().contains("invaddingorder") || getUri().contains("invpermissionorder_1")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invAddingOrderSelected"));
            setInvInvoiceDTO(invAddingOrderService.findInvAddingOrderById(getInvInvoiceDTO(), getUserData().getUser()));
        } else if (getUri().contains("invQuotation_1")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invQuotationSelected"));
            setInvInvoiceDTO(invQuotationService.findInvPurchaseOrderById(getInvInvoiceDTO(), getUserData().getUser()));
        } else if (getUri().contains("invReservationForm")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invReservationSelected"));
            setInvInvoiceDTO(invReservationService.findInvPurchaseInvoiceById(getInvInvoiceDTO(), getUserData().getUser()));
        } else if (getUri().contains("invtransfer")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invTransferSelected"));
            setInvInvoiceDTO(invTransferService.findInvTransferById(getInvInvoiceDTO()));
        } 
        else if (getUri().contains("invMonthTarget")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invMonthTargetSelected"));
            getInvInvoiceDTO().setInvMonthTargetDTO(monthTargetService.getMonthTargetDTOById(getInvInvoiceDTO()));
        } else if (getUri().contains("invstrip")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invStripSelected"));
            setInvInvoiceDTO(invStripService.findInvStripById(getInvInvoiceDTO()));
        } else if (getUri().contains("invupdate")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invUpdateSelected"));
            setInvInvoiceDTO(invUpdateService.findInvUpdateById(getInvInvoiceDTO()));
        } else if (getUri().contains("invopeningbalanceitem")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invOpenningBalanceItemSelected"));
            setInvInvoiceDTO(invOpeningBalanceItemService.findOpeningBalanceById(getInvInvoiceDTO()));
        }
    }
    
    

    /**
     * @param invPurchaseInvoiceDTO the invInvoiceDTO to set
     */
    public void setInvInvoiceDTO(InvInvoiceDTO invPurchaseInvoiceDTO) {
        this.invInvoiceDTO = invPurchaseInvoiceDTO;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<InvInvoiceDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<InvInvoiceDTO> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the invInvoiceDTOSelected
     */
    public InvInvoiceDTO getInvInvoiceDTOSelected() {
        if (invInvoiceDTOSelected == null) {
            invInvoiceDTOSelected = new InvInvoiceDTO();
        }
        return invInvoiceDTOSelected;
    }

    /**
     * @param invInvoiceDTOSelected the invInvoiceDTOSelected to set
     */
    public void setInvInvoiceDTOSelected(InvInvoiceDTO invInvoiceDTOSelected) {
        this.invInvoiceDTOSelected = invInvoiceDTOSelected;
    }

    /**
     * @return the dataModelInvDailogueData
     */
    public LazyDataModel<InvDailogueData> getDataModelInvDailogueData() {
        return dataModelInvDailogueData;
    }

    /**
     * @param dataModelInvDailogueData the dataModelInvDailogueData to set
     */
    public void setDataModelInvDailogueData(LazyDataModel<InvDailogueData> dataModelInvDailogueData) {
        this.dataModelInvDailogueData = dataModelInvDailogueData;
    }

    /**
     * @return the firstLoad
     */
    public Boolean getFirstLoad() {
        return firstLoad;
    }

    /**
     * @param firstLoad the firstLoad to set
     */
    public void setFirstLoad(Boolean firstLoad) {
        this.firstLoad = firstLoad;
    }

    public List<ItemsBarcodesDetailsView> getItemsBarcodesDetailsViewList() {
        if (itemsBarcodesDetailsViewList == null || itemsBarcodesDetailsViewList.isEmpty()) {
            itemsBarcodesDetailsViewList = getItemsBarcodesDetailsViewService().findItemsBarcodesDetailsViewBranchIdByBarcode(getUserData().getUserBranch().getId(), null);
            setItemsBarcodesDetailsViewConverter(new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViewList));
            convertListToMap(itemsBarcodesDetailsViewList);
        }
        return itemsBarcodesDetailsViewList;
    }

    public Map<String, ItemsBarcodesDetailsView> convertListToMap(List<ItemsBarcodesDetailsView> list) {
        if (itemsBarcodesDetailsViewMap == null) {
            Map<String, ItemsBarcodesDetailsView> map = list.stream()
                    .collect(Collectors.toMap(ItemsBarcodesDetailsView::getBarcode, ItemsBarcodesDetailsView -> ItemsBarcodesDetailsView));
            itemsBarcodesDetailsViewMap = map;
        }
        return itemsBarcodesDetailsViewMap;
    }

    /**
     * @return the invInventoryDTOListTo
     */
    public List<InvInventoryDTO> getInvInventoryDTOListTo() {
        if (invInventoryDTOListTo == null) {
            invInventoryDTOListTo = getUserData().getInventoryDTOList();
            setInvInventoryDTOConverterTO(new InvInventoryDTOConverter(invInventoryDTOListTo));
        }
        return invInventoryDTOListTo;
    }

    /**
     * @param invInventoryDTOListTo the invInventoryDTOListTo to set
     */
    public void setInvInventoryDTOListTo(List<InvInventoryDTO> invInventoryDTOListTo) {
        this.invInventoryDTOListTo = invInventoryDTOListTo;
    }

    /**
     * @return the invInventoryDTOConverterTO
     */
    public InvInventoryDTOConverter getInvInventoryDTOConverterTO() {
        return invInventoryDTOConverterTO;
    }

    /**
     * @param invInventoryDTOConverterTO the invInventoryDTOConverterTO to set
     */
    public void setInvInventoryDTOConverterTO(InvInventoryDTOConverter invInventoryDTOConverterTO) {
        this.invInventoryDTOConverterTO = invInventoryDTOConverterTO;
    }

    /**
     * @return the invMonthTargetDTOList
     */
    public List<InvMonthTargetDTO> getInvMonthTargetDTOList() {
        if (invMonthTargetDTOList == null) {
            setInvMonthTargetDTOList(monthTargetService.getMonthTargetDTOByBranchId(getUserData().getUserBranch().getId()));
        }
        return invMonthTargetDTOList;
    }

    /**
     * @param invMonthTargetDTOList the invMonthTargetDTOList to set
     */
    public void setInvMonthTargetDTOList(List<InvMonthTargetDTO> invMonthTargetDTOList) {
        this.invMonthTargetDTOList = invMonthTargetDTOList;
    }

    /**
     * @return the organizationSiteId
     */
    public InvOrganizationSiteDTO getOrganizationSiteId() {
        if (organizationSiteId == null) {
            organizationSiteId = new InvOrganizationSiteDTO();
        }
        return organizationSiteId;
    }

    /**
     * @param organizationSiteId the organizationSiteId to set
     */
    public void setOrganizationSiteId(InvOrganizationSiteDTO organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the itemsBarcodesDetailsViewConverterForSearch
     */
    public ItemsBarcodesDetailsViewConverter getItemsBarcodesDetailsViewConverterForSearch() {
        return itemsBarcodesDetailsViewConverterForSearch;
    }

    /**
     * @param itemsBarcodesDetailsViewConverterForSearch the
     * itemsBarcodesDetailsViewConverterForSearch to set
     */
    public void setItemsBarcodesDetailsViewConverterForSearch(ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverterForSearch) {
        this.itemsBarcodesDetailsViewConverterForSearch = itemsBarcodesDetailsViewConverterForSearch;
    }

    /**
     * @return the itemsBarcodesDetailsViewListForSearch
     */
    public List<ItemsBarcodesDetailsView> getItemsBarcodesDetailsViewListForSearch() {
        return itemsBarcodesDetailsViewListForSearch;
    }

    /**
     * @param itemsBarcodesDetailsViewListForSearch the
     * itemsBarcodesDetailsViewListForSearch to set
     */
    public void setItemsBarcodesDetailsViewListForSearch(List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewListForSearch) {
        this.itemsBarcodesDetailsViewListForSearch = itemsBarcodesDetailsViewListForSearch;
    }

    /**
     * @return the itemsBarcodesDetailsViewListForSearchSelected
     */
    public ItemsBarcodesDetailsView getItemsBarcodesDetailsViewListForSearchSelected() {
        return itemsBarcodesDetailsViewListForSearchSelected;
    }

    /**
     * @param itemsBarcodesDetailsViewListForSearchSelected the
     * itemsBarcodesDetailsViewListForSearchSelected to set
     */
    public void setItemsBarcodesDetailsViewListForSearchSelected(ItemsBarcodesDetailsView itemsBarcodesDetailsViewListForSearchSelected) {
        this.itemsBarcodesDetailsViewListForSearchSelected = itemsBarcodesDetailsViewListForSearchSelected;
    }

    /**
     * @return the barcodeType
     */
    public Integer getBarcodeType() {
        if (barcodeType == null) {
            barcodeType = 1;
        }
        return barcodeType;
    }

    /**
     * @param barcodeType the barcodeType to set
     */
    public void setBarcodeType(Integer barcodeType) {
        this.barcodeType = barcodeType;
    }

    /**
     * @return the checkDiscountValue
     */
    public Boolean getCheckDiscountValue() {
        return checkDiscountValue;
    }

    /**
     * @param checkDiscountValue the checkDiscountValue to set
     */
    public void setCheckDiscountValue(Boolean checkDiscountValue) {
        this.checkDiscountValue = checkDiscountValue;
    }

    /**
     * @return the checkDiscountRate
     */
    public Boolean getCheckDiscountRate() {
        return checkDiscountRate;
    }

    /**
     * @param checkDiscountRate the checkDiscountRate to set
     */
    public void setCheckDiscountRate(Boolean checkDiscountRate) {
        this.checkDiscountRate = checkDiscountRate;
    }

}

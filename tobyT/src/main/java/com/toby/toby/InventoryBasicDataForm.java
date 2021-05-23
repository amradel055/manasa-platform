/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.CurrencyService;
import com.toby.businessservice.GlAdminUnitService;
import com.toby.businessservice.InvAddingOrderService;
import com.toby.businessservice.InvDelegatorService;
import com.toby.businessservice.InvDialogueService;
import com.toby.businessservice.InvGlobalService;
import com.toby.businessservice.InvInventoryService;
import com.toby.businessservice.InvItemService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.InvPurchaseOrderService;
import com.toby.businessservice.InvQuotationService;
import com.toby.businessservice.InvReservationService;
import com.toby.businessservice.InvTransferService;
import com.toby.businessservice.InventorySetupService;
import com.toby.businessservice.ItemsBarcodesDetailsViewService;
import com.toby.businessservice.OfferCompanyService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.ProDetailTypeService;
import com.toby.businessservice.QuantityItemsStoreService;
import com.toby.businessservice.ReportManagementService;
import com.toby.businessservice.SymbolService;
import com.toby.businessservice.report.SupplierAccountService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.converter.CostCenterDTOConverter;
import com.toby.converter.CurrencyDTOConverter;
import com.toby.converter.GlAdminUnitDTOConverter;
import com.toby.converter.GlBankDTOConverter;
import com.toby.converter.InvDelegatorConvertor;
import com.toby.converter.InvDelegatorDTOConvertor;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.converter.ItemDTOConverter;
import com.toby.converter.ItemsBarcodesDetailsViewConverter;
import com.toby.converter.ProDetailTypeDTOConverter;
import com.toby.converter.SymbolDTOConverter;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.dto.GlBankDTO;
import com.toby.dto.InvDailogueData;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.dto.ProDetailTypeDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvOrganizationSite;
import com.toby.print.IReportService;
import com.toby.print.IReportServiceImpl;
import com.toby.views.ItemsBarcodesDetailsView;
import com.toby.views.QuantityItemsStore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;
    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverterForSearch;
    private InvDelegatorConvertor purchasePersonConvertor;
    private InvOrganizationSiteConverter supplierConvertor;
    private InvOrganizationSiteDTOConverter organizationSiteDTOConvertor;
    private InvDelegatorDTOConvertor invDelegatorDTOConvertor;
    private InvInventoryConverter invInventoryConverter;
    private SymbolDTOConverter regionConverter;

    private List<InvInventoryDTO> invInventoryDTOList;
    private InvInventoryDTOConverter invInventoryDTOConverter;
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
    private List<SymbolDTO> detirminationPriceList;
    private SymbolDTOConverter detirminationPriceConverter;
    private List<GlBankDTO> glBankList;
    private GlBankDTOConverter glBankConverter;
    private List<ProDetailTypeDTO> proDetailTypeDTOList;
    private ProDetailTypeDTOConverter proDetailTypeDTOConverter;

    private List<InvInventoryDTO> invInventoryDTOListTo;
    private InvInventoryDTO invInventoryDTO;
    private InvInventoryDTOConverter invInventoryDTOConverterTO;
    private Map<String, ItemsBarcodesDetailsView> itemsBarcodesDetailsViewMap;
    private Boolean firstLoad;

    private InvOrganizationSiteDTO organizationSiteId;

    private InvInvoiceDTO invInvoiceDTO;
    private InvInvoiceDTO invInvoiceDTOSelected;
    private InvDetailDTO invDetailDTOSelected;

    IReportService iReportService = new IReportServiceImpl();

    private LazyDataModel<InvInvoiceDTO> dataModel;
    private LazyDataModel<InvDailogueData> dataModelInvDailogueData;

    //////Cloth/////
    private InvInventoryDTOConverter gallaryDTOConverter;
    private List<InvInventoryDTO> gallaryList;
    private List<InvItemDTO> invItemDTOParentList;
    private ItemDTOConverter itemDTOParentConverter;
    private InvInventoryDTO invGallarySelection;
    private InvInventory invInventorySelection;
    private List<InvInventory> inventoryList;

    @EJB
    AutoCompleteService autoCompleteService;
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
    private TobyUserInventoryService tobyUserInventoryService;
    @EJB
    private SymbolService symbolService;
    @EJB
    private InvTransferService invTransferService;

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
    private ProDetailTypeService proDetailTypeService;
    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    InvInventoryService invInventoryService;
    @EJB
    OfferCompanyService companyService;
    @EJB
    SupplierAccountService supplierAccountService;

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
            getInvInvoiceDTO().setInvInventoryId(getUserData().getInventoryDTODefault());
            getInvInvoiceDTO().setGlBankId(getUserData().getGlBankDTODefault());
            if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPURCHASEORDERFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)) {
                getInvInvoiceDTO().setOrganizationSiteId(getInvOrganizationSiteDTOList(true, 1).get(0));
//                getInvInvoiceDTO().setInvDelegatorId(getInventoryDelegatorList(0,getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId()).get(0));
                getInvInvoiceDTO().setGallaryId(getGallaryList() == null ? null : getGallaryList().get(0));
                putCurrency();
            } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESORDERFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRESERVATIONFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVQUOTATIONFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPERMISSIONORDERFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.PROWORKORDER)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
                getInvInvoiceDTO().setOrganizationSiteId(getInvOrganizationSiteDTOList(true, 0).get(0));
//                getInvInvoiceDTO().setInvDelegatorId(getInventoryDelegatorList(1,getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId()).get(0));
                getInvInvoiceDTO().setGallaryId((getGallaryList() == null && getGallaryList().size() > 0) ? null : getGallaryList().get(0));
                putCurrency();
            } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVTRANSFERINVENTORY)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVTRANSFER)) {
                if (getInvInventoryDTOList() != null && !getInvInventoryDTOList().isEmpty()) {
                    getInvInvoiceDTO().setInvInventoryId(getInvInventoryDTOList().get(0));
                    getInvInvoiceDTO().setInvInventoryIdTo(getInvInventoryDTOList().get(getInvInventoryDTOList().size() - 1));
                }
            }

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "resetInvPurchaseInvoiceForm");
        }
    }

    public void convertDateFrom() {
        getInvInvoiceDTO().setDate(findDateFromHijriFrom());
    }

    public void fillDefaultList() {
        getInvInventoryDTOList();

        if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.PROWORKORDER)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
            if (getInvInvoiceDTO().getGallaryId() == null) {
                getInvInvoiceDTO().setGallaryId(getGallaryList().get(0));
            }
            getGlBankList();
        }

        if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPURCHASEORDERFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)) {
            getInvOrganizationSiteDTOList(true, 1);
//            getInventoryDelegatorList(0, getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId());
            if (getInvInvoiceDTO().getInvDelegatorId() == null) {
                getInvInvoiceDTO().setInvDelegatorId(getInventoryDelegatorList(0, getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId()).get(0));
            }
        } else if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRESERVATIONFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVQUOTATIONFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.PROWORKORDER)
                || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPERMISSIONORDERFORM)) {
            getInvOrganizationSiteDTOList(true, 0);
//            getInventoryDelegatorList(1, getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId());
            if (getInvInvoiceDTO().getInvDelegatorId() == null) {
                getInvInvoiceDTO().setInvDelegatorId(getInventoryDelegatorList(1, getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId()).get(0));
            }
        }
        getInvInvoiceDTO().setDueperiod(invGlobalService.getDeliveryDaysByGalleryId(getInvInvoiceDTO() == null ? null : getInvInvoiceDTO().getGallaryId().getId()));
        sumDateToDueDate();
        if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.PROWORKORDER)) {
            if (getInvInvoiceDTO().getPricetype() == null) {
                getInvInvoiceDTO().setPricetype(1);
            }
        }
    }

    public void fillLists() {
        try {
            fillDefaultList();
            if (!getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVTRANSFER)
                    && !getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVTRANSFER)) {
                getCurrencyDTOList();
            }
//            getItemsBarcodesDetailsViewList(getInvInvoiceDTO().getParent());
            if (getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.PROWORKORDER)
                    || getInvInvoiceDTO().getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
                getCostCenterList();
                getAdminUnitList();
            }

        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "fillLists");
        }

    }

    public void addInvDetail() {
        try {
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invGlobalService.addInvPurchaseInvoiceDetail(getInvInvoiceDTO(), getIndex()));
            errorMsg(getInvInvoiceDTO().getMsg());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "addInvPurchaseInvoiceDetail");
        }
    }

    public void validatedetail() {
        String error = invGlobalService.validateSave(getInvInvoiceDTO().getInvPurchaseInvoiceDetailList(),
                getInvInvoiceDTO().getInvInventoryId() != null ? getInvInvoiceDTO().getInvInventoryId().getId() : null,
                getInvInvoiceDTO().getScreenName(), getInvInvoiceDTO().getInventorySetupDTO().getNegativeSell(),
                (getInvInvoiceDTO().getLoadedSerial() != null), getInvInvoiceDTO().getInventorySetupDTO().getSellBuy(), getInvInvoiceDTO().getCompanyActivity());
        if (error != null && !error.isEmpty()) {
            errorMsg(error);
        }
    }

    public void recalcHeadValues() {
        try {
            setInvInvoiceDTO(invGlobalService.recalcHeadValues(getInvInvoiceDTO()));
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "recalcHeadValues");
        }
    }

    public void removeHalala() {
        Integer num = getInvInvoiceDTO().getFinalNet().intValue();
        BigDecimal remain = getInvInvoiceDTO().getFinalNet().subtract(new BigDecimal(num));
        BigDecimal discHalala = remain.divide(BigDecimal.valueOf(1.15), 5, RoundingMode.HALF_UP);
        getInvInvoiceDTO().setDiscHalala(discHalala);
        calcDiscountValue();
        calculateRemainValue();
    }

    public void calcDiscountValue() {
        try {
            if (getInvInvoiceDTO().getDiscHalala() != null
                    && getInvInvoiceDTO().getDiscHalala().compareTo(BigDecimal.ZERO) == 1) {
                setInvInvoiceDTO(invGlobalService.recalcHeadValues(getInvInvoiceDTO()));
            }

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "recalcHeadValues");
        }
    }

    public void validateDiscountColumn(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            invPurchaseInvoiceDetailsValidate.setMsg(null);
            invPurchaseInvoiceDetailsValidate = invGlobalService.validateDiscountColumn(invPurchaseInvoiceDetailsValidate);
            afterValidation(invPurchaseInvoiceDetailsValidate);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateDiscountColumn");
        }
    }

    private void afterValidation(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        replacedetail(invPurchaseInvoiceDetailsValidate);
        checkProof(invPurchaseInvoiceDetailsValidate);
        updateSummition();
        errorMsg(invPurchaseInvoiceDetailsValidate.getMsg());
    }

    public void validatePriceColumn(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            invPurchaseInvoiceDetailsValidate.setMsg(null);
            invPurchaseInvoiceDetailsValidate = invGlobalService.validatePriceColumn(invPurchaseInvoiceDetailsValidate);
            afterValidation(invPurchaseInvoiceDetailsValidate);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validatePriceColumn");
        }
    }

    public void validateQuantityColum(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            if (checkdetail(invPurchaseInvoiceDetailsValidate)) {
                invPurchaseInvoiceDetailsValidate.setMsg(null);
                invPurchaseInvoiceDetailsValidate = invGlobalService.validateQuantityColum(invPurchaseInvoiceDetailsValidate, (getInvInvoiceDTO().getLoadedSerial() != null), getInvInvoiceDTO().getScreenName(), getInvInvoiceDTO().getInventorySetupDTO().getNegativeSell(), getInvInvoiceDTO().getCompanyActivity());
                afterValidation(invPurchaseInvoiceDetailsValidate);
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateQuantityColum");
        }
    }

    public void validateQuantityOfUnitColum(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            if (checkdetail(invPurchaseInvoiceDetailsValidate)) {
                invPurchaseInvoiceDetailsValidate.setMsg(null);
                invPurchaseInvoiceDetailsValidate = invGlobalService.validateQuantityOfUnitColum(invPurchaseInvoiceDetailsValidate, getInvInvoiceDTO());
                afterValidation(invPurchaseInvoiceDetailsValidate);
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateQuantityColum");
        }
    }

    public Boolean validateItems(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            if (checkdetail(invPurchaseInvoiceDetailsValidate)) {
                invPurchaseInvoiceDetailsValidate.setMsg(null);
                invPurchaseInvoiceDetailsValidate.setCost(BigDecimal.ZERO);
                getInvInvoiceDTO().setIsEdit(Boolean.FALSE);
                invPurchaseInvoiceDetailsValidate = invGlobalService.validateItems(invPurchaseInvoiceDetailsValidate, getInvInvoiceDTO());
                afterValidation(invPurchaseInvoiceDetailsValidate);
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateItems");
            return null;
        }
        return true;
    }

    public void updatePriceAndQuantityForDetail() {
        getInvInvoiceDTO().setInvPurchaseInvoiceDetailList(invGlobalService.updatePriceAndQuantityForDetail(getInvInvoiceDTO()));
    }

    public void sumDateToDueDate() {
        getInvInvoiceDTO().setDueDate(invGlobalService.sumDateToDueDate(getInvInvoiceDTO().getDate(), getInvInvoiceDTO().getDueDate(), getInvInvoiceDTO().getDueperiod()));
    }

    public void subtractDatefromDueDate() {
        getInvInvoiceDTO().setDueperiod(invGlobalService.getDeliveryDaysByGalleryId(getInvInvoiceDTO() == null ? null : getInvInvoiceDTO().getGallaryId().getId()));
    }

    public void addNewCustomer() {
        getOrganizationSiteId().setGallaryIdDTO(getInvInvoiceDTO().getGallaryId());
        getInvInvoiceDTO().setOrganizationSiteId(organizationSiteService.addNewCustomer(getOrganizationSiteId(), getInvOrganizationSiteDTOList(true, 0), getUserData().getUser()));
        getInvInvoiceDTO().setShowCustomerDate(Boolean.FALSE);
    }

    private Boolean checkdetail(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        if (invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() != null && !invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getBarcode().isEmpty()) {
//            for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
//                if (detailDTO.getItemsBarcodesDetail() != null && detailDTO.getItemsBarcodesDetail().getBarcode().equals(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getBarcode()) && !detailDTO.getIndex().equals(invPurchaseInvoiceDetailsValidate.getIndex())) {
//                    invPurchaseInvoiceDetailsValidate.setItemsBarcodesDetail(null);
//                    replacedetail(invPurchaseInvoiceDetailsValidate);
//                    errorMessage("تم ادخال الصنف من قبل");
//                    return false;
//                }
//            }
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
            getInvInvoiceDTO().setCustomerNotice(companyService.getNoticeByCustomerId(getInvInvoiceDTO() == null ? null : getInvInvoiceDTO().getOrganizationSiteId().getId()));

            errorMsg(getInvInvoiceDTO().getMsg());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "putCurrency");
        }
    }

    public void updateCustomerData() {
        putCurrency();
        findAccountStatement();
    }

    public void findAccountStatement() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getOrganizationSiteId() != null && getInvInvoiceDTO().getOrganizationSiteId().getId() != null) {
            getInvInvoiceDTO().setAccountStatement(supplierAccountService.getAccountForOrganizationSite(getInvInvoiceDTO().getOrganizationSiteId().getId()));
        }
    }

    public void calculateRemainValue() {
        try {
            getInvInvoiceDTO().setRemain(getInvInvoiceDTO().getFinalNet().subtract(getInvInvoiceDTO().getPayed()));
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "calculateRemainValue");
        }
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
            if (invPurchaseInvoiceDetailsValidate.getId() != null) {
                getInvInvoiceDTO().getInvPurchaseInvoiceDetailsDeletedList().add(invPurchaseInvoiceDetailsValidate);
            }

            getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().remove(invPurchaseInvoiceDetailsValidate);
            updateSummition();
            for (InvDetailDTO detailEntity : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
        }
    }

    public void repeatInvDetail(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            InvDetailDTO invDetailDTO = (InvDetailDTO) invPurchaseInvoiceDetailsValidate.clone();
            if (invDetailDTO.getId() != null) {
                invDetailDTO.setId(null);
            }
            Integer maxClothNumber = 1;
            invDetailDTO.setClothNumber(maxClothNumber + 1);
            getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().add(invDetailDTO);
            updateSummition();
            for (InvDetailDTO detailEntity : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
                if (detailEntity.getClothNumber() != null && detailEntity.getClothNumber() > maxClothNumber && detailEntity.getItemsBarcodesDetail().getBarcode().equals(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getBarcode())) {
                    maxClothNumber = detailEntity.getClothNumber();
                }
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "repeatInvDetail");
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
        calculateRemainValue();
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

                setRowCount(invDialogueService.getTotalRegistors(getUserData().getUserBranch().getId(), filters, getInvInvoiceDTO().getInventorySetupDTO().getSellBuy(), getInvInvoiceDTO().getScreenName()).intValue());
                getInvInvoiceDTO().setInvDailogueDataList(invDialogueService.loadInvDailogueData(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), filters, getInvInvoiceDTO().getInventorySetupDTO(), getUserData().getUserBranch().getId(), getInvInvoiceDTO().getScreenName()));
                return getInvInvoiceDTO().getInvDailogueDataList();
            }

        });
        OpenDlg("loaddlg");
    }

    public void loadDlg() {
        if (getInvInvoiceDTO().getInvDailogeDataSelected() != null || getInvInvoiceDTO().getNeedLoadedSerial() != null) {
            setInvInvoiceDTO(invDialogueService.loadDlg(getInvInvoiceDTO()));
            fillLists();
            putCurrency();
            CloseDlg("loaddlg");
            errorMsg(getInvInvoiceDTO().getMsg());
        }
    }

    public void findInvInvoiceDto() {
        InvInvoiceDTO invoiceDTO = invPurchaseInvoiceService_1.getInvPurchaseInvoiceDTOBySerial(getInvInvoiceDTO().getLoadedSerial(), getUserData().getUserBranch().getId());
        getInvInvoiceDTO().setOrganizationSiteId(invoiceDTO.getOrganizationSiteId());
        getInvInvoiceDTO().setInvInventoryId(invoiceDTO.getInvInventoryId());
        getInvInvoiceDTO().setInvDelegatorId(invoiceDTO.getInvDelegatorId());
        getInvInvoiceDTO().setRemarks(invoiceDTO.getRemarks());
        getInvInvoiceDTO().setInvPurchaseInvoiceId(invoiceDTO.getId());
    }

    public void print() {
        iReportService.printReportInvoice(getInvInvoiceDTO(), getUserData());
    }

    public void printList() {
        if (getUri().contains("invpurchaseinvoice_1")
                || getUri().contains("invsalesinvoice_1")
                || getUri().contains("proworkorder")
                || getUri().contains("invreturnpurchase_1")
                || getUri().contains("invreturnsales_1")) {
            getContext().getSessionMap().put("invPurchaseInvoiceIdSeclected", getInvInvoiceDTOSelected().getId());
        } else if (getUri().contains("invpurchaseorder_1") || getUri().contains("invSalesOrderForm")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invPurchaseOrderSelected"));
            setInvInvoiceDTO(invPurchaseOrderService.findInvPurchaseOrderById(getInvInvoiceDTO(), getUserData().getUser()));
        } else if (getUri().contains("invaddingorder") || getUri().contains("invpermissionorder_1")) {
            getContext().getSessionMap().put("invAddingOrderSelected", getInvInvoiceDTOSelected().getId());
//            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invAddingOrderSelected"));
//            setInvInvoiceDTO(invAddingOrderService.findInvAddingOrderById(getInvInvoiceDTO(), getUserData().getUser()));
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

    public void checkProof(InvDetailDTO invDetailDTO) {
        try {
            if (invDetailDTO != null) {
                for (InvDetailDTO detailEntity : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                    if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                        detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                    }
                    if (detailEntity.getClothNumber() != null && detailEntity.getClothNumber() == invDetailDTO.getClothNumber()
                            && !detailEntity.getItemsBarcodesDetail().getBarcode().equals(invDetailDTO.getItemsBarcodesDetail().getBarcode())) {
                        invDetailDTO.setProof(detailEntity.getProof());
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "repeatInvDetail");
        }
    }

    public void hishis() {
        System.out.print("ttt");
    }

    ////  Print Barcode
    public void printBarcode() {

        try {
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
                List<InvDetailDTO> listNew = new ArrayList<>();
                for (InvDetailDTO dTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
                    InvDetailDTO invDetailDTO = new InvDetailDTO();
                    invDetailDTO = (InvDetailDTO) dTO.clone();
                    invDetailDTO.setCost((invDetailDTO.getCost().add(invDetailDTO.getCost().multiply(new BigDecimal(0.15)))).setScale(3, BigDecimal.ROUND_UP));
                    listNew.add(invDetailDTO);
                }
                fillReport(prepareReport(), getUserData().getReportPath() + "itemBarcodeToby.jasper", listNew, "pdf");
            }
        } catch (Exception e) {

        }
    }

    public HashMap prepareReport() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            HashMap hashMap = new HashMap();

//            hashMap.put("PrintedBy", getUserData().getUser().getName());
//            hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());

//            if (isFileExist(getUserData().getImageReportPath())) {
//                hashMap.put("companyImage", getUserData().getImageReportPath());
//            } else {
//                hashMap.put("companyImage", null);
//            }
            //  hashMap.put("reportDate", new Date());
            return hashMap;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "prepareReport");
            return null;
        }
    }

    public List<ItemsBarcodesDetailsView> completeItemsDatas2(String query) {

        return getItemsBarcodesDetailsViewList();
    }

    public List<InvInventoryDTO> completeInventoryTo(String query) {
        return AutoComplete.completeInventoryDTO(query, getInvInventoryDTOListTo(), getInvInventoryDTOConverterTO());
    }

    public List<InvOrganizationSiteDTO> completeCustomerFrom(String query) {
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getUserData().getUserBranch().getId(), Boolean.TRUE, 0, query, getInvInvoiceDTO().getGallaryId() == null ? (getInvGallarySelection() == null ? null : getInvGallarySelection().getId()) : getInvInvoiceDTO().getGallaryId().getId());
        organizationSiteDTOConvertor = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    public List<ItemsBarcodesDetailsView> completeItemsDatas(String query) {
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViews = completeItemsData(query, getInvInvoiceDTO().getParent());
        return itemsBarcodesDetailsViews;
    }

    protected String validateloginDistributedScreens(ScreenNameClassEnum screenNameClassEnum, Integer gallaryId) {
        return invPurchaseInvoiceService.validateloginDistributedScreens(getUserDataDTO(), screenNameClassEnum, getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId());
    }

    public List<InvOrganizationSiteDTO> completeFromCustomer(String query) {
        return AutoComplete.completOrgSiteDTO(query, getInvOrganizationSiteDTOList(Boolean.TRUE, 0), getOrganizationSiteDTOConvertor());

    }

    public List<InvOrganizationSiteDTO> completeToCustomer(String query) {
        return AutoComplete.completOrgSiteDTO(query, getInvOrganizationSiteDTOList(Boolean.TRUE, 0), getOrganizationSiteDTOConvertor());
    }

    public List<InventoryDelegatorDTO> completeDelegatorSales(String query) {
        return AutoComplete.completeDelegatorDTO(query, getInventoryDelegatorList(1, getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId()), getInvDelegatorDTOConvertor());

    }

    public List<InventoryDelegatorDTO> completeDelegatorPurchase(String query) {
        return AutoComplete.completeDelegatorDTO(query, getInventoryDelegatorList(0, getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId()), getInvDelegatorDTOConvertor());

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
        }
    }

    public List<ItemsBarcodesDetailsView> completeItemsData(String query, Integer invPurchaseInvoiceId) {
        return AutoComplete.completeItemsData(query, getItemsBarcodesDetailsViewList(invPurchaseInvoiceId), getItemsBarcodesDetailsViewConverter());
    }

    public List<SymbolDTO> completeSymbolUnit(String query) {
        return AutoComplete.completeSymbolDTO(query, getUnitList(), getUnitConverter());
    }

    public List<SymbolDTO> completeSymbolDeterminationPrice(String query) {
        return AutoComplete.completeSymbolDTO(query, getDetirminationPriceList(), getDetirminationPriceConverter());
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

    public List<InvInventoryDTO> completeGallary(String query) {
        return AutoComplete.completeInventoryDTO(query, getGallaryList(), getGallaryDTOConverter());
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
//            setInvOrganizationSiteDTOList(organizationSiteService.getorganizationSiteByBranchIdDTO(getUserlogin().getBranchId().getId(), active, type));
            setInvOrganizationSiteDTOList(autoCompleteService.autoCompleteOrganizationSiteDTO(getUserlogin().getBranchId().getId(), active, type, null, getInvInvoiceDTO().getGallaryId() == null ? null : getInvInvoiceDTO().getGallaryId().getId()));
            setOrganizationSiteDTOConvertor(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList));
        }
        return invOrganizationSiteDTOList;
    }

    /**
     * @param invOrganizationSiteList the invOrganizationSiteDTOList to set
     */
    public void setInvOrganizationSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        this.invOrganizationSiteDTOList = invOrganizationSiteDTOList;
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

    public List<InventoryDelegatorDTO> getInventoryDelegatorList(Integer type, Integer gallaryId) {
        if (inventoryDelegatorList == null) {
//            setInventoryDelegatorList(invDelegatorService.getDelegatorDTOByBranch(getUserData().getUserBranch().getId(), type));// )مندوب المشتريات
            setInventoryDelegatorList(invDelegatorService.getDelegatorByInventory(gallaryId, type));// )مندوب المشتريات
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
            invInventoryList = tobyUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), getUserData().getUserBranch().getId());
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
//            itemsBarcodeMap = autoCompleteService.findItemsBarcodesDetailsViewBranchIdMap(getUserData().getUserBranch().getId(),null);
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
            invInvoiceDTO.setDate(new Date());
        }

        invInvoiceDTO.setInventorySetupDTO(inventorySetupService.getInventoryByBranchIdDTO(getUserData().getUserBranch().getId()));

        invInvoiceDTO.setReportManagement(reportManagementService.reportManagementByBranch(getUserData().getUserBranch().getId()));

        invInvoiceDTO.setBranchId(getUserData().getUserBranch().getId());

        invInvoiceDTO.setTaxflag(invInvoiceDTO.getInventorySetupDTO().getDefaultSalesTax());
        if (invInvoiceDTO.getInventorySetupDTO() != null && invInvoiceDTO.getInventorySetupDTO().getReservationPeriod() != null) {

            invInvoiceDTO.setDueperiod(invGlobalService.getDeliveryDaysByGalleryId(getInvInvoiceDTO() == null ? null : getInvInvoiceDTO().getGallaryId().getId()));
//           invInvoiceDTO.setDueperiod(invInvoiceDTO.getInventorySetupDTO().getReservationPeriod());
            sumDateToDueDate();
        }

        return invInvoiceDTO;
    }

    private void findScreenName() {
        if (getUri().contains("invpurchaseinvoice_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVPURCHASEINVOICEFORM);
            getInvInvoiceDTO().setType(0);
        } else if (getUri().contains("invsalesinvoice_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVSALESINVOICEFORM);
            getInvInvoiceDTO().setType(1);
        } else if (getUri().contains("invreturnpurchase_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVRETURNPURCHASEFORM);
            getInvInvoiceDTO().setType(2);
        } else if (getUri().contains("invreturnsales_1")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVRETURNSALESFORM);
            getInvInvoiceDTO().setType(3);
        } else if (getUri().contains("proworkorder")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.PROWORKORDER);
            getInvInvoiceDTO().setType(4);
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
        } else if (getUri().contains("proProductionTransactionDetail")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.PROWORKORDER);
            getInvInvoiceDTO().setType(0);
        } else if (getUri().contains("invtransfer")) {
            if (getUri().contains("invtransferInventory")) {
                getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVTRANSFERINVENTORY);
                getInvInvoiceDTO().setType(0);
                getInvInvoiceDTO().setPricetype(1);
            } else {
                getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.INVTRANSFER);
                getInvInvoiceDTO().setType(1);
            }

        }
    }

    public void fillInvInvoiceDTO() {
        fillInvInvoiceDetailDTO();
        setInvInvoiceDTO(settingInventory(getInvInvoiceDTO()));
        fillLists();
        if (!ScreenNameClassEnum.INVTRANSFER.equals(getInvInvoiceDTO().getScreenName())
                && !ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(getInvInvoiceDTO().getScreenName())) {
            putCurrency();
        }

        if (!ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(getInvInvoiceDTO().getScreenName())) {
            initData();
        }

    }

    private void fillInvInvoiceDetailDTO() {
        if (getUri().contains("invpurchaseinvoice_1")
                || getUri().contains("invsalesinvoice_1")
                || getUri().contains("proworkorder")
                || getUri().contains("invreturnpurchase_1")
                || getUri().contains("proProductionTransactionDetail")
                || getUri().contains("invreturnsales_1")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("invPurchaseInvoiceIdSeclected"));
            setInvInvoiceDTO(invPurchaseInvoiceService.findInvPurchaseInvoiceById(getInvInvoiceDTO()));
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
    }

    private void initData() {
        if (getInvInvoiceDTO() != null) {
            if (getInvInvoiceDTO().getOrganizationSiteId() != null
                    && !invOrganizationSiteDTOList.contains(getInvInvoiceDTO().getOrganizationSiteId())) {
                invOrganizationSiteDTOList.add(0, getInvInvoiceDTO().getOrganizationSiteId());
            }
            calculateRemain();
        }
    }

    public void calculateRemain() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getId() != null) {
            BigDecimal remain = invPurchaseInvoiceService_1.remainForInvPurchaseInvoice(getInvInvoiceDTO().getId());
            getInvInvoiceDTO().setRemain(remain.setScale(0, RoundingMode.HALF_UP));
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
     * @return the detirminationPriceList
     */
    public List<SymbolDTO> getDetirminationPriceList() {
        if (detirminationPriceList == null) {
            detirminationPriceList = symbolService.getDeterminationPriceDTO(getUserData().getCompany().getId()); // الوحدات: جرام, طن
            setDetirminationPriceConverter(new SymbolDTOConverter(detirminationPriceList));
        }
        return detirminationPriceList;
    }

    /**
     * @param detirminationPriceList the detirminationPriceList to set
     */
    public void setDetirminationPriceList(List<SymbolDTO> detirminationPriceList) {
        this.detirminationPriceList = detirminationPriceList;
    }

    /**
     * @return the detirminationPriceConverter
     */
    public SymbolDTOConverter getDetirminationPriceConverter() {
        return detirminationPriceConverter;
    }

    /**
     * @param detirminationPriceConverter the detirminationPriceConverter to set
     */
    public void setDetirminationPriceConverter(SymbolDTOConverter detirminationPriceConverter) {
        this.detirminationPriceConverter = detirminationPriceConverter;
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
     * @return the gallaryDTOConverter
     */
    public InvInventoryDTOConverter getGallaryDTOConverter() {
        return gallaryDTOConverter;
    }

    /**
     * @param gallaryDTOConverter the gallaryDTOConverter to set
     */
    public void setGallaryDTOConverter(InvInventoryDTOConverter gallaryDTOConverter) {
        this.gallaryDTOConverter = gallaryDTOConverter;
    }

    /**
     * @return the gallaryList
     */
    public List<InvInventoryDTO> getGallaryList() {
        if (gallaryList == null || gallaryList.isEmpty()) {
            gallaryList = tobyUserInventoryService.getAllGallaryByUserAndBranch(getUserData().getUser());
            setGallaryDTOConverter(new InvInventoryDTOConverter(gallaryList));
        }
        return gallaryList;
    }

    /**
     * @param gallaryList the gallaryList to set
     */
    public void setGallaryList(List<InvInventoryDTO> gallaryList) {
        this.gallaryList = gallaryList;
    }

    /**
     * @return the invItemDTOParentList
     */
    public List<InvItemDTO> getInvItemDTOParentList() {
        if (invItemDTOParentList == null || invItemDTOParentList.isEmpty()) {
            invItemDTOParentList = invItemService.findInvItemDTOCompletedList(getUserData().getUser());
            setItemDTOParentConverter(new ItemDTOConverter(invItemDTOParentList));
        }
        return invItemDTOParentList;
    }

    /**
     * @param invItemDTOParentList the invItemDTOParentList to set
     */
    public void setInvItemDTOParentList(List<InvItemDTO> invItemDTOParentList) {
        this.invItemDTOParentList = invItemDTOParentList;
    }

    /**
     * @return the itemDTOParentConverter
     */
    public ItemDTOConverter getItemDTOParentConverter() {
        return itemDTOParentConverter;
    }

    /**
     * @param itemDTOParentConverter the itemDTOParentConverter to set
     */
    public void setItemDTOParentConverter(ItemDTOConverter itemDTOParentConverter) {
        this.itemDTOParentConverter = itemDTOParentConverter;
    }

    /**
     * @return the invDetailDTOSelected
     */
    public InvDetailDTO getInvDetailDTOSelected() {
        return invDetailDTOSelected;
    }

    /**
     * @param invDetailDTOSelected the invDetailDTOSelected to set
     */
    public void setInvDetailDTOSelected(InvDetailDTO invDetailDTOSelected) {
        this.invDetailDTOSelected = invDetailDTOSelected;
    }

    /**
     * @return the proDetailTypeDTOList
     */
    public List<ProDetailTypeDTO> getProDetailTypeDTOList() {
        if (proDetailTypeDTOList == null || proDetailTypeDTOList.isEmpty()) {
            proDetailTypeDTOList = proDetailTypeService.getallProDetailTypeByBranchId(getUserData().getUserBranch().getId(), null);
            setProDetailTypeDTOConverter(new ProDetailTypeDTOConverter(proDetailTypeDTOList));
        }
        return proDetailTypeDTOList;
    }

    /**
     * @param proDetailTypeDTOList the proDetailTypeDTOList to set
     */
    public void setProDetailTypeDTOList(List<ProDetailTypeDTO> proDetailTypeDTOList) {
        this.proDetailTypeDTOList = proDetailTypeDTOList;
    }

    /**
     * @return the proDetailTypeDTOConverter
     */
    public ProDetailTypeDTOConverter getProDetailTypeDTOConverter() {
        return proDetailTypeDTOConverter;
    }

    /**
     * @param proDetailTypeDTOConverter the proDetailTypeDTOConverter to set
     */
    public void setProDetailTypeDTOConverter(ProDetailTypeDTOConverter proDetailTypeDTOConverter) {
        this.proDetailTypeDTOConverter = proDetailTypeDTOConverter;
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
            invInventoryDTOListTo = invInventoryService.getALLInventorieDTOListByBranch(getUserData().getUser());
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
     * @return the invGallarySelection
     */
    public InvInventoryDTO getInvGallarySelection() {
        if (invGallarySelection == null) {
            setInvGallarySelection(getGallaryList() == null || getGallaryList().isEmpty() ? null : getGallaryList().get(0));
        }
        return invGallarySelection;
    }

    /**
     * @param invGallarySelection the invGallarySelection to set
     */
    public void setInvGallarySelection(InvInventoryDTO invGallarySelection) {
        this.invGallarySelection = invGallarySelection;
    }

    /**
     * @return the invInventorySelection
     */
    public InvInventory getInvInventorySelection() {
        if (invInventorySelection == null) {
            setInvInventorySelection(getInventoryList() == null || getInventoryList().isEmpty() ? null : getInventoryList().get(0));
        }
        return invInventorySelection;
    }

    /**
     * @param invInventorySelection the invInventorySelection to set
     */
    public void setInvInventorySelection(InvInventory invInventorySelection) {
        this.invInventorySelection = invInventorySelection;
    }

    /**
     * @return the inventoryList
     */
    public List<InvInventory> getInventoryList() {
        if (inventoryList == null) {
            setInventoryList(tobyUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), getUserlogin().getBranchId().getId()));
            if (getInventoryList() != null && !getInventoryList().isEmpty()) {
                setInvInventoryConverter(new InvInventoryConverter(inventoryList));
            }
        }
        return inventoryList;
    }

    /**
     * @param inventoryList the inventoryList to set
     */
    public void setInventoryList(List<InvInventory> inventoryList) {
        this.inventoryList = inventoryList;
    }
}

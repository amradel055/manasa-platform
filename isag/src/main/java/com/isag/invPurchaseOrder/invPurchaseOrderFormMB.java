/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invPurchaseOrder;

import com.isag.businessservice.CurrencyOperationService;
import com.isag.businessservice.CurrencyService;
import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvPurchaseOrderDetailsService;
import com.isag.businessservice.InvPurchaseOrderService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.SymbolService;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.entity.Currency;
import com.isag.entity.CurrencyOperation;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.Symbol;
import com.isag.entiy.InvPurchaseOrderDetailEntity;
import com.isag.entiy.InvPurchaseOrderEntity;
import com.isag.isag.InventoryBasicDataForm;
import com.isag.isag.UserData;
import com.isag.views.ItemsBarcodesDetailsView;
import java.awt.event.KeyEvent;
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
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author hq002
 */
@Named(value = "invPurchaseOrderFormMB")
@ViewScoped
public class invPurchaseOrderFormMB extends InventoryBasicDataForm {

    private Date tryDate;

    private Currency currency;

    private CurrencyOperation currencyOperation;
    private List<CurrencyOperation> currencyOperations;

    private InvItem invItem;

    private InventoryDelegator inventoryDelegator;

    private InvPurchaseOrder invPurchaseOrder;

    private Symbol units;

    private Symbol colorPaint;
    private List<Symbol> colorPaintList;

    private InvItem invItemRelated;
    private List<InvItem> invItemsRelatedList;

    private List<Currency> currenciesNames = new ArrayList<>();

    private InvPurchaseOrderDetail invPurchaseOrderDetail = new InvPurchaseOrderDetail();
    private List<InvPurchaseOrderDetail> rowDeleted;
    private List<InvPurchaseOrderDetail> invPurchaseOrderDetailsList;
    private List<InvPurchaseOrderDetail> invPurchaseOrderDeletedList;
    private List<InvPurchaseOrderDetail> inveDetailsList;

    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal totalNet = BigDecimal.ZERO;
    private BigDecimal totalNetAfterDiscount = BigDecimal.ZERO;
    BigDecimal hundred = new BigDecimal(100);

    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private List<InvOrganizationSite> suppliersList;
    private InvOrganizationSite supplier;

    private InvPurchaseOrderEntity invPurchaseOrderEntity;
    private List<InvPurchaseOrderDetailEntity> invPurchaseOrderDetailEntityList;
    private List<InvPurchaseOrderDetailEntity> rowsDeleted = new ArrayList<>();

    private InvPurchaseOrderDetailEntity invPurchaseOrderDetailEntity;
    private InvPurchaseOrderDetailEntity invPurchaseOrderDetailEntitySelection;

    Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap;

    // Objects
    private Integer branchId;
    private Integer companyId;
    private Integer purchaseOrderId;

    private Integer index = 0;
    private Boolean markEdit = Boolean.FALSE;
    private Boolean focus = Boolean.FALSE;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private boolean showMessage = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

    //Lists and Converters
    private List<InvOrganizationSite> invOrganizationSiteList;

    //EJBs
    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;
    @EJB
    private InvPurchaseOrderDetailsService invPurchaseOrderDetailsService;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private CurrencyOperationService currencyOperationService;
    @EJB
    private SymbolService symbolService;
    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvItemService invItemService;

    private InvPurchaseOrderParent invPurchaseOrderParent;

    @Override
    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setSiteType(1);
            load();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "init");

        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            branchId = getUserData().getUserBranch().getId();
            companyId = getUserData().getCompany().getId();
            initObjs();

            filLists();

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetInvPurchaseOrderForm();
                showMessageDetails = showMessageGeneral = false;
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    invPurchaseOrderDetailEntitySelection = new InvPurchaseOrderDetailEntity();

                    purchaseOrderId = (Integer) context.getSessionMap().get("invPurchaseOrderSelected");

                    invPurchaseOrder = invPurchaseOrderService.findPurchaseOrderByPurchaseOrderId(purchaseOrderId);
                    invPurchaseOrderDetailsList = invPurchaseOrderService.getALLPurchaseOrderDetailsByPurchaseOrder(purchaseOrderId);
//                mapInvPurchaseOrderToInvPurchaseOrderEntity(invPurchaseOrder);
                    invPurchaseOrderDetailEntityList = new ArrayList<>();
                    getInvPurchaseOrderParent().mapInvPurchaseOrderToInvPurchaseOrderEntity(invPurchaseOrder,
                            invPurchaseOrderDetailsList,
                            invPurchaseOrderEntity,
                            invPurchaseOrderDetailEntityList,
                            itemsBarcodeMap, total, totalQuatity, totalNet, totalNetAfterDiscount);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            updateRate();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "load");

        }
    }

    private void initObjs() {
        try {
            invPurchaseOrder = new InvPurchaseOrder();
            invPurchaseOrderEntity = new InvPurchaseOrderEntity();
            invPurchaseOrderEntity.setDiscountType(0);
            invPurchaseOrderDetail = new InvPurchaseOrderDetail();
            invPurchaseOrderDetailEntity = new InvPurchaseOrderDetailEntity();
            invPurchaseOrderDetailEntityList = new ArrayList<>();
            invPurchaseOrderDetailEntitySelection = new InvPurchaseOrderDetailEntity();
            invPurchaseOrder = new InvPurchaseOrder();
            inveDetailsList = new ArrayList<>();
            invPurchaseOrderDeletedList = new ArrayList<>();
            setItemsBarcodesDetailsViewList(new ArrayList<>());
            itemsBarcodeMap = new HashMap<String, ItemsBarcodesDetailsView>();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "initObjs");
        }
    }

    private void filLists() {
        try {
            getCurrencyDTOList();
            getInvOrganizationSiteList();
            invPurchaseOrderDetailsList = invPurchaseOrderDetailsService.getAllPurchaseOrderDetails(invPurchaseOrder.getId());
            fillItemMap(itemsBarcodeMap, null);
            getInventoryDelegatorList(0);// )مندوب المشتريات
            getInvOrganizationSiteList(); // المورد
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "fillLists");
        }

    }

    public void resetInvPurchaseOrderForm() {
        try {
            invPurchaseOrderEntity = new InvPurchaseOrderEntity();
            invPurchaseOrderEntity.setDate(new Date());
            if (invOrganizationSiteList != null && !invOrganizationSiteList.isEmpty()) {
                invPurchaseOrderEntity.setSupplierId(invOrganizationSiteList.get(0));
            }

            invPurchaseOrderDetailEntityList = new ArrayList<>();
            invPurchaseOrderDetailsList = new ArrayList<>();

            invPurchaseOrderDetailEntitySelection = new InvPurchaseOrderDetailEntity();
            invPurchaseOrderDetailEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "resetInvPurchaseOrderForm");
        }
    }

    public InvPurchaseOrderEntity mapInvPurchaseOrderToInvPurchaseOrderEntity(InvPurchaseOrder invPurchaseOrder) {
        try {

            invPurchaseOrderEntity.setId(invPurchaseOrder.getId());
            invPurchaseOrderEntity.setSerial(invPurchaseOrder.getSerial());
            invPurchaseOrderEntity.setDelegateId(invPurchaseOrder.getDelegatorId());
            invPurchaseOrderEntity.setSupplierId(invPurchaseOrder.getSupplierId());
            invPurchaseOrderEntity.setSupplierReference(invPurchaseOrder.getSupplierReference());
            invPurchaseOrderEntity.setRate(invPurchaseOrder.getRate());
            invPurchaseOrderEntity.setRemarks(invPurchaseOrder.getRemarks());
            invPurchaseOrderEntity.setCurrencyId(invPurchaseOrder.getCurrencyId());
            invPurchaseOrderEntity.setDiscount(invPurchaseOrder.getDiscount());
            invPurchaseOrderEntity.setDiscountType(invPurchaseOrder.getDiscountType() != null ? invPurchaseOrder.getDiscountType() : 0);
            invPurchaseOrderEntity.setDate(invPurchaseOrder.getDate());
            invPurchaseOrderEntity.setCreatedBy(invPurchaseOrder.getCreatedBy());
            invPurchaseOrderEntity.setCreationDate(invPurchaseOrder.getCreationDate());

            invPurchaseOrderDetailEntityList = new ArrayList<>();
            InvPurchaseOrderDetailEntity invPurchaseOrderDetailEntityIterated;
            for (InvPurchaseOrderDetail detailsEditing : invPurchaseOrderDetailsList) {

                invPurchaseOrderDetailEntityIterated = new InvPurchaseOrderDetailEntity();
                invPurchaseOrderDetailEntityIterated.setDiscount(detailsEditing.getDiscountRate());
                invPurchaseOrderDetailEntityIterated.setId(detailsEditing.getId());
                invPurchaseOrderDetailEntityIterated.setSerial(detailsEditing.getSerial());

                if (detailsEditing.getItemBarcode() != null && !"".equals(detailsEditing.getItemBarcode())) {
                    invPurchaseOrderDetailEntityIterated.setItemsBarcodesDetail(itemsBarcodeMap.get(detailsEditing.getItemBarcode()));
                    invPurchaseOrderDetailEntityIterated.setItemsBarcodesDetailTrans(invPurchaseOrderDetailEntityIterated.getItemsBarcodesDetail());
                    invPurchaseOrderDetailEntityIterated.setScrewing(invPurchaseOrderDetailEntityIterated.getItemsBarcodesDetail().getScrewing());
                }

                if (detailsEditing.getItemId() != null) {
                    invPurchaseOrderDetailEntityIterated.setItemId(detailsEditing.getItemId().getId());
                    invPurchaseOrderDetailEntityIterated.setItemName(detailsEditing.getItemId().getName());
                } else {
                    invPurchaseOrderDetailEntityIterated.setItemId(null);
                }

                if (detailsEditing.getPrice() != null) {
                    invPurchaseOrderDetailEntityIterated.setCost(detailsEditing.getPrice());
                    total = total.add(detailsEditing.getPrice());
                } else {
                    invPurchaseOrderDetailEntityIterated.setCost(null);
                }

                if (detailsEditing.getQuantity() != null) {
                    invPurchaseOrderDetailEntityIterated.setQuantity(detailsEditing.getQuantity());
                    totalQuatity = totalQuatity.add(detailsEditing.getQuantity());
                } else {
                    invPurchaseOrderDetailEntityIterated.setQuantity(null);
                }

                invPurchaseOrderDetailEntityIterated.setTotal(detailsEditing.getTotal());
                invPurchaseOrderDetailEntityIterated.setUnit(detailsEditing.getUnitId());
                invPurchaseOrderDetailEntityIterated.setUnitName(detailsEditing.getUnitId().getName());
                invPurchaseOrderDetailEntityIterated.setColorPaintId((detailsEditing.getItemId().getPaintColor() != null && detailsEditing.getItemId().getPaintColor().getName() != null) ? detailsEditing.getItemId().getPaintColor().getName() : null);
                invPurchaseOrderDetailEntityIterated.setEnamel((detailsEditing.getItemId().getEnamelColor() != null && detailsEditing.getItemId().getEnamelColor().getName() != null) ? detailsEditing.getItemId().getEnamelColor().getName() : null);
                invPurchaseOrderDetailEntityIterated.setStones((detailsEditing.getItemId().getStone() != null && detailsEditing.getItemId().getStone().getName() != null) ? detailsEditing.getItemId().getStone().getName() : null);

                invPurchaseOrderDetailEntityIterated.setCreatedBy(detailsEditing.getCreatedBy());
                invPurchaseOrderDetailEntityIterated.setCreationDate(detailsEditing.getCreationDate());
                invPurchaseOrderDetailEntityIterated.setScrewing(detailsEditing.getScrewing());

                invPurchaseOrderDetailEntityList.add(invPurchaseOrderDetailEntityIterated);

            }

            updateSummition();

            return invPurchaseOrderEntity;
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "mapInvPurchaseOrderToInvPurchaseOrderEntity");
            return null;
        }
    }

    public KeyEvent resetKeys(KeyEvent keyEvent) {
        try {

            Map<String, String> reqParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String c = (String) reqParams.get("keyCode");

            System.out.println(c);

            if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.VK_TAB) {
                keyEvent.setKeyCode(13);
            }

            return keyEvent;
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "resetKeys");
            return null;
        }
    }

    public void putCurrency() {
        try {
            if (invPurchaseOrderEntity.getSupplierId() != null && invPurchaseOrderEntity.getSupplierId().getCurrencyId() != null) {
                invPurchaseOrderEntity.setCurrencyId(invPurchaseOrderEntity.getSupplierId().getCurrencyId());
                updateRate();

                updateSummition();
            } else {
                errorMessage("هذا المورد ليس له عمله !");
                showMessageGeneral = true;
            }
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "putCurrency");
        }
    }

    public void adddetails() {
        try {

            for (InvPurchaseOrderDetailEntity detailEntity : invPurchaseOrderDetailEntityList) {
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

            InvPurchaseOrderDetailEntity invPurchaseOrderDetailsEntity = new InvPurchaseOrderDetailEntity();
            invPurchaseOrderDetailsEntity.setMarkEdit(Boolean.TRUE);
            invPurchaseOrderDetailEntitySelection = invPurchaseOrderDetailsEntity;
            invPurchaseOrderDetailEntityList.add(invPurchaseOrderDetailsEntity);
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "adddetails");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {
            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();
            String column_name = event.getColumn().getClientId();
            if (column_name.contains("itemName")) {
                updateinvItemsRelated((Integer) newValue);
            }

            if (column_name.contains("Price")) {
                total = total.subtract(oldValue != null ? (BigDecimal) oldValue : BigDecimal.ZERO);
                total = total.add(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO);
                focus = true;

            }
            if (column_name.contains("Quantity")) {
                totalQuatity = totalQuatity.subtract(oldValue != null ? (BigDecimal) oldValue : BigDecimal.ZERO);
                totalQuatity = totalQuatity.add(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO);
                focus = true;

            }

            if (column_name.contains("NET")) {
                totalNet = totalNet.subtract(oldValue != null ? (BigDecimal) oldValue : BigDecimal.ZERO);
                totalNet = totalNet.add(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO);
                focus = true;

            }

            updateSummition();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "onCellEdit");
        }
    }

    public void validateQuantityColum(InvPurchaseOrderDetailEntity invPurchaseOrderDetailTable) {
        try {
            if (!validateQuantity(invPurchaseOrderDetailTable)) {
                if (invPurchaseOrderDetailTable.getItemsBarcodesDetail() != null) {
                    invPurchaseOrderDetailTable.setQuantity(BigDecimal.ZERO);
                } else {
                    invPurchaseOrderDetailTable.setQuantity(null);
                }
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "validateQuantityColum");
        }
    }

    public void validatePriceColumn(InvPurchaseOrderDetailEntity invPurchaseOrderDetailTable) {
        try {
            if (!validatePrice(invPurchaseOrderDetailTable)) {
                invPurchaseOrderDetailTable.setCost(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "validatePriceColumn");
        }
    }

    public void validateDiscountColumn(InvPurchaseOrderDetailEntity invPurchaseOrderDetailTable) {
        try {
            if (!validateDiscount(invPurchaseOrderDetailTable)) {
                invPurchaseOrderDetailTable.setDiscount(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "validateDiscountColumn");
        }
    }

    public Boolean validateDiscount(InvPurchaseOrderDetailEntity invPurchaseOrderDetailTable) {
        try {
            return isDiscountValid(invPurchaseOrderDetailTable.getDiscount());
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "validateDiscount");
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
            saveError(e, "invPurchaseOrderFormMB", "isDiscountValid");
            return null;
        }
    }

    public Boolean validateItems(InvPurchaseOrderDetailEntity invPurchaseOrderDetailTable) {
        try {
            if (invPurchaseOrderDetailTable.getItemsBarcodesDetail() == null) {
                resetInvItem(invPurchaseOrderDetailTable);
            } else {
                invPurchaseOrderDetailTable.setItemsBarcodesDetailTrans(invPurchaseOrderDetailTable.getItemsBarcodesDetail());

                if (invPurchaseOrderDetailTable.getItemsBarcodesDetail().getUnitName() != null
                        && !"".equals(invPurchaseOrderDetailTable.getItemsBarcodesDetail().getUnitName())) {
                    Symbol unit = new Symbol();
                    unit.setId(invPurchaseOrderDetailTable.getItemsBarcodesDetail().getUnitId());
                    unit.setName(invPurchaseOrderDetailTable.getItemsBarcodesDetail().getUnitName());
                    invPurchaseOrderDetailTable.setUnit(unit);
                    invPurchaseOrderDetailTable.setUnitName(unit.getName());
//                invPurchaseOrderDetailTable.setCost(invPurchaseOrderDetailTable.getItemsBarcodesDetail().getSellPrice());
                    invPurchaseOrderDetailTable.setScrewing(invPurchaseOrderDetailTable.getItemsBarcodesDetail().getScrewing());

                } else {
                    resetInvItem(invPurchaseOrderDetailTable);

                    errorMessage("هذا الصنف ليس لديه وحدات.");
                    showMessageDetails = true;
                    return false;
                }
            }
            updateSummition();
            return true;
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "validateItems");
            return null;
        }

    }

    public Boolean validateQuantity(InvPurchaseOrderDetailEntity invPurchaseInvoiceDetailsValidate) {
        try {
            return invPurchaseInvoiceDetailsValidate != null && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() != null
                    && invPurchaseInvoiceDetailsValidate.getQuantity() != null
                    && invPurchaseInvoiceDetailsValidate.getQuantity().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "validateQuantity");
            return null;
        }
    }

    public Boolean validateUnit(InvPurchaseOrderDetailEntity invPurchaseInvoiceDetailsValidate) {
        try {
            return invPurchaseInvoiceDetailsValidate != null && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() != null
                    && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getUnitId() != null
                    && invPurchaseInvoiceDetailsValidate.getUnit() != null;
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "validateUnit");
            return null;
        }
    }

    public Boolean validatePrice(InvPurchaseOrderDetailEntity invPurchaseInvoiceDetailsValidate) {
        try {
            return invPurchaseInvoiceDetailsValidate != null
                    && invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetailTrans() != null
                    && invPurchaseInvoiceDetailsValidate.getCost() != null
                    && invPurchaseInvoiceDetailsValidate.getCost().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "validatePrice");
            return null;
        }
    }

    private void resetInvItem(InvPurchaseOrderDetailEntity invPurchaseInvoiceDetailsValidate) {
        try {
            invPurchaseInvoiceDetailsValidate.setQuantity(null);
            invPurchaseInvoiceDetailsValidate.setCost(null);
            invPurchaseInvoiceDetailsValidate.setDiscount(null);
            invPurchaseInvoiceDetailsValidate.setUnit(null);
            invPurchaseInvoiceDetailsValidate.setUnitName(null);
            updateSummition();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "resetInvItem");
        }
    }

    public void updateSummition() {
        try {

            BigDecimal quantity, discount, cost, valueAfterDiscount;
            totalQuatity = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;

            BigDecimal rate = invPurchaseOrderEntity.getRate() != null ? invPurchaseOrderEntity.getRate() : BigDecimal.ONE;

            for (InvPurchaseOrderDetailEntity detailEntity : invPurchaseOrderDetailEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
                quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
                cost = detailEntity.getCost() != null ? detailEntity.getCost().multiply(rate) : BigDecimal.ZERO;
                discount = detailEntity.getDiscount() != null ? detailEntity.getDiscount() : BigDecimal.ZERO;
                total = quantity.multiply(cost);
                detailEntity.setTotal(total);

                valueAfterDiscount = total.subtract((total.multiply(discount).divide(hundred)));
                detailEntity.setNet(valueAfterDiscount.setScale(2, BigDecimal.ROUND_UP));
                totalQuatity = totalQuatity.add(quantity);
                totalNet = (totalNet.add(detailEntity.getNet())).setScale(2, BigDecimal.ROUND_UP);
            }
            calculateTotalNetAfterDiscount();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "updateSummition");
        }

    }

    public void calculateTotalNetAfterDiscount() {
        try {

            totalNetAfterDiscount = BigDecimal.ZERO;
            if (invPurchaseOrderDetailEntityList != null && !invPurchaseOrderDetailEntityList.isEmpty()) {
                if (invPurchaseOrderEntity.getDiscountType() != null && invPurchaseOrderEntity.getDiscountType() == 0) {
                    if (totalNet.compareTo(BigDecimal.ZERO) == 1
                            && (totalNet.compareTo(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO) == 0
                            || totalNet.compareTo(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO) == 1)) {
                        totalNetAfterDiscount = (totalNet.subtract(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO)).setScale(2, BigDecimal.ROUND_UP);
                    } else {
                        invPurchaseOrderEntity.setDiscount(BigDecimal.ZERO);
                        totalNetAfterDiscount = totalNet.subtract(BigDecimal.ZERO);
                    }

                } else {
                    if (isDiscountValid(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO)) {
                        totalNetAfterDiscount = totalNet.subtract(totalNet.multiply(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO).divide(hundred));
                    } else {
                        invPurchaseOrderEntity.setDiscount(BigDecimal.ZERO);
                        totalNetAfterDiscount = totalNet.subtract(BigDecimal.ZERO);
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "calculateTotalNetAfterDiscount");
        }
    }

    public void deletePurchaseOrderDetail() {
        try {

            setShowMessageGeneral(Boolean.FALSE);
            setShowMessageDetails(Boolean.TRUE);
            if (invPurchaseOrderDetailEntitySelection.getId() != null) {
                setDelete(Boolean.TRUE);
                getRowsDeleted().add(invPurchaseOrderDetailEntitySelection);
            }

            updateSummition();

            for (InvPurchaseOrderDetailEntity detailEntity : invPurchaseOrderDetailEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
            }

            invPurchaseOrderDetailEntityList.remove(invPurchaseOrderDetailEntitySelection);
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "deletePurchaseOrderDetail");
        }
    }

    public void updateRate() {
        try {
            if (invPurchaseOrderEntity.getCurrencyId() == null) {
                Currency currency = new Currency();
                currency.setId(getCurrencyDTOList().get(0).getId());
                currency.setCode(getCurrencyDTOList().get(0).getCode());
                currency.setName(getCurrencyDTOList().get(0).getName());
                invPurchaseOrderEntity.setCurrencyId(currency);
            }
            currencyOperation = currencyOperationService.getRatesByDates(invPurchaseOrderEntity.getCurrencyId().getId(), invPurchaseOrderEntity.getDate(), companyId);
            invPurchaseOrderEntity.setRate(currencyOperation == null ? BigDecimal.ONE : currencyOperation.getRate());
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "updateRate");
        }
    }

    public void updateDate(SelectEvent event) {
        try {
            updateRate();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "updateDate");
        }
    }

    public void updateinvItemsRelated(Integer itemId) {

//        invItemRelated = invItemService.findInvItem(itemId);
//        invPurchaseOrderDetailEntitySelection.setColorPaintId((invItemRelated.getPaintColor() != null && invItemRelated.getPaintColor().getName() != null) ? invItemRelated.getPaintColor().getName() : null);
//        invPurchaseOrderDetailEntitySelection.setEnamel((invItemRelated.getEnamelColor() != null && invItemRelated.getEnamelColor().getName() != null) ? invItemRelated.getEnamelColor().getName() : null);
//        invPurchaseOrderDetailEntitySelection.setStones((invItemRelated.getStone() != null && invItemRelated.getStone().getName() != null) ? invItemRelated.getStone().getName() : null);
    }

    @Override
    public String save() {
        try {
            total = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;

            invPurchaseOrder.setCompanyId(getUserData().getCompany());
            invPurchaseOrder.setBranchId(getUserData().getUserBranch());
            invPurchaseOrder.setDelegatorId(invPurchaseOrderEntity.getDelegateId());
            invPurchaseOrder.setCurrencyId(invPurchaseOrderEntity.getCurrencyId());
            invPurchaseOrder.setSupplierId(invPurchaseOrderEntity.getSupplierId());
            invPurchaseOrder.setDiscountType(invPurchaseOrderEntity.getDiscountType());
            invPurchaseOrder.setDiscount(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO);
            invPurchaseOrder.setRate(invPurchaseOrderEntity.getRate());
            invPurchaseOrder.setDate(invPurchaseOrderEntity.getDate());
            invPurchaseOrder.setRemarks(invPurchaseOrderEntity.getRemarks());
            invPurchaseOrder.setSupplierReference(invPurchaseOrderEntity.getSupplierReference());
            invPurchaseOrder.setId(invPurchaseOrderEntity.getId());
            invPurchaseOrder.setCreatedBy(invPurchaseOrderEntity.getCreatedBy());
            invPurchaseOrder.setCreationDate(invPurchaseOrderEntity.getCreationDate());

            if (invPurchaseOrder.getId() == null) {
                invPurchaseOrder.setCreatedBy(getUserData().getUser());
                invPurchaseOrder.setCreationDate(new Date());
                invPurchaseOrder.setStatus(0);
            } else {
                invPurchaseOrder.setModificationDate(new Date());
                invPurchaseOrder.setModifiedBy(getUserData().getUser());
            }
            /*
        
             */

            InvPurchaseOrderDetail purchaseOrderDetail;
            InvItem invItem;
            if (invPurchaseOrderDetailEntityList != null && !invPurchaseOrderDetailEntityList.isEmpty()) {
                inveDetailsList = new ArrayList<>();
                invPurchaseOrderDeletedList = new ArrayList<>();
                for (InvPurchaseOrderDetailEntity detailsEntity : invPurchaseOrderDetailEntityList) {

                    purchaseOrderDetail = new InvPurchaseOrderDetail();

                    if (detailsEntity.getItemsBarcodesDetail() == null && detailsEntity.getItemsBarcodesDetailTrans() == null) {
                        errorMessage("يجب ادخال الصنف");
                        showMessageDetails = true;
                        return "";
                    } else {
                        invItem = new InvItem();
                        invItem.setId(detailsEntity.getItemsBarcodesDetailTrans().getItemId());
                        purchaseOrderDetail.setItemId(invItem);
                        purchaseOrderDetail.setItemBarcode(detailsEntity.getItemsBarcodesDetailTrans().getBarcode());
                        detailsEntity.setItemsBarcodesDetail(detailsEntity.getItemsBarcodesDetailTrans());
                    }

                    if (detailsEntity.getId() != null) {
                        purchaseOrderDetail.setId(detailsEntity.getId());
                        purchaseOrderDetail.setModifiedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                        purchaseOrderDetail.setModificationDate(new Date());
                        purchaseOrderDetail.setCreatedBy(detailsEntity.getCreatedBy());
                        purchaseOrderDetail.setCreationDate(detailsEntity.getCreationDate());
                    } else {
                        purchaseOrderDetail.setCreatedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                        purchaseOrderDetail.setCreationDate(new Date());
                    }
                    purchaseOrderDetail.setBranchId(getUserData().getUserBranch());
                    purchaseOrderDetail.setCompanyId(getUserData().getCompany());
                    purchaseOrderDetail.setScrewing(detailsEntity.getScrewing() == null ? BigDecimal.ONE : detailsEntity.getScrewing());
                    purchaseOrderDetail.setSerial(detailsEntity.getSerial());

                    purchaseOrderDetail.setStatus(0);
                    if (detailsEntity.getDiscount() != null) {
                        if (isDiscountValid(detailsEntity.getDiscount())) {
                            purchaseOrderDetail.setDiscountRate(detailsEntity.getDiscount());
                        } else {
                            return "";
                        }
                    }

                    if (validatePrice(detailsEntity)) {
                        purchaseOrderDetail.setPrice(detailsEntity.getCost().setScale(2, BigDecimal.ROUND_UP));
                    } else {
                        errorMessage("يجب ادخال تفاصيل الفاتورة");
                        return "";
                    }

                    if (validateQuantity(detailsEntity)) {
                        purchaseOrderDetail.setQuantity(detailsEntity.getQuantity());
                        purchaseOrderDetail.setFinalQuantity(detailsEntity.getQuantity());
                    } else {
                        errorMessage("يجب ادخال تفاصيل الفاتورة");
                        return "";
                    }

                    purchaseOrderDetail.setUnitId(detailsEntity.getUnit());
                    purchaseOrderDetail.setNet(detailsEntity.getNet());

                    inveDetailsList.add(purchaseOrderDetail);
                }
            } else {
                errorMessage("يجب ادخال تفاصيل الفاتورة");
                return "";
            }

            /*
        
        
             */
            if (getRowsDeleted() != null) {
                InvPurchaseOrderDetail ipo;

                for (InvPurchaseOrderDetailEntity deletedPurchaseDetail : getRowsDeleted()) {
                    ipo = new InvPurchaseOrderDetail();
                    ipo.setId(deletedPurchaseDetail.getId());
                    invPurchaseOrderDeletedList.add(ipo);

                }
            }
            InvPurchaseReturnSave invPurchaseReturnSave;
            invPurchaseReturnSave = invPurchaseOrderService.addPurchaseOrder(invPurchaseOrder, inveDetailsList,
                    invPurchaseOrderDeletedList);

            resetPurchase();
            invPurchaseOrder = invPurchaseReturnSave.getInvPurchaseOrder();

            invPurchaseOrderDetailsList = invPurchaseReturnSave.getInvPurchaseOrderDetailList();

//        invPurchaseOrderEntity = mapInvPurchaseOrderToInvPurchaseOrderEntity(invPurchaseReturnSave.getInvPurchaseOrder());
            invPurchaseOrderDetailEntityList = new ArrayList<>();
            getInvPurchaseOrderParent().mapInvPurchaseOrderToInvPurchaseOrderEntity(invPurchaseOrder, invPurchaseOrderDetailsList, invPurchaseOrderEntity,
                    invPurchaseOrderDetailEntityList, itemsBarcodeMap, total, totalQuatity, totalNet, totalNetAfterDiscount);

            return "";
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "save");
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
            saveError(e, "invPurchaseOrderFormMB", "reset");
        }
    }

    public void resetPurchase() {
        try {
            invPurchaseOrder = new InvPurchaseOrder();
            inveDetailsList = new ArrayList<>();
            invPurchaseOrderDeletedList = new ArrayList<>();

            invPurchaseOrderEntity.setSerial(null);
            invPurchaseOrderEntity.setId(null);

            invPurchaseOrderDetailEntityList = new ArrayList<>();
            //invPurchaseOrderEntity.setDiscount(BigDecimal.ZERO);

            total = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;
            totalNetAfterDiscount = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            invPurchaseOrderEntity.setDiscountType(0);

            index = 0;
            markEdit = Boolean.FALSE;
            focus = Boolean.FALSE;
            showMessageDetails = Boolean.FALSE;
            showMessageGeneral = Boolean.FALSE;
            showMessage = Boolean.FALSE;
            delete = Boolean.FALSE;
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "resetPurchase");
        }

    }

    public String exit() {
        try {
            exit("../invPurchaseOrder/invPurchaseOrderList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "exit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void print() {
        try {

            getInvPurchaseOrderParent().print(invPurchaseOrderEntity, invPurchaseOrderDetailEntityList, getUserData());
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "print");

        }
    }

    //------------------------------------    
    public Date getTryDate() {
        try{
        return tryDate;
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "getTryDate"); 
            return null;

        }
    }

    public void setTryDate(Date tryDate) {
        this.tryDate = tryDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public CurrencyOperation getCurrencyOperation() {
        return currencyOperation;
    }

    public void setCurrencyOperation(CurrencyOperation currencyOperation) {
        this.currencyOperation = currencyOperation;
    }

    public List<CurrencyOperation> getCurrencyOperations() {
        return currencyOperations;
    }

    public void setCurrencyOperations(List<CurrencyOperation> currencyOperations) {
        this.currencyOperations = currencyOperations;
    }

    public InvItem getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItem invItem) {
        this.invItem = invItem;
    }

    public InventoryDelegator getInventoryDelegator() {
        return inventoryDelegator;
    }

    public void setInventoryDelegator(InventoryDelegator inventoryDelegator) {
        this.inventoryDelegator = inventoryDelegator;
    }

    public InvPurchaseOrder getInvPurchaseOrder() {
        return invPurchaseOrder;
    }

    public void setInvPurchaseOrder(InvPurchaseOrder invPurchaseOrder) {
        this.invPurchaseOrder = invPurchaseOrder;
    }

    public Symbol getUnits() {
        return units;
    }

    public void setUnits(Symbol units) {
        this.units = units;
    }

    public Symbol getColorPaint() {
        return colorPaint;
    }

    public void setColorPaint(Symbol colorPaint) {
        this.colorPaint = colorPaint;
    }

    public List<Symbol> getColorPaintList() {
        return colorPaintList;
    }

    public void setColorPaintList(List<Symbol> colorPaintList) {
        this.colorPaintList = colorPaintList;
    }

    public InvItem getInvItemRelated() {
        return invItemRelated;
    }

    public void setInvItemRelated(InvItem invItemRelated) {
        this.invItemRelated = invItemRelated;
    }

    public List<InvItem> getInvItemsRelatedList() {
        return invItemsRelatedList;
    }

    public void setInvItemsRelatedList(List<InvItem> invItemsRelatedList) {
        this.invItemsRelatedList = invItemsRelatedList;
    }

    public List<Currency> getCurrenciesNames() {
        return currenciesNames;
    }

    public void setCurrenciesNames(List<Currency> currenciesNames) {
        this.currenciesNames = currenciesNames;
    }

    public InvPurchaseOrderDetail getInvPurchaseOrderDetail() {
        return invPurchaseOrderDetail;
    }

    public void setInvPurchaseOrderDetail(InvPurchaseOrderDetail invPurchaseOrderDetail) {
        this.invPurchaseOrderDetail = invPurchaseOrderDetail;
    }

    public List<InvPurchaseOrderDetail> getRowDeleted() {
        return rowDeleted;
    }

    public void setRowDeleted(List<InvPurchaseOrderDetail> rowDeleted) {
        this.rowDeleted = rowDeleted;
    }

    public List<InvPurchaseOrderDetail> getInvPurchaseOrderDetailsList() {
        return invPurchaseOrderDetailsList;
    }

    public void setInvPurchaseOrderDetailsList(List<InvPurchaseOrderDetail> invPurchaseOrderDetailsList) {
        this.invPurchaseOrderDetailsList = invPurchaseOrderDetailsList;
    }

    public List<InvPurchaseOrderDetail> getInvPurchaseOrderDeletedList() {
        return invPurchaseOrderDeletedList;
    }

    public void setInvPurchaseOrderDeletedList(List<InvPurchaseOrderDetail> invPurchaseOrderDeletedList) {
        this.invPurchaseOrderDeletedList = invPurchaseOrderDeletedList;
    }

    public List<InvPurchaseOrderDetail> getInveDetailsList() {
        return inveDetailsList;
    }

    public void setInveDetailsList(List<InvPurchaseOrderDetail> inveDetailsList) {
        this.inveDetailsList = inveDetailsList;
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

    public BigDecimal getTotalNetAfterDiscount() {
        return totalNetAfterDiscount;
    }

    public void setTotalNetAfterDiscount(BigDecimal totalNetAfterDiscount) {
        this.totalNetAfterDiscount = totalNetAfterDiscount;
    }

    public BigDecimal getTotalQuatity() {
        return totalQuatity;
    }

    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
    }

    public List<InvOrganizationSite> getSuppliersList() {
        return suppliersList;
    }

    public void setSuppliersList(List<InvOrganizationSite> suppliersList) {
        this.suppliersList = suppliersList;
    }

    public InvOrganizationSite getSupplier() {
        return supplier;
    }

    public void setSupplier(InvOrganizationSite supplier) {
        this.supplier = supplier;
    }

    public InvPurchaseOrderEntity getInvPurchaseOrderEntity() {
        return invPurchaseOrderEntity;
    }

    public void setInvPurchaseOrderEntity(InvPurchaseOrderEntity invPurchaseOrderEntity) {
        this.invPurchaseOrderEntity = invPurchaseOrderEntity;
    }

    public List<InvPurchaseOrderDetailEntity> getRowsDeleted() {
        return rowsDeleted;
    }

    public void setRowsDeleted(List<InvPurchaseOrderDetailEntity> rowsDeleted) {
        this.rowsDeleted = rowsDeleted;
    }

    public InvPurchaseOrderDetailEntity getInvPurchaseOrderDetailEntity() {
        return invPurchaseOrderDetailEntity;
    }

    public void setInvPurchaseOrderDetailEntity(InvPurchaseOrderDetailEntity invPurchaseOrderDetailEntity) {
        this.invPurchaseOrderDetailEntity = invPurchaseOrderDetailEntity;
    }

    public InvPurchaseOrderDetailEntity getInvPurchaseOrderDetailEntitySelection() {
        return invPurchaseOrderDetailEntitySelection;
    }

    public void setInvPurchaseOrderDetailEntitySelection(InvPurchaseOrderDetailEntity invPurchaseOrderDetailEntitySelection) {
        this.invPurchaseOrderDetailEntitySelection = invPurchaseOrderDetailEntitySelection;
    }

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

    public Boolean getFocus() {
        return focus;
    }

    public void setFocus(Boolean focus) {
        this.focus = focus;
    }

    public List<InvPurchaseOrderDetailEntity> getInvPurchaseOrderDetailEntityList() {
        return invPurchaseOrderDetailEntityList;
    }

    public void setInvPurchaseOrderDetailEntityList(List<InvPurchaseOrderDetailEntity> invPurchaseOrderDetailEntityList) {
        this.invPurchaseOrderDetailEntityList = invPurchaseOrderDetailEntityList;
    }

    /**
     * @return the invPurchaseOrderParent
     */
    public InvPurchaseOrderParent getInvPurchaseOrderParent() {
        if (invPurchaseOrderParent == null) {
            invPurchaseOrderParent = new InvPurchaseOrderParent(getItemsBarcodesDetailsViewService());
        }
        return invPurchaseOrderParent;
    }

    /**
     * @param invPurchaseOrderParent the invPurchaseOrderParent to set
     */
    public void setInvPurchaseOrderParent(InvPurchaseOrderParent invPurchaseOrderParent) {
        this.invPurchaseOrderParent = invPurchaseOrderParent;
    }

}

package com.isag.invtransfer;

import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvTransferDetailService;
import com.isag.businessservice.InvTransferService;
import com.isag.businessservice.InventorySetupService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.ItemInStoreViewService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.QuantityItemsStoreService;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.converter.InvInventoryConverter;
import com.isag.converter.ItemsBarcodesDetailsViewConverter;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.entity.InvTransfer;
import com.isag.entity.InvTransferDetail;
import com.isag.entity.InventorySetup;
import com.isag.entity.Symbol;
import com.isag.entiy.InvTransferDetailsEntity;
import com.isag.entiy.InvTransferEntity;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.views.QuantityItemsStore;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import com.isag.converter.InvTransferConverter;
import com.isag.converter.InvTransferDetailConverter;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * @author khaled
 */
@Named(value = "invTransferFormMB")
@ViewScoped
public class InvTransferFormMB extends BaseFormBean {

    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;

    private List<InvItem> invItemList;
    private InvItem invItem;

    private InvTransfer invTransfer;
    private List<InvTransfer> invTransferList;

    private InvTransferDetail invTransferDetail;

    private InvTransferEntity invTransferEntity;
    private InvTransferDetailsEntity invTransferDetailEntity;
    private InvTransferDetailsEntity invTransferDetailEntitySelection;
    private List<InvTransferDetailsEntity> invTransferDetailsEntityList = new ArrayList<>();
    private List<InvTransferDetailsEntity> invTransferDetailsEntityCopyList = new ArrayList<>();
    private List<InvTransferDetailsEntity> rowDeleted = new ArrayList<>();

    private List<InvTransferDetail> invTransferDetailList;
    private List<InvTransferDetail> invTransferDeletedList;

    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailList;
    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;
    Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<String, ItemsBarcodesDetailsView>();

    private Integer invTransferId;

    private Integer index = 0;
//------------------
    private InvTransferConverter invTransferConverter;
    private InvTransferDetailConverter invTransferDetailConverter;
    private InvTransferDetailsEntity invTransferDetailEntityFirstBackup;
    private List<InvTransferDetail> invTransferDetailsLoadedList;
    private List<InvTransfer> invTransferSelectedList;

    private String uri;
    private Integer transferType;
    private boolean isPermission;
    private List<Integer> updatedInvTransferList;
    private StringBuilder headIdList = new StringBuilder();

//------------------------------
    private BigDecimal totalQuatity = BigDecimal.ZERO;

    private Boolean markEdit = Boolean.FALSE;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private boolean showMessage = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

    private Map<Integer, InvTransferDetail> invTransferDetailMap;
    private Map<Integer, BigDecimal> quantityItemMap = new HashMap<>();

    InventorySetup invSetup;
    Map<Integer, BigDecimal> itemsMap;

    List<String> itemBarcodes;
    StringBuilder itemBarcodeStr = new StringBuilder();

    @EJB
    private InvItemService invItemService;
    @EJB
    private InvTransferService invTransferService;
    @EJB
    private InvTransferDetailService invTransferDetailService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private QuantityItemsStoreService quantityItemsStoreService;
    @EJB
    private ItemInStoreViewService itemInStoreViewService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    InventorySetupService inventorySetupService;

    @Override
    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            load();
            fillItemMap();
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "init()");
        }
    }

    @Override
    public void load() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
            if (uri.contains("invtransferInventory")) {
                isPermission = false;
                setTransferType((Integer) 0);
            } else {
                isPermission = true;
                setTransferType((Integer) 1);
            }

            initObjs();
            fillList();
            totalQuatity = BigDecimal.ZERO;

//        fillInventoryItem();
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetItemDataForm();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    invTransferId = (Integer) context.getSessionMap().get("invTransferSelected");
                    invTransfer = new InvTransfer();
                    invTransfer = invTransferService.findInvTransferByInvTransferId(invTransferId);
                    invTransferDetailList = invTransferService.getALLInvTransferDetailsByInvTransfer(invTransferId);
                    validateInputsForEdit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "load()");
        }
    }

    private void initObjs() {
        try {

            invInventoryList = new ArrayList<>();
            invTransferList = new ArrayList<>();
            invTransfer = new InvTransfer();
            invTransferDetail = new InvTransferDetail();
            invTransferEntity = new InvTransferEntity();
            invTransferDetailEntity = new InvTransferDetailsEntity();
            invTransferDetailList = new ArrayList<>();
            invTransferDeletedList = new ArrayList<>();
            invTransferDetailEntitySelection = new InvTransferDetailsEntity();
            itemsBarcodesDetailList = new ArrayList<>();
            invTransferDetailEntitySelection = new InvTransferDetailsEntity();
            invSetup = new InventorySetup();
            itemsMap = new HashMap<>();
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "initObjs()");
        }
    }

    private void fillList() {
        try {

            invTransferDetailList = invTransferDetailService.getAllInvTransferDetails(invTransfer.getId());
            invTransferDetailConverter = new InvTransferDetailConverter(invTransferDetailList);
            invTransferList = invTransferService.getALLInvTransferByBranchIdAndTransferType(getUserData().getUserBranch().getId(), isPermission ? 0 : 1);
            invTransferConverter = new InvTransferConverter(invTransferList);
            invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranch(getUserData().getUser().getId(), getUserData().getUserBranch().getId());
            invInventoryConverter = new InvInventoryConverter(invInventoryList);

            itemsBarcodesDetailList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(getUserData().getUserBranch().getId());
            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailList);
            for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailList) {
                itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
            }
            invSetup = inventorySetupService.getInventoryByBranchId(getUserData().getUserBranch().getId());
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "fillList()");
        }
    }

    private String prepareItemBarcodes() {
        try {

            if (validateInvFrom()) {
                itemBarcodes = itemInStoreViewService.findItemsInStoreByBranchIdAndInventoryId(getUserData().getUserBranch().getId(),
                        invTransferEntity.getInvFrom().getId());

                if (itemBarcodes != null && !itemBarcodes.isEmpty()) {
                    for (String itemBarcode : itemBarcodes) {
                        itemBarcodeStr.append(itemBarcode).append(',');
                    }
                    return itemBarcodeStr.substring(0, itemBarcodeStr.length() - 1);
                }

                errorMessage("ليس هناك اي اصناف بالمخزن");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "prepareItemBarcodes()");
            return "";
        }
    }

    private void resetItemDataForm() {
        try {

            invTransferDetailList = new ArrayList<>();

            invTransferDetailEntitySelection = new InvTransferDetailsEntity();
            invTransferEntity = new InvTransferEntity();
            if (invInventoryList != null && !invInventoryList.isEmpty()) {
                invTransferEntity.setInvFrom(invInventoryList.get(0));
            }

            invTransferEntity.setTransferDate(new Date());
            invTransferDetailsEntityList = new ArrayList();
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "resetItemDataForm()");
        }
    }

    public void validateInputsForEdit() {
        try {

            invTransferEntity.setId(invTransfer.getId());
            invTransferEntity.setBranchId(invTransfer.getBranchId());
            invTransferEntity.setMarkEdit(invTransfer.getMarkEdit());
            invTransferEntity.setTransferDate(invTransfer.getTransferDate());
            invTransferEntity.setInvFrom(invTransfer.getInvFrom());
            invTransferEntity.setInvTo(invTransfer.getInvTo());
            invTransferEntity.setRemark(invTransfer.getRemarks());
            invTransferEntity.setSerialNo(invTransfer.getSerialNo());
            invTransferEntity.setInvTransfer(invTransfer.getParent());
            invTransferEntity.setInvTransferTrans(invTransfer.getParent());

            invTransferDetailsEntityList = new ArrayList();
            invTransferDetailMap = new HashMap<>();
            for (InvTransferDetail transferdetail : invTransferDetailList) {
                invTransferDetailEntity = new InvTransferDetailsEntity();
                invTransferDetailEntity.setId(transferdetail.getId());
                invTransferDetailEntity.setMarkEdit(false);
                if (transferdetail.getInvItemId() != null) {
                    invTransferDetailEntity.setItem(transferdetail.getInvItemId().getId());
                    invTransferDetailEntity.setItemCode(transferdetail.getInvItemId().getCode());
                    invTransferDetailEntity.setItemName(transferdetail.getInvItemId().getName());
                    invTransferDetailEntity.setUnit(transferdetail.getUnitId());
                    invTransferDetailEntity.setUnitReport(transferdetail.getUnitId() != null ? transferdetail.getUnitId().getName() : "");

                }
                if (!isPermission) {
                    invTransferDetailEntity.setSelectedId(transferdetail);

                }
                if (transferdetail.getItemBarcode() != null && !transferdetail.getItemBarcode().isEmpty()) {
                    invTransferDetailEntity.setItemsBarcodesDetail(itemsBarcodeMap.get(transferdetail.getItemBarcode()));
                    invTransferDetailEntity.setItemsBarcodesDetailTrans(invTransferDetailEntity.getItemsBarcodesDetail());
                    invTransferDetailEntity.setScrewing(invTransferDetailEntity.getItemsBarcodesDetail().getScrewing());
                }
                invTransferDetailEntity.setTransferFrom(transferdetail.getTransferFrom() != null ? transferdetail.getTransferFrom() : 0);
                invTransferDetailEntity.setAmount(transferdetail.getAmount() != null ? transferdetail.getAmount() : BigDecimal.ZERO);
                invTransferDetailEntity.setFinalAmount(transferdetail.getFinalQuantity() != null ? transferdetail.getFinalQuantity() : transferdetail.getAmount());
                invTransferDetailEntity.setBalance(transferdetail.getBalance() != null ? transferdetail.getBalance() : BigDecimal.ZERO);
                if (!isPermission) {
                    if (transferdetail != null && transferdetail.getSelectedId() != null) {
                        if (headIdList == null || headIdList.length() == 0) {
                            headIdList.append(transferdetail.getId());
                        } else {
                            headIdList.append(",").append(transferdetail.getId());
                        }
                    }
                }
                invTransferDetailMap.put(transferdetail.getId(), transferdetail);
                totalQuatity = totalQuatity.add(transferdetail.getAmount() != null ? transferdetail.getAmount() : BigDecimal.ZERO);

                BigDecimal dbQut = itemsMap.get(invTransferDetailEntity.getItemsBarcodesDetail().getItemId());
                if (dbQut != null) {
                    itemsMap.put(invTransferDetailEntity.getItemsBarcodesDetail().getItemId(), dbQut.add(invTransferDetailEntity.getAmount()));
                } else {
                    itemsMap.put(invTransferDetailEntity.getItemsBarcodesDetail().getItemId(), invTransferDetailEntity.getAmount());
                }

                invTransferDetailsEntityList.add(invTransferDetailEntity);

            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "validateInputsForEdit()");
        }
    }

    public boolean validateTransfer() {
        try {

            if (invTransferEntity.getTransferDate() == null) {
                errorMessage("يجب اختيار تاريخ");
                return false;
            }

            if (invTransferEntity.getInvTo() == null) {
                errorMessage("يجب اختيار الى مستودع");
                return false;
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "validateTransfer()");
        }
        return validateInvFrom();
    }

    private boolean validateInvFrom() {
        try {

            if (invTransferEntity.getInvFrom() == null) {
                errorMessage("يجب اختيار من مستودع");
                return false;
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "validateInvFrom()");
        }
        return true;
    }

    public void adddetails() {
        try {

            if (validateTransfer()) {
                InvTransferDetailsEntity invTransferDetailEntity = new InvTransferDetailsEntity();
                for (InvTransferDetailsEntity detailEntity : invTransferDetailsEntityList) {
                    detailEntity.setMarkEdit(Boolean.FALSE);
                }
                invTransferDetailEntity.setMarkEdit(Boolean.TRUE);
                invTransferDetailsEntityList.add(invTransferDetailEntity);
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "adddetails()");
        }
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
            saveError(e, "InvTransferFormMB", "completeInventory()");
        }
        return filteredInvs;
    }

    //-----------------------------
    public List<InvTransfer> completeInvTransfer(String query) {
        List<InvTransfer> filteredInvs = new ArrayList<>();

        try {

            List<InvTransfer> invList = invTransferList;
            if (query == null || query.trim().equals("")) {

                invTransferConverter = new InvTransferConverter(invList);
                return invList;
            }

            Integer code;
            InvTransfer invFilter;
            for (int i = 0; i < invTransferList.size(); i++) {
                invFilter = invList.get(i);

                code = invFilter.getSerialNo();
                if (code != null && code.toString().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invFilter)) {
                        filteredInvs.add(invFilter);
                    }
                }
            }

            invTransferConverter = new InvTransferConverter(filteredInvs);
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "completeInvTransfer()");
        }
        return filteredInvs;
    }

    public List<ItemsBarcodesDetailsView> completeItemsData(String query) {
        List<ItemsBarcodesDetailsView> filteredItems = new ArrayList<>();
        try {

            List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViews = itemsBarcodesDetailList;
            if (query == null || query.trim().equals("")) {
                itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViews);
                return itemsBarcodesDetailsViews;
            }

            String nameAr;
            String code;
            ItemsBarcodesDetailsView barcodesDetailsView;
            for (int i = 0; i < itemsBarcodesDetailList.size(); i++) {
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
            saveError(e, "InvTransferFormMB", "completeItemsData()");
        }
        return filteredItems;
    }

    public void deleteInvTransferDetail() {
        try {

            setShowMessageGeneral(Boolean.FALSE);
            setShowMessageDetails(Boolean.TRUE);
            if (invTransferDetailEntitySelection.getId() != null) {
                setDelete(Boolean.TRUE);
                rowDeleted.add(invTransferDetailEntitySelection);
            }
            invTransferDetailsEntityList.remove(invTransferDetailEntitySelection);
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "deleteInvTransferDetail()");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {

            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();
            String column_name = event.getColumn().getClientId();

            DataTable dataTable = (DataTable) event.getSource();
            invTransferDetailEntitySelection = (InvTransferDetailsEntity) dataTable.getRowData();

            if (column_name.contains("itemName") || column_name.contains("itemNumber")) {
                updateInvItemsRelated((Integer) newValue);
            }

            if (column_name.contains("Quantity")) {
                totalQuatity = totalQuatity.subtract(oldValue != null ? (BigDecimal) oldValue : BigDecimal.ZERO);
                totalQuatity = totalQuatity.add(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO);
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "onCellEdit()");
        }
    }

    public Boolean validateItems(InvTransferDetailsEntity invPurchaseInvoiceDetailsValidate) {
        try {

            if (invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail() == null) {
                resetInvItem(invPurchaseInvoiceDetailsValidate);
            } else {

                invPurchaseInvoiceDetailsValidate.setItemsBarcodesDetailTrans(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail());

                if (invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getUnitName() != null
                        && !"".equals(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getUnitName())) {

                    invPurchaseInvoiceDetailsValidate.setItemCode(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getBarcode());
                    invPurchaseInvoiceDetailsValidate.setItemName(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getName());

                    Symbol unit = new Symbol();
                    unit.setId(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getUnitId());
                    unit.setName(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getUnitName());
                    invPurchaseInvoiceDetailsValidate.setUnit(unit);
                    invPurchaseInvoiceDetailsValidate.setUnitReport(unit.getName());
                    BigDecimal amount = itemsMap.get(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getItemId());

                    invPurchaseInvoiceDetailsValidate.setAmount(amount != null ? amount : BigDecimal.ZERO);

                } else {
                    resetInvItem(invPurchaseInvoiceDetailsValidate);
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "هذا الصنف ليس لديه وحدات."));
                    showMessageDetails = true;
                    return false;
                }
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "validateItems()");
        }
        return true;
    }

    public void validateAmmountColum(InvTransferDetailsEntity invTransferTable) {
        try {

            if (!validateAmmount(invTransferTable)) {
                if (invTransferTable.getItemsBarcodesDetail() != null) {
                    invTransferTable.setAmount(BigDecimal.ZERO);
                } else {
                    invTransferTable.setAmount(null);
                }
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "validateAmmountColum()");
        }
    }

    public Boolean validateAmmount(InvTransferDetailsEntity invTransferTable) {
        try {

            if (invTransferTable != null && invTransferTable.getItemsBarcodesDetail() != null
                    && invTransferTable.getAmount() != null
                    && invTransferTable.getAmount().compareTo(BigDecimal.ZERO) == 1) {
                if (invSetup.getNegativeSell() == false) {
                    if (!itemsMap.isEmpty()) {
                        BigDecimal dbQut = itemsMap.get(invTransferTable.getItemsBarcodesDetail().getItemId());
                        if (dbQut != null) {
                            BigDecimal usrQut = sumItemQuantity(invTransferTable.getItemsBarcodesDetail().getItemId());
                            return dbQut.compareTo(BigDecimal.ZERO) == 1 && dbQut.compareTo(usrQut) != -1;
                        } else {
                            return false;
                        }
                    }
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "validateAmmount()");
        }
        return false;
    }

    BigDecimal sumItemQuantity(Integer itemId) {
        BigDecimal quant = BigDecimal.ZERO;
        try {

            BigDecimal detailQuant, detailScrewing;
            for (InvTransferDetailsEntity detailEntity : invTransferDetailsEntityList) {
                if (detailEntity.getItemsBarcodesDetail() != null && detailEntity.getItemsBarcodesDetail().getItemId() != null
                        && Objects.equals(itemId, detailEntity.getItemsBarcodesDetail().getItemId())
                        && detailEntity.getAmount() != null && detailEntity.getAmount().compareTo(BigDecimal.ZERO) == 1) {

                    detailQuant = detailEntity.getAmount();
                    detailScrewing = detailEntity.getScrewing() != null ? detailEntity.getScrewing() : BigDecimal.ONE;

                    quant = quant.add(detailQuant.multiply(detailScrewing));
                }
            }

        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "sumItemQuantity()");
        }
        return quant;
    }

    private void resetInvItem(InvTransferDetailsEntity invPurchaseInvoiceDetailsValidate) {
        try {

            invPurchaseInvoiceDetailsValidate.setAmount(null);
            invPurchaseInvoiceDetailsValidate.setBalance(null);
            invPurchaseInvoiceDetailsValidate.setUnit(null);
            invPurchaseInvoiceDetailsValidate.setUnitReport(null);
            updateSummition();
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "resetInvItem()");
        }
    }

    public void updateSummition() {
        try {

            BigDecimal amount;
            totalQuatity = BigDecimal.ZERO;

            for (InvTransferDetailsEntity detailEntity : invTransferDetailsEntityList) {
                setBackupItemsBarcodeDetail(detailEntity);
                amount = detailEntity.getAmount() != null ? detailEntity.getAmount() : BigDecimal.ZERO;
                detailEntity.setAmount(amount);

                totalQuatity = totalQuatity.add(detailEntity.getAmount());
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "updateSummition()");
        }
    }

    public void setBackupItemsBarcodeDetail(InvTransferDetailsEntity detailEntity) {
        try {

            if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "setBackupItemsBarcodeDetail()");
        }
    }

    private void updateInvItemsRelated(Integer id) {
        try {

            InvItem invItem = invItemService.findInvItem(id);
            invTransferDetailEntitySelection.setItemCode(invItem.getCode());
            invTransferDetailEntitySelection.setItemName(invItem.getName());
            invTransferDetailEntitySelection.setItemId(invItem);
            invTransferDetailEntitySelection.setUnit(invItem.getUnitId() != null ? invItem.getUnitId() : null);
            invTransferDetailEntitySelection.setUnitReport(invItem.getUnitId() != null ? invItem.getUnitId().getName() : null);
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "updateInvItemsRelated()");
        }
    }

    public void fillItemMap() {
        try {

            if (invSetup != null && invSetup.getNegativeSell() == false && invTransferEntity.getInvFrom() != null) {
                List<QuantityItemsStore> quantityItemsStoresList = quantityItemsStoreService.findInventoryItemsList(invTransferEntity.getInvFrom().getId(), getUserData().getUserBranch().getId(), invSetup.getSellBuy());

                for (QuantityItemsStore quantityItemsStore : quantityItemsStoresList) {
                    itemsMap.put(quantityItemsStore.getItemId(), quantityItemsStore.getQty());
                }
                completeFillMap();
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "fillItemMap()");
        }
    }

    public void completeFillMap() {
        try {

            if (invTransferDetailList != null && !invTransferDetailList.isEmpty()) {
                for (InvTransferDetail item : invTransferDetailList) {
                    BigDecimal dbQut = itemsMap.get(item.getInvItemId());
                    if (dbQut != null) {
                        itemsMap.put(item.getInvItemId().getId(), dbQut.add(item.getAmount()));
                    } else {
                        itemsMap.put(item.getInvItemId().getId(), item.getAmount());
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "completeFillMap()");
        }
    }

    public void fillInventoryItem() {
        try {

//        invItemList = invItemService.findInventoryItem(invTransferEntity.getInvFrom().getId());
//        List<QuantityItemsStore> findInventoryItemsList = quantityItemsStoreService.findInventoryItemsList(invTransferEntity.getInvFrom().getId(), getUserData().getUserBranch().getId());
//        for (QuantityItemsStore itemsStore : findInventoryItemsList) {
////            quantityItemMap.put(index, invItem)
//
//        }
//        itemBarcodeStr = new StringBuilder();
//        itemBarcodeStr = new StringBuilder(prepareItemBarcodes());
//        if (itemBarcodeStr != null && !"".equals(itemBarcodeStr)) {
//            itemsBarcodesDetailList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdAndStore(getUserData().getUserBranch().getId(), itemBarcodeStr.toString());
//            itemsBarcodesDetailList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(getUserData().getUserBranch().getId());
//            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailList);
//            for ( ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailList) {
//                itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
//            }
//            if (itemsBarcodesDetailList == null || itemsBarcodesDetailList.isEmpty()) {
//                errorMessage("ليس هناك اي اصناف بالمخزن");
//            }
//        }
            if (invTransferEntity.getInvFrom() != null && invTransferEntity.getInvFrom().getId() != null
                    && invTransferEntity.getInvTo() != null && invTransferEntity.getInvTo().getId() != null) {
                if (invTransferEntity.getInvFrom().getId().compareTo(invTransferEntity.getInvTo().getId()) == 0) {
                    invTransferEntity.setInvTo(null);
                    errorMessage("يجب اختيار مستودعين مختلفين");
                } else {
                    fillItemMap();
                }
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "fillInventoryItem()");
        }
    }
    //-----------------------------------------
    List<InvTransferDetail> invTransferDetailsList = new ArrayList<>();

    public void loadInvTransfer() {
        try {

            if (invTransferDetailsEntityList != null && !invTransferDetailsEntityList.isEmpty()) {
                if (!invTransferEntity.getInvTransfer().equals(invTransferEntity.getInvTransferTrans())) {
                    OpenDlg("dlg2");
                }
            } else {
                initPurchaseInvoice();
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "loadInvTransfer()");
        }
    }

    private void detailTransfer(List<InvTransferDetail> List) {
        try {

            totalQuatity = BigDecimal.ZERO;
            //-----------------------
            for (InvTransferDetail detailList : List) {
                InvTransferDetailsEntity detail = new InvTransferDetailsEntity();
                detail.setAmount(detailList.getFinalQuantity() != null ? detailList.getFinalQuantity() : BigDecimal.ZERO);
                detail.setFinalAmount(detailList.getFinalQuantity() != null ? detailList.getFinalQuantity() : detailList.getAmount());
                detail.setBalance(detailList.getBalance());
                detail.setScrewing(detailList.getScrewing());
                detail.setSelectedId(detailList);
                detail.setTransferFrom(0);
                detail.setItem(detailList.getInvItemId().getId());
                detail.setItemId(detailList.getInvItemId());
                detail.setItemName(detailList.getInvItemId().getName());
                detail.setItemCode(detailList.getInvItemId().getCode());
                detail.setUnit(detailList.getUnitId() == null ? detailList.getInvItemId().getUnitId() : detailList.getUnitId());
                detail.setUnitReport(detailList.getUnitId() == null ? detailList.getInvItemId().getUnitId().getName() : detailList.getUnitId().getName());
                if (detailList.getItemBarcode() != null && !"".equals(detailList.getItemBarcode())) {
                    detail.setItemsBarcodesDetail(itemsBarcodeMap.get(detailList.getItemBarcode()));
                    detail.setItemsBarcodesDetailTrans(detail.getItemsBarcodesDetail());
                    detail.setScrewing(detail.getItemsBarcodesDetail().getScrewing());
                }
                detail.setItemsBarcodesDetailTrans(null);
                totalQuatity = totalQuatity.add(detailList.getAmount());
                invTransferDetailsEntityList.add(detail);
            }
            //------------------------
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "detailTransfer()");
        }
    }

    private void initPurchaseInvoice() {
        try {

            invTransferDetailsEntityList = new ArrayList<>();
            invTransferDetailsLoadedList = invTransferDetailService.getAllInvTransferDetailsByInvTransferIdWithStatus(invTransferEntity.getInvTransfer().getId());
            invTransferEntity.setInvTransferTrans(invTransferEntity.getInvTransfer());
            invTransferDetailConverter = new InvTransferDetailConverter(invTransferDetailsLoadedList);
            detailTransfer(invTransferDetailsLoadedList);
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "initPurchaseInvoice()");
        }
    }
    ///------------------------

    public void OpenDlg(String dlgvar) {
        try {

            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "OpenDlg()");
        }
    }

    public void configmDlg() {
        try {

            if (invTransferEntity.getInvTransfer() != null) {
                initPurchaseInvoice();
                CloseDlg("dlg2");
            } else {
                invTransferDetailsEntityList = new ArrayList<>();
                invTransferEntity.setInvTransfer(null);
                invTransferEntity.setInvTransferTrans(null);
                CloseDlg("dlg2");
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "configmDlg()");
        }
    }

    public void CloseDlg(String dlgvar) {
        try {

            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");

            invTransferEntity.setInvTransfer(invTransferEntity.getInvTransferTrans());
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "CloseDlg()");
        }
    }

    //----------------------------------
    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String save() {
        try {

            if (validateTransfer()) {
                invTransfer.setCompanyId(getUserData().getCompany());
                invTransfer.setBranchId(getUserData().getUserBranch());
                if (!isPermission) {
                    invTransfer.setTransferType(0);
                    invTransfer.setParent(invTransferEntity.getInvTransfer());

                } else {
                    invTransfer.setTransferType(1);
                }
                if (invTransfer.getId() == null) {
                    invTransfer.setCreationDate(new Date());
                    invTransfer.setCreatedBy(getUserData().getUser());
                } else {
                    invTransfer.setModificationDate(new Date());
                    invTransfer.setModifiedBy(getUserData().getUser());
                    invTransfer.setStatus(0);
                }

                validateInputsForSave();

                InvTransferDetail reservationDetail;
                invTransferDeletedList = new ArrayList<>();
                updatedInvTransferList = new ArrayList<>();
                invTransferDetailList = new ArrayList<>();
                for (InvTransferDetailsEntity transferDetailsEntity : invTransferDetailsEntityList) {
                    reservationDetail = new InvTransferDetail();

                    if (transferDetailsEntity.getId() != null) {
                        reservationDetail = invTransferDetailMap.get(transferDetailsEntity.getId());

                        reservationDetail.setModificationDate(new Date());
                        reservationDetail.setModifiedBy(getUserData().getUser());

                    } else {
                        reservationDetail.setCreationDate(new Date());
                        reservationDetail.setCreatedBy(getUserData().getUser());
                        reservationDetail.setStatus(0);
                    }
                    reservationDetail.setCompanyId(getUserData().getCompany());
                    reservationDetail.setAmount(transferDetailsEntity.getAmount() != null ? transferDetailsEntity.getAmount() : null);
                    reservationDetail.setFinalQuantity(transferDetailsEntity.getFinalAmount() != null ? transferDetailsEntity.getFinalAmount() : null);
                    reservationDetail.setBalance(transferDetailsEntity.getBalance() != null ? transferDetailsEntity.getBalance() : null);
                    reservationDetail.setTransferFrom(transferDetailsEntity.getTransferFrom() != null ? transferDetailsEntity.getTransferFrom() : 0);
                    setBackupItemsBarcodeDetail(transferDetailsEntity);

                    if (transferDetailsEntity.getItemsBarcodesDetail() != null && transferDetailsEntity.getItemsBarcodesDetail().getItemId() != null) {
                        InvItem item = new InvItem();
                        item.setId(transferDetailsEntity.getItemsBarcodesDetail().getItemId());
                        reservationDetail.setInvItemId(item);
                        reservationDetail.setItemBarcode(transferDetailsEntity.getItemsBarcodesDetail().getBarcode());
                        reservationDetail.setScrewing(transferDetailsEntity.getScrewing() != null ? transferDetailsEntity.getScrewing() : BigDecimal.ONE);
                        reservationDetail.setUnitId(transferDetailsEntity.getUnit());

                        if (!isPermission) {
                            reservationDetail.setSelectedId(transferDetailsEntity.getSelectedId() != null ? transferDetailsEntity.getSelectedId() : null);
                        }
                    } else {
                        errorMessage("يجب ادخال تفاصيل الفاتورة");
                        showMessageDetails = true;
                        return "";
//                    reservationDetail.setInvItemId(null);
                    }

                    if (!validateAmmount(transferDetailsEntity)) {
                        errorMessage("الكمية غير صحيحة");
                        showMessageDetails = true;
                        return "";
                    }

                    invTransferDetailList.add(reservationDetail);
                }

                if (rowDeleted != null) {
                    InvTransferDetail ipo;

                    for (InvTransferDetailsEntity deletedDetailsEntity : getRowDeleted()) {
                        ipo = new InvTransferDetail();
                        ipo.setId(deletedDetailsEntity.getId());
                        ipo.setTransferFrom(deletedDetailsEntity.getTransferFrom() != null ? deletedDetailsEntity.getTransferFrom() : null);
                        if (!isPermission) {

                            ipo.setSelectedId(deletedDetailsEntity.getSelectedId() != null ? deletedDetailsEntity.getSelectedId() : null);
                        }
                        invTransferDeletedList.add(ipo);
                    }
                }
                InvPurchaseReturnSave invPurchaseReturnSave;
                invPurchaseReturnSave = invTransferService.addInvTransfer(invTransfer, invTransferDetailList, invTransferDeletedList, updatedInvTransferList, headIdList, isPermission);
                //     invAddingOrderService.addInvAddingOrder(invAddingOrder, invAddingorderDetailListUpdated, invAddingorderDetailListDeleted);

                resetTransfer();
                invTransfer = invPurchaseReturnSave.getInvTransfer();

                invTransferDetailList = invPurchaseReturnSave.getInvTransferDetailList();

                validateInputsForEdit();
//---------------------------------------------------------------
            /*         ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                 invTransferId = (Integer) context.getSessionMap().get("invTransferSelected");
                 validateInputsForEdit(invTransferId);*/
//-----------------------------------------------------------------              

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("SAVED"), getUserData().getUserDDs().get("SAVED")));
                showMessageDetails = true;
                return "";
            } else {
                return "";

            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "save()");
            return "";
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
            saveError(e, "InvTransferFormMB", "reset()");
        }
    }

    public void resetTransfer() {
        try {

            invTransfer = new InvTransfer();
            invTransferDetailList = new ArrayList<>();
            invTransferDeletedList = new ArrayList<>();
            updatedInvTransferList = new ArrayList<>();

            headIdList = new StringBuilder();
            isPermission = true;
            invTransferEntity.setSerial(null);
            invTransferEntity.setId(null);
            totalQuatity = BigDecimal.ZERO;
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "resetTransfer()");
        }
    }

    //------------------------------------
    public void validateInputsForSave() {
        try {

            if (invTransferEntity.getInvFrom() != null) {
                invTransfer.setInvFrom(invTransferEntity.getInvFrom());
            } else {
                invTransfer.setInvFrom(null);
            }
            if (invTransferEntity.getInvTo() != null) {
                invTransfer.setInvTo(invTransferEntity.getInvTo());
            } else {
                invTransfer.setInvTo(null);
            }
            invTransfer.setTransferDate(invTransferEntity.getTransferDate() != null ? invTransferEntity.getTransferDate() : null);
            invTransfer.setRemarks(invTransferEntity.getRemark() != null ? invTransferEntity.getRemark() : null);
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "validateInputsForSave()");
        }
    }

    public String exit() {
        try {

            if (isPermission) {
                exit("../invtransfer/invTransferList.xhtml");
            } else {
                exit("../invtransferInventory/invTransferInventoryList.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "exit()");
        }
        return "";
    }

    //-------------------------------------------------------------------------------
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {

            fillReport(prepareReport(), getUserData().getReportPath() + "InvTransferOrderReport.jasper", invTransferDetailsEntityList, "pdf");
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "exportPDF()");
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
            saveError(e, "InvTransferFormMB", "fillReport()");
        }
    }

    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();
        try {

            Map<String, String> userDDs = getUserData().getUserDDs();

            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
            hashMap.put("CompanyName", getUserData().getCompany().getName());

            if (isFileExist(getUserData().getImageReportPath())) {
                hashMap.put("companyImage", getUserData().getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }
            hashMap.put("dateText", userDDs.get("DATE"));

            hashMap.put("header2", "تقرير طلب التحويل");

            hashMap.put("InvFromLabel", "من مستودع");
            hashMap.put("invFrom", invTransferEntity.getInvFrom().getName());
            hashMap.put("invTo", invTransferEntity.getInvTo().getName());
            hashMap.put("transferDate", invTransferEntity.getTransferDate());
            hashMap.put("InvToLabel", "إلى مستودع");
            hashMap.put("dateText", "التاريخ");

            hashMap.put("itemNameText", "رقم الصنف");

            hashMap.put("unitText", "الوحدة");
            hashMap.put("totalReportText", "الاجمالى");
            hashMap.put("netCost", "سعر البيع");
            hashMap.put("quantity", "الكمية ");
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "prepareReport()");
        }
        return hashMap;
    }

    //--------------------------------------------------------------
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

    public InvTransfer getInvTransfer() {
        return invTransfer;
    }

    public void setInvTransfer(InvTransfer invTransfer) {
        this.invTransfer = invTransfer;
    }

    public List<InvTransfer> getInvTransferList() {
        return invTransferList;
    }

    public void setInvTransferList(List<InvTransfer> invTransferList) {
        this.invTransferList = invTransferList;
    }

    public InvTransferDetail getInvTransferDetail() {
        return invTransferDetail;
    }

    public void setInvTransferDetail(InvTransferDetail invTransferDetail) {
        this.invTransferDetail = invTransferDetail;
    }

    public InvTransferEntity getInvTransferEntity() {
        return invTransferEntity;
    }

    public void setInvTransferEntity(InvTransferEntity invTransferEntity) {
        this.invTransferEntity = invTransferEntity;
    }

    public InvTransferDetailsEntity getInvTransferDetailEntity() {
        return invTransferDetailEntity;
    }

    public void setInvTransferDetailEntity(InvTransferDetailsEntity invTransferDetailEntity) {
        this.invTransferDetailEntity = invTransferDetailEntity;
    }

    public InvTransferDetailsEntity getInvTransferDetailEntitySelection() {
        return invTransferDetailEntitySelection;
    }

    public void setInvTransferDetailEntitySelection(InvTransferDetailsEntity invTransferDetailEntitySelection) {
        this.invTransferDetailEntitySelection = invTransferDetailEntitySelection;
    }

    public List<InvTransferDetailsEntity> getRowDeleted() {
        return rowDeleted;
    }

    public void setRowDeleted(List<InvTransferDetailsEntity> rowDeleted) {
        this.rowDeleted = rowDeleted;
    }

    public List<InvTransferDetail> getInvTransferDetailList() {
        return invTransferDetailList;
    }

    public void setInvTransferDetailList(List<InvTransferDetail> invTransferDetailList) {
        this.invTransferDetailList = invTransferDetailList;
    }

    public List<InvTransferDetail> getInvTransferDeletedList() {
        return invTransferDeletedList;
    }

    public void setInvTransferDeletedList(List<InvTransferDetail> invTransferDeletedList) {
        this.invTransferDeletedList = invTransferDeletedList;
    }

    public Integer getInvTransferId() {
        return invTransferId;
    }

    public void setInvTransferId(Integer invTransferId) {
        this.invTransferId = invTransferId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public BigDecimal getTotalQuatity() {
        return totalQuatity;
    }

    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
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

    public Map<Integer, InvTransferDetail> getInvTransferDetailMap() {
        return invTransferDetailMap;
    }

    public void setInvTransferDetailMap(Map<Integer, InvTransferDetail> invTransferDetailMap) {
        this.invTransferDetailMap = invTransferDetailMap;
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

    public List<ItemsBarcodesDetailsView> getItemsBarcodesDetailList() {
        return itemsBarcodesDetailList;
    }

    public void setItemsBarcodesDetailList(List<ItemsBarcodesDetailsView> itemsBarcodesDetailList) {
        this.itemsBarcodesDetailList = itemsBarcodesDetailList;
    }

    public ItemsBarcodesDetailsViewConverter getItemsBarcodesDetailsViewConverter() {
        return itemsBarcodesDetailsViewConverter;
    }

    public void setItemsBarcodesDetailsViewConverter(ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter) {
        this.itemsBarcodesDetailsViewConverter = itemsBarcodesDetailsViewConverter;
    }

    public List<InvTransferDetailsEntity> getInvTransferDetailsEntityList() {
        return invTransferDetailsEntityList;
    }

    public void setInvTransferDetailsEntityList(List<InvTransferDetailsEntity> invTransferDetailsEntityList) {
        this.invTransferDetailsEntityList = invTransferDetailsEntityList;
    }

    /**
     * @return the invTransferDetailsEntityCopyList
     */
    public List<InvTransferDetailsEntity> getInvTransferDetailsEntityCopyList() {
        return invTransferDetailsEntityCopyList;
    }

    /**
     * @param invTransferDetailsEntityCopyList the
     * invTransferDetailsEntityCopyList to set
     */
    public void setInvTransferDetailsEntityCopyList(List<InvTransferDetailsEntity> invTransferDetailsEntityCopyList) {
        this.invTransferDetailsEntityCopyList = invTransferDetailsEntityCopyList;
    }

    /**
     * @return the invTransferConverter
     */
    public InvTransferConverter getInvTransferConverter() {
        return invTransferConverter;
    }

    /**
     * @param invTransferConverter the invTransferConverter to set
     */
    public void setInvTransferConverter(InvTransferConverter invTransferConverter) {
        this.invTransferConverter = invTransferConverter;
    }

    /**
     * @return the invTransferDetailEntityFirstBackup
     */
    public InvTransferDetailsEntity getInvTransferDetailEntityFirstBackup() {
        return invTransferDetailEntityFirstBackup;
    }

    /**
     * @param invTransferDetailEntityFirstBackup the
     * invTransferDetailEntityFirstBackup to set
     */
    public void setInvTransferDetailEntityFirstBackup(InvTransferDetailsEntity invTransferDetailEntityFirstBackup) {
        this.invTransferDetailEntityFirstBackup = invTransferDetailEntityFirstBackup;
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
     * @return the transferType
     */
    public Integer getTransferType() {
        return transferType;
    }

    /**
     * @param transferType the transferType to set
     */
    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    /**
     * @return the invTransferDetailConverter
     */
    public InvTransferDetailConverter getInvTransferDetailConverter() {
        return invTransferDetailConverter;
    }

    /**
     * @param invTransferDetailConverter the invTransferDetailConverter to set
     */
    public void setInvTransferDetailConverter(InvTransferDetailConverter invTransferDetailConverter) {
        this.invTransferDetailConverter = invTransferDetailConverter;
    }

    /**
     * @return the invTransferDetailsLoadedList
     */
    public List<InvTransferDetail> getInvTransferDetailsLoadedList() {
        return invTransferDetailsLoadedList;
    }

    /**
     * @param invTransferDetailsLoadedList the invTransferDetailsLoadedList to
     * set
     */
    public void setInvTransferDetailsLoadedList(List<InvTransferDetail> invTransferDetailsLoadedList) {
        this.invTransferDetailsLoadedList = invTransferDetailsLoadedList;
    }

}

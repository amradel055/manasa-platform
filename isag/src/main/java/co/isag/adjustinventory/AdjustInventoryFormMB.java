package co.isag.adjustinventory;

import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvStripDetailService;
import com.isag.businessservice.InvStripService;
import com.isag.businessservice.InventorySetupService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.QuantityItemsStoreService;
import com.isag.converter.InvInventoryConverter;
import com.isag.converter.QuantityItemsStoreConverter;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.entity.InvStrip;
import com.isag.entity.InvStripDetail;
import com.isag.entity.InventorySetup;
import com.isag.entiy.InvStripDetailEntity;
import com.isag.entiy.InvStripEntity;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.views.QuantityItemsStore;

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
@Named(value = "adjustInventoryFormMB")
@ViewScoped
public class AdjustInventoryFormMB extends BaseFormBean {

    private InvItem invItem;

    private InvStrip invStrip;

    private InvStripDetail invStripDetail;
    private List<InvStripDetailEntity> invStripDetailEntityList = new ArrayList<>();
    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;

    private InvStripEntity invStripEntity;
    private InvStripDetailEntity invStripDetailEntity;
    private InvStripDetailEntity invStripDetailEntitySelection;
    private List<InvStripDetailEntity> rowDeleted = new ArrayList<>();

    private List<InvStripDetail> invStripDetailList;
    private List<InvStripDetail> invStripDeletedList;
    private List<InvStripDetail> inveDetailsList;
    private InventorySetup invSetup;
    private Integer invStripId;

    private Integer index = 0;
    private Integer branchId;

    private BigDecimal totalQuantity = BigDecimal.ZERO;
    private BigDecimal totalBalance = BigDecimal.ZERO;

    private Boolean markEdit = Boolean.FALSE;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private boolean showMessage = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;
    List<QuantityItemsStore> quantityItemsStoresList;
    Map<Integer, QuantityItemsStore> quantityItemsStoreMap;
    private QuantityItemsStoreConverter quantityItemsStoreConverter;

    private Map<Integer, InvStripDetail> invStripDetailMap;

    @EJB
    private InvItemService invItemService;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    private InvStripService invStripService;
    @EJB
    private InvStripDetailService invStripDetailService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private QuantityItemsStoreService quantityItemsStoreService;

    @Override
    @PostConstruct
    public void init() {
        load();
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
            invSetup = inventorySetupService.getInventoryByBranchId(branchId);
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetInvStripForm();
                fillItemMap();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    invStripId = (Integer) context.getSessionMap().get("invStripSelected");
                    invStrip = invStripService.findInvStripByInvStripId(invStripId);
                    invStripDetailList = invStripDetailService.getAllInvStripDetails(invStrip.getId());

                    invStripEntity.setInvInventory(invStrip.getInventoryId());
                    fillItemMap();
                    invStripEntity = mapInvStripToEntity(invStrip);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "load");
        }

    }

    private void initObjs() {
        try {
            invStripEntity = new InvStripEntity();
            invStrip = new InvStrip();
            invSetup = new InventorySetup();
            invStripDetail = new InvStripDetail();
            invStripDetailEntity = new InvStripDetailEntity();
            inveDetailsList = new ArrayList<>();
            invStripDeletedList = new ArrayList<>();
            invStripDetailEntitySelection = new InvStripDetailEntity();
            invInventoryList = new ArrayList<>();
            invStripDetailEntityList = new ArrayList<>();
            invStripDetailList = new ArrayList<>();
            quantityItemsStoresList = new ArrayList<>();
            quantityItemsStoreMap = new HashMap<>();
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "initObjs");
        }
    }

    private void fillLists() {
        try {

            invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranch(getUserData().getUser().getId(), branchId);
            invInventoryConverter = new InvInventoryConverter(invInventoryList);

            quantityItemsStoresList = quantityItemsStoreService.findAllQuantityItemsListStoreByBranchId(branchId, invSetup.getSellBuy());
            for (QuantityItemsStore itemsStore : quantityItemsStoresList) {
                quantityItemsStoreMap.put(itemsStore.getItemId(), itemsStore);
            }
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "fillLists");
        }
    }

    public void fillItemMap() {
        try {
            if (invStripEntity.getInvInventory() != null) {
                quantityItemsStoresList = quantityItemsStoreService.findInventoryItemsList(invStripEntity.getInvInventory().getId(), branchId, invSetup.getSellBuy());
                quantityItemsStoreConverter = new QuantityItemsStoreConverter(quantityItemsStoresList);

                invStripDetailEntityList = new ArrayList<>();
            } else {
                errorMessage("لا يوجد مخازن لهذا المستخدم");
            }
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "fillItemMap");
        }
    }

    private void resetInvStripForm() {
        try {
            invStripEntity = new InvStripEntity();
            invStripDetailList = new ArrayList<>();

            invStripDetailEntitySelection = new InvStripDetailEntity();
            if (invInventoryList != null && !invInventoryList.isEmpty()) {
                invStripEntity.setInvInventory(invInventoryList.get(0));
            }
            invStripDetailEntityList = new ArrayList<>();
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "resetInvStripForm");
        }
    }

    public InvStripEntity mapInvStripToEntity(InvStrip strip) {
        try {

            invStripEntity.setId(strip.getId());
            invStripEntity.setBranchId(strip.getBranchId());
            invStripEntity.setStripDate(strip.getStripDate());
            invStripEntity.setStripDocument(strip.getStripDocument());
            invStripEntity.setRemark(strip.getRemarks());
            invStripEntity.setSerial(strip.getSerial());
            invStripEntity.setInvInventory(strip.getInventoryId());

            invStripDetailEntityList = new ArrayList<>();

            invStripDetailMap = new HashMap<>();
            for (InvStripDetail stripdetail : invStripDetailList) {
                invStripDetailEntity = new InvStripDetailEntity();
                invStripDetailEntity.setId(stripdetail.getId() != null ? stripdetail.getId() : null);
                invStripDetailEntity.setSerial(stripdetail.getSerial());
                if (stripdetail.getInvItemId() != null) {
                    invStripDetailEntity.setItemId(stripdetail.getInvItemId());
                    invStripDetailEntity.setItemCode(stripdetail.getInvItemId().getCode());
                    invStripDetailEntity.setItemName(stripdetail.getInvItemId().getName());
                    invStripDetailEntity.setItemsInStore(quantityItemsStoreMap.get(stripdetail.getInvItemId().getId()));
                    invStripDetailEntity.setItemsInStoreTrans(invStripDetailEntity.getItemsInStore());
                }

                invStripDetailEntity.setActualAmount(stripdetail.getActualAmount() != null ? stripdetail.getActualAmount() : BigDecimal.ZERO);
                invStripDetailEntity.setBookBalance(stripdetail.getBookBalance() != null ? stripdetail.getBookBalance() : BigDecimal.ZERO);

                invStripDetailEntity.setDifference(invStripDetailEntity.getBookBalance().subtract(invStripDetailEntity.getActualAmount()));

                invStripDetailMap.put(stripdetail.getId(), stripdetail);

                totalQuantity = totalQuantity.add(invStripDetailEntity.getActualAmount());
                totalBalance = totalBalance.add(invStripDetailEntity.getBookBalance());

                invStripDetailEntityList.add(invStripDetailEntity);
            }

            return invStripEntity;
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "mapInvStripToEntity");
            return null;
        }
    }

    public void adddetails() {
        try {
            for (InvStripDetailEntity detailEntity : invStripDetailEntityList) {

                if (detailEntity.getItemsInStore() == null && detailEntity.getItemsInStoreTrans() != null) {
                    detailEntity.setItemsInStore(detailEntity.getItemsInStoreTrans());
                }

                if (detailEntity.getItemsInStoreTrans() == null || !validateActualAmmount(detailEntity)) {
                    errorMessage("يجب ادخال تفاصيل ضبط المخزن");
                    return;
                }
                detailEntity.setMarkEdit(Boolean.FALSE);
            }
            InvStripDetailEntity newDetailEntity = new InvStripDetailEntity();
            newDetailEntity.setMarkEdit(Boolean.TRUE);
            invStripDetailEntitySelection = newDetailEntity;
            invStripDetailEntityList.add(newDetailEntity);
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "adddetails");
        }
    }

    public void deleteInvStripDetail() {
        try {
            if (invStripDetailEntitySelection.getId() != null) {
                setDelete(Boolean.TRUE);
                rowDeleted.add(invStripDetailEntitySelection);
            }
            invStripDetailEntityList.remove(invStripDetailEntitySelection);

            for (InvStripDetailEntity detailEntity : invStripDetailEntityList) {
                if (detailEntity.getItemsInStore() == null && detailEntity.getItemsInStoreTrans() != null) {
                    detailEntity.setItemsInStore(detailEntity.getItemsInStoreTrans());
                }
            }
            updateSummition();
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "deleteInvStripDetail");
        }
    }

    public boolean validateActualAmmount(InvStripDetailEntity invStripTable) {
        try {
            if (invStripTable.getItemsInStore() != null && invStripTable.getActualAmount() != null && invStripTable.getActualAmount().compareTo(BigDecimal.ZERO) == 1) {
                invStripTable.setDifference(invStripTable.getBookBalance().subtract(invStripTable.getActualAmount()));
                updateSummition();
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "validateActualAmmount");
            return false;
        }
    }

    public Boolean validateItems(InvStripDetailEntity invStripTable) {
        try {
            if (invStripTable.getItemsInStore() == null) {
                resetInvItem(invStripTable);
                return false;
            } else {
                invStripTable.setItemsInStoreTrans(invStripTable.getItemsInStore());
                invStripTable.setBookBalance(invStripTable.getItemsInStore().getQty() != null ? invStripTable.getItemsInStore().getQty() : BigDecimal.ZERO);
                invItem = new InvItem();
                invItem.setId(invStripTable.getItemsInStore().getItemId());
                invStripTable.setItemId(invItem);
                invStripTable.setItemCode(invStripTable.getItemsInStore().getItemCode());
                invStripTable.setItemName(invStripTable.getItemsInStore().getItemCode());
                return true;
            }
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "validateItems");
            return false;
        }
    }

    private void resetInvItem(InvStripDetailEntity invStripTable) {
        try {
            invStripTable.setActualAmount(null);
            invStripTable.setBookBalance(null);
            invStripTable.setDifference(null);
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "resetInvItem");
        }
    }

    private void updateSummition() {
        try {

            BigDecimal actualAmount, bookBalance;
            totalQuantity = BigDecimal.ZERO;
            totalBalance = BigDecimal.ZERO;

            for (InvStripDetailEntity detailEntity : invStripDetailEntityList) {
                if (detailEntity.getItemsInStore() == null && detailEntity.getItemsInStoreTrans() != null) {
                    detailEntity.setItemsInStore(detailEntity.getItemsInStoreTrans());
                }
                actualAmount = detailEntity.getActualAmount() != null ? detailEntity.getActualAmount() : BigDecimal.ZERO;
                bookBalance = detailEntity.getBookBalance() != null ? detailEntity.getBookBalance() : BigDecimal.ZERO;

                detailEntity.setDifference(bookBalance.subtract(actualAmount));

                totalQuantity = totalQuantity.add(actualAmount);
                totalBalance = totalBalance.add(bookBalance);
            }
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "updateSummition");
        }
    }

    @Override
    public String save() {
        try {
            invStrip.setCompanyId(getUserData().getCompany());
            invStrip.setBranchId(getUserData().getUserBranch());

            if (invStrip.getId() == null) {
                invStrip.setCreationDate(new Date());
                invStrip.setCreatedBy(getUserData().getUser());
            } else {
                invStrip.setModificationDate(new Date());
                invStrip.setModifiedBy(getUserData().getUser());
            }

            invStrip.setStripDocument(invStripEntity.getStripDocument());
            invStrip.setStripDate(invStripEntity.getStripDate());
            invStrip.setRemarks(invStripEntity.getRemark());
            invStrip.setType(1);

            if (invStripEntity.getInvInventory() != null) {
                invStrip.setInventoryId(invStripEntity.getInvInventory());
            } else {
                errorMessage("يجب اختيار مخزن");
                return "";
            }

            InvStripDetail reservationDetail;
            if (invStripDetailEntityList != null && !invStripDetailEntityList.isEmpty()) {
                for (InvStripDetailEntity stripDetailsEntity : invStripDetailEntityList) {
                    reservationDetail = new InvStripDetail();

                    reservationDetail.setSerial(stripDetailsEntity.getSerial());
                    if (stripDetailsEntity.getId() != null) {
                        reservationDetail = invStripDetailMap.get(stripDetailsEntity.getId());

                        reservationDetail.setModificationDate(new Date());
                        reservationDetail.setModifiedBy(getUserData().getUser());

                    } else {
                        reservationDetail.setCreationDate(new Date());
                        reservationDetail.setCreatedBy(getUserData().getUser());
                    }
                    reservationDetail.setCompanyId(getUserData().getCompany());
                    reservationDetail.setActualAmount(stripDetailsEntity.getActualAmount());
                    reservationDetail.setBookBalance(stripDetailsEntity.getBookBalance());

                    if (stripDetailsEntity.getItemsInStore() != null && stripDetailsEntity.getItemsInStoreTrans() != null) {
                        invItem = new InvItem();
                        invItem.setId(stripDetailsEntity.getItemsInStore().getItemId());
                        reservationDetail.setInvItemId(stripDetailsEntity.getItemId());
                    } else {
                        errorMessage("يجب اختيار صنف");
                        return "";
                    }
                    reservationDetail.setDifference(((stripDetailsEntity.getItemsInStore() != null && stripDetailsEntity.getItemsInStore().getQty() != null) ? stripDetailsEntity.getItemsInStore().getQty() : BigDecimal.ZERO).subtract(stripDetailsEntity.getActualAmount()));

                    inveDetailsList.add(reservationDetail);
                }

            } else {
                errorMessage("هذا المخزن لا يحتوى على اصناف");
                return "";
            }

            if (rowDeleted != null) {
                InvStripDetail ipo;

                for (InvStripDetailEntity deletedDetailsEntity : getRowDeleted()) {
                    ipo = new InvStripDetail();
                    ipo.setId(deletedDetailsEntity.getId());
                    invStripDeletedList.add(ipo);
                }
            }
            invStripService.addInvStrip(invStrip, inveDetailsList, invStripDeletedList);
            invStripEntity.setId(invStrip.getId());
            return "adjustinventorylist";
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "save");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        try {
            exit("../adjustinventory/adjustinventorylist.xhtml");
            return "";
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "exit");
            return null;
        }
    }

    public List<QuantityItemsStore> completItemsInStore(String query) {
        try {
            List<QuantityItemsStore> itemsStoresList = quantityItemsStoresList;
            if (query == null || query.trim().equals("")) {

                quantityItemsStoreConverter = new QuantityItemsStoreConverter(itemsStoresList);
                return itemsStoresList;
            }
            List<QuantityItemsStore> filteredItems = new ArrayList<>();

            String nameAr;
            String code;
            QuantityItemsStore itemFilter;
            for (int i = 0; i < quantityItemsStoresList.size(); i++) {
                itemFilter = itemsStoresList.get(i);
                nameAr = itemFilter.getItemName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredItems.contains(itemFilter)) {
                        filteredItems.add(itemFilter);
                    }
                }

                code = itemFilter.getItemCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredItems.contains(itemFilter)) {
                        filteredItems.add(itemFilter);
                    }
                }
            }

            quantityItemsStoreConverter = new QuantityItemsStoreConverter(filteredItems);
            return filteredItems;
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "completItemsInStore");
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
        } catch (Exception ex) {
            saveError(ex, "AdjustInventoryFormMB", "completInventory");
            return null;
        }
    }

    public InvItem getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItem invItem) {
        this.invItem = invItem;
    }

    public InvStrip getInvStrip() {
        return invStrip;
    }

    public void setInvStrip(InvStrip invStrip) {
        this.invStrip = invStrip;
    }

    public InvStripDetail getInvStripDetail() {
        return invStripDetail;
    }

    public void setInvStripDetail(InvStripDetail invStripDetail) {
        this.invStripDetail = invStripDetail;
    }

    public InvStripEntity getInvStripEntity() {
        return invStripEntity;
    }

    public void setInvStripEntity(InvStripEntity invStripEntity) {
        this.invStripEntity = invStripEntity;
    }

    public InvStripDetailEntity getInvStripDetailEntity() {
        return invStripDetailEntity;
    }

    public void setInvStripDetailEntity(InvStripDetailEntity invStripDetailEntity) {
        this.invStripDetailEntity = invStripDetailEntity;
    }

    public InvStripDetailEntity getInvStripDetailEntitySelection() {
        return invStripDetailEntitySelection;
    }

    public void setInvStripDetailEntitySelection(InvStripDetailEntity invStripDetailEntitySelection) {
        this.invStripDetailEntitySelection = invStripDetailEntitySelection;
    }

    public List<InvStripDetailEntity> getRowDeleted() {
        return rowDeleted;
    }

    public void setRowDeleted(List<InvStripDetailEntity> rowDeleted) {
        this.rowDeleted = rowDeleted;
    }

    public List<InvStripDetail> getInvStripDetailList() {
        return invStripDetailList;
    }

    public void setInvStripDetailList(List<InvStripDetail> invStripDetailList) {
        this.invStripDetailList = invStripDetailList;
    }

    public List<InvStripDetail> getInvStripDeletedList() {
        return invStripDeletedList;
    }

    public void setInvStripDeletedList(List<InvStripDetail> invStripDeletedList) {
        this.invStripDeletedList = invStripDeletedList;
    }

    public List<InvStripDetail> getInveDetailsList() {
        return inveDetailsList;
    }

    public void setInveDetailsList(List<InvStripDetail> inveDetailsList) {
        this.inveDetailsList = inveDetailsList;
    }

    public Integer getInvStripId() {
        return invStripId;
    }

    public void setInvStripId(Integer invStripId) {
        this.invStripId = invStripId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
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

    public Map<Integer, InvStripDetail> getInvStripDetailMap() {
        return invStripDetailMap;
    }

    public void setInvStripDetailMap(Map<Integer, InvStripDetail> invStripDetailMap) {
        this.invStripDetailMap = invStripDetailMap;
    }

    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    public List<InvStripDetailEntity> getInvStripDetailEntityList() {
        return invStripDetailEntityList;
    }

    public void setInvStripDetailEntityList(List<InvStripDetailEntity> invStripDetailEntityList) {
        this.invStripDetailEntityList = invStripDetailEntityList;
    }

    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }

    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    public QuantityItemsStoreConverter getQuantityItemsStoreConverter() {
        return quantityItemsStoreConverter;
    }

    public void setQuantityItemsStoreConverter(QuantityItemsStoreConverter quantityItemsStoreConverter) {
        this.quantityItemsStoreConverter = quantityItemsStoreConverter;
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

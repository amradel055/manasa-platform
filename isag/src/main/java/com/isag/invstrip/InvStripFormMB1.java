package com.isag.invstrip;

import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvStripDetailService;
import com.isag.businessservice.InvStripService;
import com.isag.businessservice.InventorySetupService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.QuantityItemsStoreService;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.converter.InvInventoryConverter;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.entity.InvStrip;
import com.isag.entity.InvStripDetail;
import com.isag.entity.InventorySetup;
import com.isag.entiy.InvStripDetailEntity;
import com.isag.entiy.InvStripEntity;
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
@Named(value = "InvStripFormMB1")
@ViewScoped
public class InvStripFormMB1 extends BaseFormBean {

    private InvItem invItem;

    private InvStrip invStrip;
    private InventorySetup invSetup;
    private InvStripDetail invStripDetail;
    private List<InvStripDetailEntity> invStripDetailEntityList = new ArrayList<>();
    private List<InvStripDetailEntity> invStripDetailEntityBackUpList = new ArrayList<>();
    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;

    private InvStripEntity invStripEntity;
    private InvStripDetailEntity invStripDetailEntity;
    private InvStripDetailEntity invStripDetailEntitySelection;
    private List<InvStripDetailEntity> rowDeleted = new ArrayList<>();

    private List<InvStripDetail> invStripDetailList;
    private List<InvStripDetail> invStripDeletedList;
    private List<InvStripDetail> inveDetailsList;

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
                invStripEntity = mapInvStripToEntity(invStrip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initObjs() {
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
    }

    private void fillLists() {

        invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranch(getUserData().getUser().getId(), branchId);
        invInventoryConverter = new InvInventoryConverter(invInventoryList);
    }

    private void resetInvStripForm() {
        invStripEntity = new InvStripEntity();
        invStripDetailList = new ArrayList<>();

        invStripDetailEntitySelection = new InvStripDetailEntity();
        if (invInventoryList != null && !invInventoryList.isEmpty()) {
            invStripEntity.setInvInventory(invInventoryList.get(0));
            invStripEntity.setStripDate(new Date());

        }
        invStripDetailEntityList = invStripDetailEntityBackUpList = new ArrayList<>();
    }

    public InvStripEntity mapInvStripToEntity(InvStrip strip) {

        invStripEntity.setId(strip.getId());
        invStripEntity.setBranchId(strip.getBranchId());
        invStripEntity.setStripDate(strip.getStripDate());
        invStripEntity.setStripDocument(strip.getStripDocument());
        invStripEntity.setRemark(strip.getRemarks());
        invStripEntity.setSerial(strip.getSerial());
        invStripEntity.setInvInventory(strip.getInventoryId());

        invStripDetailEntityList = new ArrayList<>();
        invStripDetailEntityBackUpList = new ArrayList<>();

        invStripDetailMap = new HashMap<>();
        for (InvStripDetail stripdetail : invStripDetailList) {
            invStripDetailEntity = new InvStripDetailEntity();
            invStripDetailEntity.setId(stripdetail.getId() != null ? stripdetail.getId() : null);
            invStripDetailEntity.setSerial(stripdetail.getSerial());
            if (stripdetail.getInvItemId() != null) {
                invStripDetailEntity.setItemId(stripdetail.getInvItemId());
                invStripDetailEntity.setItemCode(stripdetail.getInvItemId().getCode());
                invStripDetailEntity.setItemName(stripdetail.getInvItemId().getName());
            }
            invStripDetailEntity.setActualAmount(stripdetail.getActualAmount() != null ? stripdetail.getActualAmount() : BigDecimal.ZERO);
            invStripDetailEntity.setBookBalance(stripdetail.getBookBalance() != null ? stripdetail.getBookBalance() : BigDecimal.ZERO);

            invStripDetailEntity.setDifference(invStripDetailEntity.getBookBalance().subtract(invStripDetailEntity.getActualAmount()));

            invStripDetailMap.put(stripdetail.getId(), stripdetail);

            updateSummition(invStripDetailEntity);

            invStripDetailEntityList.add(invStripDetailEntity);
            invStripDetailEntityBackUpList.add(invStripDetailEntity);
        }

        return invStripEntity;
    }

    public void adddetails() {
        for (InvStripDetailEntity detailEntity : invStripDetailEntityList) {
            detailEntity.setMarkEdit(Boolean.FALSE);
        }
        InvStripDetailEntity newDetailEntity = new InvStripDetailEntity();
        newDetailEntity.setMarkEdit(Boolean.TRUE);
        invStripDetailEntityList.add(newDetailEntity);
        invStripDetailEntityBackUpList.add(newDetailEntity);
    }

    @Override
    public String save() {
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
        invStrip.setType(0);

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
                reservationDetail.setDifference(stripDetailsEntity.getBookBalance().subtract(stripDetailsEntity.getActualAmount()));

                if (stripDetailsEntity.getItemId() != null) {
                    reservationDetail.setInvItemId(stripDetailsEntity.getItemId());
                } else {
                    reservationDetail.setInvItemId(null);
                }
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
        InvPurchaseReturnSave invPurchaseReturnSave;
        invPurchaseReturnSave = invStripService.addInvStrip(invStrip, inveDetailsList, invStripDeletedList);

        resetStrip();
        invStrip = invPurchaseReturnSave.getInvStrip();
        invStripDetailList = invPurchaseReturnSave.getInvStripDetailList();

        invStripEntity = mapInvStripToEntity(invPurchaseReturnSave.getInvStrip());

        return "";
    }
public void reset() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        context.getSessionMap().replace("ScreenMode", "Add");
        setScreenMode("Add");

        init();
    }
    public void resetStrip() {
        invStrip = new InvStrip();
        inveDetailsList = new ArrayList<>();
        invStripDeletedList = new ArrayList<>();
        invStripDetailList = new ArrayList<>();

        invStripEntity.setSerial(null);
        invStripEntity.setId(null);
        invStripDetailEntityList = new ArrayList<>();
        totalQuantity = BigDecimal.ZERO;
        totalBalance = BigDecimal.ZERO;
    }

    //------------------------------------

    public void deleteInvStripDetail() {

        setShowMessageGeneral(Boolean.FALSE);
        setShowMessageDetails(Boolean.TRUE);
        if (invStripDetailEntitySelection.getId() != null) {
            setDelete(Boolean.TRUE);
            rowDeleted.add(invStripDetailEntitySelection);
        }
        invStripDetailEntityList.remove(invStripDetailEntitySelection);
        invStripDetailEntityBackUpList.remove(invStripDetailEntitySelection);

        totalBalance = totalQuantity = BigDecimal.ZERO;
        for (InvStripDetailEntity detailEntity : invStripDetailEntityList) {
            updateSummition(detailEntity);
        }
    }

    public void updateDifference(InvStripDetailEntity invStripTable) {
        if (invStripTable.getActualAmount() != null) {
            invStripTable.setDifference(
                    (invStripTable.getBookBalance() != null ? invStripTable.getBookBalance() : BigDecimal.ZERO).
                    subtract(invStripTable.getActualAmount())
            );
        }

        for (InvStripDetailEntity detailEntity : invStripDetailEntityList) {
            updateSummition(detailEntity);
        }
    }

    private void updateSummition(InvStripDetailEntity detailEntity) {
        totalQuantity = totalQuantity.add(detailEntity.getActualAmount() != null ? detailEntity.getActualAmount() : BigDecimal.ZERO);
        totalBalance = totalBalance.add(detailEntity.getBookBalance() != null ? detailEntity.getBookBalance() : BigDecimal.ZERO);
    }

    private void updateInvItemsRelated(Integer id) {
        invItem = new InvItem();
        invItem = invItemService.findInvItem(id);
        invStripDetailEntitySelection.setItemCode(invItem.getCode());
        invStripDetailEntitySelection.setItemName(invItem.getName());
    }

    public void fillItemMap() {
        if (invStripEntity.getInvInventory() != null) {
            List<QuantityItemsStore> quantityItemsStoresList = quantityItemsStoreService.findInventoryItemsList(invStripEntity.getInvInventory().getId(), branchId, invSetup.getSellBuy());
//            Map<Integer,QuantityItemsStore> map = new HashMap<>();
//            
//            for (QuantityItemsStore itemsStore : quantityItemsStoresList) {
//                map.put(itemsStore.getItemId(), itemsStore);
//            }

            totalBalance = totalQuantity = BigDecimal.ZERO;

            invStripDetailEntityList = new ArrayList<>();
            for (QuantityItemsStore itemsStore : quantityItemsStoresList) {
                invStripDetailEntity = new InvStripDetailEntity();
                invStripDetailEntity.setActualAmount(BigDecimal.ZERO);
                invStripDetailEntity.setBookBalance(itemsStore.getQty());
                invStripDetailEntity.setDifference(invStripDetailEntity.getActualAmount().subtract(invStripDetailEntity.getBookBalance()));
                invItem = new InvItem();
                invItem.setId(itemsStore.getItemId());
                invStripDetailEntity.setItemId(invItem);
                invStripDetailEntity.setItemCode(itemsStore.getItemCode());
                invStripDetailEntity.setItemName(itemsStore.getItemName());

                updateSummition(invStripDetailEntity);
                invStripDetailEntityList.add(invStripDetailEntity);
            }

//            if (invStripId != null && invStripId > 0) { // In Edit Mode
//                for (InvStripDetailEntity entity : invStripDetailEntityBackUpList) {
//                    for (InvStripDetailEntity entity1 : invStripDetailEntityList) {
//                        if (Objects.equals(entity.getItem(), entity1.getItem())) {
//                            entity1.setId(entity.getId());
//                            entity1.setSerial(entity.getSerial());
//                            break;
//                        }
//                    }
//                }
//            }
            if (invStripDetailList != null && !invStripDetailList.isEmpty()) {
                for (InvStripDetail entity : invStripDetailList) {
                    for (InvStripDetailEntity entity1 : invStripDetailEntityList) {
                        if (Objects.equals(entity.getInvItemId().getId(), entity1.getItemId())) {
                            entity1.setId(entity.getId());
                            entity1.setSerial(entity.getSerial());
                            break;
                        }
                    }
                }
            }

        } else {
            errorMessage("يجب اختيار مخزن");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        String column_name = event.getColumn().getClientId();

        DataTable dataTable = (DataTable) event.getSource();
        invStripDetailEntitySelection = (InvStripDetailEntity) dataTable.getRowData();

        if (column_name.contains("itemName") || column_name.contains("itemNumber")) {
            updateInvItemsRelated((Integer) newValue);
        }

        if (column_name.contains("Quantity")) {
            invStripDetailEntitySelection.setDifference(invStripDetailEntitySelection.getBookBalance()
                    .subtract(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO));
            totalQuantity = totalQuantity.subtract(oldValue != null ? (BigDecimal) oldValue : BigDecimal.ZERO);
            totalQuantity = totalQuantity.add(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO);
        }

        if (column_name.contains("balance")) {
            invStripDetailEntitySelection.setDifference((newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO)
                    .subtract(invStripDetailEntitySelection.getActualAmount()));
            totalBalance = totalBalance.subtract(oldValue != null ? (BigDecimal) oldValue : BigDecimal.ZERO);
            totalBalance = totalBalance.add(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO);
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../invstrip/invStripList.xhtml");
        return "";
    }

    public List<InvInventory> completInventory(String query) {
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

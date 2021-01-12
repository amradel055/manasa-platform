package com.toby.invupdate;

import com.toby.businessservice.InvItemService;
import com.toby.businessservice.InvUpdateDetailService;
import com.toby.businessservice.InvUpdateService;
import com.toby.businessservice.QuantityItemsStoreService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.converter.InvInventoryConverter;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvUpdate;
import com.toby.entity.InvUpdateDetail;
import com.toby.entity.InventorySetup;
import com.toby.entiy.InvUpdateDetailEntity;
import com.toby.entiy.InvUpdateEntity;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import com.toby.views.QuantityItemsStore;
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
import com.toby.businessservice.InventorySetupService;

/**
 * @author khaled
 */
@Named(value = "invUpdateFormMB")
@ViewScoped
public class InvUpdateFormMB extends BaseFormBean {

    private InvItem invItem;

    private InvUpdate invUpdate;

    private InvUpdateDetail invUpdateDetail;
    private List<InvUpdateDetailEntity> invUpdateDetailEntityList = new ArrayList<>();
    private List<InvUpdateDetailEntity> invUpdateDetailEntityBackUpList = new ArrayList<>();
    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;

    private InvUpdateEntity invUpdateEntity;
    private InvUpdateDetailEntity invUpdateDetailEntity;
    private InvUpdateDetailEntity invUpdateDetailEntitySelection;
    private List<InvUpdateDetailEntity> rowDeleted = new ArrayList<>();

    private List<InvUpdateDetail> invUpdateDetailList;
    private List<InvUpdateDetail> invUpdateDeletedList;
    private List<InvUpdateDetail> inveDetailsList;

    private Integer invUpdateId;

    private Integer index = 0;
    private Integer branchId;

    private BigDecimal totalQuantity = BigDecimal.ZERO;
    private BigDecimal totalBalance = BigDecimal.ZERO;

    private Boolean markEdit = Boolean.FALSE;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private boolean showMessage = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

    private Map<Integer, InvUpdateDetail> invUpdateDetailMap;
    InventorySetup invSetup;
    @EJB
    private InvItemService invItemService;
    @EJB
    InventorySetupService inventorySetupService;
    @EJB
    private InvUpdateService invUpdateService;
    @EJB
    private InvUpdateDetailService invUpdateDetailService;
    @EJB
    private TobyUserInventoryService isagUserInventoryService;
    @EJB
    private QuantityItemsStoreService quantityItemsStoreService;

    @Override
    @PostConstruct
    public void init() {
        try {
            invSetup = new InventorySetup();
            load();
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "init()");
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
                resetInvUpdateForm();
                fillItemMap();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    invUpdateId = (Integer) context.getSessionMap().get("invUpdateSelected");
                    invUpdate = invUpdateService.findInvUpdateByInvUpdateId(invUpdateId);
                    invUpdateDetailList = invUpdateDetailService.getAllInvUpdateDetails(invUpdate.getId());
                    invUpdateEntity = mapInvUpdateToEntity(invUpdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "load()");
        }
    }

    private void initObjs() {
        try {

            invUpdateEntity = new InvUpdateEntity();
            invUpdate = new InvUpdate();
            invUpdateDetail = new InvUpdateDetail();
            invUpdateDetailEntity = new InvUpdateDetailEntity();
            inveDetailsList = new ArrayList<>();
            invUpdateDeletedList = new ArrayList<>();
            invUpdateDetailEntitySelection = new InvUpdateDetailEntity();
            invInventoryList = new ArrayList<>();
            invUpdateDetailEntityList = new ArrayList<>();
            invUpdateDetailList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "initObjs()");
        }
    }

    private void fillLists() {
        try {
            invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId);
            invInventoryConverter = new InvInventoryConverter(invInventoryList);
            invSetup = inventorySetupService.getInventoryByBranchId(branchId);
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "fillLists()");
        }
    }

    private void resetInvUpdateForm() {
        try {

            invUpdateEntity = new InvUpdateEntity();
            invUpdateDetailList = new ArrayList<>();

            invUpdateDetailEntitySelection = new InvUpdateDetailEntity();
            if (invInventoryList != null && !invInventoryList.isEmpty()) {
                invUpdateEntity.setInvInventory(invInventoryList.get(0));
                invUpdateEntity.setUpdateDate(new Date());

            }
            invUpdateDetailEntityList = invUpdateDetailEntityBackUpList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "resetInvUpdateForm()");
        }
    }

    public InvUpdateEntity mapInvUpdateToEntity(InvUpdate update) {
        try {
            invUpdateEntity.setId(update.getId());
            invUpdateEntity.setBranchId(update.getBranchId());
            invUpdateEntity.setUpdateDate(update.getDate());
            invUpdateEntity.setUpdateDocument(update.getDocument());
            invUpdateEntity.setRemark(update.getRemarks());
            invUpdateEntity.setSerial(update.getSerial());
            invUpdateEntity.setInvInventory(update.getInventoryId());
            invUpdateEntity.setCreatedBy(update.getCreatedBy());
            invUpdateDetailEntityList = new ArrayList<>();
            invUpdateDetailEntityBackUpList = new ArrayList<>();

            invUpdateDetailMap = new HashMap<>();
            for (InvUpdateDetail updatedetail : invUpdateDetailList) {
                invUpdateDetailEntity = new InvUpdateDetailEntity();
                invUpdateDetailEntity.setId(updatedetail.getId() != null ? updatedetail.getId() : null);
                invUpdateDetailEntity.setSerial(updatedetail.getSerial());
                if (updatedetail.getInvItemId() != null) {
                    invUpdateDetailEntity.setInvItemId(updatedetail.getInvItemId());

                    invUpdateDetailEntity.setItemBarcode(updatedetail.getItemBarcode());
                }
                invUpdateDetailEntity.setOldAmount(updatedetail.getOldAmount() != null ? updatedetail.getOldAmount() : BigDecimal.ZERO);
                invUpdateDetailEntity.setNewAmount(updatedetail.getNewAmount() != null ? updatedetail.getNewAmount() : BigDecimal.ZERO);

                invUpdateDetailEntity.setDifference(invUpdateDetailEntity.getNewAmount().subtract(invUpdateDetailEntity.getOldAmount()));
                //   invUpdateDetailEntity.setValue(invUpdateDetailEntity.getInvItemId().getCostAverage().multiply(invUpdateDetailEntity.getDifference()));
                invUpdateDetailMap.put(updatedetail.getId(), updatedetail);

                updateSummition(invUpdateDetailEntity);

                invUpdateDetailEntityList.add(invUpdateDetailEntity);
                invUpdateDetailEntityBackUpList.add(invUpdateDetailEntity);
            }

        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "mapInvUpdateToEntity()");
        }
        return invUpdateEntity;
    }

    public void adddetails() {
        try {

            for (InvUpdateDetailEntity detailEntity : invUpdateDetailEntityList) {
                detailEntity.setMarkEdit(Boolean.FALSE);
            }
            InvUpdateDetailEntity newDetailEntity = new InvUpdateDetailEntity();
            newDetailEntity.setMarkEdit(Boolean.TRUE);
            invUpdateDetailEntityList.add(newDetailEntity);
            invUpdateDetailEntityBackUpList.add(newDetailEntity);
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "adddetails()");
        }
    }

    @Override
    public String save() {
        try {
            invUpdate.setCompanyId(getUserData().getCompany());
            invUpdate.setBranchId(getUserData().getUserBranch());

            if (invUpdate.getId() == null) {
                invUpdate.setCreationDate(new Date());
                invUpdate.setCreatedBy(getUserData().getUser());
            } else {
                invUpdate.setModificationDate(new Date());
                invUpdate.setModifiedBy(getUserData().getUser());
            }

            invUpdate.setDocument(invUpdateEntity.getUpdateDocument());
            invUpdate.setDate(invUpdateEntity.getUpdateDate());
            invUpdate.setRemarks(invUpdateEntity.getRemark());

            if (invUpdateEntity.getInvInventory() != null) {
                invUpdate.setInventoryId(invUpdateEntity.getInvInventory());
            } else {
                errorMessage("يجب اختيار مخزن");
                return "";
            }

            InvUpdateDetail reservationDetail;
            if (invUpdateDetailEntityList != null && !invUpdateDetailEntityList.isEmpty()) {
                for (InvUpdateDetailEntity updateDetailsEntity : invUpdateDetailEntityList) {
                    reservationDetail = new InvUpdateDetail();

                    reservationDetail.setSerial(updateDetailsEntity.getSerial());
                    if (updateDetailsEntity.getId() != null) {
                        reservationDetail = invUpdateDetailMap.get(updateDetailsEntity.getId());

                        reservationDetail.setModificationDate(new Date());
                        reservationDetail.setModifiedBy(getUserData().getUser());

                    } else {
                        reservationDetail.setCreationDate(new Date());
                        reservationDetail.setCreatedBy(getUserData().getUser());
                    }
                    reservationDetail.setCompanyId(getUserData().getCompany());
                    reservationDetail.setOldAmount(updateDetailsEntity.getOldAmount());
                    reservationDetail.setNewAmount(updateDetailsEntity.getNewAmount());
                    reservationDetail.setDifference(updateDetailsEntity.getNewAmount().subtract(updateDetailsEntity.getOldAmount()));

                    if (updateDetailsEntity.getInvItemId() != null) {
                        reservationDetail.setInvItemId(updateDetailsEntity.getInvItemId());
                        reservationDetail.setValue(updateDetailsEntity.getInvItemId().getCostAverage().multiply(updateDetailsEntity.getDifference()));

                    } else {
                        reservationDetail.setInvItemId(null);
                        reservationDetail.setValue(BigDecimal.ZERO);

                    }

                    inveDetailsList.add(reservationDetail);
                }

            } else {
                errorMessage("هذا المخزن لا يحتوى على اصناف");
                return "";
            }

            if (rowDeleted != null) {
                InvUpdateDetail ipo;

                for (InvUpdateDetailEntity deletedDetailsEntity : getRowDeleted()) {
                    ipo = new InvUpdateDetail();
                    ipo.setId(deletedDetailsEntity.getId());
                    invUpdateDeletedList.add(ipo);
                }
            }
            invUpdateService.addInvUpdate(invUpdate, inveDetailsList, invUpdateDeletedList);
            invUpdateEntity.setId(invUpdate.getId());
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "save()");
        }
        return "invUpdateList";
    }

    public void deleteInvUpdateDetail() {
        try {
            setShowMessageGeneral(Boolean.FALSE);
            setShowMessageDetails(Boolean.TRUE);
            if (invUpdateDetailEntitySelection.getId() != null) {
                setDelete(Boolean.TRUE);
                rowDeleted.add(invUpdateDetailEntitySelection);
            }
            invUpdateDetailEntityList.remove(invUpdateDetailEntitySelection);
            invUpdateDetailEntityBackUpList.remove(invUpdateDetailEntitySelection);

            totalBalance = totalQuantity = BigDecimal.ZERO;
            for (InvUpdateDetailEntity detailEntity : invUpdateDetailEntityList) {
                updateSummition(detailEntity);
            }
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "deleteInvUpdateDetail()");
        }
    }

    public void updateDifference(InvUpdateDetailEntity invUpdateTable) {
        try {
            if (invUpdateTable.getOldAmount() != null) {
                invUpdateTable.setDifference(
                        (invUpdateTable.getNewAmount() != null ? invUpdateTable.getNewAmount() : BigDecimal.ZERO).
                        subtract(invUpdateTable.getOldAmount())
                );
            }

            for (InvUpdateDetailEntity detailEntity : invUpdateDetailEntityList) {
                updateSummition(detailEntity);
            }
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "updateDifference()");
        }
    }

    private void updateSummition(InvUpdateDetailEntity detailEntity) {
        try {

            totalQuantity = totalQuantity.add(detailEntity.getOldAmount() != null ? detailEntity.getOldAmount() : BigDecimal.ZERO);
            totalBalance = totalBalance.add(detailEntity.getNewAmount() != null ? detailEntity.getNewAmount() : BigDecimal.ZERO);
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "updateSummition()");
        }
    }

    private void updateInvItemsRelated(Integer id) {
        try {

            invItem = new InvItem();
            invItem = invItemService.findInvItem(id);
            invUpdateDetailEntitySelection.setItemBarcode(invItem.getCode());
            invUpdateDetailEntitySelection.setInvItemId(invItem);
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "updateInvItemsRelated()");
        }
    }

    public void fillItemMap() {
        try {

            if (invUpdateEntity.getInvInventory() != null) {

                List<QuantityItemsStore> quantityItemsStoresList = quantityItemsStoreService.findInventoryItemsList(invUpdateEntity.getInvInventory().getId(), branchId, invSetup.getSellBuy());
//            Map<Integer,QuantityItemsStore> map = new HashMap<>();
//            
//            for (QuantityItemsStore itemsStore : quantityItemsStoresList) {
//                map.put(itemsStore.getItemId(), itemsStore);
//            }

                totalBalance = totalQuantity = BigDecimal.ZERO;

                invUpdateDetailEntityList = new ArrayList<>();
                for (QuantityItemsStore itemsStore : quantityItemsStoresList) {
                    invUpdateDetailEntity = new InvUpdateDetailEntity();
                    invUpdateDetailEntity.setOldAmount(BigDecimal.ZERO);
                    invUpdateDetailEntity.setNewAmount(itemsStore.getQty());
                    invUpdateDetailEntity.setDifference(invUpdateDetailEntity.getOldAmount().subtract(invUpdateDetailEntity.getNewAmount()));

                    invItem = new InvItem();
                    invItem.setId(itemsStore.getItemId());

                    invItem.setCostAverage(itemsStore.getCostAverage());
                    invItem.setName(itemsStore.getItemName());
                    invItem.setCode(itemsStore.getItemCode());
                    invUpdateDetailEntity.setInvItemId(invItem);

                    invUpdateDetailEntity.setItemBarcode(itemsStore.getItemCode());

                    // invUpdateDetailEntity.setValue(invUpdateDetailEntity.getInvItemId().getCostAverage().multiply(invUpdateDetailEntity.getDifference()));
                    updateSummition(invUpdateDetailEntity);
                    invUpdateDetailEntityList.add(invUpdateDetailEntity);
                }

//            if (invUpdateId != null && invUpdateId > 0) { // In Edit Mode
//                for (InvUpdateDetailEntity entity : invUpdateDetailEntityBackUpList) {
//                    for (InvUpdateDetailEntity entity1 : invUpdateDetailEntityList) {
//                        if (Objects.equals(entity.getItem(), entity1.getItem())) {
//                            entity1.setId(entity.getId());
//                            entity1.setSerial(entity.getSerial());
//                            break;
//                        }
//                    }
//                }
//            }
                if (invUpdateDetailList != null && !invUpdateDetailList.isEmpty()) {
                    for (InvUpdateDetail entity : invUpdateDetailList) {
                        for (InvUpdateDetailEntity entity1 : invUpdateDetailEntityList) {
                            if (Objects.equals(entity.getInvItemId().getId(), entity1.getInvItemId())) {
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
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "fillItemMap()");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {

            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();
            String column_name = event.getColumn().getClientId();

            DataTable dataTable = (DataTable) event.getSource();
            invUpdateDetailEntitySelection = (InvUpdateDetailEntity) dataTable.getRowData();

            if (column_name.contains("itemName") || column_name.contains("itemNumber")) {
                updateInvItemsRelated((Integer) newValue);
            }

            if (column_name.contains("Quantity")) {
                invUpdateDetailEntitySelection.setDifference(invUpdateDetailEntitySelection.getNewAmount()
                        .subtract(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO));
                totalQuantity = totalQuantity.subtract(oldValue != null ? (BigDecimal) oldValue : BigDecimal.ZERO);
                totalQuantity = totalQuantity.add(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO);
            }

            if (column_name.contains("balance")) {
                invUpdateDetailEntitySelection.setDifference((newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO)
                        .subtract(invUpdateDetailEntitySelection.getOldAmount()));
                totalBalance = totalBalance.subtract(oldValue != null ? (BigDecimal) oldValue : BigDecimal.ZERO);
                totalBalance = totalBalance.add(newValue != null ? (BigDecimal) newValue : BigDecimal.ZERO);
            }
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "onCellEdit()");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        try {

            exit("../invupdate/invUpdateList.xhtml");
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "exit()");
        }
        return "";
    }

    public List<InvInventory> completInventory(String query) {
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
            saveError(e, "InvUpdateFormMB", "completInventory()");
        }
        return filteredInvs;
    }

    public InvItem getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItem invItem) {
        this.invItem = invItem;
    }

    public InvUpdate getInvUpdate() {
        return invUpdate;
    }

    public void setInvUpdate(InvUpdate invUpdate) {
        this.invUpdate = invUpdate;
    }

    public InvUpdateDetail getInvUpdateDetail() {
        return invUpdateDetail;
    }

    public void setInvUpdateDetail(InvUpdateDetail invUpdateDetail) {
        this.invUpdateDetail = invUpdateDetail;
    }

    public InvUpdateEntity getInvUpdateEntity() {
        return invUpdateEntity;
    }

    public void setInvUpdateEntity(InvUpdateEntity invUpdateEntity) {
        this.invUpdateEntity = invUpdateEntity;
    }

    public InvUpdateDetailEntity getInvUpdateDetailEntity() {
        return invUpdateDetailEntity;
    }

    public void setInvUpdateDetailEntity(InvUpdateDetailEntity invUpdateDetailEntity) {
        this.invUpdateDetailEntity = invUpdateDetailEntity;
    }

    public InvUpdateDetailEntity getInvUpdateDetailEntitySelection() {
        return invUpdateDetailEntitySelection;
    }

    public void setInvUpdateDetailEntitySelection(InvUpdateDetailEntity invUpdateDetailEntitySelection) {
        this.invUpdateDetailEntitySelection = invUpdateDetailEntitySelection;
    }

    public List<InvUpdateDetailEntity> getRowDeleted() {
        return rowDeleted;
    }

    public void setRowDeleted(List<InvUpdateDetailEntity> rowDeleted) {
        this.rowDeleted = rowDeleted;
    }

    public List<InvUpdateDetail> getInvUpdateDetailList() {
        return invUpdateDetailList;
    }

    public void setInvUpdateDetailList(List<InvUpdateDetail> invUpdateDetailList) {
        this.invUpdateDetailList = invUpdateDetailList;
    }

    public List<InvUpdateDetail> getInvUpdateDeletedList() {
        return invUpdateDeletedList;
    }

    public void setInvUpdateDeletedList(List<InvUpdateDetail> invUpdateDeletedList) {
        this.invUpdateDeletedList = invUpdateDeletedList;
    }

    public List<InvUpdateDetail> getInveDetailsList() {
        return inveDetailsList;
    }

    public void setInveDetailsList(List<InvUpdateDetail> inveDetailsList) {
        this.inveDetailsList = inveDetailsList;
    }

    public Integer getInvUpdateId() {
        return invUpdateId;
    }

    public void setInvUpdateId(Integer invUpdateId) {
        this.invUpdateId = invUpdateId;
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

    public Map<Integer, InvUpdateDetail> getInvUpdateDetailMap() {
        return invUpdateDetailMap;
    }

    public void setInvUpdateDetailMap(Map<Integer, InvUpdateDetail> invUpdateDetailMap) {
        this.invUpdateDetailMap = invUpdateDetailMap;
    }

    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    public List<InvUpdateDetailEntity> getInvUpdateDetailEntityList() {
        return invUpdateDetailEntityList;
    }

    public void setInvUpdateDetailEntityList(List<InvUpdateDetailEntity> invUpdateDetailEntityList) {
        this.invUpdateDetailEntityList = invUpdateDetailEntityList;
    }

    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }

    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }
}

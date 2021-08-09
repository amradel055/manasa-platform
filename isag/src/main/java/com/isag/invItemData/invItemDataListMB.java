/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invItemData;

import com.isag.businessservice.InvGlobalService;
import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InventorySetupService;
import com.isag.entity.InvItem;
import com.isag.entiy.ItemDataEntity;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import com.isag.views.InvItemView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author WIN7
 */
@Named(value = "itemDataListMB")
@ViewScoped
public class invItemDataListMB extends BaseListBean implements Serializable {

    private Integer branchId;
    private UserData userData;
    private ItemDataEntity itemDataEntity;
    private Integer itemDataEntityId;
    private List<ItemDataEntity> itemDataEntityList;
    private InvItem invItem;
    private List<InvItem> itemDataByBranch;
    private List<InvItemView> itemDataViewByBranch;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private boolean post_flag = Boolean.FALSE;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean markEdit = Boolean.FALSE;

    private LazyDataModel<ItemDataEntity> dataModel;

    @EJB
    private InvItemService itemDataService;
    @EJB
    InvGlobalService invGlobalService;
    
    
    @Override
    @PostConstruct
    public void init() {
        try {
            userData = new UserData();
            itemDataEntity = new ItemDataEntity();
            itemDataEntityList = new ArrayList<>();
            invItem = new InvItem();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            load();
        } catch (Exception e) {
            saveError(e, "invItemDataListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            setBranchId(getUserData().getUserBranch().getId());
            itemDataByBranch = new ArrayList<>();
            setItemDataViewByBranch(new ArrayList<>());

//        itemDataByBranch = itemDataService.getInvItemByBranchId(branchId);
            this.dataModel = new LazyDataModel<ItemDataEntity>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<ItemDataEntity> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    setRowCount(itemDataService.getTotalRegistors(branchId, filters).intValue());
                    itemDataByBranch = itemDataService.getInvItemViewData(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), branchId, filters,getInvInventorySelection() == null ? null : getInvInventorySelection().getId());
                    itemDataEntityList = new ArrayList<>();
                    for (InvItem invItemMapper : itemDataByBranch) {

                        itemDataEntity = new ItemDataEntity();

                        itemDataEntity.setId(invItemMapper.getId() != null ? invItemMapper.getId() : null);
                        itemDataEntity.setBranchId(invItemMapper.getBranchId());
                        itemDataEntity.setBrandId(invItemMapper.getBrandId());
                        itemDataEntity.setCode(invItemMapper.getCode());
                        itemDataEntity.setCompanyId(invItemMapper.getCompanyId());
                        itemDataEntity.setGroupId(invItemMapper.getGroupId());
                        itemDataEntity.setGroupName(invItemMapper.getGroupId() != null ? invItemMapper.getGroupId().getName() : null);
                        itemDataEntity.setName(invItemMapper.getName());
                        itemDataEntity.setNameen(invItemMapper.getNameen());
                        itemDataEntity.setNickName(invItemMapper.getNickname() != null ? invItemMapper.getNickname() : null);
                        itemDataEntity.setWeightPackage(invItemMapper.getWeightPackage());
                        itemDataEntity.setSiteId(invItemMapper.getSiteId());
                        itemDataEntity.setUnitId(invItemMapper.getUnitId());
                        itemDataEntity.setUnitName(invItemMapper.getUnitId() != null ? (invItemMapper.getUnitId().getName() != null ? invItemMapper.getUnitId().getName() : null) : null);
                        itemDataEntity.setSellPrice(invItemMapper.getSellPrice());
                        itemDataEntity.setCostAverage(invItemMapper.getCostAverage());
                        itemDataEntity.setStoreQuantity(invItemMapper.getStoreQuantity());

                        itemDataEntityList.add(itemDataEntity);
                    }

                    return itemDataEntityList;
                }
            };
        } catch (Exception e) {
            saveError(e, "invItemDataListMB", "load");
        }

    }
    
    public void loadInventoryList() {
        try {
            if (getInvInventorySelection() != null && getInvInventorySelection().getId() != null && getInvInventorySelection().getId() > 0) {
                load();
            } else {
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "loadInventoryList");
        }

    }

    @Override
    public void filter() {
        try {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (Exception e) {
            saveError(e, "invItemDataListMB", "filter");
        }

    }

    @Override
    public String goToAdd() {
        try {
            itemDataEntity = new ItemDataEntity();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            if (getUri().contains("dis")) {
                exit("../itemsdata/itemsdataformdis.xhtml");
            } else {
                exit("../itemsdata/itemsdataform.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "invItemDataListMB", "goToAdd");
        }
        return "";

    }

    @Override
    public String goToEdit() {

        if (itemDataEntityId != null && itemDataEntityId > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("itemDataEntitySelected", itemDataEntityId);
            context.getSessionMap().put("ScreenMode", "Edit");
            if (getUri().contains("dis")) {
                exit("../itemsdata/itemsdataformdis.xhtml");
            } else {
                exit("../itemsdata/itemsdataform.xhtml");
            }
            
            return "";
        } else {
            return "";
        }

    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (itemDataEntityId != null) {
                invItem.setId(itemDataEntityId);
                showMessageGeneral = Boolean.TRUE;
                showMessageDetails = Boolean.FALSE;
                try {
                    if (invItem.getId() != null) {
                        itemDataService.deleteInvItem(invItem);
                        getItemDataEntityList().remove(itemDataEntity);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    itemDataByBranch = itemDataService.getInvItemByBranchId((userData.getSelectedBranch()));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "invItemDataListMB", "delete");
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public ItemDataEntity getItemDataEntity() {
        return itemDataEntity;
    }

    public void setItemDataEntity(ItemDataEntity itemDataEntity) {
        this.itemDataEntity = itemDataEntity;
    }

    public List<ItemDataEntity> getItemDataEntityList() {
        return itemDataEntityList;
    }

    public void setItemDataEntityList(List<ItemDataEntity> itemDataEntityList) {
        this.itemDataEntityList = itemDataEntityList;
    }

    public InvItem getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItem invItem) {
        this.invItem = invItem;
    }

    public List<InvItem> getItemDataByBranch() {
        return itemDataByBranch;
    }

    public void setItemDataByBranch(List<InvItem> itemDataByBranch) {
        this.itemDataByBranch = itemDataByBranch;
    }

    public Boolean getShowMessageGeneral() {
        return showMessageGeneral;
    }

    public void setShowMessageGeneral(Boolean showMessageGeneral) {
        this.showMessageGeneral = showMessageGeneral;
    }

    public boolean isPost_flag() {
        return post_flag;
    }

    public void setPost_flag(boolean post_flag) {
        this.post_flag = post_flag;
    }

    public Boolean getShowMessageDetails() {
        return showMessageDetails;
    }

    public void setShowMessageDetails(Boolean showMessageDetails) {
        this.showMessageDetails = showMessageDetails;
    }

    public Boolean getMarkEdit() {
        return markEdit;
    }

    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    public InvItemService getItemDataService() {
        return itemDataService;
    }

    public void setItemDataService(InvItemService itemDataService) {
        this.itemDataService = itemDataService;
    }

    public Integer getItemDataEntityId() {
        return itemDataEntityId;
    }

    public void setItemDataEntityId(Integer itemDataEntityId) {
        this.itemDataEntityId = itemDataEntityId;
    }

    /**
     * @return the itemDataViewByBranch
     */
    public List<InvItemView> getItemDataViewByBranch() {
        return itemDataViewByBranch;
    }

    /**
     * @param itemDataViewByBranch the itemDataViewByBranch to set
     */
    public void setItemDataViewByBranch(List<InvItemView> itemDataViewByBranch) {
        this.itemDataViewByBranch = itemDataViewByBranch;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<ItemDataEntity> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<ItemDataEntity> dataModel) {
        this.dataModel = dataModel;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.pricechange;

import com.isag.businessservice.InvGroupService;
import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvPriceSellItemService;
import com.isag.converter.InvGroupConverter;
import com.isag.converter.ItemConverter;
import com.isag.entity.InvGroup;
import com.isag.entity.InvItem;
import com.isag.entity.InvPriceSellItem;
import com.isag.entiy.ItemGroupEntity;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author WIN7
 */
@Named(value = "PriceChangeMB")
@ViewScoped
public class PriceChangeMB extends BaseFormBean implements Serializable {

    private Integer branchId;
    private Integer companyId;
    private Boolean showMessageGeneral = Boolean.FALSE;

    private ItemGroupEntity itemGroupEntity;

    private ItemGroupEntity itemGroupEntitySelected;
    private List<ItemGroupEntity> itemGroupEntityList;
    private List<InvItem> invItemsList;
    private InvGroup invGroupSelected;
    private List<InvGroup> invGroupList;
    private InvGroupConverter invGroupConverter;

    private ItemConverter itemConverter;

    private Integer index = 0;

    Map<Integer, InvItem> invItemMap = new HashMap<>();

    @EJB
    private InvItemService invItemService;

    @EJB
    InvGroupService invGroupService;

    @EJB
    InvPriceSellItemService invPriceSellItemService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "PriceChangeMB");
        }
    }

    @Override
    public void load() {
        try {
            itemGroupEntity = new ItemGroupEntity();
            itemGroupEntitySelected = new ItemGroupEntity();

            itemGroupEntityList = new ArrayList<>();
            invItemsList = new ArrayList<>();
            invGroupSelected = new InvGroup();
            invGroupList = new ArrayList<>();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            setBranchId(getUserData().getUserBranch().getId());

            fillLists();
            loadItems();
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "load");
        }

    }

    private void fillLists() {
        try {
            invGroupList = invGroupService.getallInvGroupByBranchId(branchId);
            if (invGroupList != null && !invGroupList.isEmpty()) {
                invGroupSelected = invGroupList.get(0);
            }
            invGroupConverter = new InvGroupConverter(invGroupList);
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "fillLists");
        }
    }

    public void loadItems() {
        try {
            invItemsList = new ArrayList<>();
            invItemsList = invItemService.getInvItemByBranchIdAndGroupId(branchId, invGroupSelected.getId());

            itemConverter = new ItemConverter(invItemsList);

            itemGroupEntityList = new ArrayList<>();
            for (InvItem invItem : invItemsList) {
//            index++;
                invItemMap.put(invItem.getId(), invItem);
                itemGroupEntity = new ItemGroupEntity();
                if (invItem.getUnitId() != null && invItem.getUnitId().getName() == null) {
                    invItem.getUnitId().setName("");
                }
                itemGroupEntity.setInvItemSelected(invItem);
                itemGroupEntity.setId(invItem.getId());
                itemGroupEntity.setBranchId(invItem.getBranchId().getId());
                itemGroupEntity.setCompanyId(invItem.getCompanyId().getId());
                itemGroupEntity.setDate(new Date());
                itemGroupEntity.setGroupId(invItem.getGroupId() != null ? invItem.getGroupId() : null);
                itemGroupEntity.setId(invItem.getId());
                itemGroupEntity.setItemCode(invItem.getCode() != null ? invItem.getCode() : null);
                itemGroupEntity.setItemName(invItem.getName() != null ? invItem.getName() : null);

                if (invItem.getUnitId() != null && invItem.getUnitId().getName() != null) {
                    itemGroupEntity.setUnitName(invItem.getUnitId().getName());
                }
                itemGroupEntity.setOldPrice(invItem.getSellPrice() != null ? invItem.getSellPrice() : null);
                itemGroupEntityList.add(itemGroupEntity);
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "loadItems");
        }
    }

    public List<InvGroup> completeInvGroup(String query) {
        try {
            List<InvGroup> listGroups = getInvGroupList();
            if (query == null || query.trim().equals("")) {

                invGroupConverter = new InvGroupConverter(listGroups);
                return listGroups;
            }
            List<InvGroup> filteredGroups = new ArrayList<>();

            String nameAr;
            Integer code;
            InvGroup groupFilter;
            for (int i = 0; i < getInvGroupList().size(); i++) {
                groupFilter = listGroups.get(i);
                nameAr = groupFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !filteredGroups.contains(groupFilter)) {
                    filteredGroups.add(groupFilter);
                }

                code = groupFilter.getId();
                if (code != null && String.valueOf(code).contains(query) && !filteredGroups.contains(groupFilter)) {
                    filteredGroups.add(groupFilter);
                }
            }

            invGroupConverter = new InvGroupConverter(filteredGroups);
            return filteredGroups;
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "completeInvGroup");
            return null;
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ItemGroupEntity itemGroupEntityUpdated = context.getApplication().evaluateExpressionGet(context, "#{invItemGroupTable}", ItemGroupEntity.class);

            itemGroupEntitySelected = itemGroupEntityUpdated;
            String column_name = event.getColumn().getClientId();

            if (itemGroupEntitySelected != null) {

                if (column_name.contains("NewPrice")) {

                    BigDecimal newValue = (BigDecimal) event.getNewValue();
                    BigDecimal oldValue = (BigDecimal) event.getOldValue();
                    itemGroupEntitySelected.setOldPrice(oldValue);
                    itemGroupEntitySelected.setNewPrice(newValue);

//                itemGroupEntitySelected.setValueLocal(newValue != null ? newValue.multiply(glBankTransactionEntity.getRate()) : null);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار سطر للتعديل"));
                setShowMessageGeneral(Boolean.TRUE);
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "onCellEdit");
        }
    }

    @Override
    public String save() {
        try {
            InvPriceSellItem invPriceSellItem = new InvPriceSellItem();

            invPriceSellItem.setBranchId(getUserData().getUserBranch());
            invPriceSellItem.setCompanyId(getUserData().getCompany());
            invPriceSellItem.setCreatedBy(getUserData().getUserBranch().getCreatedBy());
            invPriceSellItem.setCreationDate(getUserData().getUserBranch().getCreationDate());
            invPriceSellItem.setDate(new Date());
            InvItem invItem = new InvItem();
            invItem = invItemMap.get(itemGroupEntitySelected.getId());
            invPriceSellItem.setItemId(invItem);

            if (itemGroupEntitySelected.getNewPrice() != null) {
                invPriceSellItem.setValue(itemGroupEntitySelected.getNewPrice());
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ادخال سعر جديد"));
                setShowMessageGeneral(Boolean.TRUE);
                return "";
            }

            invPriceSellItemService.addInvPriceSellItem(invPriceSellItem);

            invItem.setSellPrice(itemGroupEntitySelected.getNewPrice());
            invItemService.updateInvItem(invItem);

            loadItems();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("SAVED"), getUserData().getUserDDs().get("SAVED")));
            showMessageGeneral = true;
            return "";
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "save");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public Boolean getShowMessageGeneral() {
        return showMessageGeneral;
    }

    public void setShowMessageGeneral(Boolean showMessageGeneral) {
        this.showMessageGeneral = showMessageGeneral;
    }

    public List<InvItem> getInvItemsList() {
        return invItemsList;
    }

    public void setInvItemsList(List<InvItem> invItemsList) {
        this.invItemsList = invItemsList;
    }

    public ItemGroupEntity getItemGroupEntity() {
        return itemGroupEntity;
    }

    public void setItemGroupEntity(ItemGroupEntity itemGroupEntity) {
        this.itemGroupEntity = itemGroupEntity;
    }

    public List<ItemGroupEntity> getItemGroupEntityList() {
        return itemGroupEntityList;
    }

    public void setItemGroupEntityList(List<ItemGroupEntity> itemGroupEntityList) {
        this.itemGroupEntityList = itemGroupEntityList;
    }

    public List<InvGroup> getInvGroupList() {
        return invGroupList;
    }

    public void setInvGroupList(List<InvGroup> invGroupList) {
        this.invGroupList = invGroupList;
    }

    public InvGroup getInvGroupSelected() {
        return invGroupSelected;
    }

    public void setInvGroupSelected(InvGroup invGroupSelected) {
        this.invGroupSelected = invGroupSelected;
    }

    public InvGroupConverter getInvGroupConverter() {
        return invGroupConverter;
    }

    public void setInvGroupConverter(InvGroupConverter invGroupConverter) {
        this.invGroupConverter = invGroupConverter;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public ItemGroupEntity getItemGroupEntitySelected() {
        return itemGroupEntitySelected;
    }

    public void setItemGroupEntitySelected(ItemGroupEntity itemGroupEntitySelected) {
        this.itemGroupEntitySelected = itemGroupEntitySelected;
    }

    public ItemConverter getItemConverter() {
        return itemConverter;
    }

    public void setItemConverter(ItemConverter itemConverter) {
        this.itemConverter = itemConverter;
    }
}

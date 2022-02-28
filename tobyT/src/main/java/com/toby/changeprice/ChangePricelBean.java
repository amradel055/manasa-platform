package com.toby.changeprice;

import com.toby.businessservice.ChangePriceViewService;
import com.toby.entiy.ItemDataEntity;
import com.toby.businessservice.HistoryChangeItemPriceService;
import com.toby.businessservice.InvItemService;
import com.toby.entity.HistoryChangeItemPrice;
import com.toby.entity.InvItem;
import com.toby.entiy.ChangePriceViewEntity;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import com.toby.views.ChangePriceView;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

@Named(value = "ChangePricelBean")
@ViewScoped
public class ChangePricelBean extends BaseFormBean {

    //Variables
    Integer branchId;
    Map<String, String> userDDs;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private Boolean showMessageDetail = Boolean.FALSE;

    // Entities
    private ChangePriceViewEntity changePriceViewEntity;
    private ChangePriceViewEntity changePriceViewEntitySelected;
    private ItemDataEntity itemDataEntitySelected;
    private ItemDataEntity itemDataEntity;

    Map<Integer, InvItem> itemMap;

    //Interfaces Lists
    private List<ChangePriceViewEntity> changePriceViewEntityList;
    private List<ItemDataEntity> itemDataEntityList;

    //DB Lists
    private List<ChangePriceView> changePriceViewList;
    private List<InvItem> invItemList;
    private InvItem invItemSelected;
    HistoryChangeItemPrice historyChangeItemPriceSelected;
    List<HistoryChangeItemPrice> historyChangeItemPriceList;
    List<InvItem> invItemUpdated;

    //EJBs
    @EJB
    private ChangePriceViewService changePriceViewService;
    @EJB
    private InvItemService invItemService;
    @EJB
    HistoryChangeItemPriceService historyChangeItemPriceService;

    @Override
    @PostConstruct
    public void init() {
        try {
            changePriceViewEntity = new ChangePriceViewEntity();
            changePriceViewEntitySelected = new ChangePriceViewEntity();

            changePriceViewEntityList = new ArrayList<>();
            changePriceViewList = new ArrayList<>();
            invItemList = new ArrayList<>();
            invItemUpdated = new ArrayList<>();
            historyChangeItemPriceList = new ArrayList<>();

            historyChangeItemPriceSelected = new HistoryChangeItemPrice();
            itemDataEntity = new ItemDataEntity();
            itemDataEntitySelected = new ItemDataEntity();
            userDDs = new HashMap<>();

            itemMap = new HashMap<>();

            load();
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            userDDs = getUserData().getUserDDs();
            branchId = getUserData().getUserBranch().getId();
            fillList();

            mapDBListToInterfaceList();
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "load");
        }
    }

    private void fillList() {
        try {
            changePriceViewList = changePriceViewService.getAllChangePriceViewByBranchId(branchId);
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "fillList");
        }
    }

    private void mapDBListToInterfaceList() {
        try {
            for (ChangePriceView priceView : changePriceViewList) {
                changePriceViewEntity = new ChangePriceViewEntity();

                changePriceViewEntity.setBranchId(priceView.getBranchId());
                changePriceViewEntity.setCostAverage(priceView.getCostAverage());
                changePriceViewEntity.setGroupCode(priceView.getGroupCode());
                changePriceViewEntity.setGroupId(priceView.getGroupId());
                changePriceViewEntity.setGroupName(priceView.getGroupName());
                changePriceViewEntity.setId(priceView.getId());
                changePriceViewEntity.setItemSum(priceView.getItemSum());

                changePriceViewEntityList.add(changePriceViewEntity);

            }
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "mapDBListToInterfaceList");
        }
    }

    public void onCellEdit(CellEditEvent event) {

    }

    public void onCellEditDetail(CellEditEvent event) {

    }

    public void onRowSelect(SelectEvent e) {
        try {

            changePriceViewEntity = (ChangePriceViewEntity) e.getObject();

            if (changePriceViewEntity != null) {
                invItemList = new ArrayList<>();
                itemDataEntityList = new ArrayList<>();
                invItemList = invItemService.getInvItemByBranchIdAndGroupId(branchId, changePriceViewEntity.getGroupId());

                for (InvItem item : invItemList) {
                    itemMap.put(item.getId(), item);

                    itemDataEntity = new ItemDataEntity();
                    itemDataEntity.setId(item.getId());
                    itemDataEntity.setBranchId(item.getBranchId());
                    itemDataEntity.setDateCreateCat(item.getModificationDate());
                    itemDataEntity.setCode(item.getCode());
                    itemDataEntity.setName(item.getName());
                    itemDataEntity.setUnitName(item.getUnitId() != null ? item.getUnitId().getName() : null);
                    itemDataEntity.setCostAverage(item.getCostAverage());
                    itemDataEntity.setMaxpricemen(item.getMaxpricemen());
                    itemDataEntity.setMaxpriceyoung(item.getMaxpriceyoung());
                    itemDataEntity.setMinpricemen(item.getMinpricemen());
                    itemDataEntity.setMinpriceyoung(item.getMinpriceyoung());

                    itemDataEntityList.add(itemDataEntity);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب أختيار سطر"));
                showMessageGeneral = true;
            }
        } catch (Exception ex) {
            saveError(ex, "ChangePricelBean", "onRowSelect");
        }

    }

    public void loadPrices() {
        try {
            if (changePriceViewEntitySelected != null && itemDataEntityList != null && !itemDataEntityList.isEmpty()) {
                for (ItemDataEntity entity : itemDataEntityList) {
                    entity.setLastCost(changePriceViewEntitySelected.getNewCost() != null
                            ? changePriceViewEntitySelected.getNewCost() : BigDecimal.ZERO);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب أختيار سطر"));
                showMessageGeneral = true;
            }
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "loadPrices");
        }
    }

    @Override
    public String getScreenName() {
        return "";
    }

    public void reload() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "reload");
        }
    }

    public void saveItem(ItemDataEntity itemEntity) {
        try {
            
            itemEntity = itemDataEntitySelected;
            invItemSelected = prepareItem(itemEntity);
            if (invItemSelected != null && invItemSelected.getId() != null) {
                invItemService.updateInvItem(invItemSelected);
                historyChangeItemPriceSelected = prepareHistoryChangeItemPrice(itemEntity);
                historyChangeItemPriceService.addHistoryChangeItemPriceService(historyChangeItemPriceSelected);
                itemDataEntitySelected.setMaxpricemen(itemEntity.getMaxpricemenLast());
            itemDataEntitySelected.setMaxpricemenLast(new BigDecimal(BigInteger.ZERO));

            itemDataEntitySelected.setMaxpriceyoung(itemEntity.getMaxpriceyoungLast());
            itemDataEntitySelected.setMaxpriceyoungLast(new BigDecimal(BigInteger.ZERO));

            itemDataEntitySelected.setMinpricemen(itemEntity.getMinpricemenLast());
            itemDataEntitySelected.setMinpricemenLast(new BigDecimal(BigInteger.ZERO));

            itemDataEntitySelected.setMinpriceyoung(itemEntity.getMinpriceyoungLast());
            itemDataEntitySelected.setMinpriceyoungLast(new BigDecimal(BigInteger.ZERO));
                
//                reload();
            }
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "saveItem");
        }
    }

    InvItem prepareItem(ItemDataEntity itemEntity) {
        try {
            invItemSelected = new InvItem();
            if (itemEntity.getMaxpricemenLast() != null && itemEntity.getMaxpricemenLast().compareTo(BigDecimal.ZERO) == 1
                    || itemEntity.getMaxpriceyoungLast() != null && itemEntity.getMaxpriceyoungLast().compareTo(BigDecimal.ZERO) == 1
                    || itemEntity.getMinpricemenLast() != null && itemEntity.getMinpricemenLast().compareTo(BigDecimal.ZERO) == 1
                    || itemEntity.getMinpriceyoungLast() != null && itemEntity.getMinpriceyoungLast().compareTo(BigDecimal.ZERO) == 1) {
                invItemSelected = itemMap.get(itemEntity.getId());
                invItemSelected.setId(itemEntity.getId());
                invItemSelected.setModificationDate(new Date());
                invItemSelected.setModifiedBy(getUserData().getUser());
                invItemSelected.setCreationDate(itemEntity.getCreationDate() != null ? itemEntity.getCreationDate() : new Date());
                invItemSelected.setCompanyId(itemEntity.getCompanyId() != null ? itemEntity.getCompanyId() : getUserData().getUser().getCompanyId());
                invItemSelected.setCostAverage(itemEntity.getLastCost());
                invItemSelected.setMaxpricemen(itemEntity.getMaxpricemenLast());
                invItemSelected.setMaxpriceyoung(itemEntity.getMaxpriceyoungLast());
                invItemSelected.setMinpricemen(itemEntity.getMinpricemenLast());
                invItemSelected.setMinpriceyoung(itemEntity.getMinpricemenLast());
            
            }
            return invItemSelected;
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "prepareItem");
            return null;
        }
    }

    HistoryChangeItemPrice prepareHistoryChangeItemPrice(ItemDataEntity itemEntity) {
        try {
            historyChangeItemPriceSelected = new HistoryChangeItemPrice();
            invItemSelected = new InvItem();
            invItemSelected = itemMap.get(itemEntity.getId());

            historyChangeItemPriceSelected.setBranchId(invItemSelected.getBranchId().getId());
            historyChangeItemPriceSelected.setChagePriceDate(new Date());
            historyChangeItemPriceSelected.setCompanyId(invItemSelected.getCompanyId());
            historyChangeItemPriceSelected.setCreatedBy(invItemSelected.getCreatedBy());
            historyChangeItemPriceSelected.setCreationDate(invItemSelected.getCreationDate());
            historyChangeItemPriceSelected.setItemId(invItemSelected.getId());
            historyChangeItemPriceSelected.setModificationDate(new Date());
            historyChangeItemPriceSelected.setModifiedBy(getUserData().getUser());
            historyChangeItemPriceSelected.setPrice(itemEntity.getCostAverage());

            return historyChangeItemPriceSelected;
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "prepareHistoryChangeItemPrice");
            return null;
        }
    }

    @Override
    public String save() {
        try {
            invItemUpdated = new ArrayList<>();
            InvItem invItem;
            for (ItemDataEntity entity : itemDataEntityList) {
                invItem = new InvItem();
                invItem = prepareItem(entity);
                invItemUpdated.add(invItem);

                historyChangeItemPriceSelected = new HistoryChangeItemPrice();
                historyChangeItemPriceSelected = prepareHistoryChangeItemPrice(entity);
                historyChangeItemPriceList.add(historyChangeItemPriceSelected);

            }
            if (invItemUpdated != null && !invItemUpdated.isEmpty()) {
                invItemService.updateAllInvItems(invItemUpdated);

                if (historyChangeItemPriceList != null && !historyChangeItemPriceList.isEmpty()) {
                    historyChangeItemPriceService.addListOfHistoryChangeItemPriceService(historyChangeItemPriceList);
                }
                try {
                    reload();
                } catch (Exception ex) {
                    Logger.getLogger(ChangePricelBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return "";
        } catch (Exception e) {
            saveError(e, "ChangePricelBean", "save");
            return null;
        }
    }

    public ChangePriceViewEntity getChangePriceViewEntity() {
        return changePriceViewEntity;
    }

    public void setChangePriceViewEntity(ChangePriceViewEntity changePriceViewEntity) {
        this.changePriceViewEntity = changePriceViewEntity;
    }

    public ChangePriceViewEntity getChangePriceViewEntitySelected() {
        return changePriceViewEntitySelected;
    }

    public void setChangePriceViewEntitySelected(ChangePriceViewEntity changePriceViewEntitySelected) {
        this.changePriceViewEntitySelected = changePriceViewEntitySelected;
    }

    public List<ChangePriceViewEntity> getChangePriceViewEntityList() {
        return changePriceViewEntityList;
    }

    public void setChangePriceViewEntityList(List<ChangePriceViewEntity> changePriceViewEntityList) {
        this.changePriceViewEntityList = changePriceViewEntityList;
    }

    public Boolean getShowMessageGeneral() {
        return showMessageGeneral;
    }

    public void setShowMessageGeneral(Boolean showMessageGeneral) {
        this.showMessageGeneral = showMessageGeneral;
    }

    public Boolean getShowMessageDetail() {
        return showMessageDetail;
    }

    public void setShowMessageDetail(Boolean showMessageDetail) {
        this.showMessageDetail = showMessageDetail;
    }

    public List<InvItem> getInvItemList() {
        return invItemList;
    }

    public void setInvItemList(List<InvItem> invItemList) {
        this.invItemList = invItemList;
    }

    public InvItem getInvItemSelected() {
        return invItemSelected;
    }

    public void setInvItemSelected(InvItem invItemSelected) {
        this.invItemSelected = invItemSelected;
    }

    public ItemDataEntity getItemDataEntitySelected() {
        return itemDataEntitySelected;
    }

    public void setItemDataEntitySelected(ItemDataEntity itemDataEntitySelected) {
        this.itemDataEntitySelected = itemDataEntitySelected;
    }

    public List<ItemDataEntity> getItemDataEntityList() {
        return itemDataEntityList;
    }

    public void setItemDataEntityList(List<ItemDataEntity> itemDataEntityList) {
        this.itemDataEntityList = itemDataEntityList;
    }

    public ItemDataEntity getItemDataEntity() {
        return itemDataEntity;
    }

    public void setItemDataEntity(ItemDataEntity itemDataEntity) {
        this.itemDataEntity = itemDataEntity;
    }
}

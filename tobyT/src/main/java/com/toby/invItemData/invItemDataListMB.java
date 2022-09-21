/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invItemData;

import com.toby.businessservice.InvItemService;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvItemDTO;
import com.toby.entity.InvItem;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import com.toby.views.InvItemView;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
    private InvItemDTO itemDataEntity;
    private Integer itemDataEntityId;
    private InvItemDTO invItem;
    private List<InvItemDTO> itemDataByBranch;
    private List<InvItemView> itemDataViewByBranch;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private boolean post_flag = Boolean.FALSE;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean markEdit = Boolean.FALSE;

    private LazyDataModel<InvItemDTO> dataModel;

    @EJB
    private InvItemService itemDataService;

    @Override
    @PostConstruct
    public void init() {
        try {
            userData = new UserData();
            itemDataEntity = new InvItemDTO();
            itemDataByBranch = new ArrayList<>();
            invItem = new InvItemDTO();

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
            this.dataModel = new LazyDataModel<InvItemDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<InvItemDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    setRowCount(itemDataService.getTotalRegistors(branchId, filters).intValue());
                    itemDataByBranch = itemDataService.getInvItemDTOView(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), branchId, filters, getUserData().getUser());
                    return itemDataByBranch;
                }
            };
        } catch (Exception e) {
            saveError(e, "invItemDataListMB", "load");
        }

    }

    public void printBarcodeLeRoyal() {

        try {
            if (itemDataEntityId != null) {
                InvItem item = itemDataService.findInvItem(itemDataEntityId);
                List<InvDetailDTO> listNew = new ArrayList<>();
                InvDetailDTO invDetailDTO = new InvDetailDTO();

                if (item != null && item.getProGroupId() != null && item.getProGroupId().getId() == 1 && item.getIsOfferItem() == 1) {
                    item.setMaxpricemen(new BigDecimal((item.getMaxpricemen().multiply(new BigDecimal(0.85))).setScale(5, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString()));
                }

                invDetailDTO.setCost((item.getMaxpricemen().add(item.getMaxpricemen().multiply(new BigDecimal(0.15)))).setScale(3, BigDecimal.ROUND_UP));
                invDetailDTO.setItemCode(item.getCode());
                invDetailDTO.setItemName(item.getName());
                if (item.getDiscountrate() != null && item.getDiscountrate().compareTo(BigDecimal.ZERO) == 1) {
                    BigDecimal priceAfterDiscount = item.getDiscountvalue();
                    invDetailDTO.setCostAfterDiscount((priceAfterDiscount.add(priceAfterDiscount.multiply(new BigDecimal(0.15)))).setScale(3, BigDecimal.ROUND_UP));
                }
                listNew.add(invDetailDTO);
                fillReport(prepareReportLeRoyal(), getUserData().getReportPath() + "itemBarcodeToby.jasper", listNew, "pdf");
            }
        } catch (Exception e) {

        }
    }
    
    public HashMap prepareReportLeRoyal() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("companyName", "لي رويال");
            return hashMap;
        } catch (Exception e) {
            saveError(e, "invItemDataListMB", "prepareReport");
            return null;
        }
    }

    public void printBarcode() {

        try {
            if (itemDataEntityId != null) {
                InvItem item = itemDataService.findInvItem(itemDataEntityId);
                List<InvDetailDTO> listNew = new ArrayList<>();
                InvDetailDTO invDetailDTO = new InvDetailDTO();
                invDetailDTO.setCost(new BigDecimal(((item.getMaxpricemen().add(item.getMaxpricemen().multiply(new BigDecimal(0.15)))).setScale(5, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
                invDetailDTO.setItemCode(item.getCode());
                invDetailDTO.setItemName(item.getName());
                if (item.getDiscountrate() != null && item.getDiscountrate().compareTo(BigDecimal.ZERO) == 1) {
                    BigDecimal priceAfterDiscount = item.getDiscountvalue();
                    invDetailDTO.setCostAfterDiscount((priceAfterDiscount.add(priceAfterDiscount.multiply(new BigDecimal(0.15)))).setScale(3, BigDecimal.ROUND_UP));
                }
                listNew.add(invDetailDTO);
                fillReport(prepareReport(), getUserData().getReportPath() + "itemBarcodeToby.jasper", listNew, "pdf");
            }
        } catch (Exception e) {

        }
    }

    public HashMap prepareReport() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("companyName","برنس");
            return hashMap;
        } catch (Exception e) {
            saveError(e, "invItemDataListMB", "prepareReport");
            return null;
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
            itemDataEntity = new InvItemDTO();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../itemsdata/itemsdataform.xhtml");
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
            exit("../itemsdata/itemsdataform.xhtml");
            return "";
        } else {
            return "";
        }

    }
    
    public void updateOffer(InvItemDTO invItemDTO){
     if (invItemDTO != null && invItemDTO.getId()!=null) {
     itemDataService.updateByOffer(invItemDTO.getId(),invItemDTO.getIsOfferItemFinal() );
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
                        itemDataService.deleteInvItemDTO(invItem);
                        itemDataByBranch.remove(itemDataEntity);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    itemDataByBranch = itemDataService.getInvItemDTOByBranchId((userData.getSelectedBranch()), getUserlogin());
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

    public InvItemDTO getInvItemDTO() {
        return itemDataEntity;
    }

    public void setInvItemDTO(InvItemDTO itemDataEntity) {
        this.itemDataEntity = itemDataEntity;
    }

    public InvItemDTO getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItemDTO invItem) {
        this.invItem = invItem;
    }

    public List<InvItemDTO> getItemDataByBranch() {
        return itemDataByBranch;
    }

    public void setItemDataByBranch(List<InvItemDTO> itemDataByBranch) {
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

    public Integer getInvItemDTOId() {
        return itemDataEntityId;
    }

    public void setInvItemDTOId(Integer itemDataEntityId) {
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
    public LazyDataModel<InvItemDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<InvItemDTO> dataModel) {
        this.dataModel = dataModel;
    }
    
    
}

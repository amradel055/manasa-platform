/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invopeningbalanceitem;

import com.isag.businessservice.InvOpeningBalanceItemService;
import com.isag.entity.InvOpenningBalanceItem;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq002
 */
@Named(value = "InvOpeningBalanceItemListMB")
@ViewScoped
public class InvOpeningBalanceItemListMB extends BaseListBean {

    private UserData userData;
    private List<InvOpenningBalanceItem> invOpenningBalanceItemList = new ArrayList<>();
    private InvOpenningBalanceItem invOpenningBalanceItem;
    private Integer invOpenningBalanceItemSelected;
    private Integer index = 0;

    @EJB
    private InvOpeningBalanceItemService balanceItemService;

    @Override
    @PostConstruct
    public void init() {
        try {
            invOpenningBalanceItem = new InvOpenningBalanceItem();
            load();
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            invOpenningBalanceItemList = balanceItemService.getInvOpeningBalanceItemByBranchId(getUserData().getUserBranch().getId());
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemListMB", "load");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (invOpenningBalanceItem != null) {
                try {
                    balanceItemService.deleteInvOpeningBalanceItem(invOpenningBalanceItem);
                    invOpenningBalanceItemList.remove(invOpenningBalanceItem);
                    savedMeesage(userData, userDDs.get("DELETED"));
                } catch (Exception e) {
                    errorMessage(getUserData(), userDDs.get("UNIQE_KEY_ERROR"));
                }
            } else {
                errorMessage(getUserData(), "يجب اختيار سطر");
            }
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemListMB", "delete");
        }
    }

    @Override
    public void filter() {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "addINVOPENINGBALANCE");
            exit("../invopeningbalanceitem/invopeningbalanceitemform.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (invOpenningBalanceItemSelected != null && invOpenningBalanceItemSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invOpenningBalanceItemSelected", invOpenningBalanceItemSelected);
                context.getSessionMap().put("ScreenMode", "editINVOPENINGBALANCE");
                exit("../invopeningbalanceitem/invopeningbalanceitemform.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvOpeningBalanceItemListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the invOpenningBalanceItemSelected
     */
    public Integer getInvOpenningBalanceItemSelected() {
        return invOpenningBalanceItemSelected;
    }

    /**
     * @param invOpenningBalanceItemSelected the invOpenningBalanceItemSelected
     * to set
     */
    public void setInvOpenningBalanceItemSelected(Integer invOpenningBalanceItemSelected) {
        this.invOpenningBalanceItemSelected = invOpenningBalanceItemSelected;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<InvOpenningBalanceItem> getInvOpenningBalanceItemList() {
        return invOpenningBalanceItemList;
    }

    public void setInvOpenningBalanceItemList(List<InvOpenningBalanceItem> invOpenningBalanceItemList) {
        this.invOpenningBalanceItemList = invOpenningBalanceItemList;
    }

    public InvOpenningBalanceItem getInvOpenningBalanceItem() {
        return invOpenningBalanceItem;
    }

    public void setInvOpenningBalanceItem(InvOpenningBalanceItem invOpenningBalanceItem) {
        this.invOpenningBalanceItem = invOpenningBalanceItem;
    }
}

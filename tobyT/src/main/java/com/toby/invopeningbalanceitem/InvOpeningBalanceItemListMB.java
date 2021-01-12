/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invopeningbalanceitem;

import com.toby.businessservice.InvOpeningBalanceItemService;
import com.toby.dto.InvOpenningBalanceItemDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
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
    private List<InvOpenningBalanceItemDTO> invOpenningBalanceItemList = new ArrayList<>();
    private InvOpenningBalanceItemDTO invOpenningBalanceItem;
    private Integer invOpenningBalanceItemSelected;
   

    @EJB
    private InvOpeningBalanceItemService balanceItemService;

    @Override
    @PostConstruct
    public void init() {
        try {
            invOpenningBalanceItem = new InvOpenningBalanceItemDTO();
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
            getInvOpenningBalanceItemList();
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
                    invOpenningBalanceItemList.remove(invOpenningBalanceItem);
                    balanceItemService.deleteInvOpeningBalanceItemDTO(invOpenningBalanceItem,getUserData().getUser());
                    
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
            context.getSessionMap().put("ScreenMode", "Add");
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
                context.getSessionMap().put("ScreenMode", "Edit");
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

    public UserData getUserData() {
        return userData;
    }

   
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Integer getInvOpenningBalanceItemSelected() {
        return invOpenningBalanceItemSelected;
    }

   
    public void setInvOpenningBalanceItemSelected(Integer invOpenningBalanceItemSelected) {
        this.invOpenningBalanceItemSelected = invOpenningBalanceItemSelected;
    }


    public List<InvOpenningBalanceItemDTO> getInvOpenningBalanceItemList() {
        if(invOpenningBalanceItemList == null || invOpenningBalanceItemList.isEmpty()){
          invOpenningBalanceItemList=  balanceItemService.getInvOpeningBalanceItemByBranchIdDTO(getUserData().getUserBranch().getId(),getUserData().getUser());
        
        }
        return invOpenningBalanceItemList;
    }

    public void setInvOpenningBalanceItemList(List<InvOpenningBalanceItemDTO> invOpenningBalanceItemList) {
        this.invOpenningBalanceItemList = invOpenningBalanceItemList;
    }

    public InvOpenningBalanceItemDTO getInvOpenningBalanceItem() {
        return invOpenningBalanceItem;
    }

    public void setInvOpenningBalanceItem(InvOpenningBalanceItemDTO invOpenningBalanceItem) {
        this.invOpenningBalanceItem = invOpenningBalanceItem;
    }
}

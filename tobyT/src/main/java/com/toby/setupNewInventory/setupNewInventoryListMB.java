/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.setupNewInventory;

import com.toby.businessservice.InvInventoryService;
import com.toby.entity.InvInventory;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq002
 */
@Named(value = "setupNewInventoryListMB")
@ViewScoped
public class setupNewInventoryListMB extends BaseListBean implements Serializable {

    private List<InvInventory> invInventories = new ArrayList<>();
    private InvInventory invInventory;
    private UserData userData;
    private Integer invInventorySelected;

    @EJB
    private InvInventoryService inventoryService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "setupNewInventoryListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            invInventories = inventoryService.getALLInventoriesByBranch(userData.getUserBranch().getId());
        } catch (Exception e) {
            saveError(e, "setupNewInventoryListMB", "load");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (invInventory != null) {
                try {
                    inventoryService.deleteNewInventory(invInventory);
                    invInventories.remove(invInventory);
//                load();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "setupNewInventoryListMB", "delete");
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
            exit("../invcreate/setupNewInventoryForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "setupNewInventoryListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (invInventorySelected != null && invInventorySelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invInventorySelected", invInventorySelected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../invcreate/setupNewInventoryForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "setupNewInventoryListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invInventories
     */
    public List<InvInventory> getInvInventories() {
        return invInventories;
    }

    /**
     * @param invInventories the invInventories to set
     */
    public void setInvInventories(List<InvInventory> invInventories) {
        this.invInventories = invInventories;
    }

    /**
     * @return the invInventory
     */
    public InvInventory getInvInventory() {
        return invInventory;
    }

    /**
     * @param invInventory the invInventory to set
     */
    public void setInvInventory(InvInventory invInventory) {
        this.invInventory = invInventory;
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
     * @return the invInventorySelected
     */
    public Integer getInvInventorySelected() {
        return invInventorySelected;
    }

    /**
     * @param invInventorySelected the invInventorySelected to set
     */
    public void setInvInventorySelected(Integer invInventorySelected) {
        this.invInventorySelected = invInventorySelected;
    }

    /**
     * @return the inventoryService
     */
    public InvInventoryService getInventoryService() {
        return inventoryService;
    }
}

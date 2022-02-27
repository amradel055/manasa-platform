/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.inventoryDelegator;

import com.toby.businessservice.InvDelegatorService;
import com.toby.entity.InventoryDelegator;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
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
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author hq002
 */
@Named(value = "inventoryDelegatorListBean")
@ViewScoped
public class inventoryDelegatorListBean extends BaseListBean {

    private List<InventoryDelegator> inventoryDelegators = new ArrayList<>();
    private InventoryDelegator inventoryDelegator;
    private UserData userData;
    private Integer inventoryDelegatorSelected;

    private HttpServletRequest request;
    private String url;

    @EJB
    InvDelegatorService ineventoryDelegatorService;

    @Override
    @PostConstruct
    public void init() {
        try {
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            url = request.getRequestURL().toString();
            load();
        } catch (Exception e) {
            saveError(e, "inventoryDelegatorListBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");

            if (url.contains("salesperson")) {
                inventoryDelegators = ineventoryDelegatorService.getSalesByBranch(userData.getUserBranch().getId());
            } else {
                inventoryDelegators = ineventoryDelegatorService.getPurchaseByBranch(userData.getUserBranch().getId());
            }
        } catch (Exception e) {
            saveError(e, "inventoryDelegatorListBean", "load");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (inventoryDelegator != null) {
                try {
                    ineventoryDelegatorService.deleteInventoryDelegator(inventoryDelegator);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "inventoryDelegatorListBean", "delete");
        }
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "Add");
        if (url.contains("salesperson")) {
            exit("../salesperson/salespersonForm.xhtml");
        } else {
            exit("../purchasesrepresentative/purchasesrepresentativeForm.xhtml");
        }
        return "";
    }

    @Override
    public String goToEdit() {
        try {
            if (inventoryDelegatorSelected != null && inventoryDelegatorSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("inventoryDelegatorSelected", inventoryDelegatorSelected);
                context.getSessionMap().put("ScreenMode", "Edit");

                if (url.contains("salesperson")) {
                    exit("../salesperson/salespersonForm.xhtml");
                } else {
                    exit("../purchasesrepresentative/purchasesrepresentativeForm.xhtml");
                }
            }
            return "";
        } catch (Exception e) {
            saveError(e, "inventoryDelegatorListBean", "goToEdit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<InventoryDelegator> getInventoryDelegators() {
        return inventoryDelegators;
    }

    public void setInventoryDelegators(List<InventoryDelegator> inventoryDelegators) {
        this.inventoryDelegators = inventoryDelegators;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the inventoryDelegatorSelected
     */
    public Integer getInventoryDelegatorSelected() {
        return inventoryDelegatorSelected;
    }

    /**
     * @param inventoryDelegatorSelected the inventoryDelegatorSelected to set
     */
    public void setInventoryDelegatorSelected(Integer inventoryDelegatorSelected) {
        this.inventoryDelegatorSelected = inventoryDelegatorSelected;
    }

    /**
     * @return the inventoryDelegator
     */
    public InventoryDelegator getInventoryDelegator() {
        return inventoryDelegator;
    }

    /**
     * @param inventoryDelegator the inventoryDelegator to set
     */
    public void setInventoryDelegator(InventoryDelegator inventoryDelegator) {
        this.inventoryDelegator = inventoryDelegator;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.inventorysetup;

import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.InventorySetupService;
import com.isag.entity.InventorySetup;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.util.Date;
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
@ViewScoped
@Named(value = "inventorySetupBean")
public class InventorySetupBean extends BaseFormBean {

    private InventorySetup inventorySetup;

    @EJB
    InventorySetupService inventorySetupService;
    @EJB
    InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;

    @Override
    public String save() {
        try {

            inventorySetup.setCompanyId(getUserData().getCompany());
            inventorySetup.setBranchId(getUserData().getUserBranch());

            if (inventorySetup.getId() == null) {
                inventorySetup.setCreationDate(new Date());
                inventorySetup.setCreatedBy(getUserData().getUser());
            } else {
                inventorySetup.setModificationDate(new Date());
                inventorySetup.setModifiedBy(getUserData().getUser());
            }
            inventorySetupService.addInventorySetup(inventorySetup);

            savedMeesage(getUserData().getUserDDs().get("SAVED"));

            return "";
        } catch (Exception e) {
            saveError(e, "InventorySetupBean", "save");
            return null;
        }
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            load();
        } catch (Exception e) {
            saveError(e, "InventorySetupBean", "init");
        }
    }

    public void cancel() {
        try {
            inventorySetup = new InventorySetup();
        } catch (Exception e) {
            saveError(e, "InventorySetupBean", "cancel");
        }
    }

    public void reCalcCostAvarage() {
        try {
            invPurchaseInvoiceDetailsService.reCalcCostAvarage(getUserData().getUserBranch().getId());
        } catch (Exception e) {
            saveError(e, "InventorySetupBean", "reCalcCostAvarage");
        }
    }

    @Override

    public void load() {
        try {
            inventorySetup = inventorySetupService.getInventoryByBranchId(getUserData().getUserBranch().getId());
            if (inventorySetup == null) {
                inventorySetup = new InventorySetup();
            }
        } catch (Exception e) {
            saveError(e, "InventorySetupBean", "load");
        }
    }

    @Override
    public String getScreenName() {
        return "inventorysetupscreen";
    }

    public InventorySetup getInventorySetup() {
        return inventorySetup;
    }

    public void setInventorySetup(InventorySetup inventorySetup) {
        this.inventorySetup = inventorySetup;
    }

}

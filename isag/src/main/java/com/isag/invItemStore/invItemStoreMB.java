/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invItemStore;

import com.isag.businessservice.InventorySetupService;
import com.isag.define.HttpMethode;
import com.isag.dto.InventorySetupDTO;
import com.isag.isag.BaseFormBean;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ahmedEssam <ahmedEssam@isag.com>
 */
@Named(value = "itemStoreMB")
@ViewScoped
public class invItemStoreMB extends BaseFormBean implements Serializable {

    private InventorySetupDTO inventorySetupDTO;

    @EJB
    private InventorySetupService inventorySetupService;

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @PostConstruct
    @Override
    public void init() {
    }

    @Override
    public void load() {
        StringBuilder builder = new StringBuilder();
        if (getInventorySetupDTO() != null) {
            if (getInventorySetupDTO().getShopLink() == null || getInventorySetupDTO().getShopLink().isEmpty()) {
                builder.append("يجب ادخال رابط المتجر" + "\n");
            }
            if (getInventorySetupDTO().getApiShopUserName() == null || getInventorySetupDTO().getApiShopUserName().isEmpty()) {
                builder.append("يجب ادخال اسم المستخدم" + "\n");
            }
            if (getInventorySetupDTO().getApiShopPassword() == null || getInventorySetupDTO().getApiShopPassword().isEmpty()) {
                builder.append("يجب ادخال كلمه المرور" + "\n");
            }

        }
        if (builder.toString() == null || builder.toString().isEmpty()) {
         String jsonString = "{\"page\":4}";
 
            String link = getInventorySetupDTO().getShopLink()+"/wp-json/wc/v3/products/";
            String json = callApiPostBasic(link, getInventorySetupDTO().getApiShopUserName(), getInventorySetupDTO().getApiShopPassword(), jsonString,HttpMethode.GET);
            System.out.println(json);
        } else {
            errorMessage(builder.toString());
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the inventorySetupDTO
     */
    public InventorySetupDTO getInventorySetupDTO() {
        if (inventorySetupDTO == null) {
            inventorySetupDTO = inventorySetupService.getInventoryByBranchIdDTO(getUserData().getUserBranch().getId());
        }
        return inventorySetupDTO;
    }

    /**
     * @param inventorySetupDTO the inventorySetupDTO to set
     */
    public void setInventorySetupDTO(InventorySetupDTO inventorySetupDTO) {
        this.inventorySetupDTO = inventorySetupDTO;
    }

}

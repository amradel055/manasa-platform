/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.itemdataview;

import com.isag.businessservice.ItemsDataViewService;
import com.isag.businessservice.ItemsDataViewServiceImpl;
import com.isag.isag.CrmBasicDataForm;
import com.isag.views.ItemsDataView;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author khale
 */

@Named(value = "itemDataViewMB")
@ViewScoped

public class itemDataViewMB extends CrmBasicDataForm {
    
    @EJB 
    private ItemsDataViewService itemsDataViewService;
    
    private ItemsDataViewServiceImpl itemDataView;
    
    private List<ItemsDataView> itemsList;
    
    
    
     

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the itemsList
     */
    public List<ItemsDataView> getItemsList() {
        
       itemsList = itemsDataViewService.findItemsByBranchId(getUserlogin().getBranchId().getId());
        return itemsList;
    }

    /**
     * @param itemsList the itemsList to set
     */
    public void setItemsList(List<ItemsDataView> itemsList) {
        this.itemsList = itemsList;
    }
    

    
}

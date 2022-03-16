/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.define.ScreenNameClassEnum;
import com.isag.views.ItemsBarcodesDetailsView;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author anady
 */
@Remote
public interface ItemsBarcodesDetailsViewService {

    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchId(Integer branchId);
    
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdAll(Integer branchId);
    
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchId(Integer branchId ,Integer groupId);
    
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdByBarcode(Integer branchId ,List<String> barcodeList);
        
    public Map<String, ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdMap(Integer branchId);
    
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdAndStore(Integer branchId, String barcodes);
    
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewByInvoiceId(Integer headId, ScreenNameClassEnum screenNameClassEnum);
}

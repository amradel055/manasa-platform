/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.views.ItemsBarcodesDetailsView;
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
    
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdByBarcode(Integer branchId , StringBuilder barcode,List<String> barcodeList);
    
    public Map<String, ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdMap(Integer branchId);
    
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdAndStore(Integer branchId, String barcodes);
    
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdByBarcode(Integer branchId ,List<String> barcodeList);
        
}

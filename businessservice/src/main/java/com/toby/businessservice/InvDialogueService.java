/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDailogueData;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InventorySetupDTO;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

@Remote
public interface InvDialogueService {

    // public List<InvDailogueData> loadInvDailogueData(InventorySetupDTO inventorySetupDTO, ScreenNameClassEnum screenNameClassEnum);
    public List<InvDailogueData> loadInvDailogueData(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters, InventorySetupDTO inventorySetupDTO,
            Integer branchId, ScreenNameClassEnum screenNameClassEnum,Integer inventoryId);

    public InvInvoiceDTO loadDlg(InvInvoiceDTO dTO);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters, Boolean sellBuy, ScreenNameClassEnum screenNameClassEnum,Integer inventoryId);
    
    public List<InvDailogueData> loadInvDailogueDataForInvStrip(Integer branchId, Integer inventoryId);
}

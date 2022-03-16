/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDailogueData;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InventorySetupDTO;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

@Remote
public interface InvDialogueService {

    // public List<InvDailogueData> loadInvDailogueData(InventorySetupDTO inventorySetupDTO, ScreenNameClassEnum screenNameClassEnum);
    public List<InvDailogueData> loadInvDailogueData(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters, InventorySetupDTO inventorySetupDTO, ScreenNameClassEnum screenNameClassEnum,Integer inventoryId);

    public InvInvoiceDTO loadDlg(InvInvoiceDTO dTO);

    public Long getTotalRegistors(Integer branchId,Integer inventoryId, Map<String, Object> filters, Boolean sellBuy, ScreenNameClassEnum screenNameClassEnum);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvOpenningBalanceItem;
import com.isag.entity.InvOpenningBalanceItemDetail;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvOpeningBalanceItemService {

    public InvPurchaseReturnSave addInvOpeningBalanceItem(InvOpenningBalanceItem invOpenningBalanceItem, List<InvOpenningBalanceItemDetail> invOpenningBalanceItemDetailList, List<InvOpenningBalanceItemDetail> balanceItemDetailsDeleted);

    public void deleteInvOpeningBalanceItem(InvOpenningBalanceItem balanceItem);

    public List<InvOpenningBalanceItem> getALLInvOpeningBalanceItem();

    public List<InvOpenningBalanceItem> getALLInvOpeningBalanceItemByCompanyId(Integer Id);

    public List<InvOpenningBalanceItem> getInvOpeningBalanceItemByBranchId(Integer Id);

    public List<InvOpenningBalanceItemDetail> getALLInvOpeningBalanceItemDetailsByInvOpeningBalanceItem(Integer openingBalance);

    public InvOpenningBalanceItem findInvOpeningBalanceItemByInvOpeningBalanceItemId(Integer openingBalanceId);
    
    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) ;
    
    public InvInvoiceDTO findOpeningBalanceById(InvInvoiceDTO invInvoiceDTO);
}

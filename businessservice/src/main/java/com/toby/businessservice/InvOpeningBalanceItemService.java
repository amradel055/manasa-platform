/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOpenningBalanceItemDTO;
import com.toby.dto.InvOpenningBalanceItemDetailDTO;
import com.toby.entity.InvOpenningBalanceItem;
import com.toby.entity.InvOpenningBalanceItemDetail;
import com.toby.entity.TobyUser;
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

    public List<InvOpenningBalanceItemDTO> getInvOpeningBalanceItemByBranchIdDTO(Integer Id, TobyUser tobyUser);

    public InvOpenningBalanceItemDTO deleteInvOpeningBalanceItemDTO(InvOpenningBalanceItemDTO balanceItem, TobyUser tobyUser);

    public InvOpenningBalanceItemDTO updateSummition(InvOpenningBalanceItemDTO invOpenningBalanceItemDTO);

    public boolean validateDetails(InvOpenningBalanceItemDTO invOpenningBalanceItemDTO);

    public InvOpenningBalanceItemDTO validateQuantityColum(InvOpenningBalanceItemDetailDTO invOpenningBalanceItemDetailDTOSelected, InvOpenningBalanceItemDTO invOpenningBalanceItemDTO);

    public InvOpenningBalanceItemDTO validatePriceColumn(InvOpenningBalanceItemDetailDTO invOpenningBalanceItemDetailDTOSelected, InvOpenningBalanceItemDTO invOpenningBalanceItemDTO);

   public InvOpenningBalanceItemDTO saveInvOpenningBalanceItemDTO(InvOpenningBalanceItemDTO invOpenningBalanceItemDTO ,TobyUser tobyUser);

   public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) ;
    
    public InvInvoiceDTO findOpeningBalanceById(InvInvoiceDTO invInvoiceDTO);
}

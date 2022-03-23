/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvOpenningBalanceItemDetail;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvOpeningBalanceItemDetailService {

    public void addInvOpenningBalanceItemDetail(InvOpenningBalanceItemDetail details);

    public void deleteInvOpenningBalanceItemDetail(InvOpenningBalanceItemDetail details);

    public InvOpenningBalanceItemDetail updateInvOpenningBalanceItemDetail(InvOpenningBalanceItemDetail details);

    public List<InvOpenningBalanceItemDetail> getAllInvOpenningBalanceItemDetails();

    public List<InvOpenningBalanceItemDetail> getAllInvOpenningBalanceItemDetails(Integer id);
    
     public Boolean getInvOpenningBalanceItemDetailsByItemId(Integer itemId);

    public InvOpenningBalanceItemDetail findInvOpenningBalanceItemDetailsById(Integer id);
    
    public List<InvDetailDTO> getAllInvOpenningBalanceItemDetailByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invInvoiceDTO, Integer branchId) ;
    
    public void deleteInvOpenningBalanceItemDetailListDTO(List<InvDetailDTO> invDetailDTOList);
    
    public String addInvOpenningBalanceItemDetailDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO,ScreenNameClassEnum screenNameClassEnum,  IsagUser isagUser);

    public List<Object[]> getInvOpeningBalanceItemDetailsByBranchId(Integer branchId, List<Integer> itemIdList);
    
}

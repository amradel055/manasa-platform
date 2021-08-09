/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entity.IsagUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvAddingOrderService {

    public InvPurchaseReturnSave addInvAddingOrder(InvAddingorder invAddingOrder,
            List<InvAddingorderDetail> invAddingOrderDetailList,
            List<InvAddingorderDetail> invAddingOrderDetailListDeleted,
            List<Integer> updatedPurchaseOrAddingOrderList,
            StringBuilder headIdList, Boolean isPurchaseOrSales);
    public void deleteInvAddingOrder(InvAddingorder invAddingOrder);

    public List<InvAddingorder> getALLInvAddingOrderByCompanyId(Integer companyId);

    public List<InvAddingorder> getALLInvAddingOrderByBranchId(Integer branchId, Integer type);

    public InvAddingorder findInvAddingOrderByInvAddingOrderId(Integer invAddingOrderId);

    public InvAddingorder updateInvAddingOrder(InvAddingorder invAddingOrder);

    public List<InvAddingorder> getALLInvSalesOrderByBranchIdByStatus(Integer branchId);

    public List<InvAddingorder> getALLInvAddingOrderByBranchIdByStatus(Integer branchId);
    
    public InvInvoiceDTO convertAddingOrderToPurchaseInvoice(InvInvoiceDTO invPurchaseInvoiceDTO);
    
    public InvInvoiceDTO findInvAddingOrderById(InvInvoiceDTO invAddingInvoice, IsagUser isagUser);
    
    public InvInvoiceDTO save(InvInvoiceDTO invPurchaseInvoiceDTO, IsagUser isagUser);
    
     public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters);
     
     public List<InvInvoiceDTO> getALLInvAddingorderDTOByBranchId(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);
}

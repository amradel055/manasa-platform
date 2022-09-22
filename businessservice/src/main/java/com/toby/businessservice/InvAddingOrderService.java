/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.API.InvInvoiceDTOApi;
import com.toby.entity.TobyUser;
import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.InvAddingorder;
import com.toby.entity.InvAddingorderDetail;
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
    
    public InvInvoiceDTO findInvAddingOrderById(InvInvoiceDTO invAddingInvoice, TobyUser tobyUser);
    
    public InvInvoiceDTO save(InvInvoiceDTO invPurchaseInvoiceDTO, TobyUser tobyUser);
    
     public Long getTotalRegistors(Integer type, Integer branchId, Integer gallaryId, Map<String, Object> filters);
     
     public List<InvInvoiceDTO> getALLInvAddingorderDTOByBranchId(Integer type, Integer gallaryId, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);
     
     public InvInvoiceDTOApi save(InvInvoiceDTOApi invInvoiceDTO, TobyUser tobyUser);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;


import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entity.IsagUser;
import com.isag.views.PurchaseOrderNotLoadedFromInvAddingOrder;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvPurchaseOrderService {

    public InvPurchaseReturnSave addPurchaseOrder(InvPurchaseOrder invPurchaseOrder, List<InvPurchaseOrderDetail> invPurchaseOrderDetailList, List<InvPurchaseOrderDetail> invPurchaseOrderDeleted);

    public void deletePurchaseOrder(Integer invPurchaseOrderId);

    public List<InvPurchaseOrder> getALLPurchaseOrder();

    public List<InvPurchaseOrder> getALLPurchaseOrderByCompanyId(Integer Id);

    public List<InvPurchaseOrder> getALLPurchaseOrderByBranchId(Integer Id);

    public List<InvPurchaseOrderDetail> getALLPurchaseOrderDetailsByPurchaseOrder(Integer purchaseOrderId);

    public InvPurchaseOrder findPurchaseOrderByPurchaseOrderId(Integer purchaseOrderId);
    
     public List<PurchaseOrderNotLoadedFromInvAddingOrder> getAllPurchaseOrderNotLoaded(Integer branchId);

    public InvPurchaseOrder updateInvPurchaseOrder(InvPurchaseOrder invPurchaseOrder);

    public List<InvPurchaseOrder> getALLPurchaseOrderByBranchIdByStatus(Integer branchId);
    
    public InvInvoiceDTO convertPurchaseOrderToPurchaseInvoice(InvInvoiceDTO invPurchaseInvoiceDTO);
    
    public InvInvoiceDTO findInvPurchaseOrderById(InvInvoiceDTO invPurchaseInvoiceId, IsagUser isagUser);
    
    public InvInvoiceDTO save(InvInvoiceDTO invPurchaseInvoiceDTO, IsagUser isagUser);
    
    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters);
    
    public List<InvInvoiceDTO> getALLPurchaseOrderByBranchIdDTO(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);
    
    

}

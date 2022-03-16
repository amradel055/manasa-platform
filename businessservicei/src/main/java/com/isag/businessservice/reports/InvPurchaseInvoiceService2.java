/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.InvPurchaseInvoiceSearchBean;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvPurchaseInvoice;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvPurchaseInvoiceService2 {

   

   

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoiceByBranchId(Integer branchId);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchId(Integer branchId, Integer type);

    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchId(Integer branchId);
    
    
    public List<InvInvoiceDTO> getPurchaseInvoice(Integer branchId , InvPurchaseInvoiceSearchBean invPurchaseInvoiceSearchBean);

   
     
     
}

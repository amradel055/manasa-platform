/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvSalesInvoiceService {

    public InvPurchaseInvoice addInvSalesInvoice(InvPurchaseInvoice invSalesInvoice, List<InvPurchaseInvoiceDetail> invSalesInvoiceDetailsList, List<InvPurchaseInvoiceDetail> invSalesInvoiceDetailListDeleted);

    public void deleteInvSalesInvoice(InvPurchaseInvoice invSalesInvoice);

    public List<InvPurchaseInvoice> getALLInvSalesInvoiceByCompanyId(Integer companyId);

    public List<InvPurchaseInvoice> getALLInvSalesInvoiceByBranchId(Integer branchId);

    public InvPurchaseInvoice findInvSalesInvoiceById(Integer invSalesInvoiceId);

}

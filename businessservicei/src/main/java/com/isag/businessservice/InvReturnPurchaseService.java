/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.entity.InvReturnPurchase;
import com.isag.entity.InvReturnPurchaseDetail;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvReturnPurchaseService {

    public InvPurchaseReturnSave addInvReturnPurchase(InvReturnPurchase invReturnPurchase,
            List<InvReturnPurchaseDetail> invReturnPurchaseDetailUpdatedList, List<InvReturnPurchaseDetail> invReturnPurchaseDetailDeletedList);

    public void deleteInvReturnPurchase(InvReturnPurchase invPurchaseInvoice);

    public List<InvReturnPurchase> getALLInvReturnPurchaseByCompanyId(Integer companyId);

    public List<InvReturnPurchase> getALLInvReturnPurchaseByBranchId(Integer branchId, Boolean type);
    
    public List<InvReturnPurchase> getALLInvReturnPurchaseByBranchIdPer(Integer branchId, Boolean type);

    public InvReturnPurchase findInvReturnPurchaseById(Integer invReturnPurchaseId);
    
    public InvReturnPurchase updateReturnPurchaseInvoice(InvReturnPurchase invReturnPurchase);

    public Integer getMaxSerialForInvoiceDetail(InvReturnPurchase invReturnPurchase);
}

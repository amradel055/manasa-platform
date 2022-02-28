/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.views.NetView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author elsakr6
 */
@Remote
public interface InvoiceNetService {

    public List<NetView> getInvPurchaseInvoiceFromViewByBranchId(Integer branchId, Boolean type);
    
    public NetView getInvPurchaseInvoiceFromViewByInvoiceId(Integer branchId, Boolean type,Integer invoiceId);
    
    public List<NetView>getInvoiceFromViewByBranchIdandInventory(Integer branchId, Boolean type,Integer invId);
}

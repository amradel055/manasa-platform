/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.views.ProductionTransactionStagesInvoice;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Toby
 */
@Remote
public interface ProductionTransactionStagesInvoiceService {
    public List<ProductionTransactionStagesInvoice> getProductionTransactionStagesInvoiceList (Integer proProductionStagesId,TobyUser tobyUser);
    
}

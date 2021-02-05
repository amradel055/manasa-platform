/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.views.ProProductionTransactionOfInvoice;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface ProProductionTransactionOfInvoiceService {

    public List<ProProductionTransactionOfInvoice> getProProductionTransactionOfInvoiceListOfPourchais(Integer invPurchaseInvoiceDTOId, TobyUser tobyUser);

    public ProProductionTransactionOfInvoice getRenaimNumberListOfproduction(Integer invPurchaseInvoiceId, Integer clothNumber, Integer itemId, Integer proProductionStage, TobyUser tobyUser);

    public ProProductionTransactionOfInvoice getRenaimNumberListOfproduction(Integer invPurchaseInvoiceId,  Integer itemId, Integer proProductionStage, TobyUser tobyUser) ;
}

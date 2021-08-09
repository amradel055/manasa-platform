/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.views.InstPurchaseInvoiceContractDifrranceView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface InstPurchaseInvoiceContractDifrranceViewService {
    
     public List<InstPurchaseInvoiceContractDifrranceView> getAllInstPurchaseInvoiceContractDifrrance(Integer branchId);
    
     public InstPurchaseInvoiceContractDifrranceView getAllInstPurchaseInvoiceContractDifrranceByType(Integer branchId,Integer id , Integer type);
}

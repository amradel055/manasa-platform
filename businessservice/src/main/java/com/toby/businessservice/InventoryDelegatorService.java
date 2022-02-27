/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface InventoryDelegatorService {
    
    public List<InventoryDelegatorDTO> findInvPurchaseInvoiceforDelegator(TobyUser tobyUser);
    

}

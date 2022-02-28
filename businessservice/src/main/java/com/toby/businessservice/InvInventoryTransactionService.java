/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvInventoryTransactionDTO;
import com.toby.dto.InvInventoryTransactionDetailDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.entity.InvInventoryTransaction;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author user4
 */
@Remote
public interface InvInventoryTransactionService {

    public InvInventoryTransactionDTO saveInvInventorryTransaction(InvInventoryTransactionDTO invInventoryTransactionDTO, TobyUser tobyUser);

    public void saveInvInventorryTransactionList(List<InvInventoryTransactionDTO> invInventoryTransactionDTOList, TobyUser tobyUser);

    public InvInventoryTransactionDTO getInvInventoryTransactionDetailsByPurchaseId(InvInventoryTransactionDTO invInventoryTransactionDTO, InvPurchaseInvoiceDTO1 invPurchaseInvoiceId, TobyUser tobyUser);

    public InvInventoryTransactionDTO getInvInventoryTransactionDetailsByPurchaseIdFromView(InvInventoryTransactionDTO invInventoryTransactionDTO, InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTOId, Integer InventoryId, TobyUser tobyUser);

    public List<InvInventoryTransactionDTO> getInventoryTransactionList(TobyUser tobyUser);

    public InvInventoryTransactionDTO getInvInventoryTransactionDTO(Integer invInventoryTransactionDTOId, TobyUser tobyUser);

    public InvInventoryTransactionDTO deleteInvInventoryTransaction(InvInventoryTransactionDTO invInventoryTransactionDTO, TobyUser tobyUser);
     public List<InvInventoryTransactionDTO> getInventoryTransactionListTocash(TobyUser tobyUser);

 //     public void deleteInvInventoryTransaction(InvInventoryTransaction invInventoryTransaction);
}

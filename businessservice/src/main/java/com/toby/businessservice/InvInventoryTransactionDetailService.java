/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvInventoryTransactionDetailDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author user4
 */
@Remote
public interface InvInventoryTransactionDetailService {
    public List<InvInventoryTransactionDetailDTO>  savainvInventoryTransactionDetailDTO(List<InvInventoryTransactionDetailDTO> invInventoryTransactionDetailDTO,Integer InvInventoryTransactionDTOId,TobyUser tobyUser);
    public List<InvInventoryTransactionDetailDTO>  selectDetailsInvInvetoryByIdDTO(Integer inventoryTransactionId ,TobyUser tobyUser);
     public List<InvInventoryTransactionDetailDTO> deleteInvInventoryTransactionDetailList(Integer inventoryTransactionId, TobyUser tobyUser);
        public List<InvInventoryTransactionDetailDTO>  invInventoryTransactionDetails(TobyUser tobyUser);
}

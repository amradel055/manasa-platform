/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvOpenningBalanceItemDetailDTO;
import com.toby.entity.InvOpenningBalanceItemDetail;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvOpeningBalanceItemDetailService {

    public void addInvOpenningBalanceItemDetail(InvOpenningBalanceItemDetail details);

    public void deleteInvOpenningBalanceItemDetail(InvOpenningBalanceItemDetail details);

    public InvOpenningBalanceItemDetail updateInvOpenningBalanceItemDetail(InvOpenningBalanceItemDetail details);

    public List<InvOpenningBalanceItemDetail> getAllInvOpenningBalanceItemDetails();

    public List<InvOpenningBalanceItemDetail> getAllInvOpenningBalanceItemDetails(Integer id);

    public InvOpenningBalanceItemDetail findInvOpenningBalanceItemDetailsById(Integer id);

    public List<InvOpenningBalanceItemDetailDTO> deleteInvInventoryTransactionDetailList(Integer inventoryTransactionId, TobyUser tobyUser);

    public List<InvOpenningBalanceItemDetailDTO> saveInvOpenningBalanceItemDetailDTO(List<InvOpenningBalanceItemDetailDTO> invOpenningBalanceItemDetailDTOList, Integer invOpenningBalanceItemDetailDTOId, TobyUser tobyUser);

    public List<Object[]> getInvOpeningBalanceItemDetailsByBranchId(Integer branchId, List<Integer> itemIdList);

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.ItemOfferDTO;
import com.toby.dto.ItemOfferDetailDTO;
import java.util.List;
import javax.ejb.Remote;
/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface ItemOfferDetailService {

    public InvDetailDTO findItemOfferDetailByItemId(Integer itemId,Integer branchId);

    public void deleteItemOfferDetail(ItemOfferDetailDTO itemOfferDTO);
    
    public void deleteItemOfferDetailList(List<InvDetailDTO> itemOfferDetailDTOList);
    
    public void deleteItemOfferDetail(Integer itemOfferDTOId);

    public void addItemOfferDetail(InvDetailDTO itemOfferDTO,Integer itemOfferId, TobyUser tobyUser);
    
    public List<InvDetailDTO> addItemOfferDetailList(List<InvDetailDTO> itemOfferDetailDTOList,Integer itemOfferId, TobyUser tobyUser);
    
    public List<ItemOfferDetailDTO> findItemOfferDetailByBranch(Integer branchId);
    
    public List<InvDetailDTO> findItemOfferDetailByItemOfferId(Integer branchId , Integer itemOffer);
    
    public ItemOfferDTO addInvPurchaseInvoiceDetail(ItemOfferDTO itemOfferDTO, Integer index);


}

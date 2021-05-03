/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.dto.InvInvoiceDTO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface ItemOfferService {

    public InvInvoiceDTO findItemOfferByItemId(Integer itemOfferId);

    public void deleteItemOffer(InvInvoiceDTO itemOfferDTO, TobyUser tobyUser);

    public InvInvoiceDTO addItemOffer(InvInvoiceDTO itemOfferDTO, TobyUser tobyUser);
    
    public List<InvInvoiceDTO> findItemOfferByBranch(Integer branchId);
    
    public BigDecimal findCostOffer(Integer branchId,String itemCode,Date date);


}

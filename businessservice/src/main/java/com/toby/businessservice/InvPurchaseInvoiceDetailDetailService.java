/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvPurchaseInvoiceDetailDetailDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface InvPurchaseInvoiceDetailDetailService {

    public void deleteInvPurchaseInvoiceDetailDetailDTO(Integer InvPurchaseInvoiceDetailId);

    public InvDetailDTO save(InvDetailDTO invDetailDTO, TobyUser tobyUser);

    public InvDetailDTO findDetailDetail(InvDetailDTO dTO,ScreenNameClassEnum screenNameClassEnum);

    public List<InvPurchaseInvoiceDetailDetailDTO> getInvPurchaseInvoiceDetailsDetailsByDetailId(Integer InvDetailDTOId);

}

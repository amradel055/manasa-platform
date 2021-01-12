/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface ProFactoryDeliveryDetailService {

    public List<InvDetailDTO> getAllProFactoryDeliveryDetailList(InvInvoiceDTO dto);

    public List<InvDetailDTO> getAllProFactoryDeliveryDetailListNotLoaded(InvInvoiceDTO dto);

    public List<InvDetailDTO> getAllProPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO dto);
    
    public String addProFactoryDeliveryDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser);
    
    public void deleteProFactoryDeliveryDetailListDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList);

}

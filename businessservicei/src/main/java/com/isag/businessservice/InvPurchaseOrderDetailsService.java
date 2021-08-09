/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvPurchaseOrderDetailsService {

    public void addPurchaseOrderDetails(InvPurchaseOrderDetail details);

    public void deletePurchaseOrderDetails(InvPurchaseOrderDetail details);

    public InvPurchaseOrderDetail updatePurchaseOrderDetails(InvPurchaseOrderDetail details);

    public List<InvPurchaseOrderDetail> getAllPurchaseOrderDetails();

    public List<InvPurchaseOrderDetail> getAllPurchaseOrderDetailsByInvPurchaseOrderId(Integer invPurchaseOrderId);

    public List<InvPurchaseOrderDetail> getAllPurchaseOrderDetails(Integer id);

    public InvPurchaseOrderDetail findInvPurchaseOrderDetailById(Integer invPurchaseOrderDetailId);
    
    public List<InvPurchaseOrderDetail> getAllPurchaseOrderDetailsWithStatus(Integer id);
    
    public List<InvDetailDTO> getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO dto);
    
    public void deleteInvPurchaseInvoiceDetailListDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList);
    
    public String addInvPurchaseOrderDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invPurchaseInvoiceDTO, ScreenNameClassEnum screenNameClassEnum, IsagUser isagUser);
}

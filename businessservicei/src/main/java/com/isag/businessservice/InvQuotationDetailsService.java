/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvQotationDetail;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvQuotationDetailsService {

    public void addInvQotationDetails(InvQotationDetail details);

    public void deleteInvQotationDetails(InvQotationDetail details);

    public InvQotationDetail updateInvQotationDetails(InvQotationDetail details);

    public List<InvQotationDetail> getAllInvQotationDetails();

    public List<InvQotationDetail> getAllInvQotationDetails(Integer id);

    public InvQotationDetail findInvQotationDetailById(Integer invQotationDetailId);

    public List<InvQotationDetail> getAllInvQotationDetailsByIInvQotationIdWithStatus(Integer invQotationId);

    public List<InvDetailDTO> getAllInvQutationDetailByInvPurchaseInvoiceIdDTO(InvInvoiceDTO dto);
    
    public void deleteInvQuotationDetailListDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList);
    
    public String addInvQuotationDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invPurchaseInvoiceDTO, ScreenNameClassEnum screenNameClassEnum, IsagUser isagUser);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.InvQotationDetail;
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
    
    public String addInvQuotationDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invPurchaseInvoiceDTO, ScreenNameClassEnum screenNameClassEnum, TobyUser tobyUser);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.InvQotation;
import com.toby.entity.InvQotationDetail;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvQuotationService {

    public InvPurchaseReturnSave addInvQotation(InvQotation invQotation, List<InvQotationDetail> invQotationDetailDetailList, List<InvQotationDetail> qotationDetailDeleted);

    public void deleteInvQotation(InvQotation invQotation, Integer invQotationIdToPass);
    
    public void deleteInvQotation(InvInvoiceDTO invInvoiceDTO);

    public List<InvQotation> getALLInvQotation();

    public List<InvQotation> getALLInvQotationByCompanyId(Integer Id);

    public List<InvQotation> getALLInvQotationByBranchId(Integer Id);

    public InvQotation updateInvQotation(InvQotation invQotation);

    public List<InvQotationDetail> getALLInvQotationDetailsByInvQotation(Integer invQotationId);

    public InvQotation findInvQotationByInvQotationId(Integer QuotationId);

    public List<InvQotation> getALLInvQotationByBranchIdByStatus(Integer branchId);

    public InvInvoiceDTO findInvPurchaseOrderById(InvInvoiceDTO invPurchaseInvoiceDto, TobyUser tobyUser);

    public InvInvoiceDTO save(InvInvoiceDTO invPurchaseInvoiceDTO, TobyUser tobyUser);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters);

    public List<InvInvoiceDTO> getALLInvQotationByBranchId( int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

     public InvInvoiceDTO convertQotationToPurchaseInvoice(InvInvoiceDTO invPurchaseInvoiceDTO,Integer type);
}

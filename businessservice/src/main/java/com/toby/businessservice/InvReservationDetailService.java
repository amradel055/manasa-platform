package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvReservationDTO;
import com.toby.dto.InvReservationDetailDTO;
import com.toby.entity.InvReservationDetail;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author khaled
 */
@Remote
public interface InvReservationDetailService {

    void addInvReservationDetails(InvReservationDetail details);

    void deleteInvReservationDetails(InvReservationDetail details);

    InvReservationDetail updateInvReservationDetails(InvReservationDetail details);

    List<InvReservationDetail> getAllInvReservationDetails();

    List<InvReservationDetail> getAllInvReservationDetails(Integer ReservationId);

    public InvReservationDetail findInvReservationDetailById(Integer invReservationDetailId);
    
    public List<InvReservationDetail> getAllInvReservationDetailsByInvReservationIdWithStatus(Integer invReservationId);
    
    public List<InvReservationDetailDTO> getAllInvReservationDetailsDTO(InvReservationDTO invReservationDTO);
    
    public List<InvDetailDTO> getAllReservationDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invPurchaseInvoiceDTO, Integer branchId);
    
    public void deleteInvPurchaseInvoiceDetailListDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList);
    
    public String addInvPurchaseInvoiceDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invPurchaseInvoiceDTO, TobyUser tobyUser);
}

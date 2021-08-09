package com.isag.businessservice;

import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InvReservationDTO;
import com.isag.dto.InvReservationDetailDTO;
import com.isag.entity.InvReservationDetail;
import com.isag.entity.IsagUser;

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
    
    public String addInvPurchaseInvoiceDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invPurchaseInvoiceDTO, IsagUser isagUser);
}

package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.InvReservation;
import com.toby.entity.InvReservationDetail;

import javax.ejb.Remote;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Remote
public interface InvReservationService {

    public InvPurchaseReturnSave addInvReservation(InvReservation invReservation, List<InvReservationDetail> invReservationDetailDetailList,
            List<InvReservationDetail> ReservationDetailDeleted);

    void deleteInvReservation(InvReservation invReservation, Integer invReservationIdToPass);

    public void deleteInvReservation(InvInvoiceDTO invInvoiceDTO);

    List<InvReservation> getALLInvReservation();

    List<InvReservation> getALLInvReservationByCompanyId(Integer Id);

    List<InvReservation> getALLInvReservationByBranchId(Integer Id);

    public List<InvReservation> getALLInvReservationsByBranchIdByStatus(Integer branchId);

    List<InvReservationDetail> getALLInvReservationDetailsByInvReservation(Integer invReservationId);

    InvReservation findInvReservationByInvReservationId(Integer invReservationId);

    Integer findMaxSerialNoByBranch(Integer branchId);

    public InvReservation updateInvReservation(InvReservation invReservation);

    public Integer getMaxSerialForInvoiceDetail(InvReservation invReservation);

    public InvInvoiceDTO findInvPurchaseInvoiceById(InvInvoiceDTO invPurchaseInvoiceDto, TobyUser tobyUser);

    public InvInvoiceDTO save(InvInvoiceDTO invPurchaseInvoiceDTO, TobyUser tobyUser);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceByBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

}

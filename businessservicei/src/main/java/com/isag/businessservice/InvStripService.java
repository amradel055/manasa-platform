package com.isag.businessservice;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvStrip;
import com.isag.entity.InvStripDetail;
import com.isag.entity.IsagUser;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author khaled
 */
@Remote
public interface InvStripService {

    InvPurchaseReturnSave addInvStrip(InvStrip invStrip, List<InvStripDetail> invStripDetailDetailList, List<InvStripDetail> StripDetailDeleted);

    void deleteInvStrip(InvStrip invStrip, Integer invStripIdToPass);

    List<InvStrip> getALLInvStrip();

    List<InvStrip> getALLInvStripByCompanyId(Integer Id);

    List<InvStrip> getALLInvStripByBranchId(Integer Id, Integer type);

    List<InvStripDetail> getALLInvStripDetailsByInvStrip(Integer invStripId);

    InvStrip findInvStripByInvStripId(Integer invStripId);

    Integer getMaxSerialForInvoiceDetail(InvStrip invStrip);

    public InvInvoiceDTO findInvStripById(InvInvoiceDTO invInvoiceDTO);

    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser);

    public InvInvoiceDTO convertInvStripListToInvInvoice(InvInvoiceDTO invInvoiceDTO);

    public Integer FindItemsNumber(Integer branchId);
}

package com.toby.businessservice;

import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvStripDTO;
import com.toby.entity.InvStrip;
import com.toby.entity.InvStripDetail;
import com.toby.entity.TobyUser;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author khaled
 */
@Remote
public interface InvStripService {

    InvPurchaseReturnSave addInvStrip(InvStrip invStrip, List<InvStripDetail> invStripDetailDetailList, List<InvStripDetail> StripDetailDeleted);

    void deleteInvStrip(InvStrip invStrip);

    List<InvStrip> getALLInvStrip();

    List<InvStrip> getALLInvStripByCompanyId(Integer Id);

    List<InvStrip> getALLInvStripByBranchId(Integer Id, Integer type);

    List<InvStripDetail> getALLInvStripDetailsByInvStrip(Integer invStripId);

    InvStrip findInvStripByInvStripId(Integer invStripId);

    Integer getMaxSerialForInvoiceDetail(InvStrip invStrip);

    public InvStripDTO findInvStripByInvStripIdDTO(Integer invStripId, TobyUser tobyUser);

    public InvStripDTO saveNew(InvStripDTO invStripDTO, TobyUser tobyUser);

}

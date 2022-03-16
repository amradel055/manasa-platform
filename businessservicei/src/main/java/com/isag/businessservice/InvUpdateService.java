package com.isag.businessservice;

import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvUpdate;
import com.isag.entity.InvUpdateDetail;
import com.isag.entity.IsagUser;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author khaled
 */
@Remote
public interface InvUpdateService {

    InvUpdate addInvUpdate(InvUpdate invUpdate, List<InvUpdateDetail> invUpdateDetailList, List<InvUpdateDetail> UpdateDetailDeleted);

    void deleteInvUpdate(InvUpdate invUpdate, Integer invUpdateIdToPass);

    List<InvUpdate> getALLInvUpdate();

    List<InvUpdate> getALLInvUpdateByCompanyId(Integer Id);

    List<InvUpdate> getALLInvUpdateByBranchId(Integer Id, Integer type);

    List<InvUpdateDetail> getALLInvUpdateDetailsByInvUpdate(Integer invUpdateId);

    InvUpdate findInvUpdateByInvUpdateId(Integer invUpdateId);

    Integer getMaxSerialForInvoiceDetail(InvUpdate invUpdate);

    public InvInvoiceDTO findInvUpdateById(InvInvoiceDTO invInvoiceDTO);

    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser);

    public InvInvoiceDTO findInvUpdateBySerial(InvInvoiceDTO invInvoiceDTO, Integer branchId);
}

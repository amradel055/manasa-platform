package com.toby.businessservice;

import com.toby.entity.InvUpdate;
import com.toby.entity.InvUpdateDetail;

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
}

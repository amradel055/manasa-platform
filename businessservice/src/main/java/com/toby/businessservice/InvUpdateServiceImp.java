package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.InvUpdate;
import com.toby.entity.InvUpdateDetail;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Stateless
public class InvUpdateServiceImp implements InvUpdateService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    @EJB
    private InvUpdateDetailService invUpdateDetailService;

    @Override
    public InvUpdate addInvUpdate(InvUpdate invUpdate, List<InvUpdateDetail> invUpdateDetailList, List<InvUpdateDetail> UpdateDetailDeleted) {
        Integer serial;
        if (invUpdate.getId() != null) {
            dao.updateEntity(invUpdate);
        } else {
            serial = genericService.getMaxGenericSerialByType(InvUpdate.class, invUpdate.getBranchId().getId(), null);
            invUpdate.setSerial(serial);
            dao.saveEntity(invUpdate);
        }

        for (InvUpdateDetail deleted : UpdateDetailDeleted) {
            invUpdateDetailService.deleteInvUpdateDetails(deleted);
        }

        for (InvUpdateDetail detail : invUpdateDetailList) {
            detail.setInvUpdateId(invUpdate);
            if (detail.getId() != null) {
                invUpdateDetailService.updateInvUpdateDetails(detail);
            } else {
                serial = getMaxSerialForInvoiceDetail(invUpdate);
                detail.setSerial(serial);
                invUpdateDetailService.addInvUpdateDetails(detail);
            }
        }
        return invUpdate;
    }

    @Override
    public void deleteInvUpdate(InvUpdate invUpdate, Integer invUpdateIdToPass) {
        if (invUpdateIdToPass != null && invUpdateIdToPass > 0) {
            dao.executeDeleteQuery("delete from InvUpdateDetail e WHERE e.id > 0 AND e.invUpdateId.id = " + invUpdateIdToPass);
        }
        dao.deleteEntity(invUpdate);
    }

    @Override
    public List<InvUpdate> getALLInvUpdate() {
        return dao.findAll(InvUpdate.class);
    }

    @Override
    public List<InvUpdate> getALLInvUpdateByCompanyId(Integer Id) {
        return dao.findEntityListByCompanyId(InvUpdate.class, Id);
    }

    @Override
    public List<InvUpdate> getALLInvUpdateByBranchId(Integer Id, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);

        List<InvUpdate> invUpdateList = dao.findListByQuery("SELECT e FROM InvUpdate e WHERE e.branchId.id = :branchId ", params);
        return invUpdateList;
    }

    @Override
    public List<InvUpdateDetail> getALLInvUpdateDetailsByInvUpdate(Integer invUpdateId) {
        List<InvUpdateDetail> details = invUpdateDetailService.getAllInvUpdateDetails(invUpdateId);
        return details;
    }

    @Override
    public InvUpdate findInvUpdateByInvUpdateId(Integer invUpdateId) {
        return dao.findEntityById(InvUpdate.class, invUpdateId);
    }

    @Override
    public synchronized Integer getMaxSerialForInvoiceDetail(InvUpdate invUpdate) {
        Map<String, Object> params = new HashMap<>();
        params.put("invUpdateId", invUpdate.getId());

        Integer serial = dao.findEntityByQuery("SELECT max(e.serial) FROM InvUpdateDetail e WHERE e.invUpdateId.id =:invUpdateId ", params);

        if (serial == null) {
            return 1;
        } else {
            serial = serial + 1;
            return serial;
        }
    }
}

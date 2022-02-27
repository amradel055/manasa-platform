/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.core.GenericDAO;
import com.toby.entity.InvReturnPurchase;
import com.toby.entity.InvReturnPurchaseDetail;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hhhh
 */
@Stateless
public class InvReturnPurchaseServiceImpl implements InvReturnPurchaseService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InvReturnPurchaseDetailService invReturnPurchaseDetailService;
    @EJB
    private GenericService genericService;
    
    
    InvPurchaseReturnSave invPurchaseReturnSave;

    @Override
    public InvPurchaseReturnSave addInvReturnPurchase(InvReturnPurchase invReturnPurchase,
            List<InvReturnPurchaseDetail> invReturnPurchaseDetailUpdatedList, List<InvReturnPurchaseDetail> invReturnPurchaseDetailDeletedList) {
         
        invPurchaseReturnSave = new InvPurchaseReturnSave();
        if (invReturnPurchase.getId() != null) {
            dao.updateEntity(invReturnPurchase);
        } else {
//            Integer serial = genericService.getMaxSerialForInvoice(InvReturnPurchase.class, invReturnPurchase.getBranchId().getId(), invReturnPurchase.getInvInventoryId().getId(), invReturnPurchase.getType());
//            invReturnPurchase.setSerial(serial);
            invReturnPurchase.setPostFlag(0);
            dao.saveEntity(invReturnPurchase);
        }

        for (InvReturnPurchaseDetail updatedPurchaseDetail : invReturnPurchaseDetailUpdatedList) {
            if (updatedPurchaseDetail.getId() != null) {
                invReturnPurchaseDetailService.updateReturnPurchaseDetail(updatedPurchaseDetail);
            } else {
                updatedPurchaseDetail.setSerial(getMaxSerialForInvoiceDetail(invReturnPurchase));
                updatedPurchaseDetail.setInvReturnPurchaseId(invReturnPurchase);
                invReturnPurchaseDetailService.addReturnPurchaseDetail(updatedPurchaseDetail);
            }
        }

        for (InvReturnPurchaseDetail deletedPurchaseDetail : invReturnPurchaseDetailDeletedList) {
            invReturnPurchaseDetailService.deleteReturnPurchaseDetail(deletedPurchaseDetail);
        }
        invPurchaseReturnSave.setInvReturnPurchase(invReturnPurchase);
        invPurchaseReturnSave.setInvReturnPurchaseDetailList(invReturnPurchaseDetailService.getAllReturnPurchaseDetailsByReturnPurchaseId(invReturnPurchase.getId()));
      
        return invPurchaseReturnSave;
    }

    @Override
    public void deleteInvReturnPurchase(InvReturnPurchase invPurchaseInvoice) {
        if (invPurchaseInvoice.getId() != null && invPurchaseInvoice.getId() > 0) {
            List<InvReturnPurchaseDetail> detailList = invReturnPurchaseDetailService.getAllReturnPurchaseDetailsByReturnPurchaseId(invPurchaseInvoice.getId());
            deleteDetails(detailList);
            dao.deleteEntity(invPurchaseInvoice);
        }
    }

    private synchronized void deleteDetails(List<InvReturnPurchaseDetail> detailList) {
        for (InvReturnPurchaseDetail detail : detailList) {
            invReturnPurchaseDetailService.deleteReturnPurchaseDetail(detail);
        }
    }

    @Override
    public List<InvReturnPurchase> getALLInvReturnPurchaseByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(InvReturnPurchase.class, companyId);
    }

    @Override
    public List<InvReturnPurchase> getALLInvReturnPurchaseByBranchId(Integer branchId, Boolean type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        return dao.findListByQuery("SELECT e FROM InvReturnPurchase e WHERE e.branchId.id = :branchId AND e.type = :type", params);
    }
    
    @Override
    public List<InvReturnPurchase> getALLInvReturnPurchaseByBranchIdPer(Integer branchId, Boolean type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<InvReturnPurchase>  invReturnPurchases = dao.findListByQuery("SELECT e FROM InvReturnPurchase e WHERE e.branchId.id = :branchId AND e.type = :type", params);
        List<InvReturnPurchase> list = new ArrayList<>();
        for(InvReturnPurchase invReturnPurchase : invReturnPurchases){
            InvReturnPurchase purchase = new InvReturnPurchase();
            purchase.setId(invReturnPurchase.getId());
            purchase.setSerial(invReturnPurchase.getSerial());            
            list.add(purchase);
        }
        return list;
    }

    @Override
    public InvReturnPurchase findInvReturnPurchaseById(Integer invReturnPurchaseId) {
        return dao.findEntityById(InvReturnPurchase.class, invReturnPurchaseId);
    }

    @Override
    public synchronized Integer getMaxSerialForInvoiceDetail(InvReturnPurchase returnPurchaseId) {
        Map<String, Object> params = new HashMap<>();
        params.put("returnPurchaseId", returnPurchaseId.getId());

        Integer serial = dao.findEntityByQuery("SELECT max(e.serial) FROM InvReturnPurchaseDetail e WHERE e.invReturnPurchaseId.id =:returnPurchaseId ", params);

        if (serial == null) {
            return 1;
        } else {
            return serial + 1;
        }
    }

    @Override
    public InvReturnPurchase updateReturnPurchaseInvoice(InvReturnPurchase invReturnPurchase) {
   InvReturnPurchase returnPurchase = new InvReturnPurchase();

        returnPurchase = dao.findEntityById(InvReturnPurchase.class, invReturnPurchase.getId());
        returnPurchase.setPostFlag(invReturnPurchase.getPostFlag());
        if (invReturnPurchase.getPostFlag().compareTo(1) == 0) {
            returnPurchase.setGeneralJournalId(invReturnPurchase.getGeneralJournalId());
        } else {
            returnPurchase.setGeneralJournalId(null);
        }

        return dao.updateEntity(invReturnPurchase);
    }
}

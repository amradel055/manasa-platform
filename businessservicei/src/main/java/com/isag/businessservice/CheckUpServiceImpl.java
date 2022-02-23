/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.GeneralJournal;
import com.isag.entity.HosCheckUp;
import com.isag.entity.HosCheckUpDetail;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author hq002
 */
@Stateless
public class CheckUpServiceImpl implements CheckUpService {

    @EJB
    private GenericDAO dao;
    private EntityManager em;
    @EJB
    CheckUpDetailsService checkUpDetailsService;

    @Override
    public HosCheckUp addHosCheckUp(HosCheckUp hosCheckUp, List<HosCheckUpDetail> hosCheckUpDetailList, List<HosCheckUpDetail> hosCheckUpDetailListDeleted) {

        if (hosCheckUp.getId() != null) {
            dao.updateEntity(hosCheckUp);
        } else {

            hosCheckUp.setSerial(getMaxSerail(hosCheckUp));
            dao.saveEntity(hosCheckUp);
        }

        if (hosCheckUpDetailListDeleted != null && !hosCheckUpDetailListDeleted.isEmpty()) {
            for (HosCheckUpDetail deleted : hosCheckUpDetailListDeleted) {
                checkUpDetailsService.deleteHosCheckUpDetails(deleted);
            }
        }

        Integer maxSerial = 0;
        if (hosCheckUp != null && hosCheckUp.getId() != null) {
            maxSerial = checkUpDetailsService.finMaxSerial(hosCheckUp.getId());
        } else {
            maxSerial++;
        }

        for (HosCheckUpDetail hosCheckUpDetail : hosCheckUpDetailList) {
            hosCheckUpDetail.setHosCheckUpId(hosCheckUp);

            if (hosCheckUpDetail.getId() != null) {
                checkUpDetailsService.updateHosCheckUpDetails(hosCheckUpDetail);
            } else {
                hosCheckUpDetail.setSerial(maxSerial);
                checkUpDetailsService.addHosCheckUpDetails(hosCheckUpDetail);
                maxSerial++;
            }
        }

        return hosCheckUp;
    }

    @Override
    public synchronized Integer getMaxSerail(HosCheckUp hosCheckUp) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", hosCheckUp.getBranchId().getId());
        params.put("companyId", hosCheckUp.getCompanyId().getId());
        //  params.put("transactionType", hosCheckUp.getTransactionType());

        Integer max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.serial),0) +1  FROM HosCheckUp g WHERE g.branchId.id =:branchId AND g.companyId.id =:companyId ", params);

        return max;
    }

    @Override
    public void deleteHosCheckUp(HosCheckUp hosCheckUp) {
        if (hosCheckUp.getId() != null && hosCheckUp.getId() > 0) {
            dao.executeDeleteQuery("delete from HosCheckUpDetail e WHERE e.id > 0 AND e.hosCheckUpId.id = " + hosCheckUp.getId());
        }
        dao.deleteEntity(hosCheckUp);
    }

    @Override
    public List<HosCheckUp> getALLHosCheckUpByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(HosCheckUp.class, companyId);
    }

    @Override
    public HosCheckUp findHosCheckUpById(Integer hosCheckUpId) {
        return dao.findEntityById(HosCheckUp.class, hosCheckUpId);
    }

    @Override
    public HosCheckUp addOneHosCheckUp(HosCheckUp hosCheckUp, HosCheckUpDetail hosCheckUpDetail) {
        if (hosCheckUp.getId() != null) {
            hosCheckUp = dao.updateEntity(hosCheckUp);
        } else {

            hosCheckUp.setSerial(getMaxSerail(hosCheckUp));
            dao.saveEntity(hosCheckUp);
        }

        //  hosCheckUpDetail.setHosCheckUpId(hosCheckUp);
        checkUpDetailsService.updateHosCheckUpDetails(hosCheckUpDetail);
        return hosCheckUp;
    }

    @Override
    public List<HosCheckUp> getALLHosCheckUpSettlmentByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("transactionType", 0);
        List<HosCheckUp> hosCheckUpList = dao.findListByQuery("SELECT e FROM HosCheckUp e WHERE e.branchId.id = :branchId AND e.transactionType =:transactionType order by e.serial DESC ", params);
        return hosCheckUpList;
    }

    @Override
    public List<HosCheckUp> getALLHosCheckUpRecievableByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        return dao.findListByQuery("SELECT e FROM HosCheckUp e WHERE e.branchId.id = :branchId order by e.serial DESC ", params);
    }

    @Override
    public List<HosCheckUp> getALLHosCheckUpTransferByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("transactionType", 2);

        return dao.findListByQuery("SELECT e FROM HosCheckUp e WHERE e.branchId.id = :branchId AND e.transactionType =:transactionType order by e.serial DESC", params);
    }

    @Override
    public HosCheckUp updateHosCheckUp(HosCheckUp hosCheckUp) {

        HosCheckUp bankTransactionupdate = new HosCheckUp();

        bankTransactionupdate = dao.findEntityById(HosCheckUp.class, hosCheckUp.getId());
        /*   bankTransactionupdate.setPostFlag(hosCheckUp.getPostFlag());
        if (hosCheckUp.getPostFlag().compareTo(1) == 0) {
            bankTransactionupdate.setGeneralJournalId(hosCheckUp.getGeneralJournalId());
        } else {
            bankTransactionupdate.setGeneralJournalId(null);
        }
         */
        return dao.updateEntity(bankTransactionupdate);

    }

}

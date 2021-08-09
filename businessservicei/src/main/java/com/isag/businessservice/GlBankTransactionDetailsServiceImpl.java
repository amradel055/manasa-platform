/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.GlBankTransactionDetail;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class GlBankTransactionDetailsServiceImpl implements GlBankTransactionDetailsService {

    @EJB
    private GenericDAO dao;

    @Override
    public void addGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail) {
        dao.saveEntity(glBankTransactionDetail);
    }

    @Override
    public void deleteGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail) {
        dao.deleteEntity(glBankTransactionDetail);
    }

    @Override
    public GlBankTransactionDetail updateGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail) {
        return dao.updateEntity(glBankTransactionDetail);
    }

    @Override
    public List<GlBankTransactionDetail> getAllGlBankTransactionDetailsByGlBankTransactionId(Integer glBankTransactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("glBankTransactionId", glBankTransactionId);
        List<GlBankTransactionDetail> details = dao.findListByQuery("SELECT e FROM GlBankTransactionDetail e WHERE e.glBankTransactionId.id = :glBankTransactionId", params);
        return details;
    }

    @Override
    public Boolean checkGlBankIsUsed(Integer glBankId, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("glBankId", glBankId);
        params.put("branchId", branchId);
       Long check = dao.findEntityByQuery("SELECT COALESCE(Count(e),0)  FROM  GlBankTransactionDetail e WHERE  e.glBankTransactionId.branchId.id = :branchId AND (e.glBankTransactionId.glBankId.id = :glBankId  OR (e.bankIdTo.id = :glBankId OR e.bankIdFrom.id = :glBankId))", params);
        if (check != null) {
            Boolean check1 = (Boolean) (check > 0);
            if (check1 != null && check1) {
                return check1;
            } else {
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public GlBankTransactionDetail findGlBankTransactionDetailsById(Integer glBankTransactionDetailsId) {
        return dao.findEntityById(GlBankTransactionDetail.class, glBankTransactionDetailsId);
    }

    @Override
    public GlBankTransactionDetail addOneGlBankTransactionDetail(GlBankTransactionDetail glBankTransactionDetail) {
        return dao.updateEntity(glBankTransactionDetail);
    }

    @Override
    public GlBankTransactionDetail getGlBankTransactionDetailByGlBankTransactionId(Integer glBankTransactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("glBankTransactionId", glBankTransactionId);
        return dao.findEntityByQuery("SELECT e FROM GlBankTransactionDetail e WHERE e.glBankTransactionId.id = :glBankTransactionId", params);

    }

    @Override
    public Integer finMaxSerial(Integer glBankTransactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("glBankTransactionId", glBankTransactionId);
        Integer max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.serial),0) +1  FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.id =:glBankTransactionId  ", params);
        return max;
    }

}

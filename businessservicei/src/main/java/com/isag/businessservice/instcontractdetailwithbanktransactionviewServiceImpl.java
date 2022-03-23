/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.views.InstcontractdetailwithbanktransactionView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class instcontractdetailwithbanktransactionviewServiceImpl implements instcontractdetailwithbanktransactionviewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<InstcontractdetailwithbanktransactionView> findInstContractDetailByBankTransaction(Integer instContractId) {
        Map<String, Object> params = new HashMap();
        params.put("instContractId", instContractId);
        List<InstcontractdetailwithbanktransactionView> list = dao.findListByQuery("SELECT i FROM InstcontractdetailwithbanktransactionView i WHERE i.instContractId =:instContractId", params);
        return list;
    }

    @Override
    public List<Integer> findInstContractDetailByInstContractId(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<Integer> list = dao.findListByQuery("SELECT i.instContractId FROM InstcontractdetailwithbanktransactionView i WHERE i.branchId =:branchId", params);
        return list;
    }

}

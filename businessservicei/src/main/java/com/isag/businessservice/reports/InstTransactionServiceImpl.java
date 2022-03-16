/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.core.GenericDAO;
import com.isag.core.storedProcedureParameter;
import com.isag.views.InstTransactionTemp;
import com.isag.views.InstTransactionView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class InstTransactionServiceImpl implements InstTransactionService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<InstTransactionView> getInstViewBySearchBean(Integer instContactId) {
        Map<String, Object> params = new HashMap<>();

        params.put("instContactId", instContactId);
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e FROM InstTransactionView e WHERE e.instContractId = :instContactId ORDER BY e.serial ";
//        String query = "SELECT e FROM InstTransactionView e WHERE e.instContractId = :instContactId AND e.remaining > 0 ORDER BY e.serial ";
        stringBuilder.append(query);
        List<InstTransactionView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }
    
       private void instTransactionProcedure( Integer branchId , Integer instContactId) {
        List<storedProcedureParameter> procedureParameter = new ArrayList<>();
        procedureParameter.add(new storedProcedureParameter("branchId", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("ContractId", Integer.class, 'i'));
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("ContractId",instContactId);
        dao.StoredProcedure("inst_Transaction_procedure", procedureParameter, params);
    }
    
        @Override
    public List<InstTransactionTemp> getInstTransactionTemp(Integer branchId , Integer instContactId) {
        instTransactionProcedure(branchId,instContactId); 
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT i FROM InstTransactionTemp i HAVING i.remaining > 0 ORDER BY i.serial ";
        stringBuilder.append(query);
        List<InstTransactionTemp> details = dao.findListByQuery(stringBuilder.toString());
        return details;
    }


}

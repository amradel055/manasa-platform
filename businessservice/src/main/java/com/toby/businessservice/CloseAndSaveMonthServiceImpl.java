/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.CloseAndSaveMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class CloseAndSaveMonthServiceImpl implements CloseAndSaveMonthService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteCloseAndSaveMonth(CloseAndSaveMonth CloseAndSaveMonth) {
        dao.deleteEntity(CloseAndSaveMonth);
    }

    @Override
    public List<CloseAndSaveMonth> getAllCloseAndSaveMonths() {
        return dao.findAll(CloseAndSaveMonth.class);
    }

    @Override
    public CloseAndSaveMonth addCloseAndSaveMonth(CloseAndSaveMonth CloseAndSaveMonth) {
        return dao.updateEntity(CloseAndSaveMonth);
    }

    @Override
    public CloseAndSaveMonth findCloseAndSaveMonth(Integer CloseAndSaveMonthId) {
        return dao.findEntityById(CloseAndSaveMonth.class, CloseAndSaveMonthId);
    }

    @Override
    public CloseAndSaveMonth updateCloseAndSaveMonth(CloseAndSaveMonth CloseAndSaveMonth) {
        return dao.updateEntity(CloseAndSaveMonth);
    }

    @Override
    public List<CloseAndSaveMonth> getAllCloseAndSaveMonthsByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT c FROM CloseAndSaveMonth c WHERE c.branchId.id =:branchId", params);

    }

    @Override
    public List<CloseAndSaveMonth> findCloseAndSaveMonth(Integer monthId, Integer monthNumber, Integer yearId, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("monthNumber", monthNumber);
        params.put("yearId", yearId);
        params.put("branchId", branchId);

        String query = "SELECT c FROM CloseAndSaveMonth c WHERE c.branchId.id =:branchId and c.year.id = :yearId and c.monthNumber = :monthNumber";
        if (monthId != null && monthId > 0) {
            query += " And c.id != :monthId";
            params.put("monthId", monthId);
        }

        return dao.findListByQuery(query, params);

    }

    @Override
    public List<CloseAndSaveMonth> getCloseAndSaveMonthsByYearIdAndMounthNumberAndBranchId(Integer branchId, Integer yearId, Integer monthNumber) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        params.put("yearId", yearId);
        params.put("monthNumber", monthNumber);
        return dao.findListByQuery("SELECT c FROM CloseAndSaveMonth c "
                + "WHERE c.branchId.id =:branchId AND c.year.id = :yearId AND c.monthNumber = :monthNumber AND c.status= 1 ", params);
    }
 @Override
    public boolean isCloseAndSaveMonthsByYearIdAndMounthNumberAndBranchIdExist(Integer branchId, Integer yearId, Integer monthNumber) {
        Map<String, Object> params = new HashMap<>();
         
        params.put("branchId", branchId);
        params.put("yearId", yearId);
        params.put("monthNumber", monthNumber);
        List<CloseAndSaveMonth> closeAndSaveMonthList
                = dao.findListByQuery("SELECT c FROM CloseAndSaveMonth c "
                        + "WHERE c.branchId.id =:branchId AND c.year.id = :yearId AND c.monthNumber = :monthNumber AND c.status= 1 ", params);
        if (closeAndSaveMonthList != null && !closeAndSaveMonthList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

}

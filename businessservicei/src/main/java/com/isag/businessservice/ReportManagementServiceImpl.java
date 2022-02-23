/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.ReportManagement;
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
public class ReportManagementServiceImpl implements ReportManagementService {

    @EJB
    private GenericDAO dao;

    @Override
    public ReportManagement reportManagementByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ReportManagement> list = dao.findListByQuery("SELECT rm FROM ReportManagement rm WHERE rm.branchId.id=:branchId", params);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void savereportManagement(ReportManagement reportManagement) {
        reportManagement = dao.updateEntity(reportManagement);
    }
    

}

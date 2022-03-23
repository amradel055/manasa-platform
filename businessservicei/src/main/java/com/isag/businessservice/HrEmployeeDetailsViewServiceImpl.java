/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.views.EmployeeDetailsSelectView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class HrEmployeeDetailsViewServiceImpl implements HrEmployeeDetailsViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<EmployeeDetailsSelectView> findHrEmployeeDetailsView(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<EmployeeDetailsSelectView> hrEmployeeDetailsViewList = dao.findListByQuery("SELECT I FROM EmployeeDetailsSelectView I WHERE I.branchId =:branchId ", params);
        return hrEmployeeDetailsViewList;
    }

}

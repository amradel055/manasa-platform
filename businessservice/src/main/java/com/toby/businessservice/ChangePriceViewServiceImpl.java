/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.views.ChangePriceView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author elsakr6
 */
@Stateless
public class ChangePriceViewServiceImpl implements ChangePriceViewService {

    @EJB
    private GenericDAO dao;
    
    @Override
    public List<ChangePriceView> getAllChangePriceViewByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ChangePriceView> ChangePriceViewList=new ArrayList<>();
        ChangePriceViewList= dao.findListByQuery("SELECT c FROM ChangePriceView c WHERE c.branchId = :branchId ", params);
        return ChangePriceViewList;
    }
}

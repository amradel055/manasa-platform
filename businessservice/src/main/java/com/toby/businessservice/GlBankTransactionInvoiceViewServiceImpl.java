/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.TobyUser;
import com.toby.views.GlBankTransactionInvoiceView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author H
 */
@Stateless
public class GlBankTransactionInvoiceViewServiceImpl implements GlBankTransactionInvoiceViewService{
@EJB
    private GenericDAO dao;
    @Override
    public List<GlBankTransactionInvoiceView> getGlBankTransactionInvoiceViewList(Integer organizationSiteId,TobyUser tobyUser) {
        Map<String,Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("organizationSiteId", organizationSiteId);
        List<GlBankTransactionInvoiceView> detList =dao.findListByQuery("SELECT e FROM GlBankTransactionInvoiceView e where e.branchId=:branchId and e.organizationSiteId=:organizationSiteId ", params); 
        return detList;
    }
    
}

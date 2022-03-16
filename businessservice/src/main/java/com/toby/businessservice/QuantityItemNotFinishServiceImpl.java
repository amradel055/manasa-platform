/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.QuantityItemNotFinish;
import com.toby.entity.TobyUser;
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
public class QuantityItemNotFinishServiceImpl implements QuantityItemNotFinishService{

    @EJB
    private GenericDAO dao;
    @Override
    public List<QuantityItemNotFinish> getPurchaseIdFromView(TobyUser tobyUser) {
       Map<String, Object> params = new HashMap();
       params.put("branchId", tobyUser.getBranchId().getId());
       List<InvPurchaseInvoice> detailes = dao.findEntityByQuery("SELECT e FROM InvPurchaseInvoice e where (e.id in(select f.invoiceId from FindPurchaseNotFinishView f where f.branchId.id =:branchId )and e.branchId.id =:branchId)  ", params);
       
    return null;
    }
}
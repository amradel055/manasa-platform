/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.TobyUser;
import com.toby.views.ProductionTransactionStagesInvoice;
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
public class ProductionTransactionStagesInvoiceServiceImpl implements ProductionTransactionStagesInvoiceService{
@EJB
    private GenericDAO dao;

    @Override
    public List<ProductionTransactionStagesInvoice> getProductionTransactionStagesInvoiceList(Integer proProductionStagesId, TobyUser tobyUser) {
    Map<String,Object> params =new HashMap<>(); 
    params.put("proProductionStagesId", proProductionStagesId);
    
    List<ProductionTransactionStagesInvoice> details =dao.findListByQuery("SELECT e FROM ProductionTransactionStagesInvoice e where e.proProductionStagesId =:proProductionStagesId", params);
            return details;
    }
    
}

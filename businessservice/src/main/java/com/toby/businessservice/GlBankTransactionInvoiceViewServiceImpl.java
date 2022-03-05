/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.TobyUser;
import com.toby.views.GlBankTransactionInvoiceView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author H
 */
@Stateless
public class GlBankTransactionInvoiceViewServiceImpl implements GlBankTransactionInvoiceViewService {
    
    @EJB
    private GenericDAO dao;
    
    @Override
    public List<GlBankTransactionInvoiceView> getGlBankTransactionInvoiceViewList(Integer organizationSiteId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("organizationSiteId", organizationSiteId);
        List<GlBankTransactionInvoiceView> detList = dao.findListByQuery("SELECT e FROM GlBankTransactionInvoiceView e where e.branchId=:branchId and e.organizationSiteId=:organizationSiteId ", params);
        return detList;
    }
    
    @Override
    public List<GlBankTransactionInvoiceView> getGlBankTransactionInvoiceViewListNew(Integer organizationSiteId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("organizationSiteId", organizationSiteId);
        List<Object[]> res = dao.findListByQuery("SELECT i.invPurchaseInvoiceId.serial as serial ,i.invPurchaseInvoiceId.id as invoice_id ,"
                + " i.invPurchaseInvoiceId.organizationSiteId.id as organization_site_id ,sum(i.netAfterDiscountHead) as valuet   FROM InvPurchaseInvoiceDetail i where i.invPurchaseInvoiceId.branchId.id =:branchId and i.invPurchaseInvoiceId.organizationSiteId.id =:organizationSiteId and i.invPurchaseInvoiceId.type = 4 group by i.invPurchaseInvoiceId.id ", params);
        return findGlBankTransactionInvoiceView(res);
    }
    
    public List<GlBankTransactionInvoiceView> findGlBankTransactionInvoiceView(List<Object[]> res) {
        
        List<GlBankTransactionInvoiceView> list = new ArrayList<>();
        
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            GlBankTransactionInvoiceView detailsIterated = new GlBankTransactionInvoiceView();
            detailsIterated.setSerial((Integer) line[0]);
            detailsIterated.setInvoiceId((Integer) line[1]);
            detailsIterated.setOrganizationSiteId((Integer) line[2]);
            BigDecimal value = (BigDecimal) line[3];
            if(value == null){
                value = BigDecimal.ZERO;
            }
            detailsIterated.setValue(value.multiply(new BigDecimal(1.15)));
            list.add(detailsIterated);
        }
        return list;
    }
    
}

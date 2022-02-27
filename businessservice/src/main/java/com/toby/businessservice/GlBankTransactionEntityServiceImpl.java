/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.GlBankTransaction;
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
public class GlBankTransactionEntityServiceImpl implements GlBankTransactionEntityService{
  @EJB
    private GenericDAO dao;
    @Override
    public List<GlBankTransaction> getGlBankTransactionEntityList(TobyUser tobyUser) {
     Map<String,Object> params =new HashMap<>(); 
     params.put("branchId", tobyUser.getBranchId().getId()); 
     List<GlBankTransaction> details= dao.findListByQuery("SELECT e FROM GlBankTransaction e where e.branchId=:branchId", params);
     return details;
     
    }
    
}

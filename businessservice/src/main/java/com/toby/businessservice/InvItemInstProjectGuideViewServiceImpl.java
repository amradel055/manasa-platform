/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.views.InvItemInstProjectGuideView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class InvItemInstProjectGuideViewServiceImpl implements InvItemInstProjectGuideViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<InvItemInstProjectGuideView> findInvItemInstProjectGuideView(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<InvItemInstProjectGuideView> invItemInstProjectGuideViewList = dao.findListByQuery("SELECT I FROM InvItemInstProjectGuideView I WHERE I.branchId =:branchId", params);
        return invItemInstProjectGuideViewList;
    }

  @Override
    public InvItemInstProjectGuideView findInvItemInstProjectGuideViewBytableId(Integer branchId ,Integer tableType,Integer tableId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("tableType", tableType);
        params.put("tableId", tableId);
     return  dao.findEntityByQuery("SELECT I FROM InvItemInstProjectGuideView I WHERE I.branchId =:branchId AND I.id =:tableId  AND I.tableType =:tableType ", params);
        
    }


}

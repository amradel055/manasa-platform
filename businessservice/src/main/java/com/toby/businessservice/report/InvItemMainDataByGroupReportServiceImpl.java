/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.toby.core.GenericDAO;
import com.toby.views.ItemMainDataByGroupView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hhhh
 */
@Stateless
public class InvItemMainDataByGroupReportServiceImpl implements InvItemMainDataByGroupReportService {

    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;

    @Override
    public List<ItemMainDataByGroupView> getAllItemMainDataByGroupViewByItemMainDataByGroupSearchBean(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean) {

        Map<String, Object> params = new HashMap<>();
        
        params.put("branchId", ItemMainDataByGroupSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM ItemMainDataByGroupView RIV WHERE RIV.branchId = :branchId ");

       stringBuilder.append(appendGroup(ItemMainDataByGroupSearchBean, params));
       stringBuilder.append(appendInventory(ItemMainDataByGroupSearchBean, params));
  //     stringBuilder.append(appendDate(ItemMainDataByGroupSearchBean, params));
        List<ItemMainDataByGroupView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    

    private String appendGroup(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean, Map<String, Object> params) {
         appendQuery = new StringBuilder();
        
        if (ItemMainDataByGroupSearchBean.getGroupSelected() != null && !ItemMainDataByGroupSearchBean.getGroupSelected().isEmpty() 
                && ItemMainDataByGroupSearchBean.getGroupSelected().size() > 0) {
            StringBuilder groups = new StringBuilder();
            for(String groupId : ItemMainDataByGroupSearchBean.getGroupSelected()){
                if(groups.toString().isEmpty()){
                    groups.append(groupId);
                }else{
                    groups.append(",").append(groupId);
                }
            }
            appendQuery.append(" AND RIV.groupId in (").append(groups).append(")");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private String appendInventory(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean, Map<String, Object> params) {
         appendQuery = new StringBuilder();

        if (ItemMainDataByGroupSearchBean.getFrominventory()!= null) {
            params.put("fromInventory", ItemMainDataByGroupSearchBean.getFrominventory().getId());
            appendQuery.append(" AND RIV.inventoryId >= :fromInventory");
        }

        if (ItemMainDataByGroupSearchBean.getToinventory() != null) {
            params.put("inventoryTo", ItemMainDataByGroupSearchBean.getToinventory().getId());
            appendQuery.append(" AND RIV.inventoryId <= :inventoryTo");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /* private String appendDate(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean, Map<String, Object> params) {
         appendQuery = new StringBuilder();

        if (ItemMainDataByGroupSearchBean.getFrominventory()!= null) {
            params.put("dateFrom", ItemMainDataByGroupSearchBean.getDateFrom());
            appendQuery.append(" AND RIV.inventoryId >= :dateFrom");
        }

        if (ItemMainDataByGroupSearchBean.getToinventory() != null) {
            params.put("dateTo", ItemMainDataByGroupSearchBean.getDateTo());
            appendQuery.append(" AND RIV.inventoryId <= :dateTo");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    } */
      

}

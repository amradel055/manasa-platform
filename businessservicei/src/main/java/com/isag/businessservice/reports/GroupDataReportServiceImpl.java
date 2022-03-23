/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;


import com.isag.businessservice.reports.searchBean.GroupDataReportSearchBean;

import com.isag.core.GenericDAO;
import com.isag.entity.InvGroup;

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
public class GroupDataReportServiceImpl implements GroupDataReportService {
    
    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;

  
    public List<InvGroup> getAllGroupsSearchBean(GroupDataReportSearchBean groupDataReportSearchBean)
    {
        Map<String, Object> params = new HashMap<>();
        
        params.put("branchId", groupDataReportSearchBean.getBranchId());
       StringBuilder stringBuilder = new StringBuilder();
       
        stringBuilder.append("SELECT RIV FROM InvGroup RIV WHERE RIV.branchId.id = :branchId ");
        
      
       stringBuilder.append(appendGroup(groupDataReportSearchBean, params));
        List<InvGroup> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private String appendGroup(GroupDataReportSearchBean groupDataReportSearchBean, Map<String, Object> params) {
         appendQuery = new StringBuilder();

        if (groupDataReportSearchBean.getFromGroupName()!= null) {
            params.put("FromGroup", groupDataReportSearchBean.getFromGroupName().getId());
            appendQuery.append(" AND RIV.id >= :FromGroup ");
        }

        if (groupDataReportSearchBean.getToGroupName() != null) {
            params.put("ToGroup", groupDataReportSearchBean.getToGroupName().getId());
            appendQuery.append(" AND RIV.id <= :ToGroup ");
        }
          appendQuery.append(" order by RIV.code ASC ");
        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.InstProjectGuide;
import com.isag.views.ProjectStatusView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class ProjectStatusServiceImpl implements ProjectStatusService {

    @EJB
    private GenericDAO dao;

   

    @Override
    public List<ProjectStatusView> getProjectStatusByBranchId(Integer branchId,Integer parent,Integer level,Integer status) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT e FROM ProjectStatusView e WHERE e.branchId = :branchId ");
        if(parent != null){
            params.put("parentId", parent);
            sql.append(" AND  e.parent in ( select i.id FROM InstProjectGuide i where i.parent.id = :parentId) ");
        }
        
        if(level != null){
            params.put("levelId", level);
            sql.append(" AND  e.parent = :levelId ");
        }
        
        if(status != null){
            params.put("status", status);
            sql.append(" AND  e.status = :status ");
        }
        
        sql.append(" order by e.projectCode "); 
        List<ProjectStatusView> detailses = dao.findListByQuery(sql.toString(), params);
        return detailses;
    }
    
 @Override
    public List<InstProjectGuide> getProjectGuideByLevel(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
       params.put("branchId", branchId);
        List<InstProjectGuide> detailses = dao.findListByQuery("SELECT e FROM InstProjectGuide e WHERE  e.branchId.id = :branchId  and e.parent is null ", params);
        return detailses;
    }
      @Override
    public List<InstProjectGuide> getProjectGuideByLevelAndParent(Integer branchId,Integer parent) {
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parent);
        params.put("branchId", branchId);
        List<InstProjectGuide> detailses = dao.findListByQuery("SELECT e FROM InstProjectGuide e WHERE  e.branchId.id = :branchId  and e.parent.id = :parentId ", params);
        return detailses;
    }
  
}

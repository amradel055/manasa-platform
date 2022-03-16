/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.isag.core.GenericDAO;
import com.isag.entity.InvGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class InvGroupServiceImpl implements InvGroupService {

    @EJB
    private GenericDAO dao;
  

    @Override
    public List<InvGroup> getAllInvGroups() {
        return dao.findAll(InvGroup.class);
    }

    @Override
    public void deleteInvGroup(InvGroup invGroup) {
        dao.deleteEntity(invGroup);
    }

    @Override
    public InvGroup updateInvGroup(InvGroup invGroup) {
        return dao.updateEntity(invGroup);
    }
   

    @Override
    public InvGroup addInvGroup(InvGroup invGroup) {
        return dao.updateEntity(invGroup);
    }

    @Override
    public InvGroup findInvGroup(Integer id) {
        return dao.findEntityById(InvGroup.class, id);
    }

    @Override
    public List<InvGroup> getallInvGroupByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(InvGroup.class, companyId);
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(invGroup.id) FROM InvGroup invGroup ");
        return maxId;
    }

    @Override
    public List<InvGroup> getInvGroupForUser(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InvGroup> getallInvGroupByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId ORDER BY e.code", params);
        List<InvGroup> list = improvePerformane(invGroupList);
        return list;
    }

    @Override
    public List<InvGroup> getSubInvGroupByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE  e.id in (select ii.groupId.id from InvItem ii where ii.branchId.id = :branchId)  ORDER BY e.code", params);
        return invGroupList;
    }
    
    @Override
    public List<InvGroup> getSubInvGroupByBranchIdPer(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE  e.id in (select ii.groupId.id from InvItem ii where ii.branchId.id = :branchId)  ORDER BY e.code", params);
        List<InvGroup> list = improvePerformane(invGroupList);
        return list;
    }

    private List<InvGroup> improvePerformane(List<InvGroup> invGroupList) {
        List<InvGroup> list = new ArrayList<>();
        for(InvGroup group : invGroupList){
            InvGroup invGroup = new InvGroup();
            invGroup.setId(group.getId());
            invGroup.setName(group.getName());
            invGroup.setCode(group.getCode());
            invGroup.setLevel(group.getLevel());
            invGroup.setParent(group.getParent());
            list.add(invGroup);
        }
        return list;
    }

    @Override
    public List<InvGroup> getInvGroupByGroupId(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean) {

        Map<String, Object> params = new HashMap<>();

        params.put("branchId", ItemMainDataByGroupSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId");

        stringBuilder.append(appendGroup(ItemMainDataByGroupSearchBean, params));

        stringBuilder.append(" ORDER BY e.code");
        List<InvGroup> invGroupList = dao.findListByQuery(stringBuilder.toString(), params);
        return invGroupList;
    }

    private String appendGroup(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (ItemMainDataByGroupSearchBean.getGroupSelected() != null && !ItemMainDataByGroupSearchBean.getGroupSelected().isEmpty()
                && ItemMainDataByGroupSearchBean.getGroupSelected().size() > 0) {
            StringBuilder groups = new StringBuilder();
            for (String groupId : ItemMainDataByGroupSearchBean.getGroupSelected()) {
                if (groups.toString().isEmpty()) {
                    groups.append(groupId);
                } else {
                    groups.append(",").append(groupId);
                }
            }
            appendQuery.append(" AND e.id in (").append(groups).append(")");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InvGroup> getInvGroupByBranchId(Integer branchId, Integer invGroupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("invGroupId", invGroupId);
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId AND e.id != :invGroupId AND (e.parent.id != :invGroupId OR e.parent IS NULL)  ORDER BY e.code", params);
        return invGroupList;
    }
    
    @Override
    public List<InvGroup> getInvGroupByBranchIdANDItem(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId AND e.id in (select i.groupId.id from InvItem i where i.branchId.id = :branchId )", params);
        return invGroupList;
    }
    
    @Override
    public InvGroup getInvGroupBySerial(Integer branchId, Integer code) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("code", code);
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId AND e.code = :code ORDER BY e.code", params);
        return invGroupList.get(0);
    }

    @Override
    public List<InvGroup> getInvGroupFromToByBranchId(Integer branchId, InvGroup group1, InvGroup group2) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId ";
        stringBuilder.append(query);
        searchForGroup(stringBuilder, group1, group2, params);
        stringBuilder.append(" ORDER BY e.code");
        List<InvGroup> invGroupList = dao.findListByQuery(stringBuilder.toString(), params);
        return invGroupList;
    }

    public void searchForGroup(StringBuilder stringBuilder, InvGroup group1, InvGroup group2, Map<String, Object> params) {
        if (group1 != null) {
            params.put("invGroupId1", group1.getId());
            stringBuilder.append(" AND e.id >= :invGroupId1 ");
        }
        if (group2 != null) {
            params.put("invGroupId2", group2.getId());
            stringBuilder.append(" AND e.id <= :invGroupId2 ");
        }
    }

    @Override
    public List<InvGroup> getInvGroupsRootsByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);//AND e.parent is not null
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId AND e.id not in (select g.parent.id  FROM InvGroup g WHERE g.branchId.id = :branchId AND g.parent is not null)  ORDER BY e.code", params);
        List<InvGroup> list = improvePerformane(invGroupList);
        return list;
    }

    //@param id may be null if you want all childs
    @Override
    public List<InvGroup> getAllSubInvGroupByBranchId(Integer id, Integer branchId) {
        List<InvGroup> invGroups = new ArrayList<>();
        if (id != null) {
            InvGroup ig = findInvGroup(id);
            invGroups.add(ig);
        } else {
            invGroups = getInvGroupsRootsByBranchId(branchId);
        }

        List<InvGroup> childrens = new ArrayList<>();
        if (invGroups != null && !invGroups.isEmpty()) {
            for (InvGroup group : invGroups) {
                if (group != null && group.getId() != null) {
                    findTheLastChilds(group, childrens);
                }
            }
        }
        return childrens;
    }

    @Override
    public List<InvGroup> getAllSubInvGroupByGroupsAndBranchId(List<InvGroup> invGroups, Integer branchId) {

        List<InvGroup> childrens = new ArrayList<>();
        if (invGroups != null && !invGroups.isEmpty()) {
            for (InvGroup group : invGroups) {
                if (group != null && group.getId() != null) {
                    findTheLastChilds(group, childrens);
                }
            }
        }
        return childrens;
    }

    public void findTheLastChilds(InvGroup group, List<InvGroup> childrens) {
        if (group.getInvGroupCollection() != null && !group.getInvGroupCollection().isEmpty()) {
            for (InvGroup childInvGroup : group.getInvGroupCollection()) {
                InvGroup invGroupValue = findInvGroup(childInvGroup.getId());
                findTheLastChilds(invGroupValue, childrens);
            }
        } else {
            childrens.add(group);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import java.util.List;
import com.isag.entity.InvGroup;
import javax.ejb.Remote;

@Remote
public interface InvGroupService {

    public List<InvGroup> getAllInvGroups();

    public void deleteInvGroup(InvGroup invGroup);

    public InvGroup updateInvGroup(InvGroup invGroup);

    public InvGroup addInvGroup(InvGroup invGroup);

    public InvGroup findInvGroup(Integer id);

    public List<InvGroup> getallInvGroupByCompanyId(Integer companyId);

    public List<InvGroup> getallInvGroupByBranchId(Integer branchId);

    public List<InvGroup> getSubInvGroupByBranchId(Integer branchId);

    public List<InvGroup> getSubInvGroupByBranchIdPer(Integer branchId);

    public List<InvGroup> getInvGroupByGroupId(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean);

    public List<InvGroup> getInvGroupByBranchId(Integer branchId, Integer invGroupId);
    
    public List<InvGroup> getInvGroupByBranchIdANDItem(Integer branchId);
    
    public InvGroup getInvGroupBySerial(Integer branchId, Integer code);

    public List<InvGroup> getInvGroupsRootsByBranchId(Integer branchId);

    public Integer getMaxId();

    public List<InvGroup> getInvGroupForUser(Integer id);

    public List<InvGroup> getAllSubInvGroupByBranchId(Integer id, Integer branchId);

    public List<InvGroup> getInvGroupFromToByBranchId(Integer branchId, InvGroup group1, InvGroup group2);

    public List<InvGroup> getAllSubInvGroupByGroupsAndBranchId(List<InvGroup> invGroups, Integer branchId);
}

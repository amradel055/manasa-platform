/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.toby.dto.InvGroupDTO;
import com.toby.entity.InvGroup;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface InvGroupService {

    public List<InvGroup> getAllInvGroups();

    public void deleteInvGroup(InvGroup invGroup);

    public InvGroupDTO deleteInvGroupDTO(InvGroupDTO invGroupDTO, TobyUser tobyUser);

    public InvGroup findInvGroup(Integer id);

    public List<InvGroup> getallInvGroupByBranchId(Integer branchId);

    public InvGroup updateInvGroup(InvGroup invGroup);

    public InvGroup addInvGroup(InvGroup invGroup);

    public InvGroupDTO findInvGroupDTO(Integer id, TobyUser tobyUser);

    public List<InvGroup> getallInvGroupByCompanyId(Integer companyId);

    public List<InvGroupDTO> getallInvGroupByBranchIdDTO(TobyUser tobyUser);

    public List<InvGroup> getSubInvGroupByBranchId(Integer branchId);

    public List<InvGroup> getSubInvGroupByBranchIdPer(Integer branchId);

//    public List<InvGroup> getInvGroupByGroupId(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean);
    public List<InvGroupDTO> getInvGroupByBranchId(TobyUser tobyUser, Integer invGroupId);

    public List<InvGroup> getInvGroupsRootsByBranchId(Integer branchId);

    public Integer getMaxId();

    public List<InvGroup> getInvGroupForUser(Integer id);

    public List<InvGroup> getAllSubInvGroupByBranchId(Integer id, Integer branchId);

    public List<InvGroup> getInvGroupFromToByBranchId(Integer branchId, InvGroup group1, InvGroup group2);

    public List<InvGroup> getAllSubInvGroupByGroupsAndBranchId(List<InvGroup> invGroups, Integer branchId);

    public List<InvGroup> getInvGroupByGroupId(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean);

    public List<InvGroupDTO> getinvGroupDTOList(TobyUser tobyUser);

    public InvGroupDTO saveInvGroupDTO(InvGroupDTO invGroupDTO, TobyUser tobyUser);

    public Boolean validateName(TobyUser tobyUser, Integer invGroupid, String name);

    public Boolean validateCode(TobyUser tobyUser, Integer invGroupid, Integer code);
    
     public InvGroup getInvGroupBySerial(Integer branchId, Integer code);


}

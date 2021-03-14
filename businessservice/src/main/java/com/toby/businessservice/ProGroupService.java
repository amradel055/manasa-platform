/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.toby.dto.ProGroupDTO;
import com.toby.entity.ProGroup;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ProGroupService {

    public List<ProGroup> getAllProGroups();

    public void deleteProGroup(ProGroup proGroup);

    public ProGroupDTO deleteProGroupDTO(ProGroupDTO proGroupDTO, TobyUser tobyUser);

    public ProGroup findProGroup(Integer id);

    public List<ProGroup> getallProGroupByBranchId(Integer branchId);

    public ProGroup updateProGroup(ProGroup proGroup);

    public ProGroup addProGroup(ProGroup proGroup);

    public ProGroupDTO findProGroupDTO(Integer id, TobyUser tobyUser);

    public List<ProGroup> getallProGroupByCompanyId(Integer companyId);

    public List<ProGroupDTO> getallProGroupByBranchIdDTO(TobyUser tobyUser);

    public List<ProGroup> getSubProGroupByBranchId(Integer branchId);

    public List<ProGroup> getSubProGroupByBranchIdPer(Integer branchId);

//    public List<ProGroup> getProGroupByGroupId(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean);
    public List<ProGroupDTO> getProGroupByBranchId(TobyUser tobyUser, Integer proGroupId);

    public List<ProGroup> getProGroupsRootsByBranchId(Integer branchId);

    public Integer getMaxId();

    public List<ProGroup> getProGroupForUser(Integer id);
    
    public ProGroupDTO findProGroupDTO(Integer id) ;

    public List<ProGroup> getAllSubProGroupByBranchId(Integer id, Integer branchId);

    public List<ProGroup> getProGroupFromToByBranchId(Integer branchId, ProGroup group1, ProGroup group2);

    public List<ProGroup> getAllSubProGroupByGroupsAndBranchId(List<ProGroup> proGroups, Integer branchId);

    public List<ProGroup> getProGroupByGroupId(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean);

    public List<ProGroupDTO> getproGroupDTOList(TobyUser tobyUser);

    public ProGroupDTO saveProGroupDTO(ProGroupDTO proGroupDTO, TobyUser tobyUser);

    public Boolean validateName(TobyUser tobyUser, Integer proGroupid, String name);

    public Boolean validateCode(TobyUser tobyUser, Integer proGroupid, Integer code);
    
     public ProGroup getProGroupBySerial(Integer branchId, Integer code);


}

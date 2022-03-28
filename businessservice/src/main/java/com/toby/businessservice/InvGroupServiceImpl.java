/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.InvGroupDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvGroup;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
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
    public List<InvGroup> getallInvGroupByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId ORDER BY e.code", params);
        List<InvGroup> list = improvePerformane(invGroupList);
        return list;
    }

    @Override
    public List<InvGroup> getAllInvGroups() {
        return dao.findAll(InvGroup.class);
    }

    @Override
    public void deleteInvGroup(InvGroup invGroup) {
        dao.deleteEntity(invGroup);
    }

    @Override
    public InvGroupDTO deleteInvGroupDTO(InvGroupDTO invGroupDTO, TobyUser tobyUser) {
        InvGroup invGroup = mapToEntity(invGroupDTO, tobyUser);
        invGroup.setIsDeleted(1);
        invGroup = dao.updateEntity(invGroup);
        return mapToDTO(invGroup, tobyUser);
        
    }

    @Override
    public InvGroup findInvGroup(Integer id) {
        return dao.findEntityById(InvGroup.class, id);
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
    public InvGroupDTO findInvGroupDTO(Integer id, TobyUser tobyUser) {
        InvGroup invGroup = dao.findEntityById(InvGroup.class, id);
        return mapToDTO(invGroup, tobyUser);
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
    public List<InvGroupDTO> getallInvGroupByBranchIdDTO(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId ORDER BY e.code", params);
        return mapToDTOList(invGroupList, tobyUser);
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
        for (InvGroup group : invGroupList) {
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

//    @Override
//    public List<InvGroup> getInvGroupByGroupId(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean) {
//
//        Map<String, Object> params = new HashMap<>();
//
//        params.put("branchId", ItemMainDataByGroupSearchBean.getBranchId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId");
//
//        stringBuilder.append(appendGroup(ItemMainDataByGroupSearchBean, params));
//
//        stringBuilder.append(" ORDER BY e.code");
//        List<InvGroup> invGroupList = dao.findListByQuery(stringBuilder.toString(), params);
//        return invGroupList;
//    }
//    private String appendGroup(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean, Map<String, Object> params) {
//        StringBuilder appendQuery = new StringBuilder();
//
//        if (ItemMainDataByGroupSearchBean.getGroupSelected() != null && !ItemMainDataByGroupSearchBean.getGroupSelected().isEmpty()
//                && ItemMainDataByGroupSearchBean.getGroupSelected().size() > 0) {
//            StringBuilder groups = new StringBuilder();
//            for (String groupId : ItemMainDataByGroupSearchBean.getGroupSelected()) {
//                if (groups.toString().isEmpty()) {
//                    groups.append(groupId);
//                } else {
//                    groups.append(",").append(groupId);
//                }
//            }
//            appendQuery.append(" AND e.id in (").append(groups).append(")");
//        }
//
//        return appendQuery.toString();
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    public List<InvGroupDTO> getInvGroupByBranchId(TobyUser tobyUser, Integer invGroupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("invGroupId", invGroupId);
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId AND e.id != :invGroupId AND (e.parent.id != :invGroupId OR e.parent IS NULL)  ORDER BY e.code", params);
        return mapToDTOList(invGroupList, tobyUser);
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
        params.put("branchId", branchId);
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId AND e.id not in (select g.parent.id  FROM InvGroup g WHERE g.branchId.id = :branchId AND g.parent is not null) ORDER BY e.code", params);
        List<InvGroup> list = improvePerformane(invGroupList);
        return list;
    }

    //@param id may be null if you want all childs
    @Override
    public List<InvGroup> getAllSubInvGroupByBranchId(Integer id, Integer branchId) {
        List<InvGroup> invGroups = new ArrayList<>();
        if (id != null) {
//            InvGroup ig = findInvGroupDTO(id);
//            invGroups.add(ig);
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
//                InvGroup invGroupValue = findInvGroup(childInvGroup.getId());
//                findTheLastChilds(invGroupValue, childrens);
            }
        } else {
            childrens.add(group);
        }
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

    public InvGroup mapToEntity(InvGroupDTO invGroupDTO, TobyUser tobyUser) {
        InvGroup invGroup = new InvGroup();
        invGroup.setId(invGroupDTO.getId());
        invGroup.setIsDeleted(invGroupDTO.getIsDeleted());
        if (invGroupDTO.getParent() != null) {
            InvGroup group = new InvGroup();
            group.setId(invGroupDTO.getParent().getId());
            group.setName(invGroupDTO.getParent().getName());
            group.setCode(invGroupDTO.getParent().getCode());
            invGroup.setParent(group);

        }

        invGroup.setCode(invGroupDTO.getCode());
        invGroup.setLevel(invGroupDTO.getLevel());

        invGroup.setName(invGroupDTO.getName());
        if (invGroupDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invGroupDTO.getCreatedBy());
            invGroup.setCreatedBy(user);
            invGroup.setCreationDate(invGroupDTO.getCreatedDate());
            invGroup.setModifiedBy(tobyUser);
            invGroup.setModificationDate(new Date());
            if (invGroupDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invGroupDTO.getCompanyId());
                invGroup.setCompanyId(company);
            }
            if (invGroupDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invGroupDTO.getBranchId());
                invGroup.setBranchId(branch);
            }
        } else {
            invGroup.setCreatedBy(tobyUser);
            invGroup.setCreationDate(new Date());
            invGroup.setCompanyId(tobyUser.getCompanyId());
            invGroup.setBranchId(tobyUser.getBranchId());

        }
        return invGroup;

    }

    public InvGroupDTO mapToDTO(InvGroup invGroup, TobyUser tobyUser) {

        InvGroupDTO invGroupDTO = new InvGroupDTO();

        invGroupDTO.setId(invGroup.getId());
        invGroupDTO.setCreatedDate(invGroup.getCreationDate());
        invGroupDTO.setCode(invGroup.getCode());
        invGroupDTO.setIndex(invGroup.getId());
        invGroupDTO.setLevel(invGroup.getLevel());
        invGroupDTO.setIsDeleted(invGroup.getIsDeleted());

        if (invGroup.getParent() != null) {
            InvGroupDTO group = new InvGroupDTO();
            group.setId(invGroup.getParent().getId());
            group.setName(invGroup.getParent().getName());
            group.setCode(invGroup.getParent().getCode());
            invGroupDTO.setParent(group);

        }
        invGroupDTO.setName(invGroup.getName());
        invGroupDTO.setCreatedBy(invGroup.getCreatedBy() != null ? invGroup.getCreatedBy().getId() : null);
        invGroupDTO.setCreatedDate(invGroup.getCreationDate());
        invGroupDTO.setBranchId(invGroup.getBranchId().getId() != null ? invGroup.getBranchId().getId() : null);
        invGroupDTO.setCompanyId(invGroup.getCompanyId() != null ? invGroup.getCompanyId().getId() : null);

        return invGroupDTO;
    }

    public List<InvGroupDTO> mapToDTOList(List<InvGroup> invGroupList, TobyUser tobyUser) {
        List<InvGroupDTO> invGroupDTOList = new ArrayList<>();
        for (InvGroup invGroup : invGroupList) {
            invGroupDTOList.add(mapToDTO(invGroup, tobyUser));
        }
        return invGroupDTOList;
    }

    @Override
    public List<InvGroupDTO> getinvGroupDTOList(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvGroup> details = dao.findListByQuery("SELECT e FROM InvGroup e where e.branchId.id =:branchId and ( e.isDeleted != 1 or e.isDeleted is NULL )", params);
        return mapToDTOList(details, tobyUser);
    }
    
    

    @Override
    public InvGroupDTO saveInvGroupDTO(InvGroupDTO invGroupDTO, TobyUser tobyUser) {
        try {

            invGroupDTO.setMsg(null);
            String error = genralValidate(tobyUser, invGroupDTO);
            if (error.isEmpty()) {
                InvGroup invGroup = dao.updateEntity(mapToEntity(invGroupDTO, tobyUser));
                invGroupDTO = mapToDTO(invGroup, tobyUser);
            } else {
                invGroupDTO.setMsg(error);
            }
            return invGroupDTO;
        } catch (Exception e) {
        }
        return null;
    }

    public String genralValidate(TobyUser tobyUser, InvGroupDTO invGroupDTO) {
        StringBuilder errorMessage = new StringBuilder();
        if (!validateName(tobyUser, invGroupDTO.getId(), invGroupDTO.getName())) {
            errorMessage.append("الاسم موجود");

        }
        if (!validateCode(tobyUser, invGroupDTO.getId(), invGroupDTO.getCode())) {
            errorMessage.append("الكود موجود");
        }

        return errorMessage.toString();
    }

    @Override
    public Boolean validateName(TobyUser tobyUser, Integer invGroupid, String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());

        params.put("name", name);
        StringBuilder querry = new StringBuilder();
        querry.append("SELECT i FROM InvGroup i where i.branchId.id = :branchId  and i.name = :name");
        if (invGroupid != null) {
            params.put("invGroupid", invGroupid);
            querry.append(" and i.id != :invGroupid");
        }
        List<InvGroup> invGroupList = dao.findListByQuery(querry.toString(), params);
        if (invGroupList.isEmpty()) {
            return true;
        } else {

            return false;
        }
    }

    @Override
    public Boolean validateCode(TobyUser tobyUser, Integer invGroupid, Integer code) {
      
        StringBuilder quary = new StringBuilder();
        
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("code", code);
        StringBuilder querry = new StringBuilder();
        querry.append("SELECT e FROM InvGroup e where e.branchId.id = :branchId  and (e.code = :code)");
        if (invGroupid != null) {
            params.put("invGroupid", invGroupid);
            querry.append("and e.id != :invGroupid");
        }
        List<InvGroup> invGroupList = dao.findListByQuery(querry.toString(), params);
        if (invGroupList.isEmpty()) {
            return true;
        } else {

            return false;
        }

    }
    
    @Override
    public InvGroup getInvGroupBySerial(Integer branchId, Integer code) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("code", code);
        List<InvGroup> invGroupList = dao.findListByQuery("SELECT e FROM InvGroup e WHERE e.branchId.id = :branchId AND e.code = :code ORDER BY e.code", params);
        return invGroupList.get(0);
    }

}

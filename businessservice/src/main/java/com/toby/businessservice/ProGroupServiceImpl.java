/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.ProGroupDTO;
import com.toby.entity.Branch;
import com.toby.entity.ProGroup;
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
public class ProGroupServiceImpl implements ProGroupService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ProGroup> getallProGroupByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ProGroup> proGroupList = dao.findListByQuery("SELECT e FROM ProGroup e WHERE e.branchId.id = :branchId ORDER BY e.code", params);
        List<ProGroup> list = improvePerformane(proGroupList);
        return list;
    }

    @Override
    public List<ProGroup> getAllProGroups() {
        return dao.findAll(ProGroup.class);
    }

    @Override
    public void deleteProGroup(ProGroup proGroup) {
        dao.deleteEntity(proGroup);
    }

    @Override
    public ProGroupDTO deleteProGroupDTO(ProGroupDTO proGroupDTO, TobyUser tobyUser) {
        ProGroup proGroup = mapToEntity(proGroupDTO, tobyUser);
        proGroup.setIsDeleted(1);
        proGroup = dao.updateEntity(proGroup);
        return mapToDTO(proGroup, tobyUser);
        
    }

    @Override
    public ProGroup findProGroup(Integer id) {
        return dao.findEntityById(ProGroup.class, id);
    }

    @Override
    public ProGroup updateProGroup(ProGroup proGroup) {
        return dao.updateEntity(proGroup);
    }

    @Override
    public ProGroup addProGroup(ProGroup proGroup) {
        return dao.updateEntity(proGroup);
    }

    @Override
    public ProGroupDTO findProGroupDTO(Integer id, TobyUser tobyUser) {
        ProGroup proGroup = dao.findEntityById(ProGroup.class, id);
        return mapToDTO(proGroup, tobyUser);
    }

    @Override
    public List<ProGroup> getallProGroupByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(ProGroup.class, companyId);
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(proGroup.id) FROM ProGroup proGroup ");
        return maxId;
    }

    @Override
    public List<ProGroup> getProGroupForUser(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ProGroupDTO findProGroupDTO(Integer id) {
        ProGroup proGroup = dao.findEntityById(ProGroup.class, id);
        return mapToDTO(proGroup, null);
    }

    @Override
    public List<ProGroupDTO> getallProGroupByBranchIdDTO(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<ProGroup> proGroupList = dao.findListByQuery("SELECT e FROM ProGroup e WHERE e.branchId.id = :branchId ORDER BY e.code", params);
        return mapToDTOList(proGroupList, tobyUser);
    }

    @Override
    public List<ProGroup> getSubProGroupByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ProGroup> proGroupList = dao.findListByQuery("SELECT e FROM ProGroup e WHERE  e.id in (select ii.groupId.id from InvItem ii where ii.branchId.id = :branchId)  ORDER BY e.code", params);
        return proGroupList;
    }

    @Override
    public List<ProGroup> getSubProGroupByBranchIdPer(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ProGroup> proGroupList = dao.findListByQuery("SELECT e FROM ProGroup e WHERE  e.id in (select ii.groupId.id from InvItem ii where ii.branchId.id = :branchId)  ORDER BY e.code", params);
        List<ProGroup> list = improvePerformane(proGroupList);
        return list;
    }

    private List<ProGroup> improvePerformane(List<ProGroup> proGroupList) {
        List<ProGroup> list = new ArrayList<>();
        for (ProGroup group : proGroupList) {
            ProGroup proGroup = new ProGroup();
            proGroup.setId(group.getId());
            proGroup.setName(group.getName());
            proGroup.setCode(group.getCode());
            proGroup.setLevel(group.getLevel());
            list.add(proGroup);
        }
        return list;
    }

//    @Override
//    public List<ProGroup> getProGroupByGroupId(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean) {
//
//        Map<String, Object> params = new HashMap<>();
//
//        params.put("branchId", ItemMainDataByGroupSearchBean.getBranchId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("SELECT e FROM ProGroup e WHERE e.branchId.id = :branchId");
//
//        stringBuilder.append(appendGroup(ItemMainDataByGroupSearchBean, params));
//
//        stringBuilder.append(" ORDER BY e.code");
//        List<ProGroup> proGroupList = dao.findListByQuery(stringBuilder.toString(), params);
//        return proGroupList;
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
    public List<ProGroupDTO> getProGroupByBranchId(TobyUser tobyUser, Integer proGroupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("proGroupId", proGroupId);
        List<ProGroup> proGroupList = dao.findListByQuery("SELECT e FROM ProGroup e WHERE e.branchId.id = :branchId AND e.id != :proGroupId AND (e.parent.id != :proGroupId OR e.parent IS NULL)  ORDER BY e.code", params);
        return mapToDTOList(proGroupList, tobyUser);
    }

    @Override
    public List<ProGroup> getProGroupFromToByBranchId(Integer branchId, ProGroup group1, ProGroup group2) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e FROM ProGroup e WHERE e.branchId.id = :branchId ";
        stringBuilder.append(query);
        searchForGroup(stringBuilder, group1, group2, params);
        stringBuilder.append(" ORDER BY e.code");
        List<ProGroup> proGroupList = dao.findListByQuery(stringBuilder.toString(), params);
        return proGroupList;
    }

    public void searchForGroup(StringBuilder stringBuilder, ProGroup group1, ProGroup group2, Map<String, Object> params) {
        if (group1 != null) {
            params.put("proGroupId1", group1.getId());
            stringBuilder.append(" AND e.id >= :proGroupId1 ");
        }
        if (group2 != null) {
            params.put("proGroupId2", group2.getId());
            stringBuilder.append(" AND e.id <= :proGroupId2 ");
        }
    }

    @Override
    public List<ProGroup> getProGroupsRootsByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ProGroup> proGroupList = dao.findListByQuery("SELECT e FROM ProGroup e WHERE e.branchId.id = :branchId AND e.id not in (select g.parent.id  FROM ProGroup g WHERE g.branchId.id = :branchId AND g.parent is not null) AND e.parent is not null ORDER BY e.code", params);
        List<ProGroup> list = improvePerformane(proGroupList);
        return list;
    }

    //@param id may be null if you want all childs
    @Override
    public List<ProGroup> getAllSubProGroupByBranchId(Integer id, Integer branchId) {
        List<ProGroup> proGroups = new ArrayList<>();
        if (id != null) {
//            ProGroup ig = findProGroupDTO(id);
//            proGroups.add(ig);
        } else {
            proGroups = getProGroupsRootsByBranchId(branchId);
        }

        List<ProGroup> childrens = new ArrayList<>();
        if (proGroups != null && !proGroups.isEmpty()) {
            for (ProGroup group : proGroups) {
                if (group != null && group.getId() != null) {

                }
            }
        }
        return childrens;
    }

    @Override
    public List<ProGroup> getAllSubProGroupByGroupsAndBranchId(List<ProGroup> proGroups, Integer branchId) {

        List<ProGroup> childrens = new ArrayList<>();
        if (proGroups != null && !proGroups.isEmpty()) {
            for (ProGroup group : proGroups) {
                if (group != null && group.getId() != null) {

                }
            }
        }
        return childrens;
    }

    @Override
    public List<ProGroup> getProGroupByGroupId(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean) {

        Map<String, Object> params = new HashMap<>();

        params.put("branchId", ItemMainDataByGroupSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT e FROM ProGroup e WHERE e.branchId.id = :branchId");

        stringBuilder.append(appendGroup(ItemMainDataByGroupSearchBean, params));

        stringBuilder.append(" ORDER BY e.code");
        List<ProGroup> proGroupList = dao.findListByQuery(stringBuilder.toString(), params);
        return proGroupList;
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

    public ProGroup mapToEntity(ProGroupDTO proGroupDTO, TobyUser tobyUser) {
        ProGroup proGroup = new ProGroup();
        proGroup.setId(proGroupDTO.getId());
        proGroup.setIsDeleted(proGroupDTO.getIsDeleted());

        proGroup.setCode(proGroupDTO.getCode());
        proGroup.setLevel(proGroupDTO.getLevel());

        proGroup.setName(proGroupDTO.getName());
        if (proGroupDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(proGroupDTO.getCreatedBy());
            proGroup.setCreatedBy(user);
            proGroup.setCreationDate(proGroupDTO.getCreatedDate());
            proGroup.setModifiedBy(tobyUser);
            proGroup.setModificationDate(new Date());
            if (proGroupDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(proGroupDTO.getCompanyId());
                proGroup.setCompanyId(company);
            }
            if (proGroupDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(proGroupDTO.getBranchId());
                proGroup.setBranchId(branch);
            }
        } else {
            proGroup.setCreatedBy(tobyUser);
            proGroup.setCreationDate(new Date());
            proGroup.setCompanyId(tobyUser.getCompanyId());
            proGroup.setBranchId(tobyUser.getBranchId());

        }
        return proGroup;

    }

    public ProGroupDTO mapToDTO(ProGroup proGroup, TobyUser tobyUser) {

        ProGroupDTO proGroupDTO = new ProGroupDTO();

        proGroupDTO.setId(proGroup.getId());
        proGroupDTO.setCreatedDate(proGroup.getCreationDate());
        proGroupDTO.setCode(proGroup.getCode());
        proGroupDTO.setIndex(proGroup.getId());
        proGroupDTO.setLevel(proGroup.getLevel());
        proGroupDTO.setIsDeleted(proGroup.getIsDeleted());

        proGroupDTO.setName(proGroup.getName());
        proGroupDTO.setCreatedBy(proGroup.getCreatedBy() != null ? proGroup.getCreatedBy().getId() : null);
        proGroupDTO.setCreatedDate(proGroup.getCreationDate());
        proGroupDTO.setBranchId(proGroup.getBranchId().getId() != null ? proGroup.getBranchId().getId() : null);
        proGroupDTO.setCompanyId(proGroup.getCompanyId() != null ? proGroup.getCompanyId().getId() : null);

        return proGroupDTO;
    }

    public List<ProGroupDTO> mapToDTOList(List<ProGroup> proGroupList, TobyUser tobyUser) {
        List<ProGroupDTO> proGroupDTOList = new ArrayList<>();
        for (ProGroup proGroup : proGroupList) {
            proGroupDTOList.add(mapToDTO(proGroup, tobyUser));
        }
        return proGroupDTOList;
    }

    @Override
    public List<ProGroupDTO> getproGroupDTOList(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<ProGroup> details = dao.findListByQuery("SELECT e FROM ProGroup e where e.branchId.id =:branchId and ( e.isDeleted != 1 or e.isDeleted is NULL )", params);
        return mapToDTOList(details, tobyUser);
    }

    @Override
    public ProGroupDTO saveProGroupDTO(ProGroupDTO proGroupDTO, TobyUser tobyUser) {
        try {

            proGroupDTO.setMsg(null);
            String error = genralValidate(tobyUser, proGroupDTO);
            if (error.isEmpty()) {
                ProGroup proGroup = dao.updateEntity(mapToEntity(proGroupDTO, tobyUser));
                proGroupDTO = mapToDTO(proGroup, tobyUser);
            } else {
                proGroupDTO.setMsg(error);
            }
            return proGroupDTO;
        } catch (Exception e) {
        }
        return null;
    }

    public String genralValidate(TobyUser tobyUser, ProGroupDTO proGroupDTO) {
        StringBuilder errorMessage = new StringBuilder();
        if (!validateName(tobyUser, proGroupDTO.getId(), proGroupDTO.getName())) {
            errorMessage.append("الاسم موجود");

        }
        if (!validateCode(tobyUser, proGroupDTO.getId(), proGroupDTO.getCode())) {
            errorMessage.append("الكود موجود");
        }

        return errorMessage.toString();
    }

    @Override
    public Boolean validateName(TobyUser tobyUser, Integer proGroupid, String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());

        params.put("name", name);
        StringBuilder querry = new StringBuilder();
        querry.append("SELECT i FROM ProGroup i where i.branchId.id = :branchId  and i.name = :name");
        if (proGroupid != null) {
            params.put("proGroupid", proGroupid);
            querry.append(" and i.id != :proGroupid");
        }
        List<ProGroup> proGroupList = dao.findListByQuery(querry.toString(), params);
        if (proGroupList.isEmpty()) {
            return true;
        } else {

            return false;
        }
    }

    @Override
    public Boolean validateCode(TobyUser tobyUser, Integer proGroupid, Integer code) {
      
        StringBuilder quary = new StringBuilder();
        
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("code", code);
        StringBuilder querry = new StringBuilder();
        querry.append("SELECT e FROM ProGroup e where e.branchId.id = :branchId  and (e.code = :code)");
        if (proGroupid != null) {
            params.put("proGroupid", proGroupid);
            querry.append("and e.id != :proGroupid");
        }
        List<ProGroup> proGroupList = dao.findListByQuery(querry.toString(), params);
        if (proGroupList.isEmpty()) {
            return true;
        } else {

            return false;
        }

    }
    
    @Override
    public ProGroup getProGroupBySerial(Integer branchId, Integer code) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("code", code);
        List<ProGroup> proGroupList = dao.findListByQuery("SELECT e FROM ProGroup e WHERE e.branchId.id = :branchId AND e.code = :code ORDER BY e.code", params);
        return proGroupList.get(0);
    }

}

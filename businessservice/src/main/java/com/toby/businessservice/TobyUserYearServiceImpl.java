/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.GlYearDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlYear;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUserRole;
import com.toby.entity.TobyUserYear;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class TobyUserYearServiceImpl implements TobyUserYearService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteTobyUserYear(TobyUserYear tobyUserYear) {
        dao.deleteEntity(tobyUserYear);
    }

    @Override
    public List<TobyUserYear> getAllTobyUserYear() {
        return dao.findAll(TobyUserYear.class);
    }

    @Override
    public TobyUserYear addTobyUserYear(TobyUserYear tobyUserYear) {
        return dao.updateEntity(tobyUserYear);
    }

    @Override
    public TobyUserYear findTobyUserYear(Integer tobyUserYearId) {
        return dao.findEntityById(TobyUserYear.class, tobyUserYearId);
    }

    @Override
    public TobyUserYear updateTobyUserYear(TobyUserYear tobyUserYear) {
        return dao.updateEntity(tobyUserYear);
    }

    @Override
    public List<TobyUserYear> findTobyUserYearByUserId(Integer userid) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userid);
        List<TobyUserYear> userRoles = dao.findListByQuery("SELECT u FROM TobyUserRole u "
                + " WHERE u.id =:userId"
                + " )", params);
        return userRoles;
    }

    @Override
    public TobyUserYear findTobyUserYearByUserIdAndCompany(Integer userid, Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userid);
        params.put("companyId", companyId);
        List<TobyUserYear> userRoles = dao.findListByQuery("SELECT u FROM TobyUserRole u "
                + " WHERE u.userId.id =:userId AND u.branchId.id = :branchId", params);
        if (userRoles == null || userRoles.isEmpty()) {
            return null;
        }
        return userRoles.get(0);
    }

    @Override
    public List<TobyUserYear> findTobyUserYearByCompany(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        List<TobyUserYear> userRoles = dao.findListByQuery("SELECT u FROM TobyUserRole u "
                + " WHERE u.branchId.id = :branchId", params);
        return userRoles;
    }

    @Override
    public List<GlYear> findYearByUserId(Integer userId) {
        List<GlYear> yearList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        List<TobyUserYear> tobyUserYearList = dao.findListByQuery("SELECT u FROM TobyUserYear u "
                + " WHERE u.userId.id =:userId"
                + " )", params);
        if (tobyUserYearList.isEmpty()) {
            return null;
        } else {
            for (TobyUserYear tobyUserYear : tobyUserYearList) {
                yearList.add(tobyUserYear.getYearId());
            }
        }
        return yearList;
    }

    @Override
    public List<GlYear> findYearByUserId(Integer userId, Branch branchId) {
        List<GlYear> yearList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId.getId());
        Map<Integer, GlYear> glYearMap = new HashMap<>();

        List<TobyUserYear> tobyUserYearList = dao.findListByQuery("SELECT u FROM TobyUserYear u  WHERE u.branchId.id = :branchId AND u.userId.id = :userId", params);
        if (tobyUserYearList.isEmpty()) {
            return null;
        } else {
            for (TobyUserYear tobyUserYear : tobyUserYearList) {
                if (!glYearMap.containsKey(tobyUserYear.getYearId().getId())) {
                    glYearMap.put(tobyUserYear.getYearId().getId(), tobyUserYear.getYearId());
                    yearList.add(tobyUserYear.getYearId());
                }
            }
        }
        return yearList;
    }

    @Override
    public List<GlYear> findYearByUserId(Integer userId, TobyCompany companyId) {
        List<GlYear> yearList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("companyId", companyId.getId());
        List<TobyUserYear> tobyUserYearList = dao.findListByQuery("SELECT u FROM TobyUserYear u "
                + " WHERE u.userId.id =:userId AND u.companyId.id = :companyId"
                + " )", params);
        if (tobyUserYearList.isEmpty()) {
            return null;
        } else {
            for (TobyUserYear tobyUserYear : tobyUserYearList) {
                yearList.add(tobyUserYear.getYearId());
            }
        }
        return yearList;
    }

    @Override
    public void DeleteYear(TobyUserRole userRole, GlYear year) {
        String sql = "delete from TobyUserYear e  where e.branchId.id  = " + userRole.getBranchId().getId() + " AND e.userId.id = " + userRole.getUserId().getId() + " AND e.yearId.id = " + year.getId();
        dao.executeDeleteQuery(sql);
    }

    @Override
    public List<TobyUserYear> findTobyUserYearByUserIdAndBranchId(Integer userId, Branch branch) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branch.getId());
        List<TobyUserYear> tobyUserYearList = dao.findListByQuery("SELECT u FROM TobyUserYear u  WHERE u.branchId.id = :branchId AND u.userId.id = :userId", params);
        if (tobyUserYearList.isEmpty()) {
            return null;
        }
        return tobyUserYearList;
    }

    @Override
    public void deleteTobyUserYear(List<TobyUserYear> tobyUserYearList) {
        dao.deleteEntity(tobyUserYearList);
    }

    @Override
    public List<GlYearDTO> findYearDTOByUserId(Integer userId, Branch branchId) {
        List<GlYearDTO> yearList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId.getId());
        Map<Integer, GlYearDTO> glYearMap = new HashMap<>();

        List<TobyUserYear> tobyUserYearList = dao.findListByQuery("SELECT u FROM TobyUserYear u  WHERE u.branchId.id = :branchId AND u.userId.id = :userId", params);
        if (tobyUserYearList.isEmpty()) {
            return null;
        } else {
            for (TobyUserYear tobyUserYear : tobyUserYearList) {
                if (!glYearMap.containsKey(tobyUserYear.getYearId().getId())) {
                    GlYearDTO glYeardto = new GlYearDTO();
                    glYeardto = mapTODTO(tobyUserYear.getYearId(), false);
                    glYearMap.put(tobyUserYear.getYearId().getId(), glYeardto);
//                    yearList.add(tobyUserYear.getYearId());
                    yearList.add(glYeardto);
                }
            }
        }
        return yearList;
    }

    private void returnListDTO(List<GlYear> list, List<GlYearDTO> dTOList) {
        for (GlYear glYear : list) {
            dTOList.add(mapTODTO(glYear, false));
        }
    }

    public GlYearDTO mapTODTO(GlYear glYear, Boolean check) {
        GlYearDTO dTO = new GlYearDTO();
        initMapToDTO(dTO, glYear);
        if (check) {

        }
        return dTO;
    }

    private void initMapToDTO(GlYearDTO dTO, GlYear glYear) {

        dTO.setYear(glYear.getYear());
        dTO.setDateFrom(glYear.getDateFrom());
        dTO.setDateTo(glYear.getDateTo());
        dTO.setName(glYear.getName());
        dTO.setOpenning(glYear.getOpenning());
        dTO.setIsDefault(glYear.getIsDefault());
        dTO.setBranchId(glYear.getBranchId().getId());

        dTO.setId(glYear.getId());
        dTO.setCreatedDate(glYear.getCreationDate());
        dTO.setCreatedBy(glYear.getCreatedBy().getId());
        dTO.setIndex(glYear.getId());
    }

}

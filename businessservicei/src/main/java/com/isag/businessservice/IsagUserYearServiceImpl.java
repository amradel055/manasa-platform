/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GlYearDTO;
import com.isag.entity.Branch;
import com.isag.entity.GlYear;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUserRole;
import com.isag.entity.IsagUserYear;
import java.util.ArrayList;
import java.util.Date;
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
public class IsagUserYearServiceImpl implements IsagUserYearService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteIsagUserYear(IsagUserYear isagUserYear) {
        dao.deleteEntity(isagUserYear);
    }

    @Override
    public List<IsagUserYear> getAllIsagUserYear() {
        return dao.findAll(IsagUserYear.class);
    }

    @Override
    public IsagUserYear addIsagUserYear(IsagUserYear isagUserYear) {
        return dao.updateEntity(isagUserYear);
    }

    @Override
    public IsagUserYear findIsagUserYear(Integer isagUserYearId) {
        return dao.findEntityById(IsagUserYear.class, isagUserYearId);
    }

    @Override
    public IsagUserYear updateIsagUserYear(IsagUserYear isagUserYear) {
        return dao.updateEntity(isagUserYear);
    }

    @Override
    public List<IsagUserYear> findIsagUserYearByUserId(Integer userid) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userid);
        List<IsagUserYear> userRoles = dao.findListByQuery("SELECT u FROM IsagUserRole u "
                + " WHERE u.id =:userId"
                + " )", params);
        return userRoles;
    }

    @Override
    public IsagUserYear findIsagUserYearByUserIdAndCompany(Integer userid, Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userid);
        params.put("companyId", companyId);
        List<IsagUserYear> userRoles = dao.findListByQuery("SELECT u FROM IsagUserRole u "
                + " WHERE u.userId.id =:userId AND u.branchId.id = :branchId", params);
        if (userRoles == null || userRoles.isEmpty()) {
            return null;
        }
        return userRoles.get(0);
    }

    @Override
    public List<IsagUserYear> findIsagUserYearByCompany(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        List<IsagUserYear> userRoles = dao.findListByQuery("SELECT u FROM IsagUserRole u "
                + " WHERE u.branchId.id = :branchId", params);
        return userRoles;
    }

    @Override
    public List<GlYear> findYearByUserId(Integer userId) {
        List<GlYear> yearList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        List<IsagUserYear> isagUserYearList = dao.findListByQuery("SELECT u FROM IsagUserYear u "
                + " WHERE u.userId.id =:userId"
                + " )", params);
        if (isagUserYearList.isEmpty()) {
            return null;
        } else {
            for (IsagUserYear isagUserYear : isagUserYearList) {
                yearList.add(isagUserYear.getYearId());
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

        List<IsagUserYear> isagUserYearList = dao.findListByQuery("SELECT u FROM IsagUserYear u  WHERE u.branchId.id = :branchId AND u.userId.id = :userId", params);
        if (isagUserYearList.isEmpty()) {
            return null;
        } else {
            for (IsagUserYear isagUserYear : isagUserYearList) {
                if (!glYearMap.containsKey(isagUserYear.getYearId().getId())) {
                    glYearMap.put(isagUserYear.getYearId().getId(), isagUserYear.getYearId());
                    yearList.add(isagUserYear.getYearId());
                }
            }
        }
        return yearList;
    }

    @Override
    public List<GlYear> findYearByUserId(Integer userId, IsagCompany companyId) {
        List<GlYear> yearList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("companyId", companyId.getId());
        List<IsagUserYear> isagUserYearList = dao.findListByQuery("SELECT u FROM IsagUserYear u "
                + " WHERE u.userId.id =:userId AND u.companyId.id = :companyId"
                + " )", params);
        if (isagUserYearList.isEmpty()) {
            return null;
        } else {
            for (IsagUserYear isagUserYear : isagUserYearList) {
                yearList.add(isagUserYear.getYearId());
            }
        }
        return yearList;
    }

    @Override
    public void DeleteYear(IsagUserRole userRole, GlYear year) {
        String sql = "delete from IsagUserYear e  where e.branchId.id  = " + userRole.getBranchId().getId() + " AND e.userId.id = " + userRole.getUserId().getId() + " AND e.yearId.id = " + year.getId();
        dao.executeDeleteQuery(sql);
    }

    @Override
    public List<IsagUserYear> findIsagUserYearByUserIdAndBranchId(Integer userId, Branch branch) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branch.getId());
        List<IsagUserYear> isagUserYearList = dao.findListByQuery("SELECT u FROM IsagUserYear u  WHERE u.branchId.id = :branchId AND u.userId.id = :userId", params);
        if (isagUserYearList.isEmpty()) {
            return null;
        }
        return isagUserYearList;
    }

    @Override
    public void deleteIsagUserYear(List<IsagUserYear> isagUserYearList) {
        dao.deleteEntity(isagUserYearList);
    }
    
    @Override
    public List<GlYearDTO> findYearDTOByUserId(Integer userId, Branch branchId) {
        List<GlYearDTO> yearList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId.getId());
        Map<Integer, GlYearDTO> glYearMap = new HashMap<>();

        List<IsagUserYear> isagUserYearList = dao.findListByQuery("SELECT u FROM IsagUserYear u  WHERE u.branchId.id = :branchId AND u.userId.id = :userId", params);
        if (isagUserYearList.isEmpty()) {
            return null;
        } else {
            for (IsagUserYear isagUserYear : isagUserYearList) {
                if (!glYearMap.containsKey(isagUserYear.getYearId().getId())) {
                  GlYearDTO glYeardto = new GlYearDTO();
                  glYeardto = mapTODTO(isagUserYear.getYearId(), false);
                    glYearMap.put(isagUserYear.getYearId().getId(), glYeardto);
//                    yearList.add(isagUserYear.getYearId());
                      yearList.add(glYeardto);
                }
            }
        }
        return yearList;
    }
    
      private void returnListDTO(List<GlYear> list, List<GlYearDTO> dTOList) {
        for (GlYear glYear : list) {
            dTOList.add(mapTODTO(glYear,false));
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

    @Override
    public GlYearDTO findYearDTOByUserIdAndDate(Integer userId, Branch branchId, Date date) {
        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId.getId());
         params.put("date",date); 
        IsagUserYear isagUserYear = dao.findEntityByQuery("SELECT u FROM IsagUserYear u  WHERE u.branchId.id = :branchId AND u.userId.id = :userId AND  :date BETWEEN u.yearId.dateFrom AND u.yearId.dateTo", params);
        
       if(isagUserYear != null){
        return mapTODTO(isagUserYear.getYearId(), false); 
       }
        return null;
    }
}

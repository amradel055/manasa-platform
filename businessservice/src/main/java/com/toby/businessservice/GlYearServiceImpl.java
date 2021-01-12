/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.GlYear;
import java.text.SimpleDateFormat;
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
public class GlYearServiceImpl implements GlYearService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteYear(GlYear glYear) {
        dao.deleteEntity(glYear);
    }

    @Override
    public List<GlYear> getAllYear() {
        return dao.findAll(GlYear.class);
    }

    @Override
    public GlYear addYear(GlYear glYear) {
        return dao.updateEntity(glYear);
    }

    @Override
    public GlYear findYear(Integer yearId) {
        return dao.findEntityById(GlYear.class, yearId);
    }

    @Override
    public GlYear updateYear(GlYear glYear) {
        if (glYear.getIsDefault()) {
            GlYear defaultYear = getDefaultYearsByBranchId(glYear.getBranchId().getId(), glYear);
            if (defaultYear != null) {
                defaultYear.setIsDefault(Boolean.FALSE);
                dao.updateEntity(defaultYear);
            }
        }
        return dao.updateEntity(glYear);
    }

    @Override
    public List<GlYear> getALLGlyearByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT e FROM GlYear e WHERE e.companyId.id =:companyId", params);
    }

    @Override
    public List<GlYear> getALLGlyearByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM GlYear e WHERE e.branchId.id =:branchId", params);
    }

    @Override
    public GlYear getDefaultYearsByBranchId(Integer branchId, GlYear newGLYear) {
        Map<String, Object> params = new HashMap<>();
        List<GlYear> yearList;
        params.put("branchId", branchId);
        if (newGLYear.getId() != null) {
            params.put("id", newGLYear.getId());
            yearList = dao.findListByQuery("SELECT e FROM GlYear e WHERE e.branchId.id =:branchId AND e.isDefault = true AND e.id != :id", params);
        } else {
            yearList = dao.findListByQuery("SELECT e FROM GlYear e WHERE e.branchId.id =:branchId AND e.isDefault = true", params);
        }

        if (yearList != null && yearList.size() > 0) {
            return yearList.get(0);
        }
        return null;
    }

    @Override
    public GlYear getDefaultYearsByBranchIdByUser(Integer userId,Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        List<GlYear> yearList;
        params.put("userId", userId);
        params.put("branchId", branchId);
        yearList = dao.findListByQuery("SELECT e FROM GlYear e left join TobyUserYear iu WHERE e.id = iu.yearId.id and e.isDefault = true and iu.userId.id = :userId and e.branchId.id = :branchId ", params);
        if (yearList != null && yearList.size() > 0) {
            return yearList.get(0);
        }
        return null;
    }

    @Override
    public List<GlYear> getSimilarYearsByBranchId(Integer branchId, GlYear newGLYear) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("year", newGLYear.getYear());
        if (newGLYear.getId() != null) {
            params.put("yearId", newGLYear.getId());
            return dao.findListByQuery("SELECT e FROM GlYear e WHERE e.branchId.id =:branchId AND e.year =:year AND e.id != :yearId", params);
        } else {
            return dao.findListByQuery("SELECT e FROM GlYear e WHERE e.branchId.id =:branchId AND e.year =:year", params);
        }
    }

    @Override
    public List<GlYear> checkDateWithinDate(Integer branchId, GlYear newGLYear) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder queryBuilder = new StringBuilder();
        appendGlYearDateFromForQueryBuilder(queryBuilder, params, newGLYear.getDateFrom());
        appendGlYearDateToForQueryBuilder(queryBuilder, params, newGLYear.getDateTo());

        String query;
        if (newGLYear.getId() != null) {
            params.put("yearId", newGLYear.getId());
            query = "SELECT e FROM GlYear e WHERE e.branchId.id =:branchId AND e.id != :yearId " + queryBuilder.toString();
        } else {
            query = "SELECT e FROM GlYear e WHERE e.branchId.id =:branchId " + queryBuilder.toString();
        }
        List<GlYear> glYears = dao.findListByQuery(query, params);
        return glYears;
    }

    public void appendGlYearDateFromForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, Date startDate) {
        if (startDate != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            queryBuilder.append("AND ('").append(formatDateFrom).append("' between e.dateFrom AND e.dateTo ");
        }
    }

    public void appendGlYearDateToForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, Date lastDate) {
        if (lastDate != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(lastDate);
            queryBuilder.append("OR '").append(formatDateFrom).append("' between e.dateFrom AND e.dateTo ) ");
        }
    }

    @Override
    public List<GlYear> findNextGlYear(Integer branchId, GlYear gLYear) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder queryBuilder = new StringBuilder();

        if (gLYear != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(gLYear.getDateTo());
            queryBuilder.append("AND e.dateFrom > '").append(formatDateTo).append("'");
        }

        queryBuilder.append(" Order By e.dateFrom");
        String query = "SELECT e FROM GlYear e WHERE e.branchId.id =:branchId AND (e.openning = null OR e.openning = 0 ) " + queryBuilder.toString();

        List<GlYear> glYears = dao.findListByQuery(query, params);
        return glYears;
    }
    
    @Override
    public List<GlYear> findGlYearWithCode(Integer branchId, Integer year) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("year", year);
        String query = "SELECT e FROM GlYear e WHERE e.branchId.id =:branchId AND e.year = :year ";

        List<GlYear> glYears = dao.findListByQuery(query, params);
        return glYears;
    }
    
    @Override
    public List<GlYear> findGlYearByDateBetween(Integer branchId, Date date) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        
        StringBuilder builder = new StringBuilder();

        String query = "SELECT e FROM GlYear e WHERE e.branchId.id =:branchId  ";
        builder.append(query);
        if (date != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(date);
            builder.append(" and e.dateFrom <= '").append(d3).append("'");
            builder.append(" and e.dateTo >= '").append(d3).append("'");
        }

        List<GlYear> glYears = dao.findListByQuery(builder.toString(), params);
        return glYears;
    }

}

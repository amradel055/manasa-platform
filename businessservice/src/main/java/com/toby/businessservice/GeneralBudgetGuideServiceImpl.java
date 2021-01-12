/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.GeneralBudgetReportSearchBean;
import com.toby.core.GenericDAO;
import com.toby.define.GroupItemEnum;
import com.toby.entity.GeneralBudgetGuide;
import java.util.ArrayList;
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
public class GeneralBudgetGuideServiceImpl implements GeneralBudgetGuideService {

    @EJB
    private GenericDAO dao;

    @Override

    public List<GeneralBudgetGuide> getAllGeneralBudgetGuide() {
        return dao.findAll(GeneralBudgetGuide.class);
    }

    @Override
    public void deleteGeneralBudgetGuide(GeneralBudgetGuide generalBudgetGuide) {
        dao.deleteEntity(generalBudgetGuide);
    }

    @Override
    public GeneralBudgetGuide updateGeneralBudgetGuide(GeneralBudgetGuide generalBudgetGuide) {
        if (generalBudgetGuide.getId() == null) {
            dao.saveEntity(generalBudgetGuide);
        } else {
            dao.updateEntity(generalBudgetGuide);
        }
        return generalBudgetGuide;
    }

    @Override
    public GeneralBudgetGuide addGeneralBudgetGuide(GeneralBudgetGuide generalBudgetGuide) {
        return dao.updateEntity(generalBudgetGuide);
    }

    @Override
    public GeneralBudgetGuide findGeneralBudgetGuide(Integer id) {
        return dao.findEntityById(GeneralBudgetGuide.class, id);

    }

    @Override
    public List<GeneralBudgetGuide> getAllGeneralBudgetGuideByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(GeneralBudgetGuide.class, companyId);
    }

    @Override
    public Integer getMaxId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GeneralBudgetGuide> getGeneralBudgetGuideForUser(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT userRole.branchId FROM TobyUserRole userRole WHERE userRole.userId.id = :id", params);
    }

    @Override
    public List<GeneralBudgetGuide> getGeneralBudgetGuideByBranchIdAndCode(Integer branchId, String number, Integer generalBudgetGuideId) {
        Map<String, Object> params = new HashMap<>();
        List<GeneralBudgetGuide> generalBudgetGuides = new ArrayList<>();
        String query = null;
        params.put("branchId", branchId);
        params.put("number", number);

        if (generalBudgetGuideId == null) {
            query = "SELECT e FROM GeneralBudgetGuide e WHERE e.branchId.id = :branchId AND e.number = :number";
        } else {
            params.put("generalBudgetGuideId", generalBudgetGuideId);
            query = "SELECT e FROM GeneralBudgetGuide e WHERE e.id != :generalBudgetGuideId AND e.branchId.id = :branchId AND e.number = :number";
        }

        generalBudgetGuides = dao.findListByQuery(query, params);

        return generalBudgetGuides;
    }

    @Override
    public List<GeneralBudgetGuide> getGeneralBudgetGuide(GeneralBudgetReportSearchBean generalBudgetReportSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", generalBudgetReportSearchBean.getCompanyId());
        if (generalBudgetReportSearchBean.getItemName() != null && !generalBudgetReportSearchBean.getItemName().isEmpty()) {
            params.put("name", generalBudgetReportSearchBean.getItemName());
        }
        String sql = "Select gbg FROM GeneralBudgetGuide gbg WHERE gbg.companyId.id = :companyId " + createSql(generalBudgetReportSearchBean).toString();
        return dao.findListByQuery(sql, params);
    }

    private Object createSql(GeneralBudgetReportSearchBean generalBudgetReportSearchBean) {

        StringBuilder sql = new StringBuilder();
        addItemName(sql, generalBudgetReportSearchBean);
        addSerialNumberFrom(sql, generalBudgetReportSearchBean);
        addSerialNumberTo(sql, generalBudgetReportSearchBean);

        return sql;
    }

    public void addItemName(StringBuilder sql, GeneralBudgetReportSearchBean generalBudgetReportSearchBean) {
        if (generalBudgetReportSearchBean.getItemName() != null && !generalBudgetReportSearchBean.getItemName().isEmpty()) {
            sql.append(" and gbg.nameAr Like CONCAT('%',:name ,'%')");
        }
    }

    public void addSerialNumberFrom(StringBuilder sql, GeneralBudgetReportSearchBean generalBudgetReportSearchBean) {
        if (generalBudgetReportSearchBean.getSerialNumberFrom() != null && generalBudgetReportSearchBean.getSerialNumberFrom() >= 0) {
            sql.append(" and gbg.number >= ").append(generalBudgetReportSearchBean.getSerialNumberFrom());
        }
    }

    public void addSerialNumberTo(StringBuilder sql, GeneralBudgetReportSearchBean generalBudgetReportSearchBean) {
        if (generalBudgetReportSearchBean.getSerialNumberTo() != null && generalBudgetReportSearchBean.getSerialNumberTo() >= 0) {
            sql.append(" and gbg.number <= ").append(generalBudgetReportSearchBean.getSerialNumberTo());
        }
    }

    @Override
    public List<GeneralBudgetGuide> getAllGeneralBudgetGuideByBranchId(Integer BranchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", BranchId);

        String sql = "Select gbg FROM GeneralBudgetGuide gbg WHERE gbg.branchId.id = :branchId";
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<GeneralBudgetGuide> getAllGeneralBudgetGuideByForFinancialItemMenuBranchId(Integer BranchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", BranchId);
        String sql = "Select gbg FROM GeneralBudgetGuide gbg WHERE gbg.branchId.id = :branchId AND("
                + " gbg.accGroup = com.toby.define.GroupItemEnum.FIXED_ASSETS OR gbg.accGroup = com.toby.define.GroupItemEnum.CIRCULATED_ASSETS "
                + "OR gbg.accGroup = com.toby.define.GroupItemEnum.SHORT_TERM_LIABILITIES OR gbg.accGroup = com.toby.define.GroupItemEnum.LONG_TERM_LIABILITIES "
                + "OR gbg.accGroup = com.toby.define.GroupItemEnum.PROPERTY_RIGHTS )";
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<GeneralBudgetGuide> getAllGeneralBudgetGuideByForIncomeItemMenuBranchId(Integer BranchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", BranchId);
        String sql = "Select gbg FROM GeneralBudgetGuide gbg WHERE gbg.branchId.id = :branchId AND("
                + " gbg.accGroup = com.toby.define.GroupItemEnum.INCOME OR gbg.accGroup = com.toby.define.GroupItemEnum.EXPENSES )";
        return dao.findListByQuery(sql, params);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.AdminUnitSearchBean;
import com.isag.core.GenericDAO;
import com.isag.entity.GlAdminUnit;
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
public class AdminUnitsServiceImpl implements AdminUnitsService {

    @EJB
    GenericDAO dao;

    @Override
    public List<GlAdminUnit> getAllAdminUnits() {
        return dao.findListByQuery("SELECT au FROM GlAdminUnit au WHERE au.parent IS NULL");
    }

    @Override
    public List<GlAdminUnit> getCompanyAdminUnits(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("id", companyId);
        List<GlAdminUnit> List = dao.findListByQuery("SELECT au FROM GlAdminUnit au WHERE au.parent IS NULL AND au.companyId.id= :id", params);
        return List;
    }

    @Override
    public void deleteAdminUnit(GlAdminUnit selectedAU) {
        dao.deleteEntity(selectedAU);
    }

    @Override
    public GlAdminUnit addAdminUnit(GlAdminUnit newAdminUnit) {
        return dao.updateEntity(newAdminUnit);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<GlAdminUnit> getAllAdminUnitsNodesByCompany(Integer companyId) {
        return dao.findEntityListByCompanyId(GlAdminUnit.class, companyId);
    }

    @Override
    public List<GlAdminUnit> getAllAdminUnitsNodesByBranch(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT au FROM GlAdminUnit au WHERE au.branchId.id=:id", params); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GlAdminUnit> getAllAdminUnitsByCompany(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT au FROM GlAdminUnit au WHERE au.companyId.id=:id ORDER BY au.level ASC", params); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GlAdminUnit> getBranchAdminUnitsRoots(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<GlAdminUnit> List = dao.findListByQuery("SELECT au FROM GlAdminUnit au WHERE au.parent IS NULL AND au.branchId.id= :branchId", params);
        return List;
    }

    @Override
    public List<GlAdminUnit> getAdminUnits(AdminUnitSearchBean adminUnitSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", adminUnitSearchBean.getCompanyId());
        if (adminUnitSearchBean.getUnitName() != null && !adminUnitSearchBean.getUnitName().isEmpty()) {
            params.put("name", adminUnitSearchBean.getUnitName());
        }
        String sql = "Select au FROM GlAdminUnit au WHERE au.companyId.id = :companyId " + createSql(adminUnitSearchBean).toString();

        //+ createSql(adminUnitSearchBean).toString();
        //List<GlAdminUnit> data = dao.findEntityByQuery(sql,params);
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<GlAdminUnit> getAdminUnitsBBranchId(Integer branchId, AdminUnitSearchBean adminUnitSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        if (adminUnitSearchBean.getUnitName() != null && !adminUnitSearchBean.getUnitName().isEmpty()) {
            params.put("name", adminUnitSearchBean.getUnitName());
        }
        String sql = "Select au FROM GlAdminUnit au WHERE au.branchId.id = :branchId " + createSql(adminUnitSearchBean).toString() + " order by au.code";

        //+ createSql(adminUnitSearchBean).toString();
        //List<GlAdminUnit> data = dao.findEntityByQuery(sql,params);
        return dao.findListByQuery(sql, params);
    }

    private Object createSql(AdminUnitSearchBean adminUnitSearchBean) {

        StringBuilder sql = new StringBuilder();
        addAdminUnitName(sql, adminUnitSearchBean);
        addAdminUnitCodeFrom(sql, adminUnitSearchBean);
        addAdminUnitCodeTo(sql, adminUnitSearchBean);

        return sql;
    }

    public void addAdminUnitCodeFrom(StringBuilder sql, AdminUnitSearchBean adminUnitSearchBean) {
        if (adminUnitSearchBean.getUnitCodeFrom() != null && adminUnitSearchBean.getUnitCodeFrom().getId() > 0) {
            sql.append(" and au.id >= ").append(adminUnitSearchBean.getUnitCodeFrom().getId());
        }
    }

    public void addAdminUnitCodeTo(StringBuilder sql, AdminUnitSearchBean adminUnitSearchBean) {
        if (adminUnitSearchBean.getUnitCodeTo() != null && adminUnitSearchBean.getUnitCodeTo().getId() > 0) {
            sql.append(" and au.id <= ").append(adminUnitSearchBean.getUnitCodeTo().getId());
        }
    }

    public void addAdminUnitName(StringBuilder sql, AdminUnitSearchBean adminUnitSearchBean) {
        if (adminUnitSearchBean.getUnitName() != null && !adminUnitSearchBean.getUnitName().isEmpty()) {
            sql.append(" and au.name Like CONCAT('%',:name ,'%')");
        }
    }

    @Override
    public List<GlAdminUnit> getAllAdminUnitsByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer adminUnitCode) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("adminUnitCode", adminUnitCode);
        return dao.findListByQuery("SELECT gau FROM GlAdminUnit gau WHERE gau.branchId.id = :id AND gau.code = :adminUnitCode", params);
    }

    @Override
    public List<GlAdminUnit> getAllAdminUnitsByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer adminUnitCode, Integer adminUnitId) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("adminUnitCode", adminUnitCode);
        params.put("glAccountId", adminUnitId);
        return dao.findListByQuery("SELECT gau FROM GlAdminUnit gau WHERE gau.id != :glAccountId AND gau.branchId.id = :id AND gau.code = :adminUnitCode", params);
    }
}

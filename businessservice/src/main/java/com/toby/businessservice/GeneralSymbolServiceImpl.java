/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.GeneralSymbolSearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.GeneralSymbol;
import com.toby.entity.Symbol;
import com.toby.views.GeneralSymbolView;
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
public class GeneralSymbolServiceImpl implements GeneralSymbolService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<GeneralSymbol> getAllGSymbol() {
        return dao.findAll(GeneralSymbol.class);
    }

    @Override
    public GeneralSymbol addGSymbol(GeneralSymbol gSymbol) {
        return dao.updateEntity(gSymbol);
    }

    @Override
    public GeneralSymbol updateGSymbol(GeneralSymbol gSymbol) {
        return dao.updateEntity(gSymbol);
    }

    @Override
    public void deleteGSymbol(GeneralSymbol gSymbol) {
        dao.deleteEntity(gSymbol);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<GeneralSymbol> getAllGSymbolListByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(GeneralSymbol.class, companyId);
    }

    @Override
    public List<Symbol> getSymbolsForGeneralSymbol(Integer selectedGSymbol, Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("GSymbol", selectedGSymbol);
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT e FROM Symbol e WHERE  e.generalSymbolId.id= :GSymbol AND e.companyId.id= :companyId", params);

    }

    public List<GeneralSymbol> findAllPerant() {
        List<GeneralSymbol> generalSymbols = dao.executeNativeQuery("SELECT * FROM General_symbol WHERE parent is null and deleted_by is null");

        return generalSymbols;
    }

    public List<GeneralSymbol> findAllChildByParentId(Integer parentId) {
        List<GeneralSymbol> generalSymbols = dao.executeNativeQuery("SELECT * FROM general_symbol WHERE  deleted_by is null and parent = " + parentId);
        return generalSymbols;
    }

    @Override
    public List<GeneralSymbol> getAllGSymbolWithoutLanguage() {
        return dao.findListByQuery("SELECT g FROM GeneralSymbol g WHERE g.serial <> 0 order by g.serial asc");
    }

    @Override
    public List<GeneralSymbol> getAllGSymbolAscended() {
        return dao.findListByQuery("SELECT g FROM GeneralSymbol g order by g.serial asc");
    }

    @Override
    public List<GeneralSymbolView> getAllGSymbol(GeneralSymbolSearchBean generalSymbolSearchBean) {

        Map<String, Object> params = new HashMap<>();
        params.put("companyId", generalSymbolSearchBean.getCompanyId());
        String sql = "SELECT gsv FROM GeneralSymbolView gsv WHERE gsv.company_id = :companyId " + createSql(generalSymbolSearchBean).toString();

        List<GeneralSymbolView> data = dao.findListByQuery(sql, params);

        return data;
    }

    public StringBuilder createSql(GeneralSymbolSearchBean generalSymbolSearchBean) {
        StringBuilder sql = new StringBuilder();
        addSerialFrom(sql, generalSymbolSearchBean);
        addSerialTo(sql, generalSymbolSearchBean);
        sql.append(" order by gsv.generalSerial");
        return sql;
    }

    public void addSerialFrom(StringBuilder sql, GeneralSymbolSearchBean generalSymbolSearchBean) {
        if (generalSymbolSearchBean.getSerialFrom() != null && generalSymbolSearchBean.getSerialFrom() > 0) {
            sql.append(" and gsv.generalSerial >= ").append(generalSymbolSearchBean.getSerialFrom());
        }
    }

    public void addSerialTo(StringBuilder sql, GeneralSymbolSearchBean generalSymbolSearchBean) {
        if (generalSymbolSearchBean.getSerialTo() != null && generalSymbolSearchBean.getSerialTo() > 0) {
            sql.append(" and gsv.generalSerial <= ").append(generalSymbolSearchBean.getSerialTo());
        }
    }

    @Override
    public List<GeneralSymbol> getAllGeneralSymbolListByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        String sql = "SELECT g FROM GeneralSymbol g WHERE g.companyId = :companyId ";

        List<GeneralSymbol> data = dao.findListByQuery(sql, params);

        return data;
    }

    @Override
    public List<Symbol> getSymbolsByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT e FROM Symbol e WHERE e.generalSymbolId.id = 6 AND e.companyId.id = :companyId", params);
    }
}
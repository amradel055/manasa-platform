/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.CostCenter;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class GlProfitCenterServiceImpl implements GlProfitCenterService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteProfitCenter(CostCenter glProfitCenter) {
        dao.deleteEntity(glProfitCenter);
    }

    @Override
    public List<CostCenter> getAllProfitCenter() {
        return dao.findAll(CostCenter.class);
    }

    @Override
    public CostCenter addProfitCenter(CostCenter glProfitCenter) {
        return dao.updateEntity(glProfitCenter);
    }

    @Override
    public CostCenter findProfitCenter(Integer glProfitCenterId) {
        return dao.findEntityById(CostCenter.class, glProfitCenterId);
    }

    @Override
    public CostCenter updateProfitCenter(CostCenter glProfitCenter) {
        return dao.updateEntity(glProfitCenter);
    }
    
    public CostCenter searchprofitcenter(CostCenter glprofitcenter) {
        if (glprofitcenter != null) {
            StringBuilder filter = new StringBuilder();
            if (glprofitcenter.getId() != null) {
                if (filter.toString().isEmpty()) {
                    filter.append("where cast(code as character) like '%").append(glprofitcenter.getCode()).append("%'");
                } else {
                    filter.append("and cast(code as character) like '%").append(glprofitcenter.getCode()).append("%'");
                }
            }
            List<CostCenter> profitCenter = dao.executeNativeQuery("SELECT * FROM gl_profit_center" + filter.toString());
            if (!profitCenter.isEmpty()) {
                return profitCenter.get(0);
            } else {
                return null;
            }
        }
        return null;
    }
    
    public List<CostCenter> findAllprofitcenter() {
            List<CostCenter> profitCenter = dao.executeNativeQuery("SELECT * FROM gl_profit_center where deleted_by is null");
            if (!profitCenter.isEmpty()) {
                return profitCenter;
            }
            return null;
            
    }
    
    public boolean validateBarcodeprofitcenter(Integer barcode) {
        List<CostCenter> profitCenter = dao.executeNativeQuery("SELECT * FROM glprofitcenter WHERE barcode = "+barcode);
        if (profitCenter.isEmpty()) {
            return true;
        }
        return false;
    }
    
    public boolean validatecodeprofitcenter(Integer code) {
        List<CostCenter> profitCenter = dao.executeNativeQuery("SELECT * FROM glprofitcenter WHERE code = "+code);
        if (profitCenter.isEmpty()) {
            return true;
        }
        return false;
    }
    
    public int findLevelprofitcenter(Integer codeParent) {
        if (codeParent != null) {
            CostCenter adminUnitParent = findParentprofitcenter(codeParent);
            int level = adminUnitParent.getLevel() + 1;
            return level;
        } else {
            return 1;
        }
    }
    
    public CostCenter findParentprofitcenter(Integer codeParent) {
        if (codeParent != null) {
            List<CostCenter> profitCenter = dao.executeNativeQuery("SELECT * FROM glprofitcenter  WHERE code = "+codeParent);
            if (!profitCenter.isEmpty()) {
                return profitCenter.get(0);
            } else {
                return null;
            }
        }
        return null;
    }


}

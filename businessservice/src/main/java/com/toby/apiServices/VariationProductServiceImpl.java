/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.core.GenericDAO;
import com.toby.entity.Variation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Lenovo
 */
@Stateless
public class VariationProductServiceImpl implements VariationProductService {

    @EJB
    private GenericDAO dao;
    
    @Override
     public Variation addVariation(Variation variation) {
       return dao.updateEntity(variation);
    }
    
    @Override
   public Variation updateVariation(Variation variation){
       return dao.updateEntity(variation);
    }
    
    @Override
    public Variation findVariation(Integer id) {
        return dao.findEntityById(Variation.class, id);
    }


    @Override
    public List<Variation> getAllVariationsByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT a FROM Variation a WHERE a.companyId.id =:companyId", params);
        //return dao.findEntityListByCompanyId(Branch.class, companyId);
    }
    
    
    @Override
    public List<Variation> getAllVariationByCompanyIdAndBranchId(Integer companyId , Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT a FROM Variation a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId", params);
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(a.id) FROM Variation a");
        return maxId;
    }
}

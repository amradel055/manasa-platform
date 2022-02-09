/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.core.GenericDAO;
import com.toby.entity.VariationDetails;
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
public class VariationProductDatailsServiceImpl implements VariationProductDetailsService {

    @EJB
    private GenericDAO dao;

    @Override
    public VariationDetails addVariationDetails(VariationDetails variationDetails) {
        return dao.updateEntity(variationDetails);
    }

    @Override
    public VariationDetails updateVariationDetails(VariationDetails variationDetails) {
        return dao.updateEntity(variationDetails);
    }

    @Override
    public VariationDetails findVariationDetails(Integer id) {
        return dao.findEntityById(VariationDetails.class, id);
    }

    @Override
    public List<VariationDetails> getAllVariationsDetailsByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT a FROM VariationDetails a WHERE a.companyId.id =:companyId", params);
        //return dao.findEntityListByCompanyId(Branch.class, companyId);
    }

    @Override
    public List<VariationDetails> getAllVariationDetailsByCompanyIdAndBranchId(Integer companyId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT a FROM VariationDetails a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId", params);
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(a.id) FROM VariationDetails a");
        return maxId;
    }
}

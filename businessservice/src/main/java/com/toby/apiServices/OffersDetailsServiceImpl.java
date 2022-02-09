/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.core.GenericDAO;
import com.toby.entity.OffersDetails;
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
public class OffersDetailsServiceImpl implements OffersDatailsService {

    @EJB
    private GenericDAO dao;

    @Override
    public OffersDetails addOffersDetails(OffersDetails offersDetails) {
        return dao.updateEntity(offersDetails);
    }

    @Override
    public OffersDetails updateOffersDetails(OffersDetails offersDetails) {
        return dao.updateEntity(offersDetails);
    }

    @Override
    public OffersDetails findOffersDetails(Integer id) {
        return dao.findEntityById(OffersDetails.class, id);
    }

    @Override
    public List<OffersDetails> getAllOffersDetailsByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT a FROM OffersDetails a WHERE a.companyId.id =:companyId", params);
        //return dao.findEntityListByCompanyId(Branch.class, companyId);
    }

    @Override

    public List<OffersDetails> getAllOffersDetailsByCompanyIdAndBranchId(Integer companyId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT a FROM OffersDetails a WHERE a.companyId.id =:companyId AND a.branchId.id =:branchId", params);
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(a.id) FROM OffersDetails a");
        return maxId;
    }
}

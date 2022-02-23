/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.HosPatientRegistration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class HosPatientRegistrationServiceImpl implements HosPatientRegistrationService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<HosPatientRegistration> getAllHosPatientRegistration() {
        return dao.findAll(HosPatientRegistration.class);
    }

    @Override
    public void deleteHosPatientRegistration(HosPatientRegistration hosPatientRegistration) {
        dao.deleteEntity(hosPatientRegistration);
    }

    @Override
    public HosPatientRegistration updateHosPatientRegistration(HosPatientRegistration hosPatientRegistration) {
        return dao.updateEntity(hosPatientRegistration);
    }

    @Override
    public HosPatientRegistration addHosPatientRegistration(HosPatientRegistration hosPatientRegistration) {
        if (hosPatientRegistration.getId() == null) {
            dao.saveEntity(hosPatientRegistration);
        } else {
            return dao.updateEntity(hosPatientRegistration);
        }
        return hosPatientRegistration;
    }

    @Override
    public HosPatientRegistration findHosPatientRegistration(Integer id) {
        return dao.findEntityById(HosPatientRegistration.class, id);
    }

    @Override
    public List<HosPatientRegistration> getAllHosPatientRegistrationByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<HosPatientRegistration> hosPatientRegistrationList = dao.findListByQuery("SELECT e FROM HosPatientRegistration e WHERE e.companyId.id = :companyId ", params);
        return hosPatientRegistrationList;
    }

    @Override
    public List<HosPatientRegistration> getAllHosPatientRegistrationByBranchId(Integer branchId , Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<HosPatientRegistration> hosPatientRegistrationList = dao.findListByQuery("SELECT e FROM HosPatientRegistration e WHERE e.branchId.id = :branchId AND e.type=:type ", params);
        return hosPatientRegistrationList;
    }

}

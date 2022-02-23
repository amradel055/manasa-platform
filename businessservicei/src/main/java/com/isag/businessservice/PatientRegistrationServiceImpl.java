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
 * @author hq002
 */
@Stateless
public class PatientRegistrationServiceImpl implements PatientRegistrationService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<HosPatientRegistration> getPatientRegistrationByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(HosPatientRegistration.class, companyId);
    }

    @Override
    public List<HosPatientRegistration> getPatientRegistrationByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<HosPatientRegistration> detailses = dao.findListByQuery("SELECT e FROM HosPatientRegistration e WHERE e.branchId.id = :branchId", params);
        return detailses;
    }

    @Override
    public HosPatientRegistration addPatientRegistration(HosPatientRegistration hosPatientRegistration) {
        dao.saveEntity(hosPatientRegistration);
        return hosPatientRegistration;
    }

    @Override
    public HosPatientRegistration updatePatientRegistration(HosPatientRegistration hosPatientRegistration) {
        return dao.updateEntity(hosPatientRegistration);
    }

    @Override
    public void deletePatientRegistration(HosPatientRegistration hosPatientRegistration) {
        dao.deleteEntity(hosPatientRegistration);
    }

    @Override
    public HosPatientRegistration findPatientRegistrationById(Integer hosPatientRegistrationId) {
        return dao.findEntityById(HosPatientRegistration.class, hosPatientRegistrationId);
    }

    @Override
    public List<HosPatientRegistration> getPatientByBranchId(Integer BranchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", BranchId);
        List<HosPatientRegistration> detailses = dao.findListByQuery("SELECT e FROM HosPatientRegistration e WHERE e.branchId.id = :branchId AND ((e.type = 0) AND e.active = 1)", params);
        return detailses;
    }

    @Override
    public List<HosPatientRegistration> getCompanyByBranchId(Integer BranchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", BranchId);
        List<HosPatientRegistration> detailses = dao.findListByQuery("SELECT e FROM HosPatientRegistration e WHERE e.branchId.id = :branchId AND ((e.type = 1) AND e.active = 1)", params);
        return detailses;
    }

    @Override
    public Boolean checkCodeExistence(HosPatientRegistration hosPatientRegistration) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        params.put("code", hosPatientRegistration.getCode());
        params.put("branchId", hosPatientRegistration.getBranchId().getId());

        query.append("SELECT i FROM HosPatientRegistration i WHERE i.branchId.id = :branchId AND i.code = :code");

        if (hosPatientRegistration.getType() != 2) {
            params.put("type", hosPatientRegistration.getType());
            query.append(" AND i.type = :type");
        }

        if (hosPatientRegistration.getId() != null) {
            params.put("id", hosPatientRegistration.getId());
            query.append(" AND i.id != :id");
        }

        String q = query.toString();

        List<HosPatientRegistration> patientRegistration = dao.findListByQuery(q, params);

        if (patientRegistration != null && !patientRegistration.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public List<HosPatientRegistration> getAllHosPatientRegistrationByBranchId(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<HosPatientRegistration> hosPatientRegistrationList = dao.findListByQuery("SELECT e FROM HosPatientRegistration e WHERE e.branchId.id = :branchId AND e.type=:type ", params);
        return hosPatientRegistrationList;
    }
}

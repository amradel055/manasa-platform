/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.HosDoctorSearchBean;
import com.isag.core.GenericDAO;
import com.isag.entity.CostCenter;
import com.isag.entity.HosDoctor;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class HosDoctorServiceImpl implements HosDoctorService {

    @EJB
    private HosDoctorService hosDoctorService;
    @EJB
    private GenericDAO dao;

    @Override
    public List<HosDoctor> getAllHosDoctorRoots() {
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.parent IS NULL");
    }

    @Override
    public void deleteHosDoctor(HosDoctor selectedCS) {
        dao.deleteEntity(selectedCS);
    }

    @Override
    public HosDoctor addHosDoctor(HosDoctor newHosDoctor) {
        return dao.updateEntity(newHosDoctor);
    }

    @Override
    public void saveHosDoctor(HosDoctor newHosDoctor) {
        dao.saveEntity(newHosDoctor);
    }

    @Override
    public List<HosDoctor> getCompanyHosDoctorRoots(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("id", companyId);
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.parent IS NULL AND cs.companyId.id=:id", params);
    }

    @Override
    public List<HosDoctor> getAllHosDoctors(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.branchId.id=:branchId AND cs.active = 1", params);
    }

    @Override
    public List<HosDoctor> getCompanyHosDoctors(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("id", companyId);
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.companyId.id=:id ORDER BY cs.level ASC", params);
    }

    @Override
    public List<HosDoctor> getActiveSubHosDoctorsByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.parent IS NOT NULL AND cs.branchId.id=:id AND cs.active = 1", params);
    }

    @Override
    public List<HosDoctor> getBranchWithoutParentHosDoctors(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.branchId.id=:id", params);
    }

    @Override
    public List<HosDoctor> getBranchHosDoctorsActive(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.branchId.id=:id AND cs.active=1", params);
    }

    @Override
    public List<HosDoctor> getAllSubHosDoctorByBranchIdActive(Integer id) {
        List<HosDoctor> allHosDoctorUnits = hosDoctorService.getBranchHosDoctorsActive(id);
        List<HosDoctor> childrens = new ArrayList<>();
        Map<Integer, HosDoctor> map = new HashMap<>();

        for (HosDoctor gau : allHosDoctorUnits) {
            if (gau.getParent() != null) {
                map.put(gau.getParent().getId(), gau.getParent());
            }
        }

        for (HosDoctor gau : allHosDoctorUnits) {
            if (!map.containsKey(gau.getId())) {
                if (gau.getParent() == null) {
                    childrens.add(gau);
                    break;
                }
                childrens.add(gau);
            }
        }

        return childrens;

    }

    @Override
    public List<HosDoctor> getAllSubHosDoctorByBranchIdActiveAndHosDoctorId(Integer id) {
        List<HosDoctor> childrens = new ArrayList<>();
        HosDoctor HosDoctorUnit = findHosDoctor(id);
        if (HosDoctorUnit != null && HosDoctorUnit.getId() != null) {
            findTheLastChilds(HosDoctorUnit, childrens);
        }

        return childrens;

    }

    public void findTheLastChilds(HosDoctor HosDoctorUnit, List<HosDoctor> childrens) {
        if (HosDoctorUnit.getChildNodes() != null && !HosDoctorUnit.getChildNodes().isEmpty()) {
            for (HosDoctor childHosDoctor : HosDoctorUnit.getChildNodes()) {
                HosDoctor HosDoctorValue = findHosDoctor(childHosDoctor.getId());
                findTheLastChilds(HosDoctorValue, childrens);
            }
        } else {
            childrens.add(HosDoctorUnit);
        }
    }

    @Override
    public HosDoctor findHosDoctor(Integer hosDoctorId) {
        if (hosDoctorId != null) {
            return dao.findEntityById(HosDoctor.class, hosDoctorId);
        } else {
            return null;
        }
    }

    @Override
    public HosDoctor findHosDoctorByCode(Integer parentCode, Integer branchId) {
        if (parentCode != null) {
            List<HosDoctor> hosDoctorList = null;
            Map<String, Object> params = new HashMap();
            params.put("parentCode", parentCode);
            params.put("branchId", branchId);
            hosDoctorList = dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.code = :parentCode AND cs.branchId.id=:branchId", params);
            HosDoctor center = hosDoctorList != null ? hosDoctorList.get(0) : null;
            return center;
        } else {
            return null;
        }
    }

    @Override
    public List<HosDoctor> getBranchHosDoctorRoots(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.parent IS NULL AND cs.branchId.id=:id", params);
    }

    @Override
    public List<HosDoctor> getAllHosDoctorsIndex(HosDoctorSearchBean hosDoctorSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", hosDoctorSearchBean.getBranchId());
        StringBuilder queryBuilder = new StringBuilder();
        appendHosDoctorForQueryBuilder(queryBuilder, params, hosDoctorSearchBean);
        //String query = "SELECT e FROM HosDoctor e WHERE e.parent IS NOT NULL AND e.branchId.id = :branchId " + queryBuilder.toString();
        String query = "SELECT e FROM HosDoctor e WHERE e.branchId.id = :branchId AND e.level = 1 " + queryBuilder.toString();
        List<HosDoctor> hosDoctors = dao.findListByQuery(query, params);
        return hosDoctors;
    }

    private void appendHosDoctorForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, HosDoctorSearchBean hosDoctorSearchBean) {
        if (hosDoctorSearchBean.getHosDoctorIdFrom() != null && hosDoctorSearchBean.getHosDoctorIdFrom().getId() != null) {
            params.put("hosDoctorIdFrom", hosDoctorSearchBean.getHosDoctorIdFrom().getId());
            queryBuilder.append(" and e.id >= :hosDoctorIdFrom");
        }

        if (hosDoctorSearchBean.getHosDoctorIdTo() != null && hosDoctorSearchBean.getHosDoctorIdTo().getId() != null) {
            params.put("hosDoctorIdTo", hosDoctorSearchBean.getHosDoctorIdTo().getId());
            queryBuilder.append(" and e.id <= :hosDoctorIdTo");
        }
    }

    @Override
    public List<HosDoctor> getAllHosDoctorByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, String HosDoctorCode) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("HosDoctorCode", HosDoctorCode);
        return dao.findListByQuery("SELECT cc FROM HosDoctor cc WHERE cc.branchId.id = :id AND cc.code = :HosDoctorCode", params);
    }

    @Override
    public List<HosDoctor> getAllHosDoctorByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, String HosDoctorCode, Integer HosDoctorId) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("HosDoctorCode", HosDoctorCode);
        params.put("HosDoctorId", HosDoctorId);
        return dao.findListByQuery("SELECT cc FROM HosDoctor cc WHERE cc.id != :HosDoctorId AND cc.branchId.id = :id AND cc.code = :HosDoctorCode", params);
    }

    @Override
    public List<HosDoctor> getAllSubHosDoctorByBranchIdActiveAndLevel(Integer branchId, Integer level) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("level", level);
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.branchId.id=:branchId AND cs.active=1 AND cs.level =:level ", params);
    }

    /**
     * @return the hosDoctorService
     */
    public HosDoctorService getHosDoctorService() {
        return hosDoctorService;
    }

    /**
     * @param hosDoctorService the hosDoctorService to set
     */
    public void setHosDoctorService(HosDoctorService hosDoctorService) {
        this.hosDoctorService = hosDoctorService;
    }

    @Override
    public List<HosDoctor> getAllServicesByDoctorIdAndBranchId(Integer branchId, Integer doctorId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("doctorId", doctorId);
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.branchId.id=:branchId AND cs.parent.id=:doctorId", params);
    }

    @Override
    public List<HosDoctor> getAllSubHosDoctorByBranchIdActiveAndLevelAndClinicId(Integer branchId, Integer level, Integer clinicId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("level", level);
        params.put("clinicId", clinicId);
        return dao.findListByQuery("SELECT cs FROM HosDoctor cs WHERE cs.branchId.id=:branchId AND cs.active=1 AND cs.level =:level AND cs.parent.id = :clinicId ", params);
    }
}

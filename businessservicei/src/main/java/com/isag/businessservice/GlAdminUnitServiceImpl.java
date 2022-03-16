/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.GlAdminUnitSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.GlAdminUnitDTO;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.IsagUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class GlAdminUnitServiceImpl implements GlAdminUnitService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GlAdminUnitService glAdminUnitService;

    @Override
    public void deleteAdminUnit(GlAdminUnit isagAdminUnit) {
        dao.deleteEntity(isagAdminUnit);
    }

    @Override
    public List<GlAdminUnit> getAllAdminUnit() {
        return dao.findAll(GlAdminUnit.class);
    }

    @Override
    public GlAdminUnit addAdminUnit(GlAdminUnit adminUnit) {
        adminUnit.setCreationDate(new Date());
        return dao.updateEntity(adminUnit);
    }

    @Override
    public GlAdminUnit findAdminUnit(Integer adminUnitId) {
        if (adminUnitId != null) {
            return dao.findEntityById(GlAdminUnit.class, adminUnitId);
        } else {
            return null;
        }
    }

    @Override
    public GlAdminUnit updateAdminUnit(GlAdminUnit adminUnit) {
        adminUnit.setModificationDate(new Date());
        return dao.updateEntity(adminUnit);
    }

    public GlAdminUnit searchAdminUnit(GlAdminUnit gladminunit) {
        if (gladminunit != null) {
            StringBuilder filter = new StringBuilder();
            if (gladminunit.getId() != null) {
                if (filter.toString().isEmpty()) {
                    filter.append("where cast(code as character) like '%").append(gladminunit.getCode()).append("%'");
                } else {
                    filter.append("and cast(code as character) like '%").append(gladminunit.getCode()).append("%'");
                }
            }
            List<GlAdminUnit> adminUnit = dao.executeNativeQuery("SELECT * FROM gl_admin_unit" + filter.toString() + " ORDER BY code ASC");
            if (!adminUnit.isEmpty()) {
                return adminUnit.get(0);
            } else {
                return null;
            }
        }
        return null;
    }

    public List<GlAdminUnit> findAllAdminUnit() {
        List<GlAdminUnit> adminUnit = dao.executeNativeQuery("SELECT * FROM gl_admin_unit where deleted_by is null  ORDER BY code ASC");
        if (!adminUnit.isEmpty()) {
            return adminUnit;
        }
        return null;
    }

    public boolean validateAdminUnitNumber(Integer code) {
        List<GlAdminUnit> adminUnit = dao.executeNativeQuery("SELECT * FROM gl_admin_unit  WHERE code = " + code + "  ORDER BY code ASC");
        if (adminUnit.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean validateBarcodeAdminUnit(Integer shortCode) {
        List<GlAdminUnit> adminUnit = dao.executeNativeQuery("SELECT * FROM gl_admin_unit WHERE short_code = " + shortCode + "  ORDER BY code ASC");
        if (adminUnit.isEmpty()) {
            return true;
        }
        return false;
    }

    public int findLevelAdminUnit(Integer codeParent) {
        if (codeParent != null) {
            GlAdminUnit adminUnitParent = findParentAdminUnit(codeParent);
            int level = adminUnitParent.getLevel() + 1;
            return level;
        } else {
            return 1;
        }
    }

    public GlAdminUnit findParentAdminUnit(Integer codeParent) {
        if (codeParent != null) {
            List<GlAdminUnit> adminUnit = dao.executeNativeQuery("SELECT * FROM gl_admin_unit  WHERE code = " + codeParent + "  ORDER BY code ASC");
            if (!adminUnit.isEmpty()) {
                return adminUnit.get(0);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public List<GlAdminUnit> getAllAdminUnitByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT e FROM GlAdminUnit e WHERE  e.companyId.id= :companyId  ORDER BY e.code ASC ", params);

    }

    @Override
    public List<GlAdminUnit> getAllAdminUnitByBranchIdActive(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT e FROM GlAdminUnit e WHERE  e.branchId.id= :id and e.active = 1   ORDER BY e.code ASC ", params);
    }

    @Override
    public List<GlAdminUnit> getAllSubAdminUnitByBranchIdActive(Integer id) {
        List<GlAdminUnit> allAdminUnits = getAllAdminUnitByBranchIdActive(id);
        List<GlAdminUnit> childrens = new ArrayList<>();
        Map<Integer, GlAdminUnit> map = new HashMap<>();

        for (GlAdminUnit gau : allAdminUnits) {
            if (gau.getParent() != null) {
                map.put(gau.getParent().getId(), gau);
            }
        }

        for (GlAdminUnit gau : allAdminUnits) {
            if (!map.containsKey(gau.getId())) {
                if (gau.getParent() == null) {
                    childrens.add(gau);
                    continue;
                }
                childrens.add(gau);
            }
        }

        return childrens;

    }

    @Override
    public List<GlAdminUnit> getAllAdminUnitIndex(GlAdminUnitSearchBean glAdminUnitSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", glAdminUnitSearchBean.getBranchId());
        StringBuilder queryBuilder = new StringBuilder();
        appendGlAdminUnitForQueryBuilder(queryBuilder, params, glAdminUnitSearchBean);
        String query = "SELECT e FROM GlAdminUnit e WHERE e.branchId.id = :branchId " + queryBuilder.toString() + "   ORDER BY e.code ASC ";
        List<GlAdminUnit> glAdminUnit = dao.findListByQuery(query, params);
        return glAdminUnit;
    }

    private void appendGlAdminUnitForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, GlAdminUnitSearchBean glAdminUnitSearchBean) {
        if (glAdminUnitSearchBean.getAdminUnitFrom() != null && glAdminUnitSearchBean.getAdminUnitFrom() > 0) {
            params.put("adminUnitFrom", glAdminUnitSearchBean.getAdminUnitFrom());
            queryBuilder.append(" and e.id >= :adminUnitFrom");
        }

        if (glAdminUnitSearchBean.getAdminUnitTo() != null && glAdminUnitSearchBean.getAdminUnitTo() > 0) {
            params.put("adminUnitTo", glAdminUnitSearchBean.getAdminUnitTo());
            queryBuilder.append(" and e.id <= :adminUnitTo");
        }
    }

    @Override
    public GlAdminUnit findAdminUnitByCode(Integer parentCode, Integer branchId) {
        if (parentCode != null) {
            List<GlAdminUnit> itemList = null;
            Map<String, Object> params = new HashMap();
            params.put("parentCode", parentCode);
            params.put("branchId", branchId);
            itemList = dao.findListByQuery("SELECT i FROM GlAdminUnit i WHERE i.code = :parentCode AND i.branchId.id=:branchId   ORDER BY i.code ASC ", params);
            GlAdminUnit item = itemList != null ? itemList.get(0) : null;
            return item;
        } else {
            return null;
        }
    }

    @Override
    public List<GlAdminUnit> getAllSubAdminUnitByBranchIdActiveAndLevel(Integer branchId, Integer level) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("level", level);
        return dao.findListByQuery("SELECT i FROM GlAdminUnit i WHERE i.branchId.id=:branchId AND i.active=1 AND i.level =:level   ORDER BY i.code ASC ", params);
    }

    @Override
    public List<GlAdminUnit> getAllSubAdminUnitByBranchIdActiveAndCostCenterId(Integer id) {
        List<GlAdminUnit> childrens = new ArrayList<>();
        GlAdminUnit adminUnit = findAdminUnit(id);
        if (adminUnit != null && adminUnit.getId() != null) {
            findTheLastChilds(adminUnit, childrens);
        }
        return childrens;
    }

    public void findTheLastChilds(GlAdminUnit adminUnit, List<GlAdminUnit> childrens) {
        if (adminUnit.getAdminUnitChilds() != null && !adminUnit.getAdminUnitChilds().isEmpty()) {
            for (GlAdminUnit childAdminUnit : adminUnit.getAdminUnitChilds()) {
                GlAdminUnit adminUnitValue = findAdminUnit(childAdminUnit.getId());
                findTheLastChilds(adminUnitValue, childrens);
            }
        } else {
            childrens.add(adminUnit);
        }
    }

    private List<GlAdminUnitDTO> returnListDTO(List<GlAdminUnit> list) {
        List<GlAdminUnitDTO> dTOList = new ArrayList<>();
        for (GlAdminUnit entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private GlAdminUnitDTO mapTODTO(GlAdminUnit entity, Boolean needDetail) {
        GlAdminUnitDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private GlAdminUnitDTO initMapToDTO(GlAdminUnit entity) {

        GlAdminUnitDTO dTO = new GlAdminUnitDTO();
        dTO.setId(entity.getId());

        if (entity.getParent() != null) {
            GlAdminUnitDTO glAdminUnitDTO = new GlAdminUnitDTO();
            glAdminUnitDTO.setId(entity.getParent().getId());
            dTO.setParent(glAdminUnitDTO);
        }

        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setShortCode(entity.getShortCode());
        dTO.setLevel(entity.getLevel());
        dTO.setActive(entity.getActive());
        dTO.setBranchId(entity.getBranchId().getId());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    private GlAdminUnit mapFromDTO(GlAdminUnitDTO dTO, IsagUser isagUser) {
        GlAdminUnit entity = new GlAdminUnit();

        if (dTO.getParent() != null && dTO.getParent().getId() != null) {
            GlAdminUnit parent = new GlAdminUnit();
            parent.setId(dTO.getParent().getId());
            entity.setParent(parent);
        }

        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());
        entity.setShortCode(dTO.getShortCode());
        entity.setLevel(dTO.getLevel());
        entity.setActive(dTO.getActive());

        if (dTO.getId()
                == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }
    
     @Override
    public List<GlAdminUnitDTO> getAllSubAdminUnitByBranchIdActiveDTO(Integer id) {
        List<GlAdminUnit> allAdminUnits = getAllAdminUnitByBranchIdActive(id);
        List<GlAdminUnit> childrens = new ArrayList<>();
        Map<Integer, GlAdminUnit> map = new HashMap<>();

        for (GlAdminUnit gau : allAdminUnits) {
            if (gau.getParent() != null) {
                map.put(gau.getParent().getId(), gau);
            }
        }

        for (GlAdminUnit gau : allAdminUnits) {
            if (!map.containsKey(gau.getId())) {
                if (gau.getParent() == null) {
                    childrens.add(gau);
                    continue;
                }
                childrens.add(gau);
            }
        }

        return returnListDTO(childrens);

    }

}

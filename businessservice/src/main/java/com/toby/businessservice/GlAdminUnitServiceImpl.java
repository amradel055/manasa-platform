/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.GlAdminUnitSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.entity.Branch;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
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
    public void deleteAdminUnit(GlAdminUnit tobyAdminUnit) {
        dao.deleteEntity(tobyAdminUnit);
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
        List<GlAdminUnit> adminUnit = dao.executeNativeQuery("SELECT * FROM gl_admin_unit  WHERE code = " + code +"  ORDER BY code ASC");
        if (adminUnit.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean validateBarcodeAdminUnit(Integer shortCode) {
        List<GlAdminUnit> adminUnit = dao.executeNativeQuery("SELECT * FROM gl_admin_unit WHERE short_code = " + shortCode +"  ORDER BY code ASC");
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
            List<GlAdminUnit> adminUnit = dao.executeNativeQuery("SELECT * FROM gl_admin_unit  WHERE code = " + codeParent+"  ORDER BY code ASC");
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
    public List<GlAdminUnit> getAllAdminUnitByBranchIdActive(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM GlAdminUnit e WHERE  e.branchId.id= :branchId and e.active = 1   ORDER BY e.code ASC ", params);
    }

    @Override
    public List<GlAdminUnit> getAllSubAdminUnitByBranchIdActive(Integer branchId) {
        List<GlAdminUnit> allAdminUnits = getAllAdminUnitByBranchIdActive(branchId);
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
        String query = "SELECT e FROM GlAdminUnit e WHERE e.branchId.id = :branchId " + queryBuilder.toString() +"   ORDER BY e.code ASC ";
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
    //mapToEntity
    public GlAdminUnit mapToEntity(GlAdminUnitDTO adminUnitDTO, TobyUser tobyUser) {
        
        GlAdminUnit adminUnit = new GlAdminUnit();
        adminUnit.setCode(adminUnitDTO.getCode());
        adminUnit.setId(adminUnitDTO.getId());
        adminUnit.setMarkDisable(adminUnitDTO.getMarkDisable());
        adminUnit.setMarkEdit(adminUnitDTO.getMarkEdit());
        adminUnit.setLevel(adminUnitDTO.getLevel());
        adminUnit.setName(adminUnitDTO.getName());
        adminUnit.setShortCode(adminUnitDTO.getShortCode());
        if (adminUnitDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(adminUnitDTO.getCreatedBy());
            adminUnit.setCreatedBy(user);
            adminUnit.setCreationDate(adminUnitDTO.getCreatedDate());
            adminUnit.setModifiedBy(tobyUser);
            adminUnit.setModificationDate(new Date());
            if (adminUnitDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(adminUnitDTO.getCompanyId());
                adminUnit.setCompanyId(company);
            }

            if (adminUnit.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(adminUnitDTO.getBranchId());
                adminUnit.setBranchId(branch);
            }
        } else {
            adminUnit.setCreatedBy(tobyUser);
            adminUnit.setCreationDate(new Date());
            adminUnit.setCompanyId(tobyUser.getCompanyId());
            adminUnit.setBranchId(tobyUser.getBranchId());
        }

        return adminUnit;
    }
    
    
    

    public GlAdminUnitDTO mapToDTO(GlAdminUnit adminUnit) {
        
        GlAdminUnitDTO adminUnitDTO = new GlAdminUnitDTO();
        adminUnitDTO.setCreatedBy(adminUnit.getCreatedBy() != null ? adminUnit.getCreatedBy().getId() : null);
        adminUnitDTO.setCreatedDate(adminUnit.getCreationDate());
        adminUnitDTO.setBranchId(adminUnit.getBranchId() !=null ? adminUnit.getBranchId().getId() : null);
        adminUnitDTO.setId(adminUnit.getId());
        adminUnitDTO.setCompanyId(adminUnit.getCompanyId() !=null ? adminUnit.getCompanyId().getId() : null );
        adminUnitDTO.setMarkDisable(adminUnit.getMarkDisable());
        adminUnitDTO.setMarkEdit(adminUnit.getMarkEdit());
        adminUnitDTO.setName(adminUnit.getName());
        adminUnitDTO.setCode(adminUnit.getCode());
        adminUnitDTO.setShortCode(adminUnit.getShortCode());
        adminUnitDTO.setLevel(adminUnit.getLevel());
        adminUnitDTO.setMsg(adminUnit.getMsg());
        
        return adminUnitDTO;
    }

    public List<GlAdminUnitDTO> mapToDTOList(List<GlAdminUnit> adminUnitList, TobyUser tobyUser) {
        List<GlAdminUnitDTO> adminUnitDTOList = new ArrayList<>();
        for (GlAdminUnit adminUnit : adminUnitList) {
            adminUnitDTOList.add(mapToDTO(adminUnit));
        }
        return adminUnitDTOList;
    }

    @Override
    public List<GlAdminUnitDTO> getAllAdminUnitDTOByBranchIdActive(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<GlAdminUnit> adminUnits =  dao.findListByQuery("SELECT e FROM GlAdminUnit e WHERE  e.branchId.id= :branchId and e.active = 1   ORDER BY e.code ASC ", params);
        return mapToDTOList(adminUnits, tobyUser);
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

    private GlAdminUnit mapFromDTO(GlAdminUnitDTO dTO, TobyUser tobyUser) {
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
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }
    
}

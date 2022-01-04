/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.CostCenterSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.CostCenterDTO;
import com.toby.entity.Branch;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAccount;
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
 * @author hq003
 */
@Stateless
public class CostCenterServiceImpl implements CostCenterService {

    @EJB
    private CostCenterService costCenterService;
    @EJB
    private GenericDAO dao;

    @Override
    public List<CostCenter> getAllCostCenterRoots() {
        return dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.parent IS NULL   ORDER BY cs.code ASC ");
    }

    @Override
    public void deleteCostCenter(CostCenter selectedCS) {
        dao.deleteEntity(selectedCS);
    }

    @Override
    public CostCenter addCostCenter(CostCenter newCostCenter) {
        return dao.updateEntity(newCostCenter);
    }

    @Override
    public void saveCostCenter(CostCenter newCostCenter) {
        dao.saveEntity(newCostCenter);
    }

    @Override
    public List<CostCenter> getCompanyCostCenterRoots(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("id", companyId);
        return dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.parent IS NULL AND cs.companyId.id=:id   ORDER BY cs.code ASC ", params);
    }

    @Override
    public List<CostCenter> getAllCostCenters(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.branchId.id=:branchId AND cs.active = 1   ORDER BY cs.code ASC ", params);
    }

    @Override
    public List<CostCenter> getCompanyCostCenters(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("id", companyId);
        return dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.companyId.id=:id ORDER BY cs.level ASC", params);
    }

    @Override
    public List<CostCenter> getActiveSubCostCentersByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        return dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.parent IS NOT NULL AND cs.branchId.id=:id AND cs.active = 1   ORDER BY cs.code ASC ", params);
    }

    @Override
    public List<CostCenter> getBranchWithoutParentCostCenters(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        return dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.branchId.id=:id   ORDER BY cs.code ASC ", params);
    }

    @Override
    public List<CostCenter> getBranchCostCentersActive(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.branchId.id=:branchId AND cs.active=1   ORDER BY cs.code ASC ", params);
    }

    @Override
    public List<CostCenter> getAllSubCostCenterByBranchIdActive(Integer branchId) {
        List<CostCenter> allCostCenterUnits = getBranchCostCentersActive(branchId);
        List<CostCenter> childrens = new ArrayList<>();
        Map<Integer, CostCenter> map = new HashMap<>();

        for (CostCenter gau : allCostCenterUnits) {
            if (gau.getParent() != null) {
                map.put(gau.getParent().getId(), gau.getParent());
            }
        }

        for (CostCenter gau : allCostCenterUnits) {
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
    public List<CostCenter> getAllSubCostCenterByBranchIdActiveAndCostCenterId(Integer id) {
        List<CostCenter> childrens = new ArrayList<>();
        CostCenter CostCenterUnit = findCostCenter(id);
        if (CostCenterUnit != null && CostCenterUnit.getId() != null) {
            findTheLastChilds(CostCenterUnit, childrens);
        }

        return childrens;

    }

    public void findTheLastChilds(CostCenter CostCenterUnit, List<CostCenter> childrens) {
        if (CostCenterUnit.getChildNodes() != null && !CostCenterUnit.getChildNodes().isEmpty()) {
            for (CostCenter childCostCenter : CostCenterUnit.getChildNodes()) {
                CostCenter CostCenterValue = findCostCenter(childCostCenter.getId());
                findTheLastChilds(CostCenterValue, childrens);
            }
        } else {
            childrens.add(CostCenterUnit);
        }
    }

    @Override
    public CostCenter findCostCenter(Integer costCenterId) {
        if (costCenterId != null) {
            return dao.findEntityById(CostCenter.class, costCenterId);
        } else {
            return null;
        }
    }

    @Override
    public CostCenter findCostCenterByCode(Integer parentCode, Integer branchId) {
        if (parentCode != null) {
            List<CostCenter> costCenterList = null;
            Map<String, Object> params = new HashMap();
            params.put("parentCode", parentCode);
            params.put("branchId", branchId);
            costCenterList = dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.code = :parentCode AND cs.branchId.id=:branchId   ORDER BY cs.code ASC ", params);
            CostCenter center = costCenterList != null ? costCenterList.get(0) : null;
            return center;
        } else {
            return null;
        }
    }

    @Override
    public List<CostCenter> getBranchCostCenterRoots(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.parent IS NULL AND cs.branchId.id=:id   ORDER BY cs.code ASC ", params);
    }

    @Override
    public List<CostCenter> getAllCostCentersIndex(CostCenterSearchBean costCenterSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", costCenterSearchBean.getBranchId());
        StringBuilder queryBuilder = new StringBuilder();
        //String query = "SELECT e FROM CostCenter e WHERE e.parent IS NOT NULL AND e.branchId.id = :branchId " + queryBuilder.toString();
        String query = "SELECT e FROM CostCenter e WHERE e.branchId.id = :branchId ";
        queryBuilder.append(query);
        appendCostCenterForQueryBuilder(queryBuilder, params, costCenterSearchBean);
        queryBuilder.append(" ORDER BY e.code");
        List<CostCenter> costCenters = dao.findListByQuery(queryBuilder.toString(), params);
        return costCenters;
    }

    private void appendCostCenterForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, CostCenterSearchBean costCenterSearchBean) {
        if (costCenterSearchBean.getCostCenterIdFrom() != null && costCenterSearchBean.getCostCenterIdFrom().getId() != null) {
            params.put("costCenterIdFrom", costCenterSearchBean.getCostCenterIdFrom().getCode());
            queryBuilder.append(" and e.code >= :costCenterIdFrom");
        }

        if (costCenterSearchBean.getCostCenterIdTo() != null && costCenterSearchBean.getCostCenterIdTo().getId() != null) {
            params.put("costCenterIdTo", costCenterSearchBean.getCostCenterIdTo().getCode());
            queryBuilder.append(" and e.code <= :costCenterIdTo");
        }
    }

    @Override
    public List<CostCenter> getAllCostCenterByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer CostCenterCode) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("CostCenterCode", CostCenterCode);
        return dao.findListByQuery("SELECT cc FROM CostCenter cc WHERE cc.branchId.id = :id AND cc.code = :CostCenterCode   ORDER BY cc.code ASC ", params);
    }

    @Override
    public List<CostCenter> getAllCostCenterByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer CostCenterCode, Integer CostCenterId) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("CostCenterCode", CostCenterCode);
        params.put("CostCenterId", CostCenterId);
        return dao.findListByQuery("SELECT cc FROM CostCenter cc WHERE cc.id != :CostCenterId AND cc.branchId.id = :id AND cc.code = :CostCenterCode   ORDER BY cc.code ASC ", params);
    }

    @Override
    public List<CostCenter> getAllSubCostCenterByBranchIdActiveAndLevel(Integer branchId, Integer level) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("level", level);
        return dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.branchId.id=:branchId AND cs.active=1 AND cs.level =:level   ORDER BY cs.code ASC ", params);
    }
    //mapToEntity
    public CostCenter mapToEntity(CostCenterDTO centerDTO, TobyUser tobyUser) {
        
        CostCenter costCenter = new CostCenter();
        costCenter.setCode(centerDTO.getCode());
        costCenter.setCode(centerDTO.getCode());
        costCenter.setId(centerDTO.getId());
        costCenter.setMarkDisable(centerDTO.getMarkDisable());
        costCenter.setMarkEdit(centerDTO.getMarkEdit());
        costCenter.setLevel(centerDTO.getLevel());
        costCenter.setName(centerDTO.getName());
        costCenter.setShortCode(centerDTO.getShortCode());
        if (centerDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(centerDTO.getCreatedBy());
            costCenter.setCreatedBy(user);
            costCenter.setCreationDate(centerDTO.getCreatedDate());
            costCenter.setModifiedBy(tobyUser);
            costCenter.setModificationDate(new Date());
            if (centerDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(centerDTO.getCompanyId());
                costCenter.setCompanyId(company);
            }

            if (centerDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(centerDTO.getBranchId());
                costCenter.setBranchId(branch);
            }
        } else {
            costCenter.setCreatedBy(tobyUser);
            costCenter.setCreationDate(new Date());
            costCenter.setCompanyId(tobyUser.getCompanyId());
            costCenter.setBranchId(tobyUser.getBranchId());
        }
         if (centerDTO.getCurrentaccountId()!= null) {
             GlAccount glAccount =new GlAccount();
             glAccount.setId(centerDTO.getCurrentaccountId().getId());
             costCenter.setCurrentaccountId(glAccount);
            }

        return costCenter;
    }
    
    
    

    public CostCenterDTO mapToDTO(CostCenter costCenter) {
        
        CostCenterDTO costCenterDTO = new CostCenterDTO();
        costCenterDTO.setCreatedBy(costCenter.getCreatedBy() != null ? costCenter.getCreatedBy().getId() : null);
        costCenterDTO.setCreatedDate(costCenter.getCreationDate());
        costCenterDTO.setBranchId(costCenter.getBranchId() !=null ? costCenter.getBranchId().getId() : null);
        costCenterDTO.setId(costCenter.getId());
        costCenterDTO.setCompanyId(costCenter.getCompanyId() !=null ? costCenter.getCompanyId().getId() : null );
        costCenterDTO.setMarkDisable(costCenter.getMarkDisable());
        costCenterDTO.setMarkEdit(costCenter.getMarkEdit());
        costCenterDTO.setName(costCenter.getName());
        costCenterDTO.setCode(costCenter.getCode());
        costCenterDTO.setShortCode(costCenter.getShortCode());
        costCenterDTO.setLevel(costCenter.getLevel());
        costCenterDTO.setMsg(costCenter.getMsg());
        costCenterDTO.setCurrentaccountId(costCenter.getCurrentaccountId() !=null ? costCenter.getCurrentaccountId() : null);
  
        return costCenterDTO;
    }

    public List<CostCenterDTO> mapToDTOList(List<CostCenter> costCenterList, TobyUser tobyUser) {
        List<CostCenterDTO> costCenterDTOList = new ArrayList<>();
        for (CostCenter costCenter : costCenterList) {
            costCenterDTOList.add(mapToDTO(costCenter));
        }
        return costCenterDTOList;
    }

    @Override
    public List<CostCenterDTO> getAllCostCenterDTOList(TobyUser tobyUser) {
        List<CostCenter> details = getAllSubCostCenterByBranchIdActive(tobyUser.getBranchId().getId());
        return mapToDTOList(details, tobyUser);
    }

    @Override
    public List<CostCenterDTO> getActiveSubCostCenterDTOListByBranch(TobyUser tobyUser) {
         Map<String, Object> params = new HashMap();
        params.put("id", tobyUser.getBranchId().getId());
        List<CostCenter> costCenterList =  dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.parent IS NOT NULL AND cs.branchId.id=:id AND cs.active = 1   ORDER BY cs.code ASC ", params);
        return mapToDTOList(costCenterList, tobyUser);
    }
    
     ///////////// new /////////////////////
    @Override
    public List<CostCenterDTO> getActiveSubCostCentersByBranchDTO(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        List<CostCenter> list = dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.parent IS NOT NULL AND cs.branchId.id=:id AND cs.active = 1   ORDER BY cs.code ASC ", params);
        return returnListDTOSmall(list);
    }

    private List<CostCenterDTO> returnListDTO(List<CostCenter> list) {
        List<CostCenterDTO> dTOList = new ArrayList<>();
        for (CostCenter entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private CostCenterDTO mapTODTO(CostCenter entity, Boolean needDetail) {
        CostCenterDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private CostCenterDTO initMapToDTO(CostCenter entity) {

        CostCenterDTO dTO = new CostCenterDTO();
        dTO.setId(entity.getId());

        if (entity.getParent() != null) {
            CostCenterDTO costCenterDTO = new CostCenterDTO();
            costCenterDTO.setId(entity.getParent().getId());
            dTO.setParent(costCenterDTO);
        }

        if (entity.getChildNodes() != null && !entity.getChildNodes().isEmpty()) {
            List<CostCenterDTO> list = new ArrayList<>();
            for (CostCenter childNode : entity.getChildNodes()) {
                CostCenterDTO costCenterDTO = new CostCenterDTO();
                costCenterDTO.setId(childNode.getId());
                list.add(costCenterDTO);
            }
            dTO.setChildNodes(list);
        }

        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setShortCode(entity.getShortCode());
        dTO.setLevel(entity.getLevel());
        dTO.setActive(entity.getActive());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private CostCenter mapFromDTO(CostCenterDTO dTO, TobyUser tobyUser) {
        CostCenter entity = new CostCenter();

        if (dTO.getParent() != null && dTO.getParent().getId() != null) {
            CostCenter parent = new CostCenter();
            parent.setId(dTO.getParent().getId());
            entity.setParent(parent);
        }

        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());
        entity.setShortCode(dTO.getShortCode());
        entity.setLevel(dTO.getLevel());
        entity.setActive(dTO.getActive());

        if (dTO.getChildNodes() != null && !dTO.getChildNodes().isEmpty()) {
            List<CostCenter> list = new ArrayList<>();
            for (CostCenterDTO childNode : dTO.getChildNodes()) {
                CostCenter costCenter = new CostCenter();
                costCenter.setId(childNode.getId());
                list.add(costCenter);
            }
            entity.setChildNodes(list);
        }

        if (dTO.getId() == null) {
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

    private List<CostCenterDTO> returnListDTOSmall(List<CostCenter> list) {
        List<CostCenterDTO> dTOList = new ArrayList<>();
        for (CostCenter entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private CostCenterDTO mapTODTOSmall(CostCenter entity, Boolean needDetail) {
        CostCenterDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private CostCenterDTO initMapToDTOSmall(CostCenter entity) {

        CostCenterDTO dTO = new CostCenterDTO();
        dTO.setId(entity.getId());

        if (entity.getParent() != null) {
            CostCenterDTO costCenterDTO = new CostCenterDTO();
            costCenterDTO.setId(entity.getParent().getId());
            dTO.setParent(costCenterDTO);
        }

        if (entity.getChildNodes() != null && !entity.getChildNodes().isEmpty()) {
            List<CostCenterDTO> list = new ArrayList<>();
            for (CostCenter childNode : entity.getChildNodes()) {
                CostCenterDTO costCenterDTO = new CostCenterDTO();
                costCenterDTO.setId(childNode.getId());
                list.add(costCenterDTO);
            }
            dTO.setChildNodes(list);
        }

        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setShortCode(entity.getShortCode());
        dTO.setLevel(entity.getLevel());
        dTO.setActive(entity.getActive());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }
    
    @Override
    public Map<Integer,Integer> getBranchCostCentersByMap(Integer branchId) {
        Map<Integer,Integer> map = new HashMap<>();
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<CostCenter> list =  dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.branchId.id=:branchId ", params);
        for(CostCenter costCenter : list){
            map.put(costCenter.getCode(), costCenter.getId());
        }
        return map;
    }

}

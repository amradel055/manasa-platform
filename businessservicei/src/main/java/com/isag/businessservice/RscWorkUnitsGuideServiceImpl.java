/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscWorkUnitsGuide;
import com.isag.entity.Symbol;
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
public class RscWorkUnitsGuideServiceImpl implements RscWorkUnitsGuideService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<RscWorkUnitsGuideDTO> getBranchRscWorkUnitsGuidesActiveByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscWorkUnitsGuideDTO> dTOList = new ArrayList<>();
        List<RscWorkUnitsGuide> list = dao.findListByQuery("SELECT acc FROM RscWorkUnitsGuide acc WHERE acc.branchId.id=:branchId ORDER BY acc.code ASC", params);
        returmListDTO(list, dTOList);
        return dTOList;
    }
    @Override
    public List<RscWorkUnitsGuide> getBranchRscWorkUnitsGuidesByBranchANDLevel(Integer branchId,Integer level) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("level", level);
        List<RscWorkUnitsGuide> list = dao.findListByQuery("SELECT acc FROM RscWorkUnitsGuide acc WHERE acc.branchId.id=:branchId AND acc.level <= :level ORDER BY acc.code ASC", params);
        return list;
    }
    
    @Override
    public RscWorkUnitsGuide findRscWorkUnitsGuidesbyBranchIdAndCode(Integer branchId, String code) {

        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("code", code);
        String query;
        query = "SELECT r FROM RscWorkUnitsGuide r  WHERE r.code =:code AND r.branchId.id =:branchId order by r.code ASC";
        RscWorkUnitsGuide rscWorkUnitsGuide = dao.findEntityByQuery(query, params);
        return rscWorkUnitsGuide;
    }

    @Override
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuidesForBranchForMainUnits(Integer selectedBranchId, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("parent", parentId);
        List<RscWorkUnitsGuideDTO> dTOList = new ArrayList<>();
        List<RscWorkUnitsGuide> list = dao.findListByQuery("SELECT acc FROM RscWorkUnitsGuide acc WHERE acc.branchId.id = :id AND acc.parent.id = :parent ORDER BY acc.code ASC", params);
        returmListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscWorkUnitsGuideDTO> getCompanyWorkUnitsForMainUnits(Integer selectedBranchId, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("parent", parentId);
        List<RscWorkUnitsGuideDTO> dTOList = new ArrayList<>();
        List<RscWorkUnitsGuide> list = dao.findListByQuery("SELECT acc FROM RscWorkUnitsGuide acc WHERE acc.branchId.id = :id AND acc.parent.id = :parent ORDER BY acc.code ASC", params);
        returmListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscWorkUnitsGuideDTO findRscWorkUnitsGuide(Integer workUnitsGuide) {
        if (workUnitsGuide != null) {
            RscWorkUnitsGuide guide = dao.findEntityById(RscWorkUnitsGuide.class, workUnitsGuide);
            return mapTODTO(guide);
        } else {
            return null;
        }
    }

    @Override
    public List<RscWorkUnitsGuideDTO> getAllRscWorkUnitsGuideByBranchIdAndcode(Integer selectedBranchId, String code) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("code", code);

        List<RscWorkUnitsGuideDTO> dTOList = new ArrayList<>();
        List<RscWorkUnitsGuide> list = dao.findListByQuery("SELECT acc FROM RscWorkUnitsGuide acc WHERE acc.branchId.id = :id AND acc.code = :code ", params);
        returmListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscWorkUnitsGuideDTO> getAllRscWorkUnitsGuideByBranchIdAndcode(Integer selectedBranchId, String code, Integer workunitId) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("code", code);
        params.put("workunitId", workunitId);
        String sql = "SELECT acc FROM RscWorkUnitsGuide acc WHERE acc.id != :workunitId and acc.branchId.id = :id AND acc.code = :code ";
        List<RscWorkUnitsGuideDTO> dTOList = new ArrayList<>();
        List<RscWorkUnitsGuide> list = dao.findListByQuery(sql, params);
        returmListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public void addRscWorkUnitsGuide(RscWorkUnitsGuideDTO unit, IsagUser isagUser) {
        RscWorkUnitsGuide rscWorkUnitsGuide = mapFromDTO(unit, isagUser);
        dao.updateEntity(rscWorkUnitsGuide);
    }

    @Override
    public void deleteRscWorkUnitsGuide(RscWorkUnitsGuideDTO unit, IsagUser isagUser) {
        RscWorkUnitsGuide rscWorkUnitsGuide = mapFromDTO(unit, isagUser);
        dao.deleteEntity(rscWorkUnitsGuide);
    }

    @Override
    public List<RscWorkUnitsGuideDTO> getBranchUnit(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<RscWorkUnitsGuideDTO> dTOList = new ArrayList<>();
        List<RscWorkUnitsGuide> list = dao.findListByQuery("SELECT acc FROM RscWorkUnitsGuide acc WHERE acc.parent IS NULL AND acc.branchId.id=:id ORDER BY acc.code ASC", params);
        returmListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public List<RscWorkUnitsGuideDTO> getBranchUnitAndType(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<RscWorkUnitsGuideDTO> dTOList = new ArrayList<>();
        List<RscWorkUnitsGuide> list = dao.findListByQuery("SELECT acc FROM RscWorkUnitsGuide acc WHERE acc.type=1 AND acc.branchId.id=:id ORDER BY acc.code ASC", params);
        returmListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscWorkUnitsGuideDTO> getAllUnitsRoots() {
        List<RscWorkUnitsGuideDTO> dTOList = new ArrayList<>();
        List<RscWorkUnitsGuide> list = dao.findListByQuery("SELECT acc FROM RscWorkUnitsGuide acc WHERE acc.parent IS NULL ORDER BY acc.code ASC");
        returmListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscWorkUnitsGuideDTO findUnitbyBranchIdAndCode(Integer branchId, String code) {

        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("code", code);
        String query;
        query = "SELECT g FROM RscWorkUnitsGuide g  WHERE g.code =:code AND g.branchId.id =:branchId";
        RscWorkUnitsGuide unit = dao.findEntityByQuery(query, params);

        return mapTODTO(unit);
    }

    @Override
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuidesChildForParent(Integer selectedBranchId, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("parentId", parentId);
        List<RscWorkUnitsGuideDTO> dTOList = new ArrayList<>();
        List<RscWorkUnitsGuide> list = dao.findListByQuery("SELECT acc FROM RscWorkUnitsGuide acc WHERE acc.branchId.id = :id AND acc.parent.id = :parentId", params);
        returmListDTO(list, dTOList);
        return dTOList;
    }

    public RscWorkUnitsGuideDTO mapTODTO(RscWorkUnitsGuide guide) {
        RscWorkUnitsGuideDTO dTO = new RscWorkUnitsGuideDTO();
        initMapToDTO(dTO, guide);
        if (guide.getParent() != null) {
            dTO.setParent(new RscWorkUnitsGuideDTO());
            initMapToDTO(dTO.getParent(), guide.getParent());
        }
        return dTO;
    }

    private void initMapToDTO(RscWorkUnitsGuideDTO dTO, RscWorkUnitsGuide guide) {
        dTO.setActive(guide.getActive());
        dTO.setCode(guide.getCode());
        dTO.setId(guide.getId());
        dTO.setLevel(guide.getLevel());
        dTO.setName(guide.getName());
        dTO.setType(guide.getType());
        if (guide.getUnitId() != null && guide.getUnitId().getId() != null) {
            Symbol unit = new Symbol();
            unit.setId(guide.getUnitId().getId());
            unit.setName(guide.getUnitId().getName());
            unit.setSerial(guide.getUnitId().getSerial());
            dTO.setUnitName(unit);
        }
        dTO.setQuantity(guide.getQuantity());
        dTO.setValue(guide.getValue());
    }

    public RscWorkUnitsGuide mapFromDTO(RscWorkUnitsGuideDTO dTO, IsagUser isagUser) {
        RscWorkUnitsGuide guide = new RscWorkUnitsGuide();
        guide.setActive(dTO.getActive());
        guide.setCode(dTO.getCode());
        guide.setId(dTO.getId());
        guide.setLevel(dTO.getLevel());
        guide.setName(dTO.getName());
        guide.setUnitId(dTO.getUnitName());
        guide.setQuantity(dTO.getQuantity());
        guide.setValue(dTO.getValue());
        guide.setType(dTO.getType());
        if (dTO.getParent() != null && dTO.getParent().getId() != null) {
            RscWorkUnitsGuide guideParent = new RscWorkUnitsGuide();
            guideParent.setId(dTO.getParent().getId());
            guide.setParent(guideParent);
        }

        if (dTO.getId() == null) {
            guide.setCreatedBy(isagUser);
            guide.setCreationDate(new Date());
        } else {
            guide.setId(dTO.getId());
            guide.setModificationDate(new Date());
            guide.setModifiedBy(isagUser);
        }

        guide.setBranchId(isagUser.getBranchId());
        guide.setCompanyId(isagUser.getCompanyId());
        return guide;
    }

    private void returmListDTO(List<RscWorkUnitsGuide> list, List<RscWorkUnitsGuideDTO> dTOList) {
        for (RscWorkUnitsGuide guide : list) {
            dTOList.add(mapTODTO(guide));
        }
    }
    
    @Override
    public void addRscWorkUnitsGuideForTransfer(RscWorkUnitsGuide rscWorkUnitsGuide) {
        dao.updateEntity(rscWorkUnitsGuide);
    }
}

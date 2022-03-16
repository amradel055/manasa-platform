/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.dto.CostCenterDTO;
import com.toby.entity.CostCenter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class CostCenterServiceImplDTO implements CostCenterServiceDTO {

    @EJB
    private CostCenterService costCenterService;
    @EJB
    private GenericDAO dao;

    @Override
    public List<CostCenterDTO> getAllSubCostCenterByBranchIdActive(Integer id) {
        List<CostCenter> allCostCenterUnits = costCenterService.getBranchCostCentersActive(id);
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

        return returnListDTO(childrens);

    }

    @Override
    public CostCenterDTO findCostCenter(Integer costCenterId) {
        if (costCenterId != null) {
            CostCenter costCenter = new CostCenter();
            costCenter = dao.findEntityById(CostCenter.class, costCenterId);
            return mapTODTO(costCenter, false);
        } else {
            return null;
        }
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

}

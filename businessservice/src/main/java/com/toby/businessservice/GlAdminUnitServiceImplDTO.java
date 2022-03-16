/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.entity.GlAdminUnit;
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
public class GlAdminUnitServiceImplDTO implements GlAdminUnitServiceDTO{
     
    @EJB
    private GenericDAO dao;
     
     
    private List<GlAdminUnit> getAllAdminUnitByBranchIdActive(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT e FROM GlAdminUnit e WHERE  e.branchId.id= :id and e.active = 1   ORDER BY e.code ASC ", params);
    }
    
    @Override
    public List<GlAdminUnitDTO> getAllSubAdminUnitByBranchIdActive(Integer id) {
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
    
     @Override
    public GlAdminUnitDTO findAdminUnit(Integer adminUnitId) {
        if (adminUnitId != null) {
            GlAdminUnit glAdminUnit = new GlAdminUnit();
            glAdminUnit = dao.findEntityById(GlAdminUnit.class, adminUnitId);
            return mapTODTO(glAdminUnit , false);
        } else {
            return null;
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
        
        if (entity.getParent()!= null) {
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

        if (dTO.getParent()!= null && dTO.getParent().getId() != null) {
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

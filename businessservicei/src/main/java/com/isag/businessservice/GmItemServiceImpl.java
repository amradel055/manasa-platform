/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GmItemDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.GeneralSymbol;
import com.isag.entity.GlAccount;
import com.isag.entity.GmItem;
import com.isag.entity.IsagUser;
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
 * @author hq002
 */
@Stateless
public class GmItemServiceImpl implements GmItemService {

    @EJB
    private GenericDAO dao;

    @Override
    public GmItemDTO mapTODTO(GmItem entity, GmItemDTO dTO) {
        if (dTO == null) {
            dTO = new GmItemDTO();
        }
        initMapToDTO(dTO, entity);

        return dTO;
    }

    private void initMapToDTO(GmItemDTO dTO, GmItem entity) {

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setCost(entity.getCost());
        dTO.setSymbolType(entity.getSymbolType() != null && entity.getSymbolType() == 1);
        dTO.setSerial(entity.getSerial());
        dTO.setArrange(entity.getArrange());
        dTO.setName(entity.getName());
        dTO.setId(entity.getId());
        dTO.setPeriod(entity.getPeriod());
        dTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        dTO.setUseability(entity.getUseability() != null && entity.getUseability() == 1);

    }

    public GmItem mapToEntity(GmItemDTO dTO, IsagUser isagUser) {
        GmItem entity = new GmItem();
        entity.setName(dTO.getName());
        entity.setSerial(dTO.getSerial());
        entity.setArrange(dTO.getArrange());
        entity.setPeriod(dTO.getPeriod());
        entity.setCost(dTO.getCost());
        entity.setSymbolType(dTO.getSymbolType() ? 1 : 0);
        entity.setUseability(dTO.getUseability() ? 1 : 0);

        if (dTO.getId() == null) {
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

        entity.setCompanyId(isagUser.getCompanyId());
        entity.setBranchId(isagUser.getBranchId());
        return entity;
    }

    private List<GmItemDTO> returnListDTO(List<GmItem> list) {
        List<GmItemDTO> dTOList = new ArrayList<>();
        for (GmItem entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
        return dTOList;
    }

    @Override
    public List<GmItemDTO> findGmItemByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<GmItemDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<GmItem> list = dao.findListByQuery("SELECT sym FROM GmItem sym WHERE  sym.companyId.id= :companyId", params);
        dTOList = returnListDTO(list);
        return dTOList;
    }

    @Override
    public void deleteGmItem(GmItem gmItem) {
        dao.deleteEntity(gmItem);
    }

    @Override
    public GmItemDTO updateGmItemDTO(GmItemDTO gmItemDTO, IsagUser isagUser) {
        GmItem symbol = mapToEntity(gmItemDTO, isagUser);
        symbol = dao.updateEntity(symbol);
        return mapTODTO(symbol, gmItemDTO);
    }

    @Override
    public GmItemDTO getGmItemDTO(Integer SymbolId) {
        return mapTODTO(dao.findEntityById(GmItem.class, SymbolId), null);
    }

    @Override
    public List<GmItemDTO> autoCompleteGmItemDTO(Integer branchId) {
        List<GmItemDTO> list = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        query.append("SELECT sym FROM GmItem sym WHERE  sym.branchId.id= :branchId AND sym.useability = 1 ");

        List<GmItem> detailses = dao.findListByQuery(query.toString(), params);
        list = returnListDTO(detailses);
        return list;
    }
}
